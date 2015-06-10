package hr.fer.zemris.java.hw06.part2;

import java.util.Arrays;

import hr.fer.zemris.java.tecaj_06.rays.GraphicalObject;
import hr.fer.zemris.java.tecaj_06.rays.IRayTracerProducer;
import hr.fer.zemris.java.tecaj_06.rays.LightSource;
import hr.fer.zemris.java.tecaj_06.rays.Point3D;
import hr.fer.zemris.java.tecaj_06.rays.Ray;
import hr.fer.zemris.java.tecaj_06.rays.RayIntersection;
import hr.fer.zemris.java.tecaj_06.rays.RayTracerViewer;
import hr.fer.zemris.java.tecaj_06.rays.IRayTracerResultObserver;
import hr.fer.zemris.java.tecaj_06.rays.Scene;

public class RayCaster {

	public static void main(String[] args) {
		RayTracerViewer.show(
				getIRayTracerProducer(),
				new Point3D(10, 0, 0),
				new Point3D(0, 0, 0),
				new Point3D(0, 0, 10),
				20, 20);
	}
	
	private static IRayTracerProducer getIRayTracerProducer() {
		return new IRayTracerProducer() {
			
			@Override
			public void produce(Point3D eye, Point3D view, Point3D viewUp,
					double horizontal, double vertical, int width, int height,
					long requestNo, IRayTracerResultObserver observer) {
				
				System.out.println("Zapocinjem izracune...");
				short[] red = new short[width*height];
				short[] green = new short[width*height];
				short[] blue = new short[width*height];
				
				
				Point3D yAxis = getAxis(eye, view, viewUp, true);
				Point3D xAxis = getAxis(eye, view, viewUp, false);
				/*
				 * Iako je spomenuta u PDF-u, ne vidim korist
				 * od ove varijable.
				 * Point3D zAxis = yAxis.vectorProduct(xAxis);
				 */
				
				Point3D screenCorner = view
						.sub(xAxis.scalarMultiply(horizontal / 2.0))
						.add(yAxis.scalarMultiply(vertical / 2.0));
				
				Scene scene = RayTracerViewer.createPredefinedScene();
				
				short[] rgb = new short[3];
				int offset = 0;
				long startTime = System.currentTimeMillis();
				for(int y = 0; y < height; y++) {
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
						
						tracer(scene, ray, rgb, eye);	// put the colors into 'rgb'
						
						red[offset] = rgb[0] > 255 ? 255 : rgb[0];
						green[offset] = rgb[1] > 255 ? 255 : rgb[1];
						blue[offset] = rgb[2] > 255 ? 255 : rgb[2];
						
						offset++;
						}
				}
				long endTime = System.currentTimeMillis();
				System.out.println("Took " + (endTime - startTime) + " milliseconds.");
				System.out.println("Izracuni gotovi...");
				observer.acceptResult(red, green, blue, requestNo);
				System.out.println("Dojava gotova...");
			}
			
			/**
			 * Checks if there exists an intersection with an object and a
			 * ray which was provided and if it does, if it is obscured by
			 * another object (that is, if there exist a closer intersection
			 * by the same ray). If the former is false or the latter true,
			 * no color calculations for the pixel need be done.
			 * @param scene
			 * @param Ray ray which intersections are checked
			 * @param short rgb used to save the RGB values of a pixel
			 * @param eye
			 */
			private void tracer(Scene scene, Ray ray, short[] rgb, Point3D eye) {
				for(GraphicalObject obj : scene.getObjects()) {
					RayIntersection ri = obj.findClosestRayIntersection(ray);
					if(ri != null && notObscured(ray, scene, ri)) {
						determineColorFor(ri, scene, rgb, eye);
					}
				}
			}
			
			/**
			 * Determine the color of an intersection.
			 * @param RayIntersection ri that should be colored
			 * @param short rgb red, green and blue values of a pixel
			 * @param scene
			 */
			private void determineColorFor(RayIntersection ri, Scene scene, 
					short[] rgb, Point3D eye) {
				rgb[0] = rgb[1] = rgb[2] = 15;	// ambient lightning
				for(LightSource ls : scene.getLights()) {
					Ray lsRay = Ray.fromPoints(ls.getPoint(), ri.getPoint());
					RayIntersection closest = findClosestIntersec(lsRay, scene, ls);
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
			
			/**
			 * Finds the closest intersection of a ray from the 
			 * light source and an object in the scene.
			 * @param ray which may intersect the objects
			 * @param scene containing the objects
			 * @return RayIntersection denoting the closest intersection
			 */
			private RayIntersection findClosestIntersec(Ray lsRay, Scene scene,
					LightSource ls) {
				RayIntersection closest = null;
				for(GraphicalObject obj : scene.getObjects()) {
					if(closest == null) {
						closest = obj.findClosestRayIntersection(lsRay);
					} else {
						RayIntersection ri = obj.findClosestRayIntersection(lsRay);
						if((ri != null) 
							&& (closest.getDistance() + 0.001 > ri.getDistance())) {
							closest = ri;
						}
					}
				}
				return closest;
			}
			
			/**
			 * Calculates the x and y axes (or the normalized i and j
			 * vectors) which are required for image rendering.
			 * @param eye 
			 * @param view
			 * @param viewUp
			 * @param yOrX true if the yAxis should be returned,
			 * false will return xAxis
			 * @return depends on the yOrX parameter
			 */
			private Point3D getAxis(Point3D eye, Point3D view, 
					Point3D viewUp, boolean yOrX) {
				Point3D OG = view.sub(eye).normalize();
				Point3D VUV = viewUp.normalize();
				Point3D j = VUV.sub(
						OG.scalarMultiply(OG.scalarProduct(VUV)))
						.normalize();
				if(yOrX == true) {
					return j;
				} else {
					// return i
					return OG.vectorProduct(j).normalize();
				}
			}
			
			/**
			 * Checks if an intersection is completely obscured by another object.
			 * @param Ray ray (that intersected)
			 * @param Scene scene 
			 * @param RayIntersection ri the intersection to check
			 * @return true if it is not obscured and should be rendered
			 */
			private boolean notObscured(Ray ray, Scene scene, RayIntersection ri) {
				for(GraphicalObject obj : scene.getObjects()) {
					RayIntersection anotherRi = obj.findClosestRayIntersection(ray);
					if(anotherRi != null 
						&& anotherRi.getDistance() < ri.getDistance()) {
						return false;
					}
				}
				return true;
			}
		};
	}

}
