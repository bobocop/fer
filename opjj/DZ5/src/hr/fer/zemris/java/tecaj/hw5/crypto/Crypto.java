package hr.fer.zemris.java.tecaj.hw5.crypto;

import java.io.BufferedReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Crypto {

	public static void main(String[] args)  {
		if(args.length < 2 || args.length > 3) {
			System.out.println("Usage: <command> <arguments> ...(max 2)");
			System.exit(-1);
		}
		
		try {
			if(args[0].equals("checksha")) {
				(new MySHAChecker(args[1])).operate();
			} else if(args[0].equals("decrypt") || args[0].equals("encrypt")){
				if(args.length < 3) {
					System.out.println("Too few input arguments for " +args[0]);
					System.exit(-1);
				}
				(new MyEnDeCryptor(args[0], args[1], args[2])).operate();
			} else {
				System.out.println("Invalid command");
			}
		} catch (InvalidKeyException | NoSuchAlgorithmException
				| NoSuchProviderException | NoSuchPaddingException
				| InvalidAlgorithmParameterException
				| IllegalBlockSizeException | BadPaddingException e) {
			System.err.println("An error has occured in the encryption method: ");
			e.printStackTrace();
			System.err.println("The data structures might have been "
								+ "set up incorrectly");
		}
	}
	
}

class MySHAChecker {
	MessageDigest sha;
	byte[] buf;
	byte[] providedDigest;
	byte[] generatedDigest;
	String inputFileName;
	
	public MySHAChecker(String inputFileName) 
			throws NoSuchAlgorithmException, NoSuchProviderException {
		this.sha = MessageDigest.getInstance("SHA-1");
		this.inputFileName = inputFileName;
		this.buf = new byte[4096];
		this.providedDigest = new byte[128];
		this.generatedDigest = new byte[128];
	}
	
	/**
	 * Begins the digest.
	 */
	public void operate() {
		try(BufferedReader in = new BufferedReader(
								new InputStreamReader(System.in)
								);
			FileInputStream src = new FileInputStream(inputFileName)) {
			
			System.out.println("Please provide the expected "
								+ "sha signature for " + inputFileName);
			providedDigest = MyEnDeCryptor.hextobyte(in.readLine());
			int len;
			while((len = src.read(buf)) != -1) {
				sha.update(buf, 0, len);
			}
			generatedDigest = sha.digest();
			if(Arrays.equals(providedDigest, generatedDigest)) {
				System.out.println("Digesting completed. Digest of "
									+ inputFileName + " matches expected "
									+ "digest");
			} else {
				System.out.println("Digesting completed. Digest of "
									+ inputFileName + "does not match the "
									+ "expected digest. Digest was: "
									+ toHex(generatedDigest));
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found.");
			System.exit(-1);
		} catch (IOException e) {
			System.out.println("I/O error");
			System.exit(-1);
		}
	}
	
	/**
	 * Converts the byte array into a string of hexadecimal numbers.
	 * @param bytes array
	 * @return the string
	 */
	public static String toHex(byte[] bytes) {
	    BigInteger bi = new BigInteger(1, bytes);
	    return String.format("%0" + (bytes.length << 1) + "x", bi);
	}
}

class MyEnDeCryptor {
	boolean encrypt;
	SecretKeySpec keySpec;
	AlgorithmParameterSpec paramSpec;
	Cipher cipher;
	Path src;
	Path dest;
	byte[] buf;
	
	/**
	 * Call this method to begin the operation specified in the contructor.
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws InvalidAlgorithmParameterException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public void operate() throws 	NoSuchAlgorithmException, 
									NoSuchPaddingException, 
									InvalidKeyException, 
									InvalidAlgorithmParameterException, 
									IllegalBlockSizeException, 
									BadPaddingException {
		try(BufferedReader lineInput = 	new BufferedReader(
										new InputStreamReader(System.in)
										);
			FileInputStream in = new FileInputStream(src.toFile());
			FileOutputStream out = new FileOutputStream(dest.toFile())) {
			
			System.out.println("Please provide password as hex-encoded text:");
			getHexPassword(lineInput.readLine());
			System.out.println("Please provide initialization vector as "
								+ "hex-encoded text:");
			getIvector(lineInput.readLine());
			
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(encrypt ? 	
						Cipher.ENCRYPT_MODE : 
						Cipher.DECRYPT_MODE, keySpec, paramSpec);
			
			int len;
			while((len = in.read(buf)) != -1) {
				out.write(cipher.update(buf, 0, len));
			}
			out.write(cipher.doFinal());
			
			System.out.print(encrypt? "Encryption " : "Decryption ");
			System.out.print(" completed. Generated file " 
								+ dest.getFileName() + " based on "
								+ src.getFileName());
			} catch (FileNotFoundException e) {
				System.err.println("File not found.");
				System.exit(-1);
			} catch (IOException e) {
				System.err.println("I/O error");
				System.exit(-1);
			}
	}
	
	/**
	 * Initializes some of the structures. It should be passed either the
	 * string "encrypt" or "decrypt" as the first argument. If passed something else,
	 * it will act as if "decrypt" is entered.
	 * @param operation
	 * @param inputFileName
	 * @param outputFileName
	 */
	public MyEnDeCryptor(String op, String inputFileName, String outputFileName){
		this.encrypt = op.equals("encrypt");	// otherwise it's decrypt
		this.buf = new byte[4096];
		this.src = Paths.get(inputFileName);
		this.dest = Paths.get(outputFileName);
		if(!Files.isReadable(src) && !Files.isDirectory(src)) {
			System.out.println("Cannot access the specified file.");
			System.exit(-1);
		}
	}
	
	/**
	 * Sets the key/password (provided as a string).
	 */
	public void getHexPassword(String keyText) {
		this.keySpec = new SecretKeySpec(hextobyte(keyText), "AES");
	}
	
	/**
	 * Sets the initialization vector (provided as a string).
	 */
	public void getIvector(String ivText) {
		this.paramSpec = new IvParameterSpec(hextobyte(ivText));
	}
	
	/**
	 * Converts the string that represents hexadecimal bytes into the
	 * byte array.
	 * @param the text to be converted
	 * @return the text as bytes
	 */
	public static byte[] hextobyte(String keytext) {
		int len = keytext.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(keytext.charAt(i), 16) << 4)
	                             + Character.digit(keytext.charAt(i+1), 16));
	    }
	    return data;
	}
}