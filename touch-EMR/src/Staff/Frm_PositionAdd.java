package Staff;

import ErrorMessage.StoredErrorMessage;
import cc.johnwu.sql.DBC;
import Multilingual.language;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

public class Frm_PositionAdd extends javax.swing.JFrame {
    java.awt.event.ActionEvent ActionEvt;
    Boolean SaveJudge = false;
    String Original_Guid = null;
    String Change_Guid = null;
    /*多國語言變數*/
    private language paragraph = new language();
    private String[] line = new String(paragraph.setlanguage("POSITIONADD")).split("\n") ;
    private String[] message = new String(paragraph.setlanguage("MESSAGE")).split("\n") ;
    /*輸出錯誤資訊變數*/
    StoredErrorMessage ErrorMessage = new StoredErrorMessage() ;

    public Frm_PositionAdd() {
        initComponents();
        init();
        initComboBox();
        initLanguage();
    }
    public void init(){
        addWindowListener(new WindowAdapter() {  // 畫面關閉原視窗enable
            @Override
            public void windowClosing(WindowEvent windowevent) {
                btn_LeaveActionPerformed(null);
            }
        });
        this.setLocationRelativeTo(this);
        tab_OriginalPos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tab_ChangePos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    public void initComboBox(){
        ResultSet leftRs = null;
        try {
            leftRs = DBC.executeQuery("SELECT * FROM position");
            this.cob_OriginalPos.addItem("None");
            while(leftRs.next()){
                this.cob_OriginalPos.addItem(new PosGuidAdd(leftRs.getString(2),leftRs.getString(1)));
            }
        } catch (SQLException e) {
            ErrorMessage.setData("Staff", "Frm_PositionAdd" ,"initComboBox()",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        } finally {
            try{DBC.closeConnection(leftRs);}
            catch (SQLException e) {
                ErrorMessage.setData("Staff", "Frm_PositionAdd" ,"initComboBox() - DBC.closeConnection",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            }
        }
        
        for(int i = 0; i < cob_OriginalPos.getItemCount(); i ++){
            System.out.println(cob_OriginalPos.getSelectedItem().toString());
        }
    }

    private void initLanguage() {
        this.lab_OriginalPos.setText(paragraph.getLanguage(line, "ORIGINALDEP"));
        this.lab_ChangePos.setText(paragraph.getLanguage(line, "CHANGEDEP"));
        this.btn_Save.setText(paragraph.getLanguage(message, "SAVE"));
        this.btn_Leave.setText(paragraph.getLanguage(line, "LEAVE"));
        this.setTitle(paragraph.getLanguage(line, "ADDSTAFFPOSITION"));

    }
    public void setTableModel(JTable table, Object selectValue){
        ResultSet rs = null;
        String sql = "";

        if(selectValue.toString().equals("None")){
            sql = "SELECT staff_info.s_no AS NO, " +
                         "concat(staff_info.firstname, staff_info.lastName) AS Name, " +
                         "staff_info.s_id AS Account " +
                 " FROM staff_info " +
                 " WHERE staff_info.posi_guid IS null" +
                 " AND exist = 1";
        }else{
            sql = " SELECT staff_info.s_no AS NO, " +
                            " concat(staff_info.firstname, staff_info.lastName) AS Name, " +
                            " staff_info.s_id AS ACCOUNT  " +
                      " FROM staff_info, position " +
                      " WHERE staff_info.posi_guid = position.guid " +
                      " AND position.name = '" + selectValue.toString() + "'" +
                      " AND exist = 1 ";
        }

        try {
            System.out.println(sql);
            rs = DBC.executeQuery(sql);
            if(rs.next()){
                table.setModel(new MyPosiTableModel(rs, new String[]{"", "No.","Name","Account"}));
            }else
                table.setModel(getModle(new String[]{"Message"},new String[][]{{"No Information."}}));
        } catch (SQLException e) {
            ErrorMessage.setData("Staff", "Frm_PositionAdd" ,"setTableModel(JTable table, Object selectValue)",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        } finally {
            try{
                DBC.closeConnection(rs);
            }catch (SQLException e) {
                ErrorMessage.setData("Staff", "Frm_PositionAdd" ,"setTableModel(JTable table, Object selectValue) - DBC.closeConnection",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            }
        }
    }
    public void setTables(JTable tabReceive, JTable tabSend){
        Object[][] Receive = null, Stay = null;
        int StayCount = 0,SendCount=0;
        int RowCount=0;

        for(int i = 0; i < tabSend.getRowCount(); i++){
            if(tabSend.getValueAt(i, 0).equals(true)){
                SendCount+=1;// 紀錄總共有幾筆被選取
            }else if(tabSend.getValueAt(i, 0).equals(false)){
                StayCount +=1;// 紀錄總共有幾筆沒被選取
            }
        }
        System.out.println("送出 = " + SendCount + " 保留 = " + StayCount);
        if(tabReceive.getColumnCount()!=1){
            Receive = new Object[(tabReceive.getRowCount()+SendCount)][4];
            Stay = new Object[StayCount][4];
        }else{
            Receive = new Object[SendCount][4];
            Stay = new Object[StayCount][4];
        }
        

        System.out.println((tabReceive.getRowCount()+SendCount) + "  " +  StayCount);

        
        //接收方的值加入陣列
        if(tabReceive.getColumnCount()!=1){
            for(int i = 0; i < tabReceive.getRowCount(); i++){
                for(int j = 0; j < 4; j++){
                    Receive[i][j] = tabReceive.getValueAt(i, j);
                }RowCount++;
            }
        }else{
            Receive = new Object[SendCount][4];
        }

        System.out.println(" RowCount = " + RowCount);
        //將要傳的值加入陣列
        for(int i = 0 ; i < tabSend.getRowCount() ; i++){
            if(tabSend.getValueAt(i, 0).equals(true)){
                for(int j = 0; j < 4; j++){
                    Receive[RowCount][j] = tabSend.getValueAt(i,j);
                }RowCount++;
            }
        }

        RowCount = 0;
        if(Receive.length!=0){
            tabReceive.setModel(new MyPosiTableModel(Receive,new String[]{"", "No.","Name","Account"}));
        }else{
            tabReceive.setModel(getModle(new String[]{"Message"},new String[][]{{"No Information."}}));
        }
        
        //保留的值
        for(int i = 0; i < tabSend.getRowCount(); i++){
            if(tabSend.getValueAt(i, 0).equals(false)){
                for(int j = 0; j < 4; j++){
                    Stay[RowCount][j] = tabSend.getValueAt(i, j);
                }RowCount++;
            }
        }RowCount=0;
        if(Stay.length!=0){
            tabSend.setModel(new MyPosiTableModel(Stay,new String[]{"", "No.","Name","Account"}));
        }else{
            tabSend.setModel(getModle(new String[]{"Message"},new String[][]{{"No Information."}}));
        }


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

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tab_OriginalPos = new javax.swing.JTable();
        lab_OriginalPos = new javax.swing.JLabel();
        cob_OriginalPos = new javax.swing.JComboBox();
        btn_Save = new javax.swing.JButton();
        btn_Leave = new javax.swing.JButton();
        cob_ChangePos = new javax.swing.JComboBox();
        lab_ChangePos = new javax.swing.JLabel();
        btn_RightToLeft = new javax.swing.JButton();
        btn_LeftToRight = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tab_ChangePos = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Add Staff Position");
        setAlwaysOnTop(true);

        tab_OriginalPos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tab_OriginalPos.setRowHeight(25);
        jScrollPane1.setViewportView(tab_OriginalPos);

        lab_OriginalPos.setText("Position Title");

        cob_OriginalPos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cob_OriginalPosActionPerformed(evt);
            }
        });

        btn_Save.setText("Save");
        btn_Save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_SaveActionPerformed(evt);
            }
        });

        btn_Leave.setText("Close");
        btn_Leave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_LeaveActionPerformed(evt);
            }
        });

        cob_ChangePos.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cob_ChangePosItemStateChanged(evt);
            }
        });

        lab_ChangePos.setText("Position Title");

        btn_RightToLeft.setText("<<");
        btn_RightToLeft.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_RightToLeftActionPerformed(evt);
            }
        });

        btn_LeftToRight.setText(">>");
        btn_LeftToRight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_LeftToRightActionPerformed(evt);
            }
        });

        tab_ChangePos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tab_ChangePos.setRowHeight(25);
        jScrollPane2.setViewportView(tab_ChangePos);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 34, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 37, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addComponent(lab_OriginalPos)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cob_OriginalPos, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 349, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btn_RightToLeft)
                                    .addComponent(btn_LeftToRight)
                                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(lab_ChangePos)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cob_ChangePos, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 351, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap(563, Short.MAX_VALUE)
                        .addComponent(btn_Save, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_Leave, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cob_OriginalPos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lab_OriginalPos)
                            .addComponent(lab_ChangePos)
                            .addComponent(cob_ChangePos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_Save)
                            .addComponent(btn_Leave)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(216, 216, 216)
                        .addComponent(btn_RightToLeft, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_LeftToRight, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_LeaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_LeaveActionPerformed
        this.dispose();
}//GEN-LAST:event_btn_LeaveActionPerformed

    private void btn_SaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SaveActionPerformed
        try {

            if(tab_OriginalPos.getColumnCount()!=1){
                for(int i = 0; i < tab_OriginalPos.getRowCount(); i++){
                    if(this.cob_OriginalPos.getSelectedItem().toString().equals("None")){
                        String sql = "UPDATE staff_info SET posi_guid = NULL WHERE s_no = " + tab_OriginalPos.getValueAt(i, 1) ;
                        System.out.println(sql);
                        DBC.executeUpdate(sql);
                    }else{
                        String sql = "UPDATE staff_info SET posi_guid = " + Original_Guid + " WHERE s_no = " + tab_OriginalPos.getValueAt(i, 1) ;
                        DBC.executeUpdate(sql);
                        System.out.println(sql);
                    }
                }
            }


            if(tab_ChangePos.getColumnCount()!=1){
                for(int i = 0; i < tab_ChangePos.getRowCount(); i++){
                    if(this.cob_ChangePos.getSelectedItem().toString().equals("None")){
                        String sql = "UPDATE staff_info SET posi_guid = NULL WHERE s_no = " + tab_ChangePos.getValueAt(i, 1) ;
                        DBC.executeUpdate(sql);
                        System.out.println(sql);
                    }else{
                        String sql = "UPDATE staff_info SET posi_guid = " + Change_Guid + " WHERE s_no = " + tab_ChangePos.getValueAt(i, 1) ;
                        DBC.executeUpdate(sql);
                    }

                }
            }
        } catch (SQLException e) {
            ErrorMessage.setData("Staff", "Frm_PositionAdd" ,"btn_SaveActionPerformed()",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        }
        JOptionPane.showConfirmDialog(null, paragraph.getLanguage(message , "SAVEACCESS"),
                                    paragraph.getLanguage(message , "MESSAGE"), JOptionPane.CLOSED_OPTION);
        this.SaveJudge = false;
}//GEN-LAST:event_btn_SaveActionPerformed

    private void cob_OriginalPosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cob_OriginalPosActionPerformed
        ResultSet rs = null;
        if(SaveJudge){
            Object[] options = {paragraph.getLanguage(line , "YES"),paragraph.getLanguage(line , "NO")};
            int response = JOptionPane.showOptionDialog(
                                new Frame(),
                                paragraph.getLanguage(message , "DOYOUSAVEIT"),
                                paragraph.getLanguage(message , "ERRORMESSAGE"),
                                JOptionPane.YES_OPTION,
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                options,
                                options[0]
                            );
            if(response==0){
                btn_SaveActionPerformed(ActionEvt);
                cob_OriginalPosActionPerformed(ActionEvt);
                SaveJudge = false;
            }

            else if(response==1){
                cob_OriginalPosActionPerformed(ActionEvt);
                SaveJudge = false;
            }

        }else{
            setTableModel(tab_OriginalPos, cob_OriginalPos.getSelectedItem());
            try {
                rs = DBC.executeQuery("SELECT * FROM position");
                cob_ChangePos.removeAllItems();
                if(!cob_OriginalPos.getSelectedItem().toString().equals("None")){
                    this.cob_ChangePos.addItem("None");
                }
                while (rs.next()) {
                    if (!rs.getString("name").equals(cob_OriginalPos.getSelectedItem().toString())) {
                        cob_ChangePos.addItem(new PosGuidAdd(rs.getString("name"), rs.getString("guid")));
                    }
                }
            } catch (SQLException e) {
                ErrorMessage.setData("Staff", "Frm_PositionAdd" ,"cob_OriginalPosActionPerformed()",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            } finally {
                try{
                    DBC.closeConnection(rs);
                }catch (SQLException e) {
                    ErrorMessage.setData("Staff", "Frm_PositionAdd" ,"cob_OriginalPosActionPerformed() - DBC.closeConnection",
                    e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
                }
            }
            setTableModel(this.tab_ChangePos, this.cob_ChangePos.getSelectedItem());
        }

        

}//GEN-LAST:event_cob_OriginalPosActionPerformed

    private void btn_RightToLeftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_RightToLeftActionPerformed
        ResultSet rs = null;
        String sql = null;
        setTables(tab_OriginalPos,tab_ChangePos);
        try {
            if(!cob_OriginalPos.getSelectedItem().toString().equals("None")){
                sql = "SELECT guid " +
                                    "FROM position " +
                                    "WHERE name = '" +this.cob_OriginalPos.getSelectedItem().toString() + "'";
                System.out.println(sql);
                rs = DBC.executeQuery(sql);
                if(rs.next())
                Original_Guid = rs.getString("guid");

            }
            DBC.closeConnection(rs);
            if(!cob_ChangePos.getSelectedItem().toString().equals("None")){
                sql = "SELECT guid " +
                      "FROM position " +
                      "WHERE name = '" +this.cob_ChangePos.getSelectedItem().toString() + "'";
                System.out.println(sql);
                rs = DBC.executeQuery(sql);
                if(rs.next())
                Change_Guid =  "'" + rs.getString("guid") + "'";
            }
            
            System.out.println("Original_Guid = " + Original_Guid + ", Change_Guid = " + Change_Guid );
        } catch (SQLException e) {
                ErrorMessage.setData("Staff", "Frm_PositionAdd" ,"btn_RightToLeftActionPerformed()",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        }
        SaveJudge = true;
}//GEN-LAST:event_btn_RightToLeftActionPerformed

    private void btn_LeftToRightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_LeftToRightActionPerformed
        ResultSet rs = null;
        String sql = null;
        setTables(tab_ChangePos,tab_OriginalPos);
        try {
            if(!cob_OriginalPos.getSelectedItem().toString().equals("None")){
                sql = "SELECT guid " +
                                    "FROM position " +
                                    "WHERE name = '" +this.cob_OriginalPos.getSelectedItem().toString() + "'";
                System.out.println(sql);
                rs = DBC.executeQuery(sql);
                if(rs.next())
                Original_Guid = rs.getString("guid");

            }
            DBC.closeConnection(rs);
            if(!cob_ChangePos.getSelectedItem().toString().equals("None")){
                sql = "SELECT guid " +
                      "FROM position " +
                      "WHERE name = '" +this.cob_ChangePos.getSelectedItem().toString() + "'";
                System.out.println(sql);
                rs = DBC.executeQuery(sql);
                if(rs.next())
                Change_Guid = "'" + rs.getString("guid") + "'";
            }

            System.out.println("Original_Guid = " + Original_Guid + ", Change_Guid = " + Change_Guid );
        } catch (SQLException e) {
                ErrorMessage.setData("Staff", "Frm_PositionAdd" ,"btn_LeftToRightActionPerformed()",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        }
        SaveJudge = true;

}//GEN-LAST:event_btn_LeftToRightActionPerformed

    private void cob_ChangePosItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cob_ChangePosItemStateChanged

        if(SaveJudge){
            Object[] options = {paragraph.getLanguage(line , "YES"),paragraph.getLanguage(line , "NO")};
            int response = JOptionPane.showOptionDialog(
                                new Frame(),
                                paragraph.getLanguage(message , "DOYOUSAVEIT"),
                                paragraph.getLanguage(message , "ERRORMESSAGE"),
                                JOptionPane.YES_OPTION,
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                options,
                                options[0]
                            );
            if(response==0){
                btn_SaveActionPerformed(ActionEvt);
                SaveJudge = false;
            }
                
            else if(response==1){
                SaveJudge = false;
                cob_ChangePosItemStateChanged(evt);
            }
        }
        if(evt.getStateChange() == java.awt.event.ItemEvent.SELECTED){
            setTableModel(tab_ChangePos, cob_ChangePos.getSelectedItem());
        }
    }//GEN-LAST:event_cob_ChangePosItemStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Leave;
    private javax.swing.JButton btn_LeftToRight;
    private javax.swing.JButton btn_RightToLeft;
    private javax.swing.JButton btn_Save;
    private javax.swing.JComboBox cob_ChangePos;
    private javax.swing.JComboBox cob_OriginalPos;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lab_ChangePos;
    private javax.swing.JLabel lab_OriginalPos;
    private javax.swing.JTable tab_ChangePos;
    private javax.swing.JTable tab_OriginalPos;
    // End of variables declaration//GEN-END:variables
}
class MyPosiTableModel extends AbstractTableModel {
    Object[][] rowData = null;
    String[] columnName, rowName = null;
    int row=0, col=0, rowCount=0, columnCount=0;
    boolean createDefaultColumnName = false;
    /*輸出錯誤資訊變數*/
    StoredErrorMessage ErrorMessage = new StoredErrorMessage() ;

    MyPosiTableModel (ResultSet rs, String[] columnName){
        this.columnName = columnName ;
        try {
            columnCount = rs.getMetaData().getColumnCount();
            rs.last();
            rowCount = rs.getRow();
            rs.first();
            rowData = new Object[rowCount][columnCount+1];
            if(this.columnName == null){
                createDefaultColumnName = true;
                this.columnName = new String[columnCount];
            }

            //set row data
            do {
                rowData[row][0] = new Boolean(false);
                for (col = 0; col < columnCount; col++) {
                        rowData[row][col+1] = rs.getString(col + 1);
                }
                row++;
            } while (row<rowCount && rs.next());

            //set column title
            if(createDefaultColumnName)
            while (columnCount-- != 0) {
                this.columnName[columnCount] = rs.getMetaData().getColumnName(columnCount + 1);
            }
        } catch (SQLException e) {
                ErrorMessage.setData("Staff", "Frm_PositionAdd" ,"MyPosiTableModel - MyPosiTableModel()",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        }
    }

    MyPosiTableModel(Object[][] tableDate, String[] columnName){
        this.rowData = tableDate;
        this.columnName = columnName ;

        if(this.columnName == null){
            createDefaultColumnName = true;
            this.columnName = new String[columnCount];
        }
    }

    @Override
    public int getColumnCount() {
        return this.columnName.length ;
    }

    @Override
    public int getRowCount() {
        return this.rowData.length;
    }

    @Override
    public String getColumnName(int col) {
        return this.columnName[col];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return this.rowData[rowIndex][columnIndex];
    }

    @Override
    public Class getColumnClass(int columnIndex){
        return this.getValueAt(0,columnIndex).getClass();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
	return true;
    }
    @Override
    public void setValueAt(Object value, int row, int col){
        rowData[row][col] = value;
        fireTableCellUpdated(row, col);
    }
}
class PosGuidAdd{
    String name;
    String guid;
    PosGuidAdd(String name, String guid){
        this.name = name;
        this.guid = guid;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
    public void setGuid(String guid){
        this.guid = guid;
    }
    public String getGuid(){
        return guid;
    }
    @Override
    public String toString(){
        return name;
    }
}