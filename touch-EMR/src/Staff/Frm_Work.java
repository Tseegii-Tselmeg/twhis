package Staff;

import Multilingual.language;
import cc.johnwu.sql.*;
import java.awt.event.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JList;
import java.sql.SQLException;

/**
 *
 * @author steven
 */
public class Frm_Work extends javax.swing.JFrame {
      private int m_Sno;
      private String m_UUID = null;
       /*多國語言變數*/
    private language paragraph = new language();
    private String[] line = paragraph.setlanguage("WORK").split("\n") ;
    private String[] message = paragraph.setlanguage("MESSAGE").split("\n") ;
      public Frm_Work(int sno, String uuid) {
        m_UUID = uuid;
        m_Sno = sno;
        initComponents();
        initComboBox();
        init();
        initLanguage();
        
      }

    public void initComboBox(){
        ResultSet rs_dep = null;
        ResultSet rs_pos = null;
        ResultSet rs_permission = null;
        ResultSet rs_poli = null;
        try {

/*----------設定部門(Administrative)的combobox ----------*/
            rs_dep = DBC.executeQuery("SELECT * FROM department GROUP BY name");
            this.cob_TransferDep.addItem("");
            this.cob_Administrative.addItem("");
            while (rs_dep.next()) {
                this.cob_Administrative.addItem(rs_dep.getString("name"));
                this.cob_TransferDep.addItem(rs_dep.getString("name"));
            }
/*----------設定職位的combobox ----------*/
            rs_pos = DBC.executeQuery("SELECT * FROM position GROUP BY name");
            this.cob_Position.addItem("");
            while (rs_pos.next()) {
                this.cob_Position.addItem(rs_pos.getString("name"));
            }
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

    private void initLanguage() {
        this.btn_Save.setText(paragraph.getLanguage(message, "SAVE"));
        this.btn_Cancel.setText(paragraph.getLanguage(message, "CANCEL"));
        this.pan_EmployeeStatus.setTitleAt(0, paragraph.getLanguage(line, "DEMPARTMENT"));
        this.pan_EmployeeStatus.setTitleAt(1, paragraph.getLanguage(line, "GRADE"));
        this.pan_EmployeeStatus.setTitleAt(2, paragraph.getLanguage(line, "SALARY"));
        this.pan_EmployeeStatus.setTitleAt(3, paragraph.getLanguage(line, "EMPLOYEESTATUS"));
        this.pan_EmployeeStatus.setTitleAt(4, paragraph.getLanguage(line, "TRANSFERINTERDEPARTMENTAL"));
        this.pan_Administrative.setBorder(javax.swing.BorderFactory.createTitledBorder(paragraph.getLanguage(line, "ADMINISTRATIVE")));
       
        this.lab_AdministrativeUnit.setText(paragraph.getLanguage(line, "UINT"));
       
        this.btn_AdministrativeAdd.setText(paragraph.getLanguage(line, "ADD"));
       
        this.btn_AdministrativeDelete.setText(paragraph.getLanguage(line, "DELETE"));
        
        this.lab_Group.setText(paragraph.getLanguage(line, "GROUP"));
        this.lab_Poli.setText(paragraph.getLanguage(line, "POLICLINIC"));
        this.lab_GradeLevel.setText(paragraph.getLanguage(line, "GRADELEVEL"));
        this.lab_CategoryPost.setText(paragraph.getLanguage(line, "CATEGORYPOST"));
        this.lab_Position.setText(paragraph.getLanguage(line, "POSITION"));
        this.lab_SalaryLevel.setText(paragraph.getLanguage(line, "LEVEL"));
        this.lab_Step.setText(paragraph.getLanguage(line, "STEP"));
        this.lab_GroosSalary.setText(paragraph.getLanguage(line, "GROOSSALARY"));
        this.lab_FirstAppointmentDate.setText(paragraph.getLanguage(line, "DATEOFFIRSTAPPOINTMENT"));
        this.lab_FirstAppointmentStafftype.setText(paragraph.getLanguage(line, "Staff Type at first appointment :"));
        this.lab_FirstAppointmentGrade.setText(paragraph.getLanguage(line, "GRADEATFIRSTAPPOINTMENT"));
        this.lab_LastPromotionDate.setText(paragraph.getLanguage(line, "DATEOFLASTPROMOTION"));
        this.lab_Employeestatus.setText(paragraph.getLanguage(line, "EMPLOYEESTATUS"));
        this.pan_Suspended.setBorder(javax.swing.BorderFactory.createTitledBorder(paragraph.getLanguage(line, "SUSPENDED")));
        this.pan_Dismissed.setBorder(javax.swing.BorderFactory.createTitledBorder(paragraph.getLanguage(line, "DISMISSED")));
        this.pan_Retired.setBorder(javax.swing.BorderFactory.createTitledBorder(paragraph.getLanguage(line, "RETIRED")));
        this.lab_DateSuspension.setText(paragraph.getLanguage(line, "DATEOFSUSPENSION"));
        this.lab_DateResumption.setText(paragraph.getLanguage(line, "DATEOFRESUMPTION"));
        this.lab_SuspendedReason.setText(paragraph.getLanguage(line, "REASON"));
        this.lab_DateDismissal.setText(paragraph.getLanguage(line, "DATEOFDISMISSAL"));
        this.lab_DismissedReason.setText(paragraph.getLanguage(line, "REASON"));
        this.lab_DateRetirement.setText(paragraph.getLanguage(line, "DATEOFRETIREMENT"));
        this.lab_ToDepartment.setText(paragraph.getLanguage(line, "TODEPARTMENT"));
        this.lbl_Dateoftransfer.setText(paragraph.getLanguage(line, "DATEOFTRANSFER"));
        cob_EmployeeStatus.setModel(
                new javax.swing.DefaultComboBoxModel(
                    new String[] {paragraph.getLanguage(line, "ACTIVE"),
                    paragraph.getLanguage(line, "SUSPENDED"),
                    paragraph.getLanguage(line, "DISMISSED"),
                    paragraph.getLanguage(line, "RETIRED") }
        ));


    }

    private void init(){//修改資料
        if (m_UUID != null) {
            this.setTitle(paragraph.getLanguage(line, "WORKJOBPROFILE") + " ("+paragraph.getLanguage(line, "NEWSTAFFNO") +" "+m_Sno +" )");
        } else {
            this.setTitle(paragraph.getLanguage(line, "WORKJOBPROFILE") +  " ("+paragraph.getLanguage(line, "EDITSTAFFNO") +" "+m_Sno +" )");
        }

       
        this.setExtendedState(Frm_Work.MAXIMIZED_BOTH);  // 最大化
        this.setLocationRelativeTo(this);//視窗顯示至中
        addWindowListener(new WindowAdapter() {//視窗關閉事件
            @Override
            public void windowClosing(WindowEvent windowevent)
            {
                btn_CancelActionPerformed(null);
            }
        });
        this.btn_Save.setVisible(true);
        String sql = "";
        ResultSet rs = null;
        try {
            sql="Select * From staff_info where staff_info.s_no="+m_Sno+" and staff_info.exist=1";
            rs = DBC.executeQuery(sql);
            rs.next();

            this.cob_EmployeeStatus.setSelectedIndex(Integer.parseInt(rs.getString("employee_status")));
            this.dateChooser_DateSuspension.setValue(rs.getString("date_suspension"));
            this.dateChooser_DateResumption.setValue(rs.getString("date_resumption"));
            this.txta_SuspendedReason.setText(rs.getString("suspension_reason"));
            this.dateChooser_DateDismissal.setValue(rs.getString("date_dismissal"));
            this.txta_DismissedReason.setText(rs.getString("dismissal_reason"));
            this.dateChooser_DateRetirement.setValue(rs.getString("date_retirement"));

            this.txt_GradeLevel.setText(rs.getString("grade_level"));
            this.txt_CategoryPost.setText(rs.getString("category_post"));
            this.txt_SalaryLevel.setText(rs.getString("level"));
            this.txt_Step.setText(rs.getString("step"));
            this.txt_GroosSalary.setText(String.valueOf(rs.getInt("groos_salary")));
            this.dateCob_FirstAppointmentDate.setValue(rs.getString("first_appointment_date"));
            this.txt_FirstAppointmentStafftype.setText(rs.getString("first_appointment_stafftype"));
            this.txt_FirstAppointmentGrade.setText(rs.getString("first_appointment_grade"));
            this.dateCob_LastPromotionDate.setValue(rs.getString("last_promotion_date"));
            this.dateChooser_TransferDate.setValue(rs.getString("transfer_date"));


            initWorkCob();
            showDepartmentList("administrative",list_Administrative);
            //showDepartmentList("clinical",list_Clinical);

        } catch (SQLException e) {

            System.out.println(e);
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
            for (int i = 0; i < cob_Position.getItemCount(); i++) {
                if (rs_staffInfo.getString("posi_guid") != null) {
                    if (rs_staffInfo.getString("posi_guid").equals(rs_pos.getString("guid"))) {
                        cob_Position.setSelectedIndex(i + 1);
                        break;
                    } else {
                        rs_pos.next();
                    }
                } else {
                    cob_Position.setSelectedIndex(0);
                }
            }
            /*將combobox設定為員工所屬部門 */
            for (int i = 0; i < cob_TransferDep.getItemCount(); i++) {
                if (rs_staffInfo.getString("dep_guid") != null) {
                    if (rs_staffInfo.getString("dep_guid").equals(rs_dep.getString("guid"))) {
                        cob_TransferDep.setSelectedIndex(i + 1);
                        break;
                    } else {
                        rs_dep.next();
                    }
                } else {
                    cob_TransferDep.setSelectedIndex(0);
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
            Logger.getLogger(Frm_Work.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**顯示Department 兩張list*/
    private void showDepartmentList(String listName,JList list){
        int s_no = m_Sno;
        ResultSet rs = null;
        String[] listValue = null;
        String sql = null;

        if (listName.equals("administrative")) {
             sql = "SELECT department.name FROM department,administrative WHERE department.guid = administrative.dep_guid AND administrative.s_no = '"+s_no+"'";

        } else if (listName.equals("clinical")) {
             sql = "SELECT policlinic.name FROM policlinic,clinical WHERE policlinic.guid = clinical.poli_guid AND clinical.s_no = '"+s_no+"'";

        }
     try {
            rs = DBC.executeQuery(sql);
            rs.last();
            int rowCount = rs.getRow();
            listValue = new String [rowCount];
            rs.beforeFirst();
            for (int i = 0; i < rowCount; i++) {
                rs.next();
                listValue[i] = rs.getString("name");
            }
            list.setListData(listValue);
        } catch (SQLException ex) {
            Logger.getLogger(Frm_Work.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pan_EmployeeStatus = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        pan_Administrative = new javax.swing.JPanel();
        lab_AdministrativeUnit = new javax.swing.JLabel();
        cob_Administrative = new javax.swing.JComboBox();
        btn_AdministrativeDelete = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        list_Administrative = new javax.swing.JList();
        btn_AdministrativeAdd = new javax.swing.JButton();
        lab_Group = new javax.swing.JLabel();
        cob_Permission = new javax.swing.JComboBox();
        lab_Poli = new javax.swing.JLabel();
        cob_Poli = new javax.swing.JComboBox();
        jPanel10 = new javax.swing.JPanel();
        lab_GradeLevel = new javax.swing.JLabel();
        lab_CategoryPost = new javax.swing.JLabel();
        lab_Position = new javax.swing.JLabel();
        cob_Position = new javax.swing.JComboBox();
        txt_GradeLevel = new javax.swing.JTextField();
        txt_CategoryPost = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        lab_SalaryLevel = new javax.swing.JLabel();
        lab_Step = new javax.swing.JLabel();
        lab_GroosSalary = new javax.swing.JLabel();
        lab_FirstAppointmentDate = new javax.swing.JLabel();
        lab_FirstAppointmentStafftype = new javax.swing.JLabel();
        lab_LastPromotionDate = new javax.swing.JLabel();
        txt_GroosSalary = new javax.swing.JTextField();
        dateCob_FirstAppointmentDate = new cc.johnwu.date.DateComboBox();
        dateCob_LastPromotionDate = new cc.johnwu.date.DateComboBox();
        txt_FirstAppointmentStafftype = new javax.swing.JTextField();
        lab_FirstAppointmentGrade = new javax.swing.JLabel();
        txt_FirstAppointmentGrade = new javax.swing.JTextField();
        txt_SalaryLevel = new javax.swing.JTextField();
        txt_Step = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        lab_Employeestatus = new javax.swing.JLabel();
        cob_EmployeeStatus = new javax.swing.JComboBox();
        pan_Suspended = new javax.swing.JPanel();
        lab_DateSuspension = new javax.swing.JLabel();
        lab_DateResumption = new javax.swing.JLabel();
        lab_SuspendedReason = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txta_SuspendedReason = new javax.swing.JTextArea();
        dateChooser_DateSuspension = new cc.johnwu.date.DateComboBox();
        dateChooser_DateResumption = new cc.johnwu.date.DateComboBox();
        pan_Dismissed = new javax.swing.JPanel();
        lab_DateDismissal = new javax.swing.JLabel();
        lab_DismissedReason = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txta_DismissedReason = new javax.swing.JTextArea();
        dateChooser_DateDismissal = new cc.johnwu.date.DateComboBox();
        pan_Retired = new javax.swing.JPanel();
        lab_DateRetirement = new javax.swing.JLabel();
        dateChooser_DateRetirement = new cc.johnwu.date.DateComboBox();
        jPanel4 = new javax.swing.JPanel();
        lab_ToDepartment = new javax.swing.JLabel();
        lbl_Dateoftransfer = new javax.swing.JLabel();
        cob_TransferDep = new javax.swing.JComboBox();
        dateChooser_TransferDate = new cc.johnwu.date.DateComboBox();
        btn_Save = new javax.swing.JButton();
        btn_Cancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);

        pan_Administrative.setBorder(javax.swing.BorderFactory.createTitledBorder("Administrative"));

        lab_AdministrativeUnit.setText("Unit :");

        btn_AdministrativeDelete.setText("Delete");
        btn_AdministrativeDelete.setEnabled(false);
        btn_AdministrativeDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_AdministrativeDeleteActionPerformed(evt);
            }
        });

        list_Administrative.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                list_AdministrativeMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(list_Administrative);

        btn_AdministrativeAdd.setText("Add");
        btn_AdministrativeAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_AdministrativeAddActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pan_AdministrativeLayout = new javax.swing.GroupLayout(pan_Administrative);
        pan_Administrative.setLayout(pan_AdministrativeLayout);
        pan_AdministrativeLayout.setHorizontalGroup(
            pan_AdministrativeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_AdministrativeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lab_AdministrativeUnit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cob_Administrative, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_AdministrativeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btn_AdministrativeDelete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_AdministrativeAdd, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 394, Short.MAX_VALUE)
                .addContainerGap())
        );
        pan_AdministrativeLayout.setVerticalGroup(
            pan_AdministrativeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_AdministrativeLayout.createSequentialGroup()
                .addGroup(pan_AdministrativeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan_AdministrativeLayout.createSequentialGroup()
                        .addGroup(pan_AdministrativeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lab_AdministrativeUnit)
                            .addComponent(cob_Administrative, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_AdministrativeAdd))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_AdministrativeDelete))
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        lab_Group.setText("Group :");

        lab_Poli.setText("Policlinic :");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(pan_Administrative, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lab_Poli)
                            .addComponent(lab_Group, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cob_Permission, 0, 281, Short.MAX_VALUE)
                            .addComponent(cob_Poli, 0, 281, Short.MAX_VALUE))
                        .addGap(417, 417, 417))))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pan_Administrative, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cob_Poli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_Poli))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab_Group)
                    .addComponent(cob_Permission, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(279, Short.MAX_VALUE))
        );

        pan_EmployeeStatus.addTab("Department", jPanel6);

        lab_GradeLevel.setText("Grade Level :");

        lab_CategoryPost.setText("Category Post :");

        lab_Position.setText("Position :");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lab_GradeLevel)
                    .addComponent(lab_CategoryPost)
                    .addComponent(lab_Position))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cob_Position, javax.swing.GroupLayout.PREFERRED_SIZE, 437, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_GradeLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 437, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_CategoryPost, javax.swing.GroupLayout.PREFERRED_SIZE, 437, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(249, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_GradeLevel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_GradeLevel))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab_CategoryPost)
                    .addComponent(txt_CategoryPost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cob_Position, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_Position))
                .addContainerGap(403, Short.MAX_VALUE))
        );

        pan_EmployeeStatus.addTab("Grade", jPanel10);

        lab_SalaryLevel.setText("Level :");

        lab_Step.setText("Step :");

        lab_GroosSalary.setText("Groos Salary :");

        lab_FirstAppointmentDate.setText("Date of first appointment :");

        lab_FirstAppointmentStafftype.setText("Staff Type at first appointment :");

        lab_LastPromotionDate.setText("Date of last promotion :");

        lab_FirstAppointmentGrade.setText("Grade at first appointment :");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lab_GroosSalary)
                    .addComponent(lab_FirstAppointmentGrade)
                    .addComponent(lab_FirstAppointmentDate)
                    .addComponent(lab_FirstAppointmentStafftype)
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lab_Step)
                        .addComponent(lab_SalaryLevel))
                    .addComponent(lab_LastPromotionDate))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(dateCob_LastPromotionDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dateCob_FirstAppointmentDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_GroosSalary)
                    .addComponent(txt_FirstAppointmentStafftype)
                    .addComponent(txt_FirstAppointmentGrade)
                    .addComponent(txt_Step, javax.swing.GroupLayout.DEFAULT_SIZE, 339, Short.MAX_VALUE)
                    .addComponent(txt_SalaryLevel))
                .addContainerGap(270, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab_SalaryLevel)
                    .addComponent(txt_SalaryLevel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab_Step)
                    .addComponent(txt_Step, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_GroosSalary, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_GroosSalary))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dateCob_FirstAppointmentDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_FirstAppointmentDate))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_FirstAppointmentStafftype, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_FirstAppointmentStafftype))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_FirstAppointmentGrade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_FirstAppointmentGrade))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dateCob_LastPromotionDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_LastPromotionDate))
                .addContainerGap(319, Short.MAX_VALUE))
        );

        pan_EmployeeStatus.addTab("Salary", jPanel9);

        lab_Employeestatus.setText("Employee status :");

        cob_EmployeeStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Active", "Suspended", "Dismissed", "Retired" }));
        cob_EmployeeStatus.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cob_EmployeeStatusItemStateChanged(evt);
            }
        });

        pan_Suspended.setBorder(javax.swing.BorderFactory.createTitledBorder("Suspended"));

        lab_DateSuspension.setText("Date of suspension :");

        lab_DateResumption.setText("Date of resumption :");

        lab_SuspendedReason.setText("Reason :");

        txta_SuspendedReason.setColumns(20);
        txta_SuspendedReason.setRows(3);
        txta_SuspendedReason.setEnabled(false);
        jScrollPane2.setViewportView(txta_SuspendedReason);

        dateChooser_DateSuspension.setEnabled(false);

        dateChooser_DateResumption.setEnabled(false);

        javax.swing.GroupLayout pan_SuspendedLayout = new javax.swing.GroupLayout(pan_Suspended);
        pan_Suspended.setLayout(pan_SuspendedLayout);
        pan_SuspendedLayout.setHorizontalGroup(
            pan_SuspendedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_SuspendedLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan_SuspendedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan_SuspendedLayout.createSequentialGroup()
                        .addComponent(lab_DateSuspension)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dateChooser_DateSuspension, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pan_SuspendedLayout.createSequentialGroup()
                        .addGroup(pan_SuspendedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lab_SuspendedReason)
                            .addComponent(lab_DateResumption))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan_SuspendedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dateChooser_DateResumption, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE))))
                .addContainerGap())
        );
        pan_SuspendedLayout.setVerticalGroup(
            pan_SuspendedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_SuspendedLayout.createSequentialGroup()
                .addGroup(pan_SuspendedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lab_DateSuspension)
                    .addComponent(dateChooser_DateSuspension, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_SuspendedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lab_DateResumption)
                    .addComponent(dateChooser_DateResumption, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_SuspendedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lab_SuspendedReason)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pan_Dismissed.setBorder(javax.swing.BorderFactory.createTitledBorder("Dismissed"));

        lab_DateDismissal.setText("Date of dismissal :");

        lab_DismissedReason.setText("Reason :");

        txta_DismissedReason.setColumns(20);
        txta_DismissedReason.setRows(3);
        txta_DismissedReason.setEnabled(false);
        jScrollPane1.setViewportView(txta_DismissedReason);

        dateChooser_DateDismissal.setEnabled(false);

        javax.swing.GroupLayout pan_DismissedLayout = new javax.swing.GroupLayout(pan_Dismissed);
        pan_Dismissed.setLayout(pan_DismissedLayout);
        pan_DismissedLayout.setHorizontalGroup(
            pan_DismissedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_DismissedLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(pan_DismissedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lab_DateDismissal)
                    .addComponent(lab_DismissedReason))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_DismissedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 603, Short.MAX_VALUE)
                    .addComponent(dateChooser_DateDismissal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        pan_DismissedLayout.setVerticalGroup(
            pan_DismissedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_DismissedLayout.createSequentialGroup()
                .addGroup(pan_DismissedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dateChooser_DateDismissal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_DateDismissal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_DismissedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_DismissedReason))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pan_Retired.setBorder(javax.swing.BorderFactory.createTitledBorder("Retired"));

        lab_DateRetirement.setText("Date of retirement :");

        dateChooser_DateRetirement.setEnabled(false);

        javax.swing.GroupLayout pan_RetiredLayout = new javax.swing.GroupLayout(pan_Retired);
        pan_Retired.setLayout(pan_RetiredLayout);
        pan_RetiredLayout.setHorizontalGroup(
            pan_RetiredLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_RetiredLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(lab_DateRetirement)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dateChooser_DateRetirement, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(480, Short.MAX_VALUE))
        );
        pan_RetiredLayout.setVerticalGroup(
            pan_RetiredLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_RetiredLayout.createSequentialGroup()
                .addGroup(pan_RetiredLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lab_DateRetirement)
                    .addComponent(dateChooser_DateRetirement, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pan_Suspended, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(lab_Employeestatus)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cob_EmployeeStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pan_Dismissed, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pan_Retired, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab_Employeestatus)
                    .addComponent(cob_EmployeeStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pan_Suspended, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pan_Dismissed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pan_Retired, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(128, Short.MAX_VALUE))
        );

        pan_EmployeeStatus.addTab("Employee status", jPanel5);

        lab_ToDepartment.setText("To Department :");

        lbl_Dateoftransfer.setText("Date of transfer :");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lab_ToDepartment)
                    .addComponent(lbl_Dateoftransfer))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cob_TransferDep, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dateChooser_TransferDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(541, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab_ToDepartment)
                    .addComponent(cob_TransferDep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_Dateoftransfer)
                    .addComponent(dateChooser_TransferDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(442, Short.MAX_VALUE))
        );

        pan_EmployeeStatus.addTab("Transfer(Inter-departmental)", jPanel4);

        btn_Save.setText("Save");
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
                    .addComponent(pan_EmployeeStatus, javax.swing.GroupLayout.DEFAULT_SIZE, 776, Short.MAX_VALUE)
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
                .addComponent(pan_EmployeeStatus, javax.swing.GroupLayout.DEFAULT_SIZE, 541, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_Cancel, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_Save, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_AdministrativeDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_AdministrativeDeleteActionPerformed
  
        String sql = "DELETE FROM administrative WHERE s_no = '"+m_Sno+"' " +
                "AND (SELECT guid FROM department WHERE name = '"+list_Administrative.getSelectedValue()+"') = dep_guid";
        try {
            DBC.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(Frm_Work.class.getName()).log(Level.SEVERE, null, ex);
        }
        btn_AdministrativeDelete.setEnabled(false);
        showDepartmentList("administrative",list_Administrative);
}//GEN-LAST:event_btn_AdministrativeDeleteActionPerformed

    private void list_AdministrativeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_list_AdministrativeMouseClicked
        if (list_Administrative.getSelectedIndex() != -1) {
            btn_AdministrativeDelete.setEnabled(true);
        }
}//GEN-LAST:event_list_AdministrativeMouseClicked

    private void btn_AdministrativeAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_AdministrativeAddActionPerformed
        if (cob_Administrative.getSelectedIndex() != 0) {
           
            String sql ="INSERT INTO administrative (no, s_no, dep_guid) " +
                    "SELECT IF(COUNT(no) <> 0,MAX(no)+1,0), " +
                    "'"+m_Sno+"', " +
                    "(SELECT guid FROM department WHERE name = '"+cob_Administrative.getSelectedItem()+"') " +
                    "FROM administrative";
            try {
                DBC.executeUpdate(sql);
                cob_Administrative.setSelectedIndex(0);
            } catch (SQLException ex) {
                Logger.getLogger(Frm_Work.class.getName()).log(Level.SEVERE, null, ex);
            }
            showDepartmentList("administrative",list_Administrative);
        }
}//GEN-LAST:event_btn_AdministrativeAddActionPerformed

    private void cob_EmployeeStatusItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cob_EmployeeStatusItemStateChanged
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            txta_SuspendedReason.setText("");
            txta_DismissedReason.setText("");
            switch(cob_EmployeeStatus.getSelectedIndex()){
                case 0:
                    dateChooser_DateSuspension.setEnabled(false);
                    dateChooser_DateResumption.setEnabled(false);
                    txta_SuspendedReason.setEnabled(false);

                    dateChooser_DateDismissal.setEnabled(false);
                    txta_DismissedReason.setEnabled(false);

                    dateChooser_DateRetirement.setEnabled(false);
                    break;
                case 1:
                    dateChooser_DateSuspension.setEnabled(true);
                    dateChooser_DateResumption.setEnabled(true);
                    txta_SuspendedReason.setEnabled(true);

                    dateChooser_DateDismissal.setEnabled(false);
                    txta_DismissedReason.setEnabled(false);

                    dateChooser_DateRetirement.setEnabled(false);
                    break;
                case 2:
                    dateChooser_DateSuspension.setEnabled(false);
                    dateChooser_DateResumption.setEnabled(false);
                    txta_SuspendedReason.setEnabled(false);

                    dateChooser_DateDismissal.setEnabled(true);
                    txta_DismissedReason.setEnabled(true);

                    dateChooser_DateRetirement.setEnabled(false);
                    break;
                case 3:
                    dateChooser_DateSuspension.setEnabled(false);
                    dateChooser_DateResumption.setEnabled(false);
                    txta_SuspendedReason.setEnabled(false);

                    dateChooser_DateDismissal.setEnabled(false);
                    txta_DismissedReason.setEnabled(false);

                    dateChooser_DateRetirement.setEnabled(true);
                    break;
            }

        }
}//GEN-LAST:event_cob_EmployeeStatusItemStateChanged

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

            if(this.cob_Position.getSelectedIndex()!=0){
               rsPosi = DBC.executeQuery("SELECT guid FROM position WHERE name = '" + cob_Position.getSelectedItem().toString() + "'" );
               if(rsPosi.next()){
                    Posi = "'" + rsPosi.getString("guid") + "'";
                }
            }

            if(this.cob_Permission.getSelectedIndex()!=0){
                rsPermission = DBC.executeQuery("SELECT guid FROM permission_info WHERE grp_name = '" + cob_Permission.getSelectedItem().toString() + "'" );
                if(rsPermission.next()){
                    Permission = "'" + rsPermission.getString("guid") + "'";
                }
            }

            if(this.cob_TransferDep.getSelectedIndex()!=0){
                rsDep = DBC.executeQuery("SELECT guid FROM department WHERE name = '" + cob_TransferDep.getSelectedItem().toString() + "'" );
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
                    "employee_status  = '"+this.cob_EmployeeStatus.getSelectedIndex()+"'," +
                    "date_suspension  = '"+this.dateChooser_DateSuspension.getValue()+"'," +
                    "date_resumption  = '"+this.dateChooser_DateResumption.getValue()+"'," +
                    "suspension_reason  = '"+this.txta_SuspendedReason.getText()+"'," +
                    "date_dismissal  = '"+this.dateChooser_DateDismissal.getValue()+"'," +
                    "dismissal_reason  = '"+this.txta_DismissedReason.getText()+"'," +
                    "date_retirement  = '"+this.dateChooser_DateRetirement.getValue()+"'," +
                    "grade_level  = '"+this.txt_GradeLevel.getText()+"'," +
                    "category_post  = '"+this.txt_CategoryPost.getText()+"'," +
                    "level  = '"+this.txt_SalaryLevel.getText()+"'," +
                    "step  = '"+this.txt_Step.getText()+"'," +
                    "groos_salary  = '"+this.txt_GroosSalary.getText()+"'," +
                    "first_appointment_date  = '"+this.dateCob_FirstAppointmentDate.getValue()+"'," +
                    "first_appointment_stafftype  = '"+this.txt_FirstAppointmentStafftype.getText()+"'," +
                    "first_appointment_grade  = '"+this.txt_FirstAppointmentGrade.getText()+"'," +
                    "last_promotion_date  = '"+this.dateCob_LastPromotionDate.getValue()+"'," +
                    "transfer_date  = '"+this.dateChooser_TransferDate.getValue()+"' " +
                    ",dep_guid = " + Dep +
                    ",posi_guid = " + Posi +
                    ",gp_guid = " + Permission +
                    ",poli_guid = " + Poli +
                    ",exist = true "+
                    "WHERE s_no = '"+m_Sno+"' ";
            DBC.executeUpdate(sql);
            //cc.johnwu.login.ChangesLog.ChangesLog("staff_info", m_Sno, "add");

         //   JOptionPane.showMessageDialog(new Frame(), paragraph.getLanguage(message, "SAVECOMPLETE"));
            btn_CancelActionPerformed(null);
          //  m_frame.onPatientMod(this.txt_No.getText());
        } catch (SQLException e) {
           // ErrorMessage.setData("Patients", "Frm_PatientMod" ,"btn_OKActionPerformed(java.awt.event.ActionEvent evt)",
             //       e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            System.out.println(e);
        } finally {
            try {DBC.closeConnection(rsPosi);
                DBC.closeConnection(rsPermission);
                DBC.closeConnection(rsDep);
            }
            catch (SQLException e){

            }
        }
}//GEN-LAST:event_btn_SaveActionPerformed

    private void btn_CancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CancelActionPerformed
        new Frm_HRM(m_Sno,m_UUID).setVisible(true);
        this.dispose();
}//GEN-LAST:event_btn_CancelActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_AdministrativeAdd;
    private javax.swing.JButton btn_AdministrativeDelete;
    private javax.swing.JButton btn_Cancel;
    private javax.swing.JButton btn_Save;
    private javax.swing.JComboBox cob_Administrative;
    private javax.swing.JComboBox cob_EmployeeStatus;
    private javax.swing.JComboBox cob_Permission;
    private javax.swing.JComboBox cob_Poli;
    private javax.swing.JComboBox cob_Position;
    private javax.swing.JComboBox cob_TransferDep;
    private cc.johnwu.date.DateComboBox dateChooser_DateDismissal;
    private cc.johnwu.date.DateComboBox dateChooser_DateResumption;
    private cc.johnwu.date.DateComboBox dateChooser_DateRetirement;
    private cc.johnwu.date.DateComboBox dateChooser_DateSuspension;
    private cc.johnwu.date.DateComboBox dateChooser_TransferDate;
    private cc.johnwu.date.DateComboBox dateCob_FirstAppointmentDate;
    private cc.johnwu.date.DateComboBox dateCob_LastPromotionDate;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JLabel lab_AdministrativeUnit;
    private javax.swing.JLabel lab_CategoryPost;
    private javax.swing.JLabel lab_DateDismissal;
    private javax.swing.JLabel lab_DateResumption;
    private javax.swing.JLabel lab_DateRetirement;
    private javax.swing.JLabel lab_DateSuspension;
    private javax.swing.JLabel lab_DismissedReason;
    private javax.swing.JLabel lab_Employeestatus;
    private javax.swing.JLabel lab_FirstAppointmentDate;
    private javax.swing.JLabel lab_FirstAppointmentGrade;
    private javax.swing.JLabel lab_FirstAppointmentStafftype;
    private javax.swing.JLabel lab_GradeLevel;
    private javax.swing.JLabel lab_GroosSalary;
    private javax.swing.JLabel lab_Group;
    private javax.swing.JLabel lab_LastPromotionDate;
    private javax.swing.JLabel lab_Poli;
    private javax.swing.JLabel lab_Position;
    private javax.swing.JLabel lab_SalaryLevel;
    private javax.swing.JLabel lab_Step;
    private javax.swing.JLabel lab_SuspendedReason;
    private javax.swing.JLabel lab_ToDepartment;
    private javax.swing.JLabel lbl_Dateoftransfer;
    private javax.swing.JList list_Administrative;
    private javax.swing.JPanel pan_Administrative;
    private javax.swing.JPanel pan_Dismissed;
    private javax.swing.JTabbedPane pan_EmployeeStatus;
    private javax.swing.JPanel pan_Retired;
    private javax.swing.JPanel pan_Suspended;
    private javax.swing.JTextField txt_CategoryPost;
    private javax.swing.JTextField txt_FirstAppointmentGrade;
    private javax.swing.JTextField txt_FirstAppointmentStafftype;
    private javax.swing.JTextField txt_GradeLevel;
    private javax.swing.JTextField txt_GroosSalary;
    private javax.swing.JTextField txt_SalaryLevel;
    private javax.swing.JTextField txt_Step;
    private javax.swing.JTextArea txta_DismissedReason;
    private javax.swing.JTextArea txta_SuspendedReason;
    // End of variables declaration//GEN-END:variables

}
