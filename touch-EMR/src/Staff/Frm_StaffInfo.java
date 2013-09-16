package Staff;

import ErrorMessage.StoredErrorMessage;
import cc.johnwu.sql.DBC;
import cc.johnwu.sql.HISModel;
import cc.johnwu.finger.*;
import Multilingual.language;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.sql.*;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;


public class Frm_StaffInfo extends javax.swing.JFrame implements FingerPrintViewerInterface {

    private final int MAX_ROWS_OF_PAGE = 50;
    private ResultSet m_StaffRS;
    private String sql_FingerSelect;
    /*** 判斷醫生與一般員工的方法 ***/
    private int searchCondition = 0;
    private JFormattedTextField txt_Conditions = null;
    private JComboBox cob_Page = null;
    private JTable tab_List = null;
    private JButton btn_Next = null;
    private JButton btn_Previous = null;
    private JButton btn_Delete = null;
    private JButton btn_Edit = null;
    private JButton btn_Add = null;
    private String m_KeepId = "Admin 1";  // 系統保留帳號
    private String m_UUID;
    /*多國語言變數*/
    private language paragraph = new language();
    private String[] line = paragraph.setlanguage("STAFFINFO").split("\n") ;
    private String[] message = paragraph.setlanguage("MESSAGE").split("\n") ;
    /*輸出錯誤資訊變數*/
    StoredErrorMessage ErrorMessage = new StoredErrorMessage() ;

    public Frm_StaffInfo() {
        initComponents();
        init();
        setControl();
        showStaffList();
        initLanguage() ;
    }
    public void init(){
        this.setExtendedState(Frm_StaffInfo.MAXIMIZED_BOTH);  // 最大化
        tab_GeneralList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);//設定tab_GeneralList只能選一row
        tab_DoctorList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //設定tab_DoctorList只能選一row
        setLocationRelativeTo(this);
        FingerPrintScanner.setParentFrame(this);
        addWindowListener(new WindowAdapter() {  // 畫面關閉原視窗enable
            @Override
            public void windowClosing(WindowEvent windowevent) {
                FingerPrintScanner.stop();
                btn_GeneralBackActionPerformed(null);
            }
        });
    }
    private void initLanguage() {
        this.setTitle(paragraph.getLanguage(line, "STAFFINFOMATION"));
        this.btn_GeneralSearch.setText(paragraph.getLanguage(message, "SEARCH"));
        this.btn_GeneralAdd.setText(paragraph.getLanguage(line, "GENERALADD"));
        this.btn_GeneralEdit.setText(paragraph.getLanguage(line, "GENERALEDIT"));
        this.btn_GeneralDelete.setText(paragraph.getLanguage(line, "GENERALDELETE"));
        this.btn_GeneralBack.setText(paragraph.getLanguage(line, "GENERALBACK"));
        this.btn_DoctorSearch.setText(paragraph.getLanguage(message, "SEARCH"));
        this.tabp_StaffInfo.setTitleAt(0,paragraph.getLanguage(line, "GENERAL"));
        this.tabp_StaffInfo.setTitleAt(1,paragraph.getLanguage(line, "DOCTOR"));
        cob_GeneralSearch.setModel(new javax.swing.DefaultComboBoxModel(
                    new String[] { paragraph.getLanguage(line, "ALL"), paragraph.getLanguage(line, "NO"),
                                   paragraph.getLanguage(line, "NAME"), paragraph.getLanguage(line, "BIRTH"),
                                   paragraph.getLanguage(line, "PHONE"), paragraph.getLanguage(line, "ADDRESS") }
                )
        );
        cob_DoctorSearch.setModel(new javax.swing.DefaultComboBoxModel(
                    new String[] { paragraph.getLanguage(line, "ALL"), paragraph.getLanguage(line, "NO"),
                                   paragraph.getLanguage(line, "NAME"), paragraph.getLanguage(line, "BIRTH"),
                                   paragraph.getLanguage(line, "PHONE"), paragraph.getLanguage(line, "ADDRESS") }
                )
        );
    }


    public void showStaffList(){
        ResultSet rs = null;
        String sql = "";
        String conditions = "";

        try {
            switch(searchCondition){
                case 0: // ALL
                conditions = "(UPPER(s_no) LIKE UPPER('%" + txt_Conditions.getText().replace(" ", "%") + "%')" +
                             "OR UPPER(concat(firstname,' ',lastname)) LIKE UPPER('%" + txt_Conditions.getText().replace(" ", "%") + "%') " +
                             "OR UPPER(date_birth) LIKE UPPER('%" + txt_Conditions.getText().replace(" ", "%") + "%') " +
                             "OR UPPER(phone) LIKE UPPER('%" + txt_Conditions.getText().replace(" ", "%") + "%') " +
                             "OR UPPER(cellphone) LIKE UPPER('%" + txt_Conditions.getText().replace(" ", "%") + "%') " +
                             "OR UPPER(address) LIKE UPPER('%" + txt_Conditions.getText().replace(" ", "%") + "%') " +
                             ")";
                    break;
                case 1: // P_NO
                    conditions = "(UPPER(s_no) LIKE UPPER('%" + txt_Conditions.getText().replace(" ", "%") + "%'))";
                    break;
                case 2: // NAME
                    conditions = "(UPPER(concat(firstname,' ',lastname)) LIKE UPPER('%" + txt_Conditions.getText().replace(" ", "%") + "%'))";
                    break;
                case 3: // BIRTH
                    conditions = "(UPPER(date_birth) LIKE UPPER('%" + txt_Conditions.getText().replace(" ", "%") + "%'))";
                    break;
                
                case 4: // PHONE
                    conditions = "(UPPER(phone) LIKE UPPER('%" + txt_Conditions.getText().replace(" ", "%") + "%') " +
                             "OR UPPER(cellphone) LIKE UPPER('%" + txt_Conditions.getText().replace(" ", "%") + "%'))";
                    break;
                case 5: // ADDRESS
                    conditions = "(UPPER(address) LIKE UPPER('%" + txt_Conditions.getText().replace(" ", "%") + "%'))";
                    break;
            }
            sql = "SELECT s_no AS '"+paragraph.getLanguage(line, "NO")+"', " +
                        "concat(firstname,' ',lastname) AS '"+paragraph.getLanguage(line, "NAME")+"', " +
                        "date_birth AS '"+paragraph.getLanguage(line, "BIRTH")+"', " +
                        "phone AS '"+paragraph.getLanguage(line, "PHONE")+"', " +
                        "cellphone AS '"+paragraph.getLanguage(line, "CELLPHONE")+"', " +
                        "address AS '"+paragraph.getLanguage(line, "ADDRESS")+"', " +
                       // "policlinic.name AS '"+paragraph.getLanguage(line, "DEPNAME")+"' " +
                        "department.name AS 'Position' ," +
                        "policlinic.name AS 'Dep.' " +
                  "FROM staff_info " +
                  "LEFT JOIN department ON department.guid=staff_info.dep_guid LEFT JOIN policlinic ON policlinic.guid=staff_info.poli_guid " +
                  "WHERE exist = 1 " +
                  "AND " + conditions;
            if (this.tabp_StaffInfo.getSelectedIndex()==0) {
                sql = sql + " AND (department.name <> 'Doctor' OR staff_info.dep_guid IS NULL) ";
            } else {
                sql = sql + " AND department.name = 'Doctor' ";
            }
            System.out.println(sql);
            m_StaffRS = DBC.executeQuery(sql);
            if(m_StaffRS.last()){

                cob_Page.removeAllItems();
                int page = (m_StaffRS.getRow()/MAX_ROWS_OF_PAGE)+(m_StaffRS.getRow()%MAX_ROWS_OF_PAGE==0?0:1);
                for(int i=0; i<page;i++){
                    cob_Page.addItem(+ (i+1) + " of " + page);
                }
                m_StaffRS.beforeFirst();
                tab_List.setModel(HISModel.getModel(m_StaffRS, 1, MAX_ROWS_OF_PAGE));
                if(cob_Page.getItemCount()>1){
                    btn_Next.setEnabled(true);
                }else{
                    btn_Next.setEnabled(false);
                }
                btn_Previous.setEnabled(false);
                tab_List.setEnabled(true);
            }else{
                tab_List.setModel(getModle(new String[]{"Message"},new String[][]{{"No Information."}}));
                cob_Page.removeAllItems();
                btn_Next.setEnabled(false);
                tab_List.setEnabled(false);
            }
            System.out.println(sql);
        } catch (SQLException e) {
            ErrorMessage.setData("Staff", "Frm_StaffInfo" ,"showStaffList()",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            System.out.println(e);
        } finally {
            try{DBC.closeConnection(rs);}
            catch (SQLException e) {
            }
        }
    }
    public void setControl(){
        if(this.tabp_StaffInfo.getSelectedIndex()==0){
//            1txt_Conditions = txt_Conditions.getText().replace(" ", "%");
            txt_Conditions = this.txt_GeneralSearch;
            searchCondition = cob_GeneralSearch.getSelectedIndex();
            cob_Page = this.cob_GeneralPage;
            tab_List = this.tab_GeneralList;
            btn_Next = this.btn_GeneralNext;
            btn_Previous = this.btn_GeneralPrevious;
            btn_Delete = this.btn_GeneralDelete;
            btn_Edit = this.btn_GeneralEdit;
            btn_Add = this.btn_GeneralAdd;
        }else{
            txt_Conditions = this.txt_DoctorSearch;
            searchCondition = cob_DoctorSearch.getSelectedIndex();
            cob_Page = this.cob_DoctorPage;
            tab_List = this.tab_DoctorList;
            btn_Next = this.btn_DoctorNext;
            btn_Previous = this.btn_DoctorPrevious;

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

        tabp_StaffInfo = new javax.swing.JTabbedPane();
        pan_General = new javax.swing.JPanel();
        spn_PatientsList = new javax.swing.JScrollPane();
        tab_GeneralList = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        btn_GeneralPrevious = new javax.swing.JButton();
        cob_GeneralPage = new javax.swing.JComboBox();
        btn_GeneralNext = new javax.swing.JButton();
        btn_GeneralSearch = new javax.swing.JButton();
        txt_GeneralSearch = new javax.swing.JFormattedTextField();
        cob_GeneralSearch = new javax.swing.JComboBox();
        pan_Doctor = new javax.swing.JPanel();
        spn_PatientsList1 = new javax.swing.JScrollPane();
        tab_DoctorList = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        btn_DoctorPrevious = new javax.swing.JButton();
        cob_DoctorPage = new javax.swing.JComboBox();
        btn_DoctorNext = new javax.swing.JButton();
        btn_DoctorSearch = new javax.swing.JButton();
        txt_DoctorSearch = new javax.swing.JFormattedTextField();
        cob_DoctorSearch = new javax.swing.JComboBox();
        jPanel8 = new javax.swing.JPanel();
        btn_GeneralBack = new javax.swing.JButton();
        btn_GeneralAdd = new javax.swing.JButton();
        btn_GeneralEdit = new javax.swing.JButton();
        btn_GeneralDelete = new javax.swing.JButton();
        fingerPrintViewer1 = new cc.johnwu.finger.FingerPrintViewer();
        lab_FingerprintSearch = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Staff Infomation");

        tabp_StaffInfo.setBackground(new java.awt.Color(237, 236, 235));
        tabp_StaffInfo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabp_StaffInfoMouseClicked(evt);
            }
        });

        tab_GeneralList.setAutoCreateRowSorter(true);
        tab_GeneralList.setModel(new javax.swing.table.DefaultTableModel(
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
        tab_GeneralList.setRowHeight(25);
        tab_GeneralList.getTableHeader().setReorderingAllowed(false);
        tab_GeneralList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tab_GeneralListMouseClicked(evt);
            }
        });
        tab_GeneralList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tab_GeneralListKeyPressed(evt);
            }
        });
        spn_PatientsList.setViewportView(tab_GeneralList);

        jPanel3.setLayout(new java.awt.GridBagLayout());

        btn_GeneralPrevious.setText("    <<    ");
        btn_GeneralPrevious.setEnabled(false);
        btn_GeneralPrevious.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_GeneralPreviousActionPerformed(evt);
            }
        });
        jPanel3.add(btn_GeneralPrevious, new java.awt.GridBagConstraints());

        cob_GeneralPage.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cob_GeneralPageItemStateChanged(evt);
            }
        });
        jPanel3.add(cob_GeneralPage, new java.awt.GridBagConstraints());

        btn_GeneralNext.setText("    >>    ");
        btn_GeneralNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_GeneralNextActionPerformed(evt);
            }
        });
        jPanel3.add(btn_GeneralNext, new java.awt.GridBagConstraints());

        btn_GeneralSearch.setText("Search");
        btn_GeneralSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_GeneralSearchActionPerformed(evt);
            }
        });

        txt_GeneralSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_GeneralSearchKeyPressed(evt);
            }
        });

        cob_GeneralSearch.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "ALL", "No.", "Name", "Birth", "Phone", "Address" }));
        cob_GeneralSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cob_GeneralSearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pan_GeneralLayout = new javax.swing.GroupLayout(pan_General);
        pan_General.setLayout(pan_GeneralLayout);
        pan_GeneralLayout.setHorizontalGroup(
            pan_GeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_GeneralLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan_GeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 631, Short.MAX_VALUE)
                    .addComponent(spn_PatientsList, javax.swing.GroupLayout.DEFAULT_SIZE, 631, Short.MAX_VALUE)
                    .addGroup(pan_GeneralLayout.createSequentialGroup()
                        .addComponent(cob_GeneralSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_GeneralSearch, javax.swing.GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_GeneralSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        pan_GeneralLayout.setVerticalGroup(
            pan_GeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_GeneralLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan_GeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan_GeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btn_GeneralSearch)
                        .addComponent(txt_GeneralSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(cob_GeneralSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spn_PatientsList, javax.swing.GroupLayout.DEFAULT_SIZE, 463, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        tabp_StaffInfo.addTab("Staff", pan_General);

        tab_DoctorList.setAutoCreateRowSorter(true);
        tab_DoctorList.setModel(new javax.swing.table.DefaultTableModel(
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
        tab_DoctorList.setRowHeight(25);
        tab_DoctorList.getTableHeader().setReorderingAllowed(false);
        tab_DoctorList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tab_DoctorListMouseClicked(evt);
            }
        });
        tab_DoctorList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tab_DoctorListKeyPressed(evt);
            }
        });
        spn_PatientsList1.setViewportView(tab_DoctorList);

        jPanel5.setLayout(new java.awt.GridBagLayout());

        btn_DoctorPrevious.setText("    <<    ");
        btn_DoctorPrevious.setEnabled(false);
        btn_DoctorPrevious.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_DoctorPreviousActionPerformed(evt);
            }
        });
        jPanel5.add(btn_DoctorPrevious, new java.awt.GridBagConstraints());

        cob_DoctorPage.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cob_DoctorPageItemStateChanged(evt);
            }
        });
        jPanel5.add(cob_DoctorPage, new java.awt.GridBagConstraints());

        btn_DoctorNext.setText("    >>    ");
        btn_DoctorNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_DoctorNextActionPerformed(evt);
            }
        });
        jPanel5.add(btn_DoctorNext, new java.awt.GridBagConstraints());

        btn_DoctorSearch.setText("Search");
        btn_DoctorSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_DoctorSearchActionPerformed(evt);
            }
        });

        txt_DoctorSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_DoctorSearchKeyPressed(evt);
            }
        });

        cob_DoctorSearch.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "ALL", "No.", "Name", "Birth", "Phone", "Address" }));
        cob_DoctorSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cob_DoctorSearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pan_DoctorLayout = new javax.swing.GroupLayout(pan_Doctor);
        pan_Doctor.setLayout(pan_DoctorLayout);
        pan_DoctorLayout.setHorizontalGroup(
            pan_DoctorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_DoctorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan_DoctorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(spn_PatientsList1, javax.swing.GroupLayout.DEFAULT_SIZE, 631, Short.MAX_VALUE)
                    .addGroup(pan_DoctorLayout.createSequentialGroup()
                        .addComponent(cob_DoctorSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_DoctorSearch, javax.swing.GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE)
                        .addGap(6, 6, 6)
                        .addComponent(btn_DoctorSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 631, Short.MAX_VALUE))
                .addContainerGap())
        );
        pan_DoctorLayout.setVerticalGroup(
            pan_DoctorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_DoctorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan_DoctorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_DoctorSearch)
                    .addComponent(txt_DoctorSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cob_DoctorSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spn_PatientsList1, javax.swing.GroupLayout.DEFAULT_SIZE, 463, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        tabp_StaffInfo.addTab("Doctor", pan_Doctor);

        btn_GeneralBack.setText("Close");
        btn_GeneralBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_GeneralBackActionPerformed(evt);
            }
        });

        btn_GeneralAdd.setText("Add");
        btn_GeneralAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_GeneralAddActionPerformed(evt);
            }
        });

        btn_GeneralEdit.setText("Details");
        btn_GeneralEdit.setEnabled(false);
        btn_GeneralEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_GeneralEditActionPerformed(evt);
            }
        });

        btn_GeneralDelete.setText("Delete");
        btn_GeneralDelete.setEnabled(false);
        btn_GeneralDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_GeneralDeleteActionPerformed(evt);
            }
        });

        fingerPrintViewer1.setVisible(true);

        javax.swing.GroupLayout fingerPrintViewer1Layout = new javax.swing.GroupLayout(fingerPrintViewer1.getContentPane());
        fingerPrintViewer1.getContentPane().setLayout(fingerPrintViewer1Layout);
        fingerPrintViewer1Layout.setHorizontalGroup(
            fingerPrintViewer1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 78, Short.MAX_VALUE)
        );
        fingerPrintViewer1Layout.setVerticalGroup(
            fingerPrintViewer1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 111, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lab_FingerprintSearch, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btn_GeneralBack, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_GeneralEdit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_GeneralAdd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_GeneralDelete, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                        .addComponent(fingerPrintViewer1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(btn_GeneralAdd)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_GeneralEdit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_GeneralDelete)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_GeneralBack)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fingerPrintViewer1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lab_FingerprintSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(257, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabp_StaffInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 656, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tabp_StaffInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_DoctorNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_DoctorNextActionPerformed
        cob_Page.setSelectedIndex(cob_Page.getSelectedIndex()+1);
}//GEN-LAST:event_btn_DoctorNextActionPerformed

    private void cob_DoctorPageItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cob_DoctorPageItemStateChanged
        if(evt.getStateChange() == java.awt.event.ItemEvent.SELECTED && m_StaffRS!=null){
            tab_List.setModel(HISModel.getModel(m_StaffRS, this.cob_Page.getSelectedIndex()*MAX_ROWS_OF_PAGE+1,MAX_ROWS_OF_PAGE));
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
}//GEN-LAST:event_cob_DoctorPageItemStateChanged

    private void btn_DoctorPreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_DoctorPreviousActionPerformed
        cob_Page.setSelectedIndex(cob_Page.getSelectedIndex()-1);
}//GEN-LAST:event_btn_DoctorPreviousActionPerformed

    private void btn_DoctorSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_DoctorSearchActionPerformed
        showStaffList();
}//GEN-LAST:event_btn_DoctorSearchActionPerformed

    private void cob_DoctorSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cob_DoctorSearchActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_cob_DoctorSearchActionPerformed

    private void txt_DoctorSearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_DoctorSearchKeyPressed
        if(evt.getKeyCode()==java.awt.event.KeyEvent.VK_ENTER) showStaffList();
}//GEN-LAST:event_txt_DoctorSearchKeyPressed

    private void btn_GeneralNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_GeneralNextActionPerformed
        cob_Page.setSelectedIndex(cob_Page.getSelectedIndex()+1);
}//GEN-LAST:event_btn_GeneralNextActionPerformed

    private void cob_GeneralPageItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cob_GeneralPageItemStateChanged
        if(evt.getStateChange() == java.awt.event.ItemEvent.SELECTED && m_StaffRS!=null){
            tab_List.setModel(HISModel.getModel(m_StaffRS, this.cob_Page.getSelectedIndex()*MAX_ROWS_OF_PAGE+1,MAX_ROWS_OF_PAGE));
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
}//GEN-LAST:event_cob_GeneralPageItemStateChanged

    private void btn_GeneralPreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_GeneralPreviousActionPerformed
        cob_Page.setSelectedIndex(cob_Page.getSelectedIndex()-1);
}//GEN-LAST:event_btn_GeneralPreviousActionPerformed

    private void btn_GeneralDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_GeneralDeleteActionPerformed
        String sql = "";
        int s_no = -1;
        String s_name = null;
        if (tab_GeneralList.getSelectedRow() != -1) {
            s_no = Integer.parseInt(tab_GeneralList.getValueAt(tab_GeneralList.getSelectedRow(), 0).toString());
            s_name = tab_GeneralList.getValueAt(tab_GeneralList.getSelectedRow(), 1).toString();
        } else if (tab_DoctorList.getSelectedRow() != -1) {
            s_no = Integer.parseInt(tab_DoctorList.getValueAt(tab_DoctorList.getSelectedRow(), 0).toString());
            s_name = tab_DoctorList.getValueAt(tab_DoctorList.getSelectedRow(), 1).toString();
        }
        Object[] options = {"YES","NO"};
        int response = JOptionPane.showOptionDialog(
                new Frame(),
                 paragraph.getLanguage(line, "WILLITBEDELETENO") +s_no+" Name: "+s_name+"?",
                "MESSAGE",
                JOptionPane.YES_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
                );


        try {
            if(response==0){
                sql = "UPDATE staff_info SET exist = 0 " +
                        "WHERE s_no ='"+ s_no +"'";
                DBC.executeUpdate(sql);
                JOptionPane.showMessageDialog(new Frame(), paragraph.getLanguage(message , "DELETECOMPLETE"));
                this.btn_GeneralDelete.setEnabled(false);
            }
        } catch (SQLException e) {
            ErrorMessage.setData("Staff", "Frm_StaffInfo" ,"btn_GeneralDeleteActionPerformed()",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        }
        showStaffList();
        this.btn_GeneralEdit.setEnabled(false);
        this.btn_GeneralDelete.setEnabled(false);
}//GEN-LAST:event_btn_GeneralDeleteActionPerformed

    private void btn_GeneralEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_GeneralEditActionPerformed
        FingerPrintScanner.stop();
        int getSNo = 0;
        if (tabp_StaffInfo.getSelectedIndex() == 0 && tab_GeneralList.getSelectedRow() != -1) {
            getSNo = Integer.parseInt(tab_GeneralList.getValueAt(tab_GeneralList.getSelectedRow(), 0).toString());
        } else if (tabp_StaffInfo.getSelectedIndex() == 1 && tab_DoctorList.getSelectedRow() != -1) {
            getSNo = Integer.parseInt(tab_DoctorList.getValueAt(tab_DoctorList.getSelectedRow(), 0).toString());
        }

        
        new Frm_StaffDetails(getSNo).setVisible(true);
        this.dispose();

        
}//GEN-LAST:event_btn_GeneralEditActionPerformed

    private void btn_GeneralAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_GeneralAddActionPerformed
        FingerPrintScanner.stop();
        new Frm_StaffDetails().setVisible(true);
        this.setVisible(false);
}//GEN-LAST:event_btn_GeneralAddActionPerformed

    private void btn_GeneralBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_GeneralBackActionPerformed
        FingerPrintScanner.stop();
        new Main.Frm_Main().setVisible(true);
        this.dispose();
}//GEN-LAST:event_btn_GeneralBackActionPerformed

    private void btn_GeneralSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_GeneralSearchActionPerformed
        showStaffList();
}//GEN-LAST:event_btn_GeneralSearchActionPerformed

    private void cob_GeneralSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cob_GeneralSearchActionPerformed

}//GEN-LAST:event_cob_GeneralSearchActionPerformed

    private void txt_GeneralSearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_GeneralSearchKeyPressed
        if(evt.getKeyCode()==java.awt.event.KeyEvent.VK_ENTER) showStaffList();
}//GEN-LAST:event_txt_GeneralSearchKeyPressed

    private void tabp_StaffInfoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabp_StaffInfoMouseClicked
        setControl();
        showStaffList();
        btn_Edit.setEnabled(false);
        btn_Delete.setEnabled(false);
    }//GEN-LAST:event_tabp_StaffInfoMouseClicked

    private void tab_GeneralListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab_GeneralListMouseClicked
        if(tab_List.getSelectedRow()<0 || tab_List.getSelectedColumnCount() == 0 || tab_List.getValueAt(tab_List.getSelectedRow(), 1).equals(m_KeepId)) {
            btn_Delete.setEnabled(false);
            btn_Edit.setEnabled(false);
        } else {
            btn_Delete.setEnabled(true);
            btn_Edit.setEnabled(true);
        }
        
    }//GEN-LAST:event_tab_GeneralListMouseClicked

    private void tab_GeneralListKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tab_GeneralListKeyPressed
        tab_GeneralListMouseClicked(null);
    }//GEN-LAST:event_tab_GeneralListKeyPressed

    private void tab_DoctorListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab_DoctorListMouseClicked
        if(tab_List.getSelectedRow()<0 || tab_List.getSelectedColumnCount() == 0 || tab_List.getValueAt(tab_List.getSelectedRow(), 1).equals(m_KeepId)){
            btn_Delete.setEnabled(false);
            btn_Edit.setEnabled(false);
        }  else {
            btn_Delete.setEnabled(true);
            btn_Edit.setEnabled(true);
        }
    }//GEN-LAST:event_tab_DoctorListMouseClicked

    private void tab_DoctorListKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tab_DoctorListKeyPressed
        tab_DoctorListMouseClicked(null);
    }//GEN-LAST:event_tab_DoctorListKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_DoctorNext;
    private javax.swing.JButton btn_DoctorPrevious;
    private javax.swing.JButton btn_DoctorSearch;
    private javax.swing.JButton btn_GeneralAdd;
    private javax.swing.JButton btn_GeneralBack;
    private javax.swing.JButton btn_GeneralDelete;
    private javax.swing.JButton btn_GeneralEdit;
    private javax.swing.JButton btn_GeneralNext;
    private javax.swing.JButton btn_GeneralPrevious;
    private javax.swing.JButton btn_GeneralSearch;
    private javax.swing.JComboBox cob_DoctorPage;
    private javax.swing.JComboBox cob_DoctorSearch;
    private javax.swing.JComboBox cob_GeneralPage;
    private javax.swing.JComboBox cob_GeneralSearch;
    private cc.johnwu.finger.FingerPrintViewer fingerPrintViewer1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JLabel lab_FingerprintSearch;
    private javax.swing.JPanel pan_Doctor;
    private javax.swing.JPanel pan_General;
    private javax.swing.JScrollPane spn_PatientsList;
    private javax.swing.JScrollPane spn_PatientsList1;
    private javax.swing.JTable tab_DoctorList;
    private javax.swing.JTable tab_GeneralList;
    private javax.swing.JTabbedPane tabp_StaffInfo;
    private javax.swing.JFormattedTextField txt_DoctorSearch;
    private javax.swing.JFormattedTextField txt_GeneralSearch;
    // End of variables declaration//GEN-END:variables

    public void onFingerDown() {
        ResultSet rs = null;
        String sql = null ;
        long start = 0;
        long finish = 0;
        try {
            sql_FingerSelect = "SELECT id,template FROM staff_fingertemplate ";

            DBC.closeConnection(rs);
            start = System.currentTimeMillis();
            lab_FingerprintSearch.setText(paragraph.getLanguage(line, "SEARCH"));
            rs = DBC.executeQuery(sql_FingerSelect);
            String PatientsNO = FingerPrintScanner.identify(rs);

            finish = System.currentTimeMillis();
            lab_FingerprintSearch.setText(paragraph.getLanguage(line, "ITTOOK")+((float)(finish-start)/(float)1000)+"/s");

            if(PatientsNO.equals("")){
                lab_FingerprintSearch.setText(paragraph.getLanguage(line, "NOTFOUND"));
                JOptionPane.showMessageDialog(new Frame(), paragraph.getLanguage(line, "NOINFORMATION"));
            }else{
                FingerPrintScanner.stop();
                new Frm_StaffDetails(rs.getInt("id")).setVisible(true);
                dispose();
                FingerPrintScanner.setParentFrame(null);
            }

        } catch (SQLException e) {
                ErrorMessage.setData("Registration", "Frm_Registration" ,"onFingerDown()",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        } finally {
            try{DBC.closeConnection(rs);}
            catch (SQLException e) {
                ErrorMessage.setData("Registration", "Frm_Registration" ,"onFingerDown() - DBC.closeConnection",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            }
        }
    }

    public void showImage(BufferedImage bufferedimage, String msg) {
        this.fingerPrintViewer1.showImage(bufferedimage);
        this.fingerPrintViewer1.setTitle(msg);
    }

}
