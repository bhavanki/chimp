package havanki.chimp;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SecDateTest {
    private Calendar cal;
    private SecDate sd;
    private Calendar sdcal;

    @Before public void setUp() {
        cal = Calendar.getInstance();
        if (cal.get(Calendar.MILLISECOND) == 0) {
            cal.set(Calendar.MILLISECOND, 1);
        }
        sdcal = Calendar.getInstance();
    }
    private void checkFields(Calendar expected, Calendar actual) {
        int[] fields = {
            Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH,
            Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND
        };
        for (int f : fields) {
            assertEquals("Mismatch in field " + f,
                         expected.get(f), actual.get(f));
        }
    }
    @Test public void testConstructFromDate() {
        Date d = cal.getTime();
        sd = new SecDate(d);
        assertFalse(d.getTime() == sd.getTime());
        assertTrue(sd.before(d));
        sdcal.setTime(sd);
        checkFields(cal, sdcal);
        assertTrue(sdcal.get(Calendar.MILLISECOND) == 0);
    }
    @Test public void testConstructFromTS() {
        long ms = cal.getTimeInMillis();
        sd = new SecDate(ms);
        assertTrue(sd.getTime() < ms);
        sdcal.setTime(sd);
        checkFields(cal, sdcal);
        assertTrue(sdcal.get(Calendar.MILLISECOND) == 0);
    }
}
