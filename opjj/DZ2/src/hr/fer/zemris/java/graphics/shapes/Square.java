package hr.fer.zemris.java.graphics.shapes;

import hr.fer.zemris.java.graphics.raster.BWRaster;

public class Square extends GeometricShape {
	private int topLeftX;
	private int topLeftY;
	private int size;
	
	/**
	 * The user should provide the top left coordinate and the area of a square
	 * @param topLeftX top left x coordinate
	 * @param topLeftY top left y coordinate
	 * @param size area of a square
	 * @throws IllegalArgumentException if the size is less than 1
	 */
	public Square(int topLeftX, int topLeftY, int size) 
		throws IllegalArgumentException{
			if(size < 1) {
				throw new IllegalArgumentException();
			}
			this.topLeftX = topLeftX;
			this.topLeftY = topLeftY;
			this.size = size;
	}
	
	/**
	 * @return true or false depending on if the Rectangle contains the
	 * point specified by the method arguments
	 */
	@Override
	public boolean containsPoint(int x, int y) {
		if(
				x 	>= 	topLeftX
			&& 	x 	< 	topLeftX + size
			&& 	y 	>= 	topLeftY
			&& 	y 	< 	topLeftY + size
			) return true;
		return false;
	}

	/**
	 * This method draws a square considerably faster that
	 * GeometricShape's draw. It is to be used only for drawing squares.
	 */
	@Override
	public void draw(BWRaster r) {
		for(int y = topLeftY; y < topLeftY + size; y++) {
			for(int x = topLeftX; x < topLeftX + size; x++) {
				r.turnOn(x, y);
			}
		}
	}
	
	/*Following are getters and setters for the Square properties*/
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

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
}
