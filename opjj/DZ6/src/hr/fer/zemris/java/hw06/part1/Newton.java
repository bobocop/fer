package hr.fer.zemris.java.hw06.part1;

import hr.fer.zemris.java.tecaj_06.fractals.FractalViewer;
import hr.fer.zemris.java.tecaj_06.fractals.IFractalProducer;
import hr.fer.zemris.java.tecaj_06.fractals.IFractalResultObserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Newton {

	public static void main(String[] args) {
		System.out.println("Welcome to the Newton-Raphson iteration-based "
				+ "fractal viewer");
		System.out.println("Please enter at least two roots, one root per "
				+ "line. Enter 'done', when done.");
		
		int i = 1;
		List<Complex> rootList = new ArrayList<>();
		BufferedReader in = new BufferedReader(
				new InputStreamReader(System.in));
		while(true) {
			try {
				System.out.print("Root " + i + "> ");
				String input = in.readLine();
				if(input.equals("done")) {
					if(in != null) in.close();
					break;
				} else {
					Complex root = Complex.parseComplex(input);
					rootList.add(root);
					i++;
				}
			} catch(IOException e) {
				e.printStackTrace();
				System.out.println("I/O error");
				System.exit(-1);
			} catch(NumberFormatException e) {
				System.out.println("Unable to parse the input; recommended format "
						+ "is a + ib (a - ib) please try again");
			}
		}
		Complex[] roots = new Complex[rootList.size()];
		ComplexRootedPolynomial poly = new ComplexRootedPolynomial(
				rootList.toArray(roots));
		FractalViewer.show(new NRFracGenerator(poly));
	}
	
	public static class NRFracGenerator implements IFractalProducer {
		/*
		 * Produces the fractal images using parallelisation.
		 */
		ComplexRootedPolynomial rootPoly;
		
		public NRFracGenerator(ComplexRootedPolynomial rootPoly) {
			super();
			this.rootPoly = rootPoly;
		}

		@Override
		public void produce(double reMin, double reMax, double imMin, 
				double imMax, int width, int height, long requestNumber, 
				IFractalResultObserver gui) {
			short[] data = new short[width*height];
			int cpuCount = Runtime.getRuntime().availableProcessors();
			int numStripes = 8 * cpuCount;
			int yPerStripe = height / numStripes;
			ExecutorService pool = Executors.newFixedThreadPool(cpuCount);
			List<Future<Void>> results = new ArrayList<Future<Void>>();
			
			for (int i = 0; i < numStripes; i++) {
				int yMin = i * yPerStripe;
				int yMax = (i+1) * yPerStripe;
				if (i == numStripes - 1) {
					yMax = height;
				}
				NRFracRenderJob job = new NRFracRenderJob(
						reMin, reMax, imMin, imMax, width, height, 
						data, yMin, yMax, rootPoly);
				results.add(pool.submit(job));
			}
			
			for (Future<Void> res : results) {
				try {
					res.get();
				} catch (InterruptedException | ExecutionException ignorable) {}
			}
			
			pool.shutdown();
			
			gui.acceptResult(
					data, 
					(short) (rootPoly.toComplexPolynom().order() + 1), 
					requestNumber
					);
		}
	}
	
	public static class NRFracRenderJob implements Callable<Void> {
		double reMin;
		double reMax;
		double imMin;
		double imMax;
		int width;
		int height;
		short[] data;
		int yMin;
		int yMax;
		ComplexRootedPolynomial rootPoly;
		ComplexPolynomial coefPoly;
		final double CONV_THRS = 0.001;
		final int MAX_ITER = 256;
		final double ROOT_THRS = 0.002;

		public NRFracRenderJob(double reMin, double reMax, double imMin,
				double imMax, int width, int height, short[] data,
				int yMin, int yMax, ComplexRootedPolynomial rootPoly) {
			super();
			this.reMin = reMin;
			this.reMax = reMax;
			this.imMin = imMin;
			this.imMax = imMax;
			this.width = width;
			this.height = height;
			this.data = data;
			this.yMin = yMin;
			this.yMax = yMax;
			this.rootPoly = rootPoly;
			this.coefPoly = rootPoly.toComplexPolynom();
		}

		@Override
		public Void call() throws Exception {
			int offset = yMin * width;
			for(int y = yMin; y < yMax; y++) {
				for(int x = 0; x < width; x++) {
					Complex zn = mapToCmplxPlane(x, y);
					int iter = 0;
					double module;
					do {
						Complex brojnik = coefPoly.apply(zn);
						Complex nazivnik = coefPoly.derive().apply(zn);
						Complex razlomak = brojnik.divide(nazivnik);
						Complex zn1 = zn.sub(razlomak);
						module = zn1.sub(zn).module();
						zn = zn1;
						iter++;
					} while(module > CONV_THRS && iter < MAX_ITER);
					int index = rootPoly.indexOfClosestRootFor(zn, ROOT_THRS);
					if(index == -1) {
						data[offset++] = 0;
					} else {
						data[offset++] = (short) index;
					}
				}
			}
			return null;
		}
		
		/**
		 * Given the x and y coordinates of the display, it maps them to
		 * the complex plane, producing the Complex number as a result.
		 * @param x real coordinate
		 * @param y imaginary coordinate
		 * @return Complex number on a complex display plane
		 */
		private Complex mapToCmplxPlane(double x, double y) {
			return new Complex(
					(x / (width - 1.0) * (reMax - reMin) + reMin),
					((height - 1.0 - y) / (height  - 1.0) * (imMax - imMin) + imMin)
					);
		}
	}
}