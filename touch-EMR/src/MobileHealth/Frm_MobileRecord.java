
package MobileHealth;

import Main.Frm_Main;
import MobileHealth.TableTriStateCell.TriStateCellEditor;
import MobileHealth.TableTriStateCell.TriStateCellRenderer;
import Common.TabTools;
import cc.johnwu.sql.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
/**
 *
 * @author Steven
 */
public class Frm_MobileRecord extends javax.swing.JFrame {
    private Frm_MobileHealth m_Frm;
    /** Creates new form Frm_MobileRecord */
    public Frm_MobileRecord(Frm_MobileHealth frm) {
        m_Frm = frm;
        initComponents();
        this.setLocationRelativeTo(this);
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent windowevent) {
                    btn_CloseActionPerformed(null);
                }
            });
         tab_Send.setModel(getModle(new String[]{"Message"},new String[][]{{"You need click Search."}}));
         jLabel3.setVisible(false);
         cbox_SendStatus.setVisible(false);
    }

    /** 設定表單預設模型。*/
    private DefaultTableModel getModle(String[] title,String[][] data){
        return new DefaultTableModel(data,title){
                    @Override
                    public boolean isCellEditable(int r, int c){
                    return false;}
               };
    }

    private void setSendList(String pno) {
        try {
         Object[] title = {"","Patient No.","Name","Registration Date", "Back Date" ,"Advance Notice", "Sent Date","Cell Phone","Content" ,"guid"};
            String sql = "SELECT registration_info.guid, patients_info.p_no AS 'Patient No.', package_set.cell_phone AS 'Cell Phone', " +
                    "concat(patients_info.firstname,'  ',patients_info.lastname) AS 'Name', " +
                    "package_set.use_date AS 'Registration Date' ,package_set.content AS 'Content', " +
                    "(DATE_ADD(package_set.use_date, INTERVAL package_set.days DAY)) AS 'Back Days', "+
                    "(DATE_SUB((DATE_ADD(package_set.use_date, INTERVAL package_set.days DAY)), INTERVAL sys_info.remind_days DAY)) AS 'Advance Notice', " +
                    "package_set.send_time AS 'Sent Date' " +
                    "FROM package_set, patients_info LEFT JOIN contactperson_info ON  contactperson_info.guid = patients_info.cp_guid, registration_info, sys_info " +
                    "WHERE package_set.reg_guid = registration_info.guid  " +
                    "AND registration_info.p_no = patients_info.p_no  " +
    
                    //"AND sms_state = '"+cbox_SendStatus.getSelectedIndex()+"'  " +
                    "AND sms_state = '1'  " +
                    "AND  date_format(package_set.send_time,'%Y-%m-%d') = '"+date_Com.getValue()+"' AND patients_info.p_no LIKE '%"+pno+"%'  ";
            System.out.println(sql);
            ResultSet rs = DBC.executeQuery(sql);

            rs.last();
            Object[][] dataArray = new Object[rs.getRow()][title.length];
            txt_Number.setText(String.valueOf(rs.getRow()));
            rs.beforeFirst();
            int i = 0;

            while (rs.next()) {

                 dataArray[i][0] = i+1;
                 dataArray[i][1] = rs.getString("Patient No.");
                 dataArray[i][2] = rs.getString("Name");
                 dataArray[i][3] = rs.getString("Registration Date");
                 dataArray[i][4] = rs.getString("Back Days");
                 dataArray[i][5] = rs.getString("Advance Notice");
                 dataArray[i][6] = rs.getString("Sent Date");
                 dataArray[i][7] = rs.getString("Cell Phone");
                 dataArray[i][8] = rs.getString("Content");
                 dataArray[i][9] = rs.getString("guid");

                 i++;
             }
              DefaultTableModel tabModel = new DefaultTableModel(dataArray,title);
              tab_Send.setModel(tabModel);
              this.tab_Send.getColumnModel().getColumn(0).setMaxWidth(35);
              this.tab_Send.getColumnModel().getColumn(1).setPreferredWidth(70);
              this.tab_Send.getColumnModel().getColumn(3).setPreferredWidth(70);
              this.tab_Send.getColumnModel().getColumn(4).setPreferredWidth(70);
              this.tab_Send.getColumnModel().getColumn(5).setPreferredWidth(70);
              this.tab_Send.getColumnModel().getColumn(6).setPreferredWidth(70);
              this.tab_Send.getColumnModel().getColumn(6).setPreferredWidth(120);
              tab_Send.setRowHeight(30);
              TabTools.setHideColumn(tab_Send, tab_Send.getColumnCount() - 1);


        } catch (SQLException ex) {
            Logger.getLogger(Frm_MobileHealth.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        date_Com = new cc.johnwu.date.DateComboBox();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tab_Send = new javax.swing.JTable();
        btn_Search = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txt_Number = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        cbox_SendStatus = new javax.swing.JComboBox();
        btn_Close = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Mobile Health Record");

        jLabel2.setText("Date of sending SMS:");

        tab_Send.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tab_Send);

        btn_Search.setText("Search");
        btn_Search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_SearchActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("新細明體", 1, 14));
        jLabel1.setText("Number:");

        txt_Number.setEditable(false);
        txt_Number.setFont(new java.awt.Font("新細明體", 1, 14));
        txt_Number.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel3.setText("Send Status:");

        cbox_SendStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Not sent", "Sent" }));
        cbox_SendStatus.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbox_SendStatusItemStateChanged(evt);
            }
        });

        btn_Close.setText("Close");
        btn_Close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CloseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 780, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(date_Com, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_Search)
                                .addGap(158, 158, 158)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbox_SendStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 127, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txt_Number, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addComponent(btn_Close)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(date_Com, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(cbox_SendStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btn_Search)))
                .addGap(28, 28, 28)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 487, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txt_Number)
                        .addComponent(jLabel1))
                    .addComponent(btn_Close))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_SearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SearchActionPerformed
        setSendList("");
    }//GEN-LAST:event_btn_SearchActionPerformed

    private void btn_CloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CloseActionPerformed
        m_Frm.setEnabled(true);
        this.dispose();
    }//GEN-LAST:event_btn_CloseActionPerformed

    private void cbox_SendStatusItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbox_SendStatusItemStateChanged

    }//GEN-LAST:event_cbox_SendStatusItemStateChanged



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Close;
    private javax.swing.JButton btn_Search;
    private javax.swing.JComboBox cbox_SendStatus;
    private cc.johnwu.date.DateComboBox date_Com;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tab_Send;
    private javax.swing.JTextField txt_Number;
    // End of variables declaration//GEN-END:variables

}
