package hr.fer.zemris.java.dz3;

import java.util.Iterator;

public class PrimesCollection implements Iterable<Integer> {
	/*
	 * The actual work of finding primes is done by the iterator. The
	 * collection only has a size property, that is, a number of primes
	 * it will contain.
	 */
	private int size;
	
	/**
	 * The user must specify the collections size.
	 * @param size
	 */
	public PrimesCollection(int size) {
		this.size = size;
	}

	/**
	 * @return this PrimeCollections iterator.
	 */
	@Override
	public Iterator<Integer> iterator() {
		return new PrimesIterator();
	}
	
	/**
	 * @return this PrimesCollections size property.
	 */
	public int getSize() {
		return size;
	}

	private class PrimesIterator implements Iterator<Integer> {
		/*
		 * The collection doesn't actually store any primes, they are
		 * instead found by this iterator (using a rather primitive algorithm).
		 */
		private int curPrimIndex;
		private int curPrimValue;
		
		public PrimesIterator() {
			this.curPrimIndex = 1;	// the first...
			this.curPrimValue = 2;	// ...prime number
		}

		/**
		 * Checks whether there are more primes in the collection. The number of primes
		 * in the collection is equal to its size.
		 */
		@Override
		public boolean hasNext() {
			if(curPrimIndex > size) return false;
			return true;
		}

		/**
		 * Returns the next prime number.
		 */
		@Override
		public Integer next() {
			if(curPrimIndex == 1) {
				curPrimIndex++;
				return curPrimValue;	// returns 2 only once
			}
			for(int i = curPrimValue + 1; ; i++) {
				if(isPrime(i)) {
					curPrimIndex++;
					curPrimValue = i;
					return i;
				}
			}
		}

		/**
		 * This collection doesn't support removal of its elements.
		 */
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
		private boolean isPrime(int n) {
		    if (n % 2 == 0) return false;
		    for(int i = 3; i * i <= n; i += 2) {
		        if(n % i == 0)
		            return false;
		    }
		    return true;
		}
	}
}