
package Permission;

import ErrorMessage.StoredErrorMessage;
import Multilingual.language;
import cc.johnwu.sql.DBC;
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
public class Frm_Permission_NewData extends javax.swing.JFrame {
    Frm_Permission_Info fpi ;
    /*多國語言變數*/
    private language paragraph = new language();
    private String[] line = paragraph.setlanguage("PERMISSIONINFO").split("\n");
    private String[] message = paragraph.setlanguage("MESSAGE").split("\n");
    /*輸出錯誤資訊變數*/
    StoredErrorMessage ErrorMessage = new StoredErrorMessage();
    /*資料庫變數*/
    String sql = "";
    ResultSet rs = null;

    /** Creates new form Frm_Permission_NewData */
    public Frm_Permission_NewData(Frm_Permission_Info FPI) {
        fpi = FPI;
        initComponents();
        this.setLocationRelativeTo(this);
        init();
        initLanguage();
    }
    /**變更物件文字（多國語言）*/
    public void initLanguage() {
        this.btn_Save.setText(paragraph.getLanguage(line, "SAVE"));
        this.btn_Close.setText(paragraph.getLanguage(line, "CLOSE"));
        this.lab_Name.setText(paragraph.getLanguage(line, "GROUPNAME"));
        this.setTitle(paragraph.getLanguage(line, "TITLEADDPERMISSION"));
    }
    public void init() {
        Object[] str_Column = {paragraph.getLanguage(line, "SYSTEM"),
                               paragraph.getLanguage(line, "LNQUIRY"),
                               paragraph.getLanguage(line, "ADD"),
                               paragraph.getLanguage(line, "UPDATE"),
                               paragraph.getLanguage(line, "DELETE"),
                               paragraph.getLanguage(line, "ALL")} ;
        try{
            sql = "SELECT * FROM permission_info WHERE grp_name = 'Adminstration'";
            rs = DBC.executeQuery(sql);
            int row = 0 ;
            while(rs.next()){
                row++ ;
            }
            Object[][] str_Row = new Object[row][6] ;
            row = 0 ;
            for(int i = 0 ; i < str_Row.length ; i++){
                for(int j = 1 ; j < str_Row[i].length ; j++){
                    str_Row[i][j] = false ;
                }
            }
            rs = DBC.executeQuery(sql);
            while(rs.next()) {
                str_Row[row][0] = rs.getString("sys_name");
                row++;
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
            this.tab_Show.setModel(tableData);
            this.tab_Show.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }catch(SQLException e){
                ErrorMessage.setData("Permission", "Frm_Permission_Info" ,"initTable()",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        }finally{
            sql = "" ;
            txt_Name.setText("");
            /** 畫面關閉原視窗enable */
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent windowevent) {
                    btn_CloseActionPerformed(null);
                }
            });
            try{
                DBC.closeConnection(rs);
            }catch(Exception e){
                ErrorMessage.setData("Permission", "Frm_Permission_Info" ,"initTable()",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            }
        }
    }

    public void setCheckBox(boolean isCheck, int selectRow) {
        for(int i = 1 ; i < 5 ; i++)
            this.tab_Show.setValueAt(isCheck, selectRow, i);
    }

    public void setTable_Check (int row , int Column , boolean isCheck) {
        for(int i = 1 ; i < Column ; i++){
            this.tab_Show.setValueAt(isCheck, row, i);
        }
    }

    public boolean isChick (int row) {
        for(int i = 1 ; i < 5 ; i++)
            if(this.tab_Show.getValueAt(row, i).equals(false)) return false;
        return true ;
    }

    public boolean isDuplicateName() {
        try{
            sql = "SELECT * FROM permission_info WHERE grp_name = 'ADMIN'";
            rs = DBC.executeQuery(sql);
            while(rs.next()){
                if(txt_Name.getText().trim().equals(rs.getString("sys_name"))){
                    return false;
                }
            }
        }catch(SQLException e){
                ErrorMessage.setData("Permission", "Frm_Permission_Info" ,"isDuplicateName()",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        }finally{
            sql = "" ;
            try{
                DBC.closeConnection(rs);
            }catch(Exception e){
                ErrorMessage.setData("Permission", "Frm_Permission_Info" ,"isDuplicateName()",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            }
        }
        return true ;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tab_Show = new javax.swing.JTable();
        pan_Button = new javax.swing.JPanel();
        btn_Save = new javax.swing.JButton();
        btn_Close = new javax.swing.JButton();
        pan_Title = new javax.swing.JPanel();
        txt_Name = new javax.swing.JTextField();
        lab_Name = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Add Permission");

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
        jScrollPane1.setViewportView(tab_Show);

        btn_Save.setText("Save");
        btn_Save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_SaveActionPerformed(evt);
            }
        });

        btn_Close.setText("Close");
        btn_Close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CloseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pan_ButtonLayout = new javax.swing.GroupLayout(pan_Button);
        pan_Button.setLayout(pan_ButtonLayout);
        pan_ButtonLayout.setHorizontalGroup(
            pan_ButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_ButtonLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_Save, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Close, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
                .addContainerGap())
        );
        pan_ButtonLayout.setVerticalGroup(
            pan_ButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_ButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btn_Save)
                .addComponent(btn_Close))
        );

        lab_Name.setText("Group Name :");

        javax.swing.GroupLayout pan_TitleLayout = new javax.swing.GroupLayout(pan_Title);
        pan_Title.setLayout(pan_TitleLayout);
        pan_TitleLayout.setHorizontalGroup(
            pan_TitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_TitleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lab_Name, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_Name, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pan_TitleLayout.setVerticalGroup(
            pan_TitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_TitleLayout.createSequentialGroup()
                .addGroup(pan_TitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab_Name, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_Name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 645, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap(437, Short.MAX_VALUE)
                        .addComponent(pan_Button, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pan_Title, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pan_Title, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 449, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pan_Button, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_CloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CloseActionPerformed
        this.dispose();
    }//GEN-LAST:event_btn_CloseActionPerformed

    private void btn_SaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SaveActionPerformed
        if(!this.txt_Name.getText().trim().equals("")){
            for(int i = 0 ; i < this.tab_Show.getRowCount() ; i++){
                int Calculations = 0 ;
                if(this.tab_Show.getValueAt(i, 1).equals(true)) Calculations+=1 ;
                if(this.tab_Show.getValueAt(i, 2).equals(true)) Calculations+=2 ;
                if(this.tab_Show.getValueAt(i, 3).equals(true)) Calculations+=4 ;
                if(this.tab_Show.getValueAt(i, 4).equals(true)) Calculations+=8 ;
                sql = "INSERT INTO permission_info (guid, grp_name, lvl, sys_name)" +
                        " VALUES (uuid(), " +
                        "'" + this.txt_Name.getText() + "', "+
                        "'" + Calculations + "', " +
                        "'" + this.tab_Show.getValueAt(i, 0) + "')";
                try{
                    DBC.executeUpdate(sql);
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
            fpi.initTableName();
            fpi.setTableModel(this.txt_Name.getText());
            this.dispose();
            JOptionPane.showConfirmDialog(null, paragraph.getLanguage(message, "SAVEACCESS"),
                                                     paragraph.getLanguage(message, "MESSAGE") ,
                                                     JOptionPane.CLOSED_OPTION);
        }else{
            JOptionPane.showConfirmDialog(null, paragraph.getLanguage(message, "PLEASECHECKPERMISIONNAME"),
                                                     paragraph.getLanguage(message, "MESSAGE") ,
                                                     JOptionPane.CLOSED_OPTION);
            init();
        }
    }//GEN-LAST:event_btn_SaveActionPerformed

    private void tab_ShowMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab_ShowMouseReleased
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
    }//GEN-LAST:event_tab_ShowMouseReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Close;
    private javax.swing.JButton btn_Save;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lab_Name;
    private javax.swing.JPanel pan_Button;
    private javax.swing.JPanel pan_Title;
    private javax.swing.JTable tab_Show;
    private javax.swing.JTextField txt_Name;
    // End of variables declaration//GEN-END:variables

}
