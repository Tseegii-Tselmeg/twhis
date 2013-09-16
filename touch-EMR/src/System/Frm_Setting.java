package System;

import cc.johnwu.sql.DBC;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author Steven
 */
public class Frm_Setting extends javax.swing.JFrame {
    public Frm_Setting() {
        initComponents();
        addWindowListener(new WindowAdapter() {                                // 畫面關閉原視窗enable
            @Override
            public void windowClosing(WindowEvent windowevent) {
                btn_CloseActionPerformed(null);
            }
        });
        this.setExtendedState(Frm_Setting.MAXIMIZED_BOTH);  // 最大化
        this.setLocationRelativeTo(this); // 置中
        btn_Save.setEnabled(false);
        init();

    }

    // 初始化
    private void init() {
        try{
           ResultSet rs = DBC.executeQuery("SELECT * FROM setting WHERE id = 1");
           if(rs.next()){
               this.txt_MorningS.setText(rs.getString("morning_shift_s"));
               this.txt_MorningE.setText(rs.getString("morning_shift_e"));
               this.txt_NoonS.setText(rs.getString("noon_shift_s"));
               this.txt_NoonE.setText(rs.getString("noon_shift_e"));
               this.txt_NightS.setText(rs.getString("evening_shift_s"));
               this.txt_NightE.setText(rs.getString("evening_shift_e"));
               this.txt_NightE.setText(rs.getString("evening_shift_e"));
           }
           reLoad();

           
        }
        catch (SQLException ex){
            Logger.getLogger(Frm_Setting.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void reLoad() {
        btn_ReLoad.setEnabled(false);
        btn_Save.setEnabled(false);
    }

    // 判斷藥品輸入的值是否為數字
    public boolean isNumber(String str) {
        if (str.length() ==3 || str.length() == 4) {
            Pattern pattern = Pattern.compile("[0-9]*");
            Matcher isNum = pattern.matcher(str);
            if (!isNum.matches()) {
                return false;
            } else {
                if (Integer.parseInt(str) < 2400 && Integer.parseInt(str) >= 0) {
                    return true;
                } else {
                    return false;
                }

            }
        }
        return false;

    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFileChooser1 = new javax.swing.JFileChooser();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txt_MorningS = new javax.swing.JTextField();
        txt_NoonS = new javax.swing.JTextField();
        txt_NightS = new javax.swing.JTextField();
        txt_NoonE = new javax.swing.JTextField();
        txt_NightE = new javax.swing.JTextField();
        txt_MorningE = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        pan_under = new javax.swing.JPanel();
        btn_Save = new javax.swing.JButton();
        btn_ReLoad = new javax.swing.JButton();
        btn_Default = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        btn_EnterHL7 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        txt_Pno = new javax.swing.JTextField();
        btn_Close = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Setting");
        setMinimumSize(new java.awt.Dimension(800, 600));

        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setText("Morning Shift : ");

        jLabel2.setText("Afternoon Shift : ");

        jLabel3.setText("Night Shift : ");

        txt_MorningS.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_MorningSKeyReleased(evt);
            }
        });

        txt_NoonS.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_NoonSKeyReleased(evt);
            }
        });

        txt_NightS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_NightSActionPerformed(evt);
            }
        });
        txt_NightS.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_NightSKeyReleased(evt);
            }
        });

        txt_NoonE.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_NoonEKeyReleased(evt);
            }
        });

        txt_NightE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_NightEActionPerformed(evt);
            }
        });
        txt_NightE.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_NightEKeyReleased(evt);
            }
        });

        txt_MorningE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_MorningEActionPerformed(evt);
            }
        });
        txt_MorningE.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_MorningEKeyReleased(evt);
            }
        });

        jLabel8.setText("To");

        jLabel9.setText("To");

        jLabel10.setText("To");

        btn_Save.setText("Save");
        btn_Save.setEnabled(false);
        btn_Save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_SaveActionPerformed(evt);
            }
        });

        btn_ReLoad.setText("Re-read");
        btn_ReLoad.setEnabled(false);
        btn_ReLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ReLoadActionPerformed(evt);
            }
        });

        btn_Default.setText("Default");
        btn_Default.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_DefaultActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pan_underLayout = new javax.swing.GroupLayout(pan_under);
        pan_under.setLayout(pan_underLayout);
        pan_underLayout.setHorizontalGroup(
            pan_underLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan_underLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_Default, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_ReLoad, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Save, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pan_underLayout.setVerticalGroup(
            pan_underLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_underLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btn_Save)
                .addComponent(btn_ReLoad)
                .addComponent(btn_Default))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_NoonS, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_NightS, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_MorningS, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_MorningE, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_NoonE, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_NightE, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(377, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(pan_under, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txt_MorningS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(txt_MorningE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txt_NoonS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(txt_NoonE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txt_NightS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(txt_NightE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 376, Short.MAX_VALUE)
                .addComponent(pan_under, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Shift Setting", jPanel1);

        btn_EnterHL7.setText("Enter");
        btn_EnterHL7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_EnterHL7ActionPerformed(evt);
            }
        });

        jLabel4.setText("Patient No.:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_Pno, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_EnterHL7)
                .addContainerGap(449, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_EnterHL7)
                    .addComponent(jLabel4)
                    .addComponent(txt_Pno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(491, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("HL7", jPanel2);

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
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 787, Short.MAX_VALUE)
                    .addComponent(btn_Close, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Close)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_SaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SaveActionPerformed
        try {

                if (isNumber(txt_MorningS.getText()) && isNumber(txt_MorningE.getText())
                        && isNumber(txt_NoonS.getText()) && isNumber(txt_NoonE.getText())
                        && isNumber(txt_NightS.getText()) && isNumber(txt_NightE.getText())) {
                        DBC.executeUpdate("UPDATE setting SET " +
                            "morning_shift_s = '"+txt_MorningS.getText()+"'," +
                            "morning_shift_e = '"+txt_MorningE.getText()+"'," +
                            "noon_shift_s ='"+txt_NoonS.getText()+"'," +
                            "noon_shift_e ='"+txt_NoonE.getText()+"'," +
                            "evening_shift_s ='"+txt_NightS.getText()+"'," +
                            "evening_shift_e ='"+txt_NightE.getText()+"'" +
                            " WHERE id = 1");
                        JOptionPane.showMessageDialog(null, "Save Completeadmin .");
                        reLoad();
                } else {
                    JOptionPane.showMessageDialog(null, "Input Time Error.");
                }
          

    
            

        } catch (SQLException ex) {
            Logger.getLogger(Frm_Setting.class.getName()).log(Level.SEVERE, null, ex);
        }
}//GEN-LAST:event_btn_SaveActionPerformed

    private void btn_CloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CloseActionPerformed
        new Main.Frm_Main().setVisible(true);
        this.dispose();
}//GEN-LAST:event_btn_CloseActionPerformed

    private void btn_ReLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ReLoadActionPerformed
         init();
}//GEN-LAST:event_btn_ReLoadActionPerformed

    private void txt_NightSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_NightSActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_NightSActionPerformed

    private void txt_NightEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_NightEActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_NightEActionPerformed

    private void txt_MorningSKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_MorningSKeyReleased
        btn_ReLoad.setEnabled(true);
        btn_Save.setEnabled(true);
    }//GEN-LAST:event_txt_MorningSKeyReleased

    private void txt_MorningEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_MorningEActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_MorningEActionPerformed

    private void txt_MorningEKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_MorningEKeyReleased
        btn_ReLoad.setEnabled(true);
        btn_Save.setEnabled(true);
    }//GEN-LAST:event_txt_MorningEKeyReleased

    private void txt_NoonSKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_NoonSKeyReleased
        btn_ReLoad.setEnabled(true);
        btn_Save.setEnabled(true);
    }//GEN-LAST:event_txt_NoonSKeyReleased

    private void txt_NoonEKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_NoonEKeyReleased
        btn_ReLoad.setEnabled(true);
        btn_Save.setEnabled(true);
    }//GEN-LAST:event_txt_NoonEKeyReleased

    private void txt_NightSKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_NightSKeyReleased
       btn_ReLoad.setEnabled(true);
        btn_Save.setEnabled(true);
    }//GEN-LAST:event_txt_NightSKeyReleased

    private void txt_NightEKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_NightEKeyReleased
        btn_ReLoad.setEnabled(true);
        btn_Save.setEnabled(true);
    }//GEN-LAST:event_txt_NightEKeyReleased

    private void btn_DefaultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_DefaultActionPerformed
        this.txt_MorningS.setText("800");
        this.txt_MorningE.setText("1159");
        this.txt_NoonS.setText("1200");
        this.txt_NoonE.setText("1759");
        this.txt_NightS.setText("1800");
        this.txt_NightE.setText("2359");
    }//GEN-LAST:event_btn_DefaultActionPerformed

    private void btn_EnterHL7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_EnterHL7ActionPerformed
        try {
            ResultSet rs = DBC.executeQuery("SELECT p_no FROM patients_info WHERE p_no = '"+txt_Pno.getText().trim()+"'");
            if (rs.next()) new frm_outputHl7(txt_Pno.getText().trim()).setVisible(true);
            else JOptionPane.showMessageDialog(new Frame(), "No Information.");

        } catch (SQLException ex) {
            Logger.getLogger(Frm_Setting.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_EnterHL7ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Close;
    private javax.swing.JButton btn_Default;
    private javax.swing.JButton btn_EnterHL7;
    private javax.swing.JButton btn_ReLoad;
    private javax.swing.JButton btn_Save;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JPanel pan_under;
    private javax.swing.JTextField txt_MorningE;
    private javax.swing.JTextField txt_MorningS;
    private javax.swing.JTextField txt_NightE;
    private javax.swing.JTextField txt_NightS;
    private javax.swing.JTextField txt_NoonE;
    private javax.swing.JTextField txt_NoonS;
    private javax.swing.JTextField txt_Pno;
    // End of variables declaration//GEN-END:variables
}
