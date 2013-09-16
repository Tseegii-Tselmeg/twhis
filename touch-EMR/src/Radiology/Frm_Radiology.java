package Radiology;

import Common.Constant;
import Common.PrintTools;
import Diagnosis.Summary;
import Multilingual.language;
import Common.TabTools;
import WorkList.Frm_WorkList;
import cc.johnwu.date.DateMethod;
import cc.johnwu.login.UserInfo;
import cc.johnwu.sql.DBC;
import cc.johnwu.sql.HISModel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author steven
 */
public class Frm_Radiology extends javax.swing.JFrame {
    private String m_Pno;
    private String m_RegistrationGuid;  // registration guid
    private boolean m_FinishState;      //
    private int m_WorkListRowNo;        // Frm_DiagnosisWorkList's stop rowNo
    private String m_TmpGuid;   // 暫存Gued
    
    
     /*多國語言變數*/
    private language paragraph = new language();
    private String[] line = paragraph.setlanguage("LABORATORY").split("\n") ;
    private String[] message = paragraph.setlanguage("MESSAGE").split("\n") ;
    /*輸出錯誤資訊變數*/
    public Frm_Radiology(String p_no,String regGuid, int stopRowNo, boolean finishState) {
        this.m_Pno=p_no;
        this.m_RegistrationGuid = regGuid;
        this.m_WorkListRowNo = stopRowNo;
        this.m_FinishState = finishState;

        initComponents();
        init();
        initLanguage();
    }

    private void initLanguage() {
        //this.lab_DateOfTest.setText(paragraph.getLanguage(line, "DATEOfTEST"));
        this.lab_NameOfTest.setText(paragraph.getLanguage(line, "NAMEOfTEST"));
        //this.lab_ResultsCollectionDate.setText(paragraph.getLanguage(line, "RESYLTSCOLLECTIONDATE"));
        //this.btn_Close.setText(paragraph.getLanguage(message, "CLOSE"));
        this.setTitle("Radiology(X-RAY)");
    }

    public void setOnTop() {
        this.setAlwaysOnTop(true);
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
        
        String sql = "SELECT * FROM patients_info WHERE p_no = "+m_Pno+"";
        try {
            ResultSet rs = DBC.executeQuery(sql);
            rs.next();
            // 取出病患基本資料
            this.txt_No.setText(rs.getString("p_no"));
            this.txt_Name.setText(rs.getString("firstname")+" "+rs.getString("lastname"));
            this.txt_Sex.setText(rs.getString("gender"));
            this.txt_Age.setText(DateMethod.getAgeWithMonth(rs.getDate("birth")));
            this.txt_Bloodtype.setText(rs.getString("bloodtype") + " " + rs.getString("rh_type"));
            this.txt_Height.setText(rs.getString("height"));
            this.txt_Weight.setText(rs.getString("weight"));
            this.txt_Ps.setText(rs.getString("ps"));

            setTab_Prescription();


        } catch (SQLException ex) {
            Logger.getLogger(Frm_Radiology.class.getName()).log(Level.SEVERE, null, ex);
        }

    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lab_NameOfTest = new javax.swing.JLabel();
        txt_TestName = new javax.swing.JTextField();
        lab_ResultsCollectionDate1 = new javax.swing.JLabel();
        pan_Summary = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txt_Result = new javax.swing.JTextArea();
        radiobtn_Normal = new javax.swing.JRadioButton();
        radiobtn_Unnormal = new javax.swing.JRadioButton();
        btn_add = new javax.swing.JButton();
        txt_SpecimenTest = new javax.swing.JTextField();
        txt_SpecimenResult = new javax.swing.JTextField();
        lab_ResultsCollectionDate2 = new javax.swing.JLabel();
        ckbox_Test = new javax.swing.JCheckBox();
        ckbox_Result = new javax.swing.JCheckBox();
        lab_DateOfTest1 = new javax.swing.JLabel();
        pan_Top = new javax.swing.JPanel();
        lab_TitleNo = new javax.swing.JLabel();
        lab_TitleName = new javax.swing.JLabel();
        lab_TitlePs = new javax.swing.JLabel();
        lab_TitleSex = new javax.swing.JLabel();
        lab_TitleAge = new javax.swing.JLabel();
        lab_TitleBloodtype = new javax.swing.JLabel();
        txt_No = new javax.swing.JTextField();
        txt_Age = new javax.swing.JTextField();
        txt_Bloodtype = new javax.swing.JTextField();
        txt_Name = new javax.swing.JTextField();
        txt_Sex = new javax.swing.JTextField();
        txt_Ps = new javax.swing.JTextField();
        lab_Height = new javax.swing.JLabel();
        lab_Weight = new javax.swing.JLabel();
        txt_Height = new javax.swing.JTextField();
        txt_Weight = new javax.swing.JTextField();
        btn_Finish = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        span_Prescription = new javax.swing.JScrollPane();
        tab_Prescription = new javax.swing.JTable();
        btn_Close = new javax.swing.JButton();
        mnb = new javax.swing.JMenuBar();
        mn_Fiele = new javax.swing.JMenu();
        mnit_XRayRecord = new javax.swing.JMenuItem();
        mnit_Close = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Radiology(X-RAY)");

        jPanel1.setBackground(new java.awt.Color(247, 250, 247));

        lab_NameOfTest.setText("Name of Test :");

        txt_TestName.setEditable(false);
        txt_TestName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_TestNameKeyReleased(evt);
            }
        });

        lab_ResultsCollectionDate1.setText("Results :");

        txt_Result.setColumns(20);
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
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 674, Short.MAX_VALUE)
        );
        pan_SummaryLayout.setVerticalGroup(
            pan_SummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE)
        );

        radiobtn_Normal.setBackground(new java.awt.Color(247, 250, 247));
        radiobtn_Normal.setText("Normal");
        radiobtn_Normal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radiobtn_NormalActionPerformed(evt);
            }
        });

        radiobtn_Unnormal.setBackground(new java.awt.Color(247, 250, 247));
        radiobtn_Unnormal.setText("Abnormal");
        radiobtn_Unnormal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radiobtn_UnnormalActionPerformed(evt);
            }
        });

        btn_add.setText("Save");
        btn_add.setEnabled(false);
        btn_add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_addActionPerformed(evt);
            }
        });

        txt_SpecimenTest.setEditable(false);

        txt_SpecimenResult.setEditable(false);

        lab_ResultsCollectionDate2.setText("Date of Results :");

        ckbox_Test.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ckbox_TestMouseClicked(evt);
            }
        });

        ckbox_Result.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ckbox_ResultMouseClicked(evt);
            }
        });

        lab_DateOfTest1.setText("Date of Test :");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lab_NameOfTest, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lab_ResultsCollectionDate2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lab_DateOfTest1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lab_ResultsCollectionDate1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(ckbox_Test)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txt_SpecimenTest, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(ckbox_Result)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txt_SpecimenResult, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(475, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addComponent(radiobtn_Normal)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(radiobtn_Unnormal)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 456, Short.MAX_VALUE)
                                .addComponent(btn_add, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txt_TestName, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 674, Short.MAX_VALUE)
                            .addComponent(pan_Summary, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab_NameOfTest)
                    .addComponent(txt_TestName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(ckbox_Test)
                    .addComponent(lab_DateOfTest1)
                    .addComponent(txt_SpecimenTest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lab_ResultsCollectionDate2)
                    .addComponent(ckbox_Result)
                    .addComponent(txt_SpecimenResult, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lab_ResultsCollectionDate1)
                    .addComponent(pan_Summary, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(radiobtn_Normal)
                            .addComponent(radiobtn_Unnormal)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_add)))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        pan_Top.setBackground(new java.awt.Color(247, 250, 247));
        pan_Top.setFont(new java.awt.Font("Bitstream Vera Sans", 1, 18));

        lab_TitleNo.setText("Patient No.");

        lab_TitleName.setText("Name");

        lab_TitlePs.setText("p.s.");

        lab_TitleSex.setText("Gender");

        lab_TitleAge.setText("Age");

        lab_TitleBloodtype.setText("Blood type");

        txt_No.setEditable(false);

        txt_Age.setEditable(false);

        txt_Bloodtype.setEditable(false);

        txt_Name.setEditable(false);

        txt_Sex.setEditable(false);

        txt_Ps.setEditable(false);

        lab_Height.setText("Height");

        lab_Weight.setText("Weight");

        txt_Height.setEditable(false);

        txt_Weight.setEditable(false);

        javax.swing.GroupLayout pan_TopLayout = new javax.swing.GroupLayout(pan_Top);
        pan_Top.setLayout(pan_TopLayout);
        pan_TopLayout.setHorizontalGroup(
            pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_TopLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(lab_TitleName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lab_TitleNo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txt_No, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
                    .addComponent(txt_Name, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(lab_TitleAge, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lab_TitleSex, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt_Age)
                    .addComponent(txt_Sex, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(lab_TitlePs, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lab_Height, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan_TopLayout.createSequentialGroup()
                        .addComponent(txt_Height, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lab_Weight)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_Weight, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lab_TitleBloodtype)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_Bloodtype, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txt_Ps, javax.swing.GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE))
                .addContainerGap())
        );
        pan_TopLayout.setVerticalGroup(
            pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_TopLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab_TitleSex)
                    .addComponent(txt_Sex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_No, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_Weight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_Weight)
                    .addComponent(lab_Height)
                    .addComponent(txt_Height, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_TitleBloodtype)
                    .addComponent(txt_Bloodtype, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_TitleNo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_Name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_TitleAge)
                    .addComponent(txt_Age, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_Ps, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_TitleName)
                    .addComponent(lab_TitlePs))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btn_Finish.setText("Finish All");
        btn_Finish.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_FinishActionPerformed(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(247, 250, 247));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Prescription"));

        tab_Prescription.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null}
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
                .addComponent(span_Prescription, javax.swing.GroupLayout.DEFAULT_SIZE, 748, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(span_Prescription, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
                .addContainerGap())
        );

        btn_Close.setText("Close");
        btn_Close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CloseActionPerformed(evt);
            }
        });

        mn_Fiele.setText("File");

        mnit_XRayRecord.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, java.awt.event.InputEvent.CTRL_MASK));
        mnit_XRayRecord.setText("Radiology(X-RAY) Recoard");
        mnit_XRayRecord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnit_XRayRecordActionPerformed(evt);
            }
        });
        mn_Fiele.add(mnit_XRayRecord);

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
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pan_Top, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btn_Finish, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_Close, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pan_Top, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_Close)
                    .addComponent(btn_Finish))
                .addGap(10, 10, 10))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void btn_FinishActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_FinishActionPerformed

//         try {
//             boolean iscanFinish = false;
//             for (int i = 0; i < tab_Prescription.getRowCount(); i++) {
//                if (tab_Prescription.getValueAt(i, 1) == null || tab_Prescription.getValueAt(i, 5) == null || tab_Prescription.getValueAt(i, 6) == null ) {
//                    iscanFinish = false;
//
//                    JOptionPane.showMessageDialog(null,"Row "+ (i+1) +" is outstanding.");
//                    break;
//                }
//                else
//                {
//                    iscanFinish = true;
//                }
//             }
//             if (iscanFinish) {
//                   for (int i = 0; i < tab_Prescription.getRowCount(); i++) {
//                            String sql = "UPDATE prescription SET finish = 'F' WHERE guid = '" + tab_Prescription.getValueAt(i, 1).toString() + "'";
//                            DBC.executeUpdate(sql);
//                    }
//                   JOptionPane.showMessageDialog(null,"Saved successfully");
//                    PrinterJob pj = PrinterJob.getPrinterJob();
//                    PageFormat pf = pj.defaultPage();
//                    Paper paper = new Paper();
//                    pf.setPaper(paper);
//                    pj.setPrintable(new MyPrintable(), pf);
//                   // if (pj.printDialog())
//
//                    try {
//                        pj.print();
//                    } catch (PrinterException ex) {
//                        Logger.getLogger(Frm_Radiology.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                     if (tab_Prescription.getRowCount() == 0) {
//                        if (m_WorkListRowNo -1 == -1) {m_WorkListRowNo = 0;}
//                        else {m_WorkListRowNo--;}
//                    }
//                    new Frm_WorkList(m_WorkListRowNo,"xray").setVisible(true);
//                    this.dispose();
//              }
//        } catch (SQLException ex) {
//            Logger.getLogger(Frm_Radiology.class.getName()).log(Level.SEVERE, null, ex);
//        }

       try {
             boolean iscanFinish = false;
             for (int i = 0; i < tab_Prescription.getRowCount(); i++) {
                if (tab_Prescription.getValueAt(i, 1) == null || tab_Prescription.getValueAt(i, 5) == null || tab_Prescription.getValueAt(i, 6) == null || tab_Prescription.getValueAt(i, 10) == null) {
                    iscanFinish = false;

                    Object[] options = {"YES", "NO"};
                        int dialog = JOptionPane.showOptionDialog(
                            new Frame(),
                            "Some Results are Pending, do you want to finish them?",
                            "Message",
                            JOptionPane.YES_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options,
                            options[0]);
                        if (dialog == 0) {
                             iscanFinish = true;
                        } else {
                            //選擇 NO 時
                        }
                    break;
                }
                else
                {
                    iscanFinish = true;
                }
             }
             if (iscanFinish) {
                   for (int i = 0; i < tab_Prescription.getRowCount(); i++) {
                            String sql = "UPDATE prescription SET finish = 'F' WHERE guid = '" + tab_Prescription.getValueAt(i, 1).toString() + "'";
                            DBC.executeUpdate(sql);
                    }
                   JOptionPane.showMessageDialog(null,"Saved successfully.");

                     PrintTools pt = new PrintTools();
                     pt.DoPrint(5, m_RegistrationGuid);


                     if (tab_Prescription.getRowCount() == 0) {
                        if (m_WorkListRowNo -1 == -1) {m_WorkListRowNo = 0;}
                        else {m_WorkListRowNo--;}
                    }
                    new Frm_WorkList(m_WorkListRowNo,"lab").setVisible(true);
                    this.dispose();
              }
        } catch (SQLException ex) {
            Logger.getLogger(Frm_Radiology.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_FinishActionPerformed


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
                "prescription.date_results AS 'Date of Results', " +
                "prescription.specimen_status AS 'Specimen status', " +
                "prescription.specimen_received " +
                "FROM prescription LEFT JOIN outpatient_services ON prescription.os_guid = outpatient_services.guid, registration_info, " +
                     "prescription_code, shift_table, policlinic,poli_room,staff_info " +
                "WHERE registration_info.guid = '"+m_RegistrationGuid+"' " +
                    "AND registration_info.shift_guid = shift_table.guid " +
                    "AND shift_table.room_guid = poli_room.guid " +
                    "AND poli_room.poli_guid = policlinic.guid " +
                    "AND staff_info.s_id = shift_table.s_id " +
                    "AND  (outpatient_services.reg_guid = registration_info.guid " +
                    "OR prescription.case_guid = registration_info.guid) " +
                    "AND prescription_code.code = prescription.code " +
                    "AND prescription_code.type = '"+Constant.X_RAY_CODE+"' ";
            rsPrescription = DBC.executeQuery(sqlPrescription);

            if (rsPrescription.next()) {
                this.tab_Prescription.setModel(HISModel.getModel(rsPrescription, true));
            } else {
                ((DefaultTableModel) tab_Prescription.getModel()).setRowCount(0);
            }

            //設定寬度與最小寬度
            TableColumn columnNo = this.tab_Prescription.getColumnModel().getColumn(0);
            TableColumn columnCode = this.tab_Prescription.getColumnModel().getColumn(2);
            TableColumn columnName = this.tab_Prescription.getColumnModel().getColumn(3);
            TableColumn columnDate = this.tab_Prescription.getColumnModel().getColumn(4);
            TableColumn columnDept = this.tab_Prescription.getColumnModel().getColumn(5);
            TableColumn columnDoctor = this.tab_Prescription.getColumnModel().getColumn(6);


            columnNo.setMaxWidth(25);
            columnDate.setPreferredWidth(50);
            columnDept.setPreferredWidth(30);
            columnDoctor.setPreferredWidth(80);
            columnCode.setPreferredWidth(50);
            columnName.setPreferredWidth(270);
            tab_Prescription.setRowHeight(30);
            TabTools.setHideColumn(tab_Prescription, 1);
            TabTools.setHideColumn(tab_Prescription, 11);
            TabTools.setHideColumn(tab_Prescription, 12);
          
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



     // 將 Summary 資料丟入 txta_Summary
    public void getTxtaSummary(String text) {
        txt_Result.setText(text);
    }
    
      private void initValue() {
        // 清空資料
            txt_TestName.setText("");
            txt_Result.setText("");
            radiobtn_Unnormal.setSelected(false);
            radiobtn_Normal.setSelected(false);
            txt_SpecimenTest.setText("");
            txt_SpecimenResult.setText("");
            ckbox_Test.setSelected(false);
            ckbox_Result.setSelected(false);
            btn_add.setEnabled(false);
    }

    private void setSavePre() {
        try {
             String sql = "UPDATE prescription " +
                "SET " ;

             if (ckbox_Test.isSelected())
                sql += " date_test = '" + txt_SpecimenTest.getText() + "' " ;
             else
                sql += ", date_test = NULL " ;

             if (ckbox_Result.isSelected())
                sql += ", date_results = '" + txt_SpecimenResult.getText() + "' " ;
             else
                 sql += ", date_results = NULL " ;


             if (txt_Result.getText() != null )
                sql += ", result = '" + txt_Result.getText() + "' " ;

            if (radiobtn_Normal.isSelected() || radiobtn_Unnormal.isSelected()) {
                if (radiobtn_Normal.isSelected()) sql += ", isnormal = 1 ";
                else if (radiobtn_Unnormal.isSelected()) sql += ", isnormal = 0 ";
            }

            sql += "WHERE guid = '" + m_TmpGuid + "'";
            System.out.println(sql);
            DBC.executeUpdate(sql);
            initValue();
            setTab_Prescription();
            JOptionPane.showMessageDialog(null,"Saved successfully.");
        } catch (SQLException ex) {
            Logger.getLogger(Frm_Radiology.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void mnit_EnterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnit_EnterActionPerformed

}//GEN-LAST:event_mnit_EnterActionPerformed

    private void mnit_CloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnit_CloseActionPerformed
        btn_CloseActionPerformed(null);
}//GEN-LAST:event_mnit_CloseActionPerformed

    private void tab_PrescriptionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab_PrescriptionMouseClicked
            initValue();
              if (this.tab_Prescription.getSelectedRow() != -1 && tab_Prescription.getColumnCount() != 1 && this.tab_Prescription.getValueAt(this.tab_Prescription.getSelectedRow(), 1) != null ) {
             btn_add.setEnabled(true);

             if (tab_Prescription.getValueAt(tab_Prescription.getSelectedRow(), 4) != null) {
                txt_Result.setText(tab_Prescription.getValueAt(tab_Prescription.getSelectedRow(), 4).toString());
             }

             if (tab_Prescription.getValueAt(tab_Prescription.getSelectedRow(), 9) != null) {
                txt_SpecimenTest.setText(tab_Prescription.getValueAt(tab_Prescription.getSelectedRow(), 9).toString());
                ckbox_Test.setSelected(true);
             }

             if (tab_Prescription.getValueAt(tab_Prescription.getSelectedRow(), 10) != null) {
                txt_SpecimenResult.setText(tab_Prescription.getValueAt(tab_Prescription.getSelectedRow(), 10).toString());
                ckbox_Result.setSelected(true);
             }


             if (tab_Prescription.getValueAt(tab_Prescription.getSelectedRow(), 5) != null) {
                 if (tab_Prescription.getValueAt(tab_Prescription.getSelectedRow(), 5).toString().equals("Y")) {
                    radiobtn_Unnormal.setSelected(false);
                    radiobtn_Normal.setSelected(true);
                 } else if (tab_Prescription.getValueAt(tab_Prescription.getSelectedRow(), 5).toString().equals("N")){
                    radiobtn_Unnormal.setSelected(true);
                    radiobtn_Normal.setSelected(false);
                 }
             }

             txt_TestName.setText(tab_Prescription.getValueAt(tab_Prescription.getSelectedRow(), 2) + " "+ tab_Prescription.getValueAt(tab_Prescription.getSelectedRow(), 3));
             m_TmpGuid = tab_Prescription.getValueAt(tab_Prescription.getSelectedRow(), 1).toString();

             this.tab_Prescription.setToolTipText(TabTools.getTooltipStr((String)this.tab_Prescription.getValueAt(this.tab_Prescription.getSelectedRow(),4), 30));
              }
    }//GEN-LAST:event_tab_PrescriptionMouseClicked

    private void tab_PrescriptionKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tab_PrescriptionKeyReleased
        tab_PrescriptionMouseClicked(null);
    }//GEN-LAST:event_tab_PrescriptionKeyReleased

    private void txt_ResultMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_ResultMouseClicked
        new Summary(this, txt_Result ,true, pan_Summary ).setVisible(true);
}//GEN-LAST:event_txt_ResultMouseClicked

    private void txt_TestNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_TestNameKeyReleased

}//GEN-LAST:event_txt_TestNameKeyReleased

    private void btn_CloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CloseActionPerformed
        if (tab_Prescription.getRowCount() == 0) {
            if (m_WorkListRowNo -1 == -1) {m_WorkListRowNo = 0;}
            else {m_WorkListRowNo--;}
        }
        new Frm_WorkList(m_WorkListRowNo,"xray").setVisible(true);

        this.dispose();
    }//GEN-LAST:event_btn_CloseActionPerformed

    private void radiobtn_NormalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radiobtn_NormalActionPerformed
        if (radiobtn_Unnormal.isSelected()) {
            radiobtn_Unnormal.setSelected(false);
        }
}//GEN-LAST:event_radiobtn_NormalActionPerformed

    private void radiobtn_UnnormalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radiobtn_UnnormalActionPerformed
        if (radiobtn_Normal.isSelected()) {
            radiobtn_Normal.setSelected(false);
        }
}//GEN-LAST:event_radiobtn_UnnormalActionPerformed

    private void mnit_XRayRecordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnit_XRayRecordActionPerformed
        this.setEnabled(false);
        new Frm_RadiologyHistory(this, m_Pno).setVisible(true);
    }//GEN-LAST:event_mnit_XRayRecordActionPerformed

    private void btn_addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_addActionPerformed
        setSavePre();
    }//GEN-LAST:event_btn_addActionPerformed

    private void ckbox_TestMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ckbox_TestMouseClicked
        if (ckbox_Test.isSelected()) txt_SpecimenTest.setText(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime()));
        else txt_SpecimenTest.setText("");
}//GEN-LAST:event_ckbox_TestMouseClicked

    private void ckbox_ResultMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ckbox_ResultMouseClicked
        if (ckbox_Result.isSelected()) txt_SpecimenResult.setText(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime()));
        else txt_SpecimenResult.setText("");
}//GEN-LAST:event_ckbox_ResultMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Close;
    private javax.swing.JButton btn_Finish;
    private javax.swing.JButton btn_add;
    private javax.swing.JCheckBox ckbox_Result;
    private javax.swing.JCheckBox ckbox_Test;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lab_DateOfTest1;
    private javax.swing.JLabel lab_Height;
    private javax.swing.JLabel lab_NameOfTest;
    private javax.swing.JLabel lab_ResultsCollectionDate1;
    private javax.swing.JLabel lab_ResultsCollectionDate2;
    private javax.swing.JLabel lab_TitleAge;
    private javax.swing.JLabel lab_TitleBloodtype;
    private javax.swing.JLabel lab_TitleName;
    private javax.swing.JLabel lab_TitleNo;
    private javax.swing.JLabel lab_TitlePs;
    private javax.swing.JLabel lab_TitleSex;
    private javax.swing.JLabel lab_Weight;
    private javax.swing.JMenu mn_Fiele;
    private javax.swing.JMenuBar mnb;
    private javax.swing.JMenuItem mnit_Close;
    private javax.swing.JMenuItem mnit_XRayRecord;
    private javax.swing.JPanel pan_Summary;
    private javax.swing.JPanel pan_Top;
    private javax.swing.JRadioButton radiobtn_Normal;
    private javax.swing.JRadioButton radiobtn_Unnormal;
    private javax.swing.JScrollPane span_Prescription;
    private javax.swing.JTable tab_Prescription;
    private javax.swing.JTextField txt_Age;
    private javax.swing.JTextField txt_Bloodtype;
    private javax.swing.JTextField txt_Height;
    private javax.swing.JTextField txt_Name;
    private javax.swing.JTextField txt_No;
    private javax.swing.JTextField txt_Ps;
    private javax.swing.JTextArea txt_Result;
    private javax.swing.JTextField txt_Sex;
    private javax.swing.JTextField txt_SpecimenResult;
    private javax.swing.JTextField txt_SpecimenTest;
    private javax.swing.JTextField txt_TestName;
    private javax.swing.JTextField txt_Weight;
    // End of variables declaration//GEN-END:variables
//    class MyPrintable implements Printable {
//
//
//        public int print(Graphics g, PageFormat pf, int pageIndex) {
//            String sqlPatient =
//                    "SELECT registration_info.touchtime, patients_info.p_no, registration_info.pharmacy_no, " +
//                           "registration_info.modify_count, concat(patients_info.firstname,'  ',patients_info.lastname) AS name, " +
//                           "patients_info.gender, patients_info.birth " +
//                    "FROM registration_info, patients_info " +
//                    "WHERE guid = '"+m_RegistrationGuid+"' " +
//                        "AND registration_info.p_no = patients_info.p_no";
//
//            ResultSet rsReceiveMedicineNo = null;
//            ResultSet rsPatient = null;
//            if (pageIndex != 0) return NO_SUCH_PAGE;
//            Graphics2D g2 = (Graphics2D) g;
//
//            g2.setFont(new Font("Serif", Font.PLAIN, 8));
//            g2.setPaint(Color.black);
//            int i = 80;
//            String finishNo = null;
//            // g2.drawString("文字", X, Y);
//            //********************************************************//
//            try {
//                rsPatient = DBC.executeQuery(sqlPatient);
//                rsPatient.next();
//                if (i != 0) {
//                    finishNo = rsPatient.getString("pharmacy_no");
//                } else {
//                    finishNo = "--";
//                }
//                if (m_FinishState == true) {
//                    g2.drawString("Modify-" + rsPatient.getInt("modify_count"), 380, i);
//                }
//                g2.drawString(rsPatient.getString("touchtime").substring(4, 14), 450, i);
//                i+=60;
//                g2.drawString("Date: "+DateMethod.getTodayYMD(), 80, i);
//                g2.drawString("Department: "+UserInfo.getUserPoliclinic(), 220, i);
//                g2.drawString("Staff: "+UserInfo.getUserNO()+" "+UserInfo.getUserName(), 400, i);
//                i+=20;
//                g2.drawString("Name: "+rsPatient.getString("name"), 80, i);
//                g2.drawString("Gender: "+rsPatient.getString("gender"), 220, i);
//                g2.drawString("Patient No.: "+rsPatient.getString("p_no"), 400, i);
//                i+=20;
//                g2.drawString("Age: "+DateMethod.getAgeWithMonth(rsPatient.getDate("birth")), 80, i);
//
//                    //g2.drawString("Receive Medicine Number: "+ finishNo, 220, i);  //  領藥號
//
//
//                i+=15;
//                g2.drawString("---------------------------------------------------------------------------------------------" +
//                        "---------------------------------------------------------------------------------------------" +
//                        "---------------------------------------------------------------------------------------------", 0, i);
//                i+=15;
//
//                doDrawPrescription(g2, i);
//            } catch (SQLException e) {
//                Logger.getLogger(Frm_Radiology.class.getName()).log(Level.SEVERE, null, e);
//
//            } finally {
//                try {DBC.closeConnection(rsReceiveMedicineNo);
//                     DBC.closeConnection(rsPatient);}
//                catch (SQLException e){
//
//                }
//            }
//            return PAGE_EXISTS;
//        }
//
//        private void doDrawTitle(Graphics2D g2, String txt) {
//            g2.setFont(new Font("Serif", Font.PLAIN, 16));
//            g2.drawString(txt, 80, 95);
//        }
//
//        // 列印處置 不列印X光
//        private void doDrawPrescription(Graphics2D g2,int i) {
//            ResultSet rs = null;
//            double totlecost = 0;
//            try {
//                String sql =
//                    "SELECT prescription_code.code AS code, prescription_code.name AS name, prescription.place, prescription_code.type, prescription.cost AS cost, prescription.result AS result, " +
//                       "CASE prescription.isnormal WHEN 1 THEN 'Y' WHEN 0 THEN 'N' ELSE null  END 'Normal' " +
//                    "FROM prescription, prescription_code, outpatient_services, registration_info " +
//                    "WHERE registration_info.guid = '"+m_RegistrationGuid+"' " +
//                      "AND prescription.os_guid = outpatient_services.guid " +
//                      "AND prescription_code.code = prescription.code " +
//                      "AND outpatient_services.reg_guid = registration_info.guid " +
//                      "AND prescription_code.type = '"+Constant.X_RAY_CODE+"'";
//                rs = DBC.executeQuery(sql);
//                g2.drawString("X-Ray", 80, i);
//                int r = 0; // 序號
//                int tmp =i+15;
//
//                while (rs.next()) {
//                  ++r;
//                  g2.drawString(String.valueOf(r)+"."+"      "+rs.getString("code")+"      "+rs.getString("name"), 80,tmp);
//                  tmp+=15;
//                  g2.drawString("        Type: "+rs.getString("type")+"          Place: "+rs.getString("place"), 80,tmp);
//                  tmp+=15;
//                  //g2.drawString("        Normal: "+rs.getString("Normal")+"          Cost: "+rs.getString("cost"), 80,tmp);
//                  g2.drawString("        Normal: "+rs.getString("Normal"), 80,tmp);
//                  tmp+=15;
//                  g2.drawString("        Result: "+rs.getString("result"), 80,tmp);
//                  tmp+=15;
////                  if (rs.getString("cost") != null && !rs.getString("cost").equals("")  ) {
////                    totlecost +=rs.getDouble("cost");
////                  }
//
//                }
////                g2.drawString("---------------------------", 80,tmp);
////                  tmp+=15;
////                g2.drawString("        Total cost: "+totlecost, 80,tmp);
//                doDrawTitle(g2,"X-Ray Form  ( For History )");
//            } catch (SQLException ex) {
//                Logger.getLogger(Frm_Radiology.class.getName()).log(Level.SEVERE, null, ex);
//            }  finally {
//                try {
//                    DBC.closeConnection(rs);
//                }
//                catch (SQLException e){
//
//             }
//           }
//        }
//    }
}
