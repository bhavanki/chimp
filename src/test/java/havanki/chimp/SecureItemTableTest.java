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
    assertFalse(t.isDirty());
  }

  @Test
  public void testPutAndGet() {
    t.put(item1);
    assertEquals(1, t.size());
    assertEquals(item1, t.get("Ponybook"));
    assertTrue(t.isDirty());
    assertNull(t.get("Trotter"));

    t.put(item2);
    assertEquals(2, t.size());
    assertEquals(item2, t.get("Trotter"));
    assertTrue(t.isDirty());

    t.setDirty(false);
    t.get("Trotter");
    assertFalse(t.isDirty());
  }

  @Test
  public void testRemove() {
    t.put(item1).put(item2);
    t.setDirty(false);

    t.remove(item1.getTitle());
    assertEquals(1, t.size());
    assertNull(t.get("Ponybook"));
    assertEquals(item2, t.get("Trotter"));
    assertTrue(t.isDirty());
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

  @Test public void testGetDocument() throws Exception {
    t.put(item1).put(item2).put(item3);
    Document doc = t.getDocument();

    SecureItemTable t2 = new SecureItemTableFactory().createTable(doc);
    assertEquals(3, t.size());
    assertTrue(t.contains(SecureItemTableTest.item1));
    assertTrue(t.contains(SecureItemTableTest.item2));
    assertTrue(t.contains(SecureItemTableTest.item3));
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
