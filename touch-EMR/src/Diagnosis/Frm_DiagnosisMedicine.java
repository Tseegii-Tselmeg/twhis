package Diagnosis;

import Diagnosis.TableTriStateCell.TriStateCellEditor;
import Diagnosis.TableTriStateCell.TriStateCellRenderer;
import cc.johnwu.sql.*;
import Multilingual.language;
import java.awt.event.ItemEvent;
import java.sql.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import AutoComplete.CompleterComboBox;
import ErrorMessage.StoredErrorMessage;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class Frm_DiagnosisMedicine extends javax.swing.JFrame {
    private CompleterComboBox m_Cobww;
    private Map<Object, Object> m_ChooseHashMap = new HashMap<Object, Object>();
    private DefaultTableModel m_MedicineModel;
    private Frm_DiagnosisInfo m_DiagnosisInfo;
    private Frm_DiagnosisAllergy m_DiagnosisAllergy;
    private String m_Allergy;
    /*多國語言變數*/
    private language paragraph = new language();
    private String[] line = paragraph.setlanguage("DIAGNOSISMEDICINE").split("\n") ;
    private String[] message = paragraph.setlanguage("MESSAGE").split("\n") ;
    /*輸出錯誤資訊變數*/
    StoredErrorMessage ErrorMessage = new StoredErrorMessage() ;

    public Frm_DiagnosisMedicine(Frm_DiagnosisAllergy diagnosisAllergy, String Allergy){
        this.m_DiagnosisAllergy = diagnosisAllergy;
        this.m_Allergy = Allergy;
        initComponents();
        initTab_Medicine();
        initLanguage();
    }
 
    public Frm_DiagnosisMedicine(Frm_DiagnosisInfo diagnosisInfo, Boolean isDM) throws IOException {
        this.m_Allergy = "Frm_DiagnosisInfo";
        this.m_DiagnosisInfo = diagnosisInfo;
        initComponents();
        initTab_Medicine();
        initLanguage();
        addWindowListener(new WindowAdapter() {  // 畫面關閉原視窗enable
            @Override
            public void windowClosing(WindowEvent windowevent) {
                btn_CloseActionPerformed(null);
            }
        });

        if (isDM) showExplain();

    }

    private void initTab_Medicine(){
        this.tab_Medicine.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  // tabble不可按住多選
        addWindowListener(new WindowAdapter() {  // 畫面關閉原視窗enable
            @Override
            public void windowClosing(WindowEvent windowevent) {
                btn_CloseActionPerformed(null);
            }
        });
        this.setLocationRelativeTo(this);
        String[] medicineCob = null ;
        ResultSet rs = null;
        try {
            String sql = "SELECT code, item, injection, unit_dosage, unit " +
                         "FROM medicines ORDER BY sort";
            rs = DBC.localExecuteQuery(sql);
            rs.last();
            medicineCob = new String[rs.getRow()+1];
            rs.beforeFirst();
            int i = 0;
            medicineCob[i++] = "";
            while (rs.next()) medicineCob[i++] = rs.getString("code").trim() + "    " + rs.getString("item").trim();

        } catch (SQLException e) {
            Logger.getLogger(Frm_DiagnosisTherapy.class.getName()).log(Level.SEVERE, null, e);
                ErrorMessage.setData("Diagnosis", "Frm_DiagnosisMedicine" ,"initTab_Medicine()",
                        e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        } finally {
            try {DBC.closeConnection(rs);}
            catch (SQLException e){
                ErrorMessage.setData("Diagnosis", "Frm_DiagnosisMedicine" ,"initTab_Medicine() - DBC.closeConnection",
                        e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            }
        }
        new CompleterComboBox(medicineCob);
        m_Cobww = new CompleterComboBox(medicineCob);
        m_Cobww.setBounds(80, 15, 580, 20);
        m_Cobww.addItemListener(new java.awt.event.ItemListener() {
        public void itemStateChanged(java.awt.event.ItemEvent evt) {
                    comboBoxItemStateChanged(evt);
           }
        });
        tab_Medicine.setRowHeight(30);
        pan_Center.add(m_Cobww);
        m_Cobww.setSelectedIndex(0);
        setModel("code LIKE '%'","");
    }

    // 多語系對應
    private void initLanguage() {
        lab_Medicine.setText(paragraph.getLanguage(line, "THERAPY"));
        btn_Search.setText(paragraph.getLanguage(message, "SEARCH"));
        btn_Enter.setText(paragraph.getLanguage(message, "ENTER"));
        btn_Close.setText(paragraph.getLanguage(message, "CLOSE"));
        menu_File.setText(paragraph.getLanguage(message, "FILE"));
        setTitle(paragraph.getLanguage(line, "TITLEMEDICINE"));
        mnit_Back.setText(paragraph.getLanguage(line, "BACK"));
    }

    // 顯示糖尿病提示資訊
    private void showExplain() throws IOException {
        Frm_Explain Frm_Explain = new Frm_Explain();
        Frm_Explain.setVisible(true);
        Frm_Explain.setAlwaysOnTop(true);
    }

    //condition 搜尋方式與條件  state KeyPress搜尋或是value change
    public void setModel(String condition, String state) {  
        Object[][] dataArray = null;
        ResultSet rsTabMedicine = null;
        try {
             Object[] title = {"","Medicine Code", "Item", "Information","Unit Dosage","Unit"};
             String sql = "SELECT * FROM medicines WHERE "+condition+"  ORDER BY sort";
             String[] medicineArray = new String[m_ChooseHashMap.size()];
             int x = 0;
             Collection collection = m_ChooseHashMap.values();
             Iterator iterator = collection.iterator();
             while (iterator.hasNext()) {             // 將點選過code設成資料庫搜尋不到的條件
                 medicineArray[x] =  (String) iterator.next();
                 sql += " AND item <> '"+medicineArray[x]+"'";
                 x++;
             }
             rsTabMedicine = DBC.localExecuteQuery(sql);
             rsTabMedicine.last();
             dataArray = new Object[rsTabMedicine.getRow()][6];
             rsTabMedicine.beforeFirst();
             int i = 0;
             if (!state.equals("ENTER")) {
                while (rsTabMedicine.next()) {
                    dataArray[i][1] = rsTabMedicine.getString("code");
                    dataArray[i][2] = rsTabMedicine.getString("item");
                    dataArray[i][3] = rsTabMedicine.getString("injection");
                    dataArray[i][4] = rsTabMedicine.getString("unit_dosage");
                    dataArray[i][5] = rsTabMedicine.getString("unit");
                    if (rsTabMedicine.getString("effective").equals("true")) {
                        dataArray[i][0] = false;
                    } else {
                        dataArray[i][0] = null;
                    }
                    i++;
                    
                }
             } else {
                while (rsTabMedicine.next()) {
                    String search = m_Cobww.getSelectedItem().toString().trim();

                    dataArray[i][1] = rsTabMedicine.getString("code");
                    dataArray[i][2] = "<html>"+(rsTabMedicine.getString("item").replace(search.toUpperCase() , "<font color='FF0000'>"+
                            search.toUpperCase()+"</font>")).replace(search.toLowerCase() , "<font color='FF0000'>"+search.toLowerCase()+"</font>")+"</html>";
                    dataArray[i][3] = rsTabMedicine.getString("injection");
                    dataArray[i][4] = rsTabMedicine.getString("unit_dosage");
                    dataArray[i][5] = rsTabMedicine.getString("unit");
                    if (rsTabMedicine.getString("effective").equals("true")) dataArray[i][0] = false;
                    else dataArray[i][0] = null;
                    
                    i++;
                }
             }
             m_MedicineModel = new DefaultTableModel(dataArray,title) {
                @Override
                 public boolean isCellEditable(int rowIndex,int columnIndex) {
                     if (columnIndex == 0) return true;
                     else return false;
                 }
             };
              this.tab_Medicine.setModel(m_MedicineModel);
              this.tab_Medicine.getColumnModel().getColumn(1).setPreferredWidth(70);
              this.tab_Medicine.getColumnModel().getColumn(2).setPreferredWidth(250);
              this.tab_Medicine.getColumnModel().getColumn(3).setPreferredWidth(600);
//              TableColumn columnUnitDosage = this.tab_Medicine.getColumnModel().getColumn(4);
//              TableColumn columnUnit = this.tab_Medicine.getColumnModel().getColumn(5);
              TableColumn columnEffective = this.tab_Medicine.getColumnModel().getColumn(0);
              
              columnEffective.setMaxWidth(50);
              columnEffective.setCellRenderer(new TriStateCellRenderer());
              columnEffective.setCellEditor(new TriStateCellEditor());
              tab_Medicine.setRowHeight(30);
              Common.TabTools.setHideColumn(tab_Medicine,4); // Medicine hide
              Common.TabTools.setHideColumn(tab_Medicine,5); // Medicine hide
        } catch (SQLException e) {
            Logger.getLogger(Frm_DiagnosisTherapy.class.getName()).log(Level.SEVERE, null, e);
                ErrorMessage.setData("Diagnosis", "Frm_DiagnosisMedicine" ,"setModel(String condition, String state)",
                        e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        } finally {
            try {DBC.closeConnection(rsTabMedicine);}
            catch (SQLException e){
                ErrorMessage.setData("Diagnosis", "Frm_DiagnosisMedicine" ,"setModel(String condition, String state) - DBC.closeConnection",
                        e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            }
        }
    }

    public void setHashMap() {
        String code = tab_Medicine.getValueAt(tab_Medicine.getSelectedRow(), 1).toString().trim();
        String item = tab_Medicine.getValueAt(tab_Medicine.getSelectedRow(), 2).toString().
                                             replace("<html>", "").replace("<font color='FF0000'>", "").
                                             replace("</font>", "").replace("</html>", "").trim();
        String injection = (String)tab_Medicine.getValueAt(tab_Medicine.getSelectedRow(), 3).toString();

        Object unitDosage = (String) tab_Medicine.getValueAt(tab_Medicine.getSelectedRow(), 4);
        
        String unit = (String) tab_Medicine.getValueAt(tab_Medicine.getSelectedRow(), 5);
        if (unit == null)unit = "";

        if (tab_Medicine.getValueAt(this.tab_Medicine.getSelectedRow(), 0).equals(true) ) {
            if (!m_Allergy.equals("Allergy"))  {
                m_ChooseHashMap.put(code,code+"    "+item+"    "+injection+"    "+unitDosage+"    "+unit+"    "+"N"+"    "+"N");
            } else {
                m_ChooseHashMap.put(code,code+"    "+item+"    "+paragraph.getLanguage(line, "GENERAL"));
            }
        } else if (tab_Medicine.getValueAt(this.tab_Medicine.getSelectedRow(), 0).equals(false) ){
            m_ChooseHashMap.remove(code);
        }
    }

    // checkbox 狀態改變
    public void setCheckChange() {
        if(tab_Medicine.getValueAt(this.tab_Medicine.getSelectedRow(), 0).equals(false)) {
            tab_Medicine.setValueAt(true,this.tab_Medicine.getSelectedRow(), 0);
        } else if(tab_Medicine.getValueAt(this.tab_Medicine.getSelectedRow(), 0).equals(true)) {
            tab_Medicine.setValueAt(false,this.tab_Medicine.getSelectedRow(), 0);
        }
        setHashMap();
    }

    // 藥品列表排序
    private static String getSortCode(String strCode) {
        String code = "";
        String arr[] = strCode.split("\\.");
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].length() != 2) code += '0' + arr[i];
            else code += arr[i];
        }
        while (code.length() < 10)  code += '0';

        return code;
    }

    private void comboBoxItemStateChanged(ItemEvent evt) {
        if(evt.getStateChange() == ItemEvent.SELECTED ){
             String []condition = m_Cobww.getSelectedItem().toString().trim().split("    ");  //get select table condition from m_Cobww
             if (condition.length > 1 || condition[0].trim().equals("") ) {
                setModel("code LIKE '"+condition[0]+"%'", "");
             } else {
                setModel("LOWER(item) LIKE LOWER('%"+m_Cobww.getSelectedItem().toString().trim()+"%') ", "ENTER");
             }
         }
    }
 
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pan_Center = new javax.swing.JPanel();
        lab_Medicine = new javax.swing.JLabel();
        span_Medicine = new javax.swing.JScrollPane();
        tab_Medicine = new javax.swing.JTable();
        btn_Search = new javax.swing.JButton();
        btn_Close = new javax.swing.JButton();
        lab_Message = new javax.swing.JLabel();
        txt_Message = new javax.swing.JTextField();
        btn_Enter = new javax.swing.JButton();
        mnb = new javax.swing.JMenuBar();
        menu_File = new javax.swing.JMenu();
        mnit_Back = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Medicine");
        setAlwaysOnTop(true);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        lab_Medicine.setText("Medicine");

        tab_Medicine.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
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
        tab_Medicine.setRowHeight(25);
        tab_Medicine.getTableHeader().setReorderingAllowed(false);
        tab_Medicine.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tab_MedicineMouseClicked(evt);
            }
        });
        tab_Medicine.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tab_MedicineKeyReleased(evt);
            }
        });
        span_Medicine.setViewportView(tab_Medicine);

        btn_Search.setText("Search");
        btn_Search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_SearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pan_CenterLayout = new javax.swing.GroupLayout(pan_Center);
        pan_Center.setLayout(pan_CenterLayout);
        pan_CenterLayout.setHorizontalGroup(
            pan_CenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_CenterLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan_CenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan_CenterLayout.createSequentialGroup()
                        .addComponent(lab_Medicine)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 625, Short.MAX_VALUE)
                        .addComponent(btn_Search, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(span_Medicine, javax.swing.GroupLayout.DEFAULT_SIZE, 768, Short.MAX_VALUE))
                .addContainerGap())
        );
        pan_CenterLayout.setVerticalGroup(
            pan_CenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_CenterLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan_CenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_Search)
                    .addComponent(lab_Medicine))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(span_Medicine, javax.swing.GroupLayout.DEFAULT_SIZE, 483, Short.MAX_VALUE)
                .addContainerGap())
        );

        btn_Close.setText("Close");
        btn_Close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CloseActionPerformed(evt);
            }
        });

        lab_Message.setText("--");

        txt_Message.setEditable(false);
        txt_Message.setText("Version : WHO Model List of Essential Medicines");

        btn_Enter.setText("Enter");
        btn_Enter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_EnterActionPerformed(evt);
            }
        });

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
                    .addComponent(pan_Center, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(txt_Message, javax.swing.GroupLayout.DEFAULT_SIZE, 576, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_Enter, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_Close, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(395, 395, 395)
                    .addComponent(lab_Message)
                    .addContainerGap(405, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pan_Center, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_Close)
                    .addComponent(txt_Message, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_Enter))
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(291, 291, 291)
                    .addComponent(lab_Message)
                    .addContainerGap(275, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tab_MedicineMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab_MedicineMouseClicked
        if (tab_Medicine.getValueAt(tab_Medicine.getSelectedRow(), 0) != null
         && tab_Medicine.getSelectedColumn() == 0) {
            setHashMap();
        }
        if (evt.getClickCount() == 2 && tab_Medicine.getValueAt(tab_Medicine.getSelectedRow(), 0) != null) {
             setCheckChange();
        }
}//GEN-LAST:event_tab_MedicineMouseClicked

    private void btn_CloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CloseActionPerformed
        if(!m_Allergy.equals("Allergy")) {
            m_DiagnosisInfo.setMedicineRowNo();
            m_DiagnosisInfo.reSetEnable();
        } else {
            m_DiagnosisAllergy.reSetEanble();
        }
        this.dispose();
    }//GEN-LAST:event_btn_CloseActionPerformed

    private void btn_SearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SearchActionPerformed
        setModel("LOWER(item) LIKE LOWER('%"+m_Cobww.getSelectedItem().toString().trim()+"%') " , "ENTER");
    }//GEN-LAST:event_btn_SearchActionPerformed

    private void mnit_BackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnit_BackActionPerformed
        btn_CloseActionPerformed(null);
    }//GEN-LAST:event_mnit_BackActionPerformed

    private void tab_MedicineKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tab_MedicineKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_SPACE && tab_Medicine.getValueAt(tab_Medicine.getSelectedRow(), 2) != null) {
            setCheckChange();
        }
    }//GEN-LAST:event_tab_MedicineKeyReleased

    private void btn_EnterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_EnterActionPerformed

        if (!m_Allergy.equals("Allergy")) {  // 用於看診藥品
            int[] column = {2,1,3,4,5,10,11};
            Collection collection = m_ChooseHashMap.values();
            Iterator iterator = collection.iterator();
            while (iterator.hasNext()) {
                 Object[] value = iterator.next().toString().split("    ");
                 if (m_DiagnosisInfo.isCodeAtHashMap(value[0].toString().trim()) == true ) {
                    m_DiagnosisInfo.setDiagnosisInfoTable(value, column);
                 }
            }
        } else {  // 用於過敏紀錄
            int[] column = {2, 1, 3};
            Collection collection = m_ChooseHashMap.values();
            Iterator iterator = collection.iterator();
            while (iterator.hasNext()) {
                Object[] value = iterator.next().toString().split("    ");
                if (m_DiagnosisAllergy.isAllergyHashMap(value[0].toString().trim()) == true ) {
                    this.m_DiagnosisAllergy.setAllergy(value, column);
                }
            }
        }
        btn_CloseActionPerformed(null);
    }//GEN-LAST:event_btn_EnterActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Close;
    private javax.swing.JButton btn_Enter;
    private javax.swing.JButton btn_Search;
    private javax.swing.JLabel lab_Medicine;
    private javax.swing.JLabel lab_Message;
    private javax.swing.JMenu menu_File;
    private javax.swing.JMenuBar mnb;
    private javax.swing.JMenuItem mnit_Back;
    private javax.swing.JPanel pan_Center;
    private javax.swing.JScrollPane span_Medicine;
    private javax.swing.JTable tab_Medicine;
    private javax.swing.JTextField txt_Message;
    // End of variables declaration//GEN-END:variables

}
