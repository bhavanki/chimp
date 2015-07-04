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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.w3c.dom.Document;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

/**
 * A single secured item.
 */
public class SecureItem {
  private final String title;
  private final String resource;
  private final String username;
  private final char[] password;
  private final String comments;
  private final SecDate modificationDate;

  SecureItem(String title, String resource, String username, char[] password,
             String comments, SecDate modificationDate) {
    this.title = title;
    this.resource = resource;
    this.username = username;
    this.password = password;
    this.comments = comments;
    this.modificationDate = modificationDate;
  }

  public String getTitle() { return title; }

  public String getResource() { return resource; }

  public String getUsername() { return username; }

  public char[] getPassword() {
    return Arrays.copyOf(password, password.length);
  }

  public String getComments() { return comments; }

  public SecDate getModificationDate() {
      return new SecDate(modificationDate.getTime());
  }

  public Element getElement(Document doc) throws ChimpException {
    try {
      Element el = doc.createElement("secure-item");
      el.setAttribute("title", title);

      attachSubElement(el, "resource", resource, doc);
      attachSubElement(el, "username", username, doc);
      attachSubElement(el, "password", new String(password), doc);
      attachSubElement(el, "comments", comments, doc);
      attachSubElement(el, "modificationDate",
                       getDateFormat().format(modificationDate), doc);

      return el;
    } catch (DOMException exc) {
      throw new ChimpException("Unable to create element for " + title, exc);
    }
  }
  private static void attachSubElement(Element el, String name, String value,
                                       Document doc) {
    Element el_sub = doc.createElement(name);
    Text tx = doc.createTextNode(value);
    el_sub.appendChild(tx);
    el.appendChild(el_sub);
  }

  static DateFormat getDateFormat() {
    return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
  }

  public void dispose() {
    Arrays.fill(password, (char) 0);
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) { return true; }
    if (!(other instanceof SecureItem)) { return false; }
    SecureItem o = (SecureItem) other;

    if (!(title.equals(o.title))) { return false; }
    if (!(resource.equals(o.resource))) { return false; }
    if (!(username.equals(o.username))) { return false; }
    if (!(Arrays.equals(password, o.password))) { return false; }
    if (!(comments.equals(o.comments))) { return false; }
    if (!(modificationDate.equals(o.modificationDate))) { return false; }
    return true;
  }

  @Override
  public int hashCode() {
    int c = 17;
    c = 37 * c + title.hashCode();
    c = 37 * c + resource.hashCode();
    c = 37 * c + username.hashCode();
    c = 37 * c + Arrays.hashCode(password);
    c = 37 * c + comments.hashCode();
    c = 37 * c + modificationDate.hashCode();
    return c;
  }

  @Override
  public String toString() {
    return "{" +
      "title='" + title + "'" +
      ",resource='" + resource + "'" +
      ",username='" + username + "'" +
      ",password='" + Arrays.toString(password) + "'" +
      ",comments='" + comments + "'" +
      ",modificationDate=" + modificationDate + "'" +
      "}";
  }
}
