package at.ac.ait.speedr.table;

import java.awt.Component;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.EventObject;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author visnei
 */
public class RTableCellEditor implements TableCellEditor {

    private static NumberFormat nf;
    private TableCellEditor delegate;

    public RTableCellEditor(TableCellEditor defaultCellEditor) {
        this.delegate = defaultCellEditor;
        if (nf == null) {
            nf = NumberFormat.getInstance();
            nf.setGroupingUsed(false);
            nf.setMaximumFractionDigits(9);
            
            if (nf instanceof DecimalFormat) {
                DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance();
                dfs.setDecimalSeparator('.');
                ((DecimalFormat)nf).setDecimalFormatSymbols(dfs);
//                ((DecimalFormat)nf).setDecimalSeparatorAlwaysShown(true);
//                ((DecimalFormat)nf).setMinimumFractionDigits(1);
            }
        }
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (value != null && value instanceof Double) {
            JTextField textfield = (JTextField) delegate.getTableCellEditorComponent(table, value, isSelected, row, column);
            textfield.setText(nf.format(value));
            return textfield;
        } else {
            return delegate.getTableCellEditorComponent(table, value, isSelected, row, column);
        }
    }

    public Object getCellEditorValue() {
        return delegate.getCellEditorValue();
    }

    public boolean isCellEditable(EventObject anEvent) {
        return delegate.isCellEditable(anEvent);
    }

    public boolean shouldSelectCell(EventObject anEvent) {
        return delegate.shouldSelectCell(anEvent);
    }

    public boolean stopCellEditing() {
        return delegate.stopCellEditing();
    }

    public void cancelCellEditing() {
        delegate.cancelCellEditing();
    }

    public void addCellEditorListener(CellEditorListener l) {
        delegate.addCellEditorListener(l);
    }

    public void removeCellEditorListener(CellEditorListener l) {
        delegate.removeCellEditorListener(l);
    }
}
