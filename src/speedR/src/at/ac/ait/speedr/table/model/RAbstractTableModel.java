package at.ac.ait.speedr.table.model;

import javax.swing.table.AbstractTableModel;

/**
 *
 * @author visnei
 */
public abstract class RAbstractTableModel extends AbstractTableModel{
    abstract public String getColumnIndexCode(int columnIndex);
    abstract public String getRownameIndexCode(String var);
}
