package Anamnesis;


import ErrorMessage.StoredErrorMessage;
import cc.johnwu.login.UserInfo;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.*;
import cc.johnwu.sql.DBC;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import Multilingual.language;
import javax.swing.ListSelectionModel;


public class Frm_Anamnesis extends javax.swing.JFrame {

    private final long REFRASHTIME = 1000; //自動刷新病歷調閱時間
    private RefrashRecord m_RefrashRecord;
    private Boolean cobControl = true;
    private Boolean judge;
    protected static Set<String> m_Select = new HashSet<String>();

    /*多國語言變數*/
    private language paragraph = new language();
    private String[] line = paragraph.setlanguage("ANAMNESIS").split("\n") ;

    /*輸出錯誤資訊變數*/
    StoredErrorMessage ErrorMessage = new StoredErrorMessage() ;

    public Frm_Anamnesis(Boolean judge) {
  
        initComponents();
        init();

        this.judge = judge;
        
        this.setLocationRelativeTo(this);
        if(judge){
            m_RefrashRecord = new RefrashRecord(tab_Anamnesis,REFRASHTIME,true,"");
            m_RefrashRecord.start();
            btn_ReturnSave.setVisible(false);
            btn_ReturnSave.setEnabled(false);
        }else{
            m_RefrashRecord = new RefrashRecord(tab_Anamnesis,REFRASHTIME,false,"");
            m_RefrashRecord.start();
            btn_BorrowSave.setVisible(false);
            btn_BorrowSave.setEnabled(false);
        }

        initComboBox();
        initLables();
        initLanguage();
        txt_BarCode.setFocusable(true);
    }

    private void init(){
        this.setExtendedState(Frm_Anamnesis.MAXIMIZED_BOTH);  // 最大化
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowevent)
            {
                btn_LeaveActionPerformed(null);
                m_RefrashRecord.interrupt();
            }
        });
        this.tab_Anamnesis.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);      // tabble不可按住多選
        this.jTextField1.setText(UserInfo.getUserName());
    }

    public void initComboBox(){
        if(judge){
            this.cob_PoliRoom.setVisible(false);
            this.cob_Policlinic.setVisible(false);
        }else{
            ResultSet rs = null;

            this.cob_PoliRoom.setVisible(true);
            this.cob_Policlinic.setVisible(true);
            this.cob_Policlinic.addItem("");
            try {
                rs = DBC.executeQuery("SELECT name FROM policlinic");
                while(rs.next()){
                    this.cob_Policlinic.addItem(rs.getString("name"));
                }
            } catch (SQLException e) {
                ErrorMessage.setData("Anamnesis", "Frm_Anamnesis" ,"initComboBox()",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
                Logger.getLogger(Frm_Anamnesis.class.getName()).log(Level.SEVERE, null, e);
                
            }
        }
    }
    public void initLables(){
        if(judge){
            this.lab_PoliRoom.setVisible(false);
            this.lab_Policlinic.setVisible(false);
        }
    }

    private void initLanguage() {
        this.jLabel1.setText(paragraph.getLanguage(line, "LABEL"));
        this.lab_Policlinic.setText(paragraph.getLanguage(line, "POLICLINIC"));
       // this.lab_PoliRoom.setText(paragraph.getLanguage(line, "POLIROOM"));
        this.btn_Leave.setText(paragraph.getLanguage(line, "LEAVE"));
        this.btn_BorrowSave.setText(paragraph.getLanguage(line, "BORROWSAVE"));
        this.btn_ReturnSave.setText(paragraph.getLanguage(line, "RETURNSAVE"));
        this.setTitle(paragraph.getLanguage(line, "TITLEMEDICALHISTORY"));
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        btn_ReturnSave = new javax.swing.JButton();
        btn_BorrowSave = new javax.swing.JButton();
        cob_Policlinic = new javax.swing.JComboBox();
        lab_PoliRoom = new javax.swing.JLabel();
        lab_Policlinic = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        cob_PoliRoom = new javax.swing.JComboBox();
        jTextField1 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tab_Anamnesis = new javax.swing.JTable();
        btn_Leave = new javax.swing.JButton();
        txt_BarCode = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Medical History");

        btn_ReturnSave.setText("Return");
        btn_ReturnSave.setEnabled(false);
        btn_ReturnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ReturnSaveActionPerformed(evt);
            }
        });

        btn_BorrowSave.setText("Borrow");
        btn_BorrowSave.setEnabled(false);
        btn_BorrowSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_BorrowSaveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(btn_ReturnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_BorrowSave, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btn_ReturnSave)
                .addComponent(btn_BorrowSave))
        );

        cob_Policlinic.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cob_PoliclinicItemStateChanged(evt);
            }
        });

        lab_PoliRoom.setText("Clinic");

        lab_Policlinic.setText("Policlinic");

        jLabel1.setText("User");

        cob_PoliRoom.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cob_PoliRoomItemStateChanged(evt);
            }
        });

        jTextField1.setEditable(false);

        tab_Anamnesis.setAutoCreateRowSorter(true);
        tab_Anamnesis.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tab_Anamnesis.setGridColor(new java.awt.Color(0, 0, 0));
        tab_Anamnesis.setRowHeight(30);
        tab_Anamnesis.getTableHeader().setReorderingAllowed(false);
        tab_Anamnesis.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tab_AnamnesisMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tab_AnamnesisMouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tab_Anamnesis);

        btn_Leave.setText("Close");
        btn_Leave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_LeaveActionPerformed(evt);
            }
        });

        txt_BarCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_BarCodeKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_BarCodeKeyTyped(evt);
            }
        });

        jLabel2.setText("Barcode:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                                .addGap(298, 298, 298)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_BarCode, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_Leave, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 780, Short.MAX_VALUE))
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lab_Policlinic)
                        .addGap(6, 6, 6)
                        .addComponent(cob_Policlinic, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lab_PoliRoom)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cob_PoliRoom, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_BarCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab_Policlinic)
                    .addComponent(lab_PoliRoom)
                    .addComponent(cob_PoliRoom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cob_Policlinic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 471, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btn_Leave)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tab_AnamnesisMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab_AnamnesisMouseReleased

}//GEN-LAST:event_tab_AnamnesisMouseReleased

    private void tab_AnamnesisMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab_AnamnesisMouseClicked
        if(tab_Anamnesis.getSelectedRow()>=0){
            this.btn_BorrowSave.setEnabled(true);
            this.btn_ReturnSave.setEnabled(true);
            if(tab_Anamnesis.getValueAt(tab_Anamnesis.getSelectedRow(),tab_Anamnesis.getColumnCount()-2).equals(true))
                m_Select.add(tab_Anamnesis.getValueAt(tab_Anamnesis.getSelectedRow(),tab_Anamnesis.getColumnCount()-1).toString());
            else
                m_Select.remove(tab_Anamnesis.getValueAt(tab_Anamnesis.getSelectedRow(),tab_Anamnesis.getColumnCount()-1).toString());
        }else{
            this.btn_BorrowSave.setEnabled(false);
            this.btn_ReturnSave.setEnabled(false);
        }


}//GEN-LAST:event_tab_AnamnesisMouseClicked

    private void cob_PoliRoomItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cob_PoliRoomItemStateChanged
        String conditions = "AND policlinic.name = '" + this.cob_Policlinic.getSelectedItem().toString() + "' ";
        if(evt.getStateChange() == java.awt.event.ItemEvent.SELECTED &&  cobControl==true){
            if(this.cob_Policlinic.getSelectedIndex()!=0){
                if(cob_PoliRoom.getSelectedIndex()!=0){
                    conditions = conditions + "AND poli_room.name = '" + this.cob_PoliRoom.getSelectedItem().toString() + "' ";
                    m_RefrashRecord.interrupt();
                    m_RefrashRecord = new RefrashRecord(tab_Anamnesis,REFRASHTIME,false,conditions);
                    m_RefrashRecord.start();
                }else if(cob_PoliRoom.getSelectedIndex()==0){
                    m_RefrashRecord.interrupt();
                    m_RefrashRecord = new RefrashRecord(tab_Anamnesis,REFRASHTIME,false,conditions);
                    m_RefrashRecord.start();
                }
            }
        }
}//GEN-LAST:event_cob_PoliRoomItemStateChanged

    private void cob_PoliclinicItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cob_PoliclinicItemStateChanged
        String conditions;
        ResultSet rs = null;
        if(evt.getStateChange() == java.awt.event.ItemEvent.SELECTED ){
            if(this.cob_Policlinic.getSelectedIndex()==0){
                m_RefrashRecord.interrupt();
                m_RefrashRecord = new RefrashRecord(tab_Anamnesis,REFRASHTIME,false,"");
                m_RefrashRecord.start();
            }else {
                conditions = "AND policlinic.name = '" + this.cob_Policlinic.getSelectedItem().toString() + "' ";
                m_RefrashRecord.interrupt();
                m_RefrashRecord = new RefrashRecord(tab_Anamnesis,REFRASHTIME,false,conditions);
                m_RefrashRecord.start();
            }
            try {
                rs = DBC.executeQuery("SELECT poli_room.name " +
                        "FROM poli_room, policlinic " +
                        "WHERE poli_room.poli_guid = policlinic.guid " +
                        "AND policlinic.name = '" + this.cob_Policlinic.getSelectedItem().toString() + "' ");
                cobControl = false;
                this.cob_PoliRoom.removeAllItems();
                this.cob_PoliRoom.addItem("");
                while (rs.next()) {
                    this.cob_PoliRoom.addItem(rs.getString("name"));
                }
                cobControl = true;
                cob_PoliRoomItemStateChanged(evt);
            } catch (SQLException e) {
                ErrorMessage.setData("Anamnesis", "Frm_Anamnesis" ,"cob_PoliclinicItemStateChanged(java.awt.event.ItemEvent evt)",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
                Logger.getLogger(Frm_Anamnesis.class.getName()).log(Level.SEVERE, null, e);

            }

        }
    }//GEN-LAST:event_cob_PoliclinicItemStateChanged

    private void btn_ReturnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ReturnSaveActionPerformed
        String sql = null ;
        if(tab_Anamnesis.getSelectedRow()>=0){
           try{
                m_RefrashRecord.interrupt();
                for(int i=0; i<tab_Anamnesis.getRowCount(); i++){
                    if(this.tab_Anamnesis.getValueAt(i, this.tab_Anamnesis.getColumnCount()-2).equals(true)){
                        sql = "UPDATE anamnesis_retrieve " +
                                "SET return_time = now() " +
                                "WHERE anamnesis_retrieve.reg_guid = '" + tab_Anamnesis.getValueAt(i,tab_Anamnesis.getColumnCount()-1 ) + "'";
                        DBC.executeUpdate(sql);
                        m_Select.remove(tab_Anamnesis.getValueAt(i, tab_Anamnesis.getColumnCount()-1).toString());
                    }
                }
                m_RefrashRecord = new RefrashRecord(tab_Anamnesis,REFRASHTIME,false,"");
                m_RefrashRecord.start();
            }catch(Exception e){
                ErrorMessage.setData("Anamnesis", "Frm_Anamnesis" ,"btn_ReturnSaveActionPerformed(java.awt.event.ActionEvent evt)",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
                System.out.println(e);
            }
        }
        btn_ReturnSave.setEnabled(false);
}//GEN-LAST:event_btn_ReturnSaveActionPerformed

@SuppressWarnings(value = "empty-statement")
    private void btn_BorrowSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_BorrowSaveActionPerformed
        String sql = null ;
        try{
            m_RefrashRecord.interrupt();
            for(int i=0; i<tab_Anamnesis.getRowCount(); i++){
                if(m_Select.contains(tab_Anamnesis.getValueAt(i, tab_Anamnesis.getColumnCount()-1).toString())){
                    sql = "INSERT INTO anamnesis_retrieve " +
                            "SELECT uuid(), " +
                            "'"+tab_Anamnesis.getValueAt(i, tab_Anamnesis.getColumnCount()-1).toString()+"', "+
                            "now(), " +
                            "null " +
                            "FROM dual " +
                            "WHERE NOT EXISTS (SELECT * FROM anamnesis_retrieve " +
                                "WHERE anamnesis_retrieve.reg_guid = '"+tab_Anamnesis.getValueAt(i, tab_Anamnesis.getColumnCount()-1).toString()+"') ";
                        DBC.executeUpdate(sql);
                        m_Select.remove(tab_Anamnesis.getValueAt(i, tab_Anamnesis.getColumnCount()-1).toString());
                }
            }
            m_RefrashRecord = new RefrashRecord(tab_Anamnesis,REFRASHTIME,true,"");
            m_RefrashRecord.start();
        }catch(Exception e){
            ErrorMessage.setData("Anamnesis", "Frm_Anamnesis" ,"btn_BorrowSaveActionPerformed(java.awt.event.ActionEvent evt)",
            e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            System.out.println(e);
        }
        btn_BorrowSave.setEnabled(false);
}//GEN-LAST:event_btn_BorrowSaveActionPerformed

private void btn_LeaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_LeaveActionPerformed
    m_RefrashRecord.interrupt();
    new Main.Frm_Main().setVisible(true);
    this.dispose();
}//GEN-LAST:event_btn_LeaveActionPerformed

private void txt_BarCodeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_BarCodeKeyReleased

    String sql = null ;
    System.out.println("0");
    if(evt.getKeyCode() == 10){
        try{
            for(int i=0; i<tab_Anamnesis.getRowCount(); i++){
                if(tab_Anamnesis.getValueAt(i, 0).equals(txt_BarCode.getText())){
                    System.out.println("1");
                    tab_Anamnesis.setValueAt(true, i,9);
                    if(lab_Policlinic.isVisible()==false){
                        //**********
                            m_RefrashRecord.interrupt();
                            sql = "INSERT INTO anamnesis_retrieve " +
                                    "SELECT uuid(), " +
                                    "'"+tab_Anamnesis.getValueAt(i, tab_Anamnesis.getColumnCount()-1).toString()+"', "+
                                    "now(), " +
                                    "null " +
                                    "FROM dual " +
                                    "WHERE NOT EXISTS (SELECT * FROM anamnesis_retrieve " +
                                        "WHERE anamnesis_retrieve.reg_guid = '"+tab_Anamnesis.getValueAt(i, tab_Anamnesis.getColumnCount()-1).toString()+"') ";
                            DBC.executeUpdate(sql);
                            m_Select.remove(tab_Anamnesis.getValueAt(i, tab_Anamnesis.getColumnCount()-1).toString());
                            m_RefrashRecord = new RefrashRecord(tab_Anamnesis,REFRASHTIME,true,"");
                            m_RefrashRecord.start();

                        //**********
                    }else {//if(lab_Policlinic.isVisible()==true){
                            m_RefrashRecord.interrupt();
                            sql = "UPDATE anamnesis_retrieve " +
                                    "SET return_time = now() " +
                                    "WHERE anamnesis_retrieve.reg_guid = '" + tab_Anamnesis.getValueAt(i,tab_Anamnesis.getColumnCount()-1 ) + "'";
                            DBC.executeUpdate(sql);
                            m_Select.remove(tab_Anamnesis.getValueAt(i, tab_Anamnesis.getColumnCount()-1).toString());
                            m_RefrashRecord = new RefrashRecord(tab_Anamnesis,REFRASHTIME,false,"");
                            m_RefrashRecord.start();
                        }}
                    }
                    txt_BarCode.setText("");
            }catch(SQLException e) {
                ErrorMessage.setData("Anamnesis", "Frm_Anamnesis" ,"cob_PoliclinicItemStateChanged(java.awt.event.ItemEvent evt)",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
                System.out.println(e);
            }
        }
}//GEN-LAST:event_txt_BarCodeKeyReleased

private void txt_BarCodeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_BarCodeKeyTyped
    // TODO add your handling code here:
}//GEN-LAST:event_txt_BarCodeKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_BorrowSave;
    private javax.swing.JButton btn_Leave;
    private javax.swing.JButton btn_ReturnSave;
    private javax.swing.JComboBox cob_PoliRoom;
    private javax.swing.JComboBox cob_Policlinic;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel lab_PoliRoom;
    private javax.swing.JLabel lab_Policlinic;
    private javax.swing.JTable tab_Anamnesis;
    private javax.swing.JTextField txt_BarCode;
    // End of variables declaration//GEN-END:variables

}