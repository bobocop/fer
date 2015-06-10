package hr.fer.zemris.java.webserver.workers;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Creates and sends a PNG image to the {@link RequestContext}'s 
 * output stream. The image contains a circle inside a square and its 
 * dimensions are 200*200.
 */
public class CircleWorker implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) {
		context.setMimeType("image/png");
		BufferedImage bim = new BufferedImage(200, 200,
				BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g2d = bim.createGraphics();
		g2d.setBackground(Color.blue);
		g2d.setColor(Color.red);
		g2d.fillOval(0, 0, 200, 200);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ImageIO.write(bim, "png", bos);
			context.write(bos.toByteArray());
		} catch (IOException e) {
			System.err.println("CircleWorker: could not output the image");
		}
		g2d.dispose();
	}

}
