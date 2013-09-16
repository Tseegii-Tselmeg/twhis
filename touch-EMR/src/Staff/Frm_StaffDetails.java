package Staff;

import ErrorMessage.StoredErrorMessage;
import cc.johnwu.sql.*;
import cc.johnwu.finger.FingerPrintScanner;
import cc.johnwu.finger.FingerPrintViewerInterface;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.sql.*;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.TableColumn;

/**
 *
 * @author steven
 */
public class Frm_StaffDetails extends javax.swing.JFrame implements FingerPrintViewerInterface {
    private StoredErrorMessage ErrorMessage = new StoredErrorMessage() ;
    private int m_Sno = 0;
    private String m_UUID = null;

     /*多國語言變數*/
//    private language paragraph = new language();
//    private String[] line = paragraph.setlanguage("EMPLOYEE").split("\n") ;
//    private String[] message = paragraph.setlanguage("MESSAGE").split("\n") ;

    // 修改資料
    public Frm_StaffDetails(int sno) {
        
        initComponents();
        m_Sno = sno;
        init();
        initComboBox();
        initWorkCob();

        //initLanguage();
    }

    // 新增資料
    public Frm_StaffDetails() {
        initComponents();
        initNew();
        initComboBox();

        //initLanguage();
    }

     /** 初始化  新增員工*/
    private void initNew(){
         FingerPrintScanner.setParentFrame(this);//打開指紋機
        this.setLocationRelativeTo(this);//視窗顯示至中
        //this.setExtendedState(Frm_Employee.MAXIMIZED_BOTH);  // 最大化
        addWindowListener(new WindowAdapter() {//視窗關閉事件
            @Override
            public void windowClosing(WindowEvent windowevent)
            {
                 FingerPrintScanner.stop();
                btn_CancelActionPerformed(null);
            }
        });

        ResultSet rs = null;
        try {
            m_UUID = UUID.randomUUID().toString();
            String sql = "INSERT INTO staff_info (s_no,firstname,lastname,sex,bloodgroup,rh_type,marital_status,staff_type,staff_category,employee_status,training_type,sponsorship,commitment,entitlement,type,exist) " +
                "SELECT MAX(s_no)+1, " +// s_no
                "'"+m_UUID+"', " +      // firstname
                "'"+m_UUID+"', " +      // lastname
                "'', " +
                "'', " +
                "'', " +
                "0, " +
                "0, " +
                "0, " +
                "0, " +
                "0, " +
                "0, " +
                "0, " +
                "0, " +
                "0, " +
                "false " +              // exist
                "FROM staff_info ";
            DBC.executeUpdate(sql);
            sql = "SELECT s_no FROM staff_info " +
                  "WHERE firstname = '"+m_UUID+"' ";
            rs = DBC.executeQuery(sql);
            if(rs.next()){
                m_Sno = Integer.parseInt(rs.getString(1));
                this.txt_No.setText(String.valueOf(m_Sno));
              //  this.setTitle(paragraph.getLanguage(line, "HRMSYSTEM")+" ("+paragraph.getLanguage(line, "NEWSTAFFNO") +" "+m_Sno +" )");
            }

        } catch (SQLException e) {
           // ErrorMessage.setData("Patients", "Frm_PatientMod" ,"init()",
              //       e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            System.out.println(e);
        } finally{
            try{ DBC.closeConnection(rs); }
            catch (SQLException e) {
            }
        }
    }

    private void init(){
        FingerPrintScanner.setParentFrame(this);//打開指紋機
        if (m_Sno != 0) {
            //this.setTitle(paragraph.getLanguage(line, "EMPLOYEEINFORMATION") + " ("+paragraph.getLanguage(line, "NEWSTAFFNO")+" "+m_Sno +" )");
        } else {
            //this.setTitle(paragraph.getLanguage(line, "EMPLOYEEINFORMATION") +" ("+paragraph.getLanguage(line, "EDITSTAFFNO")+m_Sno +" )");
        }
        //this.setExtendedState(Frm_Employee.MAXIMIZED_BOTH);  // 最大化
        this.setLocationRelativeTo(this);//視窗顯示至中
        addWindowListener(new WindowAdapter() {//視窗關閉事件
            @Override
            public void windowClosing(WindowEvent windowevent)
            {
                FingerPrintScanner.stop();
                btn_CancelActionPerformed(null);
            }
        });
        String sql = "";
        ResultSet rs = null;

        this.txt_No.setText(String.valueOf(m_Sno));

        try {
            sql="SELECT * FROM staff_info WHERE staff_info.s_no="+m_Sno+" AND staff_info.exist=1";
            
            rs = DBC.executeQuery(sql);
            rs.next();
            this.txt_UserId.setText(rs.getString("s_id"));
            this.pwd_PassWord.setText(HISPassword.deCode(rs.getString("passwd")));
            this.pwd_Confirmation.setText(HISPassword.deCode(rs.getString("passwd")));
            this.txt_HssNo.setText(rs.getString("hss_no"));
            this.txt_FirstName.setText(rs.getString("firstname"));
            this.txt_LastName.setText(rs.getString("lastname"));
            this.txt_PlaceOfBirth.setText(rs.getString("place_birth"));
            this.txt_Email.setText(rs.getString("email"));
            this.txt_CellPhone.setText(rs.getString("cellphone"));

            btn_Save.setEnabled(isCanSave());
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            try {DBC.closeConnection(rs);}
            catch (SQLException e){

            }
        }
    }

    public void initComboBox(){
        ResultSet rs_dep = null;
        ResultSet rs_pos = null;
        ResultSet rs_permission = null;
        ResultSet rs_poli = null;
        try {

/*----------設定部門(Administrative)的combobox ----------*/
            rs_dep = DBC.executeQuery("SELECT * FROM department GROUP BY name");
            this.cob_Administrative.addItem("");
            while (rs_dep.next()) {
                this.cob_Administrative.addItem(rs_dep.getString("name"));
            }
/*----------設定職位的combobox ----------*/
//            rs_pos = DBC.executeQuery("SELECT * FROM position GROUP BY name");
//            this.cob_Position.addItem("");
//            while (rs_pos.next()) {
//                this.cob_Position.addItem(rs_pos.getString("name"));
//            }
/*----------設定群組的combobox ----------*/
            rs_permission = DBC.executeQuery("SELECT * FROM permission_info GROUP BY grp_name");
            this.cob_Permission.addItem("");
            while (rs_permission.next()) {
                this.cob_Permission.addItem(rs_permission.getString("grp_name"));
            }
/*----------設定門診部(Clinical)的combobox ----------*/
            rs_poli = DBC.executeQuery("SELECT * FROM policlinic GROUP BY name");
            this.cob_Poli.addItem("");
            //this.cob_Clinical.addItem("");
            while (rs_poli.next()) {
                //this.cob_Clinical.addItem(rs_poli.getString("name"));
                this.cob_Poli.addItem(rs_poli.getString("name"));
            }

        } catch (SQLException e) {
    //        ErrorMessage.setData("Staff", "Frm_StaffChange" ,"initComboBox()",
    //            e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        }finally{
            try {
                DBC.closeConnection(rs_dep);
                DBC.closeConnection(rs_pos);
                DBC.closeConnection(rs_permission);
                DBC.closeConnection(rs_poli);
            } catch (SQLException e) {

            }

        }
    }

     private void initWorkCob() {
        try {

            ResultSet rs_staffInfo = DBC.executeQuery("SELECT * FROM staff_info WHERE s_no = " + m_Sno);
            ResultSet rs_dep = DBC.executeQuery("SELECT * FROM department GROUP BY name");
            ResultSet rs_pos = DBC.executeQuery("SELECT * FROM position GROUP BY name");
            ResultSet rs_permission = DBC.executeQuery("SELECT * FROM permission_info GROUP BY grp_name");
            ResultSet rs_poli = DBC.executeQuery("SELECT * FROM policlinic GROUP BY name");
            rs_staffInfo.next();
            rs_dep.next();
            rs_pos.next();
            rs_permission.next();
            rs_poli.next();
            /*將combobox設定為員工所屬群組 */
            for (int i = 0; i < cob_Permission.getItemCount(); i++) {
                if (rs_staffInfo.getString("gp_guid") != null) {
                    if (rs_staffInfo.getString("gp_guid").equals(rs_permission.getString("guid"))) {
                        cob_Permission.setSelectedIndex(i + 1);
                        break;
                    } else {
                        rs_permission.next();
                    }
                } else {
                    cob_Permission.setSelectedIndex(0);
                }
            }
            /*將combobox設定為員工所屬職位 */
//            for (int i = 0; i < cob_Administrative.getItemCount(); i++) {
//                if (rs_staffInfo.getString("posi_guid") != null) {
//                    if (rs_staffInfo.getString("posi_guid").equals(rs_pos.getString("guid"))) {
//                        cob_Administrative.setSelectedIndex(i + 1);
//                        break;
//                    } else {
//                        rs_pos.next();
//                    }
//                } else {
//                    cob_Administrative.setSelectedIndex(0);
//                }
//            }
            /*將combobox設定為員工所屬部門 */
            for (int i = 0; i < cob_Administrative.getItemCount(); i++) {
                if (rs_staffInfo.getString("dep_guid") != null) {
                    if (rs_staffInfo.getString("dep_guid").equals(rs_dep.getString("guid"))) {
                        cob_Administrative.setSelectedIndex(i + 1);
                        break;
                    } else {
                        rs_dep.next();
                    }
                } else {
                    cob_Administrative.setSelectedIndex(0);
                }
            }
            /*將combobox設定為員工所屬部門 */
            for (int i = 0; i < cob_Poli.getItemCount(); i++) {
                if (rs_staffInfo.getString("poli_guid") != null) {
                    if (rs_staffInfo.getString("poli_guid").equals(rs_poli.getString("guid"))) {
                        cob_Poli.setSelectedIndex(i + 1);
                        break;
                    } else {
                        rs_poli.next();
                    }
                } else {
                    cob_Poli.setSelectedIndex(0);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Frm_StaffDetails.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


/*
     private void initLanguage() {
        this.Emp_List.setTitleAt(0,paragraph.getLanguage(line, "PERSONAL"));
        this.Emp_List.setTitleAt(1,paragraph.getLanguage(line, "CONTACTDETAILS"));
        this.Emp_List.setTitleAt(2, paragraph.getLanguage(line, "FAMILYINFORMATION"));
        this.pan_Fingerprint.setBorder(javax.swing.BorderFactory.createTitledBorder(paragraph.getLanguage(line, "FINGERPRINT")));
        this.lab_No.setText(paragraph.getLanguage(line, "STAFFNO"));
        this.lab_HssNo.setText(paragraph.getLanguage(line, "HSSNO"));
        this.lab_UserId.setText(paragraph.getLanguage(line,"USERID"));
        this.lab_PassWord.setText(paragraph.getLanguage(line,"PASSWORD"));
        this.lab_Confirmation.setText(paragraph.getLanguage(line,"CONFIRMATION"));
        this.lab_FirstName.setText(paragraph.getLanguage(line,"FIRSTNAME"));
        this.lab_DateOfBirth.setText(paragraph.getLanguage(line,"DATAOFBIRTH"));
        this.lab_LastName.setText(paragraph.getLanguage(line,"LASTNAME"));
        this.lab_PlaceOfBirth.setText(paragraph.getLanguage(line,"PLACEOFBIRTH"));
        this.btn_Enroll.setText(paragraph.getLanguage(line,"ENROLL"));
        this.btn_Save.setText(paragraph.getLanguage(message,"SAVE"));
        this.btn_Cancel.setText(paragraph.getLanguage(line,"CANCLE"));

        this.setTitle(paragraph.getLanguage(line, "EMPLOYEEINFORMATION"));


      }*/

    /**隱藏欄位*/
     public void setHideColumn(javax.swing.JTable table,int index){
        TableColumn tc= table.getColumnModel().getColumn(index);
        tc.setMaxWidth(0); tc.setPreferredWidth(0); tc.setMinWidth(0);
        tc.setWidth(0); table.getTableHeader().getColumnModel().getColumn(index).setMaxWidth(0);
        table.getTableHeader().getColumnModel().getColumn(index).setMinWidth(0);
    }

 

    private boolean isCanSave(){
        if(!this.txt_FirstName.getText().equals("")
        && !this.txt_LastName.getText().equals("")
        && !this.txt_UserId.getText().equals("")
        && this.txt_FirstName.getText() != null
        && this.txt_LastName.getText() != null
        && this.txt_UserId.getText() != null
        && this.pwd_PassWord.getPassword() != null
        && this.pwd_Confirmation.getPassword() != null
       ){
            if(new String(this.pwd_Confirmation.getPassword()).equals(new String(this.pwd_PassWord.getPassword()))){//密碼是否相同
                    return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btn_Save = new javax.swing.JButton();
        btn_Cancel = new javax.swing.JButton();
        pan_Personal_Personal = new javax.swing.JPanel();
        pan_Fingerprint1 = new javax.swing.JPanel();
        fingerPrintViewer = new cc.johnwu.finger.FingerPrintViewer();
        btn_Enroll = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        txt_FirstName = new javax.swing.JTextField();
        lab_UserId = new javax.swing.JLabel();
        cob_Administrative = new javax.swing.JComboBox();
        cob_Permission = new javax.swing.JComboBox();
        lab_cellphone = new javax.swing.JLabel();
        lab_Posi = new javax.swing.JLabel();
        dateChooser_PersonalDateBirth = new cc.johnwu.date.DateComboBox();
        lab_mail = new javax.swing.JLabel();
        txt_LastName = new javax.swing.JTextField();
        txt_PlaceOfBirth = new javax.swing.JTextField();
        lab_PlaceOfBirth = new javax.swing.JLabel();
        lab_DateOfBirth = new javax.swing.JLabel();
        txt_No = new javax.swing.JTextField();
        lab_Group = new javax.swing.JLabel();
        lab_No = new javax.swing.JLabel();
        cob_Poli = new javax.swing.JComboBox();
        lab_ToDepartment = new javax.swing.JLabel();
        lab_FirstName = new javax.swing.JLabel();
        lab_LastName = new javax.swing.JLabel();
        lab_HssNo = new javax.swing.JLabel();
        txt_HssNo = new javax.swing.JTextField();
        pwd_PassWord = new javax.swing.JPasswordField();
        pwd_Confirmation = new javax.swing.JPasswordField();
        txt_Email = new javax.swing.JTextField();
        lab_Confirmation = new javax.swing.JLabel();
        txt_CellPhone = new javax.swing.JTextField();
        lab_PassWord = new javax.swing.JLabel();
        txt_UserId = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Employee Information");
        setResizable(false);

        btn_Save.setText("Save");
        btn_Save.setEnabled(false);
        btn_Save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_SaveActionPerformed(evt);
            }
        });

        btn_Cancel.setText("Cancle");
        btn_Cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CancelActionPerformed(evt);
            }
        });

        pan_Personal_Personal.setBackground(new java.awt.Color(240, 239, 239));

        pan_Fingerprint1.setBackground(new java.awt.Color(255, 255, 255));
        pan_Fingerprint1.setBorder(javax.swing.BorderFactory.createTitledBorder("Fingerprint"));

        fingerPrintViewer.setVisible(true);

        javax.swing.GroupLayout fingerPrintViewerLayout = new javax.swing.GroupLayout(fingerPrintViewer.getContentPane());
        fingerPrintViewer.getContentPane().setLayout(fingerPrintViewerLayout);
        fingerPrintViewerLayout.setHorizontalGroup(
            fingerPrintViewerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 86, Short.MAX_VALUE)
        );
        fingerPrintViewerLayout.setVerticalGroup(
            fingerPrintViewerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 119, Short.MAX_VALUE)
        );

        btn_Enroll.setText("Enroll");
        btn_Enroll.setEnabled(false);
        btn_Enroll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_EnrollActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pan_Fingerprint1Layout = new javax.swing.GroupLayout(pan_Fingerprint1);
        pan_Fingerprint1.setLayout(pan_Fingerprint1Layout);
        pan_Fingerprint1Layout.setHorizontalGroup(
            pan_Fingerprint1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan_Fingerprint1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan_Fingerprint1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btn_Enroll, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE)
                    .addComponent(fingerPrintViewer, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE))
                .addGap(10, 10, 10))
        );
        pan_Fingerprint1Layout.setVerticalGroup(
            pan_Fingerprint1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_Fingerprint1Layout.createSequentialGroup()
                .addComponent(fingerPrintViewer, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Enroll)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.setBackground(new java.awt.Color(240, 239, 239));

        txt_FirstName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_FirstNameKeyReleased(evt);
            }
        });

        lab_UserId.setText("* User ID :");

        cob_Permission.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cob_PermissionActionPerformed(evt);
            }
        });

        lab_cellphone.setText("Contact Number :");

        lab_Posi.setText("Position of Staff :");

        lab_mail.setText("Email Address :");

        txt_LastName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_LastNameKeyReleased(evt);
            }
        });

        lab_PlaceOfBirth.setText("Place of Birth :");

        lab_DateOfBirth.setText("Date of Birth :");

        txt_No.setText("00000000");
        txt_No.setEnabled(false);

        lab_Group.setText("Permmision Group :");

        lab_No.setText("Staff No. :");

        lab_ToDepartment.setText("Depart/Clinic:");

        lab_FirstName.setText("* First Name :");

        lab_LastName.setText("* Last Name :");

        lab_HssNo.setText("HSS NO. :");

        txt_HssNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_HssNoKeyReleased(evt);
            }
        });

        pwd_PassWord.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                pwd_PassWordKeyReleased(evt);
            }
        });

        pwd_Confirmation.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                pwd_ConfirmationKeyReleased(evt);
            }
        });

        lab_Confirmation.setText("* Confirmation :");

        lab_PassWord.setText("* Password :");

        txt_UserId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_UserIdFocusLost(evt);
            }
        });
        txt_UserId.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_UserIdKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lab_UserId)
                    .addComponent(lab_cellphone)
                    .addComponent(lab_Posi)
                    .addComponent(lab_mail)
                    .addComponent(lab_PlaceOfBirth)
                    .addComponent(lab_DateOfBirth)
                    .addComponent(lab_No)
                    .addComponent(lab_Group)
                    .addComponent(lab_FirstName)
                    .addComponent(lab_LastName)
                    .addComponent(lab_HssNo)
                    .addComponent(lab_Confirmation)
                    .addComponent(lab_PassWord)
                    .addComponent(lab_ToDepartment))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_FirstName, javax.swing.GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE)
                    .addComponent(cob_Administrative, 0, 329, Short.MAX_VALUE)
                    .addComponent(cob_Permission, 0, 329, Short.MAX_VALUE)
                    .addComponent(dateChooser_PersonalDateBirth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_LastName, javax.swing.GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE)
                    .addComponent(txt_PlaceOfBirth, javax.swing.GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE)
                    .addComponent(txt_No, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cob_Poli, 0, 329, Short.MAX_VALUE)
                    .addComponent(txt_HssNo, javax.swing.GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE)
                    .addComponent(pwd_PassWord, javax.swing.GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE)
                    .addComponent(pwd_Confirmation, javax.swing.GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE)
                    .addComponent(txt_Email, javax.swing.GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE)
                    .addComponent(txt_CellPhone, javax.swing.GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE)
                    .addComponent(txt_UserId, javax.swing.GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE))
                .addContainerGap(78, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_No, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_No))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_FirstName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_FirstName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_LastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lab_LastName))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dateChooser_PersonalDateBirth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lab_DateOfBirth))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_PlaceOfBirth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_PlaceOfBirth))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_HssNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_HssNo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_UserId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_UserId))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pwd_PassWord, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_PassWord))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pwd_Confirmation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_Confirmation))
                .addGap(7, 7, 7)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_Email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_mail))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_CellPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_cellphone))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cob_Administrative, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_Posi))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cob_Permission, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_Group))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cob_Poli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_ToDepartment))
                .addGap(148, 148, 148))
        );

        javax.swing.GroupLayout pan_Personal_PersonalLayout = new javax.swing.GroupLayout(pan_Personal_Personal);
        pan_Personal_Personal.setLayout(pan_Personal_PersonalLayout);
        pan_Personal_PersonalLayout.setHorizontalGroup(
            pan_Personal_PersonalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan_Personal_PersonalLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(50, 50, 50)
                .addComponent(pan_Fingerprint1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pan_Personal_PersonalLayout.setVerticalGroup(
            pan_Personal_PersonalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_Personal_PersonalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan_Personal_PersonalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan_Personal_PersonalLayout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(pan_Personal_PersonalLayout.createSequentialGroup()
                        .addComponent(pan_Fingerprint1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(318, 318, 318))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(529, Short.MAX_VALUE)
                .addComponent(btn_Save, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Cancel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pan_Personal_Personal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pan_Personal_Personal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_Cancel)
                    .addComponent(btn_Save))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_CancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CancelActionPerformed
        FingerPrintScanner.stop();
       try {
            String sql = "DELETE FROM staff_info WHERE firstname = '"+m_UUID+"' AND exist = false";
            DBC.executeUpdate(sql);
        } catch (SQLException e) {
        //    ErrorMessage.setData("Patients", "Frm_PatientMod" ,"btn_CancelActionPerformed(java.awt.event.ActionEvent evt)",
           //         e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            System.out.println(e);
        }
        new Frm_StaffInfo().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btn_CancelActionPerformed

    private void btn_SaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SaveActionPerformed
        String sql = null;
        ResultSet rsPosi = null;
        ResultSet rsPermission = null;
        ResultSet rsDep = null;
        ResultSet rsPoli = null;

        String Posi = null;
        String Permission = null;
        String Dep = null;
        String Poli = null;
        try {
//             if(this.cob_Position.getSelectedIndex()!=0){
//               rsPosi = DBC.executeQuery("SELECT guid FROM position WHERE name = '" + cob_Administrative.getSelectedItem().toString() + "'" );
//               if(rsPosi.next()){
//                    Posi = "'" + rsPosi.getString("guid") + "'";
//                }
//            }

            if(this.cob_Permission.getSelectedIndex()!=0){
                rsPermission = DBC.executeQuery("SELECT guid FROM permission_info WHERE grp_name = '" + cob_Permission.getSelectedItem().toString() + "'" );
                if(rsPermission.next()){
                    Permission = "'" + rsPermission.getString("guid") + "'";
                }
            }

            if(this.cob_Administrative.getSelectedIndex()!=0){
                rsDep = DBC.executeQuery("SELECT guid FROM department WHERE name = '" + cob_Administrative.getSelectedItem().toString() + "'" );
                if(rsDep.next()){
                    Dep = "'" + rsDep.getString("guid") + "'";
                }
            }
            if(this.cob_Poli.getSelectedIndex()!=0){
                rsPoli = DBC.executeQuery("SELECT guid FROM policlinic WHERE name = '" + cob_Poli.getSelectedItem().toString() + "'" );
                if(rsPoli.next()){
                    Poli = "'" + rsPoli.getString("guid") + "'";
                }
            }

              sql = "UPDATE staff_info SET "+
                    "s_id = '"+this.txt_UserId.getText()+"', " +
                    "passwd = '"+HISPassword.enCode(new String(this.pwd_PassWord.getPassword()))+"', " +
                    "hss_no = '"+this.txt_HssNo.getText()+"', " +
                    "firstname = '"+this.txt_FirstName.getText()+"', " +
                    "lastname = '"+this.txt_LastName.getText()+"', " +
                    "date_birth = '"+this.dateChooser_PersonalDateBirth.getValue()+"', " +
                    "place_birth = '"+this.txt_PlaceOfBirth.getText()+"', " +
                    "cellphone = '"+this.txt_CellPhone.getText()+"', " +
                    "email = '"+this.txt_Email.getText()+"', " +
                    "gp_guid = "+ Permission + ", "+
                    "poli_guid ="+ Poli + ", "+
                    "posi_guid = "+ Posi + ", "+
                    "dep_guid = "+ Dep + ", "+
                    "exist = true "+
                    "WHERE s_no = '"+this.txt_No.getText()+"' ";
            DBC.executeUpdate(sql);
           // cc.johnwu.login.ChangesLog.ChangesLog("staff_info", this.txt_No.getText(), "add");

            JOptionPane.showMessageDialog(null,"Saved successfully.");
         //   JOptionPane.showMessageDialog(new Frame(), paragraph.getLanguage(message, "SAVECOMPLETE"));
            btn_CancelActionPerformed(null);
          //  m_frame.onPatientMod(this.txt_No.getText());
        } catch (SQLException e) {
           // ErrorMessage.setData("Patients", "Frm_PatientMod" ,"btn_OKActionPerformed(java.awt.event.ActionEvent evt)",
             //       e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            JOptionPane.showMessageDialog(null,"User ID Repeat.");
            txt_UserId.setFocusable(true);
            System.out.println(e);
        } finally {
            try {DBC.closeConnection(rsPosi);
                DBC.closeConnection(rsPermission);
                DBC.closeConnection(rsDep);
                DBC.closeConnection(rsPoli);
            }
            catch (SQLException e){

            }
        }
    }//GEN-LAST:event_btn_SaveActionPerformed

    private void txt_LastNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_LastNameKeyReleased
        this.btn_Save.setEnabled(isCanSave());
    }//GEN-LAST:event_txt_LastNameKeyReleased

    private void txt_HssNoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_HssNoKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_HssNoKeyReleased

    private void txt_UserIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_UserIdFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_UserIdFocusLost

    private void txt_UserIdKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_UserIdKeyReleased
        this.btn_Save.setEnabled(isCanSave());
    }//GEN-LAST:event_txt_UserIdKeyReleased

    private void pwd_PassWordKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pwd_PassWordKeyReleased
        this.btn_Save.setEnabled(isCanSave());
    }//GEN-LAST:event_pwd_PassWordKeyReleased

    private void pwd_ConfirmationKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pwd_ConfirmationKeyReleased
        this.btn_Save.setEnabled(isCanSave());
    }//GEN-LAST:event_pwd_ConfirmationKeyReleased

    private void btn_EnrollActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_EnrollActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_EnrollActionPerformed

    private void cob_PermissionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cob_PermissionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cob_PermissionActionPerformed

    private void txt_FirstNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_FirstNameKeyReleased
        this.btn_Save.setEnabled(isCanSave());
    }//GEN-LAST:event_txt_FirstNameKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Cancel;
    private javax.swing.JButton btn_Enroll;
    private javax.swing.JButton btn_Save;
    private javax.swing.JComboBox cob_Administrative;
    private javax.swing.JComboBox cob_Permission;
    private javax.swing.JComboBox cob_Poli;
    private cc.johnwu.date.DateComboBox dateChooser_PersonalDateBirth;
    private cc.johnwu.finger.FingerPrintViewer fingerPrintViewer;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lab_Confirmation;
    private javax.swing.JLabel lab_DateOfBirth;
    private javax.swing.JLabel lab_FirstName;
    private javax.swing.JLabel lab_Group;
    private javax.swing.JLabel lab_HssNo;
    private javax.swing.JLabel lab_LastName;
    private javax.swing.JLabel lab_No;
    private javax.swing.JLabel lab_PassWord;
    private javax.swing.JLabel lab_PlaceOfBirth;
    private javax.swing.JLabel lab_Posi;
    private javax.swing.JLabel lab_ToDepartment;
    private javax.swing.JLabel lab_UserId;
    private javax.swing.JLabel lab_cellphone;
    private javax.swing.JLabel lab_mail;
    private javax.swing.JPanel pan_Fingerprint1;
    private javax.swing.JPanel pan_Personal_Personal;
    private javax.swing.JPasswordField pwd_Confirmation;
    private javax.swing.JPasswordField pwd_PassWord;
    private javax.swing.JTextField txt_CellPhone;
    private javax.swing.JTextField txt_Email;
    private javax.swing.JTextField txt_FirstName;
    private javax.swing.JTextField txt_HssNo;
    private javax.swing.JTextField txt_LastName;
    private javax.swing.JTextField txt_No;
    private javax.swing.JTextField txt_PlaceOfBirth;
    private javax.swing.JTextField txt_UserId;
    // End of variables declaration//GEN-END:variables

     public void onFingerDown() {
            this.btn_Enroll.setEnabled(isCanSave());
    }

    public void showImage(BufferedImage bufferedimage, String msg) {
        this.fingerPrintViewer.showImage(bufferedimage);
        this.fingerPrintViewer.setTitle(msg);
    }
}
/**staff_fingertemplate*/