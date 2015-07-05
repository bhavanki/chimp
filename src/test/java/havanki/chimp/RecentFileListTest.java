package havanki.chimp;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

public class RecentFileListTest {

  private static final File FILE1 = new File("/file1");
  private static final File FILE2 = new File("/file2");
  private static final File FILE3 = new File("/file3");
  private static final File[] FILES = new File[] { FILE1, FILE2, FILE3 };

  private RecentFileList l;

  @Before
  public void setUp() {
    l = new RecentFileList();
  }

  @Test
  public void testAddAndIteration() {
    l.add(FILE1);
    l.add(FILE2);
    l.add(FILE3);

    int ct = 0;
    for (File f : l) {
      assertEquals(FILES[2 - (ct++)], f);  // reverse of adding order
    }
  }

  @Test
  public void testAddToMaxLength() {
    File file4 = new File("/file4");
    File file5 = new File("/file5");
    File file6 = new File("/file6");
    File file7 = new File("/file7");
    File file8 = new File("/file8");
    File file9 = new File("/file9");
    File filea = new File("/filea");
    File fileb = new File("/fileb");

    l.add(FILE1);
    l.add(FILE2);
    l.add(FILE3);
    l.add(file4);
    l.add(file5);
    l.add(file6);
    l.add(file7);
    l.add(file8);
    l.add(file9);
    l.add(filea);
    l.add(fileb);

    File[] expectedFiles = new File[] {
      FILE2, FILE3, file4, file5, file6,
      file7, file8, file9, filea, fileb
    };
    int ct = 0;
    for (File f : l) {
      assertEquals(expectedFiles[9 - (ct++)], f);  // reverse of adding order
    }
  }

  @Test
  public void testClear() {
    l.add(FILE1);
    l.add(FILE2);
    l.add(FILE3);
    l.clear();
    for (File f : l) {
      fail();
    }
  }

}
