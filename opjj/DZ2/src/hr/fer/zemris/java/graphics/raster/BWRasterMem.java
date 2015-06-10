package hr.fer.zemris.java.graphics.raster;

public class BWRasterMem implements BWRaster {
	private int width;
	private int height;
	private boolean[][] pixels;
	private boolean flipMode = false;
	
	/**
	 * Lets the user specify raster's size and initially turns all
	 * of it's pixels off.
	 * @param width of the raster
	 * @param height of the raster
	 * @throws IllegalArgumentException when height and width are less than 1
	 */
	public BWRasterMem(int width, int height)
		throws IllegalArgumentException {
			if(width < 1 || height < 1) {
				throw new IllegalArgumentException();
			}
			this.width = width;
			this.height = height;
			this.pixels = new boolean[height][width];
			clear();	// the pixels are initially turned off
	}
	
	/*
	 * The following are implementations of methods thoroughly
	 * explained in BWRaster interface. They are all pretty straightforward
	 * because the BWRasterMem uses a 2D array of booleans representing the
	 * state of it's pixels.
	 */
	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}
	
	@Override
	public void clear() {
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				pixels[y][x] = false;
			}
		}
	}
	/*
	 * Additional info regarding any method that calls the pixelLocationCheck method:
	 * If pLC returns false, meaning that the provided pixel is not
	 * a part of the raster, the calling method (that requires an existing pixel
	 * for its operation) will end without warning, so as to not illegaly access
	 * the pixels arrray or throw an exception to abort the program.
	 * This has been done to enable drawing of shapes which can be displayed only
	 * partially on the raster, that is, shapes which contain points whose
	 * coordinates are negative.
	 *
	 */
	@Override
	public void turnOn(int x, int y) {
		if(pixelLocationCheck(x, height - y - 1)) {
			if(flipMode) {
				pixels[height-y-1][x] = !pixels[height-y-1][x];
			} else {
				pixels[height-y-1][x] = true;
			}
		}
	}
	
	@Override
	public void turnOff(int x, int y) {
		if(pixelLocationCheck(x, y)) {
			pixels[y][x] = false;
		}
	}

	@Override
	public void enableFlipMode() {
		flipMode = true;
	}

	@Override
	public void disableFlipMode() {
		flipMode = false;
	}

	@Override
	public boolean isTurnedOn(int x, int y) {
		if(pixelLocationCheck(x, y)) {
			return pixels[y][x];
		}
		return false;
	}

	/*
	 * This method checks whether the entered pixel location
	 * is within bounds of the raster. It prevents accessing the pixels
	 * array through invalid indexes.
	 */
	private boolean pixelLocationCheck(int x, int y) {
			if(x > width - 1 || y > height - 1 || x < 0 || y < 0) { 
				return false;
			}
			return true;
	}

}
