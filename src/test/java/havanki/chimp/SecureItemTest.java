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
  private static final SecDate NOW = new SecDate();
  private static final String NOW_STRING = NOW.toString();

  private SecureItem item;

  @Test
  public void testGettersAndSetters() {
    item = new SecureItemBuilder("Ponybook")
        .resource("http://www.book.pony/")
        .username("Applejack")
        .password("password".toCharArray())
        .comments("Yeehaw")
        .modificationDate(NOW)
        .build();
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
  @Test(expected=NullPointerException.class)
  public void testNoNullTitle() {
    new SecureItemBuilder((String) null);
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

  @Test
  public void testBuildFromElement() throws Exception {
    item = new SecureItemBuilder(DOC.getDocumentElement()).build();
    checkProperties(item);
  }
  @Test
  public void testGetElement() throws Exception {
    item = new SecureItemBuilder(DOC.getDocumentElement()).build();
    Document doc = XmlUtils.newDocument();
    Element el = item.getElement(doc);
    doc.adoptNode(el);

    SecureItem item2 = new SecureItemBuilder(el).build();
    checkProperties(item2);
  }

  @Test
  public void testEquals() {
    item = new SecureItemBuilder("Ponybook")
        .resource("http://www.book.pony/")
        .username("Applejack")
        .password("password".toCharArray())
        .comments("Yeehaw")
        .modificationDate(NOW)
        .build();
    assertTrue(item.equals(item));
    assertFalse(item.equals(null));
    SecureItem item2 = new SecureItemBuilder("Ponybook")
        .resource("http://www.book.pony/")
        .username("Applejack")
        .password("password".toCharArray())
        .comments("Yeehaw")
        .modificationDate(new Date(NOW.getTime()))
        .build();
    assertTrue(item.equals(item2));
    assertTrue(item2.equals(item));

    item2 = new SecureItemBuilder(item).title("Ponynook").build();
    assertFalse(item.equals(item2));
    item2 = new SecureItemBuilder(item).resource("http://www.book.derp").build();
    assertFalse(item.equals(item2));
    item2 = new SecureItemBuilder(item).username("Derpy").build();
    assertFalse(item.equals(item2));
    item2 = new SecureItemBuilder(item).password(new char[0]).build();
    assertFalse(item.equals(item2));
    item2 = new SecureItemBuilder(item).comments("Muffins").build();
    assertFalse(item.equals(item2));
    item2 = new SecureItemBuilder(item)
        .modificationDate(new Date(NOW.getTime() + 1000L)).build();
    assertFalse(item.equals(item2));
  }

    // TBD testHashCode
}
