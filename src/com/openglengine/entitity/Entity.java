package com.openglengine.entitity;

import java.util.*;

import com.openglengine.entitity.component.*;
import com.openglengine.entitity.component.event.*;
import com.openglengine.util.math.*;

/**
 * Entity class containing all the components
 * 
 * @author Dominik
 *
 */
public final class Entity {
	private static int globalID = 0;

	private int entityUID = 0;
	private List<Component> components;

	/* Entity data TODO: come up with better way */
	public Vector3f position;
	public float rotX, rotY, rotZ;
	public float scale;

	public Entity(Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		this.entityUID = globalID++;
		this.components = new ArrayList<>();

		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
	}

	public int getEntityUID() {
		return this.entityUID;
	}

	public void addComponent(Component component) {
		this.components.add(component);
	}

	public void removeComponent(Component component) {
		this.components.remove(component);
	}

	public void notifyOtherComponents(ComponentEvent event, Component sender) {
		for (Component component : this.components) {
			if (!sender.equals(component))
				component.receiveEvent(event);
		}
	}

	public void update() {
		for (Component component : this.components)
			component.update(this);
	}
}
