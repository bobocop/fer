package hr.fer.zemris.java.tecaj.hw5;

import java.util.HashMap;
import java.util.Map;

public class ShellSettings {
	/*
	 * This class contains some settings (at the moment only
	 * the symbols used as prompts) and is one of the properties of
	 * the Shell instance. The user (MyShell) never directly sends messages 
	 * to the instances of this class, rather it is handled through the
	 * proxy methods of the Shell instance.
	 */
	private Map<String, String> shellSymbols;
	
	public ShellSettings() {
		this.shellSymbols = new HashMap<String, String>();
		/*Set the symbols to defaults*/
		this.shellSymbols.put("PROMPT", ">");
		this.shellSymbols.put("MORELINES", "\\");
		this.shellSymbols.put("MULTILINE", "|");
	}
	
	/**
	 * @param one of the symbols which have been "put" in the constructor
	 * @return the symbol requested or null if such does not exist
	 */
	public String getSymbol(String symName) {
		return shellSymbols.get(symName);
	}
	
	/**
	 * Set the symbol by the name of symName to symValue.
	 * @param symName
	 * @param symValue
	 */
	public void setSymbol(String symName, String symValue) {
		if(shellSymbols.get(symName) == null) {
			System.out.println("No symbol by the name of " + symName
								+ " is defined in this shell");
		} else {
			shellSymbols.put(symName, symValue);
		}
	}
	
	/**
	 * Returns true if symName symbol is defined for this shell. 
	 */
	public boolean symbolExists(String symName) {
		return shellSymbols.containsKey(symName);
	}
}
