/* Karlo Podbreznicki, 0036458312 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class KromatskiBroj {
	
	private static int[][] adjMatrix;
	private static int nVertices;

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
			for (int j = 0; j < nVertices; j++)
				adjMatrix[i][j] = Integer.parseInt(adjStr[j]);
			i++;
		}
		
		in.close();
		
		int[] color = new int[nVertices];
		int[] visited = new int[nVertices];
		int v = 0;
		color[v] = 0;
		
		while (sum(visited) < nVertices) {
			visited[v] = 1;
			int[] neighbors = getNeighbors(v);
			
			for (int x : neighbors)
				if (color[x] == color[v])
					color[x]++;
			
			v = firstUnvisitedNeighbor(v, visited);
			v = v != -1 ? v : firstUnvisited(visited);
		}
		
		System.out.println(max(color) + 1);
		
		/*
		outer:
		while (++k < nVertices) {
			for (i = 0; i < Math.pow(k, nVertices); i++) {
				String sTemp = Integer.toString(i, k);
				int j;
				
				for (j = 0; j < nVertices - sTemp.length(); j++) {
					var[j] = 0;
				}
				
				for (int l = 0; l < sTemp.length(); l++) {
					var[j++] = Character.getNumericValue(sTemp.charAt(l));
				}
			
				if (checkColoring(var)) {
					foundColoring = Arrays.copyOf(var, var.length);
					break outer;
				}
			}
		}*/
	}
	
	private static int[] getNeighbors(int v) {
		int size = 0;
		
		for (int i = 0; i < nVertices; i++) {
			if (adjMatrix[v][i] == 1)
				size++;
		}
		
		int[] ret = new int[size];
		
		for (int i = 0, j = 0; i < nVertices; i++) {
			if (adjMatrix[v][i] == 1)
				ret[j++] = i;
		}
		
		
		return ret;
	}
	
	private static int firstUnvisited(int[] visited) {
		for (int i = 0; i < nVertices; i++)
			if (visited[i] == 0)
				return i;
		
		return -1;	// should never happen
	}
	
	private static int firstUnvisitedNeighbor(int v, int[] visited) {
		for (int i = 0; i < nVertices; i++)
			if (isNeighborOf(v, i) && visited[i] == 0)
				return i;
		
		return -1;
	}
	
	private static int sum(int[] arr) {
		int sum = 0;
		
		for (int i = 0; i < arr.length; i++)
			sum += arr[i];
		
		return sum;
	}
	
	private static int max(int[] arr) {
		int max = arr[0];
		
		for (int i = 1; i < arr.length; i++)
			max = arr[i] > max ? arr[i] : max;
		
		return max;
	}
	
	/*
	 * Vraca true ukoliko su vrhovi a i b susjedni.
	 */
	private static boolean isNeighborOf(int a, int b) {
		return adjMatrix[a][b] > 0;
	}
	
}
