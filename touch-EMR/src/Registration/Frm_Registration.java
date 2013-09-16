package Registration;

import ErrorMessage.StoredErrorMessage;
import GPSConn.GPSPosition;
import GpsLog.GpsLog;
import Patients.*;
import cc.johnwu.date.*;
import cc.johnwu.finger.*;
import cc.johnwu.sql.*;
import Main.Frm_Main;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import Multilingual.language;
import java.text.SimpleDateFormat;
import AutoComplete.CompleterComboBox;
import Common.Constant;
import System.Setting;
import Common.PrintTools;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;

public class Frm_Registration extends javax.swing.JFrame implements FingerPrintViewerInterface, DateInterface, PatientsInterface {

//    private final String SYSTEMNAME = "Registration";   //系統名稱
    private final long REFRASH_TIME = 3000; //自度刷新跨號資訊時間
    private final int MAX_FINGERPRINT_COUNT = 5000;
    private final int MAX_SEARCH_ROWS = 50;
    private final String POLINAME_DM = Constant.POLINAME_DM;
    private String m_RegShiftGuid; //條件班表
    private String m_RegtGuid;  //修改掛號需要
    private boolean m_SelectShift;
    private boolean m_isSearch = true ;
    private boolean m_isCanClear = true ;
    private RefrashRecord m_RefrashRecord;
    private String sql_FingerSelect;
    private int m_Number = 0;
    private String m_Guid;
    /*多國語言變數*/
    private language paragraph = new language();
    private String[] line = paragraph.setlanguage("REGISTRATION").split("\n") ;
    private String[] message = paragraph.setlanguage("MESSAGE").split("\n") ;
    /*輸出錯誤資訊變數*/
    StoredErrorMessage ErrorMessage = new StoredErrorMessage() ;
    SimpleDateFormat m_FormatDate=new SimpleDateFormat("dd-MM-yyyy");
    private Setting set = new Setting();
    private String[] lineSet = set.setSystem("GIS").split("\n") ;
    /*GPS相關*/
    public  boolean m_ShowGPS;
    private String  m_Gis;
    private String  m_Country;
    public static String s_Position;
    private ReSearchGps m_ReSearchGps;
    private CompleterComboBox m_Cobww;
    private boolean IsConnGPS = false;


    
    public Frm_Registration() {
        // 是否啟動GPS
        if (set.getSetting(lineSet, "ISSTART").equals("1")) {
            IsConnGPS=true;
            m_ShowGPS=true;
        }


        // 初始化
        initComponents();
        init();
        initShiftInfo();
        initPermission();
        initPatientsInfo();
        initTables();
        initLanguage();
        initCobww();
        btn_EditP.setEnabled(false);
        txt_Search.setFocusable(true);
    }

    // 鎖定欄位
   

     private void initLanguage() {
        //this.lab_TitlePNo.setText(paragraph.getLanguage(line, "TITLEPNO"));
        this.lab_TitleNhisNo.setText(paragraph.getLanguage(line, "TITLENHISNO"));
        this.lab_TitleNiaNo.setText(paragraph.getLanguage(line, "TITLENIANO"));
        this.lab_TitleFirstName.setText(paragraph.getLanguage(line, "TITLEFIRSTNAME"));
        this.lab_TitleLastName.setText(paragraph.getLanguage(line, "TITLELASTNAME"));
        this.lab_TitleBirth.setText(paragraph.getLanguage(line, "TITLEBIRTHDAY"));
        this.lab_TitleAge.setText(paragraph.getLanguage(line, "TITLEAGE"));
        this.lab_TitleGender.setText(paragraph.getLanguage(line, "TITLEGENDER"));
        this.lab_TitleBloodtype.setText(paragraph.getLanguage(line, "TITLEBLOODTYPE"));
        this.lab_TitleHeight.setText(paragraph.getLanguage(line, "TITLEHEIGHT"));
        this.lab_TitleWeight.setText(paragraph.getLanguage(line, "TITLEWEIGHT"));
        this.lab_TitleVisits.setText(paragraph.getLanguage(line, "TITLEVISITS"));
        this.lab_TitleShiftDate.setText(paragraph.getLanguage(line, "TITLESHIFTDATE"));
        //this.lab_TitlePoliclinic.setText(paragraph.getLanguage(line, "TITLEPOLICLINIC"));
        this.lab_TitleShift.setText(paragraph.getLanguage(line, "TITLESHIFT"));
        this.lab_TitleDoctorName.setText(paragraph.getLanguage(line, "TITLEDOCTORNAME"));
        //this.lab_TitleRoom.setText(paragraph.getLanguage(line, "TITLEROOM"));
        this.lab_TitleWaitNo.setText(paragraph.getLanguage(line, "TITLEWAITNO"));
        this.lab_FingerprintSearch.setText(paragraph.getLanguage(line, "FINGERPRINTSEARCH"));
        //this.lab_TitleSearch.setText(paragraph.getLanguage(line, "TITLESEARCHDOCTORS")) ;

        this.btn_CancelRegistration.setText(paragraph.getLanguage(line, "CANCELREGISTRATION"));
        this.btn_Cancel.setText(paragraph.getLanguage(line, "CANCEL"));
        this.btn_NewPatient.setText(paragraph.getLanguage(line, "NEWPATIENT"));
        this.btn_Save.setText(paragraph.getLanguage(message, "SAVE"));

        this.btn_Back.setText(paragraph.getLanguage(message, "BACK"));
        this.btn_Search.setText(paragraph.getLanguage(message, "SEARCH"));
        this.jLabel5.setText(paragraph.getLanguage(line, "PORT")) ;
        this.jLabel6.setText(paragraph.getLanguage(line, "GPSADDRESS")) ;
        this.jLabel2.setText(paragraph.getLanguage(line, "COUNTRY")) ;
        this.checkBox_ShowGPS.setText(paragraph.getLanguage(line, "DEFAULT")) ;
        this.btn_Read.setText(paragraph.getLanguage(line, "READ")) ;
        this.btn_SaveGIS.setText(paragraph.getLanguage(line, "SAVEGIS")) ;
        this.setTitle(paragraph.getLanguage(line, "TITLEREQISTRATION"));
        Dialog_SetGpsAddress.setTitle(paragraph.getLanguage(line, "TITLGPSINFORMATION"));
        tpan_List.setTitleAt(1,paragraph.getLanguage(line, "DOCTORLIST"));
        tpan_List.setTitleAt(0,paragraph.getLanguage(line, "PATIENTLIST"));
        tpan_List.setTitleAt(2,paragraph.getLanguage(line, "REGISTRATIONRECORD"));

        cob_Shift.setModel(new javax.swing.DefaultComboBoxModel(
                new String[] { paragraph.getLanguage(line, "ALL"), paragraph.getLanguage(line, "MORNING"),
                               paragraph.getLanguage(line, "AFTERNOON"), paragraph.getLanguage(line, "NIGHT") })
        );
    }

    /** 初始化*/
    private void init(){
        this.setExtendedState(Frm_Registration.MAXIMIZED_BOTH);  // 最大化
        FingerPrintScanner.setParentFrame(this);
        this.setLocationRelativeTo(this);
        setRegisteredState(false);
        this.lab_FingerprintSearch.setText("");

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowevent)
            {
                if (IsConnGPS) {
                    m_ReSearchGps.SetRunState(false);
                    m_ReSearchGps.SetGpsConnClose();
                    m_ReSearchGps.interrupt();
                }
                
                if(isEnabled()){
                    
                    FingerPrintScanner.stop();
                    m_RefrashRecord.interrupt();
                    new Frm_Main().setVisible(true);
                    dispose();
                    
                }
            }
        });

        Dialog_SetGpsAddress.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowevent)
            {
                 btn_SaveGISActionPerformed(null);
            }
        });
        btn_NewPatient.setMnemonic(java.awt.event.KeyEvent.VK_N);
    }

    /** 初始化下拉式選單*/
    public void initCobww() {
        String[] icdCob = null ;
        ResultSet rs = null;

        try {

            String sql = "SELECT name FROM map_country ORDER BY name";
            rs = DBC.executeQuery(sql);
            rs.last();
            icdCob = new String[rs.getRow()+1];
            rs.beforeFirst();
            int i = 0;
            icdCob[i++] = "";
            while (rs.next()) {
                  icdCob[i++] = rs.getString("name").trim();
            }

            m_Cobww = new CompleterComboBox(icdCob);
            m_Cobww.setBounds(115, 72, 265, 20);
            pan_gps.add(m_Cobww);
            m_Cobww.setSelectedIndex(0);
        } catch (SQLException e) {
            ErrorMessage.setData("Diagnosis", "Frm_DiagnosisPrescription" ,"initCobww()",
                    e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        } finally {
            try {DBC.closeConnection(rs);}
            catch (SQLException e){
                ErrorMessage.setData("Diagnosis", "Frm_DiagnosisPrescription" ,"initCobww() - DBC.closeConnection",
                        e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            }
        }
        if (IsConnGPS) {
            m_ReSearchGps = new ReSearchGps(this,lab_Port,lab_GPS,m_Cobww);
            m_ReSearchGps.start();
        }
        
    }

    /** 初始化權限*/
    private void initPermission(){
    }

    /** 初始化下拉式選單*/
    private void initShiftInfo(){
        ResultSet rs = null;
        try {
            this.lab_Register.setText("REISTER");
            this.dateChooser1.setParentFrame(this);
            this.cob_Policlinic.removeAllItems();
            this.cob_Policlinic.addItem(paragraph.getLanguage(line, "ALL"));
            String sql = "SELECT name FROM policlinic";

                rs = DBC.executeQuery(sql);
                while (rs.next()){
                    this.cob_Policlinic.addItem(rs.getString("name"));
                }
                DBC.closeConnection(rs);
            this.cob_Shift.setSelectedIndex(DateMethod.getNowShiftNum());
            this.lab_Doctor.setText("");
            this.lab_Room.setText("");
            this.lab_WaitNo.setText("");
            this.m_RegtGuid = "";
            DBC.closeConnection(rs);
        } catch (SQLException e) {
            ErrorMessage.setData("Registration", "Frm_Registration" ,"initShiftInfo()",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        }
        this.cob_Shift.setSelectedItem(DateMethod.getNowShiftNum());
    }

    /** 初始化病患資料*/
    private void initPatientsInfo(){
        this.lab_PNo.setText("");
        this.lab_NhisNo.setText("");
        this.lab_NiaNo.setText("");
        this.lab_FirstName.setText("");
        this.lab_LastName.setText("");
        this.lab_Birth.setText("");
        this.lab_Age.setText("");
        this.lab_Gender.setText("");
        this.lab_Bloodtype.setText("");
        this.lab_Rh.setText("");
        this.lab_Height.setText("");
        this.lab_Weight.setText("");
    }

    /** 初始化表單*/
    private void initTables(){
        showShiftList();
        tab_PatientsList.setModel(getModle(new String[]{"Message"},new String[][]{{"You need to give more keywords."}}));
        tab_RegistrationList.setModel(getModle(new String[]{"Message"},new String[][]{{"No Information."}}));
        m_RefrashRecord = new RefrashRecord(tab_RegistrationList,REFRASH_TIME);
        m_RefrashRecord.start();
        this.lab_msg.setText("");
    }

    /** 設定班表清單*/
    private void showShiftList(){
        //*********************************************//
        ResultSet rs = null;
        String sql  = "SELECT policlinic.name AS "+paragraph.getLanguage(line, "COL_POLICLINIC")+", " +
                        "CASE shift_table.shift " +
                            "WHEN '1' THEN 'Morning' " +
                            "WHEN '2' THEN 'Afternoon' " +
                            "WHEN '3' THEN 'Night' " +
                            "ELSE 'All Night'" +
                            "END '"+paragraph.getLanguage(line, "COL_SHIFT")+"' , " +
                        "concat(staff_info.firstname,'  ',staff_info.lastname) AS '"+paragraph.getLanguage(line, "COL_DOCTOR")+"' " +
                "FROM staff_info,shift_table,policlinic,poli_room " +
                "WHERE shift_table.shift_date = '" + dateChooser1.getValue() + "' " +
                "AND shift_table.s_id = staff_info.s_id " +
                "AND shift_table.room_guid = poli_room.guid " +
                "AND poli_room.poli_guid = policlinic.guid ";

        if(DateMethod.getDaysRange(dateChooser1.getValue())>0
        || (DateMethod.getDaysRange(dateChooser1.getValue())==0
        && cob_Shift.getSelectedIndex()>DateMethod.getNowShiftNum())){
            this.lab_Register.setText(paragraph.getLanguage(line, "RESERVATION"));
        }else{
            this.lab_Register.setText(paragraph.getLanguage(line, "LOCALITY"));
        }
        /**判斷是否用搜尋按鈕搜尋醫生*//**********************************/
        if(m_isSearch){
            //判斷掛號狀態
            //*********************************************//
            if(DateMethod.getDaysRange(dateChooser1.getValue())<0
            || (DateMethod.getDaysRange(dateChooser1.getValue())==0
                && cob_Shift.getSelectedIndex()<DateMethod.getNowShiftNum()
                && cob_Shift.getSelectedIndex()!=0)
            ){
                tab_ShiftList.setModel(getModle(new String[]{"Message"},new String[][]{{"Has expired."}}));
                return;
            }
            //科別搜尋條件
            //*********************************************//
            if(cob_Policlinic.getSelectedIndex() != 0){
                sql += "AND policlinic.name = '"+cob_Policlinic.getSelectedItem().toString()+"' ";
            }
            //午別搜尋條件
            //*********************************************//
            if(cob_Shift.getSelectedIndex() == 0){
                if(this.dateChooser1.getValue().equals(DateMethod.getTodayYMD()))
                    sql += "AND shift_table.shift >= "+DateMethod.getNowShiftNum()+" ";
            }else{
                sql += "AND shift_table.shift = '"+(cob_Shift.getSelectedIndex())+"' ";
            }
        }else{
            sql += "AND (UPPER(concat(staff_info.firstname,' ',staff_info.lastname)) LIKE UPPER('%" + txt_Search.getText().replace(" ", "%") + "%')) ";
        }
        m_isSearch = true ;
        //*********************************************//        
        try {
            rs = DBC.executeQuery(sql);
            ((DefaultTableModel)tab_ShiftList.getModel()).setRowCount(0);
            if(rs.next()){
                tab_ShiftList.setModel(HISModel.getModel(rs, true));
                TableColumn tc = tab_ShiftList.getColumnModel().getColumn(0);
                tc.setMaxWidth(40);
                tc.setMinWidth(40);

                TableColumn colShift = tab_ShiftList.getColumnModel().getColumn(2);
                colShift.setMaxWidth(130);
                colShift.setMinWidth(130);
            }else{
                tab_ShiftList.setModel(getModle(new String[]{paragraph.getLanguage(message, "Message")},new String[][]{{"No Information."}}));
            }
            DBC.closeConnection(rs);
        } catch (SQLException e) {
            ErrorMessage.setData("Registration", "Frm_Registration" ,"showShiftList()",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        }
        //*********************************************//
    }

    /** 設定病患清單*/
    private void showPatientList(){
        ResultSet rs = null;
        String sql = "";
        try {

            sql = "SELECT p_no AS 'Patient No.', " +
                        "concat(FirstName,' ',LastName) AS '"+paragraph.getLanguage(line, "COL_NAME")+"', " +
                        "Gender AS '"+paragraph.getLanguage(line, "COL_GENDER")+"', " +
                        "(DATE_FORMAT(now(),'%Y')-DATE_FORMAT(birth,'%Y')) AS '"+paragraph.getLanguage(line, "COL_AGE")+"', " +
                        "concat(bloodtype,' ',rh_type) AS '"+paragraph.getLanguage(line, "COL_BLOODTYPE")+"' " +
                  "FROM patients_info " +
                  "WHERE exist =  1 " +
                  "AND (UPPER(p_no) LIKE UPPER('%" + txt_Search.getText().replace(" ", "%") + "%') " +
                  "OR UPPER(nhis_no) LIKE UPPER('%" + txt_Search.getText().replace(" ", "%") + "%') " +
                  "OR UPPER(nia_no) LIKE UPPER('%" + txt_Search.getText().replace(" ", "%") + "%') " +
                  "OR UPPER(concat(firstname,' ',lastname)) LIKE UPPER('%" + txt_Search.getText().replace(" ", "%") + "%')" +
                  ")";

            rs = DBC.executeQuery(sql);
            rs.last();
            if(rs.getRow() >= MAX_SEARCH_ROWS){
                this.lab_msg.setText("<html><font color='FF0000'>"+paragraph.getLanguage(message ,"MOREINFORMATION")+
                                     "</font></html>");
                JOptionPane.showMessageDialog(new Frame(), paragraph.getLanguage(message , "GIVEMOREKEYWORDS"));
                tab_PatientsList.setModel(HISModel.getModel(rs,1,MAX_SEARCH_ROWS));
                return;
            }
            rs.beforeFirst();
            if(rs.next()){
                tab_PatientsList.setModel(HISModel.getModel(rs));
                TableColumn tc = tab_PatientsList.getColumnModel().getColumn(0);
                tc.setMaxWidth(80);
                tc.setMinWidth(80);
                tc = tab_PatientsList.getColumnModel().getColumn(2);
                tc.setMaxWidth(40);
                tc.setMinWidth(40);
                tc = tab_PatientsList.getColumnModel().getColumn(3);
                tc.setMaxWidth(40);
                tc.setMinWidth(40);
                tc = tab_PatientsList.getColumnModel().getColumn(4);
                tc.setMaxWidth(40);
                tc.setMinWidth(40);
            }else{
                tab_PatientsList.setModel(getModle(new String[]{"Message"},new String[][]{{"No Information."}}));
            }
            this.tpan_List.setSelectedIndex(0);
        } catch (SQLException e) {
            ErrorMessage.setData("Registration", "Frm_Registration" ,"showPatientList()",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        } finally {
            try{DBC.closeConnection(rs);}
            catch (SQLException e) {
                ErrorMessage.setData("Registration", "Frm_Registration" ,"showPatientList() - DBC.closeConnection",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            }
        }
        txt_Search.setFocusable(true);
    }

    /** 取得掛號資訊*/
    private void showRegistered(){
        ResultSet rs = null;
        try{
            String sql = "SELECT CASE registration_info.register " +
                                   "WHEN 'R' THEN 'Reservation' " +
                                   "ELSE 'Locality'" +
                                   "END 'Register', " +
                               "registration_info.guid, " +
                               "policlinic.name, " +
                               "poli_room.name, " +
                               "shift_table.shift_date, " +
                               "shift_table.shift, " +
                               "concat(staff_info.firstname,'  ',staff_info.lastname) AS Doctor, " +
                               "registration_info.visits_no " +
                         "FROM registration_info, patients_info, shift_table, poli_room, policlinic, staff_info " +
                         "WHERE patients_info.p_no = '"+this.lab_PNo.getText()+"' " +
                         "AND registration_info.shift_guid = shift_table.guid " +
                         "AND registration_info.p_no = patients_info.p_no " +
                         "AND shift_table.room_guid = poli_room.guid " +
                         "AND poli_room.poli_guid = policlinic.guid " +
                         "AND staff_info.s_id = shift_table.s_id " +
                         "AND registration_info.finish IS NULL " +
                         "AND shift_table.shift_date >= DATE_FORMAT(now(),'%Y%m%d') ";
            System.out.println(sql);
            rs = DBC.executeQuery(sql);
            if(!rs.next()){
                //未掛過號程序
                //*********************************************//
                setRegisteredState(false);
//                initShiftInfo();
                //*********************************************//
                this.btn_Save.setEnabled(isCanRegistration());
            }else{
                //已掛過號程序
                //*********************************************//
                setRegisteredState(true);
                //*********************************************//
                this.m_RegtGuid = rs.getString("registration_info.guid");
                this.lab_Register.setText(rs.getString("Register"));
                this.dateChooser1.setValue(m_FormatDate.format(rs.getDate("shift_table.shift_date")));
                this.cob_Policlinic.setSelectedItem(rs.getString("policlinic.name"));
                this.cob_Shift.setSelectedIndex(Integer.parseInt(rs.getString("shift_table.shift")));
                this.lab_Doctor.setText(rs.getString("Doctor"));
                this.lab_Room.setText(rs.getString("poli_room.name"));
                this.lab_WaitNo.setText(rs.getString("registration_info.visits_no"));
                //*********************************************//
//                tab_ShiftList.setModel(getModle(new String[]{"Message"},new String[][]{{"你已經掛號過了."}}));
            }
        } catch (SQLException e) {
            ErrorMessage.setData("Registration", "Frm_Registration" ,"showRegistered()",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        } finally {
            try{DBC.closeConnection(rs);}
            catch (SQLException e) {
                ErrorMessage.setData("Registration", "Frm_Registration" ,"showRegistered() - DBC.closeConnection",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            }
        }
    }

    /** 取得帶診人數。*/
    private void showRoomInfo(){ //抓取目前選定醫生的待診人數 and Poli_room name
        ResultSet rs = null;
        String sql = "";
        try {
            sql = "SELECT shift_table.guid, poli_room.name " +
                  "FROM shift_table, staff_info, poli_room " +
                  "WHERE concat(staff_info.firstname,'  ',staff_info.lastname) = '" + this.lab_Doctor.getText() + "' " +
                  "AND shift_table.shift_date = '" + this.dateChooser1.getValue() + "' " +
                  "AND shift_table.shift = '"+this.cob_Shift.getSelectedIndex()+"' " +
                  "AND poli_room.guid = shift_table.room_guid " +
                  "AND shift_table.s_id = staff_info.s_id ";
            rs = DBC.executeQuery(sql);
            rs.next();
            m_RegShiftGuid = rs.getString("shift_table.guid");
            this.lab_Room.setText(rs.getString("poli_room.name"));

            sql = "SELECT COUNT(guid) AS count " +
                  "FROM registration_info " +
                  "WHERE shift_guid = '"+m_RegShiftGuid+"' " +
                  "AND finish IS NULL ";
            rs = DBC.executeQuery(sql); //計算這個班表有幾人待診
            rs.next();
            this.lab_WaitNo.setText(rs.getString("count"));
            this.btn_Save.setEnabled(isCanRegistration());
        } catch (SQLException e) {
            ErrorMessage.setData("Registration", "Frm_Registration" ,"showRoomInfo()",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        } finally {
            try{DBC.closeConnection(rs);}
            catch (SQLException e) {
                ErrorMessage.setData("Registration", "Frm_Registration" ,"showRoomInfo() - DBC.closeConnection",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            }
        }
    }

    /** 設定按鈕顯示狀態*/
    private void setRegisteredState(boolean flag){
        this.btn_Cancel.setVisible(flag);
        this.btn_CancelRegistration.setVisible(flag);
        this.btn_EditP1.setVisible(flag);

        this.btn_EditP.setVisible(!flag);
        this.btn_EditP.setEnabled(!flag);
        this.btn_NewPatient.setVisible(!flag);
        this.btn_NewPatient.setEnabled(!flag);
        this.btn_Save.setVisible(!flag);
        this.btn_Save.setEnabled(!flag && isCanRegistration());
        this.btn_Back.setVisible(!flag);
        this.btn_Back.setEnabled(!flag);
        this.dateChooser1.setEnabled(!flag);
        this.cob_Policlinic.setEnabled(!flag);
        this.cob_Shift.setEnabled(!flag);

        this.pan_ShiftInfo.setEnabled(!flag);
        this.tpan_List.setEnabled(!flag);
        this.tab_PatientsList.setEnabled(!flag);
        this.tab_RegistrationList.setEnabled(!flag);
        this.tab_ShiftList.setEnabled(!flag);
    }

    /** 設定病患資料*/
    private void setPatientsInfo(String sql){
        ResultSet rs = null;
        try {
            rs = DBC.executeQuery(sql);
            rs.next();
            //**********************************
            this.lab_PNo.setText(rs.getString("p_no"));
            this.lab_NhisNo.setText(rs.getString("nhis_no"));
            this.lab_NiaNo.setText(rs.getString("nia_no"));
            this.lab_FirstName.setText(rs.getString("firstname"));
            this.lab_LastName.setText(rs.getString("lastname"));
            this.lab_Birth.setText(rs.getString("birth"));
            this.lab_Age.setText(DateMethod.getAgeWithMonth(rs.getDate("birth")));
            this.lab_Gender.setText(rs.getString("gender"));
            this.lab_Bloodtype.setText(rs.getString("bloodtype"));
            this.lab_Rh.setText(rs.getString("rh_type"));
            this.lab_Height.setText(rs.getString("height"));
            this.lab_Weight.setText(rs.getString("weight"));
            //***********************************
            btn_EditP.setEnabled(true);
        } catch (SQLException e) {
            ErrorMessage.setData("Registration", "Frm_Registration" ,"setPatientsInfo(String sql)",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            this.btn_Save.setEnabled(false);
            initPatientsInfo();

        } finally {
            try{DBC.closeConnection(rs);}
            catch (SQLException e) {
                ErrorMessage.setData("Registration", "Frm_Registration" ,"setPatientsInfo(String sql) - DBC.closeConnection",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            }
        }
    }

public void ShowGpsFrom() {
    if (m_ShowGPS) {
        Point p = this.getLocation();
        int x = p.x+(this.getWidth()-Dialog_SetGpsAddress.getWidth())/2;
        int y = p.y+(this.getHeight()-Dialog_SetGpsAddress.getHeight())/2;
        this.Dialog_SetGpsAddress.setLocation(x, y);
        this.Dialog_SetGpsAddress.setVisible(true);
        this.Dialog_SetGpsAddress.setAlwaysOnTop(true);
        this.checkBox_ShowGPS.setSelected(false);
    }
    
}


    /** 設定表單預設模型。*/
    private DefaultTableModel getModle(String[] title,String[][] data){
        return new DefaultTableModel(data,title){
                    @Override
                    public boolean isCellEditable(int r, int c){
                    return false;}
               };
    }

    private boolean isCanRegistration(){
        if(!this.lab_PNo.getText().trim().equals("")
        && !this.lab_Doctor.getText().equals("")
        && (DateMethod.getDaysRange(dateChooser1.getValue())>0
        || (DateMethod.getDaysRange(dateChooser1.getValue())==0
            && cob_Shift.getSelectedIndex()>=DateMethod.getNowShiftNum()))
        ){
            return true;
        }else{
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Dialog_FingerConditions = new javax.swing.JDialog();
        jPanel2 = new javax.swing.JPanel();
        lab_Gender1 = new javax.swing.JLabel();
        lab_ListName = new javax.swing.JLabel();
        txt_Town = new javax.swing.JTextField();
        btn_Ok = new javax.swing.JButton();
        lab_Town = new javax.swing.JLabel();
        cob_Gender = new javax.swing.JComboBox();
        lab_FirstName1 = new javax.swing.JLabel();
        txt_FirstName = new javax.swing.JTextField();
        txt_LastName = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        Dialog_SetGpsAddress = new javax.swing.JDialog();
        pan_gps = new javax.swing.JPanel();
        lab_Port = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        checkBox_ShowGPS = new javax.swing.JCheckBox();
        btn_SaveGIS = new javax.swing.JButton();
        btn_Read = new javax.swing.JButton();
        lab_GPS = new javax.swing.JLabel();
        pan_PatientInfo = new javax.swing.JPanel();
        fingerPrintViewer1 = new cc.johnwu.finger.FingerPrintViewer();
        pan_TopWest = new javax.swing.JPanel();
        lab_TitleGender = new javax.swing.JLabel();
        lab_TitlePNo = new javax.swing.JLabel();
        lab_TitleFirstName = new javax.swing.JLabel();
        lab_TitleNhisNo = new javax.swing.JLabel();
        lab_TitleBloodtype = new javax.swing.JLabel();
        lab_TitleBirth = new javax.swing.JLabel();
        lab_TitleAge = new javax.swing.JLabel();
        lab_PNo = new javax.swing.JLabel();
        lab_NhisNo = new javax.swing.JLabel();
        lab_FirstName = new javax.swing.JLabel();
        lab_Age = new javax.swing.JLabel();
        lab_Birth = new javax.swing.JLabel();
        lab_Gender = new javax.swing.JLabel();
        lab_Bloodtype = new javax.swing.JLabel();
        lab_Rh = new javax.swing.JLabel();
        lab_LastName = new javax.swing.JLabel();
        lab_TitleLastName = new javax.swing.JLabel();
        lab_TitleHeight = new javax.swing.JLabel();
        lab_Height = new javax.swing.JLabel();
        lab_Weight = new javax.swing.JLabel();
        lab_TitleWeight = new javax.swing.JLabel();
        lab_TitleNiaNo = new javax.swing.JLabel();
        lab_NiaNo = new javax.swing.JLabel();
        lab_FingerprintSearch = new javax.swing.JLabel();
        pan_ShiftInfo = new javax.swing.JPanel();
        lab_TitleVisits = new javax.swing.JLabel();
        lab_TitlePoliclinic = new javax.swing.JLabel();
        cob_Policlinic = new javax.swing.JComboBox();
        lab_TitleShift = new javax.swing.JLabel();
        cob_Shift = new javax.swing.JComboBox();
        lab_TitleDoctorName = new javax.swing.JLabel();
        lab_TitleShiftDate = new javax.swing.JLabel();
        lab_TitleRoom = new javax.swing.JLabel();
        lab_TitleWaitNo = new javax.swing.JLabel();
        lab_WaitNo = new javax.swing.JLabel();
        lab_Room = new javax.swing.JLabel();
        lab_Register = new javax.swing.JLabel();
        lab_Doctor = new javax.swing.JLabel();
        dateChooser1 = new cc.johnwu.date.DateChooser();
        pan_Bottom = new javax.swing.JPanel();
        btn_Save = new javax.swing.JButton();
        btn_Back = new javax.swing.JButton();
        btn_Cancel = new javax.swing.JButton();
        btn_CancelRegistration = new javax.swing.JButton();
        btn_NewPatient = new javax.swing.JButton();
        btn_EditP = new javax.swing.JButton();
        btn_EditP1 = new javax.swing.JButton();
        tpan_List = new javax.swing.JTabbedPane();
        span_PatientsList = new javax.swing.JScrollPane();
        tab_PatientsList = new javax.swing.JTable();
        span_ShiftList = new javax.swing.JScrollPane();
        tab_ShiftList = new javax.swing.JTable();
        span_RegistrationList = new javax.swing.JScrollPane();
        tab_RegistrationList = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        txt_Search = new javax.swing.JTextField();
        btn_Search = new javax.swing.JButton();
        lab_TitleSearch = new javax.swing.JLabel();
        lab_msg = new javax.swing.JLabel();

        Dialog_FingerConditions.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        Dialog_FingerConditions.setTitle("Fingetprint Conditions");
        Dialog_FingerConditions.setAlwaysOnTop(true);
        Dialog_FingerConditions.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        Dialog_FingerConditions.setMinimumSize(new java.awt.Dimension(238, 220));
        Dialog_FingerConditions.setModal(true);
        Dialog_FingerConditions.setResizable(false);

        lab_Gender1.setText("Gender : ");

        lab_ListName.setText("LastName : ");

        btn_Ok.setText("OK");
        btn_Ok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_OkActionPerformed(evt);
            }
        });

        lab_Town.setText("Town : ");

        cob_Gender.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "M", "F" }));

        lab_FirstName1.setText("FirstName : ");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lab_Gender1)
                    .addComponent(lab_ListName)
                    .addComponent(lab_FirstName1)
                    .addComponent(lab_Town))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btn_Ok, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE)
                    .addComponent(txt_Town, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE)
                    .addComponent(cob_Gender, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_LastName, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE)
                    .addComponent(txt_FirstName, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_FirstName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_FirstName1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_LastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_ListName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(cob_Gender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_Gender1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_Town, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_Town))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Ok, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("<html>Please input condition</html>");

        javax.swing.GroupLayout Dialog_FingerConditionsLayout = new javax.swing.GroupLayout(Dialog_FingerConditions.getContentPane());
        Dialog_FingerConditions.getContentPane().setLayout(Dialog_FingerConditionsLayout);
        Dialog_FingerConditionsLayout.setHorizontalGroup(
            Dialog_FingerConditionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Dialog_FingerConditionsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Dialog_FingerConditionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE))
                .addContainerGap())
        );
        Dialog_FingerConditionsLayout.setVerticalGroup(
            Dialog_FingerConditionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Dialog_FingerConditionsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        Dialog_SetGpsAddress.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        Dialog_SetGpsAddress.setTitle("Gps Information");
        Dialog_SetGpsAddress.setAlwaysOnTop(true);
        Dialog_SetGpsAddress.setBackground(new java.awt.Color(233, 242, 255));
        Dialog_SetGpsAddress.setMinimumSize(new java.awt.Dimension(600, 200));
        Dialog_SetGpsAddress.setResizable(false);

        pan_gps.setBackground(new java.awt.Color(224, 224, 224));
        pan_gps.setMaximumSize(new java.awt.Dimension(495, 155));

        lab_Port.setText("-----");

        jLabel5.setText("Port");

        jLabel2.setText("Country");

        jLabel6.setText("GPS Address");

        checkBox_ShowGPS.setBackground(new java.awt.Color(224, 224, 224));
        checkBox_ShowGPS.setText("Default");
        checkBox_ShowGPS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBox_ShowGPSActionPerformed(evt);
            }
        });

        btn_SaveGIS.setText("Save");
        btn_SaveGIS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_SaveGISActionPerformed(evt);
            }
        });

        btn_Read.setText("Read");
        btn_Read.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ReadActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pan_gpsLayout = new javax.swing.GroupLayout(pan_gps);
        pan_gps.setLayout(pan_gpsLayout);
        pan_gpsLayout.setHorizontalGroup(
            pan_gpsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_gpsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan_gpsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan_gpsLayout.createSequentialGroup()
                        .addComponent(checkBox_ShowGPS)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_SaveGIS, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pan_gpsLayout.createSequentialGroup()
                        .addGroup(pan_gpsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addGap(20, 20, 20)
                        .addGroup(pan_gpsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lab_GPS, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pan_gpsLayout.createSequentialGroup()
                                .addComponent(lab_Port)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 246, Short.MAX_VALUE)
                                .addComponent(btn_Read, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jLabel2))
                .addContainerGap())
        );
        pan_gpsLayout.setVerticalGroup(
            pan_gpsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_gpsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan_gpsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(btn_Read)
                    .addComponent(lab_Port))
                .addGap(7, 7, 7)
                .addGroup(pan_gpsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lab_GPS, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pan_gpsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_SaveGIS)
                    .addComponent(checkBox_ShowGPS))
                .addContainerGap())
        );

        javax.swing.GroupLayout Dialog_SetGpsAddressLayout = new javax.swing.GroupLayout(Dialog_SetGpsAddress.getContentPane());
        Dialog_SetGpsAddress.getContentPane().setLayout(Dialog_SetGpsAddressLayout);
        Dialog_SetGpsAddressLayout.setHorizontalGroup(
            Dialog_SetGpsAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Dialog_SetGpsAddressLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pan_gps, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        Dialog_SetGpsAddressLayout.setVerticalGroup(
            Dialog_SetGpsAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Dialog_SetGpsAddressLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pan_gps, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Registration");

        pan_PatientInfo.setBackground(new java.awt.Color(245, 245, 251));
        pan_PatientInfo.setToolTipText("");
        pan_PatientInfo.setPreferredSize(new java.awt.Dimension(687, 304));

        fingerPrintViewer1.setVisible(true);

        javax.swing.GroupLayout fingerPrintViewer1Layout = new javax.swing.GroupLayout(fingerPrintViewer1.getContentPane());
        fingerPrintViewer1.getContentPane().setLayout(fingerPrintViewer1Layout);
        fingerPrintViewer1Layout.setHorizontalGroup(
            fingerPrintViewer1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 95, Short.MAX_VALUE)
        );
        fingerPrintViewer1Layout.setVerticalGroup(
            fingerPrintViewer1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 111, Short.MAX_VALUE)
        );

        pan_TopWest.setMaximumSize(new java.awt.Dimension(250, 181));
        pan_TopWest.setMinimumSize(new java.awt.Dimension(222, 181));

        lab_TitleGender.setText("Gender :");

        lab_TitlePNo.setText("Patient No.:");

        lab_TitleFirstName.setText("First Name :");

        lab_TitleNhisNo.setText("NHIS No. :");

        lab_TitleBloodtype.setText("Bloodtype :");

        lab_TitleBirth.setText("Birthday :");

        lab_TitleAge.setText("Age :");

        lab_PNo.setText("00000000");

        lab_NhisNo.setText("000000000000000");

        lab_FirstName.setText("First Name");

        lab_Age.setText("20-year-old");

        lab_Birth.setText("0000-00-00");

        lab_Gender.setText("M/F");

        lab_Bloodtype.setText("AB");

        lab_Rh.setText("RH");

        lab_LastName.setText("Last Name");

        lab_TitleLastName.setText("Last Name :");

        lab_TitleHeight.setText("Height :");

        lab_Height.setText("000");

        lab_Weight.setText("000");

        lab_TitleWeight.setText("Weight :");

        lab_TitleNiaNo.setText("NID No. :");

        lab_NiaNo.setText("000000000000000");

        javax.swing.GroupLayout pan_TopWestLayout = new javax.swing.GroupLayout(pan_TopWest);
        pan_TopWest.setLayout(pan_TopWestLayout);
        pan_TopWestLayout.setHorizontalGroup(
            pan_TopWestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_TopWestLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan_TopWestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lab_TitleLastName, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lab_TitleHeight, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lab_TitlePNo, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lab_TitleWeight, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lab_TitleGender, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lab_TitleNiaNo, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lab_TitleNhisNo, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lab_TitleFirstName, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lab_TitleBirth, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lab_TitleBloodtype, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lab_TitleAge, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_TopWestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lab_Weight)
                    .addComponent(lab_LastName, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                    .addComponent(lab_Gender)
                    .addComponent(lab_NiaNo)
                    .addGroup(pan_TopWestLayout.createSequentialGroup()
                        .addComponent(lab_Bloodtype)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lab_Rh))
                    .addComponent(lab_Age, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                    .addComponent(lab_Birth, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                    .addComponent(lab_NhisNo, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                    .addComponent(lab_PNo, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                    .addComponent(lab_FirstName, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                    .addComponent(lab_Height))
                .addGap(63, 63, 63))
        );
        pan_TopWestLayout.setVerticalGroup(
            pan_TopWestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_TopWestLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan_TopWestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab_TitlePNo, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_PNo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_TopWestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(lab_NhisNo)
                    .addComponent(lab_TitleNhisNo, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_TopWestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab_TitleNiaNo)
                    .addComponent(lab_NiaNo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_TopWestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(lab_TitleFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_FirstName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_TopWestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(lab_LastName)
                    .addComponent(lab_TitleLastName, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_TopWestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(lab_Birth)
                    .addComponent(lab_TitleBirth, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_TopWestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(lab_Age, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_TitleAge, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_TopWestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(lab_TitleGender, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_Gender))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_TopWestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(lab_Bloodtype)
                    .addComponent(lab_TitleBloodtype)
                    .addComponent(lab_Rh))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_TopWestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(lab_Height)
                    .addComponent(lab_TitleHeight))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_TopWestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(lab_Weight)
                    .addComponent(lab_TitleWeight))
                .addContainerGap(35, Short.MAX_VALUE))
        );

        lab_FingerprintSearch.setText("Search...");

        javax.swing.GroupLayout pan_PatientInfoLayout = new javax.swing.GroupLayout(pan_PatientInfo);
        pan_PatientInfo.setLayout(pan_PatientInfoLayout);
        pan_PatientInfoLayout.setHorizontalGroup(
            pan_PatientInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan_PatientInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pan_TopWest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_PatientInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lab_FingerprintSearch)
                    .addComponent(fingerPrintViewer1, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE))
                .addContainerGap())
        );
        pan_PatientInfoLayout.setVerticalGroup(
            pan_PatientInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_PatientInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan_PatientInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pan_TopWest, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pan_PatientInfoLayout.createSequentialGroup()
                        .addComponent(fingerPrintViewer1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lab_FingerprintSearch)))
                .addContainerGap())
        );

        pan_ShiftInfo.setBackground(new java.awt.Color(245, 245, 251));
        pan_ShiftInfo.setName("pan_ShiftInfo"); // NOI18N

        lab_TitleVisits.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lab_TitleVisits.setText("Registered Method :");

        lab_TitlePoliclinic.setText("Depart/Clinic :");

        cob_Policlinic.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cob_PoliclinicItemStateChanged(evt);
            }
        });

        lab_TitleShift.setText("Shift :");

        cob_Shift.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "All", "Morning", "Afternoon", "Night" }));
        cob_Shift.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cob_ShiftItemStateChanged(evt);
            }
        });

        lab_TitleDoctorName.setText("Doctor :");

        lab_TitleShiftDate.setText("Date :");

        lab_TitleRoom.setText("Clinic :");

        lab_TitleWaitNo.setText("Waiting No. :");

        lab_WaitNo.setText("000");

        lab_Room.setText("Room's Name");

        lab_Register.setText("現掛/預約");

        lab_Doctor.setText("Doctor Name");

        javax.swing.GroupLayout pan_ShiftInfoLayout = new javax.swing.GroupLayout(pan_ShiftInfo);
        pan_ShiftInfo.setLayout(pan_ShiftInfoLayout);
        pan_ShiftInfoLayout.setHorizontalGroup(
            pan_ShiftInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_ShiftInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan_ShiftInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lab_TitleDoctorName)
                    .addComponent(lab_TitleShift)
                    .addComponent(lab_TitlePoliclinic)
                    .addComponent(lab_TitleShiftDate)
                    .addComponent(lab_TitleRoom)
                    .addComponent(lab_TitleVisits))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_ShiftInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_Register)
                    .addComponent(lab_Doctor, javax.swing.GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
                    .addGroup(pan_ShiftInfoLayout.createSequentialGroup()
                        .addComponent(lab_Room, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE)
                        .addGap(55, 55, 55)
                        .addComponent(lab_TitleWaitNo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lab_WaitNo, javax.swing.GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE))
                    .addComponent(cob_Shift, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cob_Policlinic, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        pan_ShiftInfoLayout.setVerticalGroup(
            pan_ShiftInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_ShiftInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan_ShiftInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab_Register)
                    .addComponent(lab_TitleVisits))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_ShiftInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan_ShiftInfoLayout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(lab_TitleShiftDate))
                    .addGroup(pan_ShiftInfoLayout.createSequentialGroup()
                        .addComponent(dateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan_ShiftInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cob_Policlinic, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lab_TitlePoliclinic))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_ShiftInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cob_Shift, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_TitleShift))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_ShiftInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                    .addComponent(lab_TitleDoctorName)
                    .addComponent(lab_Doctor))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_ShiftInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                    .addComponent(lab_WaitNo)
                    .addComponent(lab_Room)
                    .addComponent(lab_TitleRoom)
                    .addComponent(lab_TitleWaitNo))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pan_Bottom.setMinimumSize(new java.awt.Dimension(432, 91));

        btn_Save.setText("Register");
        btn_Save.setEnabled(false);
        btn_Save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_SaveActionPerformed(evt);
            }
        });

        btn_Back.setText("Close");
        btn_Back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_BackActionPerformed(evt);
            }
        });

        btn_Cancel.setText("Cancel");
        btn_Cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CancelActionPerformed(evt);
            }
        });

        btn_CancelRegistration.setText("Cancel Registration");
        btn_CancelRegistration.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CancelRegistrationActionPerformed(evt);
            }
        });

        btn_NewPatient.setText("Add Patient");
        btn_NewPatient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_NewPatientActionPerformed(evt);
            }
        });

        btn_EditP.setText("Edit");
        btn_EditP.setEnabled(false);
        btn_EditP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_EditPActionPerformed(evt);
            }
        });

        btn_EditP1.setText("Edit");
        btn_EditP1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_EditP1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pan_BottomLayout = new javax.swing.GroupLayout(pan_Bottom);
        pan_Bottom.setLayout(pan_BottomLayout);
        pan_BottomLayout.setHorizontalGroup(
            pan_BottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_BottomLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan_BottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan_BottomLayout.createSequentialGroup()
                        .addComponent(btn_EditP1, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_CancelRegistration, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_Cancel, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pan_BottomLayout.createSequentialGroup()
                        .addComponent(btn_EditP, javax.swing.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_NewPatient, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_Save, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_Back, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        pan_BottomLayout.setVerticalGroup(
            pan_BottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_BottomLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan_BottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_Cancel)
                    .addComponent(btn_EditP1)
                    .addComponent(btn_CancelRegistration))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_BottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_Save)
                    .addComponent(btn_NewPatient)
                    .addComponent(btn_Back)
                    .addComponent(btn_EditP))
                .addContainerGap(29, Short.MAX_VALUE))
        );

        tpan_List.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tpan_ListStateChanged(evt);
            }
        });

        tab_PatientsList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tab_PatientsList.setRowHeight(30);
        tab_PatientsList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tab_PatientsList.getTableHeader().setReorderingAllowed(false);
        tab_PatientsList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tab_PatientsListMouseClicked(evt);
            }
        });
        tab_PatientsList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tab_PatientsListKeyPressed(evt);
            }
        });
        span_PatientsList.setViewportView(tab_PatientsList);

        tpan_List.addTab("Patient List", span_PatientsList);

        tab_ShiftList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tab_ShiftList.setRowHeight(30);
        tab_ShiftList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tab_ShiftList.getTableHeader().setReorderingAllowed(false);
        tab_ShiftList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tab_ShiftListMouseClicked(evt);
            }
        });
        tab_ShiftList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tab_ShiftListKeyPressed(evt);
            }
        });
        span_ShiftList.setViewportView(tab_ShiftList);

        tpan_List.addTab("Doctor List", span_ShiftList);

        tab_RegistrationList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tab_RegistrationList.setRowHeight(30);
        tab_RegistrationList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tab_RegistrationList.getTableHeader().setReorderingAllowed(false);
        tab_RegistrationList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tab_RegistrationListMouseClicked(evt);
            }
        });
        tab_RegistrationList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tab_RegistrationListKeyPressed(evt);
            }
        });
        span_RegistrationList.setViewportView(tab_RegistrationList);

        tpan_List.addTab("Registration Record", span_RegistrationList);

        txt_Search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_SearchActionPerformed(evt);
            }
        });
        txt_Search.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_SearchKeyPressed(evt);
            }
        });

        btn_Search.setText("Search");
        btn_Search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_SearchActionPerformed(evt);
            }
        });

        lab_TitleSearch.setText("Patient : ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(lab_TitleSearch)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_Search, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Search, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(txt_Search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(lab_TitleSearch)
                .addComponent(btn_Search))
        );

        lab_msg.setText("msg");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(pan_ShiftInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pan_PatientInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 428, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lab_msg, javax.swing.GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tpan_List, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(pan_Bottom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(348, 348, 348)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tpan_List, javax.swing.GroupLayout.DEFAULT_SIZE, 524, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lab_msg)
                        .addGap(16, 16, 16))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pan_PatientInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pan_ShiftInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pan_Bottom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_NewPatientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_NewPatientActionPerformed
        showImage(null,"");
        new Frm_PatientMod(this).setVisible(true);
        this.setEnabled(false);
}//GEN-LAST:event_btn_NewPatientActionPerformed

    private void btn_BackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_BackActionPerformed
        FingerPrintScanner.stop();
         if (IsConnGPS) {
            m_ReSearchGps.SetGpsConnClose();
         }
        new Frm_Main().setVisible(true);
        this.dispose();
}//GEN-LAST:event_btn_BackActionPerformed

    private void btn_SaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SaveActionPerformed
        String sql = "";
        m_Number = 0;
        try {
            sql = "INSERT INTO registration_info " +
                  "SELECT uuid(), " +
                        "'"+this.lab_PNo.getText()+"', " +
                        "now(), " +
                        "NULL, " + // GIS欄位
                        "'"+m_RegShiftGuid+"', " +
                        "'"+lab_Register.getText().charAt(0) +"', " +
                        "NULL, " +
                        "NULL, " +
                        "NULL, " +
                        "NULL, " +
                        "NULL, " +
                        "NULL, " +
                        "NULL, " +
                        "NULL, " +
                        "NULL, " +
                        "COUNT(shift_guid)+1, " +
                        "NULL, " +
                        "RPAD((SELECT CASE " +
                            "WHEN MAX(B.touchtime) >= DATE_FORMAT(now(),'%Y%m%d%H%i%S') " +
                                "THEN concat(DATE_FORMAT(now(),'%Y%m%d%H%i%S'),COUNT(B.touchtime)) " +
                            "ELSE DATE_FORMAT(now(),'%Y%m%d%H%i%S') " +
                            "END touchtime " +
                        "FROM (SELECT touchtime FROM registration_info) AS B " +
                        "WHERE B.touchtime LIKE concat(DATE_FORMAT(now(),'%Y%m%d%H%i%S'),'%')),20,'000000'), " +
                        "NULL, " +
                        "RPAD((SELECT CASE WHEN MAX(C.record_touchtime) >= DATE_FORMAT(now(),'%Y%m%d%H%i%S') " +
                                "THEN concat(DATE_FORMAT(now(),'%Y%m%d%H%i%S'), COUNT(C.record_touchtime)) " +
                                "ELSE DATE_FORMAT(now(),'%Y%m%d%H%i%S') " +
                                "END record_touchtime FROM (SELECT record_touchtime FROM registration_info) AS C " +
                                "WHERE C.record_touchtime LIKE " +
                                "concat(DATE_FORMAT(now(),'%Y%m%d%H%i%S'),'%')),20,'000000') " +
                  "FROM registration_info "+
                  "WHERE shift_guid = '"+m_RegShiftGuid+"'";

            DBC.executeUpdate(sql);

            sql = "SELECT visits_no, guid FROM registration_info " +
                  "WHERE shift_guid = '"+m_RegShiftGuid+"' " +
                  "AND p_no ='"+this.lab_PNo.getText()+"' " +
                  "AND finish IS NULL " +
                  "AND touchtime LIKE concat(DATE_FORMAT(now(),'%Y%m%d%H%i'),'%') ";
            ResultSet rs = DBC.executeQuery(sql);
            if(rs.next()){
                m_Number = rs.getInt(1);
                m_Guid = rs.getString("guid");
            }


          
            String html = "<html><font size='6'>";
            //******************************************//
            //*****儲存目前位置*****//
            m_Gis = this.lab_GPS.getText().trim();
            m_Country = (String) this.m_Cobww.getSelectedItem();
            sql = "INSERT INTO gis(guid, gis, reg_guid, address) " +
                  "VALUES(uuid(),'" + m_Gis + "','" + m_Guid + "','" + m_Country+ "' ) ";
            DBC.executeUpdate(sql);
             //*****儲存目前位置*****//
            //****掛號成功訊息******//


            PrintTools pt = null;

            sql = "SELECT p_no, firstname, lastname, gender, birth, '"+lab_Age.getText()+"' AS age, '"+dateChooser1.getValue()+"' AS date, '"+lab_Room.getText()+"' AS clinic, " +
                    "'"+cob_Shift.getSelectedItem().toString()+"' AS shift, " +
                    "'"+cob_Policlinic.getSelectedItem().toString()+"' AS dept, " +
                    "'"+m_Number+"' AS waitno, " +
                    "'"+lab_Doctor.getText()+"' AS doctor," +
                    "'"+lab_Register.getText()+"' AS type " +
              "FROM patients_info " +
              "WHERE exist = 1 AND p_no = '" + lab_PNo.getText() + "' ";
    
            rs = DBC.executeQuery(sql);
            rs.next();
            pt = new PrintTools();
            pt.DoPrint(1, rs);

            System.out.println(cob_Policlinic.getSelectedItem().toString());
            if (cob_Policlinic.getSelectedItem().toString().equals(POLINAME_DM)) {
                sql = "SELECT p_no, firstname, lastname,'"+dateChooser1.getValue()+"' AS date, " +
                        "'"+cob_Shift.getSelectedItem().toString()+"' AS shift, " +
                        "'"+cob_Policlinic.getSelectedItem().toString()+"' AS dept, " +
                        "'"+lab_Room.getText()+"' AS clinic, " +
                        "'"+lab_Doctor.getText()+"' AS doctor " +
                  "FROM patients_info " +
                  "WHERE exist = 1 AND p_no = '" + lab_PNo.getText() + "' ";
                rs = DBC.executeQuery(sql);
                rs.next();
                pt = new PrintTools();
                pt.DoPrint(2, rs);
            }
            JOptionPane.showMessageDialog(null, html + paragraph.getLanguage(line, "PATIENTNAME") + 
                    this.lab_FirstName.getText() + " " +
                    this.lab_LastName.getText() + " \n" + html + paragraph.getLanguage(line, "TITLEBIRTHDAY") +
                    this.lab_Birth.getText() + " \n" + html + paragraph.getLanguage(line, "TITLEAGE") +
                    this.lab_Age.getText() + " \n" + html + paragraph.getLanguage(line, "TITLEGENDER") +
                    this.lab_Gender.getText() + " "+
                    this.lab_Rh.getText() + " \n" + html + "***************************************\n" + html + paragraph.getLanguage(line, "DATE") +
                    this.dateChooser1.getValue() + "\n" + html + paragraph.getLanguage(line, "TITLEPOLICLINIC") +
                    this.cob_Policlinic.getSelectedItem().toString() + "\n" + html + paragraph.getLanguage(line, "TITLESHIFT") +
                    this.cob_Shift.getSelectedItem().toString() + "\n" + html + paragraph.getLanguage(line, "TITLEDOCTORNAME") +
                    this.lab_Doctor.getText() + "\n" + html + "Clinic：" +
                    this.lab_Room.getText() + "\n" + html + paragraph.getLanguage(line, "TITLEWAITNO") + m_Number + "\n" + html + "***************************************", paragraph.getLanguage(message, "REGISTRATIONSUCCESSFUL"), JOptionPane.DEFAULT_OPTION);
            //******************************************//

           DBC.closeConnection(rs);
           reLoad();
           tab_PatientsList.setModel(getModle(new String[]{"Message"},new String[][]{{"You need to give more keywords."}}));
        } catch (SQLException e) {
            Logger.getLogger(Frm_Registration.class.getName()).log(Level.SEVERE, null, e);
                ErrorMessage.setData("Registration", "Frm_Registration" ,"btn_SaveActionPerformed()",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        }
        
}//GEN-LAST:event_btn_SaveActionPerformed

    private void tab_ShiftListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab_ShiftListMouseClicked
        if(tab_ShiftList.getSelectedRow()<0 || tab_ShiftList.getColumnCount()==1) return ;
        m_SelectShift = true;
        String[] str = new String[tab_ShiftList.getColumnCount()];
        for(int i=0; i<str.length; i++){
            str[i] = tab_ShiftList.getValueAt(tab_ShiftList.getSelectedRow(), i).toString();
        }
        this.cob_Policlinic.setSelectedItem(str[1]);
        this.cob_Shift.setSelectedIndex(DateMethod.getShiftStr2Num(str[2]));
        this.lab_Doctor.setText(str[3]);
        this.showRoomInfo();
        m_SelectShift = false;
}//GEN-LAST:event_tab_ShiftListMouseClicked

    private void btn_CancelRegistrationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CancelRegistrationActionPerformed
        try {
            String sql = "UPDATE registration_info SET finish = 'C', " +
                            "touchtime = RPAD((SELECT CASE " +
                                "WHEN MAX(B.touchtime) >= DATE_FORMAT(now(),'%Y%m%d%H%i%S') THEN concat(DATE_FORMAT(now(),'%Y%m%d%H%i%S'),COUNT(B.touchtime)) " +
                                "ELSE DATE_FORMAT(now(),'%Y%m%d%H%i%S') " +
                                "END touchtime " +
                                "FROM (SELECT touchtime FROM registration_info) AS B " +
                                "WHERE B.touchtime LIKE concat(DATE_FORMAT(now(),'%Y%m%d%H%i%S'),'%')),20,'000000') " +
                         " WHERE guid = '"+m_RegtGuid+"'";

            DBC.executeUpdate(sql);
            JOptionPane.showMessageDialog(null, paragraph.getLanguage(message , "CANCELREGISTRATION"));
            reLoad();
        } catch (SQLException e) {
                ErrorMessage.setData("Registration", "Frm_Registration" ,"btn_CancelRegistrationActionPerformed()",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        }
}//GEN-LAST:event_btn_CancelRegistrationActionPerformed

    private void btn_CancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CancelActionPerformed
        reLoad();
}//GEN-LAST:event_btn_CancelActionPerformed

    private void cob_ShiftItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cob_ShiftItemStateChanged
        if(evt.getStateChange() == java.awt.event.ItemEvent.SELECTED && !m_SelectShift)
            showShiftList();
    }//GEN-LAST:event_cob_ShiftItemStateChanged

    private void cob_PoliclinicItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cob_PoliclinicItemStateChanged
        if(evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) if(!m_SelectShift) showShiftList();
    }//GEN-LAST:event_cob_PoliclinicItemStateChanged

    private void tab_ShiftListKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tab_ShiftListKeyPressed
        tab_ShiftListMouseClicked(null);
    }//GEN-LAST:event_tab_ShiftListKeyPressed

    private void txt_SearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_SearchKeyPressed
        if(evt.getKeyCode()==java.awt.event.KeyEvent.VK_ENTER) btn_SearchActionPerformed(null);
    }//GEN-LAST:event_txt_SearchKeyPressed

    private void tab_PatientsListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab_PatientsListMouseClicked
        if(tab_PatientsList.getSelectedRow()<0 || tab_PatientsList.getColumnCount()==1) return ;
        String sql = "SELECT * " +
                     "FROM patients_info " +
                     "WHERE exist = 1 " +
                     "AND p_no = '" + tab_PatientsList.getValueAt(tab_PatientsList.getSelectedRow(), 0) + "' ";
        showImage(null,"");
        setPatientsInfo(sql);
        showRegistered();
    }//GEN-LAST:event_tab_PatientsListMouseClicked

    private void tab_RegistrationListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab_RegistrationListMouseClicked
        if(tab_RegistrationList.getSelectedRow()<0 || tab_RegistrationList.getColumnCount()==1) return ;
        String sql = "SELECT * " +
                     "FROM patients_info " +
                     "WHERE exist = 1 " +
                     "AND p_no = '" + tab_RegistrationList.getValueAt(tab_RegistrationList.getSelectedRow(), 1) + "' ";
        showImage(null,"");
        setPatientsInfo(sql);
        showRegistered();
    }//GEN-LAST:event_tab_RegistrationListMouseClicked

    private void tab_RegistrationListKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tab_RegistrationListKeyPressed
        tab_RegistrationListMouseClicked(null);
    }//GEN-LAST:event_tab_RegistrationListKeyPressed

    private void tab_PatientsListKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tab_PatientsListKeyPressed
        tab_PatientsListMouseClicked(null);
    }//GEN-LAST:event_tab_PatientsListKeyPressed

    private void btn_SearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SearchActionPerformed
        switch(this.tpan_List.getSelectedIndex()){
            case 0:
                showPatientList();
                
                break ;
            case 1:
                m_isSearch = false ;
                cob_Policlinic.setSelectedIndex(0);
                cob_Shift.setSelectedIndex(0);
                showShiftList() ;
                break ;
            
        }
    }//GEN-LAST:event_btn_SearchActionPerformed

    private void tpan_ListStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tpan_ListStateChanged
        this.lab_msg.setText("");
        switch(this.tpan_List.getSelectedIndex()){
            case 0:
               // showPatientList();
                btn_Search.setVisible(true) ;
                txt_Search.setVisible(true) ;
                lab_TitleSearch.setVisible(true) ;
                lab_TitleSearch.setText(paragraph.getLanguage(line, "TITLESEARCHPATIENT")) ;
                break ;
            case 1:
                btn_Search.setVisible(true) ;
                txt_Search.setVisible(true) ;
                lab_TitleSearch.setVisible(true) ;
                lab_TitleSearch.setText(paragraph.getLanguage(line, "TITLESEARCHDOCTORS")) ;
                break ;
            
            case 2 :
                this.jPanel1.setVisible(true);
                btn_Search.setVisible(false) ;
                txt_Search.setVisible(false) ;
                lab_TitleSearch.setVisible(false) ;
                break ;
        }
    }//GEN-LAST:event_tpan_ListStateChanged

    private void btn_OkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_OkActionPerformed
        sql_FingerSelect = "SELECT fingertemplate.id AS id,fingertemplate.template AS template " +
                            "FROM fingertemplate LEFT JOIN patients_info " +
                            "ON patients_info.p_no =  fingertemplate.id " +
                            "WHERE patients_info.exist = true " ;
        /**判斷各項選擇是否有輸入值*/
        if(!txt_FirstName.getText().trim().equals(""))
            sql_FingerSelect += "AND UPPER(patients_info.firstname) LIKE UPPER('%" + txt_FirstName.getText().replace(" ", "%") + "%') " ;
        if(!txt_LastName.getText().trim().equals(""))
            sql_FingerSelect += "AND UPPER(patients_info.lastname) LIKE UPPER('%" + txt_LastName.getText().replace(" ", "%") + "%') " ;
        if(!txt_Town.getText().trim().equals(""))
            sql_FingerSelect += "AND UPPER(patients_info.town) LIKE UPPER('%" + txt_Town.getText().replace(" ", "%") + "%') " ;
        sql_FingerSelect += "AND patients_info.gender = '" + cob_Gender.getSelectedItem().toString() + "' ";

        if(txt_FirstName.getText().trim().equals("")
        && txt_LastName.getText().trim().equals("")
        && txt_Town.getText().trim().equals("")){
            Dialog_FingerConditions.setAlwaysOnTop(false);
            Object[] options = {paragraph.getLanguage(message , "YES"),paragraph.getLanguage(message , "NO")};
            int response = JOptionPane.showOptionDialog(
                            new Frame(),
                            paragraph.getLanguage(message ,"KEYWORDSARETOOFEW")+"\n"+
                            paragraph.getLanguage(message ,"AREYOUSUREYOUWANTTOCONTINUE"),
                            paragraph.getLanguage(message ,"WARNING"),
                            JOptionPane.YES_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options,
                            options[0]
                        );
            if(response!=0){
                txt_FirstName.setText("--------------------");
                txt_LastName.setText("--------------------");
                txt_Town.setText("--------------------");
                lab_FingerprintSearch.setText("");
            }
        }
        Dialog_FingerConditions.setVisible(false);
}//GEN-LAST:event_btn_OkActionPerformed

    private void btn_SaveGISActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SaveGISActionPerformed
            GpsLog gpsLog = new GpsLog();
            m_Gis = this.lab_GPS.getText().trim();
            m_Country = (String) this.m_Cobww.getSelectedItem();

            this.Dialog_SetGpsAddress.setVisible(false);

            gpsLog.setData(m_Gis, m_Country);
            m_ReSearchGps.SetRunState(true);
    }//GEN-LAST:event_btn_SaveGISActionPerformed

    private void btn_ReadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ReadActionPerformed
       
        GPSPosition GPS = new GPSPosition();
        if (GPS.getScanPort() == null) {  //沒有搜尋到port
            lab_GPS.setText("");
        } else {
            this.lab_Port.setText(GPS.getScanPort());
            String address = GPS.getPosition(GPS.getScanPort());
            this.lab_GPS.setText(address);
            if (address.equals("0")) {
                this.lab_GPS.setText("");
            }
        }
        
    }//GEN-LAST:event_btn_ReadActionPerformed

    private void checkBox_ShowGPSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBox_ShowGPSActionPerformed
        if (this.checkBox_ShowGPS.getSelectedObjects() == null){
            this.m_ShowGPS = true;
        } else {
            this.m_ShowGPS = false;
        }
    }//GEN-LAST:event_checkBox_ShowGPSActionPerformed

    private void txt_SearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_SearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_SearchActionPerformed

    private void btn_EditPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_EditPActionPerformed
        showImage(null,"");
        new Frm_PatientMod(this, lab_PNo.getText()).setVisible(true);
        m_isCanClear = false;
        this.setEnabled(false);
    }//GEN-LAST:event_btn_EditPActionPerformed

    private void btn_EditP1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_EditP1ActionPerformed
       btn_EditPActionPerformed(null);
    }//GEN-LAST:event_btn_EditP1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDialog Dialog_FingerConditions;
    private javax.swing.JDialog Dialog_SetGpsAddress;
    private javax.swing.JButton btn_Back;
    private javax.swing.JButton btn_Cancel;
    private javax.swing.JButton btn_CancelRegistration;
    private javax.swing.JButton btn_EditP;
    private javax.swing.JButton btn_EditP1;
    private javax.swing.JButton btn_NewPatient;
    private javax.swing.JButton btn_Ok;
    private javax.swing.JButton btn_Read;
    private javax.swing.JButton btn_Save;
    private javax.swing.JButton btn_SaveGIS;
    private javax.swing.JButton btn_Search;
    private javax.swing.JCheckBox checkBox_ShowGPS;
    private javax.swing.JComboBox cob_Gender;
    private javax.swing.JComboBox cob_Policlinic;
    private javax.swing.JComboBox cob_Shift;
    private cc.johnwu.date.DateChooser dateChooser1;
    private cc.johnwu.finger.FingerPrintViewer fingerPrintViewer1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lab_Age;
    private javax.swing.JLabel lab_Birth;
    private javax.swing.JLabel lab_Bloodtype;
    private javax.swing.JLabel lab_Doctor;
    private javax.swing.JLabel lab_FingerprintSearch;
    private javax.swing.JLabel lab_FirstName;
    private javax.swing.JLabel lab_FirstName1;
    private javax.swing.JLabel lab_GPS;
    private javax.swing.JLabel lab_Gender;
    private javax.swing.JLabel lab_Gender1;
    private javax.swing.JLabel lab_Height;
    private javax.swing.JLabel lab_LastName;
    private javax.swing.JLabel lab_ListName;
    private javax.swing.JLabel lab_NhisNo;
    private javax.swing.JLabel lab_NiaNo;
    private javax.swing.JLabel lab_PNo;
    private javax.swing.JLabel lab_Port;
    private javax.swing.JLabel lab_Register;
    private javax.swing.JLabel lab_Rh;
    private javax.swing.JLabel lab_Room;
    private javax.swing.JLabel lab_TitleAge;
    private javax.swing.JLabel lab_TitleBirth;
    private javax.swing.JLabel lab_TitleBloodtype;
    private javax.swing.JLabel lab_TitleDoctorName;
    private javax.swing.JLabel lab_TitleFirstName;
    private javax.swing.JLabel lab_TitleGender;
    private javax.swing.JLabel lab_TitleHeight;
    private javax.swing.JLabel lab_TitleLastName;
    private javax.swing.JLabel lab_TitleNhisNo;
    private javax.swing.JLabel lab_TitleNiaNo;
    private javax.swing.JLabel lab_TitlePNo;
    private javax.swing.JLabel lab_TitlePoliclinic;
    private javax.swing.JLabel lab_TitleRoom;
    private javax.swing.JLabel lab_TitleSearch;
    private javax.swing.JLabel lab_TitleShift;
    private javax.swing.JLabel lab_TitleShiftDate;
    private javax.swing.JLabel lab_TitleVisits;
    private javax.swing.JLabel lab_TitleWaitNo;
    private javax.swing.JLabel lab_TitleWeight;
    private javax.swing.JLabel lab_Town;
    private javax.swing.JLabel lab_WaitNo;
    private javax.swing.JLabel lab_Weight;
    private javax.swing.JLabel lab_msg;
    private javax.swing.JPanel pan_Bottom;
    private javax.swing.JPanel pan_PatientInfo;
    private javax.swing.JPanel pan_ShiftInfo;
    private javax.swing.JPanel pan_TopWest;
    private javax.swing.JPanel pan_gps;
    private javax.swing.JScrollPane span_PatientsList;
    private javax.swing.JScrollPane span_RegistrationList;
    private javax.swing.JScrollPane span_ShiftList;
    private javax.swing.JTable tab_PatientsList;
    private javax.swing.JTable tab_RegistrationList;
    private javax.swing.JTable tab_ShiftList;
    private javax.swing.JTabbedPane tpan_List;
    private javax.swing.JTextField txt_FirstName;
    private javax.swing.JTextField txt_LastName;
    private javax.swing.JTextField txt_Search;
    private javax.swing.JTextField txt_Town;
    // End of variables declaration//GEN-END:variables

    @Override
    public void onFingerDown() {
        ResultSet rs = null;
        String sql = null ;
        long start = 0;
        long finish = 0;
        try {
            sql_FingerSelect = "SELECT id,template FROM fingertemplate ";
            rs = DBC.executeQuery("SELECT COUNT(*) FROM fingertemplate ");
            if(rs.next() && rs.getInt(1) > MAX_FINGERPRINT_COUNT){
                Point p = this.getLocation();
                int x = p.x+(this.getWidth()-Dialog_FingerConditions.getWidth())/2;
                int y = p.y+(this.getHeight()-Dialog_FingerConditions.getHeight())/2;
                this.Dialog_FingerConditions.setLocation(x, y);
                txt_FirstName.setText("");
                txt_LastName.setText("");
                cob_Gender.setSelectedIndex(0);
                txt_Town.setText("");
                this.Dialog_FingerConditions.setVisible(true);
            }
            DBC.closeConnection(rs);

            this.tpan_List.setSelectedIndex(0);
            this.txt_Search.setText("");
            initPatientsInfo();
            start = System.currentTimeMillis();
            lab_FingerprintSearch.setText(paragraph.getLanguage(message, "SEACRH..."));
            rs = DBC.executeQuery(sql_FingerSelect);
            String PatientsNO = FingerPrintScanner.identify(rs);
            finish = System.currentTimeMillis();
            lab_FingerprintSearch.setText("It took "+((float)(finish-start)/(float)1000)+"/s");
            if(PatientsNO.equals("")){
                lab_FingerprintSearch.setText("Not found");
                JOptionPane.showMessageDialog(new Frame(), "No information\n you can use alt + n add Patients");
            }else{
                sql = "SELECT * " +
                      "FROM patients_info " +
                      "WHERE exist = 1 " +
                      "AND p_no = '" + PatientsNO + "' ";
                setPatientsInfo(sql);
            }
            showRegistered();
        } catch (SQLException e) {
                ErrorMessage.setData("Registration", "Frm_Registration" ,"onFingerDown()",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        } finally {
            try{DBC.closeConnection(rs);}
            catch (SQLException e) {
                ErrorMessage.setData("Registration", "Frm_Registration" ,"onFingerDown() - DBC.closeConnection",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            }
        }
    }

    @Override
    public void showImage(BufferedImage bufferedimage,String msg) {
        this.fingerPrintViewer1.showImage(bufferedimage);
        this.fingerPrintViewer1.setTitle(msg);
    }

    @Override
    public void reLoad(){

        if (m_isCanClear) {
            initShiftInfo();
            initPermission();
            initPatientsInfo();
            this.tpan_List.setSelectedIndex(0);
            showShiftList();
            setRegisteredState(false);
            System.out.println("啟動指紋機");
            FingerPrintScanner.setParentFrame(this);

            this.setEnabled(true);
            this.txt_Search.setText("");
            this.btn_Save.setEnabled(false);
            this.btn_EditP.setEnabled(false);

            m_RegtGuid = "";
        } else {
  
            m_isCanClear = true;
            this.setEnabled(true);
        }

    }

    @Override
    public void onDateChanged() {
        showShiftList();
    }

    @Override
    public void onPatientMod(String pno) {
        String sql = "SELECT * " +
                      "FROM patients_info " +
                      "WHERE exist = 1 " +
                      "AND p_no = '" + pno + "' ";
        setPatientsInfo(sql);
    }

}