package Case;

import Case.TableTriStateCell.TriStateCellEditor;
import Case.TableTriStateCell.TriStateCellRenderer;
import Diagnosis.Frm_DiagnosisPrescription;
import ErrorMessage.StoredErrorMessage;
import Laboratory.Frm_LabDM;
import Common.*;
import cc.johnwu.date.DateInterface;
import cc.johnwu.date.DateMethod;
import cc.johnwu.login.UserInfo;
import cc.johnwu.sql.DBC;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class Frm_Case extends javax.swing.JFrame implements DateInterface{

    private String m_Pno;
    
    private DefaultTableModel m_PrescriptionModel;
    private int m_PrescriptionRowNo;   // Prescription table row number
    //---AUTO COMPLETE
    private String m_AutoTable;
    private String[] m_AutoColumn;   // AutoComplete 印出的欄位
    private String m_AutoColumnName; // 資料庫比對的Column name
    private JTextField m_AutoTxt = new JTextField();
    private JScrollPane m_AutoLocationSpane;
    private String[] m_AutoListValue;
    private int m_RsRowCount;
    private int m_Row;
    // focus 的 table 成為目前系統的暫存變數
    private JTable m_SelectTable = null;
    private DefaultTableModel m_SelectTableModel = null;
    private Map<Object, Object> m_PrescriptionHashMap = new LinkedHashMap<Object, Object>(); // 儲存Prescription看診資料 用來比對避免重複相同診斷
    private int m_SelectTableRowNo = 0;
    private int m_SelectTableNo;
    private int m_InsertRow;
    private int m_SelectTableAddRowLimitColumn; // TABLE 按下 ENTER 判斷是否允許增加新行的欄位
    private Map<Object, Object> m_SelectTableHashMap = new LinkedHashMap<Object, Object>();
    /*輸出錯誤資訊變數*/
    StoredErrorMessage ErrorMessage = new StoredErrorMessage();

    private boolean m_FinishState;     // T 完成  F 第一次
    private int m_PrintType;         // 列印項目 0:藥品列印 1:處置列印 2:X-Ray列印
    private String m_RegGuid;  // registration guid
    private int m_ModifyCount = 0;  // 修改次數
    private String m_From;
    /*是否使用套餐記錄*/
    private String m_PackageSet;
    private String m_PackageSetId;

    public Frm_Case(String p_no, String regGuid, boolean finishState, String From) {
        initComponents();
        
        addWindowListener(new WindowAdapter() {                               
            @Override
            public void windowClosing(WindowEvent windowevent) {
                setFrmClose();
            }
        });

        dia_RevisitTime.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowevent) {
              setCloseRevisitTime();
            }
        });
        if(From.equals("dia")){
            pan_Ass.setVisible(false);
            btn_Ddate_Save.setVisible(false);
            mnb.setVisible(false);
            //Assement
                com_family_history.setEnabled(false);
                com_self_care.setEnabled(false);
                com_dm_type.setEnabled(false);
                dateComboBox.setEnabled(false);
                txt_dm_typeo.setEnabled(false);
                com_oral_hypoglycemic.setEnabled(false);
                cbox_oral_syear.setEnabled(false);
                com_insulin.setEnabled(false);
                cbox_insulin_syear.setEnabled(false);
                com_gestation.setEnabled(false);
                spi_gestation_count.setEnabled(false);
                spi_abortions_count.setEnabled(false);
                che_smoke.setEnabled(false);
                spi_smoke_aday.setEnabled(false);
                che_drink.setEnabled(false);
                ftf_drink_aweek.setEnabled(false);
                com_education.setEnabled(false);
                com_sport.setEnabled(false);
                spi_bloodtest_aweek.setEnabled(false);
                spi_urine_aweek.setEnabled(false);
                txt_dbp.setEnabled(false);
                txt_sbp.setEnabled(false);
                txt_bmi.setEnabled(false);
                ftf_eye_lvision.setEnabled(false);
                ftf_eye_rvision.setEnabled(false);
                com_fundus_check.setEnabled(false);
                che_light_coagulation_L.setEnabled(false);
                che_light_coagulation_R.setEnabled(false);
                che_cataract_L.setEnabled(false);
                che_cataract_R.setEnabled(false);
                che_retinal_check_L.setEnabled(false);
                che_retinal_check_R.setEnabled(false);
                che_non_proliferative_retinopathy_L.setEnabled(false);
                che_non_proliferative_retinopathy_R.setEnabled(false);
                che_pre_proliferative_retinopathy_L.setEnabled(false);
                che_pre_proliferative_retinopathy_R.setEnabled(false);
                che_proliferative_retinopathy_L.setEnabled(false);
                che_proliferative_retinopathy_R.setEnabled(false);
                che_macular_degeneration_L.setEnabled(false);
                che_macular_degeneration_R.setEnabled(false);
                che_advanced_dm_eyedisease_L.setEnabled(false);
                che_advanced_dm_eyedisease_R.setEnabled(false);
                che_vibration_L.setEnabled(false);
                che_vibration_R.setEnabled(false);
                che_pulse_L.setEnabled(false);
                che_pulse_R.setEnabled(false);
                che_ulcer_L.setEnabled(false);
                che_ulcer_R.setEnabled(false);
                che_acupuncture_L.setEnabled(false);
                che_acupuncture_R.setEnabled(false);
                che_ulcer_cured_L.setEnabled(false);
                che_ulcer_cured_R.setEnabled(false);
                che_bypass_surgery_L.setEnabled(false);
                che_bypass_surgery_R.setEnabled(false);
            tab_HealthTeach.setEnabled(false);
            tab_Prescription.setEnabled(false);
            jTabbedPane1.setSelectedIndex(1);
        }else if(From.equals("medicine")){
            jPanel4.setVisible(false);
            btn_Ddate_Save.setVisible(false);
            mnb.setVisible(false);
            jTabbedPane1.remove(3);
            jTabbedPane1.remove(2);
            jTabbedPane1.remove(1);
            jTabbedPane1.remove(0);
            this.setTitle("Medicine Education");
        }
        m_From = From;
        m_Pno = p_no;
        m_RegGuid = regGuid;
        m_FinishState = finishState;
        showWhoUpdate(m_FinishState);     
        this.setExtendedState(Frm_Case.MAXIMIZED_BOTH);  // 最大化
        this.setLocationRelativeTo(this);
        init();
        initTable();
        if (finishState) setHistoryPre();
        txt_bmi.setText(Tools.getBmi(txt_Height.getText(), txt_Weight.getText()));
        setOverValue();
        this.dateComboBox.setParentFrame(this);
    }
    // 初始化

    private void init() {
        addWindowListener(new WindowAdapter() {  // 畫面關閉原視窗enable

            @Override
            public void windowClosing(WindowEvent windowevent) {
                //jButton11ActionPerformed(null);
            }
        });
        try {
            for (int i = 1901; i < 2060; i++) {
                cbox_oral_syear.addItem(i);
                cbox_insulin_syear.addItem(i);
            }

            String sql = "SELECT * FROM patients_info WHERE p_no = '" + this.m_Pno + "'";
            System.out.println(sql);
            // 取出病患基本資料
            ResultSet rs = DBC.executeQuery(sql);
            rs.next();
            m_Pno = rs.getString("p_no");
            this.lab_Pno.setText(rs.getString("p_no"));
            this.lab_Name.setText(rs.getString("firstname") + " " + rs.getString("lastname"));
            this.lab_Gender.setText(rs.getString("gender"));
            this.lab_Age.setText(DateMethod.getAgeWithMonth(rs.getDate("birth")));
            this.txt_Height.setText(rs.getString("height"));
            this.txt_Weight.setText(rs.getString("weight"));
            this.txt_AC.setText(Tools.getPrescriptionResult("BGAc", m_Pno));
            this.txt_PC.setText(Tools.getPrescriptionResult("BGPc", m_Pno));
            this.txt_ST.setText(Tools.getPrescriptionResult("St.", m_Pno));
            if (rs.getString("education") != null) this.com_edu.setSelectedIndex(rs.getInt("education"));
            
            //新病患第一次進入case management 所有資料帶入空值
            String sqlas = "SELECT family_history, self_care, dm_type, dm_typeo, dm_year, oral_hypoglycemic, oral_syear, insulin, insulin_syear, "+
                     " gestation, gestation_count, abortions_count, education, sport, fundus_check, gestation_count, "+
                     " abortions_count, smoke, drink, smoke_aday, drink_aweek, education, sport, bloodtest_aweek, urine_aweek, "+
                     " dbp, sbp, eye_lvision, eye_rvision, fundus_check, light_coagulation, cataract, retinal_check, non_proliferative_retinopathy, pre_proliferative_retinopathy ,proliferative_retinopathy, macular_degeneration, advanced_dm_eyedisease, vibration, pulse, ulcer, acupuncture, ulcer_cured, bypass_surgery, u_sid, udate"+
                     " FROM asscement , registration_info"+
                     " WHERE registration_info.guid = asscement.reg_guid"+
                     " AND registration_info.p_no = '"+m_Pno+"' ORDER BY udate DESC LIMIT 0, 1";
            System.out.println(sqlas);
            ResultSet as = DBC.executeQuery(sqlas);
            if (as.next()) {
                if(as.getString("udate") != null)
                {
                //讀取asscement
                    com_family_history.setSelectedIndex(Integer.parseInt(as.getString("family_history")));
                    com_self_care.setSelectedIndex(Integer.parseInt(as.getString("self_care")));
                    com_dm_type.setSelectedIndex(Integer.parseInt(as.getString("dm_type")));
                    if (com_dm_type.getSelectedIndex() == 3 ) txt_dm_typeo.setEnabled(true);
                    else {
                        txt_dm_typeo.setEnabled(false);
                        txt_dm_typeo.setText("");
                    }
                    txt_dm_typeo.setText(as.getString("dm_typeo"));
                    if (as.getString("dm_year") != null) dateComboBox.setValue (as.getString("dm_year"));
                    
                    com_oral_hypoglycemic.setSelectedIndex(Integer.parseInt(as.getString("oral_hypoglycemic")));


                    System.out.println(as.getString("oral_syear"));

                    cbox_oral_syear.setSelectedItem(as.getObject("oral_syear"));




                    com_insulin.setSelectedIndex(Integer.parseInt(as.getString("insulin")));
                    cbox_insulin_syear.setSelectedItem(as.getObject("insulin_syear"));
                    com_gestation.setSelectedIndex(Integer.parseInt(as.getString("gestation")));
                    spi_gestation_count.setValue(Integer.parseInt(as.getString("gestation_count")));
                    spi_abortions_count.setValue(Integer.parseInt(as.getString("abortions_count")));

                    if (as.getString("smoke") != null && as.getString("smoke").equals("0")) {
                        che_smoke.setSelected(true);
                    } else {
                    }

                    if (as.getString("drink") != null && as.getString("drink").equals("0")) {
                        che_drink.setSelected(true);
                    } else {
                    }

                    spi_smoke_aday.setValue(Integer.parseInt(as.getString("smoke_aday")));
                    ftf_drink_aweek.setText(as.getString("drink_aweek"));
                    com_education.setSelectedIndex(Integer.parseInt(as.getString("education")));
                    com_sport.setSelectedIndex(Integer.parseInt(as.getString("sport")));
                    spi_bloodtest_aweek.setValue(Integer.parseInt(as.getString("bloodtest_aweek")));
                    spi_urine_aweek.setValue(Integer.parseInt(as.getString("urine_aweek")));
                    txt_dbp.setText(as.getString("dbp"));
                    txt_sbp.setText(as.getString("sbp"));
                    ftf_eye_lvision.setText(as.getString("eye_lvision"));
                    ftf_eye_rvision.setText(as.getString("eye_rvision"));
                    com_fundus_check.setSelectedIndex(Integer.parseInt(as.getString("fundus_check")));

                    if (as.getString("light_coagulation").equals("3")) {
                        che_light_coagulation_L.setSelected(true);
                        che_light_coagulation_R.setSelected(true);
                    } else if (as.getString("light_coagulation").equals("2")) {
                        che_light_coagulation_R.setSelected(true);
                    } else if (as.getString("light_coagulation").equals("1")) {
                        che_light_coagulation_L.setSelected(true);
                    } else {
                    }

                    if (as.getString("cataract").equals("3")) {
                        che_cataract_L.setSelected(true);
                        che_cataract_R.setSelected(true);
                    } else if (as.getString("cataract").equals("2")) {
                        che_cataract_R.setSelected(true);
                    } else if (as.getString("cataract").equals("1")) {
                        che_cataract_L.setSelected(true);
                    } else {
                    }

                    if (as.getString("retinal_check").equals("3")) {
                        che_retinal_check_L.setSelected(true);
                        che_retinal_check_R.setSelected(true);
                    } else if (as.getString("retinal_check").equals("2")) {
                        che_retinal_check_R.setSelected(true);
                    } else if (as.getString("retinal_check").equals("1")) {
                        che_retinal_check_L.setSelected(true);
                    } else {
                    }

                    if (as.getString("non_proliferative_retinopathy").equals("3")) {
                        che_non_proliferative_retinopathy_L.setSelected(true);
                        che_non_proliferative_retinopathy_R.setSelected(true);
                    } else if (as.getString("non_proliferative_retinopathy").equals("2")) {
                        che_non_proliferative_retinopathy_R.setSelected(true);
                    } else if (as.getString("non_proliferative_retinopathy").equals("1")) {
                        che_non_proliferative_retinopathy_L.setSelected(true);
                    } else {
                    }

                    if (as.getString("pre_proliferative_retinopathy").equals("3")) {
                        che_pre_proliferative_retinopathy_L.setSelected(true);
                        che_pre_proliferative_retinopathy_R.setSelected(true);
                    } else if (as.getString("pre_proliferative_retinopathy").equals("2")) {
                        che_pre_proliferative_retinopathy_R.setSelected(true);
                    } else if (as.getString("pre_proliferative_retinopathy").equals("1")) {
                        che_pre_proliferative_retinopathy_L.setSelected(true);
                    } else {
                    }

                    if (as.getString("proliferative_retinopathy").equals("3")) {
                        che_proliferative_retinopathy_L.setSelected(true);
                        che_proliferative_retinopathy_R.setSelected(true);
                    } else if (as.getString("proliferative_retinopathy").equals("2")) {
                        che_proliferative_retinopathy_R.setSelected(true);
                    } else if (as.getString("proliferative_retinopathy").equals("1")) {
                        che_proliferative_retinopathy_L.setSelected(true);
                    } else {
                    }

                    if (as.getString("macular_degeneration").equals("3")) {
                        che_macular_degeneration_L.setSelected(true);
                        che_macular_degeneration_R.setSelected(true);
                    } else if (as.getString("macular_degeneration").equals("2")) {
                        che_macular_degeneration_R.setSelected(true);
                    } else if (as.getString("macular_degeneration").equals("1")) {
                        che_macular_degeneration_L.setSelected(true);
                    } else {
                    }

                    if (as.getString("advanced_dm_eyedisease").equals("3")) {
                        che_advanced_dm_eyedisease_L.setSelected(true);
                        che_advanced_dm_eyedisease_R.setSelected(true);
                    } else if (as.getString("advanced_dm_eyedisease").equals("2")) {
                        che_advanced_dm_eyedisease_R.setSelected(true);
                    } else if (as.getString("advanced_dm_eyedisease").equals("1")) {
                        che_advanced_dm_eyedisease_L.setSelected(true);
                    } else {
                    }

                    if (as.getString("vibration").equals("3")) {
                        che_vibration_L.setSelected(true);
                        che_vibration_R.setSelected(true);
                    } else if (as.getString("vibration").equals("2")) {
                        che_vibration_R.setSelected(true);
                    } else if (as.getString("vibration").equals("1")) {
                        che_vibration_L.setSelected(true);
                    } else {
                    }

                    if (as.getString("pulse").equals("3")) {
                        che_pulse_L.setSelected(true);
                        che_pulse_R.setSelected(true);
                    } else if (as.getString("pulse").equals("2")) {
                        che_pulse_R.setSelected(true);
                    } else if (as.getString("pulse").equals("1")) {
                        che_pulse_L.setSelected(true);
                    } else {
                    }

                    if (as.getString("ulcer").equals("3")) {
                        che_ulcer_L.setSelected(true);
                        che_ulcer_R.setSelected(true);
                    } else if (as.getString("ulcer").equals("2")) {
                        che_ulcer_R.setSelected(true);
                    } else if (as.getString("ulcer").equals("1")) {
                        che_ulcer_L.setSelected(true);
                    } else {
                    }

                    if (as.getString("acupuncture").equals("3")) {
                        che_acupuncture_L.setSelected(true);
                        che_acupuncture_R.setSelected(true);
                    } else if (as.getString("acupuncture").equals("2")) {
                        che_acupuncture_R.setSelected(true);
                    } else if (as.getString("acupuncture").equals("1")) {
                        che_acupuncture_L.setSelected(true);
                    } else {
                    }

                    if (as.getString("ulcer_cured").equals("3")) {
                        che_ulcer_cured_L.setSelected(true);
                        che_ulcer_cured_R.setSelected(true);
                    } else if (as.getString("ulcer_cured").equals("2")) {
                        che_ulcer_cured_R.setSelected(true);
                    } else if (as.getString("ulcer_cured").equals("1")) {
                        che_ulcer_cured_L.setSelected(true);
                    } else {
                    }

                    if (as.getString("bypass_surgery").equals("3")) {
                        che_bypass_surgery_L.setSelected(true);
                        che_bypass_surgery_R.setSelected(true);
                    } else if (as.getString("bypass_surgery").equals("2")) {
                        che_bypass_surgery_R.setSelected(true);
                    } else if (as.getString("bypass_surgery").equals("1")) {
                        che_bypass_surgery_L.setSelected(true);
                    } else {
                    }
                }
            } else {
                //所有資料帶入空值
            }
            String sqlcs = "SELECT dka, hhs, hypoglycemia, stroke, coronary_heart, paod, eye_lesions, neuropathy, kidney, waist, other, postural_hypotension, peripheral_neuropathy, angina, claudication, u_sid, udate "+
                     " FROM complication, registration_info "+
                     " WHERE registration_info.guid = complication.reg_guid "+
                     " AND registration_info.p_no = '"+m_Pno+"' ORDER BY udate DESC LIMIT 0, 1";
            System.out.println(sqlcs);
            ResultSet cs = DBC.executeQuery(sqlcs);
            if (cs.next()) {
                if(cs.getString("udate") != null)
                {
                //讀取complication
                    com_dka.setSelectedIndex(Integer.parseInt(cs.getString("dka")));
                    com_hhs.setSelectedIndex(Integer.parseInt(cs.getString("hhs")));
                    com_hypoglycemia.setSelectedIndex(Integer.parseInt(cs.getString("hypoglycemia")));

                    com_stroke.setSelectedIndex(Integer.parseInt(cs.getString("stroke")));
                    com_coronary_heart.setSelectedIndex(Integer.parseInt(cs.getString("coronary_heart")));
                    com_paod.setSelectedIndex(Integer.parseInt(cs.getString("paod")));

                    com_eye_lesions.setSelectedIndex(Integer.parseInt(cs.getString("eye_lesions")));
                    com_neuropathy.setSelectedIndex(Integer.parseInt(cs.getString("neuropathy")));
                    com_kidney.setSelectedIndex(Integer.parseInt(cs.getString("kidney")));

                    txt_waist.setText(cs.getString("waist"));
                    txt_other.setText(cs.getString("other"));
                    //txt_other.setSelectedIndex(Integer.parseInt(cs.getString("dka")));

                    if (cs.getString("postural_hypotension").equals("1")) {
                        rad_postural_hypotension_yes.setSelected(true);
                    } else if (cs.getString("postural_hypotension").equals("2")) {
                        rad_postural_hypotension_no.setSelected(true);
                    } else {
                    }

                    if (cs.getString("peripheral_neuropathy").equals("1")) {
                        rad_peripheral_neuropathy_yes.setSelected(true);
                    } else if (cs.getString("peripheral_neuropathy").equals("2")) {
                        rad_peripheral_neuropathy_no.setSelected(true);
                    } else {
                    }

                    if (cs.getString("angina").equals("1")) {
                        rad_angina_yes.setSelected(true);
                    } else if (cs.getString("angina").equals("2")) {
                        rad_angina_no.setSelected(true);
                    } else {
                    }

                    if (cs.getString("claudication").equals("1")) {
                        rad_claudication_yes.setSelected(true);
                    } else if (cs.getString("claudication").equals("2")) {
                        rad_claudication_no.setSelected(true);
                    } else {
                    }
                }
            } else {
                //所有資料帶入空值
            }
        } catch (SQLException ex) {
            Logger.getLogger(Frm_Case.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Save按鍵初始化
        btn_Ddate_Save.setEnabled(false);
        btn_AssSave.setEnabled(false);
        btn_ComSave.setEnabled(false);
        btn_ConSave.setEnabled(false);
        btn_PreSave.setEnabled(false);
        btn_DheSave.setEnabled(false);
    }

    private void setCloseRevisitTime() {
        this.setEnabled(true);
        dia_RevisitTime.dispose();
    }

    // 建立 TABLE
    public void initTable() {
        Object getModelAndRowNo[] = new Object[1];  // getModelAndRowNo[0] get model   getModelAndRowNo[1] get rowNo
        //-----tab_Prescription-------------------------------------------------
        String[] prescriptionTitle = {" ", "Code", "Item", "Body Part", "Type"};   // table表頭
        int[] prescriptionColumnEditable = {1, 3};     // 可編輯欄位
        getModelAndRowNo = TabTools.setTableEditColumn(m_PrescriptionModel, this.tab_Prescription, prescriptionTitle,
                prescriptionColumnEditable, m_PrescriptionRowNo);
        m_PrescriptionModel = (DefaultTableModel) getModelAndRowNo[0];
        m_PrescriptionRowNo = Integer.parseInt(getModelAndRowNo[1].toString());

        TableColumn prescriptionColumnNo = tab_Prescription.getColumnModel().getColumn(0);
        TableColumn prescriptionColumnCode = tab_Prescription.getColumnModel().getColumn(1);
        TableColumn prescriptionColumnName = tab_Prescription.getColumnModel().getColumn(2);
        TableColumn prescriptionColumnPlace = tab_Prescription.getColumnModel().getColumn(3);
        TableColumn prescriptionColumnType = tab_Prescription.getColumnModel().getColumn(4);
        prescriptionColumnNo.setMaxWidth(30);
        prescriptionColumnCode.setMaxWidth(80);
        prescriptionColumnName.setPreferredWidth(650);
        prescriptionColumnPlace.setPreferredWidth(80);
        prescriptionColumnCode.setCellEditor(new DefaultCellEditor(m_AutoTxt));     // textField加入table
        TabTools.setHideColumn(tab_Prescription, 3);

        // ----tab_HealthTeach  衛教確認---------------------
        Object[][] dataArray = null;
        ResultSet rs = null;

        try {
            Object[] title = {"", "Code", "Item", "ID", "Uesr", "Check", "Acceptance","guid"};
            String sql = "";

            sql = "SELECT health_teach_item.item, health_teach.guid, " +
                    "concat(staff_info.firstname,'  ',staff_info.lastname) AS user, " +
                    "health_teach_item.code, health_teach.s_id, health_teach.confirm, " +
                    "health_teach.acceptance FROM health_teach_item " +
                    "LEFT JOIN (health_teach LEFT JOIN staff_info ON health_teach.s_id = staff_info.s_id " +
                    " ) " +
                    "ON health_teach_item.code = health_teach.hti_code AND health_teach.reg_guid = '"+this.m_RegGuid+"'";


           System.out.println(sql);
            
            rs = DBC.executeQuery(sql);
            rs.last();
            dataArray = new Object[rs.getRow()][8];
            rs.beforeFirst();
            int i = 0;
           
                while (rs.next()) {
                    dataArray[i][1] = rs.getString("code");
                    dataArray[i][2] = rs.getString("item");
                    if (rs.getString("s_id") != null) {
                        dataArray[i][3] = rs.getString("s_id");
                    }

                    if (rs.getString("user") != null) {
                        dataArray[i][4] = rs.getString("user");
                    }

                    if (rs.getString("confirm") != null && rs.getString("confirm").equals("1")) {
                        dataArray[i][5] = true;
                    } else {
                        dataArray[i][5] = false;
                    }

                    if (rs.getString("acceptance") != null && !rs.getString("acceptance").equals("0")) {
                        if (rs.getString("acceptance").equals("1")) {
                            dataArray[i][6] = "Excellent";
                        } else if (rs.getString("acceptance").equals("2")) {
                            dataArray[i][6] = "Good";
                        } else if (rs.getString("acceptance").equals("3")) {
                            dataArray[i][6] = "<html><font color='FF0000'>Poor</font></html>";
                        }
                    }  else {
                         dataArray[i][6] = "";
                    }

                    if (rs.getString("guid") != null) {
                        dataArray[i][7] = rs.getString("guid");
                    }

                    dataArray[i][1] = rs.getString("code");
                    dataArray[i][2] = rs.getString("item");
                    dataArray[i][0] = i + 1;
                    i++;
                }

            DefaultTableModel TableModel = new DefaultTableModel(dataArray, title) {

                @Override
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    if (columnIndex == 5 || columnIndex == 6) {
                        return true;
                    } else {
                        return false;
                    }
                }
            };
            tab_HealthTeach.setModel(TableModel);
            TableColumn columnNumber = this.tab_HealthTeach.getColumnModel().getColumn(0);
            Common.TabTools.setHideColumn(tab_HealthTeach, 1);
            TableColumn columnName = this.tab_HealthTeach.getColumnModel().getColumn(2);
            Common.TabTools.setHideColumn(tab_HealthTeach, 3);
            TableColumn columnUser = this.tab_HealthTeach.getColumnModel().getColumn(4);
            TableColumn columnChoose = this.tab_HealthTeach.getColumnModel().getColumn(5);
            TableColumn columnAcceptance = this.tab_HealthTeach.getColumnModel().getColumn(6);
            Common.TabTools.setHideColumn(tab_HealthTeach, 7);
            //set column width
            columnNumber.setMaxWidth(30);
            columnName.setPreferredWidth(250);
            columnUser.setPreferredWidth(50);
            columnChoose.setMaxWidth(40);
            //columnAcceptance.setPreferredWidth(50);
            final JComboBox com_Acceptance = new JComboBox();

            com_Acceptance.addItem("");
            com_Acceptance.addItem("Excellent");
            com_Acceptance.addItem("Good");
            com_Acceptance.addItem("<html><font color='FF0000'>Poor</font></html>");
            columnAcceptance.setCellEditor(new DefaultCellEditor(com_Acceptance));

            columnChoose.setCellRenderer(new TriStateCellRenderer());
            columnChoose.setCellEditor(new TriStateCellEditor());
            tab_HealthTeach.setRowHeight(30);


        } catch (SQLException e) {
            Logger.getLogger(Frm_Case.class.getName()).log(Level.SEVERE, null, e);
            ErrorMessage.setData("Case", "Frm_Case", "initTable()",
                    e.toString().substring(e.toString().lastIndexOf(".") + 1, e.toString().length()));
        } finally {
            try {
                DBC.closeConnection(rs);
            } catch (SQLException e) {
                ErrorMessage.setData("Diagnosis", "Frm_DiagnosisTherapy", "setModel(String condition, String state) - DBC.closeConnection",
                        e.toString().substring(e.toString().lastIndexOf(".") + 1, e.toString().length()));
            }
        }

        // ---- tab_MedicineTeach   藥品衛教---------------------
        Object[][] dataArray_MT = null;
        ResultSet rs_MT = null;
        try {
            Object[] title_MT = {"", "Code", "Item", "s_id", "Uesr", "Check", "ps"};
            String sql_MT = "SELECT medicine_stock.m_code AS 'code', medicines.item AS 'item', medicine_stock.ps AS 'ps', medicine_stock.teach_complete AS 'teach_complete', concat(staff_info.firstname,'  ',staff_info.lastname) AS 'user', medicine_stock.s_id AS 'sid'"
                    + " FROM  medicine_stock LEFT JOIN staff_info ON staff_info.s_id = medicine_stock.s_id , medicines, registration_info, outpatient_services "
                    + " WHERE  medicines.code= medicine_stock.m_code "
                    + " AND medicine_stock.os_guid = outpatient_services.guid "
                    + " AND outpatient_services.reg_guid = registration_info.guid "
                    + " AND registration_info.p_no = '" + m_Pno + "' "
                    + " AND registration_info.guid = '" + m_RegGuid + "'";
            System.out.println(sql_MT);
            rs_MT = DBC.executeQuery(sql_MT);
            rs_MT.last();
            dataArray_MT = new Object[rs_MT.getRow()][7];
            rs_MT.beforeFirst();

            int i = 0;
            while (rs_MT.next()) {
                dataArray_MT[i][0] = i + 1;
                dataArray_MT[i][1] = rs_MT.getString("code");
                dataArray_MT[i][2] = rs_MT.getString("item");
                dataArray_MT[i][3] = rs_MT.getString("sid");

                if (rs_MT.getString("user") != null) {
                    dataArray_MT[i][4] = rs_MT.getString("user");
                }

                if (rs_MT.getString("teach_complete") != null && rs_MT.getString("teach_complete").equals("1")) {
                    dataArray_MT[i][5] = true;
                } else {
                    dataArray_MT[i][5] = false;
                }
                if(rs_MT.getString("ps") != null){
                    dataArray_MT[i][6] = rs_MT.getString("ps");
                }
                i++;
            }

            DefaultTableModel TableModel = new DefaultTableModel(dataArray_MT, title_MT) {

                @Override
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    if (columnIndex == 5 || columnIndex == 6) {
                        return true;
                    } else {
                        return false;
                    }
                }
            };
            tab_MedicineTeach.setModel(TableModel);
            TableColumn columnNumber = this.tab_MedicineTeach.getColumnModel().getColumn(0);
            Common.TabTools.setHideColumn(tab_MedicineTeach, 1);
            TableColumn columnName = this.tab_MedicineTeach.getColumnModel().getColumn(2);
            Common.TabTools.setHideColumn(tab_MedicineTeach, 3);
            TableColumn columnUser = this.tab_MedicineTeach.getColumnModel().getColumn(4);
            TableColumn columnChoose = this.tab_MedicineTeach.getColumnModel().getColumn(5);
            TableColumn columnPs = this.tab_MedicineTeach.getColumnModel().getColumn(6);
            //set column width
            columnNumber.setMaxWidth(30);
            columnName.setPreferredWidth(200);
            columnUser.setPreferredWidth(50);
            columnChoose.setMaxWidth(40);
            columnChoose.setCellRenderer(new TriStateCellRenderer());
            columnChoose.setCellEditor(new TriStateCellEditor());
            columnPs.setPreferredWidth(100);
            tab_MedicineTeach.setRowHeight(30);                   
        } catch (SQLException e) {
            Logger.getLogger(Frm_Case.class.getName()).log(Level.SEVERE, null, e);
            ErrorMessage.setData("Case", "Frm_Case", "initTable()",
                    e.toString().substring(e.toString().lastIndexOf(".") + 1, e.toString().length()));
        } finally {
            try {
                DBC.closeConnection(rs);
            } catch (SQLException e) {
                ErrorMessage.setData("Diagnosis", "Frm_DiagnosisTherapy", "setModel(String condition, String state) - DBC.closeConnection",
                        e.toString().substring(e.toString().lastIndexOf(".") + 1, e.toString().length()));
            }
        }
    }

    public void setFrmClose(){
        if(m_From.equals("dia") || m_From.equals("medicine")){
            //關閉此視窗
            this.dispose();
        }else{
            //開啟看診 視窗
            new WorkList.Frm_WorkList(0, "case").setVisible(true);
            //關閉此視窗
            this.dispose();
        }
    }


    private void setHistoryPre() {
        ResultSet rs = null;
        try {
            // 取出處置
            String sqlPrescription = "SELECT prescription.code, prescription_code.name, prescription_code.type, prescription.place " +
                    "FROM prescription, registration_info, prescription_code " +
                    "WHERE registration_info.guid = '" + m_RegGuid + "' " +
                    "AND prescription.case_guid = '" + m_RegGuid + "' " +
                    "AND prescription_code.code = prescription.code ";

            rs = DBC.executeQuery(sqlPrescription);
            int rowPrescription = 0;
            while (rs.next()) {
                tab_Prescription.setValueAt(rs.getString("code"), rowPrescription, 1);
                tab_Prescription.setValueAt(rs.getString("name"), rowPrescription, 2);
                tab_Prescription.setValueAt(rs.getString("place"), rowPrescription, 3);
                tab_Prescription.setValueAt(rs.getString("type"), rowPrescription, 4);
                if ((rowPrescription + 2) > tab_Prescription.getRowCount()) {
                    m_PrescriptionModel.addRow(new Vector());
                    tab_Prescription.setValueAt(++m_PrescriptionRowNo, tab_Prescription.getRowCount() - 1, 0);
                }
                rowPrescription++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Frm_Case.class.getName()).log(Level.SEVERE, null, ex);
        }  finally {
            try {
                DBC.closeConnection(rs);
            } catch (SQLException e) {

            }
        }
    }

    // 取得檢驗值
    private double getResult(String name) {
        try {
            ResultSet rs = null;
            String sql = "SELECT prescription.result AS result "
                    + "FROM prescription, patients_info, outpatient_services, registration_info, prescription_code  "
                    + "WHERE prescription.os_guid = outpatient_services.guid "
                    + "AND outpatient_services.reg_guid = registration_info.guid "
                    + "AND registration_info.p_no = patients_info.p_no "
                    + "AND prescription_code.name = '" + name + "' "
                    + "AND patients_info.p_no = '" + m_Pno + "'";
            rs = DBC.executeQuery(sql);
            if (rs.next()) {
                return rs.getDouble("result");
            } else {
                return -1;
            }

        } catch (SQLException ex) {
            Logger.getLogger(Frm_Case.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    private void setOverValue() {
       int check = 0;
       String tg = Tools.getPrescriptionResult("TG", m_Pno);
       String hdl = Tools.getPrescriptionResult("HDL", m_Pno);
       String bgac = Tools.getPrescriptionResult("BGAc", m_Pno);
       String sbp = txt_sbp.getText();
       String waist = txt_waist.getText();

        //----------------------------
        if (!tg.equals("")) {
            if (Double.parseDouble(tg) >= 150) {
                lab_tg.setText("Yes");
                check++;
            } else {
                lab_tg.setText("No");
            }
        } else {
            lab_tg.setText("");
        }
        //----------------------------
        if (!hdl.equals("")) {
            if (lab_Gender.getText().equals("M") && Double.parseDouble(hdl) < 40
             || lab_Gender.getText().equals("F") && Double.parseDouble(hdl) < 50) {
                lab_hdl.setText("Yes");
                check++;
            } else {
                lab_hdl.setText("No");
            }
        } else {
            lab_hdl.setText("");
        }
        //----------------------------
        if (!sbp.equals("")) {
            if (Double.parseDouble(sbp) >= 130) {
                lab_sbp.setText("Yes");
                check++;
            } else {
                lab_sbp.setText("No");
            }
        } else {
            lab_sbp.setText("");
        }
        //---------------------------
        if (!bgac.equals("")) {
            if (Double.parseDouble(bgac) >= 5.6) {
                lab_bgac.setText("Yes");
                check++;
            } else {
                lab_bgac.setText("No");
            }
        } else {
            lab_bgac.setText("");
        }
        //---------------------------
        txt_waist.setText(String.valueOf(waist));
        if (!bgac.equals("") && lab_Gender.getText() != null
            && waist != null && !waist.trim().equals("")) {
            if ((lab_Gender.getText().equals("M") && Double.parseDouble(waist) >= 90)
             || (lab_Gender.getText().equals("F") && Double.parseDouble(waist) >= 80)) {
                
                check++;
            }
        }

        if (check >= 3) {
            lab_dm.setText("Yes");
        } else {
            lab_dm.setText("No");
        }
  
    }

   

    // AUTOCOMPLET 顯示
    // 參數：span的point  table的point  span的寬度  span的高度  編號欄位的寬度  欄位高度  搜尋的資料庫名稱  顯示的資料庫欄位[]  作為條件比對的欄位名稱
    public void showAutoComplete(Point point, Point barPoint, int width, int height,
            int NoColumnWidth, int row, String DBtable, String[] DBColumn, String ColumnName) {

        String[] barPointArray = barPoint.toString().replace("java.awt.Point[x=", "").replace(",y=", "").replace("]", "").trim().split("-");
        int bar = 0;
        if (barPointArray.length == 2) {
            bar = Integer.parseInt(barPointArray[1]);
        }
        this.m_Row = row;
        this.m_AutoTable = DBtable;
        this.m_AutoColumn = DBColumn;
        this.m_AutoColumnName = ColumnName;
        // point取出 X Y
        int x = point.x;
        int y = point.y;
        y += (row + 1) * height + 15 - bar;
        x += NoColumnWidth;
        dia.setLocation(x, y);  // 設定位置
        dia.setSize(width - NoColumnWidth, Constant.AUTOCOMPLETE_HEIGHT); // 設定大小
    }
    // 焦點在於 AUTOCOMPLETE 的按鍵判斷 限制輸入值

    public boolean isKeyIn(String str) {
        Pattern pattern = Pattern.compile("[0-9,.,-,~,A-Z,a-z]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        } else {
            return true;
        }
    }

    // 顯示 AUTOCOMPLETE 前的按鍵判斷 迴避非值輸入
    public boolean isAllowKeyIn(KeyEvent evt) {
//        System.out.println("顯示 auto 前的判斷");
        if (m_SelectTable.getSelectedColumn() == 1
                && evt.getKeyCode() != KeyEvent.VK_UP
                && evt.getKeyCode() != KeyEvent.VK_DOWN
                && evt.getKeyCode() != KeyEvent.VK_RIGHT
                && evt.getKeyCode() != KeyEvent.VK_LEFT
                && evt.getKeyCode() != KeyEvent.VK_DELETE
                && evt.getKeyCode() != KeyEvent.VK_ENTER
                && evt.getKeyCode() != KeyEvent.VK_SPACE
                && evt.getKeyCode() != KeyEvent.VK_ESCAPE
                && evt.getKeyCode() != KeyEvent.VK_SUBTRACT
                && evt.getKeyCode() != KeyEvent.VK_ADD
                && evt.getKeyCode() != KeyEvent.VK_PAGE_DOWN
                && evt.getKeyCode() != KeyEvent.VK_PAGE_UP) {
            return true;
        }
        return false;
    }

    // 焦點在於 AUTOCOMPLETE 按鍵輸入 進行搜尋
    public void setTxt(String keyWord) {
//        System.out.println("按下 " +keyWord + " 搜尋");
        m_AutoTxt.setText(m_AutoTxt.getText() + keyWord);
        setAutoCompleteList(m_AutoTxt.getText());
    }

    // 焦點在於 AUTOCOMPLETE 按下 Backspace 刪除 m_AutoTxt 最後一個字元
    public void setBackspaceTxt() {
//        System.out.println("按下Backspace刪除最後一個字元 ");
        if (m_SelectTable.getValueAt(m_SelectTable.getSelectedRow(), 2) != null) {
//            System.out.println("移除hash map "+m_SelectTable.getValueAt(m_SelectTable.getSelectedRow(), 1 ));
            m_SelectTableHashMap.remove(m_SelectTable.getValueAt(m_SelectTable.getSelectedRow(), 1).toString().trim());
            for (int i = 2; i < m_SelectTable.getColumnCount(); i++) {
                m_SelectTable.setValueAt(null, m_SelectTable.getSelectedRow(), i);
            }
        }
        if (m_AutoTxt.getText().length() != 0) {
            m_AutoTxt.setText(m_AutoTxt.getText().substring(0, m_AutoTxt.getText().length() - 1));
            m_SelectTable.setValueAt(m_AutoTxt.getText(), m_SelectTable.getSelectedRow(), 1);
            setAutoCompleteList(m_AutoTxt.getText());
        }
    }

    // AUTOCOMPLET split陣列的value丟入各個tableColumn
    public void setTableValue(String[] value) {
//        System.out.println("split 傳入陣列值 ");
        switch (m_SelectTableNo) {
            case 2:  //tab_Prescription
                m_AutoTxt.setText(value[0]);
                tab_Prescription.setValueAt(value[0], tab_Prescription.getSelectedRow(), 1);
                tab_Prescription.setValueAt(value[1], tab_Prescription.getSelectedRow(), 2);
                tab_Prescription.setValueAt(value[2], tab_Prescription.getSelectedRow(), 4);
                break;

        }
    }

    // AUTOCOMPLETE 搜尋不到值  清空前一個遺留下的值
    public void setClearTableRow(int row) {
//        System.out.println("auto搜不到值  清空前一個遺留下的值 ROW 為 " + row);

        for (int i = 1; i < m_SelectTable.getColumnCount(); i++) {
            if (m_SelectTable.getSelectedRow() != -1 && m_SelectTable.getValueAt(m_SelectTable.getSelectedRow(), i) != null) {
                m_SelectTable.setValueAt(null, row, i);
            }
        }
    }

    // AUTOCOMPLETE 選單消失 移除單元格的編輯0
    public void setLostAutoCompleteEdit() {
        this.list_Menu.clearSelection();
        dia.setVisible(false);
//        System.out.println("選單消失 移除單元格的編輯");
        m_AutoTxt.setText(null);
        m_SelectTable.removeEditor();
    }

    // hsql 搜尋顯示出資料表  傳入要搜尋的資料表
    public void setAutoCompleteList(String condition) {
        System.out.println("搜尋條件 " + condition);
        if (condition.equals("")) {
            setClearTableRow(m_Row);
            btn_CloseActionPerformed(null);
        } else {
            String[] list = null;
            String sql = null;
           if (m_AutoTable.equals("prescription_code")) {
//                System.out.println("搜尋用 處置 語法");
                sql = "SELECT * FROM " + m_AutoTable + "  "
                        + "WHERE LOWER(" + m_AutoColumnName + ") LIKE LOWER('" + condition + "%') "
                        + "AND effective = 1  ORDER BY " + m_AutoColumnName + "";
            }
            int index = 0;
            ResultSet rs = null;
            try {
                rs = DBC.localExecuteQuery(sql);
                rs.last();
                m_RsRowCount = rs.getRow();
                setListheight();
                if (m_RsRowCount < Constant.AUTOCOMPLETE_SHOW_ROW) {
                    list = new String[m_RsRowCount];
                } else {
                    list = new String[Constant.AUTOCOMPLETE_SHOW_ROW + 1];
                }

                rs.beforeFirst();
                String str = "";
                while (rs.next()) {
                    if (rs.getRow() > Constant.AUTOCOMPLETE_SHOW_ROW + 1) {
                        break;
                    } else {
                        str = "";
                        for (int i = 0; i < m_AutoColumn.length; i++) {
                            if (m_AutoColumn.length > 2 && i == 1 && m_AutoTable.equals("medicines")) {
                                str += ("" + "       ");  // Medicine hide
                            } else {
                                str += (rs.getString(m_AutoColumn[i]).trim() + "       ");
                            }
                        }
                        list[index++] = str;
                    }
                }
                list_Menu.setListData(list);

                list_Menu.removeSelectionInterval(0, Constant.AUTOCOMPLETE_SHOW_ROW + 1);
            } catch (SQLException e) {
//                Logger.getLogger(Frm_DiagnosisInfo.class.getName()).log(Level.SEVERE, null, e);
                ErrorMessage.setData("Diagnosis", "Frm_DiagnosisInfo", "setAutoCompleteList(String condition)",
                        e.toString().substring(e.toString().lastIndexOf(".") + 1, e.toString().length()));
            } finally {
                try {
                    DBC.closeConnection(rs);
                } catch (SQLException e) {
                    ErrorMessage.setData("Diagnosis", "Frm_DiagnosisInfo", "setAutoCompleteList(String condition) - DBC.closeConnection(rs)",
                            e.toString().substring(e.toString().lastIndexOf(".") + 1, e.toString().length()));
                }
            }
        }
    }

    // AUTOCOMPLETE 值顯示到經過 split 轉為陣列丟入 TABLE
    public void getSplitValue() {
        if (list_Menu.getSelectedValue() != null) {
            m_AutoListValue = list_Menu.getSelectedValue().toString().split("       ");
            //回傳table  autoCompleteList表單值切割的陣列
            if (m_AutoListValue.length > 1) {
                setTableValue(m_AutoListValue);
            } else if (m_AutoListValue.length == 1) {
                setTableValue(m_AutoListValue);
            } else {
                m_AutoListValue = new String[5];
                for (int i = 1; i < m_AutoListValue.length; i++) {
                    m_AutoListValue[i] = null;
                }
                setTableValue(m_AutoListValue);
            }
        }
    }

   // 輸入值傳入 AUTOCOMPLETE 進行HSQL搜尋
    public void getAutoCompleteValue(String value) {
//        System.out.println("顯示auto 值傳到autocomplete "+ value);
        if(m_SelectTable.getSelectedColumn() == 1 && !m_AutoTxt.getText().equals("")) {
            TableColumn columnNo = m_SelectTable.getColumnModel().getColumn(0);
            showAutoComplete(m_AutoLocationSpane.getLocationOnScreen(), m_SelectTable.getLocation(), m_SelectTable.getWidth(),
                             m_SelectTable.getRowHeight(),columnNo.getWidth(), m_SelectTable.getSelectedRow(), m_AutoTable, m_AutoColumn, m_AutoColumnName);
            setAutoCompleteList(value);
            dia.setVisible(true);
        }
    }

    // TABLE 按鍵進行 ROW 增加 或清除值
    public int setRowValue(KeyEvent evt) {
        m_SelectTable.removeEditor();
        //上一行特定欄位不為空白才能增加行
        if (evt.getKeyCode() == KeyEvent.VK_ENTER
                && m_SelectTable.getValueAt(m_SelectTableRowNo - 1, m_SelectTableAddRowLimitColumn) != null
                && m_SelectTable.getValueAt(m_SelectTableRowNo - 1, m_SelectTableAddRowLimitColumn) != null
                && !m_SelectTable.getValueAt(m_SelectTableRowNo - 1, m_SelectTableAddRowLimitColumn).toString().trim().equals("")
                && !m_SelectTable.getValueAt(m_SelectTableRowNo - 1, m_SelectTableAddRowLimitColumn).toString().trim().equals("")) {
            m_SelectTableModel.addRow(new Vector());
            m_SelectTable.setValueAt(++m_SelectTableRowNo, m_SelectTable.getRowCount() - 1, 0);
        }
        // DELETE 鍵 清除資料
        if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
            if (m_SelectTable.getColumnCount() > 2 && m_SelectTable.getValueAt(m_SelectTable.getSelectedRow(), 2) != null) {
//                    System.out.println("移除 hashmap code = "+m_SelectTable.getValueAt(m_SelectTable.getSelectedRow(), 1).toString().trim());
                m_SelectTableHashMap.remove(m_SelectTable.getValueAt(m_SelectTable.getSelectedRow(), 1).toString().trim());
            }
            setClearRowValue();
            m_SelectTable.removeEditor();
        }
        return m_SelectTableRowNo;
    }

    // 判斷代碼是否有重複
    public boolean isCodeAtHashMap(Object code) {
        System.out.println(code);
        if (m_SelectTableHashMap.get(code) != null) { // 取出做判斷
            m_SelectTableHashMap.put(code.toString().trim(), code.toString().trim());  // 在放回
            if(m_SelectTable.getSelectedRow() != -1) {
                setClearRowValue();
            }
            return false;
        } else if (code != null && !code.toString().trim().equals("")) {
             m_SelectTableHashMap.put(code.toString().trim(), code.toString().trim());
             return true;
        }
        return true;
    }

    // 診斷代碼重複新增後再清除新增的該行
    public void setClearRowValue() {
        if (m_SelectTable.getSelectedRow() != -1) {
            for (int i = 1; i < m_SelectTable.getColumnCount(); i++) {
                m_SelectTable.setValueAt(null, m_SelectTable.getSelectedRow(), i);
            }
        } else {
            for (int i = 1; i < m_SelectTable.getColumnCount(); i++) {
                m_SelectTable.setValueAt(null, m_InsertRow, i);
            }
        }
    }

    // 設定 AUTOCOMPLETE 高度
    public void setListheight() {
        if (m_RsRowCount <= Constant.AUTOCOMPLETE_HEIGHT && m_RsRowCount != 0) {
            if (m_RsRowCount * 40 > Constant.AUTOCOMPLETE_HEIGHT) {
                dia.setSize(dia.getWidth(), Constant.AUTOCOMPLETE_HEIGHT);
            } else {
                dia.setSize(dia.getWidth(), m_RsRowCount * 40);
            }

        } else if (m_RsRowCount == 0) {
            dia.setSize(dia.getWidth(), 1);
            setClearTableRow(m_Row);
        } else {
            dia.setSize(dia.getWidth(), Constant.AUTOCOMPLETE_HEIGHT);
        }
    }

    // 移除所有TABLE 編輯狀態的欄位 與 取消光條選
    public void setAllRemoveEditAndSelection() {
        setRemoveEditAndSelection(this.tab_Prescription);
    }

    // 移除TABLE 編輯狀態的欄位 與 取消光條選
    public void setRemoveEditAndSelection(JTable table) {
        table.removeEditor();
        table.removeRowSelectionInterval(0, table.getRowCount() - 1);
    }

    
    public void showWhoUpdate(boolean m_finsh){
        try {
            if(m_finsh){
                ResultSet rs = DBC.executeQuery("SELECT * FROM case_manage , staff_info WHERE reg_guid = '" + m_RegGuid +  "'");
                while (rs.next()){
                    //跳出Lable"顯示資訊"
                    this.Lab_record.setText("Last Modified By : "+rs.getString("s_no")+" "+rs.getString("firstname")+" "+rs.getString("lastname")+"    Last Modified : "+rs.getString("finish_time"));
                }
            }else{
                //跳出Lable"尚無資訊"
                this.Lab_record.setText("No Modify the message");
            }
        } catch(SQLException ex){
            Logger.getLogger(Frm_Case.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // 套餐 V1&V3
    private void setV(String v) {

        tab_Prescription.removeRowSelectionInterval(0, tab_Prescription.getRowCount()-1);
        tab_PrescriptionFocusGained(null);

       int[] column = {1,2,4};
       Collection collection = Tools.getV(v).values();
       Iterator iterator = collection.iterator();
       while (iterator.hasNext()) {
            Object[] value = iterator.next().toString().split("  ");
            if (isCodeAtHashMap(value[0].toString().trim()))  setDiagnosisInfoTable(value, column);
        }
       btn_PreSave.setEnabled(true);
    }

    // 從其他 FRAME 增加一筆資料到指定 TABLE  指定 TABLE 欄位
    public void setDiagnosisInfoTable(Object[] value, int[] appointColumn) {
        btn_PreSave.setEnabled(true);
        for (int i = 0; i <= m_SelectTable.getRowCount(); i++) {

            if (i < m_SelectTable.getRowCount()
            && (m_SelectTable.getValueAt(i, 1) == null
             || m_SelectTable.getValueAt(i, 1).toString().trim().equals(""))) {

                for (int t = 0; t < value.length; t++) {
                    m_SelectTable.setValueAt(value[t], i, appointColumn[t]);
                }
                m_InsertRow = i;
                break;
            } else if (i == m_SelectTable.getRowCount()) {

                m_SelectTableModel.addRow(new Vector());
                m_SelectTable.setValueAt(m_SelectTableRowNo+1, m_SelectTableRowNo, 0);  //設定行號
                for (int t = 0; t < value.length; t++) {
                    m_SelectTable.setValueAt(value[t], m_SelectTableRowNo, appointColumn[t]);
                }
                m_SelectTableRowNo++;
                m_InsertRow = i;
                break;
            }
        }
    }

    // 儲存X-Ray照射部位與列印
    private void setPrint(boolean prescriptionState, boolean xrayState) {

        if (prescriptionState) {
            new PrintTools().DoPrint(12, this.m_RegGuid);
        }

//        if (xrayState) {
//            new PrintTools().DoPrint(4, this.m_RegGuid);
//        }
    }

        
        private void showEnterClinic () {
        try {
        String sql1="",sql2="";
        String getfinish = null;
        int getisd = 0;
        ResultSet rs = DBC.executeQuery("SELECT finish FROM registration_info WHERE guid = '" + m_RegGuid + "'");
        while(rs.next()){
            getfinish = rs.getString("finish");
        }
        ResultSet rs2 = DBC.executeQuery("SELECT isdiagnosis FROM case_manage WHERE guid = '" + m_RegGuid + "'");
        while(rs2.next()){
            getisd = Integer.parseInt(rs2.getString("isdiagnosis"));
        }
        if(m_FinishState){
                //跳出Message
                if(getfinish.equals("") || getfinish.equals("O")){
                    Object[] options = {"YES", "NO"};
                    int dialog = JOptionPane.showOptionDialog(new Frame(), "Modify patient need to visit the doctor ?", "Message", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                    String fin = "";
                    int isd = 0;
                    if (dialog == 0) {
                        //選擇 YES 時
                        if(getisd == 0){
                            isd = 1;
                        }else{
                            isd = 0;
                        }
                    } else {
                        //選擇 NO 時
                    }
                    sql1 = "UPDATE registration_info SET " + "finish  = '" + fin + "'," + "case_finish = 'F'" + "WHERE guid = '" + m_RegGuid + "' ";
                    DBC.executeUpdate(sql1);
                    m_ModifyCount += 1;
                    sql2 = "UPDATE case_manage SET " + "finish_time = NOW() ," + "modify_count = '" + m_ModifyCount + "'," + "isdiagnosis = '" + isd + "'" + "WHERE reg_guid = '" + m_RegGuid + "'";
                    DBC.executeUpdate(sql2);
                }
                //開啟看診 視窗
                new WorkList.Frm_WorkList(0, "case").setVisible(true);
                //關閉此視窗
                this.dispose();
            }else{
                //第一次
                //跳出Message
                Object[] options = {"YES", "NO"};
                int dialog = JOptionPane.showOptionDialog(
                    new Frame(),
                    "If the patient need to visit the doctor ?",
                    "Message",
                    JOptionPane.YES_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);
                String fin ="";
                int isd = 1;
                if (dialog == 0) {
                    //選擇 YES 時
                    isd = 1;
                } else {
                    //選擇 NO 時
                    fin = "O";
                    isd = 0;
                }
                sql1 = "UPDATE registration_info SET "+
                    "finish  = '"+ fin +"', " +
                    "case_finish = 'F', "+
                    "touchtime = RPAD((SELECT CASE " +
                            "WHEN MAX(B.touchtime) >= DATE_FORMAT(now(),'%Y%m%d%H%i%S') " +
                                "THEN concat(DATE_FORMAT(now(),'%Y%m%d%H%i%S'),COUNT(B.touchtime)) " +
                            "ELSE DATE_FORMAT(now(),'%Y%m%d%H%i%S') " +
                            "END touchtime " +
                        "FROM (SELECT touchtime FROM registration_info) AS B " +
                        "WHERE B.touchtime LIKE concat(DATE_FORMAT(now(),'%Y%m%d%H%i%S'),'%')),20,'000000') " +
                    "WHERE guid = '" + m_RegGuid + "'";
                DBC.executeUpdate(sql1);
                sql2 = "INSERT INTO case_manage (guid, reg_guid, finish_time, modify_count, s_no, isdiagnosis)"
                      + "VALUES (uuid() , '"+m_RegGuid+"', NOW() , "
                      + ""+m_ModifyCount+","+UserInfo.getUserNO()+",'"+isd+"')";
                DBC.executeUpdate(sql2);
                //開啟看診 視窗
                new WorkList.Frm_WorkList(0, "case").setVisible(true);
                //關閉此視窗
                this.dispose();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Frm_Case.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        btn_Close = new javax.swing.JButton();
        dia = new javax.swing.JDialog();
        span_ListMenu = new javax.swing.JScrollPane();
        list_Menu = new javax.swing.JList();
        dia_RevisitTime = new javax.swing.JDialog();
        jButton2 = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        txt_PackageId = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        txt_PackageType = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        txt_ComeBackDays = new javax.swing.JTextField();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        pan_Ass = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jPanel8 = new javax.swing.JPanel();
        com_family_history = new javax.swing.JComboBox();
        jLabel16 = new javax.swing.JLabel();
        JLable17 = new javax.swing.JLabel();
        com_self_care = new javax.swing.JComboBox();
        jLabel30 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        spi_bloodtest_aweek = new javax.swing.JSpinner();
        spi_urine_aweek = new javax.swing.JSpinner();
        jLabel51 = new javax.swing.JLabel();
        jPanel24 = new javax.swing.JPanel();
        jLabel158 = new javax.swing.JLabel();
        txt_dbp = new javax.swing.JTextField();
        jLabel159 = new javax.swing.JLabel();
        jLabel161 = new javax.swing.JLabel();
        jLabel162 = new javax.swing.JLabel();
        txt_sbp = new javax.swing.JTextField();
        jLabel191 = new javax.swing.JLabel();
        txt_bmi = new javax.swing.JTextField();
        jLabel192 = new javax.swing.JLabel();
        jLabel196 = new javax.swing.JLabel();
        jPanel25 = new javax.swing.JPanel();
        che_cataract_L = new javax.swing.JCheckBox();
        che_light_coagulation_L = new javax.swing.JCheckBox();
        che_light_coagulation_R = new javax.swing.JCheckBox();
        jLabel197 = new javax.swing.JLabel();
        jLabel198 = new javax.swing.JLabel();
        che_cataract_R = new javax.swing.JCheckBox();
        che_non_proliferative_retinopathy_R = new javax.swing.JCheckBox();
        che_pre_proliferative_retinopathy_R = new javax.swing.JCheckBox();
        che_non_proliferative_retinopathy_L = new javax.swing.JCheckBox();
        jLabel199 = new javax.swing.JLabel();
        jLabel200 = new javax.swing.JLabel();
        jLabel201 = new javax.swing.JLabel();
        che_retinal_check_R = new javax.swing.JCheckBox();
        che_proliferative_retinopathy_L = new javax.swing.JCheckBox();
        che_proliferative_retinopathy_R = new javax.swing.JCheckBox();
        che_macular_degeneration_L = new javax.swing.JCheckBox();
        jLabel202 = new javax.swing.JLabel();
        che_advanced_dm_eyedisease_R = new javax.swing.JCheckBox();
        che_advanced_dm_eyedisease_L = new javax.swing.JCheckBox();
        jLabel203 = new javax.swing.JLabel();
        che_retinal_check_L = new javax.swing.JCheckBox();
        che_pre_proliferative_retinopathy_L = new javax.swing.JCheckBox();
        jLabel204 = new javax.swing.JLabel();
        che_macular_degeneration_R = new javax.swing.JCheckBox();
        jPanel26 = new javax.swing.JPanel();
        jLabel64 = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();
        jLabel66 = new javax.swing.JLabel();
        jLabel72 = new javax.swing.JLabel();
        jLabel73 = new javax.swing.JLabel();
        jLabel74 = new javax.swing.JLabel();
        che_vibration_R = new javax.swing.JCheckBox();
        che_vibration_L = new javax.swing.JCheckBox();
        che_pulse_R = new javax.swing.JCheckBox();
        che_pulse_L = new javax.swing.JCheckBox();
        che_ulcer_R = new javax.swing.JCheckBox();
        che_ulcer_L = new javax.swing.JCheckBox();
        che_acupuncture_R = new javax.swing.JCheckBox();
        che_acupuncture_L = new javax.swing.JCheckBox();
        che_ulcer_cured_R = new javax.swing.JCheckBox();
        che_ulcer_cured_L = new javax.swing.JCheckBox();
        che_bypass_surgery_R = new javax.swing.JCheckBox();
        che_bypass_surgery_L = new javax.swing.JCheckBox();
        com_fundus_check = new javax.swing.JComboBox();
        jPanel7 = new javax.swing.JPanel();
        jLabel194 = new javax.swing.JLabel();
        jLabel195 = new javax.swing.JLabel();
        ftf_eye_lvision = new javax.swing.JFormattedTextField();
        ftf_eye_rvision = new javax.swing.JFormattedTextField();
        com_sport = new javax.swing.JComboBox();
        com_education = new javax.swing.JComboBox();
        jPanel1 = new javax.swing.JPanel();
        che_smoke = new javax.swing.JCheckBox();
        che_drink = new javax.swing.JCheckBox();
        spi_smoke_aday = new javax.swing.JSpinner();
        ftf_drink_aweek = new javax.swing.JFormattedTextField();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        com_gestation = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        spi_gestation_count = new javax.swing.JSpinner();
        jLabel10 = new javax.swing.JLabel();
        spi_abortions_count = new javax.swing.JSpinner();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        com_oral_hypoglycemic = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        com_insulin = new javax.swing.JComboBox();
        jLabel11 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        cbox_oral_syear = new javax.swing.JComboBox();
        cbox_insulin_syear = new javax.swing.JComboBox();
        jPanel6 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        com_dm_type = new javax.swing.JComboBox();
        jLabel19 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txt_dm_typeo = new javax.swing.JTextField();
        dateComboBox = new cc.johnwu.date.DateComboBox();
        btn_AssSave = new javax.swing.JButton();
        jPanel18 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jPanel15 = new javax.swing.JPanel();
        jLabel182 = new javax.swing.JLabel();
        com_dka = new javax.swing.JComboBox();
        jLabel183 = new javax.swing.JLabel();
        jLabel184 = new javax.swing.JLabel();
        com_stroke = new javax.swing.JComboBox();
        com_coronary_heart = new javax.swing.JComboBox();
        jLabel185 = new javax.swing.JLabel();
        com_neuropathy = new javax.swing.JComboBox();
        jLabel186 = new javax.swing.JLabel();
        jLabel187 = new javax.swing.JLabel();
        jLabel188 = new javax.swing.JLabel();
        jLabel189 = new javax.swing.JLabel();
        jLabel190 = new javax.swing.JLabel();
        txt_other = new javax.swing.JTextField();
        com_hhs = new javax.swing.JComboBox();
        jLabel47 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jLabel48 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        rad_postural_hypotension_yes = new javax.swing.JRadioButton();
        rad_postural_hypotension_no = new javax.swing.JRadioButton();
        rad_peripheral_neuropathy_yes = new javax.swing.JRadioButton();
        rad_peripheral_neuropathy_no = new javax.swing.JRadioButton();
        rad_angina_yes = new javax.swing.JRadioButton();
        rad_angina_no = new javax.swing.JRadioButton();
        rad_claudication_yes = new javax.swing.JRadioButton();
        rad_claudication_no = new javax.swing.JRadioButton();
        jLabel67 = new javax.swing.JLabel();
        jLabel68 = new javax.swing.JLabel();
        jLabel69 = new javax.swing.JLabel();
        com_paod = new javax.swing.JComboBox();
        com_eye_lesions = new javax.swing.JComboBox();
        jLabel59 = new javax.swing.JLabel();
        com_hypoglycemia = new javax.swing.JComboBox();
        com_kidney = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        lab_sbp = new javax.swing.JLabel();
        lab_hdl = new javax.swing.JLabel();
        lab_tg = new javax.swing.JLabel();
        lab_dm = new javax.swing.JLabel();
        txt_waist = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        lab_bgac = new javax.swing.JLabel();
        btn_ComSave = new javax.swing.JButton();
        Lab_record = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btn_ConSave = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tab_HealthTeach = new javax.swing.JTable();
        jPanel12 = new javax.swing.JPanel();
        pan_Prescription = new javax.swing.JPanel();
        span_Prescription = new javax.swing.JScrollPane();
        tab_Prescription = new javax.swing.JTable();
        btn_PreSave = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tab_MedicineTeach = new javax.swing.JTable();
        btn_DheSave = new javax.swing.JButton();
        btn_CaseClose = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jLabel60 = new javax.swing.JLabel();
        lab_Pno = new javax.swing.JLabel();
        btn_Ddate_Save = new javax.swing.JButton();
        jLabel85 = new javax.swing.JLabel();
        lab_Age = new javax.swing.JLabel();
        jLabel88 = new javax.swing.JLabel();
        lab_Gender = new javax.swing.JLabel();
        jLabel90 = new javax.swing.JLabel();
        lab_Name = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txt_PC = new javax.swing.JTextField();
        txt_AC = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        txt_ST = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel62 = new javax.swing.JLabel();
        txt_Height = new javax.swing.JTextField();
        jLabel63 = new javax.swing.JLabel();
        txt_Weight = new javax.swing.JTextField();
        jLabel71 = new javax.swing.JLabel();
        com_edu = new javax.swing.JComboBox();
        jLabel18 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        mnb = new javax.swing.JMenuBar();
        mn_Fiele = new javax.swing.JMenu();
        mnit_Lab = new javax.swing.JMenuItem();
        mnit_History = new javax.swing.JMenuItem();
        mnit_Close = new javax.swing.JMenuItem();
        menu_SetDM = new javax.swing.JMenu();
        jMenu1 = new javax.swing.JMenu();
        mnit_V1 = new javax.swing.JMenuItem();
        mnit_V2 = new javax.swing.JMenuItem();
        mnit_V3 = new javax.swing.JMenuItem();

        btn_Close.setText("Close");
        btn_Close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CloseActionPerformed(evt);
            }
        });

        dia.setAlwaysOnTop(true);
        dia.setModal(true);
        dia.setResizable(false);
        dia.setUndecorated(true);

        list_Menu.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        list_Menu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                list_MenuMouseClicked(evt);
            }
        });
        list_Menu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                list_MenuKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                list_MenuKeyReleased(evt);
            }
        });
        span_ListMenu.setViewportView(list_Menu);

        javax.swing.GroupLayout diaLayout = new javax.swing.GroupLayout(dia.getContentPane());
        dia.getContentPane().setLayout(diaLayout);
        diaLayout.setHorizontalGroup(
            diaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(span_ListMenu, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
        );
        diaLayout.setVerticalGroup(
            diaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(span_ListMenu, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
        );

        dia_RevisitTime.setTitle("Revisit");
        dia_RevisitTime.setMinimumSize(new java.awt.Dimension(480, 185));
        dia_RevisitTime.setResizable(false);

        jButton2.setText("OK");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jPanel13.setBackground(new java.awt.Color(246, 246, 246));

        txt_PackageId.setEditable(false);

        jLabel27.setText("Need to revisit the project:");

        jLabel28.setText("Package Codet:");

        jLabel29.setText("Next revisit will be in days is:");

        txt_PackageType.setEditable(false);

        jLabel31.setText("Days");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel29)
                    .addComponent(jLabel28)
                    .addComponent(jLabel27))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_PackageId, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                    .addComponent(txt_PackageType, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                        .addComponent(txt_ComeBackDays, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel31)))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(txt_PackageType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(txt_PackageId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(jLabel31)
                    .addComponent(txt_ComeBackDays, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout dia_RevisitTimeLayout = new javax.swing.GroupLayout(dia_RevisitTime.getContentPane());
        dia_RevisitTime.getContentPane().setLayout(dia_RevisitTimeLayout);
        dia_RevisitTimeLayout.setHorizontalGroup(
            dia_RevisitTimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dia_RevisitTimeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dia_RevisitTimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        dia_RevisitTimeLayout.setVerticalGroup(
            dia_RevisitTimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dia_RevisitTimeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addContainerGap())
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Case Management");

        jScrollPane4.setHorizontalScrollBar(null);

        jPanel8.setBackground(new java.awt.Color(204, 204, 204));
        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel8.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        com_family_history.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "　", "Yes", "No", "I don’t know", "Patrilineal", "Maternal", "Siblings", "Children" }));
        com_family_history.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ItemStateChanged(evt);
            }
        });

        jLabel16.setText("Family history：");

        JLable17.setText("Self-Care Ability：");

        com_self_care.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "　", "Completely independent ", "Needs some assistance", "Completely dependent" }));
        com_self_care.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ItemStateChanged(evt);
            }
        });

        jLabel30.setText("Have you had a health education：");

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder("Self-monitoring"));

        jLabel4.setText("The number of weekly blood tests:");

        jLabel6.setText("The number of weekly urine:");

        spi_bloodtest_aweek.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                StateChanged(evt);
            }
        });

        spi_urine_aweek.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                StateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spi_bloodtest_aweek, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spi_urine_aweek, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(1283, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spi_bloodtest_aweek, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spi_urine_aweek, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)))
        );

        jLabel51.setText("Sports：");

        jPanel24.setBorder(javax.swing.BorderFactory.createTitledBorder("Physical examination"));

        jLabel158.setText("DBP：");

        txt_dbp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                KeyReleased(evt);
            }
        });

        jLabel159.setText("mmHg");

        jLabel161.setText("SBP：");

        jLabel162.setText("mmHg");

        txt_sbp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                KeyReleased(evt);
            }
        });

        jLabel191.setText("BMI：");

        txt_bmi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                KeyReleased(evt);
            }
        });

        jLabel192.setText("kg/m^2");

        jLabel196.setText("Ophthalmoscopy：");

        jPanel25.setBorder(javax.swing.BorderFactory.createTitledBorder("Eye examination and lesions"));

        che_cataract_L.setText("Left eye");
        che_cataract_L.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ItemStateChanged(evt);
            }
        });

        che_light_coagulation_L.setText("Left eye");
        che_light_coagulation_L.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ItemStateChanged(evt);
            }
        });

        che_light_coagulation_R.setText("Right eye");
        che_light_coagulation_R.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ItemStateChanged(evt);
            }
        });

        jLabel197.setText("Advanced Diabetic Eye Disease:");

        jLabel198.setText("Cataract:");

        che_cataract_R.setText("Right eye");
        che_cataract_R.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ItemStateChanged(evt);
            }
        });

        che_non_proliferative_retinopathy_R.setText("Right eye");
        che_non_proliferative_retinopathy_R.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ItemStateChanged(evt);
            }
        });

        che_pre_proliferative_retinopathy_R.setText("Right eye");
        che_pre_proliferative_retinopathy_R.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ItemStateChanged(evt);
            }
        });

        che_non_proliferative_retinopathy_L.setText("Left eye");
        che_non_proliferative_retinopathy_L.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ItemStateChanged(evt);
            }
        });

        jLabel199.setText("Nonproliferative diabetic retinopathy:");

        jLabel200.setText("Retinal examination:");

        jLabel201.setText("Preproliferative diabetic retinopathy:");

        che_retinal_check_R.setText("Right eye");
        che_retinal_check_R.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ItemStateChanged(evt);
            }
        });

        che_proliferative_retinopathy_L.setText("Left eye");
        che_proliferative_retinopathy_L.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ItemStateChanged(evt);
            }
        });

        che_proliferative_retinopathy_R.setText("Right eye");
        che_proliferative_retinopathy_R.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ItemStateChanged(evt);
            }
        });
        che_proliferative_retinopathy_R.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                che_proliferative_retinopathy_RActionPerformed(evt);
            }
        });

        che_macular_degeneration_L.setText("Left eye");
        che_macular_degeneration_L.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ItemStateChanged(evt);
            }
        });

        jLabel202.setText("Proliferative diabetic retinopathy:");

        che_advanced_dm_eyedisease_R.setText("Right eye");
        che_advanced_dm_eyedisease_R.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ItemStateChanged(evt);
            }
        });

        che_advanced_dm_eyedisease_L.setText("Left eye");
        che_advanced_dm_eyedisease_L.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ItemStateChanged(evt);
            }
        });
        che_advanced_dm_eyedisease_L.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                che_advanced_dm_eyedisease_LActionPerformed(evt);
            }
        });

        jLabel203.setText("Macular degeneration:");

        che_retinal_check_L.setText("Left eye");
        che_retinal_check_L.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ItemStateChanged(evt);
            }
        });

        che_pre_proliferative_retinopathy_L.setText("Left eye");
        che_pre_proliferative_retinopathy_L.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ItemStateChanged(evt);
            }
        });

        jLabel204.setText("Photocoagulation therapy:");

        che_macular_degeneration_R.setText("Right eye");
        che_macular_degeneration_R.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel202, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel198, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel200, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel201, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel204, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel199, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE))
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel197)
                            .addComponent(jLabel203))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(che_macular_degeneration_L)
                    .addComponent(che_advanced_dm_eyedisease_L)
                    .addComponent(che_proliferative_retinopathy_L)
                    .addComponent(che_cataract_L)
                    .addComponent(che_light_coagulation_L)
                    .addComponent(che_retinal_check_L)
                    .addComponent(che_pre_proliferative_retinopathy_L)
                    .addComponent(che_non_proliferative_retinopathy_L))
                .addGap(18, 18, 18)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(che_proliferative_retinopathy_R)
                    .addComponent(che_advanced_dm_eyedisease_R)
                    .addComponent(che_pre_proliferative_retinopathy_R)
                    .addComponent(che_non_proliferative_retinopathy_R)
                    .addComponent(che_cataract_R)
                    .addComponent(che_retinal_check_R)
                    .addComponent(che_light_coagulation_R)
                    .addComponent(che_macular_degeneration_R))
                .addContainerGap())
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(che_light_coagulation_R)
                    .addComponent(che_light_coagulation_L)
                    .addComponent(jLabel204))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel25Layout.createSequentialGroup()
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(che_cataract_L)
                            .addComponent(jLabel198))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(che_retinal_check_L)
                            .addComponent(jLabel200)
                            .addComponent(che_retinal_check_R))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(che_non_proliferative_retinopathy_L)
                            .addComponent(jLabel199)
                            .addComponent(che_non_proliferative_retinopathy_R))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(che_pre_proliferative_retinopathy_L)
                            .addComponent(jLabel201)
                            .addComponent(che_pre_proliferative_retinopathy_R)))
                    .addComponent(che_cataract_R, javax.swing.GroupLayout.Alignment.LEADING))
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(che_proliferative_retinopathy_L)
                            .addComponent(jLabel202)))
                    .addComponent(che_proliferative_retinopathy_R, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(che_macular_degeneration_L)
                        .addComponent(che_macular_degeneration_R))
                    .addComponent(jLabel203))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(che_advanced_dm_eyedisease_L)
                    .addComponent(che_advanced_dm_eyedisease_R)
                    .addComponent(jLabel197))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jPanel26.setBorder(javax.swing.BorderFactory.createTitledBorder("Foot inspection and disease"));

        jLabel64.setText("Vibration Sensation:");

        jLabel65.setText("Dorsalis pedis pulse:");

        jLabel66.setText("Acute foot ulcers:");

        jLabel72.setText("Pinprick sensation:");

        jLabel73.setText("Healed ulcers:");

        jLabel74.setText("Coronary bypass/ Coronary angioplasty:");

        che_vibration_R.setText("Right");
        che_vibration_R.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ItemStateChanged(evt);
            }
        });
        che_vibration_R.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                che_vibration_RActionPerformed(evt);
            }
        });

        che_vibration_L.setText("Left");
        che_vibration_L.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ItemStateChanged(evt);
            }
        });

        che_pulse_R.setText("Right");
        che_pulse_R.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ItemStateChanged(evt);
            }
        });
        che_pulse_R.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                che_pulse_RActionPerformed(evt);
            }
        });

        che_pulse_L.setText("Left");
        che_pulse_L.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ItemStateChanged(evt);
            }
        });
        che_pulse_L.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                che_pulse_LActionPerformed(evt);
            }
        });

        che_ulcer_R.setText("Right");
        che_ulcer_R.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ItemStateChanged(evt);
            }
        });
        che_ulcer_R.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                che_ulcer_RActionPerformed(evt);
            }
        });

        che_ulcer_L.setText("Left");
        che_ulcer_L.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ItemStateChanged(evt);
            }
        });

        che_acupuncture_R.setText("Right");
        che_acupuncture_R.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ItemStateChanged(evt);
            }
        });
        che_acupuncture_R.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                che_acupuncture_RActionPerformed(evt);
            }
        });

        che_acupuncture_L.setText("Left");
        che_acupuncture_L.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ItemStateChanged(evt);
            }
        });

        che_ulcer_cured_R.setText("Right");
        che_ulcer_cured_R.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ItemStateChanged(evt);
            }
        });
        che_ulcer_cured_R.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                che_ulcer_cured_RActionPerformed(evt);
            }
        });

        che_ulcer_cured_L.setText("Left");
        che_ulcer_cured_L.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ItemStateChanged(evt);
            }
        });

        che_bypass_surgery_R.setText("Right");
        che_bypass_surgery_R.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ItemStateChanged(evt);
            }
        });
        che_bypass_surgery_R.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                che_bypass_surgery_RActionPerformed(evt);
            }
        });

        che_bypass_surgery_L.setText("Left");
        che_bypass_surgery_L.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel66)
                    .addComponent(jLabel65)
                    .addComponent(jLabel64)
                    .addComponent(jLabel72)
                    .addComponent(jLabel73)
                    .addComponent(jLabel74))
                .addGap(18, 18, 18)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(che_ulcer_cured_L)
                        .addComponent(che_acupuncture_L)
                        .addComponent(che_pulse_L)
                        .addComponent(che_ulcer_L)
                        .addComponent(che_vibration_L))
                    .addComponent(che_bypass_surgery_L))
                .addGap(18, 18, 18)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(che_bypass_surgery_R)
                    .addComponent(che_ulcer_cured_R)
                    .addComponent(che_vibration_R)
                    .addComponent(che_ulcer_R)
                    .addComponent(che_acupuncture_R)
                    .addComponent(che_pulse_R))
                .addContainerGap())
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(che_vibration_L)
                    .addComponent(che_vibration_R)
                    .addComponent(jLabel64))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(che_pulse_L)
                    .addComponent(che_pulse_R)
                    .addComponent(jLabel65))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(che_ulcer_L)
                    .addComponent(che_ulcer_R)
                    .addComponent(jLabel66))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(che_acupuncture_L)
                            .addComponent(che_acupuncture_R))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(che_ulcer_cured_L)
                            .addComponent(che_ulcer_cured_R)
                            .addComponent(jLabel73)))
                    .addComponent(jLabel72))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(che_bypass_surgery_L)
                    .addComponent(jLabel74)
                    .addComponent(che_bypass_surgery_R))
                .addContainerGap(78, Short.MAX_VALUE))
        );

        com_fundus_check.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " ", "Yes", "No" }));
        com_fundus_check.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ItemStateChanged(evt);
            }
        });

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Vision"));

        jLabel194.setText("Left eye：");

        jLabel195.setText("Right eye：");

        try {
            ftf_eye_lvision.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("#.#")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        ftf_eye_lvision.setFocusLostBehavior(javax.swing.JFormattedTextField.COMMIT);
        ftf_eye_lvision.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                KeyReleased(evt);
            }
        });

        try {
            ftf_eye_rvision.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("#.#")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        ftf_eye_rvision.setFocusLostBehavior(javax.swing.JFormattedTextField.COMMIT);
        ftf_eye_rvision.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel195)
                    .addComponent(jLabel194))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ftf_eye_lvision, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ftf_eye_rvision, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(253, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel194)
                    .addComponent(ftf_eye_lvision, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel195)
                    .addComponent(ftf_eye_rvision, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel161)
                            .addComponent(jLabel158)
                            .addComponent(jLabel191))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt_dbp, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_sbp, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_bmi, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel24Layout.createSequentialGroup()
                                .addComponent(jLabel159)
                                .addGap(41, 41, 41)
                                .addComponent(jLabel196)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(com_fundus_check, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel162)
                            .addComponent(jLabel192))
                        .addGap(54, 54, 54)
                        .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(633, Short.MAX_VALUE))
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel158)
                            .addComponent(txt_dbp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel159)
                            .addComponent(jLabel196)
                            .addComponent(com_fundus_check, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel161)
                            .addComponent(txt_sbp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel162))
                        .addGap(7, 7, 7)
                        .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_bmi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel191)
                            .addComponent(jLabel192)))
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        com_sport.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " ", "Yes", "No" }));
        com_sport.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ItemStateChanged(evt);
            }
        });

        com_education.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " ", "Yes", "No" }));
        com_education.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ItemStateChanged(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Status of risk factors"));

        che_smoke.setText("Smoking");
        che_smoke.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ItemStateChanged(evt);
            }
        });

        che_drink.setText("Drinking");
        che_drink.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ItemStateChanged(evt);
            }
        });

        spi_smoke_aday.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        spi_smoke_aday.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                StateChanged(evt);
            }
        });

        ftf_drink_aweek.setFocusLostBehavior(javax.swing.JFormattedTextField.COMMIT);
        ftf_drink_aweek.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                KeyReleased(evt);
            }
        });

        jLabel21.setText("A / Daily");

        jLabel22.setText("CC / Weekly");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(che_drink, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                    .addComponent(che_smoke, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(spi_smoke_aday, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel21))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(ftf_drink_aweek, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE)))
                .addContainerGap(1169, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(che_smoke)
                    .addComponent(spi_smoke_aday, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(che_drink)
                    .addComponent(ftf_drink_aweek, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Pregnant pregnant"));

        jLabel8.setText("Termination of the last 12 months：");

        com_gestation.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " ", "Yes", "No" }));
        com_gestation.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ItemStateChanged(evt);
            }
        });

        jLabel9.setText("Number of normal pregnancy：");

        spi_gestation_count.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        spi_gestation_count.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                StateChanged(evt);
            }
        });

        jLabel10.setText("Number of abortions：");

        spi_abortions_count.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                StateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8)
                    .addComponent(jLabel10)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(com_gestation, 0, 100, Short.MAX_VALUE)
                    .addComponent(spi_gestation_count)
                    .addComponent(spi_abortions_count))
                .addContainerGap(1204, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(com_gestation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(jLabel10))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(spi_gestation_count, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spi_abortions_count, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Treatment"));

        jLabel1.setText("Oral hypoglycemic：");

        com_oral_hypoglycemic.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "　", "Dietitian", "Nurse", "Other" }));
        com_oral_hypoglycemic.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ItemStateChanged(evt);
            }
        });

        jLabel5.setText("Insulin：");

        com_insulin.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " ", "Yes", "No" }));
        com_insulin.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ItemStateChanged(evt);
            }
        });
        com_insulin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                com_insulinActionPerformed(evt);
            }
        });

        jLabel11.setText("Use starting year：");

        jLabel3.setText("Use starting year：");

        cbox_oral_syear.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbox_oral_syearItemStateChanged(evt);
            }
        });

        cbox_insulin_syear.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbox_insulin_syearItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(com_oral_hypoglycemic, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(com_insulin, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cbox_insulin_syear, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbox_oral_syear, 0, 103, Short.MAX_VALUE))
                .addGap(1058, 1058, 1058))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(com_oral_hypoglycemic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(cbox_oral_syear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel5)
                    .addComponent(com_insulin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbox_insulin_syear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Diabetes"));

        jLabel17.setText("Diabetes Type：");

        com_dm_type.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "　", "Type I ", "Type II ", "Others" }));
        com_dm_type.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ItemStateChanged(evt);
            }
        });

        jLabel19.setText("Date of Discovery：");

        jLabel2.setText("Other：");

        txt_dm_typeo.setEnabled(false);
        txt_dm_typeo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(com_dm_type, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel19))
                    .addComponent(txt_dm_typeo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dateComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(1037, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel17)
                            .addComponent(com_dm_type, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel19))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txt_dm_typeo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(dateComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel24, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(com_family_history, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(JLable17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(com_self_care, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel30)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(com_education, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(63, 63, 63)
                        .addComponent(jLabel51)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(com_sport, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(com_family_history, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JLable17)
                    .addComponent(com_self_care, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(com_education, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(com_sport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel51))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane4.setViewportView(jPanel8);

        btn_AssSave.setText("Save");
        btn_AssSave.setEnabled(false);
        btn_AssSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_AssSaveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pan_AssLayout = new javax.swing.GroupLayout(pan_Ass);
        pan_Ass.setLayout(pan_AssLayout);
        pan_AssLayout.setHorizontalGroup(
            pan_AssLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan_AssLayout.createSequentialGroup()
                .addGroup(pan_AssLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pan_AssLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 791, Short.MAX_VALUE))
                    .addComponent(btn_AssSave, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        pan_AssLayout.setVerticalGroup(
            pan_AssLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan_AssLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_AssSave)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Assessment", pan_Ass);

        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel182.setText("DKA：");

        com_dka.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "　", "Yes", "No", "I don’t know" }));
        com_dka.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ItemStateChanged_C(evt);
            }
        });

        jLabel183.setText("Stroke：");

        jLabel184.setText("Coronary Heart Disease：");

        com_stroke.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "　", "Yes", "No", "I don’t know" }));
        com_stroke.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ItemStateChanged_C(evt);
            }
        });

        com_coronary_heart.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "　", "Yes", "No", "I don’t know" }));
        com_coronary_heart.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ItemStateChanged_C(evt);
            }
        });

        jLabel185.setText("Neuropathy：");

        com_neuropathy.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "　", "Yes", "No", "I don’t know" }));
        com_neuropathy.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ItemStateChanged_C(evt);
            }
        });

        jLabel186.setText("SBP：");

        jLabel187.setText("HDL：");

        jLabel188.setText("Kidney Diseases：");

        jLabel189.setText("TG > 150：");

        jLabel190.setText("Others：");

        txt_other.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                KeyReleased_C(evt);
            }
        });

        com_hhs.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "　", "Yes", "No", "I don’t know" }));
        com_hhs.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ItemStateChanged_C(evt);
            }
        });

        jLabel47.setText("Hypertension：");

        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder("Symptom"));

        jLabel48.setText("Postural hypotension：");

        jLabel49.setText("Peripheral neuropathy：");

        jLabel50.setText("Angina chest pain：");

        jLabel58.setText("Leg claudication：");

        buttonGroup1.add(rad_postural_hypotension_yes);
        rad_postural_hypotension_yes.setText("Yes");
        rad_postural_hypotension_yes.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ItemStateChanged_C(evt);
            }
        });
        rad_postural_hypotension_yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rad_postural_hypotension_yesActionPerformed(evt);
            }
        });

        buttonGroup1.add(rad_postural_hypotension_no);
        rad_postural_hypotension_no.setText("No");
        rad_postural_hypotension_no.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ItemStateChanged_C(evt);
            }
        });

        buttonGroup2.add(rad_peripheral_neuropathy_yes);
        rad_peripheral_neuropathy_yes.setText("Yes");
        rad_peripheral_neuropathy_yes.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ItemStateChanged_C(evt);
            }
        });

        buttonGroup2.add(rad_peripheral_neuropathy_no);
        rad_peripheral_neuropathy_no.setText("No");
        rad_peripheral_neuropathy_no.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ItemStateChanged_C(evt);
            }
        });

        buttonGroup3.add(rad_angina_yes);
        rad_angina_yes.setText("Yes");
        rad_angina_yes.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ItemStateChanged_C(evt);
            }
        });

        buttonGroup3.add(rad_angina_no);
        rad_angina_no.setText("No");
        rad_angina_no.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ItemStateChanged_C(evt);
            }
        });

        buttonGroup4.add(rad_claudication_yes);
        rad_claudication_yes.setText("Yes");
        rad_claudication_yes.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ItemStateChanged_C(evt);
            }
        });

        buttonGroup4.add(rad_claudication_no);
        rad_claudication_no.setText("No");
        rad_claudication_no.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ItemStateChanged_C(evt);
            }
        });

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel50, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel49, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel48, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel58, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(33, 33, 33)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rad_claudication_yes)
                    .addComponent(rad_angina_yes)
                    .addComponent(rad_peripheral_neuropathy_yes)
                    .addComponent(rad_postural_hypotension_yes))
                .addGap(18, 18, 18)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rad_angina_no)
                    .addComponent(rad_peripheral_neuropathy_no)
                    .addComponent(rad_postural_hypotension_no)
                    .addComponent(rad_claudication_no))
                .addContainerGap(573, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel48)
                    .addComponent(rad_postural_hypotension_yes)
                    .addComponent(rad_postural_hypotension_no))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel49)
                    .addComponent(rad_peripheral_neuropathy_no)
                    .addComponent(rad_peripheral_neuropathy_yes))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel50)
                    .addComponent(rad_angina_no)
                    .addComponent(rad_angina_yes))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel58)
                    .addComponent(rad_claudication_no)
                    .addComponent(rad_claudication_yes))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jLabel67.setText("HHHK：");

        jLabel68.setText("PAOD：");

        jLabel69.setText("Eye lesions：");

        com_paod.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "　", "Yes", "No", "I don’t know" }));
        com_paod.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ItemStateChanged_C(evt);
            }
        });

        com_eye_lesions.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "　", "Yes", "No", "I don’t know" }));
        com_eye_lesions.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ItemStateChanged_C(evt);
            }
        });

        jLabel59.setText("Waist：");

        com_hypoglycemia.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "　", "Yes", "No", "I don’t know" }));
        com_hypoglycemia.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ItemStateChanged_C(evt);
            }
        });

        com_kidney.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "　", "Yes", "No", "I don’t know" }));
        com_kidney.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ItemStateChanged_C(evt);
            }
        });

        jLabel12.setText("DM：");

        lab_sbp.setText("No");

        lab_hdl.setText("No");

        lab_tg.setText("No");

        lab_dm.setText("No");

        txt_waist.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                KeyReleased_C(evt);
            }
        });

        jLabel13.setText("cm");

        jLabel14.setText("BGAc：");

        lab_bgac.setText("No");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel16, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel187)
                            .addComponent(jLabel186)
                            .addComponent(jLabel67)
                            .addComponent(jLabel47)
                            .addComponent(jLabel182)
                            .addComponent(jLabel189))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(com_dka, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(com_hhs, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(com_hypoglycemia, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lab_tg)
                            .addComponent(lab_hdl)
                            .addComponent(lab_sbp))
                        .addGap(30, 30, 30)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel183)
                            .addComponent(jLabel12)
                            .addComponent(jLabel14)
                            .addComponent(jLabel59)
                            .addComponent(jLabel184)
                            .addComponent(jLabel68))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(com_stroke, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lab_bgac)
                            .addGroup(jPanel15Layout.createSequentialGroup()
                                .addComponent(txt_waist, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel13))
                            .addComponent(lab_dm)
                            .addComponent(com_coronary_heart, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(com_paod, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(70, 70, 70)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel185, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel190, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel69, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel188, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_other)
                            .addComponent(com_eye_lesions, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(com_kidney, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(com_neuropathy, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel15Layout.createSequentialGroup()
                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(com_dka, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel182))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(com_eye_lesions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel69))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(com_hhs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel67)
                            .addComponent(jLabel184)
                            .addComponent(com_coronary_heart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel185)
                            .addComponent(com_neuropathy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel183)
                        .addComponent(com_stroke, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(com_hypoglycemia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel47)
                        .addComponent(com_paod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel68))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel188)
                            .addComponent(com_kidney, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(19, 19, 19)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel186)
                            .addComponent(lab_sbp)
                            .addComponent(jLabel59)
                            .addComponent(txt_waist, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13)
                            .addComponent(jLabel190)
                            .addComponent(txt_other, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lab_hdl)
                        .addComponent(jLabel14)
                        .addComponent(lab_bgac))
                    .addComponent(jLabel187))
                .addGap(8, 8, 8)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel189)
                    .addComponent(lab_tg)
                    .addComponent(jLabel12)
                    .addComponent(lab_dm))
                .addGap(15, 15, 15)
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane6.setViewportView(jPanel15);

        btn_ComSave.setText("Save");
        btn_ComSave.setEnabled(false);
        btn_ComSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ComSaveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 791, Short.MAX_VALUE)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addComponent(Lab_record, javax.swing.GroupLayout.DEFAULT_SIZE, 701, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_ComSave, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btn_ComSave)
                    .addComponent(Lab_record, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Complication", jPanel18);

        btn_ConSave.setText("Save");
        btn_ConSave.setEnabled(false);
        btn_ConSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ConSaveActionPerformed(evt);
            }
        });

        tab_HealthTeach.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {}
            },
            new String [] {

            }
        ));
        tab_HealthTeach.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tab_HealthTeachMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tab_HealthTeach);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 791, Short.MAX_VALUE)
                    .addComponent(btn_ConSave, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_ConSave)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Confirm the completion of health education", jPanel2);

        pan_Prescription.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        pan_Prescription.setPreferredSize(new java.awt.Dimension(813, 173));

        tab_Prescription.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tab_Prescription.setRowHeight(25);
        tab_Prescription.getTableHeader().setReorderingAllowed(false);
        tab_Prescription.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tab_PrescriptionMouseClicked(evt);
            }
        });
        tab_Prescription.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tab_PrescriptionFocusGained(evt);
            }
        });
        tab_Prescription.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tab_PrescriptionKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tab_PrescriptionKeyReleased(evt);
            }
        });
        span_Prescription.setViewportView(tab_Prescription);

        javax.swing.GroupLayout pan_PrescriptionLayout = new javax.swing.GroupLayout(pan_Prescription);
        pan_Prescription.setLayout(pan_PrescriptionLayout);
        pan_PrescriptionLayout.setHorizontalGroup(
            pan_PrescriptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(span_Prescription, javax.swing.GroupLayout.DEFAULT_SIZE, 779, Short.MAX_VALUE)
        );
        pan_PrescriptionLayout.setVerticalGroup(
            pan_PrescriptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(span_Prescription, javax.swing.GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE)
        );

        btn_PreSave.setText("Save");
        btn_PreSave.setEnabled(false);
        btn_PreSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_PreSaveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pan_Prescription, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 791, Short.MAX_VALUE)
                    .addComponent(btn_PreSave, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pan_Prescription, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_PreSave)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Laboratory", jPanel12);

        tab_MedicineTeach.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tab_MedicineTeach.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tab_MedicineTeachMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tab_MedicineTeach);

        btn_DheSave.setText("Save");
        btn_DheSave.setEnabled(false);
        btn_DheSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_DheSaveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 791, Short.MAX_VALUE)
                    .addComponent(btn_DheSave, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_DheSave)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Medicine Education", jPanel9);

        btn_CaseClose.setText("Send");
        btn_CaseClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CaseCloseActionPerformed(evt);
            }
        });

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder("Demographic data"));

        jLabel60.setText("Patient No：");

        lab_Pno.setText("0");

        btn_Ddate_Save.setText("Save");
        btn_Ddate_Save.setEnabled(false);
        btn_Ddate_Save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Ddate_SaveActionPerformed(evt);
            }
        });

        jLabel85.setText("Age：");

        lab_Age.setText("30");

        jLabel88.setText("Gender：");

        lab_Gender.setText("M");

        jLabel90.setText("Name：");

        lab_Name.setText("Steven Chung");

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("One Touch"));

        jLabel23.setText("BGPc： ");

        jLabel15.setText("BGAc：");

        txt_PC.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                KeyReleased_D(evt);
            }
        });

        txt_AC.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                KeyReleased_D(evt);
            }
        });

        jLabel24.setText("mg/dl");

        jLabel25.setText("mg/dl");

        jLabel26.setText("BG(st)：");

        txt_ST.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                KeyReleased_D(evt);
            }
        });

        jLabel7.setText("mg/dl");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(jLabel15)
                .addGap(18, 18, 18)
                .addComponent(txt_AC, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jLabel24)
                .addGap(32, 32, 32)
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_PC, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel25)
                .addGap(42, 42, 42)
                .addComponent(jLabel26)
                .addGap(12, 12, 12)
                .addComponent(txt_ST, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addContainerGap(121, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jLabel24)
                    .addComponent(jLabel23)
                    .addComponent(txt_AC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_PC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25)
                    .addComponent(txt_ST, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26)
                    .addComponent(jLabel7))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel62.setText("Height：");

        txt_Height.setText("165");
        txt_Height.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_HeightActionPerformed(evt);
            }
        });
        txt_Height.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txt_HeightFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_HeightFocusLost(evt);
            }
        });
        txt_Height.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                KeyReleased_D(evt);
            }
        });

        jLabel63.setText("Weight：");

        txt_Weight.setText("60");
        txt_Weight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_WeightActionPerformed(evt);
            }
        });
        txt_Weight.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_WeightFocusLost(evt);
            }
        });
        txt_Weight.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                KeyReleased_D(evt);
            }
        });

        jLabel71.setText("Education：");

        com_edu.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "　", "Illiterate", "Digital knowledge", "Literacy", "Elementary School", "Middle S. ", "High S. ", "College or above" }));
        com_edu.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ItemStateChanged_D(evt);
            }
        });

        jLabel18.setText("cm");

        jLabel20.setText("kg");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                        .addComponent(btn_Ddate_Save, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel63)
                                    .addGroup(jPanel11Layout.createSequentialGroup()
                                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel11Layout.createSequentialGroup()
                                                .addGap(87, 87, 87)
                                                .addComponent(lab_Pno, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(jLabel60))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel90)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel11Layout.createSequentialGroup()
                                        .addComponent(txt_Weight, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel20))
                                    .addComponent(lab_Name, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel62)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_Height, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(jLabel18)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel71)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addComponent(jLabel85)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addComponent(lab_Age)
                                .addGap(46, 46, 46)
                                .addComponent(jLabel88)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lab_Gender))
                            .addComponent(com_edu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btn_Ddate_Save)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel60)
                                    .addComponent(lab_Pno)
                                    .addComponent(jLabel90)
                                    .addComponent(lab_Name))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel62)
                                    .addComponent(txt_Height, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel18)
                                    .addComponent(jLabel63)
                                    .addComponent(txt_Weight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel20)))
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel85)
                                    .addComponent(lab_Age)
                                    .addComponent(jLabel88)
                                    .addComponent(lab_Gender))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel71)
                                    .addComponent(com_edu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(14, 14, 14)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        mn_Fiele.setText("File");
        mn_Fiele.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mn_FieleActionPerformed(evt);
            }
        });

        mnit_Lab.setText("Laboratory/Radiology(X-RAY) Request");
        mnit_Lab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnit_LabActionPerformed(evt);
            }
        });
        mn_Fiele.add(mnit_Lab);

        mnit_History.setText("Laboratory Recoard(For DM)");
        mnit_History.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnit_HistoryActionPerformed(evt);
            }
        });
        mn_Fiele.add(mnit_History);

        mnit_Close.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0));
        mnit_Close.setText("Close");
        mnit_Close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnit_CloseActionPerformed(evt);
            }
        });
        mn_Fiele.add(mnit_Close);

        mnb.add(mn_Fiele);

        menu_SetDM.setText("Set");

        jMenu1.setText("DM  ");

        mnit_V1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, java.awt.event.InputEvent.ALT_MASK));
        mnit_V1.setText("V1 new patient");
        mnit_V1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnit_V1ActionPerformed(evt);
            }
        });
        jMenu1.add(mnit_V1);

        mnit_V2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F2, java.awt.event.InputEvent.ALT_MASK));
        mnit_V2.setText("V2 F/U per 3 months");
        mnit_V2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnit_V2ActionPerformed(evt);
            }
        });
        jMenu1.add(mnit_V2);

        mnit_V3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F3, java.awt.event.InputEvent.ALT_MASK));
        mnit_V3.setText("V3 per year");
        mnit_V3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnit_V3ActionPerformed(evt);
            }
        });
        jMenu1.add(mnit_V3);

        menu_SetDM.add(jMenu1);

        mnb.add(menu_SetDM);

        setJMenuBar(mnb);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTabbedPane1, 0, 0, Short.MAX_VALUE)
                            .addComponent(btn_CaseClose, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 357, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_CaseClose)
                .addContainerGap())
        );

        jTabbedPane1.getAccessibleContext().setAccessibleName("Assess");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jCheckBox142ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox142ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox142ActionPerformed
    
    private void btn_CaseCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CaseCloseActionPerformed
        
            if(m_From.equals("dia") || m_From.equals("medicine")){
                if(m_From.equals("dia")){
                    if(btn_ComSave.isEnabled() == true){
                        Object[] options = {"YES", "NO"};
                        int dialog = JOptionPane.showOptionDialog(
                            new Frame(),
                            "Not saved to continue ?",
                            "Message",
                            JOptionPane.YES_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options,
                            options[0]);
                        if (dialog == 0) {
                            //選擇 YES 時
                            //關閉此視窗
                            this.dispose();
                        } else {
                            //選擇 NO 時
                        }                       
                    }
      
                    //關閉此視窗
                    this.dispose();
                }else if(m_From.equals("medicine")){
                    if(btn_DheSave.isEnabled() == true){
                        Object[] options = {"YES", "NO"};
                        int dialog = JOptionPane.showOptionDialog(
                            new Frame(),
                            "Not saved to continue ?",
                            "Message",
                            JOptionPane.YES_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options,
                            options[0]);
                        if (dialog == 0) {
                            //選擇 YES 時
                            //關閉此視窗
                            this.dispose();
                        } else {
                            //選擇 NO 時
                        }
                    }
                    //關閉此視窗
                    this.dispose();
                }
            }else{
                //判斷哪些表格還沒存檔
                String tab_name = "";

                if (btn_Ddate_Save.isEnabled() ) {
                    tab_name += "Demographic data \n";
                }


                if (btn_AssSave.isEnabled() ) {
                    tab_name += "Asscement \n";
                }

                if (btn_ComSave.isEnabled() ) {
                    tab_name += "Complication \n";
                }

                if (btn_ConSave.isEnabled() ) {
                    tab_name += "Confirm the completion of health education \n";
                }

                if (btn_PreSave.isEnabled() ) {
                    tab_name += "Laboratory \n";
                }

                if (btn_DheSave.isEnabled() ) {
                    tab_name += "Drug health education \n";
                }

                if(!tab_name.equals("")){
                    Object[] options_con = {"YES", "NO"};
                    int dialog_con = JOptionPane.showOptionDialog(
                        new Frame(),
                        "Not saved to continue ?\n"+tab_name+"\n",
                        "Message",
                        JOptionPane.YES_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options_con,
                        options_con[0]);
                    if (dialog_con == 0) showEnterClinic ();
                } else showEnterClinic ();
            }
   
    }//GEN-LAST:event_btn_CaseCloseActionPerformed



    private void txt_HeightFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_HeightFocusLost
}//GEN-LAST:event_txt_HeightFocusLost

    private void txt_HeightFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_HeightFocusGained
        // TODO add your handling code here:
}//GEN-LAST:event_txt_HeightFocusGained

    private void txt_HeightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_HeightActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_txt_HeightActionPerformed

    private void txt_WeightFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_WeightFocusLost
}//GEN-LAST:event_txt_WeightFocusLost

    private void txt_WeightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_WeightActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_txt_WeightActionPerformed

    private void btn_Ddate_SaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Ddate_SaveActionPerformed

        try {
            String sql = "UPDATE patients_info  SET height = '" + txt_Height.getText() + "',  weight = '" + txt_Weight.getText() + "', education = '"+com_edu.getSelectedIndex()+"' WHERE p_no = '" + m_Pno + "'";
            System.out.println(sql);
            DBC.executeUpdate(sql);

            String sqlBgac = "INSERT INTO prescription (guid, code, case_guid, date_test, date_results, result, isnormal, cost, finish, state) "
                    + "SELECT UUID(), prescription_code.code,'"+m_RegGuid+"', NOW(), NOW(), '"+txt_AC.getText()+"', 0,0,'F',1  FROM prescription_code WHERE prescription_code.name = 'BGAc'";
            DBC.executeUpdate(sqlBgac);

            String sqlBgpc = "INSERT INTO prescription (guid, code, case_guid, date_test, date_results, result, isnormal, cost, finish, state) "
                    + "SELECT UUID(), prescription_code.code,'"+m_RegGuid+"', NOW(), NOW(), '"+txt_PC.getText()+"', 0,0,'F',1  FROM prescription_code WHERE prescription_code.name = 'BGPc'";
            DBC.executeUpdate(sqlBgpc);

            String sqlSt = "INSERT INTO prescription (guid, code, case_guid, date_test, date_results, result, isnormal, cost, finish, state) "
                    + "SELECT UUID(), prescription_code.code,'"+m_RegGuid+"', NOW(), NOW(), '"+txt_ST.getText()+"', 0,0,'F',1  FROM prescription_code WHERE prescription_code.name = 'St.'";
            DBC.executeUpdate(sqlSt);

            JOptionPane.showMessageDialog(null, "Save Complete");
            txt_bmi.setText(Tools.getBmi(txt_Height.getText(), txt_Weight.getText()));
            btn_Ddate_Save.setEnabled(false);
        } catch (SQLException ex) {
            Logger.getLogger(Frm_Case.class.getName()).log(Level.SEVERE, null, ex);
        }
        
}//GEN-LAST:event_btn_Ddate_SaveActionPerformed

    private void btn_CloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CloseActionPerformed
        setLostAutoCompleteEdit();
        if(m_RsRowCount == 0 ) {
            setClearTableRow(m_Row);
        }
        if (m_SelectTable.getValueAt(m_Row, 2) != null && m_AutoListValue != null || m_AutoTable.equals("prescription_code")) {
            if(!isCodeAtHashMap(m_AutoListValue[0].trim())) {
                setClearTableRow(m_Row);
            } 
        } else {
            setClearTableRow(m_Row);
        }
        dia.setVisible(false);
}//GEN-LAST:event_btn_CloseActionPerformed

    private void tab_MedicineTeachMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab_MedicineTeachMouseClicked

        if (tab_MedicineTeach.getSelectedColumn() == 5 || tab_MedicineTeach.getSelectedColumn() == 6) {
            btn_DheSave.setEnabled(true);
        }

        if (tab_MedicineTeach.getValueAt(tab_MedicineTeach.getSelectedRow(), 5).toString().equals("true")) {
            tab_MedicineTeach.setValueAt(UserInfo.getUserID(), tab_MedicineTeach.getSelectedRow(), 3);
            tab_MedicineTeach.setValueAt(UserInfo.getUserName(), tab_MedicineTeach.getSelectedRow(), 4);
        } else {
            tab_MedicineTeach.setValueAt("", tab_MedicineTeach.getSelectedRow(), 3);
            tab_MedicineTeach.setValueAt("", tab_MedicineTeach.getSelectedRow(), 4);
        }


}//GEN-LAST:event_tab_MedicineTeachMouseClicked

    private void btn_PreSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_PreSaveActionPerformed

        try {
            boolean prescriptionState = false; //  判斷是否有檢驗處置
            boolean xrayState = false; //  判斷是否有x光處置
            // 存入處置
            String sqlDelete = "DELETE FROM prescription WHERE case_guid = '" + m_RegGuid + "'";
            DBC.executeUpdate(sqlDelete);
            for (int i = 0; i < this.tab_Prescription.getRowCount(); i++) {
                if (this.tab_Prescription.getValueAt(i, 1) != null && !this.tab_Prescription.getValueAt(i, 1).toString().trim().equals("")) {
                    if (this.tab_Prescription.getValueAt(i, 4) != null && this.tab_Prescription.getValueAt(i, 4).toString().trim().equals(Constant.X_RAY_CODE)) {
                        xrayState = true;
                    } else {
                        prescriptionState = true;
                    }
                    if (this.tab_Prescription.getValueAt(i, 3) == null) {
                        this.tab_Prescription.setValueAt("", i, 3);
                    }
                    String sql = "INSERT INTO prescription (guid, case_guid, code, place, state) " +
                            "VALUES (uuid(), '" + m_RegGuid + "', '" + this.tab_Prescription.getValueAt(i, 1).toString().trim() + "', '" + this.tab_Prescription.getValueAt(i, 3).toString().trim() + "', 1)";
                    DBC.executeUpdate(sql);
                }
            }
            setPrint(prescriptionState, xrayState);
            JOptionPane.showMessageDialog(null, "Save Complete");


            // 提示回診日 *************************************
//                String packageSetAll = "";
//                if (m_PackageSet != null) {
//                    dia_RevisitTime.setLocationRelativeTo(this);
//                    dia_RevisitTime.setVisible(true);
//                    String packageSet[] = m_PackageSet.split(",");
//                    String packageSetId[] = m_PackageSetId.split(",");
//                    for (int i = 0; i < packageSet.length; i++) {
//                        packageSetAll+= i+1 +". "+  packageSet[i] + " ";
//                        String sql = "SELECT days FROM package_item WHERE id = '"+packageSetId[i]+"'";
//                        ResultSet rs = DBC.executeQuery(sql);
//                        txt_PackageId.setText(packageSetId[i]);
//                        if (rs.next()) txt_ComeBackDays.setText(rs.getString ("days"));
//                    }
//                    txt_PackageType.setText(packageSetAll);
//                    this.setEnabled(false);
//                }
                //*************************************************
            btn_PreSave.setEnabled(false);
        } catch (SQLException ex) {
            Logger.getLogger(Frm_Case.class.getName()).log(Level.SEVERE, null, ex);
        }
}//GEN-LAST:event_btn_PreSaveActionPerformed

    private void tab_PrescriptionKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tab_PrescriptionKeyReleased
        btn_PreSave.setEnabled(true);
        if (isAllowKeyIn(evt) && m_SelectTable.getSelectedRow() != -1) {
            if (m_SelectTable.getColumnCount() > 2 && m_SelectTable.getValueAt(m_SelectTable.getSelectedRow(), 2) != null) {
                //                    System.out.println("移除 hashmap code = "+m_SelectTable.getValueAt(m_SelectTable.getSelectedRow(), 1).toString().trim());
                m_SelectTableHashMap.remove(m_SelectTable.getValueAt(m_SelectTable.getSelectedRow(), 1).toString().trim());
            }
            getAutoCompleteValue(m_AutoTxt.getText());
        }
        tab_PrescriptionMouseClicked(null);
        if (tab_Prescription.getSelectedRow() != -1
                && (evt.getKeyCode() == KeyEvent.VK_ENTER
                || evt.getKeyCode() == KeyEvent.VK_DELETE)) {
            m_PrescriptionRowNo = setRowValue(evt);
        }
}//GEN-LAST:event_tab_PrescriptionKeyReleased

    private void tab_PrescriptionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tab_PrescriptionKeyPressed
       if (m_SelectTable.getSelectedColumn() == 1 && evt.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            if(m_SelectTable.getValueAt(m_SelectTable.getSelectedRow(), 1) != null) {
                setBackspaceTxt();
            }
        }

         if (evt.getKeyCode() == KeyEvent.VK_ALT) {
            setAllRemoveEditAndSelection();
        }

}//GEN-LAST:event_tab_PrescriptionKeyPressed

    private void tab_PrescriptionFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tab_PrescriptionFocusGained
        m_AutoTable = "prescription_code";
        String[] prescriptionRsList = {"code", "name", "type"};
        m_AutoColumn = prescriptionRsList;
        m_AutoColumnName = "code";
        m_SelectTable = tab_Prescription;
        m_AutoLocationSpane = span_Prescription;
        m_SelectTableAddRowLimitColumn = 1;
        m_SelectTableModel = m_PrescriptionModel;
        m_SelectTableRowNo = m_PrescriptionRowNo;
        m_SelectTableNo = 2;
        m_SelectTableHashMap = m_PrescriptionHashMap;
}//GEN-LAST:event_tab_PrescriptionFocusGained

    private void tab_PrescriptionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab_PrescriptionMouseClicked
  
}//GEN-LAST:event_tab_PrescriptionMouseClicked

    private void tab_HealthTeachMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab_HealthTeachMouseClicked
         if(! m_From.equals("dia")){
            if (tab_HealthTeach.getSelectedColumn() == 5 || tab_HealthTeach.getSelectedColumn() == 6) {
                btn_ConSave.setEnabled(true);
            }

            if (tab_HealthTeach.getValueAt(tab_HealthTeach.getSelectedRow(), 5).toString().equals("true")) {
                tab_HealthTeach.setValueAt(UserInfo.getUserID(), tab_HealthTeach.getSelectedRow(), 3);
                tab_HealthTeach.setValueAt(UserInfo.getUserName(), tab_HealthTeach.getSelectedRow(), 4);
            } else {
                tab_HealthTeach.setValueAt("", tab_HealthTeach.getSelectedRow(), 3);
                tab_HealthTeach.setValueAt("", tab_HealthTeach.getSelectedRow(), 4);
            }
        }
    }//GEN-LAST:event_tab_HealthTeachMouseClicked

    private void btn_ConSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ConSaveActionPerformed
        for (int i = 0; i < tab_HealthTeach.getRowCount(); i++) {

            if (tab_HealthTeach.getValueAt(i,3) == null || tab_HealthTeach.getValueAt(i, 3).toString().trim().equals("")) continue;

            try {
                if(tab_HealthTeach.getValueAt(i,7) == null){
                    System.out.println("INININININININININ");
                    String thcheck = "0";
                    if (tab_HealthTeach.getValueAt(i, 5).toString().equals("true")) {
                        thcheck = "1";
                    }
                    String acceptance = "0";
                    if (tab_HealthTeach.getValueAt(i, 6).toString().equals("Excellent")) {
                        acceptance = "1";
                    } else if (tab_HealthTeach.getValueAt(i, 6).toString().equals("Good")) {
                        acceptance = "2";
                    } else if (tab_HealthTeach.getValueAt(i, 6).toString().equals("<html><font color='FF0000'>Poor</font></html>")) {
                        acceptance = "3";
                    }

                    String sql = "INSERT INTO health_teach (guid, hti_code, reg_guid, s_id, confirm, acceptance) "
                        + "VALUES (uuid(), '" + tab_HealthTeach.getValueAt(i, 1) + "','" + m_RegGuid + "', ";

                    if (tab_HealthTeach.getValueAt(i, 3) == null) {
                        sql += "NULL, ";
                    } else {
                        sql += "'" + tab_HealthTeach.getValueAt(i, 3) + "', ";
                    }
                    sql += " '" + thcheck + "','" + acceptance + "' )";

                    System.out.println(sql);
                    DBC.executeUpdate(sql);

                 }else{
                    System.out.println("UPUPUPUPUUPUPUPUUPUP");
                    //做UPDATE的動作
                    String thcheck = "0";
                    if (tab_HealthTeach.getValueAt(i, 5).toString().equals("true")) {
                        thcheck = "1";
                    }

                    String acceptance = "0";
                    if (tab_HealthTeach.getValueAt(i, 6).toString().equals("Excellent")) {
                        acceptance = "1";
                    } else if (tab_HealthTeach.getValueAt(i, 6).toString().equals("Good")) {
                        acceptance = "2";
                    } else if (tab_HealthTeach.getValueAt(i, 6).toString().equals("<html><font color='FF0000'>Poor</font></html>")) {
                        acceptance = "3";
                    }

                    String sql = "UPDATE health_teach SET "
                    + "guid = uuid(),"
                    + "hti_code = '" + tab_HealthTeach.getValueAt(i, 1) + "',"
                    + "reg_guid = '" + m_RegGuid + "',";

                    if (tab_HealthTeach.getValueAt(i, 3) == null) {
                        sql += "s_id = NULL, ";
                    } else {
                        sql += "s_id = '" + tab_HealthTeach.getValueAt(i, 3) + "', ";
                    }

                    sql += " confirm = '" + thcheck + "',"
                    + "acceptance = '" + acceptance + "'"
                    + " WHERE hti_code = '" + tab_HealthTeach.getValueAt(i, 1) +"'";
                    System.out.println(sql);
                    DBC.executeUpdate(sql);
                 }
                 
            } catch (SQLException ex) {
                Logger.getLogger(Frm_Case.class.getName()).log(Level.SEVERE, null, ex);
            }         
        }       
        JOptionPane.showMessageDialog(null, "Save Complete");
        btn_ConSave.setEnabled(false);
}//GEN-LAST:event_btn_ConSaveActionPerformed

    private void btn_ComSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ComSaveActionPerformed
        String bg1 = "0", bg2 = "0", bg3 = "0", bg4 = "0";

        if (buttonGroup1.getSelection() == rad_postural_hypotension_yes.getModel()) {
            bg1 = "1";
        } else if (buttonGroup1.getSelection() == rad_postural_hypotension_no.getModel()) {
            bg1 = "2";
        } else {
            bg1 = "3";
        }

        if (buttonGroup2.getSelection() == rad_peripheral_neuropathy_yes.getModel()) {
            bg2 = "1";
        } else if (buttonGroup2.getSelection() == rad_peripheral_neuropathy_no.getModel()) {
            bg2 = "2";
        } else {
            bg2 = "3";
        }

        if (buttonGroup3.getSelection() == rad_angina_yes.getModel()) {
            bg3 = "1";
        } else if (buttonGroup3.getSelection() == rad_angina_no.getModel()) {
            bg3 = "2";
        } else {
            bg3 = "3";
        }

        if (buttonGroup4.getSelection() == rad_claudication_yes.getModel()) {
            bg4 = "1";
        } else if (buttonGroup4.getSelection() == rad_claudication_no.getModel()) {
            bg4 = "2";
        } else {
            bg4 = "3";
        }

        try {
            String sql = "INSERT INTO complication (guid, reg_guid, dka, hhs, hypoglycemia, stroke, "+
                     " coronary_heart, paod, eye_lesions, neuropathy, kidney, waist, other, postural_hypotension, "+
                     " peripheral_neuropathy, angina, claudication, u_sid, udate ) VALUES (UUID(), '"+m_RegGuid+"', "+
                     " " + com_dka.getSelectedIndex() + ", " + com_hhs.getSelectedIndex() + ", " + com_hypoglycemia.getSelectedIndex() + ","+
                     " " + com_stroke.getSelectedIndex() + ", " + com_coronary_heart.getSelectedIndex() + ", " + com_paod.getSelectedIndex() + ","+
                     " " + com_eye_lesions.getSelectedIndex() + ", " + com_neuropathy.getSelectedIndex() + ", " + com_kidney.getSelectedIndex() + ",";
                    if (txt_waist.getText().equals("")) {
                        sql += " NULL, ";
                    } else {
                        sql += "'" + txt_waist.getText() + "'," ;
                    }
                    sql += " '" + txt_other.getText() + "', " + bg1 + ", " + bg2 + ", " + bg3 + ", " + bg4+ ", '"+ UserInfo.getUserID() +"', NOW() )";
            DBC.executeUpdate(sql);
            setOverValue();
            JOptionPane.showMessageDialog(null, "Save Complete");
            btn_ComSave.setEnabled(false);
        } catch (SQLException ex) {
            Logger.getLogger(Frm_Case.class.getName()).log(Level.SEVERE, null, ex);
        }
}//GEN-LAST:event_btn_ComSaveActionPerformed

    private void rad_postural_hypotension_yesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rad_postural_hypotension_yesActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_rad_postural_hypotension_yesActionPerformed

    private void ItemStateChanged_C(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ItemStateChanged_C
        // TODO add your handling code here:
        btn_ComSave.setEnabled(true);
    }//GEN-LAST:event_ItemStateChanged_C

    private void KeyReleased_C(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KeyReleased_C
        // TODO add your handling code here:
        btn_ComSave.setEnabled(true);
    }//GEN-LAST:event_KeyReleased_C

    private void mnit_LabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnit_LabActionPerformed
        new Frm_DiagnosisPrescription(this).setVisible(true);
        tab_Prescription.removeRowSelectionInterval(0, tab_Prescription.getRowCount()-1);
        tab_PrescriptionFocusGained(null);
}//GEN-LAST:event_mnit_LabActionPerformed

    private void mnit_HistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnit_HistoryActionPerformed
        new Frm_LabDM( m_Pno).setVisible(true);
}//GEN-LAST:event_mnit_HistoryActionPerformed

    private void mnit_CloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnit_CloseActionPerformed

}//GEN-LAST:event_mnit_CloseActionPerformed

    private void mn_FieleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mn_FieleActionPerformed

}//GEN-LAST:event_mn_FieleActionPerformed

    private void btn_AssSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_AssSaveActionPerformed
        String smo, dri;

        if (che_smoke.isSelected() == true) {
            smo = "0";
        } else {
            smo = "1";
        }

        if (che_drink.isSelected() == true) {
            dri = "0";
        } else {
            dri = "1";
        }

        String lig;
        if (che_light_coagulation_L.isSelected() == true && che_light_coagulation_R.isSelected() == true) {
            lig = "3";
        } else if (che_light_coagulation_L.isSelected() == false && che_light_coagulation_R.isSelected() == false) {
            lig = "0";
        } else if (che_light_coagulation_L.isSelected() == true) {
            lig = "1";
        } else {
            lig = "2";
        }

        String cat;
        if (che_cataract_L.isSelected() == true && che_cataract_R.isSelected() == true) {
            cat = "3";
        } else if (che_cataract_L.isSelected() == false && che_cataract_R.isSelected() == false) {
            cat = "0";
        } else if (che_cataract_L.isSelected() == true) {
            cat = "1";
        } else {
            cat = "2";
        }

        String ret;
        if (che_retinal_check_L.isSelected() == true && che_retinal_check_R.isSelected() == true) {
            ret = "3";
        } else if (che_retinal_check_L.isSelected() == false && che_retinal_check_R.isSelected() == false) {
            ret = "0";
        } else if (che_retinal_check_L.isSelected() == true) {
            ret = "1";
        } else {
            ret = "2";
        }

        String non;
        if (che_non_proliferative_retinopathy_L.isSelected() == true && che_non_proliferative_retinopathy_R.isSelected() == true) {
            non = "3";
        } else if (che_non_proliferative_retinopathy_L.isSelected() == false && che_non_proliferative_retinopathy_R.isSelected() == false) {
            non = "0";
        } else if (che_non_proliferative_retinopathy_L.isSelected() == true) {
            non = "1";
        } else {
            non = "2";
        }

        String pre;
        if (che_pre_proliferative_retinopathy_L.isSelected() == true && che_pre_proliferative_retinopathy_R.isSelected() == true) {
            pre = "3";
        } else if (che_pre_proliferative_retinopathy_L.isSelected() == false && che_pre_proliferative_retinopathy_R.isSelected() == false) {
            pre = "0";
        } else if (che_pre_proliferative_retinopathy_L.isSelected() == true) {
            pre = "1";
        } else {
            pre = "2";
        }

        String pro;
        if (che_proliferative_retinopathy_L.isSelected() == true && che_proliferative_retinopathy_R.isSelected() == true) {
            pro = "3";
        } else if (che_proliferative_retinopathy_L.isSelected() == false && che_proliferative_retinopathy_R.isSelected() == false) {
            pro = "0";
        } else if (che_proliferative_retinopathy_L.isSelected() == true) {
            pro = "1";
        } else {
            pro = "2";
        }

        String mac;
        if (che_macular_degeneration_L.isSelected() == true && che_macular_degeneration_R.isSelected() == true) {
            mac = "3";
        } else if (che_macular_degeneration_L.isSelected() == false && che_macular_degeneration_R.isSelected() == false) {
            mac = "0";
        } else if (che_macular_degeneration_L.isSelected() == true) {
            mac = "1";
        } else {
            mac = "2";
        }

        String adv;
        if (che_advanced_dm_eyedisease_L.isSelected() == true && che_advanced_dm_eyedisease_R.isSelected() == true) {
            adv = "3";
        } else if (che_advanced_dm_eyedisease_L.isSelected() == false && che_advanced_dm_eyedisease_R.isSelected() == false) {
            adv = "0";
        } else if (che_advanced_dm_eyedisease_L.isSelected() == true) {
            adv = "1";
        } else {
            adv = "2";
        }

        String vib;
        if (che_vibration_L.isSelected() == true && che_vibration_R.isSelected() == true) {
            vib = "3";
        } else if (che_vibration_L.isSelected() == false && che_vibration_R.isSelected() == false) {
            vib = "0";
        } else if (che_vibration_L.isSelected() == true) {
            vib = "1";
        } else {
            vib = "2";
        }

        String pul;
        if (che_pulse_L.isSelected() == true && che_pulse_R.isSelected() == true) {
            pul = "3";
        } else if (che_pulse_L.isSelected() == false && che_pulse_R.isSelected() == false) {
            pul = "0";
        } else if (che_pulse_L.isSelected() == true) {
            pul = "1";
        } else {
            pul = "2";
        }

        String ulc;
        if (che_ulcer_L.isSelected() == true && che_ulcer_R.isSelected() == true) {
            ulc = "3";
        } else if (che_ulcer_L.isSelected() == false && che_ulcer_R.isSelected() == false) {
            ulc = "0";
        } else if (che_ulcer_L.isSelected() == true) {
            ulc = "1";
        } else {
            ulc = "2";
        }

        String acu;
        if (che_acupuncture_L.isSelected() == true && che_acupuncture_R.isSelected() == true) {
            acu = "3";
        } else if (che_acupuncture_L.isSelected() == false && che_acupuncture_R.isSelected() == false) {
            acu = "0";
        } else if (che_acupuncture_L.isSelected() == true) {
            acu = "1";
        } else {
            acu = "2";
        }

        String cur;
        if (che_ulcer_cured_L.isSelected() == true && che_ulcer_cured_R.isSelected() == true) {
            cur = "3";
        } else if (che_ulcer_cured_L.isSelected() == false && che_ulcer_cured_R.isSelected() == false) {
            cur = "0";
        } else if (che_ulcer_cured_L.isSelected() == true) {
            cur = "1";
        } else {
            cur = "2";
        }

        String byp;
        if (che_bypass_surgery_L.isSelected() == true && che_bypass_surgery_R.isSelected() == true) {
            byp = "3";
        } else if (che_bypass_surgery_L.isSelected() == false && che_bypass_surgery_R.isSelected() == false) {
            byp = "0";
        } else if (che_bypass_surgery_L.isSelected() == true) {
            byp = "1";
        } else {
            byp = "2";
        }


        String dm_year = null;
        try {
            if(dateComboBox.getValue() != null && !dateComboBox.getValue().trim().equals("")){
                dm_year = dateComboBox.getValue() ;
            }
        } catch(Exception ex) {
            System.out.println("Bug");
        }

        String oral_syear = null;
        try {
            if(cbox_oral_syear.getSelectedItem() != null && !cbox_oral_syear.getSelectedItem().toString().trim().equals("")){
                oral_syear = cbox_oral_syear.getSelectedItem().toString();
            }
        } catch(Exception ex) {
            System.out.println("Bug");
        }

        String insulin_syear = null;
        try {
            if(cbox_insulin_syear.getSelectedItem() != null && !cbox_insulin_syear.getSelectedItem().toString().trim().equals("")){
                insulin_syear = cbox_insulin_syear.getSelectedItem().toString();
            }
        } catch(Exception ex) {
            System.out.println("Bug");
        }

        String drink_aweek = null;
        try {
            if(ftf_drink_aweek.getText() != null && !ftf_drink_aweek.getText().trim().equals("")){
                drink_aweek = ftf_drink_aweek.getText();
            }
        } catch(Exception ex) {
            System.out.println("Bug");
        }

        Double dbp = null;
        try {
            dbp = Double.valueOf(txt_dbp.getText());
        } catch(Exception ex) {
            System.out.println("Bug");
        }

        Double sbp = null;
        try {
            sbp = Double.valueOf(txt_sbp.getText());
        } catch(Exception ex) {
            System.out.println("Bug");
        }

        
        Double eye_l = null;
        try {
            eye_l = Double.valueOf(ftf_eye_lvision.getText());
        } catch(Exception ex) {
            System.out.println("Bug");
        }

        Double eye_r = null;
        try {
            eye_r = Double.valueOf(ftf_eye_rvision.getText());
        } catch(Exception ex) {
            System.out.println("Bug");
        }

        try {
            String sql = "INSERT INTO asscement (guid, reg_guid, family_history, self_care, dm_type, dm_typeo, dm_year, "+
                     " oral_hypoglycemic, oral_syear, insulin, insulin_syear, gestation, gestation_count, abortions_count, "+
                     " smoke, smoke_aday, drink, drink_aweek, sport, education, bloodtest_aweek, urine_aweek, dbp, sbp, "+
                     " eye_lvision, eye_rvision, fundus_check, light_coagulation, cataract, retinal_check, non_proliferative_retinopathy, "+
                     " pre_proliferative_retinopathy, proliferative_retinopathy, macular_degeneration, advanced_dm_eyedisease, "+
                     " vibration, pulse, ulcer, acupuncture, ulcer_cured, bypass_surgery, u_sid, udate) "+
                     " VALUES (UUID(), '"+m_RegGuid+"', " + com_family_history.getSelectedIndex() + ", " + com_self_care.getSelectedIndex() + ", "+
                     " " + com_dm_type.getSelectedIndex() + ", '" + txt_dm_typeo.getText() + "', " + dm_year + ", " + com_oral_hypoglycemic.getSelectedIndex() + ", "+
                     " " + oral_syear + ", " + com_insulin.getSelectedIndex() + ", " + insulin_syear + ", " + com_gestation.getSelectedIndex() + ", "+
                     " " + spi_gestation_count.getValue() + ", " + spi_abortions_count.getValue() + ", " + smo + ", " + spi_smoke_aday.getValue() + ", "+
                     " " + dri + ", " + drink_aweek + ", " + com_sport.getSelectedIndex() + ", " + com_education.getSelectedIndex() + ", "+
                     " " + spi_bloodtest_aweek.getValue() + ", " + spi_urine_aweek.getValue() + ", " + dbp + ", " + sbp + ", " + eye_l + ", "+
                     " " + eye_r + ", " + com_fundus_check.getSelectedIndex() + ", " + lig + ", " + cat + ", " + ret + ", " + non + ", " + pre + ", " + pro + ", "+
                     " " + mac + ", " + adv + ", " + vib + ", " + pul + ", " + ulc + ", " + acu + ", " + cur + ", " + byp + ", '"+ UserInfo.getUserID() +"', NOW() )";
            System.out.print(sql);
            DBC.executeUpdate(sql);
            setOverValue();
            JOptionPane.showMessageDialog(null, "Save Complete");
            btn_AssSave.setEnabled(false);
        } catch (SQLException ex) {
            Logger.getLogger(Frm_Case.class.getName()).log(Level.SEVERE, null, ex);
        }
//        try {
//            Double.valueOf(jFormattedTextField2.getText());
//        } catch(Exception ex) {
//            System.out.println("fuck");
//        }


    }//GEN-LAST:event_btn_AssSaveActionPerformed

    private void KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KeyReleased
        // TODO add your handling code here:
        btn_AssSave.setEnabled(true);
}//GEN-LAST:event_KeyReleased

    private void com_insulinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_com_insulinActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_com_insulinActionPerformed

    private void ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ItemStateChanged
         if(evt.getStateChange() == java.awt.event.ItemEvent.SELECTED ) {
            if (com_dm_type.getSelectedIndex() == 3 ) txt_dm_typeo.setEnabled(true);
            else {
                txt_dm_typeo.setEnabled(false);
                txt_dm_typeo.setText("");
            }
         }


        btn_AssSave.setEnabled(true);
}//GEN-LAST:event_ItemStateChanged

    private void che_bypass_surgery_RActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_che_bypass_surgery_RActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_che_bypass_surgery_RActionPerformed

    private void che_ulcer_cured_RActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_che_ulcer_cured_RActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_che_ulcer_cured_RActionPerformed

    private void che_acupuncture_RActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_che_acupuncture_RActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_che_acupuncture_RActionPerformed

    private void che_ulcer_RActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_che_ulcer_RActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_che_ulcer_RActionPerformed

    private void che_pulse_LActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_che_pulse_LActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_che_pulse_LActionPerformed

    private void che_pulse_RActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_che_pulse_RActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_che_pulse_RActionPerformed

    private void che_vibration_RActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_che_vibration_RActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_che_vibration_RActionPerformed

    private void che_advanced_dm_eyedisease_LActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_che_advanced_dm_eyedisease_LActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_che_advanced_dm_eyedisease_LActionPerformed

    private void che_proliferative_retinopathy_RActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_che_proliferative_retinopathy_RActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_che_proliferative_retinopathy_RActionPerformed

    private void StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_StateChanged
        // TODO add your handling code here:
        btn_AssSave.setEnabled(true);
}//GEN-LAST:event_StateChanged

    private void btn_DheSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_DheSaveActionPerformed
        // TODO add your handling code here:
        for (int i = 0; i < tab_MedicineTeach.getRowCount(); i++) {
            if(tab_MedicineTeach.getValueAt(i, 5).toString().equals("true")){
                try{

                    String check = "0";
                    if (tab_MedicineTeach.getValueAt(i, 5).toString().equals("true")) {
                        check = "1";
                    }

                    String sql = "UPDATE medicine_stock SET ";

                        if (tab_MedicineTeach.getValueAt(i, 3) == null) {
                            sql += "s_id = NULL, ";
                        } else {
                            sql += "s_id = '" + tab_MedicineTeach.getValueAt(i, 3) + "', ";
                        }

                    sql += "teach_complete = '" + check + "', ";

                         if (tab_MedicineTeach.getValueAt(i, 6) == null) {
                            sql += " ps = NULL ";
                        } else {
                            sql += " ps = '" + tab_MedicineTeach.getValueAt(i, 6) + "' ";
                        }
                    sql += " WHERE os_guid = (SELECT outpatient_services.guid  FROM outpatient_services, registration_info WHERE registration_info.guid = outpatient_services.reg_guid AND registration_info.guid = '"+ m_RegGuid +"' AND m_code = '"+tab_MedicineTeach.getValueAt(i, 1)+"')";
                    System.out.println(sql);
                    DBC.executeUpdate(sql);
                } catch (SQLException ex) {
                    Logger.getLogger(Frm_Case.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        JOptionPane.showMessageDialog(null, "Save Complete");
        btn_DheSave.setEnabled(false);
    }//GEN-LAST:event_btn_DheSaveActionPerformed

    private void ItemStateChanged_D(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ItemStateChanged_D
        // TODO add your handling code here:
        btn_Ddate_Save.setEnabled(true);
    }//GEN-LAST:event_ItemStateChanged_D

    private void KeyReleased_D(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KeyReleased_D
        // TODO add your handling code here:
        btn_Ddate_Save.setEnabled(true);
    }//GEN-LAST:event_KeyReleased_D

    private void mnit_V1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnit_V1ActionPerformed
        setV("V1");
        m_PackageSet = "V1 new patient,";
        m_PackageSetId = "V1,";
        jTabbedPane1.setSelectedIndex(3);
}//GEN-LAST:event_mnit_V1ActionPerformed

    private void mnit_V2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnit_V2ActionPerformed
        setV("V2");
        m_PackageSet = "V2 F/U per 3 months,";
        m_PackageSetId = "V2,";
        jTabbedPane1.setSelectedIndex(3);
}//GEN-LAST:event_mnit_V2ActionPerformed

    private void mnit_V3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnit_V3ActionPerformed
        setV("V3");
        m_PackageSet = "V3 per year,";
        m_PackageSetId = "V3,";
        jTabbedPane1.setSelectedIndex(3);

}//GEN-LAST:event_mnit_V3ActionPerformed

    private void list_MenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_list_MenuMouseClicked
        getSplitValue();
        if (evt.getClickCount() == 2) {
            btn_CloseActionPerformed(null);
        }
}//GEN-LAST:event_list_MenuMouseClicked

    private void list_MenuKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_list_MenuKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            btn_CloseActionPerformed(null);
            setClearTableRow(m_Row);
            setLostAutoCompleteEdit();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btn_CloseActionPerformed(null);
        }
        if (evt.getKeyCode() == KeyEvent.VK_BACK_SPACE) {  // 刪除一個字元
            setBackspaceTxt();
        }
}//GEN-LAST:event_list_MenuKeyPressed

    private void list_MenuKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_list_MenuKeyReleased
        if(isKeyIn(String.valueOf(evt.getKeyChar()))) {
            setTxt(String.valueOf(evt.getKeyChar()));
        }
        if (evt.getKeyCode() != KeyEvent.VK_DELETE && list_Menu.getVisibleRowCount() > 0
                && (evt.getKeyCode() == KeyEvent.VK_UP || evt.getKeyCode() == KeyEvent.VK_DOWN)) {
            getSplitValue();
        }
}//GEN-LAST:event_list_MenuKeyReleased

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            if (Common.Tools.isNumber(txt_ComeBackDays.getText().trim())) {

                DBC.executeUpdate("INSERT INTO package_set(guid, reg_guid, id, use_date, sms_state, days) " +
                        "VALUES(UUID(), '" + m_RegGuid + "', '" + txt_PackageId.getText() + "', NOW(), '0', "+txt_ComeBackDays.getText().trim()+")");



                JOptionPane.showMessageDialog(null,"Saved successfully.");
                this.setEnabled(true);
                dia_RevisitTime.dispose();
                
            } else {
                JOptionPane.showMessageDialog(null,"Please Check Days");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Frm_Case.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void cbox_oral_syearItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbox_oral_syearItemStateChanged
            btn_AssSave.setEnabled(true);
    }//GEN-LAST:event_cbox_oral_syearItemStateChanged

    private void cbox_insulin_syearItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbox_insulin_syearItemStateChanged
            btn_AssSave.setEnabled(true);
    }//GEN-LAST:event_cbox_insulin_syearItemStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel JLable17;
    private javax.swing.JLabel Lab_record;
    private javax.swing.JButton btn_AssSave;
    private javax.swing.JButton btn_CaseClose;
    private javax.swing.JButton btn_Close;
    private javax.swing.JButton btn_ComSave;
    private javax.swing.JButton btn_ConSave;
    private javax.swing.JButton btn_Ddate_Save;
    private javax.swing.JButton btn_DheSave;
    private javax.swing.JButton btn_PreSave;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.JComboBox cbox_insulin_syear;
    private javax.swing.JComboBox cbox_oral_syear;
    private javax.swing.JCheckBox che_acupuncture_L;
    private javax.swing.JCheckBox che_acupuncture_R;
    private javax.swing.JCheckBox che_advanced_dm_eyedisease_L;
    private javax.swing.JCheckBox che_advanced_dm_eyedisease_R;
    private javax.swing.JCheckBox che_bypass_surgery_L;
    private javax.swing.JCheckBox che_bypass_surgery_R;
    private javax.swing.JCheckBox che_cataract_L;
    private javax.swing.JCheckBox che_cataract_R;
    private javax.swing.JCheckBox che_drink;
    private javax.swing.JCheckBox che_light_coagulation_L;
    private javax.swing.JCheckBox che_light_coagulation_R;
    private javax.swing.JCheckBox che_macular_degeneration_L;
    private javax.swing.JCheckBox che_macular_degeneration_R;
    private javax.swing.JCheckBox che_non_proliferative_retinopathy_L;
    private javax.swing.JCheckBox che_non_proliferative_retinopathy_R;
    private javax.swing.JCheckBox che_pre_proliferative_retinopathy_L;
    private javax.swing.JCheckBox che_pre_proliferative_retinopathy_R;
    private javax.swing.JCheckBox che_proliferative_retinopathy_L;
    private javax.swing.JCheckBox che_proliferative_retinopathy_R;
    private javax.swing.JCheckBox che_pulse_L;
    private javax.swing.JCheckBox che_pulse_R;
    private javax.swing.JCheckBox che_retinal_check_L;
    private javax.swing.JCheckBox che_retinal_check_R;
    private javax.swing.JCheckBox che_smoke;
    private javax.swing.JCheckBox che_ulcer_L;
    private javax.swing.JCheckBox che_ulcer_R;
    private javax.swing.JCheckBox che_ulcer_cured_L;
    private javax.swing.JCheckBox che_ulcer_cured_R;
    private javax.swing.JCheckBox che_vibration_L;
    private javax.swing.JCheckBox che_vibration_R;
    private javax.swing.JComboBox com_coronary_heart;
    private javax.swing.JComboBox com_dka;
    private javax.swing.JComboBox com_dm_type;
    private javax.swing.JComboBox com_edu;
    private javax.swing.JComboBox com_education;
    private javax.swing.JComboBox com_eye_lesions;
    private javax.swing.JComboBox com_family_history;
    private javax.swing.JComboBox com_fundus_check;
    private javax.swing.JComboBox com_gestation;
    private javax.swing.JComboBox com_hhs;
    private javax.swing.JComboBox com_hypoglycemia;
    private javax.swing.JComboBox com_insulin;
    private javax.swing.JComboBox com_kidney;
    private javax.swing.JComboBox com_neuropathy;
    private javax.swing.JComboBox com_oral_hypoglycemic;
    private javax.swing.JComboBox com_paod;
    private javax.swing.JComboBox com_self_care;
    private javax.swing.JComboBox com_sport;
    private javax.swing.JComboBox com_stroke;
    private cc.johnwu.date.DateComboBox dateComboBox;
    private javax.swing.JDialog dia;
    private javax.swing.JDialog dia_RevisitTime;
    private javax.swing.JFormattedTextField ftf_drink_aweek;
    private javax.swing.JFormattedTextField ftf_eye_lvision;
    private javax.swing.JFormattedTextField ftf_eye_rvision;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel158;
    private javax.swing.JLabel jLabel159;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel161;
    private javax.swing.JLabel jLabel162;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel182;
    private javax.swing.JLabel jLabel183;
    private javax.swing.JLabel jLabel184;
    private javax.swing.JLabel jLabel185;
    private javax.swing.JLabel jLabel186;
    private javax.swing.JLabel jLabel187;
    private javax.swing.JLabel jLabel188;
    private javax.swing.JLabel jLabel189;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel190;
    private javax.swing.JLabel jLabel191;
    private javax.swing.JLabel jLabel192;
    private javax.swing.JLabel jLabel194;
    private javax.swing.JLabel jLabel195;
    private javax.swing.JLabel jLabel196;
    private javax.swing.JLabel jLabel197;
    private javax.swing.JLabel jLabel198;
    private javax.swing.JLabel jLabel199;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel200;
    private javax.swing.JLabel jLabel201;
    private javax.swing.JLabel jLabel202;
    private javax.swing.JLabel jLabel203;
    private javax.swing.JLabel jLabel204;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lab_Age;
    private javax.swing.JLabel lab_Gender;
    private javax.swing.JLabel lab_Name;
    private javax.swing.JLabel lab_Pno;
    private javax.swing.JLabel lab_bgac;
    private javax.swing.JLabel lab_dm;
    private javax.swing.JLabel lab_hdl;
    private javax.swing.JLabel lab_sbp;
    private javax.swing.JLabel lab_tg;
    private javax.swing.JList list_Menu;
    private javax.swing.JMenu menu_SetDM;
    private javax.swing.JMenu mn_Fiele;
    private javax.swing.JMenuBar mnb;
    private javax.swing.JMenuItem mnit_Close;
    private javax.swing.JMenuItem mnit_History;
    private javax.swing.JMenuItem mnit_Lab;
    private javax.swing.JMenuItem mnit_V1;
    private javax.swing.JMenuItem mnit_V2;
    private javax.swing.JMenuItem mnit_V3;
    private javax.swing.JPanel pan_Ass;
    private javax.swing.JPanel pan_Prescription;
    private javax.swing.JRadioButton rad_angina_no;
    private javax.swing.JRadioButton rad_angina_yes;
    private javax.swing.JRadioButton rad_claudication_no;
    private javax.swing.JRadioButton rad_claudication_yes;
    private javax.swing.JRadioButton rad_peripheral_neuropathy_no;
    private javax.swing.JRadioButton rad_peripheral_neuropathy_yes;
    private javax.swing.JRadioButton rad_postural_hypotension_no;
    private javax.swing.JRadioButton rad_postural_hypotension_yes;
    private javax.swing.JScrollPane span_ListMenu;
    private javax.swing.JScrollPane span_Prescription;
    private javax.swing.JSpinner spi_abortions_count;
    private javax.swing.JSpinner spi_bloodtest_aweek;
    private javax.swing.JSpinner spi_gestation_count;
    private javax.swing.JSpinner spi_smoke_aday;
    private javax.swing.JSpinner spi_urine_aweek;
    private javax.swing.JTable tab_HealthTeach;
    private javax.swing.JTable tab_MedicineTeach;
    private javax.swing.JTable tab_Prescription;
    private javax.swing.JTextField txt_AC;
    private javax.swing.JTextField txt_ComeBackDays;
    private javax.swing.JTextField txt_Height;
    private javax.swing.JTextField txt_PC;
    private javax.swing.JTextField txt_PackageId;
    private javax.swing.JTextField txt_PackageType;
    private javax.swing.JTextField txt_ST;
    private javax.swing.JTextField txt_Weight;
    private javax.swing.JTextField txt_bmi;
    private javax.swing.JTextField txt_dbp;
    private javax.swing.JTextField txt_dm_typeo;
    private javax.swing.JTextField txt_other;
    private javax.swing.JTextField txt_sbp;
    private javax.swing.JTextField txt_waist;
    // End of variables declaration//GEN-END:variables
     @Override
    public void onDateChanged() {

    }
   
}
