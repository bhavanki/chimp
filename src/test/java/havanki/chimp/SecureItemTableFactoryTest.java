package havanki.chimp;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;

public class SecureItemTableFactoryTest {

  private static final String ELEMENTS =
      "<secure-item-table>" +
      "  <secure-item title=\"Instagrass\">" +
      "    <resource>http://www.instagrass.pony/</resource>" +
      "    <username>Fluttershy</username>" +
      "    <password>flutterpass</password>" +
      "    <comments>yay</comments>" +
      "    <modificationDate>2013-01-01T12:34:56-0500</modificationDate>" +
      "  </secure-item>" +
      "  <secure-item title=\"Ponybook\">" +
      "    <resource>http://www.book.pony/</resource>" +
      "    <username>Applejack</username>" +
      "    <password>password</password>" +
      "    <comments>Yeehaw</comments>" +
      "    <modificationDate>2013-01-02T12:34:56-0500</modificationDate>" +
      "  </secure-item>" +
      "  <secure-item title=\"Trotter\">" +
      "    <resource>http://www.trotter.pony/</resource>" +
      "    <username>Rarity</username>" +
      "    <password>rarepass</password>" +
      "    <comments>My word</comments>" +
      "    <modificationDate>2013-01-03T12:34:56-0500</modificationDate>" +
      "  </secure-item>" +
      "</secure-item-table>";
  static final Document DOC;
  static {
    try {
      DOC = XmlUtils.parse(ELEMENTS);
    } catch (ChimpException exc) {
      throw new ExceptionInInitializerError(exc);
    }
  }

  private SecureItemTableFactory factory;

  @Before
  public void setUp() {
    factory = new SecureItemTableFactory();
  }

  @Test
  public void testEmpty() {
    SecureItemTable t = factory.createTable();
    assertEquals(0, t.size());
    assertFalse(t.isDirty());
  }

  @Test
  public void testFromXml() throws Exception {
    SecureItemTable t = factory.createTable(DOC);
    assertEquals(3, t.size());
    assertFalse(t.isDirty());
    assertTrue(t.contains(SecureItemTableTest.item1));
    assertTrue(t.contains(SecureItemTableTest.item2));
    assertTrue(t.contains(SecureItemTableTest.item3));
  }

}
