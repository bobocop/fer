package hr.fer.zemris.java.graphics.raster;

public interface BWRaster {
	
	/**
	 * @return the raster's width
	 */
	int getWidth();
	
	/**
	 * @return the raster's height
	 */
	int getHeight();
	
	/**
	 * Turns off all the pixels.
	 */
	void clear();
	
	/**
	 * If the flipping mode is disabled, this method should turn on the pixel
	 * at the location specified by the arguments. Otherwise, it should flip
	 * the pixel's state.
	 * @param x location of the pixel on the x-axis
	 * @param y location of the pixel on the y-axis
	 */
	void turnOn(int x, int y) throws IllegalArgumentException;
	
	/**
	 * Turns off the pixel at the specified location.
	 * @param x location of the pixel on the x-axis
	 * @param y location of the pixel on the y-axis
	 * @throws When the specified pixel does not exist.
	 */
	void turnOff(int x, int y) throws IllegalArgumentException;
	
	/**
	 * Enables the flipping mode (using the turnOn method to flip
	 * the pixel's state
	 * @throws When the specified pixel does not exist.
	 */
	void enableFlipMode();
	
	/**
	 * Disables the flipping mode.
	 */
	void disableFlipMode();
	
	/**
	 * @param x location of the pixel on the x-axis
	 * @param y location of the pixel on the y-axis
	 * @return True if the pixel is turned on, false otherwise
	 * @throws When the specified pixel does not exist.
	 */
	boolean isTurnedOn(int x, int y) throws IllegalArgumentException;

}
