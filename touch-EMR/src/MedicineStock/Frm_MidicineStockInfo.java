package MedicineStock;

import AutoComplete.CompleterComboBox;
import ErrorMessage.StoredErrorMessage;
import cc.johnwu.sql.DBC;
import cc.johnwu.sql.HISModel;
import Multilingual.language;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;


public class Frm_MidicineStockInfo extends javax.swing.JFrame implements cc.johnwu.date.DateInterface {
    private CompleterComboBox m_Cobww;
    private CompleterComboBox m_PurchaseCombo;
    /*多國語言變數*/
    private language paragraph = new language();
    private String[] line = new String(paragraph.setlanguage("MIDICINESTOCKINFO")).split("\n") ;
    private String[] message = new String(paragraph.setlanguage("MESSAGE")).split("\n") ;
    /*輸出錯誤資訊變數*/
    StoredErrorMessage ErrorMessage = new StoredErrorMessage() ;

    public Frm_MidicineStockInfo() {
        initComponents();
        initLanguage() ;
        this.setExtendedState(this.MAXIMIZED_BOTH);  // 最大化
        this.tab_MedicineList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  // tabble不可按住多選
        this.setLocationRelativeTo(this);
        addWindowListener(new WindowAdapter() {  // 畫面關閉原視窗enable
        @Override
        public void windowClosing(WindowEvent windowevent) {
            btn_CloseActionPerformed(null);
        }});
        showMedTabList();

    }
    private void initLanguage() {
        this.btn_Search.setText(paragraph.getLanguage(message, "SEARCH"));
        this.btn_Purchase.setText(paragraph.getLanguage(line, "PURCHASE"));
        this.btn_More.setText(paragraph.getLanguage(line, "MORE"));
        this.btn_Close.setText(paragraph.getLanguage(message, "CLOSE"));
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pan_Center = new javax.swing.JPanel();
        pan_Top = new javax.swing.JPanel();
        btn_Search = new javax.swing.JButton();
        text_Search = new javax.swing.JTextField();
        span_MedicineList = new javax.swing.JScrollPane();
        tab_MedicineList = new javax.swing.JTable();
        pan_Right = new javax.swing.JPanel();
        btn_Purchase = new javax.swing.JButton();
        btn_Close = new javax.swing.JButton();
        btn_More = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Medicine Stock ");

        btn_Search.setText("Search");
        btn_Search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_SearchActionPerformed(evt);
            }
        });

        text_Search.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                text_SearchKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout pan_TopLayout = new javax.swing.GroupLayout(pan_Top);
        pan_Top.setLayout(pan_TopLayout);
        pan_TopLayout.setHorizontalGroup(
            pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan_TopLayout.createSequentialGroup()
                .addComponent(text_Search, javax.swing.GroupLayout.DEFAULT_SIZE, 563, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Search, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(117, 117, 117))
        );
        pan_TopLayout.setVerticalGroup(
            pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(text_Search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btn_Search))
        );

        tab_MedicineList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tab_MedicineList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tab_MedicineListMouseClicked(evt);
            }
        });
        span_MedicineList.setViewportView(tab_MedicineList);

        btn_Purchase.setText("Purchase");
        btn_Purchase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_PurchaseActionPerformed(evt);
            }
        });

        btn_Close.setText("Close");
        btn_Close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CloseActionPerformed(evt);
            }
        });

        btn_More.setText("More");
        btn_More.setEnabled(false);
        btn_More.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_MoreActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pan_RightLayout = new javax.swing.GroupLayout(pan_Right);
        pan_Right.setLayout(pan_RightLayout);
        pan_RightLayout.setHorizontalGroup(
            pan_RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_RightLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan_RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_Purchase, javax.swing.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE)
                    .addComponent(btn_More, javax.swing.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE)
                    .addComponent(btn_Close, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE))
                .addContainerGap())
        );
        pan_RightLayout.setVerticalGroup(
            pan_RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_RightLayout.createSequentialGroup()
                .addComponent(btn_Purchase)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_More)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Close)
                .addContainerGap(396, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pan_CenterLayout = new javax.swing.GroupLayout(pan_Center);
        pan_Center.setLayout(pan_CenterLayout);
        pan_CenterLayout.setHorizontalGroup(
            pan_CenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_CenterLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan_CenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pan_Top, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan_CenterLayout.createSequentialGroup()
                        .addComponent(span_MedicineList, javax.swing.GroupLayout.DEFAULT_SIZE, 660, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pan_Right, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        pan_CenterLayout.setVerticalGroup(
            pan_CenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_CenterLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pan_Top, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_CenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pan_Right, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(span_MedicineList, javax.swing.GroupLayout.DEFAULT_SIZE, 517, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pan_Center, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pan_Center, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void showMedTabList(){//各藥品剩餘總量
        ResultSet tabArray= null;
        try{
            String ArraySql="SELECT medicines.code, ( "+
                        "CASE WHEN (SELECT SUM(quantity) FROM medicine_stock AS B WHERE B.m_code = medicines.code AND os_guid IS NULL GROUP BY code) "+
                        "IS NULL THEN 0 "+
                        "ELSE (SELECT SUM(quantity) FROM medicine_stock AS B WHERE B.m_code = medicines.code AND os_guid IS NULL GROUP BY code) "+
                        "END "+
                        "- "+
                        "CASE WHEN (SELECT SUM(quantity) FROM medicine_stock AS B WHERE B.m_code = medicines.code AND os_guid IS NOT NULL GROUP BY code) "+
                        "IS NULL THEN 0 "+
                        "ELSE (SELECT SUM(quantity) FROM medicine_stock AS B WHERE B.m_code = medicines.code AND os_guid IS NOT NULL GROUP BY code) "+
                        "END "+
                        ") AS quantity "+
                        "FROM medicines ,medicine_stock " +
                        "WHERE exist = 1 " +
                        "AND effective = 1 " +
                        "GROUP BY medicines.code ";
            tabArray= DBC.executeQuery(ArraySql);
            this.tab_MedicineList.setModel(HISModel.getModel(tabArray, true));
            DBC.closeConnection(tabArray);
        }catch(SQLException e) {
            ErrorMessage.setData("MedicineStock", "Frm_MedicineStockInfo" ,"showMedTabList()",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        }
        setCloumnWidth(this.tab_MedicineList);
    }

    private void setCloumnWidth(javax.swing.JTable tab){
        //設定column寬度
        TableColumn columnVisitNo = tab.getColumnModel().getColumn(0);
        TableColumn columnVisitName = tab.getColumnModel().getColumn(1);
        TableColumn columnVisitQuantity = tab.getColumnModel().getColumn(2);
        columnVisitNo.setPreferredWidth(30);
        columnVisitName.setPreferredWidth(250);
        columnVisitQuantity.setPreferredWidth(150);
        tab.setRowHeight(30);
    }

    // 回到看診畫面 畫面重設為可編輯
    public void reSetEnable() {
        this.setEnabled(true);
    }
    
    private void btn_PurchaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_PurchaseActionPerformed
        new Frm_MedicineStockPurchase(this).setVisible(true);
        btn_More.setEnabled(false);
        tab_MedicineList.removeRowSelectionInterval(0, tab_MedicineList.getRowCount()-1);
        this.setEnabled(false);
    }//GEN-LAST:event_btn_PurchaseActionPerformed

    private void btn_CloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CloseActionPerformed
        new Main.Frm_Main().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btn_CloseActionPerformed

    private void btn_SearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SearchActionPerformed
        ResultSet tabArray= null;
        if(!text_Search.getText().equals("")){
            String conditions = "AND (UPPER(medicines.code) LIKE UPPER('%" + text_Search.getText().replace(" ", "%") + "%') ) ";
            try{
                String ArraySql="SELECT medicines.code, ( "+
                            "CASE WHEN (SELECT SUM(quantity) FROM medicine_stock AS B WHERE B.m_code = medicines.code AND os_guid IS NULL GROUP BY code) "+
                            "IS NULL THEN 0 "+
                            "ELSE (SELECT SUM(quantity) FROM medicine_stock AS B WHERE B.m_code = medicines.code AND os_guid IS NULL GROUP BY code) "+
                            "END "+
                            "- "+
                            "CASE WHEN (SELECT SUM(quantity) FROM medicine_stock AS B WHERE B.m_code = medicines.code AND os_guid IS NOT NULL GROUP BY code) "+
                            "IS NULL THEN 0 "+
                            "ELSE (SELECT SUM(quantity) FROM medicine_stock AS B WHERE B.m_code = medicines.code AND os_guid IS NOT NULL GROUP BY code) "+
                            "END "+
                            ") AS quantity "+
                            "FROM medicines ,medicine_stock " +
                            "WHERE exist = 1 " +
                            "AND effective = 1 " +
                            conditions +
                            "GROUP BY medicines.code ";
                tabArray= DBC.executeQuery(ArraySql);
                this.tab_MedicineList.setModel(HISModel.getModel(tabArray, true));
                DBC.closeConnection(tabArray);
            }catch(SQLException e) {
            ErrorMessage.setData("MedicineStock", "Frm_MedicineStockInfo" ,"btn_SearchActionPerformed()",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            }
            setCloumnWidth(this.tab_MedicineList);
        }else{showMedTabList();}
    }//GEN-LAST:event_btn_SearchActionPerformed

    private void btn_MoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_MoreActionPerformed
        this.setEnabled(false);
        
        new Frm_MedicineStockPurchaseList(this, tab_MedicineList.getValueAt(tab_MedicineList.getSelectedRow(), 1)).setVisible(true);

    }//GEN-LAST:event_btn_MoreActionPerformed

    private void tab_MedicineListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab_MedicineListMouseClicked
        if (tab_MedicineList.getSelectedRow() != -1) {
            btn_More.setEnabled(true);
        }else {btn_More.setEnabled(false);}

        if (evt.getClickCount() == 2) {
            btn_MoreActionPerformed(null);
        }
    }//GEN-LAST:event_tab_MedicineListMouseClicked

    private void text_SearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_text_SearchKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btn_SearchActionPerformed(null);
        }
    }//GEN-LAST:event_text_SearchKeyPressed
   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Close;
    private javax.swing.JButton btn_More;
    private javax.swing.JButton btn_Purchase;
    private javax.swing.JButton btn_Search;
    private javax.swing.JPanel pan_Center;
    private javax.swing.JPanel pan_Right;
    private javax.swing.JPanel pan_Top;
    private javax.swing.JScrollPane span_MedicineList;
    private javax.swing.JTable tab_MedicineList;
    private javax.swing.JTextField text_Search;
    // End of variables declaration//GEN-END:variables

    public void onDateChanged() {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

}
