package hr.fer.zemris.java.tecaj.hw5;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class mkdirShellComm implements ShellCommand {

	/**
	 * Creates a new directory.
	 */
	@Override
	public ShellStatus executeCommand(BufferedReader in, BufferedWriter out,
			String[] arguments) {
		PrintWriter shellOutput = new PrintWriter(out);
		if(arguments.length != 1) {
			shellOutput.println("Usage: mkdir DIRNAME");
			return ShellStatus.CONTINUE;
		}
		Path newDir = Paths.get(Shell.parsePath(arguments[0]));
		try {
			if(Files.exists(newDir)) {
				shellOutput.println("The specified directory already exists");
			} else {
				Files.createDirectories(newDir);
			}
		} catch (IOException e) {
			shellOutput.println("I/O error");
		}
		return ShellStatus.CONTINUE;
	}

}
