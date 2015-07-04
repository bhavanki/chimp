package havanki.chimp;

import java.io.File;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class PassFileReaderTest {
  private static final String TEST_XML = "/test.xml";

  private static File testFile;

  @BeforeClass
  public static void setUpClass() throws Exception {
    testFile = new File(PassFileReaderTest.class.getResource(TEST_XML).toURI());
  }

  private PassFileReader r;

  @Before
  public void setUp() {
    r = new PassFileReader(testFile);
  }

  @Test
  public void testReadUnencrypted() throws Exception {
    SecureItemTable tbl = r.read(new char[0]);
    assertEquals(1, tbl.size());
    SecureItem item = tbl.get("Ponybook");
    assertNotNull(item);
  }
  // encrypted reading tested in PassFileWriterTest
}
