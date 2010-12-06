package at.ac.ait.speedr.table.model.twodim;

import at.ac.ait.speedr.table.RColumnIndexModel;
import javax.swing.table.AbstractTableModel;
import org.rosuda.REngine.REXPGenericVector;
import org.rosuda.REngine.REXPMismatchException;

/**
 * TableModel for R data.frame objects
 * @author visnei
 */
public class RDataFrameTableModel extends AbstractTableModel implements RColumnIndexModel{

    private REXPGenericVector data;
    private String[] rownames;
    private String[] colnames;

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
        if (columnIndex == 0) {
            return String.class;
        } else {
            columnIndex -= 1;
        }

        if (data.asList().at(columnIndex).isNumeric()) {
            return Double.class;
        } else if (data.asList().at(columnIndex).isString()) {
            return String.class;
        } else if (data.asList().at(columnIndex).isFactor()) {
            return String.class;
        } else {
            return super.getColumnClass(columnIndex);
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
            if (data.asList().at(columnIndex).isNumeric()) {
                return data.asList().at(columnIndex).asDoubles()[rowIndex];
            } else if (data.asList().at(columnIndex).isString()) {
                return data.asList().at(columnIndex).asStrings()[rowIndex];
            } else if (data.asList().at(columnIndex).isFactor()) {
                return data.asList().at(columnIndex).asFactor().at(rowIndex);
            } else {
                return "Error: null";
            }
        } catch (REXPMismatchException ex) {
            ex.printStackTrace();
            return "Error: exception";
        }

    }

    public String getColumnIndexCode(int columnIndex) {
        return "[[\"" + getColumnName(columnIndex) +"\"]]";
    }

    public String getRownameIndexCode(String var) {
        return "rownames("+var+")";
    }
}
