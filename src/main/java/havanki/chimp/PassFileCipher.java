/*
 * CHIMP 1.2 - Cyber Helper Internet Monkey Program
 * Copyright (C) 2001-2015 Bill Havanki
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package havanki.chimp;

import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.*;
import javax.crypto.spec.*;

/**
 * A generator for ciphers used to encrypt and decrypt password files.
 */
public class PassFileCipher {

  private static final Random RANDOM = new Random();

  private final String cipherAlgorithm;
  private final int iterationCount;
  private final byte[] salt;

  /**
   * Creates a new cipher generator.
   *
   * @param cipherAlgorithm cipher algorithm
   * @param iterationCount iteration count
   * @param saltLength length of salt (bytes)
   */
  public PassFileCipher(String cipherAlgorithm, int iterationCount,
                        int saltLength) {
    this(cipherAlgorithm, iterationCount, generateSalt(saltLength));
  }

  /**
   * Creates a new cipher generator.
   *
   * @param cipherAlgorithm cipher algorithm
   * @param iterationCount iteration count
   * @param salt salt as bytes
   */
  public PassFileCipher(String cipherAlgorithm, int iterationCount,
                        byte[] salt) {
    this.cipherAlgorithm = cipherAlgorithm;
    this.iterationCount = iterationCount;
    this.salt = Arrays.copyOf(salt, salt.length);
  }

  public String getCipherAlgorithm() {
    return cipherAlgorithm;
  }

  public int getIterationCount() {
    return iterationCount;
  }

  public byte[] getSalt() {
    return Arrays.copyOf(salt, salt.length);
  }

  private static byte[] generateSalt(int length) {
    byte[] salt = new byte[length];
    RANDOM.nextBytes(salt);
    return salt;
  }

  /**
   * Gets a cipher.
   *
   * @param mode cipher mode: {@code Cipher.ENCRYPT_MODE} or
   * {@code Cipher.DECRYPT_MODE}
   * @param password password as key
   * @return cipher
   * @throws GeneralSecurityException if a cipher could not be created
   */
  public Cipher getCipher(int mode, char[] password)
      throws GeneralSecurityException {
    PBEKeySpec keyspec = new PBEKeySpec(password);
    SecretKeyFactory fac = SecretKeyFactory.getInstance(cipherAlgorithm);
    SecretKey key = fac.generateSecret(keyspec);

    Cipher c = Cipher.getInstance(cipherAlgorithm);
    c.init(mode, key, new PBEParameterSpec(salt, iterationCount));
    return c;
  }
}
