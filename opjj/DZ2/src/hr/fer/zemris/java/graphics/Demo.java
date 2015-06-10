package hr.fer.zemris.java.graphics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import hr.fer.zemris.java.graphics.raster.*;
import hr.fer.zemris.java.graphics.shapes.*;
import hr.fer.zemris.java.graphics.views.*;

public class Demo {
	
	public static void main(String[] args) throws IOException {
		BWRaster raster = null;
		
		/*Checking whether the command line arguments are valid*/
		try {
			switch(args.length) {
				case 1:	
					raster = new BWRasterMem(
											Integer.parseInt(args[0]), 
											Integer.parseInt(args[0])
											);
					break;
				case 2:
					raster = new BWRasterMem(
											Integer.parseInt(args[0]),
											Integer.parseInt(args[1])
											);
					break;
				default:
					System.out.println("This program only accepts"
										+ " one or two arguments");
					System.exit(-1);
			}
		} catch(NumberFormatException e) {
			System.out.println("The arguments must be positive integers");
			System.exit(-1);
		} catch(IllegalArgumentException e) {
			System.out.println("Raster width and height must be greater than zero");
			System.exit(-1);
		}
		
		
		BufferedReader reader = new BufferedReader(
													new InputStreamReader(
													System.in
													));
		int linesRemain = 0;
		
		/*The number of commands is input here*/
		try {
		linesRemain = Integer.parseInt(reader.readLine());
		} catch(NumberFormatException e) {
			System.out.println("Please enter a single integer");
			System.exit(-1);
		}
		
		GeometricShape[] shapes = null;
		
		/*The user inputs the desired operations here*/
		if(linesRemain > 0) {
			shapes = new GeometricShape[linesRemain];
		} else {
			System.out.println("Unable to accept the provided number"
								+ " of additional lines, aborting");
			System.exit(-1);
		}
		
		for(int i = 0; i < linesRemain; i++) {
			String[] inputArgs = reader.readLine().split("\\s+");
			try {
				switch(inputArgs[0]) {
				case "FLIP":	
					shapes[i] = null;
					break;
				case "TRIANGLE":
					shapes[i] = getTriangle(inputArgs);
					break;
				case "RECTANGLE":
					shapes[i] = getRectangle(inputArgs);
					break;
				case "CIRCLE":
					shapes[i] = getCircle(inputArgs);
					break;
				case "ELLIPSE":	
					shapes[i] = getEllipse(inputArgs);
					break;
				case "SQUARE":
					shapes[i] = getSquare(inputArgs);
					break;
				default:
					throw new IllegalArgumentException();
				}
			} catch(IllegalArgumentException e) {
				System.out.println("Either an insufficient number or"
									+ "	an invalid type of arguments has been"
									+ " provided");
				System.exit(-1);
			}
		}
		
		
		boolean flipped = false;	// flipping mode is initially turned off
		for(int i = 0; i < linesRemain; i++) {
			if(shapes[i] == null) {
				/*Change the flipping mode when a null reference is encountered*/
				if(flipped) {
					raster.disableFlipMode();
					flipped = false;
				} else {
					raster.enableFlipMode();
					flipped = true;
				}
			} else {
				try {
				shapes[i].draw(raster);
				} catch(IllegalArgumentException e) {
					System.out.println("Unable to create an image - "
										+ "invalid shape dimensions "
										+ "have been provided");
					System.exit(-1);
				}
			}
		}
		
		RasterView view = new SimpleRasterView();
		view.produce(raster);
		reader.close();
	}
	
	/*
	 * These methods check the number of input arguments and pass them to
	 * shapes' constructors.
	 */
	public static Triangle getTriangle(String[] inputArgs) {
		if(inputArgs.length != 7)
			throw new IllegalArgumentException();
		return new Triangle(
							Integer.parseInt(inputArgs[1]),
							Integer.parseInt(inputArgs[2]),
							Integer.parseInt(inputArgs[3]),
							Integer.parseInt(inputArgs[4]),
							Integer.parseInt(inputArgs[5]),
							Integer.parseInt(inputArgs[6])
							);
	}
	
	public static Rectangle getRectangle(String[] inputArgs) {
		if(inputArgs.length != 5)
			throw new IllegalArgumentException();
		return new Rectangle(
							Integer.parseInt(inputArgs[1]),
							Integer.parseInt(inputArgs[2]),
							Integer.parseInt(inputArgs[3]),
							Integer.parseInt(inputArgs[4])
							);
	}
	
	public static Circle getCircle(String[] inputArgs) {
		if(inputArgs.length != 4)
			throw new IllegalArgumentException();
		return new Circle(
							Integer.parseInt(inputArgs[1]),
							Integer.parseInt(inputArgs[2]),
							Integer.parseInt(inputArgs[3])
							);
	}
	
	public static Ellipse getEllipse(String[] inputArgs) {
		if(inputArgs.length != 5)
			throw new IllegalArgumentException();
		return new Ellipse(
							Integer.parseInt(inputArgs[1]),
							Integer.parseInt(inputArgs[2]),
							Integer.parseInt(inputArgs[3]),
							Integer.parseInt(inputArgs[4])
							);
	}
	
	public static Square getSquare(String[] inputArgs) {
		if(inputArgs.length < 4)
			throw new IllegalArgumentException();
		return new Square(
							Integer.parseInt(inputArgs[1]),
							Integer.parseInt(inputArgs[2]),
							Integer.parseInt(inputArgs[3])
							);
	}
}
