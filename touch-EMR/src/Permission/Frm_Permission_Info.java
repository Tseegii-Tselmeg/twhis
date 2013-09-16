package Permission;

import ErrorMessage.StoredErrorMessage;
import Multilingual.language;
import cc.johnwu.sql.DBC; 
import java.awt.Frame;
import java.sql.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author opo10818
 */
public class Frm_Permission_Info extends javax.swing.JFrame {
    /*多國語言變數*/
    private language paragraph = new language();
    private String[] line = paragraph.setlanguage("PERMISSIONINFO").split("\n");
    private String[] message = paragraph.setlanguage("MESSAGE").split("\n");
    /*輸出錯誤資訊變數*/
    StoredErrorMessage ErrorMessage = new StoredErrorMessage();
    /*資料庫變數*/
    String sql = "";
    ResultSet rs = null;

    /** Creates new form Frm_Permission_Info */
    public Frm_Permission_Info() {
        initComponents();
        initTableName();
        initTableShow();
        initLanguage();
        init();
    }


    public void init () {
        /** 視窗最大化 */
        this.setExtendedState(this.MAXIMIZED_BOTH);
        this.setLocationRelativeTo(this);
        /** 畫面關閉原視窗enable */
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowevent) {
                btn_BackActionPerformed(null);
            }
        });
    }
    /**初始化Table_Name內容*/
    public void initTableName () {
        try{
            sql = "SELECT grp_name FROM permission_info WHERE sys_name = 'System' AND grp_name <> 'ADMIN'";
            rs = DBC.executeQuery(sql);
            Object[] str_Column = {paragraph.getLanguage(line, "GROUP")};
            int row = 0 ;
            while(rs.next()){
                row++;
            }
            Object[][] str_Row = new Object[row][1] ;
            rs = DBC.executeQuery(sql);
            row = 0;
            while(rs.next()){
                str_Row[row][0] = rs.getString("grp_name");
                row++ ;
            }
            TableModel tableData = new DefaultTableModel(str_Row,str_Column) ;
            tab_name.setModel(tableData);
            tab_name.setRowHeight(30);
            tab_name.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);     // tabble不可按住多選
        }catch(SQLException e){
                ErrorMessage.setData("Permission", "Frm_Permission_Info" ,"initTable()",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        }finally{
            sql = "" ;
            try{
                DBC.closeConnection(rs);
            }catch(Exception e){
                ErrorMessage.setData("Permission", "Frm_Permission_Info" ,"initTable()",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            }
        }
    }
    public void initTableShow () {
        Object[] str_Column = {paragraph.getLanguage(message, "MESSAGE")};
        Object[][] str_Row = {{paragraph.getLanguage(message, "NOINFORMATION")}};
        TableModel tableData = new DefaultTableModel(str_Row,str_Column) ;
        this.tab_Show.setModel(tableData);
        tab_Show.setRowHeight(30);
    }
    /**變更物件文字（多國語言）*/
    public void initLanguage () {
        this.btn_Add.setText(paragraph.getLanguage(line, "NEWLIMIT"));
        this.btn_Back.setText(paragraph.getLanguage(message, "BACK"));
        this.btn_Delete.setText(paragraph.getLanguage(line, "DELETEGROUP"));
        this.btn_Update.setText(paragraph.getLanguage(line, "UPDATE"));
        this.setTitle(paragraph.getLanguage(line, "TITLEPERMISSION"));
    }
    /**點選點選name表單後show表單的變化*/
    public void setTableModel (String group) {
        /*宣告表單Column變數*/
        Object[] str_Column = {paragraph.getLanguage(line, "SYSTEM"),
                               paragraph.getLanguage(line, "LNQUIRY"),
                               paragraph.getLanguage(line, "ADD"),
                               paragraph.getLanguage(line, "UPDATE"),
                               paragraph.getLanguage(line, "DELETE"),
                               paragraph.getLanguage(line, "ALL")} ;
        try{
            sql = "SELECT * FROM permission_info WHERE grp_name = '" + group + "' ORDER BY sys_name";
            rs = DBC.executeQuery(sql);
            int row = 0 ;
            while(rs.next()){
                row++ ;
            }
            Object[][] str_Row = new Object[row][6] ;
            row = 0 ;
            rs = DBC.executeQuery(sql);
            for(int i = 0 ; i < str_Row.length ; i++){
                for(int j = 1 ; j < str_Row[i].length ; j++){
                    str_Row[i][j] = false ;
                }
            }
            while(rs.next()) {
                str_Row[row][0] = rs.getString("sys_name");
                /**判斷權限*/
                int determineNumber = rs.getInt("lvl") ;
                /**判斷有無全部權限*/
                if(determineNumber == 15 ) str_Row[row][5] = true ;

                while(determineNumber != 0) {
                    if(determineNumber >= 8) {
                        str_Row[row][4] = true ;
                        determineNumber-=8 ;
                    }else if(determineNumber >= 4) {
                        str_Row[row][3] = true ;
                        determineNumber-=4 ;
                    }else if(determineNumber >= 2) {
                        str_Row[row][2] = true ;
                        determineNumber-=2 ;
                    }else if(determineNumber == 1) {
                        str_Row[row][1] = true ;
                        determineNumber-=1 ;
                    }
                }
                row++ ;
            }
            TableModel tableData = new DefaultTableModel(str_Row,str_Column) {
                @Override
                public Class getColumnClass(int columnIndex) {
                    if(columnIndex!=0)
                        return Boolean.class ;
                    else
                        return String.class ;
                }
                @Override
                public boolean isCellEditable(int rowIndex, int mColIndex) {
                    if(mColIndex==0)
                        return false;
                    else
                        return true;
                }
            } ;
            tab_Show.setModel(tableData);
            tab_Show.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            tab_Show.setRowHeight(30);

            
        }catch(SQLException e){
                ErrorMessage.setData("Permission", "Frm_Permission_Info" ,"setTableModel()",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        }finally{
            sql = "" ;
            this.tab_Show.setEnabled(false);
            try{
                DBC.closeConnection(rs);
            }catch(Exception e){
                ErrorMessage.setData("Permission", "Frm_Permission_Info" ,"setTableModel()",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            }
        }

        
    }
    /**表單的變更及儲存*/
    public void setUpdateGroup (String groupName) {
        for(int i = 0 ; i < this.tab_Show.getRowCount() ; i++){
            int Calculations = 0 ;
            if(this.tab_Show.getValueAt(i, 1).equals(true)) Calculations+=1 ;
            if(this.tab_Show.getValueAt(i, 2).equals(true)) Calculations+=2 ;
            if(this.tab_Show.getValueAt(i, 3).equals(true)) Calculations+=4 ;
            if(this.tab_Show.getValueAt(i, 4).equals(true)) Calculations+=8 ;
            sql = "UPDATE permission_info SET lvl = '" + Calculations + "' " +
                    "WHERE grp_name = '" + groupName + "' " +
                    "AND sys_name = '" + this.tab_Show.getValueAt(i, 0) + "' ";        
            try{
                DBC.executeUpdate(sql);
            }catch(SQLException e){
                    ErrorMessage.setData("Permission", "Frm_Permission_Info" ,"initTable()",
                    e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            }finally{
                sql = "" ;
                try{
                    DBC.closeConnection(rs);
                    setTableModel(groupName);
                }catch(Exception e){
                    ErrorMessage.setData("Permission", "Frm_Permission_Info" ,"setUpdateGroup()",
                    e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
                }
            }
        }
        JOptionPane.showConfirmDialog(null, "Saved successfully",
                                                 "Message" ,
                                                 JOptionPane.CLOSED_OPTION);
    }

    public void setTable_Check (int row , int Column , boolean isCheck){
        for(int i = 1 ; i < Column ; i++){
            this.tab_Show.setValueAt(isCheck, row, i);
        }
    }

    public boolean isChick (int row){
        for(int i = 1 ; i < 5 ; i++)
            if(this.tab_Show.getValueAt(row, i).equals(false)) return false;
        return true ;
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tab_name = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tab_Show = new javax.swing.JTable();
        pan_Button = new javax.swing.JPanel();
        btn_Back = new javax.swing.JButton();
        btn_Delete = new javax.swing.JButton();
        btn_Update = new javax.swing.JButton();
        btn_Add = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Permission");

        tab_name.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tab_name.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tab_nameMouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tab_name);

        tab_Show.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tab_Show.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tab_ShowMouseReleased(evt);
            }
        });
        jScrollPane2.setViewportView(tab_Show);

        btn_Back.setText("Close");
        btn_Back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_BackActionPerformed(evt);
            }
        });

        btn_Delete.setText("Delete");
        btn_Delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_DeleteActionPerformed(evt);
            }
        });

        btn_Update.setText("Edit");
        btn_Update.setEnabled(false);
        btn_Update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_UpdateActionPerformed(evt);
            }
        });

        btn_Add.setText("Add");
        btn_Add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_AddActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pan_ButtonLayout = new javax.swing.GroupLayout(pan_Button);
        pan_Button.setLayout(pan_ButtonLayout);
        pan_ButtonLayout.setHorizontalGroup(
            pan_ButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_ButtonLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan_ButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan_ButtonLayout.createSequentialGroup()
                        .addComponent(btn_Delete, javax.swing.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(pan_ButtonLayout.createSequentialGroup()
                        .addComponent(btn_Back, javax.swing.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(pan_ButtonLayout.createSequentialGroup()
                        .addComponent(btn_Update, javax.swing.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(pan_ButtonLayout.createSequentialGroup()
                        .addComponent(btn_Add, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        pan_ButtonLayout.setVerticalGroup(
            pan_ButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_ButtonLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_Add)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Update)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Delete)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Back)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pan_Button, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pan_Button, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 595, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 595, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void btn_AddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_AddActionPerformed
        new Frm_Permission_NewData(this).setVisible(true);
//        this.btn_Add.setEnabled(false);
    }//GEN-LAST:event_btn_AddActionPerformed

    private void btn_UpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_UpdateActionPerformed
        if(this.btn_Update.getText().equals(paragraph.getLanguage(line, "UPDATE"))){
            this.btn_Update.setText(paragraph.getLanguage(line, "SAVE"));
            this.tab_Show.setEnabled(true);
        }else if(this.btn_Update.getText().equals(paragraph.getLanguage(line, "SAVE"))){
            setUpdateGroup(String.valueOf(this.tab_name.getValueAt(this.tab_name.getSelectedRow(), 0))) ;
            this.btn_Update.setText(paragraph.getLanguage(line, "UPDATE"));
        }
    }//GEN-LAST:event_btn_UpdateActionPerformed

    private void btn_DeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_DeleteActionPerformed
        Object[] options = {"Yes","No"};
        int response = JOptionPane.showOptionDialog(
                        new Frame(),
                        paragraph.getLanguage(message, "WILLITBEDELETE")+
                        this.tab_name.getValueAt(this.tab_name.getSelectedRow(), 0)+" ?",
                        paragraph.getLanguage(message, "MESSAGE"),
                        JOptionPane.YES_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]);
        if (response == 0){
            sql = "Delete FROM permission_info WHERE grp_name = '" + this.tab_name.getValueAt(this.tab_name.getSelectedRow(), 0) + "'";
            this.btn_Delete.setEnabled(false);
            this.btn_Update.setEnabled(false);
        }
        try {
                DBC.executeUpdate(sql);
        } catch (SQLException e) {
            ErrorMessage.setData("Permission", "Frm_PermissionInfo" ,"btn_DeleteGroupActionPerformed()",
            e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        }finally{
            sql = "" ;
            initTableName();
            initTableShow();
            try{
                DBC.closeConnection(rs);
                setTableModel(String.valueOf(this.tab_name.getValueAt(this.tab_name.getSelectedRow(), 0)));
            }catch(Exception e){
                ErrorMessage.setData("Permission", "Frm_Permission_Info" ,"btn_DeleteActionPerformed()",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            }
        }
    }//GEN-LAST:event_btn_DeleteActionPerformed

    private void btn_BackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_BackActionPerformed
        new Main.Frm_Main().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btn_BackActionPerformed

    private void tab_nameMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab_nameMouseReleased
        if(this.btn_Update.getText().equals(paragraph.getLanguage(line, "SAVE"))){
            this.btn_Update.setText(paragraph.getLanguage(line, "UPDATE"));
            this.tab_Show.setEnabled(false);
        }

        setTableModel(String.valueOf(this.tab_name.getValueAt(this.tab_name.getSelectedRow(), 0)));

        if (tab_name.getValueAt(tab_name.getSelectedRow(), 0).equals("Adminstration")) {
            btn_Update.setEnabled(false);
            btn_Delete.setEnabled(false);
        }  else {
            btn_Delete.setEnabled(true);
            btn_Update.setEnabled(true);
        }
    }//GEN-LAST:event_tab_nameMouseReleased

    private void tab_ShowMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab_ShowMouseReleased
        if(this.btn_Update.getText().equals(paragraph.getLanguage(line, "SAVE"))){
            int selectRow = this.tab_Show.getSelectedRow();
            int selectColumn = this.tab_Show.getSelectedColumn() ;
            if (selectColumn==5 && this.tab_Show.getValueAt(selectRow, selectColumn).equals(true)) {
                setTable_Check(selectRow, selectColumn, true) ;
            } else if (selectColumn==5 && this.tab_Show.getValueAt(selectRow, selectColumn).equals(false)) {
                setTable_Check(selectRow, selectColumn, false) ;
            } else {
                if(isChick(selectRow))
                    this.tab_Show.setValueAt(true, selectRow, 5);
                else
                    this.tab_Show.setValueAt(false, selectRow, 5);
            }
        }
    }//GEN-LAST:event_tab_ShowMouseReleased
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Add;
    private javax.swing.JButton btn_Back;
    private javax.swing.JButton btn_Delete;
    private javax.swing.JButton btn_Update;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel pan_Button;
    private javax.swing.JTable tab_Show;
    private javax.swing.JTable tab_name;
    // End of variables declaration//GEN-END:variables
}
