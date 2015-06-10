package hr.fer.zemris.java.tecaj_06.rays;

public class SphereIntersection extends RayIntersection {
	Sphere sphereObj; 
	
	/**
	 * It is required to provide the instances of this class with the
	 * reference to the object (Sphere) they belong to, 
	 * hence the sphereObj parameter.
	 * @param point
	 * @param distance
	 * @param outer
	 * @param Sphere sphereObj to which this intersection belongs to
	 */
	protected SphereIntersection(Point3D point, double distance, boolean outer,
			Sphere sphereObj) {
		super(point, distance, outer);
		this.sphereObj = sphereObj;
	}

	@Override
	public Point3D getNormal() {
		return (this.getPoint().sub(sphereObj.getCenter())).normalize();
	}

	@Override
	public double getKdr() {
		return sphereObj.getKdr();
	}

	@Override
	public double getKdg() {
		return sphereObj.getKdg();
	}

	@Override
	public double getKdb() {
		return sphereObj.getKdb();
	}

	@Override
	public double getKrr() {
		return sphereObj.getKrr();
	}

	@Override
	public double getKrg() {
		return sphereObj.getKrg();
	}

	@Override
	public double getKrb() {
		return sphereObj.getKrb();
	}

	@Override
	public double getKrn() {
		return sphereObj.getKrn();
	}
}
