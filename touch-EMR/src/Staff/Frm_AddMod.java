package Staff;

import ErrorMessage.StoredErrorMessage;
import Multilingual.language;
import java.sql.*;
import java.awt.Frame;
import javax.swing.JOptionPane;
import cc.johnwu.sql.DBC;

public class Frm_AddMod extends javax.swing.JFrame {
    Frm_Department frm_dep;
    Frm_Position frm_pos;
    /*輸出錯誤資訊變數*/
    StoredErrorMessage ErrorMessage = new StoredErrorMessage() ;
    private language paragraph = new language();
    private String[] line = new String(paragraph.setlanguage("ADDMOD")).split("\n") ;
    private String[] message = new String(paragraph.setlanguage("MESSAGE")).split("\n") ;
    public Frm_AddMod(Frm_Department frm_dep) {
        this.frm_dep = frm_dep;
        initComponents();
        init();
        initLanguage();
        this.btn_PosSave.setVisible(false);
        this.lab_Name.setText(paragraph.getLanguage(line, "NEWDEPARTMENTNAME"));

    }
    public Frm_AddMod(Frm_Position frm_pos) {
        this.frm_pos = frm_pos;
        initComponents();
        init();
        initLanguage();
        this.btn_DepSave.setVisible(false);
        this.lab_Name.setText(paragraph.getLanguage(line, "NEWPOSITIONNAME"));

    }

    public void init(){
        this.setLocationRelativeTo(this);
    }
       private void initLanguage() {
        this.btn_PosSave.setText(paragraph.getLanguage(line, "POSSAVE"));
        this.btn_DepSave.setText(paragraph.getLanguage(line, "DEPSAVE"));
        this.btn_Leave.setText(paragraph.getLanguage(line, "LEAVE"));
        this.setTitle(paragraph.getLanguage(line, "NEWSTAFF"));

    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        btn_DepSave = new javax.swing.JButton();
        btn_PosSave = new javax.swing.JButton();
        btn_Leave = new javax.swing.JButton();
        txt_Name = new javax.swing.JTextField();
        lab_Name = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("New Staff");
        setResizable(false);

        btn_DepSave.setText("Add");
        btn_DepSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_DepSaveActionPerformed(evt);
            }
        });

        btn_PosSave.setText("Add");
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
                .addComponent(btn_PosSave)
                .addComponent(btn_DepSave))
        );

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
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Leave, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_Leave)
                    .addComponent(jPanel2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        txt_Name.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_NameKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lab_Name, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_Name, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)))
                .addGap(12, 12, 12))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lab_Name, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_Name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_DepSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_DepSaveActionPerformed
        ResultSet rs = null;
        String sql = null;
        try {
            sql = "SELECT * FROM department WHERE name = '" + txt_Name.getText().trim() + "'";
            rs = DBC.executeQuery(sql);
            if(rs.next() || txt_Name.getText().trim().equals("None")){
                JOptionPane.showMessageDialog(new Frame(),"The Name \"" + txt_Name.getText() + "\" Is Already Repetition!!");
                txt_Name.setText("");
                DBC.closeConnection(rs);
            }else{
                sql = "INSERT INTO department (guid, name) VALUES (uuid(), '" + txt_Name.getText().trim() + "')";
                DBC.executeUpdate(sql);
                frm_dep.initTables();
                dispose();
            }
            
        } catch (SQLException e) {
            ErrorMessage.setData("Staff", "Frm_AddMod" ,"btn_DepSaveActionPerformed()",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        }

}//GEN-LAST:event_btn_DepSaveActionPerformed

    private void btn_LeaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_LeaveActionPerformed
        this.dispose();
}//GEN-LAST:event_btn_LeaveActionPerformed

    private void btn_PosSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_PosSaveActionPerformed
        ResultSet rs = null;
        String sql = null;
        try {
            sql = "SELECT * FROM position WHERE name = '" + txt_Name.getText().trim() + "'";
            rs = DBC.executeQuery(sql);
            if(rs.next() && txt_Name.getText().trim()=="None"){
                JOptionPane.showMessageDialog(new Frame(),"The Name \"" + txt_Name.getText() + "\" Is Already Repetition!!");
                txt_Name.setText("");
                DBC.closeConnection(rs);
            }else{
                sql = "INSERT INTO position (guid, name) VALUES (uuid(), '" + txt_Name.getText().trim() + "')";
                DBC.executeUpdate(sql);
                frm_pos.initTables();
                dispose();
            }

        } catch (SQLException e) {
            ErrorMessage.setData("Staff", "Frm_AddMod" ,"btn_PosSaveActionPerformed()",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        }
    }//GEN-LAST:event_btn_PosSaveActionPerformed

    private void txt_NameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_NameKeyReleased
        if(!txt_Name.getText().trim().equals("")){
            this.btn_DepSave.setEnabled(true);
        }else if(txt_Name.getText().trim().equals("")){
            this.btn_DepSave.setEnabled(false);
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