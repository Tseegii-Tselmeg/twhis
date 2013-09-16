package Staff;

import Multilingual.language;
import cc.johnwu.finger.FingerPrintScanner;
import cc.johnwu.sql.DBC;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author steven
 */

public class Frm_HRM extends javax.swing.JFrame {
    private int m_EditSno;    // 修改用 員工編號
    private int m_NewSno;  // 新增用 員工編號
    private String m_UUID;
    /*多國語言變數*/
    private language paragraph = new language();
    private String[] line = new String(paragraph.setlanguage("HRM")).split("\n") ;
    private String[] message = new String(paragraph.setlanguage("MESSAGE")).split("\n") ;

    public Frm_HRM() {  // 新增員工
        initComponents();
        initLanguage();
        init();
    }
    public Frm_HRM(int s_no) {  // 修改員工
        FingerPrintScanner.stop();
        initComponents();
        initLanguage();
        this.m_EditSno = s_no;
        this.setTitle(paragraph.getLanguage(line, "HRMSYSTEM")+" ("+paragraph.getLanguage(line, "EDITSTAFFNO") +" "+m_EditSno +" )");
        this.setLocationRelativeTo(this);//視窗顯示至中

        addWindowListener(new WindowAdapter() {//視窗關閉事件
            @Override
            public void windowClosing(WindowEvent windowevent)
            {
                btn_BackActionPerformed(null);
            }
        });
    }
    public Frm_HRM(int s_no,String UUID) {
        initComponents();
        initLanguage();
        this.setLocationRelativeTo(this);//視窗顯示至中

        addWindowListener(new WindowAdapter() {//視窗關閉事件
            @Override
            public void windowClosing(WindowEvent windowevent)
            {
                btn_BackActionPerformed(null);
            }
        });
        if (UUID != null) {  // 新增員工 返回
            m_NewSno = s_no;
            m_UUID = UUID;
            this.setTitle(paragraph.getLanguage(line, "HRMSYSTEM")+" ("+paragraph.getLanguage(line, "NEWSTAFFNO") +" "+s_no +" )");
            setExist();
            
        } else {            // 修改員工 返回
            m_EditSno = s_no;
            this.setTitle(paragraph.getLanguage(line, "HRMSYSTEM")+" ("+paragraph.getLanguage(line, "EDITSTAFFNO") +" "+s_no +" )");
        }

    }



    /** 初始化  新增員工*/
    private void init(){
        this.setLocationRelativeTo(this);//視窗顯示至中

        addWindowListener(new WindowAdapter() {//視窗關閉事件
            @Override
            public void windowClosing(WindowEvent windowevent)
            {
                btn_BackActionPerformed(null);
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
                m_NewSno = Integer.parseInt(rs.getString(1));
                this.setTitle(paragraph.getLanguage(line, "HRMSYSTEM")+" ("+paragraph.getLanguage(line, "NEWSTAFFNO") +" "+m_NewSno +" )");
            }
            setExist();
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
    private void initLanguage() {
            this.setTitle(paragraph.getLanguage(line, "HRMSYSTEM"));
            this.btn_Employee.setText(paragraph.getLanguage(line, "EMPLOYEEINFORMATION"));
            this.btn_Educational.setText(paragraph.getLanguage(line, "EDUCATIONALPROFILE"));
            this.btn_Work.setText(paragraph.getLanguage(line, "WORKJOBPROFILE"));
            this.btn_Administrative.setText(paragraph.getLanguage(line, "ADMINISTRATIVEPROFILE"));
            this.btn_Training.setText(paragraph.getLanguage(line, "TRAINING"));
            this.btn_Leave.setText(paragraph.getLanguage(line, "LEAVE"));
            this.btn_Back.setText(paragraph.getLanguage(line, "BACK"));
    }

     private void setExist() {
        ResultSet rs = null;
        String sql = "SELECT exist FROM staff_info WHERE s_no = "+m_NewSno+" AND exist = true";
        try {
            rs = DBC.executeQuery(sql);
            if(rs.next()){
                btn_Educational.setEnabled(true);
                btn_Work.setEnabled(true);
                btn_Administrative.setEnabled(true);
                btn_Training.setEnabled(true);
                btn_Leave.setEnabled(true);
            } else {
                btn_Educational.setEnabled(false);
                btn_Work.setEnabled(false);
                btn_Administrative.setEnabled(false);
                btn_Training.setEnabled(false);
                btn_Leave.setEnabled(false);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Frm_HRM.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {DBC.closeConnection(rs);}
            catch (SQLException e){
            }
        }

     }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btn_Administrative = new javax.swing.JButton();
        btn_Work = new javax.swing.JButton();
        btn_Educational = new javax.swing.JButton();
        btn_Employee = new javax.swing.JButton();
        btn_Leave = new javax.swing.JButton();
        btn_Training = new javax.swing.JButton();
        btn_Back = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("HRM System");
        setAlwaysOnTop(true);
        setResizable(false);

        btn_Administrative.setText("Administrative Profile");
        btn_Administrative.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_AdministrativeActionPerformed(evt);
            }
        });

        btn_Work.setText("Work(Job) Profile");
        btn_Work.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_WorkActionPerformed(evt);
            }
        });

        btn_Educational.setText("Educational  Profile");
        btn_Educational.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_EducationalActionPerformed(evt);
            }
        });

        btn_Employee.setText("Employee Information");
        btn_Employee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_EmployeeActionPerformed(evt);
            }
        });

        btn_Leave.setText("Leave");
        btn_Leave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_LeaveActionPerformed(evt);
            }
        });

        btn_Training.setText("Training");
        btn_Training.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_TrainingActionPerformed(evt);
            }
        });

        btn_Back.setText("Back");
        btn_Back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_BackActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btn_Back, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_Leave, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_Training, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_Administrative, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_Work, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_Educational, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_Employee, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(78, 78, 78))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_Employee, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Educational, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Work, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Administrative, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Training, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Leave, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Back, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(202, 202, 202)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(198, 198, 198))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(83, 83, 83))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_WorkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_WorkActionPerformed
        if (m_UUID != null) {  // 不等於null代表新增員工
            new Frm_Work(m_NewSno,m_UUID).setVisible(true);
            this.setVisible(false);
        } else {  // 等於null修改資料
            new Frm_Work(m_EditSno,m_UUID).setVisible(true);
            this.setVisible(false);
        }

    }//GEN-LAST:event_btn_WorkActionPerformed

    private void btn_EmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_EmployeeActionPerformed
       if (m_UUID != null) {  // 不等於null代表新增員工
            new Frm_Employee(m_NewSno,m_UUID).setVisible(true);
        } else {              // 等於null修改資料
            new Frm_Employee(m_EditSno,m_UUID).setVisible(true);
        }
        this.setVisible(false);
    }//GEN-LAST:event_btn_EmployeeActionPerformed

    private void btn_EducationalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_EducationalActionPerformed

        if (m_UUID != null) {  // 不等於null代表新增員工
            new Frm_Educational(m_NewSno,m_UUID).setVisible(true);
        } else {              // 等於null修改資料
            new Frm_Educational(m_EditSno,m_UUID).setVisible(true);
        }
        this.setVisible(false);
    }//GEN-LAST:event_btn_EducationalActionPerformed

    private void btn_AdministrativeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_AdministrativeActionPerformed

        if (m_UUID != null) {  // 不等於null代表新增員工
            new Frm_Administrative(m_NewSno,m_UUID).setVisible(true);
        } else {              // 等於null修改資料
            new Frm_Administrative(m_EditSno,m_UUID).setVisible(true);
        }
        this.setVisible(false);
  
    }//GEN-LAST:event_btn_AdministrativeActionPerformed

    private void btn_LeaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_LeaveActionPerformed

         if (m_UUID != null) {  // 不等於null代表新增員工
            new Frm_Leave(m_NewSno,m_UUID).setVisible(true);
        } else {              // 等於null修改資料
            new Frm_Leave(m_EditSno,m_UUID).setVisible(true);
        }
        this.setVisible(false);

    }//GEN-LAST:event_btn_LeaveActionPerformed

    private void btn_TrainingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_TrainingActionPerformed

         if (m_UUID != null) {  // 不等於null代表新增員工
            new Frm_Training(m_NewSno,m_UUID).setVisible(true);
        } else {              // 等於null修改資料
            new Frm_Training(m_EditSno,m_UUID).setVisible(true);
        }
        this.setVisible(false);
    
    }//GEN-LAST:event_btn_TrainingActionPerformed

    private void btn_BackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_BackActionPerformed
        try {
            String sql = "DELETE FROM staff_info WHERE firstname = '"+m_UUID+"' AND exist = false";
            DBC.executeUpdate(sql);
        } catch (SQLException e) {
        //    ErrorMessage.setData("Patients", "Frm_PatientMod" ,"btn_CancelActionPerformed(java.awt.event.ActionEvent evt)",
           //         e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            System.out.println(e);
        }
        new Frm_StaffInfo().setVisible(true);
        dispose();
    }//GEN-LAST:event_btn_BackActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Administrative;
    private javax.swing.JButton btn_Back;
    private javax.swing.JButton btn_Educational;
    private javax.swing.JButton btn_Employee;
    private javax.swing.JButton btn_Leave;
    private javax.swing.JButton btn_Training;
    private javax.swing.JButton btn_Work;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables

}
