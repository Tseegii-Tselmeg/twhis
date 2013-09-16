package Pharmacy;

import ErrorMessage.StoredErrorMessage;
import cc.johnwu.date.DateMethod;
import cc.johnwu.sql.*;
import Multilingual.language;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.table.TableColumn;
import java.sql.* ;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class Frm_PharmacyInquire extends javax.swing.JFrame {
    private Frm_Pharmacy m_Pharmacy;
    /*多國語言變數*/
    private language paragraph = new language();
    private String[] line = new String(paragraph.setlanguage("PHARMACYINQUIRE")).split("\n") ;
    private String[] message = new String(paragraph.setlanguage("MESSAGE")).split("\n") ;
    /*輸出錯誤資訊變數*/
    StoredErrorMessage ErrorMessage = new StoredErrorMessage() ;
    
    public Frm_PharmacyInquire(Frm_Pharmacy pharmacy) {
        m_Pharmacy = pharmacy;
        initComponents();
        initFrame();
        initLanguage() ;
    }
    private void initFrame(){
        this.setExtendedState(this.MAXIMIZED_BOTH);  // 最大化
        this.setLocationRelativeTo(this);
        this.tab_Pharmacy.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  // tabble不可按住多選
        addWindowListener(new WindowAdapter() {  // 畫面關閉原視窗enable
            @Override
            public void windowClosing(WindowEvent windowevent) {
                btn_CloseActionPerformed(null);
            }
        });
        cob_Shift.setSelectedIndex(DateMethod.getNowShiftNum()-1);
        btn_SearchActionPerformed(null);
    }

    private void initLanguage() {
        this.btn_Close.setText(paragraph.getLanguage(message, "CLOSE"));
        this.btn_Check.setText(paragraph.getLanguage(message, "CHECK"));
        this.btn_Search.setText(paragraph.getLanguage(message, "SEARCH"));
        cob_Shift.setModel(new javax.swing.DefaultComboBoxModel(
                new String[] { paragraph.getLanguage(line, "MORNING"), paragraph.getLanguage(line, "NOON"), paragraph.getLanguage(line, "AFTERNOON")
            }
        ));
        this.setTitle(paragraph.getLanguage(line, "TITLEHISTORYRECORD"));
    }
    private void setCloumnWidth(javax.swing.JTable tab){
        TableColumn columnNo = tab.getColumnModel().getColumn(0);
        TableColumn columnPNo = tab.getColumnModel().getColumn(1);
        TableColumn columnName = tab.getColumnModel().getColumn(2);
        TableColumn columnBirth = tab.getColumnModel().getColumn(3);
        TableColumn columnGender = tab.getColumnModel().getColumn(4);
        TableColumn columnPs = tab.getColumnModel().getColumn(5);
        TableColumn columnPOLI = tab.getColumnModel().getColumn(6);
        TableColumn columnDoctor = tab.getColumnModel().getColumn(7);
        TableColumn columnBlood = tab.getColumnModel().getColumn(8);
        TableColumn columnTime = tab.getColumnModel().getColumn(10);
        columnNo.setPreferredWidth(30);
        columnPOLI.setPreferredWidth(120);
        columnDoctor.setPreferredWidth(100);
        columnPNo.setPreferredWidth(70);
        columnName.setPreferredWidth(150);
        columnBirth.setPreferredWidth(90);
        columnGender.setPreferredWidth(50);
        columnBlood.setPreferredWidth(50);
        columnPs.setPreferredWidth(200);
        columnTime.setPreferredWidth(375);
        tab.setRowHeight(30);
        setHideColumn(tab_Pharmacy, 9);
    }

    public void setHideColumn(javax.swing.JTable table,int index){  // 隱藏欄位
         TableColumn tc= table.getColumnModel().getColumn(index);
         tc.setMaxWidth(0); tc.setPreferredWidth(0); tc.setMinWidth(0);
         tc.setWidth(0); table.getTableHeader().getColumnModel().getColumn(index).setMaxWidth(0);
         table.getTableHeader().getColumnModel().getColumn(index).setMinWidth(0);
    }

    public void reSetEnable() {
        this.setEnabled(true);
        this.setAlwaysOnTop(true);
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tab_Pharmacy = new javax.swing.JTable();
        btn_Close = new javax.swing.JButton();
        btn_Search = new javax.swing.JButton();
        dateComboBox = new cc.johnwu.date.DateComboBox();
        cob_Shift = new javax.swing.JComboBox();
        btn_Check = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("History Record");
        setAlwaysOnTop(true);

        tab_Pharmacy.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tab_Pharmacy.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tab_PharmacyMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tab_Pharmacy);

        btn_Close.setText("Close");
        btn_Close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CloseActionPerformed(evt);
            }
        });

        btn_Search.setText("Inquire ");
        btn_Search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_SearchActionPerformed(evt);
            }
        });

        cob_Shift.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Morning", "Noon", "Afternoon" }));

        btn_Check.setText("Check");
        btn_Check.setEnabled(false);
        btn_Check.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CheckActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 776, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(dateComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cob_Shift, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_Search, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btn_Check, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_Close, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(dateComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btn_Search)
                        .addComponent(cob_Shift, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(8, 8, 8)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 504, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_Close)
                    .addComponent(btn_Check))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_CloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CloseActionPerformed
        m_Pharmacy.reSetEnable();
        this.dispose() ;
    }//GEN-LAST:event_btn_CloseActionPerformed

    private void btn_SearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SearchActionPerformed
        ((DefaultTableModel)this.tab_Pharmacy.getModel()).setRowCount(0);
        this.btn_Check.setEnabled(false);
        ResultSet rs = null;
        String sql = "SELECT registration_info.p_no AS '"+paragraph.getLanguage(line, "NO")+"'," +
                 "concat(patients_info.firstname,'  ',patients_info.lastname) AS '"+paragraph.getLanguage(line, "NAME")+"', "+
                 "patients_info.birth AS '"+paragraph.getLanguage(line, "BIRTH")+"', " +
                 "patients_info.gender AS '"+paragraph.getLanguage(line, "GENDER")+"', "+
                 "patients_info.ps AS '"+paragraph.getLanguage(line, "PS")+"', "+
                 "policlinic.name AS '"+paragraph.getLanguage(line, "POLI")+"', " +
                 "concat(staff_info.firstname,'  ',staff_info.lastname) AS '"+paragraph.getLanguage(line, "DOCTOR")+"'', "+
                 "concat(patients_info.bloodtype,patients_info.rh_type) AS '"+paragraph.getLanguage(line, "BLOOD")+"'," +
                 "registration_info.guid, " +
                 "DATE_FORMAT(registration_info.touchtime,'%Y-%m-%d_%H:%i:%s') AS '"+paragraph.getLanguage(line, "TIME")+"' "+
             "FROM registration_info, patients_info, shift_table,staff_info ,poli_room, policlinic, outpatient_services ,medicine_stock "+
             "WHERE registration_info.shift_guid = shift_table.guid "+
                "AND shift_table.room_guid = poli_room.guid "+
                "AND poli_room.poli_guid = policlinic.guid "+
                "AND shift_table.s_id = staff_info.s_id "+
                "AND registration_info.p_no = patients_info.p_no "+
                "AND shift_table.shift_date = '"+dateComboBox.getValue()+"' "+
                "AND shift_table.shift = '"+(cob_Shift.getSelectedIndex()+1)+"' "+
                "AND registration_info.finish = 'F' "+
                "AND registration_info.guid = outpatient_services.reg_guid "+
                "AND medicine_stock.os_guid = outpatient_services.guid "+
                "GROUP BY registration_info.guid "+
                "ORDER BY registration_info.touchtime ";
        try{
            System.out.println(sql);
            rs = DBC.executeQuery(sql) ;
            rs.next();
            this.tab_Pharmacy.setModel(HISModel.getModel(rs,true));
            setCloumnWidth(tab_Pharmacy);
        } catch (SQLException e) {
            ErrorMessage.setData("Pharmacy", "Frm_PharmacyInquire" ,"btn_SearchActionPerformed(java.awt.event.ActionEvent evt)",
                    e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            Logger.getLogger(Frm_PharmacyInquire.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {DBC.closeConnection(rs);}
            catch (SQLException e){
                ErrorMessage.setData("Pharmacy", "Frm_PharmacyInquire" ,"btn_SearchActionPerformed(java.awt.event.ActionEvent evt) - DBC.closeConnection",
                    e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            }
        }
    }//GEN-LAST:event_btn_SearchActionPerformed

    private void tab_PharmacyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab_PharmacyMouseClicked
        if(this.tab_Pharmacy.getRowCount() > 0) {
            this.btn_Check.setEnabled(true);
        }
        if (evt.getClickCount() == 2) {
            btn_CheckActionPerformed(null);
        }
    }//GEN-LAST:event_tab_PharmacyMouseClicked

    private void btn_CheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CheckActionPerformed
        if (tab_Pharmacy.getSelectedRow() != -1 && tab_Pharmacy.getValueAt(tab_Pharmacy.getSelectedRow(), 1) != null) {
            String getDep = (String)this.tab_Pharmacy.getValueAt(tab_Pharmacy.getSelectedRow(), 1);
            String getDocter = (String)this.tab_Pharmacy.getValueAt(tab_Pharmacy.getSelectedRow(), 2);
            String getNo = (String)this.tab_Pharmacy.getValueAt(tab_Pharmacy.getSelectedRow(), 3);
            String getName = (String)this.tab_Pharmacy.getValueAt(tab_Pharmacy.getSelectedRow(), 4);
            String getBirth = (String)this.tab_Pharmacy.getValueAt(tab_Pharmacy.getSelectedRow(), 5);
            String getGender = (String)this.tab_Pharmacy.getValueAt(tab_Pharmacy.getSelectedRow(), 6);
            String getBlood = (String)this.tab_Pharmacy.getValueAt(tab_Pharmacy.getSelectedRow(), 7);
            String getps = (String)this.tab_Pharmacy.getValueAt(tab_Pharmacy.getSelectedRow(), 8);
            String getGuid = (String)this.tab_Pharmacy.getValueAt(tab_Pharmacy.getSelectedRow(),9);
            this.setAlwaysOnTop(false);
            new Frm_PharmacyInfo(this,getDep,getDocter,getNo,getName,getBirth,getGender,getBlood,getps,getGuid).setVisible(true);
            this.setEnabled(false);
        }
    }//GEN-LAST:event_btn_CheckActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Check;
    private javax.swing.JButton btn_Close;
    private javax.swing.JButton btn_Search;
    private javax.swing.JComboBox cob_Shift;
    private cc.johnwu.date.DateComboBox dateComboBox;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tab_Pharmacy;
    // End of variables declaration//GEN-END:variables

}
