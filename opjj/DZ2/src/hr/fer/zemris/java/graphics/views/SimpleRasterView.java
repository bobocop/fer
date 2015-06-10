package hr.fer.zemris.java.graphics.views;

import hr.fer.zemris.java.graphics.raster.BWRaster;

public class SimpleRasterView implements RasterView {
	private char pixelOn;
	private char pixelOff;
	
	/**
	 * This contstructor allows the user to specify how he would like
	 * the pixels displayed during the raster image rendering.
	 * @param pixelOn character that will represent a turned on pixel
	 * @param pixelOff character that will represent a turned off pixel
	 */
	public SimpleRasterView(char pixelOn, char pixelOff) {
		this.pixelOn = pixelOn;
		this.pixelOff = pixelOff;
	}
	
	/**
	 * By default, '*' represents turned on pixels, while '.' represents the
	 * pixels that are turned off.
	 */
	public SimpleRasterView() {
		this('*', '.');
	}
	
	/*
	 * The contained image is printed in console, using the pixel
	 * representation specified in constructor arguments.
	 * @see hr.fer.zemris.java.graphics.views.RasterView
	 */
	@Override
	public void produce(BWRaster raster) {
		int rasWidth = raster.getWidth();
		int rasHeight = raster.getHeight();
		for(int y = rasHeight - 1; y >= 0; y--) {
			for(int x = 0; x < rasWidth; x++) {
				if(raster.isTurnedOn(x, y)) {
					System.out.printf("%c", pixelOn);
				} else {
					System.out.printf("%c", pixelOff);
				}
			}
			System.out.printf("\n");
		}
	}

}
