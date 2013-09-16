package Diagnosis;

import Laboratory.Frm_LabHistory;
import Laboratory.Frm_Laboratory;
import Radiology.Frm_Radiology;
import Radiology.Frm_RadiologyHistory;
import java.awt.Point;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Summary extends javax.swing.JFrame {
    private final int MAX_SUMMARY_HEIGHT = 600;
    private Frm_DiagnosisInfo m_DiagnosisInfo;
    private Frm_DiagnosisDiagnostic m_Diagnostic;
    private Frm_Laboratory m_Laboratory;
    private Frm_LabHistory m_Labhistory;
    private Frm_Radiology m_XRay;
    private Frm_RadiologyHistory m_XRayHistory;
    private int m_lineCount;
    private int m_State = 0;    // 1 lab 2 xray  0 dia


    public Summary(Frm_Laboratory frm, JTextArea txt, boolean edit, JPanel pan) {
        initComponents();
        m_State = 1;
        int h = 0;
        if(txt.getLineCount() > 4) {
            if((txt.getLineCount()-4) * 15 > MAX_SUMMARY_HEIGHT) {
                h = MAX_SUMMARY_HEIGHT;
            } else {
                h = (txt.getLineCount()-4) * 15;
            }
        }
        this.setSize(pan.getWidth(), pan.getHeight()+15+h);
        this.m_Laboratory = frm;
        txta_Summary.setText(txt.getText().trim());
        this.setLocation(pan.getLocationOnScreen());
        m_lineCount = txta_Summary.getLineCount();
    }

    public Summary(Frm_LabHistory frm, JTextArea txt, boolean edit, JPanel pan) {
         initComponents();
         txta_Summary.setEditable(edit);
        m_State = 1;
        int h = 0;
        if(txt.getLineCount() > 4) {
            if((txt.getLineCount()-4) * 15 > MAX_SUMMARY_HEIGHT) {
                h = MAX_SUMMARY_HEIGHT;
            } else {
                h = (txt.getLineCount()-4) * 15;
            }
        }
        this.setSize(pan.getWidth(), pan.getHeight()+15+h);
        this.m_Labhistory = frm;
        txta_Summary.setText(txt.getText().trim());
        this.setLocation(pan.getLocationOnScreen());
        m_lineCount = txta_Summary.getLineCount();
    }

     public Summary(Frm_RadiologyHistory frm, JTextArea txt, boolean edit, JPanel pan) {
         initComponents();
         txta_Summary.setEditable(edit);
        m_State = 1;
        int h = 0;
        if(txt.getLineCount() > 4) {
            if((txt.getLineCount()-4) * 15 > MAX_SUMMARY_HEIGHT) {
                h = MAX_SUMMARY_HEIGHT;
            } else {
                h = (txt.getLineCount()-4) * 15;
            }
        }
        this.setSize(pan.getWidth(), pan.getHeight()+15+h);
        this.m_XRayHistory = frm;
        txta_Summary.setText(txt.getText().trim());
        this.setLocation(pan.getLocationOnScreen());
        m_lineCount = txta_Summary.getLineCount();
    }

      public Summary(Frm_Radiology frm, JTextArea txt, boolean edit, JPanel pan) {
         initComponents();
        m_State = 1;
        int h = 0;
        if(txt.getLineCount() > 4) {
            if((txt.getLineCount()-4) * 15 > MAX_SUMMARY_HEIGHT) {
                h = MAX_SUMMARY_HEIGHT;
            } else {
                h = (txt.getLineCount()-4) * 15;
            }
        }
        this.setSize(pan.getWidth(), pan.getHeight()+15+h);
        this.m_XRay = frm;
        txta_Summary.setText(txt.getText().trim());
        this.setLocation(pan.getLocationOnScreen());
        m_lineCount = txta_Summary.getLineCount();
    }

    public Summary(Frm_DiagnosisDiagnostic diagnostic,String txtSummary, Point Localtion, int width, int height, int rowCount, boolean edit ) {
        initComponents();
        txta_Summary.setEditable(edit);
        txta_Summary.setLineWrap(true);  // txta_Summary 自動換行
        this.m_Diagnostic = diagnostic;
        int h = 0;
        if(rowCount > 4) {
            if((rowCount-4) * 15 > MAX_SUMMARY_HEIGHT) {
                h = MAX_SUMMARY_HEIGHT;
            } else {
                h = (rowCount-4) * 15;
            }
        }
        this.setSize(width,  height+h+10);
        txta_Summary.setText(txtSummary);
        this.setLocation(Localtion);
    }
    public Summary(Frm_DiagnosisInfo diagnosisInfo, String txtSummary, Point Localtion, int width, int height, int rowCount, boolean edit ) {
        initComponents();

        int h = 0;
        if(rowCount > 4) {
            if((rowCount-4) * 15 > MAX_SUMMARY_HEIGHT) {
                h = MAX_SUMMARY_HEIGHT;
            } else {
                h = (rowCount-4) * 15;
            }
        }
        this.setSize(width, height+15+h);
        this.m_DiagnosisInfo = diagnosisInfo;
        txta_Summary.setText(txtSummary);
        this.setLocation(Localtion);
        m_lineCount = txta_Summary.getLineCount();
    }



    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pan_Close = new javax.swing.JPanel();
        btn_Close = new javax.swing.JButton();
        span_Summary = new javax.swing.JScrollPane();
        txta_Summary = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
        setResizable(false);
        setUndecorated(true);

        btn_Close.setPreferredSize(new java.awt.Dimension(0, 0));
        btn_Close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CloseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pan_CloseLayout = new javax.swing.GroupLayout(pan_Close);
        pan_Close.setLayout(pan_CloseLayout);
        pan_CloseLayout.setHorizontalGroup(
            pan_CloseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan_CloseLayout.createSequentialGroup()
                .addComponent(btn_Close, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(194, 194, 194))
        );
        pan_CloseLayout.setVerticalGroup(
            pan_CloseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_CloseLayout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addComponent(btn_Close, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        txta_Summary.setColumns(5);
        txta_Summary.setRows(5);
        txta_Summary.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txta_SummaryMouseClicked(evt);
            }
        });
        txta_Summary.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txta_SummaryFocusLost(evt);
            }
        });
        txta_Summary.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txta_SummaryKeyPressed(evt);
            }
        });
        span_Summary.setViewportView(txta_Summary);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(pan_Close, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(span_Summary, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(span_Summary, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pan_Close, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_CloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CloseActionPerformed
        this.setAlwaysOnTop(false);
        if (m_Labhistory != null) {
            if (txta_Summary.isEditable()) {
                m_Labhistory.getTxtaSummary(txta_Summary.getText().trim());
            } else {
                m_Labhistory.setOnTop();
            }
        } else if (m_DiagnosisInfo != null) {
             if (txta_Summary.isEditable()) {
                    m_DiagnosisInfo.getTxtaSummary(txta_Summary.getText().trim());
             } else {
                m_Diagnostic.setOnTop();
             }
        } else if (m_Laboratory != null) {
             if (txta_Summary.isEditable()) {
                    m_Laboratory.getTxtaSummary(txta_Summary.getText().trim());
             } else {
                    m_Laboratory.setOnTop();
             }
        } else if (m_XRay != null) {
             if (txta_Summary.isEditable()) {
                    m_XRay.getTxtaSummary(txta_Summary.getText().trim());
             } else {
                    m_XRay.setOnTop();
             }
        } else if (m_XRayHistory != null) {
             if (txta_Summary.isEditable()) {
                    m_XRayHistory.getTxtaSummary(txta_Summary.getText().trim());
             } else {
                    m_XRayHistory.setOnTop();
             }
        }

       

        this.dispose();
    }//GEN-LAST:event_btn_CloseActionPerformed

    private void txta_SummaryFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txta_SummaryFocusLost
        btn_CloseActionPerformed(null);
    }//GEN-LAST:event_txta_SummaryFocusLost

    private void txta_SummaryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txta_SummaryMouseClicked
        if (evt.getClickCount() == 2) {
            btn_CloseActionPerformed(null);
        }
    }//GEN-LAST:event_txta_SummaryMouseClicked

    private void txta_SummaryKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txta_SummaryKeyPressed
        if (txta_Summary.getHeight() < MAX_SUMMARY_HEIGHT && txta_Summary.getLineCount() > 5) {
            if (txta_Summary.getLineCount() > m_lineCount && evt.getKeyCode() == KeyEvent.VK_ENTER) {
                this.setSize(this.getWidth(), this.getHeight()+15);
                m_lineCount = txta_Summary.getLineCount();
            }
        }
        btn_Close.setMnemonic(KeyEvent.VK_ENTER);
    }//GEN-LAST:event_txta_SummaryKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Close;
    private javax.swing.JPanel pan_Close;
    private javax.swing.JScrollPane span_Summary;
    private javax.swing.JTextArea txta_Summary;
    // End of variables declaration//GEN-END:variables
}