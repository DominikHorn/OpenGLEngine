package com.openglengine.util.light;

import com.openglengine.util.math.*;

/**
 * Container class representing a light source
 * 
 * @author Dominik
 *
 */
public class LightSource {
	public Vector3f position;
	public Vector3f color;
	public Vector3f attenuation;

	public LightSource(Vector3f position, Vector3f color) {
		this(position, color, new Vector3f(1, 0, 0));
	}

	public LightSource(Vector3f position, Vector3f color, Vector3f attenuation) {
		this.position = position;
		this.color = color;
		this.attenuation = attenuation;
	}

	@Override
	public String toString() {
		return "LightSource: pos" + position + ", color" + color + ", attenuation" + attenuation;
	}
}
