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
                fis.close();
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
