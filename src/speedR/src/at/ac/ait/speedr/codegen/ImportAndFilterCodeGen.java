package at.ac.ait.speedr.codegen;

import at.ac.ait.speedr.codegen.FilterExpressionRCodeGen.rcode_return;
import at.ac.ait.speedr.importwizard.steps.DataImportPanelUserActionListener;
import at.ac.ait.speedr.table.RDate;
import at.ac.ait.speedr.table.RPOSIXct;
import at.ac.arcs.tablefilter.events.FilterEvent;
import at.ac.arcs.tablefilter.events.FilterListener;
import at.ac.arcs.tablefilter.filtermodel.DateFilterDevice;
import at.ac.arcs.tablefilter.filtermodel.NumericFilterDevice;
import at.ac.arcs.tablefilter.filtermodel.info.FilterColumnInfo;
import at.ac.arcs.tablefilter.filtermodel.info.FilterInfo;
import at.ac.arcs.tablefilter.filtermodel.info.FilterRowInfo;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import org.ait.table.filter.FilterExpressionLexer;
import org.ait.table.filter.FilterExpressionParser;
import org.ait.table.filter.FilterExpressionParser.formula_return;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeNodeStream;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;

/**
 *
 * @author visnei
 */
public class ImportAndFilterCodeGen implements FilterListener, DataImportPanelUserActionListener {

    private static final Logger logger = Logger.getLogger(ImportAndFilterCodeGen.class.getName());
    private static StringTemplateGroup filter_stg;
    private static StringTemplateGroup import_stg;
    private static StringTemplateGroup importandfilter_stg;
    private EventListenerList listenerList = new EventListenerList();
    private ChangeEvent changeEvent;
    private StringTemplate importanyfunctionST;
    private StringTemplate fileST;
    private StringTemplate delimiterST;
    private StringTemplate textqualifierST;
    private StringTemplate rowstartST;
    private StringTemplate rowendST;
    private StringTemplate colstartST;
    private StringTemplate colendST;
    private StringTemplate hasRowNamesST;
    private StringTemplate rowNamesColumnIndexST;
    private StringTemplate hasColumnNamesST;
    private StringTemplate columnNamesRowIndexST;
    private StringTemplate colClassesST;
    private String variablename = "temp";
    private String filterFunctionName = "temp_filter";
    private TableModel model;
    private String importcode = "";
    private String filtercode = "";
    private boolean hasRowNames = false;
    private int rowNamesColumnIndex = 1;
    private int rowstartIndex = 1;
    private int rowendIndex = 0;
    private int colstartIndex = 1;
    private int colendIndex = 0;
    private String[] colClasses;
    
    // rowIndex -> columnIndex + values
    private HashMap<Integer, HashMap<Integer, Object>> cellUpdates =
            new HashMap<Integer, HashMap<Integer, Object>>();

    public ImportAndFilterCodeGen() throws IOException {
        if (importandfilter_stg == null) {
            InputStreamReader r = new InputStreamReader(
                    ImportAndFilterCodeGen.class.getResourceAsStream("/at/ac/ait/speedr/codegen/template/ImportCodeGen.stg"));
            import_stg = new StringTemplateGroup(r);
            r.close();

            r = new InputStreamReader(
                    ImportAndFilterCodeGen.class.getResourceAsStream("/at/ac/ait/speedr/codegen/template/RCodeGen.stg"));
            filter_stg = new StringTemplateGroup(r);
            r.close();

            r = new InputStreamReader(
                    ImportAndFilterCodeGen.class.getResourceAsStream("/at/ac/ait/speedr/codegen/template/ImportAndFilterCodeGen.stg"));
            importandfilter_stg = new StringTemplateGroup(r);
            r.close();
        }
    }

    private void setFiltercode() {
        importanyfunctionST = import_stg.getInstanceOf("importany");
        importanyfunctionST.setAttribute("varname", variablename);

        if (fileST != null) {
            importanyfunctionST.setAttribute("params", fileST);
        }
        if (delimiterST != null) {
            importanyfunctionST.setAttribute("params", delimiterST);
        }
        if (textqualifierST != null) {
            importanyfunctionST.setAttribute("params", textqualifierST);
        }
        if (rowstartST != null) {
            importanyfunctionST.setAttribute("params", rowstartST);
        }
        if (rowendST != null) {
            importanyfunctionST.setAttribute("params", rowendST);
        }
        if (colstartST != null) {
            importanyfunctionST.setAttribute("params", colstartST);
        }
        if (colendST != null) {
            importanyfunctionST.setAttribute("params", colendST);
        }
        if (hasRowNamesST != null) {
            importanyfunctionST.setAttribute("params", hasRowNamesST);
        }
        if (rowNamesColumnIndexST != null) {
            importanyfunctionST.setAttribute("params", rowNamesColumnIndexST);
        }
        if (hasColumnNamesST != null) {
            importanyfunctionST.setAttribute("params", hasColumnNamesST);
        }
        if (columnNamesRowIndexST != null) {
            importanyfunctionST.setAttribute("params", columnNamesRowIndexST);
        }
        if (colClassesST != null) {
            importanyfunctionST.setAttribute("params", colClassesST);
        }

        importcode = importanyfunctionST.toString();
    }

    public String getImportAndFilterCode() {
        setFiltercode();
        StringTemplate t = importandfilter_stg.getInstanceOf("importandfilter");
        t.setAttribute("params", importcode);
        StringTemplate _cellUpdates = generateCellUpdates();
        if (_cellUpdates != null) {
            t.setAttribute("params", _cellUpdates);
        }
        t.setAttribute("params", filtercode);
        if (filtercode != null && !filtercode.equals("")) {
            t.setAttribute("params", variablename + "<-" + getFilterFunctionName() + "(" + variablename + ")");
        }
        return t.toString();
    }

    public void filterChanged(FilterEvent event) {
        StringTemplate filterfunction = filter_stg.getInstanceOf("filterfunction");
        filterfunction.setAttribute("functionname", getFilterFunctionName());
        FilterInfo filterInfo = event.getFilterInfo();

        FilterRowInfo[] filterRows = filterInfo.getFilterRows();

        for (FilterRowInfo filterRowInfo : filterRows) {
            StringTemplate filterlevel = filter_stg.getInstanceOf("filterlevel");

            List<FilterColumnInfo> columns = filterRowInfo.getColumns();
            if (columns.isEmpty()) {
                filterfunction.setAttribute("filterlevels", "(TRUE)");
                continue;
            }
            for (FilterColumnInfo filterColumnInfo : columns) {
                String exp = filterColumnInfo.getExpression();
                try {
                    // create a CharStream
                    ANTLRStringStream input = new ANTLRStringStream(exp);

                    // create a lexer that feeds off of input CharStream
                    FilterExpressionLexer lexer = new FilterExpressionLexer(input);
                    // create a buffer of tokens pulled from the lexer
                    CommonTokenStream tokens = new CommonTokenStream(lexer);
                    // create a parser that feeds off the tokens buffer
                    FilterExpressionParser parser = new FilterExpressionParser(tokens);

                    lexer.setNumberColumn(filterInfo.getFilterDevices()[filterColumnInfo.getColumn()] instanceof NumericFilterDevice);

                    parser.setNumberColumn(
                            filterInfo.getFilterDevices()[filterColumnInfo.getColumn()] instanceof NumericFilterDevice);

                    parser.setDateColumn(
                            filterInfo.getFilterDevices()[filterColumnInfo.getColumn()] instanceof DateFilterDevice);

                    // begin parsing at rule formula
                    formula_return formula = parser.formula();

                    /** WALK RESULTING TREE **/
                    // get tree from parser
                    CommonTree tree = (CommonTree) formula.getTree();

                    // Create a tree node stream from resulting tree
                    CommonTreeNodeStream nodes = new CommonTreeNodeStream(tree);
                    nodes.setTokenStream(tokens);

                    FilterExpressionRCodeGen rcodegen = new FilterExpressionRCodeGen(nodes);

                    int col = filterColumnInfo.getColumn();
                    if (col == 0 && model.getColumnName(0).equals("row.names")) {
                        rcodegen.setHasRowNames(true);
                        rcodegen.setColumnIndex("rownames(x)");
                    } else {
                        rcodegen.setDataframe(true);
                        rcodegen.setDataframeColumnIndex(model.getColumnName(col));
                        if (model.getColumnClass(col) == RPOSIXct.class) {
                            rcodegen.setPOSIXct(true);
                        }
                    }

                    rcodegen.setTemplateLib(filter_stg);

                    rcode_return rcode = rcodegen.rcode();
                    filterlevel.setAttribute("selectors", rcode.st);

                } catch (RecognitionException recognitionException) {
                    Logger.getLogger(FilterFunctionCodeGen.class.getName()).log(Level.SEVERE, "Can't parse the expression: " + exp, recognitionException);
                }
            }
            filterfunction.setAttribute("filterlevels", filterlevel);
        }

        filtercode = filterfunction.toString();
        fireStateChanged();
    }

    private String getFilterFunctionName() {
        return filterFunctionName;
    }

    private void setFilterFunctionName() {
        filterFunctionName = filterFunctionName = variablename + "_filter";
    }

    public void setFile(String path) {
        fileST = import_stg.getInstanceOf("file");
        fileST.setAttribute("value", path);
    }

    public void separatorChanged(String newvalue) {
        this.delimiterST = import_stg.getInstanceOf("separator");
        this.delimiterST.setAttribute("value", newvalue);

        cellUpdates.clear();

        fireStateChanged();
    }

    public void quoteChanged(String newvalue) {
        cellUpdates.clear();
        if (newvalue == null) {
            textqualifierST = null;
        } else {
            textqualifierST = import_stg.getInstanceOf("quote");
            textqualifierST.setAttribute("value", newvalue);
            fireStateChanged();
        }
    }

    public void rowStartChanged(int newvalue) {
        rowstartIndex = newvalue;
        this.rowstartST = import_stg.getInstanceOf("rowstart");
        this.rowstartST.setAttribute("value", newvalue);
        fireStateChanged();
    }

    public void rowEndChange(int newvalue) {
        rowendIndex = newvalue;
        this.rowendST = import_stg.getInstanceOf("rowend");
        this.rowendST.setAttribute("value", newvalue);
        fireStateChanged();
    }

    public void colStartChanged(int newvalue) {
        colstartIndex = newvalue;
        this.colstartST = import_stg.getInstanceOf("colstart");
        this.colstartST.setAttribute("value", newvalue);
        fireStateChanged();
    }

    public void colEndChange(int newvalue) {
        colendIndex = newvalue;
        this.colendST = import_stg.getInstanceOf("colend");
        this.colendST.setAttribute("value", newvalue);
        fireStateChanged();
    }

    public void hasColumnNamesChanged(Boolean newvalue) {
        this.hasColumnNamesST = import_stg.getInstanceOf("hasColumnNames");
        this.hasColumnNamesST.setAttribute("value", newvalue.toString().toUpperCase());
        fireStateChanged();
    }

    public void columnNamesRowIndexChanged(int newvalue) {
        this.columnNamesRowIndexST = import_stg.getInstanceOf("columnNamesRowIndex");
        if (logger.isLoggable(Level.INFO)) {
            logger.log(Level.INFO, "new columnNamesRowIndex = {0}", newvalue);
        }
        this.columnNamesRowIndexST.setAttribute("value", newvalue);
        fireStateChanged();
    }

    public void hasRowNamesChanged(Boolean newvalue) {
        hasRowNames = newvalue;
        this.hasRowNamesST = import_stg.getInstanceOf("hasRowNames");
        this.hasRowNamesST.setAttribute("value", newvalue.toString().toUpperCase());
        fireStateChanged();
    }

    public void rowNamesNoChanged(int newvalue) {
        this.rowNamesColumnIndex = newvalue;
        this.rowNamesColumnIndexST = import_stg.getInstanceOf("rowNamesColumnIndex");
        this.rowNamesColumnIndexST.setAttribute("value", newvalue);
        fireStateChanged();
    }

    public void variableNameChanged(String newvalue) {
        String oldFilterFunctionName = getFilterFunctionName();

        variablename = newvalue;
        setFilterFunctionName();

        String newFilterFunctionName = getFilterFunctionName();

        filtercode = filtercode.replaceFirst(oldFilterFunctionName, newFilterFunctionName);

        fireStateChanged();
    }

    public void tableCellValueChanged(Object aValue, int rowIndex, int columnIndex) {
        Integer realRowIndex = rowstartIndex + rowIndex;
        Integer realColumnIndex;
        if (hasRowNames && columnIndex == 0) {
            realColumnIndex = rowNamesColumnIndex;
        } else {
            realColumnIndex = colstartIndex + columnIndex;
        }
        if (!cellUpdates.containsKey(realRowIndex)) {
            cellUpdates.put(realRowIndex, new HashMap<Integer, Object>());
        }
        cellUpdates.get(realRowIndex).put(realColumnIndex, aValue);
        fireStateChanged();
    }

    public void colClassesChanged(String[] classes) {
        colClasses = classes;
        colClassesST = import_stg.getInstanceOf("colClasses");
        colClassesST.setAttribute("classes", colClasses);
        fireStateChanged();
    }

    public void addChangeListener(ChangeListener l) {
        listenerList.add(ChangeListener.class, l);
    }

    public void removeChangeListener(ChangeListener l) {
        listenerList.remove(ChangeListener.class, l);
    }

    void fireStateChanged() {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                // Lazily create the event:
                if (changeEvent == null) {
                    changeEvent = new ChangeEvent(this);
                }
                ((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
            }
        }
    }

    public void setModel(TableModel tablemodel) {
        this.model = tablemodel;
        cellUpdates.clear();
    }

    private StringTemplate generateCellUpdates() {
        StringTemplate updates = null;
        StringTemplate update;
        for (Integer rowIndex : cellUpdates.keySet()) {
            if (isRowInTheRange(rowIndex)) {
                if (updates == null) {
                    updates = import_stg.getInstanceOf("cellUpdates");
                }
                for (Integer columnIndex : cellUpdates.get(rowIndex).keySet()) {
                    if (isColumnInTheRange(columnIndex)) {

                        if (hasRowNames && columnIndex == rowNamesColumnIndex) { // ==> a row.name change
                            update = createRowNamesUpdateST(rowIndex);
                        } else {
                            update = createCellUpdateST(rowIndex, columnIndex);
                        }
                        updates.setAttribute("allCellUpdates", update);
                    }
                }
            }
        }

        return updates;
    }

    private boolean isRowInTheRange(int rowIndex) {
        return rowIndex >= rowstartIndex && (rowIndex <= rowendIndex || rowendIndex == 0);
    }

    private boolean isColumnInTheRange(int columnIndex) {
        if (hasRowNames && columnIndex == rowNamesColumnIndex) {
            return true;
        } else if (hasRowNames) {

            if (columnIndex < rowNamesColumnIndex) {
                return (columnIndex >= colstartIndex)
                        && (colendIndex == 0 || columnIndex - 1 <= colendIndex);
            } else {
                return (columnIndex >= colstartIndex + 1)
                        && (colendIndex == 0 || columnIndex - 1 <= colendIndex);
            }
        } else {
            return columnIndex >= colstartIndex && (columnIndex <= colendIndex || colendIndex == 0);
        }
    }

    private StringTemplate createCellUpdateST(Integer rowIndex, Integer columnIndex) {
        StringTemplate update = import_stg.getInstanceOf("cellUpdate");
        update.setAttribute("varname", variablename);
        if (cellUpdates.get(rowIndex).get(columnIndex) == null || cellUpdates.get(rowIndex).get(columnIndex).equals("")) {
            update.setAttribute("value", "NA");
        } else if (cellUpdates.get(rowIndex).get(columnIndex) instanceof Number
                || cellUpdates.get(rowIndex).get(columnIndex) instanceof RDate
                || cellUpdates.get(rowIndex).get(columnIndex) instanceof RPOSIXct) {
            update.setAttribute("value", cellUpdates.get(rowIndex).get(columnIndex));
        } else {
            update.setAttribute("value", "\"" + cellUpdates.get(rowIndex).get(columnIndex) + "\"");
        }
        update.setAttribute("rowIndex", rowIndex - rowstartIndex + 1);
        if (hasRowNames && columnIndex > rowNamesColumnIndex) {
            update.setAttribute("columnIndex", columnIndex - colstartIndex);
        } else {
            update.setAttribute("columnIndex", columnIndex - colstartIndex + 1);
        }
        return update;
    }

    private StringTemplate createRowNamesUpdateST(int rowIndex) {
        StringTemplate update = import_stg.getInstanceOf("rownameUpdate");
        update.setAttribute("varname", variablename);
        update.setAttribute("rowIndex", rowIndex - rowstartIndex + 1);
        update.setAttribute("value", "\"" + cellUpdates.get(rowIndex).get(rowNamesColumnIndex) + "\"");

        return update;
    }

    public void colClassChanged(int columnIndex, String clazz) {

        Integer realColumnIndex;
        if (hasRowNames && columnIndex == 0) {
            realColumnIndex = rowNamesColumnIndex;
        } else {
            realColumnIndex = colstartIndex + columnIndex;
        }

        ArrayList<Integer> todelete = new ArrayList<Integer>();

        for (Integer rowIndex : cellUpdates.keySet()) {
            cellUpdates.get(rowIndex).remove(realColumnIndex);
            if(cellUpdates.get(rowIndex).isEmpty()){
                todelete.add(rowIndex);
            }
        }

        for(Integer row:todelete){
            cellUpdates.remove(row);
        }

        fireStateChanged();
    }
}
