package MedicineStock;

import ErrorMessage.StoredErrorMessage;
import Pharmacy.RefrashPharmacy;
import cc.johnwu.login.UserInfo;
import cc.johnwu.sql.DBC;
import cc.johnwu.sql.HISModel;
import Multilingual.language;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;

public class Frm_MedicineStockPurchaseList extends javax.swing.JFrame {
    private String m_code;
    private Frm_MidicineStockInfo m_MmidicineStockInfo;
    /*多國語言變數*/
    private language paragraph = new language();
    private String[] line = new String(paragraph.setlanguage("MEDICINESTOCKPURCHASELIST")).split("\n") ;
    private String[] message = new String(paragraph.setlanguage("MESSAGE")).split("\n") ;
    /*輸出錯誤資訊變數*/
    StoredErrorMessage ErrorMessage = new StoredErrorMessage() ;

    public Frm_MedicineStockPurchaseList(Frm_MidicineStockInfo midicineStockInfo, Object path) {
        initComponents();
        initLanguage();
        m_code = (String) path;
        m_MmidicineStockInfo = midicineStockInfo;
        ShowList();
        this.tab_List.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  // tabble不可按住多選
        this.setLocationRelativeTo(this);
        addWindowListener(new WindowAdapter() {  // 畫面關閉原視窗enable
        @Override
        public void windowClosing(WindowEvent windowevent) {
            btn_CloseActionPerformed(null);
        }});
        setTotal();

        System.out.println(UserInfo.getSelectPow("Stock"));
        if (!UserInfo.getSelectPow("Stock")) {
            setHidePrice();
        }
    }
    private void initLanguage() {
        this.lab_Quantity.setText(paragraph.getLanguage(line, "QUANTITY"));
        this.lab_Coast.setText(paragraph.getLanguage(line, "COAST"));
        this.btn_Close.setText(paragraph.getLanguage(message, "CLOSE"));
        this.setTitle(paragraph.getLanguage(line, "TITLEPURCHASELIST"));
    }
    public void setTotal() {
        int coast = 0;
        int quantity = 0;
        for (int i = 0; i < tab_List.getRowCount(); i++) {
            if (tab_List.getValueAt(i, 4) != null && tab_List.getValueAt(i, 3) != null) {
                 coast += (Integer.parseInt(tab_List.getValueAt(i, 4).toString())*Integer.parseInt(tab_List.getValueAt(i, 3).toString()));
            }
        }
        txt_Coast.setText(String.valueOf(coast));

        for (int i = 0; i < tab_List.getRowCount(); i++) {
            if (tab_List.getValueAt(i, 3) != null) {
                 quantity += Integer.parseInt(tab_List.getValueAt(i, 3).toString());
            }
        }
        txt_Quantity.setText(String.valueOf(quantity));
    }

    public void setHidePrice() {
        lab_Coast.setVisible(false);
        txt_Coast.setVisible(false);
        setHideColumn(tab_List, 4);
    }

    // 隱藏欄位
    public void setHideColumn(javax.swing.JTable table,int index){
        TableColumn tc= table.getColumnModel().getColumn(index);
        tc.setMaxWidth(0); tc.setPreferredWidth(0); tc.setMinWidth(0);
        tc.setWidth(0); table.getTableHeader().getColumnModel().getColumn(index).setMaxWidth(0);
        table.getTableHeader().getColumnModel().getColumn(index).setMinWidth(0);
    }

    private void ShowList(){
        ResultSet rs = null;
        String Sql="SELECT medicines.code AS 'Medicine',medicine_stock.druggist AS 'Druggist' ,medicine_stock.quantity AS 'Quantity',medicine_stock.price AS 'Price'," +
                    " DATE_FORMAT(medicine_stock.replenish_date,'%Y-%m-%d') AS 'Date' " +
                    "FROM medicines,medicine_stock " +
                    "WHERE medicines.effective=1 AND medicine_stock.exist=1 AND medicine_stock.os_guid IS null AND medicines.code=medicine_stock.m_code AND medicines.code= '"+
                    m_code +"' ";
        System.out.println(Sql);
        try{
            rs = DBC.executeQuery(Sql);
            rs.next();
            this.tab_List.setModel(HISModel.getModel(rs,true));
            
        }catch (SQLException e) {
            ErrorMessage.setData("MedicineStock", "Frm_MedicineStockPurchaseList" ,"ShowList()",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        }finally{ 
            try{
                DBC.closeConnection(rs);
            }catch(SQLException e){
                ErrorMessage.setData("MedicineStock", "Frm_MedicineStockPurchaseList" ,"ShowList() - DBC.closeConnection",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            }
        }
        setCloumnWidth(this.tab_List);
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tab_List = new javax.swing.JTable();
        txt_Coast = new javax.swing.JTextField();
        lab_Quantity = new javax.swing.JLabel();
        txt_Quantity = new javax.swing.JTextField();
        lab_Coast = new javax.swing.JLabel();
        btn_Close = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Purchase List");
        setAlwaysOnTop(true);

        tab_List.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tab_List);

        txt_Coast.setEnabled(false);

        lab_Quantity.setText("Quantity");

        txt_Quantity.setEnabled(false);

        lab_Coast.setText("Cost");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 752, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lab_Quantity)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_Quantity, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lab_Coast)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_Coast, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab_Quantity)
                    .addComponent(txt_Quantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_Coast)
                    .addComponent(txt_Coast, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 496, Short.MAX_VALUE)
                .addContainerGap())
        );

        btn_Close.setText("Close");
        btn_Close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CloseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_Close, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Close)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void setCloumnWidth(javax.swing.JTable tab){
        TableColumn columnNo = tab_List.getColumnModel().getColumn(0);
        TableColumn columnCode = tab_List.getColumnModel().getColumn(1);
        TableColumn columnDruggist = tab_List.getColumnModel().getColumn(2);
        TableColumn columnQuantity = tab_List.getColumnModel().getColumn(3);
        TableColumn columnPrice = tab_List.getColumnModel().getColumn(4);
        TableColumn columnDate = tab_List.getColumnModel().getColumn(5);
        columnNo.setPreferredWidth(30);
        columnCode.setPreferredWidth(150);
        columnDruggist.setPreferredWidth(60);
        columnQuantity.setPreferredWidth(30);
        columnPrice.setPreferredWidth(30);
        columnDate.setPreferredWidth(100);
        tab.setRowHeight(30);

    }

    private void btn_CloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CloseActionPerformed
        m_MmidicineStockInfo.reSetEnable();
        this.dispose();
    }//GEN-LAST:event_btn_CloseActionPerformed



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Close;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lab_Coast;
    private javax.swing.JLabel lab_Quantity;
    private javax.swing.JTable tab_List;
    private javax.swing.JTextField txt_Coast;
    private javax.swing.JTextField txt_Quantity;
    // End of variables declaration//GEN-END:variables

}
