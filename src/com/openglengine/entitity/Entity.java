package com.openglengine.entitity;

import java.util.*;

import com.openglengine.entitity.component.*;
import com.openglengine.eventsystem.defaultevents.*;
import com.openglengine.util.math.*;

/**
 * Entity class. One entity may contain multiple components, all of which should be as independent of one another as
 * possible
 * 
 * @author Dominik
 *
 */
public class Entity {
	/** Each entity has a unique global id. This is used to generate those ids */
	private static int globalID = 0;

	/** The entity's globally unique id */
	private int entityUID = 0;

	/** The entity's components */
	private List<Component> components;

	private Map<String, EntityProperty<? extends Object>> properties;

	/**
	 * Initialize this entity
	 */
	public Entity() {
		this.entityUID = globalID++;
		this.components = new ArrayList<>();
		this.properties = new HashMap<>();
	}

	/**
	 * Puts the default properties in place (convenience method)
	 * 
	 * TODO: needed?
	 * 
	 * @return convenience self return for method chaining
	 */
	public Entity putEmptyDefaultProperties() {
		this.putProperty(DefaultEntityProperties.PROPERTY_POSITION, new Vector3f(0, 0, 0));
		this.putProperty(DefaultEntityProperties.PROPERTY_ROTATION, new Vector3f(0, 0, 0));
		this.putProperty(DefaultEntityProperties.PROPERTY_SCALE, new Vector3f(1, 1, 1));

		return this;
	}

	/**
	 * retrieve the globally unique id of this entity
	 * 
	 * @return
	 */
	public int getEntityUID() {
		return this.entityUID;
	}

	/**
	 * add a component to this entity
	 * 
	 * @param component
	 * @return convenience self return to make addComponent().addComponent().addComponent()... possible
	 */
	public Entity addComponent(Component component) {
		// TODO: maybe implement component reordering because some components might be execution order dependent
		this.components.add(component);

		return this;
	}

	/**
	 * remove a component from this entity
	 * 
	 * @param component
	 * @return convenience self return to make removeComponent().removeComponent().removeComponent()... possible
	 */
	public Entity removeComponent(Component component) {
		this.components.remove(component);

		return this;
	}

	/**
	 * Retrieve a property of this entity
	 * 
	 * @param propertyName
	 * @return
	 */
	public EntityProperty<? extends Object> getProperty(String propertyName) throws PropertyNotSetException {
		EntityProperty<? extends Object> prop = this.properties.get(propertyName);
		if (prop == null)
			throw new PropertyNotSetException();

		return prop;
	}

	/**
	 * Retrieve a value property property of this entity
	 * 
	 * @param propertyName
	 * @return
	 */
	public Object getValueProperty(String propertyName) throws PropertyNotSetException {
		return this.getProperty(propertyName).getValue();
	}

	/**
	 * Put a new property
	 * 
	 * @param propertyName
	 * @param property
	 * 
	 * @return convenience self return for method chaining
	 */
	public Entity putProperty(String propertyName, Object property) {
		this.properties.put(propertyName, new EntityProperty<Object>(property));
		return this;
	}

	/**
	 * Returns true when the property does exist
	 * 
	 * @param propertyName
	 * @return
	 */
	public boolean doesPropertyExist(String propertyName) {
		return this.properties.get(propertyName) != null;
	}

	/**
	 * notify all other components of this entity that something happened
	 * 
	 * 
	 * @param event
	 * @param sender
	 *            use "this" keyword for sender field
	 */
	public void notifyOtherComponents(BaseEvent event, Component sender) {
		this.components.forEach(c -> {
			if (!sender.equals(c))
				c.receiveEvent(event);
		});
	}

	/**
	 * Update this entity. Internally, this will update all components of this entity
	 */
	public void update() {
		this.components.forEach(c -> c.update(this));
	}

	public void cleanup() {
		this.components.forEach(c -> c.cleanup());
		this.properties.clear();
	}
}
