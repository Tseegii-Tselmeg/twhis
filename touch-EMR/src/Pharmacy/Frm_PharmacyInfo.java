package Pharmacy;

import Case.Frm_Case;
import ErrorMessage.StoredErrorMessage;
import cc.johnwu.sql.DBC;
import cc.johnwu.sql.HISModel;
import Multilingual.language;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 *
 * @author bee
 */
public class Frm_PharmacyInfo extends javax.swing.JFrame {
    private String m_Dep,m_Docter,m_No,m_Name,m_Birth,m_Gender,m_Blood,m_Guid,m_Ps;
    private Frm_Pharmacy Frm_p = null;
    private Frm_PharmacyInquire Frm_pi = null;
    /*多國語言變數*/
    private language paragraph = new language();
    private String[] line = new String(paragraph.setlanguage("PHARMACYINFO")).split("\n") ;
    private String[] message = new String(paragraph.setlanguage("MESSAGE")).split("\n") ;
    /*輸出錯誤資訊變數*/
    StoredErrorMessage ErrorMessage = new StoredErrorMessage() ;

    public Frm_PharmacyInfo(Frm_PharmacyInquire frm,String dep,String doctor,String no,String name,String birth,String gender,String blood,String ps,String guid) {
        initComponents();
        initLanguage() ;
        this.setLocationRelativeTo(this);
        Frm_pi=frm;
        addWindowListener(new WindowAdapter() {  // 畫面關閉原視窗enable
            @Override
            public void windowClosing(WindowEvent windowevent) {
                Frm_pi.setEnabled(true);
            }
        });
        this.m_Dep = dep;
        this.m_Docter = doctor;
        this.m_No = no;
        this.m_Name = name;
        this.m_Birth = birth;
        this.m_Gender = gender;
        this.m_Blood = blood;
        this.m_Ps=ps;
        this.m_Guid = guid;
        setTab_Medicines();
    }

    public Frm_PharmacyInfo(Frm_Pharmacy frm,String dep,String doctor,String no,String name,String birth,String gender,String blood,String ps,String guid) {
        initComponents();
        initLanguage() ;
        this.setLocationRelativeTo(this);
        Frm_p=frm;
        addWindowListener(new WindowAdapter() {  // 畫面關閉原視窗enable
            @Override
            public void windowClosing(WindowEvent windowevent) {
                Frm_p.setEnabled(true);
            }
        });
        this.m_Dep = dep;
        this.m_Docter = doctor;
        this.m_No = no;
        this.m_Name = name;
        this.m_Birth = birth;
        this.m_Gender = gender;
        this.m_Blood = blood;
        this.m_Ps=ps;
        this.m_Guid = guid;
        setTab_Medicines();
    }
    public void initFram() {

    }
    private void initLanguage() {
        this.lab_TitleNo.setText(m_No);
        this.lab_TitleNo.setText(paragraph.getLanguage(line, "TITLENO"));
        this.lab_TitleName.setText(paragraph.getLanguage(line, "TITLENAME"));
        this.lab_TitleSex.setText(paragraph.getLanguage(line, "TITLESEX"));
        this.lab_TitleBloodtype.setText(paragraph.getLanguage(line, "TITLEBLOODTYPE"));
       // this.lab_TitleAge.setText(paragraph.getLanguage(line, "TITLEAGE"));
        this.lab_Doctor.setText(paragraph.getLanguage(line, "DOCTOR"));
//        this.lab_Department.setText(paragraph.getLanguage(line, "DEPARTMENT"));
        this.lab_TitlePs.setText(paragraph.getLanguage(line, "TITLEPS"));
        this.btn_Close.setText(paragraph.getLanguage(message, "CLOSE"));
        this.setTitle(paragraph.getLanguage(line, "TITLEMEDICINEINFORMATION"));

    }
    // 藥品
    public void setTab_Medicines() {

        ResultSet rsMedicines = null;
        try {
            String sqlMedicines =
                "SELECT medicines.code AS 'Code', medicines.item AS 'Item', medicine_stock.dosage AS 'Dosage', medicines.unit AS '"+paragraph.getLanguage(line, "UNIT")+"', medicine_stock.usage AS '"+paragraph.getLanguage(line, "USAGE")+"', " +
                       "medicine_stock.way AS 'Frequency', medicine_stock.day AS 'Duration', medicine_stock.quantity AS 'Quantity', " +
                       "medicine_stock.urgent AS '"+paragraph.getLanguage(line, "URGENT")+"', medicine_stock.powder AS '"+paragraph.getLanguage(line, "POWDER")+"', medicine_stock.ps AS '"+paragraph.getLanguage(line, "PS")+"' " +
                "FROM medicines, medicine_stock, outpatient_services, registration_info " +
                "WHERE registration_info.guid = '"+m_Guid+"' " +
                "AND medicine_stock.os_guid = outpatient_services.guid " +
                "AND outpatient_services.reg_guid = registration_info.guid " +
                "AND medicines.code = medicine_stock.m_code";
               System.out.println(sqlMedicines);
            rsMedicines = DBC.executeQuery(sqlMedicines);


            if (rsMedicines.next()) {
                this.tab_Medicines.setModel(HISModel.getModel(rsMedicines, true));
            } else {
                ((DefaultTableModel) tab_Medicines.getModel()).setRowCount(0);
            }

            txt_Dep.setText(m_Dep);
            txt_Doctor.setText(m_Docter);
            txt_No.setText(m_No);
            txt_Name.setText(m_Name);
            txt_Sex.setText(m_Gender);
            txt_Bloodtype.setText(m_Blood);
            txt_Age.setText(m_Birth);
            txt_Ps.setText(m_Ps);
            
            TableColumn columnNo = this.tab_Medicines.getColumnModel().getColumn(0);
            TableColumn columnIcdCode = this.tab_Medicines.getColumnModel().getColumn(1);
            TableColumn columnName = this.tab_Medicines.getColumnModel().getColumn(2);
            TableColumn columnSubUsage = this.tab_Medicines.getColumnModel().getColumn(3);
            TableColumn columnUnit = this.tab_Medicines.getColumnModel().getColumn(4);
            TableColumn columnWay = this.tab_Medicines.getColumnModel().getColumn(5);
            TableColumn columnRoute = this.tab_Medicines.getColumnModel().getColumn(6);
            TableColumn columnDay = this.tab_Medicines.getColumnModel().getColumn(7);
            TableColumn columnQuantity = this.tab_Medicines.getColumnModel().getColumn(8);
            TableColumn columnUrgent = this.tab_Medicines.getColumnModel().getColumn(9);
            TableColumn columnPowder = this.tab_Medicines.getColumnModel().getColumn(10);
            TableColumn columnPs = this.tab_Medicines.getColumnModel().getColumn(11);


            columnNo.setMaxWidth(30);
            columnIcdCode.setPreferredWidth(300);
            columnName.setPreferredWidth(301);
            columnSubUsage.setPreferredWidth(55);
            columnUnit.setPreferredWidth(45);
            columnWay.setPreferredWidth(45);
            columnRoute.setPreferredWidth(45);
            columnDay.setPreferredWidth(50);
            columnQuantity.setPreferredWidth(70);
            columnUrgent.setPreferredWidth(40);
            columnPowder.setPreferredWidth(40);
            columnPs.setPreferredWidth(150);
            tab_Medicines.setRowHeight(30);
            //setHideColumn(tab_Medicines, 2);
            Common.TabTools.setHideColumn(tab_Medicines, 4);

        } catch (SQLException e) {
            ErrorMessage.setData("Pharmacy", "Frm_PharmacyInfo" ,"setTab_Medicines()",
                    e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            Logger.getLogger(Frm_PharmacyInfo.class.getName()).log(Level.SEVERE, null, e);
        } finally {

            try {DBC.closeConnection(rsMedicines);}
            catch (SQLException e){
                 ErrorMessage.setData("Pharmacy", "Frm_PharmacyInfo" ,"setTab_Medicines() - DBC.closeConnection",
                    e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        span_How = new javax.swing.JScrollPane();
        tab_Medicines = new javax.swing.JTable();
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
        lab_Doctor = new javax.swing.JLabel();
        lab_Department = new javax.swing.JLabel();
        txt_Dep = new javax.swing.JTextField();
        txt_Doctor = new javax.swing.JTextField();
        btn_Close = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Medicine Information");
        setAlwaysOnTop(true);

        tab_Medicines.setAutoCreateRowSorter(true);
        tab_Medicines.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "", "藥品代碼", "藥品名稱", "服法", "次劑量", "次數/天", "日數", "總劑量"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tab_Medicines.setRowHeight(25);
        span_How.setViewportView(tab_Medicines);

        pan_Top.setBackground(new java.awt.Color(228, 228, 228));
        pan_Top.setFont(new java.awt.Font("Bitstream Vera Sans", 1, 18));

        lab_TitleNo.setText("ID");

        lab_TitleName.setText("Name");

        lab_TitlePs.setText("PS");

        lab_TitleSex.setText("Gender");

        lab_TitleAge.setText("Birthday");

        lab_TitleBloodtype.setText("Blood Type");

        txt_No.setEditable(false);

        txt_Age.setEditable(false);

        txt_Bloodtype.setEditable(false);

        txt_Name.setEditable(false);

        txt_Sex.setEditable(false);

        txt_Ps.setEditable(false);

        lab_Doctor.setText("Doctor");

        lab_Department.setText("Depart/Clinic");

        txt_Dep.setEditable(false);

        txt_Doctor.setEditable(false);

        javax.swing.GroupLayout pan_TopLayout = new javax.swing.GroupLayout(pan_Top);
        pan_Top.setLayout(pan_TopLayout);
        pan_TopLayout.setHorizontalGroup(
            pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_TopLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(lab_TitleBloodtype, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE)
                        .addComponent(lab_TitleSex, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lab_TitleName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lab_TitleNo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(lab_TitleAge, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_No, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
                    .addComponent(txt_Name, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
                    .addComponent(txt_Dep, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
                    .addComponent(txt_Doctor, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
                    .addComponent(txt_Ps, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE))
                .addGap(54, 54, 54)
                .addGroup(pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(lab_Doctor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lab_Department, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(lab_TitlePs, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_Age, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
                    .addComponent(txt_Bloodtype, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
                    .addComponent(txt_Sex, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE))
                .addGap(117, 117, 117))
        );
        pan_TopLayout.setVerticalGroup(
            pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_TopLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan_TopLayout.createSequentialGroup()
                        .addGroup(pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lab_Doctor)
                            .addComponent(txt_Bloodtype, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(7, 7, 7)
                        .addGroup(pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lab_Department)
                            .addComponent(txt_Sex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lab_TitlePs)
                            .addComponent(txt_Age, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pan_TopLayout.createSequentialGroup()
                        .addGroup(pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lab_TitleNo)
                            .addComponent(txt_Dep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lab_TitleName)
                            .addComponent(txt_Doctor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lab_TitleSex)
                            .addComponent(txt_Name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lab_TitleBloodtype)
                            .addComponent(txt_Ps, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_No, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lab_TitleAge))))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        pan_TopLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {txt_Age, txt_Bloodtype, txt_Dep, txt_Doctor, txt_Name, txt_No, txt_Ps, txt_Sex});

        btn_Close.setText("Close");
        btn_Close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CloseActionPerformed(evt);
            }
        });

        jButton1.setText("Medicine Education");
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
                    .addComponent(span_How, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 796, Short.MAX_VALUE)
                    .addComponent(pan_Top, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_Close, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(span_How, javax.swing.GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pan_Top, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_Close)
                    .addComponent(jButton1))
                .addGap(22, 22, 22))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_CloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CloseActionPerformed
        if (Frm_p == null) {
            Frm_pi.reSetEnable();
        } else {
            Frm_p.setEnabled(true);
        }
        this.dispose();        
    }//GEN-LAST:event_btn_CloseActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.setAlwaysOnTop(false);
        new Frm_Case(txt_Dep.getText(), m_Guid, true,"medicine").setVisible(true);
}//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Close;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel lab_Department;
    private javax.swing.JLabel lab_Doctor;
    private javax.swing.JLabel lab_TitleAge;
    private javax.swing.JLabel lab_TitleBloodtype;
    private javax.swing.JLabel lab_TitleName;
    private javax.swing.JLabel lab_TitleNo;
    private javax.swing.JLabel lab_TitlePs;
    private javax.swing.JLabel lab_TitleSex;
    private javax.swing.JPanel pan_Top;
    private javax.swing.JScrollPane span_How;
    private javax.swing.JTable tab_Medicines;
    private javax.swing.JTextField txt_Age;
    private javax.swing.JTextField txt_Bloodtype;
    private javax.swing.JTextField txt_Dep;
    private javax.swing.JTextField txt_Doctor;
    private javax.swing.JTextField txt_Name;
    private javax.swing.JTextField txt_No;
    private javax.swing.JTextField txt_Ps;
    private javax.swing.JTextField txt_Sex;
    // End of variables declaration//GEN-END:variables

}
