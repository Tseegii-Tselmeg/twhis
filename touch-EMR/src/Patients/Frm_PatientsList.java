package Patients;


import cc.johnwu.sql.*;
import cc.johnwu.finger.*;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import Multilingual.language;

public class Frm_PatientsList extends javax.swing.JFrame implements PatientsInterface,FingerPrintViewerInterface {
    
    private final int MAX_ROWS_OF_PAGE = 50;
    private ResultSet m_PatientsRS;

    private final int MAX_FINGERPRINT_COUNT = 5000;
    private String sql_FingerSelect;
    private String m_PatientsNO = "";
    /*多國語言變數*/
    private language paragraph = new language();
    private String[] line = new String(paragraph.setlanguage("PATIENTSLIST")).split("\n") ;
    private String[] message = new String(paragraph.setlanguage("MESSAGE")).split("\n") ;

    public Frm_PatientsList() {
        initComponents();
        init();
        showPatientsList();
        initLanguage();
    }

        /** 初始化*/
    private void init(){
        this.setExtendedState(Frm_PatientsList.MAXIMIZED_BOTH);  // 最大化
        this.setLocationRelativeTo(this);//視窗顯示至中
        FingerPrintScanner.setParentFrame(this);//打開指紋機
        addWindowListener(new WindowAdapter() {//視窗關閉事件
            @Override
            public void windowClosing(WindowEvent windowevent)
            {
                btn_CloseActionPerformed(null);
            }
        });
    }

    private void initLanguage() {
        this.btn_Search.setText(paragraph.getLanguage(message, "SEARCH"));
        this.btn_Add.setText(paragraph.getLanguage(line, "ADD"));
        this.btn_Edit.setText(paragraph.getLanguage(line, "EDIT"));
        this.btn_Delete.setText(paragraph.getLanguage(line, "DELETE"));
        this.btn_Close.setText(paragraph.getLanguage(message, "CLOSE"));
        cob_Conditions.setModel(
                new javax.swing.DefaultComboBoxModel(
                new String[] { paragraph.getLanguage(line, "ALL"), paragraph.getLanguage(line, "NO"), paragraph.getLanguage(line, "NAME"), paragraph.getLanguage(line, "BIRTH"),
                               paragraph.getLanguage(line, "IDNO"), paragraph.getLanguage(line, "PHONE"), paragraph.getLanguage(line, "TOWN"), paragraph.getLanguage(line, "ADDRESS")
                }
            )
        );
        this.setTitle(paragraph.getLanguage(line, "TITLEPATIENTLIST"));
    }

    private void showPatientsList(){
        ResultSet rs = null;
        String sql = "";
        String conditions = "";
        try {
            switch(this.cob_Conditions.getSelectedIndex()){
                case 0: // ALL
                conditions = "(UPPER(p_no) LIKE UPPER('%" + txt_Search.getText().replace(" ", "%") + "%')" +
                             "OR UPPER(concat(FirstName,' ',LastName)) LIKE UPPER('%" + txt_Search.getText().replace(" ", "%") + "%') " +
                             "OR UPPER(birth) LIKE UPPER('%" + txt_Search.getText().replace(" ", "%") + "%') " +
                             "OR UPPER(nhis_no) LIKE UPPER('%" + txt_Search.getText().replace(" ", "%") + "%') " +
                             "OR UPPER(nia_no) LIKE UPPER('%" + txt_Search.getText().replace(" ", "%") + "%') " +
                             "OR UPPER(phone) LIKE UPPER('%" + txt_Search.getText().replace(" ", "%") + "%') " +
                             "OR UPPER(cell_phone) LIKE UPPER('%" + txt_Search.getText().replace(" ", "%") + "%') " +
                             "OR UPPER(town) LIKE UPPER('%" + txt_Search.getText().replace(" ", "%") + "%') " +
                             "OR UPPER(address) LIKE UPPER('%" + txt_Search.getText().replace(" ", "%") + "%') " +
                             ")";
                    break;
                case 1: // P_NO
                    conditions = "(UPPER(p_no) LIKE UPPER('%" + txt_Search.getText().replace(" ", "%") + "%'))";
                    break;
                case 2: // NAME
                    conditions = "(UPPER(concat(FirstName,' ',LastName)) LIKE UPPER('%" + txt_Search.getText().replace(" ", "%") + "%'))";
                    break;
                case 3: // BIRTH
                    conditions = "(UPPER(birth) LIKE UPPER('%" + txt_Search.getText().replace(" ", "%") + "%'))";
                    break;
                case 4: // NHIS NO.
                    conditions = "(UPPER(nhis_no) LIKE UPPER('%" + txt_Search.getText().replace(" ", "%") + "%'))";
                    break;
                case 5: // NIA NO.
                    conditions = "(UPPER(nia_no) LIKE UPPER('%" + txt_Search.getText().replace(" ", "%") + "%'))";
                    break;
                case 6: // PHONE
                    conditions = "(UPPER(phone) LIKE UPPER('%" + txt_Search.getText().replace(" ", "%") + "%') " +
                             "OR UPPER(cell_phone) LIKE UPPER('%" + txt_Search.getText().replace(" ", "%") + "%'))";
                    break;
                case 7: // TOWN
                    conditions = "(UPPER(town) LIKE UPPER('%" + txt_Search.getText().replace(" ", "%") + "%'))";
                    break;
                case 8: // ADDRESS
                    conditions = "(UPPER(address) LIKE UPPER('%" + txt_Search.getText().replace(" ", "%") + "%'))";
                    break;
            }
            sql = "SELECT p_no AS '"+paragraph.getLanguage(line, "COL_NO")+"', " +
                        "concat(FirstName,' ',LastName) AS '"+paragraph.getLanguage(line, "COL_NAME")+"', " +
                        "Birth AS '"+paragraph.getLanguage(line, "COL_BIRTH")+"', " +
                        "phone AS '"+paragraph.getLanguage(line, "COL_PHONE")+"', " +
                        "cell_phone AS '"+paragraph.getLanguage(line, "COL_CELLPHONE")+"', " +
                        "Town AS '"+paragraph.getLanguage(line, "COL_TOWN")+"', " +
                        "Address AS '"+paragraph.getLanguage(line, "COL_ADDRESS")+"'" +
                  "FROM patients_info " +
                  "WHERE exist = 1 " +
                  "AND " + conditions;
            m_PatientsRS = DBC.executeQuery(sql);
            if(m_PatientsRS.last()){
                this.cob_Page.removeAllItems();
                int page = (m_PatientsRS.getRow()/MAX_ROWS_OF_PAGE)+(m_PatientsRS.getRow()%MAX_ROWS_OF_PAGE==0?0:1);
                for(int i=0; i<page;i++){
                    this.cob_Page.addItem(+ (i+1) + " of " + page);
                }
                m_PatientsRS.beforeFirst();
                tab_List.setModel(HISModel.getModel(m_PatientsRS, 1, MAX_ROWS_OF_PAGE));
                if(this.cob_Page.getItemCount()>1){
                    this.btn_Next.setEnabled(true);
                }else{
                    this.btn_Next.setEnabled(false);
                }
                this.btn_Previous.setEnabled(false);
            }else{
                tab_List.setModel(getModle(new String[]{"Message"},new String[][]{{paragraph.getLanguage(message, "NOINFORMATION")}}));
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        } finally {
            try{DBC.closeConnection(rs);}
            catch (SQLException ex) {}
        }
    }

    /** 設定表單預設模型。*/
    private DefaultTableModel getModle(String[] title,String[][] data){
        return new DefaultTableModel(data,title){
                @Override
                public boolean isCellEditable(int r, int c){
                return false;}};
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        Dialog_FingerConditions = new javax.swing.JDialog();
        jPanel2 = new javax.swing.JPanel();
        lab_Gender1 = new javax.swing.JLabel();
        lab_ListName = new javax.swing.JLabel();
        txt_Town = new javax.swing.JTextField();
        btn_Ok = new javax.swing.JButton();
        lab_Town = new javax.swing.JLabel();
        cob_Gender = new javax.swing.JComboBox();
        lab_FirstName1 = new javax.swing.JLabel();
        txt_FirstName = new javax.swing.JTextField();
        txt_LastName = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        pan_Center = new javax.swing.JPanel();
        pan_InnerUp = new javax.swing.JPanel();
        btn_Search = new javax.swing.JButton();
        txt_Search = new javax.swing.JFormattedTextField();
        cob_Conditions = new javax.swing.JComboBox();
        pan_InnerCenter = new javax.swing.JPanel();
        spn_PatientsList = new javax.swing.JScrollPane();
        tab_List = new javax.swing.JTable();
        pan_UnderCenter = new javax.swing.JPanel();
        btn_Previous = new javax.swing.JButton();
        cob_Page = new javax.swing.JComboBox();
        btn_Next = new javax.swing.JButton();
        pan_Right = new javax.swing.JPanel();
        btn_Close = new javax.swing.JButton();
        btn_Delete = new javax.swing.JButton();
        btn_Add = new javax.swing.JButton();
        btn_Edit = new javax.swing.JButton();
        fingerPrintViewer1 = new cc.johnwu.finger.FingerPrintViewer();
        lab_FingerprintSearch = new javax.swing.JLabel();

        Dialog_FingerConditions.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        Dialog_FingerConditions.setTitle("Fingetprint Conditions");
        Dialog_FingerConditions.setAlwaysOnTop(true);
        Dialog_FingerConditions.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        Dialog_FingerConditions.setMinimumSize(new java.awt.Dimension(238, 220));
        Dialog_FingerConditions.setModal(true);
        Dialog_FingerConditions.setResizable(false);

        lab_Gender1.setText("Gender : ");

        lab_ListName.setText("LastName : ");

        btn_Ok.setText("OK");
        btn_Ok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_OkActionPerformed(evt);
            }
        });

        lab_Town.setText("Town : ");

        cob_Gender.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "M", "F" }));

        lab_FirstName1.setText("FirstName : ");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lab_Gender1)
                    .addComponent(lab_ListName)
                    .addComponent(lab_FirstName1)
                    .addComponent(lab_Town))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btn_Ok, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE)
                    .addComponent(txt_Town, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE)
                    .addComponent(cob_Gender, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_LastName, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE)
                    .addComponent(txt_FirstName, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_FirstName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_FirstName1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_LastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_ListName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(cob_Gender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_Gender1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_Town, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_Town))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Ok, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("<html>Please input condition</html>");

        javax.swing.GroupLayout Dialog_FingerConditionsLayout = new javax.swing.GroupLayout(Dialog_FingerConditions.getContentPane());
        Dialog_FingerConditions.getContentPane().setLayout(Dialog_FingerConditionsLayout);
        Dialog_FingerConditionsLayout.setHorizontalGroup(
            Dialog_FingerConditionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Dialog_FingerConditionsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Dialog_FingerConditionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE))
                .addContainerGap())
        );
        Dialog_FingerConditionsLayout.setVerticalGroup(
            Dialog_FingerConditionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Dialog_FingerConditionsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Patient List");

        btn_Search.setText("Search");
        btn_Search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_SearchActionPerformed(evt);
            }
        });

        txt_Search.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_SearchKeyPressed(evt);
            }
        });

        cob_Conditions.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "ALL", "NO.", "Name", "Birth", "ID No.", "Phone", "Town", "Address" }));

        javax.swing.GroupLayout pan_InnerUpLayout = new javax.swing.GroupLayout(pan_InnerUp);
        pan_InnerUp.setLayout(pan_InnerUpLayout);
        pan_InnerUpLayout.setHorizontalGroup(
            pan_InnerUpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan_InnerUpLayout.createSequentialGroup()
                .addComponent(cob_Conditions, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_Search, javax.swing.GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Search, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pan_InnerUpLayout.setVerticalGroup(
            pan_InnerUpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_InnerUpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(cob_Conditions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(txt_Search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btn_Search))
        );

        pan_InnerCenter.setBackground(new java.awt.Color(234, 234, 234));

        tab_List.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tab_List.setRowHeight(25);
        tab_List.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tab_List.getTableHeader().setReorderingAllowed(false);
        tab_List.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tab_ListMouseClicked(evt);
            }
        });
        tab_List.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tab_ListKeyPressed(evt);
            }
        });
        spn_PatientsList.setViewportView(tab_List);

        pan_UnderCenter.setLayout(new java.awt.GridBagLayout());

        btn_Previous.setText("<<");
        btn_Previous.setEnabled(false);
        btn_Previous.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_PreviousActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.ipadx = 30;
        pan_UnderCenter.add(btn_Previous, gridBagConstraints);

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
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.ipadx = 30;
        pan_UnderCenter.add(btn_Next, gridBagConstraints);

        javax.swing.GroupLayout pan_InnerCenterLayout = new javax.swing.GroupLayout(pan_InnerCenter);
        pan_InnerCenter.setLayout(pan_InnerCenterLayout);
        pan_InnerCenterLayout.setHorizontalGroup(
            pan_InnerCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(spn_PatientsList, javax.swing.GroupLayout.DEFAULT_SIZE, 646, Short.MAX_VALUE)
            .addComponent(pan_UnderCenter, javax.swing.GroupLayout.DEFAULT_SIZE, 646, Short.MAX_VALUE)
        );
        pan_InnerCenterLayout.setVerticalGroup(
            pan_InnerCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_InnerCenterLayout.createSequentialGroup()
                .addComponent(spn_PatientsList, javax.swing.GroupLayout.DEFAULT_SIZE, 486, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pan_UnderCenter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout pan_CenterLayout = new javax.swing.GroupLayout(pan_Center);
        pan_Center.setLayout(pan_CenterLayout);
        pan_CenterLayout.setHorizontalGroup(
            pan_CenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pan_InnerCenter, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pan_InnerUp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pan_CenterLayout.setVerticalGroup(
            pan_CenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_CenterLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pan_InnerUp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pan_InnerCenter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btn_Close.setText("Close");
        btn_Close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CloseActionPerformed(evt);
            }
        });

        btn_Delete.setText("Delete");
        btn_Delete.setEnabled(false);
        btn_Delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_DeleteActionPerformed(evt);
            }
        });

        btn_Add.setText("Add");
        btn_Add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_AddActionPerformed(evt);
            }
        });

        btn_Edit.setText("Details");
        btn_Edit.setEnabled(false);
        btn_Edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_EditActionPerformed(evt);
            }
        });

        fingerPrintViewer1.setVisible(true);

        javax.swing.GroupLayout fingerPrintViewer1Layout = new javax.swing.GroupLayout(fingerPrintViewer1.getContentPane());
        fingerPrintViewer1.getContentPane().setLayout(fingerPrintViewer1Layout);
        fingerPrintViewer1Layout.setHorizontalGroup(
            fingerPrintViewer1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 90, Short.MAX_VALUE)
        );
        fingerPrintViewer1Layout.setVerticalGroup(
            fingerPrintViewer1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 119, Short.MAX_VALUE)
        );

        lab_FingerprintSearch.setText("  ");

        javax.swing.GroupLayout pan_RightLayout = new javax.swing.GroupLayout(pan_Right);
        pan_Right.setLayout(pan_RightLayout);
        pan_RightLayout.setHorizontalGroup(
            pan_RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_RightLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan_RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_Delete, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                    .addComponent(btn_Edit, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                    .addComponent(btn_Add, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                    .addComponent(btn_Close, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                    .addComponent(fingerPrintViewer1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lab_FingerprintSearch, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
                .addContainerGap())
        );
        pan_RightLayout.setVerticalGroup(
            pan_RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_RightLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(btn_Add)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Edit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Delete)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Close)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fingerPrintViewer1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lab_FingerprintSearch)
                .addContainerGap(232, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pan_Center, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pan_Right, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pan_Center, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pan_Right, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_AddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_AddActionPerformed
        showImage(null,"");
        new Frm_PatientMod(this).setVisible(true);
        this.setEnabled(false);
        this.setVisible(false);
}//GEN-LAST:event_btn_AddActionPerformed

    private void tab_ListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab_ListMouseClicked
        if(tab_List.getSelectedRow()<0 || tab_List.getSelectedColumnCount() == 0 ) return ;
        btn_Delete.setEnabled(true);
        btn_Edit.setEnabled(true);
}//GEN-LAST:event_tab_ListMouseClicked

    private void btn_DeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_DeleteActionPerformed
        String sql = "";
        String p_no = tab_List.getValueAt(tab_List.getSelectedRow(), 0).toString();
        String p_name = tab_List.getValueAt(tab_List.getSelectedRow(), 1).toString();
        Object[] options = {"Yes","No"};
        int response = JOptionPane.showOptionDialog(
                        new Frame(),
                        paragraph.getLanguage(message, "WILLITBEDELETE")+p_no+" "+p_name+" ?",
                        paragraph.getLanguage(message, "MESSAGE"),
                        JOptionPane.YES_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]
                    );
        try {
            if(response==0){
                sql = "UPDATE patients_info SET exist = 0 " +
                     "WHERE p_no ='"+ p_no +"'";
                DBC.executeUpdate(sql);
                JOptionPane.showMessageDialog(new Frame(), paragraph.getLanguage(message, "DELETECOMPLETE"));
                this.btn_Delete.setEnabled(false);
            }
        } catch (SQLException ex) {}
        showPatientsList();
}//GEN-LAST:event_btn_DeleteActionPerformed

    private void btn_SearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SearchActionPerformed
        showPatientsList();
}//GEN-LAST:event_btn_SearchActionPerformed

    private void btn_EditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_EditActionPerformed
        showImage(null,"");
        new Frm_PatientMod(this, tab_List.getValueAt(tab_List.getSelectedRow(), 0).toString()).setVisible(true);
        this.setEnabled(false);
        this.setVisible(false);
}//GEN-LAST:event_btn_EditActionPerformed

    private void btn_NextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_NextActionPerformed
        cob_Page.setSelectedIndex(cob_Page.getSelectedIndex()+1);
}//GEN-LAST:event_btn_NextActionPerformed

    private void btn_PreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_PreviousActionPerformed
        cob_Page.setSelectedIndex(cob_Page.getSelectedIndex()-1);
}//GEN-LAST:event_btn_PreviousActionPerformed

    private void btn_CloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CloseActionPerformed
        new Main.Frm_Main().setVisible(true);
        this.dispose();
}//GEN-LAST:event_btn_CloseActionPerformed

    private void cob_PageItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cob_PageItemStateChanged
        if(evt.getStateChange() == java.awt.event.ItemEvent.SELECTED && m_PatientsRS!=null){
            tab_List.setModel(HISModel.getModel(m_PatientsRS, this.cob_Page.getSelectedIndex()*MAX_ROWS_OF_PAGE+1,MAX_ROWS_OF_PAGE));
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

    private void txt_SearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_SearchKeyPressed
        if(evt.getKeyCode()==java.awt.event.KeyEvent.VK_ENTER) showPatientsList();
    }//GEN-LAST:event_txt_SearchKeyPressed

    private void tab_ListKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tab_ListKeyPressed
        tab_ListMouseClicked(null);
    }//GEN-LAST:event_tab_ListKeyPressed

    private void btn_OkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_OkActionPerformed
        sql_FingerSelect = "SELECT fingertemplate.id AS id,fingertemplate.template AS template " +
                "FROM fingertemplate LEFT JOIN patients_info " +
                "ON patients_info.p_no =  fingertemplate.id " +
                "WHERE patients_info.exist = true " ;
        /**判斷各項選擇是否有輸入值*/
        if(!txt_FirstName.getText().trim().equals(""))
            sql_FingerSelect += "AND UPPER(patients_info.firstname) LIKE UPPER('%" + txt_FirstName.getText().replace(" ", "%") + "%') " ;
        if(!txt_LastName.getText().trim().equals(""))
            sql_FingerSelect += "AND UPPER(patients_info.lastname) LIKE UPPER('%" + txt_LastName.getText().replace(" ", "%") + "%') " ;
        if(!txt_Town.getText().trim().equals(""))
            sql_FingerSelect += "AND UPPER(patients_info.town) LIKE UPPER('%" + txt_Town.getText().replace(" ", "%") + "%') " ;
        sql_FingerSelect += "AND patients_info.gender = '" + cob_Gender.getSelectedItem().toString() + "' ";

        if(txt_FirstName.getText().trim().equals("")
                && txt_LastName.getText().trim().equals("")
                && txt_Town.getText().trim().equals("")){
            Dialog_FingerConditions.setAlwaysOnTop(false);
            Object[] options = {"Yes","No"};
            int response = JOptionPane.showOptionDialog( 
                    new Frame(),
                    paragraph.getLanguage(message, "KEYWORDSARETOOFEW")+"\n"+
                    paragraph.getLanguage(message, "AREYOUSUREYOUWANTTOCONTINUE"),
                    paragraph.getLanguage(message, "WARNING"),
                    JOptionPane.YES_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
                    );
            if(response!=0){
                txt_FirstName.setText("--------------------");
                txt_LastName.setText("--------------------");
                txt_Town.setText("--------------------");
                lab_FingerprintSearch.setText("");
            }
        }
        Dialog_FingerConditions.setVisible(false);
}//GEN-LAST:event_btn_OkActionPerformed
 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDialog Dialog_FingerConditions;
    private javax.swing.JButton btn_Add;
    private javax.swing.JButton btn_Close;
    private javax.swing.JButton btn_Delete;
    private javax.swing.JButton btn_Edit;
    private javax.swing.JButton btn_Next;
    private javax.swing.JButton btn_Ok;
    private javax.swing.JButton btn_Previous;
    private javax.swing.JButton btn_Search;
    private javax.swing.JComboBox cob_Conditions;
    private javax.swing.JComboBox cob_Gender;
    private javax.swing.JComboBox cob_Page;
    private cc.johnwu.finger.FingerPrintViewer fingerPrintViewer1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lab_FingerprintSearch;
    private javax.swing.JLabel lab_FirstName1;
    private javax.swing.JLabel lab_Gender1;
    private javax.swing.JLabel lab_ListName;
    private javax.swing.JLabel lab_Town;
    private javax.swing.JPanel pan_Center;
    private javax.swing.JPanel pan_InnerCenter;
    private javax.swing.JPanel pan_InnerUp;
    private javax.swing.JPanel pan_Right;
    private javax.swing.JPanel pan_UnderCenter;
    private javax.swing.JScrollPane spn_PatientsList;
    private javax.swing.JTable tab_List;
    private javax.swing.JTextField txt_FirstName;
    private javax.swing.JTextField txt_LastName;
    private javax.swing.JFormattedTextField txt_Search;
    private javax.swing.JTextField txt_Town;
    // End of variables declaration//GEN-END:variables

    public void reLoad() {
        this.txt_Search.setText("");
        showPatientsList();
        this.setEnabled(true);
        this.setVisible(true);
        this.btn_Edit.setEnabled(false);
        this.btn_Delete.setEnabled(false);
        FingerPrintScanner.setParentFrame(this);//打開指紋機
    }

    public void onPatientMod(String pno) {
    }

    public void onFingerDown() {
        ResultSet rs = null;
        String sql = null ;
        long start = 0;
        long finish = 0;
        try {
            sql_FingerSelect = "SELECT id,template FROM fingertemplate ";
            rs = DBC.executeQuery("SELECT COUNT(*) FROM fingertemplate ");
            if(rs.next() && rs.getInt(1) > MAX_FINGERPRINT_COUNT){
                Point p = this.getLocation();
                int x = p.x+(this.getWidth()-Dialog_FingerConditions.getWidth())/2;
                int y = p.y+(this.getHeight()-Dialog_FingerConditions.getHeight())/2;
                this.Dialog_FingerConditions.setLocation(x, y);
                txt_FirstName.setText("");
                txt_LastName.setText("");
                cob_Gender.setSelectedIndex(0);
                txt_Town.setText("");
                this.Dialog_FingerConditions.setVisible(true);
            }
            DBC.closeConnection(rs);

            start = System.currentTimeMillis();
            lab_FingerprintSearch.setText(paragraph.getLanguage(line, "FINGERPRINTSEARCH"));
            rs = DBC.executeQuery(sql_FingerSelect);
            m_PatientsNO = FingerPrintScanner.identify(rs);
            finish = System.currentTimeMillis();
            lab_FingerprintSearch.setText(paragraph.getLanguage(line, "ITTOOK") +" "+((float)(finish-start)/(float)1000)+"/s");
            if(m_PatientsNO.equals("")){
                lab_FingerprintSearch.setText(paragraph.getLanguage(line, "NOTFOUND"));
            }else{
                if(this.isShowing()) new Frm_Check(this, m_PatientsNO).setVisible(true);
                //new Frm_PatientMod(this, m_PatientsNO).setVisible(true);
            }
        } catch (SQLException ex) {
        } finally {
            try{DBC.closeConnection(rs);}
            catch (SQLException ex) {}
        }
    }

    public void showImage(BufferedImage bufferedimage, String msg) {
        this.fingerPrintViewer1.showImage(bufferedimage);
        this.fingerPrintViewer1.setTitle(msg);
    }

}
