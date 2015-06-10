package hr.fer.zemris.java.tecaj.hw5;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MyShell {
	
	public static void main(String[] args) throws IOException {
		Shell sh = new Shell();
		BufferedReader lineIn = new BufferedReader(
								new InputStreamReader(System.in)
								);
		BufferedWriter lineOut = new BufferedWriter(
								 new OutputStreamWriter(System.out)
								 );
		String inputArgs[];
		ShellCommand cmd;
		String currentCmd = "";
		String userInput;
		do {
			if(sh.getInputMode() == ShellInputMode.SINGLELINE) {
				sh.displayPrompt();
			} else {	// MULTILINE input
				sh.displayExpectLine();
			}
			userInput = lineIn.readLine();
			if(userInput.isEmpty()) continue;	// should write something...
			inputArgs = userInput.split("\\s+");
			if(inputArgs.length > 1 
				&& ((String) Array.get(inputArgs, inputArgs.length - 1))
							.equals(sh.getMoreLinSym())) {
				/*
				* MULTILINE input begins here
				* The second to last argument is the multiline char,
				* so copy everthing you've got except the multiline char into
				* the new string that will contain the full command.
				* Retain the spaces, so it can easily be split when needed.
				*/
				sh.setInputMode(ShellInputMode.MULTILINE);
				for(String s : inputArgs) {
					currentCmd += s + " ";
				}
				currentCmd = currentCmd.substring(0, currentCmd.length() - 2);
				continue;
			} else {	// execute the current command
				for(String s : inputArgs) {	// get the complete command
					currentCmd += s + " ";
				}
				inputArgs = currentCmd.trim().split("\\s+");	// all the arguments
				cmd = sh.commands.get(inputArgs[0]);
				if(cmd == null) {
					System.err.println("Unknown command '" + inputArgs[0] + "'");
				} else {
					sh.setStatus(cmd.executeCommand(lineIn, lineOut, 
							Arrays.copyOfRange(inputArgs, 1, inputArgs.length)));
				}
				sh.setInputMode(ShellInputMode.SINGLELINE);
				lineOut.flush();	// display the output
				currentCmd = "";
			}
		} while(sh.getStatus() != ShellStatus.TERMINATE);
		lineIn.close();
		lineOut.close();
	}
}

class Shell {
	private ShellSettings settings;
	public Map<String, ShellCommand> commands;
	private ShellStatus status = ShellStatus.CONTINUE;
	private ShellInputMode inputMode;
	
	public Shell() {
		this.settings = new ShellSettings();
		this.commands = new HashMap<String, ShellCommand>();
		this.status = ShellStatus.CONTINUE;
		this.inputMode = ShellInputMode.SINGLELINE;
		commands.put("symbol", new symbolShellComm(settings));
		commands.put("exit", new exitShellComm());
		commands.put("charsets", new charsetsShellComm());
		commands.put("cat", new catShellComm());
		commands.put("ls", new lsShellComm());
		commands.put("tree", new treeShellComm());
		commands.put("mkdir", new mkdirShellComm());
		commands.put("copy", new copyShellComm());
		commands.put("hexdump", new hexdumpShellComm());
		System.out.println("Welcome to MyShell v1.0");
	}
	
	/**
	 * Displays the console prompt.
	 */
	public void displayPrompt() {
		System.out.print(settings.getSymbol("PROMPT") + " ");
	}
	
	/**
	 * Displays the multiline symbol (insted of the prompt) when more 
	 * lines of the same command are expected.
	 */
	public void displayExpectLine() {
		System.out.print(settings.getSymbol("MULTILINE") + " ");
	}
	
	/**
	 * Returns the symbol used by the user to signal that there
	 * are more lines of the same command to be entered. 
	 * @return the symbol (default: '\')
	 */
	public String getMoreLinSym() {
		return settings.getSymbol("MORELINES");
	}
	
	/**
	 * Returns the shell status.
	 * @return ShellStatus enum
	 */
	public ShellStatus getStatus() {
		return status;
	}

	/**
	 * If set to TERMINATE, the shell will not accept 
	 * any more commands and will be terminated. If set
	 * to CONTINUE, it will resume operation.
	 * @param ShellStatus enum
	 */
	public void setStatus(ShellStatus status) {
		this.status = status;
	}

	/**
	 * Get the input state of the shell. Can be either SINGLELINE or MULTILINE.
	 * @return ShellInputMode enum
	 */
	public ShellInputMode getInputMode() {
		return inputMode;
	}

	/**
	 * Set the input state of the shell.
	 * @param inputMode
	 */
	public void setInputMode(ShellInputMode inputMode) {
		this.inputMode = inputMode;
	}
	
	/**
	 * This method is used by some Commands. It enables the
	 * user to include the space separated directories in the
	 * path by surrounding it with quotation marks.
	 * @param the path
	 * @return
	 */
	public static String parsePath(String path) {
		return path.replace("\"", " ").trim();
	}
}
