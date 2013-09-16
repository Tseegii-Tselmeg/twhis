/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Common;

import java.awt.Color;

/**
 *
 * @author Steven
 */
public class Constant {
    public static final int SHOW_ROW_COUNT = 20;           // 預設 table 顯示行數
    public static final int AUTOCOMPLETE_SHOW_ROW = 20;   // 限制Auto Complete顯示出的最大筆數
    public static final int AUTOCOMPLETE_HEIGHT = 160;      // 預設 AUTOCOMPLETE height
    public static final String X_RAY_CODE = "Radiology(X-Ray)";         // 處置X光代碼
    public static final String POLINAME_DM = "Medical Clinic(diabetes)";  // 特殊診名稱
    public static final Color WARNING_COLOR =  new Color(250,232,176);  // 警告色
    public static final Color FINISH_COLOR =  new Color(156,200,244);     // 完成色
    public static final long REFRASHTIME = 1000; //自度刷新跨號資訊時間
}
