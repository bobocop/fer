package hr.fer.zemris.java.hw07.students;

public class Student {
	private String studentId;
	private String firstName;
	private String lastName;
	
	public Student(String studentId, String lastName, String firstName) {
		super();
		this.studentId = studentId;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getStudentId() {
		return studentId;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}
	
	@Override
	public String toString() {
		return studentId + " " + lastName + ", " + firstName;
	}
	
	@Override
	public boolean equals(Object other) {
		if(other instanceof Student) { 
			return ((Student) other).getStudentId().equals(this.studentId);
		} else {
			return false;
		}
	}
}
