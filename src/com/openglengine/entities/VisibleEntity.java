package com.openglengine.entities;

import com.openglengine.core.*;
import com.openglengine.eventsystem.defaultevents.*;
import com.openglengine.renderer.model.*;
import com.openglengine.renderer.shader.*;
import com.openglengine.util.math.*;

/**
 * Visible opengl entity transform data
 * 
 * @author Dominik
 *
 */
public class VisibleEntity extends BaseEntity {
	protected ShaderProgram shader;
	private RawModel model;
	private Vector3f position;
	private float rotX, rotY, rotZ;
	private float scale;

	public VisibleEntity(RawModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;

		Engine.EVENT_MANAGER.registerListenerForEvent(UpdateEvent.class, e -> update((UpdateEvent) e));
	}

	protected void update(UpdateEvent e) {
		// TODO: implement
	}

	public void render() {
		this.model.render(position, rotX, rotY, rotZ, scale);
	}
}
