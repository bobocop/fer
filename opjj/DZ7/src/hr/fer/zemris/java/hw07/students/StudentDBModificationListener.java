package hr.fer.zemris.java.hw07.students;

public interface StudentDBModificationListener {
	/*
	 * The following two methods should be invoked when a simple
	 * modification occured e. g. only a certain range of students
	 * have been modified.
	 */
	void studentsAdded(StudentDatabase db, int fromIndex, int toIndex);
	void studentsRemoved(StudentDatabase db, int fromIndex, int toIndex);
	
	/*
	 * This method should be called whenever some complex action
	 * occured.
	 */
	void databaseChanged(StudentDatabase db);
}
