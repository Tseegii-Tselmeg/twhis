package Main;

import Cashier.Frm_CashierList;
import ErrorMessage.StoredErrorMessage;
import cc.johnwu.loading.Frm_Loading;
import cc.johnwu.login.UserInfo;
import Multilingual.language;
import MobileHealth.Frm_MobileHealth;
import Statistic.Frm_Statistic;
import System.Frm_Setting;
import Test.Frm_PoliManage;
import Test.Frm_Test;
import cc.johnwu.login.Frm_Login;
import cc.johnwu.login.SysInfo;
import cc.johnwu.sql.DBC;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;


public class Frm_Main extends javax.swing.JFrame {
    /*輸出錯誤資訊變數*/
    StoredErrorMessage ErrorMessage = new StoredErrorMessage() ;

    public Frm_Main() {
        initComponents();
        //---------迦納-------------------------
        //pan_Case.setVisible(false);
        // ------------------------------------
        addWindowListener(new WindowAdapter() {  // 畫面關閉原視窗enable
        @Override
        public void windowClosing(WindowEvent windowevent) {
            mnit_ExitActionPerformed(null);
        }});

        try{
            initPermission();
        }catch(Exception e){
            ErrorMessage.setData("Main", "Frm_Main" , "initPermission()", e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length()));
        }
        this.setLocationRelativeTo(this);
   
        btn_MedicineStock.setVisible(false);
        btn_DepartmentManagement.setVisible(false);
        btn_PositionManagement.setVisible(false);
        language paragraph = new language();
        String[] line = paragraph.setlanguage("MAIN").split("\n") ;
        for(int j = 1 ; j<line.length ; j++){
            String[] col = line[j].split("=");
            if(col[0].equals("MEDICINE_STOCK")) this.btn_MedicineStock.setText(col[1]);
            if(col[0].equals("PHARMACY")) this.btn_Pharmacy.setText(col[1]);
            if(col[0].equals("POSITION_MANAGEMENT")) this.btn_PositionManagement.setText(col[1]);
            if(col[0].equals("DEPARTMENT_MANAGEMENT")) this.btn_DepartmentManagement.setText(col[1]);
            if(col[0].equals("SHIFT_MANAGEMENT")) this.btn_ShiftManagement.setText(col[1]);
            if(col[0].equals("STAFF_MANAGEMENT")) this.btn_StaffManagement.setText(col[1]);
           // if(col[0].equals("REVIEW")) this.btn_AnamnesisReturn.setText(col[1]);
            if(col[0].equals("MEDICAL HISTORYT")) this.btn_Anamnesis.setText(col[1]);
            if(col[0].equals("PATIENT_INFORMATION")) this.btn_Patients.setText(col[1]);
            if(col[0].equals("DIAGNOSIS")) this.btn_Diagnosis.setText(col[1]);
            if(col[0].equals("REGISTRATION")) this.btn_Register.setText(col[1]);
            if(col[0].equals("SHIFT_MANAGEMENT")) this.btn_ShiftManagement.setText(col[1]);

            if(col[0].equals("DOCTOR")) this.pan_Doctor.setBorder(javax.swing.BorderFactory.createTitledBorder(col[1]));
            if(col[0].equals("PERSONAL")) this.pan_PersonalManagement.setBorder(javax.swing.BorderFactory.createTitledBorder(col[1]));
            if(col[0].equals("PHARMACY")) this.pan_Pharmacy.setBorder(javax.swing.BorderFactory.createTitledBorder(col[1]));
            if(col[0].equals("MANAGEMENT")) this.pan_SystemManagement.setBorder(javax.swing.BorderFactory.createTitledBorder(col[1]));
            if(col[0].equals("LABORATORY")) this.btn_Laboratory.setText(col[1]);
            if(col[0].equals("INVESTGATIONS")) this.pan_Investgations.setBorder(javax.swing.BorderFactory.createTitledBorder(col[1]));
            if(col[0].equals("REGISTRATION")) this.pan_Registration.setBorder(javax.swing.BorderFactory.createTitledBorder(col[1]));
            if(col[0].equals("PERMISSION")) this.btn_Premission.setText(col[1]);
            if(col[0].equals("MEDICAL HISTORYT")) this.pan_Anamnesis.setBorder(javax.swing.BorderFactory.createTitledBorder(col[1]));
            //if(col[0].equals("RADIOLOGY(X-RAY)")) this.btn_Radiology.setText(col[1]);
            if(col[0].equals("MAIN")) this.setTitle(col[1]);
        }
    
    }
    
    public void initPermission(){
        btn_Register.setEnabled(UserInfo.getSelectPow("Registration"));
        btn_Diagnosis.setEnabled(UserInfo.getSelectPow("Diagnosis"));
        btn_Patients.setEnabled(UserInfo.getSelectPow("Patient Information"));
        btn_Anamnesis.setEnabled(UserInfo.getSelectPow("Medical History"));
        btn_AnamnesisReturn.setEnabled(UserInfo.getSelectPow("Review"));
        btn_StaffManagement.setEnabled(UserInfo.getSelectPow("Staff Management"));
        btn_ShiftManagement.setEnabled(UserInfo.getSelectPow("Shift Management"));
        btn_DepartmentManagement.setEnabled(UserInfo.getSelectPow("Department Management"));
        btn_PositionManagement.setEnabled(UserInfo.getSelectPow("Position Management"));
        btn_Pharmacy.setEnabled(UserInfo.getSelectPow("Pharmacy"));
        btn_MedicineStock.setEnabled(UserInfo.getSelectPow("Medicine Stock"));
        btn_Premission.setEnabled(UserInfo.getSelectPow("Permission"));
        btn_Laboratory.setEnabled(UserInfo.getSelectPow("Laboratory"));
        btn_Radiology.setEnabled(UserInfo.getSelectPow("Radiology"));
        btn_System.setEnabled(UserInfo.getSelectPow("System"));
        btn_Statistic.setEnabled(UserInfo.getSelectPow("Statistic"));
        btn_Sms.setEnabled(UserInfo.getSelectPow("Mobile Health"));
        btn_Case.setEnabled(UserInfo.getSelectPow("Case Management"));
        btn_Cashier.setEnabled(UserInfo.getSelectPow("Cashier"));

    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pan_PersonalManagement = new javax.swing.JPanel();
        btn_StaffManagement = new javax.swing.JButton();
        btn_ShiftManagement = new javax.swing.JButton();
        pan_Registration = new javax.swing.JPanel();
        btn_Register = new javax.swing.JButton();
        pan_Anamnesis = new javax.swing.JPanel();
        btn_AnamnesisReturn = new javax.swing.JButton();
        btn_Anamnesis = new javax.swing.JButton();
        pan_Doctor = new javax.swing.JPanel();
        btn_Diagnosis = new javax.swing.JButton();
        btn_Patients = new javax.swing.JButton();
        pan_Pharmacy = new javax.swing.JPanel();
        btn_Pharmacy = new javax.swing.JButton();
        pan_SystemManagement = new javax.swing.JPanel();
        btn_Premission = new javax.swing.JButton();
        btn_System = new javax.swing.JButton();
        btn_Statistic = new javax.swing.JButton();
        btn_Cashier = new javax.swing.JButton();
        pan_Investgations = new javax.swing.JPanel();
        btn_Laboratory = new javax.swing.JButton();
        btn_Radiology = new javax.swing.JButton();
        btn_PositionManagement = new javax.swing.JButton();
        btn_DepartmentManagement = new javax.swing.JButton();
        btn_MedicineStock = new javax.swing.JButton();
        pan_Case = new javax.swing.JPanel();
        btn_Case = new javax.swing.JButton();
        btn_Sms = new javax.swing.JButton();
        mbar = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        mnit_Logout = new javax.swing.JMenuItem();
        mnit_Exit = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Main");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setMinimumSize(new java.awt.Dimension(800, 600));
        setResizable(false);

        pan_PersonalManagement.setBackground(new java.awt.Color(240, 246, 255));
        pan_PersonalManagement.setBorder(javax.swing.BorderFactory.createTitledBorder("Personel Management "));

        btn_StaffManagement.setText("Staff Management");
        btn_StaffManagement.setPreferredSize(new java.awt.Dimension(75, 29));
        btn_StaffManagement.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_StaffManagementActionPerformed(evt);
            }
        });

        btn_ShiftManagement.setText("Shift Management");
        btn_ShiftManagement.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ShiftManagementActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pan_PersonalManagementLayout = new javax.swing.GroupLayout(pan_PersonalManagement);
        pan_PersonalManagement.setLayout(pan_PersonalManagementLayout);
        pan_PersonalManagementLayout.setHorizontalGroup(
            pan_PersonalManagementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_PersonalManagementLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan_PersonalManagementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_StaffManagement, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_ShiftManagement, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pan_PersonalManagementLayout.setVerticalGroup(
            pan_PersonalManagementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_PersonalManagementLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_StaffManagement, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_ShiftManagement, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        pan_Registration.setBackground(new java.awt.Color(240, 246, 255));
        pan_Registration.setBorder(javax.swing.BorderFactory.createTitledBorder("Registration"));

        btn_Register.setText("Registration");
        btn_Register.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_RegisterActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pan_RegistrationLayout = new javax.swing.GroupLayout(pan_Registration);
        pan_Registration.setLayout(pan_RegistrationLayout);
        pan_RegistrationLayout.setHorizontalGroup(
            pan_RegistrationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan_RegistrationLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_Register, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pan_RegistrationLayout.setVerticalGroup(
            pan_RegistrationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_RegistrationLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_Register, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pan_Anamnesis.setBackground(new java.awt.Color(240, 246, 255));
        pan_Anamnesis.setBorder(javax.swing.BorderFactory.createTitledBorder("Medical History"));

        btn_AnamnesisReturn.setText("Forder Return");
        btn_AnamnesisReturn.setPreferredSize(new java.awt.Dimension(200, 60));
        btn_AnamnesisReturn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_AnamnesisReturnActionPerformed(evt);
            }
        });

        btn_Anamnesis.setText("Medical History");
        btn_Anamnesis.setPreferredSize(new java.awt.Dimension(200, 60));
        btn_Anamnesis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_AnamnesisActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pan_AnamnesisLayout = new javax.swing.GroupLayout(pan_Anamnesis);
        pan_Anamnesis.setLayout(pan_AnamnesisLayout);
        pan_AnamnesisLayout.setHorizontalGroup(
            pan_AnamnesisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan_AnamnesisLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pan_AnamnesisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btn_Anamnesis, javax.swing.GroupLayout.Alignment.TRAILING, 0, 0, Short.MAX_VALUE)
                    .addComponent(btn_AnamnesisReturn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE))
                .addContainerGap())
        );
        pan_AnamnesisLayout.setVerticalGroup(
            pan_AnamnesisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_AnamnesisLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_Anamnesis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_AnamnesisReturn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pan_Doctor.setBackground(new java.awt.Color(240, 246, 255));
        pan_Doctor.setBorder(javax.swing.BorderFactory.createTitledBorder("Clinic"));

        btn_Diagnosis.setText("Diagnosis");
        btn_Diagnosis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_DiagnosisActionPerformed(evt);
            }
        });

        btn_Patients.setText("Patient Information");
        btn_Patients.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_PatientsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pan_DoctorLayout = new javax.swing.GroupLayout(pan_Doctor);
        pan_Doctor.setLayout(pan_DoctorLayout);
        pan_DoctorLayout.setHorizontalGroup(
            pan_DoctorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan_DoctorLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pan_DoctorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btn_Diagnosis, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_Patients, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE))
                .addContainerGap())
        );
        pan_DoctorLayout.setVerticalGroup(
            pan_DoctorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_DoctorLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_Diagnosis, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Patients, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pan_Pharmacy.setBackground(new java.awt.Color(240, 246, 255));
        pan_Pharmacy.setBorder(javax.swing.BorderFactory.createTitledBorder("Pharmacy"));

        btn_Pharmacy.setText("Pharmacy");
        btn_Pharmacy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_PharmacyActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pan_PharmacyLayout = new javax.swing.GroupLayout(pan_Pharmacy);
        pan_Pharmacy.setLayout(pan_PharmacyLayout);
        pan_PharmacyLayout.setHorizontalGroup(
            pan_PharmacyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_PharmacyLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_Pharmacy, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pan_PharmacyLayout.setVerticalGroup(
            pan_PharmacyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pan_PharmacyLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_Pharmacy, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pan_SystemManagement.setBackground(new java.awt.Color(240, 246, 255));
        pan_SystemManagement.setBorder(javax.swing.BorderFactory.createTitledBorder("System Management"));

        btn_Premission.setText("Permission");
        btn_Premission.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_PremissionActionPerformed(evt);
            }
        });

        btn_System.setText("System");
        btn_System.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_SystemActionPerformed(evt);
            }
        });

        btn_Statistic.setText("Statistic");
        btn_Statistic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_StatisticActionPerformed(evt);
            }
        });

        btn_Cashier.setText("Cashier");
        btn_Cashier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CashierActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pan_SystemManagementLayout = new javax.swing.GroupLayout(pan_SystemManagement);
        pan_SystemManagement.setLayout(pan_SystemManagementLayout);
        pan_SystemManagementLayout.setHorizontalGroup(
            pan_SystemManagementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_SystemManagementLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan_SystemManagementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_Cashier, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pan_SystemManagementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(btn_Premission, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                        .addComponent(btn_System, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                        .addComponent(btn_Statistic, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pan_SystemManagementLayout.setVerticalGroup(
            pan_SystemManagementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_SystemManagementLayout.createSequentialGroup()
                .addComponent(btn_Cashier, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Premission, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_System, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Statistic, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pan_Investgations.setBackground(new java.awt.Color(240, 246, 255));
        pan_Investgations.setBorder(javax.swing.BorderFactory.createTitledBorder("Investgations"));

        btn_Laboratory.setText("Laboratory");
        btn_Laboratory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_LaboratoryActionPerformed(evt);
            }
        });

        btn_Radiology.setText("Radiology(X-RAY)");
        btn_Radiology.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_RadiologyActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pan_InvestgationsLayout = new javax.swing.GroupLayout(pan_Investgations);
        pan_Investgations.setLayout(pan_InvestgationsLayout);
        pan_InvestgationsLayout.setHorizontalGroup(
            pan_InvestgationsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_InvestgationsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan_InvestgationsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_Laboratory, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_Radiology, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(8, Short.MAX_VALUE))
        );
        pan_InvestgationsLayout.setVerticalGroup(
            pan_InvestgationsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_InvestgationsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_Laboratory, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Radiology, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btn_PositionManagement.setText("Position Management");
        btn_PositionManagement.setPreferredSize(new java.awt.Dimension(200, 60));
        btn_PositionManagement.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_PositionManagementActionPerformed(evt);
            }
        });

        btn_DepartmentManagement.setText("Polinlic Management");
        btn_DepartmentManagement.setPreferredSize(new java.awt.Dimension(200, 60));
        btn_DepartmentManagement.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_DepartmentManagementActionPerformed(evt);
            }
        });

        btn_MedicineStock.setText("Medicine Stock");
        btn_MedicineStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_MedicineStockActionPerformed(evt);
            }
        });

        pan_Case.setBackground(new java.awt.Color(240, 246, 255));
        pan_Case.setBorder(javax.swing.BorderFactory.createTitledBorder("Case Management"));

        btn_Case.setText("Case Management");
        btn_Case.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CaseActionPerformed(evt);
            }
        });

        btn_Sms.setText("Mobile Health");
        btn_Sms.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_SmsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pan_CaseLayout = new javax.swing.GroupLayout(pan_Case);
        pan_Case.setLayout(pan_CaseLayout);
        pan_CaseLayout.setHorizontalGroup(
            pan_CaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_CaseLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan_CaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_Case, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                    .addComponent(btn_Sms, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))
                .addContainerGap())
        );
        pan_CaseLayout.setVerticalGroup(
            pan_CaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_CaseLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_Case, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Sms, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jMenu1.setText("File");

        mnit_Logout.setText("Logout");
        mnit_Logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnit_LogoutActionPerformed(evt);
            }
        });
        jMenu1.add(mnit_Logout);

        mnit_Exit.setText("Exit");
        mnit_Exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnit_ExitActionPerformed(evt);
            }
        });
        jMenu1.add(mnit_Exit);

        mbar.add(jMenu1);

        setJMenuBar(mbar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pan_Registration, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pan_Doctor, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pan_Anamnesis, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pan_Pharmacy, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pan_Investgations, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(btn_DepartmentManagement, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pan_Case, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pan_SystemManagement, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pan_PersonalManagement, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(btn_MedicineStock, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_PositionManagement, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(61, 61, 61))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(pan_Pharmacy, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pan_Registration, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pan_Doctor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pan_Case, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pan_Anamnesis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pan_Investgations, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_DepartmentManagement, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_MedicineStock, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_PositionManagement, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pan_PersonalManagement, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pan_SystemManagement, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_RegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_RegisterActionPerformed
        //開啟掛號視窗
        new Registration.Frm_Registration().setVisible(true);
        //關閉此視窗
        this.dispose();
}//GEN-LAST:event_btn_RegisterActionPerformed

    private void btn_DiagnosisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_DiagnosisActionPerformed
        //自動更新diagnosis_code資料
        Frm_Loading frm_Loading = new cc.johnwu.loading.Frm_Loading("diagnosis_code");
        frm_Loading.show_Loading();
        Frm_Loading frm_Loading1 = new cc.johnwu.loading.Frm_Loading("prescription_code");
        frm_Loading1.show_Loading();
        Frm_Loading frm_Loading2 = new cc.johnwu.loading.Frm_Loading("medicines");
        frm_Loading2.show_Loading();
        //開啟看診 視窗
        new WorkList.Frm_WorkList(0,"dia").setVisible(true);
        //關閉此視窗
        this.dispose();
    }//GEN-LAST:event_btn_DiagnosisActionPerformed

    private void btn_PharmacyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_PharmacyActionPerformed
        new Pharmacy.Frm_Pharmacy().setVisible(true);
        //關閉此視窗
        this.dispose();
    }//GEN-LAST:event_btn_PharmacyActionPerformed

    private void btn_PremissionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_PremissionActionPerformed
        new Permission.Frm_Permission_Info().setVisible(true);
        this.dispose();
}//GEN-LAST:event_btn_PremissionActionPerformed

    private void btn_PatientsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_PatientsActionPerformed
        new Patients.Frm_PatientsList().setVisible(true);
        this.dispose();
}//GEN-LAST:event_btn_PatientsActionPerformed

    private void btn_StaffManagementActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_StaffManagementActionPerformed
        new Staff.Frm_StaffInfo().setVisible(true);
        this.dispose();
}//GEN-LAST:event_btn_StaffManagementActionPerformed

    private void btn_ShiftManagementActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ShiftManagementActionPerformed
        new ShiftWork.Frm_ShiftWorkInfo().setVisible(true);
        this.dispose();
}//GEN-LAST:event_btn_ShiftManagementActionPerformed

    private void btn_DepartmentManagementActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_DepartmentManagementActionPerformed
        new Staff.Frm_Department().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btn_DepartmentManagementActionPerformed

    private void btn_PositionManagementActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_PositionManagementActionPerformed
        new Staff.Frm_Position().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btn_PositionManagementActionPerformed

    private void btn_MedicineStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_MedicineStockActionPerformed
        new MedicineStock.Frm_MidicineStockInfo().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btn_MedicineStockActionPerformed

    private void btn_AnamnesisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_AnamnesisActionPerformed
        new Anamnesis.Frm_Anamnesis(true).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btn_AnamnesisActionPerformed

    private void btn_AnamnesisReturnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_AnamnesisReturnActionPerformed
        new Anamnesis.Frm_Anamnesis(false).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btn_AnamnesisReturnActionPerformed

    private void btn_CaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CaseActionPerformed
        new WorkList.Frm_WorkList(0,"case").setVisible(true);
        this.dispose();
}//GEN-LAST:event_btn_CaseActionPerformed

    private void btn_RadiologyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_RadiologyActionPerformed
       new WorkList.Frm_WorkList(0,"xray").setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btn_RadiologyActionPerformed

    private void btn_LaboratoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_LaboratoryActionPerformed
       new WorkList.Frm_WorkList(0,"lab").setVisible(true);
        this.dispose();
}//GEN-LAST:event_btn_LaboratoryActionPerformed

    private void btn_SystemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SystemActionPerformed
        new Frm_Setting().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btn_SystemActionPerformed

    private void btn_SmsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SmsActionPerformed
        new Frm_MobileHealth().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btn_SmsActionPerformed

    private void btn_StatisticActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_StatisticActionPerformed
        new Frm_Statistic().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btn_StatisticActionPerformed

    private void btn_CashierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CashierActionPerformed
       new Frm_CashierList().setVisible(true);
       this.dispose();
    }//GEN-LAST:event_btn_CashierActionPerformed

    private void mnit_ExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnit_ExitActionPerformed
        cc.johnwu.login.OnlineState.LogOut();
        System.exit(0);
    }//GEN-LAST:event_mnit_ExitActionPerformed

    private void mnit_LogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnit_LogoutActionPerformed
        Frm_Login m_frm_Login;

        Object[] options = {"YES","NO"};
            int response = JOptionPane.showOptionDialog(
                            new Frame(),
                            "System Logout?",
                            "Wairning",
                            JOptionPane.YES_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options,
                            options[0]
                        );
            if(response==0){
               try {
                    //cc.johnwu.login.OnlineState.LogOut();
                    // this.dispose();
//                    new UserInfo m_UserInfo m_UserInfo.set

                           // new UserInfo(false).
                   new UserInfo(false).openFrame(new Frm_Main());
//
                } catch (InterruptedException ex) {
                    Logger.getLogger(Frm_Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
    }//GEN-LAST:event_mnit_LogoutActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Anamnesis;
    private javax.swing.JButton btn_AnamnesisReturn;
    private javax.swing.JButton btn_Case;
    private javax.swing.JButton btn_Cashier;
    private javax.swing.JButton btn_DepartmentManagement;
    private javax.swing.JButton btn_Diagnosis;
    private javax.swing.JButton btn_Laboratory;
    private javax.swing.JButton btn_MedicineStock;
    private javax.swing.JButton btn_Patients;
    private javax.swing.JButton btn_Pharmacy;
    private javax.swing.JButton btn_PositionManagement;
    private javax.swing.JButton btn_Premission;
    private javax.swing.JButton btn_Radiology;
    private javax.swing.JButton btn_Register;
    private javax.swing.JButton btn_ShiftManagement;
    private javax.swing.JButton btn_Sms;
    private javax.swing.JButton btn_StaffManagement;
    private javax.swing.JButton btn_Statistic;
    private javax.swing.JButton btn_System;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar mbar;
    private javax.swing.JMenuItem mnit_Exit;
    private javax.swing.JMenuItem mnit_Logout;
    private javax.swing.JPanel pan_Anamnesis;
    private javax.swing.JPanel pan_Case;
    private javax.swing.JPanel pan_Doctor;
    private javax.swing.JPanel pan_Investgations;
    private javax.swing.JPanel pan_PersonalManagement;
    private javax.swing.JPanel pan_Pharmacy;
    private javax.swing.JPanel pan_Registration;
    private javax.swing.JPanel pan_SystemManagement;
    // End of variables declaration//GEN-END:variables

  
}
