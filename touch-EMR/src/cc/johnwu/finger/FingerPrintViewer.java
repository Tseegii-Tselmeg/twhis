package cc.johnwu.finger;


import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.metal.MetalInternalFrameUI;
import java.awt.image.BufferedImage;


public class FingerPrintViewer extends javax.swing.JInternalFrame {

    private JPanel fingerprintViewPanel;
    private BufferedImage fingerprintImage;

    public FingerPrintViewer() {
        initComponents();
        initUI();
    }

    private void initUI(){
        setUI(
            new MetalInternalFrameUI(this) {
                private JComponent titlePane;
                @Override
                protected JComponent createNorthPane(javax.swing.JInternalFrame f) {
                    titlePane = super.createNorthPane(f);
                    return titlePane;
                }
                @Override
                public void installUI(JComponent c) {
                    super.installUI(c);
                    //將滑鼠事件移除，目的為鎖定InternalFrame不可移動
                    titlePane.removeMouseMotionListener(titlePane.getMouseMotionListeners()[0]);
                }
            }
        );
        setContentPane(createFingerprintViewPanel());
    }

    public void showImage(BufferedImage bufferedimage) {
        fingerprintImage = bufferedimage;
        repaint();
    }

    public boolean isImageExist(){
        //確認圖片是否存在
        return fingerprintImage==null?false:true;
    }

    private JComponent createFingerprintViewPanel() {
        fingerprintViewPanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                //Override panel 繪圖事件
                super.paintComponent(g);
                if (fingerprintImage != null) {
                    Insets insets = getInsets();
                    int i = insets.left;
                    int j = insets.top;
                    int k = getWidth() - getInsets().right - getInsets().left;
                    int l = getHeight() - getInsets().bottom - getInsets().top;
                    g.drawImage(fingerprintImage, i, j, k, l, null);
                }
            }
        };
        fingerprintViewPanel.setBorder(new CompoundBorder(new EmptyBorder(2, 2, 2, 2), new BevelBorder(1)));
        return fingerprintViewPanel;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(200, 300));
        setMinimumSize(new java.awt.Dimension(100, 150));
        setPreferredSize(new java.awt.Dimension(100, 150));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 98, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 123, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents



    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
