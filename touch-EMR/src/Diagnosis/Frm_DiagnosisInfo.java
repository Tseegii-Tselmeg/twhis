package Diagnosis;

import Case.Frm_Case;
import Diagnosis.TableTriStateCell.TriStateCellEditor;
import Diagnosis.TableTriStateCell.TriStateCellRenderer;
import ErrorMessage.StoredErrorMessage;
import Laboratory.Frm_LabDM;
import Laboratory.Frm_LabHistory;
import cc.johnwu.sql.*;
import Multilingual.language;
import System.Setting;
import Common.*;
import WorkList.Frm_WorkList;
import Radiology.Frm_RadiologyHistory;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.text.ParseException;
import java.util.Vector;
import cc.johnwu.login.UserInfo;
import cc.johnwu.date.DateMethod;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.DefaultCellEditor;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.print.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class Frm_DiagnosisInfo extends javax.swing.JFrame implements DiagnosisInterface {

//    private final String SYSTEMNAME = "Diagnosis";   //系統名稱

    private final int AUTOCOMPLETE_HEIGHT = 160;    // 預設 AUTOCOMPLETE height
    private final int AUTOCOMPLETE_SHOW_ROW = 10;   // 限制顯示出的最大筆數

    private String m_Pno;               // patient's p_no
    private String m_RegistrationGuid;  // registration guid
    private boolean m_FinishState;      //
    private boolean m_IsDM = false;     // DM start
    private int m_WorkListRowNo;        // Frm_DiagnosisWorkList's stop rowNo

    private DefaultTableModel m_DiagnosisModel;
    private DefaultTableModel m_PrescriptionModel;
    private DefaultTableModel m_MedicineModel;
    private int m_DiagnosisRowNo;      // Diagnosis table row number
    private int m_PrescriptionRowNo;   // Prescription table row number
    private int m_MedicineRowNo;       // MedicineRowNo table row number
    private Map<Object, Object> m_DiagnosisHashMap = new LinkedHashMap<Object, Object>();    // 儲存Diagnosis看診資料 用來比對避免重複相同診斷
    private Map<Object, Object> m_MedicineHashMap = new LinkedHashMap<Object, Object>();     // 儲存Medicine看診資料 用來比對避免重複相同診斷
    private Map<Object, Object> m_PrescriptionHashMap = new LinkedHashMap<Object, Object>(); // 儲存Prescription看診資料 用來比對避免重複相同診斷
    private Map<Object, Object> m_AllergyHashMap = new LinkedHashMap<Object, Object>();      // 儲存病患過敏 key為藥品code  value為程度
    private Map<Object, Object> m_UsageHashMap = new LinkedHashMap<Object, Object>();        // 儲存服法代碼 value為天數
    private Map<Object, Object> m_WayHashMap = new LinkedHashMap<Object, Object>();          // 儲存途徑代碼 value還是途徑

    // focus 的 table 成為目前系統的暫存變數
    private JTable m_SelectTable = null;                  
    private DefaultTableModel m_SelectTableModel = null;  
    private int m_SelectTableRowNo = 0;
    private int m_SelectTableNo;
    private int m_InsertRow;
    private int m_SelectTableAddRowLimitColumn; // TABLE 按下 ENTER 判斷是否允許增加新行的欄位
    private Map<Object, Object> m_SelectTableHashMap = new LinkedHashMap<Object, Object>();

   //---AUTO COMPLETE
    private String m_AutoTable;
    private String[] m_AutoColumn;   // AutoComplete 印出的欄位
    private String m_AutoColumnName; // 資料庫比對的Column name
    private JTextField m_AutoTxt = new JTextField();
    private JScrollPane m_AutoLocationSpane;
    private String[] m_AutoListValue;
    private int m_RsRowCount;
    private int m_Row;
    private String m_OsGuid;
    /*多國語言變數*/
    private language paragraph = new language();
    private String[] line = paragraph.setlanguage("DIAGNOSISINFO").split("\n") ;
    private String[] message = paragraph.setlanguage("MESSAGE").split("\n") ;
    /*輸出錯誤資訊變數*/
    private StoredErrorMessage ErrorMessage = new StoredErrorMessage() ;
    /*是否使用套餐記錄*/
    private String m_PackageSet;
    private String m_PackageSetId;
    private int m_PackageSetDay = 0;
    private boolean m_IsFirst;

    
    public Frm_DiagnosisInfo() {}

    // 參數：病患編號  掛號guid  worklist停留行號  看診狀態 是否為初診
    public Frm_DiagnosisInfo(String p_no, String regGuid, int stopRowNo, boolean finishState, boolean isFirst ) {
        this.m_Pno = p_no;
        this.m_RegistrationGuid = regGuid;
        this.m_WorkListRowNo = stopRowNo;
        this.m_FinishState = finishState;
        m_IsFirst = isFirst;
        initComponents();
        initTable();  // 需擺在 initFrame() 之前
        initTableColumn();
        initFrame();
        initHashMap();  // 藥品代碼儲存
        initLanguage() ;
    }

    // 取出病患資料 與 確認是否讀取上次看診資料
    public void initFrame() {
        this.setExtendedState(Frm_DiagnosisInfo.MAXIMIZED_BOTH);  // 最大化
        this.setLocationRelativeTo(this);
        this.tab_Diagnosis.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);     // tabble不可按住多選
        this.tab_Prescription.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  // tabble不可按住多選
        this.tab_Medicine.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);      // tabble不可按住多選

        addWindowListener(new WindowAdapter() {  // 畫面關閉原視窗enable
            @Override
            public void windowClosing(WindowEvent windowevent) {
                mnit_BackActionPerformed(null);
            }
        });

        dia_RevisitTime.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowevent) {
              setBackWorklist();
            }
        });
        setMoveBtnEnable();
        txta_Summary.setLineWrap(true);  // txta_Summary 自動換行
        getAllergy();                    // 取出病患過敏資料
        ResultSet rs = null;
        ResultSet rsRecord = null;
        if (UserInfo.getUserPoliclinicType().equals("DM") ) m_IsDM = true;


        String sql = "SELECT * FROM patients_info WHERE p_no = '" + this.m_Pno+"'";
        String sqlRecord =
                    "SELECT shift_table.shift_date AS date, " +
                          "concat(staff_info.firstname,'  ',staff_info.lastname) AS name, " +
                          "registration_info.guid, outpatient_services.summary, outpatient_services.ps, " +
                          "registration_info.reg_time " +
                    "FROM registration_info, shift_table, policlinic, poli_room, staff_info, outpatient_services  " +
                    "WHERE registration_info.p_no = '"+m_Pno+"' " +
                    "AND policlinic.name = '"+UserInfo.getUserPoliclinic()+"' " +
                    "AND registration_info.shift_guid = shift_table.guid " +
                    "AND shift_table.room_guid = poli_room.guid " +
                    "AND poli_room.poli_guid = policlinic.guid " +
                    "AND staff_info.s_id = shift_table.s_id " +
                    "AND registration_info.finish = 'F' " +
                    "AND outpatient_services.reg_guid = registration_info.guid " +
                    "ORDER BY registration_info.reg_time DESC";
        try {
            // 取出病患基本資料
            rs = DBC.executeQuery(sql);
            rs.next();
            this.txt_No.setText(rs.getString("p_no"));
            this.txt_Name.setText(rs.getString("firstname")+" "+rs.getString("lastname"));
            this.txt_Sex.setText(rs.getString("gender"));
            this.txt_Age.setText(DateMethod.getAgeWithMonth(rs.getDate("birth")));
            this.txt_Bloodtype.setText(rs.getString("bloodtype") + " " + rs.getString("rh_type"));
            this.txt_Height.setText(rs.getString("height"));
            this.txt_Weight.setText(rs.getString("weight"));
            this.txt_Ps.setText(rs.getString("ps"));

            // ---------確認是否讀取病患上次在該診別的看診資料--------------------------
            rsRecord = DBC.executeQuery(sqlRecord);
     
            if (rsRecord.next()) {
                if (m_FinishState == true) {
                    // 修改病歷
                    setEditCasehistory(rsRecord.getString("summary"), m_RegistrationGuid, rsRecord.getString("date") +" "+ rsRecord.getString("name"));
                    txt_Message.setText(rsRecord.getString("ps"));
                } else if (m_FinishState == false) {
                    // 糖尿病判斷
                    if (UserInfo.getUserPoliclinicType().equals("DM") ) {
                    

                        String msg = "";
                        msg = Tools.getDM(m_Pno);
                        if (msg.equals("")) {
                            msg = Tools.getPREDM(m_Pno);

                            if (msg.equals("")) {

                               msg = Tools.getCheckDM(m_Pno, txt_Sex.getText());
                            }
                        }

                        if (!msg.equals("")) {
                            JOptionPane.showMessageDialog(null,msg);
                            txta_Summary.setText(msg + txta_Summary.getText());
                        }
                    } else {
                        menu_SetDM.setVisible(false);
                        mnit_LabHistoryDM.setVisible(false);
                        mnit_Complication.setVisible(false);
                        btn_CaseManagement.setVisible(false);
                    }

                    
                    // 取出看診資料
                Object[] options = {paragraph.getLanguage(message , "YES"),paragraph.getLanguage(message , "NO")};
                int dialog = JOptionPane.showOptionDialog(
                            null,
                            rsRecord.getString("date") +"  "+rsRecord.getString("name")+paragraph.getLanguage(line, "DOCTOR")+" \n Do you want to review Diagnosis of the patient data?",
                            paragraph.getLanguage(message, "MESSAGE"),
                            JOptionPane.YES_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options,
                            options[0]
                        );
                    if (dialog == 0) {
                        getCasehistory(rsRecord.getString("summary"),  rsRecord.getString("guid"));
                    }

                }
            } else {
                if (m_IsFirst) {
                    this.setEnabled(false);
                    new Frm_DiagnosisAllergy(this, this.txt_No.getText(), this.txt_Name.getText()).setVisible(true);
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(Frm_DiagnosisInfo.class.getName()).log(Level.SEVERE, null, e);
                ErrorMessage.setData("Diagnosis", "Frm_DiagnosisInfo" ,"initFrame()",
                        e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        } finally {
            try {DBC.closeConnection(rs);}
            catch (SQLException e){
                ErrorMessage.setData("Diagnosis", "Frm_DiagnosisInfo" ,"initFrame() - DBC.closeConnection(rs)",
                        e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            }
        }
    }

    private void setBackWorklist() {
         Frm_WorkList frm_diagnosisWorkList = new Frm_WorkList(m_WorkListRowNo, "dia");
         frm_diagnosisWorkList.setVisible(true);
         this.dispose();
    }
    // 藥品代碼儲存
    public void initHashMap() {
        // 服法代碼
        // 存入代碼與天數
        m_UsageHashMap.put("QW",1);
        m_UsageHashMap.put("BIW",2);
        m_UsageHashMap.put("TIW",3);
        m_UsageHashMap.put("STAT",1);
        m_UsageHashMap.put("ASORDER",1);
        m_UsageHashMap.put("QW1",1);
        m_UsageHashMap.put("QW2",2);
        m_UsageHashMap.put("QW3",3);
        m_UsageHashMap.put("QW4",4);
        m_UsageHashMap.put("QW5",5);
        m_UsageHashMap.put("QW6",6);
        m_UsageHashMap.put("QW7",7);
        m_UsageHashMap.put("Q1H",1);
        m_UsageHashMap.put("Q2H",2);
        m_UsageHashMap.put("Q3H",3);
        m_UsageHashMap.put("Q4H",4);
        m_UsageHashMap.put("Q5H",5);
        m_UsageHashMap.put("Q6H",6);
        m_UsageHashMap.put("Q7H",7);
        m_UsageHashMap.put("Q8H",8);
        m_UsageHashMap.put("Q9H",9);
        m_UsageHashMap.put("Q10H",10);
        m_UsageHashMap.put("Q11H",11);
        m_UsageHashMap.put("Q12H",12);
        m_UsageHashMap.put("Q13H",13);
        m_UsageHashMap.put("Q14H",14);
        m_UsageHashMap.put("Q15H",15);
        m_UsageHashMap.put("Q16H",16);
        m_UsageHashMap.put("Q17H",17);
        m_UsageHashMap.put("Q18H",18);
        m_UsageHashMap.put("Q19H",19);
        m_UsageHashMap.put("Q20H",20);
        m_UsageHashMap.put("Q21H",21);
        m_UsageHashMap.put("Q22H",22);
        m_UsageHashMap.put("Q23H",23);
        m_UsageHashMap.put("Q24H",24);
        m_UsageHashMap.put("Q1MN",1);
        m_UsageHashMap.put("Q2MN",2);
        m_UsageHashMap.put("Q3MN",3);
        m_UsageHashMap.put("Q4MN",4);
        m_UsageHashMap.put("Q5MN",5);
        m_UsageHashMap.put("Q6MN",6);
        m_UsageHashMap.put("Q7MN",7);
        m_UsageHashMap.put("Q8MN",8);
        m_UsageHashMap.put("Q9MN",9);
        m_UsageHashMap.put("Q10MN",10);
        m_UsageHashMap.put("Q11MN",11);
        m_UsageHashMap.put("Q12MN",12);
        m_UsageHashMap.put("Q13MN",13);
        m_UsageHashMap.put("Q14MN",14);
        m_UsageHashMap.put("Q15MN",15);
        m_UsageHashMap.put("Q16MN",16);
        m_UsageHashMap.put("Q17MN",17);
        m_UsageHashMap.put("Q18MN",18);
        m_UsageHashMap.put("Q19MN",19);
        m_UsageHashMap.put("Q20MN",20);
        m_UsageHashMap.put("Q21MN",21);
        m_UsageHashMap.put("Q22MN",22);
        m_UsageHashMap.put("Q23MN",23);
        m_UsageHashMap.put("Q24MN",24);
        m_UsageHashMap.put("Q25MN",25);
        m_UsageHashMap.put("Q26MN",26);
        m_UsageHashMap.put("Q27MN",27);
        m_UsageHashMap.put("Q28MN",28);
        m_UsageHashMap.put("Q29MN",29);
        m_UsageHashMap.put("Q30MN",30);
        m_UsageHashMap.put("Q31MN",31);
        m_UsageHashMap.put("Q32MN",32);
        m_UsageHashMap.put("Q33MN",33);
        m_UsageHashMap.put("Q34MN",34);
        m_UsageHashMap.put("Q35MN",35);
        m_UsageHashMap.put("Q36MN",36);
        m_UsageHashMap.put("Q37MN",37);
        m_UsageHashMap.put("Q38MN",38);
        m_UsageHashMap.put("Q39MN",39);
        m_UsageHashMap.put("Q40MN",40);
        m_UsageHashMap.put("Q41MN",41);
        m_UsageHashMap.put("Q42MN",42);
        m_UsageHashMap.put("Q43MN",43);
        m_UsageHashMap.put("Q44MN",44);
        m_UsageHashMap.put("Q45MN",45);
        m_UsageHashMap.put("Q46MN",46);
        m_UsageHashMap.put("Q47MN",47);
        m_UsageHashMap.put("Q48MN",48);
        m_UsageHashMap.put("Q49MN",49);
        m_UsageHashMap.put("Q50MN",50);
        m_UsageHashMap.put("Q51MN",51);
        m_UsageHashMap.put("Q52MN",52);
        m_UsageHashMap.put("Q53MN",53);
        m_UsageHashMap.put("Q54MN",54);
        m_UsageHashMap.put("Q55MN",55);
        m_UsageHashMap.put("Q56MN",56);
        m_UsageHashMap.put("Q57MN",57);
        m_UsageHashMap.put("Q58MN",58);
        m_UsageHashMap.put("Q59MN",59);
        m_UsageHashMap.put("Q60MN",60);
        m_UsageHashMap.put("QD",1);
        m_UsageHashMap.put("QDAM",1);
        m_UsageHashMap.put("QDPM",1);
        m_UsageHashMap.put("QDHS",1);
        m_UsageHashMap.put("QN",1);
        m_UsageHashMap.put("BID",2);
        m_UsageHashMap.put("QAM&HS",2);
        m_UsageHashMap.put("QPM&HS",2);
        m_UsageHashMap.put("QAM&PM",2);
        m_UsageHashMap.put("TID",3);
        m_UsageHashMap.put("BID&HS",3);
        m_UsageHashMap.put("QID",4);
        m_UsageHashMap.put("HS",1);
        m_UsageHashMap.put("TID&HS",4);
        m_UsageHashMap.put("1W1D",1);
        m_UsageHashMap.put("1W2D",2);
        m_UsageHashMap.put("1W3D",3);
        m_UsageHashMap.put("1W4D",4);
        m_UsageHashMap.put("1W5D",5);
        m_UsageHashMap.put("1W6D",6);
        m_UsageHashMap.put("1W7D",7);
        m_UsageHashMap.put("1W8D",8);
        m_UsageHashMap.put("1W9D",9);
        m_UsageHashMap.put("2W1D",2);
        m_UsageHashMap.put("2W2D",4);
        m_UsageHashMap.put("2W3D",6);
        m_UsageHashMap.put("2W4D",8);
        m_UsageHashMap.put("2W5D",10);
        m_UsageHashMap.put("2W6D",12);
        m_UsageHashMap.put("2W7D",14);
        m_UsageHashMap.put("2W8D",16);
        m_UsageHashMap.put("2W9D",18);
        m_UsageHashMap.put("3W1D",3);
        m_UsageHashMap.put("3W2D",6);
        m_UsageHashMap.put("3W3D",9);
        m_UsageHashMap.put("3W4D",12);
        m_UsageHashMap.put("3W5D",15);
        m_UsageHashMap.put("3W6D",18);
        m_UsageHashMap.put("3W7D",21);
        m_UsageHashMap.put("3W8D",24);
        m_UsageHashMap.put("3W9D",27);
        m_UsageHashMap.put("4W1D",4);
        m_UsageHashMap.put("4W2D",8);
        m_UsageHashMap.put("4W3D",12);
        m_UsageHashMap.put("4W4D",16);
        m_UsageHashMap.put("4W5D",20);
        m_UsageHashMap.put("4W6D",24);
        m_UsageHashMap.put("4W7D",28);
        m_UsageHashMap.put("4W8D",32);
        m_UsageHashMap.put("4W9D",36);
        m_UsageHashMap.put("5W1D",5);
        m_UsageHashMap.put("5W2D",10);
        m_UsageHashMap.put("5W3D",15);
        m_UsageHashMap.put("5W4D",20);
        m_UsageHashMap.put("5W5D",25);
        m_UsageHashMap.put("5W6D",30);
        m_UsageHashMap.put("5W7D",35);
        m_UsageHashMap.put("5W8D",40);
        m_UsageHashMap.put("5W9D",45);
        m_UsageHashMap.put("6W1D",6);
        m_UsageHashMap.put("6W2D",12);
        m_UsageHashMap.put("6W3D",18);
        m_UsageHashMap.put("6W4D",24);
        m_UsageHashMap.put("6W5D",30);
        m_UsageHashMap.put("6W6D",36);
        m_UsageHashMap.put("6W7D",42);
        m_UsageHashMap.put("6W8D",48);
        m_UsageHashMap.put("6W9D",54);
        m_UsageHashMap.put("7W1D",7);
        m_UsageHashMap.put("7W2D",14);
        m_UsageHashMap.put("7W3D",21);
        m_UsageHashMap.put("7W4D",24);
        m_UsageHashMap.put("7W5D",35);
        m_UsageHashMap.put("7W6D",42);
        m_UsageHashMap.put("7W7D",49);
        m_UsageHashMap.put("7W8D",56);
        m_UsageHashMap.put("7W9D",63);
        m_UsageHashMap.put("8W1D",8);
        m_UsageHashMap.put("8W2D",16);
        m_UsageHashMap.put("8W3D",24);
        m_UsageHashMap.put("8W4D",32);
        m_UsageHashMap.put("8W5D",40);
        m_UsageHashMap.put("8W6D",48);
        m_UsageHashMap.put("8W7D",56);
        m_UsageHashMap.put("8W8D",64);
        m_UsageHashMap.put("8W9D",72);
        m_UsageHashMap.put("9W1D",9);
        m_UsageHashMap.put("9W2D",18);
        m_UsageHashMap.put("9W3D",27);
        m_UsageHashMap.put("9W4D",36);
        m_UsageHashMap.put("9W5D",45);
        m_UsageHashMap.put("9W6D",54);
        m_UsageHashMap.put("9W7D",63);
        m_UsageHashMap.put("9W8D",72);
        m_UsageHashMap.put("9W9D",81);
        m_UsageHashMap.put("MCD1D1",1);
        m_UsageHashMap.put("MCD1D2",1);
        m_UsageHashMap.put("MCD1D3",1);
        m_UsageHashMap.put("MCD1D4",1);
        m_UsageHashMap.put("MCD1D5",1);
        m_UsageHashMap.put("MCD1D6",1);
        m_UsageHashMap.put("MCD1D7",1);
        m_UsageHashMap.put("MCD1D8",1);
        m_UsageHashMap.put("MCD1D9",1);
        m_UsageHashMap.put("MCD2D1",1);
        m_UsageHashMap.put("MCD2D2",1);
        m_UsageHashMap.put("MCD2D3",1);
        m_UsageHashMap.put("MCD2D4",1);
        m_UsageHashMap.put("MCD2D5",1);
        m_UsageHashMap.put("MCD2D6",1);
        m_UsageHashMap.put("MCD2D7",1);
        m_UsageHashMap.put("MCD2D8",1);
        m_UsageHashMap.put("MCD2D9",1);
        m_UsageHashMap.put("MCD3D1",1);
        m_UsageHashMap.put("MCD3D2",1);
        m_UsageHashMap.put("MCD3D3",1);
        m_UsageHashMap.put("MCD3D4",1);
        m_UsageHashMap.put("MCD3D5",1);
        m_UsageHashMap.put("MCD3D6",1);
        m_UsageHashMap.put("MCD3D7",1);
        m_UsageHashMap.put("MCD3D8",1);
        m_UsageHashMap.put("MCD3D9",1);
        m_UsageHashMap.put("MCD4D1",1);
        m_UsageHashMap.put("MCD4D2",1);
        m_UsageHashMap.put("MCD4D3",1);
        m_UsageHashMap.put("MCD4D4",1);
        m_UsageHashMap.put("MCD4D5",1);
        m_UsageHashMap.put("MCD4D6",1);
        m_UsageHashMap.put("MCD4D7",1);
        m_UsageHashMap.put("MCD4D8",1);
        m_UsageHashMap.put("MCD4D9",1);
        m_UsageHashMap.put("MCD5D1",1);
        m_UsageHashMap.put("MCD5D2",1);
        m_UsageHashMap.put("MCD5D3",1);
        m_UsageHashMap.put("MCD5D4",1);
        m_UsageHashMap.put("MCD5D5",1);
        m_UsageHashMap.put("MCD5D6",1);
        m_UsageHashMap.put("MCD5D7",1);
        m_UsageHashMap.put("MCD5D8",1);
        m_UsageHashMap.put("MCD5D9",1);
        m_UsageHashMap.put("MCD6D1",1);
        m_UsageHashMap.put("MCD6D2",1);
        m_UsageHashMap.put("MCD6D3",1);
        m_UsageHashMap.put("MCD6D4",1);
        m_UsageHashMap.put("MCD6D5",1);
        m_UsageHashMap.put("MCD6D6",1);
        m_UsageHashMap.put("MCD6D7",1);
        m_UsageHashMap.put("MCD6D8",1);
        m_UsageHashMap.put("MCD6D9",1);
        m_UsageHashMap.put("MCD7D1",1);
        m_UsageHashMap.put("MCD7D2",1);
        m_UsageHashMap.put("MCD7D3",1);
        m_UsageHashMap.put("MCD7D4",1);
        m_UsageHashMap.put("MCD7D5",1);
        m_UsageHashMap.put("MCD7D6",1);
        m_UsageHashMap.put("MCD7D7",1);
        m_UsageHashMap.put("MCD7D8",1);
        m_UsageHashMap.put("MCD7D9",1);
        m_UsageHashMap.put("MCD8D1",1);
        m_UsageHashMap.put("MCD8D2",1);
        m_UsageHashMap.put("MCD8D3",1);
        m_UsageHashMap.put("MCD8D4",1);
        m_UsageHashMap.put("MCD8D5",1);
        m_UsageHashMap.put("MCD8D6",1);
        m_UsageHashMap.put("MCD8D7",1);
        m_UsageHashMap.put("MCD8D8",1);
        m_UsageHashMap.put("MCD8D9",1);
        m_UsageHashMap.put("MCD9D1",1);
        m_UsageHashMap.put("MCD9D2",1);
        m_UsageHashMap.put("MCD9D3",1);
        m_UsageHashMap.put("MCD9D4",1);
        m_UsageHashMap.put("MCD9D5",1);
        m_UsageHashMap.put("MCD9D6",1);
        m_UsageHashMap.put("MCD9D7",1);
        m_UsageHashMap.put("MCD9D8",1);
        m_UsageHashMap.put("MCD9D9",1);
        // 途徑代碼 可存用法
        m_WayHashMap.put("AD","AD");
        m_WayHashMap.put("AS","AS");
        m_WayHashMap.put("AU","AU");
        m_WayHashMap.put("ET","ET");
        m_WayHashMap.put("GAR","GAR");
        m_WayHashMap.put("IC","IC");
        m_WayHashMap.put("IA","IA");
        m_WayHashMap.put("IM","IM");
        m_WayHashMap.put("IV","IV");
        m_WayHashMap.put("IP","IP");
        m_WayHashMap.put("ICV","ICV");
        m_WayHashMap.put("IMP","IMP");
        m_WayHashMap.put("INHL","INHL");
        m_WayHashMap.put("IS","IS");
        m_WayHashMap.put("IT","IT");
        m_WayHashMap.put("IVA","IVA");
        m_WayHashMap.put("IVD","IVD");
        m_WayHashMap.put("IVP","IVP");
        m_WayHashMap.put("LA","LA");
        m_WayHashMap.put("LI","LI");
        m_WayHashMap.put("NA","NA");
        m_WayHashMap.put("OD","OD");
        m_WayHashMap.put("OS","OS");
        m_WayHashMap.put("OU","OU");
        m_WayHashMap.put("PO","PO");
        m_WayHashMap.put("SC","SC");
        m_WayHashMap.put("SCI","SCI");
        m_WayHashMap.put("SKIN","SKIN");
        m_WayHashMap.put("SL","SL");
        m_WayHashMap.put("SPI","SPI");
        m_WayHashMap.put("RECT","RECT");
        m_WayHashMap.put("TOPI","TOPI");
        m_WayHashMap.put("TPN","TPN");
        m_WayHashMap.put("VAG","VAG");
        m_WayHashMap.put("IRRI","IRRI");
        m_WayHashMap.put("EXT","EXT");
        m_WayHashMap.put("XX","XX");

    }

    // 建立 TABLE
    public void initTable() {
           Object getModelAndRowNo[] = new Object[1];  // getModelAndRowNo[0] get model   getModelAndRowNo[1] get rowNo
           //-----tab_Diagnosis----------------------------------------------------
           String[] diagnosisTitle = {" ",paragraph.getLanguage(line,"CODE"),paragraph.getLanguage(line,"DIAGNOSIS")};   // table表頭
           int[] diagnosisColumnEditable = {1};              // 可編輯欄位
           getModelAndRowNo = TabTools.setTableEditColumn(m_DiagnosisModel, this.tab_Diagnosis, diagnosisTitle,
                                                     diagnosisColumnEditable, m_DiagnosisRowNo);
           m_DiagnosisModel = (DefaultTableModel) getModelAndRowNo[0];
           m_DiagnosisRowNo = Integer.parseInt(getModelAndRowNo[1].toString());
           //-----tab_Prescription-------------------------------------------------
           String[] prescriptionTitle = {" ", "Code","Item","Body Part","Type"};   // table表頭
           int[] prescriptionColumnEditable = {1,3};     // 可編輯欄位
           getModelAndRowNo = TabTools.setTableEditColumn(m_PrescriptionModel, this.tab_Prescription, prescriptionTitle,
                                                     prescriptionColumnEditable, m_PrescriptionRowNo);
           m_PrescriptionModel = (DefaultTableModel) getModelAndRowNo[0];
           m_PrescriptionRowNo = Integer.parseInt(getModelAndRowNo[1].toString());
           //-----tab_Medicine-----------------------------------------------------
           String[] medicineTitle = {" ","Item","Code","Injection",
                                   "Dosage", paragraph.getLanguage(line,"UNIT"),
                                   "Frequency" ,"Usage" ,
                                   "Duration" ,paragraph.getLanguage(line,"QUANTITLY"),
                                   paragraph.getLanguage(line,"URGENT"),"Powder",
                                   paragraph.getLanguage(line,"PS"),"隱藏服法的次數"};
           int[] medicineColumnEditable = {1,4, 5,6,7,8,9,10,11,12};
           getModelAndRowNo = TabTools.setTableEditColumn(m_MedicineModel, this.tab_Medicine, medicineTitle,
                                                     medicineColumnEditable, m_MedicineRowNo);
           m_MedicineModel = (DefaultTableModel) getModelAndRowNo[0];
           m_MedicineRowNo = Integer.parseInt(getModelAndRowNo[1].toString());

         
    }

    // 設定 TABLE 攔寬
    public void initTableColumn() {

            TableColumn diagnosisColumnNo = tab_Diagnosis.getColumnModel().getColumn(0);
            TableColumn diagnosisColumnIcdCode = tab_Diagnosis.getColumnModel().getColumn(1);
            TableColumn diagnosisColumnName = tab_Diagnosis.getColumnModel().getColumn(2);

            TableColumn prescriptionColumnNo = tab_Prescription.getColumnModel().getColumn(0);
            TableColumn prescriptionColumnCode = tab_Prescription.getColumnModel().getColumn(1);
            TableColumn prescriptionColumnName = tab_Prescription.getColumnModel().getColumn(2);
            TableColumn prescriptionColumnPlace = tab_Prescription.getColumnModel().getColumn(3);
            TableColumn prescriptionColumnType = tab_Prescription.getColumnModel().getColumn(4);

            TableColumn medicineColumnNo = tab_Medicine.getColumnModel().getColumn(0);
            TableColumn medicineColumnItem = tab_Medicine.getColumnModel().getColumn(1);
            TableColumn medicineColumnCode = tab_Medicine.getColumnModel().getColumn(2);
            TableColumn medicineColumnInjection = tab_Medicine.getColumnModel().getColumn(3);
            TableColumn medicineColumnDosage = tab_Medicine.getColumnModel().getColumn(4);
            TableColumn medicineColumnUnit = tab_Medicine.getColumnModel().getColumn(5);
            TableColumn medicineColumnUsage = tab_Medicine.getColumnModel().getColumn(6);
            TableColumn medicineColumnWay = tab_Medicine.getColumnModel().getColumn(7);
            TableColumn medicineColumnDay = tab_Medicine.getColumnModel().getColumn(8);
            TableColumn medicineColumnQuantity = tab_Medicine.getColumnModel().getColumn(9);
            TableColumn medicineColumnUrgent = tab_Medicine.getColumnModel().getColumn(10);
            TableColumn medicineColumnPowder = tab_Medicine.getColumnModel().getColumn(11);
            TableColumn medicineColumnPs = tab_Medicine.getColumnModel().getColumn(12);

            diagnosisColumnNo.setMaxWidth(30);
            diagnosisColumnIcdCode.setMaxWidth(80);
            diagnosisColumnName.setPreferredWidth(650);

            prescriptionColumnNo.setMaxWidth(30);
            prescriptionColumnCode.setMaxWidth(80);
            prescriptionColumnName.setPreferredWidth(650);
            prescriptionColumnPlace.setPreferredWidth(80);

            medicineColumnNo.setMaxWidth(30);
            medicineColumnInjection.setPreferredWidth(270);
            medicineColumnItem.setPreferredWidth(270);
            medicineColumnDosage.setPreferredWidth(70);
            medicineColumnUnit.setPreferredWidth(0);
            medicineColumnUsage.setMaxWidth(70);
            medicineColumnWay.setMaxWidth(70);
            medicineColumnDay.setMaxWidth(70);
            medicineColumnQuantity.setMaxWidth(100);
            medicineColumnUrgent.setMaxWidth(70);
            medicineColumnPowder.setMaxWidth(70);
            medicineColumnPs.setPreferredWidth(150);

            Common.TabTools.setHideColumn(tab_Prescription, 3);  //隱藏看診部位
            Common.TabTools.setHideColumn(tab_Medicine, 2);
            Common.TabTools.setHideColumn(tab_Medicine, 5);  // Medicine hide
            Common.TabTools.setHideColumn(tab_Medicine, 13);  // Medicine hide
            tab_Diagnosis.setRowHeight(30);
            tab_Prescription.setRowHeight(30);
            tab_Medicine.setRowHeight(30);
            diagnosisColumnIcdCode.setCellEditor(new DefaultCellEditor(m_AutoTxt));     // textField加入table
            prescriptionColumnCode.setCellEditor(new DefaultCellEditor(m_AutoTxt));     // textField加入table
            medicineColumnItem.setCellEditor(new DefaultCellEditor(m_AutoTxt));         // textField加入table
    }

    private void initLanguage() {
        //this.lab_TitleNo.setText(paragraph.getLanguage(line, "TITLENO"));
        this.lab_TitleName.setText(paragraph.getLanguage(line, "TITLENAME"));
        this.lab_Height.setText(paragraph.getLanguage(line, "HEIGHT"));
        this.lab_TitlePs.setText(paragraph.getLanguage(line, "TITLEPS"));
        this.lab_TitleSex.setText(paragraph.getLanguage(line, "TITLESEX"));
        this.lab_TitleAge.setText(paragraph.getLanguage(line, "TITLEAGE"));
        this.lab_TitleBloodtype.setText(paragraph.getLanguage(line, "TITLEBLOODTYPE"));
        this.lab_Weight.setText(paragraph.getLanguage(line, "WEIGHT"));
        this.lab_Ps.setText(paragraph.getLanguage(line, "PS"));

        this.btn_Diagnosis.setText(paragraph.getLanguage(line, "DIAGNOSIS"));
        //this.btn_Prescription.setText(paragraph.getLanguage(line, "PRESCRIPTIONCODE"));
        this.btn_Medicine.setText(paragraph.getLanguage(line, "MEDICINE"));
        //this.mnit_LabHistoryDM.setText(paragraph.getLanguage(line, "CASEHISTORY"));
        this.btn_Allergy.setText(paragraph.getLanguage(line, "ALLERGY"));
        this.btn_Send.setText(paragraph.getLanguage(line, "SEND"));
     
        this.pan_CenterTop.setBorder(javax.swing.BorderFactory.createTitledBorder(paragraph.getLanguage(line, "MEDICALRECORDSUMMARY")));
        this.pan_Diagnosis.setBorder(javax.swing.BorderFactory.createTitledBorder(paragraph.getLanguage(line, "DIAGNSIS")));
        //this.pan_Prescription.setBorder(javax.swing.BorderFactory.createTitledBorder(paragraph.getLanguage(line, "PRESCRIPTION")));
        this.pan_Center.setBorder(javax.swing.BorderFactory.createTitledBorder(paragraph.getLanguage(line, "MEDICINE")));
        menu_Send.setText(paragraph.getLanguage(message, "FILE"));
        mnit_Diagnosis.setText(paragraph.getLanguage(line, "DIAGNOSISCODE"));
        mnit_Medicine.setText(paragraph.getLanguage(line, "MEDICINECODE"));
        //mnit_Casehistory.setText(paragraph.getLanguage(line, "CASEHISTORY"));
        mnit_Allergy.setText(paragraph.getLanguage(line, "DRUGALLERGY"));
        mnit_Send.setText(paragraph.getLanguage(line, "SEND"));
        mnit_Back.setText(paragraph.getLanguage(line, "BACK"));
        menu_Edit.setText(paragraph.getLanguage(line, "EDIT"));
        mnit_FocusS.setText(paragraph.getLanguage(line, "FOCUDSUMMARY"));
        mnit_FocusD.setText(paragraph.getLanguage(line, "FOCUSDIAGNOSIS"));
        mnit_FocusP.setText(paragraph.getLanguage(line, "FOCUSPRESCRIPTION"));
        mnit_FocusM.setText(paragraph.getLanguage(line, "FOCUSMEDICINE"));
        mnit_Clear.setText(paragraph.getLanguage(line, "CLEARALL"));
        mnit_ClearS.setText(paragraph.getLanguage(line, "CLEARSUMMARY"));
        mnit_ClearD.setText(paragraph.getLanguage(line, "CLEARDIAGNOSIS"));
        mnin_ClearP.setText(paragraph.getLanguage(line, "CLEARPRESCRIPTION"));
        mnit_ClearM.setText(paragraph.getLanguage(line, "CLEARMEDICINE"));
        this.setTitle(paragraph.getLanguage(line, "TITLEDIAQNOSISINFO"));



    }
    // AUTOCOMPLET 顯示
    // 參數：span的point  table的point  span的寬度  span的高度  編號欄位的寬度  欄位高度  搜尋的資料庫名稱  顯示的資料庫欄位[]  作為條件比對的欄位名稱
    public void showAutoComplete(Point point, Point barPoint, int width,int height,
                        int NoColumnWidth, int row, String  DBtable, String[] DBColumn, String ColumnName) {
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
        y += (row+1) * height + 15 - bar;
        x += NoColumnWidth;
        dia.setLocation(x,y);  // 設定位置
        dia.setSize(width-NoColumnWidth, AUTOCOMPLETE_HEIGHT); // 設定大小
    }

    // 看診完畢顯示清單
    public void showFinishDagnosis(String osGuid) {
        m_OsGuid = osGuid;
        PrinterJob pj = PrinterJob.getPrinterJob();
        PageFormat pf = pj.defaultPage();
        Paper paper = new Paper();
        pf.setPaper(paper);
//        pj.setPrintable(new MyPrintable(), pf);
        //if (pj.printDialog())
        try {
            pj.print();

        } catch (PrinterException e) {
             System.out.println(e);
             ErrorMessage.setData("Diagnosis", "Frm_DiagnosisInfo" ,"showFinishDiagnosis(String osGuid)",
                       e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        }
    }

    // 判斷代碼是否有重複
    public boolean isCodeAtHashMap(Object code) {
        if (m_SelectTableHashMap.get(code) != null) { // 取出做判斷
            m_SelectTableHashMap.put(code.toString().trim(), code.toString().trim());  // 在放回
            if(m_SelectTable.getSelectedRow() != -1) {
                setClearRowValue();
            }
            return false;
        } else if (code != null && !code.toString().trim().equals("")) {
             m_SelectTableHashMap.put(code.toString().trim(), code.toString().trim());
             if (m_AutoTable.equals("medicines")) {  // 判斷是否啟動過敏機制
                 return isCodeAllergy(code);
             }
             return true;
        }
        return true;
    }

    // 比對過敏資訊
    public boolean isCodeAllergy(Object code) {
        if (m_AllergyHashMap.get(code) != null) {
            String level = null;
            switch(Integer.parseInt(m_AllergyHashMap.get(code).toString())) {
                case 1:
                    level = "Slight";
                    break;
                case 2:
                    level = "";
                    break;
                case 3:
                    level = "Serious";
                    break;
            }
            Object[] options = {paragraph.getLanguage(message , "YES"),paragraph.getLanguage(message , "NO")};
            int dialog = JOptionPane.showOptionDialog(
                            null,
                            paragraph.getLanguage(message , "MEDICINE")+code+
                            paragraph.getLanguage(message , "WILLCAUSE")+level+
                            paragraph.getLanguage(message , "ALLERGY"),
                            paragraph.getLanguage(message , "WARNING"),
                            JOptionPane.YES_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options,
                            options[0]
                        );
            if (dialog == 0) {
//                System.out.println("決定新增");
                return true;
            } else {
//                System.out.println("從 hash code 取出 "+code.toString().trim());
                m_SelectTableHashMap.remove(code.toString().trim());
                return false;
            }
        } else {
             return true;
        }
    }
    
    // 顯示 AUTOCOMPLETE 前的按鍵判斷 迴避非值輸入
    public boolean isAllowKeyIn(KeyEvent evt) {
//        System.out.println("顯示 auto 前的判斷");
        if(m_SelectTable.getSelectedColumn() == 1
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

    // 判斷 Y N
    public String isCheck(String keyWord) {
        if (keyWord.toUpperCase().equals("Y")) {
            return "Y";
        } else {
            return "N";
        }
    }

    // 判斷是否符合 服法代碼
    public String isUsage(String keyWord) {
        if (m_UsageHashMap.get(keyWord.toUpperCase().trim()) != null) {
//            System.out.println("進入服法判斷 "+keyWord.toUpperCase().trim());
            return keyWord.toUpperCase().trim();
        } else {
            return null;
        }
    }

    // 判斷是否符合 途徑代碼
    public String isWay(String keyWord) {
        if (m_WayHashMap.get(keyWord.toUpperCase().trim()) != null) {
            return keyWord.toUpperCase().trim();
        } else {
            return null;
        }
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
            for(int i = 2; i < m_SelectTable.getColumnCount(); i++) {
                m_SelectTable.setValueAt(null, m_SelectTable.getSelectedRow(), i);
            }
        }
        if (m_AutoTxt.getText().length() != 0) {
                m_AutoTxt.setText(m_AutoTxt.getText().substring(0, m_AutoTxt.getText().length()-1));
                m_SelectTable.setValueAt(m_AutoTxt.getText(), m_SelectTable.getSelectedRow(), 1);
                setAutoCompleteList(m_AutoTxt.getText());
        } 
    }

    // split陣列的value丟入各個tableColumn
    public void setTableValue(String[] value){
//        System.out.println("split 傳入陣列值 ");
        switch(m_SelectTableNo) {
        case 1:  //tab_Diagnosis
            m_AutoTxt.setText(value[0]);
            tab_Diagnosis.setValueAt(value[0], tab_Diagnosis.getSelectedRow(), 1);
            tab_Diagnosis.setValueAt(value[1], tab_Diagnosis.getSelectedRow(), 2);
            break;
        case 2:  //tab_Prescription
            m_AutoTxt.setText(value[0]);
            tab_Prescription.setValueAt(value[0], tab_Prescription.getSelectedRow(), 1);
            tab_Prescription.setValueAt(value[1], tab_Prescription.getSelectedRow(), 2);
            tab_Prescription.setValueAt(value[2], tab_Prescription.getSelectedRow(), 4);
            break;
        case 3:  //tab_Medicine
            if (value.length != -1) {
                System.out.println("0  :"+value[0] + " 1  :"+value[1] + " 2  :"+value[2]);
                m_AutoTxt.setText(value[1]);
                tab_Medicine.setValueAt(value[1].trim(), tab_Medicine.getSelectedRow(), 1);  // item
                tab_Medicine.setValueAt(value[0].trim(), tab_Medicine.getSelectedRow(), 2);  // code
                tab_Medicine.setValueAt(value[2].trim(), tab_Medicine.getSelectedRow(), 3);  // injection
//                tab_Medicine.setValueAt(value[4], tab_Medicine.getSelectedRow(), 4);
                tab_Medicine.setValueAt("N", tab_Medicine.getSelectedRow(), 10);
                tab_Medicine.setValueAt("N", tab_Medicine.getSelectedRow(), 11);
            }
            
            break;
        }
    }

    // AUTOCOMPLETE 搜尋不到值  清空前一個遺留下的值
    public void setClearTableRow(int row) {
//        System.out.println("auto搜不到值  清空前一個遺留下的值 ROW 為 " + row);

        for(int i = 1; i < m_SelectTable.getColumnCount(); i++) {
            if(m_SelectTable.getSelectedRow() != -1 && m_SelectTable.getValueAt(m_SelectTable.getSelectedRow(), i) != null) {
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
        System.out.println("搜尋條件 "+ condition );
        if(condition.equals("")) {
            setClearTableRow(m_Row);
            btn_CloseActionPerformed(null);
        } else {
            String[] list = null;
            String sql = null;
            if (m_AutoTable.equals("medicines")) {
//                System.out.println("搜尋用 藥品 語法");
                sql = "SELECT * FROM "+m_AutoTable+" " +
                      "WHERE LOWER("+m_AutoColumnName+") LIKE LOWER('"+condition+"%') " +
                      "AND effective <> 0 ORDER BY "+m_AutoColumnName+"";
            } else if (m_AutoTable.equals("diagnosis_code")) {
//                System.out.println("搜尋用 icd code 語法");
                sql = "SELECT * FROM "+m_AutoTable+" " +
                      "WHERE LOWER("+m_AutoColumnName+") LIKE LOWER('"+condition+"%') " +
                      "AND icd_code NOT LIKE '%-%' AND effective <> 1 ORDER BY "+m_AutoColumnName+"";
            }  else if (m_AutoTable.equals("prescription_code")) {
                System.out.println("搜尋用 處置 語法");
                sql = "SELECT * FROM "+m_AutoTable+"  " +
                      "WHERE LOWER("+m_AutoColumnName+") LIKE LOWER('"+condition+"%') " +
                      "AND effective = 1  ORDER BY "+m_AutoColumnName+"";
            }
            System.out.println(sql);
            int index = 0;
            ResultSet rs = null;
            try {
                rs = DBC.localExecuteQuery(sql);
                rs.last();
                m_RsRowCount = rs.getRow();
                setListheight();
                if(m_RsRowCount < AUTOCOMPLETE_SHOW_ROW) {
                    list = new String[m_RsRowCount];
                } else {
                    list = new String[AUTOCOMPLETE_SHOW_ROW + 1];
                }

                rs.beforeFirst();
                String str = "";
                while (rs.next()) {
                  if(rs.getRow() > AUTOCOMPLETE_SHOW_ROW + 1) {
                      break;
                  } else {
                      str = "";
                      for (int i = 0; i < m_AutoColumn.length; i++) {
                            if (m_AutoTable.equals("medicines") && rs.getString(m_AutoColumn[2]).equals("")) {
                                str += (" "+rs.getString(m_AutoColumn[i]).trim() +"       ");
                            } else {
                                str += (rs.getString(m_AutoColumn[i]).trim() +"       ");
                            }
                      }
                      list[index++] = str;
                  }
                }
                list_Menu.setListData(list);
          
                list_Menu.removeSelectionInterval(0, AUTOCOMPLETE_SHOW_ROW + 1);
            } catch (SQLException e) {
//                Logger.getLogger(Frm_DiagnosisInfo.class.getName()).log(Level.SEVERE, null, e);
                ErrorMessage.setData("Diagnosis", "Frm_DiagnosisInfo" ,"setAutoCompleteList(String condition)",
                        e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            } finally {
                try {DBC.closeConnection(rs);}
                catch (SQLException e){
                    ErrorMessage.setData("Diagnosis", "Frm_DiagnosisInfo" ,"setAutoCompleteList(String condition) - DBC.closeConnection(rs)",
                        e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
                }
           }
        }
    }

 
    
    // 從其他 FRAME 增加一筆資料到指定 TABLE  指定 TABLE 欄位
    public void setDiagnosisInfoTable(Object[] value, int[] appointColumn) {
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

    // TABLE 按鍵進行 ROW 增加 或清除值
    public int setRowValue(KeyEvent evt) {
            m_SelectTable.removeEditor();
            //上一行特定欄位不為空白才能增加行
            if (evt.getKeyCode() == KeyEvent.VK_ENTER
             && m_SelectTable.getValueAt(m_SelectTableRowNo-1, m_SelectTableAddRowLimitColumn) != null
             && m_SelectTable.getValueAt(m_SelectTableRowNo-1, m_SelectTableAddRowLimitColumn) != null
             && !m_SelectTable.getValueAt(m_SelectTableRowNo-1, m_SelectTableAddRowLimitColumn).toString().trim().equals("")
             && !m_SelectTable.getValueAt(m_SelectTableRowNo-1, m_SelectTableAddRowLimitColumn).toString().trim().equals("")) {
                m_SelectTableModel.addRow(new Vector());
                m_SelectTable.setValueAt(++m_SelectTableRowNo, m_SelectTable.getRowCount()-1, 0);
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

    // 修改看診資料
    public void setEditCasehistory(String summary, String regGuid, String message) {
        getCasehistory(summary, regGuid );
        m_RegistrationGuid = regGuid;
        this.setTitle("Edit "+message+" data");
        btn_Send.setText("Edit");
    }


    // 移除所有TABLE 編輯狀態的欄位 與 取消光條選
    public void setAllRemoveEditAndSelection() {
        TabTools.setRemoveEditAndSelection(this.tab_Prescription);
        TabTools.setRemoveEditAndSelection(this.tab_Medicine);
        TabTools.setRemoveEditAndSelection(this.tab_Diagnosis);
    }
    
    // 設定 AUTOCOMPLETE 高度
    public void setListheight() {
        if(m_RsRowCount <= AUTOCOMPLETE_HEIGHT && m_RsRowCount != 0){
            if(m_RsRowCount * 40 > AUTOCOMPLETE_HEIGHT) {
                dia.setSize(dia.getWidth() ,AUTOCOMPLETE_HEIGHT);
            } else {
                dia.setSize(dia.getWidth() ,m_RsRowCount * 40);
            }
            
        } else if(m_RsRowCount == 0) {
            dia.setSize(dia.getWidth() ,1);
            setClearTableRow(m_Row);
        } else {
            dia.setSize(dia.getWidth() ,AUTOCOMPLETE_HEIGHT);
        }
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

    // 診斷碼 TABLE 上移下移鍵 Enable 狀態
    public void setMoveBtnEnable() {
        if (tab_Diagnosis.getSelectedRow() != -1) {
            if (tab_Diagnosis.getSelectedRow() > 0
             && tab_Diagnosis.getValueAt(tab_Diagnosis.getSelectedRow(), 2) != null ) {
                btn_Up.setEnabled(true);
                btn_First.setEnabled(true);
            } else {
                btn_Up.setEnabled(false);
                btn_First.setEnabled(false);
            }

            if (tab_Diagnosis.getSelectedRow() != tab_Diagnosis.getRowCount()-1
             && tab_Diagnosis.getValueAt(tab_Diagnosis.getSelectedRow()+1, 2) != null) {
                btn_Down.setEnabled(true);
                btn_Last.setEnabled(true);
            } else {
                btn_Down.setEnabled(false);
                btn_Last.setEnabled(false);
            }
            tab_Diagnosis.requestFocusInWindow();
            tab_Diagnosis.changeSelection(tab_Diagnosis.getSelectedRow(), tab_Diagnosis.getSelectedColumn(), false, false);
        } else {
            btn_Down.setEnabled(false);
            btn_Last.setEnabled(false);
            btn_Up.setEnabled(false);
            btn_First.setEnabled(false);
        }
    }

    // 儲存或修改病歷
    public void setSaveDiagnosis() {
        ResultSet rsOsGuid = null;
        ResultSet rsPharmacyNo = null;
        ResultSet rsModifyCount = null;
        // 防錯 診斷
        boolean diagnosisIsNull = true;
        for (int i = 0; i < tab_Diagnosis.getRowCount(); i++) {
//            System.out.println(tab_Diagnosis.getValueAt(i, 1));
            if ((tab_Diagnosis.getValueAt(i, 1) != null
              && !tab_Diagnosis.getValueAt(i, 1).toString().trim().equals(""))
              && tab_Diagnosis.getValueAt(i, 2) == null) {
               diagnosisIsNull = false;
               JOptionPane.showMessageDialog(null, paragraph.getLanguage(message, "PLEASECONFIRMDIAGNOSISTABLEROW")+(i+1)+"");
               tab_Diagnosis.requestFocusInWindow();
               tab_Diagnosis.changeSelection(i, 1, false, false);
               break;
            } 
        }
        // 防錯 藥品
        boolean medicineIsNull = true;
        for (int r = 0; r < tab_Medicine.getRowCount(); r++) {
            if (tab_Medicine.getValueAt(r, 2) != null ){
                setMedicineValue(r);
//                    && !tab_Medicine.getValueAt(r, 2).toString().trim().equals("")) { // Medicine hide
                for (int c = 0; c < tab_Medicine.getColumnCount(); c++) {
                    if (c == 1 ||  c == 4 || c == 6 || c == 7 || c == 8) { // Medicine hide  c == 2 ||
                        if (tab_Medicine.getValueAt(r, c) == null || tab_Medicine.getValueAt(r, c).toString().trim().equals("")) {
                            medicineIsNull = false;
                            JOptionPane.showMessageDialog(null, paragraph.getLanguage(message, "PLEASECONFIRMMEDICINETABLEROW")
                                                                +(r+1)+", " +
                                                                paragraph.getLanguage(message, "COLUMN")+(c)+"");  // Medicine hide 第 "+(c+1)+" 列"
                            tab_Medicine.requestFocusInWindow();
                            tab_Medicine.changeSelection(r, c, false, false);
                            break;
                        }
                    }
                }
            }
            if (medicineIsNull == false) {
                 break;
            }
        }

        if (diagnosisIsNull == true && medicineIsNull == true) {
            try {
                String os_uuid = UUID.randomUUID().toString();
                if (m_FinishState == true) {  // 將舊看診資訊刪除
                    String sqlDelete =
                           "DELETE FROM outpatient_services WHERE reg_guid = '"+m_RegistrationGuid+"'";
                    DBC.executeUpdate(sqlDelete);
                }
                //新增資料到看診 outpatient_services
                DBC.executeUpdate("INSERT outpatient_services(guid, reg_guid, summary, ps, state ) " +
                                  "VALUES ('"+os_uuid+"', '"+m_RegistrationGuid+"', '"+txta_Summary.getText().trim()+"','"+txt_Message.getText().trim()+"', 0)");
                //存入icd code診斷碼
                if (tab_Diagnosis.getValueAt(0, 2) != null) {
                    for (int i = 0 ; i < this.tab_Diagnosis.getRowCount() ; i++) {
                        if (this.tab_Diagnosis.getValueAt(i, 2) != null) {
                            DBC.executeUpdate("INSERT diagnostic(guid, os_guid, dia_code , state) VALUES (uuid(), '"+os_uuid+"', '"+this.tab_Diagnosis.getValueAt(i, 1).toString().trim()+"' , 0)");
                        }
                    }
                }
                boolean prescriptionState = false; //  判斷是否有檢驗處置
                boolean xrayState = false;         //  判斷是否有x光處置
                // 存入處置

                for (int i = 0 ; i < this.tab_Prescription.getRowCount() ; i++) {
                    if (this.tab_Prescription.getValueAt(i, 1) != null && !this.tab_Prescription.getValueAt(i, 1).toString().trim().equals("")) {

                        if (this.tab_Prescription.getValueAt(i, 4) != null && this.tab_Prescription.getValueAt(i, 4).toString().trim().equals(Constant.X_RAY_CODE)) {

                            xrayState=true;
                        } else {
                            prescriptionState = true;
                        }
                       if (this.tab_Prescription.getValueAt(i, 3) == null) {
                            this.tab_Prescription.setValueAt("",i, 3);
                       }


                        DBC.executeUpdate("INSERT prescription(guid, os_guid, case_guid, code , place, state) VALUES (uuid(), '"+os_uuid+"', NULL, " +
                                "'"+this.tab_Prescription.getValueAt(i, 1).toString().trim()+"', " +
                                "'"+this.tab_Prescription.getValueAt(i, 3).toString().trim()+"', 1)");
                    }
                }
                
                int medicineState = 0; //  判斷是否有開出藥品

                // 存入藥品
                for (int i = 0 ; i < this.tab_Medicine.getRowCount() ; i++) {
                    if (this.tab_Medicine.getValueAt(i, 2) != null ) { // Medicine hide   && !this.tab_Medicine.getValueAt(i, 2).toString().trim().equals("")

                        String ps = null;

                        if (this.tab_Medicine.getValueAt(i, 12) == null) {
                            ps = null;
                        } else {
                            ps = "'"+this.tab_Medicine.getValueAt(i, 12).toString().trim()+"'";
                        }

                        String sql = "INSERT medicine_stock(guid, os_guid, m_code," +
                                "dosage, `usage`, way," +
                                "`day`, quantity, urgent," +
                                "powder, ps, exist," +
                                "s_id, teach_complete) " +
                                "VALUES (uuid(), " +
                                "'"+os_uuid+"', " +
                                "'"+this.tab_Medicine.getValueAt(i, 2).toString().trim()+"', " +  // 藥品代碼
                                "'"+Double.parseDouble(this.tab_Medicine.getValueAt(i, 4).toString())+"', " +  // 次劑量
                                "'"+this.tab_Medicine.getValueAt(i, 6).toString().trim()+"', " +  // 服法
                                "'"+this.tab_Medicine.getValueAt(i, 7).toString().trim()+"', " +  // 途徑
                                ""+Double.parseDouble(this.tab_Medicine.getValueAt(i, 8).toString())+", " +  // 天數
                                ""+Double.parseDouble(this.tab_Medicine.getValueAt(i, 9).toString())+", " +  // 總量
                                "'"+this.tab_Medicine.getValueAt(i, 10)+"', " +   // 急
                                "'"+this.tab_Medicine.getValueAt(i, 11)+"', " +   // 磨
                                ""+ps+", " +  // 備註
                                " 1,NULL,0)";

                        // 糖尿病使用
                        if (this.m_IsDM) {
                            ResultSet rs = DBC.executeQuery("SELECT id FROM package_item WHERE id = '"+this.tab_Medicine.getValueAt(i, 2).toString().trim()+"'");
                            if (rs.next() && (m_PackageSetDay == 0 || Integer.parseInt(this.tab_Medicine.getValueAt(i, 8).toString()) <  m_PackageSetDay)) {
                                m_PackageSet = this.tab_Medicine.getValueAt(i, 2).toString().trim() +" "+ this.tab_Medicine.getValueAt(i, 1).toString().trim() +" "+this.tab_Medicine.getValueAt(i, 3).toString().trim();
                                m_PackageSetId = this.tab_Medicine.getValueAt(i, 2).toString().trim();
                                m_PackageSetDay = Integer.parseInt(this.tab_Medicine.getValueAt(i, 8).toString());
                            }
                        }
                        System.out.println(sql);
                        DBC.executeUpdate(sql);
                        medicineState += 1;
                    }
                }
                // 領藥號
                int pharmacyNo = 1;

                String sqlPharmacyNo =  // 領藥號
                        "SELECT MAX(registration_info.pharmacy_no) AS pharmacyNo " +
                        "FROM registration_info, shift_table, medicine_stock, outpatient_services " +
                        "WHERE shift_table.shift = '"+DateMethod.getNowShiftNum()+"' " +
                        "AND registration_info.finish = 'F' " +
                        "AND shift_table.shift_date = '"+DateMethod.getTodayYMD()+"' " +
                        "AND registration_info.shift_guid = shift_table.guid";
                String sqlModifyCount =  // 修改次數
                        "SELECT modify_count, pharmacy_no FROM registration_info WHERE guid = '"+m_RegistrationGuid+"'";
                       

                rsPharmacyNo = DBC.executeQuery(sqlPharmacyNo);
                rsModifyCount = DBC.executeQuery(sqlModifyCount);
                rsPharmacyNo.next();
                rsModifyCount.next();
                if (m_FinishState != true) {  //  判斷是否為第一次看診
                    rsPharmacyNo.last();
                    if (rsPharmacyNo.getRow() != 0) {
                        pharmacyNo = rsPharmacyNo.getInt("pharmacyNo") + 1;
                    }

                    // updata registration_info visits to be already
                    DBC.executeUpdate(
                            "UPDATE registration_info SET finish = 'F', pharmacy_no = "+pharmacyNo+", modify_count = 0," +
                                "touchtime = RPAD((SELECT CASE WHEN MAX(B.touchtime) >= DATE_FORMAT(now(),'%Y%m%d%H%i%S') " +
                                "THEN concat(DATE_FORMAT(now(),'%Y%m%d%H%i%S'), COUNT(B.touchtime)) " +
                                "ELSE DATE_FORMAT(now(),'%Y%m%d%H%i%S') " +
                                "END touchtime FROM (SELECT touchtime FROM registration_info) AS B WHERE B.touchtime LIKE " +
                                "concat(DATE_FORMAT(now(),'%Y%m%d%H%i%S'),'%')),20,'000000') " +
                            "WHERE guid = '"+m_RegistrationGuid+"'");



                } else {//  修改過多次
                    DBC.executeUpdate(
                            "UPDATE registration_info SET modify_count = "+(rsModifyCount.getInt("modify_count")+1)+", " +
                                "touchtime = RPAD((SELECT CASE WHEN MAX(B.touchtime) >= DATE_FORMAT(now(),'%Y%m%d%H%i%S') " +
                                "THEN concat(DATE_FORMAT(now(),'%Y%m%d%H%i%S'), COUNT(B.touchtime)) " +
                                "ELSE DATE_FORMAT(now(),'%Y%m%d%H%i%S') " +
                                "END touchtime FROM (SELECT touchtime FROM registration_info) AS B WHERE B.touchtime LIKE " +
                                "concat(DATE_FORMAT(now(),'%Y%m%d%H%i%S'),'%')),20,'000000') " +
                            "WHERE guid = '"+m_RegistrationGuid+"'");
                }

                // 變更領藥 touchtime
                DBC.executeUpdate(
                            "UPDATE registration_info SET medicine_touchtime = RPAD((SELECT CASE WHEN MAX(B.medicine_touchtime) >= DATE_FORMAT(now(),'%Y%m%d%H%i%S') " +
                                "THEN concat(DATE_FORMAT(now(),'%Y%m%d%H%i%S'), COUNT(B.medicine_touchtime)) " +
                                "ELSE DATE_FORMAT(now(),'%Y%m%d%H%i%S') " +
                                "END medicine_touchtime FROM (SELECT medicine_touchtime FROM registration_info) AS B WHERE B.medicine_touchtime LIKE " +
                                "concat(DATE_FORMAT(now(),'%Y%m%d%H%i%S'),'%')),20,'000000') " +
                            "WHERE guid = '"+m_RegistrationGuid+"'");

                
                setPrint(medicineState,prescriptionState,xrayState);
                // 提示回診日 *************************************
                String packageSetAll = "";
                if (m_PackageSet != null) {
                    dia_RevisitTime.setLocationRelativeTo(this);
                    dia_RevisitTime.setVisible(true);
                    String packageSet[] = m_PackageSet.split(",");
                    String packageSetId[] = m_PackageSetId.split(",");
                    for (int i = 0; i < packageSet.length; i++) {
                        packageSetAll+= i+1 +". "+  packageSet[i] + " ";
                        String sql = "SELECT days FROM package_item WHERE id = '"+packageSetId[i]+"'";
                        ResultSet rs = DBC.executeQuery(sql);
                        txt_PackageId.setText(packageSetId[i]);
                        if (rs.next()) txt_ComeBackDays.setText(rs.getString ("days"));
                    }
                    txt_PackageType.setText(packageSetAll);
                    this.setEnabled(false);
                } else {
                    int workListRowCount = m_WorkListRowNo;
                    Frm_WorkList frm_diagnosisWorkList = new Frm_WorkList(workListRowCount, "dia");
                    frm_diagnosisWorkList.setVisible(true);
                    this.dispose();
                }
                //*************************************************
                
            } catch (SQLException e) {
                Logger.getLogger(Frm_DiagnosisInfo.class.getName()).log(Level.SEVERE, null, e);
                    ErrorMessage.setData("Diagnosis", "Frm_DiagnosisInfo" ,"setSaveDiagnosis()",
                    e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            } finally {
                try {DBC.closeConnection(rsOsGuid);
                     DBC.closeConnection(rsPharmacyNo);
                     DBC.closeConnection(rsModifyCount);}
                catch (SQLException e){
                    ErrorMessage.setData("Diagnosis", "Frm_DiagnosisInfo" ,"setSaveDiagnosis() - DBC.closeConnection",
                    e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
                }
            }
        }
   }

    // 儲存X-Ray照射部位與列印
    private void setPrint(int medicineState,boolean prescriptionState,boolean xrayState) {
        PrintTools pt = new PrintTools();

         if (medicineState > 0) {
                pt.DoPrint(8, m_RegistrationGuid);
        }

        if (prescriptionState) {
                pt.DoPrint(6, m_RegistrationGuid);
        }

        if (xrayState) {
                pt.DoPrint(4, m_RegistrationGuid);
        }

        //----- 病歷列印---------------
         pt.DoPrint(10, m_RegistrationGuid);

          
    }
    
    // 藥品輸入防呆偵測
    public void setMedicineValue(int selectRow) {

        if (selectRow != -1 && tab_Medicine.isEditing() == false) {  //  次劑量判斷
            if(tab_Medicine.getValueAt(selectRow, 4) != null && !Tools.isNumber(tab_Medicine.getValueAt(selectRow, 4).toString().trim())) {
                tab_Medicine.setValueAt(null, selectRow, 4);
            }
            if(tab_Medicine.getValueAt(selectRow, 8) != null && !Tools.isNumber(tab_Medicine.getValueAt(selectRow, 8).toString().trim())) {
                tab_Medicine.setValueAt(null, selectRow, 8);
            }
            if(tab_Medicine.getValueAt(selectRow, 10) != null) {
                tab_Medicine.setValueAt(isCheck(tab_Medicine.getValueAt(selectRow, 10).toString().trim()),selectRow ,10);
            }
            if(tab_Medicine.getValueAt(selectRow, 11) != null) {
                tab_Medicine.setValueAt(isCheck(tab_Medicine.getValueAt(selectRow, 11).toString().trim()),selectRow ,11);
            }
            if (tab_Medicine.getValueAt(selectRow, 6) != null) {  // 服法
                tab_Medicine.setValueAt(isUsage(tab_Medicine.getValueAt(selectRow, 6).toString().trim()), selectRow, 6);
                if (tab_Medicine.getValueAt(selectRow, 6) != null) {
                    tab_Medicine.setValueAt(getFrequency(tab_Medicine.getValueAt(selectRow, 6).toString().trim()), selectRow, 13);
                } else {
                    tab_Medicine.setValueAt(null, selectRow, 13);
                }
            }
            if (tab_Medicine.getValueAt(selectRow, 7) != null) { // 途徑
                tab_Medicine.setValueAt(isWay(tab_Medicine.getValueAt(selectRow, 7).toString().trim()), selectRow, 7);
            }
            if (tab_Medicine.getValueAt(selectRow, 4) != null && tab_Medicine.getValueAt(selectRow, 8) != null &&
                tab_Medicine.getValueAt(selectRow, 13) != null && !tab_Medicine.getValueAt(selectRow, 4).toString().equals("") &&
                !tab_Medicine.getValueAt(selectRow, 8).toString().equals("") &&
                !tab_Medicine.getValueAt(selectRow, 13).toString().equals("")) {
                float dosage = (float) Double.parseDouble(this.tab_Medicine.getValueAt(selectRow, 4).toString());      // 次劑量
                float usage = (float) Double.parseDouble(this.tab_Medicine.getValueAt(selectRow, 8).toString());       // 次數
                float day = (float) Double.parseDouble(this.tab_Medicine.getValueAt(selectRow, 13).toString());        // 天
                float total = dosage*usage*day;
                tab_Medicine.setValueAt(total, selectRow, 9);
            } else {
                tab_Medicine.setValueAt(null, selectRow, 9);
            }
            if(tab_Medicine.getValueAt(selectRow, 9) != null && !Tools.isNumber(tab_Medicine.getValueAt(selectRow, 4).toString().trim())) {
                tab_Medicine.setValueAt(null, selectRow, 9);
            }
        }
    }

    // FOCUS TABLE 的快捷鍵
    public void setFocusRapid(JTable table) {
        setAllRemoveEditAndSelection();
        table.requestFocusInWindow();
        table.setRowSelectionInterval(0, 0);
        table.setColumnSelectionInterval(0, 1);
        table.changeSelection(table.getSelectedRow(), table.getSelectedColumn(), false, false);
        setMoveBtnEnable();
    }

    public void setDiagnosisRowNo() {
        m_DiagnosisRowNo = m_SelectTableRowNo;
    }

    public void setPrescriptionRowNo() {
        m_PrescriptionRowNo = m_SelectTableRowNo;
    }

    public void setMedicineRowNo() {
        m_MedicineRowNo = m_SelectTableRowNo;
    }

    // 將過去病史帶入
    public void getCasehistory(String summary ,String guid ) {
        TabTools.setClearTableValue(tab_Diagnosis);
        TabTools.setClearTableValue(tab_Prescription);
        TabTools.setClearTableValue(tab_Medicine);
        ResultSet rsDiagnosis = null;
        ResultSet rsPrescription = null;
        ResultSet rsMedicines = null;
        try {
        // 取出看診診斷
        getTxtaSummary(summary);
        // 取出看診ICD CODE
        String sqlDiagnosis =
              "SELECT diagnosis_code.icd_code, diagnosis_code.name " +
              "FROM  diagnosis_code, diagnostic, outpatient_services, registration_info " +
              "WHERE registration_info.guid = '"+guid+"' " +
              "AND diagnostic.os_guid = outpatient_services.guid " +
              "AND outpatient_services.reg_guid = registration_info.guid " +
              "AND diagnosis_code.icd_code = diagnostic.dia_code";
         rsDiagnosis = DBC.executeQuery(sqlDiagnosis);
         int rowDiagnosis = 0;
         while (rsDiagnosis.next()) {
             tab_Diagnosis.setValueAt(rsDiagnosis.getString("icd_code"), rowDiagnosis, 1);
             tab_Diagnosis.setValueAt(rsDiagnosis.getString("name").trim(), rowDiagnosis, 2);
             
             m_DiagnosisHashMap.put(rsDiagnosis.getString("icd_code"), rsDiagnosis.getString("icd_code"));
             if((rowDiagnosis+2) > tab_Diagnosis.getRowCount()) {
                m_DiagnosisModel.addRow(new Vector());
                tab_Diagnosis.setValueAt(++m_DiagnosisRowNo, tab_Diagnosis.getRowCount()-1, 0);
             }
             rowDiagnosis++;
         }
         // 取出處置
         String sqlPrescription =
            "SELECT prescription.code, prescription_code.name, prescription_code.type, prescription.place " +
            "FROM prescription, outpatient_services, registration_info, prescription_code " +
            "WHERE registration_info.guid = '"+guid+"' " +
            "AND prescription.os_guid = outpatient_services.guid " +
            "AND prescription_code.code = prescription.code " +
            "AND outpatient_services.reg_guid = registration_info.guid";
//         System.out.println(sqlPrescription);
        rsPrescription =  DBC.executeQuery(sqlPrescription);
        int rowPrescription = 0;
        while (rsPrescription.next()) {
            tab_Prescription.setValueAt(rsPrescription.getString("code"), rowPrescription, 1);
            tab_Prescription.setValueAt(rsPrescription.getString("name"), rowPrescription, 2);
            tab_Prescription.setValueAt(rsPrescription.getString("place"), rowPrescription, 3);
            tab_Prescription.setValueAt(rsPrescription.getString("type"), rowPrescription, 4);
            if((rowPrescription+2) > tab_Prescription.getRowCount()) {
                m_PrescriptionModel.addRow(new Vector());
                tab_Prescription.setValueAt(++m_PrescriptionRowNo, tab_Prescription.getRowCount()-1, 0);
            }
            rowPrescription++;
        }
        // 取出藥品
        String sqlMedicines =
            "SELECT medicines.code,medicines.injection, medicines.item, medicine_stock.dosage, medicines.unit, medicine_stock.usage, " +
                   "medicine_stock.way, medicine_stock.day, medicine_stock.quantity,medicine_stock.urgent, " +
                   "medicine_stock.powder ,medicine_stock.ps " +
            "FROM medicines, medicine_stock, outpatient_services, registration_info " +
            "WHERE registration_info.guid = '"+guid+"' " +
            "AND medicine_stock.os_guid = outpatient_services.guid " +
            "AND outpatient_services.reg_guid = registration_info.guid " +
            "AND medicines.code = medicine_stock.m_code";
            rsMedicines = DBC.executeQuery(sqlMedicines);
            int rowMedicine = 0;
            while (rsMedicines.next()) {
                tab_Medicine.setValueAt(rsMedicines.getString("item"), rowMedicine, 1);
                tab_Medicine.setValueAt(rsMedicines.getString("code"), rowMedicine, 2);
                tab_Medicine.setValueAt(rsMedicines.getString("injection"), rowMedicine, 3);
                tab_Medicine.setValueAt(rsMedicines.getFloat("dosage"), rowMedicine, 4);
                tab_Medicine.setValueAt(rsMedicines.getString("unit"), rowMedicine, 5);
                tab_Medicine.setValueAt(rsMedicines.getString("usage"), rowMedicine, 6);
                tab_Medicine.setValueAt(rsMedicines.getString("way"), rowMedicine, 7);
                tab_Medicine.setValueAt(rsMedicines.getString("day"), rowMedicine, 8);
                tab_Medicine.setValueAt(rsMedicines.getFloat("quantity"), rowMedicine,9);
                if (rsMedicines.getString("urgent").equals("Y")) {
                    tab_Medicine.setValueAt("Y", rowMedicine, 10);
                } else {
                    tab_Medicine.setValueAt("N", rowMedicine, 10);
                }
                if(rsMedicines.getString("powder").equals("Y")) {
                    tab_Medicine.setValueAt("Y", rowMedicine, 11);
                } else {
                    tab_Medicine.setValueAt("N", rowMedicine, 11);
                }
                tab_Medicine.setValueAt(rsMedicines.getString("ps"), rowMedicine, 12);
              
                m_MedicineHashMap.put(rsMedicines.getString("code"), rsMedicines.getString("code"));
                if((rowMedicine+2) > tab_Medicine.getRowCount()) {
                    m_MedicineModel.addRow(new Vector());
                    tab_Medicine.setValueAt(++m_MedicineRowNo, tab_Medicine.getRowCount()-1, 0);
                }
                rowMedicine++;
            }
      } catch (SQLException e) {
            Logger.getLogger(Frm_DiagnosisInfo.class.getName()).log(Level.SEVERE, null, e);
                    ErrorMessage.setData("Diagnosis", "Frm_DiagnosisInfo" ,"getCasehistory(String summary ,String guid )",
                    e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
      } finally {
            try {DBC.closeConnection(rsMedicines);}
            catch (SQLException e){
                    ErrorMessage.setData("Diagnosis", "Frm_DiagnosisInfo" ,"getCasehistory(String summary ,String guid - DBC.closeConnection )",
                    e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            }
        }
    }
    
    // 取出病患過敏資料
    public void getAllergy() {
        m_AllergyHashMap.clear();
//        System.out.println("載入過敏資料 ");
        ResultSet rs =null;
        try {
            String sql = "SELECT medicines.code, allergy.level " +
                         "FROM patients_info, medicines, allergy " +
                         "WHERE patients_info.p_no = '"+m_Pno+"' " +
                         "AND allergy.p_no = patients_info.p_no " +
                         "AND allergy.m_code = medicines.code  " +
                         "AND level <> 0 "+
                         "ORDER BY medicines.code ";
            rs = DBC.executeQuery(sql);
            rs.last();
            rs.beforeFirst();
            while (rs.next()) {
                m_AllergyHashMap.put(rs.getString("code"), rs.getString("level"));
            }
        } catch (SQLException e) {
//            Logger.getLogger(Frm_DiagnosisInfo.class.getName()).log(Level.SEVERE, null, e);
                    ErrorMessage.setData("Diagnosis", "Frm_DiagnosisInfo" ,"getAllergy()",
                    e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        } finally {
            try {DBC.closeConnection(rs);}
            catch (SQLException e){
                    ErrorMessage.setData("Diagnosis", "Frm_DiagnosisInfo" ,"getAllergy() - DBC.closeConnection",
                    e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
            }
        }
    }

    // AUTOCOMPLETE 值顯示到經過 split 轉為陣列丟入 TABLE
    public void getSplitValue() {
        System.out.println("AUTOCOMPLETE 值顯示到經過 split 轉為陣列丟入 TABLE");
        if (list_Menu.getSelectedValue() != null ) {
            m_AutoListValue = list_Menu.getSelectedValue().toString().split("       ");
            //回傳table  autoCompleteList表單值切割的陣列
            if (m_AutoListValue.length > 1) {
                setTableValue(m_AutoListValue);
                System.out.println("1");
            } else if (m_AutoListValue.length == 1) {
                setTableValue(m_AutoListValue);
                System.out.println("2");
            } else {
                m_AutoListValue = new String[5];
                for (int i = 1; i < m_AutoListValue.length; i++ ) {
                    m_AutoListValue[i] = null;
                }
                setTableValue(m_AutoListValue);
                System.out.println("3");
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

    // 將 Summary 資料丟入 txta_Summary
    public void getTxtaSummary(String text) {
        txta_Summary.setText(text);
    }

    // 取出服法代碼中的次數
    public float getFrequency(String keyWord) {
        return (float) Double.parseDouble(m_UsageHashMap.get(keyWord).toString());
    }
    
    // 回到看診畫面 畫面重設為可編輯
    public void reSetEnable() {
        this.setEnabled(true);
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
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dia = new javax.swing.JDialog();
        span_ListMenu = new javax.swing.JScrollPane();
        list_Menu = new javax.swing.JList();
        btn_Close = new javax.swing.JButton();
        dia_RevisitTime = new javax.swing.JDialog();
        jButton2 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        txt_PackageId = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txt_PackageType = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txt_ComeBackDays = new javax.swing.JTextField();
        pan_Top = new javax.swing.JPanel();
        lab_TitleNo = new javax.swing.JLabel();
        lab_TitleName = new javax.swing.JLabel();
        lab_TitlePs = new javax.swing.JLabel();
        lab_TitleSex = new javax.swing.JLabel();
        lab_TitleAge = new javax.swing.JLabel();
        lab_TitleBloodtype = new javax.swing.JLabel();
        txt_No = new javax.swing.JTextField();
        txt_Age = new javax.swing.JTextField();
        txt_Bloodtype = new javax.swing.JTextField();
        txt_Name = new javax.swing.JTextField();
        txt_Sex = new javax.swing.JTextField();
        txt_Ps = new javax.swing.JTextField();
        lab_Height = new javax.swing.JLabel();
        lab_Weight = new javax.swing.JLabel();
        txt_Height = new javax.swing.JTextField();
        txt_Weight = new javax.swing.JTextField();
        pan_CenterTop = new javax.swing.JPanel();
        pan_Summary = new javax.swing.JPanel();
        span_Summary = new javax.swing.JScrollPane();
        txta_Summary = new javax.swing.JTextArea();
        pan_Center = new javax.swing.JPanel();
        span_Medicine = new javax.swing.JScrollPane();
        tab_Medicine = new javax.swing.JTable();
        btn_Send = new javax.swing.JButton();
        txt_Message = new javax.swing.JTextField();
        pan_Diagnosis = new javax.swing.JPanel();
        span_Diagnosis = new javax.swing.JScrollPane();
        tab_Diagnosis = new javax.swing.JTable();
        pan_CenterRight = new javax.swing.JPanel();
        btn_First = new javax.swing.JButton();
        btn_Up = new javax.swing.JButton();
        btn_Down = new javax.swing.JButton();
        btn_Last = new javax.swing.JButton();
        pan_Prescription = new javax.swing.JPanel();
        span_Prescription = new javax.swing.JScrollPane();
        tab_Prescription = new javax.swing.JTable();
        pan_Function = new javax.swing.JPanel();
        btn_Allergy = new javax.swing.JButton();
        btn_Medicine = new javax.swing.JButton();
        btn_Diagnosis = new javax.swing.JButton();
        btn_Prescription = new javax.swing.JButton();
        btn_CaseManagement = new javax.swing.JButton();
        lab_Ps = new javax.swing.JLabel();
        menubar = new javax.swing.JMenuBar();
        menu_Send = new javax.swing.JMenu();
        mnit_Diagnosis = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        mnit_Medicine = new javax.swing.JMenuItem();
        mnit_Casehistory = new javax.swing.JMenuItem();
        mnit_LabHistory = new javax.swing.JMenuItem();
        mnit_LabHistoryDM = new javax.swing.JMenuItem();
        mnit_XRayHistory = new javax.swing.JMenuItem();
        mnit_Allergy = new javax.swing.JMenuItem();
        mnit_Complication = new javax.swing.JMenuItem();
        mnit_Send = new javax.swing.JMenuItem();
        mnit_Back = new javax.swing.JMenuItem();
        menu_Edit = new javax.swing.JMenu();
        mnit_FocusS = new javax.swing.JMenuItem();
        mnit_FocusD = new javax.swing.JMenuItem();
        mnit_FocusP = new javax.swing.JMenuItem();
        mnit_FocusM = new javax.swing.JMenuItem();
        mnit_Clear = new javax.swing.JMenuItem();
        mnit_ClearS = new javax.swing.JMenuItem();
        mnit_ClearD = new javax.swing.JMenuItem();
        mnin_ClearP = new javax.swing.JMenuItem();
        mnit_ClearM = new javax.swing.JMenuItem();
        menu_SetDM = new javax.swing.JMenu();
        jMenu1 = new javax.swing.JMenu();
        mnit_V1 = new javax.swing.JMenuItem();
        mnit_V2 = new javax.swing.JMenuItem();
        mnit_V3 = new javax.swing.JMenuItem();

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

        btn_Close.setText("Close");
        btn_Close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CloseActionPerformed(evt);
            }
        });

        dia_RevisitTime.setTitle("Revisit");
        dia_RevisitTime.setMinimumSize(new java.awt.Dimension(480, 185));
        dia_RevisitTime.setResizable(false);

        jButton2.setText("OK");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(246, 246, 246));

        txt_PackageId.setEditable(false);

        jLabel1.setText("Need to revisit the project:");

        jLabel2.setText("Package Codet:");

        jLabel3.setText("Next revisit will be in days is:");

        txt_PackageType.setEditable(false);

        jLabel4.setText("Days");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_PackageId, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                    .addComponent(txt_PackageType, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(txt_ComeBackDays, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txt_PackageType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txt_PackageId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
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
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        dia_RevisitTimeLayout.setVerticalGroup(
            dia_RevisitTimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dia_RevisitTimeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addContainerGap())
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("DiagnosisInfo");
        setBackground(new java.awt.Color(246, 246, 246));

        pan_Top.setFont(new java.awt.Font("Bitstream Vera Sans", 1, 18));

        lab_TitleNo.setText("Patient No.");

        lab_TitleName.setText("Name");

        lab_TitlePs.setText("p.s.");

        lab_TitleSex.setText("Gender");

        lab_TitleAge.setText("Age");

        lab_TitleBloodtype.setText("Blood type");

        txt_No.setEditable(false);

        txt_Age.setEditable(false);

        txt_Bloodtype.setEditable(false);

        txt_Name.setEditable(false);

        txt_Sex.setEditable(false);

        txt_Ps.setEditable(false);

        lab_Height.setText("Height");

        lab_Weight.setText("Weight");

        txt_Height.setEditable(false);

        txt_Weight.setEditable(false);

        javax.swing.GroupLayout pan_TopLayout = new javax.swing.GroupLayout(pan_Top);
        pan_Top.setLayout(pan_TopLayout);
        pan_TopLayout.setHorizontalGroup(
            pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_TopLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lab_TitleNo, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
                    .addComponent(lab_Height)
                    .addComponent(lab_TitleName)
                    .addComponent(lab_TitlePs, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pan_TopLayout.createSequentialGroup()
                        .addGroup(pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txt_No, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(pan_TopLayout.createSequentialGroup()
                                    .addComponent(txt_Height, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(lab_Weight)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txt_Weight, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(txt_Name, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(pan_TopLayout.createSequentialGroup()
                                .addGroup(pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lab_TitleAge)
                                    .addComponent(lab_TitleSex))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txt_Age)
                                    .addComponent(txt_Sex, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)))
                            .addGroup(pan_TopLayout.createSequentialGroup()
                                .addComponent(lab_TitleBloodtype)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_Bloodtype))))
                    .addComponent(txt_Ps))
                .addGap(22, 22, 22))
        );
        pan_TopLayout.setVerticalGroup(
            pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan_TopLayout.createSequentialGroup()
                .addGroup(pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lab_TitleSex)
                    .addComponent(txt_Sex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_TitleNo)
                    .addComponent(txt_No, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_Name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_TitleAge)
                    .addComponent(txt_Age, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_TitleName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_Weight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_Weight)
                    .addComponent(lab_Height)
                    .addComponent(txt_Height, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_TitleBloodtype)
                    .addComponent(txt_Bloodtype, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pan_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_Ps, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_TitlePs)))
        );

        pan_CenterTop.setBorder(javax.swing.BorderFactory.createTitledBorder("Medical Record Summary"));
        pan_CenterTop.setPreferredSize(new java.awt.Dimension(813, 132));

        txta_Summary.setColumns(20);
        txta_Summary.setRows(3);
        txta_Summary.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txta_SummaryMouseClicked(evt);
            }
        });
        span_Summary.setViewportView(txta_Summary);

        javax.swing.GroupLayout pan_SummaryLayout = new javax.swing.GroupLayout(pan_Summary);
        pan_Summary.setLayout(pan_SummaryLayout);
        pan_SummaryLayout.setHorizontalGroup(
            pan_SummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(span_Summary, javax.swing.GroupLayout.DEFAULT_SIZE, 445, Short.MAX_VALUE)
        );
        pan_SummaryLayout.setVerticalGroup(
            pan_SummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(span_Summary, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pan_CenterTopLayout = new javax.swing.GroupLayout(pan_CenterTop);
        pan_CenterTop.setLayout(pan_CenterTopLayout);
        pan_CenterTopLayout.setHorizontalGroup(
            pan_CenterTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pan_Summary, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pan_CenterTopLayout.setVerticalGroup(
            pan_CenterTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pan_Summary, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pan_Center.setBorder(javax.swing.BorderFactory.createTitledBorder("Medicine"));
        pan_Center.setPreferredSize(new java.awt.Dimension(813, 163));

        tab_Medicine.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "", "藥品代碼", "藥品名稱", "次劑量", "單位", "服法", "途徑", "天", "總量", "急", "磨", "備註", "隱藏的服法次數"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, false, true, true, true, true, true, true, true, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tab_Medicine.setRowHeight(25);
        tab_Medicine.getTableHeader().setReorderingAllowed(false);
        tab_Medicine.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tab_MedicineMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tab_MedicineMousePressed(evt);
            }
        });
        tab_Medicine.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tab_MedicineFocusGained(evt);
            }
        });
        tab_Medicine.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tab_MedicineKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tab_MedicineKeyReleased(evt);
            }
        });
        span_Medicine.setViewportView(tab_Medicine);

        javax.swing.GroupLayout pan_CenterLayout = new javax.swing.GroupLayout(pan_Center);
        pan_Center.setLayout(pan_CenterLayout);
        pan_CenterLayout.setHorizontalGroup(
            pan_CenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(span_Medicine, javax.swing.GroupLayout.DEFAULT_SIZE, 824, Short.MAX_VALUE)
        );
        pan_CenterLayout.setVerticalGroup(
            pan_CenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(span_Medicine, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)
        );

        btn_Send.setText("Send");
        btn_Send.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_SendActionPerformed(evt);
            }
        });

        pan_Diagnosis.setBorder(javax.swing.BorderFactory.createTitledBorder("Diagnosis"));

        tab_Diagnosis.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "", "ICD CODE", "NAME"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tab_Diagnosis.setRowHeight(25);
        tab_Diagnosis.getTableHeader().setReorderingAllowed(false);
        tab_Diagnosis.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tab_DiagnosisMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tab_DiagnosisMousePressed(evt);
            }
        });
        tab_Diagnosis.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tab_DiagnosisFocusGained(evt);
            }
        });
        tab_Diagnosis.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tab_DiagnosisKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tab_DiagnosisKeyReleased(evt);
            }
        });
        span_Diagnosis.setViewportView(tab_Diagnosis);

        btn_First.setText("=");
        btn_First.setEnabled(false);
        btn_First.setMaximumSize(new java.awt.Dimension(30, 20));
        btn_First.setMinimumSize(new java.awt.Dimension(30, 20));
        btn_First.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_FirstActionPerformed(evt);
            }
        });

        btn_Up.setText("_");
        btn_Up.setEnabled(false);
        btn_Up.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_UpActionPerformed(evt);
            }
        });

        btn_Down.setText("_");
        btn_Down.setEnabled(false);
        btn_Down.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_DownActionPerformed(evt);
            }
        });

        btn_Last.setText("=");
        btn_Last.setEnabled(false);
        btn_Last.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_LastActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pan_CenterRightLayout = new javax.swing.GroupLayout(pan_CenterRight);
        pan_CenterRight.setLayout(pan_CenterRightLayout);
        pan_CenterRightLayout.setHorizontalGroup(
            pan_CenterRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btn_Up, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btn_Down, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btn_First, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btn_Last)
        );
        pan_CenterRightLayout.setVerticalGroup(
            pan_CenterRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_CenterRightLayout.createSequentialGroup()
                .addComponent(btn_First, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Up, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addComponent(btn_Down, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Last, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout pan_DiagnosisLayout = new javax.swing.GroupLayout(pan_Diagnosis);
        pan_Diagnosis.setLayout(pan_DiagnosisLayout);
        pan_DiagnosisLayout.setHorizontalGroup(
            pan_DiagnosisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan_DiagnosisLayout.createSequentialGroup()
                .addComponent(span_Diagnosis, javax.swing.GroupLayout.DEFAULT_SIZE, 779, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pan_CenterRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pan_DiagnosisLayout.setVerticalGroup(
            pan_DiagnosisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(span_Diagnosis, 0, 0, Short.MAX_VALUE)
            .addComponent(pan_CenterRight, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pan_Prescription.setBorder(javax.swing.BorderFactory.createTitledBorder("Laboratory/Radiology(X-RAY)"));
        pan_Prescription.setPreferredSize(new java.awt.Dimension(813, 173));

        tab_Prescription.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "", "內容"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tab_Prescription.setRowHeight(25);
        tab_Prescription.getTableHeader().setReorderingAllowed(false);
        tab_Prescription.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tab_PrescriptionMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tab_PrescriptionMousePressed(evt);
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
            .addComponent(span_Prescription, javax.swing.GroupLayout.DEFAULT_SIZE, 824, Short.MAX_VALUE)
        );
        pan_PrescriptionLayout.setVerticalGroup(
            pan_PrescriptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(span_Prescription, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
        );

        pan_Function.setOpaque(false);

        btn_Allergy.setText("Drug Allergy");
        btn_Allergy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_AllergyActionPerformed(evt);
            }
        });

        btn_Medicine.setText("Medicine Code");
        btn_Medicine.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_MedicineActionPerformed(evt);
            }
        });

        btn_Diagnosis.setText("Diagnosis Code");
        btn_Diagnosis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_DiagnosisActionPerformed(evt);
            }
        });

        btn_Prescription.setText("Laboratory/Radiology(X-RAY) Request");
        btn_Prescription.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_PrescriptionActionPerformed(evt);
            }
        });

        btn_CaseManagement.setText("Case Management");
        btn_CaseManagement.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CaseManagementActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pan_FunctionLayout = new javax.swing.GroupLayout(pan_Function);
        pan_Function.setLayout(pan_FunctionLayout);
        pan_FunctionLayout.setHorizontalGroup(
            pan_FunctionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_FunctionLayout.createSequentialGroup()
                .addComponent(btn_Diagnosis)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Prescription)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Medicine)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Allergy)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_CaseManagement)
                .addContainerGap(171, Short.MAX_VALUE))
        );
        pan_FunctionLayout.setVerticalGroup(
            pan_FunctionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_FunctionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btn_Diagnosis, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btn_Prescription, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btn_Medicine, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btn_Allergy, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btn_CaseManagement, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        lab_Ps.setText("p.s");

        menu_Send.setText("File");
        menu_Send.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_SendActionPerformed(evt);
            }
        });

        mnit_Diagnosis.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        mnit_Diagnosis.setText("Diagnosis Code");
        mnit_Diagnosis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnit_DiagnosisActionPerformed(evt);
            }
        });
        menu_Send.add(mnit_Diagnosis);

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F2, 0));
        jMenuItem1.setText("Laboratory/Radiology(X-RAY) Request");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        menu_Send.add(jMenuItem1);

        mnit_Medicine.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F3, 0));
        mnit_Medicine.setText("Medicine Code");
        mnit_Medicine.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnit_MedicineActionPerformed(evt);
            }
        });
        menu_Send.add(mnit_Medicine);

        mnit_Casehistory.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, 0));
        mnit_Casehistory.setText("Medical Record");
        mnit_Casehistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnit_CasehistoryActionPerformed(evt);
            }
        });
        menu_Send.add(mnit_Casehistory);

        mnit_LabHistory.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
        mnit_LabHistory.setText("Laboratory Record");
        mnit_LabHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnit_LabHistoryActionPerformed(evt);
            }
        });
        menu_Send.add(mnit_LabHistory);

        mnit_LabHistoryDM.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F8, 0));
        mnit_LabHistoryDM.setText("Laboratory Record(For DM)");
        mnit_LabHistoryDM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnit_LabHistoryDMActionPerformed(evt);
            }
        });
        menu_Send.add(mnit_LabHistoryDM);

        mnit_XRayHistory.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F6, 0));
        mnit_XRayHistory.setText("Radiology(X-RAY) Record");
        mnit_XRayHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnit_XRayHistoryActionPerformed(evt);
            }
        });
        menu_Send.add(mnit_XRayHistory);

        mnit_Allergy.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F7, 0));
        mnit_Allergy.setText("Drug Allergy");
        mnit_Allergy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnit_AllergyActionPerformed(evt);
            }
        });
        menu_Send.add(mnit_Allergy);

        mnit_Complication.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        mnit_Complication.setText("Case Management");
        mnit_Complication.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnit_ComplicationActionPerformed(evt);
            }
        });
        menu_Send.add(mnit_Complication);

        mnit_Send.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        mnit_Send.setText("Send");
        mnit_Send.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnit_SendActionPerformed(evt);
            }
        });
        menu_Send.add(mnit_Send);

        mnit_Back.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0));
        mnit_Back.setText("Close");
        mnit_Back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnit_BackActionPerformed(evt);
            }
        });
        menu_Send.add(mnit_Back);

        menubar.add(menu_Send);

        menu_Edit.setText("Edit");

        mnit_FocusS.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.ALT_MASK));
        mnit_FocusS.setText("Focus Summary");
        mnit_FocusS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnit_FocusSActionPerformed(evt);
            }
        });
        menu_Edit.add(mnit_FocusS);

        mnit_FocusD.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.ALT_MASK));
        mnit_FocusD.setText("Focus Diagnosis");
        mnit_FocusD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnit_FocusDActionPerformed(evt);
            }
        });
        menu_Edit.add(mnit_FocusD);

        mnit_FocusP.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.ALT_MASK));
        mnit_FocusP.setText("Focus Prescription");
        mnit_FocusP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnit_FocusPActionPerformed(evt);
            }
        });
        menu_Edit.add(mnit_FocusP);

        mnit_FocusM.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.ALT_MASK));
        mnit_FocusM.setText("Focus Medicine");
        mnit_FocusM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnit_FocusMActionPerformed(evt);
            }
        });
        menu_Edit.add(mnit_FocusM);

        mnit_Clear.setText("Clear All");
        mnit_Clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnit_ClearActionPerformed(evt);
            }
        });
        menu_Edit.add(mnit_Clear);

        mnit_ClearS.setText("Clear Summary");
        mnit_ClearS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnit_ClearSActionPerformed(evt);
            }
        });
        menu_Edit.add(mnit_ClearS);

        mnit_ClearD.setText("Clear Diagnosis");
        mnit_ClearD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnit_ClearDActionPerformed(evt);
            }
        });
        menu_Edit.add(mnit_ClearD);

        mnin_ClearP.setText("Clear Prescription");
        mnin_ClearP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnin_ClearPActionPerformed(evt);
            }
        });
        menu_Edit.add(mnin_ClearP);

        mnit_ClearM.setText("Clear Medicine");
        mnit_ClearM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnit_ClearMActionPerformed(evt);
            }
        });
        menu_Edit.add(mnit_ClearM);

        menubar.add(menu_Edit);

        menu_SetDM.setText("Set");

        jMenu1.setText("DM");

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

        menubar.add(menu_SetDM);

        setJMenuBar(menubar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pan_Center, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 836, Short.MAX_VALUE)
                    .addComponent(pan_Prescription, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 836, Short.MAX_VALUE)
                    .addComponent(pan_Diagnosis, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pan_Function, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(lab_Ps)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_Message, javax.swing.GroupLayout.DEFAULT_SIZE, 693, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_Send, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(pan_Top, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pan_CenterTop, javax.swing.GroupLayout.DEFAULT_SIZE, 457, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pan_Top, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pan_CenterTop, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pan_Function, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pan_Diagnosis, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pan_Prescription, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pan_Center, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_Send)
                    .addComponent(txt_Message, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_Ps))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_AllergyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_AllergyActionPerformed
        this.setEnabled(false);
        new Frm_DiagnosisAllergy(this, this.txt_No.getText(), this.txt_Name.getText()).setVisible(true);
}//GEN-LAST:event_btn_AllergyActionPerformed

    private void btn_DiagnosisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_DiagnosisActionPerformed
        this.setEnabled(false);
        new Frm_DiagnosisTherapy(this, m_IsDM).setVisible(true);
        tab_Diagnosis.removeRowSelectionInterval(0, tab_Diagnosis.getRowCount()-1);
        tab_DiagnosisFocusGained(null);
        if (tab_Diagnosis.getSelectedRow() == -1) {
            btn_First.setEnabled(false);
            btn_Last.setEnabled(false);
            btn_Up.setEnabled(false);
            btn_Down.setEnabled(false);
        }
}//GEN-LAST:event_btn_DiagnosisActionPerformed

    private void btn_MedicineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_MedicineActionPerformed
        try {
            this.setEnabled(false);
            new Frm_DiagnosisMedicine(this, m_IsDM).setVisible(true);
            tab_Medicine.removeRowSelectionInterval(0, tab_Medicine.getRowCount() - 1);
            tab_MedicineFocusGained(null);
        } catch (IOException ex) {
            Logger.getLogger(Frm_DiagnosisInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
}//GEN-LAST:event_btn_MedicineActionPerformed

    private void tab_DiagnosisFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tab_DiagnosisFocusGained
        m_AutoTable = "diagnosis_code";
        String[] diagnosisRsList = {"icd_code", "name"};
        m_AutoColumn = diagnosisRsList;
        m_AutoColumnName = "icd_code";
        m_SelectTable = tab_Diagnosis;
        m_AutoLocationSpane = span_Diagnosis;
        m_SelectTableAddRowLimitColumn = 2;
        m_SelectTableModel = m_DiagnosisModel ;
        m_SelectTableRowNo = m_DiagnosisRowNo;
        m_SelectTableNo = 1;
        m_SelectTableHashMap = m_DiagnosisHashMap;
}//GEN-LAST:event_tab_DiagnosisFocusGained

    private void tab_DiagnosisKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tab_DiagnosisKeyPressed
        if (tab_Diagnosis.getSelectedColumn() == 1 && evt.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            if(m_SelectTable.getValueAt(m_SelectTable.getSelectedRow(), 1) != null) {
                setBackspaceTxt();
            }
        }

        if (evt.getKeyCode() == KeyEvent.VK_ALT) {
            setAllRemoveEditAndSelection();
        }
}//GEN-LAST:event_tab_DiagnosisKeyPressed

    private void tab_MedicineFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tab_MedicineFocusGained
        m_AutoTable = "medicines";
        String[] medicineRsList = {"code", "item", "injection"};  // , "unit_dosage",  "unit"
        m_AutoColumn = medicineRsList;
        m_AutoColumnName = "item";
        m_SelectTable = tab_Medicine;
        m_AutoLocationSpane = span_Medicine;
        m_SelectTableAddRowLimitColumn = 3;
        m_SelectTableModel = m_MedicineModel ;
        m_SelectTableRowNo = m_MedicineRowNo;
        m_SelectTableNo = 3;
        m_SelectTableHashMap = m_MedicineHashMap;
        System.out.println("鎖定藥品");
}//GEN-LAST:event_tab_MedicineFocusGained

    private void btn_SendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SendActionPerformed
        setAllRemoveEditAndSelection();
        if (m_FinishState != true) {
            setSaveDiagnosis();
        } else {
            Object[] options = {paragraph.getLanguage(message , "YES"),paragraph.getLanguage(message , "NO")};
            int dialog = JOptionPane.showOptionDialog(
                            null,
                            paragraph.getLanguage(message , "DOYOUWANTTOCHANGETHEDATA"),
                            paragraph.getLanguage(message , "WARNING"),
                            JOptionPane.YES_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options,
                            options[0]
                        );
            if (dialog == 0) {
                setSaveDiagnosis();
            }
        }
    }//GEN-LAST:event_btn_SendActionPerformed

    private void mnit_DiagnosisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnit_DiagnosisActionPerformed
        btn_DiagnosisActionPerformed(null);
    }//GEN-LAST:event_mnit_DiagnosisActionPerformed

    private void mnit_MedicineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnit_MedicineActionPerformed
        btn_MedicineActionPerformed(null);
    }//GEN-LAST:event_mnit_MedicineActionPerformed

    private void mnit_CasehistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnit_CasehistoryActionPerformed
        this.setEnabled(false);
        new Frm_DiagnosisDiagnostic(this, m_Pno ,this.txt_Name.getText()).setVisible(true);
    }//GEN-LAST:event_mnit_CasehistoryActionPerformed

    private void mnit_AllergyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnit_AllergyActionPerformed
        btn_AllergyActionPerformed(null);
    }//GEN-LAST:event_mnit_AllergyActionPerformed

    private void mnit_BackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnit_BackActionPerformed
        new Frm_WorkList(m_WorkListRowNo, "dia").setVisible(true);
        this.dispose();
    }//GEN-LAST:event_mnit_BackActionPerformed

    private void mnit_SendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnit_SendActionPerformed
        btn_SendActionPerformed(null);
    }//GEN-LAST:event_mnit_SendActionPerformed

    private void tab_DiagnosisKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tab_DiagnosisKeyReleased
        if (isAllowKeyIn(evt) && m_SelectTable.getSelectedRow() != -1) {
             if (m_SelectTable.getColumnCount() > 2 && m_SelectTable.getValueAt(m_SelectTable.getSelectedRow(), 2) != null) {
//                    System.out.println("移除 hashmap code = "+m_SelectTable.getValueAt(m_SelectTable.getSelectedRow(), 1).toString().trim());
                    m_SelectTableHashMap.remove(m_SelectTable.getValueAt(m_SelectTable.getSelectedRow(), 1).toString().trim());
             }
             getAutoCompleteValue(m_AutoTxt.getText());
        }
        tab_DiagnosisMouseClicked(null);
        if(tab_Diagnosis.getSelectedRow() != -1
            && (evt.getKeyCode() == KeyEvent.VK_ENTER
            || evt.getKeyCode() == KeyEvent.VK_DELETE)) {
            m_DiagnosisRowNo = setRowValue(evt);
        }
        setMoveBtnEnable();
    }//GEN-LAST:event_tab_DiagnosisKeyReleased

    private void btn_UpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_UpActionPerformed
        if (btn_Up.isEnabled()) {
            Object selectCode = tab_Diagnosis.getValueAt(tab_Diagnosis.getSelectedRow(), 1);
            Object selectName = tab_Diagnosis.getValueAt(tab_Diagnosis.getSelectedRow(), 2);

            Object tempCode = tab_Diagnosis.getValueAt(tab_Diagnosis.getSelectedRow()-1, 1);
            Object tempName = tab_Diagnosis.getValueAt(tab_Diagnosis.getSelectedRow()-1, 2);

            tab_Diagnosis.setValueAt(selectCode, tab_Diagnosis.getSelectedRow()-1, 1);
            tab_Diagnosis.setValueAt(selectName, tab_Diagnosis.getSelectedRow()-1, 2);

            tab_Diagnosis.setValueAt(tempCode, tab_Diagnosis.getSelectedRow(), 1);
            tab_Diagnosis.setValueAt(tempName, tab_Diagnosis.getSelectedRow(), 2);

            tab_Diagnosis.setRowSelectionInterval(tab_Diagnosis.getSelectedRow()-1, tab_Diagnosis.getSelectedRow()-1);
        }
        setMoveBtnEnable();
    }//GEN-LAST:event_btn_UpActionPerformed

    private void btn_DownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_DownActionPerformed
        if (btn_Down.isEnabled()) {
            Object selectCode = tab_Diagnosis.getValueAt(tab_Diagnosis.getSelectedRow(), 1);
            Object selectName = tab_Diagnosis.getValueAt(tab_Diagnosis.getSelectedRow(), 2);

            Object tempCode = tab_Diagnosis.getValueAt(tab_Diagnosis.getSelectedRow()+1, 1);
            Object tempName = tab_Diagnosis.getValueAt(tab_Diagnosis.getSelectedRow()+1, 2);

            tab_Diagnosis.setValueAt(selectCode, tab_Diagnosis.getSelectedRow()+1, 1);
            tab_Diagnosis.setValueAt(selectName, tab_Diagnosis.getSelectedRow()+1, 2);

            tab_Diagnosis.setValueAt(tempCode, tab_Diagnosis.getSelectedRow(), 1);
            tab_Diagnosis.setValueAt(tempName, tab_Diagnosis.getSelectedRow(), 2);

            tab_Diagnosis.setRowSelectionInterval(tab_Diagnosis.getSelectedRow()+1, tab_Diagnosis.getSelectedRow()+1);
        }
        setMoveBtnEnable();
    }//GEN-LAST:event_btn_DownActionPerformed

    private void btn_FirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_FirstActionPerformed
        while(btn_First.isEnabled()) {
            btn_UpActionPerformed(null);
        }
    }//GEN-LAST:event_btn_FirstActionPerformed

    private void btn_LastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_LastActionPerformed
       while(btn_Last.isEnabled()) {
            btn_DownActionPerformed(null);
        }
    }//GEN-LAST:event_btn_LastActionPerformed

    private void txta_SummaryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txta_SummaryMouseClicked
        mnit_FocusSActionPerformed(null);
    }//GEN-LAST:event_txta_SummaryMouseClicked

    private void mnit_ClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnit_ClearActionPerformed
         TabTools.setClearTableValue(tab_Diagnosis);
         TabTools.setClearTableValue(tab_Prescription);
         TabTools.setClearTableValue(tab_Medicine);
        txta_Summary.setText(null);
        m_DiagnosisHashMap.clear();
        m_MedicineHashMap.clear();
        m_PrescriptionHashMap.clear();
    }//GEN-LAST:event_mnit_ClearActionPerformed

    private void tab_DiagnosisMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab_DiagnosisMouseClicked
        if (this.tab_Diagnosis.getSelectedColumn() != 1) {
            this.tab_Diagnosis.setColumnSelectionInterval(1, 1);
        }


    }//GEN-LAST:event_tab_DiagnosisMouseClicked

    private void tab_MedicineKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tab_MedicineKeyReleased

        if (this.tab_Medicine.getSelectedColumn() == 0 ) {

              this.tab_Medicine.setColumnSelectionInterval(1, 1);
        }

        if (this.tab_Medicine.getSelectedColumn() == 2 ) {  // Medicine hide
            if (evt.getKeyCode() == KeyEvent.VK_RIGHT || evt.getKeyCode() == KeyEvent.VK_TAB) {
                this.tab_Medicine.setColumnSelectionInterval(3, 3);
            } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
                this.tab_Medicine.setColumnSelectionInterval(1, 1);
            }
        }

        if (this.tab_Medicine.getSelectedColumn() == 5 ) {  // Medicine hide
            if (evt.getKeyCode() == KeyEvent.VK_RIGHT || evt.getKeyCode() == KeyEvent.VK_TAB) {
                this.tab_Medicine.setColumnSelectionInterval(6, 6);
            } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
                this.tab_Medicine.setColumnSelectionInterval(4, 4);
            }
        }

        if (this.tab_Medicine.getSelectedColumn() == 13 ) {  // Medicine hide
            if (evt.getKeyCode() == KeyEvent.VK_RIGHT || evt.getKeyCode() == KeyEvent.VK_TAB) {
                this.tab_Medicine.setColumnSelectionInterval(13, 13);
            }
        }

        if (tab_Medicine.getSelectedColumn() == 1 && isAllowKeyIn(evt)) {
             
             if (m_SelectTable.getSelectedRow() != -1 && m_SelectTable.getColumnCount() > 2
              && m_SelectTable.getValueAt(m_SelectTable.getSelectedRow(), 2) != null) {
                    m_SelectTableHashMap.remove(m_SelectTable.getValueAt(m_SelectTable.getSelectedRow(), 2).toString().trim());
             }
             getAutoCompleteValue(m_AutoTxt.getText());
        }

        if (tab_Medicine.getSelectedRow() != -1) {
            int selectRow = tab_Medicine.getSelectedRow();
            int selectColumn = tab_Medicine.getSelectedColumn();
            if (selectColumn == 1 && (evt.getKeyCode() == KeyEvent.VK_ENTER || evt.getKeyCode() == KeyEvent.VK_DELETE)) {
                m_MedicineRowNo = setRowValue(evt);
            } else if (evt.getKeyCode() == KeyEvent.VK_DELETE && selectColumn == 6) {
                tab_Medicine.setValueAt(null, selectRow, 6);
                tab_Medicine.setValueAt(null, selectRow, 13);
                tab_Medicine.removeEditor(); 
            } else if (evt.getKeyCode() == KeyEvent.VK_DELETE &&
                      (selectColumn == 10 || selectColumn == 11)) {
                tab_Medicine.setValueAt("N", selectRow, selectColumn);
                tab_Medicine.removeEditor(); 
            }

            else if (evt.getKeyCode() == KeyEvent.VK_DELETE &&
                      (selectColumn != 0 && selectColumn != 2 && selectColumn != 3 && selectColumn != 5)) {
                tab_Medicine.setValueAt(null, selectRow, selectColumn);
                tab_Medicine.removeEditor();
            }
        }
        if (evt.getKeyCode() == KeyEvent.VK_ENTER || evt.getKeyCode() == KeyEvent.VK_DOWN) {
            setMedicineValue(tab_Medicine.getSelectedRow()-1);
            if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                tab_Medicine.setColumnSelectionInterval(2, 1);
            }
        } else if (evt.getKeyCode() == KeyEvent.VK_UP) {
            setMedicineValue(tab_Medicine.getSelectedRow()+1);
        } else if (evt.getKeyCode() != KeyEvent.VK_DELETE) {
            setMedicineValue(tab_Medicine.getSelectedRow());
        }

        
    }//GEN-LAST:event_tab_MedicineKeyReleased

    private void tab_PrescriptionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab_PrescriptionMouseClicked
//        if (this.tab_Prescription.getSelectedColumn() == 2) {
//            this.tab_Prescription.setColumnSelectionInterval(1, 1);
//        } else if (this.tab_Prescription.getSelectedColumn() == 1){
//
//        }
    }//GEN-LAST:event_tab_PrescriptionMouseClicked

    private void tab_PrescriptionKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tab_PrescriptionKeyReleased

        if (isAllowKeyIn(evt) && m_SelectTable.getSelectedRow() != -1) {
             if (m_SelectTable.getColumnCount() > 2 && m_SelectTable.getValueAt(m_SelectTable.getSelectedRow(), 2) != null) {
//                    System.out.println("移除 hashmap code = "+m_SelectTable.getValueAt(m_SelectTable.getSelectedRow(), 1).toString().trim());
                    m_SelectTableHashMap.remove(m_SelectTable.getValueAt(m_SelectTable.getSelectedRow(), 1).toString().trim());
             }
             getAutoCompleteValue(m_AutoTxt.getText());
        }
        tab_PrescriptionMouseClicked(null);
        if(tab_Prescription.getSelectedRow() != -1
            && (evt.getKeyCode() == KeyEvent.VK_ENTER
            || evt.getKeyCode() == KeyEvent.VK_DELETE)) {
            m_PrescriptionRowNo = setRowValue(evt);
        }
    }//GEN-LAST:event_tab_PrescriptionKeyReleased

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

    private void tab_MedicineMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab_MedicineMouseClicked
        setMedicineValue(tab_Medicine.getSelectedRow());
        if (this.tab_Medicine.getSelectedColumn() == 0 ) {
            this.tab_Medicine.setColumnSelectionInterval(1, 1);
        }
    }//GEN-LAST:event_tab_MedicineMouseClicked

    private void tab_DiagnosisMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab_DiagnosisMousePressed
        if (tab_Diagnosis.getSelectedColumn() == 1 ) {
            tab_Diagnosis.removeEditor();
        }
         TabTools.setRemoveEditAndSelection(tab_Prescription);
         TabTools.setRemoveEditAndSelection(tab_Medicine);
        setMoveBtnEnable();
    }//GEN-LAST:event_tab_DiagnosisMousePressed

    private void tab_PrescriptionMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab_PrescriptionMousePressed
         TabTools.setRemoveEditAndSelection(tab_Diagnosis);
         TabTools.setRemoveEditAndSelection(tab_Medicine);
        setMoveBtnEnable();
        tab_Prescription.removeEditor();
    }//GEN-LAST:event_tab_PrescriptionMousePressed

    private void tab_MedicineMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab_MedicineMousePressed
         TabTools.setRemoveEditAndSelection(tab_Diagnosis);
         TabTools.setRemoveEditAndSelection(tab_Prescription);
        setMoveBtnEnable();
        tab_Medicine.removeEditor();
    }//GEN-LAST:event_tab_MedicineMousePressed

    private void mnit_FocusSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnit_FocusSActionPerformed
        setAllRemoveEditAndSelection();
        setMoveBtnEnable();
        new Summary(this, txta_Summary.getText().trim(), pan_Summary.getLocationOnScreen(),
                                 pan_Summary.getWidth(), pan_Summary.getHeight(),
                                 txta_Summary.getLineCount(),true ).setVisible(true);
    }//GEN-LAST:event_mnit_FocusSActionPerformed

    private void mnit_FocusDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnit_FocusDActionPerformed
        setFocusRapid(tab_Diagnosis);
    }//GEN-LAST:event_mnit_FocusDActionPerformed

    private void mnit_FocusPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnit_FocusPActionPerformed
        setFocusRapid(tab_Prescription);
    }//GEN-LAST:event_mnit_FocusPActionPerformed

    private void mnit_FocusMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnit_FocusMActionPerformed
        setFocusRapid(tab_Medicine);
    }//GEN-LAST:event_mnit_FocusMActionPerformed

    private void mnit_ClearSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnit_ClearSActionPerformed
        txta_Summary.setText(null);
    }//GEN-LAST:event_mnit_ClearSActionPerformed

    private void mnit_ClearDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnit_ClearDActionPerformed
         TabTools.setClearTableValue(tab_Diagnosis);
        m_DiagnosisHashMap.clear();
    }//GEN-LAST:event_mnit_ClearDActionPerformed

    private void mnin_ClearPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnin_ClearPActionPerformed
         TabTools.setClearTableValue(tab_Prescription);
         m_PrescriptionHashMap.clear();
    }//GEN-LAST:event_mnin_ClearPActionPerformed

    private void mnit_ClearMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnit_ClearMActionPerformed
        TabTools.setClearTableValue(tab_Medicine);
        m_MedicineHashMap.clear();
    }//GEN-LAST:event_mnit_ClearMActionPerformed

    private void tab_MedicineKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tab_MedicineKeyPressed
        if (tab_Medicine.getSelectedColumn() == 2 && evt.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            setBackspaceTxt();
        }
       
        if ((tab_Medicine.getSelectedColumn() == 10 || tab_Medicine.getSelectedColumn() == 11) && isKeyIn(String.valueOf(evt.getKeyChar()))) {
            tab_Medicine.setValueAt(null, tab_Medicine.getSelectedRow(), tab_Medicine.getSelectedColumn());
        }
        if (evt.getKeyCode() == KeyEvent.VK_ALT) {
            setAllRemoveEditAndSelection();
        }
    }//GEN-LAST:event_tab_MedicineKeyPressed

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

    private void btn_PrescriptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_PrescriptionActionPerformed
        this.setEnabled(false);
        new Frm_DiagnosisPrescription(this).setVisible(true);
        tab_Prescription.removeRowSelectionInterval(0, tab_Prescription.getRowCount()-1);
        tab_PrescriptionFocusGained(null);
    }//GEN-LAST:event_btn_PrescriptionActionPerformed

    private void mnit_V2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnit_V2ActionPerformed
       setV("V2");
       m_PackageSet = "V2 F/U per 3 months,";
       m_PackageSetId = "V2,";
    }//GEN-LAST:event_mnit_V2ActionPerformed

    private void mnit_V3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnit_V3ActionPerformed
       setV("V3");
       m_PackageSet = "V3 per year,";
       m_PackageSetId = "V3,";
    }//GEN-LAST:event_mnit_V3ActionPerformed

    private void mnit_V1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnit_V1ActionPerformed
       setV("V1");
       m_PackageSet = "V1 new patient,";
       m_PackageSetId = "V1,";
}//GEN-LAST:event_mnit_V1ActionPerformed

    private void mnit_LabHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnit_LabHistoryActionPerformed
        this.setEnabled(false);
        new Frm_LabHistory(this, m_Pno).setVisible(true);
    }//GEN-LAST:event_mnit_LabHistoryActionPerformed

    private void list_MenuKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_list_MenuKeyReleased
        if(isKeyIn(String.valueOf(evt.getKeyChar()))) {
            setTxt(String.valueOf(evt.getKeyChar()));
        }
        if (evt.getKeyCode() != KeyEvent.VK_DELETE && list_Menu.getVisibleRowCount() > 0
                && (evt.getKeyCode() == KeyEvent.VK_UP || evt.getKeyCode() == KeyEvent.VK_DOWN)) {
            getSplitValue();
        }
}//GEN-LAST:event_list_MenuKeyReleased

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

    private void list_MenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_list_MenuMouseClicked
        getSplitValue();
        if (evt.getClickCount() == 2) {
            btn_CloseActionPerformed(null);
        }
}//GEN-LAST:event_list_MenuMouseClicked

    private void btn_CloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CloseActionPerformed
        setLostAutoCompleteEdit();
        if(m_RsRowCount == 0 ) {
            setClearTableRow(m_Row);
        }
        if (m_SelectTable.getValueAt(m_Row, 2) != null && m_AutoListValue != null || m_AutoTable.equals("prescription_code")) {
            if(!isCodeAtHashMap(m_AutoListValue[0].trim())) {
                setClearTableRow(m_Row);
            } else if (m_SelectTableNo == 1) {
                setMoveBtnEnable();
            }
        } else {
            setClearTableRow(m_Row);
        }
        dia.setVisible(false);
}//GEN-LAST:event_btn_CloseActionPerformed

    private void mnit_XRayHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnit_XRayHistoryActionPerformed
        this.setEnabled(false);
        new Frm_RadiologyHistory(this, m_Pno).setVisible(true);
    }//GEN-LAST:event_mnit_XRayHistoryActionPerformed

    private void mnit_ComplicationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnit_ComplicationActionPerformed
       new Frm_Case(m_Pno, m_RegistrationGuid, true,"dia").setVisible(true);
    }//GEN-LAST:event_mnit_ComplicationActionPerformed

    private void mnit_LabHistoryDMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnit_LabHistoryDMActionPerformed
        new Frm_LabDM(m_Pno).setVisible(true);
    }//GEN-LAST:event_mnit_LabHistoryDMActionPerformed

    private void btn_CaseManagementActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CaseManagementActionPerformed
        new Frm_Case(m_Pno, m_RegistrationGuid, true,"dia").setVisible(true);
}//GEN-LAST:event_btn_CaseManagementActionPerformed

    private void menu_SendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_SendActionPerformed
        btn_PrescriptionActionPerformed(null);
    }//GEN-LAST:event_menu_SendActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
       btn_PrescriptionActionPerformed(null);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
           if (Common.Tools.isNumber(txt_ComeBackDays.getText().trim())) {

             DBC.executeUpdate("INSERT INTO package_set(guid, reg_guid, id, use_date, sms_state, days) " +
                    "VALUES(UUID(), '" + m_RegistrationGuid + "', '" + txt_PackageId.getText() + "', NOW(), '0', "+txt_ComeBackDays.getText().trim()+")");

             JOptionPane.showMessageDialog(null,"Saved successfully.");
             dia_RevisitTime.dispose();
             this.dispose();
             Frm_WorkList frm_diagnosisWorkList = new Frm_WorkList(m_WorkListRowNo, "dia");
             frm_diagnosisWorkList.setVisible(true);
           } else {
                JOptionPane.showMessageDialog(null,"Please Check Days");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Frm_DiagnosisInfo.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jButton2ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Allergy;
    private javax.swing.JButton btn_CaseManagement;
    private javax.swing.JButton btn_Close;
    private javax.swing.JButton btn_Diagnosis;
    private javax.swing.JButton btn_Down;
    private javax.swing.JButton btn_First;
    private javax.swing.JButton btn_Last;
    private javax.swing.JButton btn_Medicine;
    private javax.swing.JButton btn_Prescription;
    private javax.swing.JButton btn_Send;
    private javax.swing.JButton btn_Up;
    private javax.swing.JDialog dia;
    private javax.swing.JDialog dia_RevisitTime;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lab_Height;
    private javax.swing.JLabel lab_Ps;
    private javax.swing.JLabel lab_TitleAge;
    private javax.swing.JLabel lab_TitleBloodtype;
    private javax.swing.JLabel lab_TitleName;
    private javax.swing.JLabel lab_TitleNo;
    private javax.swing.JLabel lab_TitlePs;
    private javax.swing.JLabel lab_TitleSex;
    private javax.swing.JLabel lab_Weight;
    private javax.swing.JList list_Menu;
    private javax.swing.JMenu menu_Edit;
    private javax.swing.JMenu menu_Send;
    private javax.swing.JMenu menu_SetDM;
    private javax.swing.JMenuBar menubar;
    private javax.swing.JMenuItem mnin_ClearP;
    private javax.swing.JMenuItem mnit_Allergy;
    private javax.swing.JMenuItem mnit_Back;
    private javax.swing.JMenuItem mnit_Casehistory;
    private javax.swing.JMenuItem mnit_Clear;
    private javax.swing.JMenuItem mnit_ClearD;
    private javax.swing.JMenuItem mnit_ClearM;
    private javax.swing.JMenuItem mnit_ClearS;
    private javax.swing.JMenuItem mnit_Complication;
    private javax.swing.JMenuItem mnit_Diagnosis;
    private javax.swing.JMenuItem mnit_FocusD;
    private javax.swing.JMenuItem mnit_FocusM;
    private javax.swing.JMenuItem mnit_FocusP;
    private javax.swing.JMenuItem mnit_FocusS;
    private javax.swing.JMenuItem mnit_LabHistory;
    private javax.swing.JMenuItem mnit_LabHistoryDM;
    private javax.swing.JMenuItem mnit_Medicine;
    private javax.swing.JMenuItem mnit_Send;
    private javax.swing.JMenuItem mnit_V1;
    private javax.swing.JMenuItem mnit_V2;
    private javax.swing.JMenuItem mnit_V3;
    private javax.swing.JMenuItem mnit_XRayHistory;
    private javax.swing.JPanel pan_Center;
    private javax.swing.JPanel pan_CenterRight;
    private javax.swing.JPanel pan_CenterTop;
    private javax.swing.JPanel pan_Diagnosis;
    private javax.swing.JPanel pan_Function;
    private javax.swing.JPanel pan_Prescription;
    private javax.swing.JPanel pan_Summary;
    private javax.swing.JPanel pan_Top;
    private javax.swing.JScrollPane span_Diagnosis;
    private javax.swing.JScrollPane span_ListMenu;
    private javax.swing.JScrollPane span_Medicine;
    private javax.swing.JScrollPane span_Prescription;
    private javax.swing.JScrollPane span_Summary;
    private javax.swing.JTable tab_Diagnosis;
    private javax.swing.JTable tab_Medicine;
    private javax.swing.JTable tab_Prescription;
    private javax.swing.JTextField txt_Age;
    private javax.swing.JTextField txt_Bloodtype;
    private javax.swing.JTextField txt_ComeBackDays;
    private javax.swing.JTextField txt_Height;
    private javax.swing.JTextField txt_Message;
    private javax.swing.JTextField txt_Name;
    private javax.swing.JTextField txt_No;
    private javax.swing.JTextField txt_PackageId;
    private javax.swing.JTextField txt_PackageType;
    private javax.swing.JTextField txt_Ps;
    private javax.swing.JTextField txt_Sex;
    private javax.swing.JTextField txt_Weight;
    private javax.swing.JTextArea txta_Summary;
    // End of variables declaration//GEN-END:variables


}


