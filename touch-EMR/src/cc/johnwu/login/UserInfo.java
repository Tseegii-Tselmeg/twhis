package cc.johnwu.login;


import cc.johnwu.sql.*;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;
import javax.swing.*;


public class UserInfo {

    private Frm_Login m_frm_Login;
    private javax.swing.JFrame m_EnteredFrm;
    private static String   s_NO;
    private static String   s_ID;
    private static String   s_Name;
    private static String   s_LoginTime;
    private static String   s_OnlineGuid;
    private static String   s_Department;
    private static String   s_Policlinic;
    private static String   s_PoliclinicType;
    private static String   s_Position;
    private static String   s_PermissionGroup;
    private static String   s_PermissionLevel[][];

    public UserInfo(boolean showLogin) throws InterruptedException{
        m_frm_Login = new Frm_Login(this, showLogin);
        new DBC(m_frm_Login);
    }

    public void openFrame(javax.swing.JFrame frm){
        m_EnteredFrm = frm;
//        boolean flag = DBC.isClosed();
//        if(!flag && !m_frm_Login.isVisible()){
//            m_EnteredFrm.setVisible(true);
//        }
    }

    protected boolean setOnLine(String userID){
        if(setUserInfo(userID)){
            m_EnteredFrm.setVisible(true);

            ((Main.Frm_Main)m_EnteredFrm).initPermission();
            m_frm_Login.dispose();  // 登入畫面消失
            return true;
        }
        return false;
    }

    private boolean setUserInfo(String userID){
        ResultSet rs = null;
        String sql_loginLog = "";   //  登入
        String sql_online = "";
        String sql_power = "";

         String sql_info = "SELECT staff_info.s_no as no, " +
                                "staff_info.s_id as id, " +
                                "'DEFAULT' AS posi, "+
                                "concat(staff_info.firstname,'  ',staff_info.lastname) as name, "+
                                "policlinic.name as poli, " +
                                "policlinic.typ as poli_type, " +
                                "department.name as dep, "+
                                "staff_info.gp_guid as pow_gp "+
                          "FROM staff_info "+
                                "LEFT JOIN (policlinic CROSS JOIN " +
                                           "department CROSS JOIN permission_info ) "+
                                "ON (department.guid = staff_info.dep_guid "+
                                "AND policlinic.guid = staff_info.poli_guid "+
                                "AND permission_info.guid = staff_info.gp_guid) "+
                          "WHERE s_id = '"+userID+"' ";


        try {
            SysInfo a =  new SysInfo();
            a.setShiftTime();

            rs = DBC.executeQuery(sql_info);
            rs.next();
            s_NO = rs.getString("no");
            s_ID = rs.getString("id");
            s_Name = rs.getString("name");
            s_PermissionGroup = rs.getString("pow_gp");
            s_Department = rs.getString("dep");
            s_Policlinic = rs.getString("poli");
            s_PoliclinicType = rs.getString("poli_type");
            s_Position = rs.getString("posi");            
            if(s_PermissionGroup==null){
                return false;
            }
            sql_power = "SELECT grp_name, lvl, sys_name " +
                    "FROM permission_info " +
                    "WHERE (SELECT grp_name FROM permission_info " +
                    "WHERE guid = '" + s_PermissionGroup + "') = grp_name";
            rs = DBC.executeQuery(sql_power);
            rs.last();
            s_PermissionGroup = rs.getString("grp_name");
            s_PermissionLevel = new String[rs.getRow()][2];
            rs.beforeFirst();
            while(rs.next()){
                s_PermissionLevel[rs.getRow()-1][0] = rs.getString("sys_name");
                s_PermissionLevel[rs.getRow()-1][1] = rs.getString("lvl");
            }
            
            s_LoginTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
            sql_loginLog = "INSERT login_log VALUES " +
                            "(uuid(), '"+s_NO+"', '"+s_LoginTime+"') ";
            DBC.executeUpdateLogin(sql_loginLog);
            s_OnlineGuid = UUID.randomUUID().toString();
            sql_online = "UPDATE staff_info SET online = 1, online_guid = '"+s_OnlineGuid+"' WHERE s_id = '"+userID+"'";
            DBC.executeUpdateLogin(sql_online);
            return true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(new Frame(), ex);
        } finally {
            try{DBC.closeConnection(rs);}
            catch (SQLException ex) {}
        }
        return false;
    }

    public static String getUserNO(){
        return s_NO;
    }

    public static String getOnlineGuid(){
        return s_OnlineGuid;
    }

    public static String getLoginTime(){
        return s_LoginTime;
    }

    public static String getUserID(){
        return s_ID;
    }

    public static String getUserName(){
        return s_Name;
    }

    public static String getUserPowerGroup(){
        return s_PermissionGroup;
    }

    public static String getUserDepartment(){
        return s_Department;
    }

    public static String getUserPoliclinic(){
        return s_Policlinic;
    }

    public static String getUserPoliclinicType(){
        if (s_PoliclinicType == null) return "";
        else return s_PoliclinicType;
    }

    public static String getUserPosition(){
        return s_Position;
    }



    public static boolean getSelectPow(String systemName){
        return getSystemPow(systemName,'s');
    }

    public static boolean getInsertPow(String systemName){
        return getSystemPow(systemName,'i');
    }

    public static boolean getUpdatePow(String systemName){
        return getSystemPow(systemName,'u');
    }

    public static boolean getDeletePow(String systemName){
        return getSystemPow(systemName,'d');
    }

    private static boolean getSystemPow(String systemName,char type){
        int rightMove = 0;
        switch(type){
            case 's':
                rightMove = 0;
                break;
            case 'i':
                rightMove = 1;
                break;
            case 'u':
                rightMove = 2;
                break;
            case 'd':
                rightMove = 3;
                break;
        }
        for(int i=0; i<s_PermissionLevel.length; i++){
            if(systemName.equals(s_PermissionLevel[i][0]))
                if(((Integer.valueOf(s_PermissionLevel[i][1])>>>rightMove)&1)==1)
                    return true;
        }
        return false;
    }

}
