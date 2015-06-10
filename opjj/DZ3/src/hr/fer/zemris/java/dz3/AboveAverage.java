package hr.fer.zemris.java.dz3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class AboveAverage {

	public static void main(String[] args) throws IOException {
		BufferedReader input = new BufferedReader(
				new InputStreamReader(System.in)
				);
		List<Double> numList = new LinkedList<Double>();
		Double sum = new Double(0);
		
		while(true) {
			String line = input.readLine();
			
			if(line.equals("quit")) break;
			Double number = Double.parseDouble(line);
			sum += number;
			numList.add(number);
		}
		input.close();	// the input ends here
		
		Double average = sum / numList.size();
		Iterator<Double> iter = numList.iterator();
		
		while(iter.hasNext()) {
			if(iter.next() < ((6 / 5) * average)) {
				iter.remove();	// removes those which are < 120% of avg
			}
		}
		/*Outputs the elements in the descending order*/
		Collections.sort(numList);
		Collections.reverse(numList);
		for(Double num : numList) {
			System.out.println(num);
		}
	}

}
