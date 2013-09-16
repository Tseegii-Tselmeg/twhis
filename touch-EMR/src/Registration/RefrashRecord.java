package Registration;

import Multilingual.language;
import Common.TabTools;
import cc.johnwu.date.*;
import cc.johnwu.sql.*;
import java.sql.*;
import javax.swing.table.*;

public class RefrashRecord extends Thread{
    private language paragraph = new language();
    private String[] line = paragraph.setlanguage("REGISTRATION").split("\n") ;
    private javax.swing.JTable tab;
    private long time;
    private String m_LastTouchTime;
    private String sql = "SELECT patients_info.p_no AS 'Patient No.' , " +
                                "concat(patients_info.firstname ,'  ',patients_info.lastname) AS 'Name' , " +
                                "policlinic.name AS '"+paragraph.getLanguage(line, "COL_POLICLINIC")+"', "+
                                "concat(staff_info.firstname,'  ',staff_info.lastname) AS '"+paragraph.getLanguage(line, "COL_DOCTOR")+"', " +
                                "touchtime " +
                 "FROM registration_info, patients_info, shift_table, staff_info, policlinic, poli_room " +
                 "WHERE registration_info.p_no = patients_info.p_no " +
                 "AND registration_info.shift_guid = shift_table.guid " +
                 "AND shift_table.s_id = staff_info.s_id " +
                 "AND shift_table.room_guid = poli_room.guid " +
                 "AND poli_room.poli_guid = policlinic.guid " +
                 "AND finish IS NULL ";
    

    protected RefrashRecord(javax.swing.JTable tab,long time){
        ResultSet rs = null;
        this.tab = tab;
        this.time = time;
        try{
            rs = DBC.executeQuery(sql+"AND shift_table.shift_date = '"+DateMethod.getTodayYMD()+"' " +
                                 "AND shift_table.shift = '"+DateMethod.getNowShiftNum()+"' ORDER BY reg_time ");
            ((DefaultTableModel)this.tab.getModel()).setRowCount(0);
            this.tab.setModel(HISModel.getModel(rs, 5, true));
            TableColumn tc = tab.getColumnModel().getColumn(0);
            tc.setMaxWidth(40);
            tc.setMinWidth(40);
            TableColumn colPno = tab.getColumnModel().getColumn(1);
            colPno.setMaxWidth(80);
            colPno.setMinWidth(80);
            TabTools.setHideColumn(tab,5);
            rs.last();
            m_LastTouchTime = rs.getString("touchtime");
            DBC.closeConnection(rs);
        }catch (SQLException ex) {System.out.println("RefrashRecord : "+ex);
        }finally{ try{ DBC.closeConnection(rs); }catch(SQLException ex){} }
    }

    @Override
    public void run(){
        ResultSet rs = null;
        try{ while(true){
            try{
                String check_sql = "SELECT MAX(touchtime) " +
                                   "FROM registration_info,shift_table " +
                                   "WHERE registration_info.shift_guid = shift_table.guid " +
                                   "AND shift_table.shift_date = '"+DateMethod.getTodayYMD()+"' " +
                                   "AND shift_table.shift = '"+DateMethod.getNowShiftNum()+"' ";
                rs = DBC.executeQuery(check_sql);
                if(rs.next()
                && (rs.getString(1) == null || rs.getString(1).equals(m_LastTouchTime))){
                    RefrashRecord.sleep(time);
                    continue;
                }
                m_LastTouchTime = rs.getString(1);
                DBC.closeConnection(rs);

                rs = DBC.executeQuery(sql+"AND shift_table.shift_date = '"+DateMethod.getTodayYMD()+"' " +
                                     "AND shift_table.shift = '"+DateMethod.getNowShiftNum()+"' ORDER BY reg_time ");
                if(rs.last()){
                    int row = 0;
                    ((DefaultTableModel)tab.getModel()).setRowCount(rs.getRow());
                    rs.beforeFirst();
                    while(rs.next()){
                        tab.setValueAt(row+1, row, 0);
                        for(int col=0; col<5; col++)
                            tab.setValueAt(rs.getString(col+1), row, col+1);
                        row++;
                    }
                    TableColumn tc = tab.getColumnModel().getColumn(0);
                    tc.setMaxWidth(40);
                    TabTools.setHideColumn(tab,5);
                }
                DBC.closeConnection(rs);
            }catch (SQLException ex) {System.out.println("Record SQLException : "+ex);
            }finally{ try{ DBC.closeConnection(rs); }catch(SQLException ex){} }
        }}catch(InterruptedException e) {}
    }
}