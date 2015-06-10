package hr.fer.zemris.java.graphics.views;

import hr.fer.zemris.java.graphics.raster.BWRaster;

public interface RasterView {
	
	/**
	 * This method should produce an image of the current contents
	 * of the provided BWRaster.
	 * @param raster which contains the image
	 */
	void produce(BWRaster raster);
}
