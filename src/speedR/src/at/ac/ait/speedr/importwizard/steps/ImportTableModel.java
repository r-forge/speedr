package at.ac.ait.speedr.importwizard.steps;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

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
            return Object.class;
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

        boolean returnval = false;
        boolean flag = true;

        for (int rowIndex = 0; rowIndex < getRowCount(); rowIndex++) {
            if (getValueAt(rowIndex, columnIndex) instanceof String) {
                flag = false;
                break;
            }
        }

        if (flag) {
            returnval = true;
            classes.put(getRealColumnIndex(columnIndex), Double.class);
        } else {
            classes.put(getRealColumnIndex(columnIndex), String.class);
        }

        return returnval;
    }

//    private boolean checkColumnClass(int columnIndex, int fromRow, int toRow) {
//        if (columnMismatchCounter.get(getRealColumnIndex(columnIndex)) >= MAXMISMATCH) {
//            return false;
//        }
//
//        boolean returnval = false;
//        boolean flag = true;
//
//        for (int rowIndex = fromRow; rowIndex < toRow; rowIndex++) {
//            if (getValueAt(rowIndex, columnIndex) instanceof String) {
//                flag = false;
//                break;
//            }
//        }
//
//        if (flag) {
//            returnval = true;
//            classes.put(getRealColumnIndex(columnIndex), Double.class);
//        } else {
//            classes.put(getRealColumnIndex(columnIndex), String.class);
//        }
//
//        return returnval;
//    }
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

        assert realColIndex < getColumnCount() || getColumnCount() == 0 : "realColIndex is out ouf bound.\n"
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

        if (aValue != null && aValue instanceof String) {
            try {
                aValue = Double.parseDouble(aValue.toString());
                allData.get(getRealRowIndex(rowIndex))[getRealColumnIndex(columnIndex)] = aValue;
                if (checkColumnClass(columnIndex)) {
                    fireTableStructureChanged();
                }
            } catch (NumberFormatException ex) {
                allData.get(getRealRowIndex(rowIndex))[getRealColumnIndex(columnIndex)] = aValue;
            }
        } else {
            allData.get(getRealRowIndex(rowIndex))[getRealColumnIndex(columnIndex)] = aValue;
        }
        if (oldValue != null && aValue != null) {
            if (!oldValue.equals(aValue)) {
                fireTableCellUpdated(rowIndex, columnIndex);
            }
        }else if(oldValue != null || aValue != null){
            fireTableCellUpdated(rowIndex, columnIndex);
        }
    }

    private void ensureRowArrayLength(int rowIndex,int columnIndex){
        int realRowIndex = getRealRowIndex(rowIndex);
        int realColumnIndex = getRealColumnIndex(columnIndex);

        if(realColumnIndex >= allData.get(realRowIndex).length){
            Object[] oldrow = allData.get(realRowIndex);
            Object[] newrow = new Object[realColumnIndex+1];
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

        Object[] newRow;
        for (String[] row : rowData) {
            newRow = convertRowAndSetColumnClasses(row);

            allData.put(index++, newRow);
            if (maxColumnCount < row.length) {
                maxColumnCount = row.length;
                setColEnd(maxColumnCount - 1);
            }
        }

        setRowEnd(allData.size() - 1);
        addingRow = false;
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
                try {
                    if (row[i] == null || row[i].trim() == ""
                            || row[i].trim().equalsIgnoreCase("NA")
                            || row[i].trim().equalsIgnoreCase("NaN")) {
                        newRow[i] = null;
                    } else {
                        d = Double.parseDouble(row[i]);
                        newRow[i] = d;
                    }
                } catch (NumberFormatException ex) {
                    columnMismatchCounter.put(i, ++mismatchcount);
                    if (classes.get(i) != String.class) {
                        classes.put(i, String.class);
                        flag = true;
                    }
                    newRow[i] = row[i];
                }
            } else {
                newRow[i] = row[i];
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
        if (this.hasColnames != hasColnames) {
            boolean oldValue = this.hasColnames;
            this.hasColnames = hasColnames;
            if (!clearing) {
                if (hasColnames && rowStart <= colnamesRowIndex) {
                    setRowStart(colnamesRowIndex + 1);
                } else {
                    checkAllColumnClasses();
                }
                fireTableStructureChanged();
            }
            propertyChangeSupport.firePropertyChange(PROP.HASCOLUMNNAMES.name(), oldValue, this.hasColnames);
        }
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
            if (colEnd > 0) {
                if (this.hasRownames && colEnd + 1 == maxColumnCount) {
                    setColEnd(colEnd - 1);
                } else {
                    setColEnd(colEnd + 1);
                }
            }
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
            if (!clearing && checkAllColumnClasses()) {
                fireTableStructureChanged();
            }
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
            if (!addingRow && !clearing && checkAllColumnClasses()) {
                fireTableStructureChanged();
            }
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

//        hasColnames = false;
//        hasRownames = false;
//        rownamesColumnIndex = 0;
//        colStart = 0;
//        colEnd = 0;
//        rowStart = 0;
//        rowEnd = 0;
//        maxColumnCount = 0;
//        allData.clear();
//        classes.clear();
//        columnMismatchCounter.clear();
//        fireTableStructureChanged();
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

    public static void main(String[] args) throws Exception {
//        final NewJFrame f = new NewJFrame();
//        java.awt.EventQueue.invokeLater(new Runnable() {
//
//            public void run() {
//                f.setVisible(true);
//            }
//        });
//        final ImportTableModel model = new ImportTableModel();
        //        f.setModel(model);
//        String readFileToString = FileUtils.readFileToString(new File("C:/Documents and Settings/visnei/My Documents/iris2.txt"));
//
//        CSVReader csvReader =
//                new CSVReader(new InputStreamReader(new FileInputStream("E:/projects/speedR/sampledata/t1.txt")), ' ', '\'');
//        String[] next;
//        while ((next = csvReader.readNext()) != null) {
//            System.out.println(next.length);
//        }
//        csvReader.setSkipEmptyRecords(true);
//        ArrayList<String[]> rowlist = new ArrayList<String[]>();
//        String[] record;
//        while (csvReader.readRecord()) {
//            record = csvReader.getValues();
//            rowlist.add(record);
//        }
//        java.awt.EventQueue.invokeLater(new Runnable() {
//
//            public void run() {
//                model.addRow(rowlist);
//                model.setHasColumnNames(true);
//                model.setHasRowNames(true);
//                model.setColumnNamesRowIndex(2);
//                System.out.println("xxx: " + model.getRowStart());
//                writeTable(model);
////                model.setRowNamesColumnIndex(1);
//            }
//        });
//        fillModel(model);
//        model.clearAll();
//        fillModel(model);
//        model.setHasColumnNames(true);
//        writeTable(model);
//        model.setColumnNamesRowIndex(0);
//        model.setHasRowNames(true);
//        model.setRowNamesColumnIndex(1);
//        model.setColEnd(2);
//        model.setRowStart(2);
//        model.setValueAt("25", 4, 4);
//
//        System.out.println(model.getColumnClass(4));
//        model.addRow(rowlist);
//        writeTable(model);
    }

    private static void fillModel(ImportTableModel model) {
        ArrayList<String[]> rowlist = new ArrayList<String[]>();
        String[] row = {"A1", "B1", "C1", "D1"};

        rowlist.add(row);

        row = new String[]{"1", "2", "3", "4", "5"};
        rowlist.add(row);

        row = new String[]{"6", "7", "8", "9", "10"};
        rowlist.add(row);

        row = new String[]{"11", "12", "13", "14", "15"};
        rowlist.add(row);

        row = new String[]{"16", "17", "18", "19", "20"};
        rowlist.add(row);

        row = new String[]{"21", "22", "23", "24", "ff"};
        rowlist.add(row);

        row = new String[]{"26", "27", "28", "29", "30"};
        rowlist.add(row);

        model.addRow(rowlist);
    }

    private static void writeTable(TableModel model) {
        for (int i = 0; i < model.getColumnCount(); i++) {
            System.out.print(model.getColumnName(i) + "\t");
        }

        System.out.println("");
        for (int i = 0; i < model.getRowCount(); i++) {
            for (int j = 0; j < model.getColumnCount(); j++) {
                System.out.print(model.getValueAt(i, j) + "\t");
            }
            System.out.println("");
        }
    }
}
