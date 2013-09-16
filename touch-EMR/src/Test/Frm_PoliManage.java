package Test;


import cc.johnwu.sql.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author Steven
 */
public class Frm_PoliManage extends javax.swing.JFrame {

    private String m_PoliGuid;  // 科別GUID
    /** Creates new form Frm_PoliManage */
    public Frm_PoliManage() {
        initComponents();
        initTab();
    }

    private void initTab() {
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM policlinic ";
            rs= DBC.executeQuery(sql);
            if (rs.next()) {
                this.tab_Poli.setModel(HISModel.getModel(rs, true));
            } else {
                tab_Poli.setModel(getModle(new String[]{"Message"},new String[][]{{"No Information."}}));
            }
            tab_Poli.setRowHeight(30);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    // 取出選定診別診間
    private void SetClinic(String poliGuid) {
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM poli_room WHERE poli_guid = '"+poliGuid+"' ";
            rs= DBC.executeQuery(sql);
            cbox_Clinic.removeAllItems();
            while (rs.next()) {
                cbox_Clinic.addItem(rs.getString("name"));
            }
            tab_Poli.setRowHeight(30);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    /** 設定表單預設模型。*/
    private DefaultTableModel getModle(String[] title,String[][] data){
        return new DefaultTableModel(data,title){
                @Override
                public boolean isCellEditable(int r, int c){
                return false;}};
    }

     public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Frm_PoliManage().setVisible(true);
            }
        });
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tab_Poli = new javax.swing.JTable();
        cbox_Clinic = new javax.swing.JComboBox();
        btn_NewClinic = new javax.swing.JButton();
        btn_DeleteClinic = new javax.swing.JButton();
        btn_EditClinic = new javax.swing.JButton();
        txt_PoliName = new javax.swing.JTextField();
        btn_EditPoli = new javax.swing.JButton();
        btn_NewPoli = new javax.swing.JButton();
        txt_Clinic = new javax.swing.JTextField();
        btn_DelPoli = new javax.swing.JButton();
        txt_PoliLinit = new javax.swing.JTextField();
        txt_PoliType = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tab_Poli.setModel(new javax.swing.table.DefaultTableModel(
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
        tab_Poli.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tab_PoliMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tab_Poli);

        btn_NewClinic.setText("增加診間");
        btn_NewClinic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_NewClinicActionPerformed(evt);
            }
        });

        btn_DeleteClinic.setText("刪除診間");
        btn_DeleteClinic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_DeleteClinicActionPerformed(evt);
            }
        });

        btn_EditClinic.setText("修改診間");
        btn_EditClinic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_EditClinicActionPerformed(evt);
            }
        });

        btn_EditPoli.setText("修改科名");
        btn_EditPoli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_EditPoliActionPerformed(evt);
            }
        });

        btn_NewPoli.setText("新增科別");
        btn_NewPoli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_NewPoliActionPerformed(evt);
            }
        });

        btn_DelPoli.setText("刪除科別");
        btn_DelPoli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_DelPoliActionPerformed(evt);
            }
        });

        jLabel1.setText("診間名稱");

        jLabel2.setText("看診人數上限");

        jLabel3.setText("診間類型");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_Clinic, javax.swing.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE)
                            .addComponent(cbox_Clinic, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btn_EditClinic, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btn_DeleteClinic, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btn_NewClinic, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel1)
                        .addComponent(jLabel3))
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txt_PoliLinit)
                    .addComponent(txt_PoliType)
                    .addComponent(txt_PoliName, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_EditPoli)
                    .addComponent(btn_NewPoli)
                    .addComponent(btn_DelPoli))
                .addContainerGap(100, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_PoliName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_EditPoli)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_NewPoli)
                            .addComponent(txt_PoliLinit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_DelPoli)
                            .addComponent(txt_PoliType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbox_Clinic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_DeleteClinic))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_EditClinic)
                            .addComponent(txt_Clinic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_NewClinic)
                .addContainerGap(228, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tab_PoliMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab_PoliMouseClicked
       if (tab_Poli.getSelectedRow() > -1) {
           m_PoliGuid = (String) tab_Poli.getValueAt(tab_Poli.getSelectedRow(), 1);
            SetClinic(m_PoliGuid);
            txt_PoliName.setText((String) tab_Poli.getValueAt(tab_Poli.getSelectedRow(), 2));
       }
    }//GEN-LAST:event_tab_PoliMouseClicked

    private void btn_DeleteClinicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_DeleteClinicActionPerformed
        try {
            String sql = "DELETE FROM poli_room WHERE poli_guid = '" + m_PoliGuid + "' AND name = '"+cbox_Clinic.getSelectedItem()+"'";
            DBC.executeUpdate(sql);
            SetClinic(m_PoliGuid);
        } catch (SQLException ex) {
            Logger.getLogger(Frm_PoliManage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_DeleteClinicActionPerformed

    private void btn_EditPoliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_EditPoliActionPerformed
        try {
            String sql = "UPDATE policlinic SET name = '" + txt_PoliName.getText() + "' WHERE guid = '" + m_PoliGuid + "' ";
            DBC.executeUpdate(sql);
            initTab();
        } catch (SQLException ex) {
            Logger.getLogger(Frm_PoliManage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_EditPoliActionPerformed

    private void btn_NewPoliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_NewPoliActionPerformed
        try {
            String limit = null;
            
            if (txt_PoliLinit.getText() != null && !txt_PoliLinit.getText().trim().equals("")) limit = txt_PoliLinit.getText();
            String sql = "INSERT INTO policlinic(guid, name, lim, typ ) VALUES(UUID(), '" + txt_PoliName.getText() + "'," + limit + ", ";
            if (txt_PoliType.getText() != null && !txt_PoliType.getText().trim().equals(""))   sql += "'" + txt_PoliType.getText() + "'"  ;
            else sql += " NULL " ;
                
             sql +=" ) ";
            
            System.out.println(sql);
            DBC.executeUpdate(sql);
            initTab();
        } catch (SQLException ex) {
            Logger.getLogger(Frm_PoliManage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_NewPoliActionPerformed

    private void btn_DelPoliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_DelPoliActionPerformed
        try {
            String sql = "DELETE FROM policlinic WHERE guid = '" + m_PoliGuid + "'";
            DBC.executeUpdate(sql);
            initTab();
        } catch (SQLException ex) {
            Logger.getLogger(Frm_PoliManage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_DelPoliActionPerformed

    private void btn_EditClinicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_EditClinicActionPerformed
        try {
            String sql = "UPDATE poli_room SET name = '"+txt_Clinic.getText()+"' " +
                         "WHERE poli_guid = '" + m_PoliGuid + "' " +
                         "AND name = '"+cbox_Clinic.getSelectedItem()+"'";
            DBC.executeUpdate(sql);
            SetClinic(m_PoliGuid);
        } catch (SQLException ex) {
            Logger.getLogger(Frm_PoliManage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_EditClinicActionPerformed

    private void btn_NewClinicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_NewClinicActionPerformed
        try {
            if (txt_Clinic.getText() != null && !txt_Clinic.getText().trim().equals("")) {
                 String sql = "INSERT INTO poli_room(guid , poli_guid, name) VALUES (UUID(), '"+m_PoliGuid+"', '"+txt_Clinic.getText()+"') ";
                System.out.println(sql);
                DBC.executeUpdate(sql);
                SetClinic(m_PoliGuid);
            }
           
        } catch (SQLException ex) {
            Logger.getLogger(Frm_PoliManage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_NewClinicActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_DelPoli;
    private javax.swing.JButton btn_DeleteClinic;
    private javax.swing.JButton btn_EditClinic;
    private javax.swing.JButton btn_EditPoli;
    private javax.swing.JButton btn_NewClinic;
    private javax.swing.JButton btn_NewPoli;
    private javax.swing.JComboBox cbox_Clinic;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tab_Poli;
    private javax.swing.JTextField txt_Clinic;
    private javax.swing.JTextField txt_PoliLinit;
    private javax.swing.JTextField txt_PoliName;
    private javax.swing.JTextField txt_PoliType;
    // End of variables declaration//GEN-END:variables

}
