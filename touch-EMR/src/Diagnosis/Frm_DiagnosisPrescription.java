package Diagnosis;

import Diagnosis.TableTriStateCell.TriStateCellEditor;
import Diagnosis.TableTriStateCell.TriStateCellRenderer;
import cc.johnwu.sql.*;
import Multilingual.language;
import java.awt.event.ItemEvent;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import AutoComplete.CompleterComboBox;
import Case.Frm_Case;
import ErrorMessage.StoredErrorMessage;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class Frm_DiagnosisPrescription extends javax.swing.JFrame {
    private CompleterComboBox m_Cobww;
    private Map<Object, Object> m_ChooseHashMap = new HashMap<Object, Object>();
    private Frm_DiagnosisInfo m_DiagnosisInfo;
    private Frm_Case m_Case;
    private DefaultTableModel m_TherapyModel;
    /*多國語言變數*/
    private language paragraph = new language();
    private String[] line = paragraph.setlanguage("DIAGNOSISPRESRIPTION").split("\n") ;
    private String[] message = paragraph.setlanguage("MESSAGE").split("\n") ;
    /*輸出錯誤資訊變數*/
    StoredErrorMessage ErrorMessage = new StoredErrorMessage() ;

    public Frm_DiagnosisPrescription(Frm_DiagnosisInfo diagnosisInfo) {
        this.m_DiagnosisInfo = diagnosisInfo;
        initComponents();
        initFram();
        initCobww();
        initLanguage();
    }

    public Frm_DiagnosisPrescription(Frm_Case Frm_Case) {
        this.m_Case = Frm_Case;
        initComponents();
        initFram();
        initCobww();
        initLanguage();
    }

    // 初始化視窗設定
    public void initFram() {
        this.tab_Prescription.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  // tabble不可按住多選
        this.setLocationRelativeTo(this);
        addWindowListener(new WindowAdapter() {  // 畫面關閉原視窗enable
            @Override
            public void windowClosing(WindowEvent windowevent) {
                btn_CloseActionPerformed(null);
            }
        });


    }

    // 初始化下拉式選單
    public void initCobww() {
        String[] icdCob = null ;
        ResultSet rs = null;

        try {
            String sql = "SELECT code, name " +
                         "FROM prescription_code " +
                         "WHERE effective = true ";
 
            rs = DBC.localExecuteQuery(sql);
            rs.last();
            icdCob = new String[rs.getRow()+1];
            rs.beforeFirst();
            int i = 0;
            icdCob[i++] = "";
            while (rs.next()) {
                  icdCob[i++] = rs.getString("code").trim() + "    " + rs.getString("name").trim();
            }

            m_Cobww = new CompleterComboBox(icdCob);

            m_Cobww.setBounds(0, 0, jPanel1.getWidth(), jPanel1.getHeight());
            m_Cobww.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        getComboBoxItemStateChanged(evt);
               }
            });
            jPanel1.add(m_Cobww);
            m_Cobww.setSelectedIndex(0);
            setModel("code LIKE '%'","");
        } catch (SQLException e) {
            Logger.getLogger(Frm_DiagnosisPrescription.class.getName()).log(Level.SEVERE, null, e);
                ErrorMessage.setData("Diagnosis", "Frm_DiagnosisPrescription" ,"initCobww()",
                        e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        } finally {
            try {DBC.closeConnection(rs);}
            catch (SQLException e){
                ErrorMessage.setData("Diagnosis", "Frm_DiagnosisPrescription" ,"initCobww() - DBC.closeConnection",
                        e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            }
        }
    }

    private void initLanguage() {
        //this.lab_Therapy.setText(paragraph.getLanguage(line, "THERAPY"));
        this.btn_Search.setText(paragraph.getLanguage(message, "SEARCH"));
        this.btn_Enter.setText(paragraph.getLanguage(message, "ENTER"));
        this.btn_Close.setText(paragraph.getLanguage(message, "CLOSE"));
        menu_File.setText(paragraph.getLanguage(message, "FILE"));
        mnit_Close.setText(paragraph.getLanguage(message, "CLOSE"));
        txt_Message.setText(paragraph.getLanguage(line, "VERSION"));
    }
    // 取值條件變動進行model重設
    // 參數：condition 搜尋方式與條件  state KeyPress搜尋或是value change
    public void setModel(String condition, String state) {
        Object[][] dataArray = null;
        ResultSet rsTabTherapy = null;
        try {
             Object[] title = {"",paragraph.getLanguage(line, "CODE"),paragraph.getLanguage(line, "TREATMENT"),"Type"};
             String sql = "SELECT * FROM prescription_code WHERE "+condition+" ";
    

             rsTabTherapy = DBC.localExecuteQuery(sql);
             rsTabTherapy.last();
             dataArray = new Object[rsTabTherapy.getRow()][4];
             rsTabTherapy.beforeFirst();
             int i = 0;
             if (!state.equals("ENTER")) {
                while (rsTabTherapy.next()) {

                     dataArray[i][1] = rsTabTherapy.getString("code");
                     dataArray[i][2] = rsTabTherapy.getString("name");
                     dataArray[i][3] = rsTabTherapy.getString("type");
                
                     if (rsTabTherapy.getString("effective").equals("false")) {
                        dataArray[i][0] = null;
                     } else {
                        dataArray[i][0] = false;
                     }
                     i++;
                 }
             } else {
                while (rsTabTherapy.next()) {
                    String search = m_Cobww.getSelectedItem().toString().trim();
                    dataArray[i][1] = rsTabTherapy.getString("code");
                    dataArray[i][2] = "<html>"+rsTabTherapy.getString("name").
                                       replace(search.toUpperCase() , "<font color='FF0000'>"+search.toUpperCase()+"</font>").
                                       replace(search.toLowerCase() , "<font color='FF0000'>"+search.toLowerCase()+"</font>")+"</html>";
                     dataArray[i][3] = rsTabTherapy.getString("type");

                    if(m_ChooseHashMap.get(dataArray[i][0]) != null) {
                        dataArray[i][0] = true;
                     } else {
                        dataArray[i][0] = false;
                     }
                    i++;
                }
             }
             m_TherapyModel = new DefaultTableModel(dataArray,title) {
                @Override
                 public boolean isCellEditable(int rowIndex,int columnIndex) {
                     if (columnIndex == 0) {
                        return true;
                     } else {
                        return false;
                     }
                 }
             };
              tab_Prescription.setModel(m_TherapyModel);
              TableColumn columnCode = this.tab_Prescription.getColumnModel().getColumn(1);
              TableColumn columnName = this.tab_Prescription.getColumnModel().getColumn(2);
              TableColumn columnChoose = this.tab_Prescription.getColumnModel().getColumn(0);
              //set column width
              columnCode.setPreferredWidth(65);
              columnName.setPreferredWidth(735);
              columnChoose.setMaxWidth(50);
              columnChoose.setCellRenderer(new TriStateCellRenderer());
              columnChoose.setCellEditor(new TriStateCellEditor());
              tab_Prescription.setRowHeight(30);
        } catch (SQLException e) {
              Logger.getLogger(Frm_DiagnosisPrescription.class.getName()).log(Level.SEVERE, null, e);
                ErrorMessage.setData("Diagnosis", "Frm_DiagnosisPrescription" ,"setModel(String condition, String state)",
                        e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        } finally {
            try {DBC.closeConnection(rsTabTherapy);}
            catch (SQLException e){
                ErrorMessage.setData("Diagnosis", "Frm_DiagnosisPrescription" ,"setModel(String condition, String state) - DBC.closeConnection",
                        e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            }
        }
    }

  // 選取的資料加入HashCode
  public void setHashMap() {
        String code = tab_Prescription.getValueAt(tab_Prescription.getSelectedRow(), 1).toString().trim();
        String name = tab_Prescription.getValueAt(tab_Prescription.getSelectedRow(), 2).toString().
                                             replace("<html>", "").replace("<font color='FF0000'>", "").
                                             replace("</font>", "").replace("</html>", "").trim();
        String type = tab_Prescription.getValueAt(tab_Prescription.getSelectedRow(), 3).toString().trim();
        if (tab_Prescription.getValueAt(this.tab_Prescription.getSelectedRow(), 0).equals(true) ) {
            m_ChooseHashMap.put(code,code+"    "+name+"    "+type);
        } else if (tab_Prescription.getValueAt(this.tab_Prescription.getSelectedRow(), 0).equals(false) ){
            m_ChooseHashMap.remove(code);
        }
  }

  // checkbox 狀態改變
  public void setCheckChange() {
    if(tab_Prescription.getValueAt(this.tab_Prescription.getSelectedRow(), 0).equals(false)) {
        tab_Prescription.setValueAt(true,this.tab_Prescription.getSelectedRow(), 0);
    } else if(tab_Prescription.getValueAt(this.tab_Prescription.getSelectedRow(), 0).equals(true)) {
        tab_Prescription.setValueAt(false,this.tab_Prescription.getSelectedRow(), 0);
    }
    setHashMap();
  }

    // cobww變動進行資料搜尋
    private void getComboBoxItemStateChanged(ItemEvent evt) {
        if(evt.getStateChange() == ItemEvent.SELECTED ){
             String[] condition = m_Cobww.getSelectedItem().toString().trim().split("    ");  //get select table condition from m_Cobww
             if (condition.length > 1 || condition[0].trim().equals("") ) {
                setModel("code LIKE '"+condition[0]+"%'","");
             } else {
                btn_SearchActionPerformed(null);
             }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pan_Center = new javax.swing.JPanel();
        span_Therapy = new javax.swing.JScrollPane();
        tab_Prescription = new javax.swing.JTable();
        lab_Therapy = new javax.swing.JLabel();
        btn_Search = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        btn_Close = new javax.swing.JButton();
        txt_Message = new javax.swing.JTextField();
        btn_Enter = new javax.swing.JButton();
        mnb = new javax.swing.JMenuBar();
        menu_File = new javax.swing.JMenu();
        mnit_Close = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Laboratory/Radiology(X-RAY) Request");
        setAlwaysOnTop(true);
        setResizable(false);

        tab_Prescription.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tab_Prescription.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tab_Prescription.setRowHeight(25);
        tab_Prescription.getTableHeader().setReorderingAllowed(false);
        tab_Prescription.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tab_PrescriptionMouseClicked(evt);
            }
        });
        tab_Prescription.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tab_PrescriptionKeyReleased(evt);
            }
        });
        span_Therapy.setViewportView(tab_Prescription);

        lab_Therapy.setText("Laboratory/Radiology(X-RAY)");

        btn_Search.setText("Search");
        btn_Search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_SearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 502, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pan_CenterLayout = new javax.swing.GroupLayout(pan_Center);
        pan_Center.setLayout(pan_CenterLayout);
        pan_CenterLayout.setHorizontalGroup(
            pan_CenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan_CenterLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan_CenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(span_Therapy, javax.swing.GroupLayout.DEFAULT_SIZE, 760, Short.MAX_VALUE)
                    .addGroup(pan_CenterLayout.createSequentialGroup()
                        .addComponent(lab_Therapy)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_Search, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        pan_CenterLayout.setVerticalGroup(
            pan_CenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_CenterLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan_CenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan_CenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lab_Therapy)
                        .addComponent(btn_Search))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(span_Therapy, javax.swing.GroupLayout.DEFAULT_SIZE, 481, Short.MAX_VALUE)
                .addContainerGap())
        );

        btn_Close.setText("Close");
        btn_Close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CloseActionPerformed(evt);
            }
        });

        txt_Message.setEditable(false);
        txt_Message.setText("Version : TEST");

        btn_Enter.setText("Enter");
        btn_Enter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_EnterActionPerformed(evt);
            }
        });

        menu_File.setText("File");

        mnit_Close.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0));
        mnit_Close.setText("Close");
        mnit_Close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnit_CloseActionPerformed(evt);
            }
        });
        menu_File.add(mnit_Close);

        mnb.add(menu_File);

        setJMenuBar(mnb);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pan_Center, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txt_Message, javax.swing.GroupLayout.DEFAULT_SIZE, 568, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_Enter, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_Close, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pan_Center, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_Close)
                    .addComponent(txt_Message, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_Enter))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_CloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CloseActionPerformed
        if (m_DiagnosisInfo != null) {
            m_DiagnosisInfo.setDiagnosisRowNo();
            m_DiagnosisInfo.reSetEnable();
        }
        this.dispose();
    }//GEN-LAST:event_btn_CloseActionPerformed

    private void btn_SearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SearchActionPerformed
        setModel("LOWER(name) LIKE LOWER('%"+m_Cobww.getSelectedItem().toString().trim()+"%') " +
                 "AND effective = true ", "ENTER");
    }//GEN-LAST:event_btn_SearchActionPerformed

    private void mnit_CloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnit_CloseActionPerformed
        btn_CloseActionPerformed(null);
}//GEN-LAST:event_mnit_CloseActionPerformed

    private void tab_PrescriptionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab_PrescriptionMouseClicked
        if (tab_Prescription.getValueAt(tab_Prescription.getSelectedRow(), 0) != null
         && tab_Prescription.getSelectedColumn() == 0) {
            setHashMap();
        }
        if (evt.getClickCount() == 2 && tab_Prescription.getValueAt(tab_Prescription.getSelectedRow(), 0) != null) {
             setCheckChange();
        }
    }//GEN-LAST:event_tab_PrescriptionMouseClicked

    private void btn_EnterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_EnterActionPerformed
        int[] column = {1,2,4};
        Collection collection = m_ChooseHashMap.values();
        Iterator iterator = collection.iterator();
        while (iterator.hasNext()) {
            Object[] value = iterator.next().toString().split("    ");
            if (m_DiagnosisInfo != null && m_DiagnosisInfo.isCodeAtHashMap(value[0].toString().trim())) m_DiagnosisInfo.setDiagnosisInfoTable(value, column);
            else if (m_Case != null && m_Case.isCodeAtHashMap(value[0].toString().trim())) m_Case.setDiagnosisInfoTable(value, column);
        }
        btn_CloseActionPerformed(null);
    }//GEN-LAST:event_btn_EnterActionPerformed

    private void tab_PrescriptionKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tab_PrescriptionKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_SPACE && tab_Prescription.getValueAt(tab_Prescription.getSelectedRow(), 2) != null) {
            setCheckChange();
        }
    }//GEN-LAST:event_tab_PrescriptionKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Close;
    private javax.swing.JButton btn_Enter;
    private javax.swing.JButton btn_Search;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lab_Therapy;
    private javax.swing.JMenu menu_File;
    private javax.swing.JMenuBar mnb;
    private javax.swing.JMenuItem mnit_Close;
    private javax.swing.JPanel pan_Center;
    private javax.swing.JScrollPane span_Therapy;
    private javax.swing.JTable tab_Prescription;
    private javax.swing.JTextField txt_Message;
    // End of variables declaration//GEN-END:variables
}