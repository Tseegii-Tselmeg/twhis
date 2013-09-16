package Radiology;

import Diagnosis.Frm_DiagnosisInfo;
import Diagnosis.Summary;
import Multilingual.language;
import Patients.Frm_PatientMod;
import Common.TabTools;
import cc.johnwu.sql.DBC;
import cc.johnwu.sql.HISModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author steven
 */
public class Frm_RadiologyHistory extends javax.swing.JFrame {
    private final String X_RAY_CODE = "X-RAY";      // 處置X光代碼
    private String m_Pno;
    private String m_RegistrationGuid;  // registration guid
    private Frm_DiagnosisInfo m_DiaFrm;
    private Frm_Radiology m_LabFrm;
    private Frm_PatientMod m_PatNodFrm;
    private boolean m_FinishState;      //
    private int m_WorkListRowNo;        // Frm_DiagnosisWorkList's stop rowNo
    private String m_TmpGuid;   // 暫存Gued
    
    
     /*多國語言變數*/
    private language paragraph = new language();
    private String[] line = paragraph.setlanguage("LABORATORY").split("\n") ;
    private String[] message = paragraph.setlanguage("MESSAGE").split("\n") ;
    /*輸出錯誤資訊變數*/
    public Frm_RadiologyHistory(Frm_Radiology frame,String p_no) {
        m_Pno = p_no;
        m_LabFrm = frame;
        initComponents();
        init();
    }

    public Frm_RadiologyHistory(Frm_DiagnosisInfo frame,String p_no) {
        m_Pno = p_no;
        m_DiaFrm = frame;
        initComponents();
        init();
    }

     public Frm_RadiologyHistory(Frm_PatientMod frame,String p_no) {
        m_Pno = p_no;
        m_PatNodFrm = frame;
        initComponents();
        init();
    }

    private void init(){

        this.setExtendedState(Frm_Radiology.MAXIMIZED_BOTH);  // 最大化
        this.setLocationRelativeTo(this);//視窗顯示至中
        this.tab_Prescription.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);     // tabble不可按住多選
        addWindowListener(new WindowAdapter() {                                // 畫面關閉原視窗enable
            @Override
            public void windowClosing(WindowEvent windowevent) {
                btn_CloseActionPerformed(null);
            }
        });
        setTabRecord();
    }

       // 加入看診紀錄
    public void setTabRecord() {
        ResultSet rsRecord = null;

        try {
           Object policlinic = null;
      
           policlinic = "%";

            String sqlRecord =
                    "SELECT shift_table.shift_date AS 'Date', " +
                           "CASE shift_table.shift " +
                           "WHEN '1' THEN 'Morning' " +
                           "WHEN '2' THEN 'Noon' " +
                           "WHEN '3' THEN 'Night' " +
                           "WHEN '4' THEN 'All Night' END 'Shift', " +
                           "policlinic.name AS 'Dep.', " +
                           "concat(staff_info.firstname,'  ',staff_info.lastname) AS 'Name', " +
                           "A.guid " +
                    "FROM registration_info AS A, shift_table, policlinic, poli_room, staff_info " +
                    "WHERE A.p_no = '"+m_Pno+"' " +
                    "AND policlinic.name LIKE '"+policlinic+"' " +
                    "AND A.shift_guid = shift_table.guid " +
                    "AND shift_table.room_guid = poli_room.guid " +
                    "AND poli_room.poli_guid = policlinic.guid " +
                    "AND staff_info.s_id = shift_table.s_id " +
                    "AND A.finish = 'F' " +
                    "AND (SELECT COUNT(prescription.code) " +
                            "FROM prescription,outpatient_services,registration_info,prescription_code " +
                            "WHERE (prescription.finish <> 'F' OR prescription.finish IS  NULL ) " +
                            "AND outpatient_services.reg_guid = registration_info.guid " +
                            "AND prescription.code = prescription_code.code "+
                            "AND prescription.os_guid = outpatient_services.guid " +
                            "AND registration_info.guid = A.guid)  > 0 "+
                    "ORDER BY A.reg_time DESC";

            rsRecord = DBC.executeQuery(sqlRecord);
            if (rsRecord.next()) {
                this.tab_Record.setModel(HISModel.getModel(rsRecord, true));
                TabTools.setHideColumn(tab_Record,5);
            } else {
                tab_Record.setModel(getModle(new String[]{"Message"},new String[][]{{"No Information."}}));
            }


             //設定寬度與最小寬度
            if (tab_Record.getColumnCount() == 6 ) {
                TableColumn columnNo = this.tab_Record.getColumnModel().getColumn(0);
                TableColumn columnDate = this.tab_Record.getColumnModel().getColumn(1);
                TableColumn columnShift = this.tab_Record.getColumnModel().getColumn(2);
                TableColumn columnPoli = this.tab_Record.getColumnModel().getColumn(3);
                TableColumn columnDoctor = this.tab_Record.getColumnModel().getColumn(4);

                columnNo.setMaxWidth(30);
                columnDate.setPreferredWidth(110);
                columnShift.setPreferredWidth(80);
                columnPoli.setPreferredWidth(130);
                columnDoctor.setPreferredWidth(143);
            }
            tab_Record.setRowHeight(30);
        } catch (SQLException e) {
            Logger.getLogger(Frm_RadiologyHistory.class.getName()).log(Level.SEVERE, null, e);

        }
    }

     

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lab_NameOfTest = new javax.swing.JLabel();
        lab_DateOfTest = new javax.swing.JLabel();
        lab_ResultsCollectionDate = new javax.swing.JLabel();
        txt_TestName = new javax.swing.JTextField();
        lab_ResultsCollectionDate1 = new javax.swing.JLabel();
        pan_Summary = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txt_Result = new javax.swing.JTextArea();
        date_Test = new javax.swing.JTextField();
        date_Res = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txt_Normal = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        span_Prescription = new javax.swing.JScrollPane();
        tab_Prescription = new javax.swing.JTable();
        btn_Close = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        span_Record = new javax.swing.JScrollPane();
        tab_Record = new javax.swing.JTable();
        mnb = new javax.swing.JMenuBar();
        mn_Fiele = new javax.swing.JMenu();
        mnit_Close = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Radiology(X-RAY) Recoard");

        jPanel1.setBackground(new java.awt.Color(247, 250, 247));

        lab_NameOfTest.setText("Name of Test :");

        lab_DateOfTest.setText("Date of Test :");

        lab_ResultsCollectionDate.setText("Date of Results :");

        txt_TestName.setEditable(false);
        txt_TestName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_TestNameKeyReleased(evt);
            }
        });

        lab_ResultsCollectionDate1.setText("Results :");

        txt_Result.setColumns(20);
        txt_Result.setEditable(false);
        txt_Result.setRows(3);
        txt_Result.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txt_ResultMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(txt_Result);

        javax.swing.GroupLayout pan_SummaryLayout = new javax.swing.GroupLayout(pan_Summary);
        pan_Summary.setLayout(pan_SummaryLayout);
        pan_SummaryLayout.setHorizontalGroup(
            pan_SummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 485, Short.MAX_VALUE)
        );
        pan_SummaryLayout.setVerticalGroup(
            pan_SummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
        );

        date_Test.setEditable(false);

        date_Res.setEditable(false);

        jLabel1.setText("Normal :");

        txt_Normal.setEditable(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lab_NameOfTest)
                    .addComponent(lab_DateOfTest)
                    .addComponent(lab_ResultsCollectionDate1)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(pan_Summary, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addComponent(date_Test, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lab_ResultsCollectionDate)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(date_Res, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txt_TestName, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 485, Short.MAX_VALUE))
                        .addGap(211, 211, 211))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txt_Normal, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab_DateOfTest)
                    .addComponent(date_Test, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_ResultsCollectionDate)
                    .addComponent(date_Res, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab_NameOfTest)
                    .addComponent(txt_TestName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(pan_Summary, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txt_Normal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(lab_ResultsCollectionDate1))
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(247, 250, 247));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Prescription"));

        tab_Prescription.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tab_Prescription.setRowHeight(25);
        tab_Prescription.getTableHeader().setReorderingAllowed(false);
        tab_Prescription.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tab_PrescriptionMouseClicked(evt);
            }
        });
        tab_Prescription.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tab_PrescriptionKeyReleased(evt);
            }
        });
        span_Prescription.setViewportView(tab_Prescription);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(span_Prescription, javax.swing.GroupLayout.DEFAULT_SIZE, 752, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(span_Prescription, javax.swing.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE)
                .addContainerGap())
        );

        btn_Close.setText("Close");
        btn_Close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CloseActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(247, 250, 247));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Registration"));

        tab_Record.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "", "", "", "", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tab_Record.setRowHeight(25);
        tab_Record.getTableHeader().setReorderingAllowed(false);
        tab_Record.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tab_RecordMouseClicked(evt);
            }
        });
        tab_Record.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tab_RecordKeyReleased(evt);
            }
        });
        span_Record.setViewportView(tab_Record);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(span_Record, javax.swing.GroupLayout.DEFAULT_SIZE, 762, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(span_Record, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
                .addContainerGap())
        );

        mn_Fiele.setText("File");

        mnit_Close.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0));
        mnit_Close.setText("Close");
        mnit_Close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnit_CloseActionPerformed(evt);
            }
        });
        mn_Fiele.add(mnit_Close);

        mnb.add(mn_Fiele);

        setJMenuBar(mnb);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_Close, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Close)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
   

     /** 設定表單預設模型。*/
    private DefaultTableModel getModle(String[] title,String[][] data){
        return new DefaultTableModel(data,title){
                @Override
                public boolean isCellEditable(int r, int c){
                return false;}};
    }

     public void setTab_Prescription() {

        ResultSet rsPrescription = null;

        try {

            String sqlPrescription =
                "SELECT prescription.guid, " +
                "prescription.code AS 'Code', " +
                "prescription_code.name AS 'Name', " +
                "prescription.result AS 'Result', " +
                   "CASE prescription.isnormal WHEN 1 THEN 'Y' WHEN 0 THEN 'N' ELSE null  END 'Normal', " +
                "shift_table.shift_date AS Date ," +
                "policlinic.name AS 'Dept.', " +
                "concat(staff_info.firstname,'  ',staff_info.lastname) AS Doctor, " +
                "prescription.date_test AS 'Date of Test', " +
                "prescription.date_results AS 'Results Date' " +
                "FROM prescription, outpatient_services, registration_info, " +
                     "prescription_code, shift_table, policlinic,poli_room,staff_info " +
                "WHERE registration_info.guid = '"+m_RegistrationGuid+"' " +
                    "AND registration_info.shift_guid = shift_table.guid " +
                    "AND shift_table.room_guid = poli_room.guid " +
                    "AND poli_room.poli_guid = policlinic.guid " +
                    "AND staff_info.s_id = shift_table.s_id " +
                    "AND prescription.os_guid = outpatient_services.guid " +
                    "AND prescription_code.code = prescription.code " +
                    "AND outpatient_services.reg_guid = registration_info.guid " +
                    "AND prescription_code.type = '"+X_RAY_CODE+"' ";
            rsPrescription = DBC.executeQuery(sqlPrescription);

            if (rsPrescription.next()) {
                this.tab_Prescription.setModel(HISModel.getModel(rsPrescription, true));
                //設定寬度與最小寬度
                TableColumn columnNo = this.tab_Prescription.getColumnModel().getColumn(0);
                TableColumn columnCode = this.tab_Prescription.getColumnModel().getColumn(2);
                TableColumn columnName = this.tab_Prescription.getColumnModel().getColumn(3);
                TableColumn columnDate = this.tab_Prescription.getColumnModel().getColumn(4);
                TableColumn columnDept = this.tab_Prescription.getColumnModel().getColumn(5);
                TableColumn columnDoctor = this.tab_Prescription.getColumnModel().getColumn(6);


                columnNo.setMaxWidth(25);
                columnDate.setPreferredWidth(50);
                columnDept.setPreferredWidth(110);
                columnDoctor.setPreferredWidth(110);
                columnCode.setPreferredWidth(50);
                columnName.setPreferredWidth(270);
                tab_Prescription.setRowHeight(30);
                TabTools.setHideColumn(tab_Prescription, 1);
            } else {
                ((DefaultTableModel) tab_Prescription.getModel()).setRowCount(0);
            }

            
          
        } catch (SQLException e) {
            Logger.getLogger(Frm_Radiology.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {DBC.closeConnection(rsPrescription);}
            catch (SQLException e){

            }
        }
    }

    public void reLoad() {
        this.setEnabled(true);
    }

     public void setOnTop() {
        this.setAlwaysOnTop(true);
    }

     // 將 Summary 資料丟入 txta_Summary
    public void getTxtaSummary(String text) {
        txt_Result.setText(text);
    }

    private void mnit_EnterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnit_EnterActionPerformed

}//GEN-LAST:event_mnit_EnterActionPerformed

    private void mnit_CloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnit_CloseActionPerformed
        btn_CloseActionPerformed(null);
}//GEN-LAST:event_mnit_CloseActionPerformed

    private void tab_PrescriptionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab_PrescriptionMouseClicked

            txt_TestName.setText("");
            txt_Result.setText("");
            date_Test.setText("");
            date_Res.setText("");
            txt_Normal.setText("");

        if (tab_Prescription.getColumnCount() != 1 && this.tab_Prescription.getValueAt(this.tab_Prescription.getSelectedRow(), 1) != null && this.tab_Prescription.getSelectedRow() != -1) {

             if (tab_Prescription.getValueAt(tab_Prescription.getSelectedRow(), 4) != null) {
                txt_Result.setText(tab_Prescription.getValueAt(tab_Prescription.getSelectedRow(), 4).toString());
             }

             if (tab_Prescription.getValueAt(tab_Prescription.getSelectedRow(), 9) != null) {
                date_Test.setText(tab_Prescription.getValueAt(tab_Prescription.getSelectedRow(), 9).toString());
             }

             if (tab_Prescription.getValueAt(tab_Prescription.getSelectedRow(), 10) != null) {
                date_Res.setText(tab_Prescription.getValueAt(tab_Prescription.getSelectedRow(), 10).toString());
             }

             if (tab_Prescription.getValueAt(tab_Prescription.getSelectedRow(), 5) != null) {
                 if (tab_Prescription.getValueAt(tab_Prescription.getSelectedRow(), 5).toString().equals("Y")) {
                    txt_Normal.setText("Normal");
                 } else if (tab_Prescription.getValueAt(tab_Prescription.getSelectedRow(), 5).toString().equals("N")){
                    txt_Normal.setText("Abnormal");
                 }
             }

             txt_TestName.setText(tab_Prescription.getValueAt(tab_Prescription.getSelectedRow(), 2) + " "+ tab_Prescription.getValueAt(tab_Prescription.getSelectedRow(), 3));
             m_TmpGuid = tab_Prescription.getValueAt(tab_Prescription.getSelectedRow(), 1).toString();
        }
    }//GEN-LAST:event_tab_PrescriptionMouseClicked

    private void tab_PrescriptionKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tab_PrescriptionKeyReleased
        tab_PrescriptionMouseClicked(null);
    }//GEN-LAST:event_tab_PrescriptionKeyReleased

    private void txt_ResultMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_ResultMouseClicked
        new Summary(this, txt_Result ,false, pan_Summary ).setVisible(true);
}//GEN-LAST:event_txt_ResultMouseClicked

    private void txt_TestNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_TestNameKeyReleased

}//GEN-LAST:event_txt_TestNameKeyReleased

    private void btn_CloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CloseActionPerformed
  
        if (m_DiaFrm != null) {
             m_DiaFrm.reSetEnable();
        } else if (m_LabFrm != null) {
             m_LabFrm.reLoad();
        } else if (m_PatNodFrm != null) {
            m_PatNodFrm.reSetEnable();
        }

        this.dispose();
    }//GEN-LAST:event_btn_CloseActionPerformed

    private void tab_RecordMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab_RecordMouseClicked
        if (tab_Record.getColumnCount() > 5) {
            m_RegistrationGuid = tab_Record.getValueAt(tab_Record.getSelectedRow(), 5).toString();
        }

          setTab_Prescription();   // set 當次診斷

    }//GEN-LAST:event_tab_RecordMouseClicked

    private void tab_RecordKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tab_RecordKeyReleased
        tab_RecordMouseClicked(null);
}//GEN-LAST:event_tab_RecordKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Close;
    private javax.swing.JTextField date_Res;
    private javax.swing.JTextField date_Test;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lab_DateOfTest;
    private javax.swing.JLabel lab_NameOfTest;
    private javax.swing.JLabel lab_ResultsCollectionDate;
    private javax.swing.JLabel lab_ResultsCollectionDate1;
    private javax.swing.JMenu mn_Fiele;
    private javax.swing.JMenuBar mnb;
    private javax.swing.JMenuItem mnit_Close;
    private javax.swing.JPanel pan_Summary;
    private javax.swing.JScrollPane span_Prescription;
    private javax.swing.JScrollPane span_Record;
    private javax.swing.JTable tab_Prescription;
    private javax.swing.JTable tab_Record;
    private javax.swing.JTextField txt_Normal;
    private javax.swing.JTextArea txt_Result;
    private javax.swing.JTextField txt_TestName;
    // End of variables declaration//GEN-END:variables
}