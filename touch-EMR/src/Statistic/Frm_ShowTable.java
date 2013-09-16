package Statistic;

import Common.TabTools;
import cc.johnwu.login.SysInfo;
import cc.johnwu.sql.DBC;
import cc.johnwu.sql.HISModel;
import java.awt.Point;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;


public class Frm_ShowTable extends javax.swing.JFrame {
    private String m_Sql;
    private Frm_Statistic frm;
    private ResultSet rs;
    private final int MAX_ROWS_OF_PAGE = 50;
    private ResultSet m_PatientsRS;

    public Frm_ShowTable(final Frm_Statistic fr,String sql, String regTime, String age, String gender ) {
        frm = fr;
        m_Sql = sql;
        initComponents();
        init();
        addWindowListener(new WindowAdapter() {  // 畫面關閉原視窗enable
            @Override
            public void windowClosing(WindowEvent windowevent) {
                fr.setEnabled(true);
            }
        });
        this.setLocationRelativeTo(this);
        lab_RegTimeValue.setText(regTime);
        lab_AgeValue.setText(age);
        lab_GenderValue.setText(gender);
    }

    @SuppressWarnings("unchecked")

    public void init(){
        try {
            m_PatientsRS = DBC.executeQuery(m_Sql);
            if (m_PatientsRS.last()) {
                this.lab_TotlalValue.setText(String.valueOf(m_PatientsRS.getRow()));
                this.cob_Page.removeAllItems();
                int page = (m_PatientsRS.getRow() / MAX_ROWS_OF_PAGE) + (m_PatientsRS.getRow() % MAX_ROWS_OF_PAGE == 0 ? 0 : 1);
                for (int i = 0; i < page; i++) {
                    this.cob_Page.addItem(+(i + 1) + " of " + page);
                }
                m_PatientsRS.beforeFirst();
                tab_Show.setModel(HISModel.getModel(m_PatientsRS, 1, MAX_ROWS_OF_PAGE));
                if (this.cob_Page.getItemCount() > 1) {
                    this.btn_Next.setEnabled(true);

                } else {
                    this.btn_Next.setEnabled(false);
                }
                this.btn_Previous.setEnabled(false);

                 TableColumn columnNo = tab_Show.getColumnModel().getColumn(0);
                 TableColumn columnAge = tab_Show.getColumnModel().getColumn(1);
                 TableColumn columnGen = tab_Show.getColumnModel().getColumn(2);
                 TableColumn columnDia = tab_Show.getColumnModel().getColumn(3);
                 //TableColumn columnMed = tab_Show.getColumnModel().getColumn(4);
                 //TableColumn columnAua = tab_Show.getColumnModel().getColumn(5);
                 TableColumn columnRegTime = tab_Show.getColumnModel().getColumn(4);
                 TableColumn columnBlodTyp = tab_Show.getColumnModel().getColumn(5);
                 //TableColumn columnAdd = tab_Show.getColumnModel().getColumn(8);

                 columnNo.setMaxWidth(35);
                 columnAge.setMaxWidth(35);
                 columnGen.setMaxWidth(35);
                // columnDia.setMaxWidth(400);
                // columnMed.setMaxWidth(400);
               //  columnAua.setMaxWidth(60);
                 columnRegTime.setMaxWidth(300);
                 columnBlodTyp.setMaxWidth(0);
                // columnAdd.setMaxWidth(400);

                 TabTools.setHideColumn(tab_Show, 5);

            } else {
                tab_Show.setModel(getModle(new String[]{"Message"},new String[][]{{"No Information."}}));
                this.lab_TotlalValue.setText("0");
                this.btn_Next.setEnabled(false);
                this.btn_Excel.setEnabled(false);
            
            }
        } catch (SQLException ex) {
            Logger.getLogger(Frm_ShowTable.class.getName()).log(Level.SEVERE, null, ex);
        }
        tab_Show.setRowHeight(30);
    }

      /** 設定表單預設模型。*/
    private DefaultTableModel getModle(String[] title,String[][] data){
        return new DefaultTableModel(data,title){
                @Override
                public boolean isCellEditable(int r, int c){
                return false;}};
    }




    /** 輸出Table */
    private void SetOutputXLS(String url) {
        try {
            // 設定檔案
            ResultSet rsExcel = null;
            rsExcel = DBC.executeQuery(m_Sql);
            WritableWorkbook workbook = Workbook.createWorkbook(new File(url));
            WritableSheet sheet = workbook.createSheet("First", 0);
            Label label = null;

            // 標頭 Taichung Hospital 第一列  第一行
            label = new Label (0,0, SysInfo.getHosName()+" Export to Excel "+new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime()) );
                System.out.println(SysInfo.getHosName());
            sheet.addCell(label);

//            int col = 0;
//            label =new Label(col,1,"Condition:");
//            sheet.addCell(label);
//            col++;
//
//            label =new Label(col,1,"Refistration Time: " + lab_RegTimeValue);
//            sheet.addCell(label);
//            col++;
//
//            label =new Label(col,1,"Age: " + lab_AgeValue);
//            sheet.addCell(label);
//            col++;
//
//            label =new Label(col,1,"Gender: "+ lab_GenderValue);
//            sheet.addCell(label);
//            col++;

            // 表頭資料
            int col = 0;
         
                label =new Label(col,2,"Age");
                sheet.addCell(label);
                col++;
            

          
                label =new Label(col,2,"Gender");
                sheet.addCell(label);
                col++;
            

         
                label =new Label(col,2,"Diagnosis");
                sheet.addCell(label);
                col++;
            

           
                label =new Label(col,2,"Registration Time");
                sheet.addCell(label);
                col++;
            

          
                label =new Label(col,2,"Bloodtype");
                sheet.addCell(label);
                col++;




            // 欄位資料
            int x=0;//y值
            int y=3;//y值
            while (rsExcel.next()) {
              
                label =new Label(x,y,rsExcel.getString("Age"));
                sheet.addCell(label);
                x++;



                label =new Label(x,y,rsExcel.getString("Gender"));
                sheet.addCell(label);
                x++;



                label =new Label(x,y,rsExcel.getString("Diagnosis"));
                sheet.addCell(label);
                x++;



                label =new Label(x,y,rsExcel.getString("Registration Time"));
                sheet.addCell(label);
                x++;



                label =new Label(x,y,rsExcel.getString("Bloodtype"));
                sheet.addCell(label);
                x++;
                

                y++;
                x = 0;
            }
            workbook.write();
            workbook.close();
        } catch (Exception e) {
            System.out.println(e);
        }

    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jScrollPane1 = new javax.swing.JScrollPane();
        tab_Show = new javax.swing.JTable();
        pan_UnderCenter = new javax.swing.JPanel();
        btn_Previous = new javax.swing.JButton();
        cob_Page = new javax.swing.JComboBox();
        btn_Next = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        lab_RegTime = new javax.swing.JLabel();
        lab_Age = new javax.swing.JLabel();
        lab_Gender = new javax.swing.JLabel();
        lab_Total = new javax.swing.JLabel();
        btn_Excel = new javax.swing.JButton();
        lab_RegTimeValue = new javax.swing.JLabel();
        lab_AgeValue = new javax.swing.JLabel();
        lab_GenderValue = new javax.swing.JLabel();
        lab_TotlalValue = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Result");
        setResizable(false);

        tab_Show.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tab_Show.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
        jScrollPane1.setViewportView(tab_Show);

        pan_UnderCenter.setBackground(new java.awt.Color(246, 246, 246));
        pan_UnderCenter.setLayout(new java.awt.GridBagLayout());

        btn_Previous.setText("<<");
        btn_Previous.setEnabled(false);
        btn_Previous.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_PreviousActionPerformed(evt);
            }
        });
        pan_UnderCenter.add(btn_Previous, new java.awt.GridBagConstraints());

        cob_Page.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cob_PageItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.ipadx = 50;
        pan_UnderCenter.add(cob_Page, gridBagConstraints);

        btn_Next.setText(">>");
        btn_Next.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_NextActionPerformed(evt);
            }
        });
        pan_UnderCenter.add(btn_Next, new java.awt.GridBagConstraints());

        jPanel1.setBackground(new java.awt.Color(246, 246, 246));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Condition"));

        lab_RegTime.setFont(new java.awt.Font("新細明體", 1, 14));
        lab_RegTime.setText("Registration Time:");

        lab_Age.setFont(new java.awt.Font("新細明體", 1, 14));
        lab_Age.setText("Age:");

        lab_Gender.setFont(new java.awt.Font("新細明體", 1, 14));
        lab_Gender.setText("Gender:");

        lab_Total.setBackground(new java.awt.Color(246, 246, 246));
        lab_Total.setFont(new java.awt.Font("新細明體", 1, 14));
        lab_Total.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lab_Total.setText("Total:");

        btn_Excel.setText("Export to Excel");
        btn_Excel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ExcelActionPerformed(evt);
            }
        });

        lab_RegTimeValue.setText("--");

        lab_AgeValue.setText("--");

        lab_GenderValue.setText("--");

        lab_TotlalValue.setText("--");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lab_Gender, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lab_Age, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lab_Total, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lab_RegTime, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lab_TotlalValue)
                        .addGap(494, 494, 494)
                        .addComponent(btn_Excel))
                    .addComponent(lab_AgeValue)
                    .addComponent(lab_RegTimeValue)
                    .addComponent(lab_GenderValue))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab_RegTime)
                    .addComponent(lab_RegTimeValue))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab_Age)
                    .addComponent(lab_AgeValue))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab_Gender)
                    .addComponent(lab_GenderValue))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab_Total)
                    .addComponent(lab_TotlalValue)
                    .addComponent(btn_Excel))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 766, Short.MAX_VALUE)
                    .addComponent(pan_UnderCenter, javax.swing.GroupLayout.DEFAULT_SIZE, 766, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pan_UnderCenter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_ExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ExcelActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File("output.xls"));
        int option = fileChooser.showSaveDialog(null);

        if(option == JFileChooser.OPEN_DIALOG) { // 按確定鍵
            SetOutputXLS(fileChooser.getSelectedFile().toString());
        } else if(option == JFileChooser.CANCEL_OPTION ) { // 按取消鍵

        }


    }//GEN-LAST:event_btn_ExcelActionPerformed

    private void btn_PreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_PreviousActionPerformed
        cob_Page.setSelectedIndex(cob_Page.getSelectedIndex()-1);
}//GEN-LAST:event_btn_PreviousActionPerformed

    private void cob_PageItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cob_PageItemStateChanged
        if(evt.getStateChange() == java.awt.event.ItemEvent.SELECTED && m_PatientsRS!=null){
            tab_Show.setModel(HISModel.getModel(m_PatientsRS, this.cob_Page.getSelectedIndex()*MAX_ROWS_OF_PAGE+1,MAX_ROWS_OF_PAGE));
            if(cob_Page.getSelectedIndex()>=0
                    && cob_Page.getItemCount()>(cob_Page.getSelectedIndex()+1)){
                this.btn_Next.setEnabled(true);
            }else{
                this.btn_Next.setEnabled(false);
            }
            if(cob_Page.getSelectedIndex()>0){
                this.btn_Previous.setEnabled(true);
            }else{
                this.btn_Previous.setEnabled(false);
            }
        }
}//GEN-LAST:event_cob_PageItemStateChanged

    private void btn_NextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_NextActionPerformed
        cob_Page.setSelectedIndex(cob_Page.getSelectedIndex()+1);
}//GEN-LAST:event_btn_NextActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Excel;
    private javax.swing.JButton btn_Next;
    private javax.swing.JButton btn_Previous;
    private javax.swing.JComboBox cob_Page;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lab_Age;
    private javax.swing.JLabel lab_AgeValue;
    private javax.swing.JLabel lab_Gender;
    private javax.swing.JLabel lab_GenderValue;
    private javax.swing.JLabel lab_RegTime;
    private javax.swing.JLabel lab_RegTimeValue;
    private javax.swing.JLabel lab_Total;
    private javax.swing.JLabel lab_TotlalValue;
    private javax.swing.JPanel pan_UnderCenter;
    private javax.swing.JTable tab_Show;
    // End of variables declaration//GEN-END:variables

}