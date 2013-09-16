package Staff;

import ErrorMessage.StoredErrorMessage;
import cc.johnwu.sql.DBC;
import cc.johnwu.sql.HISModel;
import Multilingual.language;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Frm_Position extends javax.swing.JFrame {
    /*多國語言變數*/
    private language paragraph = new language();
    private String[] line = new String(paragraph.setlanguage("DEPARTMENT")).split("\n") ;
    private String[] message = new String(paragraph.setlanguage("MESSAGE")).split("\n") ;
    /*輸出錯誤資訊變數*/
    StoredErrorMessage ErrorMessage = new StoredErrorMessage() ;

    public Frm_Position() {
        initComponents();
        init();
        initTables();
        initLanguage();
    }
    public void init(){
        this.setExtendedState(Frm_Position.MAXIMIZED_BOTH);  // 最大化
        this.setLocationRelativeTo(this);

        addWindowListener(new WindowAdapter() {  // 畫面關閉原視窗enable
            @Override
            public void windowClosing(WindowEvent windowevent) {
                 btn_BackActionPerformed(null);
            }
        });
    }

    public void initTables(){
        ResultSet rs = null;
        try {
            rs = DBC.executeQuery("SELECT name FROM position");
            this.tab_PosList.setModel(new TableModels(rs, new String[]{paragraph.getLanguage(line, "POSITIONNAME")}));
        } catch (SQLException e) {
            ErrorMessage.setData("Staff", "Frm_ Position" ,"initTables()",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            System.out.println(e);
        }
        tab_PosDetail.setModel(getModle(new String[]{paragraph.getLanguage(line, "MESSAGE")},new String[][]{{paragraph.getLanguage(line, "NOINFORMATION")}}));
    }

    private void initLanguage() {
        this.btn_PosNewData.setText(paragraph.getLanguage(line, "DEPNEWDATA"));
        this.btn_PosUpdate.setText(paragraph.getLanguage(line, "DEPUPDATE"));
        this.btn_StaffPosUpdata.setText(paragraph.getLanguage(line, "STAFFPOSIUPDATA"));
        this.btn_PosDel.setText(paragraph.getLanguage(line, "DEPDEL"));
        this.btn_Back.setText(paragraph.getLanguage(message, "CLOSE"));
        this.setTitle(paragraph.getLanguage(line, "TITLESTAFFPOSITION"));

    }
    public void showTables(){
        if(this.tab_PosList.getSelectedRow()==0){
            this.btn_PosDel.setEnabled(false);
            this.btn_PosUpdate.setEnabled(false);
            this.btn_StaffPosUpdata.setEnabled(true);
        }else {
            this.btn_PosDel.setEnabled(true);
            this.btn_PosUpdate.setEnabled(true);
            this.btn_StaffPosUpdata.setEnabled(true);
        }

//        String tabSelect = tab_PosList.getValueAt(this.tab_PosList.getSelectedRow(), 0).toString();
        String sql = "";
        ResultSet rs_Pos = null;

        try {
            if(tab_PosList.getSelectedRow()==0){
                sql = " SELECT staff_info.s_no AS NO, " +
                                       "concat(staff_info.firstname, staff_info.lastName) AS Name, " +
                                       "staff_info.nia_no AS ID_NO, " +
                                       "staff_info.s_id AS Account " +
                        " FROM staff_info " +
                        " WHERE staff_info.posi_guid IS null" +
                        " AND staff_info.exist = 1";
            }else if (tab_PosList.getSelectedRow()!=0 && tab_PosList.getSelectedRow()!=-1){
                sql = " SELECT staff_info.s_no AS NO, " +
                                       "concat(staff_info.firstname, staff_info.lastName) AS Name, " +
                                       "staff_info.nia_no AS ID_NO, " +
                                       "staff_info.s_id AS Account " +
                        " FROM staff_info,position " +
                        " WHERE staff_info.posi_guid = position.guid " +
                        " AND position.name = '" + tab_PosList.getValueAt(this.tab_PosList.getSelectedRow(), 0).toString() + "' " +
                        " AND staff_info.exist = 1 ORDER BY s_id";
            }
            rs_Pos = DBC.executeQuery(sql);
            if (rs_Pos.next()) {
                tab_PosDetail.setModel(HISModel.getModel(rs_Pos, null));
            } else {
                tab_PosDetail.setModel(getModle(new String[]{"Message"},new String[][]{{paragraph.getLanguage(line, "NOFORMATION")}})); //將表格清空
            }
        } catch (SQLException e) {
            ErrorMessage.setData("Staff", "Frm_ Position" ,"showTables()",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            System.out.println(e);
        }
    }
    public JTable getTabPoslist(){
        return tab_PosList;
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
        tab_PosList = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        tab_PosDetail = new javax.swing.JTable();
        jPanel9 = new javax.swing.JPanel();
        btn_PosDel = new javax.swing.JButton();
        btn_StaffPosUpdata = new javax.swing.JButton();
        btn_PosUpdate = new javax.swing.JButton();
        btn_PosNewData = new javax.swing.JButton();
        btn_Back = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Staff Position");

        tab_PosList.setModel(new javax.swing.table.DefaultTableModel(
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
        tab_PosList.setRowHeight(25);
        tab_PosList.getTableHeader().setReorderingAllowed(false);
        tab_PosList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tab_PosListMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tab_PosListMouseReleased(evt);
            }
        });
        tab_PosList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tab_PosListKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(tab_PosList);

        tab_PosDetail.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tab_PosDetail.setRowHeight(25);
        tab_PosDetail.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(tab_PosDetail);

        btn_PosDel.setText("Delete Name");
        btn_PosDel.setEnabled(false);
        btn_PosDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_PosDelActionPerformed(evt);
            }
        });

        btn_StaffPosUpdata.setText("Edit Postion");
        btn_StaffPosUpdata.setEnabled(false);
        btn_StaffPosUpdata.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_StaffPosUpdataActionPerformed(evt);
            }
        });

        btn_PosUpdate.setText("Edit Name");
        btn_PosUpdate.setEnabled(false);
        btn_PosUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_PosUpdateActionPerformed(evt);
            }
        });

        btn_PosNewData.setText("Add");
        btn_PosNewData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_PosNewDataActionPerformed(evt);
            }
        });

        btn_Back.setText("Close");
        btn_Back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_BackActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_PosDel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
                    .addComponent(btn_StaffPosUpdata, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
                    .addComponent(btn_PosUpdate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
                    .addComponent(btn_PosNewData, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(btn_Back, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)
                        .addGap(1, 1, 1))))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(btn_PosNewData)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_PosUpdate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_StaffPosUpdata)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_PosDel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 481, Short.MAX_VALUE)
                .addComponent(btn_Back))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 509, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 644, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 644, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tab_PosListMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab_PosListMouseReleased
        
        if(this.tab_PosList.getRowCount()>1){
            this.btn_StaffPosUpdata.setEnabled(true);
        }else{
            this.btn_StaffPosUpdata.setEnabled(false);
        }
        showTables();
}//GEN-LAST:event_tab_PosListMouseReleased

    private void tab_PosListKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tab_PosListKeyReleased
        
        if(this.tab_PosList.getRowCount()>1){
            this.btn_StaffPosUpdata.setEnabled(true);
        }else{
            this.btn_StaffPosUpdata.setEnabled(false);
        }
        showTables();
}//GEN-LAST:event_tab_PosListKeyReleased

    private void btn_PosDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_PosDelActionPerformed
        Object getValue = tab_PosList.getValueAt(tab_PosList.getSelectedRow(), 0);
        String sql = "DELETE FROM position WHERE name = '" + getValue.toString() +"'";
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
                ErrorMessage.setData("Staff", "Frm_ Position" ,"btn_PosDelActionPerformed()",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            }
        }
        tab_PosDetail.setModel(getModle(new String[]{"Message"},new String[][]{{paragraph.getLanguage(line, "NOFORMATION")}})); //將表格清空
        initTables();
        this.btn_PosDel.setEnabled(false);
        this.btn_PosUpdate.setEnabled(false);
        this.btn_StaffPosUpdata.setEnabled(false);
}//GEN-LAST:event_btn_PosDelActionPerformed

    private void btn_StaffPosUpdataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_StaffPosUpdataActionPerformed
        new Frm_PositionAdd().setVisible(true);
}//GEN-LAST:event_btn_StaffPosUpdataActionPerformed

    private void btn_PosUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_PosUpdateActionPerformed
        Object getValue = tab_PosList.getValueAt(tab_PosList.getSelectedRow(), 0);
        Frm_UpdateMod frm_Mod = new Frm_UpdateMod(this,getValue);
        frm_Mod.setVisible(true);
        this.btn_PosDel.setEnabled(false);
        this.btn_PosUpdate.setEnabled(false);
        this.btn_StaffPosUpdata.setEnabled(false);
}//GEN-LAST:event_btn_PosUpdateActionPerformed

    private void btn_PosNewDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_PosNewDataActionPerformed
        Frm_AddMod frm_Mod = new Frm_AddMod(this);
        frm_Mod.setVisible(true);
        this.btn_PosDel.setEnabled(false);
        this.btn_PosUpdate.setEnabled(false);
        this.btn_StaffPosUpdata.setEnabled(false);
}//GEN-LAST:event_btn_PosNewDataActionPerformed

    private void btn_BackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_BackActionPerformed
        new Main.Frm_Main().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btn_BackActionPerformed

    private void tab_PosListMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab_PosListMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tab_PosListMousePressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Back;
    private javax.swing.JButton btn_PosDel;
    private javax.swing.JButton btn_PosNewData;
    private javax.swing.JButton btn_PosUpdate;
    private javax.swing.JButton btn_StaffPosUpdata;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable tab_PosDetail;
    private javax.swing.JTable tab_PosList;
    // End of variables declaration//GEN-END:variables

}
