/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.ac.ait.speedr.table;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.UIManager;
import org.jdesktop.swingx.table.DatePickerCellEditor;

/**
 *
 * @author visnei
 */
public class RDateTimePickerCellEditor extends DatePickerCellEditor{

    public RDateTimePickerCellEditor() {
        this(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    private RDateTimePickerCellEditor(DateFormat dateFormat) {
        this.dateFormat = dateFormat != null ? dateFormat :
            DateFormat.getDateInstance();
        datePicker = new DateTimePicker(dateFormat);
        // default border crushes the editor/combo
        datePicker.getEditor().setBorder(
                BorderFactory.createEmptyBorder(0, 1, 0, 1));
        // should be fixed by j2se 6.0
        datePicker.setFont(UIManager.getDefaults().getFont("TextField.font"));
        if (dateFormat != null) {
            datePicker.setFormats(dateFormat);
        }
        datePicker.addActionListener(getPickerActionListener());
    }

    @Override
    public RPOSIXct getCellEditorValue() {
        return new RPOSIXct(super.getCellEditorValue());
    }

}
