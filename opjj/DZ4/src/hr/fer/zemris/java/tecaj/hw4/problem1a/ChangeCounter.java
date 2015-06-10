package hr.fer.zemris.java.tecaj.hw4.problem1a;

public class ChangeCounter implements IntegerStorageObserver {
	private int count = 0;
	
	/**
	 * Prints the number of times the stored value has changed.
	 */
	@Override
	public void valueChanged(IntegerStorage istorage) {
		count++;
		System.out.println("Number of changes since tracking: "+count);
	}

}
