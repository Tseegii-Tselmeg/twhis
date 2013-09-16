package cc.johnwu.sql;


import Multilingual.language;
import java.awt.Frame;
import java.sql.*;
import javax.swing.JOptionPane;


public class Frm_SettingMySQL extends javax.swing.JFrame {

     /*多國語言變數*/
   // private language paragraph = new language();
    //private String[] line = new String(paragraph.setlanguage("SETTINGMYSQL")).split("\n") ;
    /** Creates new form Frm_SettingMySQL */
    public Frm_SettingMySQL() {
        initComponents();
        initDefaultValues();
        //initLanguage() ;
    }

//    private void initLanguage() {
//        this.lab_Host.setText(paragraph.getLanguage(line, "HOST"));
//        this.lab_Port.setText(paragraph.getLanguage(line, "PORT"));
//        this.lab_Database.setText(paragraph.getLanguage(line, "DATABASE"));
//        this.lab_Passwd.setText(paragraph.getLanguage(line, "PASSWORD"));
//        this.lab_User.setText(paragraph.getLanguage(line, "USERNAME"));
//    }
    private void initDefaultValues(){
        //Linux bug:If JPasswordField can not keyin word, need enableInputMethods(true).
        this.txt_Passwd.enableInputMethods(true);
        //set this frame on center
        this.setLocationRelativeTo(this);
        //set text values
        ResultSet rs = null;
        try{
            rs = DBC.localExecuteQuery("SELECT * FROM conn_info");
            rs.next();
            this.txt_Host.setText(rs.getString("host").trim());
            this.txt_Port.setText(rs.getString("port").trim());
            this.txt_Database.setText(rs.getString("database").trim());
            this.txt_User.setText(HISPassword.deCode(rs.getString("user")).trim());
            this.txt_Passwd.setText(HISPassword.deCode(rs.getString("passwd")).trim());
        } catch (SQLException ex) {
            this.txt_Host.setText("");
            this.txt_Port.setText("");
            this.txt_Database.setText("");
            this.txt_User.setText("");
            this.txt_Passwd.setText("");
        } finally {
            try{DBC.closeConnection(rs);}
            catch (SQLException ex) {}
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pan_Bottom = new javax.swing.JPanel();
        btn_Save = new javax.swing.JButton();
        pan_Top = new javax.swing.JPanel();
        txt_Passwd = new javax.swing.JPasswordField();
        lab_Passwd = new javax.swing.JLabel();
        lab_User = new javax.swing.JLabel();
        lab_Port = new javax.swing.JLabel();
        lab_Host = new javax.swing.JLabel();
        txt_Host = new javax.swing.JTextField();
        txt_Port = new javax.swing.JTextField();
        txt_User = new javax.swing.JTextField();
        txt_Database = new javax.swing.JTextField();
        lab_Database = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MySQL Connection Setting");
        setAlwaysOnTop(true);
        setName("mysqlframe"); // NOI18N
        setResizable(false);

        btn_Save.setText("Save");
        btn_Save.setPreferredSize(new java.awt.Dimension(100, 29));
        btn_Save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_SaveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pan_BottomLayout = new javax.swing.GroupLayout(pan_Bottom);
        pan_Bottom.setLayout(pan_BottomLayout);
        pan_BottomLayout.setHorizontalGroup(
            pan_BottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan_BottomLayout.createSequentialGroup()
                .addContainerGap(417, Short.MAX_VALUE)
                .addComponent(btn_Save, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pan_BottomLayout.setVerticalGroup(
            pan_BottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btn_Save, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        lab_Passwd.setText("Password:");

        lab_User.setText("User Name:");

        lab_Port.setText("Port:");

        lab_Host.setText("Host:");

        lab_Database.setText("Database:");

        javax.swing.GroupLayout pan_TopLayout = new javax.swing.GroupLayout(pan_Top);
        pan_Top.setLayout(pan_TopLayout);
        pan_TopLayout.setHorizontalGroup(
            pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan_TopLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lab_Port)
                    .addComponent(lab_Database)
                    .addComponent(lab_Host)
                    .addComponent(lab_User)
                    .addComponent(lab_Passwd))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_Passwd, javax.swing.GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE)
                    .addComponent(txt_User, javax.swing.GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE)
                    .addComponent(txt_Database, javax.swing.GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE)
                    .addComponent(txt_Port, javax.swing.GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE)
                    .addComponent(txt_Host, javax.swing.GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE))
                .addContainerGap())
        );
        pan_TopLayout.setVerticalGroup(
            pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_TopLayout.createSequentialGroup()
                .addGroup(pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_Host, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_Host, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_Port, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_Port, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_Database, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_Database, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_User, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_User, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_Passwd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_Passwd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pan_Bottom, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pan_Top, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pan_Top, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pan_Bottom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_SaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SaveActionPerformed
        try {
            DBC.localExecuteUpdate("DELETE FROM conn_info");
            DBC.localExecuteUpdate("INSERT INTO conn_info VALUES(" +
                    "'"+this.txt_Host.getText()+"',"+
                    "'"+this.txt_Port.getText()+"',"+
                    "'"+this.txt_Database.getText()+"',"+
                    "'"+HISPassword.enCode(this.txt_User.getText())+"',"+
                    "'"+HISPassword.enCode(new String(this.txt_Passwd.getPassword()))+"'"+
                    ")");
            DBC.localExecute("SHUTDOWN");
            if(DBC.getConnection()){
                this.dispose();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(new Frame(), ex);
        }
}//GEN-LAST:event_btn_SaveActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Save;
    private javax.swing.JLabel lab_Database;
    private javax.swing.JLabel lab_Host;
    private javax.swing.JLabel lab_Passwd;
    private javax.swing.JLabel lab_Port;
    private javax.swing.JLabel lab_User;
    private javax.swing.JPanel pan_Bottom;
    private javax.swing.JPanel pan_Top;
    private javax.swing.JTextField txt_Database;
    private javax.swing.JTextField txt_Host;
    private javax.swing.JPasswordField txt_Passwd;
    private javax.swing.JTextField txt_Port;
    private javax.swing.JTextField txt_User;
    // End of variables declaration//GEN-END:variables

}
