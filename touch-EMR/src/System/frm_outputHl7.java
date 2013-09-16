/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * frm_outputHl7.java
 *
 * Created on 2010/10/20, 上午 11:24:08
 */
package System;

import cc.johnwu.sql.*;
import java.awt.Frame;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class frm_outputHl7 extends javax.swing.JFrame {
//private string obr =new string[48];

    /** Creates new form frm_outputHl7 */
    private String[] obr = new String[48];
    SimpleDateFormat f;
    String p_number = null;

    public frm_outputHl7() {
        super();
        initComponents();
        init();
    }

    public frm_outputHl7(String i) {
        initComponents();
        this.p_number = i;
        no = Integer.parseInt(i);
        init();
    }
    ResultSet rs;
    private int no = 0;

    private void init() {
        //
        obr[0] = "OBR";
        obr[1] = String.valueOf(no);
        No.setText(obr[1]);

        //檢體行為代碼
        this.setExtendedState(frm_outputHl7.MAXIMIZED_BOTH);
        try {
            String sql = "SELECT value,descrition FROM hls_specimen_action";
            rs = DBC.executeQuery(sql);
            while (rs.next()) {
                this.SpecimenActionCode.addItem(rs.getString("value") + " - " + rs.getString("descrition"));
            }
            //檢體來源

            sql = "SELECT value,descrition FROM hls_specimen_source";
            rs = DBC.executeQuery(sql);
            while (rs.next()) {
                this.SpecimenSource.addItem(rs.getString("value") + " - " + rs.getString("descrition"));
            }
            sql = "SELECT value,descrition FROM diagnostic_unit";
            rs = DBC.executeQuery(sql);
            while (rs.next()) {
                this.diagnostic_unit.addItem(rs.getString("value") + " - " + rs.getString("descrition"));
            }
            sql = "SELECT value,descrition FROM charge_type";
            rs = DBC.executeQuery(sql);
            while (rs.next()) {
                this.resultstatus.addItem(rs.getString("value") + " - " + rs.getString("descrition"));
            }
            sql = "SELECT value,descrition FROM shipping_method";
            rs = DBC.executeQuery(sql);
            while (rs.next()) {
                this.transportation.addItem(rs.getString("value") + " - " + rs.getString("descrition"));
            }
            sql = "SELECT value,descrition FROM transport_arranged";
            rs = DBC.executeQuery(sql);
            while (rs.next()) {
                this.TransportArranged.addItem(rs.getString("value") + " - " + rs.getString("descrition"));
            }
            sql = "SELECT value,descrition FROM escort_required";
            rs = DBC.executeQuery(sql);
            while (rs.next()) {
                this.EscortRequired.addItem(rs.getString("value") + " - " + rs.getString("descrition"));
            }
            sql = "SELECT code , name FROM prescription_code";
            rs = DBC.executeQuery(sql);
            while (rs.next()) {
                this.produre_code.addItem(rs.getString("code") + "       -       " + rs.getString("name"));
            }
            //Placer Supplemental Service Information :
            sql = "SELECT value , descrition FROM Sup_ser_info";
            rs = DBC.executeQuery(sql);
            while (rs.next()) {
                this.PlacerSupplementalServiceInformation.addItem(rs.getString("value") + " - " + rs.getString("descrition"));
                this.FillerSupplementalServiceInformation.addItem(rs.getString("value") + " - " + rs.getString("descrition"));
            }


        } catch (SQLException ex) {
            Logger.getLogger(frm_outputHl7.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dateMethod1 = new cc.johnwu.date.DateMethod();
        messagebox = new javax.swing.JDialog();
        jLabel1 = new javax.swing.JLabel();
        No = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        PlacerOrderNumber = new javax.swing.JTextField();
        FillerOrderNumber = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        UniversalServiceIdentifier = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        Priority = new javax.swing.JSpinner();
        OrderingProvider = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        OrderCallbackPhoneNumber = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        ObservationEndDateTime = new javax.swing.JSpinner();
        jLabel7 = new javax.swing.JLabel();
        RequestedDateTime = new javax.swing.JSpinner();
        jLabel6 = new javax.swing.JLabel();
        ObservationDateTime = new javax.swing.JSpinner();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        CollectionVolume = new javax.swing.JSpinner();
        SpecimenReceived = new javax.swing.JSpinner();
        jLabel15 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        CollectorIdentifier = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        SpecimenActionCode = new javax.swing.JComboBox();
        jLabel13 = new javax.swing.JLabel();
        DangerCode = new javax.swing.JComboBox();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        RelevantClinicalInfo = new javax.swing.JTextArea();
        jLabel16 = new javax.swing.JLabel();
        SpecimenSource = new javax.swing.JComboBox();
        jPanel5 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        resultsRptChange = new javax.swing.JSpinner();
        jLabel20 = new javax.swing.JLabel();
        ChargetoPractice = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        diagnostic_unit = new javax.swing.JComboBox();
        resultstatus = new javax.swing.JComboBox();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        parentresult = new javax.swing.JTextArea();
        jPanel6 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        RequestedDateTime1 = new javax.swing.JSpinner();
        jLabel25 = new javax.swing.JLabel();
        ResultCopiesTo = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        Parent = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        transportation = new javax.swing.JComboBox();
        jLabel28 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        ReasonforStudy = new javax.swing.JTextArea();
        jPanel7 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        PrincipalResultInterpreter = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        AssistantResultInterpreter = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        Technician = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        Transcriptionist = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        ScheduledDateTime = new javax.swing.JSpinner();
        jLabel34 = new javax.swing.JLabel();
        NumberofSampleContainers = new javax.swing.JSpinner();
        jLabel35 = new javax.swing.JLabel();
        TransportLogisticsofCollectedSample = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        TransportLogisticsofCollectedSample1 = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        TransportArranged = new javax.swing.JComboBox();
        jLabel37 = new javax.swing.JLabel();
        TransportArrangementResponsibility = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        EscortRequired = new javax.swing.JComboBox();
        jLabel39 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        PlannedPatientTransportComment = new javax.swing.JTextArea();
        jLabel40 = new javax.swing.JLabel();
        produre_code = new javax.swing.JComboBox();
        jLabel41 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        PlacerSupplementalServiceInformation = new javax.swing.JComboBox();
        FillerSupplementalServiceInformation = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();

        javax.swing.GroupLayout messageboxLayout = new javax.swing.GroupLayout(messagebox.getContentPane());
        messagebox.getContentPane().setLayout(messageboxLayout);
        messageboxLayout.setHorizontalGroup(
            messageboxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        messageboxLayout.setVerticalGroup(
            messageboxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Arial", 0, 14));
        jLabel1.setText("No.:");

        No.setFont(new java.awt.Font("Arial", 0, 14));
        No.setText("{xxxx}");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jLabel3.setFont(new java.awt.Font("新細明體", 0, 14));
        jLabel3.setText("Placer Order Number :");

        PlacerOrderNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PlacerOrderNumberActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("新細明體", 0, 14));
        jLabel4.setText("Filler Order Number :");

        jLabel5.setFont(new java.awt.Font("新細明體", 0, 14));
        jLabel5.setText("Universal Service Identifier :");

        jLabel8.setFont(new java.awt.Font("新細明體", 0, 14));
        jLabel8.setText("Priority :");

        Priority.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));

        jLabel17.setFont(new java.awt.Font("新細明體", 0, 14));
        jLabel17.setText("Ordering Provider :");

        jLabel18.setFont(new java.awt.Font("新細明體", 0, 14));
        jLabel18.setText("Order Callback Phone Number :");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18)
                    .addComponent(jLabel17)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(PlacerOrderNumber)
                    .addComponent(FillerOrderNumber)
                    .addComponent(OrderingProvider)
                    .addComponent(OrderCallbackPhoneNumber, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(UniversalServiceIdentifier, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Priority, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel17, jLabel3, jLabel4});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(PlacerOrderNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(UniversalServiceIdentifier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(FillerOrderNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(Priority, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(OrderingProvider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(OrderCallbackPhoneNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        ObservationEndDateTime.setModel(new javax.swing.SpinnerDateModel(new java.util.Date(), null, null, java.util.Calendar.MINUTE));

        jLabel7.setFont(new java.awt.Font("新細明體", 0, 14));
        jLabel7.setText("Observation Date/Time :");

        RequestedDateTime.setModel(new javax.swing.SpinnerDateModel(new java.util.Date(), null, null, java.util.Calendar.HOUR_OF_DAY));

        jLabel6.setFont(new java.awt.Font("新細明體", 0, 14));
        jLabel6.setText("Requested Date/Time :");

        ObservationDateTime.setModel(new javax.swing.SpinnerDateModel(new java.util.Date(), null, null, java.util.Calendar.MINUTE));

        jLabel9.setFont(new java.awt.Font("新細明體", 0, 14));
        jLabel9.setText("Observation End Date/Time :");

        jLabel10.setFont(new java.awt.Font("新細明體", 0, 14));
        jLabel10.setText("Collection Volume :");

        CollectionVolume.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));

        SpecimenReceived.setModel(new javax.swing.SpinnerDateModel(new java.util.Date(), null, null, java.util.Calendar.MINUTE));

        jLabel15.setFont(new java.awt.Font("新細明體", 0, 14));
        jLabel15.setText("Specimen Received Date/Time :");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(CollectionVolume, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15)
                            .addComponent(jLabel9)
                            .addComponent(jLabel7)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ObservationDateTime, javax.swing.GroupLayout.DEFAULT_SIZE, 661, Short.MAX_VALUE)
                            .addComponent(RequestedDateTime, javax.swing.GroupLayout.DEFAULT_SIZE, 661, Short.MAX_VALUE)
                            .addComponent(ObservationEndDateTime, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 661, Short.MAX_VALUE)
                            .addComponent(SpecimenReceived, javax.swing.GroupLayout.DEFAULT_SIZE, 661, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(RequestedDateTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(ObservationDateTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(ObservationEndDateTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(SpecimenReceived, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(CollectionVolume, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel11.setFont(new java.awt.Font("新細明體", 0, 14));
        jLabel11.setText("Collector Identifier :");

        jLabel12.setFont(new java.awt.Font("新細明體", 0, 14));
        jLabel12.setText("Specimen Action Code :");

        SpecimenActionCode.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " " }));

        jLabel13.setFont(new java.awt.Font("新細明體", 0, 14));
        jLabel13.setText("Danger Code :");

        DangerCode.setEditable(true);
        DangerCode.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " ", "Normal", "Serious", " " }));

        jLabel14.setFont(new java.awt.Font("新細明體", 0, 14));
        jLabel14.setText("Relevant Clinical Information :");

        RelevantClinicalInfo.setColumns(20);
        RelevantClinicalInfo.setRows(5);
        jScrollPane2.setViewportView(RelevantClinicalInfo);

        jLabel16.setFont(new java.awt.Font("新細明體", 0, 14));
        jLabel16.setText("Specimen Source :");

        SpecimenSource.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " " }));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 848, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addContainerGap(685, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(SpecimenSource, 0, 734, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(CollectorIdentifier, javax.swing.GroupLayout.DEFAULT_SIZE, 723, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(SpecimenActionCode, 0, 701, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(DangerCode, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(CollectorIdentifier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SpecimenActionCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(DangerCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(SpecimenSource, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel5.setEnabled(false);
        jPanel5.setFocusable(false);

        jLabel19.setFont(new java.awt.Font("新細明體", 0, 14));
        jLabel19.setText("Results Rpt/Status Chng - Date/Time :");

        resultsRptChange.setModel(new javax.swing.SpinnerDateModel(new java.util.Date(), null, null, java.util.Calendar.MINUTE));

        jLabel20.setFont(new java.awt.Font("新細明體", 0, 14));
        jLabel20.setText("Charge to Practice :");

        ChargetoPractice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChargetoPracticeActionPerformed(evt);
            }
        });

        jLabel21.setFont(new java.awt.Font("新細明體", 0, 14));
        jLabel21.setText("Diagnostic Server Sect ID :");

        diagnostic_unit.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " " }));

        resultstatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " " }));

        jLabel22.setFont(new java.awt.Font("新細明體", 0, 14));
        jLabel22.setText("Result Status :");

        jLabel23.setFont(new java.awt.Font("新細明體", 0, 14));
        jLabel23.setText("Parent Result :");

        parentresult.setColumns(20);
        parentresult.setRows(5);
        jScrollPane3.setViewportView(parentresult);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 848, Short.MAX_VALUE)
                    .addComponent(jLabel23)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel21)
                            .addComponent(jLabel22)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel19)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ChargetoPractice, javax.swing.GroupLayout.DEFAULT_SIZE, 632, Short.MAX_VALUE)
                            .addComponent(resultstatus, 0, 632, Short.MAX_VALUE)
                            .addComponent(diagnostic_unit, 0, 632, Short.MAX_VALUE)
                            .addComponent(resultsRptChange, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        jPanel5Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel21, jLabel22});

        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(resultsRptChange, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(ChargetoPractice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(diagnostic_unit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(resultstatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel24.setFont(new java.awt.Font("新細明體", 0, 14));
        jLabel24.setText("Quantity/Timing :");

        RequestedDateTime1.setModel(new javax.swing.SpinnerDateModel(new java.util.Date(), null, null, java.util.Calendar.HOUR_OF_DAY));

        jLabel25.setFont(new java.awt.Font("新細明體", 0, 14));
        jLabel25.setText("Result Copies To :");

        ResultCopiesTo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ResultCopiesToActionPerformed(evt);
            }
        });

        jLabel26.setFont(new java.awt.Font("新細明體", 0, 14));
        jLabel26.setText("Parent :");

        jLabel27.setFont(new java.awt.Font("新細明體", 0, 14));
        jLabel27.setText("Transportation Mode :");

        transportation.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " " }));

        jLabel28.setFont(new java.awt.Font("新細明體", 0, 14));
        jLabel28.setText("Reason for Study :");

        ReasonforStudy.setColumns(20);
        ReasonforStudy.setRows(5);
        jScrollPane4.setViewportView(ReasonforStudy);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 848, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel24)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(RequestedDateTime1, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel25)
                                    .addComponent(jLabel26))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Parent, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
                                    .addComponent(ResultCopiesTo))))
                        .addGap(41, 41, 41)
                        .addComponent(jLabel27)
                        .addGap(18, 18, 18)
                        .addComponent(transportation, 0, 423, Short.MAX_VALUE))
                    .addComponent(jLabel28, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap())
        );

        jPanel6Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel24, jLabel25});

        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(RequestedDateTime1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(transportation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(ResultCopiesTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(Parent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel28)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel25, jLabel26});

        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel29.setFont(new java.awt.Font("新細明體", 0, 14));
        jLabel29.setText("Principal Result Interpreter :");

        PrincipalResultInterpreter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PrincipalResultInterpreterActionPerformed(evt);
            }
        });

        jLabel30.setFont(new java.awt.Font("新細明體", 0, 14));
        jLabel30.setText("Assistant Result Interpreter :");

        jLabel31.setFont(new java.awt.Font("新細明體", 0, 14));
        jLabel31.setText("Technician :");

        jLabel32.setFont(new java.awt.Font("新細明體", 0, 14));
        jLabel32.setText("Transcriptionist :");

        jLabel33.setFont(new java.awt.Font("新細明體", 0, 14));
        jLabel33.setText("Scheduled Date/Time :");

        ScheduledDateTime.setModel(new javax.swing.SpinnerDateModel(new java.util.Date(), null, null, java.util.Calendar.MINUTE));

        jLabel34.setFont(new java.awt.Font("新細明體", 0, 14));
        jLabel34.setText("Number of Sample Containers :");

        NumberofSampleContainers.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));

        jLabel35.setFont(new java.awt.Font("新細明體", 0, 14));
        jLabel35.setText("Transport Logistics of Collected Sample:");

        jLabel36.setFont(new java.awt.Font("新細明體", 0, 14));
        jLabel36.setText("Collectors Comment :");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel7Layout.createSequentialGroup()
                            .addComponent(jLabel29)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(PrincipalResultInterpreter))
                        .addGroup(jPanel7Layout.createSequentialGroup()
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(Technician)
                                .addComponent(AssistantResultInterpreter, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)
                                .addComponent(Transcriptionist, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE))))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE)
                        .addGap(154, 154, 154)))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE)
                            .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ScheduledDateTime, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(NumberofSampleContainers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel35)
                            .addComponent(jLabel36))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TransportLogisticsofCollectedSample1, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
                            .addComponent(TransportLogisticsofCollectedSample, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE))))
                .addContainerGap())
        );

        jPanel7Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel33, jLabel34, jLabel35, jLabel36});

        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(PrincipalResultInterpreter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ScheduledDateTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel33))
                .addGap(7, 7, 7)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(AssistantResultInterpreter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel34)
                    .addComponent(NumberofSampleContainers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31)
                    .addComponent(Technician, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel35)
                    .addComponent(TransportLogisticsofCollectedSample, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel32)
                        .addComponent(Transcriptionist, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel36)
                        .addComponent(TransportLogisticsofCollectedSample1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setFont(new java.awt.Font("新細明體", 0, 14));
        jLabel2.setText("Transport Arrangement Responsibility :");

        TransportArranged.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " " }));
        TransportArranged.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TransportArrangedActionPerformed(evt);
            }
        });

        jLabel37.setFont(new java.awt.Font("新細明體", 0, 14));
        jLabel37.setText("Transport Arranged :");

        jLabel38.setFont(new java.awt.Font("新細明體", 0, 14));
        jLabel38.setText("Escort Required :");

        EscortRequired.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " " }));

        jLabel39.setFont(new java.awt.Font("新細明體", 0, 14));
        jLabel39.setText("Planned Patient Transport Comment :");

        PlannedPatientTransportComment.setColumns(20);
        PlannedPatientTransportComment.setRows(5);
        jScrollPane5.setViewportView(PlannedPatientTransportComment);

        jLabel40.setFont(new java.awt.Font("新細明體", 0, 14));
        jLabel40.setText("Procedure Code :");

        produre_code.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " " }));

        jLabel41.setFont(new java.awt.Font("新細明體", 0, 14));
        jLabel41.setText("Procedure Code Modifier :");

        jLabel42.setFont(new java.awt.Font("新細明體", 0, 14));
        jLabel42.setText("Placer Supplemental Service Information :");

        jLabel43.setFont(new java.awt.Font("新細明體", 0, 14));
        jLabel43.setText("Filler Supplemental Service Information :");

        PlacerSupplementalServiceInformation.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "" }));

        FillerSupplementalServiceInformation.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " " }));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 848, Short.MAX_VALUE)
                    .addComponent(jLabel39)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel37)
                                    .addComponent(jLabel38))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(EscortRequired, 0, 143, Short.MAX_VALUE)
                                    .addComponent(TransportArranged, 0, 143, Short.MAX_VALUE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TransportArrangementResponsibility, javax.swing.GroupLayout.DEFAULT_SIZE, 581, Short.MAX_VALUE)
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(jPanel8Layout.createSequentialGroup()
                                    .addComponent(jLabel41)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTextField2))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel8Layout.createSequentialGroup()
                                    .addComponent(jLabel40)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(produre_code, javax.swing.GroupLayout.PREFERRED_SIZE, 482, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel42)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(PlacerSupplementalServiceInformation, 0, 612, Short.MAX_VALUE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel43)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(FillerSupplementalServiceInformation, javax.swing.GroupLayout.PREFERRED_SIZE, 612, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jPanel8Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel42, jLabel43});

        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(TransportArrangementResponsibility, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel37)
                    .addComponent(TransportArranged, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel40)
                    .addComponent(produre_code, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel38)
                    .addComponent(EscortRequired, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel41)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel39)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel42)
                    .addComponent(PlacerSupplementalServiceInformation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel43)
                    .addComponent(FillerSupplementalServiceInformation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane1.setViewportView(jPanel1);

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setFont(new java.awt.Font("Arial Unicode MS", 0, 14));
        jButton1.setText("Send");
        jButton1.setBorder(null);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 901, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(No))
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(No))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 730, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void PlacerOrderNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PlacerOrderNumberActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PlacerOrderNumberActionPerformed

    private void ChargetoPracticeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChargetoPracticeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ChargetoPracticeActionPerformed

    private void ResultCopiesToActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ResultCopiesToActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ResultCopiesToActionPerformed

    private void PrincipalResultInterpreterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PrincipalResultInterpreterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PrincipalResultInterpreterActionPerformed

    private void TransportArrangedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TransportArrangedActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TransportArrangedActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        {
            PrintWriter printWriter = null;
            try {
                no++;
                info();
//                System.out.print("執行到這");
                HL7output E = new HL7output();
                E.OBR = obr;
                printWriter = new PrintWriter(new File(".\\HL7_PatientNo_"+No.getText()+".txt"));

                String p = E.printHl7(p_number);

                if(p_number.trim().equals(p.trim()) || p.trim().equals("")){
                    printWriter.println("No Message.");

                }else{
                    printWriter.println(p);
              
                }
               
                //parentresult.setText(E.printHl7(p_number));
                printWriter.close();
                //System.out.print(E.printHl7(p_number));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(frm_outputHl7.class.getName()).log(Level.SEVERE, null, ex);
            
            } finally {
                printWriter.close();
                JOptionPane.showMessageDialog(new Frame(), ".\\HL7_PatientNo_"+No.getText()+".txt  " + "Output Finish.");
            }
        }




    }//GEN-LAST:event_jButton1ActionPerformed
    //將表格欄位資料塞入OBR陣列的資料

    private void info() {
        f = new SimpleDateFormat("yyyyMMddkkmmss");

        obr[2] = PlacerOrderNumber.getText();
        obr[3] = FillerOrderNumber.getText();
        obr[4] = UniversalServiceIdentifier.getText();
        obr[5] = Priority.getValue().toString();
        obr[6] = f.format(RequestedDateTime.getValue());
        obr[7] = f.format(ObservationDateTime.getValue());
        obr[8] = f.format(ObservationEndDateTime.getValue());
        obr[9] = CollectionVolume.getValue().toString();
        obr[10] = CollectorIdentifier.getText();
        if (!SpecimenActionCode.getSelectedItem().toString().equals(" ")) {
            obr[11] = SpecimenActionCode.getSelectedItem().toString().substring(0, 1);
        } else {
            obr[11] = "";
        }
        obr[12] = DangerCode.getSelectedItem().toString();
        obr[13] = RelevantClinicalInfo.getText();
        obr[14] = f.format(SpecimenReceived.getValue());
        if (!SpecimenSource.getSelectedItem().toString().equals(" ")) {
            obr[15] = SpecimenSource.getSelectedItem().toString().substring(0, 4);
        } else {
            obr[15] = "";
        }
        obr[16] = OrderingProvider.getText();
        obr[17] = OrderCallbackPhoneNumber.getText();
        obr[18] = PlacerOrderNumber.getText();  //
        obr[19] = PlacerOrderNumber.getText();
        obr[20] = FillerOrderNumber.getText();
        obr[21] = FillerOrderNumber.getText();
        obr[22] = f.format(resultsRptChange.getValue());
        obr[23] = ChargetoPractice.getText();
        if (!diagnostic_unit.getSelectedItem().toString().equals(" ")) {
            obr[24] = diagnostic_unit.getSelectedItem().toString().substring(0, 3);
        } else {
            obr[24] = "";
        }
        if (!resultstatus.getSelectedItem().toString().equals(" ")) {
            obr[25] = resultstatus.getSelectedItem().toString().substring(0, 1);
        } else {
            obr[25] = "";
        }
        obr[26] = parentresult.getText();
        obr[27] = f.format(RequestedDateTime1.getValue());
        obr[28] = ResultCopiesTo.getText();
        obr[29] = Parent.getText();
        if (!transportation.getSelectedItem().toString().equals(" ")) {
            obr[30] = transportation.getSelectedItem().toString().substring(0, 4);
        } else {
            obr[30] = "";
        }
        obr[31] = ReasonforStudy.getText();
        obr[32] = PrincipalResultInterpreter.getText();
        obr[33] = AssistantResultInterpreter.getText();
        obr[34] = Technician.getText();
        obr[35] = Transcriptionist.getText();
        obr[36] = f.format(ScheduledDateTime.getValue());
        obr[37] = NumberofSampleContainers.getValue().toString();
        obr[38] = TransportLogisticsofCollectedSample.getText();
        obr[39] = TransportLogisticsofCollectedSample1.getText();
        obr[40] = TransportArrangementResponsibility.getText();
        if (!TransportArranged.getSelectedItem().toString().equals(" ")) {
            obr[41] = TransportArranged.getSelectedItem().toString().substring(0, 1);
        } else {
            obr[41] = "";
        }
        if (!EscortRequired.getSelectedItem().toString().equals(" ")) {
            obr[42] = EscortRequired.getSelectedItem().toString().substring(0, 1);
        } else {
            obr[42] = "";
        }
        obr[43] = PlannedPatientTransportComment.getText();
        if (!produre_code.getSelectedItem().toString().equals(" ")) {
            obr[44] = produre_code.getSelectedItem().toString().substring(0, 8);
        } else {
            obr[44] = "";
        }
        if (!PlacerSupplementalServiceInformation.getSelectedItem().toString().equals("")) {
            obr[45] = PlacerSupplementalServiceInformation.getSelectedItem().toString().substring(0, 3);
           
        } else {
            obr[45] = "";
        }
        if (!FillerSupplementalServiceInformation.getSelectedItem().toString().equals(" ")) {
            obr[46] = FillerSupplementalServiceInformation.getSelectedItem().toString().substring(0, 3);
        } else {
            obr[46] = "";
        }
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new frm_outputHl7().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField AssistantResultInterpreter;
    private javax.swing.JTextField ChargetoPractice;
    private javax.swing.JSpinner CollectionVolume;
    private javax.swing.JTextField CollectorIdentifier;
    private javax.swing.JComboBox DangerCode;
    private javax.swing.JComboBox EscortRequired;
    private javax.swing.JTextField FillerOrderNumber;
    private javax.swing.JComboBox FillerSupplementalServiceInformation;
    private javax.swing.JLabel No;
    private javax.swing.JSpinner NumberofSampleContainers;
    private javax.swing.JSpinner ObservationDateTime;
    private javax.swing.JSpinner ObservationEndDateTime;
    private javax.swing.JTextField OrderCallbackPhoneNumber;
    private javax.swing.JTextField OrderingProvider;
    private javax.swing.JTextField Parent;
    private javax.swing.JTextField PlacerOrderNumber;
    private javax.swing.JComboBox PlacerSupplementalServiceInformation;
    private javax.swing.JTextArea PlannedPatientTransportComment;
    private javax.swing.JTextField PrincipalResultInterpreter;
    private javax.swing.JSpinner Priority;
    private javax.swing.JTextArea ReasonforStudy;
    private javax.swing.JTextArea RelevantClinicalInfo;
    private javax.swing.JSpinner RequestedDateTime;
    private javax.swing.JSpinner RequestedDateTime1;
    private javax.swing.JTextField ResultCopiesTo;
    private javax.swing.JSpinner ScheduledDateTime;
    private javax.swing.JComboBox SpecimenActionCode;
    private javax.swing.JSpinner SpecimenReceived;
    private javax.swing.JComboBox SpecimenSource;
    private javax.swing.JTextField Technician;
    private javax.swing.JTextField Transcriptionist;
    private javax.swing.JComboBox TransportArranged;
    private javax.swing.JTextField TransportArrangementResponsibility;
    private javax.swing.JTextField TransportLogisticsofCollectedSample;
    private javax.swing.JTextField TransportLogisticsofCollectedSample1;
    private javax.swing.JTextField UniversalServiceIdentifier;
    private cc.johnwu.date.DateMethod dateMethod1;
    private javax.swing.JComboBox diagnostic_unit;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JDialog messagebox;
    private javax.swing.JTextArea parentresult;
    private javax.swing.JComboBox produre_code;
    private javax.swing.JSpinner resultsRptChange;
    private javax.swing.JComboBox resultstatus;
    private javax.swing.JComboBox transportation;
    // End of variables declaration//GEN-END:variables
}
