/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cc.johnwu.login;

import cc.johnwu.sql.DBC;
import java.awt.Frame;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Steven
 */
public class SysInfo {
     public static int s_Morning_S;
     public static int s_Morning_E;
     public static int s_Noon_S;
     public static int s_Noon_E;
     public static int s_Night_S;
     public static int s_Night_E;
     public static String s_HosName;

     public  void setShiftTime() {
         ResultSet rs = null;
        try {

            String sql = "SELECT * FROM setting";
            rs = DBC.executeQuery(sql);
            rs.next();
            s_Morning_S = rs.getInt("morning_shift_s");
            s_Morning_E = rs.getInt("morning_shift_e");
            s_Noon_S = rs.getInt("noon_shift_s");
            s_Noon_E = rs.getInt("noon_shift_e");
            s_Night_S = rs.getInt("evening_shift_s");
            s_Night_E = rs.getInt("evening_shift_e");



        } catch (SQLException ex) {
            Logger.getLogger(SysInfo.class.getName()).log(Level.SEVERE, null, ex);
        } try{DBC.closeConnection(rs);}
            catch (SQLException ex) {}
        }

     public static String getHosName(){
         ResultSet rs = null;
         try {
            String sql = "SELECT * FROM sys_info ";
            rs = DBC.executeQuery(sql);
            rs.next();
            s_HosName = rs.getString("hos_name");
            
            return s_HosName;
        } catch (SQLException ex) {
            Logger.getLogger(SysInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

}
