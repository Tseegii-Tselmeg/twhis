package Staff;
import Multilingual.language;
import cc.johnwu.sql.*;
import cc.johnwu.finger.FingerPrintScanner;
import java.awt.event.*;
import java.sql.*;
import java.sql.SQLException;


/**
 *
 * @author steven
 */
public class Frm_Leave extends javax.swing.JFrame {
    private language paragraph = new language();
    private String[] line = new String(paragraph.setlanguage("LEAVE")).split("\n") ;
    private String[] message = new String(paragraph.setlanguage("MESSAGE")).split("\n") ;
    private int m_Sno;
    private String m_UUID = null;
    public Frm_Leave(int sno, String uuid) {
        m_UUID = uuid;
        m_Sno = sno;
        initComponents();
        init();
        initLanguage();
    }

    private void init(){//修改資料
        if (m_UUID != null) {
            this.setTitle("Leave (New Staff No. "+m_Sno +" )");
        } else {
            this.setTitle("Leave (Edit Staff No. "+m_Sno +" )");
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
        try {
            sql="SELECT * FROM staff_info WHERE staff_info.s_no="+m_Sno+" AND staff_info.exist=1";
            System.out.println(sql);
            rs = DBC.executeQuery(sql);
            rs.next();
            this.cob_LeaveEntitlement.setSelectedIndex(Integer.parseInt(rs.getString("entitlement")));
            this.cob_LeaveType.setSelectedIndex(Integer.parseInt(rs.getString("type")));
            this.dateChooser_LeaveStartDate.setValue(rs.getString("startdate"));
            this.dateChooser_LeaveEndDate.setValue(rs.getString("enddate"));
            this.txt_LeaveDaysTaken.setText(rs.getString("days_taken"));
            this.txt_LeaveDaysRemaining.setText(rs.getString("days_remaining"));
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            try {DBC.closeConnection(rs);}
            catch (SQLException e){

            }
        }
    }

    private void initLanguage() {
        this.lab_LeaveEntitlement.setText(paragraph.getLanguage(line, "ENTITLEMENT"));
        this.lab_LeaveType.setText(paragraph.getLanguage(line, "TYPE"));
        this.lab_LeaveStartDate.setText(paragraph.getLanguage(line, "STARTDATE"));
        this.lab_LeaveEndDate.setText(paragraph.getLanguage(line, "ENDDATE"));
        this.lab_LeaveDaysTaken.setText(paragraph.getLanguage(line, "NUMBEROFDAYSTAKEN"));
        this.lab_LeaveDaysRemaining.setText(paragraph.getLanguage(line, "NUMBEROFDAYSREMAINING"));
        this.btn_Save.setText(paragraph.getLanguage(message ,"SAVE"));
        this.btn_Cancel.setText(paragraph.getLanguage(message ,"CANCEL"));
        this.setTitle(paragraph.getLanguage(line, "LEAVEEDITSTAFFNO6"));
        cob_LeaveEntitlement.setModel(
                new javax.swing.DefaultComboBoxModel(
                    new String[] { "", paragraph.getLanguage(line, "21WORKINGDAYS"),
                                    paragraph.getLanguage(line, "28WORKINGDAYS"),
                                    paragraph.getLanguage(line, "36WORKINGDAYS") }
        ));

        cob_LeaveType.setModel(
                new javax.swing.DefaultComboBoxModel(
                    new String[] { "", paragraph.getLanguage(line, "STUDYLEAVEWITHPAY"),
                                    paragraph.getLanguage(line, "STUDYLEAVEWITHOUTPAY") ,
                                    paragraph.getLanguage(line, "LEAVEWITHPAY"),
                                    paragraph.getLanguage(line, "LEAVEWITHOUTPAY"),
                                    paragraph.getLanguage(line, "MATERNITYLEAVE"),
                                    paragraph.getLanguage(line, "PATERNITYLEAVE") }
        ));
    }


    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel8 = new javax.swing.JPanel();
        lab_LeaveEntitlement = new javax.swing.JLabel();
        lab_LeaveType = new javax.swing.JLabel();
        lab_LeaveStartDate = new javax.swing.JLabel();
        lab_LeaveEndDate = new javax.swing.JLabel();
        lab_LeaveDaysTaken = new javax.swing.JLabel();
        lab_LeaveDaysRemaining = new javax.swing.JLabel();
        cob_LeaveEntitlement = new javax.swing.JComboBox();
        cob_LeaveType = new javax.swing.JComboBox();
        txt_LeaveDaysTaken = new javax.swing.JTextField();
        txt_LeaveDaysRemaining = new javax.swing.JTextField();
        dateChooser_LeaveStartDate = new cc.johnwu.date.DateComboBox();
        dateChooser_LeaveEndDate = new cc.johnwu.date.DateComboBox();
        btn_Cancel = new javax.swing.JButton();
        btn_Save = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Leave (Edit Staff No.6)");
        setAlwaysOnTop(true);

        lab_LeaveEntitlement.setText("Entitlement :");

        lab_LeaveType.setText("Type :");

        lab_LeaveStartDate.setText("Start date :");

        lab_LeaveEndDate.setText("End date :");

        lab_LeaveDaysTaken.setText("Number of days taken :");

        lab_LeaveDaysRemaining.setText("Number of days remaining :");

        cob_LeaveEntitlement.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "21 working days/year(junior Staff 1)", "28 working days/year(junior Staff 2)", "36 working days/year(Senior Staff)" }));

        cob_LeaveType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "Study leave with pay", "Study leave without pay", "Leave with pay", "Leave without pay", "Maternity leave", "Paternity Leave" }));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lab_LeaveEntitlement)
                    .addComponent(lab_LeaveStartDate)
                    .addComponent(lab_LeaveEndDate)
                    .addComponent(lab_LeaveDaysTaken)
                    .addComponent(lab_LeaveType)
                    .addComponent(lab_LeaveDaysRemaining))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_LeaveDaysRemaining, javax.swing.GroupLayout.DEFAULT_SIZE, 562, Short.MAX_VALUE)
                    .addComponent(cob_LeaveType, 0, 562, Short.MAX_VALUE)
                    .addComponent(txt_LeaveDaysTaken, javax.swing.GroupLayout.DEFAULT_SIZE, 562, Short.MAX_VALUE)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(dateChooser_LeaveStartDate, javax.swing.GroupLayout.DEFAULT_SIZE, 444, Short.MAX_VALUE)
                        .addGap(118, 118, 118))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(dateChooser_LeaveEndDate, javax.swing.GroupLayout.DEFAULT_SIZE, 444, Short.MAX_VALUE)
                        .addGap(118, 118, 118))
                    .addComponent(cob_LeaveEntitlement, 0, 562, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cob_LeaveEntitlement, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_LeaveEntitlement))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cob_LeaveType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_LeaveType))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lab_LeaveStartDate)
                    .addComponent(dateChooser_LeaveStartDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lab_LeaveEndDate)
                    .addComponent(dateChooser_LeaveEndDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_LeaveDaysTaken, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_LeaveDaysTaken))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_LeaveDaysRemaining, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_LeaveDaysRemaining))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btn_Cancel.setText("Cancel");
        btn_Cancel.setMaximumSize(new java.awt.Dimension(60, 29));
        btn_Cancel.setMinimumSize(new java.awt.Dimension(60, 29));
        btn_Cancel.setPreferredSize(new java.awt.Dimension(40, 29));
        btn_Cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CancelActionPerformed(evt);
            }
        });

        btn_Save.setText("Save");
        btn_Save.setMaximumSize(new java.awt.Dimension(60, 29));
        btn_Save.setMinimumSize(new java.awt.Dimension(60, 29));
        btn_Save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_SaveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 333, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_Cancel, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_Save, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_CancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CancelActionPerformed
         new Frm_HRM(m_Sno,m_UUID).setVisible(true);
        this.dispose();
}//GEN-LAST:event_btn_CancelActionPerformed
    

    private void btn_SaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SaveActionPerformed
        String sql = "";
        try {
            sql = "UPDATE staff_info SET "+
                    "entitlement  = '"+this.cob_LeaveEntitlement.getSelectedIndex()+"'," +
                    "type  = '"+this.cob_LeaveType.getSelectedIndex()+"'," +
                    "startdate  = '"+this.dateChooser_LeaveStartDate.getValue()+"'," +
                    "enddate  = '"+this.dateChooser_LeaveEndDate.getValue()+"'," +
                    "days_taken  = '"+this.txt_LeaveDaysTaken.getText()+"'," +
                    "days_remaining  = '"+this.txt_LeaveDaysRemaining.getText()+"' " +
                    "WHERE s_no = '"+m_Sno+"' ";
            System.out.println(sql);
            DBC.executeUpdate(sql);
            //cc.johnwu.login.ChangesLog.ChangesLog("staff_info", this.txt_No.getText(), "add");

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
    private javax.swing.JButton btn_Cancel;
    private javax.swing.JButton btn_Save;
    private javax.swing.JComboBox cob_LeaveEntitlement;
    private javax.swing.JComboBox cob_LeaveType;
    private cc.johnwu.date.DateComboBox dateChooser_LeaveEndDate;
    private cc.johnwu.date.DateComboBox dateChooser_LeaveStartDate;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JLabel lab_LeaveDaysRemaining;
    private javax.swing.JLabel lab_LeaveDaysTaken;
    private javax.swing.JLabel lab_LeaveEndDate;
    private javax.swing.JLabel lab_LeaveEntitlement;
    private javax.swing.JLabel lab_LeaveStartDate;
    private javax.swing.JLabel lab_LeaveType;
    private javax.swing.JTextField txt_LeaveDaysRemaining;
    private javax.swing.JTextField txt_LeaveDaysTaken;
    // End of variables declaration//GEN-END:variables

}
