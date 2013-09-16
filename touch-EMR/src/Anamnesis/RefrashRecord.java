package Anamnesis;


import Anamnesis.TableTriStateCell.*;
import Common.PrintTools;
import ErrorMessage.StoredErrorMessage;
import Multilingual.language;
import Common.TabTools;
import cc.johnwu.date.*;
import cc.johnwu.sql.*;
import java.awt.Color;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.*;


public class RefrashRecord extends Thread{

    private javax.swing.JTable tab;
    private long time;
    private String m_LastTouchTime;
    private Object m_LastRecordTouchTime = null;
    private String m_LastBorrowTime;
    private String conditions;
    private String[] m_Title = new String[]{"Patient No.","Register","Status","Name",
                                            "Birthday","Policlinic",
                                            "Clinic", "Doctor",
                                            "Shift","Lent Time","Check","reg_guid"};
    private Boolean judge;  // true 借閱病歷


    StoredErrorMessage ErrorMessage = new StoredErrorMessage() ;
    protected RefrashRecord(javax.swing.JTable tab,long time,Boolean judge, String conditions){
        ResultSet rs = null;
        Object[][] tableItem = null;
        this.tab = tab;
        this.time = time;
        this.judge = judge;
        this.conditions = conditions;
        
        try{
            rs = DBC.executeQuery(getSQL());
            if(rs.last()){
                tableItem = new Object[rs.getRow()][m_Title.length];

                rs.first();
                int row = 0;
                //set row data
                do {
//                    tableItem[row][0] = row+1;
                    for (int col = 0; col < m_Title.length-2; col++) {
                        tableItem[row][col] = rs.getString(col+1);
                    }
                    tableItem[row][m_Title.length-2] = false;
                    tableItem[row][m_Title.length-1] = rs.getString("reg_guid");
                    row++;
                } while (row<tableItem.length && rs.next());
                this.tab.setModel(new DefaultTableModel(tableItem,m_Title));
            }else{
                tableItem = new Object[0][m_Title.length];
                this.tab.setModel(new DefaultTableModel(tableItem,m_Title));
            }
            
            this.tab.setRowHeight(30);
            if (rs.last()) {  // 取得最後一筆資料
                System.out.println("1");
                m_LastRecordTouchTime = rs.getString("record_touchtime");
                System.out.println("FINAL ");
            } else {
                System.out.println("2");
                m_LastRecordTouchTime = "0";
            }
            
            DBC.closeConnection(rs);
            if (judge) {
                Object[][] array = {{"R",new Color(142,216,244)}};
                TabTools.setTabColor(tab, 1, array);
            } else {
                // 已完成帶送回
                Object[][] array = {{"F",new Color(142,216,244)},{"Skip",new Color(142,216,244)},
                                     {"C",new Color(142,216,244)},{"A",new Color(142,216,244)}};
                TabTools.setTabColor(tab, 2, array);
            }

            TableColumn tc = tab.getColumnModel().getColumn(0);
            tc.setMaxWidth(60);
            TabTools.setHideColumn(tab,m_Title.length-1);
            TableColumn columnChoose = this.tab.getColumnModel().getColumn(m_Title.length-2);
            columnChoose.setPreferredWidth(50);
            columnChoose.setCellRenderer(new TriStateCellRenderer());
            columnChoose.setCellEditor(new TriStateCellEditor());
            
        }catch (SQLException e) {
            ErrorMessage.setData("Anamnesis", "RefrashRecord" ,"RefrashRecord(javax.swing.JTable tab,long time,Boolean judge, String conditions)",
            e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            Logger.getLogger(RefrashRecord.class.getName()).log(Level.SEVERE, null, e);
        }finally{ try{ DBC.closeConnection(rs); }catch(SQLException e){
//            ErrorMessage.setData("Anamnesis", "RefrashRecord" ,"RefrashRecord(javax.swing.JTable tab,long time,Boolean judge, String conditions) - DBC.closeConnection",
//            e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        }

      }
    }

    private String getSQL(){
        String sql = "SELECT patients_info.p_no AS 'Patient No.', registration_info.register, " +
                                "CASE registration_info.finish WHEN 'F' THEN 'F' WHEN 'O' THEN 'Skip' END 'Status'," +
                                "concat(patients_info.firstname ,'  ',patients_info.lastname) AS Name , " +
                                "patients_info.birth AS Birthday, " +
                                "policlinic.name AS Policlinic, "+
                                "poli_room.name AS Clinic, " +
                                "concat(staff_info.firstname,'  ',staff_info.lastname) AS Doctor, " +
                                "CASE shift_table.shift " +
                                    "WHEN '1' THEN 'Morning' " +
                                    "WHEN '2' THEN 'Afternoon' " +
                                    "WHEN '3' THEN 'Night' " +
                                    "ELSE 'All Night' END Shift," +
                                "anamnesis_retrieve.borrow_time AS 'Lent Time', "+
                                "record_touchtime, " +
                                "registration_info.guid AS reg_guid "+
                 "FROM (registration_info, patients_info, shift_table, staff_info, policlinic, poli_room ) " +
                 "LEFT JOIN anamnesis_retrieve ON registration_info.guid = anamnesis_retrieve.reg_guid " +
                 "WHERE registration_info.p_no = patients_info.p_no " +
                 "AND registration_info.shift_guid = shift_table.guid " +
                 "AND shift_table.s_id = staff_info.s_id " +
                 "AND shift_table.room_guid = poli_room.guid " +
                 "AND shift_table.shift = '"+DateMethod.getNowShiftNum()+"' " +
                 "AND poli_room.poli_guid = policlinic.guid " ;

        if(judge){
            sql = sql + "AND shift_table.shift_date = '"+DateMethod.getTodayYMD() + "' " +
                        "AND borrow_time IS NULL " +
                        "ORDER BY shift_table.shift, touchtime ";
            
        }else{
            sql = sql + "AND shift_table.shift_date <= '"+DateMethod.getTodayYMD() + "' " +
                        "AND borrow_time IS NOT NULL " +
                        "AND return_time IS NULL " +
                        conditions +
                        "ORDER BY registration_info.finish DESC, shift_table.shift, touchtime ";
        }

        return sql;
    }

    @Override
    public void run(){
        ResultSet rs = null;
        
        try{
            while(true){
            try{
                String check_sql = "SELECT MAX(record_touchtime) AS record_touchtime ,MAX(borrow_time) " +
                                   "FROM (registration_info,shift_table) " +
                                   "LEFT JOIN anamnesis_retrieve ON registration_info.guid = anamnesis_retrieve.reg_guid " +
                                   "WHERE registration_info.shift_guid = shift_table.guid " +
                                   "AND finish IS NULL " +
                                   "AND shift_table.shift = '"+DateMethod.getNowShiftNum()+"' " ;
                if(judge){
                    check_sql = check_sql + "AND shift_table.shift_date = '"+DateMethod.getTodayYMD()+"' ";
                }else{
                    check_sql = check_sql + "AND shift_table.shift_date <= '"+DateMethod.getTodayYMD()+"' ";
                }
                rs = DBC.executeQuery(check_sql);

                    if(rs.next()
                    && (rs.getString(1) == null || rs.getString("record_touchtime").equals(m_LastRecordTouchTime))
                    && (rs.getString(2) == null || rs.getString(2).equals(m_LastBorrowTime))){
                        RefrashRecord.sleep(time);
                        continue;
                    }
                //m_LastTouchTime = rs.getString(1);
                m_LastBorrowTime = rs.getString(2);
                DBC.closeConnection(rs);
                
                rs = DBC.executeQuery(getSQL());
                System.out.println(getSQL());
                if(rs.last()){

                    int row = 0;
                    ((DefaultTableModel)tab.getModel()).setRowCount(rs.getRow());
                    rs.beforeFirst();

                    while(rs.next()){
                        for(int col=0; col<m_Title.length-2; col++){
                            if(col==0){
                                if(Frm_Anamnesis.m_Select.contains(rs.getString("reg_guid"))){
                                    tab.setValueAt(true, row, m_Title.length-2);
                        
                                    break;
                                }else{
                                    tab.setValueAt(false, row, m_Title.length-2);
                                }
                            }
                            tab.setValueAt(rs.getString(col+1), row, col);
                        }
                        tab.setValueAt(rs.getString("reg_guid"), row, m_Title.length-1);



                        //****************************print
                      //  System.out.println("m_LastRecordTouchTime " + ((String) m_LastRecordTouchTime).substring(6, 20) + " < "+ (rs.getString("record_touchtime").substring(6, 20))  +"  ");
               
                        if (judge && rs.getString("record_touchtime") != null && (m_LastRecordTouchTime.equals("0") || 
                                Long.parseLong(((String) m_LastRecordTouchTime).substring(6, 20)) < Long.parseLong(rs.getString("record_touchtime").substring(6, 20)))) {
                             PrintTools pt = new PrintTools();
                             pt.DoPrint(3,rs.getString("reg_guid"), true);
                        }
                        //****************************
                        row++;
                    }

              
                    if (judge) {
                        check_sql = "SELECT MAX(record_touchtime) AS record_touchtime " +
                                           "FROM (registration_info,shift_table) " +
                                           "LEFT JOIN anamnesis_retrieve ON registration_info.guid = anamnesis_retrieve.reg_guid " +
                                           "WHERE registration_info.shift_guid = shift_table.guid " +
                                           "AND finish IS NULL " +
                                           "AND shift_table.shift = '"+DateMethod.getNowShiftNum()+"' AND shift_table.shift_date = '"+DateMethod.getTodayYMD()+"' ";
                        
                        rs = DBC.executeQuery(check_sql);
                        if(rs.next()) m_LastRecordTouchTime = rs.getString("record_touchtime");
                    }

                    
                    
                    TableColumn columnChoose = this.tab.getColumnModel().getColumn(m_Title.length-2);
                    columnChoose.setPreferredWidth(50);
                    columnChoose.setCellRenderer(new TriStateCellRenderer());
                    columnChoose.setCellEditor(new TriStateCellEditor());
                }else{
                    Object[][] tableItem = null;
                    tableItem = new Object[0][m_Title.length];
                    this.tab.setModel(new DefaultTableModel(tableItem,m_Title));

                }
               tab.getColumnModel().getColumn(0).setMaxWidth(60);
                TabTools.setHideColumn(tab,m_Title.length-1);
                DBC.closeConnection(rs);
            }catch (SQLException e) {
                ErrorMessage.setData("Anamnesis", "RefrashRecord" ,"run()",
                    e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
                Logger.getLogger(RefrashRecord.class.getName()).log(Level.SEVERE, null, e);
            }finally{ try{ DBC.closeConnection(rs); }catch(SQLException e){
                ErrorMessage.setData("Anamnesis", "RefrashRecord" ,"run() - DBC.closeConnection",
                    e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            }
          }
        }
        }catch(InterruptedException e) {
            ErrorMessage.setData("Anamnesis", "RefrashRecord" ,"run() - InterruptedException",
                    e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            this.interrupt();

        }
    }
}
