package Cashier;

import Common.Constant;
import Common.PrintTools;
import cc.johnwu.login.UserInfo;
import cc.johnwu.sql.DBC;
import java.awt.Frame;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;

/**
 *
 * @author bee
 */
public class Frm_CashierInfo extends javax.swing.JFrame {

    private String m_Sysname;
    private String m_Guid;
    private String m_Pno;
    private Frm_CashierList m_Frm;


    public Frm_CashierInfo(Frm_CashierList frm, String guid, String sysname, String pno) {
        initComponents();
        m_Sysname = sysname;
        m_Guid = guid;
        m_Frm = frm;
        m_Pno = pno;
        this.setLocationRelativeTo(this);
        this.tab_Payment.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);     // tabble不可按住多選
        addWindowListener(new WindowAdapter() {  // 畫面關閉原視窗enable
            @Override
            public void windowClosing(WindowEvent windowevent) {
                m_Frm.setEnabled(true);
            }
        });
        String sql = "SELECT * FROM patients_info WHERE p_no = "+m_Pno+"";
        try {
            ResultSet rs = DBC.executeQuery(sql);
            rs.next();
            // 取出病患基本資料
            this.txt_No.setText(rs.getString("p_no"));
            this.txt_Name.setText(rs.getString("firstname")+" "+rs.getString("lastname"));
            this.txt_Gender.setText(rs.getString("gender"));
            this.txt_Ps.setText(rs.getString("ps"));
        } catch (SQLException ex) {
            Logger.getLogger(Frm_CashierInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
 
       initTable();
    }



    private void initTable() {
        DefaultTableModel tabModel = null;
        ResultSet rs = null;
        String sql =null;
        Object[][] dataArray = null;
        try {
            if (m_Sysname.equals("pha")) {

                    String[] title = {"guid", " ", "Code", "Item", "Quantity", "Urgent", "Cost"}; // table表頭
                    sql = "SELECT medicine_stock.guid, medicines.code AS 'Code', " +
                            "medicines.item AS 'Item', " +
                            "medicine_stock.quantity AS 'Quantity', " +
                            "medicine_stock.urgent AS 'Urgent', " +
                            "medicine_stock.price AS 'Price' " +
                            "FROM medicines, medicine_stock, outpatient_services, registration_info " +
                            "WHERE registration_info.guid = '" + m_Guid + "' " +
                            "AND medicine_stock.os_guid = outpatient_services.guid " +
                            "AND outpatient_services.reg_guid = registration_info.guid " +
                            "AND medicines.code = medicine_stock.m_code";
                    rs = DBC.executeQuery(sql);
                    rs.last();
                    dataArray = new Object[rs.getRow()][8];
                    rs.beforeFirst();
                    int i = 0;
                    while (rs.next()) {
                        dataArray[i][0] = rs.getString("guid");
                        dataArray[i][1] = i + 1;
                        dataArray[i][2] = rs.getString("Code");
                        dataArray[i][3] = rs.getString("Item");
                        dataArray[i][4] = rs.getString("Quantity");
                        dataArray[i][6] = rs.getString("Price");
                        i++;
                    }
                    tabModel = new DefaultTableModel(dataArray, title) {

                        @Override
                        public boolean isCellEditable(int rowIndex, int columnIndex) {
              
                            if (columnIndex == 6) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    };


            } else if (m_Sysname.equals("lab")) {
                String[] title = {"guid"," ", "Code","Name", "Urgent","Cost"};   // table表頭
                sql = "SELECT prescription.guid, " +
                        "prescription.code AS 'Code', " +
                        "prescription_code.name AS 'Name', " +
                        "prescription.cost AS 'Cost' " +
                        "FROM prescription LEFT JOIN outpatient_services ON prescription.os_guid = outpatient_services.guid, registration_info, " +
                             "prescription_code, shift_table, policlinic,poli_room,staff_info " +
                        "WHERE registration_info.guid = '"+m_Guid+"' " +
                            "AND registration_info.shift_guid = shift_table.guid " +
                            "AND shift_table.room_guid = poli_room.guid " +
                            "AND poli_room.poli_guid = policlinic.guid " +
                            "AND staff_info.s_id = shift_table.s_id " +
                            "AND  (outpatient_services.reg_guid = registration_info.guid " +
                            "OR prescription.case_guid = registration_info.guid) " +
                            "AND prescription_code.code = prescription.code " +
                            "AND prescription_code.type <> '"+Constant.X_RAY_CODE+"' ";
                    rs = DBC.executeQuery(sql);
                    rs.last();
                    dataArray = new Object[rs.getRow()][6];
                    rs.beforeFirst();
                    int i = 0;
                    while (rs.next()) {
                        dataArray[i][0] = rs.getString("guid");
                        dataArray[i][1] = i + 1;
                        dataArray[i][2] = rs.getString("Code");
                        dataArray[i][3] = rs.getString("Name");
                        dataArray[i][5] = rs.getString("Cost");
                        i++;
                    }
                    tabModel = new DefaultTableModel(dataArray, title) {

                        @Override
                        public boolean isCellEditable(int rowIndex, int columnIndex) {
                            if (columnIndex == 5) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    };
    
            } else if (m_Sysname.equals("reg")) {
                String[] title = {"guid"," ", "Pateint No.","Name", "Dept.","Clinic","Cost" };   // table表頭
              
              sql = "SELECT registration_info.guid, " +
                    "patients_info.p_no AS 'Pateint No.',"+
                    "CONCAT(patients_info.firstname, ' ' ,patients_info.lastname) AS 'Name', " +
                    "policlinic.name AS 'Dept.', " +
                    "poli_room.name AS 'Clinic', "+
                    "registration_info.cost AS 'Cost' "+
                    "FROM registration_info, shift_table,policlinic , poli_room ,patients_info "+
                    "WHERE payment IS NULL "+
                    "AND shift_table.guid = registration_info.shift_guid "+
                    "AND policlinic.guid = poli_room.poli_guid "+
                    "AND poli_room.guid = shift_table.room_guid  "+

                    "AND registration_info.guid = '"+m_Guid+"' "+
                    "AND registration_info.p_no = patients_info.p_no ORDER BY registration_info.reg_time DESC";

              rs = DBC.executeQuery(sql);
                    rs.last();
                    dataArray = new Object[rs.getRow()][7];
                    rs.beforeFirst();
                    int i = 0;
                    while (rs.next()) {
                        dataArray[i][0] = rs.getString("guid");
                        dataArray[i][1] = i + 1;
                        dataArray[i][2] = rs.getString("Pateint No.");
                        dataArray[i][3] = rs.getString("Name");
                        dataArray[i][4] = rs.getString("Dept.");
                        dataArray[i][5] = rs.getString("Clinic");
                        dataArray[i][6] = rs.getString("Cost");
                        i++;
                    }
                    tabModel = new DefaultTableModel(dataArray, title) {

                        @Override
                        public boolean isCellEditable(int rowIndex, int columnIndex) {
                            if (columnIndex == 6) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    };
            } else if (m_Sysname.equals("xray")) {
                String[] title = {"guid"," ", "Code","Name", "Urgent","Cost"};   // table表頭
           
              sql = "SELECT prescription.guid, " +
                "prescription.code AS 'Code', " +
                "prescription_code.name AS 'Name', " +
                "prescription.cost AS 'Cost' " +
                "FROM prescription, outpatient_services, registration_info, " +
                     "prescription_code, shift_table, policlinic,poli_room,staff_info " +
                "WHERE registration_info.guid = '"+m_Guid+"' " +
                    "AND registration_info.shift_guid = shift_table.guid " +
                    "AND shift_table.room_guid = poli_room.guid " +
                    "AND poli_room.poli_guid = policlinic.guid " +
                    "AND staff_info.s_id = shift_table.s_id " +
                    "AND prescription.os_guid = outpatient_services.guid " +
                    "AND prescription_code.code = prescription.code " +
                    "AND outpatient_services.reg_guid = registration_info.guid " +
                    "AND prescription_code.type = '"+Constant.X_RAY_CODE+"' ";

              rs = DBC.executeQuery(sql);
                    rs.last();
                    dataArray = new Object[rs.getRow()][6];
                    rs.beforeFirst();
                    int i = 0;
                    while (rs.next()) {
                        dataArray[i][0] = rs.getString("guid");
                        dataArray[i][1] = i + 1;
                        dataArray[i][2] = rs.getString("Code");
                        dataArray[i][3] = rs.getString("Name");
                        dataArray[i][5] = rs.getString("Cost");

                        i++;
                    }
                    tabModel = new DefaultTableModel(dataArray, title) {

                        @Override
                        public boolean isCellEditable(int rowIndex, int columnIndex) {
                            if (columnIndex == 5) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    };
            }


        }  catch (SQLException ex) {
                Logger.getLogger(Frm_CashierInfo.class.getName()).log(Level.SEVERE, null, ex);
         }

        tab_Payment.setModel(tabModel);
        tab_Payment.setRowHeight(30);
        Common.TabTools.setHideColumn(tab_Payment, 0);
        double total = 0;
        for (int i = 0; i < tab_Payment.getRowCount() ; i++) {
            if (tab_Payment.getValueAt(i, tab_Payment.getColumnCount()-1) != null && Common.Tools.isNumber(tab_Payment.getValueAt(i, tab_Payment.getColumnCount()-1).toString()))
                total += Double.parseDouble(tab_Payment.getValueAt(i, tab_Payment.getColumnCount()-1).toString());
        }
        txt_AmountReceivable.setText(String.valueOf(total));
        txt_PaidAmountKeyReleased(null);


    }


    private void setFinish(String finish) {
        // 判斷資料不是數值先把格子清空
        boolean IsCanFinish = true;
        String paymentType = null;
        for (int i = 0; i < tab_Payment.getRowCount() ; i++) {
                if (tab_Payment.getValueAt(i, tab_Payment.getColumnCount()-1) == null || tab_Payment.getValueAt(i, tab_Payment.getColumnCount()-1).toString().trim().equals("")
                        || !Common.Tools.isNumber(tab_Payment.getValueAt(i, tab_Payment.getColumnCount()-1).toString())){
                    tab_Payment.setValueAt("", i, tab_Payment.getColumnCount()-1);
                    IsCanFinish = false;
                }
         }

        if (!txt_PaidAmount.getText().trim().equals("") && !txt_AmountReceivable.getText().trim().equals("") && Common.Tools.isNumber(txt_PaidAmount.getText().trim()) && Common.Tools.isNumber(txt_AmountReceivable.getText().trim())) {
            txt_Arrears.setText(String.valueOf(Double.parseDouble(txt_AmountReceivable.getText().trim()) - Double.parseDouble(txt_PaidAmount.getText().trim())));
        } else {
            txt_Arrears.setText("");
            IsCanFinish = false;
        }

        if (!IsCanFinish) {
            JOptionPane.showMessageDialog(null, "Cost does not enter the complete.");
        } else {
            try {
                String sql = null;
                String sqlStr = null;
                
                if (m_Sysname.equals("pha")) {
                    paymentType = "P";
                    sqlStr = "PH";
                    sql = "UPDATE registration_info SET payment = '"+finish+"'";
                } else if (m_Sysname.equals("lab")) {
                    paymentType = "L";
                    sqlStr = "LA";
                      sql = "UPDATE registration_info SET lab_payment = '"+finish+"'";
                } else if (m_Sysname.equals("reg")) {
                    paymentType = "R";
                    sqlStr = "RE";
                     sql = "UPDATE registration_info SET payment = '"+finish+"'";
                }else if (m_Sysname.equals("xray")) {
                    sqlStr = "XR";
                    paymentType = "X";
                     sql = "UPDATE registration_info SET radiology_payment = '"+finish+"'";
                }
                sql += ",touchtime = RPAD((SELECT CASE WHEN MAX(B.touchtime) >= DATE_FORMAT(now(),'%Y%m%d%H%i%S') " +
                                "THEN concat(DATE_FORMAT(now(),'%Y%m%d%H%i%S'), COUNT(B.touchtime)) " +
                                "ELSE DATE_FORMAT(now(),'%Y%m%d%H%i%S') " +
                                "END touchtime FROM (SELECT touchtime FROM registration_info) AS B WHERE B.touchtime LIKE " +
                                "concat(DATE_FORMAT(now(),'%Y%m%d%H%i%S'),'%')),20,'000000')   WHERE guid = '" + m_Guid+ "'";
                DBC.executeUpdate(sql);

                // 儲存付費記錄
                sql = "INSERT INTO cashier(no, reg_guid, p_no, typ, payment_time, amount_receivable, paid_amount, arrears, s_no) " +
                        "SELECT CASE  WHEN  MAX(`no`)  IS  NULL THEN '"+sqlStr+"00000001' ELSE "+
                        " INSERT (MAX(`no`), "+
                        "LENGTH(MAX(`no`)) - LENGTH(SUBSTRING(MAX(`no`),3)+1) + 1, "+
                        "LENGTH(SUBSTRING(MAX(`no`),3)+1), "+
                        "SUBSTRING(MAX(`no`),3)+1) END  "+
                        ",'"+m_Guid+"', '"+m_Pno+"', '"+paymentType+"', " +
                        "NOW(), "+txt_AmountReceivable.getText()+", "+txt_PaidAmount.getText()+", " +
                        ""+txt_Arrears.getText()+", '"+UserInfo.getUserNO()+"' " +
                        "FROM cashier WHERE no LIKE '"+sqlStr+"%'  " ;
                        System.out.println(sql);
                DBC.executeUpdate(sql);
                

             } catch (SQLException ex) {
                Logger.getLogger(Frm_CashierInfo.class.getName()).log(Level.SEVERE, null, ex);
            }
            PrintTools pt = null;
            pt = new PrintTools();
            pt.DoPrint(11, m_Guid,paymentType);
            JOptionPane.showMessageDialog(null, "Saved successfully.");
            m_Frm.setEnabled(true);
            this.dispose();
        }
    }



    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pan_Top = new javax.swing.JPanel();
        lab_TitleNo = new javax.swing.JLabel();
        lab_TitleName = new javax.swing.JLabel();
        lab_TitlePs = new javax.swing.JLabel();
        lab_TitleSex = new javax.swing.JLabel();
        txt_Ps = new javax.swing.JTextField();
        txt_Gender = new javax.swing.JTextField();
        txt_No = new javax.swing.JTextField();
        txt_Name = new javax.swing.JTextField();
        btn_Close = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tab_Payment = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txt_AmountReceivable = new javax.swing.JTextField();
        txt_PaidAmount = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txt_Arrears = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        mnb = new javax.swing.JMenuBar();
        menu_File = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        mnit_Back = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Cashier Information");
        setResizable(false);

        pan_Top.setBackground(new java.awt.Color(228, 228, 228));
        pan_Top.setFont(new java.awt.Font("Bitstream Vera Sans", 1, 18));

        lab_TitleNo.setText("Patient No.:");

        lab_TitleName.setText("Name:");

        lab_TitlePs.setText("PS:");

        lab_TitleSex.setText("Gender:");

        txt_Ps.setEditable(false);

        txt_Gender.setEditable(false);

        txt_No.setEditable(false);

        txt_Name.setEditable(false);

        javax.swing.GroupLayout pan_TopLayout = new javax.swing.GroupLayout(pan_Top);
        pan_Top.setLayout(pan_TopLayout);
        pan_TopLayout.setHorizontalGroup(
            pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_TopLayout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addGroup(pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lab_TitleName, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lab_TitleNo, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_No, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)
                    .addComponent(txt_Name, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE))
                .addGap(63, 63, 63)
                .addGroup(pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lab_TitlePs)
                    .addComponent(lab_TitleSex))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt_Ps)
                    .addComponent(txt_Gender, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE))
                .addContainerGap(106, Short.MAX_VALUE))
        );
        pan_TopLayout.setVerticalGroup(
            pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_TopLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab_TitleNo)
                    .addComponent(txt_No, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_Gender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_TitleSex))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab_TitleName)
                    .addComponent(txt_Name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_Ps, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_TitlePs))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pan_TopLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {txt_Gender, txt_Name, txt_No, txt_Ps});

        btn_Close.setText("Save");
        btn_Close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CloseActionPerformed(evt);
            }
        });

        jButton1.setText("Complete the payment");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        tab_Payment.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tab_Payment.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tab_PaymentMouseClicked(evt);
            }
        });
        tab_Payment.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tab_PaymentFocusLost(evt);
            }
        });
        tab_Payment.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tab_PaymentKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tab_Payment);

        jLabel1.setFont(new java.awt.Font("新細明體", 1, 18));
        jLabel1.setText("Amount Receivable:");

        txt_AmountReceivable.setEditable(false);
        txt_AmountReceivable.setFont(new java.awt.Font("新細明體", 1, 18));
        txt_AmountReceivable.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        txt_PaidAmount.setFont(new java.awt.Font("新細明體", 1, 18)); // NOI18N
        txt_PaidAmount.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txt_PaidAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_PaidAmountActionPerformed(evt);
            }
        });
        txt_PaidAmount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_PaidAmountKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_PaidAmountKeyReleased(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("新細明體", 1, 18));
        jLabel2.setText("-");

        jLabel3.setFont(new java.awt.Font("新細明體", 1, 18));
        jLabel3.setText("Paid Amount:");

        jLabel4.setFont(new java.awt.Font("新細明體", 1, 18));
        jLabel4.setText("=");

        txt_Arrears.setEditable(false);
        txt_Arrears.setFont(new java.awt.Font("新細明體", 1, 18));
        txt_Arrears.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        jLabel5.setFont(new java.awt.Font("新細明體", 1, 18));
        jLabel5.setText("Arrears:");

        menu_File.setText("File");

        jMenuItem3.setText("Cashier Record");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        menu_File.add(jMenuItem3);

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
                    .addComponent(pan_Top, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 828, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_AmountReceivable, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_PaidAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(btn_Close, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_Arrears, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pan_Top, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 408, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txt_AmountReceivable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(txt_PaidAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(txt_Arrears, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(btn_Close))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_CloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CloseActionPerformed

        // 判斷資料不是數值先把格子清空
        for (int i = 0; i < tab_Payment.getRowCount() ; i++) {
                if (tab_Payment.getValueAt(i, tab_Payment.getColumnCount()-1) == null || !Common.Tools.isNumber(tab_Payment.getValueAt(i, tab_Payment.getColumnCount()-1).toString()))
                   tab_Payment.setValueAt("", i, tab_Payment.getColumnCount()-1);
         }

        try {
            String sql = "";
            if (m_Sysname.equals("pha")) {
                for (int i = 0; i < tab_Payment.getRowCount() ; i++) {
                    if (Common.Tools.isNumber(tab_Payment.getValueAt(i, tab_Payment.getColumnCount()-1).toString())) {
                            sql = "UPDATE medicine_stock SET price = " + tab_Payment.getValueAt(i, tab_Payment.getColumnCount() - 1) + " WHERE guid = '" + tab_Payment.getValueAt(i, 0) + "'";
                            DBC.executeUpdate(sql);
                    }
                }
            } else if (m_Sysname.equals("lab")) {
                for (int i = 0; i < tab_Payment.getRowCount() ; i++) {
                    if (Common.Tools.isNumber(tab_Payment.getValueAt(i, tab_Payment.getColumnCount()-1).toString())) {
                            sql = "UPDATE prescription SET cost = " + tab_Payment.getValueAt(i, tab_Payment.getColumnCount() - 1) + " WHERE guid = '" + tab_Payment.getValueAt(i, 0) + "'";
                            DBC.executeUpdate(sql);
                    }
                }

            } else if (m_Sysname.equals("reg")) {
                for (int i = 0; i < tab_Payment.getRowCount() ; i++) {
                    if (Common.Tools.isNumber(tab_Payment.getValueAt(i, tab_Payment.getColumnCount()-1).toString())) {
                            sql = "UPDATE registration_info SET cost = " + tab_Payment.getValueAt(i, tab_Payment.getColumnCount() - 1) + " WHERE guid = '" + tab_Payment.getValueAt(i, 0) + "'";
                            DBC.executeUpdate(sql);
                    }
                }

            }else if (m_Sysname.equals("xray")) {
                for (int i = 0; i < tab_Payment.getRowCount() ; i++) {
                    if (Common.Tools.isNumber(tab_Payment.getValueAt(i, tab_Payment.getColumnCount()-1).toString())) {
                            sql = "UPDATE prescription SET cost = " + tab_Payment.getValueAt(i, tab_Payment.getColumnCount() - 1) + " WHERE guid = '" + tab_Payment.getValueAt(i, 0) + "'";
                            DBC.executeUpdate(sql);
                    }
                }

            }
            JOptionPane.showMessageDialog(null, "Saved successfully.");

         } catch (SQLException ex) {
            Logger.getLogger(Frm_CashierInfo.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_btn_CloseActionPerformed

    private void tab_PaymentKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tab_PaymentKeyReleased
        double total = 0;
        if (tab_Payment.getValueAt(tab_Payment.getSelectedRow(), tab_Payment.getColumnCount()-1) != null && Common.Tools.isNumber(tab_Payment.getValueAt(tab_Payment.getSelectedRow(), tab_Payment.getColumnCount()-1).toString())) {
            for (int i = 0; i < tab_Payment.getRowCount() ; i++) {
                if ( Common.Tools.isNumber(tab_Payment.getValueAt(i, tab_Payment.getColumnCount()-1).toString()))
                    total += Double.parseDouble(tab_Payment.getValueAt(i, tab_Payment.getColumnCount()-1).toString());
            }
            txt_AmountReceivable.setText(String.valueOf(total));
            txt_PaidAmountKeyReleased(null);
        }

    }//GEN-LAST:event_tab_PaymentKeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (txt_Arrears.getText().equals("0.0")) setFinish("F");
        else setFinish("A");

    }//GEN-LAST:event_jButton1ActionPerformed

    private void tab_PaymentMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab_PaymentMouseClicked
        tab_PaymentKeyReleased(null);
    }//GEN-LAST:event_tab_PaymentMouseClicked

    private void mnit_BackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnit_BackActionPerformed
        btn_CloseActionPerformed(null);
}//GEN-LAST:event_mnit_BackActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        this.setEnabled(false);
        new Frm_CashierHistory(this,m_Pno).setVisible(true);
}//GEN-LAST:event_jMenuItem3ActionPerformed

    private void txt_PaidAmountKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_PaidAmountKeyReleased
        if (!txt_PaidAmount.getText().trim().equals("") && !txt_AmountReceivable.getText().trim().equals("") 
                && Common.Tools.isNumber(txt_PaidAmount.getText().trim()) && Common.Tools.isNumber(txt_AmountReceivable.getText().trim()) &&
                Double.parseDouble(txt_AmountReceivable.getText().trim()) >= Double.parseDouble(txt_PaidAmount.getText().trim())) {
            txt_Arrears.setText(String.valueOf(Double.parseDouble(txt_AmountReceivable.getText().trim()) - Double.parseDouble(txt_PaidAmount.getText().trim())));
        } else {
            txt_Arrears.setText("");
        }
    }//GEN-LAST:event_txt_PaidAmountKeyReleased

    private void txt_PaidAmountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_PaidAmountKeyPressed
        
    }//GEN-LAST:event_txt_PaidAmountKeyPressed

    private void tab_PaymentFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tab_PaymentFocusLost
        tab_Payment.removeEditor();
        tab_PaymentKeyReleased(null);
    }//GEN-LAST:event_tab_PaymentFocusLost

    private void txt_PaidAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_PaidAmountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_PaidAmountActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Close;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lab_TitleName;
    private javax.swing.JLabel lab_TitleNo;
    private javax.swing.JLabel lab_TitlePs;
    private javax.swing.JLabel lab_TitleSex;
    private javax.swing.JMenu menu_File;
    private javax.swing.JMenuBar mnb;
    private javax.swing.JMenuItem mnit_Back;
    private javax.swing.JPanel pan_Top;
    private javax.swing.JTable tab_Payment;
    private javax.swing.JTextField txt_AmountReceivable;
    private javax.swing.JTextField txt_Arrears;
    private javax.swing.JTextField txt_Gender;
    private javax.swing.JTextField txt_Name;
    private javax.swing.JTextField txt_No;
    private javax.swing.JTextField txt_PaidAmount;
    private javax.swing.JTextField txt_Ps;
    // End of variables declaration//GEN-END:variables

}
