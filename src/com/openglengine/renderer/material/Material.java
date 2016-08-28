package com.openglengine.renderer.material;

import java.util.*;

import com.openglengine.util.property.*;

/**
 * Container class for storing all material properties of a Model
 * 
 * @author Dominik
 *
 */
public abstract class Material implements PropertyContainer {
	/** All properties in this material */
	private Map<String, Property<? extends Object>> properties;

	/**
	 * Initialize new material
	 */
	public Material() {
		this.properties = new HashMap<>();
	}

	public abstract void initRendercode();

	public abstract void deinitRendercode();

	@Override
	public Property<? extends Object> getProperty(String propertyName) throws PropertyNotSetException {
		Property<? extends Object> prop = this.properties.get(propertyName);
		if (prop == null)
			throw new PropertyNotSetException();

		return prop;
	}

	@Override
	public Object getPropertyValue(String propertyName) throws PropertyNotSetException {
		return this.getProperty(propertyName).getValue();
	}

	@Override
	public void putProperty(String propertyName, Object property) {
		this.properties.put(propertyName, new Property<Object>(property));
	}

	@Override
	public boolean doesPropertyExist(String propertyName) {
		return this.properties.get(propertyName) != null;
	}
}
