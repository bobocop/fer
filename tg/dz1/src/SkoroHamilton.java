/* Karlo Podbreznicki, 0036458312 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class SkoroHamilton {
	
	private static int[][] adjMatrix;
	private static int nVertices;
	private static int[] foundPath = null;

	public static void main(String[] args) throws NumberFormatException, IOException {
		
		BufferedReader in = new BufferedReader(new FileReader(args[0]));
		
		// broj vrhova
		nVertices = Integer.parseInt(in.readLine());
		// konstruiraj matricu
		adjMatrix = new int[nVertices][nVertices];
		// popuni matricu
		int i = 0;
		in.readLine();	// prazan red
		while (i < nVertices) {
			String[] adjStr = in.readLine().split("\\s+");
			for (int j = 0; j < nVertices; j++) {
				adjMatrix[i][j] = Integer.parseInt(adjStr[j]);
			}
			i++;
		}
		
		in.close();
		
		// prva permutacija vrhova je {0 1 2 ... nVertices - 1}
		int[] perm = new int[nVertices];
		for (i = 0; i < nVertices; i++)
			perm[i] = i;
		
		boolean found = false;
		boolean ham = false;
		while (perm != null) {
			i = 0;
			while (i <= perm.length - 2) {
				if (!isNeighborOf(perm[i], perm[i + 1])) {
					break;
				} else {
					i++;
				}
			}
			
			
			// prvi uvjet: pronadjena staza koja prolazi jednom svim vrhovima
			// drugi uvjet: ako postoji ham. ciklus, graf nije skoro hamiltonovski, zavrsi
			if (i > perm.length - 2) {
				found = true;
				foundPath = perm;
				ham = isNeighborOf(perm[0], perm[nVertices - 1]);
			}
			perm = ham ? null : nextPermutation(perm);
		}
		
		if (ham) foundPath = null;
		System.out.println(found && !ham ? 1 : 0);
		
	}
	
	/*
	 * Pozvati nakon sto je otkriveno je li graf skoro hamiltonovski da se ispise put.
	 */
	private static void printFoundPath() {
		if (foundPath != null) {
			for (int i = 0; i < foundPath.length; i++) {
				System.out.println(foundPath[i] + " ");
			}
		}
	}
	
	/*
	 * Vraca true ukoliko su vrhovi a i b susjedni.
	 */
	private static boolean isNeighborOf(int a, int b) {
		return adjMatrix[a][b] > 0;
	}
	
	/*
	 * Nalazi (leksikografski) sljedecu permutaciju. Ukoliko se zele generirati sve
	 * permutacije nekog niza (kao sto se radi u gl. programu), potrebno je kao inicijalni
	 * argument koristiti taj niz sortiran uzastopno, te zatim opet pozivati metodu
	 * sa vracenom (prvom sljedecom) permutacijom dok ne vrati null.
	 */
	private static int[] nextPermutation(final int[] arr) {
		int i;
		int[] perm = Arrays.copyOf(arr, arr.length);
		
		for (i = perm.length - 2; i >= 0; i--) {
			if (perm[i] < perm[i + 1]) 
				break;
		}
		if (i == - 1) return null;
		
		int sub = perm.length - 1;
		while (perm[i] > perm[sub])
			sub--;
		
		// "swap"
		perm[i] ^= perm[sub];
		perm[sub] ^= perm[i];
		perm[i] ^= perm[sub];
		
		i++;
		sub = perm.length - 1;
		
		while (i < sub) {
			perm[i] ^= perm[sub];
			perm[sub] ^= perm[i];
			perm[i] ^= perm[sub];
			i++;
			sub--;
		}
		
		return perm;
	}

}
