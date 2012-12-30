/*
 * CHIMP 1.1 - Cyber Helper Internet Monkey Program
 * Copyright (C) 2001-2005 Bill Havanki
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
import org.w3c.dom.*;

public class SecureItem {
    private String title;
    private String resource;
    private String username;
    private char[] password;
    private String comments;
    private SecDate modificationDate;

    private static void clear (char[] a) {
        java.util.Arrays.fill (a, (char) 0);
    }

    public SecureItem() {
        title = "New Item";
        modificationDate = new SecDate(System.currentTimeMillis());
    }

    public String getTitle() { return title; }
    public SecureItem setTitle (String t) {
        if (t == null) { throw new IllegalArgumentException ("null title"); }
        title = t;
        return this;
    }

    public String getResource() { return resource; }
    public SecureItem setResource (String r) {
        resource = ( r != null ? r : "" );
        return this;
    }
    public String getUsername() { return username; }
    public SecureItem setUsername (String u) {
        username = ( u != null ? u : "" );
        return this;
    }

    public char[] getPassword() {
        if (password == null) { return null; }
        return Arrays.copyOf (password, password.length);
    }
    public SecureItem setPassword (char[] p) {
        if (password != null) { clear (password); }
        if (p == null) {
            password = new char[0];
        } else {
            password = Arrays.copyOf (p, p.length);
        }
        return this;
    }

    public String getComments() { return comments; }
    public SecureItem setComments (String c) {
        comments = ( c != null ? c : "" );
        return this;
    }

    public SecDate getModificationDate() {
        return new SecDate(modificationDate.getTime());
    }
    public SecureItem setModificationDate(Date md) {
        if (md == null) {
            throw new IllegalArgumentException("null modification date");
        }
        modificationDate = new SecDate(md.getTime());
        return this;
    }

    public void fillWithElement (Element el) throws ChimpException {
        setTitle (el.getAttribute ("title"));

        NodeList nl;
        Element el_sub;

        nl = el.getElementsByTagName ("resource");
        if (nl.getLength() != 1) {
            throw new ChimpException ("No unique resource for " + getTitle());
        }
        el_sub = (Element) nl.item (0);
        setResource (el_sub.getTextContent());

        nl = el.getElementsByTagName ("username");
        if (nl.getLength() != 1) {
            throw new ChimpException ("No unique username for " + getTitle());
        }
        el_sub = (Element) nl.item (0);
        setUsername (el_sub.getTextContent());

        nl = el.getElementsByTagName ("password");
        if (nl.getLength() != 1) {
            throw new ChimpException ("No unique password for " + getTitle());
        }
        el_sub = (Element) nl.item (0);
        setPassword (el_sub.getTextContent().toCharArray());

        nl = el.getElementsByTagName ("comments");
        if (nl.getLength() != 1) {
            throw new ChimpException ("No unique comments for " + getTitle());
        }
        el_sub = (Element) nl.item (0);
        setComments (el_sub.getTextContent());

        nl = el.getElementsByTagName ("modificationDate");
        if (nl.getLength() > 1) {
            throw new ChimpException ("No unique modification date for " +
                                      getTitle());
        }
        if (nl.getLength() == 0) {
            setModificationDate(new Date(0L));  // for compatibility
        } else {
            el_sub = (Element) nl.item(0);
            Date d;
            try {
                d = getDateFormat().parse(el_sub.getTextContent());
            } catch (ParseException exc) {
                throw new ChimpException ("Unparseable modification time for " +
                                          getTitle());
            }
            setModificationDate(new SecDate(d));
        }
    }

    static DateFormat getDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
    }

    public Element getElement (Document doc) throws ChimpException {
        try {
            Element el = doc.createElement ("secure-item");
            el.setAttribute ("title", title);

            attachSubElement(el, "resource", resource, doc);
            attachSubElement(el, "username", username, doc);
            if (password != null) {
                attachSubElement(el, "password", new String(password), doc);
            }
            attachSubElement(el, "comments", comments, doc);
            attachSubElement(el, "modificationDate",
                             getDateFormat().format(modificationDate), doc);

            return el;
        } catch (DOMException exc) {
            throw new ChimpException ("Unable to create element for " + title,
                                      exc);
        }
    }
    private static void attachSubElement(Element el, String name, String value,
                                         Document doc) {
        if (value == null) { return; }
        Element el_sub = doc.createElement(name);
        Text tx = doc.createTextNode(value);
        el_sub.appendChild (tx);
        el.appendChild (el_sub);
    }

    public void dispose() {
        if (password != null) clear (password);
    }

    @Override public boolean equals(Object other) {
        if (this == other) { return true; }
        if (!(other instanceof SecureItem)) { return false; }
        SecureItem o = (SecureItem) other;
        if (!(title.equals(o.title))) { return false; }
        if (!(resource == null ? o.resource == null : resource.equals(o.resource))) { return false; }
        if (!(username == null ? o.username == null : username.equals(o.username))) { return false; }
        if (!(Arrays.equals(password, o.password))) { return false; }
        if (!(comments == null ? o.comments == null : comments.equals(o.comments))) { return false; }
        if (!(modificationDate.equals(o.modificationDate))) { return false; }
        return true;
    }
    @Override public int hashCode() {
        int c = 17;
        c = 37*c + title.hashCode();
        if (resource != null) { c = 37*c + resource.hashCode(); }
        if (username != null) { c = 37*c + username.hashCode(); }
        if (password != null) { c = 37*c + password.hashCode(); }
        if (comments != null) { c = 37*c + comments.hashCode(); }
        c = 37*c + modificationDate.hashCode();
        return c;
    }
}
