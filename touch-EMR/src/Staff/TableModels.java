package Staff;

import javax.swing.table.AbstractTableModel;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

class TableModels extends AbstractTableModel {
    Object[][] rowData = null;
    String[] columnName = null;
    boolean createDefaultColumnName = false;
    int row=0, col=0, rowCount=0, columnCount=0;

    TableModels (ResultSet rs, String[] columnName){
        this.columnName = columnName;
        try {
            columnCount = rs.getMetaData().getColumnCount();
            rs.last();
            rowCount = rs.getRow();
            rs.first();
            System.out.println(rowCount + " " + columnCount);
            rowData = new Object[rowCount+1][columnCount];

            //set row data
            if(rowCount!=0){
                do {
                    rowData[0][0] = "None";
                    for (col = 0; col < columnCount; col++) {
                        rowData[row+1][col] = rs.getString(col+1);
                    }
                    row++;
                } while (row<rowCount && rs.next());
            }else{
                rowData[0][0] = "None";
            }
            

        } catch (SQLException ex) {
            Logger.getLogger(TableModels.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public int getColumnCount() {
        return this.columnName.length;
    }

    @Override
    public int getRowCount() {
        return this.rowData.length;
    }

    @Override
    public String getColumnName(int col) {
        System.out.println(this.columnName[col]);
        return this.columnName[col];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return this.rowData[rowIndex][columnIndex];
    }

    @Override
    public Class getColumnClass(int columnIndex){
        return this.getValueAt(0,columnIndex).getClass();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
	return false;
    }
    @Override
    public void setValueAt(Object value, int row, int col){
        rowData[row][col] = value;
        fireTableCellUpdated(row, col);
    }
}