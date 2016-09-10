package com.openglengine.entitity;

import java.util.*;

import com.openglengine.entitity.component.*;
import com.openglengine.eventsystem.*;
import com.openglengine.util.*;
import com.openglengine.util.math.*;

/**
 * Entity class. One entity may contain multiple components, all of which should be as independent of one another as
 * possible
 * 
 * @author Dominik
 *
 */
public class Entity implements ResourceManager {
	/** Entity's globally unique id */
	private int entityUID = 0;

	/** Entity's components */
	private List<EntityComponent> components;

	/** Event manager for component events */
	private EventManager<ComponentEvent> componentEventSystem;

	/** Entity's position in the game world */
	public Vector3f position;

	/** Entity's rotation on all three axis */
	public Vector3f rotation;

	/**
	 * Initialize this entity
	 */
	public Entity() {
		this(new Vector3f());
	}

	/**
	 * 
	 * @param position
	 */
	public Entity(Vector3f position) {
		this(position, new Vector3f());
	}

	/**
	 * 
	 * @param position
	 * @param rotation
	 */
	public Entity(Vector3f position, Vector3f rotation) {
		this.entityUID = EntityUIDContainer.GLOBAL_ENTITY_ID_CNT++;
		this.components = new ArrayList<>();
		this.componentEventSystem = new EventManager<>();
		this.position = position;
		this.rotation = rotation;
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
		// TODO: implement component reordering because some components might be execution order dependent
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
	 * Update this entity. Internally, this will update all components of this entity
	 */
	public void update() {
		this.components.forEach(c -> c.update(this));
	}

	public EventManager<ComponentEvent> getComponentEventSystem() {
		return this.componentEventSystem;
	}

	/**
	 * Cleans this entity's resources
	 */
	@Override
	public void cleanup() {
		this.components.forEach(c -> c.cleanup());
	}
}
