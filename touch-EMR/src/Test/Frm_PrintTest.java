package Test;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.geom.AffineTransform;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
/**
 *
 * @author Steven
 */
public class Frm_PrintTest extends javax.swing.JFrame {
    private int m_Type;


    /** Creates new form Frm_PrintTest */
    public Frm_PrintTest() {
        initComponents();
    }
    public void initPermission(){}

    public void DoPrint(int i) {
        PrinterJob pj = PrinterJob.getPrinterJob();
        PageFormat pf = pj.defaultPage();
        Paper paper = new Paper();
        pf.setPaper(paper);
        pj.setPrintable(new MyPrintable(), pf);
        m_Type = i ;
        switch(m_Type) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
             case 7:
                break;
            case 8:
                break;
            case 9:
                break;
            case 10:
                break;
            case 11:
                break;
        }

        //if (pj.printDialog()) {
        try {
            pj.print();
        } catch (PrinterException e) {

        }
    }

    /**
     * 列印斷行使用
     * str:要進行斷行的字串 num:每行字串長度 x:目前所在x值 y:目前所在y值 space:換行間距
     * return y值
     */
    public int getLineBreaksWord(Graphics2D g2, String str, int num, int x, int y ,int space) {

        String newStr = "";
        String[] sa = str.split(" ");
        boolean state = false;
        for (int i = 0; i < sa.length; i++) {
            if (sa.length > i + 1 && (newStr.length() + sa[i+1].length()) >=  num) {

                g2.drawString(newStr , x , y += space);
                newStr = "";
                state = true;
            } else {
                if (state) {
                    i--;
                    state = false;
                }
                
                newStr += (sa[i] + " ");
            }
        }
        
        g2.drawString(newStr , x , y += space);
        return y;
    }



    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btn_Dia = new javax.swing.JButton();
        btn_Flow = new javax.swing.JButton();
        btn_Medicine_Pt = new javax.swing.JButton();
        btn_Lab_Pt = new javax.swing.JButton();
        btn_Xray_Pt = new javax.swing.JButton();
        btn_History = new javax.swing.JButton();
        btn_All_His = new javax.swing.JButton();
        btn_Lab_His = new javax.swing.JButton();
        btn_Xray_His = new javax.swing.JButton();
        btn_Medicine_Package = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        dateChooser1 = new cc.johnwu.date.DateChooser();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("列印表單");
        setResizable(false);

        btn_Dia.setText("1.待診單(給病患)");
        btn_Dia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_DiaActionPerformed(evt);
            }
        });

        btn_Flow.setText("2.流程單(給病患)");
        btn_Flow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_FlowActionPerformed(evt);
            }
        });

        btn_Medicine_Pt.setText("8.領藥單(給病患)");
        btn_Medicine_Pt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Medicine_PtActionPerformed(evt);
            }
        });

        btn_Lab_Pt.setText("6.檢驗單(給病患)");
        btn_Lab_Pt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Lab_PtActionPerformed(evt);
            }
        });

        btn_Xray_Pt.setText("4.X-Ray(給病患)");
        btn_Xray_Pt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Xray_PtActionPerformed(evt);
            }
        });

        btn_History.setText("3.調閱單(病歷室)");
        btn_History.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_HistoryActionPerformed(evt);
            }
        });

        btn_All_His.setText("10.病歷列印(給病歷室)");
        btn_All_His.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_All_HisActionPerformed(evt);
            }
        });

        btn_Lab_His.setText("7.檢驗結果單(給病歷室)");
        btn_Lab_His.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Lab_HisActionPerformed(evt);
            }
        });

        btn_Xray_His.setText("5.X-Ray結果單(給病歷室)");
        btn_Xray_His.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Xray_HisActionPerformed(evt);
            }
        });

        btn_Medicine_Package.setText("9.藥單(藥袋上)");
        btn_Medicine_Package.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Medicine_PackageActionPerformed(evt);
            }
        });

        jButton1.setText("11.藥品收據");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        dateChooser1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dateChooser1MouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                dateChooser1MouseReleased(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(120, 120, 120)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(dateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                            .addComponent(btn_Flow, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                            .addComponent(btn_History, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                            .addComponent(btn_Dia, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                            .addComponent(btn_Medicine_Pt, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                            .addComponent(btn_All_His, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                            .addComponent(btn_Xray_Pt, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                            .addComponent(btn_Xray_His, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btn_Lab_Pt, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                            .addComponent(btn_Lab_His, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                            .addComponent(btn_Medicine_Package, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE))
                        .addGap(125, 125, 125))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(dateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_Dia)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Flow)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_History)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Xray_Pt)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Xray_His)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Lab_Pt)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Lab_His)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_Medicine_Pt)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Medicine_Package)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_All_His)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addGap(69, 69, 69))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_DiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_DiaActionPerformed
        m_Type = 1;
        DoPrint(m_Type);
    }//GEN-LAST:event_btn_DiaActionPerformed

    private void btn_HistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_HistoryActionPerformed
       m_Type = 3;
        DoPrint(m_Type);
    }//GEN-LAST:event_btn_HistoryActionPerformed

    private void btn_FlowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_FlowActionPerformed
        m_Type = 2;
        DoPrint(m_Type);
    }//GEN-LAST:event_btn_FlowActionPerformed

    private void btn_Xray_PtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Xray_PtActionPerformed
        m_Type = 4;
        DoPrint(m_Type);
    }//GEN-LAST:event_btn_Xray_PtActionPerformed

    private void btn_Xray_HisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Xray_HisActionPerformed
        m_Type = 5;
        DoPrint(m_Type);
    }//GEN-LAST:event_btn_Xray_HisActionPerformed

    private void btn_Lab_PtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Lab_PtActionPerformed
        m_Type = 6;
        DoPrint(m_Type);
    }//GEN-LAST:event_btn_Lab_PtActionPerformed

    private void btn_Lab_HisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Lab_HisActionPerformed
        m_Type = 7;
        DoPrint(m_Type);
    }//GEN-LAST:event_btn_Lab_HisActionPerformed

    private void btn_Medicine_PtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Medicine_PtActionPerformed
        m_Type = 8;
        DoPrint(m_Type);
    }//GEN-LAST:event_btn_Medicine_PtActionPerformed

    private void btn_Medicine_PackageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Medicine_PackageActionPerformed
        m_Type = 9;
        DoPrint(m_Type);
    }//GEN-LAST:event_btn_Medicine_PackageActionPerformed

    private void btn_All_HisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_All_HisActionPerformed
        m_Type = 10;
        DoPrint(m_Type);
    }//GEN-LAST:event_btn_All_HisActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       m_Type = 11;
        DoPrint(m_Type);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void dateChooser1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dateChooser1MouseClicked
        System.out.println("zxzxzxzx");
}//GEN-LAST:event_dateChooser1MouseClicked

    private void dateChooser1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dateChooser1MouseReleased
        System.out.println("zxzxzxcxcxcxxccxcxcxzx");
    }//GEN-LAST:event_dateChooser1MouseReleased

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Frm_PrintTest().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_All_His;
    private javax.swing.JButton btn_Dia;
    private javax.swing.JButton btn_Flow;
    private javax.swing.JButton btn_History;
    private javax.swing.JButton btn_Lab_His;
    private javax.swing.JButton btn_Lab_Pt;
    private javax.swing.JButton btn_Medicine_Package;
    private javax.swing.JButton btn_Medicine_Pt;
    private javax.swing.JButton btn_Xray_His;
    private javax.swing.JButton btn_Xray_Pt;
    private cc.johnwu.date.DateChooser dateChooser1;
    private javax.swing.JButton jButton1;
    // End of variables declaration//GEN-END:variables

     class MyPrintable implements Printable {

        public int print(Graphics g, PageFormat pf, int pageIndex) {
            int x = 0;
            int y = 0;   // 起始值
            int space = 0;//間距
            int rowNo = 0;
            if (pageIndex != 0) return NO_SUCH_PAGE;
            Graphics2D g2 = (Graphics2D) g;
            g2.setFont(new Font("Serif", Font.PLAIN, 10));
            g2.setPaint(Color.black);
            int i = 100;
            
            switch(m_Type) {
                case 1:  // 1.待診單(給病患)
                    setLogoTitle(g2, "Single Registration 《Local》");
                    x = 150;
                    y = 210;   // 起始值
                    space = 50;// 間距
                    g2.setFont(new Font("Serif", Font.PLAIN, 30));
                    g2.drawString("Patient No.:" , x, y += space);
                    g2.drawString("Name:" , x, y += space);
                    g2.drawString("Gender:" , x, y += space);
                    g2.drawString("Birth:" , x, y += space);
                    g2.drawString("Age.:" , x, y += space);

                    x = 750;
                    y = 190;
                    space = 70;// 間距
                    g2.setFont(new Font("Serif", Font.PLAIN, 48));
                    g2.drawString("Date:" , x, y += space);
                    g2.drawString("Shift:" , x, y += space);
                    g2.drawString("Dept.:" , x, y += space);
                    g2.drawString("Clinic:" , x, y += space);
                    g2.drawString("Doctor:" , x, y += space);
                    g2.drawString("Waiting No.:" , x, y += space);

                    x = 250;
                    y = 210;   // 起始值
                   space = 50;// 間距
                    g2.setFont(new Font("Serif", Font.BOLD, 30));
                    g2.drawString("00000001" , x + 45, y += space);
                    g2.drawString("George Clooney" , x - 15, y += space);
                    g2.drawString("F" , x + 5, y += space);
                    g2.drawString("1978/05/23" , x - 30, y += space);
                    g2.drawString("38" , x - 30, y += space);

                    x = 950;
                    y = 190;
                    space = 70;// 間距
                    g2.setFont(new Font("Serif", Font.BOLD, 48));
                    g2.drawString("2010/09/14" , x-70, y += space);
                    g2.drawString("Morning" , x-65, y += space);
                    g2.drawString("Child Health" , x-65, y += space);
                    g2.drawString("01" , x-35, y += space);
                    g2.drawString("Barack Obama" , x-35, y += space);
                    g2.drawString("1" , x+70, y += space);
                    break;
                case 2:  // 2.流程單(給病患)
                    // -------------　列印表單名稱 --------------
                    setLogoTitle(g2, "Single Process");
                    x = 50;
                    y = 190;   // 起始值
                    space = 50;//間距
                    g2.setFont(new Font("Serif", Font.PLAIN, 28));
                    g2.drawString("Patient No.:" , x, y += space);
                    g2.drawString("Name:" , x, y += space);
                    g2.drawString("Date:" , x, y += space);
                    g2.drawString("Shift:" , x, y += space);
                    g2.drawString("Dept.:" , x, y += space);
                    g2.drawString("Clinic:" , x, y += space);
                    g2.drawString("Doctor:" , x, y += space);
   

                    x = 150;
                    y = 190;   // 起始值
                    space = 50;//間距
                    g2.setFont(new Font("Serif", Font.BOLD, 32));
                    g2.drawString("00000001" , x + 45, y += space);
                    g2.drawString("George Clooney" , x - 20, y += space);
                    g2.drawString("2010/09/14" , x - 20, y += space);
                    g2.drawString("Morning" , x - 20, y += space);
                    g2.drawString("Child Health" , x - 20, y += space);
                    g2.drawString("01" , x , y += space);
                    g2.drawString("Barack Obama" , x , y += space);
      

                    x = 700;
                    y = 175;   // 起始值
                    space = 70;//間距
                    g2.setFont(new Font("Serif", Font.BOLD, 48));
                    g2.drawString("Case Management" , x, y += space);
                    g2.drawString("Clinic" , x, y += space);
                    g2.drawString("Laboratory" , x, y += space);
                    g2.drawString("Pharmacy" , x, y += space);

                    x = 1100;
                    y = 175;   // 起始值
                    g2.drawString("-----------□" , x, y += space);
                    g2.drawString("-----------□" , x, y += space);
                    g2.drawString("-----------□" , x, y += space);
                    g2.drawString("-----------□" , x, y += space);
            
                    break;
                case 3:  // 3.調閱單(病歷室)
                    // -------------　列印表單名稱 --------------
                    g2.setFont(new Font("Serif", Font.PLAIN, 12));
                    i += 15;
                    g2.drawString("Patient No.：00000001", 80, i);
                    i += 15;
                    g2.drawString("Name：George Clooney (F)", 80, i);
                    i += 15;
                    g2.drawString("Birthday：1978/05/23", 80, i);
                    i += 15;
                    g2.drawString("Register Time：2010/09/14", 80, i);
                   
                    i += 15;
                    g2.drawString("==========================================================================================" +
                            "==========================================================================================" , 0, i);
                    i += 15;
                    g2.drawString("Date：2010/09/14", 80, i);
                    i += 15;
                    g2.drawString("Shift：Morning", 80, i);
                    i += 15;
                    g2.drawString("Dept.：Child Health", 80, i);
                    i += 15;
                    g2.drawString("Clinic：01", 80, i);
                    

                    i += 95;
                    g2.drawString("Patient No.：00000001", 80, i);
                    i += 15;
                    g2.drawString("Name：George Clooney (F)", 80, i);
                    i += 15;
                    g2.drawString("Birthday：1978/05/23", 80, i);
                    i += 15;
                    g2.drawString("Register Time：2010/09/14", 80, i);

                    i += 15;
                    g2.drawString("==========================================================================================" +
                            "==========================================================================================" , 0, i);
                    i += 15;
                    g2.drawString("Date：2010/09/14", 80, i);
                    i += 15;
                    g2.drawString("Shift：Morning", 80, i);
                    i += 15;
                    g2.drawString("Dept.：Child Health", 80, i);
                    i += 15;
                    g2.drawString("Clinic：01", 80, i);
                    g2.setFont(new Font("Serif", Font.BOLD, 16));
                    g2.drawString("Access History 《Local》", 80, 100);
                    g2.setFont(new Font("Serif", Font.BOLD, 16));
                    g2.drawString("Access History 《Local》", 80, 310);
                    
                    try {
                        BufferedImage bSrc;
                        g2.drawString("---------------------------------------------------------------------------------------------" +
                            "---------------------------------------------------------------------------------------------" , 80, 260);
                        bSrc = ImageIO.read(new File("./img/cut.png"));
                        AffineTransform at = new AffineTransform();
                        g2.translate(80, 250);
                        g2.scale(0.2, 0.2);
                        g2.drawRenderedImage(bSrc, at);
                        
                    } catch (IOException ex) {
                        Logger.getLogger(Frm_PrintTest.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    break;
                case 4:  // 4.X-Ray(給病患)
                    setLogoTitle(g2, "Radiology (To The Radiology Department)");
                    x = 60;
                    y = setPatientInfoTitle(g2,-1);
                    space = 50;//間距
                    g2.setFont(new Font("Serif", Font.PLAIN, 32));  // 表頭
                    g2.drawString("Urgent" , x - 45, y);
                    g2.drawString("Item" , x + 70, y);

                    g2.setFont(new Font("Serif", Font.BOLD, 32));  // 資料
                    y += 40;
                    
                    rowNo = 0;
                    while (rowNo++ < 3) {
                        g2.drawString(rowNo + ". " + "40006 Ultrasound Scan" , x+75 , y);
                        y += space;
                    }
                    break;
                case 5:  // 5.X-Ray結果單(給病歷室)
                    setLogoTitle(g2, "Radiology Data(To The Medical Records)");
                    x = 60;
                    y = setPatientInfoTitle(g2,-1);
                    space = 100;//間距
                    g2.setFont(new Font("Serif", Font.PLAIN, 32));  // 表頭
                    g2.drawString("Urgent" , x-45, y);
                    g2.drawString("Item" , x + 70, y);
                    g2.drawString("Normal" , x + 1300, y);

                    g2.setFont(new Font("Serif", Font.BOLD, 32));  // 資料
                    y += 40;

                    rowNo = 0;
//                    while (rowNo++ < 3) {
                       // g2.drawString(rowNo + ". " + "40006 Ultrasound Scan" , x+75 , y);
                        g2.drawString(rowNo + "1. " + "40006 Ultrasound Scan" , x+75 , y);
                        g2.setFont(new Font("Serif", Font.PLAIN, 32));  // 資料
                        g2.drawString("Result:" , x + 120, y+50);
                        g2.setFont(new Font("Serif", Font.BOLD, 32));  // 資料
                        g2.drawString("Y" , x + 1320, y);
                        String result = "Ultrasound travels freely through fluid and soft tissues. However, ultrasound is reflected back (it bounces back as 'echoes') when it hits a more solid (dense) surface. For example, the ultrasound will travel freely though blood in a heart chamber. But, when it hits a solid valve, a lot of the ultrasound echoes back. Another example is that when ultrasound travels though bile in a gallbladder it will echo back strongly if it hits a solid gallstone.";
                        y = getLineBreaksWord(g2,result, 80 , x + 220, y , 50);
                        y += space;

                        g2.drawString( "2. " + "40006 Ultrasound Scan" , x+75 , y);
                        g2.setFont(new Font("Serif", Font.PLAIN, 32));  // 資料
                        g2.drawString("Result:" , x + 120, y+50);
                        g2.setFont(new Font("Serif", Font.BOLD, 32));  // 資料
                        g2.drawString("Y" , x + 1320, y);
                        result = "Ultrasound is a high frequency sound that you cannot hear, but it can be emitted and detected by special machines.";
                        y = getLineBreaksWord(g2,result, 80 , x + 220, y , 50);
                        y += space;
//                    }

                    break;
                case 6:  // 6.檢驗單(給病患)
                    setLogoTitle(g2, "Laboratory (To The Laboratory)");
                    x = 60;
                    y = setPatientInfoTitle(g2,-1);
                    space = 50;//間距
                    g2.setFont(new Font("Serif", Font.PLAIN, 32));  // 表頭
                    g2.drawString("Urgent" , x-45, y);
                    g2.drawString("Item" , x + 70, y);
                    g2.drawString("Type" , x + 1100, y);

                    g2.setFont(new Font("Serif", Font.BOLD, 32));  // 資料
                    y += 40;

                    rowNo = 0;
                    while (rowNo++ < 3) {
                        g2.drawString(rowNo + ". " + "10002 Blood Film Comments" , x + 75 , y);
                        g2.drawString("HAEMATOLOGY" , x + 1105, y);
                        y += space;
                    }
                    break;
                case 7:  // 7.檢驗結果單(給病歷室)
                    setLogoTitle(g2, "Laboratory Data (To The Medical Records)");
                    x = 60;
                    y = setPatientInfoTitle(g2,-1);
                    space = 140;//間距
                    g2.setFont(new Font("Serif", Font.PLAIN, 32));  // 表頭
                    g2.drawString("Urgent" , x-45, y);
                    g2.drawString("Item" , x + 70, y);
                    g2.drawString("Type" , x+900, y);
                    g2.drawString("Normal" , x + 1300, y);

                    g2.setFont(new Font("Serif", Font.BOLD, 32));  // 資料
                    y += 40;

                    rowNo = 0;
                    while (rowNo++ < 3) {
                        g2.drawString(rowNo + ". " + "10002 Blood Film Comments" , x + 75 , y);
                        g2.drawString("HAEMATOLOGY" , x + 905, y);
                        g2.setFont(new Font("Serif", Font.PLAIN, 32));  // 資料
                        g2.drawString("Normal Range:" , x + 900 , y+50);
                        g2.drawString("Result:" , x + 120 , y+50);
                        g2.setFont(new Font("Serif", Font.BOLD, 32));  // 資料
                        g2.drawString("10 ~ 20 mg/dl" ,x + 1110 , y+50);
                        g2.drawString("18.20" ,  x + 230, y+50);
                        g2.drawString("Y" , x + 1320, y);
                        y += space;
                    }
                    break;
                case 8:  // 8.領藥單(給病患)
                    setLogoTitle(g2, "Medicine (To The Pharmacy)");
                    x = 10;
                    y = setPatientInfoTitle(g2,-1);
                    g2.setFont(new Font("---------------", Font.BOLD, 45));  // 資料
                    g2.drawString("Pick No. : " + "1" , 1180, 50);  // 領藥號
                    space = 110;//間距
                    g2.setFont(new Font("Serif", Font.PLAIN, 28));  // 表頭
                    g2.drawString("Urgent" , x-10, y);
                    g2.drawString("Drug Name" , x + 80, y);
                    g2.drawString("Dosage" , x+530, y);
                    g2.drawString("Frequency" , x + 700, y);
                    g2.drawString("Usage" , x + 1020, y);
                    g2.drawString("Duration" , x + 1150, y);
                    g2.drawString("Quantity" , x + 1280, y);
                    g2.drawString("Powder" , x + 1420, y);

                    g2.setFont(new Font("Serif", Font.BOLD, 28));  // 資料
                    y += 50;

                    rowNo = 0;
                    while (rowNo++ < 3) {
                        g2.drawString(rowNo + ". " + "clofazimine Capsule: 50 mg; 100 mg." , x + 85 , y);
                        g2.drawString("1" , x + 535 , y+40);
                        g2.drawString("BID" , x + 705, y+40);
                        g2.drawString("IV" , x + 1025, y+40);
                        g2.drawString("3" , x + 1155, y+40);
                        g2.drawString("100" , x + 1285, y+40);
                        g2.drawString("Y" , x + 1425, y+40);
                        y += space;
                    }

                    break;
                case 9:  // 9.藥單(藥袋上)
                    setLogoTitle(g2, "Medicine Package");
                    x = 10;
                    y = setPatientInfoTitle(g2,-1);

                    g2.setFont(new Font("Serif", Font.BOLD, 30));  // 資料
                    g2.setFont(new Font("---------------", Font.BOLD, 45));  // 資料
                    g2.drawString("Pick No. : " + "1" , 1180, 50);  // 領藥號

                    space = 110;//間距
                    g2.setFont(new Font("Serif", Font.PLAIN, 28));  // 表頭
                    g2.drawString("Urgent" , x-10, y);
                    g2.drawString("Drug Name" , x + 80, y);
                    g2.drawString("Dosage" , x+530, y);
                    g2.drawString("Frequency" , x + 700, y);
                    g2.drawString("Usage" , x + 1020, y);
                    g2.drawString("Duration" , x + 1150, y);
                    g2.drawString("Quantity" , x + 1280, y);
                    g2.drawString("Powder" , x + 1420, y);

                    g2.setFont(new Font("Serif", Font.BOLD, 28));  // 資料
                    y += 50;

                    rowNo = 0;
                    while (rowNo++ < 3) {
                        g2.drawString(rowNo + ". " + "clofazimine Capsule: 50 mg; 100 mg." , x + 85 , y);
                        g2.drawString("1" , x + 560 , y+40);
                        g2.drawString("2 times per day" , x + 705, y+40);
                        g2.drawString("IV" , x + 1025, y+40);
                        g2.drawString("3" , x + 1155, y+40);
                        g2.drawString("100" , x + 1285, y+40);
                        g2.drawString("Y" , x + 1425, y+40);
                        y += space;
                    }
                   
                    break;
                case 10:  // 10.病歷列印(給病歷室)
                    setLogoTitle(g2, "Prescription Note");
                    x = 10;
                    y = setPatientInfoTitle(g2,-1);
                    g2.setFont(new Font("Serif", Font.BOLD, 28));  // 表頭
                    g2.drawString("======== Summary =========================" +
                            "============================================================================" , x, y);
                    y += 50;
                    g2.setFont(new Font("Serif", Font.BOLD, 28));  // 資料
                    g2.drawString("TEST" , x + 5 , y);
                    y += 40;

                    space = 50;//間距
                    g2.setFont(new Font("Serif", Font.BOLD, 28));  // 表頭
                    g2.drawString("======== Diagnosis =========================" +
                            "============================================================================" , x, y);
                    y += 50;
                    g2.setFont(new Font("Serif", Font.BOLD, 28));  // 資料
                    rowNo = 0;
                    while (rowNo++ < 3) {
                        g2.drawString(rowNo + ". " + "002.2 CHOLERA, UNSPECIFIED" , x + 75 , y);
                        y += space;
                    }
                    
                    space = 50;//間距

                    g2.setFont(new Font("Serif", Font.BOLD, 28));  // 表頭
                    g2.drawString("======== Laboratory =================================" +
                            "============================================================================" , x, y);
                    y += 50;
                    g2.setFont(new Font("Serif", Font.PLAIN, 28));  // 表頭
                    g2.drawString("Urgent" , x-10, y);
                    g2.drawString("Item" , x + 80, y);
                    g2.drawString("Type" , x + 1100, y);

                    g2.setFont(new Font("Serif", Font.BOLD, 28));  // 資料
                    y += 40;

                    rowNo = 0;
                    while (rowNo++ < 3) {
                        g2.drawString(rowNo + ". " + "10002 Blood Film Comments" , x + 85 , y);
                        g2.drawString("HAEMATOLOGY" , x + 1105, y);
                        y += space;
                    }
                    x = 10;
                   space = 50;//間距
                    g2.setFont(new Font("Serif", Font.BOLD, 28));  // 表頭
                    g2.drawString("======== Radiology =================================" +
                            "============================================================================" , x, y);
                    y += 50;

                    g2.setFont(new Font("Serif", Font.PLAIN, 28));  // 表頭
                    g2.drawString("Urgent" , x-10, y);
                    g2.drawString("Item" , x + 80, y);

                    g2.setFont(new Font("Serif", Font.BOLD, 28));  // 資料
                    y += 40;

                    rowNo = 0;
                    while (rowNo++ < 3) {
                        g2.drawString(rowNo + ". " + "40006 Ultrasound Scan" , x+85 , y);
                        y += space;
                    }

                    x = 10;
                    space = 110;//間距
                    g2.setFont(new Font("Serif", Font.BOLD, 28));  // 表頭
                    g2.drawString("======== Medicine =================================" +
                            "============================================================================" , x, y);
                    y += 50;
                    g2.setFont(new Font("Serif", Font.PLAIN, 28));  // 表頭
                    g2.drawString("Urgent" , x-10, y);
                    g2.drawString("Drug Name" , x + 80, y);
                    g2.drawString("Dosage" , x+530, y);
                    g2.drawString("Frequency" , x + 700, y);
                    g2.drawString("Usage" , x + 1020, y);
                    g2.drawString("Duration" , x + 1150, y);
                    g2.drawString("Quantity" , x + 1280, y);
                    g2.drawString("Powder" , x + 1420, y);

                    g2.setFont(new Font("Serif", Font.BOLD, 28));  // 資料
                    y += 50;

                    rowNo = 0;
                    while (rowNo++ < 3) {
                        g2.drawString(rowNo + ". " + "clofazimine Capsule: 50 mg; 100 mg." , x + 85 , y);
                        g2.drawString("1" , x + 535 , y+40);
                        g2.drawString("BID" , x + 705, y+40);
                        g2.drawString("IV" , x + 1025, y+40);
                        g2.drawString("3" , x + 1155, y+40);
                        g2.drawString("100" , x + 1285, y+40);
                        g2.drawString("Y" , x + 1425, y+40);
                        y += space;
                    }


                    break;
                case 11:  // 11.藥品收據
                    setLogoTitle(g2, "Drug Receipt 《Counterfoil》");
                    x = 10;
                    y = setPatientInfoTitle(g2,-1);
                    space = 150;//間距
                    g2.setFont(new Font("Serif", Font.PLAIN, 28));  // 表頭
                    g2.drawString("Urgent" , x-10, y);
                    g2.drawString("Drug Name" , x + 75, y);
                    g2.drawString("Dosage" , x+530, y);
                    g2.drawString("Frequency" , x + 700, y);
                    g2.drawString("Usage" , x + 1020, y);
                    g2.drawString("Duration" , x + 1150, y);
                    g2.drawString("Quantity" , x + 1280, y);
                    g2.drawString("Powder" , x + 1420, y);

                    g2.setFont(new Font("Serif", Font.BOLD, 28));  // 資料
                    y += 50;

                    rowNo = 0;
                    while (rowNo++ < 3) {
                        g2.drawString(rowNo + ". " + "clofazimine Capsule: 50 mg; 100 mg." , x + 80 , y);
                        g2.drawString("1" , x + 535 , y+40);
                        g2.drawString("BID" , x + 705, y+40);
                        g2.drawString("IV" , x + 1025, y+40);
                        g2.drawString("3" , x + 1155, y+40);
                        g2.drawString("100" , x + 1285, y+40);
                        g2.drawString("Y" , x + 1425, y+40);
                        g2.drawString("Cost:" , x + 1280, y+90);
                        g2.drawRect(x + 1355, y+65, 160, 33);

                        y += space;
                    }
                    g2.drawString("Sum:____________________" , x + 1230, y+=30);


                    int yLine =y + 60;  // 列印剪下線
                    y += 60;
                    
                    g2.setFont(new Font("Serif", Font.BOLD, 40));
                    g2.drawString("Drug Receipt" , 650, y+80);
                   // setLogoTitle(g2, "Drug Receipt");
                    x = 10;
                    y = setPatientInfoTitle(g2,y+=100);
                    space = 150;//間距
                    g2.setFont(new Font("Serif", Font.PLAIN, 28));  // 表頭
                    g2.drawString("Urgent" , x-10, y);
                    g2.drawString("Drug Name" , x + 75, y);
                    g2.drawString("Dosage" , x+530, y);
                    g2.drawString("Frequency" , x + 700, y);
                    g2.drawString("Usage" , x + 1020, y);
                    g2.drawString("Duration" , x + 1150, y);
                    g2.drawString("Quantity" , x + 1280, y);
                    g2.drawString("Powder" , x + 1420, y);

                    g2.setFont(new Font("Serif", Font.BOLD, 28));  // 資料
                    y += 50;

                    rowNo = 0;
                    while (rowNo++ < 3) {
                        g2.drawString(rowNo + ". " + "clofazimine Capsule: 50 mg; 100 mg." , x + 80 , y);
                        g2.drawString("1" , x + 535 , y+40);
                        g2.drawString("BID" , x + 705, y+40);
                        g2.drawString("IV" , x + 1025, y+40);
                        g2.drawString("3" , x + 1155, y+40);
                        g2.drawString("100" , x + 1285, y+40);
                        g2.drawString("Y" , x + 1425, y+40);
                        g2.drawString("Cost:" , x + 1280, y+90);
                        g2.drawRect(x + 1355, y+65, 160, 33);

                        y += space;
                    }
                    g2.drawString("Sum:____________________" , x + 1230, y+=30);


                    g2.setFont(new Font("Serif", Font.PLAIN, 28));  // 表頭
                    try {
                        BufferedImage bSrc;
                        g2.drawString("---------------------------------------------------------------------------------------------" +
                            "---------------------------------------------------------------------------------------------" , 80, yLine);
                        bSrc = ImageIO.read(new File("./img/cut.png"));
                        AffineTransform at = new AffineTransform();
                        g2.translate(0, yLine-30);
                        g2.scale(0.6, 0.6);
                        g2.drawRenderedImage(bSrc, at);

                    } catch (IOException ex) {
                        Logger.getLogger(Frm_PrintTest.class.getName()).log(Level.SEVERE, null, ex);
                    }
                 

                    break;
                 }
            return PAGE_EXISTS;
            }

       

            private int setPatientInfoTitle(Graphics2D g2, int sety) {
                int x = 130;
                int y = 180;   // 起始值
                int space = 60;//間距

                if (sety != -1) {
                    y = sety;
                }

                g2.setFont(new Font("Serif", Font.PLAIN, 36));
//                g2.drawString("Modify", 1300, 100);
//                g2.drawString("Reprint", 1300, 140);
                g2.drawString("Unpaid", 1300, 180);
                
                g2.drawString("Patient No.:" , x, y += space);
                g2.drawString("Name:" , x, y += space);
                g2.drawString("Gender:" , x, y += space);
                g2.drawString("Birth:" , x, y += space);
                g2.drawString("Age:" , x, y += space);

                x = 820;
                y = 180;
                if (sety != -1) {
                    y = sety;
                }
                g2.drawString("Date:" , x, y += space);
                g2.drawString("Shift:" , x, y += space);
                g2.drawString("Dept.:" , x, y += space);
                g2.drawString("Clinic:" , x, y += space);
                g2.drawString("Doctor:" , x, y += space);
                

                g2.setFont(new Font("Serif", Font.BOLD, 32));

                x = 320;
                y = 180;
                if (sety != -1) {
                    y = sety;
                }
                g2.drawString("00000001" , x, y += space);
                g2.drawString("George Clooney" , x-75, y += space);
                g2.drawString("F" , x-55, y += space);
                g2.drawString("1978/05/23" , x-80, y += space);
                g2.drawString("38" , x-100, y += space);

                x = 960;
                y = 180;
                if (sety != -1) {
                    y = sety;
                }
                g2.drawString("2010/09/14" , x-40, y += space);
                g2.drawString("Morning" , x-35, y += space);
                g2.drawString("Child Health" , x-30, y += space);
                g2.drawString("01" , x-20, y += space);
                g2.drawString("Barack Obama" , x-10, y += space);
                g2.drawString("========================================" +
                        "==============================================================" , 0, y += space);
                return (y + space);
            }

            private void setLogoTitle(Graphics2D g2, String tabName) {
                String hosName = "KORLE BU TEACHING HOSPITAL"; // 醫院名稱
                try {
                    // -------------　列印醫院LOGO --------------
                    BufferedImage bSrc = ImageIO.read(new File("./img/hospitallogo.png"));
                    AffineTransform at = new AffineTransform();
                    g2.translate(80, 100);
                    g2.scale(0.3, 0.3);
                    g2.drawRenderedImage(bSrc, at);
                    // -------------　列印醫院名稱 --------------
                    g2.setFont(new Font("Serif", Font.BOLD, 48));
                    g2.drawString(hosName , 210, 90);
                    // -------------　列印表單名稱 --------------
                    g2.setFont(new Font("Serif", Font.BOLD, 40));
                    g2.drawString(tabName , 400, 150);
                } catch (IOException ex) {
                     Logger.getLogger(Frm_PrintTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }



