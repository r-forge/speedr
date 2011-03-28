package at.ac.ait.speedr.importwizard.steps;

import at.ac.ait.speedr.table.RDate;
import at.ac.ait.speedr.table.RPOSIXct;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import javax.swing.table.AbstractTableModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

/**
 *
 * @author visnei
 */
public class ImportTableModel extends AbstractTableModel {

    public enum PROP {

        HASCOLUMNNAMES, COLUMNNAMESROWINDEX, HASROWNAMES, ROWNAMESCOLUMNINDEX,
        RANGE_ROWSTART, RANGE_ROWEND, RANGE_COLSTART, RANGE_COLEND
    };
    private TreeMap<Integer, Object[]> allData;
    private HashMap<Integer, Class> classes;
    private HashMap<Integer, Integer> columnMismatchCounter;
    private static final int MAXMISMATCH = 5;
    private boolean hasColnames = false;
    private int colnamesRowIndex = 0; //the row index that contains the column headers.
    private boolean hasRownames = false;
    private int rownamesColumnIndex = 0; //the column index that contains the row names
    private int maxColumnCount = 0;
    private int rowStart = 0;
    private int rowEnd = 0;
    private int colStart = 0;
    private int colEnd = 0;
    private boolean addingRow = false;
    private boolean clearing = false;
    private boolean fireTableStructureChangedCalled = false;

    public ImportTableModel() {
        allData = new TreeMap<Integer, Object[]>();
        classes = new HashMap<Integer, Class>();
        columnMismatchCounter = new HashMap<Integer, Integer>();
    }

    public int getRowCount() {
        if (allData.size() == 0) {
            return 0;
        }

        return rowEnd - rowStart + 1;
    }

    public int getRealRowIndex(int rowIndex) {
        return rowStart + rowIndex;
    }

    public int getMaxRowCount() {
        return allData.size();
    }

    @Override
    public String getColumnName(int column) {
        if (hasRownames && column == 0) {
            return "row.names";
        }

        if (hasColnames) {
            int colIndex = getRealColumnIndex(column);

            if (hasRownames && rownamesColumnIndex == 0 && allData.get(colnamesRowIndex).length + 1 == maxColumnCount) {
                colIndex -= 1;
            }

            if (colIndex < allData.get(colnamesRowIndex).length) {
                return allData.get(colnamesRowIndex)[colIndex].toString();
            } else {
                return super.getColumnName(column);
            }
        } else {
            return super.getColumnName(column);
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (hasRownames && columnIndex == 0) {
            return String.class;
        }
        return classes.get(getRealColumnIndex(columnIndex));
    }

    private boolean checkAllColumnClasses() {
        boolean returnval = false;
        boolean flag;
        for (int columnIndex = 0; columnIndex < getColumnCount(); columnIndex++) {
            flag = true;
            if (columnMismatchCounter.get(getRealColumnIndex(columnIndex)) >= MAXMISMATCH) {
                continue;
            }
            for (int rowIndex = 0; rowIndex < getRowCount(); rowIndex++) {
                if (getValueAt(rowIndex, columnIndex) instanceof String) {
                    flag = false;
                    if (classes.get(getRealColumnIndex(columnIndex)) == Double.class) {
                        classes.put(getRealColumnIndex(columnIndex), String.class);
                        returnval = true;
                    }
                    break;
                }
            }
            if (flag) {
                returnval = true;
                classes.put(getRealColumnIndex(columnIndex), Double.class);
            }
        }

        return returnval;
    }

    private boolean checkColumnClass(int columnIndex) {
        if (columnMismatchCounter.get(getRealColumnIndex(columnIndex)) >= MAXMISMATCH) {
            return false;
        }

        boolean flag = true;

        for (int rowIndex = 0; rowIndex < getRowCount(); rowIndex++) {
            if (getValueAt(rowIndex, columnIndex) instanceof String) {
                flag = false;
                break;
            }
        }

        if (flag) {
            classes.put(getRealColumnIndex(columnIndex), Double.class);
        } else {
            classes.put(getRealColumnIndex(columnIndex), String.class);
        }

        return flag;
    }

    public int getColumnCount() {
        if (maxColumnCount == 0) {
            return 0;
        } else if (hasRownames) {
            assert colEnd - colStart + 2 <= maxColumnCount : "colEnd: " + colEnd + " colStart: " + colStart;
            return colEnd - colStart + 2;
        } else {
            assert colEnd - colStart + 1 <= maxColumnCount : "getColumnCount() returns greater than maxcolumncount";
            return colEnd - colStart + 1;
        }
    }

    private int getRealColumnIndex(int columnIndex) {
        if (hasRownames && columnIndex == 0) {
            return rownamesColumnIndex;
        }

        int realColIndex = colStart + columnIndex;

        if (hasRownames && realColIndex <= rownamesColumnIndex) {
            realColIndex -= 1;
        }

        assert realColIndex >= 0 : "realColIndex: " + realColIndex + " columnIndex: " + columnIndex;

        assert realColIndex < maxColumnCount || getColumnCount() == 0 : "realColIndex is out ouf bound.\n"
                + " realColIndex: " + realColIndex + " and column count: " + getColumnCount()
                + " columnIndex: " + columnIndex;

        return realColIndex;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        ensureRowArrayLength(rowIndex, columnIndex);

        Object oldValue = getValueAt(rowIndex, columnIndex);

        allData.get(getRealRowIndex(rowIndex))[getRealColumnIndex(columnIndex)] = aValue;

        if (oldValue != null && aValue != null) {
            if (!oldValue.equals(aValue)) {
                fireTableCellUpdated(rowIndex, columnIndex);
            }
        } else if (oldValue != null || aValue != null) {
            fireTableCellUpdated(rowIndex, columnIndex);
        }
    }

    private void ensureRowArrayLength(int rowIndex, int columnIndex) {
        int realRowIndex = getRealRowIndex(rowIndex);
        int realColumnIndex = getRealColumnIndex(columnIndex);

        if (realColumnIndex >= allData.get(realRowIndex).length) {
            Object[] oldrow = allData.get(realRowIndex);
            Object[] newrow = new Object[realColumnIndex + 1];
            System.arraycopy(oldrow, 0, newrow, 0, oldrow.length);
            allData.put(realRowIndex, newrow);
        }
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if (allData.size() == 0) {
            return null;
        }

        rowIndex = getRealRowIndex(rowIndex);
        columnIndex = getRealColumnIndex(columnIndex);

        if (columnIndex < allData.get(rowIndex).length) {
            return allData.get(rowIndex)[columnIndex];
        } else {
            return null;
        }
    }

    public void addRow(List<String[]> rowData) {
        addingRow = true;
        int index = allData.size();

        boolean colendchanged = false;

        Object[] newRow;
        for (String[] row : rowData) {
            newRow = convertRowAndSetColumnClasses(row);

            allData.put(index++, newRow);
            if (maxColumnCount < row.length) {
                maxColumnCount = row.length;
                colendchanged = true;
            }
        }

        if (colendchanged) {
            setColEnd(maxColumnCount - 1);
        }

        setRowEnd(allData.size() - 1);
        addingRow = false;
    }

    public void addRows(List<String[]> rowData) {
        addingRow = true;
        int index = allData.size();
        Object[] newRow;

        for (String[] row : rowData) {
            for (int i = 0; i < row.length; i++) {
                row[i] = StringUtils.trimToNull(row[i]);
                if (!classes.containsKey(i)) {
                    classes.put(i, String.class);
                    columnMismatchCounter.put(i, 0);
                }
            }

            newRow = new Object[row.length];
            System.arraycopy(row, 0, newRow, 0, row.length);

            allData.put(index++, newRow);
            if (maxColumnCount < row.length) {
                maxColumnCount = row.length;
                setColEnd(maxColumnCount - 1);
            }
        }

        setRowEnd(allData.size() - 1);
        addingRow = false;
    }

    public void convertColumnsToNumericIfPossible() {
        boolean fireTableStructureChanged = false;

        for (int i = 0; i < getColumnCount(); i++) {
            if (i == 0 && hasRownames) {
                continue;
            }
            try {
                checkNumeric(i);
                convertToNumeric(i, false, false);
                fireTableStructureChanged = true;
            } catch (Exception ex) {
            }
        }

        if (fireTableStructureChanged) {
            fireTableStructureChanged();
        }
    }

    private Object[] convertRowAndSetColumnClasses(String[] row) {
        boolean flag = false;
        Object[] newRow = new Object[row.length];
        Double d;
        for (int i = 0; i < row.length; i++) {
            if (!classes.containsKey(i)) {
                classes.put(i, Double.class);
                columnMismatchCounter.put(i, 0);
            }

            int mismatchcount = columnMismatchCounter.get(i);
            if (mismatchcount < MAXMISMATCH) {
                if (StringUtils.stripToNull(row[i]) == null
                        || row[i].trim().equalsIgnoreCase("NA")
                        || row[i].trim().equalsIgnoreCase("NaN")) {
                    newRow[i] = null;
                } else if (NumberUtils.isNumber(row[i])) {
                    d = NumberUtils.createDouble(row[i]);
                    newRow[i] = d;
                } else {
                    columnMismatchCounter.put(i, ++mismatchcount);
                    classes.put(i, String.class);

                    newRow[i] = row[i];
                    if (!allData.isEmpty() && classes.get(i) != String.class) {
                        flag = true;
                    }
                }
            } else {
                newRow[i] = StringUtils.stripToNull(row[i]);
            }
        }
        if (flag) {
            fireTableStructureChanged();
        }
        return newRow;
    }

    public boolean hasColumnNames() {
        return hasColnames;
    }

    public void setHasColumnNames(boolean hasColnames) {
        fireTableStructureChangedCalled = false;

        if (this.hasColnames != hasColnames) {
            boolean oldValue = this.hasColnames;
            this.hasColnames = hasColnames;
            if (!clearing) {
                if (hasColnames && rowStart <= colnamesRowIndex) {
                    setRowStart(colnamesRowIndex + 1);
                }
//                else {
//                    checkAllColumnClasses();
//                }
//                if (!fireTableStructureChangedCalled) {
                fireTableStructureChanged();
//                }
            }
            propertyChangeSupport.firePropertyChange(PROP.HASCOLUMNNAMES.name(), oldValue, this.hasColnames);
        }

        fireTableStructureChangedCalled = false;
    }

    public void setColumnNamesRowIndex(int colnamesRowIndex) {
        if (this.colnamesRowIndex != colnamesRowIndex) {
            int oldValue = this.colnamesRowIndex;
            this.colnamesRowIndex = colnamesRowIndex;
            if (hasColnames && rowStart <= this.colnamesRowIndex) {
                setRowStart(this.colnamesRowIndex + 1);
            }

            fireTableStructureChanged();

            propertyChangeSupport.firePropertyChange(PROP.COLUMNNAMESROWINDEX.name(), oldValue, this.colnamesRowIndex);
        }
    }

    public int getColumnNamesRowIndex() {
        return colnamesRowIndex;
    }

    public boolean hasRowNames() {
        return hasRownames;
    }

    public void setHasRowNames(boolean hasRownames) {
        if (this.hasRownames != hasRownames && maxColumnCount > 1) {
            boolean oldValue = this.hasRownames;
            this.hasRownames = hasRownames;
            if(this.hasRownames){
                setColEnd(this.colEnd - 1);
            }else{
                setColEnd(this.colEnd + 1);
            }
//            if (this.colEnd > 0) {
//            if (this.hasRownames && this.colEnd == maxColumnCount - 1) {
//                setColEnd(this.colEnd - 1);
//            } else if(!this.hasRownames && this.colEnd == maxColumnCount - 2) {
//                setColEnd(this.colEnd + 1);
//            }
//            }
            propertyChangeSupport.firePropertyChange(PROP.HASROWNAMES.name(), oldValue, this.hasRownames);
        }
    }

    public void setRowNamesColumnIndex(int rowNamesColumnIndex) {
        if (this.rownamesColumnIndex != rowNamesColumnIndex) {
            int oldValue = this.rownamesColumnIndex;
            this.rownamesColumnIndex = rowNamesColumnIndex;

            if (hasRownames) {
                fireTableStructureChanged();
            }
            propertyChangeSupport.firePropertyChange(PROP.ROWNAMESCOLUMNINDEX.name(), oldValue, rowNamesColumnIndex);
        }
    }

    public void setRowStart(int rowStart) {
        if (this.rowStart != rowStart) {
            int oldStartValue = this.rowStart;
            this.rowStart = rowStart;

            if (oldStartValue < this.rowStart) {
                fireTableRowsDeleted(0, this.rowStart - oldStartValue - 1);
            } else {
                fireTableRowsInserted(0, oldStartValue - rowStart - 1);
            }
//            if (!clearing && checkAllColumnClasses()) {
//                fireTableStructureChanged();
//                fireTableStructureChangedCalled = true;
//            }
            propertyChangeSupport.firePropertyChange(PROP.RANGE_ROWSTART.name(), oldStartValue, this.rowStart);
        }
    }

    public int getRowStart() {
        return rowStart;
    }

    public void setRowEnd(int rowEnd) {
        if (this.rowEnd != rowEnd) {
            int oldEndValue = this.rowEnd;
            this.rowEnd = rowEnd;

            if (oldEndValue > this.rowEnd) {
                if (hasColnames) {
                    fireTableRowsDeleted(this.rowEnd - rowStart, oldEndValue - rowStart - 1);
                } else {
                    fireTableRowsDeleted(this.rowEnd - rowStart + 1, oldEndValue - rowStart);
                }
            } else {
                if (hasColnames) {
                    fireTableRowsInserted(oldEndValue - rowStart, this.rowEnd - rowStart - 1);
                } else {
                    fireTableRowsInserted(oldEndValue - rowStart, this.rowEnd - rowStart);
                }
            }
//            if (!addingRow && !clearing && checkAllColumnClasses()) {
//                fireTableStructureChanged();
//            }
            propertyChangeSupport.firePropertyChange(PROP.RANGE_ROWEND.name(), oldEndValue, this.rowEnd);
        }
    }

    public int getRowEnd() {
        return rowEnd;
    }

    public void setColStart(int colStart) {
        if (this.colStart != colStart) {
            int oldValue = this.colStart;
            this.colStart = colStart;
            fireTableStructureChanged();
            propertyChangeSupport.firePropertyChange(PROP.RANGE_COLSTART.name(), oldValue, this.colStart);
        }

    }

    public int getColStart() {
        return colStart;
    }

    public void setColEnd(int colEnd) {
        if (this.colEnd != colEnd && colEnd < maxColumnCount) {
            int oldValue = this.colEnd;
            this.colEnd = colEnd;
            fireTableStructureChanged();
            propertyChangeSupport.firePropertyChange(PROP.RANGE_COLEND.name(), oldValue, this.colEnd);
        }
    }

    public int getColEnd() {
        return colEnd;
    }

    public int getMaxColumnCount() {
        return maxColumnCount;
    }

    public void clearAll() {
        clearing = true;

        setHasColumnNames(false);
        setColumnNamesRowIndex(0);

        setHasRowNames(false);
        setRowNamesColumnIndex(0);

        setRowStart(0);
        setRowEnd(0);

        setColStart(0);
        setColEnd(0);
        maxColumnCount = 0;
        allData.clear();
        classes.clear();
        columnMismatchCounter.clear();

        clearing = false;
    }

    public void convertToNumeric(int columnIndex) throws Exception {
        convertToNumeric(columnIndex, true, true);
    }

    private void convertToNumeric(int columnIndex, boolean checkfirstnumeric, boolean fireTableStructureChanged) throws Exception {
        if (getColumnClass(columnIndex) != Double.class) {
            if (checkfirstnumeric) {
                checkNumeric(columnIndex);
            }
            String val;
            for (int rowIndex = 0; rowIndex < getRowCount(); rowIndex++) {
                if (getValueAt(rowIndex, columnIndex) == null){
                    allData.get(getRealRowIndex(rowIndex))[getRealColumnIndex(columnIndex)] = null;
                }else{
                    val = StringUtils.trimToNull(getValueAt(rowIndex, columnIndex).toString());
                    if(val == null || val.equalsIgnoreCase("NA")){
                        allData.get(getRealRowIndex(rowIndex))[getRealColumnIndex(columnIndex)] = null;
                    }else{
                        allData.get(getRealRowIndex(rowIndex))[getRealColumnIndex(columnIndex)] = NumberUtils.createDouble(val);
                    }
                }
            }

            classes.put(getRealColumnIndex(columnIndex), Double.class);
            if (fireTableStructureChanged) {
                fireTableStructureChanged();
            }
        }
    }

    private void checkNumeric(int columnIndex) throws Exception {
        StringBuilder err = new StringBuilder();
        err.append("Non-numeric values found in column ").append(getColumnName(columnIndex)).append("\nRows containing non-numeric values:\n");

        int failcount = 0;
        String val;
        for (int i = 0; i < getRowCount(); i++) {
            if (getValueAt(i, columnIndex) != null) {
                val = StringUtils.trimToNull(getValueAt(i, columnIndex).toString());

                if (val != null && !val.equalsIgnoreCase("NA") && !NumberUtils.isNumber(val)) {
                    failcount++;
                    err.append(i + 1).append(",");
                    if (failcount == MAXMISMATCH) {
                        err.deleteCharAt(err.length() - 1);
                        err.append("...");
                        break;
                    }
                }
            }
        }

        if (failcount > 0) {
            columnMismatchCounter.put(getRealColumnIndex(columnIndex), failcount);
            throw new Exception(err.toString());
        }
    }

    public boolean containsNonNumeric(int columnIndex) {
        for (int i = 0; i < getRowCount(); i++) {
            if (getValueAt(i, columnIndex) != null
                    && !StringUtils.isBlank(getValueAt(i, columnIndex).toString())
                    && !NumberUtils.isNumber(getValueAt(i, columnIndex).toString())) {
                return true;
            }
        }
        return false;
    }

    public void convertToText(int columnIndex, List<String> columndata) {
        if (columndata.size() != allData.size()) {
            throw new IllegalArgumentException("The size of the list containing column data is not equal to allData size.");
        }

        for (int i = 0; i < allData.size(); i++) {
            allData.get(i)[getRealColumnIndex(columnIndex)] = columndata.get(i);
        }

        if (getColumnClass(columnIndex) != String.class) {
            classes.put(getRealColumnIndex(columnIndex), String.class);
            fireTableStructureChanged();
        }
    }

    public void convertToDate(int columnIndex, String[] pattern) throws Exception {
        if (getColumnClass(columnIndex) == RDate.class) {
            return;
        } else if (getColumnClass(columnIndex) == RPOSIXct.class) {

            for (int i = 0;
                    i < getRowCount();
                    i++) {
                if (getValueAt(i, columnIndex) != null) {
                    allData.get(getRealRowIndex(i))[getRealColumnIndex(columnIndex)] = new RDate((Date) getValueAt(i, columnIndex));
                }
            }

            classes.put(getRealColumnIndex(
                    columnIndex), RDate.class);
            fireTableStructureChanged();
            return;
        }

        ArrayList<Date> checkDate = checkDate(columnIndex, pattern);

        for (int i = 0;
                i < checkDate.size();
                i++) {
            if (checkDate.get(i) == null) {
                allData.get(getRealRowIndex(i))[getRealColumnIndex(columnIndex)] = null;
            } else {
                allData.get(getRealRowIndex(i))[getRealColumnIndex(columnIndex)] = new RDate(checkDate.get(i));
            }
        }

        classes.put(getRealColumnIndex(columnIndex), RDate.class);
        fireTableStructureChanged();
    }

    public void convertToPOSIXct(int columnIndex, String[] pattern) throws Exception {
        if (getColumnClass(columnIndex) == RPOSIXct.class) {
            return;
        } else if (getColumnClass(columnIndex) == RDate.class) {
            for (int i = 0; i < getRowCount(); i++) {
                if (getValueAt(i, columnIndex) != null) {
                    allData.get(getRealRowIndex(i))[getRealColumnIndex(columnIndex)] = new RPOSIXct((Date) getValueAt(i, columnIndex));
                }
            }

            classes.put(getRealColumnIndex(columnIndex), RPOSIXct.class);
            fireTableStructureChanged();
            return;
        }

        ArrayList<Date> checkDate = checkDate(columnIndex, pattern);

        for (int i = 0; i < checkDate.size(); i++) {
            if (checkDate.get(i) == null) {
                allData.get(getRealRowIndex(i))[getRealColumnIndex(columnIndex)] = null;
            } else {
                allData.get(getRealRowIndex(i))[getRealColumnIndex(columnIndex)] = new RPOSIXct(checkDate.get(i));
            }
        }

        classes.put(getRealColumnIndex(columnIndex), RPOSIXct.class);
        fireTableStructureChanged();
    }

    private ArrayList<Date> checkDate(int columnIndex, String[] parsePatterns) throws Exception {
        SimpleDateFormat parser = new SimpleDateFormat();
        parser.setLenient(false);
        ParsePosition pos = new ParsePosition(0);

        StringBuilder err = new StringBuilder();

        err.append("Parse error on rows: ");

        ArrayList<Date> dates = new ArrayList<Date>();
        int failcount = 0;
        for (int i = 0; i < getRowCount() && failcount < MAXMISMATCH; i++) {
            if (getValueAt(i, columnIndex) != null
                    && !StringUtils.isBlank(getValueAt(i, columnIndex).toString())) {
                try {
                    dates.add(parseDate(getValueAt(i, columnIndex).toString().trim(), parsePatterns, parser, pos));
                } catch (ParseException ex) {
                    failcount++;
                    err.append(i + 1).append(',');
                }
            } else {
                dates.add(null);
            }
        }

        if (failcount > 0) {
            if (failcount == MAXMISMATCH) {
                err.append("...");
            } else {
                err.deleteCharAt(err.length() - 1);
            }
            throw new Exception(err.toString());
        }

        return dates;
    }

    private Date parseDate(String str, String[] parsePatterns, SimpleDateFormat parser, ParsePosition pos) throws ParseException {
        for (int i = 0; i < parsePatterns.length; i++) {

            String pattern = parsePatterns[i];
            parser.applyPattern(pattern);
            pos.setIndex(0);

            Date date = parser.parse(str, pos);

            if (date != null) {
                return date;
            }
        }
        throw new ParseException("Unable to parse the date: " + str, -1);
    }
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    /**
     * Add PropertyChangeListener.
     *
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Remove PropertyChangeListener.
     *
     * @param listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }
}
