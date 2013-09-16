package System;

import cc.johnwu.sql.DBC;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HL7output {
	boolean x = false;
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMddkkmmss");
	private Connection conn;

	public String url = "jdbc:mysql://163.17.20.102/hospital?useUnicode=true&characterEncoding=Big5";

	public String user = "his";

	public String password = "his";

	public String patient_no = "";
        private String obx="";
	private String dateString;

	private String pr1 = "";

	private String[] MSH = new String[22];

	private String[] RF1 = new String[12];

	private String[] PRD = new String[10];

	private String[] PID = new String[40];

	private String[] DG1 = new String[20];

	private String[] DRG = new String[12];

	private String[] PR1 = new String[19];

	public String[] OBR = new String[48];

	private String[] NTE = new String[5];

	private String[] OBX = new String[20];

	

	private String arg(String args[]) {
		String m = "";

		if (args[0].equals("RF1") && args[6] == null)
			return "";
		if (args[0].equals("OBR") && (
                        args[4] == null)) {
			x = true;
			return "";
		}else if(args[0].equals("OBR")&& args[3].equals("")){ args[4]= getNewPassword()+"-"+getNewPassword()+"-"+getNewPassword();System.out.print(args[4]); }
                
                if(args[0].equals("DRG")){return "";}
		if (args[0].equals("NTE") && ( args[3] == null)) {
			return "";
		}
		if (args[0].equals("OBX") && ( x|| (args[3]==null || args[11]==null)))
			return "";
		for (int i = 0; i < args.length; i++) {
			if (args[i] == null)
				args[i] = "";
			m = m + args[i];
			if (i != args.length - 1) {
				if (args[0].equals("MSH") && i == 0) {
					continue;
				}
				m += "|";
			}
		}
		m += "\n";
		return m;
	}

	//
	public String getNewPassword() {
		String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		int size = str.length();
		StringBuffer sb = new StringBuffer();
		Random rad = new Random();

		for (int i = 1; i < 8; i++) {
			int start = rad.nextInt(size);
			String tmp = str.substring(start, start + 1);
			sb.append(tmp);
		}
		String password = sb.toString();
		return password;
	}

	private void outputhl7(String p_no) {
        try {
            this.patient_no = p_no;
            ResultSet rs = DBC.executeQuery("SELECT * FROM patients_info LEFT JOIN death_info ON dead_guid = guid  WHERE p_no = + '" + p_no + "'");
            rs.next();
            MSH[0] = "MSH";
            // MSH[1]="|";
            MSH[2] = "^~\\&";
            MSH[7] = dateString;
            MSH[9] = "REF^I12";
            MSH[10] = "MES" + getNewPassword(); //
            MSH[11] = "001"; //
            MSH[12] = "2.4";
            MSH[18] = "BIG-5";
            // PID
            PID[1] = rs.getString("p_no");
            PID[2] = rs.getString("p_no");
            PID[3] = rs.getString("nia_no").equals("") ? rs.getString("p_no") : rs.getString("nia_no");
            System.out.print(PID[3]);
            PID[4] = rs.getString("p_no");
            PID[5] = rs.getString("firstname") + "^" + rs.getString("lastname");
            PID[6] = rs.getString("mother_name");
            PID[7] = rs.getString("birth");
            PID[8] = rs.getString("gender");
            PID[9] = rs.getString("alias");
            PID[10] = rs.getString("race");
            PID[11] = rs.getString("address");
            PID[12] = rs.getString("country");
            PID[13] = rs.getString("phone");
            PID[14] = rs.getString("business_phone");
            PID[15] = rs.getString("language");
            PID[16] = rs.getString("marital_status");
            PID[17] = rs.getString("religion");
            PID[18] = rs.getString("account_num");
            PID[19] = rs.getString("ssn_num");
            PID[20] = rs.getString("driver_num");
            PID[21] = rs.getString("mother_id");
            PID[22] = rs.getString("tribe");
            PID[23] = rs.getString("place_of_birth");
            PID[24] = rs.getString("multiple_birth");
            PID[25] = rs.getString("birth_order");
            PID[26] = rs.getString("citizenship");
            PID[27] = rs.getString("veterans_military");
            PID[28] = rs.getString("nationality");
            PID[30] = rs.getString("date_of_death");
            PID[31] = rs.getString("indicator");
            PID[32] = rs.getString("identity_unknown");
            PID[33] = rs.getString("identity_code");
            PID[34] = rs.getString("udate");
            PID[35] = rs.getString("u_sno");
            PID[36] = "";
            PID[37] = "";
            PID[38] = "U";
            PID[39] = "";
            RF1[0] = "RF1";
            PRD[0] = "PRD";
            PID[0] = "PID";
            DRG[0] = "DRG";
            DG1[0] = "DG1";
            PR1[0] = "PR1";
            OBR[0] = "OBR";
            NTE[0] = "NTE";
            OBX[0] = "OBX";
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(HL7output.class.getName()).log(Level.SEVERE, null, ex);
        }

	
	}

	// �t�θ�� �Ҧp�t�ΦW��
	private void SysInformation()  {
        try {
            int i = 0;
            ResultSet rs = DBC.executeQuery("SELECT * FROM sys_info");
            rs.next();
            // MSH[12] = rs.getString("hl7_edition");
            PRD[1] = "CP";
            PRD[2] = rs.getString("hos_name");
            PRD[3] = rs.getString("hos_address");
            PRD[4] = rs.getString("hos_local");
            PRD[5] = rs.getString("hos_info");
            PRD[6] = rs.getString("hos_mail");
            PRD[7] = rs.getString("hos_id");
            DG1[1] = String.valueOf(i);
            DG1[2] = rs.getString("icd_deition");
            DG1[14] = rs.getString("icd_deition");
            PR1[2] = rs.getString("icd_deition");
            rs = DBC.executeQuery("SELECT * FROM diagnosis_code");
            rs.next();
            DG1[3] = rs.getString("icd_code");
            rs.close();
            // rs = DBC.executeQuery("SELECT * FROM prescription_code , prescription ,
            // outpatient_services");
            // rs.next();
            // PR1[17] = rs.getString("type");
            // PR1[18] = rs.getString("code");
            // rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(HL7output.class.getName()).log(Level.SEVERE, null, ex);
        }
		
	}

	
	public String printHl7(String p_no)  {
            try {
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                dateString = sdf.format(date);
                outputhl7(p_no);
                SysInformation();
                String hl7message = "";
                //
                ResultSet rs = DBC.executeQuery("SELECT * FROM outpatient_services , registration_info , patients_info " + "WHERE patients_info.p_no = + '" + p_no + "'" + " AND registration_info.p_no = patients_info.p_no" + " AND outpatient_services.reg_guid = registration_info.guid");
                while (rs.next()) {

                    if (rs.getString("summary").replace("	","").replace("\n","").length() > 40) {
                        DG1[4] = rs.getString("summary").replace("	","").replace("\n","").substring(0, 40);
                    } else {
                        DG1[4] = rs.getString("summary").replace("	","").replace("\n","");
                    }

                    if (rs.getString("finishtime") != null) {
                        DG1[5] = f.format(rs.getString("finishtime"));
                    } else {
                        DG1[5] = "";
                    }
                    if (rs.getString("hospitalized") != null && rs.getString("hospitalized").equals("1")) {
                        DG1[6] = "W";
                    } else {
                        DG1[6] = "F";
                    }
                    DG1[19] = DG1[5];
                    addpr1(rs.getString("outpatient_services.guid"));
                    hl7message += printf();
                }
                return hl7message;
            } catch (SQLException ex) {
                Logger.getLogger(HL7output.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;
	}

	public String printf() {

		return arg(MSH) + arg(RF1) + arg(PRD) + arg(PID) + arg(DG1)/* + arg(DRG)**/
				+ pr1 + arg(OBR) + arg(NTE) + obx + "\n\n\n********END********\n\n\n";
	}
//-----------------------------------------------//
	private void pre(String guid){
            try {
                ResultSet rs = DBC.executeQuery("SELECT * FROM prescription , prescription_code " + "WHERE prescription.os_guid = + '" + guid + "'" + " AND prescription.code = prescription_code.code");
                while (rs.next()) {
                    PR1[3] = rs.getString("prescription.code");
                    //PR1[17] = rs.getString("");
                    PR1[18] = rs.getString("tissue_type");
                }
                rs.close();
            } catch (SQLException ex) {
                Logger.getLogger(HL7output.class.getName()).log(Level.SEVERE, null, ex);
            }
	}

	private void addpr1(String guid){
        try {
            System.out.println(guid);
            String sql = "SELECT * FROM diagnostic , outpatient_services " + " WHERE outpatient_services.guid = diagnostic.os_guid " + "AND outpatient_services.guid = '" + guid + "'";
            //            System.out.println(sql);
            ResultSet rs = DBC.executeQuery(sql);
            while (rs.next()) {
                DG1[15] = rs.getString("priority");
            }
            rs.close();
            sql = "SELECT * FROM outpatient_services LEFT JOIN treat  ON outpatient_services.guid = treat.os_guid  WHERE  outpatient_services.guid = '" + guid + "'";
            System.out.println(sql);
            rs = DBC.executeQuery(sql);
            int i = 0;
            while (rs.next()) {
                System.out.println("2");
                i++;
                pre(rs.getString("outpatient_services.guid"));
                PR1[1] = String.valueOf(i);
                PR1[4] = rs.getString("description");
                if (rs.getString("time") != null ) {
                    PR1[5] = rs.getString("time").substring(0, 4) + rs.getString("time").substring(5, 7) + rs.getString("time").substring(8, 10) + rs.getString("time").substring(11, 13) + rs.getString("time").substring(14, 16) + rs.getString("time").substring(17, 19) + rs.getString("time").substring(21, 21);
                    System.out.println("3");
                } else {
                    PR1[5] = "00000000000000";
                }
                PR1[6] = rs.getString("treat_type");
                PR1[7] = rs.getString("treat_record");
                PR1[8] = rs.getString("ane_id");
                PR1[9] = rs.getString("ane_code");
                PR1[10] = rs.getString("ane_record");
                PR1[11] = rs.getString("sur_id");
                PR1[13] = rs.getString("consent");
                PR1[14] = rs.getString("priority");
                pr1 += arg(PR1);
                // System.out.println(rs.getString("treat.os_guid"));
            }
            System.out.println("4");
            rs.close();
            rs = DBC.executeQuery("SELECT * FROM prescription , outpatient_services " + " WHERE outpatient_services.guid = prescription.os_guid " + "AND outpatient_services.guid = '" + guid + "'");
            while (rs.next()) {
                obx(rs.getString("prescription.code"));
                if (rs.getString("result") != null)  OBX[5] = rs.getString("result").replace("	","").replace("\n","");

              
                
                if (rs.getString("isnormal") == null) {
                    OBX[8] = "";
                } else {
                    OBX[8] = rs.getString("isnormal").equals("1") ? "A" : "N";
                    System.out.print(OBX[8]);
                }
                OBX[9] = rs.getString("probability");
                OBX[10] = rs.getString("abnormal_flags");
                OBX[3] = rs.getString("code");
                OBX[11] = rs.getString("state").equals("1") ? "F" : "P";
                OBX[12] = rs.getString("date_results") == null ? "" : rs.getString("date_results").substring(0, 4) + rs.getString("date_results").substring(5, 7) + rs.getString("date_results").substring(8, 10) + rs.getString("date_results").substring(11, 13) + rs.getString("date_results").substring(14, 16) + rs.getString("date_results").substring(17, 19) + rs.getString("date_results").substring(21, 21);
                OBX[14] = rs.getString("date_test") == null ? "" : rs.getString("date_test").substring(0, 4) + rs.getString("date_test").substring(5, 7) + rs.getString("date_test").substring(8, 10) + rs.getString("date_test").substring(11, 13) + rs.getString("date_test").substring(14, 16) + rs.getString("date_test").substring(17, 19) + rs.getString("date_test").substring(21, 21);
                OBX[16] = rs.getString("res_observer");
                OBX[17] = rs.getString("obs_method");
                OBX[19] = rs.getString("analysis_time");
                obx += arg(OBX);
            }
        } catch (SQLException ex) {
            Logger.getLogger(HL7output.class.getName()).log(Level.SEVERE, null, ex);
        }

		

	}

	private void obx(String obx) {
            try {
                ResultSet rs = DBC.executeQuery("SELECT * FROM prescription , prescription_code " + "WHERE prescription.os_guid = '" + obx + "'" + " AND prescription.code = prescription_code.code");
                while (rs.next()) {
                    OBX[18] = rs.getString("equipment_ID");
                    OBX[6] = rs.getString("unit");
                    OBX[7] = rs.getString("limit");
                }
            } catch (SQLException ex) {
                Logger.getLogger(HL7output.class.getName()).log(Level.SEVERE, null, ex);
            }

	}
}