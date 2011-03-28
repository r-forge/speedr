package at.ac.ait.speedr.importwizard.steps;

import at.ac.ait.speedr.WorkspaceView;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import at.ac.ait.speedr.importwizard.WizardPanel;
import at.ac.ait.speedr.importwizard.WizardValidationException;
import at.ac.ait.speedr.importwizard.tablemodel.ImportTableModelHelper;
import au.com.bytecode.opencsv.CSVReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author visnei
 */
public class CSVDataImportWizardStep implements
        WizardPanel.ValidatingStep, WizardPanel.ProgressStep, PropertyChangeListener {

    private static final Logger logger = Logger.getLogger(CSVDataImportWizardStep.class.getName());
    public static final String PROP_TABLEMODEL = "TABLEMODEL";
    private DataImportPanel impPanel;
    private WizardPanel settings;
    private boolean valid = false;
    private volatile boolean isStopped = false;
    private File dataFile;
    private OnMemoryZipEntry zippedData;
    private volatile char delimiter;
    private volatile char qualifier;
    private volatile boolean hasColumnNames = false;
    private volatile boolean hasRowNames = false;
    private CSVReaderWorker csvWorker;
    private Zipper zipper;

    public DataImportPanel getComponent() {
        if (impPanel == null) {
            impPanel = new DataImportPanel(true);
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

    public synchronized OnMemoryZipEntry getZippedData() {
        return zippedData;
    }

    public synchronized void setZippedData(OnMemoryZipEntry zippedData) {
        this.zippedData = zippedData;
    }
    private final Set<ChangeListener> listeners = new HashSet<ChangeListener>(1);

    public final void addChangeListener(ChangeListener l) {
        if (!listeners.contains(l)) {
            synchronized (listeners) {
                listeners.add(l);
            }
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
        if (evt.getPropertyName() == DataImportPanel.PROP_DELIMITER) {
            setValid(false);
            getComponent().setConfigurationPanelEnabled(false);
            setDelimiter((Character) evt.getNewValue());

            try {
                reRead();
            } catch (IOException ex) {
                settings.putProperty(WizardPanel.PROP_ERROR_MESSAGE, ex.getMessage());
            }

        } else if (evt.getPropertyName() == DataImportPanel.PROP_QUALIFIER) {
            if (evt.getNewValue() == null) {
                setQualifier((char) 0);
            } else {
                setQualifier((Character) evt.getNewValue());
            }

            setValid(false);
            getComponent().setConfigurationPanelEnabled(false);

            try {
                reRead();
            } catch (IOException ex) {
                settings.putProperty(WizardPanel.PROP_ERROR_MESSAGE, ex.getMessage());
            }

        } else if (evt.getPropertyName() == DataImportPanel.PROP_VARIABLENAME) {
            String varname = getComponent().getVariableName();

            if (varname.equals("") || !varname.equals(varname.replaceAll("[ -+*/\\()=!~`@#$%^&*<>,?;:\"\']", "."))) {
                settings.putProperty(WizardPanel.PROP_ERROR_MESSAGE, "invalid variable name");
                setValid(false);
            } else {
                settings.putProperty(WizardPanel.PROP_ERROR_MESSAGE, null);
                setValid(true);
            }
        }
    }

    private void reRead() throws IOException {
        settings.putProperty(WizardPanel.PROP_ERROR_MESSAGE, null);
        getComponent().resetRowAndColumnsMaxAndMin();
        getComponent().getTableModel().clearAll();

        csvWorker =
                new CSVReaderWorker(zippedData.getUnzippedDataStream(),
                getComponent().getTableModel());
        csvWorker.execute();
    }

    private char getDelimiter() {
        return delimiter;
    }

    private void setDelimiter(char d) {
        delimiter = d;
        getComponent().setDelimiter(delimiter);
    }

    private char getQualifier() {
        return qualifier;
    }

    private void setQualifier(char q) {
        qualifier = q;
        getComponent().setQualifer(qualifier);
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

    private InputStream getInput() {
        InputStream in = null;
        if (settings.getProperty(DataSourceWizardStep.PROP_DATASOURCE)
                == DataSourcePanel.PROP_FILE_DATASOURCE) {

            dataFile = (File) settings.getProperty(DataSourceWizardStep.PROP_FILE);
            try {
                in = new BufferedInputStream(new FileInputStream(dataFile));
            } catch (FileNotFoundException ex) {
                settings.putProperty(WizardPanel.PROP_ERROR_MESSAGE, ex.getMessage());
            }

        } else if (settings.getProperty(DataSourceWizardStep.PROP_DATASOURCE)
                == DataSourcePanel.PROP_PASTEDATA_DATASOURCE) {

            String data = (String) settings.getProperty(DataSourceWizardStep.PROP_PASTED_DATA);
            in = new ByteArrayInputStream(data.getBytes());

        } else if (settings.getProperty(DataSourceWizardStep.PROP_DATASOURCE)
                == DataSourcePanel.PROP_URL_DATASOURCE) {

            try {
                in = ((java.net.URL) settings.getProperty(DataSourceWizardStep.PROP_URL_TO_DATA)).openStream();
            } catch (IOException ex) {
                settings.putProperty(WizardPanel.PROP_ERROR_MESSAGE, ex.getMessage());
            }

        } else {
            settings.putProperty(WizardPanel.PROP_ERROR_MESSAGE, "A valid data source (file,pasted data or url) is not specified");
            throw new IllegalStateException("A valid data source (file,pasted data or url) is not specified");
        }
        return in;
    }

    public void startProcessing() {
        isStopped = false;
        hasColumnNames = false;
        hasRowNames = false;
        InputStream in = getInput();
        if (in == null) {
            return;
        }

        try {

            try {
                SwingUtilities.invokeAndWait(new Runnable() {

                    public void run() {
                        getComponent().setConfigurationPanelEnabled(false);
                    }
                });
            } catch (Exception ex) {
            }

            StreamFeeder streamfeeder = new StreamFeeder(in, 2);

            zipper = new Zipper(streamfeeder.getPipedInputStream(1));

            csvWorker = new CSVReaderWorker(
                    streamfeeder.getPipedInputStream(0),
                    getComponent().getTableModel());

            streamfeeder.start();

            try {
                csvWorker.get();
            } catch (Exception ex) {
                settings.putProperty(WizardPanel.PROP_ERROR_MESSAGE, ex.getMessage());
            }

            csvWorker = null;
            zipper = null;

        } catch (IOException ex) {
            settings.putProperty(WizardPanel.PROP_ERROR_MESSAGE, ex.getMessage());
        }
    }

    public void stopProcessing() {
        isStopped = true;
    }

    private class StreamFeeder extends Thread {

        private final int BUFFER = 8192;
        private ArrayList<PipedInputStream> pipedInStreams = new ArrayList<PipedInputStream>();
        private ArrayList<PipedOutputStream> pipedOutStreams = new ArrayList<PipedOutputStream>();
        private InputStream in;

        public StreamFeeder(InputStream bis, int streamCount) throws IOException {
            this.in = bis;
            PipedInputStream pis;
            PipedOutputStream pos;
            for (int i = 0; i < streamCount; i++) {
                pos = new PipedOutputStream();
                pis = new PipedInputStream(pos, BUFFER);

                pipedOutStreams.add(pos);
                pipedInStreams.add(pis);
            }
        }

        public PipedInputStream getPipedInputStream(int index) {
            return pipedInStreams.get(index);
        }

        @Override
        public void run() {
            try {
                int read;
                byte[] buf = new byte[16384];

                ByteArrayOutputStream byteOut = new ByteArrayOutputStream();

                while ((read = in.read(buf)) != -1) {
                    if (isStopped) {
                        return;
                    }
                    byteOut.write(buf, 0, read);

                    String lines = byteOut.toString();

                    CSVReader reader = new CSVReader(new StringReader(lines));

                    if (reader.readNext() != null && reader.readNext() != null) {
                        determineCsvSettings(lines);
                        break;
                    }
                }

                zipper.start();
                csvWorker.execute();

                buf = byteOut.toByteArray();
                for (int i = 0; i < buf.length; i++) {
                    for (PipedOutputStream pos : pipedOutStreams) {
                        pos.write(buf[i]);
                    }
                }

                while (!isStopped && (read = in.read(buf)) != -1) {
                    for (PipedOutputStream pos : pipedOutStreams) {
                        if (!isStopped) {
                            pos.write(buf, 0, read);
                        }
                    }
                }
                for (PipedOutputStream pos : pipedOutStreams) {
                    pos.flush();
                    pos.close();
                }

            } catch (IOException ex) {
                logger.log(Level.SEVERE, ex.getMessage(), ex);
            } finally {
                try {
                    in.close();
                } catch (IOException ex) {
                }
            }
        }

        private void determineCsvSettings(String lines) throws IOException {
            char[] delimiters = {',', ';', '\t', ' '};
            char[] qualifiers = {'"', '\'', '\0'};

            StringReader stringreader;
            CSVReader csv;

            int maxColCount = 0;
            int d_index = 0;
            int q_index = 0;

            for (int i = 0; i < delimiters.length; i++) {
                char d = delimiters[i];

                stringreader = new StringReader(lines);
                csv = new CSVReader(stringreader, d, '\0');

                String[] header;
                String[] nextLine;

                if ((header = csv.readNext()) != null) {
                    if ((nextLine = csv.readNext()) != null) {

                        int colCount = Math.max(header.length, nextLine.length);
                        if (colCount > maxColCount) {
                            maxColCount = colCount;
                            d_index = i;

                            for (String h : header) {
                                if (h != null && !h.equals("")) {
                                    if (h.startsWith("\"") && h.endsWith("\"")) {
                                        q_index = 0;
                                        break;
                                    } else if (h.startsWith("'") && h.endsWith("'")) {
                                        q_index = 1;
                                        break;
                                    } else {
                                        q_index = 2;
                                    }
                                }
                            }

                            setHeaderAndRowNames(header, nextLine);
                        }
                    }
                }

            }

            setDelimiter(delimiters[d_index]);
            setQualifier(qualifiers[q_index]);
        }

        private void setHeaderAndRowNames(String[] colnamesrow, String[] nextrow) {
            if (colnamesrow.length + 1 == nextrow.length
                    || (colnamesrow.length == nextrow.length
                    && (colnamesrow[0].equals("\"\"")
                    || colnamesrow[0].equals("")
                    || colnamesrow[0] == null))) {

                hasColumnNames = true;
                hasRowNames = true;
            }else if(((String) settings.getProperty(DataSourceWizardStep.PROP_EXTENSION)).equals("csv")){
                hasColumnNames = true;
            }
        }
    }

    private class Zipper extends Thread {

        private InputStream in;

        public Zipper(InputStream in) {
            this.in = in;
        }

        @Override
        public void run() {
            try {

                ByteArrayOutputStream bout = new ByteArrayOutputStream();

                ZipOutputStream out = new ZipOutputStream(bout);

                ZipEntry entry = new ZipEntry("entry");

                out.putNextEntry(entry);

                int read;
                byte[] buf = new byte[8192];
                while (!isStopped && (read = in.read(buf)) != -1) {
                    if (isStopped) {
                        return;
                    }
                    out.write(buf, 0, read);
                }
                out.flush();
                out.close();

                setZippedData(new OnMemoryZipEntry(entry, bout.toByteArray()));

                logger.log(Level.INFO, "zipped size: {0}", (zippedData.getCompressedSize() / (1024 * 1024)));

            } catch (IOException ex) {
                logger.log(Level.WARNING, ex.getMessage(), ex);
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

                CSVReader reader = new CSVReader(new InputStreamReader(in), getDelimiter(), getQualifier());
                ImportTableModelHelper modelhelper = new ImportTableModelHelper();
                String[] nextLine;
                int line = 0;
                while (!isStopped && (nextLine = reader.readNext()) != null) {
                    publish(nextLine);
                    modelhelper.saveRowDataSet(line, nextLine);
                    line++;
                }
                modelhelper.close();
                impPanel.setImportTableModelHelper(modelhelper);
            } catch (IOException ex) {
                logger.log(Level.WARNING, "Unexpected end of the swingworker", ex);
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
            tableModel.setHasColumnNames(hasColumnNames);
            tableModel.setHasRowNames(hasRowNames);
            tableModel.convertColumnsToNumericIfPossible();
            getComponent().setConfigurationPanelEnabled(true);
            logger.log(Level.INFO, "CSVReaderWorker(SwingWorker) is done");
        }
    }

    private void outOfMemoryHandle() {
        isStopped = true;
        settings.putProperty(WizardPanel.PROP_WARNING_MESSAGE, "Out of available memory!");
        JOptionPane.showMessageDialog(impPanel, "Out of memory!\nPlease restart R and call speedR with an increased value in Mb!");
    }
}
