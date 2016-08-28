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
	public Vector3f position;
	public Vector3f color;
	public float brightness;

	public LightSource(Vector3f position, Vector3f color, float brightness) {
		super();
		this.position = position;
		this.color = color;
		this.brightness = brightness;
	}
}
