package Pharmacy;



import ErrorMessage.StoredErrorMessage;
import Main.Frm_Main;
import cc.johnwu.login.UserInfo;
import Multilingual.language;
import cc.johnwu.date.DateMethod;
import cc.johnwu.sql.DBC;

import java.awt.Color;
import java.awt.Font;
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
import javax.swing.ListSelectionModel;

public class Frm_Pharmacy extends javax.swing.JFrame {
 
    private long REFRASHTIME = 1000; //自度刷新跨號資訊時間
    private RefrashPharmacy m_RefrashWorkList;
    private Thread m_Clock;
    /*多國語言變數*/
    private language paragraph = new language();
    private String[] line = paragraph.setlanguage("PHARMACY").split("\n") ;
    private String[] message = paragraph.setlanguage("MESSAGE").split("\n") ;
    /*輸出錯誤資訊變數*/
    StoredErrorMessage ErrorMessage = new StoredErrorMessage() ;
   
    public Frm_Pharmacy() {
        initComponents();
        initWorkList();
        initLanguage() ;
    }

    public Frm_Pharmacy(int LastSelectRow) {
        this();
        if (tab_Pharmacy.getRowCount() != 0) {
            btn_Check.setEnabled(true);
            tab_Pharmacy.addRowSelectionInterval(LastSelectRow, LastSelectRow);
        }
    }

    // 初始化
    public void initWorkList() {
        this.setExtendedState(this.MAXIMIZED_BOTH);  // 最大化
        this.setLocationRelativeTo(this);
        this.tab_Pharmacy.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  // tabble不可按住多選
        addWindowListener(new WindowAdapter() {  // 畫面關閉原視窗enable
            @Override
            public void windowClosing(WindowEvent windowevent) {
                btn_CloseActionPerformed(null);
            }
        });
        txt_UserName.setText(UserInfo.getUserName());
        this.m_RefrashWorkList = new RefrashPharmacy(this.tab_Pharmacy, REFRASHTIME, this, this.check_Follow);
        this.m_RefrashWorkList.start();
        this.m_Clock = new Thread(){ // Clock
            @Override
            @SuppressWarnings("static-access")
            public void run(){
            try{ while(true){
            lab_SystemTime.setText(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime()));
             this.sleep(500);
            }}catch(InterruptedException e) {
                ErrorMessage.setData("Pharmacy", "Frm_Pharmacy" ,"run() - InterruptedException",
                    e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            }}
        };
        this.m_Clock.start();
    }

    private void initLanguage() {
        this.lab_UserName.setText(paragraph.getLanguage(line, "USERNAME"));
        this.check_Follow.setText(paragraph.getLanguage(line, "FOLLOW"));
        this.lab_Finish.setText(paragraph.getLanguage(line, "FINISH"));

        //this.btn_Search.setText(paragraph.getLanguage(line, "SEARCH"));
        this.btn_Check.setText(paragraph.getLanguage(message, "CHECK"));
        this.btn_Close.setText(paragraph.getLanguage(message, "CLOSE"));
        this.btn_Reprint.setText(paragraph.getLanguage(line, "REPRINT"));
        mn_Fiele.setText(paragraph.getLanguage(message, "FILE"));
        mnit_Enter.setText(paragraph.getLanguage(message, "CHECK"));
        mnit_Close.setText(paragraph.getLanguage(message, "CLOSE"));
        this.setTitle(paragraph.getLanguage(line, "TITLEPHARMACY"));
    }
    public void setNowRowCount(String rowCount){
        lab_FinishCount.setText(rowCount);
    }

    // 回到看診畫面 畫面重設為可編輯
    public void reSetEnable() {
        this.setEnabled(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pan_Center = new javax.swing.JPanel();
        span_Pharmacy = new javax.swing.JScrollPane();
        tab_Pharmacy = new javax.swing.JTable();
        btn_Check = new javax.swing.JButton();
        lab_FinishCount = new javax.swing.JLabel();
        lab_Finish = new javax.swing.JLabel();
        btn_Close = new javax.swing.JButton();
        btn_Reprint = new javax.swing.JButton();
        lab_UserName = new javax.swing.JLabel();
        txt_UserName = new javax.swing.JTextField();
        check_Follow = new javax.swing.JCheckBox();
        lab_SystemTime = new javax.swing.JLabel();
        mnb = new javax.swing.JMenuBar();
        mn_Fiele = new javax.swing.JMenu();
        mnit_Enter = new javax.swing.JMenuItem();
        mnit_Close = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Pharmacy");

        pan_Center.setBackground(new java.awt.Color(228, 228, 228));

        tab_Pharmacy.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tab_Pharmacy.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tab_Pharmacy.setRowHeight(25);
        tab_Pharmacy.getTableHeader().setReorderingAllowed(false);
        tab_Pharmacy.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tab_PharmacyMouseClicked(evt);
            }
        });
        tab_Pharmacy.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tab_PharmacyKeyPressed(evt);
            }
        });
        span_Pharmacy.setViewportView(tab_Pharmacy);

        btn_Check.setText("Check");
        btn_Check.setEnabled(false);
        btn_Check.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CheckActionPerformed(evt);
            }
        });

        lab_FinishCount.setFont(new java.awt.Font("UnDotum", 0, 18));
        lab_FinishCount.setText("--");

        lab_Finish.setText("Number Of People ");

        btn_Close.setText("Close");
        btn_Close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CloseActionPerformed(evt);
            }
        });

        btn_Reprint.setText("Reprint");
        btn_Reprint.setEnabled(false);
        btn_Reprint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ReprintActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pan_CenterLayout = new javax.swing.GroupLayout(pan_Center);
        pan_Center.setLayout(pan_CenterLayout);
        pan_CenterLayout.setHorizontalGroup(
            pan_CenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_CenterLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan_CenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(span_Pharmacy, javax.swing.GroupLayout.DEFAULT_SIZE, 804, Short.MAX_VALUE)
                    .addGroup(pan_CenterLayout.createSequentialGroup()
                        .addComponent(lab_Finish)
                        .addGap(18, 18, 18)
                        .addComponent(lab_FinishCount, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 318, Short.MAX_VALUE)
                        .addComponent(btn_Reprint, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_Check, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_Close, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        pan_CenterLayout.setVerticalGroup(
            pan_CenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan_CenterLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(span_Pharmacy, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_CenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab_Finish)
                    .addComponent(lab_FinishCount)
                    .addComponent(btn_Close)
                    .addComponent(btn_Check)
                    .addComponent(btn_Reprint))
                .addContainerGap())
        );

        lab_UserName.setText("User");

        txt_UserName.setEditable(false);

        check_Follow.setText("Follow the latest");

        lab_SystemTime.setFont(new java.awt.Font("UnDotum", 0, 18));
        lab_SystemTime.setText("-----");

        mn_Fiele.setText("File");
        mn_Fiele.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mn_FieleActionPerformed(evt);
            }
        });

        mnit_Enter.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_SPACE, 0));
        mnit_Enter.setText("Check");
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
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pan_Center, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lab_UserName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_UserName, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lab_SystemTime, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 217, Short.MAX_VALUE)
                        .addComponent(check_Follow)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab_UserName)
                    .addComponent(txt_UserName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(check_Follow)
                    .addComponent(lab_SystemTime))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pan_Center, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_CloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CloseActionPerformed
        m_RefrashWorkList.interrupt();  // 終止重複讀取掛號表單
        new Frm_Main().setVisible(true);
        this.dispose();
}//GEN-LAST:event_btn_CloseActionPerformed

    private void mnit_EnterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnit_EnterActionPerformed
        if(btn_Check.isEnabled()) {
            btn_CheckActionPerformed(null);
        }
    }//GEN-LAST:event_mnit_EnterActionPerformed

    private void mnit_CloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnit_CloseActionPerformed
        btn_CloseActionPerformed(null);
    }//GEN-LAST:event_mnit_CloseActionPerformed

    private void tab_PharmacyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab_PharmacyMouseClicked
        check_Follow.setSelected(false);
        if(this.tab_Pharmacy.getRowCount() > 0) {
            this.btn_Check.setEnabled(true);
            this.btn_Reprint.setEnabled(true);
        }
        if (evt.getClickCount() == 2) {
            btn_CheckActionPerformed(null);
        }
    }//GEN-LAST:event_tab_PharmacyMouseClicked

    private void tab_PharmacyKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tab_PharmacyKeyPressed
        tab_PharmacyMouseClicked(null);
    }//GEN-LAST:event_tab_PharmacyKeyPressed

    private void btn_CheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CheckActionPerformed
        if (tab_Pharmacy.getSelectedRow() != -1 && tab_Pharmacy.getValueAt(tab_Pharmacy.getSelectedRow(), 1) != null) {
   
            String getDep = (String)this.tab_Pharmacy.getValueAt(tab_Pharmacy.getSelectedRow(), 1);
            String getDocter = (String)this.tab_Pharmacy.getValueAt(tab_Pharmacy.getSelectedRow(), 2);
            String getNo = (String)this.tab_Pharmacy.getValueAt(tab_Pharmacy.getSelectedRow(), 3);
            String getName = (String)this.tab_Pharmacy.getValueAt(tab_Pharmacy.getSelectedRow(), 4);
            String getBirth = (String)this.tab_Pharmacy.getValueAt(tab_Pharmacy.getSelectedRow(), 5);
            String getGender = (String)this.tab_Pharmacy.getValueAt(tab_Pharmacy.getSelectedRow(), 6);
            String getBlood = (String)this.tab_Pharmacy.getValueAt(tab_Pharmacy.getSelectedRow(), 7);
            String getps = (String)this.tab_Pharmacy.getValueAt(tab_Pharmacy.getSelectedRow(), 8);
            String getGuid = (String)this.tab_Pharmacy.getValueAt(tab_Pharmacy.getSelectedRow(),9);
            new Frm_PharmacyInfo(this,getDep,getDocter,getNo,getName,getBirth,getGender,getBlood,getps,getGuid).setVisible(true);
            this.setEnabled(false);
        } 
}//GEN-LAST:event_btn_CheckActionPerformed

    private void btn_ReprintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ReprintActionPerformed
         PrinterJob pj = PrinterJob.getPrinterJob();
         PageFormat pf = pj.defaultPage();
         Paper paper = new Paper();
         pf.setPaper(paper);
         pj.setPrintable(new MyPrintable(), pf);

         //if (pj.printDialog())
         try {
             pj.print();
         } catch (PrinterException e) {
             ErrorMessage.setData("Pharmacy", "Frm_Pharmacy" ,"btn_ReprintActionPerformed(java.awt.event.ActionEvent evt)",
                    e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
             System.out.println(e);
         }
    }//GEN-LAST:event_btn_ReprintActionPerformed

    private void mn_FieleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mn_FieleActionPerformed
        
    }//GEN-LAST:event_mn_FieleActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Check;
    private javax.swing.JButton btn_Close;
    private javax.swing.JButton btn_Reprint;
    private javax.swing.JCheckBox check_Follow;
    private javax.swing.JLabel lab_Finish;
    private javax.swing.JLabel lab_FinishCount;
    private javax.swing.JLabel lab_SystemTime;
    private javax.swing.JLabel lab_UserName;
    private javax.swing.JMenu mn_Fiele;
    private javax.swing.JMenuBar mnb;
    private javax.swing.JMenuItem mnit_Close;
    private javax.swing.JMenuItem mnit_Enter;
    private javax.swing.JPanel pan_Center;
    private javax.swing.JScrollPane span_Pharmacy;
    private javax.swing.JTable tab_Pharmacy;
    private javax.swing.JTextField txt_UserName;
    // End of variables declaration//GEN-END:variables
     class MyPrintable implements Printable {
        @SuppressWarnings("empty-statement")
        public int print(Graphics g, PageFormat pf, int pageIndex) {
            String regGuid = null;
            regGuid = (String) tab_Pharmacy.getValueAt(tab_Pharmacy.getSelectedRow(),9);
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
                rsPatient = DBC.executeQuery(sqlPatient);
                rsPatient.next();
                if (i != 0) {
                    finishNo = rsPatient.getString("pharmacy_no");
                } else {
                    finishNo = "--";
                }
                if (rsPatient.getInt("modify_count") != 0) {
                    g2.drawString("Modify-" + rsPatient.getInt("modify_count"), 380, i);
                }
                g2.drawString(rsPatient.getString("touchtime").substring(4, 14), 450, i);
                i+=60;
                g2.drawString("Date: "+DateMethod.getTodayYMD(), 80, i);
                g2.drawString("Department: "+(String)tab_Pharmacy.getValueAt(tab_Pharmacy.getSelectedRow(), 1), 220, i);
                g2.drawString("Doctor: "+(String)tab_Pharmacy.getValueAt(tab_Pharmacy.getSelectedRow(), 1), 400, i);
                i+=20;
                g2.drawString("Name: "+rsPatient.getString("name"), 80, i);
                g2.drawString("Gender: "+rsPatient.getString("gender"), 220, i);
                g2.drawString("Patient No.: "+rsPatient.getString("p_no"), 400, i);
                i+=20;
                g2.drawString("Age: "+DateMethod.getAgeWithMonth(rsPatient.getDate("birth")), 80, i);
                g2.drawString("Receive Medicine Number: "+ finishNo, 220, i);
                i+=15;
                g2.drawString("------------------------------------------------------------------------" +
                        "------------------------------------------------------------------------", 0, i);
                i+=15;
                g2.drawString("Medicine", 80, i);
                rsMedicines = DBC.executeQuery(sqlMedicines);
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
                   // g2.drawString(rsMedicines.getString("usage"), 90, i);  g2.drawString(rsMedicines.getString("way"), 125, i);  g2.drawString(ps, 160, i);
                    g2.drawString("Usage: 2 times per day", 90, i);
                    i += 15;
                    g2.drawString("Way: External", 90, i);

                    i+=15;
                    g2.drawString(rsMedicines.getString("dosage"), 90, i); //  +" "+ rsMedicines.getString("unit")
                    g2.drawString(rsMedicines.getString("day")+" Day", 125, i);
                    g2.drawString(urgent, 190, i);  g2.drawString(powder, 245, i);
                    g2.drawString("Total: "+ rsMedicines.getString("quantity"), 330, i);  // +" "+rsMedicines.getString("unit")
                    g2.drawString("Cost: _____________________", 420, i);  // +" "+rsMedicines.getString("unit")
                    //********************************************************//
                }
                g2.drawString("--------------------------------------------------------------------", 385, i+20);  // +" "+rsMedicines.getString("unit")
                g2.drawString("Total Cost: _________________________", 390, i+30);  // +" "+rsMedicines.getString("unit")
                doDrawTitle(g2,"KBTH   Medicine Package");
            } catch (SQLException e) {
                ErrorMessage.setData("Pharmacy", "Frm_Pharmacy" ,"print(Graphics g, PageFormat pf, int pageIndex)",
                    e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
                Logger.getLogger(RefrashPharmacy.class.getName()).log(Level.SEVERE, null, e);
            } finally {
                try {DBC.closeConnection(rsMedicines);
                     DBC.closeConnection(rsReceiveMedicineNo);
                     DBC.closeConnection(rsPatient);}
                catch (SQLException e){
                    ErrorMessage.setData("Pharmacy", "Frm_Pharmacy" ,"print(Graphics g, PageFormat pf, int pageIndex) - DBC.closeConnection",
                        e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
                }
            }
            return PAGE_EXISTS;
        }
        private void doDrawTitle(Graphics2D g2, String txt) {
            g2.setFont(new Font("Serif", Font.PLAIN, 16));
            g2.drawString(txt, 80, 95);
        }
    }

}