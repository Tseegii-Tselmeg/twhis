/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Cashier;

import Common.*;
import cc.johnwu.date.DateMethod;
import cc.johnwu.sql.DBC;
import cc.johnwu.sql.HISModel;
import java.awt.Color;
import java.sql.*;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author steven
 */
public class RefrashCashier extends Thread{
    private String m_SysName;                       // 進入系統名稱
    private javax.swing.JTable m_Tab;
    private long m_Time;
    private String m_LastTouchTime;
    private String[] m_Guid;
    private ResultSet rs = null;
    private String sql;

    protected RefrashCashier(javax.swing.JTable tab,long time, String SysName, JLabel lab_await){
        m_SysName = SysName;

        // 掛號收費
        if (SysName.equals("reg")) {
            sql = "SELECT registration_info.guid ,registration_info.payment, " +
                    "patients_info.p_no AS 'Pateint No.',"+
                    "CONCAT(patients_info.firstname, ' ' ,patients_info.lastname) AS 'Name', " +
                    "policlinic.name AS 'Dept.', " +
                    "poli_room.name AS 'Clinic' "+
                    "FROM registration_info, shift_table,policlinic , poli_room ,patients_info "+
                    "WHERE payment IS NULL "+
                    "AND shift_table.guid = registration_info.shift_guid "+
                    "AND policlinic.guid = poli_room.poli_guid "+
                    "AND poli_room.guid = shift_table.room_guid  "+
                    "AND shift_table.shift_date = '"+DateMethod.getTodayYMD()+"' "+
                    "AND shift_table.shift = '"+DateMethod.getNowShiftNum()+"' "+
                    "AND registration_info.p_no = patients_info.p_no ORDER BY registration_info.reg_time DESC";

        } else if (SysName.equals("lab")) {
            sql = "SELECT NEWTABLE.regguid AS guid,NEWTABLE.lab_payment AS lab_payment, " +
                    "NEWTABLE.p_no AS 'Patient No.', " +
                    "NEWTABLE.Name AS 'Name', " +
                    "NEWTABLE.gender AS 'Gender', " +
                    "NEWTABLE.reg_time  AS 'Registration time' " +
                    "FROM (" +
                    "SELECT distinct A.visits_no, A.register, A.reg_time ,A.lab_payment,  A.p_no, " +
                    "concat(patients_info.firstname,'  ',patients_info.lastname) AS 'Name', "+
                    "patients_info.birth , patients_info.gender , " +
                    "concat(patients_info.bloodtype,patients_info.rh_type) AS 'Blood', " +
                    "patients_info.ps, A.guid AS regguid , outpatient_services.guid AS osguid   "+
                    "FROM registration_info AS A,  patients_info, shift_table,staff_info, prescription " +
                    "LEFT JOIN outpatient_services ON prescription.os_guid = outpatient_services.guid ,prescription_code  "+
                    "WHERE A.shift_guid = shift_table.guid AND shift_table.s_id = staff_info.s_id " +
                    "AND shift_table.shift_date = '"+DateMethod.getTodayYMD()+"' "+
                    "AND shift_table.shift = '"+DateMethod.getNowShiftNum()+"' "+
                    "AND A.p_no = patients_info.p_no AND prescription.code = prescription_code.code  "+
                    "AND (outpatient_services.reg_guid = A.guid OR prescription.case_guid =A.guid) " +
                    "AND (SELECT COUNT(code) FROM prescription  LEFT JOIN  outpatient_services ON prescription.os_guid = outpatient_services.guid,  registration_info  "+
                    "WHERE (prescription.finish <> 'F' OR prescription.finish IS  NULL ) " +
                    "AND (outpatient_services.reg_guid = registration_info.guid OR prescription.case_guid = registration_info.guid)  " +
                    "AND prescription.code = prescription_code.code   "+
                    "AND prescription_code.type <> '"+Constant.X_RAY_CODE+"' " +
                    "AND registration_info.guid = A.guid)  > 0 ) AS NEWTABLE "+
                    "LEFT JOIN (SELECT distinct case_guid,os_guid,'1' AS status FROM prescription " +
                    "WHERE prescription.specimen_status = '1') AS pre_status ON (pre_status.os_guid = NEWTABLE.osguid  " +
                    "OR pre_status.case_guid =  NEWTABLE.regguid)  " +
                    "ORDER BY pre_status.status ,NEWTABLE.reg_time DESC, NEWTABLE.visits_no ";
        }  else if (SysName.equals("xray")) {
            sql = "SELECT distinct A.guid ,A.radiology_paymen, " +
                    "A.p_no AS 'Patient No.', "+
                    "concat(patients_info.firstname,'  ',patients_info.lastname) AS 'Name', "+
                    "patients_info.gender AS 'Gender', "+
                    "A.reg_time AS 'Registration Time' " +
                        "FROM registration_info AS A, patients_info, shift_table,staff_info, prescription, outpatient_services, prescription_code "+
                        "WHERE A.shift_guid = shift_table.guid "+
                            "AND shift_table.s_id = staff_info.s_id "+
                            "AND shift_table.shift_date = '"+DateMethod.getTodayYMD()+"' "+
                            "AND shift_table.shift = '"+DateMethod.getNowShiftNum()+"' "+
                            "AND A.p_no = patients_info.p_no "+
                            "AND prescription.code = prescription_code.code "+
                            "AND prescription.os_guid = outpatient_services.guid "+
                            "AND outpatient_services.reg_guid = A.guid "+
                            "AND (SELECT COUNT(code) " +
                            "FROM prescription,outpatient_services, registration_info " +
                            "WHERE (prescription.finish <> 'F' OR prescription.finish IS  NULL ) " +
                            "AND prescription.code = prescription_code.code "+
                            "AND prescription_code.type = '"+Constant.X_RAY_CODE+"' "+
                            "AND outpatient_services.reg_guid = registration_info.guid " +
                            "AND prescription.os_guid = outpatient_services.guid " +
                            "AND registration_info.guid = A.guid)  > 0 "+
                        "ORDER BY A.reg_time DESC, A.visits_no ";
        } else if (SysName.equals("pha")) {
            sql ="SELECT registration_info.guid ,registration_info.pharmacy_payment, " +
                 "registration_info.p_no AS 'Patient No.', " +
                 "concat(patients_info.firstname,'  ',patients_info.lastname) AS 'Name', "+
                 "patients_info.gender AS 'Gender' "+
             "FROM registration_info, patients_info, shift_table,staff_info ,poli_room, policlinic, outpatient_services ,medicine_stock "+
             "WHERE registration_info.shift_guid = shift_table.guid "+
                "AND shift_table.room_guid = poli_room.guid "+
                "AND poli_room.poli_guid = policlinic.guid "+
                "AND shift_table.s_id = staff_info.s_id "+
                "AND registration_info.p_no = patients_info.p_no "+
                "AND shift_table.shift_date = '"+DateMethod.getTodayYMD()+"' "+
                "AND shift_table.shift = '"+DateMethod.getNowShiftNum()+"' "+
                "AND registration_info.finish = 'F' "+
                "AND registration_info.guid = outpatient_services.reg_guid "+
                "AND medicine_stock.os_guid = outpatient_services.guid "+
                "ORDER BY registration_info.touchtime DESC ";
          
        }
        System.out.println(sql);
        this.m_Tab = tab;
        this.m_Time = time;
        try{
            rs = DBC.executeQuery(sql);
            ((DefaultTableModel)this.m_Tab.getModel()).setRowCount(0);
            this.m_Tab.setModel(HISModel.getModel(rs));
            m_Tab.setRowHeight(30);
            Common.TabTools.setHideColumn(m_Tab, 0);
            Common.TabTools.setHideColumn(m_Tab, 1);
            rs.last();

//
//            if(SysName.equals("dia")) {
//                Object[][] array = {{"O",Constant.FINISH_COLOR}, {"F",Constant.FINISH_COLOR}};
//                TabTools.setTabColor(m_Tab, 3, array);
//                TabTools.setHideColumn(this.m_Tab,11);
//            } else if (SysName.equals("case")) {
//                Object[][] array = {{"F",Constant.FINISH_COLOR}};
//                TabTools.setTabColor(m_Tab, 3, array);
//                TabTools.setHideColumn(m_Tab,0);
//                TabTools.setHideColumn(m_Tab,11);
//            } else if (SysName.equals("lab")) {
//
//                Object[][] array = {{"1",Constant.WARNING_COLOR}};
//                TabTools.setTabColor(m_Tab, 12, array);
//                TabTools.setHideColumn(this.m_Tab,0);
//                TabTools.setHideColumn(this.m_Tab,2);
//                TabTools.setHideColumn(this.m_Tab,3);
//                TabTools.setHideColumn(this.m_Tab,11);
//                TabTools.setHideColumn(this.m_Tab,12);
//
//            } else if (SysName.equals("xray")) {
//                TabTools.setHideColumn(this.m_Tab,0);
//                TabTools.setHideColumn(this.m_Tab,2);
//                TabTools.setHideColumn(this.m_Tab,3);
//                TabTools.setHideColumn(this.m_Tab,11);
//            }
            System.out.println(sql);
            if(this.m_Tab.getRowCount()>0) lab_await.setText(String.valueOf(m_Tab.getRowCount()));
            DBC.closeConnection(rs);
        }catch (SQLException ex) {System.out.println(ex);
        }finally{ try{ DBC.closeConnection(rs); }catch(SQLException ex){} }
    }



    @Override
    public void run(){
        try{while(true){
            try {
                String check_sql ="";
                if (m_SysName.equals("reg")) {
                     check_sql = "SELECT MAX(touchtime) " +
                                   "FROM registration_info,shift_table " +
                                   "WHERE registration_info.shift_guid = shift_table.guid " +
                                  // "AND shift_table.s_id = '"+UserInfo.getUserID()+"' " +
                                   "AND shift_table.shift_date = '"+DateMethod.getTodayYMD()+"' " +
                                   "AND shift_table.shift = '"+DateMethod.getNowShiftNum()+"' ";
                }  else if (m_SysName.equals("lab")) {  // 有問題
                    check_sql = "SELECT MAX(touchtime) " +
                                   "FROM registration_info,shift_table " +
                                   "WHERE registration_info.shift_guid = shift_table.guid " +
                                  // "AND shift_table.s_id = '"+UserInfo.getUserID()+"' " +
                                   "AND shift_table.shift_date = '"+DateMethod.getTodayYMD()+"' " +
                                   "AND shift_table.shift = '"+DateMethod.getNowShiftNum()+"' ";
                } else if (m_SysName.equals("xray")) {  // 有問題
                    check_sql = "SELECT MAX(touchtime) " +
                                   "FROM registration_info,shift_table " +
                                   "WHERE registration_info.shift_guid = shift_table.guid " +
                                   //"AND shift_table.s_id = '"+UserInfo.getUserID()+"' " +
                                   "AND shift_table.shift_date = '"+DateMethod.getTodayYMD()+"' " +
                                   "AND shift_table.shift = '"+DateMethod.getNowShiftNum()+"' ";
                } else if (m_SysName.equals("pha")) {
                    check_sql = "SELECT MAX(touchtime) " +
                                   "FROM registration_info,shift_table " +
                                   "WHERE registration_info.shift_guid = shift_table.guid " +
                                   "AND shift_table.shift_date = '"+DateMethod.getTodayYMD()+"' " +
                                   "AND shift_table.shift = '"+DateMethod.getNowShiftNum()+"' ";
                }
               
                rs = DBC.executeQuery(check_sql);
                if(rs.next()
                && (rs.getString(1) == null || rs.getString(1).equals(m_LastTouchTime))){
                    RefrashCashier.sleep(m_Time);
                    continue;
                }                
                m_LastTouchTime = rs.getString(1);
                DBC.closeConnection(rs);

                rs = DBC.executeQuery(sql);
                if(rs.last()){
                    int row = 0;
                    this.m_Guid = new String[rs.getRow()];
                    ((DefaultTableModel)m_Tab.getModel()).setRowCount(rs.getRow());
                    rs.beforeFirst();
                    while(rs.next()){
                        for(int col=0; col<m_Tab.getColumnCount(); col++)
                            m_Tab.setValueAt(rs.getString(col+1), row, col);
                        row++;
                    }
                }
                DBC.closeConnection(rs);
            }catch (SQLException ex) {
                System.out.println("Cashier:"+ex);
            }finally{ 
                try{ DBC.closeConnection(rs);
                }catch(SQLException ex){}
            }
        }}catch(InterruptedException e) {}
    }

 

    // 取得選定日期資料
    public void getSelectDate(String date) {
      if (m_SysName.equals("lab")) {
          sql = "SELECT NEWTABLE.visits_no AS 'NO.', " +
                    "NEWTABLE.register AS 'Register' ,  " +
                    "''," +
                    " NULL, " +
                    "NEWTABLE.reg_time  AS 'Reg time', " +
                    "NEWTABLE.p_no AS 'Patient No.', " +
                    "NEWTABLE.Name AS 'Name', " +
                    "NEWTABLE.birth AS 'Birthday', " +
                    "NEWTABLE.gender AS 'Gender', " +
                    "NEWTABLE.Blood, " +
                    "NEWTABLE.ps AS 'P.S.',  " +
                    "NEWTABLE.regguid AS guid, " +
                    "pre_status.status AS status "+
                    "FROM (" +
                    "SELECT distinct A.visits_no, A.register, A.reg_time , A.p_no, " +
                    "concat(patients_info.firstname,'  ',patients_info.lastname) AS 'Name', "+
                    "patients_info.birth , patients_info.gender , " +
                    "concat(patients_info.bloodtype,patients_info.rh_type) AS 'Blood', " +
                    "patients_info.ps, A.guid AS regguid , outpatient_services.guid AS osguid   "+
                    "FROM registration_info AS A,  patients_info, shift_table,staff_info, prescription " +
                    "LEFT JOIN outpatient_services ON prescription.os_guid = outpatient_services.guid ,prescription_code  "+
                    "WHERE A.shift_guid = shift_table.guid AND shift_table.s_id = staff_info.s_id " +
                    "AND A.p_no = patients_info.p_no AND prescription.code = prescription_code.code  "+
                    "AND (outpatient_services.reg_guid = A.guid OR prescription.case_guid =A.guid) " +
                    "AND (SELECT COUNT(code) FROM prescription  LEFT JOIN  outpatient_services ON prescription.os_guid = outpatient_services.guid,  registration_info  "+
                    "WHERE (prescription.finish <> 'F' OR prescription.finish IS  NULL ) " +
                    "AND (outpatient_services.reg_guid = registration_info.guid OR prescription.case_guid = registration_info.guid)  " +
                    "AND prescription.code = prescription_code.code   "+
                    "AND prescription_code.type <> '"+Constant.X_RAY_CODE+"' " +
                    "AND registration_info.guid = A.guid)  > 0 AND A.reg_time LIKE '"+date+"%' ) AS NEWTABLE "+
                    "LEFT JOIN (SELECT distinct case_guid,os_guid,'1' AS status FROM prescription " +
                    "WHERE prescription.specimen_status = '1') AS pre_status ON (pre_status.os_guid = NEWTABLE.osguid  " +
                    "OR pre_status.case_guid =  NEWTABLE.regguid)  " +
                    "ORDER BY NEWTABLE.reg_time DESC, NEWTABLE.visits_no ";

        }  else if (m_SysName.equals("xray")) {
//            sql = "SELECT distinct A.visits_no AS '"+paragraph.getLanguage(line, "COL_NO")+"', "+
//                            "A.register AS '"+paragraph.getLanguage(line, "COL_REGISTER")+"', " +
//                            "'', "+
//                            "NULL, "+
//                            "A.reg_time AS '"+paragraph.getLanguage(line, "COL_REGTIME")+"', " +
//                            "A.p_no AS '"+paragraph.getLanguage(line, "COL_PATIENTNO")+"', "+
//                            "concat(patients_info.firstname,'  ',patients_info.lastname) AS '"+paragraph.getLanguage(line, "COL_NAME")+"', "+
//                            "patients_info.birth AS '"+paragraph.getLanguage(line, "COL_BIRTH")+"', "+
//                            "patients_info.gender AS '"+paragraph.getLanguage(line, "COL_GENDER")+"', "+
//                            "concat(patients_info.bloodtype,patients_info.rh_type) AS '"+paragraph.getLanguage(line, "COL_BLOOD")+"', "+
//                            "patients_info.ps AS '"+paragraph.getLanguage(line, "COL_PS")+"', "+
//                            "A.guid "+
//                        "FROM registration_info AS A, patients_info, shift_table,staff_info, prescription, outpatient_services, prescription_code "+
//                        "WHERE A.shift_guid = shift_table.guid "+
//                            "AND shift_table.s_id = staff_info.s_id "+
//                            "AND A.p_no = patients_info.p_no "+
//                            "AND prescription.code = prescription_code.code "+
//                            "AND prescription.os_guid = outpatient_services.guid "+
//                            "AND outpatient_services.reg_guid = A.guid "+
//                            "AND (SELECT COUNT(code) " +
//                            "FROM prescription,outpatient_services, registration_info " +
//                            "WHERE (prescription.finish <> 'F' OR prescription.finish IS  NULL ) " +
//                            "AND prescription.code = prescription_code.code "+
//                            "AND prescription_code.type = '"+Constant.X_RAY_CODE+"' "+
//                            "AND outpatient_services.reg_guid = registration_info.guid " +
//                            "AND prescription.os_guid = outpatient_services.guid " +
//                            "AND registration_info.guid = A.guid)  > 0 "+
//                            "AND A.reg_time LIKE '"+date+"%' "+
//                        "ORDER BY A.reg_time DESC, A.visits_no ";
        } else if (m_SysName.equals("case")) {
//            sql = "SELECT A.visits_no AS '"+paragraph.getLanguage(line, "COL_NO")+"', "+
//                            "A.register AS '"+paragraph.getLanguage(line, "COL_REGISTER")+"', " +
//                            "(SELECT CASE COUNT(registration_info.guid) "+
//                                "WHEN 0 THEN '*' "+
//                                "END  "+
//                                "FROM outpatient_services, registration_info "+
//                                "WHERE registration_info.guid = outpatient_services.reg_guid AND p_no = A.p_no ) AS '"+paragraph.getLanguage(line, "COL_FIRST")+"', "+
//                            "CASE A.case_finish WHEN 'F' THEN 'F' END 'State', "+
//                            "A.reg_time AS '"+paragraph.getLanguage(line, "COL_REGTIME")+"', A.p_no AS '"+paragraph.getLanguage(line, "COL_PATIENTNO")+"', "+
//                            "concat(patients_info.firstname,'  ',patients_info.lastname) AS '"+paragraph.getLanguage(line, "COL_NAME")+"', "+
//                            "patients_info.birth AS '"+paragraph.getLanguage(line, "COL_BIRTH")+"', "+
//                            "patients_info.gender AS '"+paragraph.getLanguage(line, "COL_GENDER")+"', "+
//                            "concat(patients_info.bloodtype,patients_info.rh_type) AS '"+paragraph.getLanguage(line, "COL_BLOOD")+"', "+
//                            "patients_info.ps AS '"+paragraph.getLanguage(line, "COL_PS")+"', "+
//                            "A.guid, policlinic.typ  "+
//                        "FROM registration_info AS A, patients_info, shift_table,staff_info, poli_room, policlinic  "+
//                        "WHERE A.shift_guid = shift_table.guid "+
//                            "AND shift_table.room_guid = poli_room.guid "+
//                            "AND poli_room.poli_guid = policlinic.guid " +
//                            "AND shift_table.s_id = staff_info.s_id "+
//                            "AND (A.case_finish = 'F' OR A.case_finish IS NULL) "+
//                            "AND A.p_no = patients_info.p_no " +
//                            "AND policlinic.typ = 'DM' "+
//                            "AND A.reg_time LIKE '"+date+"%' "+
//                        "ORDER BY A.finish, A.visits_no";
        }
        try{
            rs = DBC.executeQuery(sql);
            ((DefaultTableModel)this.m_Tab.getModel()).setRowCount(0);
            this.m_Tab.setModel(HISModel.getModel(rs));
            rs.last();
            //setCloumnWidth(this.m_Tab);
            
            if(m_SysName.equals("dia")) {
                Object[][] array = {{"O",new Color(204,255,153)}, {"F",new Color(204,255,153)}};
                TabTools.setTabColor(m_Tab, 3, array);
                TabTools.setHideColumn(this.m_Tab,11);
            } else if (m_SysName.equals("case")) {
                Object[][] array = {{"F",new Color(204,255,153)}};
                TabTools.setTabColor(m_Tab, 3, array);
                TabTools.setHideColumn(m_Tab,0);
                TabTools.setHideColumn(m_Tab,11);
            } else if (m_SysName.equals("lab")) {
                Object[][] array = {{"1",new Color(250,232,176)}};
                TabTools.setTabColor(m_Tab, 12, array);
                TabTools.setHideColumn(this.m_Tab,0);
                TabTools.setHideColumn(this.m_Tab,2);
                TabTools.setHideColumn(this.m_Tab,3);
                TabTools.setHideColumn(this.m_Tab,11);
                //TabTools.setHideColumn(this.m_Tab,12);
            } else if (m_SysName.equals("xray")) {
                TabTools.setHideColumn(this.m_Tab,0);
                TabTools.setHideColumn(this.m_Tab,2);
                TabTools.setHideColumn(this.m_Tab,3);
                TabTools.setHideColumn(this.m_Tab,11);
            }
            DBC.closeConnection(rs);
        }catch (SQLException ex) {System.out.println(ex);
        }finally{ try{ DBC.closeConnection(rs); }catch(SQLException ex){} }
    }

//    @Override
//    public void interrupt(){
//        super.interrupt();
//        try{
//            DBC.closeConnection(rs);
//            System.out.println("DBC.closeConnection(rs)");
//        }catch(SQLException ex){}
//        try {
//            finalize();
//        } catch (Throwable ex) {}
//    }
}
        
    

