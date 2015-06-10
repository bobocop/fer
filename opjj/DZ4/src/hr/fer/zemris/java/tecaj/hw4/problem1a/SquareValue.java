package hr.fer.zemris.java.tecaj.hw4.problem1a;

public class SquareValue implements IntegerStorageObserver {

	/**
	 * This observer prints the square of the stored
	 * value, whenever it changes.
	 */
	@Override
	public void valueChanged(IntegerStorage istorage) {
		int val = istorage.getValue();
		System.out.println("Provided new value: "+val+", square is "+val*val);
	}

}
