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

import java.io.*;
import java.security.GeneralSecurityException;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;

/**
 * A reader for password files.
 */
public class PassFileReader
{
  private File file;

  public PassFileReader(File file) {
    this.file = file;
  }

  public SecureItemTable read(char[] password) throws IOException {
    Document doc;

    try (InputStream is = new FileInputStream(file)) {
      CipherInputStream in;
      InputSource source;

      if (password.length == 0) {
        in = null;
        source = new InputSource(is);
      } else {
        Cipher c;
        try {
          c = new PassFileCipher("PBEWithMD5AndDES",
                                 PassFileWriter.iterationCount,
                                 PassFileWriter.salt)
              .getCipher(Cipher.DECRYPT_MODE, password);
        } catch (GeneralSecurityException exc) {
          throw new IOException("Security exception during read", exc);
        }

        in = new CipherInputStream(is, c);
        source = new InputSource(in);
      }

      try {
        DocumentBuilder db = null;
        try {
          DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
          db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException exc) {
          throw new IOException("Could not create document builder", exc);
        }

        try {
          doc = db.parse(source);
        } catch (IOException exc) {
          // Probably the wrong password...
          return null;
        } catch (SAXException exc) {
          // Probably the wrong password...
          return null;
        }
      } finally {
        if (in != null) in.close();
      }
    }

    try {
      return new SecureItemTableFactory().createTable(doc);
    } catch (ChimpException exc) {
      throw new IOException("Unable to understand document", exc);
    }
  }
}
