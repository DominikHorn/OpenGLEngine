package com.openglengine.renderer;

import com.openglengine.util.math.*;

/**
 * Container class representing a light source
 * 
 * @author Dominik
 *
 */
public class LightSource {
	// TODO: refactor
	private Vector3f position;
	private Vector3f color;
	private float brightness;

	public LightSource(Vector3f position, Vector3f color, float brightness) {
		super();
		this.position = position;
		this.color = color;
		this.brightness = brightness;
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

	public float getBrightness() {
		return brightness;
	}

	public void setBrightness(float brightness) {
		this.brightness = brightness;
	}
}
