package cc.johnwu.sql;


import java.awt.Frame;
import java.sql.*;
import javax.swing.JOptionPane;

public class DBC {

    private final static String LOCALURL;
    private final static String LOCALNAME;
    private final static String LOCALPASSWD;

    private static String s_ServerURL;
    private static String s_ServerName;
    private static String s_ServerPasswd;

    private static Connection s_LocalConn;
    private static Statement s_LocalStmt;
    private static ResultSet s_LocalRS;

    private static javax.swing.JFrame s_EnteredFrm;


    static {    
//            File file = new File("");
//            LOCALURL = "jdbc:hsqldb:file:"+file.getCanonicalPath()+"/db/localDB;shutdown=true";
            LOCALURL = "jdbc:hsqldb:file:./db/localDB;shutdown=true";
            LOCALNAME = "SA";
            LOCALPASSWD = "";
    }

    public DBC(){
        initLocalDB();
    }

    public DBC(javax.swing.JFrame frm){
        s_EnteredFrm = frm;
        initLocalDB();
    }
    
    private void initLocalDB(){
        if(!isClosed()) return;
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            Class.forName("com.mysql.jdbc.Driver");            
            if(!getConnection()){
                new Frm_SettingMySQL().setVisible(true);
            }
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(new Frame(), ex);
        }
    }

    protected static boolean getConnection(){
        try {
            s_LocalConn = DriverManager.getConnection(LOCALURL,LOCALNAME,LOCALPASSWD);
            s_LocalStmt = s_LocalConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            s_LocalRS = s_LocalStmt.executeQuery("SELECT * FROM conn_info");
            s_LocalRS.next();
            s_ServerURL = "jdbc:mysql://"+s_LocalRS.getString("host").trim()+
                          ":"+s_LocalRS.getString("port").trim()+
                          "/"+s_LocalRS.getString("database").trim();
            s_ServerName = HISPassword.deCode(s_LocalRS.getString("user")).trim();
            s_ServerPasswd = HISPassword.deCode(s_LocalRS.getString("passwd")).trim();
            Connection conn = DriverManager.getConnection(s_ServerURL,s_ServerName,s_ServerPasswd);
            conn.close();
            if(s_EnteredFrm!=null){
                s_EnteredFrm.setVisible(true);
                s_EnteredFrm = null;
            }
            return true;
        } catch (SQLException ex) {
            try {
                s_LocalStmt.execute("CREATE TABLE conn_info ("
                                    +"host char(40) PRIMARY KEY, "
                                    +"port char(5), "
                                    +"database char(40), "
                                    +"user char(32), "
                                    +"passwd char(32)"
                                    +")");
            } catch (SQLException ex1) {}
            Object[] options = {"Yes","No"};
            int response = JOptionPane.showOptionDialog(
                                new Frame(),
                                "Can not connect database!\nDo you want to reset?",
                                "Error Message",
                                JOptionPane.YES_OPTION,
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                options,
                                options[0]
                            );
            if(response==0)
                return false;
            else if(response==1)
                System.exit(0);
        }
        return false;
    }
    
    public static boolean isClosed(){
        Connection conn = null;
        try{
            conn = DriverManager.getConnection(s_ServerURL,s_ServerName,s_ServerPasswd);
            conn.close();
            return false;
        } catch (SQLException ex) {
        } finally {
            try{ if(conn!=null)conn.close(); } catch (SQLException ex){}
        }
        return true;
    }

    public static void closeConnection(ResultSet rs) throws SQLException {
        if(rs==null) return ;
        Statement stmt = rs.getStatement();
        rs.close();
        closeConnection(stmt);
    }

    public static void closeConnection(Statement stmt) throws SQLException {
        if(stmt==null) return ;
        Connection conn = stmt.getConnection();
        if(stmt!=null)stmt.close();
        if(conn!=null)conn.close();
    }

    public static void closeConnection(PreparedStatement pstmt) throws SQLException {
        if(pstmt==null) return ;
        Connection conn = pstmt.getConnection();
        if(pstmt!=null)pstmt.close();
        if(conn!=null)conn.close();
    }

    /** 執行給定的 SQL 語句，該語句返回單個 ResultSet 物件。*/
    public static ResultSet executeQuery(String sql) throws SQLException {
        cc.johnwu.login.OnlineState.OnlineState();
        Connection conn = DriverManager.getConnection(s_ServerURL,s_ServerName,s_ServerPasswd);
        Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        return stmt.executeQuery(sql);
    }

    public static ResultSet executeQueryLogin(String sql) throws SQLException {
        Connection conn = DriverManager.getConnection(s_ServerURL,s_ServerName,s_ServerPasswd);
        Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        return stmt.executeQuery(sql);
    }

    /** 執行給定 SQL 語句，該語句可能為 INSERT、UPDATE 或 DELETE 語句，或者不返回任何內容的 SQL 語句（如 SQL DDL 語句）。*/
    public static int executeUpdate(String sql) throws SQLException {
        cc.johnwu.login.OnlineState.OnlineState();
        int count = 0;
        Connection conn = DriverManager.getConnection(s_ServerURL,s_ServerName,s_ServerPasswd);
        Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        count = stmt.executeUpdate(sql);
        closeConnection(stmt);
        return count;
    }

    public static int executeUpdateLogin(String sql) throws SQLException {
        int count = 0;
        Connection conn = DriverManager.getConnection(s_ServerURL,s_ServerName,s_ServerPasswd);
        Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        count = stmt.executeUpdate(sql);
        closeConnection(stmt);
        return count;
    }

    public static PreparedStatement prepareStatement(String sql) throws SQLException {
        Connection conn = DriverManager.getConnection(s_ServerURL,s_ServerName,s_ServerPasswd);
        return conn.prepareStatement(sql);
    }

    /** 執行給定的 SQL 語句，該語句返回單個 ResultSet 物件。*/
    public static ResultSet localExecuteQuery(String sql) throws SQLException {
        Connection conn = DriverManager.getConnection(LOCALURL,LOCALNAME,LOCALPASSWD);
        Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        return stmt.executeQuery(sql);
    }

    /** 執行給定 SQL 語句，該語句可能為 INSERT、UPDATE 或 DELETE 語句，或者不返回任何內容的 SQL 語句（如 SQL DDL 語句）。*/
    public static int localExecuteUpdate(String sql) throws SQLException {
        int count = 0;
        Connection conn = DriverManager.getConnection(LOCALURL,LOCALNAME,LOCALPASSWD);
        Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        count = stmt.executeUpdate(sql);
        closeConnection(stmt);
        return count;
    }

    public static boolean localExecute(String sql) throws SQLException {
        boolean flag = false;
        Connection conn = DriverManager.getConnection(LOCALURL,LOCALNAME,LOCALPASSWD);
        Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        flag = stmt.execute(sql);
        closeConnection(stmt);
        return flag;
    }

    public static PreparedStatement localPrepareStatement(String sql) throws SQLException {
        Connection conn = DriverManager.getConnection(LOCALURL,LOCALNAME,LOCALPASSWD);
        return conn.prepareStatement(sql);
    }

}