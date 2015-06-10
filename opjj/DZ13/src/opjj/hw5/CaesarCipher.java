package opjj.hw5;

/**
 * The Caesar cipher is a simple encryption techinique that works as follows:
 * every letter in a message is substituted with its <i>shift</i>-th successor. The
 * shift is a parameter that is chosen by the user. 
 */
public class CaesarCipher {
	private static final char[] alphabet = { 'a', 'b', 'c', 'd', 'e', 'f', 'g',
			'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
			'v', 'w', 'x', 'y', 'z' };
	
	/**
	 * Encodes the message using the Caesar cipher encryption technique. It uses the
	 * English alphabet (ASCII) when encoding and decoding. The uppercase and non-alphabetic
	 * characters are not rotated. If a non-English letter is supposed to be rotated or the 
	 * shift is negative an exception will be thrown.
	 * @param input The message to be encoded.
	 * @param shift The shift (right) to be used during encryption.
	 * @return The encoded message.
	 */
    public String encode(String input, int shift) {
    	if(shift < 0) {
    		throw new RuntimeException("Shift must be positive");
    	}
    	char[] newInput = new char[input.length()];
    	for(int i = 0; i < input.length(); i++) {
    		char c = input.charAt(i);
    		if(Character.isAlphabetic(c) && Character.isLowerCase(c)) {
    			newInput[i] = alphabet[(getLetterPos(c) + shift) % 26];
    		} else {
    			newInput[i] = c;
    		}
    	}
    	return new String(newInput);
    }
    
    /**
     * @return index of the provided letter in English alphabet. Exception if
     * the letter is not found.
     */
    private int getLetterPos(char c) {
    	for(int i = 0; i < alphabet.length; i++) {
    		if(alphabet[i] == c) {
    			return i;
    		}
    	}
    	throw new RuntimeException("Alphabet not supported");
    }

    /**
     * Decodes the provided string which was encoded using the Caesar cipher.
     * @param input The message to be decoded.
     * @param shift The shift to be used (corresponds to shift used during encryption).
     * @return The decoded message.
     */
    public String decode(String input, int shift) {
        return encode(input, 26 - shift);
    }
}
