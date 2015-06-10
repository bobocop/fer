package hr.fer.zemris.java.hw07.students;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FileStudentDatabase implements StudentDatabase {
	/*
	 * For the best results (no additional empty lines when adding students for
	 * the first time) the text file this database is
	 * constructed with should initially contain no extra (blank) lines.
	 */
	private List<Student> students;
	private Path recordsPath;
	private List<StudentDBModificationListener> listeners;
	private boolean addBlankLine;
	
	/**
	 * Reads the Student record from a file.
	 * @param records path to a file containing the records
	 */
	public FileStudentDatabase(Path records) {
		this.students = new ArrayList<>();
		this.recordsPath = records;
		this.listeners = new LinkedList<>();
		this.addBlankLine = true;	// unless writing the first line to a file
		try(BufferedReader fin = new BufferedReader(
									new FileReader(records.toFile())
									);) {
			String line;
			while((line = fin.readLine()) != null) {
				String[] lineSplit = line.split("\\s++");
				if(lineSplit.length != 3) {
					System.out.println("Incorrect input line format:\n"
										+ line + "\nSkipping...");
					continue;
				} else {
					students.add(new Student(lineSplit[0], lineSplit[1], lineSplit[2]));
				}
			}
		} catch (FileNotFoundException e) {
			System.err.println("File not found");
		} catch (IOException e) {
			System.err.println("I/O error");
		}
	}
	
	@Override
	public int size() {
		return students.size();
	}

	@Override
	public Student getStudent(int index) {
		if(index < 0 || index > students.size() -1) {
			return null;
		} else {
			return students.get(index);
		}
	}

	@Override
	public int indexOf(Student student) {
		return students.indexOf(student);
	}

	@Override
	public void remove(int index) {
		try {
			students.remove(index);
			Files.delete(recordsPath);
			Files.createFile(recordsPath);
			addBlankLine = false;
			appendToFile(students.toArray(new Student[students.size()]));
			for(StudentDBModificationListener l : listeners) {
				l.studentsRemoved(this, 0, students.size());
			}
		} catch(IndexOutOfBoundsException e) {
			System.err.println("Specified index not found");
		} catch (IOException e) {
			System.err.println("An error has occured while " 
								+ "rewriting the records file");
		}
	}

	@Override
	public boolean addStudent(String studentID, String lastName,
			String firstName) {
		Student newStud = new Student(studentID, lastName, firstName);
		if(students.contains(newStud)) {
			return false;
		} else {
			students.add(newStud);
			appendToFile(newStud);
			for(StudentDBModificationListener l : listeners) {
				l.studentsAdded(this, students.size()-1, students.size()-1);
			}
			return true;
		}
	}
	
	@Override
	public void registerObserver(StudentDBModificationListener listener) {
		if(!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	@Override
	public void unRegisterObserver(StudentDBModificationListener listener) {
		if(listeners.contains(listener)) {
			listeners.remove(listener);
		}
	}
	
	/**
	 * Appends the new text data to the file.
	 * @param Student object to be added to the database
	 */
	private void appendToFile(Student... newStudents) {
		try(BufferedWriter fout = new BufferedWriter(
									new FileWriter(
											recordsPath.toString(), true
											)
									);) {
			for(int i = 0; i < newStudents.length; i++) {
				if(addBlankLine) {
					fout.newLine();
				}
				fout.append(String.format("%s\t%s\t%s", 
						newStudents[i].getStudentId(),
						newStudents[i].getLastName(),
						newStudents[i].getFirstName()));
				addBlankLine = true;
			}
		} catch (IOException e) {
			System.err.println("An error has occured while writing to '"
								+ recordsPath.toString() + "'");
		}
	}
}
