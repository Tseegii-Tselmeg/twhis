/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Common;

import cc.johnwu.sql.DBC;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.Action;
import javax.swing.JComponent;

/**
 *
 * @author Steven
 */
public class Tools {
    /**
     * 相關檢驗對應資料庫檢驗名稱(避免日後資料庫變動)
     * "HbA1C", "BGAc", "BGPc", "TG", "HDL", "SBP", "waist"
     */
    private static final String s_HbA1C = "HbA1C";
    private static final String s_BGAc = "BGAc";
    private static final String s_BGPc = "BGPc";
    private static final String s_TG = "TG";
    private static final String s_HDL = "HDL";
    private static final String s_SBP = "SBP";
    private static final String s_waist = "waist";



    /**
     * 取得檢驗值  name:檢驗名稱 pno:病患編號
     */
    public static String getPrescriptionResult(String name, String pno) {
        ResultSet rs = null;
        try {
    
            rs = DBC.executeQuery(getPrescriptionSQL(name,pno));
            if (rs.next()) {
                return rs.getString("result");
            } else {
                return "";
            }
        } catch (SQLException ex) {
            Logger.getLogger(Tools.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        } finally {
                try {DBC.closeConnection(rs);}
                catch (SQLException e){

                }
           }
    }

    /**
     * 取得檢驗是否正常  name:檢驗名稱 pno:病患編號
     * return String 或 ""
     */
    public static String getPrescriptionIsnormal(String name, String pno) {
        ResultSet rs = null;
        try {

            rs = DBC.executeQuery(getPrescriptionSQL(name,pno));
            if (rs.next()) return rs.getString("isnormal");
             else return "";
            
        } catch (SQLException ex) {
            Logger.getLogger(Tools.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        } finally {
                try {DBC.closeConnection(rs);}
                catch (SQLException e){

                }
           }
    }
    
    /**
     * 取得最後檢驗日期  name:檢驗名稱 pno:病患編號
     * return String 或 ""
     */
    public static String getPrescriptionLastDate(String name, String pno) {
        ResultSet rs = null;
        try {

            rs = DBC.executeQuery(getPrescriptionSQL(name,pno));
            if (rs.next()) {
                return rs.getString("date_test");
            } else {
                return "";
            }
        } catch (SQLException ex) {
            Logger.getLogger(Tools.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        } finally {
                try {DBC.closeConnection(rs);}
                catch (SQLException e){

                }
           }
    }

    /**
     * 取得所有檢驗資料  name:檢驗名稱 pno:病患編號
     * return arr[0] 檢驗值  [1]是否正常 [2]檢驗日期
     */
    public static String[] getPrescription(String name, String pno) {
        ResultSet rs = null;
        String[] arr = {"","",""};
        try {
            rs = DBC.executeQuery(getPrescriptionSQL(name,pno));
            if (rs.next()) {
                if (rs.getString("result") == null ) arr[0] = "";
                else arr[0] = rs.getString("result");

                if (rs.getString("isnormal") == null ) arr[1] = "";
                else arr[1] = rs.getString("isnormal");

                if (rs.getString("date_test") == null ) arr[2] = "";
                else arr[2] = rs.getString("date_test");

                return arr;
            } else {
                return arr;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Tools.class.getName()).log(Level.SEVERE, null, ex);
            return arr;
        } finally {
                try {DBC.closeConnection(rs);}
                catch (SQLException e){

                }
           }
    }

    private static String getPrescriptionSQL(String name, String pno) {
        return "SELECT prescription.result AS result, prescription.isnormal AS isnormal, prescription.date_test AS date_test " +
                    "FROM prescription LEFT JOIN  outpatient_services ON prescription.os_guid = outpatient_services.guid , " +
                    "patients_info, registration_info, prescription_code  " +
                    "WHERE registration_info.p_no = patients_info.p_no " +
                    "AND prescription_code.code =  prescription.code " +
                    "AND (outpatient_services.reg_guid = registration_info.guid " +
                    "OR prescription.case_guid = registration_info.guid) " +
                    "AND prescription_code.name = '" + name + "' " +
                    "AND patients_info.p_no = '" + pno + "' " +
                    "AND prescription.result IS NOT NULL " +
                    "ORDER BY prescription.date_results  DESC ";
    }

    /**
     * 糖尿病套餐 V1 V2 V3
     */
    public static Map<Object, Object> getV(String v) {
        Map<Object, Object> V1HashMap = new LinkedHashMap<Object, Object>();
        if (v.equals("V2")) {
            V1HashMap.put("09005C01","09005C01  BGAc  ");
            V1HashMap.put("09006C","09006C  HbA1c  ");
            V1HashMap.put("09015C","09015C  Cr  ");
        } else if (v.equals("V1") || v.equals("V3")){
            V1HashMap.put("09005C01","09005C01  BGAc  ");
            V1HashMap.put("09005C02","09005C02  BGPc  ");
            V1HashMap.put("09006C","09006C  HbA1C  ");
            V1HashMap.put("09004C","09004C  TG  ");
            V1HashMap.put("09043C","09043C  HDL  ");
            V1HashMap.put("09044C","09044C  LDL  ");
            V1HashMap.put("09025C","09025C  GOT  ");
            V1HashMap.put("09026C","09026C  GPT  ");
            V1HashMap.put("09003C","09003C  BUN  ");
            V1HashMap.put("09015C","09015C  Cr  ");
            V1HashMap.put("06003C","06003C  U / Prot   ");
            V1HashMap.put("09038C","09038C  U / Alb   ");
            // 12個項目

        }
       return V1HashMap;
    }

    /**
     * 確認檢驗是否正常 code:檢驗code, age:年齡, gender:性別, value:檢驗值
     * return 0 正常 1 異常 2 錯誤
     */
    public static int getPrescriptionNormal(String code, int age, String gender,double value) {
        ResultSet rs = null;
        boolean isAge = false;
        boolean isGender = false;
        try {
            String sql = "SELECT `limit` FROM prescription_code " +
                        "WHERE code = '" + code + "' ";
            rs = DBC.executeQuery(sql);
            if(!rs.next() || rs.getString("limit") == null) return 2;

            String lm[] = rs.getString("limit").split("&");

            if (lm.length == 0) return 0;

            for(int i = 0; i < lm.length; i++ ) {
                String lm2[] = lm[i].split("!");  // lm2[0] 年齡 [1] 性別

                // ------------ 年齡判斷 -----------
                String ageArr[] =  lm2[0].split("-");
                if ((ageArr.length == 1 && (lm2[0].equals("n") || Integer.parseInt(lm2[0]) == age))
                || (ageArr.length == 2 && Integer.parseInt(ageArr[0]) <= age && Integer.parseInt(ageArr[1]) >= age)) isAge = true;

                // ------------ 性別判斷 -----------
                if (lm2[1].equals(gender) || lm2[1].equals("n") ) isGender = true;

                // --------- 年齡性別符合 進入檢驗值判斷 -----------
                if (isAge && isGender) {
                    for (int y =2; y < lm2.length; y++) {
                        String a[] = lm2[y].split(">");
                        String b[] = lm2[y].split("<");
                        String c[] = lm2[y].split("-");

                        if (a.length == 2 && (Double.parseDouble(a[1]) <=  value))  return 1;
                        else if (b.length == 2 && (Double.parseDouble(b[1]) >=  value))  return 1;
                        else if (c.length == 2 && (Double.parseDouble(c[0]) <=  value && value <= Double.parseDouble(c[1])))  return 1;
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Tools.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {DBC.closeConnection(rs);}
            catch (SQLException e){
                Logger.getLogger(Tools.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return 0;
    }

    /**
     * 是否為數值資料(包含小數點)
     */
    public static boolean isNumber(String str) {
        Pattern pattern = Pattern.compile("[0-9,.]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches() || str.trim().equals("")) {
            System.out.println("FALSE");
            return false;
        } else {
            System.out.println("TRUE");
            return true;
        }
    }

    /**
     * 計算BMI height:身高 weight:體重
     */
     public static String getBmi(String height, String weight) {
        if (Common.Tools.isNumber(height) && Common.Tools.isNumber(weight) &&  height != null && weight != null && !height.equals("") && !weight.equals("")) {
            Double bmi = (Double.parseDouble(weight) / (Double.parseDouble(height) / 100 * Double.parseDouble(height) / 100));

            int b = (int) Math.round(bmi * 100);
            Double k = (double) b / 100.00;

            return k.toString();
        }
        return "";
    }

     /**
     * 診間  糖尿病限定值判斷  糖尿病五選三
     * pno:病患編號 gender:病患性別
     * return 顯示字串
     * 判斷檢驗項目為:"TG", "HDL", "SBP", "BGAc", "waist"
     */
    public static String getCheckDM(String pno, String gender) {
            String checkstr = "";
            String tg = getPrescriptionResult(s_TG,pno);
            String hdl = getPrescriptionResult(s_HDL,pno);
            String sbp = getPrescriptionResult(s_SBP,pno);
            String bgac = getPrescriptionResult(s_BGAc,pno);
            String waist = getPrescriptionResult(s_waist,pno);

            int check = 0;
            if (Common.Tools.isNumber(tg) && !tg.equals("") && Double.parseDouble(tg) >= 150 ) {
                checkstr += "TG = "+tg+" mg/dL \n";
                System.out.println("TG = "+tg+" mg/dL \n");
                check++;
            }

            if (Common.Tools.isNumber(hdl) && !hdl.equals("")
                    && ((gender.equals("M") && Double.parseDouble(hdl) < 40)
                    || (gender.equals("F") && Double.parseDouble(hdl) < 50) )) {
                if (gender.equals("M")) checkstr += "HDL = "+hdl+" mg/dL in males \n";
                else checkstr += "HDL = "+hdl+" mg/dL in females \n";
                System.out.println("HDL = "+hdl+" mg/dL in females \n");
                check++;
            }

            if (Common.Tools.isNumber(sbp) && !sbp.equals("") && Double.parseDouble(sbp)>= 130 ) {
                checkstr += "SBP  = "+sbp+" mmHg \n";
                System.out.println("SBP  = "+sbp+" mmHg \n");
                check++;
            }

            if (Common.Tools.isNumber(bgac) && !bgac.equals("") && Double.parseDouble(bgac) > 5.6 ) {
                checkstr += "BGAc = "+bgac+" mmol/L \n";
                check++;
            }

 
            if (Common.Tools.isNumber(waist) && Common.Tools.isNumber(hdl) && (!hdl.equals("") && !waist.equals(""))
                    && ((gender.equals("M") && Double.parseDouble(waist) >= 90)
                    || (gender.equals("F") && Double.parseDouble(waist) >= 80) )) {
                if (gender.equals("M")) checkstr += "Waist = "+waist+" cm in males \n";
                else checkstr += "Waist = "+waist+" cm in females \n";
                check++;
            }

            if (check >= 3) {
                checkstr = "=========== Metabolic Syndrome ===========\n"+checkstr + "\n";
                return checkstr;
            }
        return "";
    }

     /**
     * 診間  糖尿病限定值判斷  糖尿病確認
     * pno:病患編號
     * return 顯示字串
     * 判斷檢驗項目為:"HbA1C", "BGAc", "BGPc", "BGAc"
     */
    public static String getDM(String pno) {
        String checkStr = "";
        String hba1c = getPrescriptionResult(s_HbA1C,pno);
        String bgac = getPrescriptionResult(s_BGAc,pno);
        String bgpc = getPrescriptionResult(s_BGPc,pno);
        //String bgac = getPrescriptionIsnormal("BGAc",pno);
        if (Common.Tools.isNumber(hba1c) && !hba1c.equals("") && Double.parseDouble(hba1c) >= 6.5 ) {
            checkStr += "A1C = "+hba1c+" % \n";
        }

        if (Common.Tools.isNumber(bgac) && !bgac.equals("") && Double.parseDouble(bgac) >= 160 ) {
            checkStr += "FPG  = "+bgac+" mg/dl \n";
        }

        if (Common.Tools.isNumber(bgpc) && !bgpc.equals("") && Double.parseDouble(bgpc)>=130 ) {
            checkStr += "2-h Plasma glucose = "+bgpc+" mmol/ol \n";
        }

        if ((Common.Tools.isNumber(bgac)  && Common.Tools.isNumber(bgpc) )&& (!bgac.equals("") && !bgpc.equals(""))
           && (Double.parseDouble(bgac)>=11.1 || Double.parseDouble(bgpc)>=11.1)) {
            checkStr += "random Plasma glucose = "+bgac+" mmol/ol \n";
        }

       if (!checkStr.equals("")) {
            checkStr = "========== DM ==========\n" + checkStr + "\n";
            return checkStr;
        }
        return "";
    }

    /**
     * 診間  糖尿病限定值判斷  疑似糖尿病五選三
     * pno:病患編號
     * return 顯示字串
     * 判斷檢驗項目為:"HbA1C", "BGAc", "BGPc"
     */
     public static String getPREDM(String pno) {
        String checkStr = "";
        String hba1c = getPrescriptionResult(s_HbA1C,pno);
        String bgac = getPrescriptionResult(s_BGAc,pno);
        String bgpc = getPrescriptionResult(s_BGPc,pno);
        if (Common.Tools.isNumber(bgac) && !bgac.equals("") && (Double.parseDouble(bgac) >= 100 && Double.parseDouble(bgac) <= 125)) {
            checkStr += "FPG = "+bgac+" mg/dl \n";
        }

        if (Common.Tools.isNumber(bgpc) && !bgpc.equals("") && (Double.parseDouble(bgpc) >= 7.8 &&  Double.parseDouble(bgpc) <= 11.0)) {
            checkStr += "2-h PG in the 75-g OGTT = "+bgpc+" mmol/ol \n";
        }

        if (Common.Tools.isNumber(hba1c) && !hba1c.equals("") && (Double.parseDouble(hba1c) >= 5.7 &&  Double.parseDouble(hba1c) <= 6.4)) {
            checkStr += "AIC = "+hba1c+" % \n";
        }

       if (!checkStr.equals("")) {
            checkStr = "========== PRE DM ========\n" + checkStr + "\n";
            return checkStr;
        }
        return "";
    }

     public static void postToolTip(JComponent comp) {
      Action action = comp.getActionMap().get("postTip");
      if (action == null) // no tooltip
       return;
      ActionEvent ae = new ActionEvent(comp, ActionEvent.ACTION_PERFORMED, "postTip", EventQueue.getMostRecentEventTime(), 0);
      action.actionPerformed(ae);
    }

     public static void hideToolTip(JComponent comp) {
        Action action = comp.getActionMap().get("hideTip");
        if (action == null) // no tooltip
        return;
        ActionEvent ae = new ActionEvent(comp, ActionEvent.ACTION_PERFORMED, "hideTip", EventQueue.getMostRecentEventTime(), 0);
        action.actionPerformed(ae);
     }


}


