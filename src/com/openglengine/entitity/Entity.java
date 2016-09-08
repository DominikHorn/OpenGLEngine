package com.openglengine.entitity;

import java.util.*;

import com.openglengine.core.*;
import com.openglengine.entitity.component.*;
import com.openglengine.eventsystem.defaultevents.*;
import com.openglengine.util.*;
import com.openglengine.util.math.*;
import com.openglengine.util.property.*;

/**
 * Entity class. One entity may contain multiple components, all of which should be as independent of one another as
 * possible
 * 
 * @author Dominik
 *
 */
public class Entity implements PropertyContainer {
	/** Each entity has a unique global id. This is used to generate those ids */
	private static int globalID = 0;

	/** Entity's globally unique id */
	private int entityUID = 0;

	/** Entity's components */
	private List<EntityComponent> components;

	/** Properties list */
	private Map<String, Property<? extends Object>> properties;

	/** Entity's position in the game world */
	public Vector3f position;

	/** Entity's rotation on all three axis */
	public Vector3f rotation;

	/** Entity's scale in all three directions */
	public Vector3f scale;

	/**
	 * Initialize this entity
	 */
	public Entity() {
		this(new Vector3f());
	}

	public Entity(Vector3f position) {
		this(position, new Vector3f());
	}

	public Entity(Vector3f position, Vector3f rotation) {
		this(position, rotation, new Vector3f(1, 1, 1));
	}

	public Entity(Vector3f position, Vector3f rotation, Vector3f scale) {
		this.entityUID = globalID++;
		this.components = new ArrayList<>();
		this.properties = new HashMap<>();
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
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
	public Entity addComponent(EntityComponent component) {
		// TODO: maybe implement component reordering because some components might be execution order dependent
		component.init(this);
		this.components.add(component);

		return this;
	}

	/**
	 * remove a component from this entity
	 * 
	 * @param component
	 * @return convenience self return to make removeComponent().removeComponent().removeComponent()... possible
	 */
	public Entity removeComponent(EntityComponent component) {
		this.components.remove(component);

		return this;
	}

	/**
	 * notify all other components of this entity that something happened
	 * 
	 * 
	 * @param event
	 * @param sender
	 *            use "this" keyword for sender field
	 */
	public void notifyOtherComponents(BaseEvent event, EntityComponent sender) {
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

	/**
	 * Cleans this entity's resources
	 */
	public void cleanup() {
		this.components.forEach(c -> c.cleanup(this));
		this.properties.clear();
	}

	/**
	 * Prepares this instance for rendering
	 * 
	 * @param shader
	 */
	public void initRendercode() {
		// Set model transform
		TransformationMatrixStack tms = Engine.getModelMatrixStack();
		tms.push();
		tms.translate(this.position);
		tms.rotateX(this.rotation.x);
		tms.rotateY(this.rotation.y);
		tms.rotateZ(this.rotation.z);
		tms.scale(this.scale.x, this.scale.y, this.scale.z);
	}

	public void deinitRenderCode() {
		Engine.getModelMatrixStack().pop();
	}

	/** Property container */
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
