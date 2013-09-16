package SMS;

import Diagnosis.*;
import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class TableTriStateCell {
    static class TriStateCellRenderer extends DefaultTableCellRenderer {
        private JCheckBox checkBox;

        public TriStateCellRenderer() {
            this.checkBox = new JCheckBox();
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {

            if (value != null) {
                Boolean selected = (Boolean) value;
                checkBox.setSelected(selected);
                return checkBox;
            }
            Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            return comp;
        }
    }

    static class TriStateCellEditor extends DefaultCellEditor {
        public TriStateCellEditor() {
            super(new JCheckBox());
        }
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (value == null) return null;
            return super.getTableCellEditorComponent(table, value, isSelected, row, column);
        }
    }
}
