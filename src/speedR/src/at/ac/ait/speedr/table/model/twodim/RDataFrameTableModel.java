package at.ac.ait.speedr.table.model.twodim;

import at.ac.ait.speedr.table.RDate;
import at.ac.ait.speedr.table.RPOSIXct;
import at.ac.ait.speedr.table.model.RAbstractTableModel;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.rosuda.REngine.REXPDouble;
import org.rosuda.REngine.REXPFactor;
import org.rosuda.REngine.REXPGenericVector;
import org.rosuda.REngine.REXPInteger;
import org.rosuda.REngine.REXPLogical;
import org.rosuda.REngine.REXPMismatchException;

/**
 * TableModel for R data.frame objects
 * @author visnei
 */
public class RDataFrameTableModel extends RAbstractTableModel {

    private REXPGenericVector data;
    private String[] rownames;
    private String[] colnames;
    private Class[] colClasses;

    public RDataFrameTableModel(REXPGenericVector data) throws REXPMismatchException {
        this.data = data;
        rownames = data.getAttribute("row.names").asStrings();
        colnames = data.getAttribute("names").asStrings();
    }

    public int getRowCount() {
        return rownames.length;
    }

    public int getColumnCount() {
        return colnames.length + 1;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (colClasses == null) {
            initColClasses();
        }
        if (columnIndex == 0) {
            return String.class;
        } else {
            columnIndex -= 1;
        }

        return colClasses[columnIndex];
    }

    private void initColClasses() {
        colClasses = new Class[data.asList().size()];

        for (int i = 0; i < colClasses.length; i++) {
            try {
                if (data.asList().at(i).hasAttribute("class")
                        && data.asList().at(i).getAttribute("class").asString().equals("Date")) {

                    colClasses[i] = RDate.class;
                    continue;
                } else if (data.asList().at(i).hasAttribute("class")
                        && data.asList().at(i).getAttribute("class").asString().equals("POSIXct")) {

                    colClasses[i] = RPOSIXct.class;
                    continue;
                }
            } catch (REXPMismatchException ex) {
                Logger.getLogger(RDataFrameTableModel.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (data.asList().at(i).isFactor()) {
                colClasses[i] = REXPFactor.class;
            } else if (data.asList().at(i).isNumeric()) {
                if (data.asList().at(i).isInteger()) {
                    colClasses[i] = REXPInteger.class;
                } else {
                    colClasses[i] = REXPDouble.class;
                }
            } else if (data.asList().at(i).isString()) {
                colClasses[i] = String.class;
            } else if (data.asList().at(i).isLogical()) {
                colClasses[i] = Boolean.class;
            } else {
                colClasses[i] = super.getColumnClass(i);
            }
        }
    }

    @Override
    public String getColumnName(int column) {
        if (column == 0) {
            return "row.names";
        } else {
            return colnames[column - 1];
        }
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return rownames[rowIndex];
        } else {
            columnIndex -= 1;
        }

        try {
            if (colClasses[columnIndex] == RDate.class) {
                return new RDate((long) data.asList().at(columnIndex).asDoubles()[rowIndex]);
            } else if (colClasses[columnIndex] == RPOSIXct.class) {
                return new RPOSIXct((long) data.asList().at(columnIndex).asDoubles()[rowIndex]);
            } else if (colClasses[columnIndex] == Boolean.class) {
                if (REXPLogical.isNA(data.asList().at(columnIndex).asBytes()[columnIndex])) {
                    return null;
                } else {
                    return data.asList().at(columnIndex).asBytes()[columnIndex] == REXPLogical.TRUE;
                }
            } else if (colClasses[columnIndex] == REXPFactor.class) {
                return data.asList().at(columnIndex).asFactor().at(rowIndex);
            } else if (colClasses[columnIndex] == REXPInteger.class) {
                if (REXPInteger.isNA(data.asList().at(columnIndex).asIntegers()[rowIndex])) {
                    return null;
                } else {
                    return data.asList().at(columnIndex).asIntegers()[rowIndex];
                }
            } else if (colClasses[columnIndex] == REXPDouble.class) {
                if (REXPDouble.isNA(data.asList().at(columnIndex).asDoubles()[rowIndex])) {
                    return null;
                } else {
                    return data.asList().at(columnIndex).asDoubles()[rowIndex];
                }
            } else if (colClasses[columnIndex] == String.class) {
                return data.asList().at(columnIndex).asStrings()[rowIndex];
            } else {
                Logger.getLogger(RDataFrameTableModel.class.getName()).log(Level.WARNING, "Unknown column class type: {0}", getColumnClass(columnIndex));
                return "Error: null";
            }
        } catch (REXPMismatchException ex) {
            Logger.getLogger(RDataFrameTableModel.class.getName()).log(Level.WARNING, null, ex);
            return "Error: exception";
        }

    }

    public String getColumnIndexCode(int columnIndex) {
        return "[[\"" + getColumnName(columnIndex) + "\"]]";
    }

    public String getRownameIndexCode(String var) {
        return "rownames(" + var + ")";
    }
}
