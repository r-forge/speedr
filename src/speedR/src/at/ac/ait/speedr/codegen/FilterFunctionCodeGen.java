package at.ac.ait.speedr.codegen;

import at.ac.ait.speedr.codegen.FilterExpressionRCodeGen.rcode_return;
import at.ac.ait.speedr.table.RPOSIXct;
import at.ac.ait.speedr.table.model.RAbstractTableModel;
import at.ac.ait.speedr.table.model.twodim.RDataFrameTableModel;
import at.ac.arcs.tablefilter.filtermodel.DateFilterDevice;
import at.ac.arcs.tablefilter.filtermodel.NumericFilterDevice;
import at.ac.arcs.tablefilter.filtermodel.info.FilterColumnInfo;
import at.ac.arcs.tablefilter.filtermodel.info.FilterInfo;
import at.ac.arcs.tablefilter.filtermodel.info.FilterRowInfo;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class FilterFunctionCodeGen {

    private static StringTemplateGroup stg;

    public FilterFunctionCodeGen() throws IOException {
        if (stg == null) {
            InputStreamReader r = new InputStreamReader(
                    FilterFunctionCodeGen.class.getResourceAsStream("/at/ac/ait/speedr/codegen/template/RCodeGen.stg"));
            stg = new StringTemplateGroup(r);
            r.close();
        }
    }

    public String getFilterFunctionCode(String functionname, FilterInfo filterInfo,
            boolean hasRowname, RAbstractTableModel tableModel) {

        StringTemplate filterfunction = stg.getInstanceOf("filterfunction");
        filterfunction.setAttribute("functionname", functionname);

        FilterRowInfo[] filterRows = filterInfo.getFilterRows();

        for (FilterRowInfo filterRowInfo : filterRows) {
            StringTemplate filterlevel = stg.getInstanceOf("filterlevel");

            List<FilterColumnInfo> columns = filterRowInfo.getColumns();
            if (columns.isEmpty()) {
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

                    /**
                     * WALK RESULTING TREE *
                     */
                    // get tree from parser
                    CommonTree tree = (CommonTree) formula.getTree();

                    // Create a tree node stream from resulting tree
                    CommonTreeNodeStream nodes = new CommonTreeNodeStream(tree);
                    nodes.setTokenStream(tokens);

                    FilterExpressionRCodeGen rcodegen = new FilterExpressionRCodeGen(nodes);




                    int col = filterColumnInfo.getColumn();
                    if (hasRowname && col == 0) {
                        rcodegen.setHasRowNames(hasRowname);
                        rcodegen.setColumnIndex(tableModel.getRownameIndexCode("x"));
                    } else {
                        rcodegen.setColumnIndex(tableModel.getColumnIndexCode(col));
                    }

                    if (tableModel instanceof RDataFrameTableModel) {
                        rcodegen.setDataframe(true);
                        rcodegen.setDataframeColumnIndex(tableModel.getColumnName(col));
                    }

                    if (tableModel.getColumnClass(col) == RPOSIXct.class) {
                        rcodegen.setPOSIXct(true);
                    }

                    rcodegen.setTemplateLib(stg);

                    rcode_return rcode = rcodegen.rcode();
                    filterlevel.setAttribute("selectors", rcode.st);

                } catch (RecognitionException recognitionException) {
                    Logger.getLogger(FilterFunctionCodeGen.class.getName()).log(Level.SEVERE, "Can't parse the expression: " + exp, recognitionException);
                }
            }
            filterfunction.setAttribute("filterlevels", filterlevel);
        }
        if (filterfunction.getAttribute("filterlevels") == null) {
            return "";
        } else {
            return filterfunction.toString();
        }
    }
}
