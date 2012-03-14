package at.ac.ait.speedr.importwizard.steps;

import at.ac.ait.speedr.WorkspaceView;
import at.ac.ait.speedr.importwizard.WizardPanel;
import at.ac.ait.speedr.importwizard.WizardValidationException;
import at.ac.ait.speedr.importwizard.tablemodel.ImportTableModelHelper;
import au.com.bytecode.opencsv.CSVReader;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.apache.poi.hssf.eventusermodel.examples.XLS2CSVmra;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.eventusermodel.XLSX2CSV;

/**
 *
 * @author visnei
 */
public class ExcelDataImportWizardStep implements
        WizardPanel.ValidatingStep, WizardPanel.ProgressStep, PropertyChangeListener {

    private static final Logger logger = Logger.getLogger(ExcelDataImportWizardStep.class.getName());
    private DataImportPanel impPanel;
    private WizardPanel settings;
    private volatile boolean isStopped = false;
    private boolean valid = false;
    private String dataFileExtension;
    private File dataFile;

    public DataImportPanel getComponent() {
        if (impPanel == null) {
            impPanel = new DataImportPanel(false);
            impPanel.addPropertyChangeListener(this);
        }
        return impPanel;
    }

    public void readSettings(WizardPanel settings) {
        if (getComponent().getTableModel().getMaxColumnCount() > 0) {
            getComponent().resetRowAndColumnsMaxAndMin();
            getComponent().getTableModel().clearAll();
        }
        
        this.settings = settings;
        dataFileExtension = (String) settings.getProperty(DataSourceWizardStep.PROP_EXTENSION);
        if (settings.getProperty(DataSourceWizardStep.PROP_DATASOURCE) == DataSourcePanel.PROP_FILE_DATASOURCE) {
            getComponent().setConfigurationPanelEnabled(false);
            dataFile = (File) settings.getProperty(DataSourceWizardStep.PROP_FILE);
        }
    }

    public void storeSettings(WizardPanel settings) {
        settings.putProperty(DataImportPanel.PROP_VARIABLENAME, getComponent().getVariableName());
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean b) {
        valid = b;
        fireChangeEvent();
    }
    private final Set<ChangeListener> listeners = new HashSet<ChangeListener>(1);

    public final void addChangeListener(ChangeListener l) {
        synchronized (listeners) {
            listeners.add(l);
        }
    }

    public final void removeChangeListener(ChangeListener l) {
        synchronized (listeners) {
            listeners.remove(l);
        }
    }

    protected final void fireChangeEvent() {
        Iterator<ChangeListener> it;
        synchronized (listeners) {
            it = new HashSet<ChangeListener>(listeners).iterator();
        }
        ChangeEvent ev = new ChangeEvent(this);
        while (it.hasNext()) {
            it.next().stateChanged(ev);
        }
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName() == DataImportPanel.PROP_VARIABLENAME) {
            String varname = getComponent().getVariableName();

            if (varname.equals("") || Character.isDigit(varname.charAt(0))
                    || !varname.equals(varname.replaceAll("[ -+*/\\()=!~`@#$%^&*<>,?;:\"\']", "."))) {
                settings.putProperty(WizardPanel.PROP_ERROR_MESSAGE, "invalid variable name");
                setValid(false);
            } else {
                settings.putProperty(WizardPanel.PROP_ERROR_MESSAGE, null);
                setValid(true);
            }
        }
    }

    public void validate() throws WizardValidationException {
        String varname = getComponent().getVariableName();
        if (varname == null || varname.trim().equals("")) {
            throw new WizardValidationException("Please set a variable name");
        }

        if (WorkspaceView.getInstance().hasVariable(varname)) {
            int showConfirmDialog =
                    JOptionPane.showConfirmDialog(getComponent(), "A variable with the name "
                    + varname + " exsists already!\n" + "Do you want to replace it?");
            if (showConfirmDialog != JOptionPane.YES_OPTION) {
                throw new WizardValidationException("Variable name exists");
            }
        }
    }

    public void startProcessing() {
        isStopped = false;
        try {
            PipedOutputStream pOut = new PipedOutputStream();
            PipedInputStream pIn = new PipedInputStream(pOut, 8192 * 2);

            ExcelProcessor excelProcessor = new ExcelProcessor(pOut, dataFile, dataFileExtension);

            excelProcessor.start();

            CSVReaderWorker worker = new CSVReaderWorker(pIn,
                    getComponent().getTableModel());
            worker.execute();

            worker.get();

        } catch (Exception ex) {
            logger.log(Level.INFO, ex.getMessage(), ex);
            settings.putProperty(WizardPanel.PROP_ERROR_MESSAGE, ex.getMessage());
        }
    }

    public void stopProcessing() {
        isStopped = true;
    }

    private class ExcelProcessor extends Thread {

        private PipedOutputStream pOut;
        private File excelFile;
        private String extension;

        public ExcelProcessor(PipedOutputStream pOut, File excelFile, String extension) {
            this.pOut = pOut;
            this.excelFile = excelFile;
            this.extension = extension;
        }

        @Override
        public void run() {

            if (extension.equalsIgnoreCase("xlsx")) {
                try {
                    OPCPackage p = OPCPackage.open(new BufferedInputStream(new FileInputStream(excelFile)));
                    XLSX2CSV xlsx2csv = new XLSX2CSV(p, new PrintStream(pOut), -1);
                    xlsx2csv.process();
                } catch (Exception ex) {
                    logger.log(Level.SEVERE, ex.getMessage(), ex);
                } finally {
                    try {
                        pOut.flush();
                        pOut.close();
                    } catch (IOException ex) {
                        logger.log(Level.WARNING, ex.getMessage(), ex);
                    }
                }
            } else if (extension.equalsIgnoreCase("xls")) {
                try {
                    XLS2CSVmra xls2csv = new XLS2CSVmra(
                            new POIFSFileSystem(new FileInputStream(excelFile.getPath())),
                            new PrintStream(pOut), -1);
                    xls2csv.process();
                } catch (IOException ex) {
                    logger.log(Level.SEVERE, ex.getMessage(), ex);
                } finally {
                    try {
                        pOut.flush();
                        pOut.close();
                    } catch (IOException ex) {
                        logger.log(Level.WARNING, ex.getMessage(), ex);
                    }
                }
            }

        }
    }

    private class CSVReaderWorker extends SwingWorker<Object, String[]> {

        private InputStream in;
        private ImportTableModel tableModel;

        public CSVReaderWorker(InputStream in, ImportTableModel tableModel) {
            this.in = in;
            this.tableModel = tableModel;
        }

        @Override
        protected Object doInBackground() throws Exception {
            try {
                CSVReader csvReader =
                        new CSVReader(new InputStreamReader(in), ',', '"','\0');
//                if (dataFileExtension.equals("xlsx")) {
//                    //skip excel column names (A,B,C ...)
//                    csvReader.readNext();
//                }

                ImportTableModelHelper modelhelper = new ImportTableModelHelper();
                int line = 0;

                String[] next;
                while (!isStopped && (next = csvReader.readNext()) != null) {
                    publish(next);
                    modelhelper.saveRowDataSet(line, next);
                    line++;
                }

                modelhelper.close();
                getComponent().setImportTableModelHelper(modelhelper);
                
                csvReader.close();
            } catch (IOException ex) {
                logger.log(Level.WARNING, ex.getMessage(), ex);
                settings.putProperty(WizardPanel.PROP_ERROR_MESSAGE, ex.getMessage());
            } catch (OutOfMemoryError err) {
                outOfMemoryHandle();
            }
            return null;
        }

        @Override
        protected void process(List<String[]> chunks) {
            try {
                tableModel.addRows(chunks);
            } catch (OutOfMemoryError err) {
                outOfMemoryHandle();
            }
        }

        @Override
        protected void done() {
            setValid(true);
            getComponent().setConfigurationPanelEnabled(true);
            tableModel.convertColumnsToNumericIfPossible();
        }
    }

    private void outOfMemoryHandle() {
        isStopped = true;
        settings.putProperty(WizardPanel.PROP_WARNING_MESSAGE, "Out of available memory!");
        JOptionPane.showMessageDialog(getComponent(), "Out of memory!\nPlease restart R and call speedR with an increased value in Mb!");
    }
}
