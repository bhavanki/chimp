/*
 * CHIMP 1.1 - Cyber Helper Internet Monkey Program
 * Copyright (C) 2001-2015 Bill Havanki
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package havanki.chimp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A date with only second resolution.
 */
public class SecDate extends Date {

  private static DateFormat getDateFormat() {
    return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
  }

  private static long eliminateMs(long ms) {
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(ms);
    cal.set(Calendar.MILLISECOND, 0);
    return cal.getTimeInMillis();
  }

  public SecDate() {
    super(eliminateMs(System.currentTimeMillis()));
  }

  public SecDate(String s) throws ParseException {
    super(eliminateMs(getDateFormat().parse(s).getTime()));
  }

  public SecDate(long ms) {
    super(eliminateMs(ms));
  }

  public SecDate(Date d) {
    super(eliminateMs(d.getTime()));
  }

  @Override
  public String toString() {
    return getDateFormat().format(this);
  }
}
