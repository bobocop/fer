package hr.fer.zemris.java.custom.scripting.exec;

public class ValueWrapper {
	/*
	 * Values stored by the setter and the constructor are not checked
	 * for validity right after being stored; checking is done when an
	 * operation using that value is performed.
	 */
	private Object value;

	/**
	 * Initializes the value stored in the wrapper.
	 * @param value
	 */
	public ValueWrapper(Object value) {
		this.value = value;
	}
	
	/**
	 * Sets the value of an object
	 * @param value
	 */
	public void setValue(Object value) {
		this.value = value;
	}
	
	/**
	 * @return the object contained in the wrapper
	 */
	public Object getValue() {
		return value;
	}
	
	/**
	 * Increments the value of the stored object, if possible
	 * @param value to increment by
	 */
	public void increment(Object incValue) {
		if(doubleOpCheck(value, incValue)) {
			value = toDouble(value) + toDouble(incValue);
		} else {
			value = toInteger(value) + toInteger(incValue);
		}
	}
	
	/**
	 * Decrements the value of a stored object, if possible.
	 * @param decValue
	 */
	public void decrement(Object decValue) {
		if(doubleOpCheck(value, decValue)) {
			value = toDouble(value) - toDouble(decValue);
		} else {
			value = toInteger(value) - toInteger(decValue);
		}
	}
	
	/**
	 * Multiplies the stored object by the argument value, if possible.
	 * @param mulValue
	 */
	public void multiply(Object mulValue) {
		if(doubleOpCheck(value, mulValue)) {
			value = toDouble(value) * toDouble(mulValue);
		} else {
			value = toInteger(value) * toInteger(mulValue);
		}
	}
	
	/**
	 * Divides the stored value by the argument value, if possible.
	 * @param divValue
	 */
	public void divide(Object divValue) {
		try {
			if(doubleOpCheck(value, divValue)) {
				value = toDouble(value) + toDouble(divValue);
			} else {
				value = toInteger(value) + toInteger(divValue);
			}
		} catch(ArithmeticException e) {
			System.out.println("Divided by zero");
		}
	}
	
	/**
	 * Compares the stored object with the provided argument.
	 * @param withValue
	 * @return -1 if ValueWrapper stored object < argument, 1 if >, 0 otherwise
	 */
	public int numCompare(Object withValue) {
		if(doubleOpCheck(value, withValue)) {
			return Double.compare(toDouble(value), toDouble(withValue));
		} else {
			return Integer.compare(toInteger(value), toInteger(withValue));
		}
	}
	
	
	private boolean doubleOpCheck(Object obj1, Object obj2) {
		try {
			if(obj1 instanceof String) {
				if(!((String) obj1).contains("E")
					&& !((String) obj1).contains(".")) {
					return false;	// could be int...
				}
				obj1 = Double.parseDouble((String) obj1);
			}
			if(obj2 instanceof String) {
				if(!((String) obj2).contains("E")
						&& !((String) obj2).contains(".")) {
						return false;	// could be int...
					}
				obj2 = Double.parseDouble((String) obj2);
			}
		} catch(NumberFormatException e) {
			System.out.println("The input string is not a valid number type");
			System.exit(-1);
		}
		if(obj1 instanceof Double || obj2 instanceof Double) {
			return true;
		}
		return false;	// either argument is null or possibly int
	}
	
	private Double toDouble(Object obj) {
		/*No try/catch needed here, already checked by doubleOpCheck*/
		if(obj instanceof Integer) {
			return ((Integer) obj).doubleValue();
		}
		else if(obj instanceof Double) {
			return (Double) obj;
		}
		else if(obj instanceof String) {
			return Double.parseDouble((String) obj);
		}
		else if(obj == null) {
			return Double.valueOf(0);	// obj is null
		}
		else throw new RuntimeException("An unsupported class has been wrapped");
	}
	
	private Integer toInteger(Object obj) {
		/*Should never be called with a Double argument*/
		try {
			if(obj instanceof String) {
				return Integer.parseInt((String) obj);
			}
		} catch (NumberFormatException e) {
			System.out.println("The input string is not a valid number type");
			System.exit(-1);
		}
		if(obj instanceof Integer) {
			return (Integer) obj;
		} 
		else if(obj == null){
			return Integer.valueOf(0); // obj is null
		}
		else throw new RuntimeException("An unsupported class has been wrapped");
	}
}
