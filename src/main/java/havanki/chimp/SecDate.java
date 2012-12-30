package havanki.chimp;

import java.util.Calendar;
import java.util.Date;

public class SecDate extends Date {
    private static long eliminateMs(long ms) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(ms);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }
    public SecDate(long ms) {
        super(eliminateMs(ms));
    }
    public SecDate(Date d) {
        super(eliminateMs(d.getTime()));
    }
}
