package at.ac.ait.speedr.table;

import com.google.common.base.Function;
import java.util.Date;

/**
 *
 * @author visnei
 */
public class RDateTimeConverter implements Function<Date,Long>{

    public Long apply(Date f) {
        return f.getTime();
    }
}
