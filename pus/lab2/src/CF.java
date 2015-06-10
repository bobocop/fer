import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;

public class CF {
	
	private static double[][] workingMatrix;
	private static int[][] itemItemMatrix;
	private static int[][] queries;
	private static int nItems;
	private static int nUsers;
	private static int nQueries;
	private static final int ITEM = 0;
	private static final int USER = 1;
	private static final int METHOD = 2;
	private static final int CARD = 3;

	public static void main(String[] args) throws NumberFormatException, IOException {
		
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		
		String line = in.readLine();
		nItems = Integer.parseInt(line.split("\\s+")[0]);
		nUsers = Integer.parseInt(line.split("\\s+")[1]);
		
		itemItemMatrix = new int[nItems][nUsers];
		
		int i = 0;
		
		while (i < nItems) {
			String[] lineSplit = in.readLine().split("\\s+");
			for (int j = 0; j < nUsers; j++)
				itemItemMatrix[i][j] = "X".equals(lineSplit[j]) ? -1 : Integer.parseInt(lineSplit[j]);
			i++;
		}
		
		nQueries = Integer.parseInt(in.readLine());
		queries = new int[nQueries][4];
		
		i = 0;
		
		while (i < nQueries) {
			String[] lineSplit = in.readLine().split("\\s+");
			queries[i][ITEM] = Integer.parseInt(lineSplit[0]);
			queries[i][USER] = Integer.parseInt(lineSplit[1]);
			queries[i][METHOD] = Integer.parseInt(lineSplit[2]);
			queries[i][CARD] = Integer.parseInt(lineSplit[3]);
			i++;
		}
		
		in.close();
		
		for (i = 0; i < nQueries; i++) {
			int user, item;
			double result = 0;
			
			if (queries[i][METHOD] == 0) {
				// item-item, workingMatrix = itemItemMatrix - mean
				workingMatrix = new double[nItems][nUsers];
				for (int j = 0; j < nItems; j++)
					for (int k = 0; k < nUsers; k++)
						workingMatrix[j][k] = itemItemMatrix[j][k] != -1 ? itemItemMatrix[j][k] - mean(j, itemItemMatrix) : 0;
				
				item = queries[i][ITEM] - 1;
				user = queries[i][USER] - 1;
				
				double[] similarities = new double[nItems];
				Arrays.fill(similarities, 0, similarities.length, Double.MAX_VALUE);
				
				for (int j = 0; j < nItems; j++)
					if (j != item)
						similarities[j] = similarity(item, j, workingMatrix);
				
				double[] orderedSimilarities = Arrays.copyOf(similarities, similarities.length);
				Arrays.sort(orderedSimilarities);
				
				double sumUp = 0;
				double sumDown = 0;
				int count = 0;
				
				for (int j = orderedSimilarities.length - 2; j >= 0 && count < queries[i][CARD]; j--) {	
					// -1 because the last one one is that same item (MAX_VALUE similarity)
					int k = 0;
				
					while (Math.abs(orderedSimilarities[j] - similarities[k]) > 0.0000001)
						k++;
					
					if (itemItemMatrix[k][user] != -1 && similarities[k] >= 0) {
						sumUp += similarities[k] * itemItemMatrix[k][user];
						sumDown += similarities[k];
						count++;
					}
				}
				
				
				result = sumUp / sumDown;
				
			} else if (queries[i][METHOD] == 1) {
				// user-user, workingMatrix = transpose(itemItemMatrix) - mean
				int[][] userUserMatrix = new int[nUsers][nItems];
				for (int j = 0; j < nUsers; j++)
					for (int k = 0; k < nItems; k++)
						userUserMatrix[j][k] = itemItemMatrix[k][j];
				
				workingMatrix = new double[nUsers][nItems];
				for (int j = 0; j < nUsers; j++)
					for (int k = 0; k < nItems; k++)
						workingMatrix[j][k] = userUserMatrix[j][k] != -1 ? userUserMatrix[j][k] - mean(j, userUserMatrix) : 0;
				
				item = queries[i][ITEM] - 1;	
				user = queries[i][USER] - 1;
				
				double[] similarities = new double[nUsers];
				Arrays.fill(similarities, 0, similarities.length, Double.MAX_VALUE);
				
				for (int j = 0; j < nUsers; j++)
					if (j != user)
						similarities[j] = similarity(user, j, workingMatrix);
				
				double[] orderedSimilarities = Arrays.copyOf(similarities, similarities.length);
				Arrays.sort(orderedSimilarities);
				
				double sumUp = 0;
				double sumDown = 0;
				int count = 0;
				
				for (int j = orderedSimilarities.length - 2; j >= 0 && count < queries[i][CARD]; j--) {	
					// -1 because the last one one is that same item (MAX_VALUE similarity)
					int k = 0;
				
					while (Math.abs(orderedSimilarities[j] - similarities[k]) > 0.0000001)
						k++;
					
					if (userUserMatrix[k][item] != -1 && similarities[k] >= 0) {
						sumUp += similarities[k] * userUserMatrix[k][item];
						sumDown += similarities[k];
						count++;
					}	
				}
				
				result = sumUp / sumDown;
				
			} else {
				throw new UnsupportedOperationException("Unknown method " + queries[i][METHOD] + ", valid are 0 (item-item) or 1 (user-user)");
			}
			
			System.out.println(new DecimalFormat("#.000").format(new BigDecimal(result).setScale(3, RoundingMode.HALF_UP)));
		}
	}
		
	private static double similarity(int row1, int row2, double[][] matrix) {
		double sum = 0;
		
		for (int i = 0; i < matrix[row1].length; i++)
			if (matrix[row1][i] != 0 && matrix[row2][i] != 0)
				sum += matrix[row1][i] * matrix[row2][i];
		
		return sum / (norm(row1, matrix) * norm(row2, matrix));
	}
	
	private static double mean(int row, int[][] matrix) {
		double sum = 0;
		int k = 0;
		
		for (int i = 0; i < matrix[row].length; i++) {
			if (matrix[row][i] != -1) {
				sum += matrix[row][i];
				k++;
			}
		}
		
		return sum / k;
	}
	
	private static double norm(int row, double[][] matrix) {
		double sum = 0;
		
		for (int i = 0; i < matrix[row].length; i++)
			if (matrix[row][i] != 0)
				sum += Math.pow(matrix[row][i], 2);
		
		return Math.sqrt(sum);
	}
	
}