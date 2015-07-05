package havanki.chimp;

import org.w3c.dom.Document;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SecureItemTableTest {
  static SecureItem item1, item2, item3;

  static {
    try {
      item1 = new SecureItemBuilder("Ponybook")
          .resource("http://www.book.pony/")
          .username("Applejack")
          .password("password".toCharArray())
          .comments("Yeehaw")
          .modificationDate(new SecDate("2013-01-02T12:34:56-0500"))
          .username("Applejack")
          .build();
      item2 = new SecureItemBuilder("Trotter")
          .resource("http://www.trotter.pony/")
          .username("Rarity")
          .password("rarepass".toCharArray())
          .comments("My word")
          .modificationDate(new SecDate("2013-01-03T12:34:56-0500"))
          .build();
      item3 = new SecureItemBuilder("Instagrass")
          .resource("http://www.instagrass.pony/")
          .username("Fluttershy")
          .password("flutterpass".toCharArray())
          .comments("yay")
          .modificationDate(new SecDate("2013-01-01T12:34:56-0500"))
          .build();
    } catch (Exception e) {
      throw new ExceptionInInitializerError(e);
    }
  }

  private SecureItemTable t;

  @Before
  public void setUp() {
    t = new SecureItemTable();
  }

  @Test
  public void testEmpty() {
    assertEquals(0, t.size());
  }

  @Test
  public void testPutAndGet() {
    t.put(item1);
    assertEquals(1, t.size());
    assertEquals(item1, t.get("Ponybook"));
    assertNull(t.get("Trotter"));
    t.put(item2);
    assertEquals(2, t.size());
    assertEquals(item2, t.get("Trotter"));
  }

  @Test
  public void testRemove() {
    t.put(item1).put(item2);
    t.remove(item1.getTitle());
    assertEquals(1, t.size());
    assertNull(t.get("Ponybook"));
    assertEquals(item2, t.get("Trotter"));
  }

  @Test
  public void testContains() {
    t.put(item1).put(item2);
    assertTrue(t.contains(item1));
    assertTrue(t.contains(item2));
    assertFalse(t.contains(item3));
  }

  @Test
  public void testGetTitlesInOrder() {
    t.put(item1).put(item2).put(item3);
    String[] titles = t.getTitlesInOrder();
    assertArrayEquals(new String[] { "Instagrass", "Ponybook", "Trotter"},
                      titles);
  }
  @Test
  public void testGetUsernamesInTitleOrder() {
    t.put(item1).put(item2).put(item3);
    String[] usernames = t.getUsernamesInTitleOrder();
    assertArrayEquals(new String[] { "Fluttershy", "Applejack", "Rarity"},
                      usernames);
  }

  private static final String ELEMENTS =
      "<secure-item-table>" +
      "  <secure-item title=\"Instagrass\">" +
      "    <resource>http://www.instagrass.pony/</resource>" +
      "    <username>Fluttershy</username>" +
      "    <password>flutterpass</password>" +
      "    <comments>Yay</comments>" +
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
  private static final Document DOC;
  static {
    try {
      DOC = XmlUtils.parse(ELEMENTS);
    } catch (ChimpException exc) {
      throw new ExceptionInInitializerError(exc);
    }
  }

  @Test public void testFillWithDocument() throws Exception {
    t.fillWithDocument(DOC);
    assertEquals(3, t.size());
    String[] titles = t.getTitlesInOrder();
    assertArrayEquals(new String[] { "Instagrass", "Ponybook", "Trotter"},
                      titles);
  }
  @Test public void testGetDocument() throws Exception {
    t.fillWithDocument(DOC);
    Document doc = t.getDocument();
    SecureItemTable t2 = new SecureItemTable();
    t2.fillWithDocument(doc);
    String[] titles = t.getTitlesInOrder();
    assertArrayEquals(new String[] { "Instagrass", "Ponybook", "Trotter"},
                      titles);
  }

  @Test public void testEquals() {
    t.put(item1).put(item2);
    assertTrue(t.equals(t));
    assertFalse(t.equals(null));
    SecureItemTable t2 = new SecureItemTable();
    t2.put(item1).put(item2);
    assertTrue(t.equals(t2));
    assertTrue(t2.equals(t));
    t2.put(item3);
    assertFalse(t.equals(t2));
    t2.remove(item3.getTitle()).remove(item1.getTitle());
    assertFalse(t.equals(t2));
    t2.put(item1);
    assertTrue(t.equals(t2));
  }
  // TBD testHashCode
}
