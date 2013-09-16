package cc.johnwu.login;


import Multilingual.language;
import cc.johnwu.sql.*;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.sql.*;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;


public class Frm_Login extends javax.swing.JFrame {
    private UserInfo m_UserInfo;

    /*多國語言變數*/
    private language paragraph = new language();
    private String[] line = paragraph.setlanguage("LOGIN").split("\n") ;

    /** Creates new form Frm_Login */
    public Frm_Login(UserInfo userInfo, boolean showWelcome) throws InterruptedException {
        m_UserInfo = userInfo;
        if (showWelcome) {
            Frm_Welcome fw = new Frm_Welcome();
            fw.setVisible(true) ;
            /**系統停止3秒*/
            Thread.sleep(3000);
            fw.setVisible(false);
        }
        

        initComponents();
        //set this frame on center
        this.setLocationRelativeTo(this);
        // linux's BUG of jPasswdField
        txt_Passwd.enableInputMethods(true);

        ImageIcon icon = new ImageIcon("./img/welcome.png");
        JLabel jl = new JLabel(icon);
        jl.setBounds(0, -30, icon.getIconWidth(), icon.getIconHeight());

        add(jl, BorderLayout.CENTER);
        initLanguage();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btn_Login = new javax.swing.JButton();
        txt_Id = new javax.swing.JTextField();
        txt_Passwd = new javax.swing.JPasswordField();
        lab_Id = new javax.swing.JLabel();
        lab_Passwd = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Welcome");
        setMinimumSize(new java.awt.Dimension(800, 600));
        setResizable(false);
        getContentPane().setLayout(null);

        btn_Login.setText("Login");
        btn_Login.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_LoginActionPerformed(evt);
            }
        });
        getContentPane().add(btn_Login);
        btn_Login.setBounds(690, 230, 100, 23);

        txt_Id.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_IdKeyReleased(evt);
            }
        });
        getContentPane().add(txt_Id);
        txt_Id.setBounds(630, 140, 160, 21);

        txt_Passwd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_PasswdKeyReleased(evt);
            }
        });
        getContentPane().add(txt_Passwd);
        txt_Passwd.setBounds(630, 190, 160, 21);

        lab_Id.setFont(lab_Id.getFont().deriveFont((float)18));
        lab_Id.setForeground(new java.awt.Color(255, 255, 200));
        lab_Id.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lab_Id.setText("ID:");
        getContentPane().add(lab_Id);
        lab_Id.setBounds(630, 120, 160, 23);

        lab_Passwd.setFont(lab_Passwd.getFont().deriveFont((float)18));
        lab_Passwd.setForeground(new java.awt.Color(255, 255, 200));
        lab_Passwd.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lab_Passwd.setText("Passwd:");
        getContentPane().add(lab_Passwd);
        lab_Passwd.setBounds(630, 170, 160, 23);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void initLanguage() {
        this.lab_Id.setText(paragraph.getLanguage(line, "ID"));
        this.lab_Passwd.setText(paragraph.getLanguage(line, "PASSWD"));
        this.btn_Login.setText(paragraph.getLanguage(line, "LOGIN"));
        this.setTitle(paragraph.getLanguage(line, "WELCOME"));

    }
    private void btn_LoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_LoginActionPerformed
        ResultSet rs = null;
        String sql = "SELECT s_id, passwd " +
                     "FROM staff_info " +
                     "WHERE s_id = '"+ txt_Id.getText() +"' " +
                     "AND passwd = '"+ HISPassword.enCode(new String(txt_Passwd.getPassword())) +"'";

        try {
            rs = DBC.executeQuery(sql);
            if(!rs.next() || !m_UserInfo.setOnLine(txt_Id.getText())){
                this.txt_Passwd.setText("");
                JOptionPane.showMessageDialog(new Frame(), paragraph.getLanguage(line, "LOGINFAILED"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(new Frame(), ex);
        } finally {
            try{DBC.closeConnection(rs);}
            catch (SQLException ex) {}
        }
    }//GEN-LAST:event_btn_LoginActionPerformed

    private void txt_PasswdKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_PasswdKeyReleased

        if (evt.getKeyCode() == 10) {
            btn_LoginActionPerformed(null);
        }
        
    }//GEN-LAST:event_txt_PasswdKeyReleased

    private void txt_IdKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_IdKeyReleased
        if (evt.getKeyCode() == 10) {
            btn_LoginActionPerformed(null);
        }
    }//GEN-LAST:event_txt_IdKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Login;
    private javax.swing.JLabel lab_Id;
    private javax.swing.JLabel lab_Passwd;
    private javax.swing.JTextField txt_Id;
    private javax.swing.JPasswordField txt_Passwd;
    // End of variables declaration//GEN-END:variables
}
