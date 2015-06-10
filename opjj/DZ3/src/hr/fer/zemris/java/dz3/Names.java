package hr.fer.zemris.java.dz3;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class Names {

	public static void main(String[] args) throws IOException {
		BufferedReader inputF1 = new BufferedReader(
								new InputStreamReader(
									new BufferedInputStream(
										new FileInputStream(args[0])
										),
									"UTF-8"
									)
								);
		BufferedReader inputF2 = new BufferedReader(
				new InputStreamReader(
					new BufferedInputStream(
						new FileInputStream(args[1])
						),
					"UTF-8"
					)
				);
		
		Set<String> namesF1 = new HashSet<String>();
		Set<String> namesF2 = new HashSet<String>();
		
		while(true) {
			/*Ucitava imena iz prve datoteke*/
			String line = inputF1.readLine();
			if(line == null) break;
			line = line.trim();
			if(line.isEmpty()) continue;
			namesF1.add(line);
		}
		inputF1.close();
		while(true) {
			/*Ucitava imena iz druge datoteke*/
			String line = inputF2.readLine();
			if(line == null) break;
			line = line.trim();
			if(line.isEmpty()) continue;
			namesF2.add(line);
		}
		inputF2.close();
		
		namesF1.removeAll(namesF2);	// razlika skupova namesF1 \ namesF2
		
		for(String name : namesF1) {
			System.out.println(name);
		}
	}

}
