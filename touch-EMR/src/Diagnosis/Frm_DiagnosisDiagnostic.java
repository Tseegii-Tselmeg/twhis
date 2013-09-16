
package Diagnosis;

import ErrorMessage.StoredErrorMessage;
import cc.johnwu.login.UserInfo;
import cc.johnwu.sql.*;
import Multilingual.language;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;


public class Frm_DiagnosisDiagnostic extends javax.swing.JFrame {
    private String m_Pname;
    private String m_Pno;
    private Frm_DiagnosisInfo m_DiagnosisInfo;
    private DiagnosisInterface m_Frame;
    private boolean m_SelectPoli;
    /*多國語言變數*/
    private language paragraph = new language();
    private String[] line = new String(paragraph.setlanguage("DIAGNOSISDIAGNOSTIC")).split("\n") ;
    private String[] message = new String(paragraph.setlanguage("MESSAGE")).split("\n") ;
    /*輸出錯誤資訊變數*/
    StoredErrorMessage ErrorMessage = new StoredErrorMessage() ;


    public Frm_DiagnosisDiagnostic(Frm_DiagnosisInfo diagnosisInfo, String pno, String pname) {
        this.m_Pname = pname;
        this.m_Pno = pno;
        this.m_DiagnosisInfo = diagnosisInfo;
        this.m_Frame = null;
        initComponents();
        initFrame();
        initPoli();
        initLanguage();
    }

    public Frm_DiagnosisDiagnostic(DiagnosisInterface frame, String pno, String pname) {
        this.m_Pname = pname;
        this.m_Pno = pno;
        this.m_Frame = frame;
        initComponents();
        initFrame();
        initPoli();
        initLanguage();
        this.btn_Insert.setVisible(false);
    }

    // 初始化
    public void initFrame() {
        this.setExtendedState(Frm_DiagnosisDiagnostic.MAXIMIZED_BOTH);  // 最大化
        this.txta_Summary.setLineWrap(true);
        this.tab_Record.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);     // tabble不可按住多選
        this.tab_Diagnosis.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  // tabble不可按住多選
        this.tab_Medicine.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  // tabble不可按住多選
        this.setLocationRelativeTo(this);
        this.txta_Summary.setEditable(false);  // 病歷摘要
        this.lab_Name.setText(m_Pname);
        this.lab_No.setText(m_Pno);
        addWindowListener(new WindowAdapter() {  // 畫面關閉原視窗enable
            @Override
            public void windowClosing(WindowEvent windowevent) {
                btn_CloseActionPerformed(null);
            }
        });
        ((DefaultTableModel) tab_Record.getModel()).setRowCount(0);
        ((DefaultTableModel) tab_Diagnosis.getModel()).setRowCount(0);
        ((DefaultTableModel) tab_Prescription.getModel()).setRowCount(0);
        ((DefaultTableModel) tab_Medicine.getModel()).setRowCount(0);
    }


    // cob 加入 poli
    public void initPoli() {
        // 加入診別
        m_SelectPoli = false;
        ResultSet rsPoli  = null;
        try {
            this.cob_Policlinic.removeAllItems();
            this.cob_Policlinic.addItem("All");
            String sqlPoli = "SELECT name FROM policlinic";
            rsPoli =  DBC.executeQuery(sqlPoli);
            while (rsPoli.next()) {
                this.cob_Policlinic.addItem(rsPoli.getString("name"));
            }
            if (m_Frame == null) {
                cob_Policlinic.setSelectedItem(UserInfo.getUserPoliclinic());
            }
        } catch (SQLException e) {
//            Class.setError("Frm","func");
//            System.out.println(e);
                ErrorMessage.setData("Diagnosis", "Frm_DiagnosisDiagnostic" ,"initPoli()",
                        e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        } finally {
             try {DBC.closeConnection(rsPoli);}
             catch (Exception e){
                ErrorMessage.setData("Diagnosis", "Frm_DiagnosisDiagnostic" ,"initPoli() - DBC.closeConnection",
                        e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
             }
        }
        m_SelectPoli = true;
        setTabRecord();  //加入看診紀錄
    }
    
    private void initLanguage() {
        //this.lab_TitleNo.setText(paragraph.getLanguage(line, "TITLENO"));
        this.lab_TitleName.setText(paragraph.getLanguage(line, "TITLENAME"));
        this.lab_Policlinic.setText(paragraph.getLanguage(line, "POLICLINIC"));
        this.lab_Ps.setText(paragraph.getLanguage(line, "PS"));
        //this.btn_Insert.setText(paragraph.getLanguage(line, "INSERT"));
        this.btn_Close.setText(paragraph.getLanguage(message, "CLOSE"));
        menu_File.setText(paragraph.getLanguage(message, "FILE"));
        mnit_Back.setText(paragraph.getLanguage(message, "CLOSE"));
        this.setTitle(paragraph.getLanguage(line, "TITLEDIAGNOSTIC"));
        
    }
       // tab_Record 預設選定行
    public void setTabRecordSelect() {   
        if(tab_Record.getRowCount() != 0) {
            tab_Record.setRowSelectionInterval(0, 0);
            tab_RecordMouseClicked(null);
        }
    }
    
    // 加入看診紀錄
    public void setTabRecord() {  
        ResultSet rsRecord = null;
  
        try {
           Object policlinic = null;
           if (cob_Policlinic.getSelectedIndex() == 0) {
                policlinic = "%";
           } else {
                policlinic = cob_Policlinic.getSelectedItem().toString().trim();
           }
           
            String sqlRecord =
                    "SELECT shift_table.shift_date AS '"+paragraph.getLanguage(line, "DATE")+"', " +
                           "CASE shift_table.shift " +
                           "WHEN '1' THEN 'Morning' " +
                           "WHEN '2' THEN 'Noon' " +
                           "WHEN '3' THEN 'Night' " +
                           "WHEN '4' THEN 'All Night' END '"+paragraph.getLanguage(line, "SHIFT")+"', " +
                           "policlinic.name AS 'Poli', " +
                           "concat(staff_info.firstname,'  ',staff_info.lastname) AS 'Doctor', " +
                           "registration_info.guid " +
                    "FROM registration_info, shift_table, policlinic, poli_room, staff_info " +
                    "WHERE registration_info.p_no = '"+m_Pno+"' " +
                    "AND policlinic.name LIKE '"+policlinic+"' " +
                    "AND registration_info.shift_guid = shift_table.guid " +
                    "AND shift_table.room_guid = poli_room.guid " +
                    "AND poli_room.poli_guid = policlinic.guid " +
                    "AND staff_info.s_id = shift_table.s_id " +
                    "AND registration_info.finish = 'F' " +
                    "ORDER BY registration_info.reg_time DESC";
            rsRecord = DBC.executeQuery(sqlRecord);
            if (rsRecord.next()) {
                this.tab_Record.setModel(HISModel.getModel(rsRecord, true));
                setHideColumn(tab_Record,5);
                setTabRecordSelect();
            } else {
                tab_Record.setModel(getModle(new String[]{"Message"},new String[][]{{"No Information."}}));
                btn_Insert.setEnabled(false);
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
//            Logger.getLogger(Frm_DiagnosisDiagnostic.class.getName()).log(Level.SEVERE, null, e);
                ErrorMessage.setData("Diagnosis", "Frm_DiagnosisDiagnostic" ,"setTabRecord()",
                        e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        }
    }

        /** 設定表單預設模型。*/
    private DefaultTableModel getModle(String[] title,String[][] data){
        return new DefaultTableModel(data,title){
                    @Override
                    public boolean isCellEditable(int r, int c){
                    return false;}
               };
    }

    // icd code
    public void setTab_Diagnosis() {
        ResultSet rsDiagnosis = null;
        try {
            
            String sqlDiagnosis =
              "SELECT diagnosis_code.icd_code AS '"+paragraph.getLanguage(line, "ICDCODE")+"', diagnosis_code.name AS 'Item'" +
              "FROM  diagnosis_code, diagnostic, outpatient_services, registration_info " +
              "WHERE registration_info.guid = '"+tab_Record.getValueAt(tab_Record.getSelectedRow(), 5)+"' " +
              "AND diagnostic.os_guid = outpatient_services.guid " +
              "AND outpatient_services.reg_guid = registration_info.guid " +
              "AND diagnosis_code.icd_code = diagnostic.dia_code";

            rsDiagnosis =  DBC.executeQuery(sqlDiagnosis);
            if (rsDiagnosis.next()) {
                this.tab_Diagnosis.setModel(HISModel.getModel(rsDiagnosis, true));
            } else {
                ((DefaultTableModel) tab_Diagnosis.getModel()).setRowCount(0);
            }
            
            //設定寬度與最小寬度
            TableColumn columnNo = this.tab_Diagnosis.getColumnModel().getColumn(0);
            TableColumn columnIcdCode = this.tab_Diagnosis.getColumnModel().getColumn(1);
            TableColumn columnName = this.tab_Diagnosis.getColumnModel().getColumn(2);

            columnNo.setMaxWidth(30);
            columnIcdCode.setPreferredWidth(80);
            columnName.setPreferredWidth(267);
            tab_Diagnosis.setRowHeight(30);

        } catch (SQLException e) {
//            Logger.getLogger(Frm_DiagnosisDiagnostic.class.getName()).log(Level.SEVERE, null, e);
                ErrorMessage.setData("Diagnosis", "Frm_DiagnosisDiagnostic" ,"setTab_Diagnosis()",
                        e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        } finally {
            try {DBC.closeConnection(rsDiagnosis);}
            catch (SQLException e){
                ErrorMessage.setData("Diagnosis", "Frm_DiagnosisDiagnostic" ,"setTab_Diagnosis() - DBC.closeConnection",
                        e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            }
        }
    }

    // 診斷
    public void settxta_Summary() {

        ResultSet rsSummary = null;
        try {
            String sqlSummary  =
                    "SELECT outpatient_services.summary, outpatient_services.ps " +
                    "FROM outpatient_services, registration_info " +
                    "WHERE registration_info.guid = '"+tab_Record.getValueAt(tab_Record.getSelectedRow(), 5)+"' " +
                    "AND outpatient_services.reg_guid = registration_info.guid";

            rsSummary = DBC.executeQuery(sqlSummary);
            rsSummary.next();
            this.txta_Summary.setText(rsSummary.getString("summary"));
            this.txt_Ps.setText(rsSummary.getString("ps"));
        } catch (SQLException e) {
//            Logger.getLogger(Frm_DiagnosisDiagnostic.class.getName()).log(Level.SEVERE, null, e);
                ErrorMessage.setData("Diagnosis", "Frm_DiagnosisDiagnostic" ,"settxta_Summary()",
                        e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        } finally {
            try {DBC.closeConnection(rsSummary);}
            catch (SQLException e){
                ErrorMessage.setData("Diagnosis", "Frm_DiagnosisDiagnostic" ,"settxta_Summary() - DBC.closeConnection",
                        e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            }
        }

    }
    
    // 處置
    public void setTab_Prescription() {
        ResultSet rsPrescription = null;
        try {
      
            String sqlPrescription =
                "SELECT prescription.code AS '"+paragraph.getLanguage(line, "CODE")+"', prescription_code.name AS 'Item' " +
                "FROM prescription, outpatient_services, registration_info, prescription_code " +
                "WHERE registration_info.guid = '"+tab_Record.getValueAt(tab_Record.getSelectedRow(), 5)+"' " +
                "AND prescription.os_guid = outpatient_services.guid " +
                "AND prescription_code.code = prescription.code " +
                "AND outpatient_services.reg_guid = registration_info.guid";
            rsPrescription = DBC.executeQuery(sqlPrescription);

            if (rsPrescription.next()) {
                this.tab_Prescription.setModel(HISModel.getModel(rsPrescription, true));
            } else {
                ((DefaultTableModel) tab_Prescription.getModel()).setRowCount(0);
            }
            
            //設定寬度與最小寬度
            TableColumn columnNo = this.tab_Prescription.getColumnModel().getColumn(0);
            TableColumn columnPrescriptionCode = this.tab_Prescription.getColumnModel().getColumn(1);
            TableColumn columnPrescriptionName = this.tab_Prescription.getColumnModel().getColumn(2);

            columnNo.setMaxWidth(25);
            columnPrescriptionCode.setMaxWidth(80);
            columnPrescriptionName.setPreferredWidth(300);
            tab_Prescription.setRowHeight(30);
        } catch (SQLException e) {
//            Logger.getLogger(Frm_DiagnosisDiagnostic.class.getName()).log(Level.SEVERE, null, e);
                ErrorMessage.setData("Diagnosis", "Frm_DiagnosisDiagnostic" ,"setTab_Prescription()",
                        e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        } finally {
            try {DBC.closeConnection(rsPrescription);}
            catch (SQLException e){
                ErrorMessage.setData("Diagnosis", "Frm_DiagnosisDiagnostic" ,"setTab_Prescription() - DBC.closeConnection",
                        e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            }
        }
    }
    
    // 藥品
    public void setTab_Medicines() {  
        ResultSet rsMedicines = null;
        Object[][] medicineArray = null;
        String[] medicineTitle = {" ","Code", "Item", "Dosage",
                                  paragraph.getLanguage(line, "UNIT"), "Frequency" ,"Usage" ,
                                  "Duration" ,paragraph.getLanguage(line, "QUANTITY"),paragraph.getLanguage(line, "URGENT"),
                                  "Powder",paragraph.getLanguage(line, "PS")};

        try {
            String sqlMedicines =
                "SELECT medicines.code, medicines.item, medicine_stock.dosage, medicines.unit, medicine_stock.usage, " +
                       "medicine_stock.way, medicine_stock.day, medicine_stock.quantity, " +
                       "medicine_stock.urgent, medicine_stock.powder , medicine_stock.ps " +
                "FROM medicines, medicine_stock, outpatient_services, registration_info " +
                "WHERE registration_info.guid = '"+tab_Record.getValueAt(tab_Record.getSelectedRow(), 5)+"' " +
                "AND medicine_stock.os_guid = outpatient_services.guid " +
                "AND outpatient_services.reg_guid = registration_info.guid " +
                "AND medicines.code = medicine_stock.m_code";
            rsMedicines = DBC.executeQuery(sqlMedicines);

            if (rsMedicines.last()) {
                medicineArray = new Object[rsMedicines.getRow()][medicineTitle.length];
                
                rsMedicines.beforeFirst();
                while (rsMedicines.next()) {
                    for(int i = 0; i < medicineTitle.length; i++) {
                        switch(i){
                            case 0:
                                medicineArray[rsMedicines.getRow()-1][i] = rsMedicines.getRow();
                                break;
                            default:
                                medicineArray[rsMedicines.getRow()-1][i] = rsMedicines.getString(i);
                                break;
                        }
                    }
                    DefaultTableModel model = new DefaultTableModel(medicineArray,medicineTitle){
                    @Override
                    // 設定欄位可否編輯
                         public boolean isCellEditable(int rowIndex,int columnIndex) {
                            return false;
                         }
                    };
                    tab_Medicine.setModel(model);
                }
            } else {
                DefaultTableModel model = new DefaultTableModel(medicineArray,medicineTitle){
                    @Override
                    // 設定欄位可否編輯
                         public boolean isCellEditable(int rowIndex,int columnIndex) {
                            return false;
                         }
                    };
                    tab_Medicine.setModel(model);
                ((DefaultTableModel) tab_Medicine.getModel()).setRowCount(0);
            }
            TableColumn medicineColumnNo = tab_Medicine.getColumnModel().getColumn(0);
            TableColumn medicineColumnCode = tab_Medicine.getColumnModel().getColumn(1);
            TableColumn medicineColumnItem = tab_Medicine.getColumnModel().getColumn(2);
            TableColumn medicineColumnDosage = tab_Medicine.getColumnModel().getColumn(3);
            TableColumn medicineColumnUnit = tab_Medicine.getColumnModel().getColumn(4);
            TableColumn medicineColumnUsage = tab_Medicine.getColumnModel().getColumn(5);
            TableColumn medicineColumnWay = tab_Medicine.getColumnModel().getColumn(6);
            TableColumn medicineColumnDay = tab_Medicine.getColumnModel().getColumn(7);
            TableColumn medicineColumnQuantity = tab_Medicine.getColumnModel().getColumn(8);
            TableColumn medicineColumnUrgent = tab_Medicine.getColumnModel().getColumn(9);
            TableColumn medicineColumnPowder = tab_Medicine.getColumnModel().getColumn(10);
            TableColumn medicineColumnPs = tab_Medicine.getColumnModel().getColumn(11);

            medicineColumnNo.setMaxWidth(30);
            medicineColumnCode.setPreferredWidth(100);
            medicineColumnItem.setPreferredWidth(200);
            medicineColumnDosage.setMaxWidth(55);
            medicineColumnUnit.setPreferredWidth(50);
            medicineColumnUsage.setMaxWidth(55);
            medicineColumnWay.setMaxWidth(55);
            medicineColumnDay.setMaxWidth(55);
            medicineColumnQuantity.setMaxWidth(55);
            medicineColumnUrgent.setMaxWidth(55);
            medicineColumnPowder.setMaxWidth(55);
            medicineColumnPs.setMaxWidth(250);
            tab_Medicine.setRowHeight(30);
            setHideColumn(tab_Medicine, 4);
        } catch (SQLException e) {
//            Logger.getLogger(Frm_DiagnosisDiagnostic.class.getName()).log(Level.SEVERE, null, e);
                ErrorMessage.setData("Diagnosis", "Frm_DiagnosisDiagnostic" ,"setTab_Medicines()",
                        e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        } finally {
            try {DBC.closeConnection(rsMedicines);}
            catch (SQLException e){
                ErrorMessage.setData("Diagnosis", "Frm_DiagnosisDiagnostic" ,"setTab_Medicines() - DBC.closeConnection",
                        e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            }
        }
    }
    
    // 隱藏欄位
    public void setHideColumn(JTable table,int index){  
             TableColumn tc= table.getColumnModel().getColumn(index);
             tc.setMaxWidth(0); tc.setPreferredWidth(0); tc.setMinWidth(0);
             tc.setWidth(0); table.getTableHeader().getColumnModel().getColumn(index).setMaxWidth(0);
             table.getTableHeader().getColumnModel().getColumn(index).setMinWidth(0);
    }

    public void setOnTop() {
        this.setAlwaysOnTop(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pan_Center = new javax.swing.JPanel();
        span_Medicine = new javax.swing.JScrollPane();
        tab_Medicine = new javax.swing.JTable();
        span_Record = new javax.swing.JScrollPane();
        tab_Record = new javax.swing.JTable();
        span_Diagnosis = new javax.swing.JScrollPane();
        tab_Diagnosis = new javax.swing.JTable();
        span_Prescription = new javax.swing.JScrollPane();
        tab_Prescription = new javax.swing.JTable();
        pan_CenterTop = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txta_Summary = new javax.swing.JTextArea();
        pan_Up = new javax.swing.JPanel();
        lab_Policlinic = new javax.swing.JLabel();
        cob_Policlinic = new javax.swing.JComboBox();
        lab_TitleName = new javax.swing.JLabel();
        lab_Name = new javax.swing.JLabel();
        lab_TitleNo = new javax.swing.JLabel();
        lab_No = new javax.swing.JLabel();
        btn_Insert = new javax.swing.JButton();
        btn_Close = new javax.swing.JButton();
        lab_Ps = new javax.swing.JLabel();
        txt_Ps = new javax.swing.JTextField();
        mnb = new javax.swing.JMenuBar();
        menu_File = new javax.swing.JMenu();
        mnit_Back = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Diagnostic");
        setAlwaysOnTop(true);

        tab_Medicine.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "", "", "", "", "", "", "", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tab_Medicine.setRowHeight(25);
        span_Medicine.setViewportView(tab_Medicine);
        tab_Medicine.getColumnModel().getColumn(0).setResizable(false);
        tab_Medicine.getColumnModel().getColumn(0).setPreferredWidth(30);
        tab_Medicine.getColumnModel().getColumn(1).setPreferredWidth(100);
        tab_Medicine.getColumnModel().getColumn(2).setPreferredWidth(252);
        tab_Medicine.getColumnModel().getColumn(3).setPreferredWidth(80);
        tab_Medicine.getColumnModel().getColumn(4).setPreferredWidth(65);
        tab_Medicine.getColumnModel().getColumn(5).setPreferredWidth(65);
        tab_Medicine.getColumnModel().getColumn(6).setResizable(false);
        tab_Medicine.getColumnModel().getColumn(6).setPreferredWidth(65);
        tab_Medicine.getColumnModel().getColumn(7).setPreferredWidth(70);

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
        tab_Record.getColumnModel().getColumn(0).setResizable(false);
        tab_Record.getColumnModel().getColumn(0).setPreferredWidth(30);
        tab_Record.getColumnModel().getColumn(1).setPreferredWidth(81);
        tab_Record.getColumnModel().getColumn(3).setPreferredWidth(130);
        tab_Record.getColumnModel().getColumn(4).setResizable(false);
        tab_Record.getColumnModel().getColumn(4).setPreferredWidth(130);

        tab_Diagnosis.setModel(new javax.swing.table.DefaultTableModel(
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
        tab_Diagnosis.setRowHeight(25);
        tab_Diagnosis.getTableHeader().setReorderingAllowed(false);
        span_Diagnosis.setViewportView(tab_Diagnosis);
        tab_Diagnosis.getColumnModel().getColumn(0).setResizable(false);
        tab_Diagnosis.getColumnModel().getColumn(0).setPreferredWidth(30);
        tab_Diagnosis.getColumnModel().getColumn(1).setPreferredWidth(100);
        tab_Diagnosis.getColumnModel().getColumn(2).setResizable(false);
        tab_Diagnosis.getColumnModel().getColumn(2).setPreferredWidth(227);

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
        span_Prescription.setViewportView(tab_Prescription);
        tab_Prescription.getColumnModel().getColumn(1).setPreferredWidth(30);

        pan_CenterTop.setBackground(new java.awt.Color(228, 228, 228));

        txta_Summary.setColumns(20);
        txta_Summary.setRows(1);
        txta_Summary.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txta_SummaryMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(txta_Summary);

        javax.swing.GroupLayout pan_CenterTopLayout = new javax.swing.GroupLayout(pan_CenterTop);
        pan_CenterTop.setLayout(pan_CenterTopLayout);
        pan_CenterTopLayout.setHorizontalGroup(
            pan_CenterTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 392, Short.MAX_VALUE)
        );
        pan_CenterTopLayout.setVerticalGroup(
            pan_CenterTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pan_CenterLayout = new javax.swing.GroupLayout(pan_Center);
        pan_Center.setLayout(pan_CenterLayout);
        pan_CenterLayout.setHorizontalGroup(
            pan_CenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan_CenterLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan_CenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(span_Medicine, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 796, Short.MAX_VALUE)
                    .addComponent(span_Prescription, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 796, Short.MAX_VALUE)
                    .addGroup(pan_CenterLayout.createSequentialGroup()
                        .addComponent(span_Record, javax.swing.GroupLayout.DEFAULT_SIZE, 398, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan_CenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(span_Diagnosis, javax.swing.GroupLayout.DEFAULT_SIZE, 392, Short.MAX_VALUE)
                            .addComponent(pan_CenterTop, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        pan_CenterLayout.setVerticalGroup(
            pan_CenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_CenterLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan_CenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(span_Record, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
                    .addGroup(pan_CenterLayout.createSequentialGroup()
                        .addComponent(pan_CenterTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(span_Diagnosis, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(span_Prescription, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(span_Medicine, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE))
        );

        lab_Policlinic.setText("Department ：");

        cob_Policlinic.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cob_PoliclinicItemStateChanged(evt);
            }
        });

        lab_TitleName.setText("Name ：");

        lab_Name.setText("--");

        lab_TitleNo.setText("Patient No. ：");

        lab_No.setText("--");

        javax.swing.GroupLayout pan_UpLayout = new javax.swing.GroupLayout(pan_Up);
        pan_Up.setLayout(pan_UpLayout);
        pan_UpLayout.setHorizontalGroup(
            pan_UpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_UpLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lab_TitleNo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lab_No, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lab_TitleName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lab_Name, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lab_Policlinic)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cob_Policlinic, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );
        pan_UpLayout.setVerticalGroup(
            pan_UpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_UpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(lab_TitleNo)
                .addComponent(lab_No)
                .addComponent(lab_TitleName)
                .addComponent(lab_Name)
                .addComponent(lab_Policlinic)
                .addComponent(cob_Policlinic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        btn_Insert.setText("Bring out");
        btn_Insert.setEnabled(false);
        btn_Insert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_InsertActionPerformed(evt);
            }
        });

        btn_Close.setText("Close");
        btn_Close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CloseActionPerformed(evt);
            }
        });

        lab_Ps.setText("p.s.");

        txt_Ps.setEditable(false);

        menu_File.setText("File");

        mnit_Back.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0));
        mnit_Back.setText("Close");
        mnit_Back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnit_BackActionPerformed(evt);
            }
        });
        menu_File.add(mnit_Back);

        mnb.add(menu_File);

        setJMenuBar(mnb);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lab_Ps)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_Ps, javax.swing.GroupLayout.DEFAULT_SIZE, 544, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_Insert, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_Close, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(19, 19, 19))
                    .addComponent(pan_Center, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pan_Up, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pan_Up, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pan_Center, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab_Ps)
                    .addComponent(btn_Insert)
                    .addComponent(btn_Close, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_Ps, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_CloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CloseActionPerformed
        if(m_Frame != null) {
            m_Frame.reSetEnable();
        } else {
            m_DiagnosisInfo.reSetEnable();
        }
        this.dispose();
    }//GEN-LAST:event_btn_CloseActionPerformed

    private void tab_RecordMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab_RecordMouseClicked
        if (tab_Record.getColumnCount() != 1 && this.tab_Record.getValueAt(this.tab_Record.getSelectedRow(), 1) != null && this.tab_Record.getSelectedRow() != -1) {
            // 判斷讀取的資料是否為目前診別  方可進行帶入資料
            if (tab_Record.getValueAt(tab_Record.getSelectedRow(), 3).equals(UserInfo.getUserPoliclinic())) {
                btn_Insert.setEnabled(true);
            } else {
                btn_Insert.setEnabled(false);
            }
            setTab_Diagnosis();      // set當次看診icd code的紀錄
            settxta_Summary();       // set 當次看診摘要
            setTab_Prescription();   // set 當次診斷
            setTab_Medicines();      // set 當次藥品
        }
        
    }//GEN-LAST:event_tab_RecordMouseClicked

    private void cob_PoliclinicItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cob_PoliclinicItemStateChanged
        if(evt.getStateChange() == java.awt.event.ItemEvent.SELECTED && m_SelectPoli == true) {
            ((DefaultTableModel)this.tab_Diagnosis.getModel()).setRowCount(0);
            ((DefaultTableModel)this.tab_Medicine.getModel()).setRowCount(0);
            ((DefaultTableModel)this.tab_Prescription.getModel()).setRowCount(0);
            this.txta_Summary.setText("");
            setTabRecord();
        }
    }//GEN-LAST:event_cob_PoliclinicItemStateChanged

    private void mnit_BackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnit_BackActionPerformed
        btn_CloseActionPerformed(null);
}//GEN-LAST:event_mnit_BackActionPerformed

    private void tab_RecordKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tab_RecordKeyReleased
        tab_RecordMouseClicked(null);
    }//GEN-LAST:event_tab_RecordKeyReleased

    private void btn_InsertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_InsertActionPerformed
        m_DiagnosisInfo.getCasehistory(txta_Summary.getText(),(String) tab_Record.getValueAt(tab_Record.getSelectedRow(), 5));
        btn_CloseActionPerformed(null);
    }//GEN-LAST:event_btn_InsertActionPerformed

    private void txta_SummaryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txta_SummaryMouseClicked
        new Summary(this, txta_Summary.getText().trim(), pan_CenterTop.getLocationOnScreen()
                , pan_CenterTop.getWidth(), pan_CenterTop.getHeight()
                , txta_Summary.getLineCount(),false ).setVisible(true);
        this.setAlwaysOnTop(false);
    }//GEN-LAST:event_txta_SummaryMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Close;
    private javax.swing.JButton btn_Insert;
    private javax.swing.JComboBox cob_Policlinic;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lab_Name;
    private javax.swing.JLabel lab_No;
    private javax.swing.JLabel lab_Policlinic;
    private javax.swing.JLabel lab_Ps;
    private javax.swing.JLabel lab_TitleName;
    private javax.swing.JLabel lab_TitleNo;
    private javax.swing.JMenu menu_File;
    private javax.swing.JMenuBar mnb;
    private javax.swing.JMenuItem mnit_Back;
    private javax.swing.JPanel pan_Center;
    private javax.swing.JPanel pan_CenterTop;
    private javax.swing.JPanel pan_Up;
    private javax.swing.JScrollPane span_Diagnosis;
    private javax.swing.JScrollPane span_Medicine;
    private javax.swing.JScrollPane span_Prescription;
    private javax.swing.JScrollPane span_Record;
    private javax.swing.JTable tab_Diagnosis;
    private javax.swing.JTable tab_Medicine;
    private javax.swing.JTable tab_Prescription;
    private javax.swing.JTable tab_Record;
    private javax.swing.JTextField txt_Ps;
    private javax.swing.JTextArea txta_Summary;
    // End of variables declaration//GEN-END:variables

}

