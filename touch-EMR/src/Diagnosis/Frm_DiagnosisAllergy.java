
package Diagnosis;

import ErrorMessage.StoredErrorMessage;
import cc.johnwu.sql.*;
import Multilingual.language;
import cc.johnwu.login.UserInfo;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class Frm_DiagnosisAllergy extends javax.swing.JFrame {
    private String m_PNo;
    private String m_PName;
    private String[] m_GetAllergyGuid;  // 儲存資料庫過敏資料guid
    private DefaultTableModel m_Model;
    private int m_RowCount = 0;
    private DiagnosisInterface m_DiagnosisAllergy;
    private JComboBox m_ComboBox = new JComboBox();   // 嚴重程度combobox
    private Map<Object, Object> m_AllergyHashMap = new LinkedHashMap<Object, Object>();  // 儲存已存在的過敏資料
    /*多國語言變數*/
    private language paragraph = new language();
    private String[] line = paragraph.setlanguage("DIAGNOSISALLERGY").split("\n") ;
    private String[] message = paragraph.setlanguage("MESSAGE").split("\n") ;
    /*輸出錯誤資訊變數*/
    StoredErrorMessage ErrorMessage = new StoredErrorMessage() ;
    
    public Frm_DiagnosisAllergy(DiagnosisInterface diagnosisInfo, String pNo, String pName) {

            this.m_DiagnosisAllergy = diagnosisInfo;
            this.m_PNo = pNo;
            this.m_PName = pName;
            initComponents();
            initFrame();
            initLanguage();
             initUdata();
    }

    private void initUdata() {
        try {
            String sql = "SELECT MAX(udate) AS udate, u_sid FROM allergy WHERE p_no = '" + m_PNo + "'";
            ResultSet rs = DBC.executeQuery(sql);
            if (rs.next() && rs.getString("udate")!= null) {
                lab_Udata.setText(rs.getString("udate") + " " + rs.getString("u_sid"));
            } else {
                lab_Udata.setText("Newly diagnosed.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Frm_DiagnosisAllergy.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void initFrame() {
        this.setLocationRelativeTo(this);
        this.lab_No.setText(m_PNo);
        this.lab_Name.setText(m_PName);
        addWindowListener(new WindowAdapter() {  // 畫面關閉原視窗enable
        @Override
        public void windowClosing(WindowEvent windowevent) {
            btn_CloseActionPerformed(null);
        }});
        showAllergy();
    }
    private void initLanguage() {
        //this.lab_TitleNo.setText(paragraph.getLanguage(line, "TITLENO"));
        this.lab_TitleName.setText(paragraph.getLanguage(line, "TITLENAME"));
        this.btn_New.setText(paragraph.getLanguage(line, "NEW"));
        this.btn_Save.setText(paragraph.getLanguage(message, "SAVE"));
        this.btn_Close.setText(paragraph.getLanguage(message, "CLOSE"));
        menu_File.setText(paragraph.getLanguage(message, "FILE"));
        mnit_New.setText(paragraph.getLanguage(line, "ADD"));
        mnit_Save.setText(paragraph.getLanguage(message, "SAVE"));
        mnit_Back.setText(paragraph.getLanguage(line, "BACK"));
        this.setTitle(paragraph.getLanguage(line, "TITLEALLERQY"));
    }
    // 顯示此人過敏紀錄
    public void showAllergy() {
            this.tab_Allergy.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  // tabble不可按住多選
            ResultSet rs = null;
            try {
                m_AllergyHashMap.clear();
                m_ComboBox.removeAllItems();
                btn_Save.setEnabled(false);
                m_RowCount = 0;
                ((DefaultTableModel)tab_Allergy.getModel()).setRowCount(0);
                m_ComboBox.addItem(paragraph.getLanguage(line, "SLIGHT"));
                m_ComboBox.addItem(paragraph.getLanguage(line, "GENERAL"));
                m_ComboBox.addItem(paragraph.getLanguage(line, "SERIOUS"));
                m_ComboBox.addItem(paragraph.getLanguage(line, "DELETE"));
                Object[] title = new Object[] {"", paragraph.getLanguage(line, "MEDICINE"), "Name", paragraph.getLanguage(line, "LEVEL")}; // Medicine hide
                Object[][] rsArray;
                String sql = "SELECT allergy.guid, medicines.code, medicines.item, allergy.level " +
                             "FROM patients_info, medicines, allergy " +
                             "WHERE patients_info.p_no = '"+m_PNo+"' " +
                             "AND allergy.p_no = patients_info.p_no " +
                             "AND allergy.m_code = medicines.code  " +
                             "AND level <> 0 "+
                             "ORDER BY medicines.code ";
                rs = (ResultSet) DBC.executeQuery(sql);

                rs.last();

                m_GetAllergyGuid = new String[rs.getRow()];
                rsArray = new Object[rs.getRow()][4];
                rs.beforeFirst();
                while (rs.next()) {
                    m_GetAllergyGuid[rs.getRow()-1] = rs.getString("guid");
                    int allergyLevel = rs.getInt("level");

                    for (int i = 0; i <= 3 ; i++) {
                        switch(i){
                            case 0:
                                rsArray[rs.getRow()-1][i] = 1+m_RowCount++;
                                break;
                            case 1:
                                rsArray[rs.getRow()-1][i] = rs.getString("item");
                                m_AllergyHashMap.put(rs.getString("code"), rs.getString("code"));
                                break;
                            case 2:
                                rsArray[rs.getRow()-1][i] = rs.getString("item");
                                break;
                            case 3:
                                if (allergyLevel == 1) {
                                    rsArray[rs.getRow()-1][i] = paragraph.getLanguage(line, "SLIGHT");
                                } else if (allergyLevel == 2) {
                                    rsArray[rs.getRow()-1][i] = paragraph.getLanguage(line, "GENERAL");
                                } else if (allergyLevel == 3) {
                                    rsArray[rs.getRow()-1][i] = paragraph.getLanguage(line, "SERIOUS");
                                }
                                break;
                        }
                    }
                }
                final int[] columnEditable = {3};

                m_Model = new DefaultTableModel(rsArray,title) {
                    @Override
                    public boolean isCellEditable(int rowIndex,int columnIndex) {
                        for (int i = 0; i < columnEditable.length; i++){
                            if(columnIndex == columnEditable[i])
                                return true;
                        }
                        return false;
                    }
                };
                this.tab_Allergy.setModel(m_Model);
                TableColumn columnNo = this.tab_Allergy.getColumnModel().getColumn(0);
                TableColumn columnCode = this.tab_Allergy.getColumnModel().getColumn(1);
                TableColumn columnItem = this.tab_Allergy.getColumnModel().getColumn(2);
                TableColumn columnL = this.tab_Allergy.getColumnModel().getColumn(3);
                //set column width
                columnNo.setPreferredWidth(25);
                columnCode.setPreferredWidth(600);
                columnItem.setPreferredWidth(600);
                columnL.setPreferredWidth(150);
                columnL.setCellEditor(new DefaultCellEditor(m_ComboBox));
                tab_Allergy.setRowHeight(30);
                Common.TabTools.setHideColumn(tab_Allergy, 2); // Medicine hide
            } catch (SQLException e) {
//                Logger.getLogger(Frm_DiagnosisAllergy.class.getName()).log(Level.SEVERE, null, e);
                    ErrorMessage.setData("Diagnosis", "Frm_DiagnosisAllergy" ,"showAllergy()",
                        e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            } finally {
                try {DBC.closeConnection(rs);}
                catch (SQLException e){
                    ErrorMessage.setData("Diagnosis", "Frm_DiagnosisAllergy" ,"showAllergy() - DBC.closeConnection",
                            e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
                }
            }
     }

    // 過敏資料加入
    public void setAllergy(Object[] value, int column[] ) {
        m_Model.addRow(new Vector());
        tab_Allergy.setValueAt(m_RowCount+1, m_RowCount, 0);  //設定行號
        for (int i = 0; i < value.length; i++) {
            tab_Allergy.setValueAt(value[i], m_RowCount, column[i]);
        }
        m_RowCount++;
    }

    // 判斷過敏資料是否已存在
    public boolean isAllergyHashMap(Object code) {
        if (m_AllergyHashMap.get(code) != null) {
            JOptionPane.showMessageDialog(null,paragraph.getLanguage(message, "CODE") + code +
                                               paragraph.getLanguage(message, "ALREADYEXIST"));
            m_AllergyHashMap.put(code.toString().trim(), code.toString().trim());
            return false;
        } else {
            m_AllergyHashMap.put(code.toString().trim(), code.toString().trim());
            return true;
        }
    }

    // 將過敏程度轉為儲存於資料庫的數字
    public int getAllergyLevel(Object level) {
        if (level.equals(paragraph.getLanguage(line, "SLIGHT"))){
            return 1;
        } else if (level.equals(paragraph.getLanguage(line, "GENERAL"))) {
            return 2;
        } else {
            return 3;
        }
    }

    // 回到看診畫面 畫面重設為可編輯
    public void reSetEanble() {  
        this.setEnabled(true);
       // this.setAlwaysOnTop(true);
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pan_Center = new javax.swing.JPanel();
        scpn_Allergy = new javax.swing.JScrollPane();
        tab_Allergy = new javax.swing.JTable();
        pan_Up = new javax.swing.JPanel();
        btn_New = new javax.swing.JButton();
        lab_TitleName = new javax.swing.JLabel();
        lab_Name = new javax.swing.JLabel();
        lab_TitleNo = new javax.swing.JLabel();
        lab_No = new javax.swing.JLabel();
        pan_Down = new javax.swing.JPanel();
        btn_Close = new javax.swing.JButton();
        btn_Save = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        lab_Udata = new javax.swing.JLabel();
        mnb = new javax.swing.JMenuBar();
        menu_File = new javax.swing.JMenu();
        mnit_New = new javax.swing.JMenuItem();
        mnit_Save = new javax.swing.JMenuItem();
        mnit_Back = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Allergy");
        setAlwaysOnTop(true);

        tab_Allergy.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tab_Allergy.setRowHeight(25);
        tab_Allergy.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tab_AllergyMouseReleased(evt);
            }
        });
        scpn_Allergy.setViewportView(tab_Allergy);

        btn_New.setText("Add");
        btn_New.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_NewActionPerformed(evt);
            }
        });

        lab_TitleName.setText("Name：");

        lab_Name.setText("--");

        lab_TitleNo.setText("Patient No.：");

        lab_No.setText("--");

        javax.swing.GroupLayout pan_UpLayout = new javax.swing.GroupLayout(pan_Up);
        pan_Up.setLayout(pan_UpLayout);
        pan_UpLayout.setHorizontalGroup(
            pan_UpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan_UpLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lab_TitleNo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lab_No, javax.swing.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(lab_TitleName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lab_Name, javax.swing.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE)
                .addGap(325, 325, 325)
                .addComponent(btn_New, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pan_UpLayout.setVerticalGroup(
            pan_UpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_UpLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pan_UpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab_TitleNo)
                    .addComponent(lab_No)
                    .addComponent(lab_TitleName)
                    .addComponent(lab_Name)
                    .addComponent(btn_New))
                .addGap(12, 12, 12))
        );

        javax.swing.GroupLayout pan_CenterLayout = new javax.swing.GroupLayout(pan_Center);
        pan_Center.setLayout(pan_CenterLayout);
        pan_CenterLayout.setHorizontalGroup(
            pan_CenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_CenterLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan_CenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pan_Up, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scpn_Allergy, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 763, Short.MAX_VALUE))
                .addContainerGap())
        );
        pan_CenterLayout.setVerticalGroup(
            pan_CenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_CenterLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pan_Up, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scpn_Allergy, javax.swing.GroupLayout.DEFAULT_SIZE, 433, Short.MAX_VALUE)
                .addContainerGap())
        );

        btn_Close.setText("Close");
        btn_Close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CloseActionPerformed(evt);
            }
        });

        btn_Save.setText("Save");
        btn_Save.setEnabled(false);
        btn_Save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_SaveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pan_DownLayout = new javax.swing.GroupLayout(pan_Down);
        pan_Down.setLayout(pan_DownLayout);
        pan_DownLayout.setHorizontalGroup(
            pan_DownLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_DownLayout.createSequentialGroup()
                .addComponent(btn_Save, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Close, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pan_DownLayout.setVerticalGroup(
            pan_DownLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_DownLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btn_Close)
                .addComponent(btn_Save))
        );

        jLabel1.setText("Latest Update：");

        lab_Udata.setText("--");

        menu_File.setText("File");

        mnit_New.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, 0));
        mnit_New.setText("Add");
        mnit_New.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnit_NewActionPerformed(evt);
            }
        });
        menu_File.add(mnit_New);

        mnit_Save.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, 0));
        mnit_Save.setText("Save");
        mnit_Save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnit_SaveActionPerformed(evt);
            }
        });
        menu_File.add(mnit_Save);

        mnit_Back.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0));
        mnit_Back.setText("Back");
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
                        .addGap(10, 10, 10)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lab_Udata)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 469, Short.MAX_VALUE)
                        .addComponent(pan_Down, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pan_Center, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pan_Down, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(lab_Udata)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_CloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CloseActionPerformed
        m_DiagnosisAllergy.reSetEnable();
        this.dispose();
    }//GEN-LAST:event_btn_CloseActionPerformed

    private void btn_NewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_NewActionPerformed
        //this.setAlwaysOnTop(false);
        this.setEnabled(false);
        btn_Save.setEnabled(true);
        new Frm_DiagnosisMedicine(this, "Allergy").setVisible(true);
    }//GEN-LAST:event_btn_NewActionPerformed

    private void btn_SaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SaveActionPerformed
        try {
            for (int i = 0; i < m_GetAllergyGuid.length; i++) {
                if (!tab_Allergy.getValueAt(i, 3).equals("Delete")) {
                    DBC.executeUpdate("UPDATE allergy SET level = "+getAllergyLevel(tab_Allergy.getValueAt(i, 3))+", u_sid = '"+UserInfo.getUserID()+"' , udate = NOW() WHERE guid = '"+m_GetAllergyGuid[i]+"'");
                } else {
                    DBC.executeUpdate("UPDATE allergy SET level = 0, u_sid = '"+UserInfo.getUserID()+"' , udate = NOW()  WHERE guid = '"+m_GetAllergyGuid[i]+"'");
                }
            }
            if (m_GetAllergyGuid.length != tab_Allergy.getRowCount()) {
                for (int i = m_GetAllergyGuid.length; i < tab_Allergy.getRowCount(); i++) {
               
                    if (!tab_Allergy.getValueAt(i, 3).equals("Delete")) {
                        String sqlSetInsert = "INSERT allergy VALUES(uuid(), '"+m_PNo+"', '"+tab_Allergy.getValueAt(i, 2)+"', "+getAllergyLevel(tab_Allergy.getValueAt(i, 3))+", '"+UserInfo.getUserID()+"' ,NOW()) ";
                        DBC.executeUpdate(sqlSetInsert);
                    }
                }
            }
            initUdata();
            m_DiagnosisAllergy.getAllergy();  // 更新儲存於Frm_DiagnosisInfo 的過敏資料
            showAllergy();
            JOptionPane.showMessageDialog(null, paragraph.getLanguage(message, "SAVECOMPLETE"));
        } catch (SQLException e) {
//            Logger.getLogger(Frm_DiagnosisAllergy.class.getName()).log(Level.SEVERE, null, e);
                ErrorMessage.setData("Diagnosis", "Frm_DiagnosisAllergy" ,"btn_SaveActionPerformed()",
                        e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        }
    }//GEN-LAST:event_btn_SaveActionPerformed

    private void tab_AllergyMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab_AllergyMouseReleased
        if(tab_Allergy.getSelectedColumn() == 3) {
            btn_Save.setEnabled(true);
        }
    }//GEN-LAST:event_tab_AllergyMouseReleased

    private void mnit_BackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnit_BackActionPerformed
        btn_CloseActionPerformed(null);
}//GEN-LAST:event_mnit_BackActionPerformed

    private void mnit_NewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnit_NewActionPerformed
        btn_NewActionPerformed(null);
    }//GEN-LAST:event_mnit_NewActionPerformed

    private void mnit_SaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnit_SaveActionPerformed
      if(btn_Save.isEnabled()) {
        btn_SaveActionPerformed(null);
      }
    }//GEN-LAST:event_mnit_SaveActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Close;
    private javax.swing.JButton btn_New;
    private javax.swing.JButton btn_Save;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lab_Name;
    private javax.swing.JLabel lab_No;
    private javax.swing.JLabel lab_TitleName;
    private javax.swing.JLabel lab_TitleNo;
    private javax.swing.JLabel lab_Udata;
    private javax.swing.JMenu menu_File;
    private javax.swing.JMenuBar mnb;
    private javax.swing.JMenuItem mnit_Back;
    private javax.swing.JMenuItem mnit_New;
    private javax.swing.JMenuItem mnit_Save;
    private javax.swing.JPanel pan_Center;
    private javax.swing.JPanel pan_Down;
    private javax.swing.JPanel pan_Up;
    private javax.swing.JScrollPane scpn_Allergy;
    private javax.swing.JTable tab_Allergy;
    // End of variables declaration//GEN-END:variables

}
