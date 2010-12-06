
/*
 * DataImportPanel.java
 *
 * Created on May 8, 2010, 5:41:50 PM
 */
package at.ac.ait.speedr.importwizard.steps;

import at.ac.ait.speedr.table.RTableCellEditor;
import at.ac.ait.speedr.table.RTableCellRenderer;
import at.ac.arcs.tablefilter.ARCTable;
import at.ac.arcs.tablefilter.events.FilterListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 *
 * @author visnei
 */
public class DataImportPanel extends javax.swing.JPanel {

    public static final String PROP_DELIMITER = "DELIMITER";
    public static final String PROP_QUALIFIER = "QUALIFIER";
    public static final String PROP_VARIABLENAME = "VARIABLENAME";
    private ImportTableModel tableModel = new ImportTableModel();
    private DataImportPanelUserActionListener useractionlistener = new DummyUserActionListener();
    private TableModelListener cellUpdateListener = new TableModelListener() {

        public void tableChanged(TableModelEvent e) {
            if (e.getType() == TableModelEvent.UPDATE
                    && e.getColumn() != TableModelEvent.ALL_COLUMNS
                    && e.getColumn() != TableModelEvent.HEADER_ROW) {
                int row = e.getFirstRow();
                int col = e.getColumn();
                Object value = tableModel.getValueAt(row, col);
                useractionlistener.tableCellValueChanged(value, row, col);
            }
        }
    };
    private boolean updateEnds = true;

    /** Creates new form DataImportPanel */
    public DataImportPanel(boolean showConfigurationPanel) {
        super();
        initComponents();
        configurationPanel.setVisible(showConfigurationPanel);
        tableModel.addPropertyChangeListener(new TableModelPropertyChangeListener());
        tableModel.addTableModelListener(cellUpdateListener);

        RTableCellRenderer cr = new RTableCellRenderer(table.getDefaultRenderer(Object.class));

        table.setDefaultRenderer(Object.class, cr);
        table.setDefaultRenderer(Integer.class, cr);
        table.setDefaultRenderer(Double.class, cr);
        table.setDefaultRenderer(String.class, cr);

        RTableCellEditor editor = new RTableCellEditor(table.getDefaultEditor(Object.class));

        table.setDefaultEditor(Object.class, editor);
        table.setDefaultEditor(Integer.class, editor);
        table.setDefaultEditor(Double.class, editor);
        table.setDefaultEditor(String.class, editor);

        table.setFilterRowHeaderVisibleColumnsMask(ARCTable.FRH_SHOW_COUNT_MASK
                | ARCTable.FRH_COLOR_MASK);
        table.setTableRowHeaderVisibleColumnsMask(ARCTable.TRH_MODEL_INDEX_MASK);
        table.setGroupingEnabled(false);
    }

    public ImportTableModel getTableModel() {
        return tableModel;
    }

    //FIXME: temporary solution for memory leak
    public void setTableModelToNull() {
        tableModel = null;
        table = null;
    }

    public ARCTable getARCTable() {
        return table;
    }

    public void setConfigurationPanelEnabled(boolean enabled) {
        rb_Comma.setEnabled(enabled);
        rb_OtherDelimiter.setEnabled(enabled);
        tf_OtherDelimiter.setEnabled(enabled);
        rb_Semicolon.setEnabled(enabled);
        rb_Tab.setEnabled(enabled);
        rb_Space.setEnabled(enabled);
        rb_DoubleQuote.setEnabled(enabled);
        rb_SingleQuote.setEnabled(enabled);
        rb_NoQuote.setEnabled(enabled);
        rb_OtherQuote.setEnabled(enabled);
        tf_otherQuote.setEnabled(enabled);

        rowsStart.setEnabled(enabled);
        rowsEnd.setEnabled(enabled);
        colStart.setEnabled(enabled);
        colEnd.setEnabled(enabled);
        cb_HeaderRow.setEnabled(enabled);
        if (tableModel.getMaxColumnCount() != 1) {
            cb_RowNames.setEnabled(enabled);
        }
        rowNamesColumnIndex.setEnabled(cb_RowNames.isSelected());

        variablename.setEditable(enabled);
        variablename.setEnabled(enabled);

        if (enabled) {
            updateEnds = false;
            colEnd.setValue(tableModel.getColEnd() + 1);
            if (tableModel.hasRowNames()) {
                ((SpinnerNumberModel) colEnd.getModel()).setMaximum(tableModel.getMaxColumnCount() - 1);
            } else {
                ((SpinnerNumberModel) colEnd.getModel()).setMaximum(tableModel.getMaxColumnCount());
            }

            ((SpinnerNumberModel) rowsEnd.getModel()).setMaximum(tableModel.getMaxRowCount());
            updateEnds = true;
        }
    }

    public void resetTableModel() {
        tableModel = new ImportTableModel();
        tableModel.addPropertyChangeListener(new TableModelPropertyChangeListener());
        table.setModel(tableModel);
    }

    public void resetRowAndColumnsMaxAndMin() {
        ((SpinnerNumberModel) colStart.getModel()).setMinimum(1);
        ((SpinnerNumberModel) colEnd.getModel()).setMaximum(null);
        ((SpinnerNumberModel) rowsStart.getModel()).setMinimum(1);
        ((SpinnerNumberModel) rowsEnd.getModel()).setMaximum(null);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        delimiterButtonGroup = new javax.swing.ButtonGroup();
        quoteButtonGroup = new javax.swing.ButtonGroup();
        table = new at.ac.arcs.tablefilter.ARCTable();
        configurationPanel = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        tf_OtherDelimiter = new javax.swing.JTextField();
        rb_Semicolon = new javax.swing.JRadioButton();
        jLabel5 = new javax.swing.JLabel();
        rb_OtherDelimiter = new javax.swing.JRadioButton();
        rb_Tab = new javax.swing.JRadioButton();
        jLabel8 = new javax.swing.JLabel();
        rb_Comma = new javax.swing.JRadioButton();
        rb_DoubleQuote = new javax.swing.JRadioButton();
        rb_SingleQuote = new javax.swing.JRadioButton();
        rb_NoQuote = new javax.swing.JRadioButton();
        rb_Space = new javax.swing.JRadioButton();
        rb_OtherQuote = new javax.swing.JRadioButton();
        tf_otherQuote = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        cb_HeaderRow = new javax.swing.JCheckBox();
        rowNamesColumnIndex = new javax.swing.JSpinner();
        cb_RowNames = new javax.swing.JCheckBox();
        jLabel3 = new javax.swing.JLabel();
        colEnd = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        rowsEnd = new javax.swing.JSpinner();
        jLabel6 = new javax.swing.JLabel();
        rowsStart = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        colStart = new javax.swing.JSpinner();
        colNamesRowIndex = new javax.swing.JSpinner();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        variablename = new javax.swing.JTextField();

        table.setHorizontalScrollEnabled(true);
        table.setInputSelectorTableEnabled(false);
        table.setModel(tableModel);

        jLabel7.setText("Separator");

        tf_OtherDelimiter.setEditable(false);
        tf_OtherDelimiter.setEnabled(false);
        tf_OtherDelimiter.setDocument(new DelimiterDocument());

        delimiterButtonGroup.add(rb_Semicolon);
        rb_Semicolon.setText("semicolon (;)");
        rb_Semicolon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb_SemicolonActionPerformed(evt);
            }
        });

        jLabel5.setText("Configuration");

        delimiterButtonGroup.add(rb_OtherDelimiter);
        rb_OtherDelimiter.setText("other");
        rb_OtherDelimiter.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rb_OtherDelimiterItemStateChanged(evt);
            }
        });

        delimiterButtonGroup.add(rb_Tab);
        rb_Tab.setText("tab (\\t)");
        rb_Tab.setName("TAB"); // NOI18N
        rb_Tab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb_TabActionPerformed(evt);
            }
        });

        jLabel8.setText("Quote");

        delimiterButtonGroup.add(rb_Comma);
        rb_Comma.setText("comma (,)");
        rb_Comma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb_CommaActionPerformed(evt);
            }
        });

        quoteButtonGroup.add(rb_DoubleQuote);
        rb_DoubleQuote.setText("double quote (\")");
        rb_DoubleQuote.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb_DoubleQuoteActionPerformed(evt);
            }
        });

        quoteButtonGroup.add(rb_SingleQuote);
        rb_SingleQuote.setText("single quote (')");
        rb_SingleQuote.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb_SingleQuoteActionPerformed(evt);
            }
        });

        quoteButtonGroup.add(rb_NoQuote);
        rb_NoQuote.setText("none");
        rb_NoQuote.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb_NoQuoteActionPerformed(evt);
            }
        });

        delimiterButtonGroup.add(rb_Space);
        rb_Space.setText("space ( )");
        rb_Space.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb_SpaceActionPerformed(evt);
            }
        });

        quoteButtonGroup.add(rb_OtherQuote);
        rb_OtherQuote.setText("other");
        rb_OtherQuote.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rb_OtherQuoteItemStateChanged(evt);
            }
        });

        tf_otherQuote.setEditable(false);
        tf_otherQuote.setDocument(new QuoteDocument());

        javax.swing.GroupLayout configurationPanelLayout = new javax.swing.GroupLayout(configurationPanel);
        configurationPanel.setLayout(configurationPanelLayout);
        configurationPanelLayout.setHorizontalGroup(
            configurationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(configurationPanelLayout.createSequentialGroup()
                .addGroup(configurationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addGroup(configurationPanelLayout.createSequentialGroup()
                        .addGroup(configurationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(configurationPanelLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel7))
                            .addGroup(configurationPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel8)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(configurationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rb_Tab)
                            .addComponent(rb_DoubleQuote))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(configurationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rb_SingleQuote)
                            .addComponent(rb_Semicolon))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(configurationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rb_Comma)
                            .addComponent(rb_NoQuote))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(configurationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(configurationPanelLayout.createSequentialGroup()
                                .addComponent(rb_Space)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rb_OtherDelimiter)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tf_OtherDelimiter, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(configurationPanelLayout.createSequentialGroup()
                                .addComponent(rb_OtherQuote)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tf_otherQuote, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        configurationPanelLayout.setVerticalGroup(
            configurationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(configurationPanelLayout.createSequentialGroup()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(configurationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(rb_Tab)
                    .addComponent(rb_Semicolon)
                    .addComponent(rb_Comma)
                    .addComponent(rb_Space)
                    .addComponent(rb_OtherDelimiter)
                    .addComponent(tf_OtherDelimiter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(configurationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(rb_DoubleQuote)
                    .addComponent(rb_SingleQuote)
                    .addComponent(rb_NoQuote)
                    .addComponent(rb_OtherQuote)
                    .addComponent(tf_otherQuote, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel2.setText("Rows: from");

        cb_HeaderRow.setText("Use row");
        cb_HeaderRow.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cb_HeaderRowItemStateChanged(evt);
            }
        });
        cb_HeaderRow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_HeaderRowActionPerformed(evt);
            }
        });

        rowNamesColumnIndex.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
        rowNamesColumnIndex.setEnabled(false);
        rowNamesColumnIndex.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rowNamesColumnIndexStateChanged(evt);
            }
        });

        cb_RowNames.setText("Use column");
        cb_RowNames.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_RowNamesActionPerformed(evt);
            }
        });

        jLabel3.setText("to");

        colEnd.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
        colEnd.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                colEndStateChanged(evt);
            }
        });

        jLabel4.setText("to");

        rowsEnd.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
        rowsEnd.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rowsEndStateChanged(evt);
            }
        });

        jLabel6.setText("Columns: from");

        rowsStart.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
        rowsStart.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rowsStartStateChanged(evt);
            }
        });

        jLabel1.setText("Data range");

        colStart.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
        colStart.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                colStartStateChanged(evt);
            }
        });

        colNamesRowIndex.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
        colNamesRowIndex.setEnabled(false);
        colNamesRowIndex.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                colNamesRowIndexStateChanged(evt);
            }
        });

        jLabel10.setText("for col.names");

        jLabel11.setText("for row.names");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(colStart)
                            .addComponent(rowsStart, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rowsEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(colEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cb_HeaderRow)
                            .addComponent(cb_RowNames))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(rowNamesColumnIndex)
                            .addComponent(colNamesRowIndex, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(jLabel10))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addComponent(jLabel1)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(rowsStart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3)
                        .addComponent(rowsEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2)
                        .addComponent(colNamesRowIndex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cb_HeaderRow)
                        .addComponent(jLabel10))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(colStart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4)
                        .addComponent(colEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rowNamesColumnIndex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cb_RowNames)
                    .addComponent(jLabel11)))
        );

        jLabel9.setText("Set variable name");

        variablename.setText("temp");
        variablename.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                firePropertyChange(PROP_VARIABLENAME, null, null);
                useractionlistener.variableNameChanged(variablename.getText());
            }

            public void removeUpdate(DocumentEvent e) {
                firePropertyChange(PROP_VARIABLENAME, null, null);
                useractionlistener.variableNameChanged(variablename.getText());
            }

            public void changedUpdate(DocumentEvent e) {

            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(variablename, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(table, javax.swing.GroupLayout.DEFAULT_SIZE, 531, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(configurationPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(configurationPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(variablename, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(table, javax.swing.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cb_RowNamesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_RowNamesActionPerformed
        rowNamesColumnIndex.setEnabled(cb_RowNames.isSelected());
        ((SpinnerNumberModel) rowNamesColumnIndex.getModel()).setMaximum(tableModel.getColumnCount());
        tableModel.setHasRowNames(cb_RowNames.isSelected());
        useractionlistener.hasRowNamesChanged(cb_RowNames.isSelected());
    }//GEN-LAST:event_cb_RowNamesActionPerformed

    private void cb_HeaderRowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_HeaderRowActionPerformed
        tableModel.setHasColumnNames(cb_HeaderRow.isSelected());
        useractionlistener.hasRowHeaderChanged(cb_HeaderRow.isSelected());
    }//GEN-LAST:event_cb_HeaderRowActionPerformed

    private void rowsStartStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rowsStartStateChanged
        Integer newvalue = (Integer) rowsStart.getValue();

        if (newvalue <= (Integer) rowsEnd.getValue()) {
            tableModel.setRowStart(newvalue - 1);
            useractionlistener.rowStartChanged(newvalue);
        } else {
            rowsStart.setValue(rowsStart.getPreviousValue());
        }

    }//GEN-LAST:event_rowsStartStateChanged

    private void rowsEndStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rowsEndStateChanged
        int newvalue = (Integer) rowsEnd.getValue();

        if (newvalue >= (Integer) rowsStart.getValue()) {
            tableModel.setRowEnd(newvalue - 1);
            if (updateEnds) {
                useractionlistener.rowEndChange(newvalue);
            }
        } else {
            rowsEnd.setValue(rowsEnd.getPreviousValue());
        }
    }//GEN-LAST:event_rowsEndStateChanged

    private void colStartStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_colStartStateChanged
        Integer newvalue = (Integer) colStart.getValue();

        if (newvalue <= (Integer) colEnd.getValue()) {
            tableModel.setColStart(newvalue - 1);
            useractionlistener.colStartChanged(newvalue);
        } else {
            colStart.setValue(colStart.getPreviousValue());
        }
    }//GEN-LAST:event_colStartStateChanged

    private void colEndStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_colEndStateChanged
        int newvalue = (Integer) colEnd.getValue();

        if (newvalue >= (Integer) colStart.getValue()) {
            tableModel.setColEnd(newvalue - 1);
            if (updateEnds) {
                useractionlistener.colEndChange(newvalue);
            }
        } else {
            colEnd.setValue(colEnd.getPreviousValue());
        }
    }//GEN-LAST:event_colEndStateChanged

    private void rb_NoQuoteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb_NoQuoteActionPerformed
        firePropertyChange(PROP_QUALIFIER, null, null);
        useractionlistener.quoteChanged(null);
}//GEN-LAST:event_rb_NoQuoteActionPerformed

    private void rb_DoubleQuoteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb_DoubleQuoteActionPerformed
        firePropertyChange(PROP_QUALIFIER, null, '"');
        useractionlistener.quoteChanged("\\\"");
}//GEN-LAST:event_rb_DoubleQuoteActionPerformed

    private void rb_SingleQuoteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb_SingleQuoteActionPerformed
        firePropertyChange(PROP_QUALIFIER, null, '\'');
        useractionlistener.quoteChanged("'");
}//GEN-LAST:event_rb_SingleQuoteActionPerformed

    private void rb_SemicolonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb_SemicolonActionPerformed
        firePropertyChange(PROP_DELIMITER, null, ';');
        useractionlistener.separatorChanged(";");
}//GEN-LAST:event_rb_SemicolonActionPerformed

    private void rb_TabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb_TabActionPerformed
        firePropertyChange(PROP_DELIMITER, null, '\t');
        useractionlistener.separatorChanged("\\t");
}//GEN-LAST:event_rb_TabActionPerformed

    private void rb_SpaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb_SpaceActionPerformed
        firePropertyChange(PROP_DELIMITER, null, ' ');
        useractionlistener.separatorChanged(" ");
}//GEN-LAST:event_rb_SpaceActionPerformed

    private void rb_CommaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb_CommaActionPerformed
        firePropertyChange(PROP_DELIMITER, null, ',');
        useractionlistener.separatorChanged(",");
}//GEN-LAST:event_rb_CommaActionPerformed

    private void rowNamesColumnIndexStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rowNamesColumnIndexStateChanged
        int newvalue = (Integer) rowNamesColumnIndex.getValue();
        tableModel.setRowNamesColumnIndex(newvalue - 1);
        useractionlistener.rowNamesNoChanged(newvalue);
    }//GEN-LAST:event_rowNamesColumnIndexStateChanged

    private void cb_HeaderRowItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cb_HeaderRowItemStateChanged
        colNamesRowIndex.setEnabled(cb_HeaderRow.isSelected());
    }//GEN-LAST:event_cb_HeaderRowItemStateChanged

    private void colNamesRowIndexStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_colNamesRowIndexStateChanged
        int newvalue = (Integer) colNamesRowIndex.getValue();
        tableModel.setColumnNamesRowIndex(newvalue - 1); // in the table model index begins with 0 instead of 1

        ((SpinnerNumberModel) rowsStart.getModel()).setMinimum(newvalue + 1);
        useractionlistener.columnNamesRowIndexChanged(newvalue);
    }//GEN-LAST:event_colNamesRowIndexStateChanged

    private void rb_OtherDelimiterItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rb_OtherDelimiterItemStateChanged
        tf_OtherDelimiter.setEditable(rb_OtherDelimiter.isSelected());
        tf_OtherDelimiter.setEnabled(rb_OtherDelimiter.isSelected());

    }//GEN-LAST:event_rb_OtherDelimiterItemStateChanged

    private void rb_OtherQuoteItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rb_OtherQuoteItemStateChanged
        tf_otherQuote.setEditable(rb_OtherQuote.isSelected());
        tf_otherQuote.setEnabled(rb_OtherQuote.isSelected());
    }//GEN-LAST:event_rb_OtherQuoteItemStateChanged

    public void setDelimiter(char delimiter) {
        if (delimiter == ',') {
            rb_Comma.setSelected(true);
        } else if (delimiter == ';') {
            rb_Semicolon.setSelected(true);
        } else if (delimiter == '\t') {
            rb_Tab.setSelected(true);
            useractionlistener.separatorChanged("\\t");
            return;
        } else if (delimiter == ' ') {
            rb_Space.setSelected(true);
        } else if (!tf_OtherDelimiter.getText().equals("" + delimiter)) {
            tf_OtherDelimiter.setText("" + delimiter);
        }
        useractionlistener.separatorChanged("" + delimiter);
    }

    public void setQualifer(char qualifer) {
        if (qualifer == '"') {
            rb_DoubleQuote.setSelected(true);
            useractionlistener.quoteChanged("\\\"");
        } else if (qualifer == '\'') {
            rb_SingleQuote.setSelected(true);
            useractionlistener.quoteChanged("" + qualifer);
        } else if (qualifer == '\0') {
            rb_NoQuote.setSelected(true);
            useractionlistener.quoteChanged(null);
        } else {
            if (!tf_otherQuote.getText().equals("" + qualifer)) {
                tf_otherQuote.setText("" + qualifer);
            }
        }
    }

    public String getVariableName() {
        return variablename.getText();
    }

    public void setRowsStart(int i) {
        rowsStart.setValue(i);
    }

    public void setRowsEnd(int i) {
        rowsEnd.setValue(i);
    }

    public void setColStart(int i) {
        colStart.setValue(i);
    }

    public void setColEnd(int i) {
        colEnd.setValue(i);
    }

    public void setCb_RowNames(boolean b) {
        cb_RowNames.setSelected(b);
    }

    public void setRowNamesNo(int i) {
        rowNamesColumnIndex.setValue(i);
    }

    public void setCb_HeaderRow(boolean b) {
        cb_HeaderRow.setSelected(b);
    }

    public void setVariablename(String varname) {
        variablename.setText(varname);
    }

    public void setUseractionlistener(DataImportPanelUserActionListener useractionlistener) {
        this.useractionlistener = useractionlistener;
    }

    public void setFilterListener(FilterListener listener) {
        table.addFilterListener(listener);
    }

    private class TableModelPropertyChangeListener implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName() == ImportTableModel.PROP.HASCOLUMNNAMES.name()) {
                Boolean oldValue = cb_HeaderRow.isSelected();
                Boolean newValue = (Boolean) evt.getNewValue();

                if (oldValue != newValue) {
                    cb_HeaderRow.setSelected(newValue);
                }

                if (newValue) {
                    ((SpinnerNumberModel) rowsStart.getModel()).setMinimum(tableModel.getColumnNamesRowIndex() + 2);
                } else {
                    ((SpinnerNumberModel) rowsStart.getModel()).setMinimum(1);
                }

                useractionlistener.hasRowHeaderChanged(newValue);
            } else if (evt.getPropertyName() == ImportTableModel.PROP.COLUMNNAMESROWINDEX.name()) {
                Integer oldValue = (Integer) colNamesRowIndex.getValue();
                Integer newValue = tableModel.getColumnNamesRowIndex() + 1;

                if (oldValue != newValue) {
                    colNamesRowIndex.setValue(newValue);
                }
            } else if (evt.getPropertyName() == ImportTableModel.PROP.RANGE_ROWSTART.name()) {
                Integer oldValue = (Integer) rowsStart.getValue();
                Integer newValue = (Integer) evt.getNewValue() + 1;

                if (oldValue != newValue) {
                    rowsStart.setValue(newValue);
                }
                useractionlistener.rowStartChanged(newValue);
            } else if (evt.getPropertyName() == ImportTableModel.PROP.RANGE_ROWEND.name()) {

                Integer oldValue = (Integer) rowsEnd.getValue();
                Integer newValue = (Integer) evt.getNewValue() + 1;

                if (oldValue != newValue) {
                    updateEnds = false;
                    rowsEnd.setValue(newValue);
                    updateEnds = true;
                }
//                useractionlistener.rowEndChange(newValue);
            } else if (evt.getPropertyName() == ImportTableModel.PROP.RANGE_COLSTART.name()) {
                Integer oldValue = (Integer) colStart.getValue();
                Integer newValue = (Integer) evt.getNewValue() + 1;

                if (oldValue != newValue) {
                    colStart.setValue(newValue);
                }
                useractionlistener.colStartChanged(newValue);
            } else if (evt.getPropertyName() == ImportTableModel.PROP.RANGE_COLEND.name()) {
                Integer oldValue = (Integer) colEnd.getValue();
                Integer newValue = (Integer) evt.getNewValue() + 1;

                if (oldValue != newValue) {
                    updateEnds = false;
                    colEnd.setValue(newValue);
                    updateEnds = true;
                }
//                useractionlistener.colEndChange(newValue);
            } else if (evt.getPropertyName() == ImportTableModel.PROP.HASROWNAMES.name()) {
                if (cb_RowNames.isSelected() != evt.getNewValue()) {
                    cb_RowNames.setSelected((Boolean) evt.getNewValue());
                }

                if (tableModel.hasRowNames()) {
                    ((SpinnerNumberModel) colEnd.getModel()).setMaximum(tableModel.getMaxColumnCount() - 1);
                } else {
                    ((SpinnerNumberModel) colEnd.getModel()).setMaximum(tableModel.getMaxColumnCount());
                }

                useractionlistener.hasRowNamesChanged((Boolean) evt.getNewValue());
            } else if (evt.getPropertyName() == ImportTableModel.PROP.ROWNAMESCOLUMNINDEX.name()) {
                Integer oldValue = (Integer) rowNamesColumnIndex.getValue();
                Integer newValue = (Integer) evt.getNewValue() + 1;

                if (oldValue != newValue) {
                    rowNamesColumnIndex.setValue(newValue);
                }
                useractionlistener.rowNamesNoChanged(newValue);
            }
        }
    }

    private class DummyUserActionListener implements DataImportPanelUserActionListener {

        public void separatorChanged(String newvalue) {
        }

        public void quoteChanged(String newvalue) {
        }

        public void rowStartChanged(int newvalue) {
        }

        public void rowEndChange(int newvalue) {
        }

        public void colStartChanged(int newvalue) {
        }

        public void colEndChange(int newvalue) {
        }

        public void hasRowHeaderChanged(Boolean newvalue) {
        }

        public void hasRowNamesChanged(Boolean newvalue) {
        }

        public void rowNamesNoChanged(int newvalue) {
        }

        public void variableNameChanged(String newvalue) {
        }

        public void columnNamesRowIndexChanged(int newvalue) {
        }

        public void tableCellValueChanged(Object aValue, int rowIndex, int columnIndex) {
        }
    }

    /**
     * accepts only single char delimiter
     */
    private class DelimiterDocument extends PlainDocument {

        @Override
        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            if (getLength() > 0) {
                super.remove(0, 1);
            }
            if (getLength() > 0) {
                str = "" + str.charAt(0);
            }
            super.insertString(0, str, a);
            firePropertyChange(PROP_DELIMITER, null, str.charAt(0));
            useractionlistener.separatorChanged(str);
        }
    }

    /**
     * accepts only single char quote
     */
    private class QuoteDocument extends PlainDocument {

        @Override
        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            if (str == null) {
                super.insertString(0, str, a);
                return;
            }
            if (getLength() > 0) {
                super.remove(0, 1);
            }
            if (str.length() > 0) {
                str = "" + str.charAt(0);
            }
            super.insertString(0, str, a);
            firePropertyChange(PROP_QUALIFIER, null, str.charAt(0));
            useractionlistener.quoteChanged(str);
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox cb_HeaderRow;
    private javax.swing.JCheckBox cb_RowNames;
    private javax.swing.JSpinner colEnd;
    private javax.swing.JSpinner colNamesRowIndex;
    private javax.swing.JSpinner colStart;
    private javax.swing.JPanel configurationPanel;
    private javax.swing.ButtonGroup delimiterButtonGroup;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.ButtonGroup quoteButtonGroup;
    private javax.swing.JRadioButton rb_Comma;
    private javax.swing.JRadioButton rb_DoubleQuote;
    private javax.swing.JRadioButton rb_NoQuote;
    private javax.swing.JRadioButton rb_OtherDelimiter;
    private javax.swing.JRadioButton rb_OtherQuote;
    private javax.swing.JRadioButton rb_Semicolon;
    private javax.swing.JRadioButton rb_SingleQuote;
    private javax.swing.JRadioButton rb_Space;
    private javax.swing.JRadioButton rb_Tab;
    private javax.swing.JSpinner rowNamesColumnIndex;
    private javax.swing.JSpinner rowsEnd;
    private javax.swing.JSpinner rowsStart;
    private at.ac.arcs.tablefilter.ARCTable table;
    private javax.swing.JTextField tf_OtherDelimiter;
    private javax.swing.JTextField tf_otherQuote;
    private javax.swing.JTextField variablename;
    // End of variables declaration//GEN-END:variables
}
