package at.ac.ait.speedr.workspace;

import at.ac.ait.speedr.importwizard.steps.DataImportPanel;
import at.ac.ait.speedr.table.RDate;
import at.ac.ait.speedr.table.RPOSIXct;
import at.ac.arcs.tablefilter.ARCTable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPDouble;
import org.rosuda.REngine.REXPFactor;
import org.rosuda.REngine.REXPGenericVector;
import org.rosuda.REngine.REXPInteger;
import org.rosuda.REngine.REXPList;
import org.rosuda.REngine.REXPString;
import org.rosuda.REngine.RList;

/**
 *
 * @author visnei
 */
public class RUtil {

    public static final String[] parsePOSIXctPattern = new String[]{"yyyy-MM-dd HH:mm:ss",
        "yyyy/MM/dd HH:mm:ss", "yyyy.MM.dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy.MM.dd HH:mm",
        "yyyy/MM/dd HH:mm", "yyyy-MM-dd", "yyyy.MM.dd", "yyyy/MM/dd"};
    public static final String[] parsePOSIXctPatternInRFormat = new String[]{
        "%Y-%m-%d %H:%M:%S", "%Y.%m.%d %H:%M:%S", "%Y/%m/%d %H:%M:%S",
        "%Y-%m-%d %H:%M", "%Y.%m.%d %H:%M", "%Y/%m/%d %H:%M", "%Y-%m-%d",
        "%Y.%m.%d", "%Y/%m/%d"};
    public static final String[] parseDatePattern =
            new String[]{"yyyy-MM-dd", "yyyy.MM.dd", "yyyy/MM/dd"};
    public static final String[] parseDatePatternInRFormat = new String[]{
        "%Y-%m-%d", "%Y.%m.%d", "%Y/%m/%d"};
    public static final String defaultPOSIXctPattern = new SimpleDateFormat().toPattern();
    public static final String defaultDatePattern =
            defaultPOSIXctPattern.substring(0, defaultPOSIXctPattern.indexOf(' '));
    public static final String defaultDatePatternInRFormat = defaultDatePattern.replaceAll("y+", "%Y").replaceAll("M+", "%m").replaceAll("d+", "%d");
    private static final String defaultTimePattern =
            defaultPOSIXctPattern.substring(defaultPOSIXctPattern.indexOf(' ') + 1, defaultPOSIXctPattern.length());
    private static final String defaultTimePatternInRFormat = defaultTimePattern.replaceAll("H+", "%H").replaceAll("m+", "%M").replaceAll("s+", "%S");
    public static final String defaultPOSIXctPatternInRFormat =
            defaultDatePatternInRFormat + " " + defaultTimePatternInRFormat;

    public static String convertPatternFromRFormat(String pattern) {
        return pattern.replaceFirst("(?i)%y", "yyyy").replaceFirst("%m", "MM").
                replaceFirst("(?i)%d", "dd").replaceFirst("(?i)%H", "HH").replaceFirst("%M", "mm").
                replaceFirst("(?i)%S", "ss");
    }
    private static NumberFormat nf;

    private static void initNumberFormatter() {
        nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        nf.setMaximumFractionDigits(9);
        if (nf instanceof DecimalFormat) {
            DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance();
            dfs.setDecimalSeparator('.');
            ((DecimalFormat) nf).setDecimalFormatSymbols(dfs);
            ((DecimalFormat) nf).setDecimalSeparatorAlwaysShown(true);
            ((DecimalFormat) nf).setMinimumFractionDigits(1);
        }
    }

    private static REXPString createREXPString(ARCTable t, int columnIndex) {
        if (nf == null) {
            initNumberFormatter();
        }
        String[] payload = new String[t.getRowCount()];

        Object valueAt;

        for (int i = 0; i < payload.length; i++) {
            valueAt = t.getValueAt(i, columnIndex);
            if (valueAt != null) {
                if (valueAt instanceof Double) {
                    payload[i] = nf.format(valueAt);
                } else {
                    payload[i] = valueAt.toString();
                }
            }
        }

        return new REXPString(payload);
    }

    private static REXPFactor createREXPFactor(ARCTable t, int columnIndex) {
        if (nf == null) {
            initNumberFormatter();
        }

        LinkedHashMap<String, Integer> levelmap = new LinkedHashMap<String, Integer>();

        ArrayList<Integer> idlist = new ArrayList<Integer>();


        Object valueAt;
        String level;
        int id = 1; // 1 based!
        for (int i = 0; i < t.getRowCount(); i++) {
            valueAt = t.getValueAt(i, columnIndex);
            if (valueAt != null) {
                if (valueAt instanceof Double) {
                    level = nf.format(valueAt);
                } else {
                    level = valueAt.toString();
                }

                if (!levelmap.containsKey(level)) {
                    levelmap.put(level, id++);
                }

                idlist.add(levelmap.get(level));
            }
        }

        int[] ids = new int[idlist.size()];

        for (int i = 0; i < idlist.size(); i++) {
            ids[i] = idlist.get(i);
        }

        return new REXPFactor(ids, levelmap.keySet().toArray(new String[levelmap.size()]));
    }

    private static REXPDouble createREXPDouble(ARCTable t, int columnIndex) {
        double[] payload = new double[t.getRowCount()];

        Object valueAt;

        for (int i = 0; i < payload.length; i++) {
            valueAt = t.getValueAt(i, columnIndex);
            if (valueAt != null) {
                payload[i] = (Double) valueAt;
            }
        }

        return new REXPDouble(payload);
    }

    private static REXPDouble createDate(ARCTable t, int columnIndex) {
        double[] payload = new double[t.getRowCount()];

        RDate valueAt;

        for (int i = 0; i < payload.length; i++) {
            valueAt = (RDate) t.getValueAt(i, columnIndex);
            if (valueAt != null) {
                payload[i] = valueAt.getDateDay();
            }
        }

        return new REXPDouble(payload, new REXPList(new RList(new REXP[]{new REXPString("Date")}, new String[]{"class"})));
    }

    private static REXPDouble createPOSIXct(ARCTable t, int columnIndex) {
        double[] payload = new double[t.getRowCount()];

        RPOSIXct valueAt;

        for (int i = 0; i < payload.length; i++) {
            valueAt = (RPOSIXct) t.getValueAt(i, columnIndex);
            if (valueAt != null) {
                payload[i] = valueAt.getPOSIXctSeconds();
            }
        }

        return new REXPDouble(payload, new REXPList(new RList(new REXP[]{new REXPString(new String[]{"POSIXct", "POSIXt"})}, new String[]{"class"})));
    }

    public static REXPGenericVector createRDataFrame(ARCTable arctable) {

        REXP[] payload;
        String[] names;
        int columnBeginIndexWithoutRowNamesColumn = 0;

        REXPString rownamesREXPString = null;

        if (arctable.getColumnName(0).equals("row.names")) {
            rownamesREXPString = createREXPString(arctable, 0);

            payload = new REXP[arctable.getColumnCount() - 1];
            names = new String[arctable.getColumnCount() - 1];
            columnBeginIndexWithoutRowNamesColumn = 1;
        } else {
            String[] rn = new String[arctable.getRowCount()];
            for (int i = 0; i < rn.length; i++) {
                rn[i] = "" + (i + 1);
            }
            rownamesREXPString = new REXPString(rn);

            payload = new REXP[arctable.getColumnCount()];
            names = new String[arctable.getColumnCount()];
        }


        if (arctable.isColumnSelectorVisible()) {
            for (int j = 0; columnBeginIndexWithoutRowNamesColumn < arctable.getColumnCount(); columnBeginIndexWithoutRowNamesColumn++, j++) {
                if (arctable.getColumnSelectorOption(columnBeginIndexWithoutRowNamesColumn).getObject() == DataImportPanel.ColumnType.NUMERIC) {
                    payload[j] = createREXPDouble(arctable, columnBeginIndexWithoutRowNamesColumn);
                } else if (arctable.getColumnSelectorOption(columnBeginIndexWithoutRowNamesColumn).getObject() == DataImportPanel.ColumnType.CHARACTER) {
                    payload[j] = createREXPString(arctable, columnBeginIndexWithoutRowNamesColumn);
                } else if (arctable.getColumnSelectorOption(columnBeginIndexWithoutRowNamesColumn).getObject() == DataImportPanel.ColumnType.FACTOR) {
                    payload[j] = createREXPFactor(arctable, columnBeginIndexWithoutRowNamesColumn);
                } else if (arctable.getColumnSelectorOption(columnBeginIndexWithoutRowNamesColumn).getObject() == DataImportPanel.ColumnType.DATE) {
                    payload[j] = createDate(arctable, columnBeginIndexWithoutRowNamesColumn);
                } else if (arctable.getColumnSelectorOption(columnBeginIndexWithoutRowNamesColumn).getObject() == DataImportPanel.ColumnType.POSIXCT) {
                    payload[j] = createPOSIXct(arctable, columnBeginIndexWithoutRowNamesColumn);
                }
                names[j] = arctable.getColumnName(columnBeginIndexWithoutRowNamesColumn);
            }
        } else {
            for (int j = 0; columnBeginIndexWithoutRowNamesColumn < arctable.getColumnCount(); columnBeginIndexWithoutRowNamesColumn++, j++) {
                if (arctable.getColumnClass(columnBeginIndexWithoutRowNamesColumn) == REXPDouble.class || arctable.getColumnClass(columnBeginIndexWithoutRowNamesColumn) == REXPInteger.class) {
                    payload[j] = createREXPDouble(arctable, columnBeginIndexWithoutRowNamesColumn);
                } else if (arctable.getColumnClass(columnBeginIndexWithoutRowNamesColumn) == String.class) {
                    payload[j] = createREXPString(arctable, columnBeginIndexWithoutRowNamesColumn);
                } else if (arctable.getColumnClass(columnBeginIndexWithoutRowNamesColumn) == REXPFactor.class) {
                    payload[j] = createREXPFactor(arctable, columnBeginIndexWithoutRowNamesColumn);
                } else if (arctable.getColumnClass(columnBeginIndexWithoutRowNamesColumn) == RDate.class) {
                    payload[j] = createDate(arctable, columnBeginIndexWithoutRowNamesColumn);
                } else if (arctable.getColumnClass(columnBeginIndexWithoutRowNamesColumn) == RPOSIXct.class) {
                    payload[j] = createPOSIXct(arctable, columnBeginIndexWithoutRowNamesColumn);
                }
                names[j] = arctable.getColumnName(columnBeginIndexWithoutRowNamesColumn);
            }
        }



        System.gc();
        REXPString namesREXPString = new REXPString(names);
        REXPString classREXPString = new REXPString("data.frame");
        REXPList attr;

        if (rownamesREXPString == null) {
            attr = new REXPList(new RList(new REXP[]{namesREXPString, classREXPString}, new String[]{"names", "class"}));
        } else {
            attr = new REXPList(new RList(new REXP[]{namesREXPString, classREXPString, rownamesREXPString}, new String[]{"names", "class", "row.names"}));
        }

        return new REXPGenericVector(new RList(payload, names), attr);
    }
}
