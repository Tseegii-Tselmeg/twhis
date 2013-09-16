package WorkList;

import Case.Frm_Case;
import Diagnosis.Frm_DiagnosisInfo;
import ErrorMessage.StoredErrorMessage;
import Laboratory.Frm_Laboratory;
import Main.Frm_Main;
import cc.johnwu.login.UserInfo;
import Multilingual.language;
import Common.TabTools;
import Radiology.Frm_Radiology;
import cc.johnwu.date.DateMethod;
import cc.johnwu.sql.DBC;
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
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;

public class Frm_WorkList extends javax.swing.JFrame {
 
    private long REFRASHTIME = 1000; //自度刷新跨號資訊時間
    private RefrashWorkList m_RefrashWorkList;
    private Thread m_Clock;
    private String m_SysName ;  // 系統名稱
    private boolean m_IsStop = false;
    private String m_RegGuid;
    private String m_Pno;
    /*多國語言變數*/
    private language paragraph = new language();
    private String[] line = paragraph.setlanguage("DIAGNOSISWORKLIST").split("\n") ;
    private String[] message = paragraph.setlanguage("MESSAGE").split("\n") ;
    /*輸出錯誤資訊變數*/
    StoredErrorMessage ErrorMessage = new StoredErrorMessage() ;

    // LastSelectRow 最後選擇行號　　SysName　系統名
    public Frm_WorkList(int LastSelectRow, String SysName) {
        m_SysName = SysName;
        initComponents();
        //---------迦納-------------------------
//        dateComboBox.setVisible(false);
//        btn_Search.setVisible(false);
//        lab_Date.setVisible(false);
        //----------------------------------
        initWorkList();
        initLanguage();
        if (tab_WorkList.getRowCount() != 0 ) {
            btn_Enter.setEnabled(true);

            if (SysName.equals("dia")) {
                tab_WorkList.addRowSelectionInterval(LastSelectRow, LastSelectRow);
            } else  {
               tab_WorkList.addRowSelectionInterval(0, 0);
            }
            tab_WorkList.changeSelection(LastSelectRow, LastSelectRow, false, false);
        }
        initLanguage();
    }

    // 初始化
    public void initWorkList() {
        // 依系統不同初始化
        if (m_SysName.equals("dia")) {
            dateComboBox.setVisible(false);
            btn_Search.setVisible(false);
            lab_Date.setVisible(false);
             this.setTitle("Diagnosis WorkList");
         } else if (m_SysName.equals("lab")) {
            this.setTitle("Laboratory WorkList");
            btn_RePrint.setVisible(false);
            lab_Name.setText("Staff");
            lab_Finish.setVisible(false);
            lab_FinishCount.setVisible(false);
        }  else if (m_SysName.equals("xray")) {
            this.setTitle("Radiology(X-RAY) WorkList");
            btn_RePrint.setVisible(false);
            lab_Name.setText("Staff");
            lab_Finish.setVisible(false);
            lab_FinishCount.setVisible(false);
        }  else if (m_SysName.equals("case")) {
            this.setTitle("Case Management WorkList");
            btn_RePrint.setVisible(false);
            lab_Name.setText("Staff");
            lab_Finish.setVisible(false);
            lab_FinishCount.setVisible(false);
            btn_RePrint.setVisible(false);
        } else if (m_SysName.equals("hos")) {
            this.setTitle("住院病患列表");
            dateComboBox.setVisible(false);
            btn_Search.setVisible(false);
            lab_Date.setVisible(false);
        }

        this.setExtendedState(Frm_WorkList.MAXIMIZED_BOTH);  // 最大化
        this.setLocationRelativeTo(this);
        this.tab_WorkList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  // tabble不可按住多選
        addWindowListener(new WindowAdapter() {  // 畫面關閉原視窗enable
            @Override
            public void windowClosing(WindowEvent windowevent) {
                mnit_CloseActionPerformed(null);
            }
        });
        this.m_RefrashWorkList = new RefrashWorkList(this.tab_WorkList, REFRASHTIME, m_SysName);
        this.m_RefrashWorkList.start();
        this.m_Clock = new Thread(){ // Clock
            @Override
            @SuppressWarnings("static-access")
            public void run(){
            try{ while(true){
            lab_SystemTime.setText(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()));
            showVisitsCount();
             this.sleep(500); 
            }}catch(InterruptedException e) {
                ErrorMessage.setData("Diagnosis", "Frm_DiagnosisWorkList" ,"initWorkList() - run()",
                        e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            }}
        };
        this.m_Clock.start();
        this.txt_Name.setText(UserInfo.getUserName());
        this.txt_Poli.setText(UserInfo.getUserPoliclinic());

    }

    private void initLanguage() {
        this.btn_RePrint.setText(paragraph.getLanguage(line, "PRINT"));
       // this.lab_Name.setText(paragraph.getLanguage(line, "NAME"));
        this.lab_Wait.setText(paragraph.getLanguage(line, "WAIT"));
        this.lab_poli.setText(paragraph.getLanguage(line, "POLI"));
        this.lab_Finish.setText(paragraph.getLanguage(line, "FINISH"));
        this.btn_Enter.setText(paragraph.getLanguage(message, "ENTER"));
        this.btn_Close.setText(paragraph.getLanguage(message, "CLOSE"));
        mn_Fiele.setText(paragraph.getLanguage(message, "FILE"));
        mnit_Enter.setText(paragraph.getLanguage(message, "ENTER"));
        mnit_Close.setText(paragraph.getLanguage(message, "CLOSE"));
        //this.setTitle(paragraph.getLanguage(line, "TITLEWORKLIST"));
    }
    // 計算待診 已診人數
    private void showVisitsCount() {
        int finishCount = 0;
        if(this.tab_WorkList.getRowCount()>0)
        for(int i=0; i<this.tab_WorkList.getRowCount(); i++){
            if(this.tab_WorkList.getValueAt(i, 3)!=null &&
               this.tab_WorkList.getValueAt(i, 3).toString().equals("F"))
                finishCount++;
        }
        this.lab_WaitCount.setText(""+(tab_WorkList.getRowCount()-finishCount));
        this.lab_FinishCount.setText(""+finishCount);
    }

    // 進入看診
    public void setEnter(boolean finishState) {
         m_RefrashWorkList.interrupt();  // 終止重複讀取掛號表單
         m_Clock.interrupt();
         m_Pno = (String)this.tab_WorkList.getValueAt(tab_WorkList.getSelectedRow(), 5);
         m_RegGuid = (String)this.tab_WorkList.getValueAt(tab_WorkList.getSelectedRow(), 11);
         int getSelectRow = this.tab_WorkList.getSelectedRow();

         if (m_SysName.equals("dia")) {
             boolean getFirst = false;  // 是否為初診(用於彈出過敏設定)
             if (tab_WorkList.getValueAt(tab_WorkList.getSelectedRow(), 2) != null &&
                 tab_WorkList.getValueAt(tab_WorkList.getSelectedRow(), 2).toString().equals("*")) {
                 getFirst = true;
             }
             new Frm_DiagnosisInfo(m_Pno, m_RegGuid, getSelectRow, finishState,getFirst ).setVisible(true);
         } else if (m_SysName.equals("lab")) {
            new Frm_Laboratory(m_Pno, m_RegGuid, getSelectRow, finishState).setVisible(true);
         } else if (m_SysName.equals("xray")) {
            new Frm_Radiology(m_Pno, m_RegGuid, getSelectRow, finishState).setVisible(true);
         } else if (m_SysName.equals("case")) {
             if(tab_WorkList.getValueAt(tab_WorkList.getSelectedRow(), 3) != null
             && tab_WorkList.getValueAt(tab_WorkList.getSelectedRow(), 3).toString().equals("F")) {
                new Frm_Case(m_Pno, m_RegGuid, true,"").setVisible(true);
             } else {
                new Frm_Case(m_Pno, m_RegGuid, false,"").setVisible(true);
             }
            
         }
         this.dispose();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pan_Center = new javax.swing.JPanel();
        span_WaitDiagnosis = new javax.swing.JScrollPane();
        tab_WorkList = new javax.swing.JTable();
        pan_Top = new javax.swing.JPanel();
        lab_Name = new javax.swing.JLabel();
        lab_poli = new javax.swing.JLabel();
        lab_Finish = new javax.swing.JLabel();
        lab_FinishCount = new javax.swing.JLabel();
        lab_Wait = new javax.swing.JLabel();
        lab_WaitCount = new javax.swing.JLabel();
        txt_Name = new javax.swing.JTextField();
        txt_Poli = new javax.swing.JTextField();
        lab_SystemTime = new javax.swing.JLabel();
        dateComboBox = new cc.johnwu.date.DateComboBox();
        btn_Search = new javax.swing.JButton();
        lab_Date = new javax.swing.JLabel();
        pan_Right = new javax.swing.JPanel();
        btn_Close = new javax.swing.JButton();
        btn_Enter = new javax.swing.JButton();
        btn_RePrint = new javax.swing.JButton();
        mnb = new javax.swing.JMenuBar();
        mn_Fiele = new javax.swing.JMenu();
        mnit_Enter = new javax.swing.JMenuItem();
        mnit_Close = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("WorkList");

        tab_WorkList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tab_WorkList.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tab_WorkList.setRowHeight(25);
        tab_WorkList.getTableHeader().setReorderingAllowed(false);
        tab_WorkList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tab_WorkListMouseClicked(evt);
            }
        });
        tab_WorkList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tab_WorkListKeyPressed(evt);
            }
        });
        span_WaitDiagnosis.setViewportView(tab_WorkList);

        javax.swing.GroupLayout pan_CenterLayout = new javax.swing.GroupLayout(pan_Center);
        pan_Center.setLayout(pan_CenterLayout);
        pan_CenterLayout.setHorizontalGroup(
            pan_CenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_CenterLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(span_WaitDiagnosis, javax.swing.GroupLayout.DEFAULT_SIZE, 601, Short.MAX_VALUE)
                .addContainerGap())
        );
        pan_CenterLayout.setVerticalGroup(
            pan_CenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_CenterLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(span_WaitDiagnosis, javax.swing.GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
                .addContainerGap())
        );

        lab_Name.setText("Doctor");

        lab_poli.setText("Department");

        lab_Finish.setText("Finish");

        lab_FinishCount.setText("-----");

        lab_Wait.setText("Await");

        lab_WaitCount.setText("-----");

        txt_Name.setEditable(false);

        txt_Poli.setEditable(false);

        lab_SystemTime.setFont(new java.awt.Font("UnDotum", 0, 18));
        lab_SystemTime.setText("-----");

        btn_Search.setText("Search");
        btn_Search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_SearchActionPerformed(evt);
            }
        });

        lab_Date.setText("Date:");

        javax.swing.GroupLayout pan_TopLayout = new javax.swing.GroupLayout(pan_Top);
        pan_Top.setLayout(pan_TopLayout);
        pan_TopLayout.setHorizontalGroup(
            pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_TopLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lab_Wait)
                    .addComponent(lab_Name)
                    .addComponent(lab_Date))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan_TopLayout.createSequentialGroup()
                        .addGroup(pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_Name, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lab_WaitCount, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lab_poli)
                            .addComponent(lab_Finish))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pan_TopLayout.createSequentialGroup()
                                .addComponent(txt_Poli, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lab_SystemTime, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lab_FinishCount, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pan_TopLayout.createSequentialGroup()
                        .addComponent(dateComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_Search, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(137, Short.MAX_VALUE))
        );
        pan_TopLayout.setVerticalGroup(
            pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_TopLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab_Name)
                    .addComponent(txt_Name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_poli)
                    .addComponent(txt_Poli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_SystemTime))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab_Wait)
                    .addComponent(lab_Finish)
                    .addComponent(lab_FinishCount)
                    .addComponent(lab_WaitCount))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lab_Date)
                    .addComponent(dateComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_Search))
                .addGap(12, 12, 12))
        );

        btn_Close.setText("Close");
        btn_Close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CloseActionPerformed(evt);
            }
        });

        btn_Enter.setText("Enter");
        btn_Enter.setEnabled(false);
        btn_Enter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_EnterActionPerformed(evt);
            }
        });

        btn_RePrint.setText("Print");
        btn_RePrint.setEnabled(false);
        btn_RePrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_RePrintActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pan_RightLayout = new javax.swing.GroupLayout(pan_Right);
        pan_Right.setLayout(pan_RightLayout);
        pan_RightLayout.setHorizontalGroup(
            pan_RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_RightLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan_RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_Enter, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                    .addComponent(btn_RePrint, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                    .addComponent(btn_Close, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE))
                .addContainerGap())
        );
        pan_RightLayout.setVerticalGroup(
            pan_RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_RightLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_Enter)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_RePrint)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Close)
                .addContainerGap(363, Short.MAX_VALUE))
        );

        mn_Fiele.setText("File");

        mnit_Enter.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_SPACE, 0));
        mnit_Enter.setText("Enter");
        mnit_Enter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnit_EnterActionPerformed(evt);
            }
        });
        mn_Fiele.add(mnit_Enter);

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
                    .addComponent(pan_Top, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pan_Center, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pan_Right, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pan_Top, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pan_Center, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pan_Right, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mnit_EnterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnit_EnterActionPerformed
        if(btn_Enter.isEnabled()) {
            btn_EnterActionPerformed(null);
        }
    }//GEN-LAST:event_mnit_EnterActionPerformed

    private void mnit_CloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnit_CloseActionPerformed

        m_RefrashWorkList.interrupt();  // 終止重複讀取掛號表單
        m_Clock.interrupt();
        new Frm_Main().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_mnit_CloseActionPerformed

    private void tab_WorkListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab_WorkListMouseClicked
        if(evt.getClickCount() == 2) {
            btn_EnterActionPerformed(null);
        }
        tab_WorkListKeyPressed(null);
    }//GEN-LAST:event_tab_WorkListMouseClicked

    private void tab_WorkListKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tab_WorkListKeyPressed
        if(this.tab_WorkList.getRowCount() > 0) {
            this.btn_Enter.setEnabled(true);
             if(this.m_SysName.equals("dia") && this.tab_WorkList.getValueAt(this.tab_WorkList.getSelectedRow(), 3) != null) {
                this.btn_Enter.setText(paragraph.getLanguage(line, "EDIT"));
                this.btn_RePrint.setEnabled(true);
            } else if(this.m_SysName.equals("dia")){
                this.btn_Enter.setText(paragraph.getLanguage(message, "ENTER"));
                this.btn_RePrint.setEnabled(false);
            }
        }
    }//GEN-LAST:event_tab_WorkListKeyPressed

    private void btn_EnterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_EnterActionPerformed

        boolean finishState = false;
        if (tab_WorkList.getValueAt(tab_WorkList.getSelectedRow(), 3) != null
                && tab_WorkList.getValueAt(tab_WorkList.getSelectedRow(), 3).equals("F")) {

        Object[] options = {paragraph.getLanguage(message , "YES"),paragraph.getLanguage(message , "NO")};
        int dialog = JOptionPane.showOptionDialog(
                new Frame(),
                paragraph.getLanguage(message, "DOYOUWANTTOCHANGETHEDATA"),
                paragraph.getLanguage(message, "MESSAGE"),
                JOptionPane.YES_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
                );
            
            if (dialog == 0) {
                finishState = true;
                m_RefrashWorkList.interrupt();  // 終止重複讀取掛號表單
                setEnter(finishState);
            }
        } else if(tab_WorkList.getValueAt(tab_WorkList.getSelectedRow(), 3) == null) {
            m_RefrashWorkList.interrupt();  // 終止重複讀取掛號表單
            setEnter(finishState);
        }
}//GEN-LAST:event_btn_EnterActionPerformed

    private void btn_CloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CloseActionPerformed
        mnit_CloseActionPerformed(null);
    }//GEN-LAST:event_btn_CloseActionPerformed

    private void btn_RePrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_RePrintActionPerformed
        PrinterJob pj = PrinterJob.getPrinterJob();
        PageFormat pf = pj.defaultPage();
        Paper paper = new Paper();
        pf.setPaper(paper);
        pj.setPrintable(new MyPrintable(), pf);
        //***************************//
        if (pj.printDialog())
        try {
            pj.print();
        } catch (PrinterException e) {
                ErrorMessage.setData("Diagnosis", "Frm_DiagnosisWorkList" ,"btn_RePrintActionPerformed()",
                        e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        }
    }//GEN-LAST:event_btn_RePrintActionPerformed

    private void btn_SearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SearchActionPerformed
        if (m_IsStop) {
            m_IsStop = false;
            dateComboBox.setEnabled(true);
            btn_Search.setText("Search");
            dateComboBox.setValue(new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()));
            this.m_RefrashWorkList = new RefrashWorkList(this.tab_WorkList, REFRASHTIME, m_SysName);
            this.m_RefrashWorkList.start();
            this.m_Clock = new Thread(){ // Clock
                @Override
                @SuppressWarnings("static-access")
                public void run(){
                try{ while(true){
                lab_SystemTime.setText(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime()));
                showVisitsCount();
                 this.sleep(500);
                }}catch(InterruptedException e) {
                    ErrorMessage.setData("Diagnosis", "Frm_DiagnosisWorkList" ,"initWorkList() - run()",
                            e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
                }}
            };
        } else {
            m_IsStop = true;
            dateComboBox.setEnabled(false);
            btn_Search.setText("Cancels Search");
            m_RefrashWorkList.interrupt();  // 終止重複讀取掛號表單
            m_RefrashWorkList.getSelectDate(dateComboBox.getValue());
        }
    }//GEN-LAST:event_btn_SearchActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Close;
    private javax.swing.JButton btn_Enter;
    private javax.swing.JButton btn_RePrint;
    private javax.swing.JButton btn_Search;
    private cc.johnwu.date.DateComboBox dateComboBox;
    private javax.swing.JLabel lab_Date;
    private javax.swing.JLabel lab_Finish;
    private javax.swing.JLabel lab_FinishCount;
    private javax.swing.JLabel lab_Name;
    private javax.swing.JLabel lab_SystemTime;
    private javax.swing.JLabel lab_Wait;
    private javax.swing.JLabel lab_WaitCount;
    private javax.swing.JLabel lab_poli;
    private javax.swing.JMenu mn_Fiele;
    private javax.swing.JMenuBar mnb;
    private javax.swing.JMenuItem mnit_Close;
    private javax.swing.JMenuItem mnit_Enter;
    private javax.swing.JPanel pan_Center;
    private javax.swing.JPanel pan_Right;
    private javax.swing.JPanel pan_Top;
    private javax.swing.JScrollPane span_WaitDiagnosis;
    private javax.swing.JTable tab_WorkList;
    private javax.swing.JTextField txt_Name;
    private javax.swing.JTextField txt_Poli;
    // End of variables declaration//GEN-END:variables
  class MyPrintable implements Printable {
        public int print(Graphics g, PageFormat pf, int pageIndex) {
            String regGuid = (String) tab_WorkList.getValueAt(tab_WorkList.getSelectedRow(), 11);

            String sqlMedicines =
                    "SELECT medicines.code, medicines.item, medicine_stock.dosage, medicines.unit, medicine_stock.usage, " +
                           "medicine_stock.way, medicine_stock.day, medicine_stock.quantity, medicine_stock.urgent, " +
                           "medicine_stock.powder, medicine_stock.ps " +
                    "FROM medicines, medicine_stock, outpatient_services, registration_info " +
                    "WHERE registration_info.guid = '"+regGuid+"' " +
                        "AND medicine_stock.os_guid = outpatient_services.guid " +
                        "AND outpatient_services.reg_guid = registration_info.guid " +
                        "AND medicines.code = medicine_stock.m_code";

            String sqlPatient =
                    "SELECT registration_info.touchtime, patients_info.p_no, registration_info.pharmacy_no, " +
                           "registration_info.modify_count, concat(patients_info.firstname,'  ',patients_info.lastname) AS name, " +
                           "patients_info.gender, patients_info.birth " +
                    "FROM registration_info, patients_info " +
                    "WHERE guid = '"+regGuid+"' " +
                        "AND registration_info.p_no = patients_info.p_no";

            ResultSet rsMedicines = null;
            ResultSet rsReceiveMedicineNo = null;
            ResultSet rsPatient = null;
            if (pageIndex != 0) return NO_SUCH_PAGE;
            Graphics2D g2 = (Graphics2D) g;
            g2.setFont(new Font("Serif", Font.PLAIN, 12));
            g2.setPaint(Color.black);
            int i = 80;
            int r = 0;
            String urgent = null;
            String powder = null;
            String ps = null;
            String finishNo = null;
            // g2.drawString("文字", X, Y);
            //********************************************************//
            try {
                rsMedicines = DBC.executeQuery(sqlMedicines);
                rsPatient = DBC.executeQuery(sqlPatient);

                rsPatient.next();
                if (rsMedicines.next()) {
                    finishNo = rsPatient.getString("pharmacy_no");
                } else {
                    finishNo = "--";
                }
                rsMedicines.beforeFirst();
                if (rsPatient.getInt("modify_count") != 0) {
                    g2.drawString("Modify-" + rsPatient.getInt("modify_count"), 380, i);
                }
                g2.drawString(rsPatient.getString("touchtime").substring(4, 14), 450, i);
                i+=60;
                g2.drawString("Date: "+DateMethod.getTodayYMD(), 80, i);
                g2.drawString("Department: "+UserInfo.getUserPoliclinic(), 220, i);
                g2.drawString("Doctor: "+UserInfo.getUserName(), 400, i);
                i+=20;
                g2.drawString("Name: "+rsPatient.getString("name"), 80, i);
                g2.drawString("Gender: "+rsPatient.getString("gender"), 220, i);
                g2.drawString("ID: "+rsPatient.getString("p_no"), 400, i);
                i+=20;
                g2.drawString("Age: "+DateMethod.getAgeWithMonth(rsPatient.getDate("birth")), 80, i);
                g2.drawString("Receive Medicine Number: "+ finishNo, 220, i);
                i+=15;
                g2.drawString("------------------------------------------------------------------------", 0, i);
                i+=15;
                g2.drawString("Medicine", 80, i);
                
                while (rsMedicines.next()) {
                    ++r;
                    if (rsMedicines.getString("urgent").equals("Y")) {
                       urgent = "Urgent";
                    } else {
                       urgent = "--";
                    }
                    if(rsMedicines.getString("powder").equals("Y")) {
                        powder = "Powder";
                    } else {
                        powder = "--";
                    }
                    if (rsMedicines.getString("ps") != null ) {
                        ps = rsMedicines.getString("ps");
                    } else {
                        ps = "";
                    }
                    i+=25;
                    g2.drawString(String.valueOf(r), 80, i);  g2.drawString(rsMedicines.getString("item"), 90, i);
                    i+=15;
                    g2.drawString(rsMedicines.getString("usage"), 90, i);  g2.drawString(rsMedicines.getString("way"), 125, i);  g2.drawString(ps, 160, i);
                    i+=15;
                    g2.drawString(rsMedicines.getString("dosage"), 90, i); //  +" "+ rsMedicines.getString("unit")
                    g2.drawString(rsMedicines.getString("day")+" Day", 125, i);
                    g2.drawString(urgent, 190, i);  g2.drawString(powder, 245, i);
                    g2.drawString("Total: "+ rsMedicines.getString("quantity"), 330, i);  // +" "+rsMedicines.getString("unit")
                    //********************************************************//
                }
            } catch (SQLException e) {
                ErrorMessage.setData("Diagnosis", "Frm_DiagnosisWorkList" ,"MyPrintable - print(Graphics g, PageFormat pf, int pageIndex)",
                        e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            } finally {
                try {DBC.closeConnection(rsMedicines);
                     DBC.closeConnection(rsReceiveMedicineNo);
                     DBC.closeConnection(rsPatient);}
                catch (SQLException e){
                    ErrorMessage.setData("Diagnosis", "Frm_DiagnosisWorkList" ,"MyPrintable - print(Graphics g, PageFormat pf, int pageIndex) - DBC.closeConnection",
                        e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
                }
            }
            return PAGE_EXISTS;
        }
    }
}