package at.ac.ait.speedr.importwizard;

import at.ac.ait.speedr.importwizard.steps.CSVDataImportWizardStep;
import at.ac.ait.speedr.importwizard.steps.CalcDataImportWizardStep;
import at.ac.ait.speedr.importwizard.steps.DataImportPanel;
import at.ac.ait.speedr.importwizard.steps.DataImportPanelUserActionListener;
import at.ac.ait.speedr.importwizard.steps.DataSourceWizardStep;
import at.ac.ait.speedr.importwizard.steps.ExcelDataImportWizardStep;
import at.ac.ait.speedr.importwizard.steps.ImportTableModel;
import at.ac.ait.speedr.workspace.RUtil;
import at.ac.arcs.tablefilter.ARCTable;
import at.ac.arcs.tablefilter.events.FilterListener;
import java.beans.PropertyChangeListener;
import java.util.NoSuchElementException;
import javax.swing.table.DefaultTableModel;
import org.rosuda.REngine.REXPGenericVector;

/**
 *
 * @author visnei
 */
public class WizardIterator {

    private int index;
    private final String[] steps = new String[]{"Select data", "Edit table"};
    private DataSourceWizardStep dataSourceStep;
    private CSVDataImportWizardStep csvStep;
    private ExcelDataImportWizardStep excelStep;
    private CalcDataImportWizardStep calcStep;
    private WizardPanel settings;
    private DataImportPanelUserActionListener useractionlistener;
    private FilterListener filterlistener;

    public WizardIterator() {
    }

    public WizardPanel.Step current() {
        if (index == 0) {
            if (dataSourceStep == null) {
                dataSourceStep = new DataSourceWizardStep();
            }
            return dataSourceStep;
        } else {
            String dataFileExtension = (String) settings.getProperty(DataSourceWizardStep.PROP_EXTENSION);

            if (dataFileExtension.equalsIgnoreCase("xls")
                    || dataFileExtension.equalsIgnoreCase("xlsx")) {
                if (excelStep == null) {
                    excelStep = new ExcelDataImportWizardStep();
                    if (useractionlistener != null) {
                        excelStep.getComponent().setUseractionlistener(useractionlistener);
                    }
                    if (filterlistener != null) {
                        excelStep.getComponent().setFilterListener(filterlistener);
                    }
                }
                return excelStep;
            } else if (dataFileExtension.equalsIgnoreCase("ods")) {

                if (calcStep == null) {
                    calcStep = new CalcDataImportWizardStep();
                    if (useractionlistener != null) {
                        calcStep.getComponent().setUseractionlistener(useractionlistener);
                    }
                    if (filterlistener != null) {
                        calcStep.getComponent().setFilterListener(filterlistener);
                    }
                }
                return calcStep;

            } else {
                if (csvStep == null) {
                    csvStep = new CSVDataImportWizardStep();
                    if (useractionlistener != null) {
                        csvStep.getComponent().setUseractionlistener(useractionlistener);
                    }
                    if (filterlistener != null) {
                        csvStep.getComponent().setFilterListener(filterlistener);
                    }
                }
                return csvStep;
            }
        }
    }

    public String name() {
        return steps[index];
    }

    public boolean hasNext() {
        return index < 1;
    }

    public boolean hasPrevious() {
        return index > 0;
    }

    public void nextStep() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        index++;
    }

    public void previousPanel() {
        if (!hasPrevious()) {
            throw new NoSuchElementException();
        }
        index--;
    }

    public REXPGenericVector instantiate() {
        System.gc();
        ARCTable arctable = ((DataImportPanel) current().getComponent()).getARCTable();
        return RUtil.createRDataFrame(arctable);
    }

    public void initialize(WizardPanel settings) {
        this.settings = settings;
    }

    public void uninitialize(WizardPanel settings) {
        if (this.settings != null) {
            //FIXIT: till i fix the memory leak
            if (current().getComponent() instanceof DataImportPanel) {
                DataImportPanel p = (DataImportPanel) current().getComponent();
                p.removePropertyChangeListener((PropertyChangeListener) current());
//                if (p.getARCTable().getModel() instanceof ImportTableModel) {
//                    p.getARCTable().setModel(new DefaultTableModel());
//                }
                p.setTableModelToNull();
            }

            this.settings = null;
            useractionlistener = null;
            filterlistener = null;
        }
    }

    public void setDataImportPanelUserActionListener(DataImportPanelUserActionListener useractionlistener) {
        this.useractionlistener = useractionlistener;
    }

    public void setDataImportTableFilterListener(FilterListener listener) {
        filterlistener = listener;
    }
}
