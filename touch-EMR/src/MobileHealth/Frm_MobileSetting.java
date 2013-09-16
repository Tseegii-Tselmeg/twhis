package MobileHealth;

import AutoComplete.CompleterComboBox;
import cc.johnwu.sql.DBC;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
/**
 *
 * @author Steven
 */
public class Frm_MobileSetting extends javax.swing.JFrame {
    private Frm_MobileHealth m_Frm;
    private CompleterComboBox m_Cobww;
    /** Creates new form Frm_MobileSetting */
    public Frm_MobileSetting(Frm_MobileHealth frm) {
            m_Frm = frm;
            initComponents();
            this.setLocationRelativeTo(this);
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent windowevent) {
                    btn_CloseActionPerformed(null);
                }
            });

       try {
            ResultSet rs = DBC.executeQuery("SELECT id FROM package_item WHERE typ = 'P'");
            while (rs.next()) {
                cbox_DmSet.addItem(rs.getString("id"));
            }
            rs = DBC.executeQuery("SELECT remind_days FROM sys_info");
            if (rs.next()) {
                txt_AdvDays.setText(rs.getString("remind_days"));
            }
            setLetterList();
            
           
        } catch (SQLException ex) {
            Logger.getLogger(Frm_MobileSetting.class.getName()).log(Level.SEVERE, null, ex);
        }


        String[] medicineCob = null ;
        ResultSet rs = null;
        try {
            String sql = "SELECT medicines.code, medicines.item,  medicines.injection " +
                         "FROM medicines " +
                         "WHERE effective = 1";

            rs = DBC.localExecuteQuery(sql);
            rs.last();
            medicineCob = new String[rs.getRow()+1];
            rs.beforeFirst();
            int i = 0;
            medicineCob[i++] = "";
            while (rs.next()) {
                  medicineCob[i++] = rs.getString("code").trim() + "    "+ rs.getString("item").trim() +" "+rs.getString("injection").trim();
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        m_Cobww = new CompleterComboBox(medicineCob);
        m_Cobww.setBounds(0, 0, jPanel2.getWidth(), jPanel2.getHeight());
        jPanel2.add(m_Cobww);
        m_Cobww.setSelectedIndex(0);
        seMedicineList();

    }
    
    private void setLetterList() {
        try {
            if (list_Letter.getModel().getSize() > 0) {
                ((DefaultListModel) list_Letter.getModel()).removeAllElements();
            }

            ResultSet rs = DBC.executeQuery("SELECT name FROM newsletter");
            DefaultListModel model = new DefaultListModel();
            while (rs.next()) {
                model.add(rs.getRow()-1, rs.getString("name"));
            }
            list_Letter.setModel(model);
        } catch (SQLException ex) {
            Logger.getLogger(Frm_MobileSetting.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // 取出需要回診的藥品列表
        private void seMedicineList() {
        try {
            if (list_Medicine.getModel().getSize() > 0) {
                ((DefaultListModel) list_Medicine.getModel()).removeAllElements();
            }

            ResultSet rs = DBC.executeQuery("SELECT concat(medicines.code,'    ',medicines.item,' ',  medicines.injection) AS 'medicine' " +
                    "FROM package_item, medicines " +
                    "WHERE package_item.typ = 'M' AND package_item.id = medicines.code");
            DefaultListModel model = new DefaultListModel();
            while (rs.next()) {
                model.add(rs.getRow()-1, rs.getString("medicine"));
            }
            list_Medicine.setModel(model);
        } catch (SQLException ex) {
            Logger.getLogger(Frm_MobileSetting.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // 取出簡訊內容   name:簡訊標題
    private void setLetterDetail(String name) {

        txt_LetterName.setEditable(false);
        btn_LetterDelete.setEnabled(true);
        try {
            txt_LetterName.setText(name);
      
            ResultSet rs = DBC.executeQuery("SELECT detail FROM newsletter WHERE name = '"+name+"'");
            if (rs.next()) {
                txt_LetterText.setText(rs.getString("detail")) ;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Frm_MobileSetting.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        txt_AdvDays = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        cbox_DmSet = new javax.swing.JComboBox();
        txt_VDays = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        list_Letter = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        txt_LetterText = new javax.swing.JTextArea();
        btn_LetterSave = new javax.swing.JButton();
        btn_LetterNew = new javax.swing.JButton();
        txt_LetterName = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        btn_LetterDelete = new javax.swing.JButton();
        btn_Close = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        list_Medicine = new javax.swing.JList();
        btn_DeleteMedicine = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Mobile Health Setting");
        setResizable(false);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Mobile Health notified well in advance the number of days"));

        jLabel11.setText("Days");

        jButton2.setText("Save");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txt_AdvDays, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(355, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_AdvDays, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2)
                    .addComponent(jLabel11))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("DM Set the date back to clinic settings"));

        cbox_DmSet.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " " }));
        cbox_DmSet.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbox_DmSetItemStateChanged(evt);
            }
        });

        jLabel7.setText("Days");

        jButton1.setText("Save");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cbox_DmSet, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txt_VDays, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(338, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbox_DmSet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(txt_VDays, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Common Newsletter"));

        list_Letter.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                list_LetterMouseClicked(evt);
            }
        });
        list_Letter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                list_LetterKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(list_Letter);

        txt_LetterText.setColumns(20);
        txt_LetterText.setRows(5);
        jScrollPane2.setViewportView(txt_LetterText);

        btn_LetterSave.setText("Save");
        btn_LetterSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_LetterSaveActionPerformed(evt);
            }
        });

        btn_LetterNew.setText("New");
        btn_LetterNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_LetterNewActionPerformed(evt);
            }
        });

        jLabel1.setText("Title:");

        btn_LetterDelete.setText("Delete");
        btn_LetterDelete.setEnabled(false);
        btn_LetterDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_LetterDeleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 624, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btn_LetterNew, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_LetterSave, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_LetterDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_LetterName, javax.swing.GroupLayout.DEFAULT_SIZE, 596, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_LetterName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_LetterNew)
                            .addComponent(btn_LetterSave)
                            .addComponent(btn_LetterDelete)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE))
                .addContainerGap())
        );

        btn_Close.setText("Close");
        btn_Close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CloseActionPerformed(evt);
            }
        });

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Medicine Set the date back to clinic settings"));

        jButton3.setText("Save");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(204, 255, 204));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 553, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 23, Short.MAX_VALUE)
        );

        list_Medicine.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                list_MedicineMouseClicked(evt);
            }
        });
        list_Medicine.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                list_MedicineKeyReleased(evt);
            }
        });
        jScrollPane3.setViewportView(list_Medicine);

        btn_DeleteMedicine.setText("Delete");
        btn_DeleteMedicine.setEnabled(false);
        btn_DeleteMedicine.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_DeleteMedicineActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 748, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_DeleteMedicine, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btn_DeleteMedicine)
                        .addComponent(jButton3))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_Close, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Close)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            if (Common.Tools.isNumber(txt_AdvDays.getText().trim())) {
                DBC.executeUpdate("UPDATE sys_info SET remind_days = "+txt_AdvDays.getText().trim()+" ");
                JOptionPane.showMessageDialog(null,"Saved successfully.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Frm_MobileSetting.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void cbox_DmSetItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbox_DmSetItemStateChanged
        if(evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            try {
                ResultSet rs = DBC.executeQuery("SELECT days FROM package_item WHERE id = '"+cbox_DmSet.getSelectedItem().toString().trim()+"'");
                while (rs.next()) {
                    txt_VDays.setText(rs.getString("days"));
                }
            } catch (SQLException ex) {
                Logger.getLogger(Frm_MobileSetting.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
}//GEN-LAST:event_cbox_DmSetItemStateChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            if (Common.Tools.isNumber(txt_VDays.getText().trim())) {
                DBC.executeUpdate("UPDATE package_item SET days = "+txt_VDays.getText().trim()+" " +
                        "WHERE id = '"+cbox_DmSet.getSelectedItem().toString().trim()+"'");
                JOptionPane.showMessageDialog(null,"Saved successfully.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Frm_MobileSetting.class.getName()).log(Level.SEVERE, null, ex);
        }
}//GEN-LAST:event_jButton1ActionPerformed

    private void list_LetterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_list_LetterMouseClicked
        if (list_Letter.getSelectedValue() != null) setLetterDetail(list_Letter.getSelectedValue().toString());
    }//GEN-LAST:event_list_LetterMouseClicked

    private void list_LetterKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_list_LetterKeyReleased
       if (list_Letter.getSelectedValue() != null) setLetterDetail(list_Letter.getSelectedValue().toString());
    }//GEN-LAST:event_list_LetterKeyReleased

    private void btn_LetterNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_LetterNewActionPerformed
        txt_LetterName.setEditable(true);
        btn_LetterDelete.setEnabled(false);
        txt_LetterName.setText("");
        txt_LetterText.setText("");
        list_Letter.removeSelectionInterval(list_Letter.getSelectedIndex(), list_Letter.getSelectedIndex());
    }//GEN-LAST:event_btn_LetterNewActionPerformed

    private void btn_LetterDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_LetterDeleteActionPerformed
        try {
            String sql = "DELETE FROM newsletter WHERE name = '"+txt_LetterName.getText()+"'";
            DBC.executeUpdate(sql);

            JOptionPane.showMessageDialog(null, "Deleted successfully.");
            setLetterList();
            btn_LetterNewActionPerformed(null);
             m_Frm.setLetterList();
        } catch (SQLException ex) {

            Logger.getLogger(Frm_MobileSetting.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_LetterDeleteActionPerformed

    private void btn_LetterSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_LetterSaveActionPerformed
        String sql = null;
        if (!btn_LetterDelete.isEnabled()) { // 新增
            sql = "INSERT INTO newsletter(name, detail) " +
                            "VALUES('"+txt_LetterName.getText().trim()+"', '"+txt_LetterText.getText().trim()+"') ";
        } else { // 修改
            sql = "UPDATE newsletter SET detail = '"+txt_LetterText.getText().trim()+"' WHERE name = '"+txt_LetterName.getText()+"'";
        }
        
        if (txt_LetterName.getText() == null && txt_LetterText.getText() ==null 
                && txt_LetterName.getText().trim().equals("") || txt_LetterText.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null, "Data not entered."); 
        } else {
            try {
                DBC.executeUpdate(sql);
                setLetterList();
                btn_LetterNewActionPerformed(null);
                 m_Frm.setLetterList();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Title Repeat.");
                Logger.getLogger(Frm_MobileSetting.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btn_LetterSaveActionPerformed

    private void btn_CloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CloseActionPerformed
        m_Frm.setEnabled(true);
        this.dispose();
    }//GEN-LAST:event_btn_CloseActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
            String a[] = m_Cobww.getSelectedItem().toString().split("    ");
            String sql = "INSERT INTO package_item(id, typ) " +
                            "VALUES('"+a[0]+"', 'M') ";
            DBC.executeUpdate(sql);
            JOptionPane.showMessageDialog(null,"Saved successfully.");
            seMedicineList();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Repeat.");
            Logger.getLogger(Frm_MobileSetting.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void list_MedicineMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_list_MedicineMouseClicked
        if (list_Medicine.getSelectedValue() != null) btn_DeleteMedicine.setEnabled(true);
    }//GEN-LAST:event_list_MedicineMouseClicked

    private void list_MedicineKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_list_MedicineKeyReleased
        if (list_Medicine.getSelectedValue() != null) btn_DeleteMedicine.setEnabled(true);
    }//GEN-LAST:event_list_MedicineKeyReleased

    private void btn_DeleteMedicineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_DeleteMedicineActionPerformed
        try {
            String[] name = list_Medicine.getSelectedValue().toString().split("    ");
            String sql = "DELETE FROM package_item WHERE id = '"+name[0]+"'";
            DBC.executeUpdate(sql);

            JOptionPane.showMessageDialog(null, "Deleted successfully.");
            btn_DeleteMedicine.setEnabled(false);
            seMedicineList();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "The item in use.");
            Logger.getLogger(Frm_MobileSetting.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_DeleteMedicineActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Close;
    private javax.swing.JButton btn_DeleteMedicine;
    private javax.swing.JButton btn_LetterDelete;
    private javax.swing.JButton btn_LetterNew;
    private javax.swing.JButton btn_LetterSave;
    private javax.swing.JComboBox cbox_DmSet;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JList list_Letter;
    private javax.swing.JList list_Medicine;
    private javax.swing.JTextField txt_AdvDays;
    private javax.swing.JTextField txt_LetterName;
    private javax.swing.JTextArea txt_LetterText;
    private javax.swing.JTextField txt_VDays;
    // End of variables declaration//GEN-END:variables

}
