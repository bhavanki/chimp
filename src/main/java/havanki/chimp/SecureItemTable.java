/*
 * CHIMP 1.1.1 - Cyber Helper Internet Monkey Program
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

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.*;
import javax.xml.parsers.*;

/**
 * A table of {@link SecureItem} objects keyed by title.
 */
public class SecureItemTable
{
  private final Map<String,SecureItem> table;

  public SecureItemTable() {
    table = new java.util.HashMap<>();
  }

  public SecureItemTable put(SecureItem si) {
    table.put(si.getTitle(), si);
    return this;
  }
  public SecureItem get(String title) {
    return table.get(title);
  }
  public SecureItemTable remove(String title) {
    table.remove(title);
    return this;
  }
  public int size() {
    return table.size();
  }
  public boolean contains(SecureItem item) {
    return table.containsValue(item);
  }


  public void dispose() {
    for (SecureItem item : table.values()) {
      item.dispose();
    }
  }

  public String[] getTitlesInOrder() {
    Set<String> keySet = table.keySet();
    String[] titles = (String []) keySet.toArray(new String [keySet.size()]);
    Arrays.sort(titles);
    return titles;
  }

  public String[] getUsernamesInTitleOrder() {
    String[] usernames = new String [table.size()];
    String[] titles = getTitlesInOrder();
    for (int i = 0; i < titles.length; i++) {
      usernames[i] = table.get(titles[i]).getUsername();
    }
    return usernames;
  }

  public Document getDocument() throws ChimpException {
    Document doc = XmlUtils.newDocument();

    Element root;
    try {
      root = doc.createElement("secure-item-table");
      doc.appendChild(root);
    } catch (DOMException exc) {
        throw new ChimpException("Could not create table element", exc);
    }

    String[] titles = getTitlesInOrder();
    for (int i = 0; i < titles.length; i++) {
      SecureItem item = get(titles[i]);
      Element el_item = item.getElement(doc);
      root.appendChild(el_item);
    }

    return doc;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) { return true; }
    if (!(other instanceof SecureItemTable)) { return false; }
    return table.equals(((SecureItemTable) other).table);
  }

  @Override
  public int hashCode() {
    return table.hashCode();
  }

  @Override
  public String toString() {
    return table.toString();
  }
}
