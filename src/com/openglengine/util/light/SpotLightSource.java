package com.openglengine.util.light;

import com.openglengine.util.math.*;

public class SpotLightSource extends LightSource {
	public Vector3f direction;
	public float cutoffAngle;

	public SpotLightSource(Vector3f position, Vector3f color, Vector3f direction, float cutoffAngle) {
		this(position, color, new Vector3f(1, 0, 0), direction, cutoffAngle);
	}

	public SpotLightSource(Vector3f position, Vector3f color, Vector3f attenuation, Vector3f direction,
			float cutoffAngle) {
		super(position, color, attenuation);

		this.direction = direction.getNormalizeResult();
		this.cutoffAngle = cutoffAngle;
	}
}
