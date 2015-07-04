package havanki.chimp;

import java.io.File;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class PassFileWriterTest {
  private static SecureItemTable tbl;

  @BeforeClass
  public static void setUpClass() {
    tbl = new SecureItemTable();
    tbl.put(new SecureItemBuilder("Ponybook")
            .resource("http://www.book.pony")
            .username("Applejack")
            .password("password".toCharArray())
            .comments("Yeehaw").build());
    tbl.put(new SecureItemBuilder("Trotter")
            .resource("http://trotter.pony")
            .username("Rarity")
            .password("diamonds".toCharArray())
            .comments("Oh my").build());
    tbl.put(new SecureItemBuilder("Instagrass")
            .resource("http://www.instagrass.pony")
            .username("Fluttershy")
            .password("angel".toCharArray())
            .comments("yay").build());
  }

  private PassFileWriter w;
  private File testFile;
  private PassFileReader r;

  @Before
  public void setUp() throws Exception {
    testFile = File.createTempFile("pfwt", "xml");
    testFile.deleteOnExit();
    w = new PassFileWriter(testFile);
    r = new PassFileReader(testFile);
  }

  @Test
  public void testWriteUnencrypted() throws Exception {
    w.write(tbl, new char[0]);
    SecureItemTable tbl2 = r.read(new char[0]);
    assertEquals(tbl, tbl2);
  }
  @Test public void testWriteEncrypted() throws Exception {
    char[] password = "HOODIENINJA".toCharArray();
    w.write(tbl, password);
    SecureItemTable tbl2 = r.read(password);
    assertEquals(tbl, tbl2);
  }
}
