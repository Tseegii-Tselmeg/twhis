package cc.johnwu.login;

import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Frm_Welcome extends javax.swing.JFrame {
 
    public Frm_Welcome() {
        initComponents();

        /**表單置中*/
        this.setLocationRelativeTo(null) ;
        ImageIcon show = new ImageIcon("./img/logo.png");
        JLabel lab_show = new JLabel(show) ;
        lab_show.setBounds(0, 0, show.getIconWidth(), show.getIconHeight()) ;
        add(lab_show ,BorderLayout.CENTER ) ;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(800, 600));
        setResizable(false);
        setUndecorated(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
