import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.codec.digest.DigestUtils;


public class Simhash {

	private static final int TEXT = 0;
	private static final int DIST = 1;
	private static byte[][] textHash;
	private static int[][] query;
	
	public static void main(String[] args) throws NumberFormatException, IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		
		textHash = new byte[Integer.parseInt(in.readLine())][16];
		for (int i = 0; i < textHash.length; i++) 
			textHash[i] = calculate(in.readLine());
		
		query = new int[Integer.parseInt(in.readLine())][2];
		for (int i = 0; i < query.length; i++) {
			String[] temp = in.readLine().split("\\s+");
			query[i][TEXT] = Integer.parseInt(temp[TEXT]);
			query[i][DIST] = Integer.parseInt(temp[DIST]);
		}
		
		in.close();
		
		for (int i = 0; i < query.length; i++)
			System.out.println(countSimilar(query[i][TEXT], query[i][DIST]));
	}
	
	private static int countSimilar(int n, int dist) {
		int similar = 0;
		
		for (int i = 0; i < textHash.length; i++) 
			similar += getHammingDistance(textHash[n], textHash[i]) <= dist ? 1 : 0;
		
		return similar > 0 ? similar - 1 : similar;	// n was similar to itself
	}
	
	private static int getHammingDistance(byte[] h1, byte[] h2) {
		int distance = 0;
		
		for (int i = 0; i < h1.length; i++)
			for (int j = 0; j < 8; j++)
				distance += ((h1[i] >> j) & 1) != ((h2[i] >> j) & 1) ? 1 : 0;
		
		return distance;
	}
	
	private static byte[] calculate(String s) {
		byte[] hash = null;
		int[] v = new int[128];
		
		for (String c : s.split("\\s+")) {
			hash = DigestUtils.getMd5Digest().digest(c.getBytes());
			for (int i = 0; i < hash.length; i++)
				for (int j = 0; j < 8; j++)
					v[i * 8 + j] += (1 & (hash[i] >> (7 - j))) == 1 ? 1 : -1;		
		}
		
		hash = new byte[16];
		
		for (int i = 0; i < hash.length; i++) 
			for (int j = 0; j < 8; j++)
				hash[i] |= v[i * 8 + j] >= 0 ? (1 << (7 - j)) : 0;
				
		return hash;
	}
	
}
