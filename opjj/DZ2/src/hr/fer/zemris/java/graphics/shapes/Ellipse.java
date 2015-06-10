package hr.fer.zemris.java.graphics.shapes;

import hr.fer.zemris.java.graphics.raster.BWRaster;

public class Ellipse extends GeometricShape {
	private int centerX;
	private int centerY;
	private int horiRadius;
	private int vertRadius;
	
	/**
	 * Coordinates of the center and sizeof of the radii shoud be provided.
	 * @param centerX x-coordinate of the center
	 * @param centerY y-coordinate of the center
	 * @param horiRadius horizontal radius
	 * @param vertRadius vertical radius
	 * @throws IllegalArgumentException if either of the radii is less than 1
	 */
	public Ellipse(int centerX, int centerY, int horiRadius, int vertRadius)
		throws IllegalArgumentException {
			if(horiRadius < 1 || vertRadius < 1) {
				throw new IllegalArgumentException();
			}
			this.centerX = centerX;
			this.centerY = centerY;
			this.horiRadius = horiRadius;
			this.vertRadius = vertRadius;
	}
	
	/**
	 * @return true or false depending on if the Rectangle contains the
	 * point specified by the method arguments
	 */
	@Override
	public boolean containsPoint(int x, int y) {
		return	(horiRadius*horiRadius) * (vertRadius * vertRadius)
				>=
				(vertRadius * vertRadius)
				* ((x - centerX)*(x - centerX))
				+ (horiRadius * horiRadius)
				* ((y - centerY) * (y - centerY));
	}

	/**
	 * This method works almost exactly the same as the method it
	 * overrides, but it checks a smaller number of pixels and therefore 
	 * is considerably faster, especially with bigger rasters. 
	 */
	@Override
	public void draw(BWRaster r) {
		for(int y = centerY - vertRadius; y < centerY + vertRadius; y++) {
			for(int x = centerX - horiRadius; x < centerX + horiRadius; x++) {
				if(containsPoint(x, y)) {
					r.turnOn(x, y);
				}
			}
		}
	}
	
	/*Following are getters and setters for the Ellipse properties*/
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

	public int getHoriRadius() {
		return horiRadius;
	}

	public void setHoriRadius(int horiRadius) {
		this.horiRadius = horiRadius;
	}

	public int getVertRadius() {
		return vertRadius;
	}

	public void setVertRadius(int vertRadius) {
		this.vertRadius = vertRadius;
	}
}
