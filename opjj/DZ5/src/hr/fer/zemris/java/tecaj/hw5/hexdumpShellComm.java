package hr.fer.zemris.java.tecaj.hw5;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

public class hexdumpShellComm implements ShellCommand {

	/**
	 * Prints the hexadecimal output of the bytes in the file. It also
	 * attempts to interpret the bytes as characters and prints them to
	 * the right of the hex output. Bytes which cannot be converted to 
	 * chars are printed as '.'.
	 */
	@Override
	public ShellStatus executeCommand(BufferedReader in, BufferedWriter out,
			String[] arguments) {
		PrintWriter shellOutput = new PrintWriter(out);
		if(arguments.length < 1) {
			shellOutput.println("Usage: hexdump FILENAME");
			return ShellStatus.CONTINUE;
		}
		Path filePath = Paths.get(arguments[0]);
		try(BufferedInputStream fin = new BufferedInputStream(
				new FileInputStream(filePath.toFile()))) {
			byte[] bytes = new byte[16];	// there are 16 bytes displayed per output line
			int lineNum = 0;
			int bytesRead;
			while((bytesRead = fin.read(bytes)) != -1) {
				StringBuilder lineOut = new StringBuilder();
				lineOut.append(String.format("%010X: ", lineNum * 16));
				for(int i = 0; i < bytes.length; i++) {
					if(i == 8) {
						// separate after 8 bytes
						lineOut.deleteCharAt(lineOut.length() - 1);
						lineOut.append("|");
					}
					if(i > bytesRead - 1) {
						// reached the end of file
						bytes[i] = (byte) ' ';	// for later use
						lineOut.append("   ");
					}
					else {
						lineOut.append(String.format("%02X ", bytes[i]));
					}
				}
				lineOut.append("| ");
				for(int i = 0; i < bytes.length; i++) {
					int byteVal = (Byte.valueOf(bytes[i])).intValue();
					if(byteVal < 32 || byteVal > 172) {
						lineOut.append(".");
					} else {
						lineOut.append(String.format("%c", bytes[i]));
					}
				}
				shellOutput.println(lineOut.toString());
				lineNum++;
			}
		} catch (FileNotFoundException e) {
			shellOutput.println("File not found");
		} catch (IOException e) {
			shellOutput.println("I/O error");
		}
		return ShellStatus.CONTINUE;
	}

}
