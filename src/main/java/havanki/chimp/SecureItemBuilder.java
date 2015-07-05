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

import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * A builder for {@link SecureItem} objects.
 */
public class SecureItemBuilder {

  private String title;
  private String resource = "";
  private String username = "";
  private char[] password = new char[0];
  private String comments = "";
  private SecDate modificationDate;

  public SecureItemBuilder(String title) {
    if (title == null) {
      throw new NullPointerException("null title");
    }
    this.title = title;
    modificationDate = new SecDate();
  }

  public SecureItemBuilder(SecureItem item) {
    if (item == null) {
      throw new NullPointerException("null item");
    }
    title = item.getTitle();
    resource = item.getResource();
    username = item.getUsername();
    password = item.getPassword();
    comments = item.getComments();
    modificationDate = item.getModificationDate();
  }

  public SecureItemBuilder(Element el) throws ChimpException {
    if (el == null) {
      throw new NullPointerException("null element");
    }
    String title = el.getAttribute("title");
    if (title == null) {
      throw new ChimpException("No title");
    }
    this.title = title;

    NodeList nl;
    Element el_sub;

    nl = el.getElementsByTagName("resource");
    if (nl.getLength() != 1) {
      throw new ChimpException("No unique resource for " + title);
    }
    el_sub = (Element) nl.item(0);
    resource = el_sub.getTextContent();

    nl = el.getElementsByTagName("username");
    if (nl.getLength() != 1) {
      throw new ChimpException("No unique username for " + title);
    }
    el_sub = (Element) nl.item (0);
    username = el_sub.getTextContent();

    nl = el.getElementsByTagName("password");
    if (nl.getLength() != 1) {
      throw new ChimpException("No unique password for " + title);
    }
    el_sub = (Element) nl.item(0);
    password = el_sub.getTextContent().toCharArray();

    nl = el.getElementsByTagName("comments");
    if (nl.getLength() != 1) {
      throw new ChimpException("No unique comments for " + title);
    }
    el_sub = (Element) nl.item(0);
    comments = el_sub.getTextContent();

    nl = el.getElementsByTagName("modificationDate");
    if (nl.getLength() > 1) {
      throw new ChimpException("No unique modification date for " + title);
    }
    if (nl.getLength() == 0) {
      modificationDate = new SecDate(0L);  // for compatibility
    } else {
      el_sub = (Element) nl.item(0);
      Date d;
      try {
          d = new SecDate(el_sub.getTextContent());
      } catch (ParseException exc) {
          throw new ChimpException("Unparseable modification time for " +
                                   title);
      }
      modificationDate = new SecDate(d);
    }
  }

  private static void clear(char[] a) {
    Arrays.fill(a, (char) 0);
  }

  public void dispose() {
    Arrays.fill(password, (char) 0);
  }

  public SecureItemBuilder title(String title) {
    if (title == null) {
      throw new NullPointerException("null title");
    }
    this.title = title;
    return this;
  }

  public SecureItemBuilder resource(String resource) {
    if (resource == null) {
      throw new NullPointerException("null resource");
    }
    this.resource = resource;
    return this;
  }

  public SecureItemBuilder username(String username) {
    if (username == null) {
      throw new NullPointerException("null username");
    }
    this.username = username;
    return this;
  }

  public SecureItemBuilder password(char[] password) {
    if (password == null) {
      throw new NullPointerException("null password");
    }
    clear(this.password);
    this.password = Arrays.copyOf(password, password.length);
    return this;
  }

  public SecureItemBuilder comments(String comments) {
    if (comments == null) {
      throw new NullPointerException("null comments");
    }
    this.comments = comments;
    return this;
  }

  public SecureItemBuilder modificationDate(Date modificationDate) {
    if (modificationDate == null) {
      throw new NullPointerException("null modificationDate");
    }
    this.modificationDate = new SecDate(modificationDate);
    return this;
  }

  public SecureItem build() {
    return new SecureItem(title, resource, username, password, comments,
                          modificationDate);
  }
}
