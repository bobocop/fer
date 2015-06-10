package hr.fer.zemris.java.custom.scripting.exec;

import java.util.HashMap;
import java.util.Map;

public class ObjectMultistack {
	Map<String, MultistackEntry> multiStack;
	
	/**
	 * No default values are required, the Multistack is internally stored
	 * as a string to stack HashMap.
	 */
	public ObjectMultistack() {
		this.multiStack = new HashMap<String, MultistackEntry>();
	}
	
	/**
	 * Pushes the wrapped value onto the stack specified by the name
	 * argument. If the specifed stack does not exist, it is created.
	 * @param name the name of the stack
	 * @param valueWrapper value to be pushed
	 */
	public void push(String name, ValueWrapper valueWrapper) {
		MultistackEntry stack = multiStack.get(name);
		if(stack == null) {
			stack = new MultistackEntry(valueWrapper, null);
			multiStack.put(name, stack);
		} else {
			multiStack.put(name, new MultistackEntry(valueWrapper, stack));
		}
	}
	
	/** 
	 * Returns the value popped from the stack specified by name.
	 * @param name
	 * @return value
	 */
	public ValueWrapper pop(String name) {
		MultistackEntry stack = multiStack.get(name);
		if(stack != null) {
			multiStack.put(name, stack.getNext());
			return stack.getValueWrap();
		}
		throw new RuntimeException("Stack \"" + name + "\" is nonexistent or empty");
	}
	
	/**
	 * Returns the value from the top of the stack.
	 * @param name
	 * @return value
	 */
	public ValueWrapper peek(String name) {
		MultistackEntry stack = multiStack.get(name);
		if(stack != null) {
			return stack.getValueWrap();
		}
		throw new RuntimeException("Stack \"" + name + "\" is nonexistent or empty");
	}
	
	/**
	 * Will return true even if the specified stack does not exist.
	 * @param name
	 * @return true if the specified stack is empty, false otherwise
	 */
	public boolean isEmpty(String name) {
		MultistackEntry stack = multiStack.get(name);
		if(stack == null) {
			return true;
		} else {
			return false;
		}
	}
	
	private class MultistackEntry {
		/*
		 * Stack nodes mapped to Strings by ObjectMultistack
		 */
		private ValueWrapper valueWrap;
		private MultistackEntry next;
		
		/**
		 * The stack is internally stored as a linked list.
		 */
		public MultistackEntry(ValueWrapper value, MultistackEntry next) {
			this.valueWrap = value;
			this.next = next;
		}
		
		/**
		 * @return ValueWrapper object contained in this node
		 */
		public ValueWrapper getValueWrap() {
			return valueWrap;
		}
		
		/**
		 * @return reference to the next element on the stack
		 */
		public MultistackEntry getNext() {
			return next;
		}
	}
}
