package hr.fer.zemris.java.tecaj.hw5;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class copyShellComm implements ShellCommand {
	
	/**
	 * Copies the file passed as the first argument into the
	 * secod argument(if it is a directory) or copies it as if
	 * the second argument is a file name.
	 */
	@Override
	public ShellStatus executeCommand(BufferedReader in, BufferedWriter out,
			String[] arguments) {
		PrintWriter shellOutput = new PrintWriter(out);
		if(arguments.length != 2) {
			shellOutput.println("Usage: copy SOURCE DESTINATION");
			return ShellStatus.CONTINUE;
		}
		/* Remove the quotation marks from the path string, if there are any */
		try {
			Path src = Paths.get(Shell.parsePath(arguments[0]));
			Path dest = Paths.get(Shell.parsePath(arguments[1]));
			if(!Files.exists(src) || Files.isDirectory(src)) {
				shellOutput.println("Source path must be an existing file");
				return ShellStatus.CONTINUE;
			}
			try {
				/*
				 * Checking if the file exist and should be overwritten is
				 * done by catching the FileAlreadyExists exception thrown in
				 * this block and calling the doOverWrite method to 
				 * handle the problem.
				 */
				if(Files.isDirectory(dest)) {
					Files.copy(src, dest.resolve(src.getFileName()), 
							StandardCopyOption.COPY_ATTRIBUTES);
				} else {
					Files.copy(src, dest);
				}
			} catch (FileAlreadyExistsException e) {
				shellOutput.println("The specified path already exists. Overwrite? y/n");
				shellOutput.flush();
				if(in.readLine().equals("y")) {
					if(Files.isDirectory(dest)) {
						Files.copy(src, dest.resolve(src.getFileName()), 
								StandardCopyOption.REPLACE_EXISTING);
					} else {
						Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
					}
				}	// if anything except for "y" is input, do nothing
			}
		} catch (IOException e) {
			shellOutput.println("I/O error: could not find the directory");
		}
		return ShellStatus.CONTINUE;
	}
}
