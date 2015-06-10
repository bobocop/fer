package hr.fer.zemris.java.tecaj.hw4.problem1b;

import java.util.List;
import java.util.LinkedList;

public class IntegerStorage {
	private int value;
	private List<IntegerStorageObserver> observers;

	/**
	 * Initializes the storage with an initial value provided.
	 * @param initialValue
	 */
	public IntegerStorage(int initialValue) {
		this.value = initialValue;
		this.observers = new LinkedList<IntegerStorageObserver>();
	}
	
	/**
	 * Adds an observer to the storeg.
	 * @param observer
	 */
	public void addObserver(IntegerStorageObserver observer) {
		observers.add(observer);
	}
	
	/**
	 * Removes the specified observer from the storage.
	 * @param observer
	 */
	public void removeObserver(IntegerStorageObserver observer) {
		observers.remove(observer);
	}
	
	/**
	 * Removes all the observers from the storage.
	 */
	public void clearObserver() {
		observers.clear();
	}
	
	/**
	 * Returns the value currently kept in the storage.
	 * @return value
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * Puts the new value in the storage and alerts the observers of the
	 * change that happened.
	 * @param value
	 */
	public void setValue(int value) {
		if (this.value != value) {
			IntegerStorageChange change = new IntegerStorageChange(this, value);
			this.value = value;
			for(IntegerStorageObserver obs : observers) {
				obs.valueChanged(change);
			}
		}
	}
}