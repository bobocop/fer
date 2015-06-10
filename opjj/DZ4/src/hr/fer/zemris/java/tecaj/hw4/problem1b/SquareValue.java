package hr.fer.zemris.java.tecaj.hw4.problem1b;

public class SquareValue implements IntegerStorageObserver {

	/**
	 * Whenever the observed IntegerStorage changes, this Observer prints
	 * the square of the new value.
	 */
	@Override
	public void valueChanged(IntegerStorageChange istoragech) {
		int val = istoragech.getCurrentVal();
		System.out.println("Provided new value: "+val+", square is "+val*val);
	}

}
