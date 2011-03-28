package at.ac.ait.speedr.importwizard.steps;

/** Listener to the action triggered by a real user
 * on the dataimportpanel like changing the delimtier type.
 *
 * @author visnei
 */
public interface DataImportPanelUserActionListener {

    public void separatorChanged(String newvalue);
    public void quoteChanged(String newvalue);
    public void rowStartChanged(int newvalue);
    public void rowEndChange(int newvalue);
    public void colStartChanged(int newvalue);
    public void colEndChange(int newvalue);
    public void hasColumnNamesChanged(Boolean newvalue);
    public void columnNamesRowIndexChanged(int newvalue);
    public void hasRowNamesChanged(Boolean newvalue);
    public void rowNamesNoChanged(int newvalue);
    public void variableNameChanged(String newvalue);
    public void tableCellValueChanged(Object aValue,int rowIndex,int columnIndex);
    public void colClassesChanged(String[] classes);
    public void colClassChanged(int columnIndex,String clazz);
}
