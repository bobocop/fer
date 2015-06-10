package hr.fer.zemris.java.dz3;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class LikeMedian<T extends Comparable<? super T>> {
	private List<T> elementList;
	
	/**
	 * Default constructor simply initalizes the internal collection.
	 */
	public LikeMedian() {
		elementList = new LinkedList<T>();
	}
	
	/**
	 * @param object an (comparable) object which 
	 * will be added to the collection.
	 */
	public void add(T object) {
		elementList.add(object);
	}
	
	/**
	 * Returns the median element based on a natural ordering of
	 * collection's elements.
	 * @return median element
	 */
	public T get() {
		Collections.sort(elementList);
		int listSiz = elementList.size();
		if(listSiz % 2 == 0) {
			/*If the number of elements is equal, return the smaller one*/
			return elementList.get(listSiz / 2 - 1);
		} else {
			return elementList.get(listSiz / 2);
		}
	}
}
