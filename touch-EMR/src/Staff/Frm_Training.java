package Staff;
import Multilingual.language;
import cc.johnwu.sql.*;
import java.awt.event.*;
import java.sql.*;
import java.sql.SQLException;

/**
 *
 * @author steven
 */
public class Frm_Training extends javax.swing.JFrame {
     private int m_Sno;
     private String m_UUID = null;
     public Frm_Training(int sno, String uuid) {
        m_UUID = uuid;
        m_Sno = sno;
        initComponents();
        init();
        initLanguage();
    }
    /*多國語言變數*/
    private language paragraph = new language();
    private String[] line = new String(paragraph.setlanguage("TRAINING")).split("\n") ;
    private String[] message = new String(paragraph.setlanguage("MESSAGE")).split("\n") ;

    private void init(){//修改資料
        if (m_UUID != null) {
            this.setTitle("Training (New Staff No. "+m_Sno +" )");
        } else {
            this.setTitle("Training (Edit Staff No. "+m_Sno +" )");
        }
        
        this.setExtendedState(Frm_Employee.MAXIMIZED_BOTH);  // 最大化
        this.setLocationRelativeTo(this);//視窗顯示至中
        addWindowListener(new WindowAdapter() {//視窗關閉事件
            @Override
            public void windowClosing(WindowEvent windowevent)
            {
                btn_CancelActionPerformed(null);
            }
        });
        String sql = "";
        ResultSet rs = null;
        try {
            sql="Select * From staff_info where staff_info.s_no="+m_Sno+" and staff_info.exist=1";
            rs = DBC.executeQuery(sql);
            rs.next();

            this.cob_TrainingType.setSelectedIndex(Integer.parseInt(rs.getString("training_type")));
            this.cob_Sponsorship.setSelectedIndex(Integer.parseInt(rs.getString("sponsorship")));
            this.txt_CourseName.setText(rs.getString("course_name"));
            this.dateChooser_TrainingStartDate.setValue(rs.getString("period_startdate"));
            this.dateChooser_TrainingEndDate.setValue(rs.getString("period_enddate"));
            this.txt_ExpectedQualification.setText(rs.getString("expected_qualification"));
            this.cob_Commitment.setSelectedIndex(Integer.parseInt(rs.getString("commitment")));
            this.dateChooser_ResumptionDate.setValue(rs.getString("resumption_date"));
            this.txt_InstitutionName.setText(rs.getString("institution_name"));
            this.txt_TrainingCountry.setText(rs.getString("training_country"));
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            try {DBC.closeConnection(rs);}
            catch (SQLException e){

            }
        }
    }

    private void initLanguage() {
        this.setTitle(paragraph.getLanguage(line, "TITLETRAININGEDITSTAFFNO6"));
        this.lab_TrainingType.setText(paragraph.getLanguage(line, "TYPE"));
        this.lab_Sponsorship.setText(paragraph.getLanguage(line, "SPONSORSHIP"));
        this.lab_InstitutionName.setText(paragraph.getLanguage(line, "NAMEOFINSTITUTION"));
        this.lab_TrainingStartDate.setText(paragraph.getLanguage(line, "STARTDATE"));
        this.lab_TrainingEndDate.setText(paragraph.getLanguage(line, "ENDDATE"));
        this.lab_CourseName.setText(paragraph.getLanguage(line, "COURSENAME"));
        this.lab_ExpectedQualification.setText(paragraph.getLanguage(line, "EXPECTEDQUALIFICATION"));
        this.lan_TrainingCountry.setText(paragraph.getLanguage(line, "COUNTRY"));
        this.lab_Commitment.setText(paragraph.getLanguage(line, "COMMITMENT"));
        this.lab_ResumptionDate.setText(paragraph.getLanguage(line, "RESUMPTIONDATE"));
        this.btn_Save.setText(paragraph.getLanguage(message, "SAVE"));
        this.btn_Cancel.setText(paragraph.getLanguage(message, "CANCEL"));
        cob_TrainingType.setModel(
                new javax.swing.DefaultComboBoxModel(
                    new String[] { "", paragraph.getLanguage(line, "PRESERVICE") ,
                                    paragraph.getLanguage(line, "INSERVICE") ,
                                    paragraph.getLanguage(line, "POSTBASIC")}
        ));

        cob_Sponsorship.setModel
                (new javax.swing.DefaultComboBoxModel
                    (new String[] { "", paragraph.getLanguage(line, "SELFSPONSORED") ,
                                    paragraph.getLanguage(line, "KBTHSPONSORED")}
        ));

        cob_Commitment.setModel(
                new javax.swing.DefaultComboBoxModel(
                    new String[] { "", paragraph.getLanguage(line, "PARTTIME") ,
                                    paragraph.getLanguage(line, "FULLTIME") }
        ));

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pan_Training1 = new javax.swing.JPanel();
        lab_TrainingType = new javax.swing.JLabel();
        lab_Sponsorship = new javax.swing.JLabel();
        lab_CourseName = new javax.swing.JLabel();
        lab_TrainingStartDate = new javax.swing.JLabel();
        lab_TrainingEndDate = new javax.swing.JLabel();
        lab_ExpectedQualification = new javax.swing.JLabel();
        lab_Commitment = new javax.swing.JLabel();
        lab_ResumptionDate = new javax.swing.JLabel();
        cob_TrainingType = new javax.swing.JComboBox();
        txt_CourseName = new javax.swing.JTextField();
        cob_Sponsorship = new javax.swing.JComboBox();
        txt_ExpectedQualification = new javax.swing.JTextField();
        cob_Commitment = new javax.swing.JComboBox();
        dateChooser_TrainingStartDate = new cc.johnwu.date.DateComboBox();
        dateChooser_TrainingEndDate = new cc.johnwu.date.DateComboBox();
        dateChooser_ResumptionDate = new cc.johnwu.date.DateComboBox();
        lab_InstitutionName = new javax.swing.JLabel();
        lan_TrainingCountry = new javax.swing.JLabel();
        txt_TrainingCountry = new javax.swing.JTextField();
        txt_InstitutionName = new javax.swing.JTextField();
        btn_Save = new javax.swing.JButton();
        btn_Cancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Training (Edit Staff No.6))");
        setAlwaysOnTop(true);

        lab_TrainingType.setText("Type :");

        lab_Sponsorship.setText("Sponsorship :");

        lab_CourseName.setText("Course Name :");

        lab_TrainingStartDate.setText("Start date :");

        lab_TrainingEndDate.setText("End date :");

        lab_ExpectedQualification.setText("Expected qualification :");

        lab_Commitment.setText("Commitment :");

        lab_ResumptionDate.setText("Resumption Date :");

        cob_TrainingType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "Pre-Service", "In-Service", "Post Basic" }));

        cob_Sponsorship.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "Self sponsored", "KBTH sponsored" }));

        cob_Commitment.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "Part-time", "Full-time" }));

        lab_InstitutionName.setText("Name of Institution :");

        lan_TrainingCountry.setText("Country :");

        javax.swing.GroupLayout pan_Training1Layout = new javax.swing.GroupLayout(pan_Training1);
        pan_Training1.setLayout(pan_Training1Layout);
        pan_Training1Layout.setHorizontalGroup(
            pan_Training1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_Training1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(pan_Training1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lab_Sponsorship)
                    .addComponent(lab_TrainingType)
                    .addComponent(lab_ResumptionDate)
                    .addComponent(lab_TrainingStartDate)
                    .addComponent(lab_CourseName)
                    .addComponent(lab_ExpectedQualification)
                    .addComponent(lab_Commitment)
                    .addComponent(lab_TrainingEndDate)
                    .addComponent(lab_InstitutionName)
                    .addComponent(lan_TrainingCountry))
                .addGroup(pan_Training1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan_Training1Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addGroup(pan_Training1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(cob_TrainingType, 0, 591, Short.MAX_VALUE)
                            .addComponent(cob_Sponsorship, 0, 591, Short.MAX_VALUE)
                            .addComponent(txt_InstitutionName, javax.swing.GroupLayout.DEFAULT_SIZE, 591, Short.MAX_VALUE)))
                    .addGroup(pan_Training1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan_Training1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dateChooser_TrainingStartDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cob_Commitment, javax.swing.GroupLayout.Alignment.TRAILING, 0, 592, Short.MAX_VALUE)
                            .addComponent(txt_TrainingCountry, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 592, Short.MAX_VALUE)
                            .addComponent(txt_CourseName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 592, Short.MAX_VALUE)
                            .addComponent(txt_ExpectedQualification, javax.swing.GroupLayout.DEFAULT_SIZE, 592, Short.MAX_VALUE)
                            .addComponent(dateChooser_ResumptionDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dateChooser_TrainingEndDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        pan_Training1Layout.setVerticalGroup(
            pan_Training1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_Training1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan_Training1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cob_TrainingType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_TrainingType))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_Training1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab_Sponsorship)
                    .addComponent(cob_Sponsorship, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_Training1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab_InstitutionName)
                    .addComponent(txt_InstitutionName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_Training1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lab_TrainingStartDate)
                    .addComponent(dateChooser_TrainingStartDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_Training1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lab_TrainingEndDate)
                    .addComponent(dateChooser_TrainingEndDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_Training1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_CourseName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_CourseName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_Training1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_ExpectedQualification, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_ExpectedQualification))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_Training1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_TrainingCountry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lan_TrainingCountry))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_Training1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab_Commitment)
                    .addComponent(cob_Commitment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_Training1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lab_ResumptionDate)
                    .addComponent(dateChooser_ResumptionDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btn_Save, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_Cancel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pan_Training1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(13, 13, 13))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pan_Training1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_Cancel, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_Save, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(205, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_SaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SaveActionPerformed
        String sql = "";
        try {
            sql = "UPDATE staff_info SET "+
                    "training_type  = '"+this.cob_TrainingType.getSelectedIndex()+"'," +
                    "sponsorship  = '"+this.cob_Sponsorship.getSelectedIndex()+"'," +
                    "course_name  = '"+this.txt_CourseName.getText()+"'," +
                    "period_startdate  = '"+this.dateChooser_TrainingStartDate.getValue()+"'," +
                    "period_enddate  = '"+this.dateChooser_TrainingEndDate.getValue()+"'," +
                    "expected_qualification  = '"+this.txt_ExpectedQualification.getText()+"'," +
                    "commitment  = '"+this.cob_Commitment.getSelectedIndex()+"'," +
                    "resumption_date  = '"+this.dateChooser_ResumptionDate.getValue()+"', " +
                    "institution_name  = '"+this.txt_InstitutionName.getText()+"'," +
                    "training_country  = '"+this.txt_TrainingCountry.getText()+"' " +
                    "WHERE s_no = '"+m_Sno+"' ";
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

    private void btn_CancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CancelActionPerformed
        new Frm_HRM(m_Sno,m_UUID).setVisible(true);
        this.dispose();
}//GEN-LAST:event_btn_CancelActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Cancel;
    private javax.swing.JButton btn_Save;
    private javax.swing.JComboBox cob_Commitment;
    private javax.swing.JComboBox cob_Sponsorship;
    private javax.swing.JComboBox cob_TrainingType;
    private cc.johnwu.date.DateComboBox dateChooser_ResumptionDate;
    private cc.johnwu.date.DateComboBox dateChooser_TrainingEndDate;
    private cc.johnwu.date.DateComboBox dateChooser_TrainingStartDate;
    private javax.swing.JLabel lab_Commitment;
    private javax.swing.JLabel lab_CourseName;
    private javax.swing.JLabel lab_ExpectedQualification;
    private javax.swing.JLabel lab_InstitutionName;
    private javax.swing.JLabel lab_ResumptionDate;
    private javax.swing.JLabel lab_Sponsorship;
    private javax.swing.JLabel lab_TrainingEndDate;
    private javax.swing.JLabel lab_TrainingStartDate;
    private javax.swing.JLabel lab_TrainingType;
    private javax.swing.JLabel lan_TrainingCountry;
    private javax.swing.JPanel pan_Training1;
    private javax.swing.JTextField txt_CourseName;
    private javax.swing.JTextField txt_ExpectedQualification;
    private javax.swing.JTextField txt_InstitutionName;
    private javax.swing.JTextField txt_TrainingCountry;
    // End of variables declaration//GEN-END:variables

}
