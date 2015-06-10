package hr.fer.zemris.java.hw07.students;

import javax.swing.AbstractListModel;

public class ListSDBModel extends AbstractListModel<Student> implements
		StudentDBModificationListener {
	private static final long serialVersionUID = 1L;
	private StudentDatabase db;
	
	public ListSDBModel(StudentDatabase db) {
		super();
		this.db = db;
		db.registerObserver(this);
	}
	
	@Override
	public int getSize() {
		return db.size();
	}

	@Override
	public Student getElementAt(int index) {
		return db.getStudent(index);
	}

	@Override
	public void studentsAdded(StudentDatabase db, int fromIndex, int toIndex) {
		fireContentsChanged(this, fromIndex, toIndex);
	}

	@Override
	public void studentsRemoved(StudentDatabase db, int fromIndex, int toIndex) {
		fireContentsChanged(this, fromIndex, toIndex);
	}

	@Override
	public void databaseChanged(StudentDatabase db) {
		fireContentsChanged(this, 0, db.size());
	}

}
