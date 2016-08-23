package com.openglengine.entitity.component;

import com.openglengine.entitity.*;
import com.openglengine.entitity.component.event.*;

/**
 * Behavioural code for an entity goes in here
 * 
 * @author Dominik
 *
 */
public abstract class Component {
	/**
	 * Use this to update component state / state of entity
	 * 
	 * @param entity
	 */
	public abstract void update(Entity entity);

	/**
	 * This method will be called when another component from this entity wants to notify about certain changes
	 * 
	 * @param event
	 */
	public abstract void receiveEvent(ComponentEvent event);
}
