package Statistic;

import AutoComplete.CompleterComboBox;
import ErrorMessage.StoredErrorMessage;
import Main.Frm_Main;
import cc.johnwu.date.DateInterface;
import cc.johnwu.sql.DBC;
import cc.johnwu.sql.HISModel;
import Multilingual.language;
import Pharmacy.Frm_Pharmacy;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.table.TableColumn;
import java.sql.* ;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
/**
 *
 * @author Steven
 */
public class Frm_Statistic extends javax.swing.JFrame  implements DateInterface{

    private CompleterComboBox m_Cobww;
    private CompleterComboBox m_CobwwTown;
    private CompleterComboBox m_CobwwRegion;
    private CompleterComboBox m_CobwwCountry;
    private CompleterComboBox m_Cobww_Medicine;
    private final String DEFAULTDATE =  "01-01-2010";
    private final String DEFAULTCONDATE =  "2010-01-01";

    public Frm_Statistic() {
        initComponents();
        initFrame();
        initRealTimeInfo();
        initBasicInfo();
        initAutoComplete("SELECT distinct town FROM patients_info WHERE town IS NOT NULL ", pan_Town, m_CobwwTown);
        initAutoComplete("SELECT distinct state FROM patients_info WHERE state IS NOT NULL ", pan_Region, m_CobwwRegion);
        initAutoComplete("SELECT distinct country FROM patients_info WHERE country IS NOT NULL ", pan_Country, m_CobwwCountry);
        for (int i = 1; i < 120; i++) {
            cbox_AgeS.addItem(i);
            cbox_AgeE.addItem(i);
        }

        datec_RegTimeS.setValue(DEFAULTDATE);
        datec_RegTimeE.setValue(DEFAULTDATE);
    }

    private void initBasicInfo() {
        ResultSet rs = null;
        try {
            String sql = "SELECT "+
                        "(SELECT COUNT(p_no) FROM patients_info WHERE exist ='1' AND dead_guid IS NULL) AS 'totalPatient',"+
                        "(SELECT COUNT(s_no) FROM staff_info LEFT JOIN department ON department.guid=staff_info.dep_guid WHERE department.name = 'Doctor' AND staff_info.exist = '1') AS 'totslDoctor',"+
                        "(SELECT COUNT(s_no) FROM staff_info LEFT JOIN department ON department.guid=staff_info.dep_guid WHERE department.name <> 'Doctor' AND staff_info.exist = '1')  AS 'totslStaff',"+
                        "(SELECT COUNT(guid) FROM policlinic) AS 'totalPoli', "+
                        "(SELECT COUNT(guid) FROM poli_room) AS 'totalClinic' "; // 病患總人數
     
            rs = DBC.executeQuery(sql);
            if(rs.next()) {
                txt_TotalPatient.setText(rs.getString("totalPatient"));
                txt_TotalDoctor.setText(rs.getString("totslDoctor"));
                txt_TotalStaff.setText(rs.getString("totslStaff"));
                txt_totalPoli.setText(rs.getString("totalPoli"));
                txt_TotalClinic.setText(rs.getString("totalClinic"));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(Frm_Statistic.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initRealTimeInfo() {
        ResultSet rs = null;
        try {
            String sql =  "SELECT COUNT(patients_info.p_no) AS 'totalWaiting' "+
                             "FROM registration_info, patients_info, shift_table, staff_info, policlinic, poli_room " +
                             "WHERE registration_info.p_no = patients_info.p_no " +
                             "AND registration_info.shift_guid = shift_table.guid " +
                             "AND shift_table.s_id = staff_info.s_id " +
                             "AND shift_table.room_guid = poli_room.guid " +
                             "AND poli_room.poli_guid = policlinic.guid " +
                             "AND finish IS NULL " ;


            rs = DBC.executeQuery(sql);
            if(rs.next()) {
                txt_TotalWaiting.setText(rs.getString("totalWaiting"));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(Frm_Statistic.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initFrame(){
 
        this.setLocationRelativeTo(this);
        this.setExtendedState(Frm_Statistic.MAXIMIZED_BOTH);  // 最大化
        this.datec_RegTimeS.setParentFrame(this);
        this.datec_RegTimeE.setParentFrame(this);
        this.tab_Medicine.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  // tabble不可按住多選
        addWindowListener(new WindowAdapter() {  // 畫面關閉原視窗enable
            @Override
            public void windowClosing(WindowEvent windowevent) {
                jButton1ActionPerformed(null);
            }
        });

        showCombo();
        initCobww();
    }

   public void setTable() {
        String sql = "SELECT registration_info.reg_time AS 'Date', medicines.item AS ' Medicine', " +
                            "concat(staff_info.firstname,'  ',staff_info.lastname) AS 'Doctor', " +
                            "concat(patients_info.firstname,'  ',patients_info.lastname) AS 'Patient Name', " +
                            "medicine_stock.quantity AS 'Quantity'" +
                            "FROM  registration_info, medicines, medicine_stock, outpatient_services, staff_info, patients_info, shift_table " +
                            "WHERE medicines.item = '"+m_Cobww_Medicine.getSelectedItem()+"' " +
                            "AND registration_info.shift_guid = shift_table.guid " +
                            "AND shift_table.s_id = staff_info.s_id " +
                            "AND medicines.code = medicine_stock.m_code " +
                            "AND registration_info.p_no = patients_info.p_no "+
                            "AND registration_info.guid = outpatient_services.reg_guid " +
                            "AND medicine_stock.os_guid = outpatient_services.guid " +
                            "AND os_guid IS NOT NULL";
        ResultSet rs = null;
        try {
            System.out.println(sql);
            rs = DBC.executeQuery(sql);
            tab_Medicine.setModel(HISModel.getModel(rs,true));
            tab_Medicine.setRowHeight(30);
            txt_Total.setText("");
            setCloumnWidth(tab_Medicine);
        } catch (SQLException e) {

            Logger.getLogger(Frm_Statistic.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {DBC.closeConnection(rs);}
            catch (SQLException e){

            }
        }
    }

       private void setCloumnWidth(javax.swing.JTable tab){
        TableColumn columnNo = tab.getColumnModel().getColumn(0);
        TableColumn columnPNo = tab.getColumnModel().getColumn(1);
        TableColumn columnName = tab.getColumnModel().getColumn(2);
        TableColumn columnBirth = tab.getColumnModel().getColumn(3);

        columnNo.setPreferredWidth(30);

        columnPNo.setPreferredWidth(70);
        columnName.setPreferredWidth(150);
        columnBirth.setPreferredWidth(90);

        tab.setRowHeight(30);

    }

       /**目前庫存頁  藥品的combo選單*/
    private void showCombo(){//
        ResultSet rsArray= null;

        try{
            String ArraySql="SELECT medicines.item AS name FROM medicines WHERE effective = 1";
            rsArray= DBC.executeQuery(ArraySql);
            rsArray.last();
            String[] medicineArray = new String[(rsArray.getRow()+1)];
            rsArray.beforeFirst();
            medicineArray[0]="";
            int pathArray=1;
            while(rsArray.next()){
                medicineArray[pathArray]=rsArray.getString("name");
                pathArray++;
            }
            new CompleterComboBox(medicineArray);
            m_Cobww_Medicine = new CompleterComboBox(medicineArray);
            m_Cobww_Medicine.setBounds(0, 0, 650, 20);
            m_Cobww_Medicine.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent evt) {
                    setTable();
                    if (tab_Medicine.getRowCount() != 0) {
                        int total = 0;
                        for (int i = 0; i < tab_Medicine.getRowCount(); i++) {
                            total += Integer.parseInt(tab_Medicine.getValueAt(i, 5).toString());
                        }
                        txt_Total.setText(String.valueOf(total));
                    }
                 }
            });
            this.pan_Top.add(m_Cobww_Medicine);
            m_Cobww_Medicine.setSelectedIndex(0);
        }catch(SQLException e) {

            Logger.getLogger(Frm_Statistic.class.getName()).log(Level.SEVERE, null, e);
        }  finally {
            try {DBC.closeConnection(rsArray);}
            catch (SQLException e){

            }
        }
    }

   public void initAutoComplete(String sql, JPanel pan, CompleterComboBox Cobww ) {
        String[] cob = null ;
        ResultSet rs = null;

        try {
            
            rs = DBC.executeQuery(sql);
            rs.last();
            cob = new String[rs.getRow()+1];
            rs.beforeFirst();
            int i = 0;
            cob[i++] = "";
            while (rs.next()) {
                  cob[i++] = rs.getString(1);
            }
            Cobww = new CompleterComboBox(cob);
            Cobww.setBounds(0, 0, pan.getWidth(), pan.getHeight());
            pan.add(Cobww);
            Cobww.setSelectedIndex(0);
        } catch (SQLException e) {
            Logger.getLogger(Frm_Statistic.class.getName()).log(Level.SEVERE, null, e);

        } finally {
            try {DBC.closeConnection(rs);}
            catch (SQLException e){

            }
        }
   }



        // 初始化下拉式選單
    public void initCobww() {
        String[] icdCob = null ;
        ResultSet rs = null;
        try {
            String sql = "SELECT icd_code, name " +
                         "FROM Diagnosis_Code " +
                         "WHERE effective = 0";
            rs = DBC.localExecuteQuery(sql);
            rs.last();
            icdCob = new String[rs.getRow()+1];
            rs.beforeFirst();
            int i = 0;
            icdCob[i++] = "";
            while (rs.next()) {
                  icdCob[i++] = rs.getString("icd_code").trim() + "    " + rs.getString("name").trim();
            }

            m_Cobww = new CompleterComboBox(icdCob);
            m_Cobww.setBounds(0, 0, pan_DiaSearch.getWidth(), pan_DiaSearch.getHeight());
            pan_DiaSearch.add(m_Cobww);
            m_Cobww.setSelectedIndex(0);

        } catch (SQLException e) {
            Logger.getLogger(Frm_Statistic.class.getName()).log(Level.SEVERE, null, e);

        } finally {
            try {DBC.closeConnection(rs);}
            catch (SQLException e){

            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        txt_TotalDoctor = new javax.swing.JTextField();
        txt_TotalPatient = new javax.swing.JTextField();
        txt_TotalClinic = new javax.swing.JTextField();
        txt_totalPoli = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txt_TotalStaff = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txt_TotalWaiting = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lis_DCselect = new javax.swing.JList();
        btn_DCclear = new javax.swing.JButton();
        btn_DCadd = new javax.swing.JButton();
        pan_DiaSearch = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        cbox_Gender = new javax.swing.JComboBox();
        jPanel11 = new javax.swing.JPanel();
        datec_RegTimeS = new cc.johnwu.date.DateChooser();
        datec_RegTimeE = new cc.johnwu.date.DateChooser();
        jLabel14 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jButton10 = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        cbox_AgeS = new javax.swing.JComboBox();
        cbox_AgeE = new javax.swing.JComboBox();
        jPanel7 = new javax.swing.JPanel();
        lab_Country = new javax.swing.JLabel();
        lab_TitleState = new javax.swing.JLabel();
        lab_TitleTown = new javax.swing.JLabel();
        pan_Town = new javax.swing.JPanel();
        pan_Region = new javax.swing.JPanel();
        pan_Country = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        pan_Top = new javax.swing.JPanel();
        lab_Total = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tab_Medicine = new javax.swing.JTable();
        txt_Total = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Statistic");

        jPanel13.setBackground(new java.awt.Color(246, 246, 246));

        txt_TotalDoctor.setEditable(false);

        txt_TotalPatient.setEditable(false);

        txt_TotalClinic.setEditable(false);

        txt_totalPoli.setEditable(false);

        jLabel1.setFont(new java.awt.Font("新細明體", 1, 14));
        jLabel1.setText("The total number of patients:");

        jLabel5.setFont(new java.awt.Font("新細明體", 1, 14));
        jLabel5.setText("The total number of doctor:");

        jLabel6.setFont(new java.awt.Font("新細明體", 1, 14));
        jLabel6.setText("The total number of clinic:");

        jLabel7.setFont(new java.awt.Font("新細明體", 1, 14));
        jLabel7.setText("The total number of policlinic:");

        jLabel3.setFont(new java.awt.Font("新細明體", 1, 14));
        jLabel3.setText("The total number of staff:");

        txt_TotalStaff.setEditable(false);

        jLabel12.setFont(new java.awt.Font("新細明體", 1, 14));
        jLabel12.setText("Number of people waiting to see the doctor:");

        txt_TotalWaiting.setEditable(false);

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(85, 85, 85)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txt_TotalStaff, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE)
                            .addComponent(txt_TotalPatient, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE)
                            .addComponent(txt_TotalClinic, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE)
                            .addComponent(txt_TotalDoctor, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE)
                            .addComponent(txt_totalPoli, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_TotalWaiting, javax.swing.GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE)))
                .addGap(144, 144, 144))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txt_TotalPatient, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txt_TotalDoctor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_TotalStaff, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(txt_totalPoli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_TotalClinic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txt_TotalWaiting, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(194, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(106, 106, 106))
        );

        jTabbedPane1.addTab("Real-time information", jPanel1);

        jPanel4.setBackground(new java.awt.Color(246, 246, 246));

        jLabel2.setFont(new java.awt.Font("DejaVu Sans", 1, 14));
        jLabel2.setText("Diagnosis Query Rules:");

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Diagnosis Code"));

        jLabel10.setFont(new java.awt.Font("DejaVu Sans", 1, 13));
        jLabel10.setText("The Diagnosis Codes that you selected:");

        lis_DCselect.setFont(new java.awt.Font("新細明體", 1, 14)); // NOI18N
        jScrollPane1.setViewportView(lis_DCselect);

        btn_DCclear.setText("Clear");
        btn_DCclear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_DCclearActionPerformed(evt);
            }
        });

        btn_DCadd.setText("Add");
        btn_DCadd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_DCaddActionPerformed(evt);
            }
        });

        pan_DiaSearch.setMaximumSize(new java.awt.Dimension(32767, 23));
        pan_DiaSearch.setMinimumSize(new java.awt.Dimension(503, 23));

        javax.swing.GroupLayout pan_DiaSearchLayout = new javax.swing.GroupLayout(pan_DiaSearch);
        pan_DiaSearch.setLayout(pan_DiaSearchLayout);
        pan_DiaSearchLayout.setHorizontalGroup(
            pan_DiaSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 517, Short.MAX_VALUE)
        );
        pan_DiaSearchLayout.setVerticalGroup(
            pan_DiaSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 23, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 715, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                        .addComponent(pan_DiaSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_DCadd, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_DCclear, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4))
                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pan_DiaSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_DCclear)
                    .addComponent(btn_DCadd))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Gender"));

        cbox_Gender.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Both", "M", "F" }));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cbox_Gender, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(112, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cbox_Gender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder("Registration Time"));

        jLabel14.setText("To");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(datec_RegTimeS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel14)
                .addGap(10, 10, 10)
                .addComponent(datec_RegTimeE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14)
                    .addComponent(datec_RegTimeE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(datec_RegTimeS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jButton10.setText("Export to Table");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap(602, Short.MAX_VALUE)
                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton10)
                .addContainerGap())
        );

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder("Age"));

        jLabel26.setText("To");

        cbox_AgeS.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " " }));

        cbox_AgeE.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " " }));

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cbox_AgeS, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel26)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbox_AgeE, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(cbox_AgeE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbox_AgeS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Residence"));

        lab_Country.setText("Country :");

        lab_TitleState.setText("Region :");

        lab_TitleTown.setText("City/Town :");

        pan_Town.setBackground(new java.awt.Color(204, 255, 255));
        pan_Town.setMaximumSize(new java.awt.Dimension(32767, 23));
        pan_Town.setMinimumSize(new java.awt.Dimension(503, 23));

        javax.swing.GroupLayout pan_TownLayout = new javax.swing.GroupLayout(pan_Town);
        pan_Town.setLayout(pan_TownLayout);
        pan_TownLayout.setHorizontalGroup(
            pan_TownLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 641, Short.MAX_VALUE)
        );
        pan_TownLayout.setVerticalGroup(
            pan_TownLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 23, Short.MAX_VALUE)
        );

        pan_Region.setBackground(new java.awt.Color(204, 255, 255));
        pan_Region.setMaximumSize(new java.awt.Dimension(32767, 23));
        pan_Region.setMinimumSize(new java.awt.Dimension(503, 23));

        javax.swing.GroupLayout pan_RegionLayout = new javax.swing.GroupLayout(pan_Region);
        pan_Region.setLayout(pan_RegionLayout);
        pan_RegionLayout.setHorizontalGroup(
            pan_RegionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 641, Short.MAX_VALUE)
        );
        pan_RegionLayout.setVerticalGroup(
            pan_RegionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 23, Short.MAX_VALUE)
        );

        pan_Country.setBackground(new java.awt.Color(204, 255, 255));
        pan_Country.setMaximumSize(new java.awt.Dimension(32767, 23));
        pan_Country.setMinimumSize(new java.awt.Dimension(503, 23));

        javax.swing.GroupLayout pan_CountryLayout = new javax.swing.GroupLayout(pan_Country);
        pan_Country.setLayout(pan_CountryLayout);
        pan_CountryLayout.setHorizontalGroup(
            pan_CountryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 641, Short.MAX_VALUE)
        );
        pan_CountryLayout.setVerticalGroup(
            pan_CountryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 23, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lab_TitleState, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lab_TitleTown, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lab_Country, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(10, 10, 10)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pan_Town, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pan_Region, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pan_Country, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pan_Town, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_TitleTown, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pan_Region, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_TitleState, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lab_Country)
                    .addComponent(pan_Country, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Diagnosis Query", jPanel2);

        jLabel13.setFont(new java.awt.Font("新細明體", 1, 14));
        jLabel13.setText("Medicine:");

        javax.swing.GroupLayout pan_TopLayout = new javax.swing.GroupLayout(pan_Top);
        pan_Top.setLayout(pan_TopLayout);
        pan_TopLayout.setHorizontalGroup(
            pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 684, Short.MAX_VALUE)
        );
        pan_TopLayout.setVerticalGroup(
            pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 26, Short.MAX_VALUE)
        );

        lab_Total.setFont(new java.awt.Font("新細明體", 1, 14));
        lab_Total.setText("Total Quantity:");

        tab_Medicine.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tab_Medicine.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tab_MedicineMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tab_Medicine);

        txt_Total.setEditable(false);
        txt_Total.setFont(new java.awt.Font("新細明體", 1, 14));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 767, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(lab_Total)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt_Total, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addGap(18, 18, 18)
                        .addComponent(pan_Top, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(pan_Top, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 436, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab_Total)
                    .addComponent(txt_Total, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jTabbedPane1.addTab(" MedicineUsed ", jPanel3);

        jButton1.setText("Close");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 792, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 551, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_DCclearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_DCclearActionPerformed
      if (lis_DCselect.getModel().getSize() > 0) ((DefaultListModel) this.lis_DCselect.getModel()).removeAllElements();
}//GEN-LAST:event_btn_DCclearActionPerformed

    private void btn_DCaddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_DCaddActionPerformed
    if(this.lis_DCselect.getModel().getSize() == 0){//jlist為空時
        DefaultListModel  model   =   new   DefaultListModel();
        if(!m_Cobww.getSelectedItem().equals("")){
            model.add(0, m_Cobww.getSelectedItem());
            this.lis_DCselect.setModel(model);
        }
    } else {
        DefaultListModel model= (DefaultListModel) this.lis_DCselect.getModel();
        model.add(model.getSize(),m_Cobww.getSelectedItem());
        this.lis_DCselect.setModel(model);//增加
    }
        
}//GEN-LAST:event_btn_DCaddActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        String gender = "--";
        String age = "--";
        String regtime = "--";

        String sql = "SELECT patients_info.p_no AS 'NO.', "+
                "(YEAR(CURDATE())-YEAR(patients_info.birth))-(RIGHT(CURDATE(),5)< RIGHT(patients_info.birth,5)) AS Age, "+
                "patients_info.gender AS Gender, "+
                "concat(diagnosis_code.icd_code,'  ',diagnosis_code.name) AS Diagnosis, "+
                "registration_info.reg_time AS 'Registration Time' , "+
                "patients_info.bloodtype AS Bloodtype "+
                "FROM diagnosis_code, "+
                "diagnostic, "+
                "registration_info, "+
                "patients_info, "+
                "outpatient_services "+
                "WHERE "+
                "outpatient_services.reg_guid=registration_info.guid "+
                "AND patients_info.p_no=registration_info.p_no "+
                "AND diagnostic.dia_code=diagnosis_code.icd_code "+
                "AND diagnostic.os_guid=outpatient_services.guid ";

        // 年齡條件
        if (cbox_AgeS.getSelectedItem() != null && cbox_AgeE.getSelectedItem() != null  && !cbox_AgeS.getSelectedItem().toString().trim().equals("") && !cbox_AgeE.getSelectedItem().toString().trim().equals("") ){
             sql+= "AND (YEAR(CURDATE())-YEAR(patients_info.birth))-(RIGHT(CURDATE(),5)< RIGHT(patients_info.birth,5)) BETWEEN "+cbox_AgeS.getSelectedItem().toString()+" AND "+cbox_AgeE.getSelectedItem().toString()+"  ";
             age = cbox_AgeS.getSelectedItem().toString() + " - "+ cbox_AgeE.getSelectedItem().toString();
        }
           

        // 性別條件
        if (cbox_Gender.getSelectedIndex() == 1) {
            sql+= "AND patients_info.gender = 'M' ";
            gender = "M";
        }
        else if (cbox_Gender.getSelectedIndex() == 2) {
             sql+= "AND patients_info.gender = 'F' ";
             gender = "F";
        }

//        // 城市條件
//        if (m_CobwwTown.getSelectedItem() != null && !m_CobwwTown.getSelectedItem().toString().trim().equals("")) sql+= "AND patients_info.town = '"+m_CobwwTown.getSelectedItem().toString()+"' ";
//
//        // 區域條件
//        if (m_CobwwRegion.getSelectedItem() != null && !m_CobwwRegion.getSelectedItem().toString().trim().equals("")) sql+= "AND patients_info.state = '"+m_CobwwRegion.getSelectedItem().toString()+"' ";
//
//        // 國家條件
//        if (m_CobwwCountry.getSelectedItem() != null && !m_CobwwCountry.getSelectedItem().toString().trim().equals("")) sql+= "AND patients_info.country = '"+m_CobwwCountry.getSelectedItem().toString()+"' ";

        // 診斷條件
        for (int i = 0; i < lis_DCselect.getModel().getSize() ; i++  ) {
            String[] str = lis_DCselect.getModel().getElementAt(i).toString().split("    ");
            sql+= "AND diagnosis_code.icd_code = '"+str[0]+"' ";
        }

        // 掛號區間
        if (!datec_RegTimeS.getValue().equals(DEFAULTCONDATE) && !datec_RegTimeE.getValue().equals(DEFAULTCONDATE)) {
            sql+= "AND registration_info.reg_time BETWEEN '"+datec_RegTimeS.getValue()+"' AND '"+datec_RegTimeE.getValue()+"'  ";
            regtime = datec_RegTimeS.getValue() + " - " + datec_RegTimeE.getValue();
        }
            

        new Frm_ShowTable(this,sql,regtime , age, gender).setVisible(true);
        this.setEnabled(false);

}//GEN-LAST:event_jButton10ActionPerformed

    private void tab_MedicineMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab_MedicineMouseClicked

}//GEN-LAST:event_tab_MedicineMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        new Frm_Main().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_DCadd;
    private javax.swing.JButton btn_DCclear;
    private javax.swing.JComboBox cbox_AgeE;
    private javax.swing.JComboBox cbox_AgeS;
    private javax.swing.JComboBox cbox_Gender;
    private cc.johnwu.date.DateChooser datec_RegTimeE;
    private cc.johnwu.date.DateChooser datec_RegTimeS;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lab_Country;
    private javax.swing.JLabel lab_TitleState;
    private javax.swing.JLabel lab_TitleTown;
    private javax.swing.JLabel lab_Total;
    private javax.swing.JList lis_DCselect;
    private javax.swing.JPanel pan_Country;
    private javax.swing.JPanel pan_DiaSearch;
    private javax.swing.JPanel pan_Region;
    private javax.swing.JPanel pan_Top;
    private javax.swing.JPanel pan_Town;
    private javax.swing.JTable tab_Medicine;
    private javax.swing.JTextField txt_Total;
    private javax.swing.JTextField txt_TotalClinic;
    private javax.swing.JTextField txt_TotalDoctor;
    private javax.swing.JTextField txt_TotalPatient;
    private javax.swing.JTextField txt_TotalStaff;
    private javax.swing.JTextField txt_TotalWaiting;
    private javax.swing.JTextField txt_totalPoli;
    // End of variables declaration//GEN-END:variables

    public void onDateChanged() {
       // throw new UnsupportedOperationException("Not supported yet.");
    }

}
