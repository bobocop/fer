package hr.fer.zemris.java.dz3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class NamesCounter {

	public static void main(String[] args) throws IOException {
		BufferedReader input = new BufferedReader(
				new InputStreamReader(System.in)
				);
		Map<String, Integer> nameCount = new HashMap<String, Integer>();
		
		while(true) {
			String line = input.readLine();
			if(line.equals("quit")) break;
			if(nameCount.containsKey(line)) {
				nameCount.put(line, nameCount.get(line) + 1);
			} else {
				nameCount.put(line, 1);
			}
		}
		input.close();	// the input ends here
		
		for(String key : nameCount.keySet()) {
			System.out.printf("%s : %d\n", key, nameCount.get(key));
		}
	}

}
