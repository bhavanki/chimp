package havanki.chimp;

import java.io.File;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class PassFileWriterTest {
    private static SecureItemTable tbl;

    @BeforeClass public static void setUpClass() {
        tbl = new SecureItemTable();
        tbl.put(new SecureItem().setTitle("Ponybook")
                .setResource("http://www.book.pony")
                .setUsername("Applejack")
                .setPassword("password".toCharArray())
                .setComments("Yeehaw"));
        tbl.put(new SecureItem().setTitle("Trotter")
                .setResource("http://trotter.pony")
                .setUsername("Rarity")
                .setPassword("diamonds".toCharArray())
                .setComments("Oh my"));
        tbl.put(new SecureItem().setTitle("Instagrass")
                .setResource("http://www.instagrass.pony")
                .setUsername("Fluttershy")
                .setPassword("angel".toCharArray())
                .setComments("yay"));
    }

    private PassFileWriter w;
    private File testFile;
    private PassFileReader r;

    @Before public void setUp() throws Exception {
        testFile = File.createTempFile("pfwt", "xml");
        w = new PassFileWriter(testFile);
        r = new PassFileReader(testFile);
    }

    @Test public void testWriteUnencrypted() throws Exception {
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
