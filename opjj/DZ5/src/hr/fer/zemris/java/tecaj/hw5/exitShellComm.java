package hr.fer.zemris.java.tecaj.hw5;

import java.io.BufferedReader;
import java.io.BufferedWriter;

public class exitShellComm implements ShellCommand {

	/**
	 * Exits the shell, silently ignores any arguments.
	 */
	@Override
	public ShellStatus executeCommand(BufferedReader in, BufferedWriter out,
			String[] arguments) {
		return ShellStatus.TERMINATE;
	}
}
