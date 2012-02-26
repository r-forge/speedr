/*
 * WizardPanel.java
 *
 * Created on Sep 23, 2010, 1:32:39 PM
 */
package at.ac.ait.speedr.importwizard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.rosuda.REngine.REXPGenericVector;

/**
 *
 * @author visnei
 */
public class WizardPanel extends javax.swing.JPanel {

    private static final Logger logger = Logger.getLogger(WizardPanel.class.getName());
    public static final String PROP_STATE_CHANGED = "WizardPanel_stateChanged";
    public static final String PROP_STATE_FINISHED = "WizardPanel_stateFinished";
    public static final String PROP_STATE_NEXT = "WizardPanel_statenext";
    public static final String PROP_STATE_CANCELED = "WizardPanel_stateCanceled";
    public static final String PROP_ERROR_MESSAGE = "WizardPanel_errorMessage";
    public static final String PROP_WARNING_MESSAGE = "WizardPanel_warningMessage";
    public static final String PROP_INFO_MESSAGE = "WizardPanel_infoMessage";
    private static final int MSG_TYPE_ERROR = 1;
    private static final int MSG_TYPE_WARNING = 2;
    private static final int MSG_TYPE_INFO = 3;
    private static ImageIcon errorIcon;
    private static ImageIcon warningIcon;
    private static ImageIcon infoIcon;
    private static final Color errorForeground = new Color(255, 0, 0);
    private static final Color warningForeground = new Color(51, 51, 51);
    private static final Color infoForeground = UIManager.getColor("Label.foreground");
    /** hashtable with additional settings that is usually used
     * by Panels to store their data
     */
    private JLabel messageLabel = new JLabel();
    private HashMap<String, Object> properties = new HashMap<String, Object>(7);
    private WizardIterator iterator;
    private volatile REXPGenericVector dataframe;
    private boolean isValid = false;
    private boolean isBusy = false;
    private ChangeListener changeListener =
            new ChangeListener() {

                public void stateChanged(ChangeEvent e) {
                    checkValid();
                    updateButtons();
                }
            };

    /** Creates new form WizardPanel */
    public WizardPanel(WizardIterator iterator) {
        initComponents();
        this.iterator = iterator;
        initialize();
    }

    private void initialize() {
        iterator.initialize(this);

        if (iterator.hasNext()) {
            nextbutton.setEnabled(true && isValid);
            finishbutton.setEnabled(false);
            setUpStep();
        }

        //TODO: i need to add something to the messagepanel, which is removed
        //at the first call to the setMessage. Otherwise the first message did not
        //displayed itself, other said, did not repainted itself.
        feedbackpanel.add(messageLabel, BorderLayout.CENTER);
    }

    private void setUpStep() {
        stepspanel.removeAll();
        iterator.current().addChangeListener(changeListener);
        iterator.current().readSettings(this);
        stepspanel.add(iterator.current().getComponent(), BorderLayout.CENTER);
        stepspanel.revalidate();
        stepspanel.repaint();
    }

    private void updateButtons() {
        nextbutton.setEnabled(isValid && !isBusy && iterator.hasNext());
        finishbutton.setEnabled(isValid && !isBusy && !iterator.hasNext());
        backbutton.setEnabled(iterator.hasPrevious() && !isBusy);
    }

    private void checkValid() {
        isValid = iterator.current().isValid();
    }

    /** Getter for the instantiated REXPList object (data.frame)
     *
     * @return the dataframe
     */
    public REXPGenericVector getDataframe() {
        return dataframe;
    }

    /** Getter for stored property.
     * @param name name of the property
     * @return the value
     */
    public synchronized Object getProperty(String name) {
        return properties.get(name);
    }

    /** Allows Panels that use WizardPanel as settings object to
     * store additional settings into it.
     *
     * @param name name of the property
     * @param value value of property
     */
    public void putProperty(final String name, final Object value) {
        Object oldValue;

        synchronized (this) {
            oldValue = properties.get(name);
            properties.put(name, value);
        }

        firePropertyChange(name, oldValue, value);

        if (PROP_ERROR_MESSAGE.equals(name)) {
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    if (nextbutton.isEnabled() || finishbutton.isEnabled()) {
                        setMessage(MSG_TYPE_WARNING, (String) ((value == null) ? "" : value));
                    } else {
                        setMessage(MSG_TYPE_ERROR, (String) ((value == null) ? "" : value));
                    }
                }
            });
        }

        if (PROP_WARNING_MESSAGE.equals(name) || PROP_INFO_MESSAGE.equals(name)) {
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    if (PROP_WARNING_MESSAGE.equals(name)) {
                        setMessage(MSG_TYPE_WARNING, (String) ((value == null) ? "" : value));
                    } else {
                        setMessage(MSG_TYPE_INFO, (String) ((value == null) ? "" : value));
                    }
                }
            });
        }
    }

    public void setMessage(int msgType, String msg) {
        if (msg != null && msg.trim().length() > 0) {
            switch (msgType) {
                case MSG_TYPE_ERROR:
                    prepareMessage(messageLabel, getErrorMessageIcon(),
                            errorForeground);
                    break;
                case MSG_TYPE_WARNING:
                    prepareMessage(messageLabel, getWarningMessageIcon(),
                            warningForeground);
                    break;
                case MSG_TYPE_INFO:
                    prepareMessage(messageLabel, getInfoMessageIcon(),
                            infoForeground);
                    break;
                default:
            }
            messageLabel.setToolTipText(msg);
        } else {
            prepareMessage(messageLabel, null, null);
            messageLabel.setToolTipText(null);
        }
        messageLabel.setText(msg);
        feedbackpanel.removeAll();
        feedbackpanel.add(messageLabel, BorderLayout.CENTER);
        feedbackpanel.validate();
        feedbackpanel.repaint();
    }

    private void prepareMessage(JLabel label, ImageIcon icon, Color fgColor) {
        label.setIcon(icon);
        label.setForeground(fgColor);
    }

    private static ImageIcon getErrorMessageIcon() {
        if (errorIcon == null) {
            errorIcon = getIcon("/at/ac/ait/speedr/icons/error.gif");
        }
        return errorIcon;
    }

    private static ImageIcon getWarningMessageIcon() {
        if (warningIcon == null) {
            warningIcon = getIcon("/at/ac/ait/speedr/icons/warning.gif");
        }
        return warningIcon;
    }

    private static ImageIcon getInfoMessageIcon() {
        if (infoIcon == null) {
            infoIcon = getIcon("/at/ac/ait/speedr/icons/info.png");
        }
        return infoIcon;
    }

    private static ImageIcon getIcon(String img) {
        return new ImageIcon(WizardPanel.class.getResource(img));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        progresspanel = new javax.swing.JPanel();
        progressLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        cancelbutton = new javax.swing.JButton();
        finishbutton = new javax.swing.JButton();
        nextbutton = new javax.swing.JButton();
        backbutton = new javax.swing.JButton();
        feedbackpanel = new javax.swing.JPanel();
        stepspanel = new javax.swing.JPanel();

        progressLabel.setText("Please wait");

        javax.swing.GroupLayout progresspanelLayout = new javax.swing.GroupLayout(progresspanel);
        progresspanel.setLayout(progresspanelLayout);
        progresspanelLayout.setHorizontalGroup(
            progresspanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(progresspanelLayout.createSequentialGroup()
                .addComponent(progressLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        progresspanelLayout.setVerticalGroup(
            progresspanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(progresspanelLayout.createSequentialGroup()
                .addGroup(progresspanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(progressLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        cancelbutton.setText("Cancel");
        cancelbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelbuttonActionPerformed(evt);
            }
        });

        finishbutton.setText("Finish");
        finishbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                finishbuttonActionPerformed(evt);
            }
        });

        nextbutton.setText("Next >");
        nextbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextbuttonActionPerformed(evt);
            }
        });

        backbutton.setText("< Back");
        backbutton.setEnabled(false);
        backbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backbuttonActionPerformed(evt);
            }
        });

        feedbackpanel.setLayout(new java.awt.BorderLayout());

        stepspanel.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(feedbackpanel, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)
                .addGap(10, 10, 10)
                .addComponent(backbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nextbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(finishbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cancelbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6))
            .addComponent(stepspanel, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(stepspanel, javax.swing.GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(feedbackpanel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cancelbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(finishbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(nextbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(backbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void backbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backbuttonActionPerformed
        if (iterator.current() instanceof ProgressStep) {
            ((ProgressStep) iterator.current()).stopProcessing();
        }
        iterator.current().storeSettings(this);
        iterator.current().removeChangeListener(changeListener);

        iterator.previousPanel();
        setUpStep();
        updateButtons();
    }//GEN-LAST:event_backbuttonActionPerformed

    private void nextbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextbuttonActionPerformed
        iterator.current().storeSettings(this);
        iterator.current().removeChangeListener(changeListener);

        iterator.nextStep();
        setUpStep();
        if (iterator.current() instanceof ProgressStep) {
            final ProgressStep step = (ProgressStep) iterator.current();

            SwingWorker worker = new SwingWorker() {

                @Override
                protected Object doInBackground() throws Exception {
                    step.startProcessing();
                    return null;
                }

                @Override
                protected void done() {
                    displayProgress(false);
                    checkValid();
                    updateButtons();
                }
            };
            displayProgress(true);
            worker.execute();
            checkValid();
            updateButtons();
            firePropertyChange(PROP_STATE_CHANGED, null, PROP_STATE_NEXT);
        }
    }//GEN-LAST:event_nextbuttonActionPerformed

    private void finishbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_finishbuttonActionPerformed
        iterator.current().storeSettings(this);

        logger.log(Level.INFO, "finish action entry");
        isBusy = true;
        updateButtons();
        if (iterator.current() instanceof ValidatingStep) {
            ValidatingStep vstep = (ValidatingStep) iterator.current();
            try {
                vstep.validate();
                displayProgress(true);

                new Thread(new Runnable() {

                    public void run() {
                        dataframe = iterator.instantiate();
                        
                        SwingUtilities.invokeLater(new Runnable() {

                            public void run() {
                                firePropertyChange(PROP_STATE_CHANGED, null, PROP_STATE_FINISHED);
                                callUnininitialize();
                            }
                        });
                    }
                }).start();
            } catch (WizardValidationException ex) {
                setMessage(MSG_TYPE_ERROR, ex.getMessage());
                isBusy = false;
                updateButtons();
                return;
            }
        }

    }//GEN-LAST:event_finishbuttonActionPerformed

    private void cancelbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelbuttonActionPerformed
        if (iterator.current() instanceof ProgressStep) {
            ((ProgressStep) iterator.current()).stopProcessing();
        }
        isBusy = false;
        updateButtons();
        firePropertyChange(PROP_STATE_CHANGED, null, PROP_STATE_CANCELED);
        callUnininitialize();
    }//GEN-LAST:event_cancelbuttonActionPerformed

    public void callUnininitialize() {
        if (iterator != null) {
            iterator.current().removeChangeListener(changeListener);
            iterator.uninitialize(WizardPanel.this);
            iterator = null;
        }
        stepspanel.removeAll();
    }

    public void displayProgress(boolean visible) {
        if (visible) {
            isBusy = true;
            feedbackpanel.removeAll();
            progressLabel.setText("Please wait");
            progressBar.setIndeterminate(true);
            feedbackpanel.add(progresspanel, BorderLayout.CENTER);
        } else {
            isBusy = false;
            progressLabel.setText("Finished");
            progressBar.setIndeterminate(false);
            progressBar.setValue(progressBar.getMaximum());
            Timer t = new Timer(5000, new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (feedbackpanel.getComponentCount() > 0 && feedbackpanel.getComponent(0) == progresspanel) {
                        feedbackpanel.removeAll();
                        feedbackpanel.revalidate();
                        feedbackpanel.repaint();
                    }
                }
            });
            t.setRepeats(false);
            t.start();
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backbutton;
    private javax.swing.JButton cancelbutton;
    private javax.swing.JPanel feedbackpanel;
    private javax.swing.JButton finishbutton;
    private javax.swing.JButton nextbutton;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel progressLabel;
    private javax.swing.JPanel progresspanel;
    private javax.swing.JPanel stepspanel;
    // End of variables declaration//GEN-END:variables

    public void doNextClick() {
        nextbutton.doClick();
    }

    public void doFinishClick() {
        finishbutton.doClick();
    }

    public boolean isFinishEnabled() {
        return finishbutton.isEnabled();
    }

    public interface Step{
        
        public JPanel getComponent();

        public void readSettings(WizardPanel settings);

        public void storeSettings(WizardPanel settings);

        public boolean isValid();

        public void addChangeListener(ChangeListener l);

        public void removeChangeListener(ChangeListener l);
    }

    
    public interface ValidatingStep extends Step {
       
        public void validate() throws WizardValidationException;
    }

    public interface ProgressStep extends Step {

        public void startProcessing();

        public void stopProcessing();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                JFrame f = new JFrame();
                f.setBounds(100, 100, 800, 600);
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.setContentPane(new WizardPanel(new WizardIterator()));
                f.setVisible(true);
            }
        });
    }

    /**
     * FIXME: temporary method till memory leak fixed...
     */
    public void setDataframeToNull() {
        if(dataframe == null){
            logger.log(Level.INFO,"dataframe is alread null!");
        }
        dataframe = null;
    }
}
