package com.openglengine.entitity;

import com.openglengine.entitity.component.*;
import com.openglengine.renderer.model.*;
import com.openglengine.util.math.*;

public class VisibleEntity extends Entity {
	/* TODO: refactor */
	/** Visual representation */
	private TexturedModel model;

	/** scale information */
	public float scale;

	public VisibleEntity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(position, rotX, rotY, rotZ);
		this.scale = scale;
		this.model = model;
	}

	/**
	 * retrieve this entities textured model
	 * 
	 * @return
	 */
	public TexturedModel getModel() {
		return this.model;
	}

	@Override
	public VisibleEntity addComponent(Component component) {
		super.addComponent(component);

		return this;
	}

	@Override
	public Entity removeComponent(Component component) {
		super.removeComponent(component);

		return this;
	}

	@Override
	public void cleanup() {
		super.cleanup();
		this.model.cleanup();
	}
}
