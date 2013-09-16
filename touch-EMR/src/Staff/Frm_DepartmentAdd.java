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
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

public class Frm_DepartmentAdd extends javax.swing.JFrame {
    java.awt.event.ActionEvent ActionEvt;
    Boolean SaveJudge = false;
    String Original_Guid = null;   
    String Change_Guid = null;
    /*多國語言變數*/
    private language paragraph = new language();
    private String[] line = new String(paragraph.setlanguage("DEPARTMENTADD")).split("\n") ;
    private String[] message = new String(paragraph.setlanguage("MESSAGE")).split("\n") ;
    /*輸出錯誤資訊變數*/
    StoredErrorMessage ErrorMessage = new StoredErrorMessage() ;

    public Frm_DepartmentAdd() {
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
    }

    public void initComboBox(){
        try {
            ResultSet rs = null;
                    rs = DBC.executeQuery("SELECT * FROM department"); //  取得部門資料
            this.cob_OriginalDep.addItem("None");
            while(rs.next()){  // 將部門資料加入到COMBOBOX
                this.cob_OriginalDep.addItem(new DepGuidAdd(rs.getString("name"),rs.getString("guid")));
            }
        } catch (SQLException e) {
            ErrorMessage.setData("Staff", "Frm_DepartmentAdd" ,"initComboBox()",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        }
    }

    private void initLanguage() {
        this.lab_OriginalDep.setText(paragraph.getLanguage(line, "ORIGINALDEP"));
        this.lab_ChangeDep.setText(paragraph.getLanguage(line, "CHANGEDEP"));
        this.btn_Save.setText(paragraph.getLanguage(message, "SAVE"));
        this.btn_Leave.setText(paragraph.getLanguage(line, "LEAVE"));
        this.setTitle(paragraph.getLanguage(line, "ADDSTAFFDEPARTMENT"));
    }
    public void setTableModel(JTable table, Object selectValue){
        ResultSet rs = null;
        String sql = "";

        if(selectValue.toString().equals("None")){
            sql = " SELECT staff_info.s_no AS NO, " +
                          "concat(staff_info.firstname, staff_info.lastName) AS Name, " +
                          "staff_info.s_id AS Account " +
                  " FROM staff_info " +
                  " WHERE staff_info.dep_guid IS null" +
                  " AND exist = 1";
        }else{
            sql = " SELECT staff_info.s_no AS NO, " +
                            " concat(staff_info.firstname, staff_info.lastName) AS Name, " +
                            " staff_info.s_id AS ACCOUNT  " +
                      " FROM (staff_info, department) " +
                      " WHERE staff_info.dep_guid = department.guid " +
                      " AND department.name = '" + selectValue.toString() + "'" +
                      " AND exist = 1";
        }

        try {
            System.out.println(sql);
            rs = DBC.executeQuery(sql);
            if(rs.next()){
                table.setModel(new MyDepTableModel(rs, new String[]{"", "No.","Name","Account"}));
            }else
                table.setModel(getModle(new String[]{"Message"},new String[][]{{"No Information."}}));
        } catch (SQLException e) {
            ErrorMessage.setData("Staff", "Frm_DepartmentAdd" ,"initComboBox()",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        } finally {
            try{
                DBC.closeConnection(rs);
            }catch (SQLException e) {
                ErrorMessage.setData("Staff", "Frm_DepartmentAdd" ,"initComboBox() - DBC.closeConnection",
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
        tab_OriginalDep = new javax.swing.JTable();
        lab_OriginalDep = new javax.swing.JLabel();
        cob_OriginalDep = new javax.swing.JComboBox();
        btn_Save = new javax.swing.JButton();
        btn_Leave = new javax.swing.JButton();
        cob_ChangeDep = new javax.swing.JComboBox();
        lab_ChangeDep = new javax.swing.JLabel();
        btn_RightToLeft = new javax.swing.JButton();
        btn_LeftToRight = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tab_ChangeDep = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Add Staff Department");
        setAlwaysOnTop(true);

        tab_OriginalDep.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tab_OriginalDep.setRowHeight(25);
        jScrollPane1.setViewportView(tab_OriginalDep);

        lab_OriginalDep.setText("Department");

        cob_OriginalDep.setMinimumSize(new java.awt.Dimension(40, 19));
        cob_OriginalDep.setPreferredSize(new java.awt.Dimension(50, 21));
        cob_OriginalDep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cob_OriginalDepActionPerformed(evt);
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

        cob_ChangeDep.setPreferredSize(new java.awt.Dimension(100, 21));
        cob_ChangeDep.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cob_ChangeDepItemStateChanged(evt);
            }
        });
        cob_ChangeDep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cob_ChangeDepActionPerformed(evt);
            }
        });

        lab_ChangeDep.setText("Department");

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

        tab_ChangeDep.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tab_ChangeDep.setRowHeight(25);
        jScrollPane2.setViewportView(tab_ChangeDep);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 47, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(lab_OriginalDep)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cob_OriginalDep, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btn_LeftToRight, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btn_RightToLeft, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(lab_ChangeDep)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cob_ChangeDep, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
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
                            .addComponent(cob_OriginalDep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lab_OriginalDep)
                            .addComponent(cob_ChangeDep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lab_ChangeDep))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 473, Short.MAX_VALUE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 473, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btn_Leave)
                            .addComponent(btn_Save))
                        .addGap(29, 29, 29))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(219, 219, 219)
                        .addComponent(btn_RightToLeft, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_LeftToRight, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 221, Short.MAX_VALUE)))
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

    private void btn_LeftToRightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_LeftToRightActionPerformed
        ResultSet rs = null;
        String sql = null;
        setTables(tab_ChangeDep,tab_OriginalDep);
        try {
            if(!cob_OriginalDep.getSelectedItem().toString().equals("None")){
                sql = "SELECT guid " +
                                    "FROM department " +
                                    "WHERE name = '" +this.cob_OriginalDep.getSelectedItem().toString() + "'";
                System.out.println(sql);
                rs = DBC.executeQuery(sql);
                if(rs.next())
                Original_Guid = rs.getString("guid");

            }
            DBC.closeConnection(rs);
            if(!cob_ChangeDep.getSelectedItem().toString().equals("None")){
                sql = "SELECT guid " +
                      "FROM department " +
                      "WHERE name = '" +this.cob_ChangeDep.getSelectedItem().toString() + "'";
                System.out.println(sql);
                rs = DBC.executeQuery(sql);
                if(rs.next())
                Change_Guid = "'" + rs.getString("guid") + "'";
            }

            System.out.println("Original_Guid = " + Original_Guid + ", Change_Guid = " + Change_Guid );
        } catch (SQLException e) {
            ErrorMessage.setData("Staff", "Frm_DepartmentAdd" ,"btn_LeftToRightActionPerformed()",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        }
        SaveJudge = true;
}//GEN-LAST:event_btn_LeftToRightActionPerformed

    private void btn_LeaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_LeaveActionPerformed
        
        this.dispose();
}//GEN-LAST:event_btn_LeaveActionPerformed

    private void btn_SaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SaveActionPerformed
        try {

            if(tab_OriginalDep.getColumnCount()!=1){
                for(int i = 0; i < tab_OriginalDep.getRowCount(); i++){
                    if(this.cob_OriginalDep.getSelectedItem().toString().equals("None")){
                        String sql = "UPDATE staff_info SET Dep_guid = NULL WHERE s_no = " + tab_OriginalDep.getValueAt(i, 1) ;
                        System.out.println(sql);
                        DBC.executeUpdate(sql);
                    }else{
                        String sql = "UPDATE staff_info SET Dep_guid = " + Original_Guid + " WHERE s_no = " + tab_OriginalDep.getValueAt(i, 1) ;
                        DBC.executeUpdate(sql);
                        System.out.println(sql);
                    }
                }
            }


            if(tab_ChangeDep.getColumnCount()!=1){
                for(int i = 0; i < tab_ChangeDep.getRowCount(); i++){
                    if(this.cob_ChangeDep.getSelectedItem().toString().equals("None")){
                        String sql = "UPDATE staff_info SET Dep_guid = NULL WHERE s_no = " + tab_ChangeDep.getValueAt(i, 1) ;
                        DBC.executeUpdate(sql);
                        System.out.println(sql);
                    }else{
                        String sql = "UPDATE staff_info SET Dep_guid = " + Change_Guid + " WHERE s_no = " + tab_ChangeDep.getValueAt(i, 1) ;
                        DBC.executeUpdate(sql);
                    }

                }
            }
        } catch (SQLException e) {
            ErrorMessage.setData("Staff", "Frm_DepartmentAdd" ,"btn_SaveActionPerformed()",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        }
        JOptionPane.showConfirmDialog(null, paragraph.getLanguage(message , "SAVEACCESS"),
                                    paragraph.getLanguage(message , "MESSAGE"), JOptionPane.CLOSED_OPTION);
        this.SaveJudge = false;
}//GEN-LAST:event_btn_SaveActionPerformed

    private void cob_OriginalDepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cob_OriginalDepActionPerformed
        ResultSet rs = null;
        if(SaveJudge){
            Object[] options = {paragraph.getLanguage(line , "YES"),paragraph.getLanguage(line , "NO")};
            int response = JOptionPane.showOptionDialog(
                                new Frame(),
                                paragraph.getLanguage(message , "DOYOUWANTTOSAVETHECHANGE"),
                                paragraph.getLanguage(message , "ERRORMESSAGE"),
                                JOptionPane.YES_OPTION,
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                options,
                                options[0]
                            );
            if(response==0){
                btn_SaveActionPerformed(ActionEvt);
                cob_OriginalDepActionPerformed(ActionEvt);
                SaveJudge = false;
            }

            else if(response==1){
                SaveJudge = false;
                cob_OriginalDepActionPerformed(ActionEvt);
            }

        }else{
            setTableModel(tab_OriginalDep, cob_OriginalDep.getSelectedItem());
            try {
                rs = DBC.executeQuery("SELECT * FROM department");
                cob_ChangeDep.removeAllItems();
                if(!cob_OriginalDep.getSelectedItem().toString().equals("None")){
                    this.cob_ChangeDep.addItem("None");
                }
                while (rs.next()) {
                    if (!rs.getString("name").equals(cob_OriginalDep.getSelectedItem().toString())) {
                        cob_ChangeDep.addItem(new DepGuidAdd(rs.getString("name"), rs.getString("guid")));
                    }
                }
            } catch (SQLException e) {
                ErrorMessage.setData("Staff", "Frm_DepartmentAdd" ,"cob_OriginalDepActionPerformed()",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            } finally {
                try{
                    DBC.closeConnection(rs);
                }catch (SQLException e) {
                    ErrorMessage.setData("Staff", "Frm_DepartmentAdd" ,"cob_OriginalDepActionPerformed() - DBC.closeConnection",
                    e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
                }
            }
            setTableModel(this.tab_ChangeDep, this.cob_ChangeDep.getSelectedItem());
        }
}//GEN-LAST:event_cob_OriginalDepActionPerformed

    private void btn_RightToLeftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_RightToLeftActionPerformed
        ResultSet rs = null;
        String sql = null;
        setTables(tab_OriginalDep,tab_ChangeDep);
        try {
            if(!cob_OriginalDep.getSelectedItem().toString().equals("None")){
                sql = "SELECT guid " +
                      "FROM department " +
                      "WHERE name = '" +this.cob_OriginalDep.getSelectedItem().toString() + "'";
                System.out.println(sql);
                rs = DBC.executeQuery(sql);
                if(rs.next())
                Original_Guid = rs.getString("guid");

            }
            DBC.closeConnection(rs);
            if(!cob_ChangeDep.getSelectedItem().toString().equals("None")){
                sql = "SELECT guid " +
                      "FROM department " +
                      "WHERE name = '" +this.cob_ChangeDep.getSelectedItem().toString() + "'";
                System.out.println(sql);
                rs = DBC.executeQuery(sql);
                if(rs.next())
                Change_Guid = "'" + rs.getString("guid") + "'";
            }

            System.out.println("Original_Guid = " + Original_Guid + ", Change_Guid = " + Change_Guid );
        } catch (SQLException e) {
                ErrorMessage.setData("Staff", "Frm_DepartmentAdd" ,"btn_RightToLeftActionPerformed()",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        }
        SaveJudge = true;
}//GEN-LAST:event_btn_RightToLeftActionPerformed

    private void cob_ChangeDepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cob_ChangeDepActionPerformed

}//GEN-LAST:event_cob_ChangeDepActionPerformed

    private void cob_ChangeDepItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cob_ChangeDepItemStateChanged
        if(SaveJudge){
            Object[] options = {paragraph.getLanguage(line , "YES"),paragraph.getLanguage(line , "NO")};
            int response = JOptionPane.showOptionDialog(
                                new Frame(),
                                paragraph.getLanguage(message , "DOYOUWANTTOSAVETHECHANGE"),
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
                cob_ChangeDepItemStateChanged(evt);
            }
        }
        if(evt.getStateChange() == java.awt.event.ItemEvent.SELECTED){
            setTableModel(tab_ChangeDep, cob_ChangeDep.getSelectedItem());
        }
    }//GEN-LAST:event_cob_ChangeDepItemStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Leave;
    private javax.swing.JButton btn_LeftToRight;
    private javax.swing.JButton btn_RightToLeft;
    private javax.swing.JButton btn_Save;
    private javax.swing.JComboBox cob_ChangeDep;
    private javax.swing.JComboBox cob_OriginalDep;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lab_ChangeDep;
    private javax.swing.JLabel lab_OriginalDep;
    private javax.swing.JTable tab_ChangeDep;
    private javax.swing.JTable tab_OriginalDep;
    // End of variables declaration//GEN-END:variables

}
class MyDepTableModel extends AbstractTableModel {
    Object[][] rowData = null;
    String[] columnName, rowName = null;
    int row=0, col=0, rowCount=0, columnCount=0;
    boolean creatDefaultColumnName = false;
    /*輸出錯誤資訊變數*/
    StoredErrorMessage ErrorMessage = new StoredErrorMessage() ;
    MyDepTableModel (ResultSet rs, String[] columnName){
        this.columnName = columnName ;

        //System.out.println(this.columnName[0]);
        try {
            columnCount = rs.getMetaData().getColumnCount();
            rs.last();
            rowCount = rs.getRow();
            rs.first();
            rowData = new Object[rowCount][columnCount+1];
            if(this.columnName == null){
                creatDefaultColumnName = true;
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
            if(creatDefaultColumnName)
            while (columnCount-- != 0) {
                this.columnName[columnCount] = rs.getMetaData().getColumnName(columnCount + 1);
            }
        } catch (SQLException e) {
                ErrorMessage.setData("Staff", "Frm_DepartmentAdd" ,"MyDepTableModel - MyDepTableModel()",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        }
    }
    MyDepTableModel(Object[][] tableDate, String[] columnName){
        this.rowData = tableDate;
        this.columnName = columnName ;

        if(this.columnName == null){
            creatDefaultColumnName = true;
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
class DepGuidItem{
    String name;
    String guid;
    DepGuidItem(String name, String guid){
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


class DepGuidAdd{
    String name;
    String guid;
    DepGuidAdd(String name, String guid){
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