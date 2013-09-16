
package Cashier;

import Common.Constant;
import cc.johnwu.login.UserInfo;
import cc.johnwu.sql.DBC;
import java.awt.Frame;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
/**
 *
 * @author Steven
 */
public class Frm_CashierHistory extends javax.swing.JFrame {
    private String m_Pno;
    private javax.swing.JFrame m_Frm;
    /** Creates new form Frm_CashierHistory */
    public Frm_CashierHistory(javax.swing.JFrame frm, String pno) {
        m_Frm = frm;
        m_Pno = pno;
        initComponents();
        this.setLocationRelativeTo(this);
        this.tab_Payment.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);     // tabble不可按住多選
        addWindowListener(new WindowAdapter() {  // 畫面關閉原視窗enable
            @Override
            public void windowClosing(WindowEvent windowevent) {
               jButton1ActionPerformed(null);
            }
        });
        initTab();
    }

    private void initTab() {
        try {
            txt_PaidAmount.setText("");
            DefaultTableModel tabModel = null;
            ResultSet rs = null;
            String sql = null;
            Object[][] dataArray = null;
            double total = 0;
            String[] title = { " ","Payment Time" ,"Type", "Amount Receivable", "Paid Amount", "Arrears","Backin", "no","reg_guid"}; // table表頭
            sql = "SELECT payment_time AS paytime, no, backin,  reg_guid, CASE typ " +
                    "WHEN 'R' THEN 'Registration' " +
                    "WHEN 'P' THEN 'Pharmacy' " +
                    "WHEN 'X' THEN 'Radiology(X-RAY)' " +
                    "WHEN 'L' THEN 'Laboratory' END 'typ' , amount_receivable, paid_amount, cashier.arrears  " +
                    "FROM cashier WHERE cashier.p_no = '"+m_Pno+"' ";
            
            if (cbox_Status.getSelectedIndex() == 1) {
                sql +=  "AND cashier.arrears <> 0 AND (backin <> 'Y'  OR cashier.backin IS NULL) ORDER BY paytime DESC";
                btn_Complete.setEnabled(true);
            } else {
                sql +=  "AND (cashier.arrears = 0 OR backin = 'Y') ORDER BY paytime DESC";
                txt_AmountReceivable.setText("");
                txt_Arrears.setText("");

                btn_Complete.setEnabled(false);
            }


            System.out.println(sql);
            rs = DBC.executeQuery(sql);
            rs.last();
            dataArray = new Object[rs.getRow()][9];
            rs.beforeFirst();
            int i = 0;
            while (rs.next()) {

                dataArray[i][0] = i + 1;
                dataArray[i][1] = rs.getString("paytime");
                dataArray[i][2] = rs.getString("typ");
                dataArray[i][3] = rs.getString("amount_receivable");
                dataArray[i][4] = rs.getString("paid_amount");
                dataArray[i][5] = rs.getString("arrears");
                dataArray[i][6] = rs.getString("backin");
                dataArray[i][7] = rs.getString("no");
                dataArray[i][8] = rs.getString("reg_guid");
                total += rs.getDouble("arrears");
                i++;
            }
            tabModel = new DefaultTableModel(dataArray, title) {
                @Override
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                        return false;
                }
            };
            if (cbox_Status.getSelectedIndex() == 1) txt_AmountReceivable.setText(String.valueOf(total));
            
            tab_Payment.setModel(tabModel);
            tab_Payment.setRowHeight(30);
            Common.TabTools.setHideColumn(tab_Payment, tab_Payment.getColumnCount()-1);
            Common.TabTools.setHideColumn(tab_Payment, tab_Payment.getColumnCount()-2);
        } catch (SQLException ex) {
            Logger.getLogger(Frm_CashierHistory.class.getName()).log(Level.SEVERE, null, ex);
        }
  }

        private void setFinish(String finish) {
        // 判斷資料不是數值先把格子清空
        boolean IsCanFinish = true;



        if (!txt_PaidAmount.getText().trim().equals("") && !txt_AmountReceivable.getText().trim().equals("") && Common.Tools.isNumber(txt_PaidAmount.getText().trim()) && Common.Tools.isNumber(txt_AmountReceivable.getText().trim())) {
            txt_AmountReceivable.setText(String.valueOf(Double.parseDouble(txt_AmountReceivable.getText().trim()) - Double.parseDouble(txt_PaidAmount.getText().trim())));
        } else {
            txt_AmountReceivable.setText("");
            IsCanFinish = false;
        }

        if (!IsCanFinish) {
            JOptionPane.showMessageDialog(null, "Cost does not enter the complete.");
        } else {
            try {
                String sql = null;
                String paymentType = null;

                // 儲存付費記錄
                for (int i = 0; i < tab_Payment.getRowCount(); i++) {
                    sql = "UPDATE cashier SET backin = 'Y', backin_time = NOW(), backin_sno = '"+UserInfo.getUserNO()+"' WHERE no = '"+tab_Payment.getValueAt(i, tab_Payment.getColumnCount()-2)+"'";
                    DBC.executeUpdate(sql);
                }
             } catch (SQLException ex) {
                Logger.getLogger(Frm_CashierInfo.class.getName()).log(Level.SEVERE, null, ex);
            }
            JOptionPane.showMessageDialog(null, "Saved successfully.");
            initTab();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tab_Payment = new javax.swing.JTable();
        cbox_Status = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        txt_AmountReceivable = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txt_Arrears = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        btn_Complete = new javax.swing.JButton();
        txt_PaidAmount = new javax.swing.JTextField();
        mnb = new javax.swing.JMenuBar();
        menu_File = new javax.swing.JMenu();
        mnit_Back = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Cashier Record");

        tab_Payment.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "2010/10/09", "30", "20", "10"},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "No.", "Date", "Payment", "Actual payment", "Actual"
            }
        ));
        jScrollPane1.setViewportView(tab_Payment);

        cbox_Status.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "All", "Arrears" }));
        cbox_Status.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbox_StatusItemStateChanged(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("新細明體", 1, 18));
        jLabel1.setText("Total Arrears:");

        txt_AmountReceivable.setEditable(false);
        txt_AmountReceivable.setFont(new java.awt.Font("新細明體", 1, 18)); // NOI18N

        jButton1.setText("Close");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("新細明體", 1, 18));
        jLabel2.setText("-");

        jLabel3.setFont(new java.awt.Font("新細明體", 1, 18));
        jLabel3.setText("Paid Amount:");

        jLabel5.setFont(new java.awt.Font("新細明體", 1, 18));
        jLabel5.setText("Arrears:");

        txt_Arrears.setEditable(false);
        txt_Arrears.setFont(new java.awt.Font("新細明體", 1, 18));
        txt_Arrears.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        jLabel4.setFont(new java.awt.Font("新細明體", 1, 18));
        jLabel4.setText("=");

        btn_Complete.setText("Complete the payment");
        btn_Complete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CompleteActionPerformed(evt);
            }
        });

        txt_PaidAmount.setFont(new java.awt.Font("新細明體", 1, 18));
        txt_PaidAmount.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txt_PaidAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_PaidAmountActionPerformed(evt);
            }
        });
        txt_PaidAmount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_PaidAmountKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_PaidAmountKeyReleased(evt);
            }
        });

        menu_File.setText("File");

        mnit_Back.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0));
        mnit_Back.setText("Close");
        mnit_Back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnit_BackActionPerformed(evt);
            }
        });
        menu_File.add(mnit_Back);

        mnb.add(menu_File);

        setJMenuBar(mnb);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 904, Short.MAX_VALUE)
                    .addComponent(cbox_Status, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btn_Complete, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_AmountReceivable, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_PaidAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_Arrears, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cbox_Status, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txt_AmountReceivable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(txt_PaidAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(txt_Arrears, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(btn_Complete))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mnit_BackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnit_BackActionPerformed

}//GEN-LAST:event_mnit_BackActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        m_Frm.setEnabled(true);
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btn_CompleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CompleteActionPerformed
        if (txt_Arrears.getText().equals("0.0")) setFinish("F");

    }//GEN-LAST:event_btn_CompleteActionPerformed

    private void txt_PaidAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_PaidAmountActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_txt_PaidAmountActionPerformed

    private void txt_PaidAmountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_PaidAmountKeyPressed

}//GEN-LAST:event_txt_PaidAmountKeyPressed

    private void txt_PaidAmountKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_PaidAmountKeyReleased
        if (!txt_PaidAmount.getText().trim().equals("") && !txt_AmountReceivable.getText().trim().equals("")
                && Common.Tools.isNumber(txt_PaidAmount.getText().trim()) && Common.Tools.isNumber(txt_AmountReceivable.getText().trim()) &&
                Double.parseDouble(txt_AmountReceivable.getText().trim()) >= Double.parseDouble(txt_PaidAmount.getText().trim())) {
            txt_Arrears.setText(String.valueOf(Double.parseDouble(txt_AmountReceivable.getText().trim()) - Double.parseDouble(txt_PaidAmount.getText().trim())));
        } else {
            txt_Arrears.setText("");
        }
}//GEN-LAST:event_txt_PaidAmountKeyReleased

    private void cbox_StatusItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbox_StatusItemStateChanged
         if(evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) initTab();
    }//GEN-LAST:event_cbox_StatusItemStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Complete;
    private javax.swing.JComboBox cbox_Status;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JMenu menu_File;
    private javax.swing.JMenuBar mnb;
    private javax.swing.JMenuItem mnit_Back;
    private javax.swing.JTable tab_Payment;
    private javax.swing.JTextField txt_AmountReceivable;
    private javax.swing.JTextField txt_Arrears;
    private javax.swing.JTextField txt_PaidAmount;
    // End of variables declaration//GEN-END:variables

}
