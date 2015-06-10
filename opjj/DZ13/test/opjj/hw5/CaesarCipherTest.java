package opjj.hw5;

import static org.junit.Assert.*;

import org.junit.Test;

public class CaesarCipherTest {
	private CaesarCipher cipher = new CaesarCipher();
	
	@Test
	public void doNotChangeTheUpperCaseAndSpecial() {
		assertEquals("AB-C!DEF", cipher.encode("AB-C!DEF", 5));
	}
	
	@Test(expected=RuntimeException.class)
	public void exceptionIfNotEnglish() {
		cipher.encode("Čoban čuva ovčice", 1);
	}
	
	@Test 
	public void shift26IsTheSameAs0() {
		assertEquals(cipher.encode("Caesar cipher", 0), cipher.encode("Caesar cipher", 26));
	}
	
	@Test(expected=RuntimeException.class)
	public void exceptionOnNegativeShiftIsTheSameAsPositive() {
		cipher.encode("Caesar cipher", -28);
	}
	
	@Test
	public void sampleEncryption() {
		assertEquals(cipher.encode("efghi", 12), cipher.decode("efghi", 14));
		assertEquals("JUqlw Thvw Zcc", cipher.encode("JUnit Test Zzz", 3));
		assertEquals("qrstu", cipher.encode("efghi", 12));
		assertEquals("JUnit Test Zzz", cipher.decode("JUqlw Thvw Zcc", 3));
	}
}
