package Staff;
import Multilingual.language;
import cc.johnwu.sql.*;
import cc.johnwu.finger.FingerPrintScanner;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.sql.SQLException;
/**
 *
 * @author steven
 */
public class Frm_Educational extends javax.swing.JFrame {
    private int m_Sno;
    private String m_UUID = null;
    
    private language paragraph = new language();
    private String[] line = paragraph.setlanguage("EDUCATIONAL").split("\n") ;
    private String[] message = paragraph.setlanguage("MESSAGE").split("\n") ;
    
    public Frm_Educational(int sno, String uuid) {
        m_UUID = uuid;
        m_Sno = sno;
        initComponents();
        init();
        initLanguage();
    }

 

    private void init(){
        if (m_UUID != null) {
            this.setTitle("Educational Profile (New Staff No. "+m_Sno +" )");
        } else {
            this.setTitle("Educational Profile (Edit Staff No. "+m_Sno +" )");
        }
        
        this.setExtendedState(Frm_Educational.MAXIMIZED_BOTH);  // 最大化
        this.setLocationRelativeTo(this);//視窗顯示至中
        addWindowListener(new WindowAdapter() {//視窗關閉事件
            @Override
            public void windowClosing(WindowEvent windowevent)
            {
                FingerPrintScanner.stop();
                btn_CancelActionPerformed(null);
            }
        });
        tabpan_RecordStateChanged();
    }
       private void initLanguage() {
           
        this.lab_AcademicRecordInstitutionAttended.setText(paragraph.getLanguage(line, "ACADEMICRECORDINSTITUTIONATTENDED"));
        this.lab_AcademicRecordCommencementDate.setText(paragraph.getLanguage(line, "ACADEMICRECORDCOMMENCEMENTDATE"));
        this.lab_AcademicRecordCompletionDate.setText(paragraph.getLanguage(line, "ACADEMICRECORDCOMPLETIONDATE"));
        this.lab_AcademicRecordQualificationAttained.setText(paragraph.getLanguage(line, "ACADEMICRECORDQUALIFICATIONATTAINED"));

        this.btn__AcademicRecordAdd.setText(paragraph.getLanguage(line, "ACADEMICRECORDADD"));
        this.btn__AcademicRecordDelete.setText(paragraph.getLanguage(line, "ACADEMICRECORDDELETE"));
        this.btn_Cancel.setText(paragraph.getLanguage(message, "CANCEL"));
        this.setTitle(paragraph.getLanguage(line, "EDUCATIONALPROFILEEDITSTAFFNO6"));


    }
   

     /**初始化表格*/
    private TableModel getTableModel(String[] title, String from, String[] row){
        ResultSet rs = null;
        try {
            String sql = "";
            
            sql = "SELECT * FROM " + from + " WHERE s_no=" + m_Sno;
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


    private void setDelete(){
        String sql = "" ;
        try{
            sql = "DELETE FROM academic_record WHERE no = "+ this.tab_AcademicRecord.getValueAt(this.tab_AcademicRecord.getSelectedRow(),0);
            DBC.executeUpdate(sql);
            tabpan_RecordStateChanged();
        }catch(SQLException e){
            System.out.println(e);
        }
    }

    private void setAdd(){
        String sql = "";
        try{
         sql ="INSERT INTO academic_record (no, s_no, name, commencement_date, completion_date, qualification) " +
            "SELECT IF(COUNT(no) <> 0,MAX(no)+1,0), " +
            "'"+m_Sno+"', " +
            "'"+txt_AcademicRecordInstitutionAttended.getText()+"', " +
            "'"+dateChooser_AcademicRecordCommencementDate.getValue()+"', " +
            "'"+dateChooser_AcademicRecordCompletionDate.getValue()+"', " +
            "'"+txt_AcademicRecordQualificationAttained.getText()+"' " +
            "FROM academic_record";
           DBC.executeUpdate(sql);
           txt_AcademicRecordInstitutionAttended.setText(null);
           txt_AcademicRecordQualificationAttained.setText(null);
           tabpan_RecordStateChanged();
        }catch(SQLException e){
            System.out.println(e);
        }
    }

    private void tabpan_RecordStateChanged() {
        String[] title = {paragraph.getLanguage(line,"NO"),paragraph.getLanguage(line,"NAMEOFINSTITUTIONATTENDED"), paragraph.getLanguage(line,"COMMENCEMENT"), paragraph.getLanguage(line,"COMPLETIONDATE"), paragraph.getLanguage(line,"QUALIFICATIONATTAINED")};
        String[] row = {"no","name","commencement_date","completion_date","qualification"};
        this.tab_AcademicRecord.setModel(getTableModel(title,"academic_record",row));
        setHideColumn(tab_AcademicRecord,0);
        btn__AcademicRecordDelete.setEnabled(false);
        tab_AcademicRecord.setRowHeight(30);
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pan_AcademicRecord = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tab_AcademicRecord = new javax.swing.JTable();
        lab_AcademicRecordInstitutionAttended = new javax.swing.JLabel();
        lab_AcademicRecordCommencementDate = new javax.swing.JLabel();
        lab_AcademicRecordCompletionDate = new javax.swing.JLabel();
        lab_AcademicRecordQualificationAttained = new javax.swing.JLabel();
        txt_AcademicRecordInstitutionAttended = new javax.swing.JTextField();
        txt_AcademicRecordQualificationAttained = new javax.swing.JTextField();
        btn__AcademicRecordAdd = new javax.swing.JButton();
        btn__AcademicRecordDelete = new javax.swing.JButton();
        dateChooser_AcademicRecordCommencementDate = new cc.johnwu.date.DateComboBox();
        dateChooser_AcademicRecordCompletionDate = new cc.johnwu.date.DateComboBox();
        btn_Cancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);

        tab_AcademicRecord.setBorder(new javax.swing.border.MatteBorder(null));
        tab_AcademicRecord.setModel(new javax.swing.table.DefaultTableModel(
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
        tab_AcademicRecord.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tab_AcademicRecordMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tab_AcademicRecord);

        lab_AcademicRecordInstitutionAttended.setText("Name of institution attended :");

        lab_AcademicRecordCommencementDate.setText("Commencement date :");

        lab_AcademicRecordCompletionDate.setText("Completion date :");

        lab_AcademicRecordQualificationAttained.setText("Qualification attained :");

        btn__AcademicRecordAdd.setText("Add");
        btn__AcademicRecordAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn__AcademicRecordAddActionPerformed(evt);
            }
        });

        btn__AcademicRecordDelete.setText("Delete");
        btn__AcademicRecordDelete.setEnabled(false);
        btn__AcademicRecordDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn__AcademicRecordDeleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pan_AcademicRecordLayout = new javax.swing.GroupLayout(pan_AcademicRecord);
        pan_AcademicRecord.setLayout(pan_AcademicRecordLayout);
        pan_AcademicRecordLayout.setHorizontalGroup(
            pan_AcademicRecordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan_AcademicRecordLayout.createSequentialGroup()
                .addGroup(pan_AcademicRecordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 764, Short.MAX_VALUE)
                    .addGroup(pan_AcademicRecordLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pan_AcademicRecordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lab_AcademicRecordCompletionDate)
                            .addComponent(lab_AcademicRecordCommencementDate)
                            .addComponent(lab_AcademicRecordInstitutionAttended)
                            .addComponent(lab_AcademicRecordQualificationAttained))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan_AcademicRecordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pan_AcademicRecordLayout.createSequentialGroup()
                                .addGroup(pan_AcademicRecordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pan_AcademicRecordLayout.createSequentialGroup()
                                        .addGroup(pan_AcademicRecordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txt_AcademicRecordQualificationAttained, javax.swing.GroupLayout.DEFAULT_SIZE, 503, Short.MAX_VALUE)
                                            .addComponent(txt_AcademicRecordInstitutionAttended, javax.swing.GroupLayout.DEFAULT_SIZE, 503, Short.MAX_VALUE))
                                        .addGap(6, 6, 6))
                                    .addGroup(pan_AcademicRecordLayout.createSequentialGroup()
                                        .addComponent(dateChooser_AcademicRecordCommencementDate, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
                                        .addGap(214, 214, 214)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pan_AcademicRecordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(btn__AcademicRecordAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btn__AcademicRecordDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(pan_AcademicRecordLayout.createSequentialGroup()
                                .addComponent(dateChooser_AcademicRecordCompletionDate, javax.swing.GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE)
                                .addGap(324, 324, 324)))))
                .addContainerGap())
        );
        pan_AcademicRecordLayout.setVerticalGroup(
            pan_AcademicRecordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_AcademicRecordLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan_AcademicRecordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab_AcademicRecordInstitutionAttended)
                    .addComponent(txt_AcademicRecordInstitutionAttended, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn__AcademicRecordAdd))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_AcademicRecordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan_AcademicRecordLayout.createSequentialGroup()
                        .addGroup(pan_AcademicRecordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lab_AcademicRecordCommencementDate)
                            .addComponent(dateChooser_AcademicRecordCommencementDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan_AcademicRecordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dateChooser_AcademicRecordCompletionDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lab_AcademicRecordCompletionDate))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan_AcademicRecordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_AcademicRecordQualificationAttained, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lab_AcademicRecordQualificationAttained)))
                    .addComponent(btn__AcademicRecordDelete))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)
                .addContainerGap())
        );

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
                    .addComponent(pan_AcademicRecord, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_Cancel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pan_AcademicRecord, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(23, 23, 23)
                .addComponent(btn_Cancel, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tab_AcademicRecordMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab_AcademicRecordMouseClicked
        if (tab_AcademicRecord.getSelectedRow() != -1) {
            btn__AcademicRecordDelete.setEnabled(true);
        }
}//GEN-LAST:event_tab_AcademicRecordMouseClicked

    private void btn__AcademicRecordAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn__AcademicRecordAddActionPerformed
        if (txt_AcademicRecordInstitutionAttended.getText() != null && txt_AcademicRecordQualificationAttained.getText() != null
        && !txt_AcademicRecordInstitutionAttended.getText().trim().equals("") && !txt_AcademicRecordQualificationAttained.getText().trim().equals("")) {

            setAdd();
        }
    }//GEN-LAST:event_btn__AcademicRecordAddActionPerformed

    private void btn__AcademicRecordDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn__AcademicRecordDeleteActionPerformed
        setDelete();
}//GEN-LAST:event_btn__AcademicRecordDeleteActionPerformed

    private void btn_CancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CancelActionPerformed
        new Frm_HRM(m_Sno,m_UUID).setVisible(true);
        this.dispose();
}//GEN-LAST:event_btn_CancelActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Cancel;
    private javax.swing.JButton btn__AcademicRecordAdd;
    private javax.swing.JButton btn__AcademicRecordDelete;
    private cc.johnwu.date.DateComboBox dateChooser_AcademicRecordCommencementDate;
    private cc.johnwu.date.DateComboBox dateChooser_AcademicRecordCompletionDate;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lab_AcademicRecordCommencementDate;
    private javax.swing.JLabel lab_AcademicRecordCompletionDate;
    private javax.swing.JLabel lab_AcademicRecordInstitutionAttended;
    private javax.swing.JLabel lab_AcademicRecordQualificationAttained;
    private javax.swing.JPanel pan_AcademicRecord;
    private javax.swing.JTable tab_AcademicRecord;
    private javax.swing.JTextField txt_AcademicRecordInstitutionAttended;
    private javax.swing.JTextField txt_AcademicRecordQualificationAttained;
    // End of variables declaration//GEN-END:variables

}
