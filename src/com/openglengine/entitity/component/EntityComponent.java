package com.openglengine.entitity.component;

import com.openglengine.entitity.*;

/**
 * Behavioural code for an entity goes in here
 * 
 * @author Dominik
 *
 */
public abstract class EntityComponent {
	/**
	 * Initialize this component/the entity for use with this component. This method will be called once when the
	 * component gets added to an entity
	 * 
	 * @param entity
	 */
	public abstract void init(Entity entity);

	/**
	 * Use this to update component state / state of entity
	 * 
	 * @param entity
	 */
	public abstract void update(Entity entity);

	/**
	 * Use this method to cleanup all resources
	 */
	public abstract void cleanup(Entity entity);
}
