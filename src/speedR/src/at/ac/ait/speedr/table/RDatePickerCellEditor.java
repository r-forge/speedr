package at.ac.ait.speedr.table;

import java.text.SimpleDateFormat;
import org.jdesktop.swingx.table.DatePickerCellEditor;

/**
 *
 * @author visnei
 */
public class RDatePickerCellEditor extends DatePickerCellEditor{

    public RDatePickerCellEditor() {
        super(new SimpleDateFormat("yyyy-MM-dd"));
    }

    @Override
    public RDate getCellEditorValue() {
        return new RDate(super.getCellEditorValue());
    }

}
