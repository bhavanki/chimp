package havanki.chimp;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class SecDateTest {
  private Calendar cal;
  private Date d;
  private SecDate sd;

  @Before
  public void setUp() throws InterruptedException {
    cal = Calendar.getInstance();
    if (cal.get(Calendar.MILLISECOND) == 0) {
        cal.set(Calendar.MILLISECOND, 1);
    }
    d = cal.getTime();

    Thread.sleep(5L);
  }

  private static final int[] FIELDS = {
    Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH,
    Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND
  };

  private void verify() {
    assertTrue(sd.getTime() < d.getTime());

    Calendar sdcal = Calendar.getInstance();
    sdcal.setTime(sd);
    for (int f : FIELDS) {
      assertEquals("Mismatch in field " + f, cal.get(f), sdcal.get(f));
    }
    assertTrue(sdcal.get(Calendar.MILLISECOND) == 0);
  }

  @Test
  public void testConstructFromString() throws Exception {
    String s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(d);
    sd = new SecDate(s);
    verify();
  }

  @Test
  public void testConstructFromDate() {
    sd = new SecDate(d);
    verify();
  }

  @Test
  public void testConstructFromTS() {
    sd = new SecDate(d.getTime());
    verify();
  }
}
