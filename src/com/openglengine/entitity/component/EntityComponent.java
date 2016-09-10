package com.openglengine.entitity.component;

import com.openglengine.entitity.*;

/**
 * Behavioral code for an entity goes in here
 * 
 * @author Dominik
 *
 */
public interface EntityComponent {
	/**
	 * Initialize this component/the entity for use with this component. This method will be called once when the
	 * component gets added to an entity
	 * 
	 * @param entity
	 */
	public void init(Entity entity);

	/**
	 * Use this to update component state / state of entity
	 * 
	 * @param entity
	 */
	public void update(Entity entity);

	/**
	 * Use this method to cleanup all resources
	 */
	public void cleanup();
}
