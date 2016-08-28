package com.openglengine.util.property;

/**
 * Interface implemented by all Classes that can contain properties
 * 
 * @author Dominik
 *
 */
public interface PropertyContainer {
	/**
	 * Retrieve a property of this container
	 * 
	 * @param propertyName
	 * @return
	 */
	public Property<? extends Object> getProperty(String propertyName) throws PropertyNotSetException;

	/**
	 * Retrieve a value property property of this container
	 * 
	 * @param propertyName
	 * @return
	 */
	public Object getPropertyValue(String propertyName) throws PropertyNotSetException;

	/**
	 * Put a new property
	 * 
	 * @param propertyName
	 * @param property
	 * 
	 * @return convenience self return for method chaining
	 */
	public void putProperty(String propertyName, Object property);

	/**
	 * Returns true when the property does exist
	 * 
	 * @param propertyName
	 * @return
	 */
	public boolean doesPropertyExist(String propertyName);

}
