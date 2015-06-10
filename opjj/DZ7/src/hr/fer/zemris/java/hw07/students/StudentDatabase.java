package hr.fer.zemris.java.hw07.students;

public interface StudentDatabase {
	
	/**
	 * @return int the number of students 
	 */
	int size();
	
	/**
	 * @param index
	 * @return Student at index parameter
	 */
	Student getStudent(int index);
	
	/**
	 * @param student
	 * @return int index of Student instance denoted by method parameter
	 */
	int indexOf(Student student);
	
	/**
	 * Removes the index-th Student.
	 * @param index
	 */
	void remove(int index);
	
	/**
	 * Adds the Student to the databes, if the instance doesn't already exists.
	 * @param studentID
	 * @param lastName
	 * @param firstName
	 * @return false if no changes have been made, true otherwise
	 */
	boolean addStudent(String studentID, String lastName, String firstName);
	
	/**
	 * Registers an observer of this database instance.
	 * @param listener to be added
	 */
	void registerObserver(StudentDBModificationListener listener);
	
	/**
	 * Removes the listener from the internally stored list. 
	 * @param listener to be removed
	 */
	void unRegisterObserver(StudentDBModificationListener listener);
}
