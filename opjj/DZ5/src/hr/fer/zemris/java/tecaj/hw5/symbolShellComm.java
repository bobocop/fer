package hr.fer.zemris.java.tecaj.hw5;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.PrintWriter;

public class symbolShellComm implements ShellCommand {
	ShellSettings settings;
	
	public symbolShellComm(ShellSettings settings) {
		this.settings = settings;
	}
	
	/**
	 * Allows the user to change the symbols displayed
	 * by the shell.
	 */
	@Override
	public ShellStatus executeCommand(BufferedReader in, BufferedWriter out,
			String[] arguments) {
		PrintWriter shellOutput = new PrintWriter(out);
		if(arguments.length < 1 || arguments.length > 2) {
			shellOutput.println("Usage: symbol SYMNAME (optnl.)NEWSYMNAME");
			return ShellStatus.CONTINUE;
		}
		
		if(settings.symbolExists(arguments[0])){
			if(arguments.length == 2) {
				shellOutput.println("The symbol for " + arguments[0]
									+ " changed from '" 
									+ settings.getSymbol(arguments[0])
									+ "'to '" + arguments[1]);
				settings.setSymbol(arguments[0], arguments[1]);
			} else {
				shellOutput.println("The symbol for " + arguments[0]
									+ " is '" + settings.getSymbol(arguments[0]) 
									+ "'");
				}
			} else {
				shellOutput.println("The symbol \'" + arguments[0] + "\'"
									+ " is undefined");
		}
		return ShellStatus.CONTINUE;
	}

}
