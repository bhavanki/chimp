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

import java.util.Map;
import java.util.Set;

import org.w3c.dom.*;
import org.xml.sax.*;
import javax.xml.parsers.*;

public class SecureItemTable
{
    private Map table;

    public SecureItemTable()
    {
	table = new java.util.HashMap();
    }

    public void put (SecureItem si)
    {
	table.put (si.getTitle(), si);
    }
    public SecureItem get (String title)
    {
	return (SecureItem) table.get (title);
    }
    public void remove (String title)
    {
	table.remove (title);
    }
    public int size()
    {
	return table.size();
    }

    public String[] getTitles()
    {
	Set keySet = table.keySet();
	return (String []) keySet.toArray (new String [keySet.size()]);
    }

    public void fillWithDocument (Document doc) throws ChimpException
    {
	NodeList nl = doc.getElementsByTagName ("secure-item");
	for (int i = 0; i < nl.getLength(); i++) {
	    Element el = (Element) nl.item (i);
	    SecureItem item = new SecureItem();
	    item.fillWithElement (el);
	    put (item);
	}
	return;
    }
    public Document getDocument() throws ChimpException
    {
	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	DocumentBuilder db = null;
	try {
	    db = dbf.newDocumentBuilder();
	} catch (javax.xml.parsers.ParserConfigurationException exc) {
	    throw new ChimpException ("Could not create document builder",
				      exc);
	}
	Document doc = db.newDocument();

	Element root;
	try {
	    root = doc.createElement ("secure-item-table");
	    doc.appendChild (root);
	} catch (DOMException exc) {
	    throw new ChimpException ("Could not create table element",
				      exc);
	}

	String[] titles = getTitles();
	for (int i = 0; i < titles.length; i++) {
	    SecureItem item = get (titles[i]);
	    Element el_item = item.getElement (doc);
	    root.appendChild (el_item);
	}

	return doc;
    }
}
