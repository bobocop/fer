package hr.fer.zemris.java.tecaj_14.utility;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Used to compare the password digests when authenticating users
 * during the login process.
 * @author karlo
 *
 */
public class PasswordChecker {
	MessageDigest shaDigest;
	
	public PasswordChecker() {
		try {
			this.shaDigest = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			System.err.println("SHA-1 not supported on this system.");
		}
	}
	
	/**
	 * Returns the digest of the provied password as text.
	 * @param password The password to calculate hash from.
	 * @return The text representation of parameter's digest.
	 */
	public String getPasswordHash(String password) {
		return hexEncode(calcHash(password));
	}
	
	/**
	 * Checks if a password matches the stored digest. Used when authenticating a user.
	 * @param passwordHash String that is fetched from some persistant storage, it
	 * is created based on registered user's password.
	 * @param password String which digest is checked against the provided hash value.
	 * @return True if the digests match, that is, the correct password was provided.
	 */
	public boolean check(String passwordHash, String password) {
		return Arrays.equals(hexToByte(passwordHash), calcHash(password));
	}
	
	/**
	 * Calculates a digest of provided password.
	 * @param password String to digest
	 * @return An array of bytes that is the SHA-1 digest of the parameter.
	 */
	private byte[] calcHash(String password) {
		shaDigest.update(password.getBytes());
		return shaDigest.digest();
	}
	
	/**
	 * Converts the string that represents hexadecimal bytes into the
	 * byte array.
	 * @param the text to be converted
	 * @return the text as bytes
	 */
	public byte[] hexToByte(String keytext) {
		int len = keytext.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(keytext.charAt(i), 16) << 4)
	                             + Character.digit(keytext.charAt(i+1), 16));
	    }
	    return data;
	}
	
	/**
	 * Converts the byte array into a string of hexadecimal numbers.
	 * @param bytes array
	 * @return the string
	 */
	public String hexEncode(byte[] bytes) {
	    BigInteger bi = new BigInteger(1, bytes);
	    return String.format("%0" + (bytes.length << 1) + "x", bi);
	}
}
