package hr.fer.zemris.java.hw06.part2;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import hr.fer.zemris.java.tecaj_06.rays.GraphicalObject;
import hr.fer.zemris.java.tecaj_06.rays.IRayTracerProducer;
import hr.fer.zemris.java.tecaj_06.rays.LightSource;
import hr.fer.zemris.java.tecaj_06.rays.Point3D;
import hr.fer.zemris.java.tecaj_06.rays.Ray;
import hr.fer.zemris.java.tecaj_06.rays.RayIntersection;
import hr.fer.zemris.java.tecaj_06.rays.RayTracerViewer;
import hr.fer.zemris.java.tecaj_06.rays.IRayTracerResultObserver;
import hr.fer.zemris.java.tecaj_06.rays.Scene;

public class RayCasterParallel {
	
	public static void main(String[] args) {
		RayTracerViewer.show(
				new ParallelProducer(),
				new Point3D(10, 0, 0),
				new Point3D(0, 0, 0),
				new Point3D(0, 0, 10),
				20, 20);
	}
	
	public static class ParallelProducer implements IRayTracerProducer {	
			/*
			 * This class uses the same algorithms as the RayCaster, although
			 * the work done is parallelized by splitting the job of pixel
			 * coloring into smaller parts. Each part is a segment of the display
			 * consisting of arbitrarily chosen number of horizontal screen lines.
			 * The number of lines (threshold) is one of TracerJobs attributes.
			 */
			
			public static Point3D eye;
			public static Point3D view;
			public static Point3D viewUp;
			public static double horizontal;
			public static double vertical;
			public static int height;
			public static int width;
			public static Point3D screenCorner;
			public static Scene scene;
			public static Point3D xAxis;
			public static Point3D yAxis;
			
			/*
			 * 
			 * @see hr.fer.zemris.java.tecaj_06.rays.IRayTracerProducer#produce(
			 * hr.fer.zemris.java.tecaj_06.rays.Point3D, 
			 * hr.fer.zemris.java.tecaj_06.rays.Point3D, 
			 * hr.fer.zemris.java.tecaj_06.rays.Point3D, 
			 * double, double, int, int, long, 
			 * hr.fer.zemris.java.tecaj_06.rays.IRayTracerResultObserver)
			 */
			@Override
			public void produce(Point3D eye, Point3D view, Point3D viewUp,
					double horizontal, double vertical, int width, int height,
					long requestNo, IRayTracerResultObserver observer) {
				
				ParallelProducer.eye = eye;
				ParallelProducer.view = view;
				ParallelProducer.height = height;
				ParallelProducer.width = width;
				ParallelProducer.horizontal = horizontal;
				ParallelProducer.vertical = vertical;
				
				System.out.println("Zapocinjem izracune...");
				short[] red = new short[width*height];
				short[] green = new short[width*height];
				short[] blue = new short[width*height];
				
				yAxis = getAxis(eye, view, viewUp, "y");
				xAxis = getAxis(eye, view, viewUp, "x");
				
				screenCorner = view
						.sub(xAxis.scalarMultiply(horizontal / 2.0))
						.add(yAxis.scalarMultiply(vertical / 2.0));
				scene = RayTracerViewer.createPredefinedScene();
				
				short[] rgb = new short[3];
				int offset = 0;
				
				// default parallelism level will be used (= # available cpus)
				ForkJoinPool pool = new ForkJoinPool();
				
				pool.invoke(new TracingJob(offset, red, green, 
						blue, rgb, height, 0));
				
				pool.shutdown();
				
				System.out.println("Izracuni gotovi...");
				observer.acceptResult(red, green, blue, requestNo);
				System.out.println("Dojava gotova...");
				}
			
			private Point3D getAxis(Point3D eye, Point3D view, 
					Point3D viewUp, String axis) {
				Point3D OG = view.sub(eye).normalize();
				Point3D VUV = viewUp.normalize();
				Point3D j = VUV.sub(
						OG.scalarMultiply(OG.scalarProduct(VUV)))
						.normalize();
				if(axis.equals("y")) {
					return j;
				} else {
					// return i
					return OG.vectorProduct(j).normalize();
				}
			}
			
			public static void tracer(Ray ray, short[] rgb) {
				for(GraphicalObject obj : scene.getObjects()) {
					RayIntersection ri = obj.findClosestRayIntersection(ray);
					if(ri != null && notObscured(ray, ri)) {
						determineColorFor(ri, rgb);
					}
				}
			}
			
			private static void determineColorFor(RayIntersection ri, short[] rgb) {
				rgb[0] = rgb[1] = rgb[2] = 15;	// ambient lightning
				for(LightSource ls : scene.getLights()) {
					Ray lsRay = Ray.fromPoints(ls.getPoint(), ri.getPoint());
					RayIntersection closest = findClosestIntersec(lsRay, ls);
					if(
						closest == null || 
						((closest.getPoint().sub(ls.getPoint()).norm() + 0.001)
						> (ri.getPoint().sub(ls.getPoint())).norm())
						) {
						// if the light source ray intersection is not obscured
						Point3D l = ls.getPoint().sub(ri.getPoint()).normalize();
						Point3D n = ri.getNormal();
						Point3D v = eye.sub(ri.getPoint()).normalize();
						Point3D r = n.scalarMultiply((n.scalarProduct(l)*2))
									.sub(l).normalize();
						
						
						rgb[0] += ls.getR() * (
								((l.scalarProduct(n) > 0) ?
								ri.getKdr() * (l.scalarProduct(n)) :
								0)
								+ ((v.scalarProduct(r) > 0) ? 
								ri.getKrr() * Math.pow(v.scalarProduct(r), ri.getKrn()) :
								0)
								);
						rgb[1] += ls.getG() * (
								((l.scalarProduct(n) > 0) ?
								ri.getKdg() * (l.scalarProduct(n)) :
								0)
								+ ((v.scalarProduct(r) > 0) ?
								ri.getKrg() * Math.pow(v.scalarProduct(r), ri.getKrn()) :
								0)
								);
						rgb[2] += ls.getB() * (
								((l.scalarProduct(n) > 0) ?
								ri.getKdb() * (l.scalarProduct(n)) :
								0)
								+ ((v.scalarProduct(r) > 0) ?
								ri.getKrb() * Math.pow(v.scalarProduct(r), ri.getKrn()) :
								0)
								);
					}
				}
			}
			
			private static RayIntersection findClosestIntersec(
					Ray lsRay, LightSource ls) {
				RayIntersection closest = null;
				for(GraphicalObject obj : scene.getObjects()) {
					if(closest == null) {
						closest = obj.findClosestRayIntersection(lsRay);
					} else {
						RayIntersection ri = obj.findClosestRayIntersection(lsRay);
						if((ri != null) 
							&& (ri.getDistance() < closest.getDistance())) {
							closest = ri;
						}
					}
				}
				return closest;
			}
			
			private static boolean notObscured(Ray ray, RayIntersection ri) {
				for(GraphicalObject obj : scene.getObjects()) {
					RayIntersection anotherRi = obj.findClosestRayIntersection(ray);
					if(anotherRi != null 
						&& anotherRi.getDistance() < ri.getDistance()) {
						return false;
					}
				}
				return true;
			}
	
	private static class TracingJob extends RecursiveAction {
		private static final long serialVersionUID = 1L;
		private int offset;
		private short[] red;
		private short[] green;
		private short[] blue;
		private short[] rgb;
		public final static int JOB_THRESHOLD = 128;	// screen lines per job
		private int yMax;
		private int yMin;
		
		public TracingJob(int offset, short[] red, short[] green, short[] blue,
				short[] rgb,  int yMax, int yMin) {
			super();
			this.offset = offset;
			this.red = red;
			this.green = green;
			this.blue = blue;
			this.rgb = rgb;
			this.yMax = yMax;
			this.yMin = yMin;
		}

		@Override
		protected void compute() {
			if(yMax - yMin + 1 < JOB_THRESHOLD) {
				computeDirectly();
				return;
			}
			invokeAll(new TracingJob(offset, red, green, blue, 
					rgb, (yMax - yMin) / 2 + yMin, yMin));
			
			invokeAll(new TracingJob(offset, red, green, blue, 
					rgb, yMax, yMin + (yMax - yMin) / 2));
		}
		
		/*
		 * If the number of lines is less than threshold value, compute.
		 */
		public void computeDirectly() {
			offset = yMin * width;
			for(int y = yMin; y < yMax; y++) {
				for(int x = 0; x < width; x++) {
					Arrays.fill(rgb, (short) 0);
					Point3D screenPoint = screenCorner
							.add(
									xAxis.scalarMultiply(
											(x * horizontal) / (width - 1.0)
											))
							.sub(
									yAxis.scalarMultiply(
											(y * vertical) / (height - 1.0)
											));
					Ray ray = Ray.fromPoints(eye, screenPoint);
					
					tracer(ray, rgb);
					
					red[offset] = rgb[0] > 255 ? 255 : rgb[0];
					green[offset] = rgb[1] > 255 ? 255 : rgb[1];
					blue[offset] = rgb[2] > 255 ? 255 : rgb[2];
					
					offset++;
					}
				}
			}
		}
	}
}



