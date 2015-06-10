package hr.fer.zemris.java.tecaj.hw4.problem1a;

public class IntegerStorage {
	private int value;
	private IntegerStorageObserver observer;

	/**
	 * Initializes the storage.
	 * @param initialValue
	 */
	public IntegerStorage(int initialValue) {
		this.value = initialValue;
	}
	
	/**
	 * Places an observer into the storage.
	 * @param observer
	 */
	public void setObserver(IntegerStorageObserver observer) {
		this.observer = observer;
	}
	
	/**
	 * Removes the observer.
	 */
	public void clearObserver() {
		this.observer = null;
	}

	/**
	 * @return the value stored in the storage
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * Sets the value in the storage and alerts the observer, if it exists.
	 * @param value
	 */
	public void setValue(int value) {
		if (this.value != value) {
			this.value = value;
			if (observer != null) {
				observer.valueChanged(this);
			}
		}
	}
}