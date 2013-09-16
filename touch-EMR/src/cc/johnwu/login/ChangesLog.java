/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cc.johnwu.login;

import cc.johnwu.sql.DBC;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author steven
 */
public class ChangesLog {
    //  傳入 哪張資料表  修改資料的guid  做什麼修改
    public static void ChangesLog(String table_name,String table_guid, String chg_info) {
        String sql = "INSERT changes_log VALUES (uuid(), '"+table_name+"', '"+table_guid+"', '"+UserInfo.getUserNO()+"', '"+chg_info+"', '"+UserInfo.getLoginTime()+"')";
        try {
            DBC.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(ChangesLog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
