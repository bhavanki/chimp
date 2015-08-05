package havanki.chimp;

// import static org.assertj.core.api.Assertions.assertThat;

import javax.crypto.Cipher;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.*;

public class PassFileCipherTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private PassFileCipher c;

  @Before
  public void setUp() {
    c = new PassFileCipher("PBEWithMD5AndDES", 20, 8);
  }

  @Test
  public void testGetters() {
    assertEquals("PBEWithMD5AndDES", c.getCipherAlgorithm());
    assertEquals(20, c.getIterationCount());
    assertEquals(8, c.getSalt().length);
  }

  @Test
  public void testConstructWithSalt() {
    byte[] salt = new byte[] {
      (byte) 1, (byte) 2, (byte) 3, (byte) 4
    };
    c = new PassFileCipher("PBEWithMD5AndDES", 20, salt);
    assertArrayEquals(salt, c.getSalt());
  }

  @Test
  public void testEnciphering() throws Exception {
    char[] password = "password".toCharArray();
    Cipher e = c.getCipher(Cipher.ENCRYPT_MODE, password);
    Cipher d = c.getCipher(Cipher.DECRYPT_MODE, password);

    String plaintext = "Jamaica";
    byte[] ciphertext = e.doFinal(plaintext.getBytes());
    assertEquals(plaintext, new String(d.doFinal(ciphertext)));
  }
}
