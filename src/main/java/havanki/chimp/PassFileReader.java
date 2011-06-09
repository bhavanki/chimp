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

import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;

import javax.crypto.*;
import javax.crypto.spec.*;

public class PassFileReader
{
  private File file;

  public PassFileReader (File file)
  {
      this.file = file;
  }

  public SecureItemTable read (char[] password) throws IOException
  {
      InputStream is = new FileInputStream (file);
      CipherInputStream in;
      InputSource source;

      if (password.length == 0) {
	  in = null;
	  source = new InputSource (is);
      } else {
	  PBEKeySpec keyspec = new PBEKeySpec (password);
	  Cipher c;
	  try {
	      SecretKeyFactory fac =
		  SecretKeyFactory.getInstance ("PBEWithMD5AndDES");
	      SecretKey key = fac.generateSecret (keyspec);

	      c = Cipher.getInstance ("PBEWithMD5AndDES");
	      c.init (Cipher.DECRYPT_MODE, key, PassFileWriter.pbeSpec);
	  } catch (java.security.GeneralSecurityException exc) {
	      IOException ioe =
		  new IOException ("Security exception during read");
	      ioe.initCause (exc);
	      throw ioe;
	  }

	  in = new CipherInputStream (is, c);
	  source = new InputSource (in);
      }

      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = null;
      try {
	  db = dbf.newDocumentBuilder();
      } catch (ParserConfigurationException exc) {
	  IOException ioe =
	      new IOException ("Could not create document builder");
	  ioe.initCause (exc);
	  throw ioe;
      }
      Document doc;
      try {
	  doc = db.parse (source);
      } catch (SAXException exc) {
	  // Probably the wrong password...
	  return null;
      }

      try { if (in != null) in.close(); } catch (IOException exc) {}
      try { is.close(); } catch (IOException exc) {}

      SecureItemTable tbl = new SecureItemTable();
      try {
	  tbl.fillWithDocument (doc);
      } catch (ChimpException exc) {
	  IOException ioe = new IOException ("Unable to understand document");
	  ioe.initCause (exc);
	  throw ioe;
      }
      return tbl;
  }
}
