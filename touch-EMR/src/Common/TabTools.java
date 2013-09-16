/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Common;

import java.awt.Color;
import java.awt.Component;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author Steven
 */
public class TabTools {
 
    /**
     * 設定 TABLE 可編輯欄位  name:檢驗名稱 pno:病患編號
     */
    public static Object[] setTableEditColumn(DefaultTableModel tableModel, JTable table, String[] tableTitle, final int[] appointColumnEditable, int tableRowNo) {
        String[][] value = new String[Constant.SHOW_ROW_COUNT][tableTitle.length];
        tableModel = new DefaultTableModel(value, tableTitle) {

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                for (int i = 0; i < appointColumnEditable.length; i++) {
                    if (columnIndex == appointColumnEditable[i]) {
                        return true;
                    }
                }
                return false;
            }
        };
        table.setModel(tableModel);
        for (int i = 0; i < Constant.SHOW_ROW_COUNT; i++) {
            table.setValueAt(++tableRowNo, i, 0); // set row number
        }

        Object ob[] = {tableModel, tableRowNo};    // return tableModel RowNumber
        return ob;
    }

    /**
     * JTable 特定ROW變色
     * tab:變色Table colindex:作為條件欄  array:變換顏色[條件值][顏色]
     *  ex : Object[][] array = {{"O",Color.BLUE}, {"F",Color.BLUE}};
     */
     public static void setTabColor(JTable table, final int colindex, final Object array[][]){
        try
        {
            DefaultTableCellRenderer tcr = new DefaultTableCellRenderer()
            {
                @Override
              public Component getTableCellRendererComponent(JTable table,
                  Object value, boolean isSelected, boolean hasFocus,int row, int column) {
                    for (int i =0; i< array.length; i++ ) {
                        if (table.getValueAt(row, colindex) != null && table.getValueAt(row, colindex).equals(array[i][0])) {
                            setBackground((Color)array[i][1]);

                        } else if (array[i][0] == null && table.getValueAt(row, colindex) == null) {
                            setBackground((Color)array[i][1]);
                        }
                    }
                    boolean IsSetWhite = true;
                    for (int y =0; y < array.length; y++ ) {
                        if ((array[y][0] == null && table.getValueAt(row, colindex) == null)
                        || (table.getValueAt(row, colindex) != null && table.getValueAt(row, colindex).equals(array[y][0])))
                           IsSetWhite =false;
                    }
                    if (IsSetWhite)  setBackground(Color.white);
                    return super.getTableCellRendererComponent(table, value,isSelected, hasFocus, row, column);
                }
            };
            for(int i = 0; i < table.getColumnCount(); i++) {
                table.getColumn(table.getColumnName(i)).setCellRenderer(tcr);
            }
        }
        catch (Exception ex) {
          Logger.getLogger(TabTools.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


     /**
     * JTable 隱藏特定欄位
     * tab:變色Table  index:Column Number
     */
     public static void setHideColumn(JTable table,int index){  // 隱藏欄位
         TableColumn tc= table.getColumnModel().getColumn(index);
         tc.setMaxWidth(0); tc.setPreferredWidth(0); tc.setMinWidth(0);
         tc.setWidth(0); table.getTableHeader().getColumnModel().getColumn(index).setMaxWidth(0);
         table.getTableHeader().getColumnModel().getColumn(index).setMinWidth(0);
    }


     /**
     * 清空 JTable 所有欄位資料
     * tab: 傳入要清空的JTable
     */
    public static void setClearTableValue(JTable tab) {
        tab.removeEditor();
        for (int i = 0; i < tab.getRowCount(); i++ ) {
            for (int t = 1; t < tab.getColumnCount(); t++) {
                tab.setValueAt(null, i, t);
            }
        }
    }

    /**
     * 移除TABLE 編輯狀態的欄位 與 取消光條選取
     * tab: 傳入要清空的JTable
     */
    public static void setRemoveEditAndSelection(JTable table) {
        table.removeEditor();
        table.removeRowSelectionInterval(0, table.getRowCount()-1);
    }

    public static String getTooltipStr(String originalStr, int charPerRow)
      {
            if (originalStr != null) {
                int len = originalStr.length();
            String tempStr1="",tempStr2="",resultStr="";

            resultStr = resultStr + "<html>";
            if(len>charPerRow)
            {
              int i=1;
              do{
                tempStr1 = originalStr.substring(charPerRow*(i-1), charPerRow*i);
                tempStr2 = originalStr.substring(charPerRow*i);
                i++;
                resultStr = resultStr + tempStr1 + "<br>";
              }while(tempStr2.length()>charPerRow);

              resultStr = resultStr + tempStr2 + "</html>";
            }
            else
              resultStr = originalStr;

            return resultStr;
        }
            return "";
        
      }
}
