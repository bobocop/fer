package hr.fer.zemris.java.tecaj.hw5;

import java.io.BufferedReader;
import java.io.BufferedWriter;

public interface ShellCommand {
	
	/**
	 * @param in command input stream
	 * @param out command output stream
	 * @param arguments command arguments
	 * @return whether the shell continues or terminates
	 */
	public ShellStatus executeCommand(BufferedReader in, 
										BufferedWriter out, 
										String[] arguments);
}
