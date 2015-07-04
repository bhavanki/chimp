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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public final class XmlUtils {
  private XmlUtils() {}

  private static final DocumentBuilderFactory dbf =
      DocumentBuilderFactory.newInstance();

  public static Document newDocument() throws ChimpException {
    try {
      return dbf.newDocumentBuilder().newDocument();
    } catch (ParserConfigurationException exc) {
      throw new ChimpException("Could not create document", exc);
    }
  }

  public static Document parse(File f) throws ChimpException {
    FileInputStream fis = null;
    try {
      fis = new FileInputStream(f);
      return parse(new InputSource(fis));
    } catch (IOException exc) {
      throw new ChimpException("Failed to open " + f, exc);
    } finally {
      try {
        if (fis != null) fis.close();
      } catch (IOException exc) {
          throw new ChimpException("Failed to close " + f, exc);
      }
    }
  }
  public static Document parse(String s) throws ChimpException {
    StringReader sr = new StringReader(s);
    try {
      return parse(new InputSource(sr));
    } finally {
      sr.close();
    }
  }
  private static Document parse(InputSource src) throws ChimpException {
    DocumentBuilder db;
    try {
      db = dbf.newDocumentBuilder();
    } catch (ParserConfigurationException exc) {
      throw new ChimpException("Failed to get document builder", exc);
    }

    try {
      return db.parse(src);
    } catch (IOException exc) {
      throw new ChimpException("I/O exception parsing XML", exc);
    } catch (SAXException exc) {
      throw new ChimpException("Parse exception", exc);
    }
  }
}
