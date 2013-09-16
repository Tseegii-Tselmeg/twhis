package Diagnosis;

import cc.johnwu.sql.DBC;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;


public class Frm_Explain extends javax.swing.JFrame {
 
    public Frm_Explain() throws IOException {
        initComponents();
       


        this.setLocationRelativeTo(null) ;
//        try {
//            ResultSet rs = DBC.executeQuery("SELECT dm_drugtip FROM setting WHERE id = 1");
//           rs.next();
//            this.edit_DMTip.setText(rs.getString("dm_drugtip"));
//        } catch (SQLException ex) {
//            Logger.getLogger(Frm_Explain.class.getName()).log(Level.SEVERE, null, ex);
//        }

        ImageIcon imageIcon = new ImageIcon("./img/explain.png");    // Icon由图片文件形成
        Image image = imageIcon.getImage();                         // 但这个图片太大不适合做Icon
        //    为把它缩小点，先要取出这个Icon的image ,然后缩放到合适的大小
        Image smallImage = image.getScaledInstance(750,795,Image.SCALE_AREA_AVERAGING);
        //    再由修改后的Image来生成合适的Icon
        ImageIcon smallIcon = new ImageIcon(smallImage);
        //   最后设置它为按钮的图片
        JLabel jl = new JLabel(smallIcon);
        jl.setBounds(0, 0, smallIcon.getIconWidth(), smallIcon.getIconHeight());
        pan.add(jl, BorderLayout.CENTER);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        spn_Explain = new javax.swing.JScrollPane();
        pan = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea3 = new javax.swing.JTextArea();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea4 = new javax.swing.JTextArea();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTextArea5 = new javax.swing.JTextArea();
        jScrollPane16 = new javax.swing.JScrollPane();
        jTextArea16 = new javax.swing.JTextArea();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTextArea6 = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTextArea7 = new javax.swing.JTextArea();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTextArea8 = new javax.swing.JTextArea();
        jScrollPane17 = new javax.swing.JScrollPane();
        jTextArea17 = new javax.swing.JTextArea();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTextArea9 = new javax.swing.JTextArea();
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane10 = new javax.swing.JScrollPane();
        jTextArea10 = new javax.swing.JTextArea();
        jScrollPane11 = new javax.swing.JScrollPane();
        jTextArea11 = new javax.swing.JTextArea();
        jScrollPane18 = new javax.swing.JScrollPane();
        jTextArea18 = new javax.swing.JTextArea();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        jTextArea12 = new javax.swing.JTextArea();
        jScrollPane13 = new javax.swing.JScrollPane();
        jTextArea13 = new javax.swing.JTextArea();
        jScrollPane14 = new javax.swing.JScrollPane();
        jTextArea14 = new javax.swing.JTextArea();
        jScrollPane15 = new javax.swing.JScrollPane();
        jTextArea15 = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane19 = new javax.swing.JScrollPane();
        jTextArea19 = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
        setBackground(new java.awt.Color(222, 252, 222));
        setMinimumSize(new java.awt.Dimension(860, 660));
        setResizable(false);

        jPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jButton1.setText("Close");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 682, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
        );

        pan.setBackground(new java.awt.Color(255, 255, 255));
        pan.setMinimumSize(new java.awt.Dimension(750, 795));

        jTextField1.setEditable(false);
        jTextField1.setFont(new java.awt.Font("新細明體", 1, 10));
        jTextField1.setText("REVIEW AND SET GLYCAEMIC TARGET:HbA1c < 7%(53mmol/mol) OR INDIVIDUALISED AS AGREED");

        jTextField2.setEditable(false);
        jTextField2.setFont(new java.awt.Font("Serif", 0, 10)); // NOI18N
        jTextField2.setText("30");
        jTextField2.setPreferredSize(new java.awt.Dimension(17, 15));

        jTextField3.setEditable(false);
        jTextField3.setFont(new java.awt.Font("Serif", 0, 10));
        jTextField3.setText("10");
        jTextField3.setPreferredSize(new java.awt.Dimension(17, 15));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(new javax.swing.border.MatteBorder(null));

        jLabel1.setFont(new java.awt.Font("新細明體", 1, 10)); // NOI18N
        jLabel1.setText("1 st LINE OPTIONS in addition to lifestyle meas ures;START ONE OF");

        jTextArea1.setBackground(new java.awt.Color(240, 240, 240));
        jTextArea1.setColumns(10);
        jTextArea1.setFont(new java.awt.Font("新細明體", 1, 10)); // NOI18N
        jTextArea1.setRows(3);
        jTextArea1.setText("Metformin (MF)");
        jTextArea1.setBorder(null);
        jScrollPane1.setViewportView(jTextArea1);

        jScrollPane2.setBackground(new java.awt.Color(231, 231, 214));

        jTextArea2.setBackground(new java.awt.Color(231, 231, 214));
        jTextArea2.setColumns(30);
        jTextArea2.setFont(new java.awt.Font("新細明體", 1, 10)); // NOI18N
        jTextArea2.setRows(3);
        jTextArea2.setText("Sulphonylurea \" (SU)\n* If intolerant of metformin or\n* If weight loss/os.motic symptoms");
        jTextArea2.setBorder(null);
        jScrollPane2.setViewportView(jTextArea2);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1))
                .addContainerGap(258, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1)
                    .addComponent(jScrollPane2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTextArea3.setBackground(new java.awt.Color(239, 214, 198));
        jTextArea3.setColumns(25);
        jTextArea3.setFont(new java.awt.Font("新細明體", 1, 10)); // NOI18N
        jTextArea3.setRows(9);
        jTextArea3.setText("　　　－－－－　　　　\n　　｜　　　　｜　　　\n　　｜　　　　｜　　　Review and if\n－－　　　　　　－－　 not reaching\n＼　　　　　　　　／　target move to\n　＼　　　　　　／　　   2nd line\n　　＼　　　　／　　　\n　　　＼　　／　　　　\n　　　　＼／　　　　　");
        jScrollPane3.setViewportView(jTextArea3);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(new javax.swing.border.MatteBorder(null));

        jLabel2.setFont(new java.awt.Font("新細明體", 1, 10)); // NOI18N
        jLabel2.setText("2 nd LINE OPTIONS in addition to lifestyle meas ures;START ONE OF");

        jTextArea4.setBackground(new java.awt.Color(240, 240, 240));
        jTextArea4.setColumns(10);
        jTextArea4.setFont(new java.awt.Font("新細明體", 1, 10)); // NOI18N
        jTextArea4.setRows(3);
        jTextArea4.setText("Metformin (MF)");
        jTextArea4.setBorder(null);
        jScrollPane4.setViewportView(jTextArea4);

        jScrollPane5.setBackground(new java.awt.Color(231, 231, 214));

        jTextArea5.setBackground(new java.awt.Color(231, 231, 214));
        jTextArea5.setColumns(30);
        jTextArea5.setFont(new java.awt.Font("新細明體", 1, 10)); // NOI18N
        jTextArea5.setRows(3);
        jTextArea5.setText("Sulphonylurea \" (SU)\n* If intolerant of metformin or\n* If weight loss/os.motic symptoms");
        jTextArea5.setBorder(null);
        jScrollPane5.setViewportView(jTextArea5);

        jScrollPane16.setBackground(new java.awt.Color(231, 231, 214));

        jTextArea16.setBackground(new java.awt.Color(231, 231, 214));
        jTextArea16.setColumns(30);
        jTextArea16.setFont(new java.awt.Font("新細明體", 1, 10)); // NOI18N
        jTextArea16.setRows(3);
        jTextArea16.setText("Sulphonylurea \" (SU)\n* If intolerant of metformin or\n* If weight loss/os.motic symptoms");
        jTextArea16.setBorder(null);
        jScrollPane16.setViewportView(jTextArea16);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jScrollPane4)
                        .addComponent(jScrollPane5))
                    .addComponent(jScrollPane16))
                .addContainerGap())
        );

        jTextArea6.setBackground(new java.awt.Color(239, 214, 198));
        jTextArea6.setColumns(25);
        jTextArea6.setFont(new java.awt.Font("新細明體", 1, 10)); // NOI18N
        jTextArea6.setRows(9);
        jTextArea6.setText("　　　－－－－　　　　\n　　｜　　　　｜　　　\n　　｜　　　　｜　　　Review and if\n－－　　　　　　－－　 not reaching\n＼　　　　　　　　／　target move to\n　＼　　　　　　／　　   2nd line\n　　＼　　　　／　　　\n　　　＼　　／　　　　\n　　　　＼／　　　　　");
        jScrollPane6.setViewportView(jTextArea6);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(new javax.swing.border.MatteBorder(null));

        jLabel3.setFont(new java.awt.Font("新細明體", 1, 10)); // NOI18N
        jLabel3.setText("2 nd LINE OPTIONS in addition to lifestyle meas ures;START ONE OF");

        jTextArea7.setBackground(new java.awt.Color(240, 240, 240));
        jTextArea7.setColumns(10);
        jTextArea7.setFont(new java.awt.Font("新細明體", 1, 10)); // NOI18N
        jTextArea7.setRows(3);
        jTextArea7.setText("Metformin (MF)");
        jTextArea7.setBorder(null);
        jScrollPane7.setViewportView(jTextArea7);

        jScrollPane8.setBackground(new java.awt.Color(231, 231, 214));

        jTextArea8.setBackground(new java.awt.Color(231, 231, 214));
        jTextArea8.setColumns(30);
        jTextArea8.setFont(new java.awt.Font("新細明體", 1, 10)); // NOI18N
        jTextArea8.setRows(3);
        jTextArea8.setText("Sulphonylurea \" (SU)\n* If intolerant of metformin or\n* If weight loss/os.motic symptoms");
        jTextArea8.setBorder(null);
        jScrollPane8.setViewportView(jTextArea8);

        jScrollPane17.setBackground(new java.awt.Color(231, 231, 214));

        jTextArea17.setBackground(new java.awt.Color(231, 231, 214));
        jTextArea17.setColumns(30);
        jTextArea17.setFont(new java.awt.Font("新細明體", 1, 10)); // NOI18N
        jTextArea17.setRows(3);
        jTextArea17.setText("Sulphonylurea \" (SU)\n* If intolerant of metformin or\n* If weight loss/os.motic symptoms");
        jTextArea17.setBorder(null);
        jScrollPane17.setViewportView(jTextArea17);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane17)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jScrollPane7)
                        .addComponent(jScrollPane8)))
                .addContainerGap())
        );

        jTextArea9.setBackground(new java.awt.Color(239, 214, 198));
        jTextArea9.setColumns(25);
        jTextArea9.setFont(new java.awt.Font("新細明體", 1, 10)); // NOI18N
        jTextArea9.setRows(9);
        jTextArea9.setText("　　　－－－－　　　　\n　　｜　　　　｜　　　\n　　｜　　　　｜　　　Review and if\n－－　　　　　　－－　 not reaching\n＼　　　　　　　　／　target move to\n　＼　　　　　　／　　   2nd line\n　　＼　　　　／　　　\n　　　＼　　／　　　　\n　　　　＼／　　　　　");
        jScrollPane9.setViewportView(jTextArea9);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(new javax.swing.border.MatteBorder(null));

        jLabel4.setFont(new java.awt.Font("新細明體", 1, 10)); // NOI18N
        jLabel4.setText("3 rd LINE OPTIONS in addition to lifestyle meas ures;START ONE OF");

        jTextArea10.setBackground(new java.awt.Color(231, 231, 214));
        jTextArea10.setColumns(10);
        jTextArea10.setFont(new java.awt.Font("新細明體", 1, 10)); // NOI18N
        jTextArea10.setRows(3);
        jTextArea10.setText("Metformin (MF)");
        jTextArea10.setBorder(null);
        jScrollPane10.setViewportView(jTextArea10);

        jScrollPane11.setBackground(new java.awt.Color(231, 231, 214));

        jTextArea11.setBackground(new java.awt.Color(231, 231, 214));
        jTextArea11.setColumns(30);
        jTextArea11.setFont(new java.awt.Font("新細明體", 1, 10)); // NOI18N
        jTextArea11.setRows(3);
        jTextArea11.setText("Sulphonylurea \" (SU)\n* If intolerant of metformin or\n* If weight loss/os.motic symptoms");
        jTextArea11.setBorder(null);
        jScrollPane11.setViewportView(jTextArea11);

        jScrollPane18.setBackground(new java.awt.Color(231, 231, 214));

        jTextArea18.setBackground(new java.awt.Color(231, 231, 214));
        jTextArea18.setColumns(30);
        jTextArea18.setFont(new java.awt.Font("新細明體", 1, 10)); // NOI18N
        jTextArea18.setRows(3);
        jTextArea18.setText("Sulphonylurea \" (SU)\n* If intolerant of metformin or\n* If weight loss/os.motic symptoms");
        jTextArea18.setBorder(null);
        jScrollPane18.setViewportView(jTextArea18);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel4))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane18)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jScrollPane10)
                        .addComponent(jScrollPane11)))
                .addContainerGap())
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(new javax.swing.border.MatteBorder(null));

        jTextArea12.setBackground(new java.awt.Color(240, 240, 240));
        jTextArea12.setColumns(5);
        jTextArea12.setFont(new java.awt.Font("新細明體", 1, 10)); // NOI18N
        jTextArea12.setRows(1);
        jTextArea12.setBorder(null);
        jScrollPane12.setViewportView(jTextArea12);

        jScrollPane13.setBackground(new java.awt.Color(231, 231, 214));

        jTextArea13.setBackground(new java.awt.Color(231, 231, 214));
        jTextArea13.setColumns(5);
        jTextArea13.setFont(new java.awt.Font("新細明體", 1, 10)); // NOI18N
        jTextArea13.setRows(1);
        jTextArea13.setBorder(null);
        jScrollPane13.setViewportView(jTextArea13);

        jScrollPane14.setBackground(new java.awt.Color(231, 231, 214));

        jTextArea14.setColumns(5);
        jTextArea14.setFont(new java.awt.Font("新細明體", 1, 10)); // NOI18N
        jTextArea14.setRows(1);
        jTextArea14.setText("    *  ");
        jTextArea14.setBorder(null);
        jScrollPane14.setViewportView(jTextArea14);

        jTextArea15.setColumns(30);
        jTextArea15.setFont(new java.awt.Font("新細明體", 1, 10)); // NOI18N
        jTextArea15.setRows(3);
        jTextArea15.setText("Pxxxxxxxxx should refer to-the Brith Bxxxx Hxxxxxx Fxxxxxxxxy xxxxxxxxxx xxxxx xxxxx Medicine Cxxxxx(xxxxxxxxx\nxxxxxxxxxxxxxxxxx");
        jScrollPane15.setViewportView(jTextArea15);

        jLabel5.setFont(new java.awt.Font("新細明體", 1, 10)); // NOI18N
        jLabel5.setText("Uaul approcah");

        jLabel6.setFont(new java.awt.Font("新細明體", 1, 10)); // NOI18N
        jLabel6.setText("Axxxxxve appxxxxx Spxxxicomxxxxxx");

        jLabel7.setFont(new java.awt.Font("新細明體", 1, 10)); // NOI18N
        jLabel7.setText("Coxxxxx medicine xx xxxxx xxxxxxxxxx xxxxx xxxxxxxxxx HbA1c xx xx xxxxx xx x xxxxxxxxxx in 3-6 mxxxx");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane15, javax.swing.GroupLayout.DEFAULT_SIZE, 650, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel6))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel7)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel7)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTextArea19.setColumns(8);
        jTextArea19.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jTextArea19.setRows(5);
        jTextArea19.setText("     NHS    UK    March    2010    p 7\n\t\t         \n            xxxxxxxxxxxxxxxxxxxx      \n            xxxxxxxxxxxxxxxxxxxx     \n                xxxxxxxxxxxxxxx\t         \n     xxxxxxxxxxxxxxxxxxxxxxxxxx\n                     xxxxxxxx\t          ");
        jScrollPane19.setViewportView(jTextArea19);

        javax.swing.GroupLayout panLayout = new javax.swing.GroupLayout(pan);
        pan.setLayout(panLayout);
        panLayout.setHorizontalGroup(
            panLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panLayout.createSequentialGroup()
                .addGap(905, 905, 905)
                .addGroup(panLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(panLayout.createSequentialGroup()
                        .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 6, Short.MAX_VALUE)
                        .addGap(208, 208, 208))))
            .addGroup(panLayout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addGroup(panLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(383, Short.MAX_VALUE))
            .addGroup(panLayout.createSequentialGroup()
                .addGap(486, 486, 486)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(99, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panLayout.createSequentialGroup()
                .addContainerGap(747, Short.MAX_VALUE)
                .addComponent(jScrollPane19, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(53, 53, 53))
        );
        panLayout.setVerticalGroup(
            panLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane19, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(170, 170, 170)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        spn_Explain.setViewportView(pan);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(spn_Explain, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 702, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(spn_Explain, javax.swing.GroupLayout.DEFAULT_SIZE, 299, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane18;
    private javax.swing.JScrollPane jScrollPane19;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea10;
    private javax.swing.JTextArea jTextArea11;
    private javax.swing.JTextArea jTextArea12;
    private javax.swing.JTextArea jTextArea13;
    private javax.swing.JTextArea jTextArea14;
    private javax.swing.JTextArea jTextArea15;
    private javax.swing.JTextArea jTextArea16;
    private javax.swing.JTextArea jTextArea17;
    private javax.swing.JTextArea jTextArea18;
    private javax.swing.JTextArea jTextArea19;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextArea jTextArea3;
    private javax.swing.JTextArea jTextArea4;
    private javax.swing.JTextArea jTextArea5;
    private javax.swing.JTextArea jTextArea6;
    private javax.swing.JTextArea jTextArea7;
    private javax.swing.JTextArea jTextArea8;
    private javax.swing.JTextArea jTextArea9;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JPanel pan;
    private javax.swing.JScrollPane spn_Explain;
    // End of variables declaration//GEN-END:variables

}
