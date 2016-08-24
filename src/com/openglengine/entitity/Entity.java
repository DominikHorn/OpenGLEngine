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

	/* TODO: potentially find better way */
	/** position to render model at */
	public Vector3f position;

	/** rotation information */
	public float rotX, rotY, rotZ;

	/**
	 * Initialize this entity
	 */
	public Entity(Vector3f position, float rotX, float rotY, float rotZ) {
		this.entityUID = globalID++;
		this.components = new ArrayList<>();
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
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
	 * notify all other components of this entity that something happened
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
	}
}
