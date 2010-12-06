package at.ac.ait.speedr.importwizard.steps;

import at.ac.ait.speedr.WorkspaceView;
import com.pensioenpage.jynx.ods2csv.Converter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import at.ac.ait.speedr.importwizard.WizardPanel;
import at.ac.ait.speedr.importwizard.WizardValidationException;
import au.com.bytecode.opencsv.CSVReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author visnei
 */
public class CalcDataImportWizardStep implements
        WizardPanel.ValidatingStep, WizardPanel.ProgressStep, PropertyChangeListener {

    private static final Logger logger = Logger.getLogger(CalcDataImportWizardStep.class.getName());
    private DataImportPanel impPanel;
    private WizardPanel settings;
    private volatile boolean isStopped = false;
    private boolean valid = false;
    private File dataFile;

    public DataImportPanel getComponent() {
        if (impPanel == null) {
            impPanel = new DataImportPanel(false);
            impPanel.addPropertyChangeListener(this);
        }
        return impPanel;
    }

    public void readSettings(WizardPanel settings) {
        this.settings = settings;
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

            CalcProcessor excelProcessor = new CalcProcessor(pOut, dataFile);

            excelProcessor.start();

            CSVReaderWorker worker = new CSVReaderWorker(pIn,
                    getComponent().getTableModel());
            worker.execute();
            worker.get();
        } catch (Exception ex) {
            logger.log(Level.WARNING, ex.getMessage(), ex);
            settings.putProperty(WizardPanel.PROP_ERROR_MESSAGE, ex.getMessage());
        }
    }

    public void stopProcessing() {
        isStopped = true;
    }

    private class CalcProcessor extends Thread {

        private PipedOutputStream pOut;
        private File calcFile;

        public CalcProcessor(PipedOutputStream pOut, File calcFile) {
            this.pOut = pOut;
            this.calcFile = calcFile;
        }

        @Override
        public void run() {
            try {
                new Converter().convert(new BufferedInputStream(new FileInputStream(calcFile)), pOut);
            } catch (Exception ex) {
                logger.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
    }

    private class CSVReaderWorker extends SwingWorker<Object, String[]> {

        private InputStream in;
        private ImportTableModel tableModel;
        private boolean gcCalled = false;

        public CSVReaderWorker(InputStream in, ImportTableModel tableModel) {
            this.in = in;
            this.tableModel = tableModel;
        }

        @Override
        protected Object doInBackground() throws Exception {
            try {
                CSVReader csvReader =
                        new CSVReader(new InputStreamReader(in), ',', '"');
                String[] next;
                while (!isStopped && (next = csvReader.readNext()) != null) {
                    publish(next);
                }
                in.close();
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
                tableModel.addRow(chunks);
            } catch (OutOfMemoryError err) {
                outOfMemoryHandle();
            }
        }

        @Override
        protected void done() {
            setValid(true);
            getComponent().setConfigurationPanelEnabled(true);
        }
    }

    private void outOfMemoryHandle() {
        isStopped = true;
        settings.putProperty(WizardPanel.PROP_WARNING_MESSAGE, "Out of available memory!");
        JOptionPane.showMessageDialog(impPanel, "Out of memory!\nPlease restart R and call speedR with an increased value in Mb!");
    }
}
