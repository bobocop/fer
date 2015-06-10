package hr.fer.zemris.java.graphics.shapes;

import hr.fer.zemris.java.graphics.raster.*;

public abstract class GeometricShape {
	
	/**
	 * This simple and slow method for drawing the shape on
	 * the raster checks if raster's each point is contained in the
	 * shape which it draws. It should be overridden with shape-specific
	 * drawing methods when possible.
	 * @param r raster on which the shape is to be drawn
	 */
	public void draw(BWRaster r) {
		int maxX, maxY;
		maxX = r.getWidth();
		maxY = r.getHeight();
		for(int y = 0; y < maxY; y++) {
			for(int x = 0; x < maxX; x++) {
				if(this.containsPoint(x, y)) {
					r.turnOn(x, y);
				}
			}
		}
	}
	
	/**
	 * This method should check if the Shape contains the specified
	 * point.
	 * @param x coordinate of the point on the x-axis
	 * @param y coordinate of the point on the y-axis
	 * @return boolean true when the shape contains (x, y) false otherwise
	 */
	public abstract boolean containsPoint(int x, int y);
}
