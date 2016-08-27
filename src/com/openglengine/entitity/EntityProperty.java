package com.openglengine.entitity;

/**
 * Container class for a property stored in entity
 * 
 * @author Dominik
 *
 */
public class EntityProperty<T extends Object> {
	private T value;

	/**
	 * Create new entity property
	 * 
	 * @param value
	 */
	protected EntityProperty(T value) {
		this.value = value;
	}

	/**
	 * Retrieve this property's value
	 * 
	 * @return
	 */
	public T getValue() {
		return this.value;
	}

	/**
	 * Retrieve the class of this type
	 * 
	 * @return
	 */
	public Class<? extends Object> getType() {
		return this.value.getClass();
	}

	/**
	 * Set a new value for this property
	 * 
	 * @param value
	 */
	public void setValue(T value) {
		this.value = value;
	}
}
