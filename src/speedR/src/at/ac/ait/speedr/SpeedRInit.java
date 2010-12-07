package at.ac.ait.speedr;

import at.ac.ait.speedr.workspace.RConnection;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import org.rosuda.JRI.RConsoleOutputStream;
import org.rosuda.JRI.Rengine;
import org.rosuda.REngine.REngineException;

/** Initializing of logger and System.out and System.err
 *
 * @author visnei
 */
public class SpeedRInit {

    public static boolean init(Level rootLogLevel) throws IOException, REngineException {
        RConnection.initREngine();
        System.setOut(new PrintStream(new RConsoleOutputStream(Rengine.getMainEngine(), 0)));
        //System.setErr(new PrintStream(new RConsoleOutputStream(Rengine.getMainEngine(), 0)));

        Logger root = Logger.getLogger("");

        root.setLevel(rootLogLevel);
        File logdir = new File(System.getProperty("user.home") + "/speedR");
        logdir.mkdir();
        String logfile = System.getProperty("user.home") + "/speedR/speedR.log";
        FileHandler fileTxt = new FileHandler(logfile);
        // Create txt Formatter
        SimpleFormatter formatterTxt = new SimpleFormatter();
        fileTxt.setFormatter(formatterTxt);
        root.addHandler(fileTxt);


        Logger.getLogger(SpeedRInit.class.getName()).log(Level.INFO, "logfile= {0}", logfile);

        return true;
    }
}
