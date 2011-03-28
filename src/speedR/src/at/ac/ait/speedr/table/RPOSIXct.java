package at.ac.ait.speedr.table;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author visnei
 */
public class RPOSIXct extends java.util.Date{

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private long seconds;

    public RPOSIXct(long seconds) {
        super(seconds * 1000);
        this.seconds = seconds;
    }

    public RPOSIXct(Date date) {
        super(date.getTime());
        this.seconds = (long) (date.getTime() / 1000);
    }

    public long getPOSIXctSeconds(){
        return seconds;
    }

    @Override
    public String toString() {
        return "as.POSIXct(\""+sdf.format(this)+"\")";
    }
}
