package hr.fer.zemris.java.tecaj.hw5;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class catShellComm implements ShellCommand {

	/**
	 * Prints the contents of a file using the specified or default
	 * encoding if none is provided.
	 */
	@Override
	public ShellStatus executeCommand(BufferedReader in, BufferedWriter out,
			String[] arguments) {
		PrintWriter shellOutput = new PrintWriter(out);
		if(arguments.length < 1 || arguments.length > 2) {
			shellOutput.println("Usage: cat FILE (optnl.)CHARSET");
			return ShellStatus.CONTINUE;
		}
		BufferedReader fin = null;
		try {
			Path filePath = Paths.get(arguments[0]);
			if(arguments.length > 1) {
				fin = new BufferedReader(
						new	InputStreamReader(
						new FileInputStream(filePath.toFile()), 
											arguments[1]
											)
										);
			} else {
				fin = new BufferedReader(
						new InputStreamReader(
						new FileInputStream(filePath.toFile())
											)
										);
			}
			char[] buf = new char[4096];
			while(fin.read(buf) != -1) {
				shellOutput.print(buf);
			}
			shellOutput.println();
		} catch (UnsupportedEncodingException e) {
			shellOutput.println("Specified encoding is not supported");
		} catch (FileNotFoundException e) {
			shellOutput.println("File \'" + arguments[0] + "\' not found");
		} catch (IOException e) {
			shellOutput.println("Could not write to the output stream");
		} finally {
			if(fin != null) {
				try {
					fin.close();
				} catch (IOException e) {
					shellOutput.println("I/O error: cannot close the input file");
				}
			}
		}
		return ShellStatus.CONTINUE;
	}

}
