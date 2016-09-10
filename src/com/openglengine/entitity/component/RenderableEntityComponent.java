package com.openglengine.entitity.component;

import com.openglengine.entitity.*;

public interface RenderableEntityComponent {
	/**
	 * Initialize this component/the entity for use with this component. This method will be called once when the
	 * component gets added to a renderableentity
	 * 
	 * @param entity
	 */
	public void init(RenderableEntity entity);

	/**
	 * Use this to update component state / state of the renderableentity
	 * 
	 * @param entity
	 */
	public void update(RenderableEntity entity);

	/**
	 * Use this method to cleanup all resources
	 */
	public void cleanup();
}
