package Staff;
import cc.johnwu.sql.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.sql.SQLException;
import Multilingual.language;
/**
 *
 * @author steven
 */
public class Frm_Administrative extends javax.swing.JFrame {
    private int m_Sno;
    private String m_UUID = null;
    /*多國語言變數*/
    
    private language paragraph = new language();
    private String[] line = new String(paragraph.setlanguage("ADMINISTRATIVE")).split("\n") ;
    private String[] message = new String(paragraph.setlanguage("MESSAGE")).split("\n") ;
     
     
    public Frm_Administrative(int sno, String uuid) {
        m_UUID = uuid;
        m_Sno = sno;
        initComponents();
        init();
        initLanguage();
    }

 
  private void init(){

      if (m_UUID != null) {
            this.setTitle(paragraph.getLanguage(line, "ADMINISTRATIVEPROFILE") + " ("+paragraph.getLanguage(line, "NEWSTAFFNO") +m_Sno +" )");
        } else {
            this.setTitle(paragraph.getLanguage(line, "ADMINISTRATIVEPROFILE") + " ("+paragraph.getLanguage(line, "EDITSTAFFNO") +m_Sno +" )");
        }
        
        this.setExtendedState(Frm_Educational.MAXIMIZED_BOTH);  // 最大化
        this.setLocationRelativeTo(this);//視窗顯示至中
        addWindowListener(new WindowAdapter() {//視窗關閉事件
            @Override
            public void windowClosing(WindowEvent windowevent)
            {
                btn_CancelActionPerformed(null);
            }
        });
        tabpan_RecordStateChanged();
    }

   private void initLanguage() {
        this.lab_EmpName.setText(paragraph.getLanguage(line, "EMPNAME"));
        this.lab_EmpPosition.setText(paragraph.getLanguage(line, "EMPPOSITION"));
        this.lab_EmpCommencementDate.setText(paragraph.getLanguage(line, "EMPCOMMENCEMENTDATE"));
        this.lab_EmpLeavingDate.setText(paragraph.getLanguage(line, "EMPLEAVINGDATE"));

        this.btn_EmpHistoryAdd.setText(paragraph.getLanguage(line, "EMPHISTORYADD"));
        this.btn_EmpHistoryDelete.setText(paragraph.getLanguage(line, "EMPHISTORYDELETE"));
        this.btn_Cancel.setText(paragraph.getLanguage(message, "CANCEL"));

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
        }  finally {
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

    /**回傳現在選個表單*/
    private String getPanelName(){
        String  str = "employment_history";
        return str;
    }


    private void setDelete(String from){
        String sql = "" ;
        try{
            if(from.equals("employment_history")){
                sql = "DELETE FROM " + from + " WHERE no = "+ this.tab_Emp.getValueAt(this.tab_Emp.getSelectedRow(),0);
            }
            DBC.executeUpdate(sql);
            tabpan_RecordStateChanged();
        }catch(SQLException e){
            System.out.println(e);
        }
    }

    private void setAdd(String from){
        String sql = "";
        try{
           if(from.equals("employment_history")){
                sql ="INSERT INTO employment_history (no, s_no, employer_name, position, commencement_date, leaving_date) " +
                    "SELECT IF(COUNT(no) <> 0,MAX(no)+1,0), " +
                    "'"+m_Sno+"', " +
                    "'"+txt_EmpName.getText()+"', " +
                    "'"+txt_EmpPosition.getText()+"', " +
                    "'"+dateComboBox1.getValue()+"', " +
                    "'"+dateChooser_EmpLeavingDate.getValue()+"' " +
                    "FROM employment_history";
            }
             DBC.executeUpdate(sql);
             tabpan_RecordStateChanged();
             }catch(SQLException e){
            System.out.println(e);
        }

    }

    private void tabpan_RecordStateChanged() {

        String[] title = {paragraph.getLanguage(line,"NO"),paragraph.getLanguage(line,"NAMEOFEMPLOYER"), paragraph.getLanguage(line,"POSITION"),paragraph.getLanguage(line,"COMMENCEMENTDATE") , paragraph.getLanguage(line,"LEAVINGDATE")};
        String[] row = {"no","employer_name","position","commencement_date","leaving_date"};
        this.tab_Emp.setModel(getTableModel(title,"employment_history",row));
        setHideColumn(tab_Emp,0);
        btn_EmpHistoryDelete.setEnabled(false);
        tab_Emp.setRowHeight(30);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pan_EmploymentHistory = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tab_Emp = new javax.swing.JTable();
        lab_EmpName = new javax.swing.JLabel();
        lab_EmpPosition = new javax.swing.JLabel();
        lab_EmpCommencementDate = new javax.swing.JLabel();
        lab_EmpLeavingDate = new javax.swing.JLabel();
        txt_EmpName = new javax.swing.JTextField();
        txt_EmpPosition = new javax.swing.JTextField();
        btn_EmpHistoryAdd = new javax.swing.JButton();
        btn_EmpHistoryDelete = new javax.swing.JButton();
        dateComboBox1 = new cc.johnwu.date.DateComboBox();
        dateChooser_EmpLeavingDate = new cc.johnwu.date.DateComboBox();
        btn_Cancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);

        tab_Emp.setModel(new javax.swing.table.DefaultTableModel(
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
        tab_Emp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tab_EmpMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tab_Emp);

        lab_EmpName.setText("Name of employer :");

        lab_EmpPosition.setText("position :");

        lab_EmpCommencementDate.setText("Commencement date :");

        lab_EmpLeavingDate.setText("Leaving date :");

        btn_EmpHistoryAdd.setText("Add");
        btn_EmpHistoryAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_EmpHistoryAddActionPerformed(evt);
            }
        });

        btn_EmpHistoryDelete.setText("Delete");
        btn_EmpHistoryDelete.setEnabled(false);
        btn_EmpHistoryDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_EmpHistoryDeleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pan_EmploymentHistoryLayout = new javax.swing.GroupLayout(pan_EmploymentHistory);
        pan_EmploymentHistory.setLayout(pan_EmploymentHistoryLayout);
        pan_EmploymentHistoryLayout.setHorizontalGroup(
            pan_EmploymentHistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_EmploymentHistoryLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan_EmploymentHistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 752, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pan_EmploymentHistoryLayout.createSequentialGroup()
                        .addGroup(pan_EmploymentHistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lab_EmpLeavingDate)
                            .addComponent(lab_EmpPosition)
                            .addComponent(lab_EmpCommencementDate)
                            .addComponent(lab_EmpName))
                        .addGap(12, 12, 12)
                        .addGroup(pan_EmploymentHistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pan_EmploymentHistoryLayout.createSequentialGroup()
                                .addComponent(dateChooser_EmpLeavingDate, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                                .addGap(356, 356, 356))
                            .addGroup(pan_EmploymentHistoryLayout.createSequentialGroup()
                                .addComponent(dateComboBox1, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                                .addGap(356, 356, 356))
                            .addGroup(pan_EmploymentHistoryLayout.createSequentialGroup()
                                .addGroup(pan_EmploymentHistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_EmpPosition, javax.swing.GroupLayout.DEFAULT_SIZE, 481, Short.MAX_VALUE)
                                    .addComponent(txt_EmpName, javax.swing.GroupLayout.DEFAULT_SIZE, 481, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pan_EmploymentHistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(btn_EmpHistoryAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btn_EmpHistoryDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap())
        );
        pan_EmploymentHistoryLayout.setVerticalGroup(
            pan_EmploymentHistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_EmploymentHistoryLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan_EmploymentHistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan_EmploymentHistoryLayout.createSequentialGroup()
                        .addGroup(pan_EmploymentHistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_EmpName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lab_EmpName))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan_EmploymentHistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lab_EmpPosition)
                            .addComponent(txt_EmpPosition, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan_EmploymentHistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lab_EmpCommencementDate)
                            .addComponent(dateComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan_EmploymentHistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dateChooser_EmpLeavingDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lab_EmpLeavingDate)))
                    .addGroup(pan_EmploymentHistoryLayout.createSequentialGroup()
                        .addComponent(btn_EmpHistoryAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_EmpHistoryDelete)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE)
                .addContainerGap())
        );

        btn_Cancel.setText("Cancel");
        btn_Cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btn_Cancel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pan_EmploymentHistory, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pan_EmploymentHistory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Cancel)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tab_EmpMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab_EmpMouseClicked
        if (tab_Emp.getSelectedRow() != -1) {
            btn_EmpHistoryDelete.setEnabled(true);
        }
}//GEN-LAST:event_tab_EmpMouseClicked

    private void btn_EmpHistoryAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_EmpHistoryAddActionPerformed
        if (txt_EmpName.getText() != null && txt_EmpPosition.getText() != null
        && !txt_EmpName.getText().trim().equals("") && !txt_EmpPosition.getText().trim().equals("")) {
            setAdd(getPanelName());
        }
}//GEN-LAST:event_btn_EmpHistoryAddActionPerformed

    private void btn_EmpHistoryDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_EmpHistoryDeleteActionPerformed
        setDelete(getPanelName());
}//GEN-LAST:event_btn_EmpHistoryDeleteActionPerformed

    private void btn_CancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CancelActionPerformed
        new Frm_HRM(m_Sno,m_UUID).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btn_CancelActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Cancel;
    private javax.swing.JButton btn_EmpHistoryAdd;
    private javax.swing.JButton btn_EmpHistoryDelete;
    private cc.johnwu.date.DateComboBox dateChooser_EmpLeavingDate;
    private cc.johnwu.date.DateComboBox dateComboBox1;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel lab_EmpCommencementDate;
    private javax.swing.JLabel lab_EmpLeavingDate;
    private javax.swing.JLabel lab_EmpName;
    private javax.swing.JLabel lab_EmpPosition;
    private javax.swing.JPanel pan_EmploymentHistory;
    private javax.swing.JTable tab_Emp;
    private javax.swing.JTextField txt_EmpName;
    private javax.swing.JTextField txt_EmpPosition;
    // End of variables declaration//GEN-END:variables

}
