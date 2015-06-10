package hr.fer.zemris.java.graphics.shapes;

public class Triangle extends GeometricShape {
	private int aX;
	private int aY;
	private int bX;
	private int bY;
	private int cX;
	private int cY;
	
	/**
	 * Triangle constructor. The user must specify the coordinates of its
	 * three vertices.
	 * @throws IllegalArgumentException if the provided points are invalid or
	 * do not constitute a triangle
	 */
	public Triangle(int aX, int aY, int bX, int bY, int cX, int cY) 
		throws IllegalArgumentException {
		this.aX = aX;
		this.aY = aY;
		this.bX = bX;
		this.bY = bY;
		this.cX = cX;
		this.cY = cY;
		triangleCheck();
	}

	/**
	 * This function checks if a point is contained in the triangle. It 
	 * actually calls another function which operates with double values 
	 * of provided arguments.
	 */
	@Override
	public boolean containsPoint(int x, int y) {
		return isInside(x, y);
	}
	
	/*Following are getters and setters for the Triangle properties*/
	public int getaX() {
		return aX;
	}

	public void setaX(int aX) {
		this.aX = aX;
		triangleCheck();
	}

	public int getaY() {
		return aY;
	}

	public void setaY(int aY) {
		this.aY = aY;
		triangleCheck();
	}
	
	public int getbX() {
		return bX;
	}
	

	public void setbX(int bX) {
		this.bX = bX;
		triangleCheck();
	}

	public int getbY() {
		return bY;
	}

	public void setbY(int bY) {
		this.bY = bY;
		triangleCheck();
	}

	public int getcX() {
		return cX;
	}

	public void setcX(int cX) {
		this.cX = cX;
		triangleCheck();
	}

	public int getcY() {
		return cY;
	}

	public void setcY(int cY) {
		this.cY = cY;
		triangleCheck();
	}
	
	/*Checks if a point is inside of a triangle*/
	private boolean isInside(double x, double y) {
		double epsilon = 0.1;
		double areaSum = triangleArea(
										distance(x, y, aX, aY),
										distance(x, y, bX, bY),
										distance(bX, bY, aX, aY)
										)
						+
						triangleArea(
										distance(x, y, aX, aY),
										distance(x, y, cX, cY),
										distance(aX, aY, cX, cY)
										)
						+
						triangleArea(
										distance(x, y, bX, bY),
										distance(x, y, cX, cY),
										distance(bX, bY, cX, cY)
										);
		double triArea = triangleArea(
										distance(aX, aY, bX, bY),
										distance(bX, bY, cX, cY),
										distance(cX, cY, aX, aY)
										);
		return Math.abs(triArea - areaSum) <= epsilon;
	}
	
	/*Calculates the distance between two points*/
	private double distance(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
	}
	
	/*Calculates the area of a triangle specified with the sizes of its sides*/
	private double triangleArea(double a, double b, double c) {
		double s = (a + b + c) / 2.0;
		return Math.sqrt(s * (s - a) * (s - b) * (s - c));
	}
	
	/*Checks if the provided points constitute a triangle*/
	private void triangleCheck() {
		double epsilon = - 0.005;
		double a = distance(bX, bY, cX, cY);
		double b =distance(cX, cY, aX, aY);
		double c = distance(aX, aY, bX, bY);
		if(
			a - (b + c) > epsilon
			|| b - (a + c) > epsilon
			|| c - (a + b) > epsilon
			) throw new IllegalArgumentException();
	}
}
