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

import org.w3c.dom.*;

public class SecureItem
{
    private String title;
    private String resource;
    private String username;
    private char[] password;
    private String comments;

    private char[] copy (char[] a)
    {
	char[] b = new char [a.length];
	System.arraycopy (a, 0, b, 0, a.length);
	return b;
    }
    private void clear (char[] a)
    {
	java.util.Arrays.fill (a, (char) 0);
    }

    public SecureItem() { title = "New Item"; }

    public String getTitle() { return title; }
    public void setTitle (String t)
    {
	if (t == null) throw new IllegalArgumentException ("null title");
	title = t;
    }

    public String getResource() { return resource; }
    public void setResource (String r) { resource = ( r != null ? r : "" ); }

    public String getUsername() { return username; }
    public void setUsername (String u) { username = ( u != null ? u : "" ); }

    public char[] getPassword()
    {
	if (password == null) return null;
	char[] p = copy (password);
	return p;
    }
    public void setPassword (char[] p)
    {
	if (password != null) clear (password);
	if (p == null)
	    password = new char[0];
	else
	    password = copy (p);
    }

    public String getComments() { return comments; }
    public void setComments (String c) { comments = ( c != null ? c : "" ); }

    private String getText (Element el) throws ChimpException
    {
	el.normalize();
	NodeList nl = el.getChildNodes();
	if (nl.getLength() == 0) return "";
	if (nl.getLength() != 1) {
	    throw new ChimpException ("No single text node for " +
				      el.getTagName());
	}
	Node n = nl.item (0);
	if (!(n.getNodeType() == Node.TEXT_NODE)) {
	    throw new ChimpException ("No text node for " + el.getTagName());
	}
	return n.getNodeValue();
    }

    public void fillWithElement (Element el) throws ChimpException
    {
	setTitle (el.getAttribute ("title"));

	NodeList nl;
	Element el_sub;

	nl = el.getElementsByTagName ("resource");
	if (nl.getLength() != 1) {
	    throw new ChimpException ("No unique resource for " + getTitle());
	}
	el_sub = (Element) nl.item (0);
	setResource (getText (el_sub));

	nl = el.getElementsByTagName ("username");
	if (nl.getLength() != 1) {
	    throw new ChimpException ("No unique username for " + getTitle());
	}
	el_sub = (Element) nl.item (0);
	setUsername (getText (el_sub));

	nl = el.getElementsByTagName ("password");
	if (nl.getLength() != 1) {
	    throw new ChimpException ("No unique password for " + getTitle());
	}
	el_sub = (Element) nl.item (0);
	setPassword (getText (el_sub).toCharArray());

	nl = el.getElementsByTagName ("comments");
	if (nl.getLength() != 1) {
	    throw new ChimpException ("No unique comments for " + getTitle());
	}
	el_sub = (Element) nl.item (0);
	setComments (getText (el_sub));
    }
    public Element getElement (Document doc) throws ChimpException
    {
	try {
	    Element el = doc.createElement ("secure-item");
	    el.setAttribute ("title", title);

	    Text tx;

	    Element el_r = doc.createElement ("resource");
	    tx = doc.createTextNode (resource);
	    el_r.appendChild (tx);
	    el.appendChild (el_r);

	    Element el_u = doc.createElement ("username");
	    tx = doc.createTextNode (username);
	    el_u.appendChild (tx);
	    el.appendChild (el_u);

	    Element el_p = doc.createElement ("password");
	    tx = doc.createTextNode (new String (password));  // sigh
	    el_p.appendChild (tx);
	    el.appendChild (el_p);

	    Element el_c = doc.createElement ("comments");
	    tx = doc.createTextNode (comments);
	    el_c.appendChild (tx);
	    el.appendChild (el_c);

	    return el;
	} catch (DOMException exc) {
	    throw new ChimpException ("Unable to create element for " + title,
				      exc);
	}
    }

    public void dispose()
    {
	if (password != null) clear (password);
    }
}
