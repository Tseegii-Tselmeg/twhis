package cc.johnwu.login;

import cc.johnwu.sql.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author steven
 */
public class OnlineState {

    public static void OnlineState() {
        String sqlCheck;
        ResultSet rs = null;
        try {
            if (UserInfo.getUserNO() != null && UserInfo.getOnlineGuid() != null) {
                sqlCheck = "SELECT s_no FROM staff_info " +
                           "WHERE s_no = '" + UserInfo.getUserNO() + "' " +
                           "AND online_guid = '" + UserInfo.getOnlineGuid() + "' " +
                           "AND online = 1";
                rs = DBC.executeQueryLogin(sqlCheck);
                if (!rs.next()) {
                    JOptionPane.showMessageDialog(null,"Warning! Repeat To Login.");
                    System.exit(0);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(OnlineState.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try{DBC.closeConnection(rs);}
            catch (SQLException ex) {}
        }
    }

    public static void LogOut() {
        try {
            String sql = "UPDATE staff_info SET online = 0, online_guid = NULL WHERE s_id = '" + UserInfo.getUserID() + "'";
            DBC.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(OnlineState.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
