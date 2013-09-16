/*
 * Frm_Test.java
 *
 * Created on 2010/9/4, 下午 04:14:27
 */

package Test;

import cc.johnwu.sql.DBC;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Steven
 */
public class Frm_Test extends javax.swing.JFrame {

    /** Creates new form Frm_Test */
    public Frm_Test() {
        initComponents();
    }

    /**
     * 確認檢驗是否正常 code:檢驗code, age:年齡, gender:性別, value:檢驗值
     * return 0 正常 1 異常 2 錯誤
     */
    private int getPrescriptionNormal(String code, int age, String gender,double value) {
        ResultSet rs = null;
        boolean isAge = false;
        boolean isGender = false;
        try {
            String sql = "SELECT `limit` FROM prescription_code " +
                        "WHERE code = '" + code + "' ";
            rs = DBC.executeQuery(sql);
            if(!rs.next() || rs.getString("limit") == null) return 2;
         

            String lm[] = rs.getString("limit").split("&");

            if (lm.length == 0) return 0;

            for(int i = 0; i < lm.length; i++ ) {
                String lm2[] = lm[i].split("!");  // lm2[0] 年齡 [1] 性別

                // ------------ 年齡判斷 -----------
                String ageArr[] =  lm2[0].split("-");
                if ((ageArr.length == 1 && (lm2[0].equals("n") || Integer.parseInt(lm2[0]) == age))
                || (ageArr.length == 2 && Integer.parseInt(ageArr[0]) <= age && Integer.parseInt(ageArr[1]) >= age)) isAge = true;

                // ------------ 性別判斷 -----------
                if (lm2[1].equals(gender) || lm2[1].equals("n") ) isGender = true;

                // --------- 年齡性別符合 進入檢驗值判斷 -----------
                if (isAge && isGender) {
                    for (int y =2; y < lm2.length; y++) {
                        String a[] = lm2[y].split(">");
                        String b[] = lm2[y].split("<");
                        String c[] = lm2[y].split("-");

                        if (a.length == 2 && (Double.parseDouble(a[1]) <=  value))  return 1;
                        else if (b.length == 2 && (Double.parseDouble(b[1]) >=  value))  return 1;
                        else if (c.length == 2 && (Double.parseDouble(c[0]) <=  value && value <= Double.parseDouble(c[1])))  return 1;
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Frm_Test.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {DBC.closeConnection(rs);}
            catch (SQLException e){
                Logger.getLogger(Frm_Test.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return 0;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txt_Gender = new javax.swing.JTextField();
        txt_Age = new javax.swing.JTextField();
        txt_Code = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txt_Value = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        txt_UpCode = new javax.swing.JTextField();
        txt_UpLimit = new javax.swing.JTextField();
        lab_State = new javax.swing.JLabel();
        btn_Spilt = new javax.swing.JButton();
        txt_Word = new javax.swing.JTextField();
        txt_Spilt = new javax.swing.JTextField();
        lab_Spilt = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("檢驗上下限值測試");

        txt_Gender.setText("F");

        txt_Age.setText("18");

        txt_Code.setText("30043");

        jLabel2.setText("Gender");

        jLabel3.setText("Age");

        jLabel4.setText("Prescription Code");

        jLabel5.setText("Value");

        jButton1.setText("jButton1");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Insert");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        lab_State.setText("STATE");

        btn_Spilt.setText("jButton3");
        btn_Spilt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_SpiltActionPerformed(evt);
            }
        });

        lab_Spilt.setText("jLabel6");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(165, 165, 165))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_Code, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
                            .addComponent(txt_Age, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
                            .addComponent(txt_Gender, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
                            .addComponent(txt_Value, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE))
                        .addGap(165, 165, 165))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lab_State)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 286, Short.MAX_VALUE)
                        .addComponent(jButton1)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txt_UpLimit, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_UpCode, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)
                        .addGap(78, 78, 78))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txt_Word, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_Spilt, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_Spilt)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lab_Spilt, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(txt_UpCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_UpLimit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_Word, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_Spilt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_Spilt)
                    .addComponent(lab_Spilt))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txt_Gender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txt_Age, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txt_Code, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_Value, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(lab_State))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       if (getPrescriptionNormal(this.txt_Code.getText(), Integer.parseInt(this.txt_Age.getText()), this.txt_Gender.getText(),
               Double.parseDouble(this.txt_Value.getText())) == 0) this.lab_State.setText("此項檢查正常");
       else if(getPrescriptionNormal(this.txt_Code.getText(), Integer.parseInt(this.txt_Age.getText()), this.txt_Gender.getText(),
               Double.parseDouble(this.txt_Value.getText())) == 1)  this.lab_State.setText("此項檢查異常");
       else if(getPrescriptionNormal(this.txt_Code.getText(), Integer.parseInt(this.txt_Age.getText()), this.txt_Gender.getText(),
               Double.parseDouble(this.txt_Value.getText())) == 2)  this.lab_State.setText("無限制資料");

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            String sql = "UPDATE prescription_code SET `limit`= '"+this.txt_UpLimit.getText().trim()+"' " +
                        " WHERE code = '"+this.txt_UpCode.getText().trim()+"'";
            DBC.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(Frm_Test.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jButton2ActionPerformed

    private void btn_SpiltActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SpiltActionPerformed

        this.lab_Spilt.setText(String.valueOf(txt_Word.getText().split(this.txt_Spilt.getText()).length));
    }//GEN-LAST:event_btn_SpiltActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Spilt;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel lab_Spilt;
    private javax.swing.JLabel lab_State;
    private javax.swing.JTextField txt_Age;
    private javax.swing.JTextField txt_Code;
    private javax.swing.JTextField txt_Gender;
    private javax.swing.JTextField txt_Spilt;
    private javax.swing.JTextField txt_UpCode;
    private javax.swing.JTextField txt_UpLimit;
    private javax.swing.JTextField txt_Value;
    private javax.swing.JTextField txt_Word;
    // End of variables declaration//GEN-END:variables

}
