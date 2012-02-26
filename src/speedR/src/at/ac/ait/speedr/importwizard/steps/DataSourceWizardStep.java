package at.ac.ait.speedr.importwizard.steps;

import at.ac.ait.speedr.importwizard.WizardPanel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author visnei
 */
public class DataSourceWizardStep implements WizardPanel.Step,PropertyChangeListener {

    public static final String PROP_EXTENSION = "PROP_EXTENSION";
    public static final String PROP_FILE = "PROP_FILE";
    public static final String PROP_URL_TO_DATA = "PROP_URL_TO_DATA";
    public static final String PROP_PASTED_DATA = "PROP_PASTED_DATA";
    public static final String PROP_DATASOURCE = "PROP_DATASOURCE";
    
    private DataSourcePanel dataSourcePanel;
    private WizardPanel settings;
    private boolean isValid = false;
    private File selectedFile;
    private String pastedData;
    private java.net.URL url;

    public javax.swing.JPanel getComponent() {
        if (dataSourcePanel == null) {
            dataSourcePanel = new DataSourcePanel();
            dataSourcePanel.addPropertyChangeListener(this);
        }
        return dataSourcePanel;
    }

    public void readSettings(WizardPanel settings) {
        this.settings = settings;
    }

    public void storeSettings(WizardPanel settings) {
        if (dataSourcePanel.getCurrentDataSource() == DataSourcePanel.PROP_FILE_DATASOURCE) {
            settings.putProperty(PROP_DATASOURCE, DataSourcePanel.PROP_FILE_DATASOURCE);
            int index = selectedFile.getName().lastIndexOf('.');
            if(index == -1){
                settings.putProperty(PROP_EXTENSION, "txt");
            }else{
                settings.putProperty(PROP_EXTENSION, selectedFile.getName().substring(index+1));
            }
            settings.putProperty(PROP_FILE, selectedFile);
        } else if (dataSourcePanel.getCurrentDataSource() == DataSourcePanel.PROP_PASTEDATA_DATASOURCE) {
            settings.putProperty(PROP_DATASOURCE, DataSourcePanel.PROP_PASTEDATA_DATASOURCE);
            settings.putProperty(PROP_PASTED_DATA, pastedData);
            settings.putProperty(PROP_EXTENSION, "txt");
        } else if (dataSourcePanel.getCurrentDataSource() == DataSourcePanel.PROP_URL_DATASOURCE) {
            settings.putProperty(PROP_DATASOURCE, DataSourcePanel.PROP_URL_DATASOURCE);
            settings.putProperty(PROP_URL_TO_DATA, url);
            String urlText = url.toString();
            int index = urlText.lastIndexOf('.');
            if(index == -1){
                settings.putProperty(PROP_EXTENSION, "txt");
            }else{
                settings.putProperty(PROP_EXTENSION, urlText.substring(index+1));
            }
        }
    }

    public boolean isValid() {
        return isValid;
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
        if (evt.getPropertyName() == javax.swing.JFileChooser.SELECTED_FILE_CHANGED_PROPERTY) {
            selectedFile = dataSourcePanel.getSelectedFile();
            checkValidity(selectedFile, "Please select a file!");
        } else if (evt.getPropertyName() == DataSourcePanel.PROP_PASTE_DATA_CHANGED) {
            pastedData = evt.getNewValue().toString();
            if (pastedData.length() == 0) {
                pastedData = null;                
            }
            checkValidity(pastedData, "Please write or paste some data on the text area!");
        } else if (evt.getPropertyName() == DataSourcePanel.PROP_URL_CHANGED) {
            checkURL(evt);
            checkValidity(url, "URL is not valid!");
        } else if (evt.getPropertyName() == DataSourcePanel.PROP_DATASOURCE_CHANGED) {
            if (evt.getNewValue() == DataSourcePanel.PROP_FILE_DATASOURCE) {
                checkValidity(selectedFile, "Please select a file!");
            } else if (evt.getNewValue() == DataSourcePanel.PROP_PASTEDATA_DATASOURCE) {
                checkValidity(pastedData, "Please write or paste some data on the text area!");
            } else if (evt.getNewValue() == DataSourcePanel.PROP_URL_DATASOURCE) {
                checkValidity(url, "URL is not valid!");                
            }
        }
    }

    private void checkValidity(Object data, String message) {
        if (data == null) {
            isValid = false;
            setMessage(message);
        } else {
            isValid = true;
            setMessage(null);
        }
        fireChangeEvent();
    }

    private void checkURL(PropertyChangeEvent evt) {
        try {
            url = new java.net.URL(evt.getNewValue().toString());
        } catch (MalformedURLException ex) {
            url=null;
        }
    }

    private void setMessage(String message) {
        settings.putProperty(WizardPanel.PROP_ERROR_MESSAGE, message);
    }
}
