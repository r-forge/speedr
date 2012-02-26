package at.ac.ait.speedr.importany;

import at.ac.ait.speedr.importwizard.steps.DataImportPanel;
import at.ac.ait.speedr.importwizard.steps.DataImportPanel.ColumnType;
import at.ac.ait.speedr.importwizard.steps.ImportTableModel;
import at.ac.ait.speedr.importwizard.tablemodel.ImportTableModelHelper;
import at.ac.ait.speedr.workspace.RConnection;
import at.ac.ait.speedr.workspace.RUtil;
import at.ac.arcs.tablefilter.ARCTable;
import at.ac.arcs.tablefilter.ist.ColumnSelectorOption;
import au.com.bytecode.opencsv.CSVReader;
import com.pensioenpage.jynx.ods2csv.ConversionException;
import com.pensioenpage.jynx.ods2csv.Converter;
import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.poi.hssf.eventusermodel.examples.XLS2CSVmra;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.eventusermodel.XLSX2CSV;
import org.rosuda.REngine.REXPGenericVector;
import org.xml.sax.SAXException;

/**
 *
 * @author visnei
 */
public class ImporterAnyFunction {

    private static final Logger logger = Logger.getLogger(ImporterAnyFunction.class.getName());
    private static ImportTableModelHelper modelhelper;

    public static void importany(String file, Integer rowstart, Integer rowend, Integer colstart, Integer colend,
            Boolean hasRowNames, Integer rowNamesColumnIndex, Boolean hasColumnNames,
            Integer columnNamesRowIndex, String separator, String quote, String[] columnClasses) throws Exception {

        String fileExtension;

        int index = file.lastIndexOf('.');
        if (index == -1) {
            fileExtension = "txt";
        } else {
            fileExtension = file.substring(index + 1);
        }

        logger.log(Level.INFO, "file: {0}", file);

        ImportTableModel model = null;
        modelhelper = new ImportTableModelHelper();
        try {
            if (fileExtension.equals("xlsx")) {
                model = readXLSX(new File(file), ',', '"');
            } else if (fileExtension.equals("xls")) {
                model = readXLS(new File(file), ',', '"');
            } else if (fileExtension.equals("ods")) {
                model = readCalc(new File(file), ',', '"');
            } else {
                model = readCSV(new File(file),
                        separator == null || separator.equals("") ? '\0' : separator.charAt(0),
                        quote == null || quote.equals("") ? '\0' : quote.charAt(0));
            }
        } catch (OutOfMemoryError err) {
            logger.log(Level.SEVERE, "Out of free memory available to speedR reached! Please see speedR() maxmemory parameter to increase it.");
            return;
        }

        if (hasColumnNames != null) {
            logger.log(Level.INFO, "hasColumnNames: {0}", hasColumnNames);
            model.setHasColumnNames(hasColumnNames);
            if (hasColumnNames && columnNamesRowIndex != null) {
                model.setColumnNamesRowIndex(columnNamesRowIndex - 1);
                logger.log(Level.INFO, "columnNamesRowIndex: {0}", columnNamesRowIndex);
            }
        }

        if (hasRowNames != null) {
            logger.log(Level.INFO, "hasRowNames: {0}", hasRowNames);
            model.setHasRowNames(hasRowNames);
            if (hasRowNames && rowNamesColumnIndex != null) {
                model.setRowNamesColumnIndex(rowNamesColumnIndex - 1);
                logger.log(Level.INFO, "rowNamesColumnIndex: {0}", rowNamesColumnIndex);
            }
        }

        if (rowstart != null) {
            model.setRowStart(rowstart - 1);
            logger.log(Level.INFO, "rowstart: {0}", rowstart);
        }
        if (rowend != null) {
            model.setRowEnd(rowend - 1);
            logger.log(Level.INFO, "rowend: {0}", rowend);
        }
        if (colstart != null) {
            model.setColStart(colstart - 1);
            logger.log(Level.INFO, "colstart: {0}", colstart);
        }
        if (colend != null) {
            model.setColEnd(colend - 1);
            logger.log(Level.INFO, "colend: {0}", colend);
        }

        logger.log(Level.INFO, "model column count: {0}", model.getColumnCount());
        logger.log(Level.INFO, "model row count: {0}", model.getRowCount());

        ARCTable table = new ARCTable();
        table.setNoneOptionCaption("");

        ColumnSelectorOption[] ops = new ColumnSelectorOption[5];
        ColumnSelectorOption<ColumnType> op = new ColumnSelectorOption<ColumnType>(ColumnType.NUMERIC, ColumnSelectorOption.OptionType.MANY_TO_ONE);
        ops[0] = op;
        op = new ColumnSelectorOption<ColumnType>(ColumnType.CHARACTER, ColumnSelectorOption.OptionType.MANY_TO_ONE);
        ops[1] = op;
        op = new ColumnSelectorOption<ColumnType>(ColumnType.FACTOR, ColumnSelectorOption.OptionType.MANY_TO_ONE);
        ops[2] = op;
        op = new ColumnSelectorOption<ColumnType>(ColumnType.DATE, ColumnSelectorOption.OptionType.MANY_TO_ONE);
        ops[3] = op;
        op = new ColumnSelectorOption<ColumnType>(ColumnType.POSIXCT, ColumnSelectorOption.OptionType.MANY_TO_ONE);
        ops[4] = op;

        table.setColumnSelectorOptions(ops);

        table.setModel(model);
        int i = 0;
        if (model.hasRowNames()) {
            i = 1;
            table.setColumnSelectorOption(DataImportPanel.ColumnType.CHARACTER, i);
            model.convertToText(i, modelhelper.getColumnData(i));
        }

        for (int j = 0; i < model.getColumnCount(); i++, j++) {
            if (columnClasses[j].equalsIgnoreCase(DataImportPanel.ColumnType.NUMERIC.toString())) {
                table.setColumnSelectorOption(DataImportPanel.ColumnType.NUMERIC, i);
                model.convertToNumeric(i);
            } else if (columnClasses[j].equalsIgnoreCase(DataImportPanel.ColumnType.CHARACTER.toString())) {
                table.setColumnSelectorOption(DataImportPanel.ColumnType.CHARACTER, i);
                model.convertToText(i, modelhelper.getColumnData(i));
            } else if (columnClasses[j].equalsIgnoreCase(DataImportPanel.ColumnType.FACTOR.toString())) {
                table.setColumnSelectorOption(DataImportPanel.ColumnType.FACTOR, i);
                model.convertToText(i, modelhelper.getColumnData(i));
            } else if (columnClasses[j].contains(DataImportPanel.ColumnType.DATE.toString())) {
                table.setColumnSelectorOption(DataImportPanel.ColumnType.DATE, i);
                if (columnClasses[j].contains("=")) {
                    model.convertToDate(i,
                            new String[]{RUtil.convertPatternFromRFormat(
                                columnClasses[j].substring(columnClasses[j].indexOf('=') + 1))});
                } else {
                    model.convertToDate(i, RUtil.parseDatePattern);
                }
            } else if (columnClasses[j].contains(DataImportPanel.ColumnType.POSIXCT.toString())) {
                table.setColumnSelectorOption(DataImportPanel.ColumnType.POSIXCT, i);

                if (columnClasses[j].contains("=")) {
                    model.convertToPOSIXct(i,
                            new String[]{RUtil.convertPatternFromRFormat(
                                columnClasses[j].substring(columnClasses[j].indexOf('=') + 1))});
                } else {
                    model.convertToPOSIXct(i, RUtil.parsePOSIXctPattern);
                }
            }
        }

        REXPGenericVector df = RUtil.createRDataFrame(table);
        RConnection.exportDataFrame(".speedrtemp", df);
    }

    private static ImportTableModel readXLSX(final File file, char separator, char quote) throws IOException {
        final PipedOutputStream pOut = new PipedOutputStream();
        PipedInputStream pIn = new PipedInputStream(pOut, 8192 * 2);

        new Thread(new Runnable() {

            public void run() {
                try {
                    OPCPackage p = OPCPackage.open(new BufferedInputStream(new FileInputStream(file)));
                    XLSX2CSV xlsx2csv = new XLSX2CSV(p, new PrintStream(pOut), -1);
                    xlsx2csv.process();
                } catch (InvalidFormatException ex) {
                    Logger.getLogger(ImporterAnyFunction.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(ImporterAnyFunction.class.getName()).log(Level.SEVERE, null, ex);
                } catch (OpenXML4JException ex) {
                    Logger.getLogger(ImporterAnyFunction.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParserConfigurationException ex) {
                    Logger.getLogger(ImporterAnyFunction.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SAXException ex) {
                    Logger.getLogger(ImporterAnyFunction.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        pOut.flush();
                        pOut.close();
                    } catch (IOException ex) {
                        Logger.getLogger(ImporterAnyFunction.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
        }).start();



        return createImportTableModel(pIn, separator, quote);
    }

    private static ImportTableModel readXLS(final File file, char separator, char quote) throws IOException {
        final PipedOutputStream pOut = new PipedOutputStream();
        PipedInputStream pIn = new PipedInputStream(pOut, 8192 * 2);

        new Thread(new Runnable() {

            public void run() {
                try {
                    XLS2CSVmra xls2csv = new XLS2CSVmra(new POIFSFileSystem(new FileInputStream(file)), new PrintStream(pOut), -1);
                    xls2csv.process();
                } catch (IOException ex) {
                    Logger.getLogger(ImporterAnyFunction.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        pOut.flush();
                        pOut.close();
                    } catch (IOException ex) {
                        Logger.getLogger(ImporterAnyFunction.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
        }).start();

        return createImportTableModel(pIn, separator, quote);
    }

    private static ImportTableModel readCalc(final File file, char separator, char quote) throws IOException {
        final PipedOutputStream pOut = new PipedOutputStream();
        PipedInputStream pIn = new PipedInputStream(pOut, 8192 * 2);

        new Thread(new Runnable() {

            public void run() {
                try {
                    new Converter().convert(new BufferedInputStream(new FileInputStream(file)), pOut);
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(ImporterAnyFunction.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(ImporterAnyFunction.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ConversionException ex) {
                    Logger.getLogger(ImporterAnyFunction.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();

        return createImportTableModel(pIn, separator, quote);
    }

    private static ImportTableModel readCSV(File file, char separator, char quote) throws FileNotFoundException, IOException {
        return createImportTableModel(new FileInputStream(file), separator, quote);
    }

    private static ImportTableModel createImportTableModel(InputStream in, char separator, char quote) throws IOException {
        ArrayList<String[]> rows = new ArrayList<String[]>();

        CSVReader csvReader =
                new CSVReader(new InputStreamReader(in), separator, quote);

        String[] next;
        int line = 0;
        while ((next = csvReader.readNext()) != null) {
            rows.add(next);
            modelhelper.saveRowDataSet(line, next);
            line++;
        }
        modelhelper.close();

        ImportTableModel model = new ImportTableModel();

        model.addRow(rows);

        return model;
    }
}
