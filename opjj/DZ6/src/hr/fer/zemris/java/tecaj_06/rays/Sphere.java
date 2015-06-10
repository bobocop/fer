package hr.fer.zemris.java.tecaj_06.rays;

public class Sphere extends GraphicalObject {
	private Point3D center;
	double radius;
	double kdr;
	double kdg;
	double kdb;
	double krr;
	double krg;
	double krb;
	double krn;
	
	public Sphere(Point3D center, double radius, double kdr, double kdg,
			double kdb, double krr, double krg, double krb, double krn) {
		super();
		this.center = center;
		this.radius = radius;
		this.kdr = kdr;
		this.kdg = kdg;
		this.kdb = kdb;
		this.krr = krr;
		this.krg = krg;
		this.krb = krb;
		this.krn = krn;
	}

	@Override
	public RayIntersection findClosestRayIntersection(Ray ray) {
		Point3D a = center.sub(ray.start);
		double discriminant = (ray.direction.scalarProduct(a) 
				* ray.direction.scalarProduct(a)) 
				- a.scalarProduct(a) 
				+ radius * radius;
		if(discriminant < 0) {
			return null;
		} else {
			double lambda1 = ray.direction.scalarProduct(a) 
					+ Math.sqrt(discriminant);
			double lambda2 = ray.direction.scalarProduct(a)
					- Math.sqrt(discriminant);
			
			double lambdaCloser;
			if(lambda1 > 0 && lambda2 < 0) {
				lambdaCloser = lambda1;
			} else if(lambda1 < 0 && lambda2 > 0) {
				lambdaCloser = lambda2;
			} else {
				if(lambda1 > lambda2) {
					lambdaCloser = lambda2;
				} else {
					lambdaCloser = lambda1;
				}
			}
			Point3D intersec = ray.start
					.add(ray.direction.scalarMultiply(lambdaCloser));
			double dist = ray.start.sub(intersec).norm();
			boolean isOuter = ray.start.sub(intersec).norm() > radius ? true : false;
			return new SphereIntersection(intersec, dist, isOuter, this);
		}
		
	}
	/** 
	 * @return Point3D Center of the sphere.
	 */
	public Point3D getCenter() {
		return center;
	}

	/**
	 * @return Double Sphere radius
	 */
	public double getRadius() {
		return radius;
	}

	/*
	 * These properties are used when the objects illumination
	 * is calculated.
	 */
	public double getKdr() {
		return kdr;
	}

	public double getKdg() {
		return kdg;
	}

	public double getKdb() {
		return kdb;
	}

	public double getKrr() {
		return krr;
	}

	public double getKrg() {
		return krg;
	}

	public double getKrb() {
		return krb;
	}

	public double getKrn() {
		return krn;
	}
}
