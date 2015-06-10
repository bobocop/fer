package hr.fer.zemris.java.dz3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class NoDupLines {

	public static void main(String[] args) throws IOException {
		BufferedReader input = new BufferedReader(
								new InputStreamReader(System.in)
								);
		List<String> inputStrings = new LinkedList<String>();
		while(true) {
			String line = input.readLine();
			if(line.isEmpty()) break;
			inputStrings.add(0, line);
		}
		Object[] inputArray = inputStrings.toArray();
		removeDubsReverse(inputArray);
		
	}

	private static void removeDubsReverse(Object[] inputArray) {
		/*
		 * A sequence of input lines has already been reversed because of using
		 * the add(0, x) method of the linked list. 
		 * Note: when removing the duplicates, the remaining instances are
		 * the ones which have been added last.
		 */
		Set<String> inputSet = new LinkedHashSet<String>();
		for(Object linija : inputArray) {
			inputSet.add((String) linija);
		}
		for(String line : inputSet) {
			System.out.println(line);
		}
	}
}
