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

import java.io.*;
import java.nio.charset.StandardCharsets;
import org.w3c.dom.*;
import org.xml.sax.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import javax.crypto.*;
import javax.crypto.spec.*;

/**
 * A writer for password files.
 */
public class PassFileWriter
{
  private static final byte[] salt = {
      (byte)0x49, (byte)0x52, (byte)0x42, (byte)0x41,
      (byte)0x42, (byte)0x00, (byte)0x00, (byte)0x4e
  };
  private static final int iterationCount = 20;
  static final PBEParameterSpec pbeSpec =
      new PBEParameterSpec(salt, iterationCount);

  private final File file;

  public PassFileWriter(File file) {
      this.file = file;
  }

  public void write(SecureItemTable tbl, char[] password) throws IOException {
    OutputStream os = new FileOutputStream(file);
    OutputStream xmlout;

    if (password.length == 0) {
      xmlout = os;
      os = null;
    } else {
      PBEKeySpec keyspec = new PBEKeySpec(password);
      Cipher c;
      try {
        SecretKeyFactory fac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey key = fac.generateSecret(keyspec);

        c = Cipher.getInstance("PBEWithMD5AndDES");
        c.init(Cipher.ENCRYPT_MODE, key, pbeSpec);
      } catch (java.security.GeneralSecurityException exc) {
        os.close();
        IOException ioe = new IOException("Security exception during write");
        ioe.initCause(exc);
        throw ioe;
      }

      CipherOutputStream out = new CipherOutputStream(os, c);
      xmlout = out;
    }

    try {
      TransformerFactory tf = TransformerFactory.newInstance();
      Transformer t = tf.newTransformer();

      DOMSource src = new DOMSource(tbl.getDocument());
      StringWriter writer = new StringWriter();
      StreamResult sr = new StreamResult(writer);
      t.transform(src, sr);

      OutputStreamWriter osw =
        new OutputStreamWriter(xmlout, StandardCharsets.UTF_8);
      osw.write(writer.toString());
      osw.close();
    } catch (Exception exc) {
      IOException ioe = new IOException("Unable to serialize XML");
      ioe.initCause(exc);
      throw ioe;
    } finally {
      xmlout.close();
      if (os != null) os.close();
    }

    tbl.setDirty(false);
    return;
  }
}
