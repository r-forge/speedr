/*
 * SpeedRFrame.java
 *
 * Created on Sep 26, 2009, 10:08:57 AM
 */
package at.ac.ait.speedr;

import at.ac.ait.speedr.codegen.ImportAndFilterCodeGen;
import at.ac.ait.speedr.codegen.FilterFunctionCodeGen;
import at.ac.ait.dockingframes.theme.AITEclipseTheme;
import at.ac.ait.speedr.importwizard.WizardPanel;
import bibliothek.gui.dock.common.event.CVetoClosingEvent;
import java.beans.PropertyChangeEvent;
import at.ac.arcs.tablefilter.ARCTable;
import bibliothek.gui.dock.common.CControl;
import bibliothek.gui.dock.common.CLocation;
import bibliothek.gui.dock.common.CWorkingArea;
import bibliothek.gui.dock.common.DefaultMultipleCDockable;
import bibliothek.gui.dock.common.DefaultSingleCDockable;
import bibliothek.gui.dock.common.EmptyMultipleCDockableFactory;
import bibliothek.gui.dock.common.MultipleCDockable;
import bibliothek.gui.dock.common.MultipleCDockableFactory;
import bibliothek.gui.dock.common.MultipleCDockableLayout;
import bibliothek.gui.dock.common.action.CButton;
import bibliothek.gui.dock.common.action.predefined.CCloseAction;
import bibliothek.gui.dock.common.action.predefined.CNormalizeAction;
import bibliothek.gui.dock.common.event.CFocusListener;
import bibliothek.gui.dock.common.event.CVetoClosingListener;
import bibliothek.gui.dock.common.intern.CDockable;
import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;
import java.awt.BorderLayout;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import at.ac.ait.speedr.importwizard.WizardIterator;
import at.ac.ait.speedr.importwizard.steps.DataImportPanel;
import at.ac.ait.speedr.importwizard.steps.DataSourceWizardStep;
import at.ac.ait.speedr.table.POSIXctCellRenderer;
import at.ac.ait.speedr.table.RDate;
import at.ac.ait.speedr.table.RDateTimeConverter;
import at.ac.ait.speedr.table.RPOSIXct;
import at.ac.ait.speedr.table.model.RAbstractTableModel;
import at.ac.ait.speedr.table.RTableCellEditor;
import at.ac.ait.speedr.table.RTableCellRenderer;
import at.ac.ait.speedr.workspace.RConnection;
import at.ac.ait.speedr.workspace.RUtil;
import at.ac.arcs.tablefilter.cell.DateCellRenderer;
import at.ac.arcs.tablefilter.events.FilterEvent;
import at.ac.arcs.tablefilter.events.FilterListener;
import at.ac.arcs.tablefilter.filtermodel.DateFilterDevice;
import au.com.bytecode.opencsv.CSVWriter;
import bibliothek.gui.DockTheme;
import bibliothek.gui.dock.common.action.CAction;
import bibliothek.gui.dock.common.action.CMenu;
import bibliothek.gui.dock.common.theme.CDockThemeFactory;
import bibliothek.gui.dock.common.theme.CEclipseTheme;
import bibliothek.gui.dock.common.theme.ThemeMap;
import bibliothek.gui.dock.themes.ThemeFactory;
import bibliothek.gui.dock.themes.ThemePropertyFactory;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JFileChooser;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.rosuda.REngine.REXPGenericVector;

/**
 *
 * @author visnei
 */
public class SpeedRFrame extends javax.swing.JFrame {

    private static final Logger logger = Logger.getLogger(SpeedRFrame.class.getName());
    private static SpeedRFrame rif;

    private static SpeedRFrame getInstance() throws REngineException, REXPMismatchException {
        if (rif == null) {
            rif = new SpeedRFrame();
        }

        return rif;
    }
    private MultipleCDockableFactory<MultipleCDockable, MultipleCDockableLayout> multiDockFactory =
            new EmptyMultipleCDockableFactory<MultipleCDockable>() {

                @Override
                public MultipleCDockable createDockable() {
                    return new DefaultMultipleCDockable(this);
                }
            };
    private CControl control;
    private CWorkingArea tablesWorkArea;
    private DefaultSingleCDockable filtercode_dock;
    private DefaultMultipleCDockable activeDock;
    private DockCloseAction activeDockCloaseAction;
    private RSyntaxTextArea textArea;
    private ImageIcon exportIcon;
    private WorkspaceView workspace;

    private CFocusListener filterCodeDockTitleUpdater = new CFocusListener() {

        public void focusGained(CDockable dock) {
            filtercode_dock.setTitleText("Filter Code - "
                    + ((DefaultMultipleCDockable) dock).getTitleText());
        }

        public void focusLost(CDockable dock) {
            filtercode_dock.setTitleText("Filter Code");
        }
    };

    private SpeedRFrame() {
        initComponents();
        initDocks();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("speedR");
        setMinimumSize(new java.awt.Dimension(850, 650));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if (activeDock != null) {
            activeDockCloaseAction.close(activeDock);
            filtercode_dock.setTitleText("Filter Code");
            System.gc();
        }
    }//GEN-LAST:event_formWindowClosing

    private void initDocks() {
        try {
            exportIcon = new ImageIcon(SpeedRFrame.class.getResource("/at/ac/ait/speedr/icons/export_16.png"));
            control = new CControl(this);
            ThemeMap tm = control.getThemes();
            final AITEclipseTheme theme = new AITEclipseTheme();
            ThemeFactory arcseclipse = new CDockThemeFactory<AITEclipseTheme>(
                    new ThemePropertyFactory<AITEclipseTheme>(AITEclipseTheme.class), control) {

                @Override
                public DockTheme create(CControl control) {
                    return new CEclipseTheme(control, theme);
                }
            };
            tm.add("KEY_ARCS_THEME", arcseclipse);
            control.setTheme("KEY_ARCS_THEME");

            control.addMultipleDockableFactory("Tables", multiDockFactory);
            add(control.getContentArea(), BorderLayout.CENTER);
            activeDockCloaseAction = new DockCloseAction(control);
            tablesWorkArea = control.createWorkingArea("tablesworkarea");

            DefaultSingleCDockable objmgr_dock = createObjectManagerDock();
            DefaultSingleCDockable filterCodeAreaDock = initFilterCodeAreaDock();
            objmgr_dock.setLocation(CLocation.base().normalWest(0.3));

            tablesWorkArea.setLocation(CLocation.base().normalEast(0.7));

            filterCodeAreaDock.setLocation(CLocation.base().normalEast(0.7).south(0.2));
            filterCodeAreaDock.setLocation(CLocation.base().minimalSouth());
            filterCodeAreaDock.setLocation(CLocation.base().normalEast(0.7).south(0.2));

            objmgr_dock.setLocation(CLocation.base().minimalWest());
            objmgr_dock.setLocation(CLocation.base().normalWest(0.3));

            tablesWorkArea.setVisible(true);
        } catch (REngineException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        } catch (REXPMismatchException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    private DefaultSingleCDockable createObjectManagerDock() throws REngineException, REXPMismatchException {
        workspace = new WorkspaceView(this);

        workspace.reload();

        DefaultSingleCDockable objmgr_dock = new DefaultSingleCDockable("Object Browser", "Object Browser", workspace);

        objmgr_dock.setCloseable(false);
        objmgr_dock.setExternalizable(false);
        objmgr_dock.setMinimizable(false);
        objmgr_dock.setMaximizable(false);

        objmgr_dock.addAction(
                new CButton("Refresh the object browser",
                new ImageIcon(SpeedRFrame.class.getResource("/at/ac/ait/speedr/icons/arrow_circle_double.png"))) {

                    @Override
                    protected void action() {
                        try {
//                            refreshObjectBrowser();
                            workspace.reload();
                        } catch (Exception ex) {
                            Logger.getLogger(SpeedRFrame.class.getName()).log(Level.SEVERE, null, ex);
                            JOptionPane.showMessageDialog(workspace, "Can not reload workspace: " + ex.getMessage());
                        }
                    }
                });

        objmgr_dock.addAction(
                new CButton("Get Data",
                new ImageIcon(SpeedRFrame.class.getResource("/at/ac/ait/speedr/icons/import_16.png"))) {

                    @Override
                    protected void action() {
                        addImportDataDock();
                    }
                });

        control.addDockable(objmgr_dock);
        objmgr_dock.setVisible(true);
        return objmgr_dock;
    }

    private DefaultSingleCDockable initFilterCodeAreaDock() {
        textArea = new RSyntaxTextArea();
        textArea.setHighlightCurrentLine(false);
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        RTextScrollPane sp = new RTextScrollPane(textArea);
        sp.setBorder(null);

        filtercode_dock = new DefaultSingleCDockable("filtercode", "Filter code", sp);
        filtercode_dock.setCloseable(false);
        filtercode_dock.setExternalizable(false);
        filtercode_dock.setMinimizable(false);
        control.addDockable(filtercode_dock);
        filtercode_dock.putAction(CDockable.ACTION_KEY_NORMALIZE, new CNormalizeAction(control) {

            @Override
            public void action(CDockable dockable) {
                dockable.setLocation(CLocation.base().normalEast(0.7).south(0.3));
                super.action(dockable);
            }
        });

        filtercode_dock.setVisible(true);

        return filtercode_dock;
    }

    public void openTable(TableModel model, String objectname) {
        ARCTable arcTable = new ARCTable();

        arcTable.setHorizontalScrollEnabled(true);

        arcTable.setFilterRowHeaderVisibleColumnsMask(ARCTable.FRH_SHOW_COUNT_MASK
                | ARCTable.FRH_COLOR_MASK);
        arcTable.setTableRowHeaderVisibleColumnsMask(ARCTable.TRH_MODEL_INDEX_MASK);
        arcTable.setGroupingEnabled(false);
        arcTable.setModel(model);
        arcTable.setColumnSelectorVisible(false);

        arcTable.registerFilterDevice(RDate.class, new DateFilterDevice());
        arcTable.registerFilterDevice(RPOSIXct.class, new DateFilterDevice());
        arcTable.registerConverter(RDate.class, new RDateTimeConverter());
        arcTable.registerConverter(RPOSIXct.class, new RDateTimeConverter());

        addFilterTable(arcTable, objectname);
    }

    public DefaultMultipleCDockable addFilterTable(final ARCTable table, final String title) {
        textArea.setText("");
        if (activeDock != null) {
            activeDockCloaseAction.close(activeDock);
        }

        setDefaultRendererAndEditor(table);

        String functionname = title.replace("[[\"", "_").replace("\"]]", "") + "_filter";
        boolean hasRownames = table.getModel().getColumnName(0).equals("row.names")
                || table.getModel().getColumnName(0).equals("dimnames[[1]]");
        final FilterListener listener = new TableFilterListener(functionname,
                hasRownames, (RAbstractTableModel) table.getModel());

        table.addFilterListener(listener);

        DefaultMultipleCDockable docktable = createFilterTable(table, title);

        activeDock = docktable;

        docktable.addVetoClosingListener(new CVetoClosingListener() {

            public void closing(CVetoClosingEvent event) {
            }

            public void closed(CVetoClosingEvent event) {
                activeDock = null;
                table.setModel(new DefaultTableModel());
                table.removeFilterListener(listener);
            }
        });

        return docktable;
    }

    private void setDefaultRendererAndEditor(final ARCTable table) {
        RTableCellRenderer cr = new RTableCellRenderer(table.getDefaultRenderer(Object.class));
        table.setDefaultRenderer(Object.class, cr);
        table.setDefaultRenderer(Integer.class, cr);
        table.setDefaultRenderer(Double.class, cr);
        table.setDefaultRenderer(String.class, cr);
        table.setDefaultRenderer(Boolean.class, cr);
        table.setDefaultRenderer(RDate.class, new DateCellRenderer());
        table.setDefaultRenderer(RPOSIXct.class, new POSIXctCellRenderer());

        RTableCellEditor editor = new RTableCellEditor(table.getDefaultEditor(Object.class));
        table.setDefaultEditor(Object.class, editor);
        table.setDefaultEditor(Integer.class, editor);
        table.setDefaultEditor(Double.class, editor);
        table.setDefaultEditor(String.class, editor);
        table.setDefaultEditor(RDate.class, new RTableCellEditor(table.getDefaultEditor(Date.class)));
        table.setDefaultEditor(RPOSIXct.class, new RTableCellEditor(table.getDefaultEditor(Date.class)));

    }

    private DefaultMultipleCDockable createFilterTable(final ARCTable table, String title) {
        DefaultMultipleCDockable dockableTable = new DefaultMultipleCDockable(multiDockFactory, title, table);
        dockableTable.setRemoveOnClose(true);
        dockableTable.setCloseable(true);
        dockableTable.setMinimizable(false);
        dockableTable.setExternalizable(false);

        dockableTable.addAction(new ExportAction(table));

        dockableTable.addSeparator();

        tablesWorkArea.add(dockableTable);
        dockableTable.setVisible(true);
        dockableTable.toFront();

        dockableTable.addFocusListener(filterCodeDockTitleUpdater);

        return dockableTable;
    }

    private void addImportDataDock() {
        textArea.setText("");
        if (activeDock != null) {
            activeDockCloaseAction.close(activeDock);
            filtercode_dock.setTitleText("Filter Code");
        }

        final WizardIterator iterator = new WizardIterator();
        final WizardPanel wizardpanel = new WizardPanel(iterator);
        final DefaultMultipleCDockable dockable = createImportDataDock(wizardpanel);

        activeDock = dockable;

        final ImportAndFilterCodeGen importandfiltercodegen;
        try {
            importandfiltercodegen = new ImportAndFilterCodeGen();

            importandfiltercodegen.addChangeListener(new ChangeListener() {

                public void stateChanged(ChangeEvent e) {
                    textArea.setText(importandfiltercodegen.getImportAndFilterCode());
                }
            });

            iterator.setDataImportPanelUserActionListener(importandfiltercodegen);
            iterator.setDataImportTableFilterListener(importandfiltercodegen);

        } catch (IOException ex) {
            Logger.getLogger(SpeedRFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(SpeedRFrame.this, "Import and Filter code genarator could not be initialized!");
            return;
        }

        final PropertyChangeListener wpListener = new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName() == WizardPanel.PROP_STATE_CHANGED
                        && evt.getNewValue() == WizardPanel.PROP_STATE_FINISHED) {

                    logger.log(Level.INFO, "Finish state entry");

                    SwingWorker<Boolean, Object> worker = new SwingWorker<Boolean, Object>() {

                        @Override
                        protected Boolean doInBackground() throws Exception {
                            REXPGenericVector dataframe2 = wizardpanel.getDataframe();
                            String varname = (String) wizardpanel.getProperty(DataImportPanel.PROP_VARIABLENAME);
                            RConnection.exportDataFrame(varname, dataframe2);
                            return null;
                        }

                        @Override
                        protected void done() {
                            try {
                                get();
                                logger.log(Level.INFO, "export to R successfull.");
                                workspace.reload();
                            } catch (Exception ex) {
                                logger.log(Level.WARNING, ex.getMessage(), ex);
                                JOptionPane.showMessageDialog(SpeedRFrame.this, "Error during exporting to R:\n" + ex.getMessage(),
                                        "Error", JOptionPane.ERROR_MESSAGE);
                            } finally {
                                wizardpanel.displayProgress(false);
                                logger.log(Level.INFO, "Export to R finished.");
                            }
                        }
                    };
                    worker.execute();
                    try {
                        worker.get();
                    } catch (Exception ex) {
                        Logger.getLogger(SpeedRFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    activeDockCloaseAction.close(dockable);
                } else if (evt.getPropertyName() == WizardPanel.PROP_STATE_CHANGED
                        && evt.getNewValue() == WizardPanel.PROP_STATE_CANCELED) {
                    activeDockCloaseAction.close(dockable);
                } else if (DataSourceWizardStep.PROP_FILE.equals(evt.getPropertyName())) {
                    importandfiltercodegen.setFile(evt.getNewValue().toString().replaceAll("\\\\", "/"));
                    textArea.setText(importandfiltercodegen.getImportAndFilterCode());
                } else if (evt.getPropertyName() == WizardPanel.PROP_STATE_CHANGED
                        && evt.getNewValue() == WizardPanel.PROP_STATE_NEXT) {
                    importandfiltercodegen.setModel(((DataImportPanel) iterator.current().getComponent()).getTableModel());
                }
            }
        };

        wizardpanel.addPropertyChangeListener(wpListener);

        dockable.addVetoClosingListener(new CVetoClosingListener() {

            public void closing(CVetoClosingEvent event) {
                wizardpanel.removePropertyChangeListener(wpListener);
                //FIXIT: till i fix the memory leak
                wizardpanel.callUnininitialize();
                wizardpanel.setDataframeToNull();
                importandfiltercodegen.setModel(null);
                System.gc();
            }

            public void closed(CVetoClosingEvent event) {
            }
        });
    }

    private DefaultMultipleCDockable createImportDataDock(final WizardPanel wizardpanel) {
        DefaultMultipleCDockable dockable = new DefaultMultipleCDockable(multiDockFactory, "Get Data", wizardpanel);
        dockable.setRemoveOnClose(true);
        dockable.setCloseable(true);
        dockable.setMinimizable(false);
        dockable.setExternalizable(false);
        dockable.putAction(CDockable.ACTION_KEY_CLOSE, activeDockCloaseAction);
        tablesWorkArea.add(dockable);
        dockable.setVisible(true);
        return dockable;
    }

    private class DockCloseAction extends CCloseAction {

        public DockCloseAction(CControl control) {
            super(control);
        }

        @Override
        public void close(CDockable cd) {
            super.close(cd);
            activeDock = null;
        }
    }

    private class TableFilterListener implements FilterListener {

        private FilterFunctionCodeGen codegen;
        private String functionname;
        private RAbstractTableModel model;
        private boolean hasRownames = false;

        public TableFilterListener(String functionname, boolean hasRownames, RAbstractTableModel model) {
            this.functionname = functionname;
            this.model = model;
            this.hasRownames = hasRownames;
            try {
                codegen = new FilterFunctionCodeGen();
            } catch (IOException ex) {
                Logger.getLogger(SpeedRFrame.class.getName()).log(Level.SEVERE, "Filter function code genarator could not be initialized!", ex);
            }
        }

        public void filterChanged(FilterEvent event) {
            String filterFunctionCode =
                    codegen.getFilterFunctionCode(functionname, event.getFilterInfo(), hasRownames, model);
            textArea.setText(filterFunctionCode);
        }
    }

    private void exportTableToR(ARCTable arctable, String varname) throws REngineException, REXPMismatchException {
        REXPGenericVector dataFrame = RUtil.createRDataFrame(arctable);
        RConnection.exportDataFrame(varname, dataFrame);
    }

    private void exportTableToTabDelimitedFile(ARCTable table, File file) throws IOException {
        exportTableToFile(table, file, '\t', '\0');
    }

    private void exportTableToCSV(ARCTable table, File file) throws IOException {
        exportTableToFile(table, file, ',', '"');
    }

    private void exportTableToFile(ARCTable table, File file, char separator, char quote) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        CSVWriter csvwriter = new CSVWriter(writer, separator, quote);
        ArrayList<String> row = new ArrayList<String>(table.getColumnCount());

        for (int i = 0; i < table.getColumnCount(); i++) {
            row.add(table.getColumnName(i));
        }

        csvwriter.writeNext(row.toArray(new String[row.size()]));

        for (int rowIndex = 0; rowIndex < table.getRowCount(); rowIndex++) {
            row = new ArrayList<String>(table.getColumnCount());
            for (int columnIndex = 0; columnIndex < table.getColumnCount(); columnIndex++) {
                row.add(table.getValueAt(rowIndex, columnIndex) == null ? "" : table.getValueAt(rowIndex, columnIndex).toString());
            }
            csvwriter.writeNext(row.toArray(new String[row.size()]));
        }
        csvwriter.close();
    }

    private class ExportAction extends CMenu {

        private ARCTable table;
        private JFileChooser fc;

        public ExportAction(ARCTable table) {
            super("Export To ...", exportIcon);
            this.table = table;
            init();
        }

        private void init() {
            fc = new JFileChooser();

            add(createExportToRAction());
            add(createExportToCSVAction());
            add(createExportToTabDelimitedAction());
        }

        private CAction createExportToRAction() {
            return new CButton("Export to R Workspace", exportIcon) {

                @Override
                protected void action() {
                    try {
                        String varname = JOptionPane.showInputDialog("Enter a variable name for the new object");

                        if (workspace.hasVariable(varname)) {
                            int showConfirmDialog = JOptionPane.showConfirmDialog(table,
                                    "A variable with the name " + varname
                                    + " exsists already!\n" + "Do you want to replace it?");
                            if (showConfirmDialog == JOptionPane.YES_OPTION) {
                                exportTableToR(table, varname);
                            }
                        } else {
                            exportTableToR(table, varname);
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(rif, "Export failed!");
                        Logger.getLogger(SpeedRFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };
        }

        private CAction createExportToCSVAction() {
            return new CButton("Export as csv file", exportIcon) {

                @Override
                protected void action() {
                    int val = fc.showSaveDialog(table);
                    if (val == JFileChooser.APPROVE_OPTION) {
                        try {
                            File f = fc.getSelectedFile();
                            if (!f.getName().contains(".csv")) {
                                f = new File(f.getParent(), f.getName() + ".csv");
                            }
                            exportTableToCSV(table, f);
                        } catch (IOException ex) {
                            Logger.getLogger(SpeedRFrame.class.getName()).log(Level.SEVERE, null, ex);
                            JOptionPane.showMessageDialog(table, "Export failed: " + ex.getMessage());
                        }
                    }
                }
            };
        }

        private CAction createExportToTabDelimitedAction() {
            return new CButton("Export as tab-delimited (.txt) file", exportIcon) {

                @Override
                protected void action() {
                    int val = fc.showSaveDialog(table);
                    if (val == JFileChooser.APPROVE_OPTION) {
                        try {
                            File f = fc.getSelectedFile();
                            if (!f.getName().contains(".txt")) {
                                f = new File(f.getParent(), f.getName() + ".txt");
                            }
                            exportTableToTabDelimitedFile(table, f);
                        } catch (IOException ex) {
                            Logger.getLogger(SpeedRFrame.class.getName()).log(Level.SEVERE, null, ex);
                            JOptionPane.showMessageDialog(table, "Export failed: " + ex.getMessage());
                        }
                    }
                }
            };
        }
    }

    public static boolean isWindows() {

        String os = System.getProperty("os.name").toLowerCase();
        //windows
        return (os.indexOf("win") >= 0);

    }

    public static boolean isMac() {

        String os = System.getProperty("os.name").toLowerCase();
        //Mac
        return (os.indexOf("mac") >= 0);

    }

    public static boolean isUnix() {

        String os = System.getProperty("os.name").toLowerCase();
        //linux or unix
        return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0);

    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {

                if (!isMac()) {
                    try {
                        UIManager.setLookAndFeel(new Plastic3DLookAndFeel());
//                        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    } catch (Exception ex) {
                        logger.log(Level.WARNING, null, ex);
                    }
                }


                try {
                    SpeedRFrame rifInstance = getInstance();
                    rifInstance.setVisible(true);
                } catch (Exception ex) {
                    logger.log(Level.SEVERE, null, ex);
                }

            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
