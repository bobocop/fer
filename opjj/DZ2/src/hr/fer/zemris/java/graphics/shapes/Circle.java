package hr.fer.zemris.java.graphics.shapes;

import hr.fer.zemris.java.graphics.raster.BWRaster;

public class Circle extends GeometricShape {
	private int centerX;
	private int centerY;
	private int radius;
	
	/**
	 * The circle constructor
	 * @param centerX the X-axis coordinate of the center
	 * @param centerY the Y-axis coordinate of the center
	 * @param radius of the circle
	 * @throws IllegalArgumentException when radius is less than 1
	 */
	public Circle(int centerX, int centerY, int radius)
		throws IllegalArgumentException {
			if(radius < 1) {
				throw new IllegalArgumentException();
			}
			this.centerX = centerX;
			this.centerY = centerY;
			this.radius = radius;
	}

	/**
	 * @return true or false depending on if the Rectangle contains the
	 * point specified by the method arguments
	 */
	@Override
	public boolean containsPoint(int x, int y) {
		return 	(x - centerX) * (x - centerX)
				+ (y - centerY) * (y - centerY)
				<= radius * radius; 
		}
	
	/**
	 * This method works almost exactly the same as the method it
	 * overrides, but it checks a smaller number of pixels and therefore 
	 * is considerably faster, especially with bigger rasters. 
	 */
	@Override
	public void draw(BWRaster r) {
		for(int y = centerY - radius; y < centerY + radius; y++) {
			for(int x = centerX - radius; x < centerX + radius; x++) {
				if(containsPoint(x, y)) {
					r.turnOn(x, y);
				}
			}
		}
	}
	
	/*Following are getters and setters for the Circle properties*/
	public int getCenterX() {
		return centerX;
	}
	
	public void setCenterX(int centerX) {
		this.centerX = centerX;
	}

	public int getCenterY() {
		return centerY;
	}

	public void setCenterY(int centerY) {
		this.centerY = centerY;
	}
	
	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

}
