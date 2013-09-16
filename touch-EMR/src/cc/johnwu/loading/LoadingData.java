package cc.johnwu.loading;


import cc.johnwu.sql.DBC;
import java.sql.*;


public class LoadingData {
    private int m_ServerDataCount;
    private int m_LocalDataCount;
    private ResultSet m_ServerRS;
    private ResultSet m_LocalRS;
    private ResultSetMetaData m_rsmd;
    private static int LoadingThreadCount;

    public LoadingData(){
        LoadingThreadCount++;
    }

    protected boolean need2Update(String tableName){
        ResultSet localRS = null;
        ResultSet serverRS = null;
        try {
            String sql = "SELECT COUNT(1) FROM " + tableName;
            localRS = DBC.localExecuteQuery(sql);
            serverRS = DBC.executeQuery(sql);
            localRS.next();
            serverRS.next();
            if(localRS.getInt(1) == serverRS.getInt(1)){
                return false;
            }
        } catch (SQLException ex) {
        } finally {
            try{DBC.closeConnection(localRS);}
            catch (SQLException ex) {}
            try{DBC.closeConnection(serverRS);}
            catch (SQLException ex) {}
        }
        return true;
    }

    protected void rebuildingData(String tableName){
        ResultSet serverRS = null;
        try {
            serverRS = DBC.executeQuery("SELECT * FROM " + tableName);
            ResultSetMetaData rsmd = serverRS.getMetaData();
            Object[][] columnData = new Object[rsmd.getColumnCount()][3];
            String sql = "CREATE TABLE "+tableName+"(";
            for(int i=0; i<columnData.length; i++){
                if(rsmd.getColumnTypeName(i+1).equalsIgnoreCase("CHAR")){
                    sql += rsmd.getColumnName(i+1)+" "+rsmd.getColumnTypeName(i+1);
                    sql += "("+rsmd.getColumnDisplaySize(i+1)+")";
                }else if(rsmd.getColumnTypeName(i+1).equalsIgnoreCase("LONGBLOB")){
                    sql += rsmd.getColumnName(i+1)+" VARBINARY";
                }else{
                    sql += rsmd.getColumnName(i+1)+" "+rsmd.getColumnTypeName(i+1);
                }
                if(i==0){
                    sql += " PRIMARY KEY";
                }
                if(i<columnData.length-1){
                    sql += ", ";
                }else{
                    sql += ")";
                }
            }
            DBC.localExecute(sql);
            DBC.localExecute("SHUTDOWN");
        } catch (SQLException ex) {
        } finally {
            try{DBC.closeConnection(serverRS);}
            catch (SQLException ex) {}
        }
        try{
            DBC.localExecuteQuery("DELECT FROM " + tableName);
            DBC.localExecute("SHUTDOWN");
        } catch (SQLException sex) { }
        loadData(tableName);
    }

    private void loadData(String tableName){
        String sql = "SELECT * FROM " + tableName;
        try {
            m_ServerRS = DBC.executeQuery(sql);
            m_ServerRS.last();
            m_ServerDataCount = m_ServerRS.getRow();
            m_ServerRS.beforeFirst();
            m_rsmd = m_ServerRS.getMetaData();
            m_LocalRS = DBC.localExecuteQuery(sql);
            m_LocalRS.last();
            m_LocalDataCount = m_LocalRS.getRow();
            m_LocalRS.beforeFirst();
        } catch (SQLException ex) {
        }
    }

    protected synchronized boolean download2LocalDB(String tableName,int comparisonRow){
        PreparedStatement localInsertStmt = null;
        try {
            String sql = "";

            sql = "INSERT INTO "+tableName+" VALUES(";
            for(int i=1; i<=m_rsmd.getColumnCount(); i++){
                if(i!=m_rsmd.getColumnCount())
                    sql += "?, ";
                else
                    sql += "?)";
            }
            localInsertStmt = DBC.localPrepareStatement(sql);

            if(m_ServerRS.absolute(comparisonRow)){
                // Insert to Local Database
                for(int i=1; i<=m_rsmd.getColumnCount(); i++){
                    if(m_rsmd.getColumnTypeName(i).equalsIgnoreCase("LONGBLOB")){
                        localInsertStmt.setBytes(i, m_ServerRS.getBytes(i));
                    }else if(m_rsmd.getColumnTypeName(i).equalsIgnoreCase("BIT")){
                        localInsertStmt.setBoolean(i, m_ServerRS.getBoolean(i));
                    }else if(m_rsmd.getColumnTypeName(i).equalsIgnoreCase("INT")){
                        localInsertStmt.setInt(i, m_ServerRS.getInt(i));
                    }else{
                        if(m_ServerRS.getString(i)!=null)
                            localInsertStmt.setString(i, m_ServerRS.getString(i).replace("'","''"));
                        else
                            localInsertStmt.setString(i, null);
                    }
                }
                localInsertStmt.executeUpdate();
            }
            return true;
        } catch (SQLException ex) {            
        } finally {
            try{DBC.closeConnection(localInsertStmt);}
            catch (SQLException ex) {}
        }
        return false;
    }

    protected void closeLocalDB(){
        LoadingThreadCount--;
        try{
            if(LoadingThreadCount==0) DBC.localExecute("SHUTDOWN");
        } catch (SQLException ex) {
            System.out.println("SQLException");
        }
    }
    
    protected  int getServerDataCount(){
        return m_ServerDataCount;
    }

    protected  int getLocalDataCount(){
        return m_LocalDataCount;
    }
}
