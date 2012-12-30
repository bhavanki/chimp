package havanki.chimp;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SecureItemTest {
    private static final SecDate NOW;
    static {
        NOW = new SecDate(System.currentTimeMillis());
    }
    private static final String NOW_STRING =
        SecureItem.getDateFormat().format(NOW);

    private SecureItem item;

    @Before public void setUp() {
        item = new SecureItem();
    }
    @Test public void testGettersAndSetters() {
        item.setTitle("Ponybook")
            .setResource("http://www.book.pony/")
            .setUsername("Applejack")
            .setPassword("password".toCharArray())
            .setComments("Yeehaw")
            .setModificationDate(NOW);
        checkProperties(item);
    }
    private void checkProperties(SecureItem item) {
        assertEquals("Ponybook", item.getTitle());
        assertEquals("http://www.book.pony/", item.getResource());
        assertEquals("Applejack", item.getUsername());
        assertEquals("password", new String(item.getPassword()));
        assertEquals("Yeehaw", item.getComments());
        assertEquals(NOW, item.getModificationDate());
    }
    @Test(expected=IllegalArgumentException.class)
    public void testNoNullTitle() {
        item.setTitle(null);
    }
    @Test public void testPassword() {
        item.setPassword(null);
        assertArrayEquals(new char[0], item.getPassword());
    }
    @Test public void testModificationDate() {
        item.setModificationDate(NOW);
        assertEquals(NOW, item.getModificationDate());
        assertNotSame(NOW, item.getModificationDate());
    }
    @Test(expected=IllegalArgumentException.class)
    public void testNoNullModificationDate() {
        item.setModificationDate(null);
    }

    private static final String ELEMENT =
    "<secure-item title=\"Ponybook\">" +
    "  <resource>http://www.book.pony/</resource>" +
    "  <username>Applejack</username>" +
    "  <password>password</password>" +
    "  <comments>Yeehaw</comments>" +
    "  <modificationDate>" + NOW_STRING + "</modificationDate>" +
    "</secure-item>";
    private static final Document DOC;
    static {
        try {
            DOC = XmlUtils.parse(ELEMENT);
        } catch (ChimpException exc) {
            throw new ExceptionInInitializerError(exc);
        }
    }

    @Test public void testFillWithElement() throws Exception {
        item.fillWithElement(DOC.getDocumentElement());
        checkProperties(item);
    }
    @Test public void testGetElement() throws Exception {
        item.fillWithElement(DOC.getDocumentElement());
        Document doc = XmlUtils.newDocument();
        Element el = item.getElement(doc);
        doc.adoptNode(el);

        SecureItem item2 = new SecureItem();
        item2.fillWithElement(el);
        checkProperties(item2);
    }

    @Test public void testEquals() {
        item.setTitle("Ponybook")
            .setResource("http://www.book.pony/")
            .setUsername("Applejack")
            .setPassword("password".toCharArray())
            .setComments("Yeehaw")
            .setModificationDate(NOW);
        assertTrue(item.equals(item));
        assertFalse(item.equals(null));
        SecureItem item2 = new SecureItem()
            .setTitle("Ponybook")
            .setResource("http://www.book.pony/")
            .setUsername("Applejack")
            .setPassword("password".toCharArray())
            .setComments("Yeehaw")
            .setModificationDate(new Date(NOW.getTime()));
        assertTrue(item.equals(item2));
        assertTrue(item2.equals(item));

        item2.setTitle("Ponynook");
        assertFalse(item.equals(item2));
        item2.setTitle(item.getTitle()).setResource("http://www.book.derp");
        assertFalse(item.equals(item2));
        item2.setResource(item.getResource()).setUsername("Derpy");
        assertFalse(item.equals(item2));
        item2.setUsername(item.getUsername()).setPassword(new char[0]);
        assertFalse(item.equals(item2));
        item2.setPassword(Arrays.copyOf(item.getPassword(), item.getPassword().length))
            .setComments("Muffins");
        assertFalse(item.equals(item2));
        item2.setComments(item.getComments())
            .setModificationDate(new Date(NOW.getTime() + 1000L));
        assertFalse(item.equals(item2));
        item2.setModificationDate(new Date(NOW.getTime()));
        assertTrue(item.equals(item2));
    }

    // TBD testHashCode
}
