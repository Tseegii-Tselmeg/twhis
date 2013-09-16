package MobileHealth;

import Main.Frm_Main;
import MobileHealth.TableTriStateCell.TriStateCellEditor;
import MobileHealth.TableTriStateCell.TriStateCellRenderer;
import Common.TabTools;
import cc.johnwu.login.UserInfo;
import cc.johnwu.sql.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import cc.johnwu.date.*;
/**
 *
 * @author Steven
 */

public class Frm_MobileHealth extends javax.swing.JFrame implements DateInterface {

    /** Creates new form Frm_MobileHealth */
    public Frm_MobileHealth() {
        initComponents();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowevent) {
                jMenuItem1ActionPerformed(null);
            }
        });
        this.setExtendedState(Frm_MobileHealth.MAXIMIZED_BOTH);  // 最大化
        this.setLocationRelativeTo(this);
        setSendList("");
        setLetterList();
    }


    private void setSendList(String pno) {
        try {
         Object[] title = {"","Patient No.","Name","Date", "Set Type", "Back Date" ,"Advance Notice","Cell Phone","Contactperson", "Check","guid"};
            String sql = "SELECT package_set.guid, patients_info.p_no AS 'Patient No.', patients_info.cell_phone AS 'Cell Phone', " +
                    "concat(patients_info.firstname,'  ',patients_info.lastname) AS 'Name', " +
                    "package_set.use_date AS 'Date' ,package_set.id AS 'Set Type', " +
                    "(DATE_ADD(package_set.use_date, INTERVAL package_set.days DAY)) AS 'Back Days', "+
                    "(DATE_SUB((DATE_ADD(package_set.use_date, INTERVAL package_set.days DAY)), INTERVAL sys_info.remind_days DAY)) AS 'Advance Notice', " +
                    "contactperson_info.cell_phone AS 'Contactperson' " +
                    "FROM package_set, patients_info LEFT JOIN contactperson_info ON  contactperson_info.guid = patients_info.cp_guid, registration_info, sys_info " +
                    "WHERE package_set.reg_guid = registration_info.guid  " +
                    "AND registration_info.p_no = patients_info.p_no  " +
            
                    "AND sms_state = '0'  " +
                    "AND '"+date_Com.getValue()+"' >=(DATE_SUB((DATE_ADD(package_set.use_date, INTERVAL package_set.days DAY)), INTERVAL sys_info.remind_days DAY)) AND patients_info.p_no LIKE '%"+pno+"%'  ";
            System.out.println(sql);
            ResultSet rs = DBC.executeQuery(sql);

            rs.last();
            Object[][] dataArray = new Object[rs.getRow()][title.length];
            rs.beforeFirst();
            int i = 0;

            while (rs.next()) {

                 dataArray[i][0] = i+1;
                 dataArray[i][1] = rs.getString("Patient No.");
                 dataArray[i][2] = rs.getString("Name");
                 dataArray[i][3] = rs.getString("Date");
                 dataArray[i][4] = rs.getString("Set Type");
                 dataArray[i][5] = rs.getString("Back Days");
                 dataArray[i][6] = rs.getString("Advance Notice");
                 dataArray[i][7] = rs.getString("Cell Phone");
                 dataArray[i][8] = rs.getString("Contactperson");
                 dataArray[i][9] = true;
                 dataArray[i][10] = rs.getString("guid");

                 i++;
             }
            DefaultTableModel tabModel = new DefaultTableModel(dataArray,title) {
                @Override
                 public boolean isCellEditable(int rowIndex,int columnIndex) {
                     if (columnIndex == 9) {
                        return true;
                     } else {
                        return false;
                     }
                 }
             };
              tab_Send.setModel(tabModel);
              this.tab_Send.getColumnModel().getColumn(0).setMaxWidth(35);
              this.tab_Send.getColumnModel().getColumn(1).setPreferredWidth(70);
              //this.tab_Send.getColumnModel().getColumn(2).setMaxWidth(50);
              this.tab_Send.getColumnModel().getColumn(3).setPreferredWidth(80);
              this.tab_Send.getColumnModel().getColumn(4).setPreferredWidth(60);
              this.tab_Send.getColumnModel().getColumn(5).setPreferredWidth(70);
              this.tab_Send.getColumnModel().getColumn(6).setPreferredWidth(70);
              TableColumn columnCheck = this.tab_Send.getColumnModel().getColumn(9);
              columnCheck.setCellRenderer(new TriStateCellRenderer());
              columnCheck.setCellEditor(new TriStateCellEditor());
              columnCheck.setMaxWidth(45);
              tab_Send.setRowHeight(30);

              TabTools.setHideColumn(tab_Send, tab_Send.getColumnCount() - 1);

      
        } catch (SQLException ex) {
            Logger.getLogger(Frm_MobileHealth.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // 取出簡訊內容   name:簡訊標題
    private void setLetterDetail(String name) {
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

    public void setLetterList() {
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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tab_Send = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        btn_Send = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        list_Letter = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        txt_LetterText = new javax.swing.JTextArea();
        txt_LetterName = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        date_Com = new cc.johnwu.date.DateComboBox();
        jLabel1 = new javax.swing.JLabel();
        txt_Condidton = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        btn_Search = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Mobile Health");

        tab_Send.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tab_Send);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Message"));

        btn_Send.setText("Send");
        btn_Send.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_SendActionPerformed(evt);
            }
        });

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
        jScrollPane3.setViewportView(list_Letter);

        txt_LetterText.setColumns(20);
        txt_LetterText.setRows(5);
        jScrollPane2.setViewportView(txt_LetterText);

        jLabel3.setText("Title:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(634, 634, 634)
                        .addComponent(btn_Send, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_LetterName, javax.swing.GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE)
                                .addGap(316, 316, 316))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 618, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_LetterName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Send)
                .addContainerGap())
        );

        jLabel1.setText("Patient No.:");

        jLabel2.setText("Date of sending SMS:");

        btn_Search.setText("Search");
        btn_Search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_SearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(txt_Condidton, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_Search, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(date_Com, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(date_Com, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txt_Condidton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_Search))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jMenu1.setText("File");
        jMenu1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu1ActionPerformed(evt);
            }
        });

        jMenuItem4.setText("Mobile Health Setting");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuItem2.setText("Mobile Health Record");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem1.setText("Close");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(259, 259, 259))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 780, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
                .addGap(10, 10, 10)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_SearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SearchActionPerformed
        setSendList(txt_Condidton.getText().trim());
    }//GEN-LAST:event_btn_SearchActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
         new Frm_Main().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void list_LetterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_list_LetterMouseClicked
        if (list_Letter.getSelectedValue() != null) setLetterDetail(list_Letter.getSelectedValue().toString());

}//GEN-LAST:event_list_LetterMouseClicked

    private void list_LetterKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_list_LetterKeyReleased
        if (list_Letter.getSelectedValue() != null) setLetterDetail(list_Letter.getSelectedValue().toString());
}//GEN-LAST:event_list_LetterKeyReleased

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
         new Frm_MobileSetting(this).setVisible(true);
         this.setEnabled(false);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenu1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu1ActionPerformed
       
    }//GEN-LAST:event_jMenu1ActionPerformed

    private void btn_SendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SendActionPerformed
        String sql = null;
         try {
            for(int i = 0; i < tab_Send.getRowCount(); i++ ) {
                if (tab_Send.getValueAt(i, tab_Send.getColumnCount()-2).toString().equals("true")) {  // 倒數第二欄為checkbox
                    String cellPhone = "";

                    if (tab_Send.getValueAt(i, 7) != null)  cellPhone = tab_Send.getValueAt(i, 7).toString().trim();
                    else if (tab_Send.getValueAt(i, 8) != null) cellPhone = tab_Send.getValueAt(i, 8).toString().trim();


                    sql = "UPDATE package_set SET content = '"+txt_LetterText.getText().trim()+"', sms_state = '1', send_time = NOW(), " +
                            "send_sno = '"+UserInfo.getUserNO()+"', cell_phone = '"+cellPhone+"', title = '"+txt_LetterName.getText()+"' " +
                       "WHERE guid = '"+tab_Send.getValueAt(i, tab_Send.getColumnCount()-1)+"' ";
                    System.out.println(sql);
                    DBC.executeUpdate(sql);

                }

            }
            if (sql != null) {
                JOptionPane.showMessageDialog(null, "Submit completed.");
                setSendList("");
            }
            
         } catch (SQLException ex) {
                Logger.getLogger(Frm_MobileHealth.class.getName()).log(Level.SEVERE, null, ex);
         }
        
    }//GEN-LAST:event_btn_SendActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        new Frm_MobileRecord(this).setVisible(true);
        this.setEnabled(false);
    }//GEN-LAST:event_jMenuItem2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Search;
    private javax.swing.JButton btn_Send;
    private cc.johnwu.date.DateComboBox date_Com;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JList list_Letter;
    private javax.swing.JTable tab_Send;
    private javax.swing.JTextField txt_Condidton;
    private javax.swing.JTextField txt_LetterName;
    private javax.swing.JTextArea txt_LetterText;
    // End of variables declaration//GEN-END:variables

    public void onDateChanged() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
