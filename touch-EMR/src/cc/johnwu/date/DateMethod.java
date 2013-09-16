/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cc.johnwu.date;

import cc.johnwu.login.SysInfo;
import java.text.*;
import java.util.*;

/**
 *
 * @author johnwu1114
 */
public class DateMethod {
    //private static Date m_Now = Calendar.getInstance().getTime();
    private static int m_Year;
    private static int m_Month;


    public static String getAge(Date birth){
        m_Year = Calendar.getInstance().getTime().getYear()-birth.getYear();
        m_Month = Calendar.getInstance().getTime().getMonth()-birth.getMonth();
        if(m_Month<0){
            m_Year -= 1;
            m_Month += 12;
        }
        return "Y"+m_Year;
    }
    
    public static String getAgeWithMonth(Date birth){
        DateMethod.getAge(birth);
        return m_Year+" Y  "+m_Month+" M";
    }

    public static int getNowShiftNum(){

        int HHmm = Calendar.getInstance().getTime().getHours()*100 + Calendar.getInstance().getTime().getMinutes();

        if(HHmm >= SysInfo.s_Morning_S && HHmm<=SysInfo.s_Morning_E){
            return 1; // "Morning";
        }else if(HHmm>=SysInfo.s_Noon_S && HHmm<SysInfo.s_Noon_E){
            return 2; // "Afternoon";
        }else if(HHmm>=SysInfo.s_Night_S && HHmm<=SysInfo.s_Night_E){
            return 3; // "Night";
        }else {
            return 1; //"All Night";
        }
    }

    public static int getShiftStr2Num(String str){
        if(str.equals("Morning")){
            return 1; // "Morning";
        }else if(str.equals("Afternoon")){
            return 2; // "Afternoon";
        }else if(str.equals("Night")){
            return 3; // "Night";
        }else {
            return 1; //"All Night";
        }
    }

    public static String getShiftNum2Str(int num){
        if(num==1){
            return "Morning";
        }else if(num==2){
            return "Afternoon";
        }else if(num==3){
            return "Night";
        }else {
            return "Morning";//"All Night";
        }
    }

    public static String getTodayYMD(){
        String str_date = (1900+Calendar.getInstance().getTime().getYear())+
                          "-"+( (1+Calendar.getInstance().getTime().getMonth())>=10?(1+Calendar.getInstance().getTime().getMonth()):"0"+(1+Calendar.getInstance().getTime().getMonth()) )+
                          "-"+( Calendar.getInstance().getTime().getDate()>=10?Calendar.getInstance().getTime().getDate():"0"+Calendar.getInstance().getTime().getDate() );
        return str_date;
    }

    public static int getDaysRange(String dateString){
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date d1 = formatter.parse(dateString);
            Date d2 = formatter.parse(getTodayYMD());
            return (int)((d1.getTime() - d2.getTime())/(1000*60*60*24));
        } catch (ParseException ex) {
        }
        return 0;
    }
}
