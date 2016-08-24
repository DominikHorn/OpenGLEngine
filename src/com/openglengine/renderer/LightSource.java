package com.openglengine.renderer;

import com.openglengine.util.math.*;

/**
 * Container class representing a light source
 * 
 * @author Dominik
 *
 */
public class LightSource {
	private Vector3f position;
	private Vector3f color;

	public LightSource(Vector3f position, Vector3f color) {
		super();
		this.position = position;
		this.color = color;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}
}
