package Patients;


//import Diagnosis.*;
import Barcode.PrintBarcode;
import Cashier.Frm_CashierHistory;
import ErrorMessage.StoredErrorMessage;
import Laboratory.Frm_LabHistory;
import cc.johnwu.finger.*;
import cc.johnwu.date.*;
import cc.johnwu.sql.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.sql.*;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import Multilingual.language;
import Registration.Frm_Registration;
import Radiology.Frm_RadiologyHistory;
import cc.johnwu.login.UserInfo;


public class Frm_PatientMod  extends javax.swing.JFrame 
        implements FingerPrintViewerInterface, DateInterface, Diagnosis.DiagnosisInterface{
    private PatientsInterface m_frame = null;
    private String m_UUID;
    private String[] m_MaritalStatus ={"m","d","s","o"};
    
    private String m_Status;
    /*多國語言變數*/

    private language paragraph = new language();
    private String[] line = paragraph.setlanguage("PATIENTMOD").split("\n") ;
    private String[] message = paragraph.setlanguage("MESSAGE").split("\n") ;
    /*輸出錯誤資訊變數*/
    StoredErrorMessage ErrorMessage = new StoredErrorMessage() ;

    java.awt.event.ActionEvent evt;
    public Frm_PatientMod(PatientsInterface m_frame) {
        initComponents();
        initPatientInfo();
        initContact() ;
        init();
        initcBox();
        initPermission();
        initLanguage() ;
        this.m_frame = m_frame;
        this.setTitle("New Patient Data");
        m_Status = "NEW";
        check_Deal.setVisible(false);
        txt_Language.setVisible(false);
        lab_Language.setVisible(false);
        txt_EmeLanguage.setVisible(false);
        lab_EmeLanguage.setVisible(false);
        if (m_frame.getClass().getName().equals("Registration.Frm_Registration")) {
            bar_menu.setVisible(false);
        }
        

    }

    public Frm_PatientMod(PatientsInterface m_frame, String p_no){
        this(m_frame);
        setPatientIndo(p_no);
        this.setTitle(paragraph.getLanguage(line, "EDITPATIENTDATA"));
        m_Status = "EDIT";
        this.btn_OK.setEnabled(isCanSave());
        check_Deal.setVisible(true);
        initLanguage() ;
        if (m_frame.getClass().getName().equals("Registration.Frm_Registration")) {
            bar_menu.setVisible(false);
        }
    }

    /** 初始化*/
    private void init(){
        this.setExtendedState(Frm_PatientMod.MAXIMIZED_BOTH);  // 最大化
        this.setLocationRelativeTo(this);//視窗顯示至中
        this.dateChooser1.setParentFrame(this);
        FingerPrintScanner.setParentFrame(this);//打開指紋機
        addWindowListener(new WindowAdapter() {//視窗關閉事件
            @Override
            public void windowClosing(WindowEvent windowevent)
            {
                btn_CancelActionPerformed(null);
            }
        });

        ResultSet rs = null;
        try {
            m_UUID = UUID.randomUUID().toString();
            String sql = "INSERT INTO patients_info (p_no,firstname,lastname,exist,c_sno) " +
                "SELECT IF(COUNT(p_no) <> 0,MAX(p_no)+1,0), " +// p_no
                "'"+m_UUID+"', " +      // firstname
                "'"+m_UUID+"', " +      // lastname
                "false, " +              // exist
                " "+UserInfo.getUserNO()+" "+
                "FROM patients_info ";
            DBC.executeUpdate(sql);
            sql = "SELECT p_no FROM patients_info " +
                  "WHERE firstname = '"+m_UUID+"' ";
            rs = DBC.executeQuery(sql);
            if(rs.next()){
                this.txt_No.setText(rs.getString(1));
            }




        } catch (SQLException e) {
            ErrorMessage.setData("Patients", "Frm_PatientMod" ,"init()",
                    e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            System.out.println(e);
        } finally{
            try{ DBC.closeConnection(rs); }
            catch (SQLException e) {
                ErrorMessage.setData("Patients", "Frm_PatientMod" ,"init() - DBC.closeConnection",
                    e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            }
        }
    }

    /** 初始化權限*/
    private void initPermission(){
        //this.btn_OK.setEnabled(false);
    }

    /** 初始化病患資料欄位*/
    private void initPatientInfo(){
        this.txt_No.setText("");
        this.txt_FirstName.setText("");
        this.txt_LastName.setText("");
        this.txt_NhisNo.setText("");
        this.txt_NiaNo.setText("");
        this.txt_Height.setText("");
        this.txt_Weight.setText("");
        this.cob_Gender.setSelectedItem("");
        this.cob_Bloodtype.setSelectedItem("");
        this.cob_Rh.setSelectedItem("");
        this.txt_Address.setText("");
        this.txt_Town.setText("");
        this.txt_State.setText("");
        this.txt_Country.setText("");
        this.txt_Phone1.setText("");
        this.txt_Phone2.setText("");
        this.txt_Ps.setText("");
        this.btn_Enroll.setEnabled(false);
        this.btn_OK.setEnabled(false);
        this.showImage(null,"");
    }

    /**初始化聯絡人欄位*/
    private void initContact(){
        this.txt_EmeFirstName.setText("");
        this.txt_EmeLastName.setText("");
        this.txt_EmePhone.setText("");
        this.txt_EmeRelation.setText("");
        this.txt_EmeState.setText("");
        this.txta_EmeAddress.setText("");
        this.txt_CPTown.setText("");
        this.txt_EmeCountry.setText("");
    }

    private void initLanguage() {
        this.lab_TitleNo.setText(paragraph.getLanguage(line, "TITLENO"));
        this.lab_TitleFirstName.setText("* "+paragraph.getLanguage(line, "TITLEFIRSTNAME"));
        this.lab_TitleLastName.setText("* "+paragraph.getLanguage(line, "TITLELASTNAME"));
        this.lab_NhisNo.setText(paragraph.getLanguage(line, "NHISNO"));
        this.lab_NiaNo.setText(paragraph.getLanguage(line, "NIANO"));
        this.lab_PlaceOfBirth.setText(paragraph.getLanguage(line, "PLACEOFBIRTH"));
        this.lab_EmePlaceOfBirth.setText(paragraph.getLanguage(line, "PLACEOFBIRTH"));
        this.lab_MaritalStatus.setText(paragraph.getLanguage(line, "MARITALSTATUS"));
        this.lab_EmeMaritalStatus.setText(paragraph.getLanguage(line, "MARITALSTATUS"));
        this.lab_Occupation.setText(paragraph.getLanguage(line, "OCCUPATION"));
        this.lab_EmeOccupation.setText(paragraph.getLanguage(line, "OCCUPATION"));
        this.lab_Tribe.setText(paragraph.getLanguage(line, "TRIBE"));
        this.lab_EmeTribe.setText(paragraph.getLanguage(line, "TRIBE"));
        this.lab_Religion.setText(paragraph.getLanguage(line, "RELIGION"));
        this.lab_EmeReligion.setText(paragraph.getLanguage(line, "RELIGION"));
        this.lab_Language.setText(paragraph.getLanguage(line, "LANGUAGE"));
        this.lab_EmeLanguage.setText(paragraph.getLanguage(line, "LANGUAGE"));

        this.lab_TitleGender.setText(paragraph.getLanguage(line, "TITLEGENDER"));
        this.lab_EmeGender.setText(paragraph.getLanguage(line, "TITLEGENDER"));
        //this.lab_TitleBirth.setText(paragraph.getLanguage(line, "TITLEBIRTH"));
        this.lab_TitleBloodtype.setText(paragraph.getLanguage(line, "TITLEBLOODTYPE"));
        this.lab_TitleHeight.setText(paragraph.getLanguage(line, "TITLEHEIGHT"));
        this.lab_TitleWeight.setText(paragraph.getLanguage(line, "TITLEWEIGHT"));

        this.check_Deal.setText(paragraph.getLanguage(line, "DEAL"));


        this.lab_TitleEmeFirstName.setText(paragraph.getLanguage(line, "TITLEEMEFIRSTNAME"));
        this.lab_TitleEmelastName.setText(paragraph.getLanguage(line, "TITLEEMELASTNAME"));
        this.lab_TitleEmeRelation.setText(paragraph.getLanguage(line, "TITLEEMERELATION"));
        this.lab_TitleCPTown.setText(paragraph.getLanguage(line, "TITLEEMECPTOWN"));
        this.lab_TitleEmeAddress.setText(paragraph.getLanguage(line, "TITLEEMEADDRESS"));
        this.lab_TitleEmePhone.setText(paragraph.getLanguage(line, "TITLEEMEPHONE"));
//        this.lab_TitleEmeState.setText(paragraph.getLanguage(line, "TITLEEMESTATE"));
        this.lab_TitleEmeCountry.setText(paragraph.getLanguage(line, "TITLEEMECOUNTRY"));

        //this.lab_TitlePhone1.setText(paragraph.getLanguage(line, "TITLEPHONE1"));
        //this.lab_TitlePhone2.setText(paragraph.getLanguage(line, "TITLEPHONE2"));
        this.lab_TitleTown.setText(paragraph.getLanguage(line, "TITLETOWN"));
        //this.lab_TitleState.setText(paragraph.getLanguage(line, "TITLESTATE"));
        this.lab_Country.setText(paragraph.getLanguage(line, "COUNTRY"));
        this.lab_TitleAddress.setText(paragraph.getLanguage(line, "TITLEADDRESS"));
        this.lab_Ps.setText(paragraph.getLanguage(line, "PS"));

        this.btn_Enroll.setText(paragraph.getLanguage(line, "ENROLL"));
        this.btn_OK.setText(paragraph.getLanguage(message, "SAVE"));
        this.btn_Cancel.setText(paragraph.getLanguage(line, "CANCEL"));

        this.pan_Right.setBorder(javax.swing.BorderFactory.createTitledBorder(paragraph.getLanguage(line, "RIGHT")));
        this.pan_Left.setBorder(javax.swing.BorderFactory.createTitledBorder(paragraph.getLanguage(line, "LEFT")));
        this.jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(paragraph.getLanguage(line, "PANEL")));
        this.Pat_List.setTitleAt(0, paragraph.getLanguage(line, "PATIENTINFRMATION"));
        this.Pat_List.setTitleAt(1, paragraph.getLanguage(line, "CONTRACTPERSON"));

        cob_MaritalStatus.setModel(
                new javax.swing.DefaultComboBoxModel(
                    new String[] { paragraph.getLanguage(line, "MARRIED"), paragraph.getLanguage(line, "DIVORCED"),
                                   paragraph.getLanguage(line, "SINGLE"), paragraph.getLanguage(line, "OTHER")
                                 }
                )
        );

        cob_EmeMaritalStatus.setModel(
                new javax.swing.DefaultComboBoxModel(
                    new String[] { paragraph.getLanguage(line, "MARRIED"), paragraph.getLanguage(line, "DIVORCED"),
                                   paragraph.getLanguage(line, "SINGLE"), paragraph.getLanguage(line, "OTHER")
                                 }
                )
        );

    }

    // 初始化 種族  宗教
    private void initcBox() {
        try {
            cbox_Trible.addItem("");
            cbox_Religion.addItem("");

            String sql = "SELECT CONCAT(value,' - ', descrition) AS religion FROM religion ";
            ResultSet rs = DBC.executeQuery(sql);
            while(rs.next()) cbox_Religion.addItem(rs.getString("religion"));
            
            sql = "SELECT CONCAT(value,' - ', descrition) AS hls_group FROM hls_group  ";
            rs = DBC.executeQuery(sql);
            while(rs.next()) cbox_Trible.addItem(rs.getString("hls_group"));
            
        } catch (SQLException ex) {
            Logger.getLogger(Frm_PatientMod.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private int getMaritalStatusIndex(String marital_status){
        
        if(marital_status==null || marital_status.equals("o")){
            return 3;
        }else if(marital_status.equals("m")) {
            return 0;
        } else if (marital_status.equals("d")) {
            return 1;
        }
            return 2;
        
    }

    /** 檢查病患編號是否存在*//*如存在顯示病患、聯絡人資料*/
    private void setPatientIndo(String p_no){
        ResultSet rs = null ;
        String sql = "";
        try {
            sql = "SELECT *, CONCAT(patients_info.tribe,' - ', hls_group.descrition) AS hls_group  ,CONCAT(patients_info.religion,' - ', religion.descrition) AS hls_religion  " +
                    "FROM patients_info LEFT JOIN hls_group ON  hls_group.value = patients_info.tribe " +
                    "LEFT JOIN religion ON religion.value = patients_info.religion  WHERE p_no ='"+ p_no +"' ";
             System.out.println(sql);
            rs = DBC.executeQuery(sql);
            if(rs.next()){
             
                String date = rs.getString("birth").substring(8, 10) + "-" +
                            rs.getString("birth").substring(5, 7) + "-" +
                            rs.getString("birth").substring(0, 4);

                this.txt_No.setText(rs.getString("p_no"));
                this.txt_FirstName.setText(rs.getString("firstname"));
                this.txt_LastName.setText(rs.getString("lastname"));
         
                this.txt_NhisNo.setText(rs.getString("nhis_no"));
                this.txt_NiaNo.setText(rs.getString("nia_no"));

                this.dateChooser1.setValue(date);
                
                this.txt_Height.setText(rs.getString("height"));
                this.txt_Weight.setText(rs.getString("weight"));
                this.cob_Gender.setSelectedItem(rs.getString("gender"));
                this.cob_Bloodtype.setSelectedItem(rs.getString("bloodtype"));
                this.cob_Rh.setSelectedItem(rs.getString("rh_type"));
                this.txt_Address.setText(rs.getString("address"));
                this.txt_Town.setText(rs.getString("town"));
                this.txt_State.setText(rs.getString("state"));
                this.txt_Country.setText(rs.getString("country"));
                this.txt_Phone1.setText(rs.getString("phone"));
                this.txt_Phone2.setText(rs.getString("cell_phone"));
                this.txt_Ps.setText(rs.getString("ps"));
                this.txt_Occupation.setText(rs.getString("occupation"));
                this.txt_Language.setText(rs.getString("language"));
                this.cob_MaritalStatus.setSelectedIndex(getMaritalStatusIndex(rs.getString("marital_status")));
                this.txt_PlaceOfBirth.setText(rs.getString("place_of_birth"));
                this.cbox_Trible.setSelectedItem(rs.getString("hls_group"));
                this.cbox_Religion.setSelectedItem(rs.getString("hls_religion"));
                if(rs.getString("dead_guid")!=null){
                    check_Deal.setSelected(true);
                    showDealTimer();
                }
                /*病患聯絡人資料*/
                sql = "SELECT * FROM contactperson_info WHERE guid = '" + rs.getString("cp_guid") + "'";
                rs = DBC.executeQuery(sql) ;
                if(rs.next()){
                    this.txt_EmeFirstName.setText(rs.getString("firstName"));
                    this.txt_EmeLastName.setText(rs.getString("lastName"));
                    this.cob_EmeGender.setSelectedItem(rs.getString("gender"));
                    this.txt_EmeOccupation.setText(rs.getString("occupation"));
                    this.txt_EmeLanguage.setText(rs.getString("language"));
                    this.cob_EmeMaritalStatus.setSelectedIndex(getMaritalStatusIndex(rs.getString("marital_status")));
                    this.txt_EmePlaceOfBirth.setText(rs.getString("place_of_birth"));
                    this.txt_EmePhone.setText(rs.getString("phone"));
                    this.txt_EmeTribe.setText(rs.getString("tribe"));
                    this.txt_EmeReligion.setText(rs.getString("religion"));
                    this.txt_EmeRelation.setText(rs.getString("relation"));
                    this.txt_EmeState.setText(rs.getString("state"));
                    this.txt_empCellPhone.setText(rs.getString("cell_phone"));
                    this.txta_EmeAddress.setText(rs.getString("address"));
                    this.txt_CPTown.setText(rs.getString("town"));
                    this.txt_EmeCountry.setText(rs.getString("country"));
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
            ErrorMessage.setData("Patients", "Frm_PatientMod" ,"setPatientIndo(String p_no)",
                    e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            initPatientInfo();
            initContact() ;
        } finally {
            try{ DBC.closeConnection(rs); }
            catch (SQLException e) {
                ErrorMessage.setData("Patients", "Frm_PatientMod" ,"setPatientIndo(String p_no) - DBC.closeConnection",
                    e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            }
        }
    }

    private void showDealTimer(){
        String sql = "SELECT * FROM death_info " +
                     "WHERE guid = (SELECT dead_guid FROM patients_info " +
                     "WHERE p_no = '"+this.txt_No.getText()+"')" ;
        try {
            ResultSet rs = DBC.executeQuery(sql);
            rs.next();
            this.check_Deal.setText(paragraph.getLanguage(line, "DEATHTIME")+"\n"+rs.getString("date_of_death").substring(0, 16));
        } catch (SQLException e) {
            ErrorMessage.setData("Patients", "Frm_PatientMod" ,"showDealTimer()",
                    e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            Logger.getLogger(Frm_PatientMod.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private boolean isCanSave(){
       
        if(!this.txt_FirstName.getText().equals("")
        && !this.txt_LastName.getText().equals("") && !txt_Phone2.getText().equals("")
        && this.txt_FirstName.getText() != null && txt_Phone2.getText() != null
        && this.txt_LastName.getText() != null ){
                return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialog1 = new javax.swing.JDialog();
        lab_date_of_death = new javax.swing.JLabel();
        dia_spi_Hour = new javax.swing.JSpinner();
        dia_spi_Min = new javax.swing.JSpinner();
        jScrollPane2 = new javax.swing.JScrollPane();
        dia_txt_Cause = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        btn_DealSave = new javax.swing.JButton();
        btn_DealCancel = new javax.swing.JButton();
        dia_cob_date = new cc.johnwu.date.DateComboBox();
        Pat_List = new javax.swing.JTabbedPane();
        pan_Left = new javax.swing.JPanel();
        pan_Noth = new javax.swing.JPanel();
        lab_TitleNo = new javax.swing.JLabel();
        txt_FirstName = new javax.swing.JFormattedTextField();
        lab_TitleFirstName = new javax.swing.JLabel();
        lab_TitleGender = new javax.swing.JLabel();
        cob_Gender = new javax.swing.JComboBox();
        lab_TitleLastName = new javax.swing.JLabel();
        txt_LastName = new javax.swing.JFormattedTextField();
        txt_NhisNo = new javax.swing.JFormattedTextField();
        lab_NhisNo = new javax.swing.JLabel();
        txt_No = new javax.swing.JTextField();
        lab_NiaNo = new javax.swing.JLabel();
        txt_NiaNo = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        dateChooser1 = new cc.johnwu.date.DateChooser();
        lab_TitleBirth = new javax.swing.JLabel();
        lab_PlaceOfBirth = new javax.swing.JLabel();
        txt_PlaceOfBirth = new javax.swing.JTextField();
        lab_MaritalStatus = new javax.swing.JLabel();
        cob_MaritalStatus = new javax.swing.JComboBox();
        lab_Occupation = new javax.swing.JLabel();
        txt_Occupation = new javax.swing.JTextField();
        check_Deal = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        txt_Phone2 = new javax.swing.JFormattedTextField();
        txt_Phone1 = new javax.swing.JFormattedTextField();
        txt_State = new javax.swing.JFormattedTextField();
        txt_Town = new javax.swing.JFormattedTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        txt_Ps = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        txt_Address = new javax.swing.JTextArea();
        lab_Ps = new javax.swing.JLabel();
        lab_TitlePhone2 = new javax.swing.JLabel();
        lab_TitlePhone1 = new javax.swing.JLabel();
        lab_TitleState = new javax.swing.JLabel();
        lab_TitleAddress = new javax.swing.JLabel();
        lab_TitleTown = new javax.swing.JLabel();
        txt_Country = new javax.swing.JTextField();
        lab_Country = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        fingerPrintViewer1 = new cc.johnwu.finger.FingerPrintViewer();
        btn_Enroll = new javax.swing.JButton();
        lab_Language = new javax.swing.JLabel();
        txt_Language = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        lab_Tribe = new javax.swing.JLabel();
        lab_Religion = new javax.swing.JLabel();
        cob_Bloodtype = new javax.swing.JComboBox();
        lab_TitleBloodtype = new javax.swing.JLabel();
        cob_Rh = new javax.swing.JComboBox();
        txt_Height = new javax.swing.JFormattedTextField();
        txt_Weight = new javax.swing.JFormattedTextField();
        lab_TitleWeight = new javax.swing.JLabel();
        lab_TitleHeight = new javax.swing.JLabel();
        cbox_Trible = new javax.swing.JComboBox();
        cbox_Religion = new javax.swing.JComboBox();
        pan_Right = new javax.swing.JPanel();
        lab_TitleCPTown = new javax.swing.JLabel();
        lab_TitleEmePhone = new javax.swing.JLabel();
        lab_TitleEmelastName = new javax.swing.JLabel();
        lab_TitleEmeRelation = new javax.swing.JLabel();
        lab_TitleEmeAddress = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txta_EmeAddress = new javax.swing.JTextArea();
        txt_EmeLastName = new javax.swing.JFormattedTextField();
        txt_EmeRelation = new javax.swing.JFormattedTextField();
        txt_EmePhone = new javax.swing.JFormattedTextField();
        txt_CPTown = new javax.swing.JFormattedTextField();
        lab_TitleEmeState = new javax.swing.JLabel();
        txt_EmeState = new javax.swing.JFormattedTextField();
        lab_TitleEmeFirstName = new javax.swing.JLabel();
        txt_EmeFirstName = new javax.swing.JFormattedTextField();
        lab_TitleEmeCountry = new javax.swing.JLabel();
        txt_EmeCountry = new javax.swing.JFormattedTextField();
        txt_EmeTribe = new javax.swing.JTextField();
        txt_EmePlaceOfBirth = new javax.swing.JTextField();
        txt_EmeReligion = new javax.swing.JTextField();
        txt_EmeOccupation = new javax.swing.JTextField();
        lab_EmeOccupation = new javax.swing.JLabel();
        lab_EmeMaritalStatus = new javax.swing.JLabel();
        lab_EmePlaceOfBirth = new javax.swing.JLabel();
        lab_EmeTribe = new javax.swing.JLabel();
        lab_EmeReligion = new javax.swing.JLabel();
        lab_EmeGender = new javax.swing.JLabel();
        cob_EmeGender = new javax.swing.JComboBox();
        cob_EmeMaritalStatus = new javax.swing.JComboBox();
        lab_EmeLanguage = new javax.swing.JLabel();
        txt_EmeLanguage = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txt_empCellPhone = new javax.swing.JTextField();
        pan_BtnGrp = new javax.swing.JPanel();
        btn_Cancel = new javax.swing.JButton();
        btn_OK = new javax.swing.JButton();
        bar_menu = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        mnit_XRayHistory = new javax.swing.JMenuItem();
        mnit_LabHistory = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();

        jDialog1.setTitle("Death records");
        jDialog1.setMinimumSize(new java.awt.Dimension(407, 181));
        jDialog1.setModal(true);
        jDialog1.setResizable(false);
        jDialog1.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                jDialog1WindowClosing(evt);
            }
        });

        lab_date_of_death.setText("Datetime :");

        dia_spi_Hour.setModel(new javax.swing.SpinnerNumberModel(0, 0, 23, 1));

        dia_spi_Min.setModel(new javax.swing.SpinnerNumberModel(0, 0, 59, 1));

        dia_txt_Cause.setColumns(20);
        dia_txt_Cause.setRows(5);
        jScrollPane2.setViewportView(dia_txt_Cause);

        jLabel2.setText("Cause :");

        btn_DealSave.setText("Save");
        btn_DealSave.setPreferredSize(new java.awt.Dimension(100, 29));
        btn_DealSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_DealSaveActionPerformed(evt);
            }
        });

        btn_DealCancel.setText("Cancel");
        btn_DealCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_DealCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jDialog1Layout.createSequentialGroup()
                        .addComponent(btn_DealCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_DealSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jDialog1Layout.createSequentialGroup()
                        .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lab_date_of_death)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, 0, 0, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jDialog1Layout.createSequentialGroup()
                                .addComponent(dia_cob_date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dia_spi_Hour, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dia_spi_Min, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(lab_date_of_death)
                    .addComponent(dia_spi_Min, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dia_spi_Hour, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dia_cob_date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDialog1Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_DealSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_DealCancel)))
                    .addComponent(jLabel2))
                .addContainerGap(29, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("New Patient");

        pan_Left.setBackground(new java.awt.Color(234, 234, 234));
        pan_Left.setBorder(javax.swing.BorderFactory.createTitledBorder("Patient Information"));
        pan_Left.setPreferredSize(new java.awt.Dimension(383, 402));

        lab_TitleNo.setText("Patient No. :");

        txt_FirstName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_FirstNameKeyReleased(evt);
            }
        });

        lab_TitleFirstName.setText("First Name :");

        lab_TitleGender.setText("Gender :");

        cob_Gender.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "M", "F" }));
        cob_Gender.setRequestFocusEnabled(false);

        lab_TitleLastName.setText("Last Name :");

        txt_LastName.setHighlighter(null);
        txt_LastName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_LastNameKeyReleased(evt);
            }
        });

        lab_NhisNo.setText("NHIS No. :");

        txt_No.setText("00000000");
        txt_No.setEnabled(false);

        lab_NiaNo.setText("NIA No. :");

        javax.swing.GroupLayout pan_NothLayout = new javax.swing.GroupLayout(pan_Noth);
        pan_Noth.setLayout(pan_NothLayout);
        pan_NothLayout.setHorizontalGroup(
            pan_NothLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan_NothLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan_NothLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lab_TitleLastName, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lab_TitleFirstName, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lab_NiaNo, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lab_TitleNo, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lab_NhisNo, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(24, 24, 24)
                .addGroup(pan_NothLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_NiaNo, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE)
                    .addComponent(txt_NhisNo, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE)
                    .addComponent(txt_FirstName, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE)
                    .addGroup(pan_NothLayout.createSequentialGroup()
                        .addComponent(txt_No, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE)
                        .addGap(7, 7, 7)
                        .addComponent(lab_TitleGender)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cob_Gender, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txt_LastName, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE)))
        );
        pan_NothLayout.setVerticalGroup(
            pan_NothLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan_NothLayout.createSequentialGroup()
                .addGroup(pan_NothLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab_TitleNo)
                    .addComponent(txt_No, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_TitleGender)
                    .addComponent(cob_Gender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_NothLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                    .addComponent(txt_FirstName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_TitleFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_NothLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab_TitleLastName, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_LastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_NothLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_NhisNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_NhisNo, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pan_NothLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab_NiaNo)
                    .addComponent(txt_NiaNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        lab_TitleBirth.setText("Date Of Birth :");

        lab_PlaceOfBirth.setText("Place Of Birth :");

        lab_MaritalStatus.setText("Marital Status :");

        cob_MaritalStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Married", "Divorced", "Single", "Other" }));

        lab_Occupation.setText("Occupation :");

        check_Deal.setText("Dead");
        check_Deal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                check_DealActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lab_Occupation, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lab_TitleBirth, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lab_PlaceOfBirth, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lab_MaritalStatus, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_Occupation, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)
                    .addComponent(cob_MaritalStatus, 0, 292, Short.MAX_VALUE)
                    .addComponent(txt_PlaceOfBirth, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(dateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 119, Short.MAX_VALUE)
                        .addComponent(check_Deal)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lab_TitleBirth, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(dateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(check_Deal)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                    .addComponent(lab_PlaceOfBirth)
                    .addComponent(txt_PlaceOfBirth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab_MaritalStatus)
                    .addComponent(cob_MaritalStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_Occupation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_Occupation))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        txt_Phone2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_Phone2KeyReleased(evt);
            }
        });

        txt_Ps.setColumns(20);
        txt_Ps.setLineWrap(true);
        txt_Ps.setRows(3);
        jScrollPane4.setViewportView(txt_Ps);

        txt_Address.setColumns(20);
        txt_Address.setLineWrap(true);
        txt_Address.setRows(3);
        jScrollPane3.setViewportView(txt_Address);

        lab_Ps.setText("P.S. :");

        lab_TitlePhone2.setText("*Cell Phone :");

        lab_TitlePhone1.setText("Phone :");

        lab_TitleState.setText("Region :");

        lab_TitleAddress.setText("Address :");

        lab_TitleTown.setText("City/Town :");

        lab_Country.setText("Country :");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Fingerprint"));

        fingerPrintViewer1.setMaximumSize(new java.awt.Dimension(100, 150));
        fingerPrintViewer1.setNormalBounds(new java.awt.Rectangle(0, 0, 120, 180));
        fingerPrintViewer1.setVisible(true);

        javax.swing.GroupLayout fingerPrintViewer1Layout = new javax.swing.GroupLayout(fingerPrintViewer1.getContentPane());
        fingerPrintViewer1.getContentPane().setLayout(fingerPrintViewer1Layout);
        fingerPrintViewer1Layout.setHorizontalGroup(
            fingerPrintViewer1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 78, Short.MAX_VALUE)
        );
        fingerPrintViewer1Layout.setVerticalGroup(
            fingerPrintViewer1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 107, Short.MAX_VALUE)
        );

        btn_Enroll.setText("Enroll");
        btn_Enroll.setMaximumSize(new java.awt.Dimension(120, 29));
        btn_Enroll.setMinimumSize(new java.awt.Dimension(120, 29));
        btn_Enroll.setPreferredSize(new java.awt.Dimension(120, 29));
        btn_Enroll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_EnrollActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(fingerPrintViewer1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Enroll, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(140, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(fingerPrintViewer1, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_Enroll, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        lab_Language.setText("Language :");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lab_Country, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lab_TitleAddress, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lab_TitleTown, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lab_TitlePhone1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lab_TitleState, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lab_Ps, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lab_TitlePhone2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lab_Language, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_State, javax.swing.GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)
                            .addComponent(txt_Phone1, javax.swing.GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)
                            .addComponent(txt_Town, javax.swing.GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)
                            .addComponent(txt_Language, javax.swing.GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)
                            .addComponent(txt_Phone2, javax.swing.GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)
                            .addComponent(txt_Country, javax.swing.GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab_Language)
                    .addComponent(txt_Language, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab_TitlePhone1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_Phone1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab_TitlePhone2, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_Phone2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab_TitleTown, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_Town, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab_TitleState, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_State, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_Country, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_Country))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lab_TitleAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lab_Ps, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        lab_Tribe.setText("Tribe :");

        lab_Religion.setText("Religion :");

        cob_Bloodtype.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "A", "B", "AB", "O" }));

        lab_TitleBloodtype.setText("Blood/RH :");

        cob_Rh.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "+", "-" }));

        lab_TitleWeight.setText("Weight :");

        lab_TitleHeight.setText("Height :");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lab_Tribe, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lab_Religion, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lab_TitleWeight, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lab_TitleBloodtype, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lab_TitleHeight, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_Weight, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE)
                    .addComponent(txt_Height, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(cob_Bloodtype, 0, 152, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cob_Rh, 0, 152, Short.MAX_VALUE))
                    .addComponent(cbox_Trible, 0, 310, Short.MAX_VALUE)
                    .addComponent(cbox_Religion, 0, 310, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cob_Bloodtype, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cob_Rh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_TitleBloodtype))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_Height, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_TitleHeight, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_Weight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_TitleWeight, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab_Tribe)
                    .addComponent(cbox_Trible, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab_Religion)
                    .addComponent(cbox_Religion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout pan_LeftLayout = new javax.swing.GroupLayout(pan_Left);
        pan_Left.setLayout(pan_LeftLayout);
        pan_LeftLayout.setHorizontalGroup(
            pan_LeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_LeftLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan_LeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pan_Noth, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(12, 12, 12))
        );
        pan_LeftLayout.setVerticalGroup(
            pan_LeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_LeftLayout.createSequentialGroup()
                .addGroup(pan_LeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan_LeftLayout.createSequentialGroup()
                        .addComponent(pan_Noth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21))
        );

        Pat_List.addTab("Patient Infrmation", pan_Left);

        pan_Right.setBackground(new java.awt.Color(234, 234, 234));
        pan_Right.setBorder(javax.swing.BorderFactory.createTitledBorder("Contact Person"));
        pan_Right.setPreferredSize(new java.awt.Dimension(383, 402));

        lab_TitleCPTown.setText("Town :");

        lab_TitleEmePhone.setText("Phone :");

        lab_TitleEmelastName.setText("Last Name :");

        lab_TitleEmeRelation.setText("Releation :");

        lab_TitleEmeAddress.setText("Address :");

        txta_EmeAddress.setColumns(20);
        txta_EmeAddress.setLineWrap(true);
        txta_EmeAddress.setRows(3);
        jScrollPane1.setViewportView(txta_EmeAddress);

        lab_TitleEmeState.setText("Region :");

        lab_TitleEmeFirstName.setText("First Name :");

        lab_TitleEmeCountry.setText("Country :");

        lab_EmeOccupation.setText("Occupation :");

        lab_EmeMaritalStatus.setText("Marital Status :");

        lab_EmePlaceOfBirth.setText("Place Of Birth :");

        lab_EmeTribe.setText("Tribe :");

        lab_EmeReligion.setText("Religion :");

        lab_EmeGender.setText("Gender :");

        cob_EmeGender.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "M", "F" }));
        cob_EmeGender.setRequestFocusEnabled(false);

        cob_EmeMaritalStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Married", "Divorced", "Single", "Other" }));

        lab_EmeLanguage.setText("Language");

        jLabel1.setText("Cell Phone:");

        javax.swing.GroupLayout pan_RightLayout = new javax.swing.GroupLayout(pan_Right);
        pan_Right.setLayout(pan_RightLayout);
        pan_RightLayout.setHorizontalGroup(
            pan_RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_RightLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan_RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lab_EmePlaceOfBirth, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lab_EmeTribe, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lab_TitleEmeAddress, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lab_TitleEmePhone, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lab_TitleEmelastName, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lab_TitleCPTown, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lab_EmeOccupation, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lab_EmeMaritalStatus, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lab_TitleEmeFirstName, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lab_EmeGender, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lab_EmeLanguage, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lab_EmeReligion, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(12, 12, 12)
                .addGroup(pan_RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 672, Short.MAX_VALUE)
                    .addComponent(txt_empCellPhone, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 672, Short.MAX_VALUE)
                    .addComponent(txt_EmeLanguage, javax.swing.GroupLayout.DEFAULT_SIZE, 672, Short.MAX_VALUE)
                    .addComponent(cob_EmeMaritalStatus, 0, 672, Short.MAX_VALUE)
                    .addComponent(txt_EmeLastName, javax.swing.GroupLayout.DEFAULT_SIZE, 672, Short.MAX_VALUE)
                    .addComponent(txt_EmeReligion, javax.swing.GroupLayout.DEFAULT_SIZE, 672, Short.MAX_VALUE)
                    .addComponent(txt_EmeOccupation, javax.swing.GroupLayout.DEFAULT_SIZE, 672, Short.MAX_VALUE)
                    .addComponent(txt_EmeTribe, javax.swing.GroupLayout.DEFAULT_SIZE, 672, Short.MAX_VALUE)
                    .addComponent(txt_EmePlaceOfBirth, javax.swing.GroupLayout.DEFAULT_SIZE, 672, Short.MAX_VALUE)
                    .addComponent(txt_EmeFirstName, javax.swing.GroupLayout.DEFAULT_SIZE, 672, Short.MAX_VALUE)
                    .addGroup(pan_RightLayout.createSequentialGroup()
                        .addComponent(txt_CPTown, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lab_TitleEmeState)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_EmeState, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lab_TitleEmeCountry)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_EmeCountry, javax.swing.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pan_RightLayout.createSequentialGroup()
                        .addComponent(cob_EmeGender, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lab_TitleEmeRelation)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_EmeRelation, javax.swing.GroupLayout.DEFAULT_SIZE, 560, Short.MAX_VALUE))
                    .addComponent(txt_EmePhone, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 672, Short.MAX_VALUE))
                .addContainerGap())
        );
        pan_RightLayout.setVerticalGroup(
            pan_RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_RightLayout.createSequentialGroup()
                .addGroup(pan_RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab_TitleEmeFirstName)
                    .addComponent(txt_EmeFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab_TitleEmelastName)
                    .addComponent(txt_EmeLastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab_EmeGender)
                    .addComponent(cob_EmeGender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_TitleEmeRelation)
                    .addComponent(txt_EmeRelation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_EmePlaceOfBirth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_EmePlaceOfBirth))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab_TitleEmePhone)
                    .addComponent(txt_EmePhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txt_empCellPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab_EmeMaritalStatus)
                    .addComponent(cob_EmeMaritalStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_EmeOccupation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_EmeOccupation))
                .addGap(8, 8, 8)
                .addGroup(pan_RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_EmeLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_EmeLanguage))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_EmeTribe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_EmeTribe))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_EmeReligion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_EmeReligion))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan_RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txt_CPTown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txt_EmeCountry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lab_TitleEmeCountry)
                        .addComponent(lab_TitleEmeState)
                        .addComponent(txt_EmeState, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lab_TitleCPTown))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lab_TitleEmeAddress)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE))
                .addContainerGap())
        );

        Pat_List.addTab("Contract Person", pan_Right);

        btn_Cancel.setText("Cancel");
        btn_Cancel.setMaximumSize(new java.awt.Dimension(60, 29));
        btn_Cancel.setMinimumSize(new java.awt.Dimension(60, 29));
        btn_Cancel.setPreferredSize(new java.awt.Dimension(40, 29));
        btn_Cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CancelActionPerformed(evt);
            }
        });

        btn_OK.setText("Save");
        btn_OK.setMaximumSize(new java.awt.Dimension(60, 29));
        btn_OK.setMinimumSize(new java.awt.Dimension(60, 29));
        btn_OK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_OKActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pan_BtnGrpLayout = new javax.swing.GroupLayout(pan_BtnGrp);
        pan_BtnGrp.setLayout(pan_BtnGrpLayout);
        pan_BtnGrpLayout.setHorizontalGroup(
            pan_BtnGrpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan_BtnGrpLayout.createSequentialGroup()
                .addContainerGap(582, Short.MAX_VALUE)
                .addComponent(btn_OK, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Cancel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pan_BtnGrpLayout.setVerticalGroup(
            pan_BtnGrpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_BtnGrpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btn_Cancel, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btn_OK, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jMenu1.setText("File");
        jMenu1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu1ActionPerformed(evt);
            }
        });

        jMenuItem1.setText("Drug Allergy");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("Medical Record");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        mnit_XRayHistory.setText("Radiology(X-RAY) Record");
        mnit_XRayHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnit_XRayHistoryActionPerformed(evt);
            }
        });
        jMenu1.add(mnit_XRayHistory);

        mnit_LabHistory.setText("Laboratory Record");
        mnit_LabHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnit_LabHistoryActionPerformed(evt);
            }
        });
        jMenu1.add(mnit_LabHistory);

        jMenuItem3.setText("Cashier Record");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        bar_menu.add(jMenu1);

        setJMenuBar(bar_menu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pan_BtnGrp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Pat_List, javax.swing.GroupLayout.DEFAULT_SIZE, 793, Short.MAX_VALUE)
                        .addGap(15, 15, 15))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Pat_List, javax.swing.GroupLayout.DEFAULT_SIZE, 538, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pan_BtnGrp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_DealSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_DealSaveActionPerformed
        String uuid = UUID.randomUUID().toString();
        String sql = "";
        try {
            sql = "INSERT INTO death_info VALUES( " +
                "'"+uuid+"', " +
                "'" + this.dia_cob_date.getValue()+" "+this.dia_spi_Hour.getValue().toString()+":"+this.dia_spi_Min.getValue().toString()+"', " +
                "'"+this.dia_txt_Cause.getText()+"')";
            DBC.executeUpdate(sql);
            sql = "UPDATE patients_info SET dead_guid = '"+uuid+"' WHERE p_no = '"+this.txt_No.getText()+"'";
            DBC.executeUpdate(sql);
        } catch (SQLException e) {
            ErrorMessage.setData("Patients", "Frm_PatientMod" ,"btn_DealSaveActionPerformed(java.awt.event.ActionEvent evt)",
                   e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            System.out.println(e);
        }
        this.jDialog1.setVisible(false);
    }//GEN-LAST:event_btn_DealSaveActionPerformed

    private void btn_DealCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_DealCancelActionPerformed
        this.jDialog1.setVisible(false);
        ResultSet rs = null;
        String sql  = "";
        try{
            sql = "DELETE FROM death_info WHERE guid = (SELECT dead_guid FROM patients_info WHERE p_no = '" + this.txt_No.getText() +"')";
            DBC.executeUpdate(sql) ;
            sql = "UPDATE patients_info SET dead_guid = NULL WHERE  p_no = '" + this.txt_No.getText() +"'";
            DBC.executeUpdate(sql) ;
            this.check_Deal.setSelected(false);
        }catch(SQLException e){
            ErrorMessage.setData("Patients", "Frm_PatientMod" ,"btn_DealCancelActionPerformed(java.awt.event.ActionEvent evt)",
                   e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        }
    }//GEN-LAST:event_btn_DealCancelActionPerformed

    private void jDialog1WindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_jDialog1WindowClosing
        this.jDialog1.setVisible(false);
        ResultSet rs = null;
        String sql  = "SELECT guid FROM death_info " +
                      "WHERE guid = " +
                      "(SELECT dead_guid FROM patients_info " +
                      "WHERE p_no = '" + this.txt_No.getText() +"')";
        try{
            rs = DBC.executeQuery(sql) ;
            if(rs.next()){
                this.check_Deal.setSelected(true);
            }else{
                this.check_Deal.setSelected(false);
            }
        }catch(SQLException e){
            ErrorMessage.setData("Patients", "Frm_PatientMod" ,"jDialog1WindowClosing(java.awt.event.WindowEvent evt)",
                   e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        }
    }//GEN-LAST:event_jDialog1WindowClosing

    private void btn_OKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_OKActionPerformed
        String sql = "";
        ResultSet rs = null;

        try {
            String trible[] = this.cbox_Trible.getSelectedItem().toString().split(" - ");
            String religion[] = this.cbox_Religion.getSelectedItem().toString().split(" - ");
            sql = "UPDATE patients_info SET "+
                    "nhis_no = '"+this.txt_NhisNo.getText()+"', " +
                    "nia_no = '"+this.txt_NiaNo.getText()+"', " +
                    "firstname = '"+this.txt_FirstName.getText()+"', " +
                    "lastname = '"+this.txt_LastName.getText()+"', " +
                    "birth = '"+this.dateChooser1.getValue()+"', " +
                    "gender = '"+this.cob_Gender.getSelectedItem().toString()+"', " +
                    "bloodtype = '"+this.cob_Bloodtype.getSelectedItem().toString()+"', " +
                    "rh_type = '"+this.cob_Rh.getSelectedItem().toString()+"', " +
                    "height = '"+this.txt_Height.getText()+"', " +
                    "weight = '"+this.txt_Weight.getText()+"', " +
                    "phone = '"+this.txt_Phone1.getText()+"', " +
                    "cell_phone = '"+this.txt_Phone2.getText()+"', " +
                    "town = '"+this.txt_Town.getText()+"', " +
                    "state = '"+this.txt_State.getText()+"', " +
                    "country = '"+this.txt_Country.getText()+"', " +
                    "address = '"+this.txt_Address.getText()+"', " +
                    "occupation = '"+this.txt_Occupation.getText()+"', " +
                    "language = '"+this.txt_Language.getText()+"', " +
                    "marital_status = '"+this.m_MaritalStatus[this.cob_MaritalStatus.getSelectedIndex()]+"', " +
                    "place_of_birth = '"+this.txt_PlaceOfBirth.getText()+"', " +
                    "tribe = '"+ trible[0]+"', " +
                    "religion = '"+religion[0]+"', " +
                    "ps = '"+this.txt_Ps.getText()+"', " +
                    "exist = true " +
                    "WHERE p_no = '"+this.txt_No.getText()+"' ";
  
            DBC.executeUpdate(sql);
            cc.johnwu.login.ChangesLog.ChangesLog("patients_info", this.txt_No.getText(), "add");
            /*新增或修改聯絡人資料*/
            if(!txt_EmeRelation.getText().trim().equals("")
                    || !this.txt_EmeFirstName.getText().trim().equals("")
                    || !this.txt_EmeLastName.getText().trim().equals("")){
                sql = "SELECT * FROM contactperson_info	LEFT JOIN patients_info " +
                        "ON contactperson_info.guid = patients_info.cp_guid " +
                        "WHERE patients_info.p_no = '" + this.txt_No.getText() + "'" ;
                rs = DBC.executeQuery(sql) ;
                if(rs.next()){
                    sql = "UPDATE contactperson_info SET "+
                            "firstName = '" + this.txt_EmeFirstName.getText() + "', " +
                            "lastName = '" + this.txt_EmeLastName.getText() + "', " +
                            "gender = '" + this.cob_EmeGender.getSelectedItem().toString() + "', " +
                            "phone = '" + this.txt_EmePhone.getText() + "', " +
                            "cell_phone = '"+txt_empCellPhone.getText()+"', " +
                            "town = '" + this.txt_CPTown.getText() + "', " +
                            "state = '" + this.txt_EmeState.getText() + "', " +
                            "country = '" + this.txt_EmeCountry.getText() + "', " +
                            "address = '" + this.txta_EmeAddress.getText() + "', " +
                            "occupation = '"+this.txt_EmeOccupation.getText()+"', " +
                            "language = '"+this.txt_EmeLanguage.getText()+"', " +
                            "marital_status = '"+this.m_MaritalStatus[this.cob_EmeMaritalStatus.getSelectedIndex()]+"', " +
                            "place_of_birth = '"+this.txt_EmePlaceOfBirth.getText()+"', " +
                            "tribe = '"+this.txt_EmeTribe.getText()+"', " +
                            "religion = '"+this.txt_EmeReligion.getText()+"', " +
                            "relation = '" + this.txt_EmeRelation.getText() + "' " +
                            "WHERE guid = " +
                            "(SELECT cp_guid FROM patients_info WHERE p_no = '"+this.txt_No.getText()+"') ";
                }else{
                    String uuid = UUID.randomUUID().toString();
                    sql = "INSERT INTO contactperson_info " +
                            "(guid,firstName,lastName,gender,phone,town,state,country,address,occupation,language,marital_status,place_of_birth,tribe,religion,relation) " +
                            "VALUES ('" + uuid + "','"+
                            this.txt_EmeFirstName.getText() + "','" +
                            this.txt_EmeLastName.getText() + "','" +
                            this.cob_EmeGender.getSelectedItem().toString() + "','" +
                            this.txt_EmePhone.getText() + "','" +
                            this.txt_CPTown.getText() + "','" +
                            this.txt_EmeState.getText() + "','" +
                            this.txt_EmeCountry.getText() + "','" +
                            this.txta_EmeAddress.getText() + "','" +
                            this.txt_EmeOccupation.getText() + "','" +
                            this.txt_EmeLanguage.getText() + "','" +

                            this.m_MaritalStatus[this.cob_EmeMaritalStatus.getSelectedIndex()] + "','" +
                            this.txt_EmePlaceOfBirth.getText() + "','" +
                            this.txt_EmeTribe.getText() + "','" +
                            this.txt_EmeReligion.getText() + "','" +
                            this.txt_EmeRelation.getText() +"' )" ;
                    DBC.executeUpdate(sql) ;
                    sql = "UPDATE patients_info SET cp_guid = '" + uuid + "' " +
                            "WHERE p_no = '" + this.txt_No.getText() + "'" ;

                }
                DBC.executeUpdate(sql) ;
            }
            //***********************列印barcode
            if (this.m_Status.equals("NEW")) {
                PrintBarcode.PrintBarcode(txt_No.getText());
            }
            
            //************************
            JOptionPane.showMessageDialog(new Frame(), paragraph.getLanguage(message, "SAVECOMPLETE"));
            btn_CancelActionPerformed(null);
            m_frame.onPatientMod(this.txt_No.getText());
        } catch (SQLException e) {
            ErrorMessage.setData("Patients", "Frm_PatientMod" ,"btn_OKActionPerformed(java.awt.event.ActionEvent evt)",
                    e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            System.out.println(e);
        }
}//GEN-LAST:event_btn_OKActionPerformed

    private void btn_CancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CancelActionPerformed
        try {
            String sql = "DELETE FROM patients_info WHERE firstname = '"+m_UUID+"' AND exist = false";
            DBC.executeUpdate(sql);
        } catch (SQLException e) {
            ErrorMessage.setData("Patients", "Frm_PatientMod" ,"btn_CancelActionPerformed(java.awt.event.ActionEvent evt)",
                    e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            System.out.println(e);
        }
        if(m_frame!=null) m_frame.reLoad();
        dispose();
}//GEN-LAST:event_btn_CancelActionPerformed

    private void btn_EnrollActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_EnrollActionPerformed
        if(this.txt_No.getText().equals("")){
            btn_OKActionPerformed(null);
        }
        try{
            PreparedStatement pstmt = DBC.prepareStatement("INSERT INTO fingertemplate VALUES(?, ?, ?)");
            FingerPrintScanner.enroll(this.txt_No.getText(),pstmt);
            JOptionPane.showMessageDialog(new Frame(),"Saved successfully.");

        } catch (SQLException e) {
            ErrorMessage.setData("Patients", "Frm_PatientMod" ,"btn_EnrollActionPerformed(java.awt.event.ActionEvent evt)",
                    e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            System.out.println(e);
        }
}//GEN-LAST:event_btn_EnrollActionPerformed

    private void check_DealActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_check_DealActionPerformed
        Point p = this.getLocation();
        ResultSet rs = null;
        String sql = null ,timer = null;
        int x = p.x+(this.getWidth()-jDialog1.getWidth())/2;
        int y = p.y+(this.getHeight()-jDialog1.getHeight())/2;
        this.jDialog1.setLocation(x, y);
        // death_info
        try{
            sql = "SELECT * FROM death_info " +
                    "WHERE guid = (SELECT dead_guid FROM patients_info " +
                    "WHERE p_no = '"+this.txt_No.getText()+"')" ;
            rs = DBC.executeQuery(sql) ;
            if(rs.next()){
                this.check_Deal.setSelected(true);
                this.dia_txt_Cause.setText(rs.getString("cause"));
                timer = rs.getString("date_of_death").substring(0, 10);
                this.dia_cob_date.setValue(timer);
                int time =Integer.parseInt(rs.getString("date_of_death").substring(11, 13));
                this.dia_spi_Hour.setValue(time);
                time = Integer.parseInt(rs.getString("date_of_death").substring(14, 16));
                this.dia_spi_Min.setValue(time);
            }else{
                this.btn_DealSave.setEnabled(true);
            }
        }catch(SQLException e){
            ErrorMessage.setData("Patients", "Frm_PatientMod" ,"check_DealActionPerformed(java.awt.event.ActionEvent evt)",
                    e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            System.out.println(e) ;
        }
        this.jDialog1.setVisible(true);
}//GEN-LAST:event_check_DealActionPerformed

    private void txt_LastNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_LastNameKeyReleased
        this.btn_OK.setEnabled(isCanSave());
}//GEN-LAST:event_txt_LastNameKeyReleased

    private void txt_FirstNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_FirstNameKeyReleased
        this.btn_OK.setEnabled(isCanSave());
}//GEN-LAST:event_txt_FirstNameKeyReleased

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        new Diagnosis.Frm_DiagnosisAllergy(this, this.txt_No.getText(), this.txt_FirstName.getText()+" "+this.txt_LastName.getText()).setVisible(true);
        this.setEnabled(false);
      
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void mnit_LabHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnit_LabHistoryActionPerformed
        this.setEnabled(false);
        new Frm_LabHistory(this, this.txt_No.getText()).setVisible(true);
     
}//GEN-LAST:event_mnit_LabHistoryActionPerformed

    private void mnit_XRayHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnit_XRayHistoryActionPerformed
        this.setEnabled(false);
        new Frm_RadiologyHistory(this, this.txt_No.getText()).setVisible(true);
      
}//GEN-LAST:event_mnit_XRayHistoryActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        new Diagnosis.Frm_DiagnosisDiagnostic(this, this.txt_No.getText(), this.txt_FirstName.getText()+" "+this.txt_LastName.getText()).setVisible(true);
        this.setEnabled(false);
      
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        this.setEnabled(false);
        new Frm_CashierHistory(this,txt_No.getText().trim()).setVisible(true);
   
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenu1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu1ActionPerformed

    }//GEN-LAST:event_jMenu1ActionPerformed

    private void txt_Phone2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_Phone2KeyReleased
        this.btn_OK.setEnabled(isCanSave());
    }//GEN-LAST:event_txt_Phone2KeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane Pat_List;
    private javax.swing.JMenuBar bar_menu;
    private javax.swing.JButton btn_Cancel;
    private javax.swing.JButton btn_DealCancel;
    private javax.swing.JButton btn_DealSave;
    private javax.swing.JButton btn_Enroll;
    private javax.swing.JButton btn_OK;
    private javax.swing.JComboBox cbox_Religion;
    private javax.swing.JComboBox cbox_Trible;
    private javax.swing.JCheckBox check_Deal;
    private javax.swing.JComboBox cob_Bloodtype;
    private javax.swing.JComboBox cob_EmeGender;
    private javax.swing.JComboBox cob_EmeMaritalStatus;
    private javax.swing.JComboBox cob_Gender;
    private javax.swing.JComboBox cob_MaritalStatus;
    private javax.swing.JComboBox cob_Rh;
    private cc.johnwu.date.DateChooser dateChooser1;
    private cc.johnwu.date.DateComboBox dia_cob_date;
    private javax.swing.JSpinner dia_spi_Hour;
    private javax.swing.JSpinner dia_spi_Min;
    private javax.swing.JTextArea dia_txt_Cause;
    private cc.johnwu.finger.FingerPrintViewer fingerPrintViewer1;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel lab_Country;
    private javax.swing.JLabel lab_EmeGender;
    private javax.swing.JLabel lab_EmeLanguage;
    private javax.swing.JLabel lab_EmeMaritalStatus;
    private javax.swing.JLabel lab_EmeOccupation;
    private javax.swing.JLabel lab_EmePlaceOfBirth;
    private javax.swing.JLabel lab_EmeReligion;
    private javax.swing.JLabel lab_EmeTribe;
    private javax.swing.JLabel lab_Language;
    private javax.swing.JLabel lab_MaritalStatus;
    private javax.swing.JLabel lab_NhisNo;
    private javax.swing.JLabel lab_NiaNo;
    private javax.swing.JLabel lab_Occupation;
    private javax.swing.JLabel lab_PlaceOfBirth;
    private javax.swing.JLabel lab_Ps;
    private javax.swing.JLabel lab_Religion;
    private javax.swing.JLabel lab_TitleAddress;
    private javax.swing.JLabel lab_TitleBirth;
    private javax.swing.JLabel lab_TitleBloodtype;
    private javax.swing.JLabel lab_TitleCPTown;
    private javax.swing.JLabel lab_TitleEmeAddress;
    private javax.swing.JLabel lab_TitleEmeCountry;
    private javax.swing.JLabel lab_TitleEmeFirstName;
    private javax.swing.JLabel lab_TitleEmePhone;
    private javax.swing.JLabel lab_TitleEmeRelation;
    private javax.swing.JLabel lab_TitleEmeState;
    private javax.swing.JLabel lab_TitleEmelastName;
    private javax.swing.JLabel lab_TitleFirstName;
    private javax.swing.JLabel lab_TitleGender;
    private javax.swing.JLabel lab_TitleHeight;
    private javax.swing.JLabel lab_TitleLastName;
    private javax.swing.JLabel lab_TitleNo;
    private javax.swing.JLabel lab_TitlePhone1;
    private javax.swing.JLabel lab_TitlePhone2;
    private javax.swing.JLabel lab_TitleState;
    private javax.swing.JLabel lab_TitleTown;
    private javax.swing.JLabel lab_TitleWeight;
    private javax.swing.JLabel lab_Tribe;
    private javax.swing.JLabel lab_date_of_death;
    private javax.swing.JMenuItem mnit_LabHistory;
    private javax.swing.JMenuItem mnit_XRayHistory;
    private javax.swing.JPanel pan_BtnGrp;
    private javax.swing.JPanel pan_Left;
    private javax.swing.JPanel pan_Noth;
    private javax.swing.JPanel pan_Right;
    private javax.swing.JTextArea txt_Address;
    private javax.swing.JFormattedTextField txt_CPTown;
    private javax.swing.JTextField txt_Country;
    private javax.swing.JFormattedTextField txt_EmeCountry;
    private javax.swing.JFormattedTextField txt_EmeFirstName;
    private javax.swing.JTextField txt_EmeLanguage;
    private javax.swing.JFormattedTextField txt_EmeLastName;
    private javax.swing.JTextField txt_EmeOccupation;
    private javax.swing.JFormattedTextField txt_EmePhone;
    private javax.swing.JTextField txt_EmePlaceOfBirth;
    private javax.swing.JFormattedTextField txt_EmeRelation;
    private javax.swing.JTextField txt_EmeReligion;
    private javax.swing.JFormattedTextField txt_EmeState;
    private javax.swing.JTextField txt_EmeTribe;
    private javax.swing.JFormattedTextField txt_FirstName;
    private javax.swing.JFormattedTextField txt_Height;
    private javax.swing.JTextField txt_Language;
    private javax.swing.JFormattedTextField txt_LastName;
    private javax.swing.JFormattedTextField txt_NhisNo;
    private javax.swing.JTextField txt_NiaNo;
    private javax.swing.JTextField txt_No;
    private javax.swing.JTextField txt_Occupation;
    private javax.swing.JFormattedTextField txt_Phone1;
    private javax.swing.JFormattedTextField txt_Phone2;
    private javax.swing.JTextField txt_PlaceOfBirth;
    private javax.swing.JTextArea txt_Ps;
    private javax.swing.JFormattedTextField txt_State;
    private javax.swing.JFormattedTextField txt_Town;
    private javax.swing.JFormattedTextField txt_Weight;
    private javax.swing.JTextField txt_empCellPhone;
    private javax.swing.JTextArea txta_EmeAddress;
    // End of variables declaration//GEN-END:variables
  
    @Override
    public void showImage(BufferedImage bufferedimage,String msg) {
        this.fingerPrintViewer1.showImage(bufferedimage);
        this.fingerPrintViewer1.setTitle(msg);
    }

    @Override
    public void onFingerDown() {
        this.btn_Enroll.setEnabled(isCanSave());
    }

    @Override
    public void onDateChanged() {
    }

    public void reSetEnable() {
        this.setEnabled(true);
    }

    public void getAllergy() {
    }

    

}
