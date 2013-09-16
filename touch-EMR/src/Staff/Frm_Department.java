package Staff;

import ErrorMessage.StoredErrorMessage;
import java.sql.*;
import cc.johnwu.sql.DBC;
import cc.johnwu.sql.HISModel;
import Multilingual.language;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Frm_Department extends javax.swing.JFrame {

    /*多國語言變數*/
    private language paragraph = new language();
    private String[] line = new String(paragraph.setlanguage("DEPARTMENT")).split("\n") ;
    private String[] message = new String(paragraph.setlanguage("MESSAGE")).split("\n") ;
    /*輸出錯誤資訊變數*/
    StoredErrorMessage ErrorMessage = new StoredErrorMessage() ;

    public Frm_Department() {
        initComponents();
        init();
        initTables();
        initLanguage();
    }
    public void init(){
        this.setExtendedState(Frm_Department.MAXIMIZED_BOTH);  // 最大化
        this.setLocationRelativeTo(this);
        addWindowListener(new WindowAdapter() {                                // 畫面關閉原視窗enable
            @Override
            public void windowClosing(WindowEvent windowevent) {
                btn_CloseActionPerformed(null);
            }
        });
    }
    public void initTables(){
        ResultSet rs = null;
        try {
            rs = DBC.executeQuery("SELECT name FROM department");
            this.tab_DepList.setModel(new TableModels(rs, new String[]{paragraph.getLanguage(line, "DEPARTMENTNAME")}));
        } catch (SQLException e) {
            ErrorMessage.setData("Staff", "Frm_Department" ,"initTables()",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        }
        tab_DepDetail.setModel(getModle(new String[]{paragraph.getLanguage(line, "MESSAGE")},new String[][]{{paragraph.getLanguage(line, "NOINFORMATION")}}));
    }

    private void initLanguage() {
        this.btn_DepNewData.setText(paragraph.getLanguage(line, "DEPNEWDATA"));
        this.btn_DepUpdate.setText(paragraph.getLanguage(line, "DEPUPDATE"));
        this.btn_StaffDepUpdata.setText(paragraph.getLanguage(line, "STAFFDEPUPDATA"));
        this.btn_DepDel.setText(paragraph.getLanguage(line, "DEPDEL"));
        this.btn_Close.setText(paragraph.getLanguage(message, "CLOSE"));
        this.setTitle(paragraph.getLanguage(line, "TITLEDEPARTMENT"));
    }

    public void showTables(){
        if(this.tab_DepList.getSelectedRow()==0){
            this.btn_DepDel.setEnabled(false);
            this.btn_DepUpdate.setEnabled(false);
        }else {
            this.btn_DepDel.setEnabled(true);
            this.btn_DepUpdate.setEnabled(true);
            this.btn_StaffDepUpdata.setEnabled(true);
        }
//        String tabSelect = tab_DepList.getValueAt(this.tab_DepList.getSelectedRow(), 0).toString();
        String sql = "";
        ResultSet rs_dep = null;

        try {
            if(tab_DepList.getSelectedRow()==0){
                sql = " SELECT staff_info.s_no AS NO, " +
                                       "concat(staff_info.firstname, staff_info.lastName) AS Name, " +
                                       "staff_info.nia_no AS ID_NO, " +
                                       "staff_info.s_id AS Account " +
                      " FROM staff_info " +
                      " WHERE staff_info.dep_guid IS null" +
                      " AND staff_info.exist = 1";
            }else if (tab_DepList.getSelectedRow()!=0 && tab_DepList.getSelectedRow()!=-1){
                sql = " SELECT staff_info.s_no AS NO, " +
                              "concat(staff_info.firstname, staff_info.lastName) AS Name, " +
                              "staff_info.nia_no AS ID_NO, " +
                              "staff_info.s_id AS Account " +
                      " FROM staff_info,department " +
                      " WHERE staff_info.dep_guid = department.guid " +
                      " AND department.name = '" + tab_DepList.getValueAt(this.tab_DepList.getSelectedRow(), 0).toString() + "' " +
                      " AND staff_info.exist = 1 " +
                      " ORDER BY s_id";
            }
            System.out.println(sql);
            rs_dep = DBC.executeQuery(sql);
            if (rs_dep.next()) {
                    tab_DepDetail.setModel(HISModel.getModel(rs_dep, null));
                } else {
                    tab_DepDetail.setModel(getModle(new String[]{"Message"},new String[][]{{paragraph.getLanguage(line, "NOFORMATION")}})); //將表格清空
                }
        } catch (SQLException e) {
            ErrorMessage.setData("Staff", "Frm_Department" ,"showTables()",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            System.out.println(e);
        }
    }
    public JTable getTabDepList(){
        return tab_DepList;
    }
            /** 設定表單預設模型。*/
    private DefaultTableModel getModle(String[] title,String[][] data){
        return new DefaultTableModel(data,title){
                @Override
                public boolean isCellEditable(int r, int c){
                return false;}};
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        tab_DepList = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        tab_DepDetail = new javax.swing.JTable();
        jPanel9 = new javax.swing.JPanel();
        btn_DepDel = new javax.swing.JButton();
        btn_StaffDepUpdata = new javax.swing.JButton();
        btn_DepUpdate = new javax.swing.JButton();
        btn_DepNewData = new javax.swing.JButton();
        btn_Close = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Department Management");

        tab_DepList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tab_DepList.setRowHeight(25);
        tab_DepList.getTableHeader().setReorderingAllowed(false);
        tab_DepList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tab_DepListMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tab_DepListMouseReleased(evt);
            }
        });
        tab_DepList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tab_DepListKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(tab_DepList);

        tab_DepDetail.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tab_DepDetail.setRowHeight(25);
        tab_DepDetail.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(tab_DepDetail);

        btn_DepDel.setText("Delete Name");
        btn_DepDel.setEnabled(false);
        btn_DepDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_DepDelActionPerformed(evt);
            }
        });

        btn_StaffDepUpdata.setText("Edit Department");
        btn_StaffDepUpdata.setEnabled(false);
        btn_StaffDepUpdata.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_StaffDepUpdataActionPerformed(evt);
            }
        });

        btn_DepUpdate.setText("Edit Name");
        btn_DepUpdate.setEnabled(false);
        btn_DepUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_DepUpdateActionPerformed(evt);
            }
        });

        btn_DepNewData.setText("Add");
        btn_DepNewData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_DepNewDataActionPerformed(evt);
            }
        });

        btn_Close.setText("Close");
        btn_Close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CloseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btn_StaffDepUpdata, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btn_DepUpdate, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
            .addComponent(btn_DepNewData, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
            .addComponent(btn_DepDel, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
            .addComponent(btn_Close, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(btn_DepNewData)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_DepUpdate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_StaffDepUpdata)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_DepDel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 467, Short.MAX_VALUE)
                .addComponent(btn_Close))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 506, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 630, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 630, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tab_DepListMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab_DepListMouseReleased
        if(this.tab_DepList.getRowCount()>1){
            this.btn_StaffDepUpdata.setEnabled(true);
        }else{
            this.btn_StaffDepUpdata.setEnabled(false);
        }
        showTables();
}//GEN-LAST:event_tab_DepListMouseReleased

    private void tab_DepListKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tab_DepListKeyReleased
        if(this.tab_DepList.getRowCount()>1){
            this.btn_StaffDepUpdata.setEnabled(true);
        }else{
            this.btn_StaffDepUpdata.setEnabled(false);
        }
        tab_DepListMouseClicked(null);
        showTables();
}//GEN-LAST:event_tab_DepListKeyReleased

    private void btn_DepDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_DepDelActionPerformed
        Object getValue = tab_DepList.getValueAt(tab_DepList.getSelectedRow(), 0);
        String sql = "DELETE FROM department WHERE name = '" + getValue.toString() +"'";
        Object[] options = {paragraph.getLanguage(message , "YES"),paragraph.getLanguage(message , "NO")};
        int response = JOptionPane.showOptionDialog(
                            new Frame(),
                            paragraph.getLanguage(message , "DOYOUWANTTODELETE"),
                            paragraph.getLanguage(message , "MESSAGE"),
                            JOptionPane.YES_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options,
                            options[0]
                        );
        if(response==0){
            try {
                DBC.executeUpdate(sql);

            } catch (SQLException e) {
                ErrorMessage.setData("Staff", "Frm_Department" ,"btn_DepDelActionPerformed()",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            }
        }
        tab_DepDetail.setModel(getModle(new String[]{"Message"},new String[][]{{paragraph.getLanguage(line, "NOFORMATION")}})); //將表格清空
        initTables();
        this.btn_DepDel.setEnabled(false);
        this.btn_DepUpdate.setEnabled(false);
        this.btn_StaffDepUpdata.setEnabled(false);
}//GEN-LAST:event_btn_DepDelActionPerformed

    private void btn_DepUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_DepUpdateActionPerformed
        Object getValue = tab_DepList.getValueAt(tab_DepList.getSelectedRow(), 0);
        Frm_UpdateMod frm_Mod = new Frm_UpdateMod(this,getValue);
        frm_Mod.setVisible(true);
        this.btn_DepDel.setEnabled(false);
        this.btn_DepUpdate.setEnabled(false);
        this.btn_StaffDepUpdata.setEnabled(false);
}//GEN-LAST:event_btn_DepUpdateActionPerformed

    private void btn_DepNewDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_DepNewDataActionPerformed
        Frm_AddMod frm_Mod = new Frm_AddMod(this);
        frm_Mod.setVisible(true);
        this.btn_DepDel.setEnabled(false);
        this.btn_DepUpdate.setEnabled(false);
        this.btn_StaffDepUpdata.setEnabled(false);
}//GEN-LAST:event_btn_DepNewDataActionPerformed

    private void btn_StaffDepUpdataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_StaffDepUpdataActionPerformed
        new Frm_DepartmentAdd().setVisible(true);
}//GEN-LAST:event_btn_StaffDepUpdataActionPerformed

    private void btn_CloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CloseActionPerformed
        new Main.Frm_Main().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btn_CloseActionPerformed

    private void tab_DepListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab_DepListMouseClicked
        if (tab_DepList.getValueAt(tab_DepList.getSelectedRow(), 0).equals("Doctor")) {
            btn_DepUpdate.setEnabled(false);
            btn_DepDel.setEnabled(false);
        }
    }//GEN-LAST:event_tab_DepListMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Close;
    private javax.swing.JButton btn_DepDel;
    private javax.swing.JButton btn_DepNewData;
    private javax.swing.JButton btn_DepUpdate;
    private javax.swing.JButton btn_StaffDepUpdata;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable tab_DepDetail;
    private javax.swing.JTable tab_DepList;
    // End of variables declaration//GEN-END:variables
}
