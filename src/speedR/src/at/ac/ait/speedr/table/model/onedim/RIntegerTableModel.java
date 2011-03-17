package at.ac.ait.speedr.table.model.onedim;

import at.ac.ait.speedr.table.model.RAbstractTableModel;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPInteger;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.RList;

/**
 * TableModel for R matrix objects
 * @author visnei
 */
public class RIntegerTableModel extends RAbstractTableModel {

    private int nrow;
    private int ncol;
    private int[] data;
    private String[] rownames;
    private String[] columnnames;

    public RIntegerTableModel(REXPInteger data) throws REXPMismatchException {
        int[] dim = data.dim();
        if (dim == null) {
            nrow = data.length();
            ncol = 1;
        } else if (dim.length == 1) {
            nrow = dim[0];
            ncol = 1;
        } else if (dim.length == 2) {
            nrow = dim[0];
            ncol = dim[1];
        } else {
            throw new RuntimeException("wrong dimension: " + dim.length);
        }
        this.data = data.asIntegers();
        setRowAndColumnNames(data);
        ncol += rownames != null ? 1 : 0;
    }

    private void setRowAndColumnNames(REXP data) throws REXPMismatchException {
        REXP attribute = data.getAttribute("dimnames");
        if (attribute != null && !attribute.isNull()) {
            RList dimnames = data.getAttribute("dimnames").asList();
            if (dimnames != null && !dimnames.isEmpty()) {
                rownames = dimnames.at(0).asStrings();
                if (dimnames.size() == 2) {
                    columnnames = dimnames.at(1).asStrings();
                }
            }
        }
    }

    public int getRowCount() {
        return nrow;
    }

    public int getColumnCount() {
        return ncol;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (rownames != null && columnIndex == 0) {
            return String.class;
        }

        return Integer.class;
    }

    @Override
    public String getColumnName(int column) {
        if (rownames != null && column == 0) {
            return "dimnames[[1]]";
        } else if (rownames != null) {
            column -= 1;
        }

        if (columnnames != null) {
            return columnnames[column];
        } else {
            return "" + (column + 1);
        }
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rownames != null && columnIndex == 0) {
            return rownames[rowIndex];
        }

        if (REXPInteger.isNA(data[rowIndex])) {
            return null;
        } else {
            return data[rowIndex];
        }
    }

    public String getColumnIndexCode(int columnIndex) {
        return "";
    }

    public String getRownameIndexCode(String var) {
        return null;
    }

}
