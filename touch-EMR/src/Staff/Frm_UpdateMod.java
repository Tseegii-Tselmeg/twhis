package Staff;

import ErrorMessage.StoredErrorMessage;
import Multilingual.language;
import cc.johnwu.sql.DBC;
import java.sql.*;

public class Frm_UpdateMod extends javax.swing.JFrame {
    Object getValue;
    private Frm_Position frm_Pos;
    private Frm_Department frm_Dep;
    private language paragraph = new language();
    private String[] line = new String(paragraph.setlanguage("EMPLOYEE")).split("\n") ;
    private String[] message = new String(paragraph.setlanguage("MESSAGE")).split("\n") ;
    /*輸出錯誤資訊變數*/
    StoredErrorMessage ErrorMessage = new StoredErrorMessage() ;

    public Frm_UpdateMod(Frm_Position frm_Posi,Object getValue) {
        this.frm_Pos = frm_Posi;
        this.getValue = getValue;
        initComponents();
        init();
        initLanguage();
        this.btn_DepSave.setVisible(false);
        
        this.lab_Name.setText(paragraph.getLanguage(line, "RENAME"));
    }
    public Frm_UpdateMod(Frm_Department frm_Dep, Object getValue){
        this.frm_Dep = frm_Dep;
        this.getValue = getValue;
        initComponents();
        init();
        initLanguage();
        this.btn_PosSave.setVisible(false);
        this.lab_Name.setText(paragraph.getLanguage(line, "RENAME"));
    }

    public void init(){
        this.setLocationRelativeTo(this);
        this.txt_Name.setText(getValue.toString());
    }

    private void initLanguage() {
         this.btn_PosSave.setText(paragraph.getLanguage(message,"SAVE"));
         this.btn_DepSave.setText(paragraph.getLanguage(message,"SAVE"));
         this.btn_Leave.setText(paragraph.getLanguage(message,"CANCEL"));
         this.setTitle(paragraph.getLanguage(line, "UPDATASTAFFPOSITION"));
     }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        btn_DepSave = new javax.swing.JButton();
        btn_PosSave = new javax.swing.JButton();
        txt_Name = new javax.swing.JTextField();
        btn_Leave = new javax.swing.JButton();
        lab_Name = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Updata Staff Position");
        setAlwaysOnTop(true);
        setResizable(false);

        btn_DepSave.setText("Save");
        btn_DepSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_DepSaveActionPerformed(evt);
            }
        });

        btn_PosSave.setText("Save");
        btn_PosSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_PosSaveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(btn_PosSave, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_DepSave, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btn_DepSave)
                .addComponent(btn_PosSave))
        );

        txt_Name.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_NameKeyReleased(evt);
            }
        });

        btn_Leave.setText("Cancel");
        btn_Leave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_LeaveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_Leave, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lab_Name, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_Name, javax.swing.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_Name, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                    .addComponent(lab_Name, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btn_Leave)
                    .addComponent(jPanel2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
     
    private void btn_DepSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_DepSaveActionPerformed
        try {
            String sql = "UPDATE department SET name = '" + txt_Name.getText().trim() + "' WHERE name = '" + (String) getValue + "'";
            DBC.executeUpdate(sql);
        } catch (SQLException e) {
            ErrorMessage.setData("Staff", "Frm_UpdateMod" ,"btn_DepSaveActionPerformed()",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        } 
        frm_Dep.initTables();
        this.dispose();
}//GEN-LAST:event_btn_DepSaveActionPerformed

    private void btn_LeaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_LeaveActionPerformed
        this.dispose();
}//GEN-LAST:event_btn_LeaveActionPerformed

    private void btn_PosSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_PosSaveActionPerformed
        try {
            String sql = "UPDATE position SET name = '" + txt_Name.getText().trim() + "' WHERE name = '" + getValue.toString() + "'";
            DBC.executeUpdate(sql);
        } catch (SQLException e) {
            ErrorMessage.setData("Staff", "Frm_UpdateMod" ,"btn_PosSaveActionPerformed()",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        }
        frm_Pos.initTables();
        this.dispose();
    }//GEN-LAST:event_btn_PosSaveActionPerformed

    private void txt_NameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_NameKeyReleased
        if(!txt_Name.getText().trim().equals("")){
            btn_DepSave.setEnabled(true);
            btn_PosSave.setEnabled(true);
        }else if(txt_Name.getText().trim().equals("")){
            btn_DepSave.setEnabled(false);
            btn_PosSave.setEnabled(false);
        }
    }//GEN-LAST:event_txt_NameKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_DepSave;
    private javax.swing.JButton btn_Leave;
    private javax.swing.JButton btn_PosSave;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lab_Name;
    private javax.swing.JTextField txt_Name;
    // End of variables declaration//GEN-END:variables

}