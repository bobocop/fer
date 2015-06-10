package hr.fer.zemris.java.custom.collections;

public class EmptyStackException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	EmptyStackException() {
		super("Cannot pop from an empty stack");
	}
}
