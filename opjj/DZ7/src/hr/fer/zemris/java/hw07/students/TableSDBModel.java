package hr.fer.zemris.java.hw07.students;

import javax.swing.table.AbstractTableModel;

public class TableSDBModel extends AbstractTableModel implements
		StudentDBModificationListener {
	private static final long serialVersionUID = 1L;
	private StudentDatabase db;
	public final int NUM_COLUMNS = 4;
	public final String[] colNames = {"#", "ID", "Prezime", "Ime"};

	public TableSDBModel(StudentDatabase database) {
		this.db = database;
		db.registerObserver(this);
	}
	
	@Override
	public int getRowCount() {
		return db.size();
	}

	@Override
	public int getColumnCount() {
		return NUM_COLUMNS;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex > NUM_COLUMNS - 1 || rowIndex > db.size() - 1
				|| columnIndex < 0) {
			return null;
		} else {
			Student row = db.getStudent(rowIndex);
			switch (columnIndex) {
			case 0:
				return rowIndex + 1;
			case 1:
				return row.getStudentId();
			case 2:
				return row.getLastName();
			case 3:
				return row.getFirstName();
			default:
				return null;
			}
		}
	}
	
	
	
	@Override
	public String getColumnName(int column) {
		return colNames[column];
	}

	@Override
	public void studentsAdded(StudentDatabase db, int fromIndex, int toIndex) {
		fireTableRowsInserted(fromIndex, toIndex);
	}

	@Override
	public void studentsRemoved(StudentDatabase db, int fromIndex, int toIndex) {
		fireTableRowsDeleted(fromIndex, toIndex);
	}

	@Override
	public void databaseChanged(StudentDatabase db) {
		fireTableRowsUpdated(0, db.size());
	}
}
