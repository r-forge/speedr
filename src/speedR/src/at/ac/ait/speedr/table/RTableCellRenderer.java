package at.ac.ait.speedr.table;

import java.awt.Component;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
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

            if (nf instanceof DecimalFormat) {
                DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance();
                dfs.setDecimalSeparator('.');
                ((DecimalFormat) nf).setDecimalFormatSymbols(dfs);
//                ((DecimalFormat) nf).setDecimalSeparatorAlwaysShown(true);
//                ((DecimalFormat) nf).setMinimumFractionDigits(1);
            }
        }
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = (JLabel) delegate.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (value == null) {
            label.setText("NA");
        } else if (value instanceof Double || value instanceof Integer) {
            label.setText(nf.format(value));
        } else if (value instanceof Boolean) {
            label.setText(((Boolean) value).toString());
        }

        return label;
    }
}
