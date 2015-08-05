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
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import org.w3c.dom.*;
import org.xml.sax.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;

/**
 * A writer for password files.
 */
public class PassFileWriter
{
  static final byte[] salt = {
      (byte)0x49, (byte)0x52, (byte)0x42, (byte)0x41,
      (byte)0x42, (byte)0x00, (byte)0x00, (byte)0x4e
  };
  static final int iterationCount = 20;

  private final File file;

  public PassFileWriter(File file) {
      this.file = file;
  }

  public void write(SecureItemTable tbl, char[] password) throws IOException {
    try (OutputStream os = new FileOutputStream(file)) {
      OutputStream xmlout;

      if (password.length == 0) {
        xmlout = os;
      } else {
        Cipher c;
        try {
          c = new PassFileCipher("PBEWithMD5AndDES", iterationCount, salt)
              .getCipher(Cipher.ENCRYPT_MODE, password);
        } catch (GeneralSecurityException exc) {
          throw new IOException("Security exception during write", exc);
        }

        xmlout = new CipherOutputStream(os, c);
      }

      try {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer t = tf.newTransformer();

        DOMSource src = new DOMSource(tbl.getDocument());
        StringWriter writer = new StringWriter();
        StreamResult sr = new StreamResult(writer);
        t.transform(src, sr);

        try (OutputStreamWriter osw =
             new OutputStreamWriter(xmlout, StandardCharsets.UTF_8)) {
          osw.write(writer.toString());
        }
      } catch (Exception exc) {
        throw new IOException("Unable to serialize XML", exc);
      } finally {
        if (xmlout != os) {
          xmlout.close();
        }
      }
    }

    tbl.setDirty(false);
    return;
  }
}
