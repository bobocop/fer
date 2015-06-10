package hr.fer.zemris.java.tecaj.hw4.problem1b;

public class IntegerStorageChange {
	/*
	 * Instances of this class are created whenever the value stored in
	 * the IntegerStorage is changed. It contains some information about
	 * the operation that took place.
	 */
	private IntegerStorage object;
	private int previousVal;
	private int currentVal;
	
	/**
	 * Contains some info about what is to be stored or changed in the
	 * provided IntegerStorage.
	 * @param object the IntegerStorage to store the value to
	 * @param newValue the value to be stored
	 */
	public IntegerStorageChange(IntegerStorage object, int newValue) {
		this.object = object;
		this.previousVal = object.getValue();
		this.currentVal = newValue;
	}
	
	/**
	 * Returns the IntegerStorage object.
	 * @return the storage on which the change took place
	 */
	public IntegerStorage getObject() {
		return object;
	}
	
	/**
	 * Returns the value of the storage before the change.
	 * @return previous value of the storage
	 */
	public int getPreviousVal() {
		return previousVal;
	}
	
	/**
	 * Returns the current value stored.
	 * @return current value of the storage
	 */
	public int getCurrentVal() {
		return currentVal;
	}
}
