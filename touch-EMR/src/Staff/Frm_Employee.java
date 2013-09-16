package Staff;

import ErrorMessage.StoredErrorMessage;
import Multilingual.language;
import cc.johnwu.sql.*;
import cc.johnwu.finger.FingerPrintScanner;
import cc.johnwu.finger.FingerPrintViewerInterface;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

/**
 *
 * @author steven
 */
public class Frm_Employee extends javax.swing.JFrame implements FingerPrintViewerInterface {
    private StoredErrorMessage ErrorMessage = new StoredErrorMessage() ;
    private int m_Sno;
    private String m_UUID = null;
     /*多國語言變數*/
    private language paragraph = new language();
    private String[] line = new String(paragraph.setlanguage("EMPLOYEE")).split("\n") ;
    private String[] message = new String(paragraph.setlanguage("MESSAGE")).split("\n") ;
  
    public Frm_Employee(int sno, String uuid) {
        m_UUID = uuid;
        m_Sno = sno;
        initComponents();
        init();
        reSetChildrenTable();
        initLanguage();

    }

    private void init(){
        FingerPrintScanner.setParentFrame(this);//打開指紋機
        if (m_UUID != null) {
            this.setTitle(paragraph.getLanguage(line, "EMPLOYEEINFORMATION") + " ("+paragraph.getLanguage(line, "NEWSTAFFNO")+" "+m_Sno +" )");
        } else {
            this.setTitle(paragraph.getLanguage(line, "EMPLOYEEINFORMATION") +" ("+paragraph.getLanguage(line, "EDITSTAFFNO")+m_Sno +" )");
        }
        
        this.setExtendedState(Frm_Employee.MAXIMIZED_BOTH);  // 最大化
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
            System.out.println(sql);
            rs = DBC.executeQuery(sql);
            rs.next();
            this.txt_UserId.setText(rs.getString("s_id"));
            this.pwd_PassWord.setText(HISPassword.deCode(rs.getString("passwd")));
            this.pwd_Confirmation.setText(HISPassword.deCode(rs.getString("passwd")));
            this.txt_HssNo.setText(rs.getString("hss_no"));
            this.txt_SocialSecurityNo.setText(rs.getString("ss_no"));
            this.txt_NationalNo.setText(rs.getString("nia_no"));
            this.txt_NhisNo.setText(rs.getString("nhis_no"));
            this.txt_PassportNo.setText(rs.getString("passport_no"));
            this.txt_FirstName.setText(rs.getString("firstname"));
            this.txt_LastName.setText(rs.getString("lastname"));
            this.cob_Sex.setSelectedItem(rs.getString("sex"));
            this.txt_PlaceOfBirth.setText(rs.getString("place_birth"));
            this.cob_MaritalStatus.setSelectedIndex(Integer.parseInt(rs.getString("marital_status")));
            this.txt_KinFirstName.setText(rs.getString("kin_firstname"));
            this.txt_KinLastName.setText(rs.getString("kin_lastname"));
            this.txt_KinAddress.setText(rs.getString("kin_address"));
            this.txt_KinPostal.setText(rs.getString("kin_postal"));
            this.txt_KinCellPhone.setText(rs.getString("kin_phone"));
            this.txt_KinEmail.setText(rs.getString("kin_email"));
            this.txt_Address.setText(rs.getString("address"));
            this.txt_Postal.setText(rs.getString("postal"));
            this.txt_Phone.setText(rs.getString("phone"));
            this.txt_CellPhone.setText(rs.getString("cellphone"));
            this.txt_KinPhone.setText(rs.getString("phone"));
            this.txt_Email.setText(rs.getString("email"));
            this.txt_Website.setText(rs.getString("website"));
            this.cob_Blood.setSelectedItem(rs.getString("bloodgroup"));
            this.cob_Rh.setSelectedItem(rs.getString("rh_type"));
            this.cob_Disability.setSelectedIndex(Integer.parseInt(rs.getString("disability_status")));
            this.txt_DisabilityDescription.setText(rs.getString("disability_description"));
            this.txt_SpouseFirstname.setText(rs.getString("spouse_firstname"));
            this.txt_SpouseLastname.setText(rs.getString("spouse_lastname"));
            this.txt_SpouseNhisNo.setText(rs.getString("Spouse_nhis_no"));
            this.cob_StaffType.setSelectedIndex(Integer.parseInt(rs.getString("staff_type")));
            this.cob_StaffCategory.setSelectedIndex(Integer.parseInt(rs.getString("staff_category")));
            btn_Save.setEnabled(isCanSave());
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            try {DBC.closeConnection(rs);}
            catch (SQLException e){

            }
        }
        initLanguage();
    }


     private void initLanguage() {
        this.Emp_List.setTitleAt(0,paragraph.getLanguage(line, "PERSONAL"));
        this.Emp_List.setTitleAt(1,paragraph.getLanguage(line, "CONTACTDETAILS"));
        this.Emp_List.setTitleAt(2, paragraph.getLanguage(line, "FAMILYINFORMATION"));
        this.pan_Spouse.setBorder(javax.swing.BorderFactory.createTitledBorder(paragraph.getLanguage(line, "SPOUSE")));
        this.pan_Children.setBorder(javax.swing.BorderFactory.createTitledBorder(paragraph.getLanguage(line, "CHILDREN")));
        this.pan_NextOfKin.setBorder(javax.swing.BorderFactory.createTitledBorder(paragraph.getLanguage(line,"NEXTOFKIN")));
        this.pan_Fingerprint.setBorder(javax.swing.BorderFactory.createTitledBorder(paragraph.getLanguage(line, "FINGERPRINT")));
        this.lab_No.setText(paragraph.getLanguage(line, "STAFFNO"));
        this.lab_HssNo.setText(paragraph.getLanguage(line, "HSSNO"));
        this.lab_SocialSecurityNo.setText(paragraph.getLanguage(line,"SOCIALSECURITYNO"));
        this.lab_NationalNo.setText(paragraph.getLanguage(line, "NATIONALID"));
        this.lab_NhisNo.setText(paragraph.getLanguage(line,"NHISNO"));
        this.lab_HSSNO4.setText(paragraph.getLanguage(line,"PASSPORTNO"));
        this.lab_UserId.setText(paragraph.getLanguage(line,"USERID"));
        this.lab_PassWord.setText(paragraph.getLanguage(line,"PASSWORD"));
        this.lab_Confirmation.setText(paragraph.getLanguage(line,"CONFIRMATION"));
        this.lab_FirstName.setText(paragraph.getLanguage(line,"FIRSTNAME"));
        this.lab_Sex.setText(paragraph.getLanguage(line,"SEX"));
        this.lab_DateOfBirth.setText(paragraph.getLanguage(line,"DATAOFBIRTH"));
        this.lab_MaritalStatus.setText(paragraph.getLanguage(line,"MARITALSTATUS"));
        this.lab_DisabilityDescription.setText(paragraph.getLanguage(line,"DISABILITYDESCRIPITION"));
        this.lab_LastName.setText(paragraph.getLanguage(line,"LASTNAME"));
        this.lab_Bloodtype.setText(paragraph.getLanguage(line,"BLOODRH"));
        this.lab_PlaceOfBirth.setText(paragraph.getLanguage(line,"PLACEOFBIRTH"));
        this.lab_Disability.setText(paragraph.getLanguage(line,"DISABILITYSTAUSE"));
        this.btn_Enroll.setText(paragraph.getLanguage(line,"ENROLL"));
        this.btn_Save.setText(paragraph.getLanguage(message,"SAVE"));
        this.btn_Cancel.setText(paragraph.getLanguage(line,"CANCLE"));
        this.lab_Address.setText(paragraph.getLanguage(line,"RESIDENTIALADDRESS"));
        this.lab_Postal.setText(paragraph.getLanguage(line,"POSTALADDRESS"));
        this.lab_Phone.setText(paragraph.getLanguage(line,"PHONENUMBER"));
        this.lab_CellPhone.setText(paragraph.getLanguage(line,"CELLPHONENUMBER"));
        this.lab_Email.setText(paragraph.getLanguage(line,"EMAIL"));
        this.lab_Website.setText(paragraph.getLanguage(line,"PERSONALWEBSITE"));
        this.lab_KinAddress.setText(paragraph.getLanguage(line,"RESIDENTIALADDRESS"));
        this.lab_KinFirstName.setText(paragraph.getLanguage(line,"FIRSTNAME"));
        this.lab_KinLastName.setText(paragraph.getLanguage(line,"LASTNAME"));
        this.lab_KinPostal.setText(paragraph.getLanguage(line,"POSTALADDRESS"));
        this.lab_KinPhone.setText(paragraph.getLanguage(line,"PHONENUMBER"));
        this.lab_KinCellPhone.setText(paragraph.getLanguage(line,"CELLPHONENUMBER"));
        this.lab_KinEmail.setText(paragraph.getLanguage(line,"EMAIL"));
        this.lab_SpouseFirstName.setText(paragraph.getLanguage(line,"FIRSTNAMEOFSPOUSE"));
        this.lab_SpouseLastname.setText(paragraph.getLanguage(line,"LANSTNAMEOFSPOUSE"));
        this.lab_SpouseDateOfBirth.setText(paragraph.getLanguage(line,"SPOUSESDATAOFBIRTH"));
        this.lab_SpouseNhisNo.setText(paragraph.getLanguage(line,"SPOUSESNHISNO"));
        this.lab_StaffType.setText(paragraph.getLanguage(line,"STAFFTYPE"));
        this.lab_StaffCategory.setText(paragraph.getLanguage(line,"STAFFCATEGORY"));
        this.lab_ChildrenName.setText(paragraph.getLanguage(line,"FULLNAME"));
        this.lab_ChildrenDateBirth.setText(paragraph.getLanguage(line,"DATAOFBIRTH"));
        this.lab_ChildrenNhisNo.setText(paragraph.getLanguage(line,"NHISNO"));
        this.btn_ChildrenAdd.setText(paragraph.getLanguage(line,"ADD"));
        this.btn_ChildrenDelete.setText(paragraph.getLanguage(line,"DELETE"));
        cob_MaritalStatus.setModel(
                new javax.swing.DefaultComboBoxModel(
                    new String[] { paragraph.getLanguage(line,"SINGLE"),
                                   paragraph.getLanguage(line,"MARRIED")
        }));
        cob_Disability.setModel(
                new javax.swing.DefaultComboBoxModel(
                    new String[] { paragraph.getLanguage(line,"NO"),
                                   paragraph.getLanguage(line,"YES")}
        ));
        cob_StaffCategory.setModel(
                new javax.swing.DefaultComboBoxModel(
                    new String[] { "", paragraph.getLanguage(line,"JUNIORSTAFF1"),
                                  paragraph.getLanguage(line,"JUNIORSTAFF2"),
                                  paragraph.getLanguage(line,"SENIORSTAFF") }
        ));

        cob_StaffType.setModel(
                new javax.swing.DefaultComboBoxModel(
                    new String[] { "", paragraph.getLanguage(line,"PERMANENT"),
                    paragraph.getLanguage(line,"CONTRACT"),
                    paragraph.getLanguage(line,"TRAININGATTACHMENT") ,
                    paragraph.getLanguage(line,"SUPPORT") ,
                    paragraph.getLanguage(line,"NATIONALYOUTHEMPLOYEE") ,
                    paragraph.getLanguage(line,"CASUALWORKER") ,
                    paragraph.getLanguage(line,"HOUSEJOB1") , paragraph.getLanguage(line,"HOUSEJOB2") }
        ));
        this.setTitle(paragraph.getLanguage(line, "EMPLOYEEINFORMATION"));


      }


    /**初始化表格*/
    private TableModel getTableModel(String[] title, String from, String[] row){
        ResultSet rs = null;
        try {
            String sql = "";
            
            sql = "SELECT * FROM " + from + " WHERE s_no=" + Integer.parseInt(this.txt_No.getText());
            System.out.println(sql);
            rs = DBC.executeQuery(sql);
            int int_row = 0;
            while (rs.next()) {
                int_row++;
            }
            Object[][] obj_row = new Object[int_row][title.length];
            rs = DBC.executeQuery(sql);
            int_row = 0;
            while (rs.next()) {
                for (int i = 0; i < title.length; i++) {
                    obj_row[int_row][i] = rs.getObject(row[i]);
                }
                int_row++;
            }
            TableModel table = new DefaultTableModel(obj_row, title);
            return table;
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        } finally {
            try {DBC.closeConnection(rs);}
            catch (SQLException e){

            }
        }
    }

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
                }else{
                    return false;
                }
        }
        return false;
    }

    private void reSetChildrenTable() {
        String[] title = {"No",paragraph.getLanguage(line,"FULLNAME"), paragraph.getLanguage(line,"DATAOFBIRTH"), paragraph.getLanguage(line,"NHISNO")};
        String[] row = {"no","name","date_birth","nhis_no"};
        this.tab_Children.setModel(getTableModel(title,"children",row));
        setHideColumn(tab_Children,0);
        btn_ChildrenDelete.setEnabled(false);
        tab_Children.setRowHeight(30);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Emp_List = new javax.swing.JTabbedPane();
        pan_Personal_Personal = new javax.swing.JPanel();
        cob_Disability = new javax.swing.JComboBox();
        cob_MaritalStatus = new javax.swing.JComboBox();
        lab_MaritalStatus = new javax.swing.JLabel();
        lab_Disability = new javax.swing.JLabel();
        lab_DateOfBirth = new javax.swing.JLabel();
        lab_PlaceOfBirth = new javax.swing.JLabel();
        lab_Sex = new javax.swing.JLabel();
        cob_Sex = new javax.swing.JComboBox();
        txt_PlaceOfBirth = new javax.swing.JTextField();
        txt_SocialSecurityNo = new javax.swing.JTextField();
        txt_NationalNo = new javax.swing.JTextField();
        txt_NhisNo = new javax.swing.JTextField();
        txt_PassportNo = new javax.swing.JTextField();
        txt_LastName = new javax.swing.JTextField();
        lab_HSSNO4 = new javax.swing.JLabel();
        lab_LastName = new javax.swing.JLabel();
        lab_FirstName = new javax.swing.JLabel();
        lab_NhisNo = new javax.swing.JLabel();
        lab_SocialSecurityNo = new javax.swing.JLabel();
        txt_HssNo = new javax.swing.JTextField();
        lab_HssNo = new javax.swing.JLabel();
        lab_No = new javax.swing.JLabel();
        txt_No = new javax.swing.JTextField();
        lab_NationalNo = new javax.swing.JLabel();
        txt_UserId = new javax.swing.JTextField();
        pwd_PassWord = new javax.swing.JPasswordField();
        pwd_Confirmation = new javax.swing.JPasswordField();
        lab_Confirmation = new javax.swing.JLabel();
        lab_PassWord = new javax.swing.JLabel();
        lab_UserId = new javax.swing.JLabel();
        lab_Bloodtype = new javax.swing.JLabel();
        cob_Blood = new javax.swing.JComboBox();
        cob_Rh = new javax.swing.JComboBox();
        lab_DisabilityDescription = new javax.swing.JLabel();
        txt_DisabilityDescription = new javax.swing.JTextField();
        txt_FirstName = new javax.swing.JTextField();
        pan_Fingerprint = new javax.swing.JPanel();
        fingerPrintViewer1 = new cc.johnwu.finger.FingerPrintViewer();
        btn_Enroll = new javax.swing.JButton();
        dateChooser_PersonalDateBirth = new cc.johnwu.date.DateComboBox();
        pan_ContactDetails = new javax.swing.JPanel();
        lab_Website = new javax.swing.JLabel();
        txt_Website = new javax.swing.JTextField();
        txt_Email = new javax.swing.JTextField();
        lab_Email = new javax.swing.JLabel();
        txt_CellPhone = new javax.swing.JTextField();
        lab_CellPhone = new javax.swing.JLabel();
        lab_Phone = new javax.swing.JLabel();
        txt_Phone = new javax.swing.JTextField();
        txt_Postal = new javax.swing.JTextField();
        lab_Postal = new javax.swing.JLabel();
        lab_Address = new javax.swing.JLabel();
        txt_Address = new javax.swing.JTextField();
        pan_NextOfKin = new javax.swing.JPanel();
        lab_KinFirstName = new javax.swing.JLabel();
        lab_KinLastName = new javax.swing.JLabel();
        lab_KinAddress = new javax.swing.JLabel();
        lab_KinPostal = new javax.swing.JLabel();
        lab_KinPhone = new javax.swing.JLabel();
        lab_KinCellPhone = new javax.swing.JLabel();
        lab_KinEmail = new javax.swing.JLabel();
        txt_KinFirstName = new javax.swing.JTextField();
        txt_KinAddress = new javax.swing.JTextField();
        txt_KinPostal = new javax.swing.JTextField();
        txt_KinPhone = new javax.swing.JTextField();
        txt_KinCellPhone = new javax.swing.JTextField();
        txt_KinEmail = new javax.swing.JTextField();
        txt_KinLastName = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        pan_Spouse = new javax.swing.JPanel();
        lab_SpouseFirstName = new javax.swing.JLabel();
        lab_SpouseLastname = new javax.swing.JLabel();
        txt_SpouseFirstname = new javax.swing.JTextField();
        txt_SpouseLastname = new javax.swing.JTextField();
        lab_SpouseDateOfBirth = new javax.swing.JLabel();
        lab_SpouseNhisNo = new javax.swing.JLabel();
        txt_SpouseNhisNo = new javax.swing.JTextField();
        dateChooser_SpouseDateOfBirth = new cc.johnwu.date.DateComboBox();
        lab_StaffType = new javax.swing.JLabel();
        lab_StaffCategory = new javax.swing.JLabel();
        cob_StaffCategory = new javax.swing.JComboBox();
        cob_StaffType = new javax.swing.JComboBox();
        pan_Children = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tab_Children = new javax.swing.JTable();
        lab_ChildrenName = new javax.swing.JLabel();
        lab_ChildrenDateBirth = new javax.swing.JLabel();
        lab_ChildrenNhisNo = new javax.swing.JLabel();
        txt_ChildrenName = new javax.swing.JTextField();
        btn_ChildrenAdd = new javax.swing.JButton();
        txt_ChildrenNhisNo = new javax.swing.JTextField();
        btn_ChildrenDelete = new javax.swing.JButton();
        dateChooser_ChildrenDateBirth = new cc.johnwu.date.DateComboBox();
        btn_Save = new javax.swing.JButton();
        btn_Cancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Employee Information");
        setAlwaysOnTop(true);

        cob_Disability.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "No", "Yes" }));
        cob_Disability.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cob_DisabilityItemStateChanged(evt);
            }
        });

        cob_MaritalStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Single", "Married" }));

        lab_MaritalStatus.setText("Marital Status :");

        lab_Disability.setText("Disability Status :");

        lab_DateOfBirth.setText("Date of Birth :");

        lab_PlaceOfBirth.setText("Place of Birth :");

        lab_Sex.setText("Sex :");

        cob_Sex.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "M", "F" }));

        txt_SocialSecurityNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_SocialSecurityNoKeyReleased(evt);
            }
        });

        txt_NationalNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_NationalNoKeyReleased(evt);
            }
        });

        txt_NhisNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_NhisNoKeyReleased(evt);
            }
        });

        txt_LastName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_LastNameKeyReleased(evt);
            }
        });

        lab_HSSNO4.setText("Passport No. :");

        lab_LastName.setText("Last Name :");

        lab_FirstName.setText("First Name :");

        lab_NhisNo.setText("NHIS No. :");

        lab_SocialSecurityNo.setText("Social Security No. :");

        txt_HssNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_HssNoKeyReleased(evt);
            }
        });

        lab_HssNo.setText("HSS NO. :");

        lab_No.setText("Staff No. :");

        txt_No.setText("00000000");
        txt_No.setEnabled(false);

        lab_NationalNo.setText("National ID:");

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

        lab_Confirmation.setText("Confirmation :");

        lab_PassWord.setText("Password :");

        lab_UserId.setText("User ID :");

        lab_Bloodtype.setText("Blood/RH :");

        cob_Blood.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "A", "B", "AB", "O" }));

        cob_Rh.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "+", "-" }));

        lab_DisabilityDescription.setText("Disability Description :");

        txt_DisabilityDescription.setEnabled(false);

        pan_Fingerprint.setBorder(javax.swing.BorderFactory.createTitledBorder("Fingerprint"));

        fingerPrintViewer1.setVisible(true);

        javax.swing.GroupLayout fingerPrintViewer1Layout = new javax.swing.GroupLayout(fingerPrintViewer1.getContentPane());
        fingerPrintViewer1.getContentPane().setLayout(fingerPrintViewer1Layout);
        fingerPrintViewer1Layout.setHorizontalGroup(
            fingerPrintViewer1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 78, Short.MAX_VALUE)
        );
        fingerPrintViewer1Layout.setVerticalGroup(
            fingerPrintViewer1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 111, Short.MAX_VALUE)
        );

        btn_Enroll.setText("Enroll");
        btn_Enroll.setEnabled(false);
        btn_Enroll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_EnrollActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pan_FingerprintLayout = new javax.swing.GroupLayout(pan_Fingerprint);
        pan_Fingerprint.setLayout(pan_FingerprintLayout);
        pan_FingerprintLayout.setHorizontalGroup(
            pan_FingerprintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_FingerprintLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan_FingerprintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_Enroll, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                    .addComponent(fingerPrintViewer1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pan_FingerprintLayout.setVerticalGroup(
            pan_FingerprintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_FingerprintLayout.createSequentialGroup()
                .addComponent(fingerPrintViewer1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Enroll)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pan_Personal_PersonalLayout = new javax.swing.GroupLayout(pan_Personal_Personal);
        pan_Personal_Personal.setLayout(pan_Personal_PersonalLayout);
        pan_Personal_PersonalLayout.setHorizontalGroup(
            pan_Personal_PersonalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_Personal_PersonalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan_Personal_PersonalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lab_FirstName)
                    .addComponent(lab_NhisNo)
                    .addComponent(lab_SocialSecurityNo)
                    .addComponent(lab_MaritalStatus)
                    .addComponent(lab_NationalNo)
                    .addComponent(lab_DateOfBirth)
                    .addComponent(lab_Confirmation)
                    .addComponent(lab_PassWord)
                    .addComponent(lab_UserId, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_Sex)
                    .addComponent(lab_DisabilityDescription)
                    .addComponent(lab_HSSNO4)
                    .addComponent(lab_No))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_Personal_PersonalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan_Personal_PersonalLayout.createSequentialGroup()
                        .addComponent(txt_No, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lab_HssNo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_HssNo, javax.swing.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE))
                    .addComponent(txt_SocialSecurityNo, javax.swing.GroupLayout.DEFAULT_SIZE, 499, Short.MAX_VALUE)
                    .addComponent(txt_DisabilityDescription, javax.swing.GroupLayout.DEFAULT_SIZE, 499, Short.MAX_VALUE)
                    .addComponent(txt_NationalNo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 499, Short.MAX_VALUE)
                    .addComponent(txt_NhisNo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 499, Short.MAX_VALUE)
                    .addComponent(txt_PassportNo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 499, Short.MAX_VALUE)
                    .addComponent(txt_UserId, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 499, Short.MAX_VALUE)
                    .addComponent(pwd_PassWord, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 499, Short.MAX_VALUE)
                    .addComponent(pwd_Confirmation, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 499, Short.MAX_VALUE)
                    .addGroup(pan_Personal_PersonalLayout.createSequentialGroup()
                        .addGroup(pan_Personal_PersonalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dateChooser_PersonalDateBirth, javax.swing.GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE)
                            .addComponent(txt_FirstName, javax.swing.GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE)
                            .addComponent(cob_Sex, javax.swing.GroupLayout.Alignment.TRAILING, 0, 233, Short.MAX_VALUE)
                            .addComponent(cob_MaritalStatus, javax.swing.GroupLayout.Alignment.TRAILING, 0, 233, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan_Personal_PersonalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lab_LastName, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lab_Disability, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lab_Bloodtype, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lab_PlaceOfBirth, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan_Personal_PersonalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cob_Disability, 0, 178, Short.MAX_VALUE)
                            .addComponent(txt_LastName, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan_Personal_PersonalLayout.createSequentialGroup()
                                .addComponent(cob_Blood, 0, 112, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cob_Rh, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txt_PlaceOfBirth, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE))))
                .addGap(18, 18, 18)
                .addComponent(pan_Fingerprint, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pan_Personal_PersonalLayout.setVerticalGroup(
            pan_Personal_PersonalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_Personal_PersonalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan_Personal_PersonalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pan_Fingerprint, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pan_Personal_PersonalLayout.createSequentialGroup()
                        .addGroup(pan_Personal_PersonalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lab_No)
                            .addComponent(txt_No, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lab_HssNo)
                            .addComponent(txt_HssNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan_Personal_PersonalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_SocialSecurityNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lab_SocialSecurityNo))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan_Personal_PersonalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_NationalNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lab_NationalNo))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan_Personal_PersonalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_NhisNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lab_NhisNo))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan_Personal_PersonalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_PassportNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lab_HSSNO4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan_Personal_PersonalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_UserId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lab_UserId))
                        .addGap(7, 7, 7)
                        .addGroup(pan_Personal_PersonalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(pwd_PassWord, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lab_PassWord))
                        .addGap(7, 7, 7)
                        .addGroup(pan_Personal_PersonalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lab_Confirmation)
                            .addComponent(pwd_Confirmation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan_Personal_PersonalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_FirstName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_LastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lab_FirstName)
                            .addComponent(lab_LastName))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan_Personal_PersonalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cob_Rh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cob_Blood, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lab_Bloodtype)
                            .addComponent(cob_Sex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lab_Sex))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan_Personal_PersonalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pan_Personal_PersonalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lab_DateOfBirth)
                                .addComponent(txt_PlaceOfBirth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lab_PlaceOfBirth))
                            .addComponent(dateChooser_PersonalDateBirth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan_Personal_PersonalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lab_MaritalStatus)
                            .addComponent(cob_MaritalStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cob_Disability, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lab_Disability))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan_Personal_PersonalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lab_DisabilityDescription)
                            .addComponent(txt_DisabilityDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(143, Short.MAX_VALUE))
        );

        Emp_List.addTab("Personal", pan_Personal_Personal);

        lab_Website.setText("Personal Website :");

        lab_Email.setText("E - Mail :");

        lab_CellPhone.setText("Cell Phone Number :");

        lab_Phone.setText("Phone Number :");

        lab_Postal.setText("Postal Address :");

        lab_Address.setText("Residential Address :");

        pan_NextOfKin.setBorder(javax.swing.BorderFactory.createTitledBorder("Next Of Kin"));

        lab_KinFirstName.setText("First Name :");

        lab_KinLastName.setText("Last Name :");

        lab_KinAddress.setText("Residential Address :");

        lab_KinPostal.setText("Postal Address :");

        lab_KinPhone.setText("Phone Number :");

        lab_KinCellPhone.setText("Cell Phone Number :");

        lab_KinEmail.setText("E - Mail :");

        javax.swing.GroupLayout pan_NextOfKinLayout = new javax.swing.GroupLayout(pan_NextOfKin);
        pan_NextOfKin.setLayout(pan_NextOfKinLayout);
        pan_NextOfKinLayout.setHorizontalGroup(
            pan_NextOfKinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_NextOfKinLayout.createSequentialGroup()
                .addGroup(pan_NextOfKinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pan_NextOfKinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lab_KinCellPhone)
                        .addGroup(pan_NextOfKinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lab_KinPostal)
                            .addComponent(lab_KinAddress)
                            .addComponent(lab_KinPhone)))
                    .addComponent(lab_KinFirstName)
                    .addComponent(lab_KinEmail))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_NextOfKinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan_NextOfKinLayout.createSequentialGroup()
                        .addComponent(txt_KinFirstName, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lab_KinLastName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_KinLastName, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE))
                    .addComponent(txt_KinCellPhone, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
                    .addComponent(txt_KinEmail, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
                    .addComponent(txt_KinPhone, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
                    .addComponent(txt_KinPostal, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
                    .addComponent(txt_KinAddress, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE))
                .addContainerGap())
        );
        pan_NextOfKinLayout.setVerticalGroup(
            pan_NextOfKinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_NextOfKinLayout.createSequentialGroup()
                .addGroup(pan_NextOfKinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_KinFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_KinLastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_KinLastName)
                    .addComponent(lab_KinFirstName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_NextOfKinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab_KinAddress)
                    .addComponent(txt_KinAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_NextOfKinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_KinPostal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_KinPostal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_NextOfKinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_KinPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_KinPhone))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_NextOfKinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab_KinCellPhone)
                    .addComponent(txt_KinCellPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_NextOfKinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_KinEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_KinEmail)))
        );

        javax.swing.GroupLayout pan_ContactDetailsLayout = new javax.swing.GroupLayout(pan_ContactDetails);
        pan_ContactDetails.setLayout(pan_ContactDetailsLayout);
        pan_ContactDetailsLayout.setHorizontalGroup(
            pan_ContactDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_ContactDetailsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan_ContactDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan_ContactDetailsLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                        .addGroup(pan_ContactDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lab_Website)
                            .addComponent(lab_Email)
                            .addComponent(lab_CellPhone)
                            .addComponent(lab_Address)
                            .addComponent(lab_Postal)
                            .addComponent(lab_Phone))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan_ContactDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_Website, javax.swing.GroupLayout.DEFAULT_SIZE, 384, Short.MAX_VALUE)
                            .addComponent(txt_Address, javax.swing.GroupLayout.DEFAULT_SIZE, 384, Short.MAX_VALUE)
                            .addComponent(txt_Email, javax.swing.GroupLayout.DEFAULT_SIZE, 384, Short.MAX_VALUE)
                            .addComponent(txt_CellPhone, javax.swing.GroupLayout.DEFAULT_SIZE, 384, Short.MAX_VALUE)
                            .addComponent(txt_Phone, javax.swing.GroupLayout.DEFAULT_SIZE, 384, Short.MAX_VALUE)
                            .addComponent(txt_Postal, javax.swing.GroupLayout.DEFAULT_SIZE, 384, Short.MAX_VALUE))
                        .addGap(51, 51, 51))
                    .addComponent(pan_NextOfKin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(199, 199, 199))
        );
        pan_ContactDetailsLayout.setVerticalGroup(
            pan_ContactDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_ContactDetailsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan_ContactDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab_Address)
                    .addComponent(txt_Address, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_ContactDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_Postal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_Postal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_ContactDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_Phone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_Phone))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_ContactDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_CellPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_CellPhone))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_ContactDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_Email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_Email))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_ContactDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_Website, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_Website))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pan_NextOfKin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(146, Short.MAX_VALUE))
        );

        Emp_List.addTab("Contact Details", pan_ContactDetails);

        pan_Spouse.setBorder(javax.swing.BorderFactory.createTitledBorder("Spouse"));

        lab_SpouseFirstName.setText("Firstname of spouse :");

        lab_SpouseLastname.setText("Lastname of spouse :");

        lab_SpouseDateOfBirth.setText("Spouse's date of birth :");

        lab_SpouseNhisNo.setText("Spouse's NHIS No. :");

        lab_StaffType.setText("Staff Type :");

        lab_StaffCategory.setText("Staff Category ;");

        cob_StaffCategory.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "Junior Staff 1", "Junior Staff 2", "Senior Staff" }));

        cob_StaffType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "Permanent", "Contract", "Training/Attachment", "Support", "National Youth Employee", "Casual Worker", "House Job 1", "House Job 2" }));

        javax.swing.GroupLayout pan_SpouseLayout = new javax.swing.GroupLayout(pan_Spouse);
        pan_Spouse.setLayout(pan_SpouseLayout);
        pan_SpouseLayout.setHorizontalGroup(
            pan_SpouseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_SpouseLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(pan_SpouseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lab_SpouseDateOfBirth)
                    .addComponent(lab_SpouseFirstName)
                    .addComponent(lab_StaffType))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_SpouseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_SpouseFirstname, javax.swing.GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE)
                    .addComponent(cob_StaffType, 0, 247, Short.MAX_VALUE)
                    .addComponent(dateChooser_SpouseDateOfBirth, javax.swing.GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_SpouseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lab_SpouseLastname)
                    .addComponent(lab_SpouseNhisNo)
                    .addComponent(lab_StaffCategory))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_SpouseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txt_SpouseLastname, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE)
                    .addComponent(txt_SpouseNhisNo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE)
                    .addComponent(cob_StaffCategory, javax.swing.GroupLayout.Alignment.LEADING, 0, 239, Short.MAX_VALUE))
                .addContainerGap())
        );
        pan_SpouseLayout.setVerticalGroup(
            pan_SpouseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_SpouseLayout.createSequentialGroup()
                .addGroup(pan_SpouseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_SpouseFirstname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_SpouseFirstName)
                    .addComponent(txt_SpouseLastname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_SpouseLastname))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_SpouseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan_SpouseLayout.createSequentialGroup()
                        .addComponent(dateChooser_SpouseDateOfBirth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan_SpouseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lab_StaffType)
                            .addComponent(cob_StaffType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pan_SpouseLayout.createSequentialGroup()
                        .addComponent(txt_SpouseNhisNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan_SpouseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lab_StaffCategory)
                            .addComponent(cob_StaffCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(lab_SpouseNhisNo)
                    .addComponent(lab_SpouseDateOfBirth)))
        );

        pan_Children.setBorder(javax.swing.BorderFactory.createTitledBorder("Children"));

        tab_Children.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tab_Children.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tab_ChildrenMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tab_Children);

        lab_ChildrenName.setText("Full name :");

        lab_ChildrenDateBirth.setText("Date of Birth :");

        lab_ChildrenNhisNo.setText("NHIS No. :");

        btn_ChildrenAdd.setText("Add");
        btn_ChildrenAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ChildrenAddActionPerformed(evt);
            }
        });

        btn_ChildrenDelete.setText("Delete");
        btn_ChildrenDelete.setEnabled(false);
        btn_ChildrenDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ChildrenDeleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pan_ChildrenLayout = new javax.swing.GroupLayout(pan_Children);
        pan_Children.setLayout(pan_ChildrenLayout);
        pan_ChildrenLayout.setHorizontalGroup(
            pan_ChildrenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_ChildrenLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan_ChildrenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 718, Short.MAX_VALUE)
                    .addGroup(pan_ChildrenLayout.createSequentialGroup()
                        .addGroup(pan_ChildrenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lab_ChildrenDateBirth)
                            .addComponent(lab_ChildrenName)
                            .addComponent(lab_ChildrenNhisNo))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan_ChildrenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dateChooser_ChildrenDateBirth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_ChildrenNhisNo, javax.swing.GroupLayout.DEFAULT_SIZE, 537, Short.MAX_VALUE)
                            .addComponent(txt_ChildrenName, javax.swing.GroupLayout.DEFAULT_SIZE, 537, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan_ChildrenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btn_ChildrenAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_ChildrenDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        pan_ChildrenLayout.setVerticalGroup(
            pan_ChildrenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_ChildrenLayout.createSequentialGroup()
                .addGroup(pan_ChildrenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_ChildrenName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_ChildrenName)
                    .addComponent(btn_ChildrenAdd))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_ChildrenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan_ChildrenLayout.createSequentialGroup()
                        .addGroup(pan_ChildrenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lab_ChildrenDateBirth)
                            .addComponent(dateChooser_ChildrenDateBirth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan_ChildrenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_ChildrenNhisNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lab_ChildrenNhisNo)))
                    .addComponent(btn_ChildrenDelete))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pan_Spouse, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(pan_Children, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pan_Spouse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pan_Children, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(106, Short.MAX_VALUE))
        );

        Emp_List.addTab("Family Information", jPanel7);

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Emp_List, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 776, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btn_Save, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_Cancel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Emp_List, javax.swing.GroupLayout.PREFERRED_SIZE, 529, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_Cancel)
                    .addComponent(btn_Save))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cob_DisabilityItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cob_DisabilityItemStateChanged
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED && cob_Disability.getSelectedIndex() == 1) {
            txt_DisabilityDescription.setEnabled(true);
        } else {
            txt_DisabilityDescription.setText("");
            txt_DisabilityDescription.setEnabled(false);
        }
}//GEN-LAST:event_cob_DisabilityItemStateChanged

    private void txt_SocialSecurityNoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_SocialSecurityNoKeyReleased
        this.btn_Save.setEnabled(isCanSave());
}//GEN-LAST:event_txt_SocialSecurityNoKeyReleased

    private void txt_NationalNoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_NationalNoKeyReleased
        this.btn_Save.setEnabled(isCanSave());
}//GEN-LAST:event_txt_NationalNoKeyReleased

    private void txt_NhisNoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_NhisNoKeyReleased
        this.btn_Save.setEnabled(isCanSave());
}//GEN-LAST:event_txt_NhisNoKeyReleased

    private void txt_LastNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_LastNameKeyReleased
        this.btn_Save.setEnabled(isCanSave());
}//GEN-LAST:event_txt_LastNameKeyReleased

    private void txt_HssNoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_HssNoKeyReleased
        this.btn_Save.setEnabled(isCanSave());
}//GEN-LAST:event_txt_HssNoKeyReleased

    private void txt_UserIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_UserIdFocusLost

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
        if(btn_Save.isEnabled()) {
            try{
                PreparedStatement pstmt = DBC.prepareStatement("INSERT INTO staff_fingertemplate VALUES(?, ?, ?)");
                FingerPrintScanner.enroll(this.txt_No.getText(),pstmt);
            } catch (SQLException e) {
                ErrorMessage.setData("Patients", "Frm_PatientMod" ,"btn_EnrollActionPerformed(java.awt.event.ActionEvent evt)",
                        e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
                System.out.println(e);
            }
        }
}//GEN-LAST:event_btn_EnrollActionPerformed

    private void tab_ChildrenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab_ChildrenMouseClicked
        if (tab_Children.getSelectedRow() != -1) {
            btn_ChildrenDelete.setEnabled(true);
        }
}//GEN-LAST:event_tab_ChildrenMouseClicked

    private void btn_ChildrenAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ChildrenAddActionPerformed
        if (txt_ChildrenName.getText() != null && txt_ChildrenNhisNo.getText() != null
        && !txt_ChildrenName.getText().trim().equals("") && !txt_ChildrenNhisNo.getText().trim().equals("")) {
            String sql = "";
            int s_no = Integer.parseInt(this.txt_No.getText());
            try{
                   sql ="INSERT INTO children (no, s_no, name, date_birth, nhis_no) " +
                        "SELECT IF(COUNT(no) <> 0,MAX(no)+1,0), " +
                        "'"+s_no+"', " +
                        "'"+txt_ChildrenName.getText()+"', " +
                        "'"+dateChooser_ChildrenDateBirth.getValue()+"', " +
                        "'"+txt_ChildrenNhisNo.getText()+"' " +
                        "FROM children";
               DBC.executeUpdate(sql);
               reSetChildrenTable();
            }catch(SQLException e){
                System.out.println(e);
            }
        }
}//GEN-LAST:event_btn_ChildrenAddActionPerformed

    private void btn_ChildrenDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ChildrenDeleteActionPerformed
        String sql = "DELETE FROM children " +
                     "WHERE no = "+ this.tab_Children.getValueAt(this.tab_Children.getSelectedRow(),0);
        try{
            DBC.executeUpdate(sql);
            reSetChildrenTable();
        }catch(SQLException e){
            System.out.println(e);
        }
}//GEN-LAST:event_btn_ChildrenDeleteActionPerformed

    private void btn_CancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CancelActionPerformed
        FingerPrintScanner.stop();
        new Frm_HRM(m_Sno,m_UUID).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btn_CancelActionPerformed

    private void btn_SaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SaveActionPerformed
        String sql = null;
        try {
              sql = "UPDATE staff_info SET "+
                    "s_id = '"+this.txt_UserId.getText()+"', " +
                    "passwd = '"+HISPassword.enCode(new String(this.pwd_PassWord.getPassword()))+"', " +
                    "hss_no = '"+this.txt_HssNo.getText()+"', " +
                    "ss_no = '"+this.txt_SocialSecurityNo.getText()+"', " +
                    "nia_no = '"+this.txt_NationalNo.getText()+"', " +
                    "nhis_no = '"+this.txt_NhisNo.getText()+"', " +
                    "passport_no = '"+this.txt_PassportNo.getText()+"', " +
                    "firstname = '"+this.txt_FirstName.getText()+"', " +
                    "lastname = '"+this.txt_LastName.getText()+"', " +
                    "sex = '"+this.cob_Sex.getSelectedItem()+"', " +
                    "date_birth = '"+this.dateChooser_PersonalDateBirth.getValue()+"', " +
                    "place_birth = '"+this.txt_PlaceOfBirth.getText()+"', " +
                    "marital_status = '"+this.cob_MaritalStatus.getSelectedIndex()+"', " +
                    "kin_firstname = '"+this.txt_KinFirstName.getText()+"', " +
                    "kin_lastname = '"+this.txt_KinLastName.getText()+"', " +
                    "kin_address = '"+this.txt_KinAddress.getText()+"', " +
                    "kin_postal = '"+this.txt_KinPostal.getText()+"', " +
                    "kin_phone = '"+this.txt_KinPhone.getText()+"', " +
                    "kin_cellphone = '"+this.txt_KinCellPhone.getText()+"', " +
                    "kin_email = '"+this.txt_KinEmail.getText()+"', " +
                    "address = '"+this.txt_Address.getText()+"', " +
                    "postal = '"+this.txt_Postal.getText()+"', " +
                    "phone = '"+this.txt_Phone.getText()+"', " +
                    "cellphone = '"+this.txt_CellPhone.getText()+"', " +
                    "email = '"+this.txt_Email.getText()+"', " +
                    "website = '"+this.txt_Website.getText()+"', " +
                    "bloodgroup = '"+this.cob_Blood.getSelectedItem()+"', " +
                    "rh_type = '"+this.cob_Rh.getSelectedItem()+"', " +
                    "disability_status = '"+this.cob_Disability.getSelectedIndex()+"', " +
                    "disability_description = '"+this.txt_DisabilityDescription.getText()+"', " +
                    "spouse_firstname = '"+this.txt_SpouseFirstname.getText()+"', " +
                    "spouse_lastname = '"+this.txt_SpouseLastname.getText()+"', " +
                    "spouse_date_birth = '"+this.dateChooser_SpouseDateOfBirth.getValue()+"', " +
                    "spouse_nhis_no  = '"+this.txt_SpouseNhisNo.getText()+"'," +
                    "staff_type = '"+this.cob_StaffType.getSelectedIndex()+"', " +
                    "staff_category = '"+this.cob_StaffCategory.getSelectedIndex()+"' " +
                    ",exist = true "+
                    "WHERE s_no = '"+this.txt_No.getText()+"' ";
            DBC.executeUpdate(sql);
           // cc.johnwu.login.ChangesLog.ChangesLog("staff_info", this.txt_No.getText(), "add");

         //   JOptionPane.showMessageDialog(new Frame(), paragraph.getLanguage(message, "SAVECOMPLETE"));
            btn_CancelActionPerformed(null);
          //  m_frame.onPatientMod(this.txt_No.getText());
        } catch (SQLException e) {
           // ErrorMessage.setData("Patients", "Frm_PatientMod" ,"btn_OKActionPerformed(java.awt.event.ActionEvent evt)",
             //       e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            System.out.println(e);
        }
    }//GEN-LAST:event_btn_SaveActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane Emp_List;
    private javax.swing.JButton btn_Cancel;
    private javax.swing.JButton btn_ChildrenAdd;
    private javax.swing.JButton btn_ChildrenDelete;
    private javax.swing.JButton btn_Enroll;
    private javax.swing.JButton btn_Save;
    private javax.swing.JComboBox cob_Blood;
    private javax.swing.JComboBox cob_Disability;
    private javax.swing.JComboBox cob_MaritalStatus;
    private javax.swing.JComboBox cob_Rh;
    private javax.swing.JComboBox cob_Sex;
    private javax.swing.JComboBox cob_StaffCategory;
    private javax.swing.JComboBox cob_StaffType;
    private cc.johnwu.date.DateComboBox dateChooser_ChildrenDateBirth;
    private cc.johnwu.date.DateComboBox dateChooser_PersonalDateBirth;
    private cc.johnwu.date.DateComboBox dateChooser_SpouseDateOfBirth;
    private cc.johnwu.finger.FingerPrintViewer fingerPrintViewer1;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JLabel lab_Address;
    private javax.swing.JLabel lab_Bloodtype;
    private javax.swing.JLabel lab_CellPhone;
    private javax.swing.JLabel lab_ChildrenDateBirth;
    private javax.swing.JLabel lab_ChildrenName;
    private javax.swing.JLabel lab_ChildrenNhisNo;
    private javax.swing.JLabel lab_Confirmation;
    private javax.swing.JLabel lab_DateOfBirth;
    private javax.swing.JLabel lab_Disability;
    private javax.swing.JLabel lab_DisabilityDescription;
    private javax.swing.JLabel lab_Email;
    private javax.swing.JLabel lab_FirstName;
    private javax.swing.JLabel lab_HSSNO4;
    private javax.swing.JLabel lab_HssNo;
    private javax.swing.JLabel lab_KinAddress;
    private javax.swing.JLabel lab_KinCellPhone;
    private javax.swing.JLabel lab_KinEmail;
    private javax.swing.JLabel lab_KinFirstName;
    private javax.swing.JLabel lab_KinLastName;
    private javax.swing.JLabel lab_KinPhone;
    private javax.swing.JLabel lab_KinPostal;
    private javax.swing.JLabel lab_LastName;
    private javax.swing.JLabel lab_MaritalStatus;
    private javax.swing.JLabel lab_NationalNo;
    private javax.swing.JLabel lab_NhisNo;
    private javax.swing.JLabel lab_No;
    private javax.swing.JLabel lab_PassWord;
    private javax.swing.JLabel lab_Phone;
    private javax.swing.JLabel lab_PlaceOfBirth;
    private javax.swing.JLabel lab_Postal;
    private javax.swing.JLabel lab_Sex;
    private javax.swing.JLabel lab_SocialSecurityNo;
    private javax.swing.JLabel lab_SpouseDateOfBirth;
    private javax.swing.JLabel lab_SpouseFirstName;
    private javax.swing.JLabel lab_SpouseLastname;
    private javax.swing.JLabel lab_SpouseNhisNo;
    private javax.swing.JLabel lab_StaffCategory;
    private javax.swing.JLabel lab_StaffType;
    private javax.swing.JLabel lab_UserId;
    private javax.swing.JLabel lab_Website;
    private javax.swing.JPanel pan_Children;
    private javax.swing.JPanel pan_ContactDetails;
    private javax.swing.JPanel pan_Fingerprint;
    private javax.swing.JPanel pan_NextOfKin;
    private javax.swing.JPanel pan_Personal_Personal;
    private javax.swing.JPanel pan_Spouse;
    private javax.swing.JPasswordField pwd_Confirmation;
    private javax.swing.JPasswordField pwd_PassWord;
    private javax.swing.JTable tab_Children;
    private javax.swing.JTextField txt_Address;
    private javax.swing.JTextField txt_CellPhone;
    private javax.swing.JTextField txt_ChildrenName;
    private javax.swing.JTextField txt_ChildrenNhisNo;
    private javax.swing.JTextField txt_DisabilityDescription;
    private javax.swing.JTextField txt_Email;
    private javax.swing.JTextField txt_FirstName;
    private javax.swing.JTextField txt_HssNo;
    private javax.swing.JTextField txt_KinAddress;
    private javax.swing.JTextField txt_KinCellPhone;
    private javax.swing.JTextField txt_KinEmail;
    private javax.swing.JTextField txt_KinFirstName;
    private javax.swing.JTextField txt_KinLastName;
    private javax.swing.JTextField txt_KinPhone;
    private javax.swing.JTextField txt_KinPostal;
    private javax.swing.JTextField txt_LastName;
    private javax.swing.JTextField txt_NationalNo;
    private javax.swing.JTextField txt_NhisNo;
    private javax.swing.JTextField txt_No;
    private javax.swing.JTextField txt_PassportNo;
    private javax.swing.JTextField txt_Phone;
    private javax.swing.JTextField txt_PlaceOfBirth;
    private javax.swing.JTextField txt_Postal;
    private javax.swing.JTextField txt_SocialSecurityNo;
    private javax.swing.JTextField txt_SpouseFirstname;
    private javax.swing.JTextField txt_SpouseLastname;
    private javax.swing.JTextField txt_SpouseNhisNo;
    private javax.swing.JTextField txt_UserId;
    private javax.swing.JTextField txt_Website;
    // End of variables declaration//GEN-END:variables

     public void onFingerDown() {
            this.btn_Enroll.setEnabled(isCanSave());
    }

    public void showImage(BufferedImage bufferedimage, String msg) {
        this.fingerPrintViewer1.showImage(bufferedimage);
        this.fingerPrintViewer1.setTitle(msg);
    }
}
/**staff_fingertemplate*/