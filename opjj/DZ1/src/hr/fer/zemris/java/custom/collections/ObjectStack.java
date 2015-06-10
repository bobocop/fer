package hr.fer.zemris.java.custom.collections;

public class ObjectStack {
	private ArrayBackedIndexedCollection arrayinst;
	
	/**
	 * Konstruktor, inicijalizira kapacitet stoga na 16 elemenata.
	 */
	public ObjectStack() {
		arrayinst = new ArrayBackedIndexedCollection();
	}
	
	/**
	 * Vraca true ako je stog prazan, false inace.
	 * @return Boolean
	 */
	public boolean isEmpty() {
		return arrayinst.isEmpty();
	}
	
	/**
	 * Vraca velicinu stoga.
	 * @return trenutni broj elemenata
	 */
	public int size() {
		return arrayinst.size();
	}
	
	/**
	 * Stavlja element na vrh stoga.
	 * @param element
	 */
	public void push(Object value) {
		arrayinst.add(value);
	}
	
	/**
	 * Vraca prvi element s vrha stoga.
	 * @return element
	 * @throws EmptyStackException ukoliko je stog prazan
	 */
	public Object pop() throws EmptyStackException {
		if(isEmpty()) {
			throw new EmptyStackException();
		}
		Object temp = arrayinst.get(arrayinst.size() - 1);
		arrayinst.remove(arrayinst.size() - 1);
		return temp;
	}
	
	/**
	 * Samo vraca element s vrha, bez micanja istog sa stoga.
	 * @return element
	 */
	public Object peek() {
		if(isEmpty()) {
			throw new EmptyStackException();
		}
		return arrayinst.get(arrayinst.size() - 1);
	}
	
	/**
	 * Mice elemente sa stoga.
	 */
	public void clear() {
		arrayinst.clear();
	}
}
