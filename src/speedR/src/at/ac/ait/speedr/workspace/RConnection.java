package at.ac.ait.speedr.workspace;

import org.rosuda.JRI.Rengine;
import org.rosuda.REngine.JRI.JRIEngine;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPGenericVector;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;

/**
 *
 * @author visnei
 */
public class RConnection {

    private static JRIEngine re;

    public static void initREngine() throws REngineException {
        if (re == null) {
            re = new JRIEngine(Rengine.getMainEngine());
        }
    }

    public static synchronized REXP eval(String cmd) throws REngineException, REXPMismatchException {
        return re.parseAndEval(cmd, null, true);
    }

    public static void exportDataFrame(String symbol,REXPGenericVector df)
            throws REngineException, REXPMismatchException{
        re.assign(symbol, df);
    }
}
