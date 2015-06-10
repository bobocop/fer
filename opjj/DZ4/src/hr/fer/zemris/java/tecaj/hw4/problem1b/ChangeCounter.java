package hr.fer.zemris.java.tecaj.hw4.problem1b;

public class ChangeCounter implements IntegerStorageObserver {
	private int count = 0;
	
	/**
	 * Prints the number of times the stored value has changed.
	 */
	@Override
	public void valueChanged(IntegerStorageChange istoragech) {
		count++;
		System.out.println("Number of changes since tracking: "+count);
	}

}
