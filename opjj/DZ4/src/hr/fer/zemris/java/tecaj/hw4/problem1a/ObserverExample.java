package hr.fer.zemris.java.tecaj.hw4.problem1a;

public class ObserverExample {
	
	public static void main(String[] args) {
		IntegerStorage istorage = new IntegerStorage(20);
		IntegerStorageObserver observer = new SquareValue();
		istorage.setObserver(observer);
		istorage.setValue(5);
		istorage.setValue(2);
		istorage.setValue(25);
		istorage.setObserver(new ChangeCounter());
		istorage.setValue(13);
		istorage.setValue(22);
		istorage.setValue(15);
	}
}
