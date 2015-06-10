package hr.fer.zemris.java.tecaj.hw5;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

public class lsShellComm implements ShellCommand {

	/**
	 * Displays some information about the directory.
	 */
	@Override
	public ShellStatus executeCommand(BufferedReader in, BufferedWriter out,
			String[] arguments) {
		PrintWriter shellOutput = new PrintWriter(out);
		if(arguments.length != 1) {
			shellOutput.println("Usage: ls DIRECTORY");
			return ShellStatus.CONTINUE;
		}
		Path dir = Paths.get(Shell.parsePath(arguments[0]));
		if(!Files.isDirectory(dir)) {
			shellOutput.println("'" + arguments[0] + "' is not a directory");
			return ShellStatus.CONTINUE;
		}
		try(DirectoryStream<Path> dirStream = Files.newDirectoryStream(dir)) {
			Iterator<Path> pathIter = dirStream.iterator();
			Path dirEntry;
			while(pathIter.hasNext()) {
				dirEntry = pathIter.next();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
				BasicFileAttributeView faView = Files.getFileAttributeView(
													dirEntry, 
													BasicFileAttributeView.class,
													LinkOption.NOFOLLOW_LINKS);
				BasicFileAttributes attributes = faView.readAttributes();
				// Time
				FileTime fileTime = attributes.creationTime();
				String formattedDateTime = sdf.format(new Date(fileTime.toMillis()));
				// Permissions
				String perms = "";
				perms += Files.isDirectory(dirEntry) ? "d" : "-";
				perms += Files.isReadable(dirEntry) ? "r" : "-";
				perms += Files.isWritable(dirEntry) ? "w" : "-";
				perms += Files.isExecutable(dirEntry) ? "x" : "-";
				shellOutput.println(perms + " "
							+ String.format("%10d", attributes.size()) + " "
							+ formattedDateTime + " "
							+ dirEntry.getFileName().toString()	// entrys name
							);
			}
		} catch (IOException e) {
			shellOutput.println("I/O error");
		}
		return ShellStatus.CONTINUE;
	}
}
