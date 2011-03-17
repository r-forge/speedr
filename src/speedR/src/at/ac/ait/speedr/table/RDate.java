package at.ac.ait.speedr.table;

import java.util.Date;

/**
 *
 * @author visnei
 */
public class RDate extends java.util.Date{

    private long day;

    public RDate(long day) {
        super(day * 86400000l - 3600000l);
        this.day = day;
    }

    public RDate(Date date){
        super(date.getTime());
        day = (int) ((date.getTime() + 3600000l) / 86400000l);
    }

    public long getDateDay(){
        return day;
    }
}
