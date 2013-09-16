package Laboratory;

import Common.Tools;
import cc.johnwu.date.DateMethod;
import cc.johnwu.sql.DBC;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class Frm_LabDM extends javax.swing.JFrame {
    private String m_Pno = null;

     // 僅衛教室與診間能使用
    public Frm_LabDM(String pno) {
        initComponents();
        this.setLocationRelativeTo(this);
        addWindowListener(new WindowAdapter() {  // 畫面關閉原視窗enable
            @Override
            public void windowClosing(WindowEvent windowevent) {
                mnit_CloseActionPerformed(null);
            }
        });
        setResultDateShow(false);

        m_Pno = pno;
        boolean state = false;
        init();
        
        this.setResizable(state);
        jPanel12.setEnabled(state);
        mnb.setVisible(state);
        txt_BGAC.setEditable(state);
        txt_BGPC.setEditable(state);
        cbox_Findings.setEditable(state);
        txt_HbA1C.setEditable(state);
        txt_Alb.setEditable(state);
        txt_Cholesterol.setEditable(state);
        txt_LDL.setEditable(state);
        txt_LowHDL.setEditable(state);
        txt_GOT.setEditable(state);
        txt_GPT.setEditable(state);
        txt_TG.setEditable(state);
        txt_BUN.setEditable(state);
        txt_UA.setEditable(state);
        txt_Creat.setEditable(state);
        txt_microalbumine.setEditable(state);
        txt_or_urine_protein.setEditable(state);
        txt_urine_routine.setEditable(state);
        txt_urine_culture.setEditable(state);
        txt_Findings.setEditable(state);

        
    }

    private void init() {
        
        try {
            String sql = "SELECT * FROM patients_info WHERE p_no = '" + this.m_Pno + "'";
            // 取出病患基本資料
            ResultSet rs = DBC.executeQuery(sql);
            rs.next();
            this.lab_Pno.setText(rs.getString("p_no"));
            this.lab_Name.setText(rs.getString("firstname") + " " + rs.getString("lastname"));
            this.lab_Gender.setText(rs.getString("gender"));
            this.lab_Age.setText(DateMethod.getAgeWithMonth(rs.getDate("birth")));
            getResult();
        } catch (SQLException ex) {
            Logger.getLogger(Frm_LabDM.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void setResultDateShow(Boolean IsShow) {
        lab_HbA1C_Date.setVisible(IsShow);
        lab_BGAc_Date.setVisible(IsShow);
        lab_BGPc_Date.setVisible(IsShow);
        lab_UAlb_Date.setVisible(IsShow);
        lab_Cholesterol_Date.setVisible(IsShow);
        lab_LDL_Date.setVisible(IsShow);
        lab_LowHDL_Date.setVisible(IsShow);
        lab_GOT_Date.setVisible(IsShow);
        lab_GPT_Date.setVisible(IsShow);
        lab_TG_Date.setVisible(IsShow);
        lab_CrBUN_Date.setVisible(IsShow);
        lab_UA_Date.setVisible(IsShow);
        lab_Creat_Date.setVisible(IsShow);
        lab_microalbumine_Date.setVisible(IsShow);
        lab_orurineprotein_Date.setVisible(IsShow);
        lab_urineroutine_Date.setVisible(IsShow);
        lab_urineculture_Date.setVisible(IsShow);
        lab_Findings_Date.setVisible(IsShow);
    }

    private void getResult() {
        String UpStr = "Last updated: ";
        String[] HbA1C = Tools.getPrescription("HbA1C", m_Pno);
        String[] UAlb = Tools.getPrescription("U / Alb", m_Pno);
        String[] BGAc = Tools.getPrescription("BGAc", m_Pno);
        String[] BGPc = Tools.getPrescription("BGPc", m_Pno);
        String[] CrBUN = Tools.getPrescription("Cr / BUN", m_Pno);
        String[] Cholesterol = Tools.getPrescription("Cholesterol", m_Pno);
        String[] Creat = Tools.getPrescription("Creat", m_Pno);
        String[] GOT = Tools.getPrescription("GOT", m_Pno);
        String[] GPT = Tools.getPrescription("GPT", m_Pno);
        String[] LDL = Tools.getPrescription("LDL", m_Pno);
        String[] LowHDL = Tools.getPrescription("HDL", m_Pno);
        String[] TG = Tools.getPrescription("TG", m_Pno);
        String[] UA = Tools.getPrescription("UA", m_Pno);
        String[] microalbumine = Tools.getPrescription("microalbumine", m_Pno);
        String[] orurineprotein = Tools.getPrescription("or urine protein", m_Pno);
        String[] urineroutine = Tools.getPrescription("urine routine", m_Pno);
        String[] urineculture = Tools.getPrescription("urine culture", m_Pno);
        String[] Findings = Tools.getPrescription("Findings", m_Pno);



        lab_HbA1C_Date.setText(" "+HbA1C[2]);
        lab_BGAc_Date.setText(" "+BGAc[2]);
        lab_BGPc_Date.setText(" "+BGPc[2]);
        lab_UAlb_Date.setText(" "+UAlb[2]);
        lab_Cholesterol_Date.setText(" "+Cholesterol[2]);
        lab_LDL_Date.setText(" "+LDL[2]);
        lab_LowHDL_Date.setText(" "+LowHDL[2]);
        lab_GOT_Date.setText(" "+GOT[2]);
        lab_GPT_Date.setText(" "+GPT[2]);
        lab_TG_Date.setText(" "+TG[2]);
        lab_CrBUN_Date.setText(" "+CrBUN[2]);
        lab_UA_Date.setText(" "+UA[2]);
        lab_Creat_Date.setText(" "+Creat[2]);
        lab_microalbumine_Date.setText(" "+microalbumine[2]);
        lab_orurineprotein_Date.setText(" "+orurineprotein[2]);
        lab_urineroutine_Date.setText(" "+urineroutine[2]);
        lab_urineculture_Date.setText(" "+urineculture[2]);
        lab_Findings_Date.setText( " "+Findings[2]);


        if (Tools.isNumber(HbA1C[0])) this.txt_HbA1C.setText(HbA1C[0]);
        this.txt_HbA1C.setToolTipText(UpStr + HbA1C[2]);

        if (Tools.isNumber(UAlb[0])) this.txt_Alb.setText(UAlb[0]);
        this.txt_Alb.setToolTipText(UpStr + UAlb[2]);


        if (Tools.isNumber(BGAc[0])) this.txt_BGAC.setText(BGAc[0]);
        this.txt_BGAC.setToolTipText(UpStr + BGAc[2]);


        if (Tools.isNumber(BGPc[0])) this.txt_BGPC.setText(BGPc[0]);
        this.txt_BGPC.setToolTipText(UpStr + BGPc[2]);

        if (Tools.isNumber(CrBUN[0])) this.txt_BUN.setText(CrBUN[0]);
        this.txt_BUN.setToolTipText(UpStr + CrBUN[2]);

        if (Tools.isNumber(Cholesterol[0])) this.txt_Cholesterol.setText(Cholesterol[0]);
        this.txt_Cholesterol.setToolTipText(UpStr + Cholesterol[2]);

        if (Tools.isNumber(Creat[0])) this.txt_Creat.setText(Creat[0]);
        this.txt_Creat.setToolTipText(UpStr + Creat[2]);

        if (Tools.isNumber(GOT[0])) this.txt_GOT.setText(GOT[0]);
        this.txt_GOT.setToolTipText(UpStr + GOT[2]);

        this.txt_GPT.setText(GPT[0]);
        this.txt_GPT.setToolTipText(UpStr + GPT[2]);

        if (Tools.isNumber(LDL[0])) this.txt_LDL.setText(LDL[0]);
        this.txt_LDL.setToolTipText(UpStr + LDL[2]);

        if (Tools.isNumber(LowHDL[0])) this.txt_LowHDL.setText(LowHDL[0]);
        this.txt_LowHDL.setToolTipText(UpStr + LowHDL[2]);

        if (Tools.isNumber(TG[0])) this.txt_TG.setText(TG[0]);
        this.txt_TG.setToolTipText(UpStr + TG[2]);

        if (Tools.isNumber(UA[0])) this.txt_UA.setText(UA[0]);
        this.txt_UA.setToolTipText(UpStr + UA[2]);

        if (Tools.isNumber(microalbumine[0])) this.txt_microalbumine.setText(microalbumine[0]);
        this.txt_microalbumine.setToolTipText(UpStr + microalbumine[2]);

        if (Tools.isNumber(orurineprotein[0])) this.txt_or_urine_protein.setText(orurineprotein[0]);
        this.txt_or_urine_protein.setToolTipText(UpStr + orurineprotein[2]);

        if (Tools.isNumber(urineroutine[0])) this.txt_urine_culture.setText(urineroutine[0]);
        this.txt_urine_culture.setToolTipText(UpStr + urineroutine[2]);

        if (Tools.isNumber(urineculture[0])) this.txt_urine_routine.setText(urineculture[0]);
        this.txt_urine_routine.setToolTipText(UpStr + urineculture[2]);

         if (!Findings[1].equals("")) {
            if (Findings[1].equals("0")) cbox_Findings.setSelectedIndex(1);
            else cbox_Findings.setSelectedIndex(2);
        } else cbox_Findings.setSelectedIndex(0);
        if (Findings[2] != null && !Findings[2].equals("")) txt_Findings.setText(Findings[2]);
        this.cbox_Findings.setToolTipText(UpStr + Findings[2]);
        setLimit();
    }


    // DM 各項標準
    private void setLimit() {
        if (!txt_BGAC.getText().equals("") && (Double.parseDouble(txt_BGAC.getText()) < 3.9 || Double.parseDouble(txt_BGAC.getText()) > 10)) {
            txt_BGAC.setBackground(Color.red);
        } else {
            txt_BGAC.setBackground(Color.white);
        }

        if (!txt_HbA1C.getText().equals("") && (Double.parseDouble(txt_HbA1C.getText()) < 6 || Double.parseDouble(txt_HbA1C.getText()) > 9)) {
            txt_HbA1C.setBackground(Color.red);
        } else {
            txt_HbA1C.setBackground(Color.white);
        }

        // 要住院
        String checkstr = "";

        if (!txt_BGPC.getText().equals("") && (Double.parseDouble(txt_BGPC.getText()) < 3.3 || Double.parseDouble(txt_BGPC.getText()) > 22.2)) {
        
            checkstr+= "BGPC = "+Double.parseDouble(txt_BGPC.getText())+" mg/dl \n";
          
            txt_BGPC.setBackground(Color.red);
            
        } else {
            txt_BGPC.setBackground(Color.white);
        }

        if (!txt_HbA1C.getText().equals("") && Double.parseDouble(txt_HbA1C.getText()) > 13) {
            checkstr+= "HbA1C = "+Double.parseDouble(txt_HbA1C.getText())+" % \n";
        }

        if (!checkstr.equals("")) {
                JOptionPane.showMessageDialog(null, "===== WARNING Requiring hospitalization =====\n"+checkstr+"\n");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel12 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txt_Cholesterol = new javax.swing.JTextField();
        jLabel190 = new javax.swing.JLabel();
        txt_Alb = new javax.swing.JTextField();
        jLabel188 = new javax.swing.JLabel();
        jLabel189 = new javax.swing.JLabel();
        jLabel191 = new javax.swing.JLabel();
        jLabel77 = new javax.swing.JLabel();
        txt_BGAC = new javax.swing.JTextField();
        txt_BGPC = new javax.swing.JTextField();
        txt_HbA1C = new javax.swing.JTextField();
        jLabel74 = new javax.swing.JLabel();
        jLabel76 = new javax.swing.JLabel();
        jLabel75 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel194 = new javax.swing.JLabel();
        jLabel196 = new javax.swing.JLabel();
        jLabel195 = new javax.swing.JLabel();
        jLabel198 = new javax.swing.JLabel();
        jLabel197 = new javax.swing.JLabel();
        txt_GPT = new javax.swing.JTextField();
        jLabel199 = new javax.swing.JLabel();
        txt_GOT = new javax.swing.JTextField();
        txt_LowHDL = new javax.swing.JTextField();
        jLabel193 = new javax.swing.JLabel();
        txt_LDL = new javax.swing.JTextField();
        jLabel192 = new javax.swing.JLabel();
        txt_TG = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        lab_HbA1C_Date = new javax.swing.JLabel();
        lab_BGAc_Date = new javax.swing.JLabel();
        lab_BGPc_Date = new javax.swing.JLabel();
        lab_UAlb_Date = new javax.swing.JLabel();
        lab_Cholesterol_Date = new javax.swing.JLabel();
        lab_LDL_Date = new javax.swing.JLabel();
        lab_LowHDL_Date = new javax.swing.JLabel();
        lab_GOT_Date = new javax.swing.JLabel();
        lab_GPT_Date = new javax.swing.JLabel();
        lab_TG_Date = new javax.swing.JLabel();
        txt_Creat = new javax.swing.JTextField();
        txt_UA = new javax.swing.JTextField();
        jLabel206 = new javax.swing.JLabel();
        jLabel205 = new javax.swing.JLabel();
        jLabel203 = new javax.swing.JLabel();
        jLabel202 = new javax.swing.JLabel();
        jLabel200 = new javax.swing.JLabel();
        txt_BUN = new javax.swing.JTextField();
        jLabel201 = new javax.swing.JLabel();
        lab_CrBUN_Date = new javax.swing.JLabel();
        lab_UA_Date = new javax.swing.JLabel();
        lab_Creat_Date = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txt_Findings = new javax.swing.JTextArea();
        jLabel79 = new javax.swing.JLabel();
        jLabel80 = new javax.swing.JLabel();
        cbox_Findings = new javax.swing.JComboBox();
        jLabel78 = new javax.swing.JLabel();
        txt_microalbumine = new javax.swing.JTextField();
        lab_microalbumine_Date = new javax.swing.JLabel();
        txt_urine_routine = new javax.swing.JTextField();
        lab_orurineprotein_Date = new javax.swing.JLabel();
        jLabel84 = new javax.swing.JLabel();
        txt_urine_culture = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txt_or_urine_protein = new javax.swing.JTextField();
        jLabel81 = new javax.swing.JLabel();
        jLabel82 = new javax.swing.JLabel();
        lab_Findings_Date = new javax.swing.JLabel();
        lab_urineculture_Date = new javax.swing.JLabel();
        lab_urineroutine_Date = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel60 = new javax.swing.JLabel();
        lab_Pno = new javax.swing.JLabel();
        jLabel85 = new javax.swing.JLabel();
        lab_Age = new javax.swing.JLabel();
        jLabel88 = new javax.swing.JLabel();
        lab_Gender = new javax.swing.JLabel();
        jLabel90 = new javax.swing.JLabel();
        lab_Name = new javax.swing.JLabel();
        btn_Close = new javax.swing.JButton();
        btn_ShowTime = new javax.swing.JToggleButton();
        mnb = new javax.swing.JMenuBar();
        mn_Fiele = new javax.swing.JMenu();
        mnit_Close = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Lab Recored(For DM)");

        jLabel3.setText("mg/dl");

        jLabel2.setText("Cholesterol：");

        txt_Cholesterol.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_Cholesterol.setText("0");

        jLabel190.setText("U / Alb：");

        txt_Alb.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_Alb.setText("0");

        jLabel188.setText("mg/dl");

        jLabel189.setText("mg/dl");

        jLabel191.setText("G/dl");

        jLabel77.setText("BGPc：");

        txt_BGAC.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_BGAC.setText("0");

        txt_BGPC.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_BGPC.setText("0");

        txt_HbA1C.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_HbA1C.setText("0");

        jLabel74.setText("HbA1C：");

        jLabel76.setText("BGAc：");

        jLabel75.setText("%");

        jLabel4.setText("TG：");

        jLabel194.setText("GOT：");

        jLabel196.setText("LU/L");

        jLabel195.setText("HDL：");

        jLabel198.setText("mg/dl");

        jLabel197.setText("LU/L");

        txt_GPT.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_GPT.setText("0");

        jLabel199.setText("GPT：");

        txt_GOT.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_GOT.setText("0");

        txt_LowHDL.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_LowHDL.setText("0");

        jLabel193.setText("LDL：");

        txt_LDL.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_LDL.setText("0");

        jLabel192.setText("mg/dl");

        txt_TG.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_TG.setText("0");

        jLabel6.setText("mg/dl");

        lab_HbA1C_Date.setForeground(new java.awt.Color(51, 204, 0));
        lab_HbA1C_Date.setText("jLabel1");

        lab_BGAc_Date.setForeground(new java.awt.Color(51, 204, 0));
        lab_BGAc_Date.setText("jLabel5");

        lab_BGPc_Date.setForeground(new java.awt.Color(51, 204, 0));
        lab_BGPc_Date.setText("jLabel7");

        lab_UAlb_Date.setForeground(new java.awt.Color(51, 204, 0));
        lab_UAlb_Date.setText("jLabel9");

        lab_Cholesterol_Date.setForeground(new java.awt.Color(51, 204, 0));
        lab_Cholesterol_Date.setText("jLabel10");

        lab_LDL_Date.setForeground(new java.awt.Color(51, 204, 0));
        lab_LDL_Date.setText("jLabel1");

        lab_LowHDL_Date.setForeground(new java.awt.Color(51, 204, 0));
        lab_LowHDL_Date.setText("jLabel5");

        lab_GOT_Date.setForeground(new java.awt.Color(51, 204, 0));
        lab_GOT_Date.setText("jLabel7");

        lab_GPT_Date.setForeground(new java.awt.Color(51, 204, 0));
        lab_GPT_Date.setText("jLabel9");

        lab_TG_Date.setForeground(new java.awt.Color(51, 204, 0));
        lab_TG_Date.setText("jLabel10");

        txt_Creat.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_Creat.setText("0");

        txt_UA.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_UA.setText("0");

        jLabel206.setText("mg/dl");

        jLabel205.setText("mg/dl");

        jLabel203.setText("UA：");

        jLabel202.setText("Creat：");

        jLabel200.setText("mg/dl");

        txt_BUN.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_BUN.setText("0");

        jLabel201.setText("Cr / BUN：");

        lab_CrBUN_Date.setForeground(new java.awt.Color(51, 204, 0));
        lab_CrBUN_Date.setText("jLabel1");

        lab_UA_Date.setForeground(new java.awt.Color(51, 204, 0));
        lab_UA_Date.setText("jLabel5");

        lab_Creat_Date.setForeground(new java.awt.Color(51, 204, 0));
        lab_Creat_Date.setText("jLabel7");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel77, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel190, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel74, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel76, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt_Cholesterol, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txt_Alb)
                    .addComponent(txt_BGPC)
                    .addComponent(txt_BGAC)
                    .addComponent(txt_HbA1C, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel189)
                    .addComponent(jLabel188)
                    .addComponent(jLabel191)
                    .addComponent(jLabel75))
                .addGap(3, 3, 3)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lab_HbA1C_Date, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(24, 24, 24))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lab_BGPc_Date, javax.swing.GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE)
                        .addGap(6, 6, 6))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lab_BGAc_Date, javax.swing.GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE)
                        .addGap(6, 6, 6))
                    .addComponent(lab_Cholesterol_Date, javax.swing.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lab_UAlb_Date, javax.swing.GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE)
                        .addGap(6, 6, 6)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel194, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel195, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel199, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel193, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txt_GOT)
                        .addComponent(txt_LowHDL)
                        .addComponent(txt_LDL, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                        .addComponent(txt_GPT, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txt_TG, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel192)
                    .addComponent(jLabel198)
                    .addComponent(jLabel197)
                    .addComponent(jLabel196)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lab_LDL_Date, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(6, 6, 6))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lab_LowHDL_Date, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(6, 6, 6))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lab_GOT_Date, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(6, 6, 6))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lab_GPT_Date, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(6, 6, 6))
                    .addComponent(lab_TG_Date, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel203, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel202, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel201, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt_Creat)
                    .addComponent(txt_UA)
                    .addComponent(txt_BUN, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel200)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lab_CrBUN_Date, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel206)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lab_UA_Date, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel205)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lab_Creat_Date, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_BUN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel200)
                            .addComponent(jLabel201)
                            .addComponent(lab_CrBUN_Date))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_UA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel206)
                            .addComponent(jLabel203)
                            .addComponent(lab_UA_Date))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_Creat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel205)
                            .addComponent(jLabel202)
                            .addComponent(lab_Creat_Date)))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txt_LDL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel193)
                                .addComponent(jLabel192)
                                .addComponent(lab_LDL_Date))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txt_LowHDL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel195)
                                .addComponent(jLabel198)
                                .addComponent(lab_LowHDL_Date))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txt_GOT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel197)
                                .addComponent(jLabel194)
                                .addComponent(lab_GOT_Date))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txt_GPT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel199)
                                .addComponent(jLabel196)
                                .addComponent(lab_GPT_Date))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel4)
                                .addComponent(txt_TG, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel6)
                                .addComponent(lab_TG_Date)))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txt_HbA1C, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel74)
                                .addComponent(jLabel75)
                                .addComponent(lab_HbA1C_Date))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txt_BGAC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel188)
                                .addComponent(jLabel76)
                                .addComponent(lab_BGAc_Date))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txt_BGPC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel189)
                                .addComponent(jLabel77)
                                .addComponent(lab_BGPc_Date))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txt_Alb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel191)
                                .addComponent(jLabel190)
                                .addComponent(lab_UAlb_Date))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txt_Cholesterol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel3)
                                .addComponent(jLabel2)
                                .addComponent(lab_Cholesterol_Date)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        txt_Findings.setColumns(5);
        txt_Findings.setRows(4);
        jScrollPane2.setViewportView(txt_Findings);

        jLabel79.setText("mg/mg of or (morning spot)");

        jLabel80.setText("or urine protein：");

        cbox_Findings.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "　", "Normal", "Abnormal" }));
        cbox_Findings.setEnabled(false);

        jLabel78.setText("microalbumine：");

        txt_microalbumine.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        lab_microalbumine_Date.setForeground(new java.awt.Color(51, 204, 0));
        lab_microalbumine_Date.setText("jLabel1");

        txt_urine_routine.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        lab_orurineprotein_Date.setForeground(new java.awt.Color(51, 204, 0));
        lab_orurineprotein_Date.setText("jLabel5");

        jLabel84.setText("urine culture：");

        txt_urine_culture.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel8.setText("Findings：");

        txt_or_urine_protein.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel81.setText("mmol/dl");

        jLabel82.setText("urine routine：");

        lab_Findings_Date.setForeground(new java.awt.Color(51, 204, 0));
        lab_Findings_Date.setText("jLabel1");

        lab_urineculture_Date.setForeground(new java.awt.Color(51, 204, 0));
        lab_urineculture_Date.setText("jLabel9");

        lab_urineroutine_Date.setForeground(new java.awt.Color(51, 204, 0));
        lab_urineroutine_Date.setText("jLabel7");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel84, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel80, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel78, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel82, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 383, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_urine_culture, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_microalbumine, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_or_urine_protein, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(cbox_Findings, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(lab_Findings_Date, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel79)
                                    .addComponent(jLabel81)))
                            .addComponent(txt_urine_routine, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lab_urineculture_Date, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                            .addComponent(lab_urineroutine_Date, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                            .addComponent(lab_orurineprotein_Date, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                            .addComponent(lab_microalbumine_Date, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE))
                        .addGap(13, 13, 13)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_microalbumine, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel78)
                    .addComponent(lab_microalbumine_Date)
                    .addComponent(jLabel79))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel80)
                    .addComponent(txt_or_urine_protein, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_orurineprotein_Date)
                    .addComponent(jLabel81))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel82)
                    .addComponent(txt_urine_routine, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_urineroutine_Date))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel84)
                    .addComponent(txt_urine_culture, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_urineculture_Date))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(cbox_Findings, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lab_Findings_Date))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(40, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(155, 155, 155))
        );

        jScrollPane1.setViewportView(jPanel12);

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder("Demographic data"));

        jLabel60.setText("Patient No：");

        lab_Pno.setText("0001");

        jLabel85.setText("Age：");

        lab_Age.setText("30");

        jLabel88.setText("Gender：");

        lab_Gender.setText("M");

        jLabel90.setText("Name：");

        lab_Name.setText("Steven Chung");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel60)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lab_Pno, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel90)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lab_Name, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel85)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lab_Age, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                .addGap(41, 41, 41)
                .addComponent(jLabel88)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lab_Gender, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
                .addGap(170, 170, 170))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel60)
                .addComponent(lab_Pno)
                .addComponent(jLabel90))
            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(lab_Name)
                .addComponent(jLabel85)
                .addComponent(lab_Age)
                .addComponent(lab_Gender)
                .addComponent(jLabel88))
        );

        btn_Close.setText("Close");
        btn_Close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CloseActionPerformed(evt);
            }
        });

        btn_ShowTime.setText("Show Last Updated");
        btn_ShowTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ShowTimeActionPerformed(evt);
            }
        });

        mn_Fiele.setText("File");
        mn_Fiele.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mn_FieleActionPerformed(evt);
            }
        });

        mnit_Close.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0));
        mnit_Close.setText("Close");
        mnit_Close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnit_CloseActionPerformed(evt);
            }
        });
        mn_Fiele.add(mnit_Close);

        mnb.add(mn_Fiele);

        setJMenuBar(mnb);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 780, Short.MAX_VALUE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btn_ShowTime)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_Close)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 483, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_Close)
                    .addComponent(btn_ShowTime))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mnit_CloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnit_CloseActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_mnit_CloseActionPerformed

    private void btn_CloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CloseActionPerformed
        mnit_CloseActionPerformed(null);
    }//GEN-LAST:event_btn_CloseActionPerformed

    private void mn_FieleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mn_FieleActionPerformed

    }//GEN-LAST:event_mn_FieleActionPerformed

    private void btn_ShowTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ShowTimeActionPerformed
        if (!btn_ShowTime.isSelected()) setResultDateShow(false);
        else setResultDateShow(true);
    }//GEN-LAST:event_btn_ShowTimeActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Close;
    private javax.swing.JToggleButton btn_ShowTime;
    private javax.swing.JComboBox cbox_Findings;
    private javax.swing.JLabel jLabel188;
    private javax.swing.JLabel jLabel189;
    private javax.swing.JLabel jLabel190;
    private javax.swing.JLabel jLabel191;
    private javax.swing.JLabel jLabel192;
    private javax.swing.JLabel jLabel193;
    private javax.swing.JLabel jLabel194;
    private javax.swing.JLabel jLabel195;
    private javax.swing.JLabel jLabel196;
    private javax.swing.JLabel jLabel197;
    private javax.swing.JLabel jLabel198;
    private javax.swing.JLabel jLabel199;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel200;
    private javax.swing.JLabel jLabel201;
    private javax.swing.JLabel jLabel202;
    private javax.swing.JLabel jLabel203;
    private javax.swing.JLabel jLabel205;
    private javax.swing.JLabel jLabel206;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lab_Age;
    private javax.swing.JLabel lab_BGAc_Date;
    private javax.swing.JLabel lab_BGPc_Date;
    private javax.swing.JLabel lab_Cholesterol_Date;
    private javax.swing.JLabel lab_CrBUN_Date;
    private javax.swing.JLabel lab_Creat_Date;
    private javax.swing.JLabel lab_Findings_Date;
    private javax.swing.JLabel lab_GOT_Date;
    private javax.swing.JLabel lab_GPT_Date;
    private javax.swing.JLabel lab_Gender;
    private javax.swing.JLabel lab_HbA1C_Date;
    private javax.swing.JLabel lab_LDL_Date;
    private javax.swing.JLabel lab_LowHDL_Date;
    private javax.swing.JLabel lab_Name;
    private javax.swing.JLabel lab_Pno;
    private javax.swing.JLabel lab_TG_Date;
    private javax.swing.JLabel lab_UA_Date;
    private javax.swing.JLabel lab_UAlb_Date;
    private javax.swing.JLabel lab_microalbumine_Date;
    private javax.swing.JLabel lab_orurineprotein_Date;
    private javax.swing.JLabel lab_urineculture_Date;
    private javax.swing.JLabel lab_urineroutine_Date;
    private javax.swing.JMenu mn_Fiele;
    private javax.swing.JMenuBar mnb;
    private javax.swing.JMenuItem mnit_Close;
    private javax.swing.JTextField txt_Alb;
    private javax.swing.JTextField txt_BGAC;
    private javax.swing.JTextField txt_BGPC;
    private javax.swing.JTextField txt_BUN;
    private javax.swing.JTextField txt_Cholesterol;
    private javax.swing.JTextField txt_Creat;
    private javax.swing.JTextArea txt_Findings;
    private javax.swing.JTextField txt_GOT;
    private javax.swing.JTextField txt_GPT;
    private javax.swing.JTextField txt_HbA1C;
    private javax.swing.JTextField txt_LDL;
    private javax.swing.JTextField txt_LowHDL;
    private javax.swing.JTextField txt_TG;
    private javax.swing.JTextField txt_UA;
    private javax.swing.JTextField txt_microalbumine;
    private javax.swing.JTextField txt_or_urine_protein;
    private javax.swing.JTextField txt_urine_culture;
    private javax.swing.JTextField txt_urine_routine;
    // End of variables declaration//GEN-END:variables

}