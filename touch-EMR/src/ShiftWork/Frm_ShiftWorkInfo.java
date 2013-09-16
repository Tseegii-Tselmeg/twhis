package ShiftWork;

import ErrorMessage.StoredErrorMessage;
import cc.johnwu.sql.*;

import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import Multilingual.language;

public class Frm_ShiftWorkInfo extends javax.swing.JFrame {
    private final String DOCTOR_DEPARTMENT = "Doctor"; // 排班只限醫生群組
    private Calendar m_Cal = Calendar.getInstance();  //時間用
    private int m_ReLoad = 1;                         //避免combobox更改造成重複讀取資料庫 0為可讀取   1為已讀取
    private JComboBox m_ComboBox = new JComboBox();   //醫生姓名combobox
    private String[][] m_DataTab;                     //將資料存入陣列
    private Map<Object, Object> m_ShiftHashMap = new LinkedHashMap<Object, Object>();   // 儲存現在班表rs
    private Map<Object, Object> m_ChooseHashMap = new LinkedHashMap<Object, Object>();  // 儲存點選過的位置
    /*多國語言變數*/
    private language paragraph = new language();
    private String[] line = new String(paragraph.setlanguage("SHIFTWORKINFO")).split("\n") ;
    private String[] message = new String(paragraph.setlanguage("MESSAGE")).split("\n") ;
    /*輸出錯誤資訊變數*/
    StoredErrorMessage ErrorMessage = new StoredErrorMessage() ;

    public Frm_ShiftWorkInfo() {
        initComponents();
        initTabShift();
        initLanguage();
    }

    // 初始化 時間診別醫生combobox加入
    public void initTabShift() {
        this.setExtendedState(Frm_ShiftWorkInfo.MAXIMIZED_BOTH);  // 最大化
        this.tab_Shift.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  // tabble不可按住多選
        addWindowListener(new WindowAdapter() {                                // 畫面關閉原視窗enable
            @Override
            public void windowClosing(WindowEvent windowevent) {
                btn_CloseActionPerformed(null);
            }
        });
        mnit_Back.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0));
        this.setLocationRelativeTo(this);

        setPoliCob();
        //將年份加入
        Object yearNow = m_Cal.get(Calendar.YEAR);
        this.cob_Year.addItem(Integer.parseInt(yearNow.toString())-1);
        this.cob_Year.addItem(Integer.parseInt(yearNow.toString()));
        this.cob_Year.addItem(Integer.parseInt(yearNow.toString())+1);
        this.cob_Year.setSelectedIndex(1);  //指向目前年份
        //將月份加入
        this.cob_Month.addItem("01");
        this.cob_Month.addItem("02");
        this.cob_Month.addItem("03");
        this.cob_Month.addItem("04");
        this.cob_Month.addItem("05");
        this.cob_Month.addItem("06");
        this.cob_Month.addItem("07");
        this.cob_Month.addItem("08");
        this.cob_Month.addItem("09");
        this.cob_Month.addItem("10");
        this.cob_Month.addItem("11");
        this.cob_Month.addItem("12");
        Object monthNow = m_Cal.get(Calendar.MONTH);
        this.cob_Month.setSelectedIndex(Integer.parseInt(monthNow.toString()));  // 指向目前月份
        setRowCount();
        setShiftData();
        setDoctorCob();
    }
    /**多國語言翻譯*/
    private void initLanguage() {
        //this.lab_PolRoom.setText(paragraph.getLanguage(line, "POLROOM"));
        this.lab_Year.setText(paragraph.getLanguage(line, "YEAR"));
        this.lab_Month.setText(paragraph.getLanguage(line, "MONTH"));
        //this.lab_Policlinic.setText(paragraph.getLanguage(line, "POLICLINIC"));
        this.btn_Close.setText(paragraph.getLanguage(message, "CLOSE"));
        this.btn_Save.setText(paragraph.getLanguage(message, "SAVE"));
        this.btn_ReLoad.setText(paragraph.getLanguage(line, "RELOAD"));
        this.menu_File.setText(paragraph.getLanguage(message, "FILE"));
        this.mnit_Back.setText(paragraph.getLanguage(message, "CLOSE"));
        this.setTitle(paragraph.getLanguage(line, "TITLESHIFTWORK"));
    }
    /**是否變更儲存資料*/
    private void showSaveMessage(){
        if (this.btn_Save.isEnabled()){
            Object[] options = {paragraph.getLanguage(message , "YES"),paragraph.getLanguage(message , "NO")};
            int dialog = JOptionPane.showOptionDialog(
                            null,
                            paragraph.getLanguage(message , "DOYOUSAVEIT"),
                            paragraph.getLanguage(message , "MESSAGE"),
                            JOptionPane.YES_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options,
                            options[0]
                        );
            if (dialog == 0) {
                 this.btn_SaveActionPerformed(null);
            }
            
        }
        reLoad();
    }
    // 讀取診別名稱 放入combobok
    public void setPoliCob() {
        ResultSet rsPoliclinic = null;
        try {
             String sql = "SELECT name FROM policlinic";
             rsPoliclinic = DBC.executeQuery(sql);
             while (rsPoliclinic.next()) {
                cob_Policlinic.addItem(rsPoliclinic.getString("name"));
             }
             setPoliRoomCob();
        } catch (SQLException e) {
            ErrorMessage.setData("ShiftWork", "Frm_ShiftWorkInfo" ,"setPoliCob()",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        } finally {
            try {DBC.closeConnection(rsPoliclinic);
            } catch (SQLException e){
                ErrorMessage.setData("ShiftWork", "Frm_ShiftWorkInfo" ,"setPoliCob() - DBC.closeConnection",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            }
        }
    }

    // 讀取診室名稱  放入combobok
    public void setPoliRoomCob() {  
        cob_PolRoom.removeAllItems();
        ResultSet rsPolRoom = null;
        try {
            String sql = "SELECT poli_room.name " +
                         "FROM poli_room, policlinic " +
                         "WHERE policlinic.name = '" + cob_Policlinic.getSelectedItem() + "' " +
                         "AND poli_room.poli_guid = policlinic.guid ORDER BY poli_room.name";
            rsPolRoom = DBC.executeQuery(sql);
            while (rsPolRoom.next()) {
               cob_PolRoom.addItem(rsPolRoom.getString("name"));
            }
        } catch (SQLException e) {
            ErrorMessage.setData("ShiftWork", "Frm_ShiftWorkInfo" ,"setPoliRoomCob()",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        } finally {
            try {DBC.closeConnection(rsPolRoom);
            } catch (SQLException e){
                ErrorMessage.setData("ShiftWork", "Frm_ShiftWorkInfo" ,"setPoliRoomCob() - DBC.closeConnection",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            }
        }
    }

    // 將該診別醫生名單存出 存入combobox
    public void setDoctorCob() {
        m_ComboBox.removeAllItems();
        ResultSet rs = null;
        try {
            String sql = "SELECT concat(staff_info.firstname,'  ',staff_info.lastname) AS name, staff_info.s_id " +
                         "FROM staff_info, policlinic, department " +
                         "WHERE department.name = '"+DOCTOR_DEPARTMENT+"' " +
                         "AND policlinic.name = '"+this.cob_Policlinic.getSelectedItem() +"' " +
                         "AND staff_info.poli_guid = policlinic.guid " +
                         //"AND staff_info.dep_guid = '2199fe84-f496-102c-b00f-0013d35816f7' "+
                         "ORDER BY name";
     
            rs = DBC.executeQuery (sql);
            m_ComboBox.addItem("");
            while (rs.next()) {   //insert doctor name in combobox
                m_ComboBox.addItem(rs.getString("name"));
            }
            TableColumn weekColumn = this.tab_Shift.getColumnModel().getColumn(0);
            TableColumn dateColumn = this.tab_Shift.getColumnModel().getColumn(1);
            TableColumn morningColumn = this.tab_Shift.getColumnModel().getColumn(2);
            TableColumn noonColumn = this.tab_Shift.getColumnModel().getColumn(3);
            TableColumn nightColumn = this.tab_Shift.getColumnModel().getColumn(4);
            TableColumn allNightColumn = this.tab_Shift.getColumnModel().getColumn(5);
            weekColumn.setMaxWidth(35);
            dateColumn.setMaxWidth(30) ;
            morningColumn.setCellEditor(new DefaultCellEditor(m_ComboBox));
            noonColumn.setCellEditor(new DefaultCellEditor(m_ComboBox));
            nightColumn.setCellEditor(new DefaultCellEditor(m_ComboBox));
            allNightColumn.setCellEditor(new DefaultCellEditor(m_ComboBox));
            setHideColumn(tab_Shift, 5);
        } catch (SQLException e) {
            ErrorMessage.setData("ShiftWork", "Frm_ShiftWorkInfo" ,"setDoctorCob()",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        } finally {
            try {
                DBC.closeConnection(rs);
            } catch (SQLException e){
                ErrorMessage.setData("ShiftWork", "Frm_ShiftWorkInfo" ,"setDoctorCob() - DBC.closeConnection",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            }
        }
    }



    // set date coloumn data and set row count
    public void setRowCount() {  
      
       Calendar calRow = Calendar.getInstance();
       calRow.set(Integer.parseInt(this.cob_Year.getSelectedItem().toString()), Integer.parseInt((String) this.cob_Month.getSelectedItem())-1, 1);
       String day = null;
       m_DataTab = new String[calRow.getActualMaximum(Calendar.DAY_OF_MONTH)][tab_Shift.getColumnCount()+2];
       for (int date = 1 ; date < calRow.getActualMaximum(Calendar.DAY_OF_MONTH)+1; date++) {  // 加入日期
           if (date <= 9) {
                day = "0"+date;
           } else {
                day = ""+date;
           }
           setWeek(Integer.parseInt(this.cob_Year.getSelectedItem().toString()), Integer.parseInt((String) this.cob_Month.getSelectedItem())-1,date);
           m_DataTab[date-1][1] = day;
       }
    }

    /**星期幾*/
    private void setWeek(int year , int month , int date){
        Calendar calRow = Calendar.getInstance();
        calRow.set(year, month , date) ;
        switch(calRow.get(Calendar.DAY_OF_WEEK)){    //那一天星期幾
            case 1:
                m_DataTab[date-1][0] = "<html><font color='00AA00'>"+paragraph.getLanguage(line , "SUN")+"</font></html>" ;
                break ;
            case 2:
                m_DataTab[date-1][0] = paragraph.getLanguage(line , "MON") ;
                break ;
            case 3:
                m_DataTab[date-1][0] = paragraph.getLanguage(line , "TUE") ;
                break ;
            case 4:
                m_DataTab[date-1][0] = paragraph.getLanguage(line , "WED") ;
                break ;
            case 5:
                m_DataTab[date-1][0] = paragraph.getLanguage(line , "THU") ;
                break ;
            case 6:
                m_DataTab[date-1][0] = paragraph.getLanguage(line , "FRI") ;
                break ;
            case 7:
                m_DataTab[date-1][0] = "<html><font color='00AA00'>"+paragraph.getLanguage(line , "SAT")+"</font></html>" ;
                break ;
        }
    }

    // set shift data and set table grid editable
    public void setShiftData() {  
        String[] tableTitle = {
                                paragraph.getLanguage(line , "WEEK"),
                                paragraph.getLanguage(line , "DATE") ,
                                paragraph.getLanguage(line , "MORNING"),
                                paragraph.getLanguage(line , "NOON"),
                                paragraph.getLanguage(line , "NIGHT"), 
                                paragraph.getLanguage(line , "ALLNIGHT")
                                };  //表頭
        ResultSet rs = null;
        try {
            setRowCount();
            String sql = "SELECT shift_table.shift_date, concat(staff_info.firstname,'  ',staff_info.lastname) AS name, shift_table.shift, shift_table.guid "+
                         "FROM  shift_table, poli_room, staff_info, policlinic "+
                         "WHERE poli_room.name = '"+cob_PolRoom.getSelectedItem()+"' "+
                         "AND   policlinic.name = '"+cob_Policlinic.getSelectedItem()+"' "+
                         "AND   shift_table.shift_date LIKE '"+cob_Year.getSelectedItem()+"-"+cob_Month.getSelectedItem()+"-%' "+
                         "AND   shift_table.s_id = staff_info.s_id  "+
                         "AND   shift_table.room_guid = poli_room.guid "+
                         "AND   poli_room.poli_guid = policlinic.guid "+
                         "ORDER BY shift_table.shift_date ";
            rs = DBC.executeQuery(sql);

            while (rs.next()) {
                m_ShiftHashMap.put(rs.getString("shift_date") +"-"+ rs.getString("shift"), rs.getString("guid"));
                String date = rs.getString("shift_date");
                String[] dateDay = date.split("-");
                int rowDay = Integer.parseInt(dateDay[2])-1;
                int coloumShift = 0;
                if (rs.getString("shift").equals("1")) {
                    coloumShift = 2;
                } else if (rs.getString("shift").equals("2")) {
                    coloumShift = 3;
                } else if (rs.getString("shift").equals("3")) {
                    coloumShift = 4;
                } else if (rs.getString("shift").equals("4")) {
                    coloumShift = 5;
                }
                m_DataTab[rowDay][coloumShift] = rs.getString("name");
            }

            DefaultTableModel model = new DefaultTableModel(m_DataTab,tableTitle) {   //設定欄位可否編輯  已經過時間 欄位不可編輯
                @Override
                public boolean isCellEditable(int rowIndex,int columnIndex) {
                     if (columnIndex == 0 || columnIndex == 1) {
                        return false;
                     }

                     if (Integer.parseInt(cob_Year.getSelectedItem().toString()) > m_Cal.get(Calendar.YEAR)) {
                         return true;
                     } else if (Integer.parseInt(cob_Year.getSelectedItem().toString()) == m_Cal.get(Calendar.YEAR)) {

                         if (cob_Month.getSelectedIndex() > m_Cal.get(Calendar.MONTH)) {
                             return true;
                         } else if (cob_Month.getSelectedIndex() == m_Cal.get(Calendar.MONTH) && rowIndex > m_Cal.get(Calendar.DAY_OF_MONTH)-1) {
                             return true;
                         } else {
                             return false;
                         }
                     } else {
                         return false;
                     }
                }
             };
             this.tab_Shift.setModel(model);
             m_ReLoad = 0;

        } catch (SQLException e) {
            ErrorMessage.setData("ShiftWork", "Frm_ShiftWorkInfo" ,"setShiftData()",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        } finally {
            try {
                DBC.closeConnection(rs);
            } catch (SQLException e){
                ErrorMessage.setData("ShiftWork", "Frm_ShiftWorkInfo" ,"setShiftData() - DBC.closeConnection",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            }
        }
     }

    public void setHideColumn(javax.swing.JTable table,int index){  // 隱藏欄位
         TableColumn tc= table.getColumnModel().getColumn(index);
         tc.setMaxWidth(0); tc.setPreferredWidth(0); tc.setMinWidth(0);
         tc.setWidth(0); table.getTableHeader().getColumnModel().getColumn(index).setMaxWidth(0);
         table.getTableHeader().getColumnModel().getColumn(index).setMinWidth(0);
    }

    // 變數初始化
    public void reLoad(){
        m_ShiftHashMap.clear();
        m_ChooseHashMap.clear();
        this.tab_Shift.removeEditor();
        ((DefaultTableModel)tab_Shift.getModel()).setRowCount(0);
        this.btn_Save.setEnabled(false);
        this.btn_ReLoad.setEnabled(false);
        m_ReLoad = 1;
        setRowCount();
        setShiftData();
        setDoctorCob();
        m_ReLoad = 0;
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pan_Center = new javax.swing.JPanel();
        spn_Shift = new javax.swing.JScrollPane();
        tab_Shift = new javax.swing.JTable();
        pan_CenterUp = new javax.swing.JPanel();
        cob_Year = new javax.swing.JComboBox();
        lab_Year = new javax.swing.JLabel();
        lab_Month = new javax.swing.JLabel();
        cob_Month = new javax.swing.JComboBox();
        pan_under = new javax.swing.JPanel();
        btn_Save = new javax.swing.JButton();
        btn_Close = new javax.swing.JButton();
        btn_ReLoad = new javax.swing.JButton();
        pan_Up = new javax.swing.JPanel();
        lab_Policlinic = new javax.swing.JLabel();
        cob_Policlinic = new javax.swing.JComboBox();
        lab_PolRoom = new javax.swing.JLabel();
        cob_PolRoom = new javax.swing.JComboBox();
        mnb = new javax.swing.JMenuBar();
        menu_File = new javax.swing.JMenu();
        mnit_Back = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Shift Work");

        tab_Shift.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Day Shift", "Swing Shift", "Night Shift"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tab_Shift.setRowHeight(30);
        tab_Shift.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tab_ShiftMouseReleased(evt);
            }
        });
        spn_Shift.setViewportView(tab_Shift);
        tab_Shift.getColumnModel().getColumn(0).setResizable(false);
        tab_Shift.getColumnModel().getColumn(0).setPreferredWidth(20);
        tab_Shift.getColumnModel().getColumn(2).setResizable(false);

        cob_Year.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cob_YearItemStateChanged(evt);
            }
        });
        cob_Year.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cob_YearFocusGained(evt);
            }
        });

        lab_Year.setText("Year");

        lab_Month.setText("Month");

        cob_Month.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cob_MonthItemStateChanged(evt);
            }
        });
        cob_Month.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cob_MonthFocusGained(evt);
            }
        });

        javax.swing.GroupLayout pan_CenterUpLayout = new javax.swing.GroupLayout(pan_CenterUp);
        pan_CenterUp.setLayout(pan_CenterUpLayout);
        pan_CenterUpLayout.setHorizontalGroup(
            pan_CenterUpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_CenterUpLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lab_Year)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cob_Year, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lab_Month)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cob_Month, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(58, 58, 58))
        );
        pan_CenterUpLayout.setVerticalGroup(
            pan_CenterUpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_CenterUpLayout.createSequentialGroup()
                .addGroup(pan_CenterUpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab_Year)
                    .addComponent(cob_Year, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_Month)
                    .addComponent(cob_Month, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pan_CenterLayout = new javax.swing.GroupLayout(pan_Center);
        pan_Center.setLayout(pan_CenterLayout);
        pan_CenterLayout.setHorizontalGroup(
            pan_CenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_CenterLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan_CenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pan_CenterUp, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spn_Shift, javax.swing.GroupLayout.DEFAULT_SIZE, 752, Short.MAX_VALUE))
                .addContainerGap())
        );
        pan_CenterLayout.setVerticalGroup(
            pan_CenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_CenterLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(pan_CenterUp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spn_Shift, javax.swing.GroupLayout.DEFAULT_SIZE, 411, Short.MAX_VALUE)
                .addContainerGap())
        );

        btn_Save.setText("Save");
        btn_Save.setEnabled(false);
        btn_Save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_SaveActionPerformed(evt);
            }
        });

        btn_Close.setText("Close");
        btn_Close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CloseActionPerformed(evt);
            }
        });

        btn_ReLoad.setText("Re-read");
        btn_ReLoad.setEnabled(false);
        btn_ReLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ReLoadActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pan_underLayout = new javax.swing.GroupLayout(pan_under);
        pan_under.setLayout(pan_underLayout);
        pan_underLayout.setHorizontalGroup(
            pan_underLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_underLayout.createSequentialGroup()
                .addComponent(btn_ReLoad, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Save, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Close, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pan_underLayout.setVerticalGroup(
            pan_underLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_underLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btn_Close)
                .addComponent(btn_Save)
                .addComponent(btn_ReLoad))
        );

        lab_Policlinic.setText("Depart/Clinic ");

        cob_Policlinic.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cob_PoliclinicItemStateChanged(evt);
            }
        });
        cob_Policlinic.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cob_PoliclinicFocusGained(evt);
            }
        });

        lab_PolRoom.setText("Clinic");

        cob_PolRoom.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cob_PolRoomItemStateChanged(evt);
            }
        });
        cob_PolRoom.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cob_PolRoomFocusGained(evt);
            }
        });

        javax.swing.GroupLayout pan_UpLayout = new javax.swing.GroupLayout(pan_Up);
        pan_Up.setLayout(pan_UpLayout);
        pan_UpLayout.setHorizontalGroup(
            pan_UpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_UpLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lab_Policlinic)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cob_Policlinic, 0, 200, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lab_PolRoom)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cob_PolRoom, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(196, 196, 196))
        );
        pan_UpLayout.setVerticalGroup(
            pan_UpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_UpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(lab_Policlinic)
                .addComponent(cob_Policlinic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(lab_PolRoom)
                .addComponent(cob_PolRoom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

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
                    .addComponent(pan_Center, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pan_under, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pan_Up, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pan_Up, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pan_Center, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pan_under, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_CloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CloseActionPerformed
        showSaveMessage();
        new Main.Frm_Main().setVisible(true);
        this.dispose();
}//GEN-LAST:event_btn_CloseActionPerformed

    private void btn_SaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SaveActionPerformed
        String year = this.cob_Year.getSelectedItem().toString();
        String month = this.cob_Month.getSelectedItem().toString();
        Object day = null;
        String date = null;
        String poli = this.cob_Policlinic.getSelectedItem().toString(); // 診別
        String poliRoom = this.cob_PolRoom.getSelectedItem().toString(); // 診間名稱
        Object shift = null;    // 午別名稱
        String staff = null;    // 員工s_no
        int [] releaseRow ;     // 儲存點選過的班表行
        int [] releaseColoum;   // 儲存點選過的班表列
        releaseRow = new int [m_ChooseHashMap.size()];
        releaseColoum = new int [m_ChooseHashMap.size()];
        int index = 0;
        String sqlInsert = null;

        Collection collection = m_ChooseHashMap.values();
        Iterator iterator = collection.iterator();
        while (iterator.hasNext()) {  // 存在HashMap的值取出
            Object[] rowAndColimnSplit = iterator.next().toString().split(" ");
            releaseRow[index] = Integer.parseInt(rowAndColimnSplit[0].toString());
            releaseColoum[index] = Integer.parseInt(rowAndColimnSplit[1].toString());
            index++;
        }

        ResultSet rsGetStaffSid = null;
        ResultSet rsGetRoomGuid = null;
        try {
            for(int tip = 0; tip < m_ChooseHashMap.size(); tip++) {

                day = tab_Shift.getValueAt(releaseRow[tip], 1);
                date = year+"-"+month+"-"+day;
                shift = releaseColoum[tip];
                if (tab_Shift.getValueAt(releaseRow[tip], (releaseColoum[tip]+1)) != null) {
                    staff = tab_Shift.getValueAt(releaseRow[tip], (releaseColoum[tip]+1)).toString();
                } else {
                    staff = null;
                }
                
                String sqlGetStaffSid = "SELECT s_id FROM staff_info,department " +
                                        "WHERE concat(staff_info.firstname,'  ',staff_info.lastname) = '"+staff+"' " +
                                        "AND department.name = '"+DOCTOR_DEPARTMENT+"' " ;
                                      //  "AND dep_guid = '2199fe84-f496-102c-b00f-0013d35816f7'";
                String sqlGetRoomGuid = "SELECT poli_room.guid FROM poli_room, policlinic " +
                                        "WHERE poli_room.name = '"+poliRoom+"' " +
                                        "AND policlinic.name = '"+poli+"' " +
                                        "AND poli_room.poli_guid = policlinic.guid";
       
                rsGetStaffSid = DBC.executeQuery(sqlGetStaffSid);
                rsGetRoomGuid = DBC.executeQuery(sqlGetRoomGuid);
                rsGetStaffSid.next();
                rsGetRoomGuid.next();
                
                // 以 date+"-"+shift 為 key 判斷 m_ShiftHashMap 有沒有資料 如果沒有進行新增 如果有進行修改與刪除
                if(m_ShiftHashMap.get(date+"-"+shift) != null) {

                    // 修改的欄位原先有值 進行修改或刪除
                    if (tab_Shift.getValueAt(releaseRow[tip], (releaseColoum[tip]+1)) != "" &&
                        tab_Shift.getValueAt(releaseRow[tip], (releaseColoum[tip]+1)) != null ) {  // 如果選擇的值不是空   如果是空就是刪除班表
                        String sql = "UPDATE shift_table " +
                            "SET s_id = (SELECT staff_info.s_id FROM staff_info WHERE concat(staff_info.firstname,'  ',staff_info.lastname) = '"+staff+"') " +
                            "WHERE shift_table.room_guid = (SELECT poli_room.guid FROM poli_room,policlinic WHERE poli_room.name = '"+poliRoom+"' AND poli_room.poli_guid = policlinic.guid  AND policlinic.name = '"+cob_Policlinic.getSelectedItem()+"') " +
                            "AND shift_table.guid = '"+m_ShiftHashMap.get(date+"-"+shift)+"'";

                        DBC.executeUpdate (sql);
                    } else {
                        DBC.executeUpdate("DELETE FROM shift_table WHERE guid = '"+m_ShiftHashMap.get(date+"-"+shift)+"'");
                    }
                // 修改的欄位原先無值 進行新增
                } else if (tab_Shift.getValueAt(releaseRow[tip], (releaseColoum[tip]+1)) !=""
                        && tab_Shift.getValueAt(releaseRow[tip], (releaseColoum[tip]+1)) != null) { // 原本就是空  修改成空 不動作跳過

                    sqlInsert = "INSERT INTO shift_table(guid, s_id, shift_date, shift, room_guid) VALUES(uuid(), '"+rsGetStaffSid.getString("s_id")+"', '"+date+"', '"+shift+"', '" +rsGetRoomGuid.getString("guid")+"')";
                   
                    DBC.executeUpdate (sqlInsert);
                    
                }
            }
            JOptionPane.showMessageDialog(null, "Saved successfully.");
            reLoad();
        } catch (SQLException e) {
            Logger.getLogger(Frm_ShiftWorkInfo.class.getName()).log(Level.SEVERE, null, e);
            ErrorMessage.setData("ShiftWork", "Frm_ShiftWorkInfo" ,"btn_SaveActionPerformed()",
                e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        } finally {
            try {
                DBC.closeConnection(rsGetStaffSid);
                DBC.closeConnection(rsGetRoomGuid);
            } catch (SQLException ex) {
                Logger.getLogger(Frm_ShiftWorkInfo.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
}//GEN-LAST:event_btn_SaveActionPerformed

    private void tab_ShiftMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab_ShiftMouseReleased
      // save mouse click table place
      Object keyRowAndColumn = tab_Shift.getSelectedRow() + " " + (tab_Shift.getSelectedColumn()-1);
      // 判斷hashmap再相同位置是否已經有值  沒有值直接進行新增  如果有值取出後再新增
      if (tab_Shift.isCellEditable(tab_Shift.getSelectedRow(),tab_Shift.getSelectedColumn()) == true
       && tab_Shift.getSelectedColumn() != 0 && tab_Shift.getSelectedColumn() != 1) {
            if (this.cob_PolRoom.getSelectedItem()==null) {
                this.btn_Save.setEnabled(false);
                this.btn_ReLoad.setEnabled(false);
            } else {
                this.btn_Save.setEnabled(true);
                this.btn_ReLoad.setEnabled(true);
            }
            if (m_ChooseHashMap.get(keyRowAndColumn) == null) {
                m_ChooseHashMap.put(keyRowAndColumn, keyRowAndColumn);
            } else {
                m_ChooseHashMap.get(keyRowAndColumn);
                m_ChooseHashMap.put(keyRowAndColumn, keyRowAndColumn);
            }
       }

}//GEN-LAST:event_tab_ShiftMouseReleased

    private void btn_ReLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ReLoadActionPerformed
       reLoad();
       JOptionPane.showMessageDialog(null, paragraph.getLanguage(message , "LOADCOMPLETE"));
}//GEN-LAST:event_btn_ReLoadActionPerformed

    private void cob_PoliclinicItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cob_PoliclinicItemStateChanged
        if(evt.getStateChange() == java.awt.event.ItemEvent.SELECTED && m_ReLoad == 0) {
            setPoliRoomCob();
            reLoad();
        }        
    }//GEN-LAST:event_cob_PoliclinicItemStateChanged

    private void cob_PolRoomItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cob_PolRoomItemStateChanged
        if(evt.getStateChange() == java.awt.event.ItemEvent.SELECTED && m_ReLoad == 0) {
            reLoad();
        }
    }//GEN-LAST:event_cob_PolRoomItemStateChanged

    private void cob_YearItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cob_YearItemStateChanged
       if(evt.getStateChange() == java.awt.event.ItemEvent.SELECTED && m_ReLoad == 0) {
            reLoad();
        }
    }//GEN-LAST:event_cob_YearItemStateChanged

    private void cob_MonthItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cob_MonthItemStateChanged

        if(evt.getStateChange() == java.awt.event.ItemEvent.SELECTED && m_ReLoad == 0) {
            reLoad();
        }
    }//GEN-LAST:event_cob_MonthItemStateChanged

    private void mnit_BackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnit_BackActionPerformed
        btn_CloseActionPerformed(null);
}//GEN-LAST:event_mnit_BackActionPerformed

    private void cob_PoliclinicFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cob_PoliclinicFocusGained
        m_ReLoad = 0;
    }//GEN-LAST:event_cob_PoliclinicFocusGained

    private void cob_PolRoomFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cob_PolRoomFocusGained
        m_ReLoad = 0;
    }//GEN-LAST:event_cob_PolRoomFocusGained

    private void cob_MonthFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cob_MonthFocusGained
        m_ReLoad = 0;
    }//GEN-LAST:event_cob_MonthFocusGained

    private void cob_YearFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cob_YearFocusGained
        m_ReLoad = 0;
    }//GEN-LAST:event_cob_YearFocusGained

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Close;
    private javax.swing.JButton btn_ReLoad;
    private javax.swing.JButton btn_Save;
    private javax.swing.JComboBox cob_Month;
    private javax.swing.JComboBox cob_PolRoom;
    private javax.swing.JComboBox cob_Policlinic;
    private javax.swing.JComboBox cob_Year;
    private javax.swing.JLabel lab_Month;
    private javax.swing.JLabel lab_PolRoom;
    private javax.swing.JLabel lab_Policlinic;
    private javax.swing.JLabel lab_Year;
    private javax.swing.JMenu menu_File;
    private javax.swing.JMenuBar mnb;
    private javax.swing.JMenuItem mnit_Back;
    private javax.swing.JPanel pan_Center;
    private javax.swing.JPanel pan_CenterUp;
    private javax.swing.JPanel pan_Up;
    private javax.swing.JPanel pan_under;
    private javax.swing.JScrollPane spn_Shift;
    private javax.swing.JTable tab_Shift;
    // End of variables declaration//GEN-END:variables
}
