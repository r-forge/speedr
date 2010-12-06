package at.ac.ait.speedr.table;

import java.awt.Component;
import java.text.NumberFormat;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author visnei
 */
public class RTableCellRenderer implements TableCellRenderer {

    private static NumberFormat nf;
    private TableCellRenderer delegate;

    public RTableCellRenderer(TableCellRenderer defaultRenderer) {
        this.delegate = defaultRenderer;
        if (nf == null) {
            nf = NumberFormat.getInstance();
            nf.setGroupingUsed(false);
            nf.setMaximumFractionDigits(9);
        }
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = (JLabel) delegate.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (value == null) {
            label.setText("NA");
        } else if (value instanceof String && ((String)value).equals("")) {
            label.setText("NA");
        } else if (value instanceof Double) {
            label.setText(nf.format(value));
        }

        return label;
    }
}
