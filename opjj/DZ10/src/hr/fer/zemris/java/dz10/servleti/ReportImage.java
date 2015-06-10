package hr.fer.zemris.java.dz10.servleti;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

/**
 * Generates an image (a pie chart) which displays some statistic.
 */
public class ReportImage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("image/png");

		// chart drawing
		DefaultPieDataset ds = new DefaultPieDataset();
		ds.setValue("Linux", 7);
		ds.setValue("Mac", 15);
		ds.setValue("Windows", 78);
		JFreeChart chart = ChartFactory.createPieChart3D("OS usage", ds, true,
				true, false);
		
		ImageIO.write(chart.createBufferedImage(512, 384), "png", 
				resp.getOutputStream());
	}
}
