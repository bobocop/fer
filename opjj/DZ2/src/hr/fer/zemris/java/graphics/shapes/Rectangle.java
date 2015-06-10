package hr.fer.zemris.java.graphics.shapes;

import hr.fer.zemris.java.graphics.raster.BWRaster;

public class Rectangle extends GeometricShape {
	private int topLeftX;
	private int topLeftY;
	private int width;
	private int height;
	
	/**
	 * The user should provide the top left coordinate as well as
	 * the width and height of the rectangle.
	 * @param topLeftX top left x coordinate
	 * @param topLeftY top left y coordinate
	 * @param width width of the rectangle
	 * @param height height of the rectangle
	 * @throws IllegalArgumentException if either side is less than 1
	 */
	public Rectangle(int x, int y, int width, int height)
		throws IllegalArgumentException {
			if(width < 1 || height < 1) {
				throw new IllegalArgumentException();
			}
			this.topLeftX = x;
			this.topLeftY = y;
			this.width = width;
			this.height = height;
	}
	
	/**
	 * @return true or false depending on if the Rectangle contains the
	 * point specified by the method arguments
	 */
	@Override
	public boolean containsPoint(int x, int y) {
		if(
				x 	>= 	topLeftX
			&& 	x 	< 	topLeftX + width
			&& 	y 	>= 	topLeftY
			&& 	y 	< 	topLeftY + height
			) return true;
		return false;
	}

	/**
	 * Specialized method for drawing rectangles. It's faster than
	 * GeometricShape's draw.
	 */
	@Override
	public void draw(BWRaster r) {
		for(int y = topLeftY; y < topLeftY + height; y++) {
			for(int x = topLeftX; x < topLeftX + width; x++) {
				r.turnOn(x, y);
			}
		}
	}
	
	/*Following are the getters and setters for the Rectangle properties*/
	public int getTopLeftX() {
		return topLeftX;
	}

	public void setTopLeftX(int topLeftX) {
		this.topLeftX = topLeftX;
	}

	public int getTopLeftY() {
		return topLeftY;
	}

	public void setTopLeftY(int topLeftY) {
		this.topLeftY = topLeftY;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
}
