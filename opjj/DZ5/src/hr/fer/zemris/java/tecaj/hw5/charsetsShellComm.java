package hr.fer.zemris.java.tecaj.hw5;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Set;

public class charsetsShellComm implements ShellCommand {
	Map<String, Charset> chsets;
	
	public charsetsShellComm() {
		this.chsets = Charset.availableCharsets();
	}
	
	/**
	 * This command shall print the charsets supported by this
	 * Java platform. It ignores any arguments which might have
	 * been passed to it and will warn the user if such incorrect
	 * usage attempt occurs, although it will still execute.
	 */
	@Override
	public ShellStatus executeCommand(BufferedReader in, BufferedWriter out,
			String[] arguments) {
		
		PrintWriter shellOutput = new PrintWriter(out);
		if(arguments.length != 0) {
			shellOutput.println("This command does not accept any arguments");
		}
		Set<String> chSetNames = chsets.keySet();
		for(String s : chSetNames) {
			shellOutput.println(s);
		}
		return ShellStatus.CONTINUE;
	}

}
