package at.ac.ait.speedr.workspace;

import at.ac.arcs.tablefilter.ARCTable;
import java.text.NumberFormat;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPDouble;
import org.rosuda.REngine.REXPGenericVector;
import org.rosuda.REngine.REXPList;
import org.rosuda.REngine.REXPString;
import org.rosuda.REngine.RList;

/**
 *
 * @author visnei
 */
public class RUtil {

    private static NumberFormat nf;

    private static void initNumberFormatter() {
        nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        nf.setMaximumFractionDigits(9);
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

    public static REXPGenericVector createRDataFrame(ARCTable arctable) {


        REXPString rownamesREXPString = null;

        if (arctable.getColumnName(0).equals("row.names")) {
            rownamesREXPString = createREXPString(arctable, 0);
        } else {
            String[] rn = new String[arctable.getRowCount()];
            for (int i = 0; i < rn.length; i++) {
                rn[i] = "" + (i + 1);
            }
            rownamesREXPString = new REXPString(rn);
        }

        REXP[] payload;
        String[] names;

        int i = 0;

        if (rownamesREXPString == null) {
            payload = new REXP[arctable.getColumnCount()];
            names = new String[arctable.getColumnCount()];
        } else {
            payload = new REXP[arctable.getColumnCount() - 1];
            names = new String[arctable.getColumnCount() - 1];
            i = 1;
        }

        for (int j = 0; i < arctable.getColumnCount(); i++, j++) {
            if (arctable.getColumnClass(i) == Double.class) {
                payload[j] = createREXPDouble(arctable, i);
            } else if (arctable.getColumnClass(i) == String.class) {
                payload[j] = createREXPString(arctable, i);
            }
            names[j] = arctable.getColumnName(i);
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
