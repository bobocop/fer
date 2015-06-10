package hr.fer.zemris.java.tecaj.hw5;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.PrintWriter;

public class treeShellComm implements ShellCommand {
	
	/**
	 * Prints the specified directory tree.
	 */
	@Override
	public ShellStatus executeCommand(BufferedReader in, BufferedWriter out,
			String[] arguments) {
		PrintWriter shellOutput = new PrintWriter(out);
		if(arguments.length < 1) {
			shellOutput.println("Usage: tree DIRECTORY");
			return ShellStatus.CONTINUE;
		}
		File rootDir = new File(Shell.parsePath(arguments[0]));
		if(rootDir.isDirectory() && rootDir.exists()) {
			try {
				shellOutput.println(rootDir.getAbsolutePath());
				printDir("", rootDir, shellOutput);
			} catch(NullPointerException e) {
				shellOutput.println("Could not open the root directory");
			}
		} else {
			shellOutput.println("Please provide an existing directory");
		}
		return ShellStatus.CONTINUE;
	}
	
	/**
	 * I made this method during the class. It recursively walks through
	 * the directories and files, printing their names with considering
	 * their postition in the directory tree.
	 * @param spaces denote the depth
	 * @param filename (directory)
	 * @param shellOutput output stream
	 */
	public void printDir(String spaces, File filename, PrintWriter shellOutput) {
		File[] djeca = filename.listFiles();
		if(djeca != null) {
			for(File file : djeca) {
				if(file.isDirectory()) {
					shellOutput.println(spaces + file.getName());
					printDir(spaces + "  ", file, shellOutput);
				} else {
					shellOutput.println(spaces + file.getName());
				}
			}
		}
	}
}