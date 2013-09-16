package SMS;

import Main.Frm_Main;
import SMS.TableTriStateCell.TriStateCellEditor;
import SMS.TableTriStateCell.TriStateCellRenderer;
import Common.TabTools;
import cc.johnwu.sql.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
/**
 *
 * @author Steven
 */
public class Frm_Sms extends javax.swing.JFrame {

    /** Creates new form Frm_Sms */
    public Frm_Sms() {
        try {

            initComponents();
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent windowevent) {
                    btn_CloseActionPerformed(null);
                }
            });
            this.setExtendedState(Frm_Sms.MAXIMIZED_BOTH);  // 最大化
            this.setLocationRelativeTo(this);
            Object[] title = {"","Patient No.","Name","Date", "Set Type", "Back Days" ,"Advance Notice","Cell Phone","Contactperson", "Check"};
            String sql = "SELECT patients_info.p_no AS 'Patient No.', patients_info.cell_phone AS 'Cell Phone', " +
                    "concat(patients_info.firstname,'  ',patients_info.lastname) AS 'Name', " +
                    "package_set.use_date AS 'Date' ,package_set.id AS 'Set Type', " +

                    "package_set.days AS 'Back Days', "+
                    "sys_info.remind_days AS 'Advance Notice', contactperson_info.cell_phone AS 'Contactperson' " +
                    "FROM package_set, patients_info, registration_info, sys_info, contactperson_info " +
                    "WHERE package_set.reg_guid = registration_info.guid  " +
                    "AND registration_info.p_no = patients_info.p_no  " +
                    "AND contactperson_info.guid = patients_info.cp_guid " +
                    "AND sms_state = '0'  " +
                    "AND NOW() >= DATE_SUB(package_set.use_date, " +
                    "INTERVAL  (package_set.days -(SELECT remind_days FROM sys_info LIMIT 0,1)) DAY)  ";
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
              this.tab_Send.getColumnModel().getColumn(1).setMaxWidth(70);
              //this.tab_Send.getColumnModel().getColumn(2).setMaxWidth(50);
              this.tab_Send.getColumnModel().getColumn(3).setMaxWidth(80);
              this.tab_Send.getColumnModel().getColumn(4).setMaxWidth(60);
              this.tab_Send.getColumnModel().getColumn(5).setMaxWidth(70);
              this.tab_Send.getColumnModel().getColumn(6).setMaxWidth(70);
              TableColumn columnCheck = this.tab_Send.getColumnModel().getColumn(9);
              columnCheck.setCellRenderer(new TriStateCellRenderer());
              columnCheck.setCellEditor(new TriStateCellEditor());
              columnCheck.setMaxWidth(45);
              tab_Send.setRowHeight(30);

              //TabTools.setHideColumn(tab_Send, 7);
            
            System.out.println(sql);
        } catch (SQLException ex) {
            Logger.getLogger(Frm_Sms.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tab_Send = new javax.swing.JTable();
        btn_Close = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txt_Message = new javax.swing.JTextArea();
        btn_V1 = new javax.swing.JButton();
        btn_Send = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("SMS");

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

        btn_Close.setText("Close");
        btn_Close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CloseActionPerformed(evt);
            }
        });

        jButton2.setText("Search");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Message"));

        txt_Message.setColumns(20);
        txt_Message.setRows(5);
        jScrollPane2.setViewportView(txt_Message);

        btn_V1.setText("Revisit Newsletter");
        btn_V1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_V1ActionPerformed(evt);
            }
        });

        btn_Send.setText("Send");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
                    .addComponent(btn_Send, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
                    .addComponent(btn_V1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_V1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 408, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Send, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jMenu1.setText("File");
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
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 493, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(btn_Close, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 528, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_Close)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_CloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CloseActionPerformed
        new Frm_Main().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btn_CloseActionPerformed

    private void btn_V1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_V1ActionPerformed
        String msg = "Please return two days later, the hospital cares about your diagnosis ";

        txt_Message.setText(msg);
    }//GEN-LAST:event_btn_V1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Close;
    private javax.swing.JButton btn_Send;
    private javax.swing.JButton btn_V1;
    private javax.swing.JButton jButton2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTable tab_Send;
    private javax.swing.JTextArea txt_Message;
    // End of variables declaration//GEN-END:variables

}
