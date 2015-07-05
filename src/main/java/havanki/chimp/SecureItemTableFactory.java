/*
 * CHIMP 1.2.0 - Cyber Helper Internet Monkey Program
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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.Map;

/**
 * A factory for {@link SecureItemTable} instances.
 */
public class SecureItemTableFactory {

  /**
   * Creates an empty table.
   *
   * @return empty table
   */
  public SecureItemTable createTable() {
    return new SecureItemTable();
  }

  /**
   * Creates a table by parsing XML.
   *
   * @param doc document to parse
   * @return parsed table
   * @throws ChimpException if the table could not be parsed
   */
  public SecureItemTable createTable(Document doc) throws ChimpException {
    SecureItemTable table = new SecureItemTable();

    NodeList nl = doc.getElementsByTagName("secure-item");
    for (int i = 0; i < nl.getLength(); i++) {
      Element el = (Element) nl.item (i);
      SecureItemBuilder b = new SecureItemBuilder(el);
      table.put(b.build());
    }

    table.setDirty(false);
    return table;
  }
}
