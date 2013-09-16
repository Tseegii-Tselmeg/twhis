package cc.johnwu.date;


import java.text.*;
import java.util.*;


public class DateComboBox extends javax.swing.JPanel{

    private Date m_Date;
    private int m_MinYear;
    private int m_MaxYear;
    private boolean m_AfterToday;
    private DateInterface m_Frame;
    private boolean initState;


    public DateComboBox() {
        m_Date = new Date();
        m_MinYear = 1900;
        m_MaxYear = 1900 + m_Date.getYear();
        m_AfterToday = false;
        initComponents();

        initState = true;
        initYear();
        initMonth();
        initDay();

        cob_Year.setSelectedItem(String.valueOf(1900+m_Date.getYear()));
        cob_Month.setSelectedItem(String.valueOf(m_Date.getMonth()+1));
        cob_Day.setSelectedIndex(m_Date.getDate()-1);
    }

    private void initYear(){
        //init ComboBox of Year
        cob_Year.removeAllItems();
        if(!m_AfterToday){
            for(int i = m_MaxYear; i >= m_MinYear; i--){
                cob_Year.addItem(""+i);
            }
        }else{
            for(int i = m_MinYear; i <= m_MaxYear; i++){
                cob_Year.addItem(""+i);
            }
        }
    }

    private void initMonth(){
        //init ComboBox of Month
        cob_Month.removeAllItems();
        for(int i = 1 ; i <= 12; i++)
        {
            if(i <= 9){
                 cob_Month.addItem("0"+i);
            }else{
                 cob_Month.addItem(""+i);
            }
        }
    }

    private void initDay(){
        //init ComboBox of Day
        cob_Day.removeAllItems();
        Calendar calRow = Calendar.getInstance();
        calRow.set(Integer.parseInt(cob_Year.getSelectedItem().toString()),
                   Integer.parseInt(cob_Month.getSelectedItem().toString())-1, 1);        
        for(int i = 1; i <= calRow.getActualMaximum(Calendar.DAY_OF_MONTH); i++)
        {
            if(i <= 9){
                 cob_Day.addItem("0"+i);
            }else{
                 cob_Day.addItem(""+i);
            }
        }
        cob_Year.setSelectedItem(""+(m_Date.getYear()+1900));
        cob_Month.setSelectedIndex(m_Date.getMonth());
        cob_Day.setSelectedItem(""+m_Date.getDate());
        initState = false;
    }

    private void setDayOfMonth(){
        int selectDay = cob_Day.getSelectedIndex();
        Calendar calRow = Calendar.getInstance();
        calRow.set(Integer.parseInt(cob_Year.getSelectedItem().toString()),
               Integer.parseInt(cob_Month.getSelectedItem().toString())-1, 1);
        calRow.getActualMaximum(Calendar.DAY_OF_MONTH);
        if(calRow.getActualMaximum(Calendar.DAY_OF_MONTH)!=cob_Day.getItemCount()){
            initState = true;
            int range = calRow.getActualMaximum(Calendar.DAY_OF_MONTH)-cob_Day.getItemCount();
            if(range>0){
                for(int i=0; i<range; i++){
                    cob_Day.addItem(cob_Day.getItemCount()+1);
                }
            }else{
                for(int i=0; i>range; i--){
                    cob_Day.removeItemAt(cob_Day.getItemCount()-1);
                }
            }
        }
        cob_Day.setSelectedIndex(selectDay<cob_Day.getItemCount()?selectDay:0);
        initState = false;
    }

    public void setAfterThisYear(int years){
        m_AfterToday = true;
        m_MinYear = 1900 + m_Date.getYear();
        m_MaxYear = m_MinYear + years;

        initState = true;
        initYear();
        initMonth();
        initDay();
    }

    public String getValue(){
        return cob_Year.getSelectedItem()+"-"+cob_Month.getSelectedItem()+"-"+cob_Day.getSelectedItem();
    }

    public void setValue(String date){
        if (date != null) {
            String[] regDate = date.split("-");
            if(regDate.length==3 ){
                try{
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    formatter.parse(regDate[0]+"-"+regDate[1]+"-"+regDate[2]);
                    cob_Year.setSelectedItem(regDate[0]);
                    cob_Month.setSelectedItem(regDate[1]);
                    cob_Day.setSelectedItem(regDate[2]);
                } catch (ParseException ex) {
                    System.out.println("DateComboBox:"+ex);
                }
            }
        } else {
            cob_Year.setSelectedIndex(9);
            cob_Month.setSelectedIndex(0);
            cob_Day.setSelectedIndex(0);
        }
        
    }

    private void onValueChanged(){
        if(m_Frame!=null)
            m_Frame.onDateChanged();
    }

    public void setParentFrame(DateInterface frame){
        m_Frame = frame;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cob_Year = new javax.swing.JComboBox();
        cob_Month = new javax.swing.JComboBox();
        cob_Day = new javax.swing.JComboBox();

        cob_Year.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0000" }));
        cob_Year.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cob_YearItemStateChanged(evt);
            }
        });

        cob_Month.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00" }));
        cob_Month.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cob_MonthItemStateChanged(evt);
            }
        });

        cob_Day.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00" }));
        cob_Day.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cob_DayItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(cob_Day, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cob_Month, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cob_Year, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(cob_Day, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(cob_Month, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(cob_Year, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cob_MonthItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cob_MonthItemStateChanged
        if(evt.getStateChange() == java.awt.event.ItemEvent.SELECTED && !initState){
            setDayOfMonth();
            onValueChanged();
        }
    }//GEN-LAST:event_cob_MonthItemStateChanged

    private void cob_YearItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cob_YearItemStateChanged
        if(evt.getStateChange() == java.awt.event.ItemEvent.SELECTED && !initState){
            setDayOfMonth();
            onValueChanged();
        }
    }//GEN-LAST:event_cob_YearItemStateChanged

    private void cob_DayItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cob_DayItemStateChanged
        if(evt.getStateChange() == java.awt.event.ItemEvent.SELECTED && !initState){
            onValueChanged();
        }
    }//GEN-LAST:event_cob_DayItemStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cob_Day;
    private javax.swing.JComboBox cob_Month;
    private javax.swing.JComboBox cob_Year;
    // End of variables declaration//GEN-END:variables

    @Override
    public void setEnabled(boolean flag){
        this.cob_Year.setEnabled(flag);
        this.cob_Month.setEnabled(flag);
        this.cob_Day.setEnabled(flag);
    }
}
