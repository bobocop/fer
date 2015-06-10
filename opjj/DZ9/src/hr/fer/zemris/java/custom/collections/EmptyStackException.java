package hr.fer.zemris.java.custom.collections;

/**
 * ObjectStack throws these when an attempt is made to pop a value
 * from an empty stack.
 */
public class EmptyStackException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	EmptyStackException() {
		super("Cannot pop from an empty stack");
	}
}
