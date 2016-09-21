package com.openglengine.util.light;

import com.openglengine.util.math.*;

public class SpotLightSource extends MovingLightSource {
	public Vector3f direction;
	public float cutoffAngle;

	private float angleX = 0;
	private int tickCount = 0;

	public SpotLightSource(Vector3f position, Vector3f color, Vector3f direction, float cutoffAngle) {
		this(position, color, new Vector3f(1, 0, 0), direction, cutoffAngle);
	}

	public SpotLightSource(Vector3f position, Vector3f color, Vector3f attenuation, Vector3f direction,
			float cutoffAngle) {
		super(position, color, attenuation);

		this.direction = direction.getNormalizeResult();
		this.cutoffAngle = cutoffAngle;
	}

	@Override
	protected void update() {
		angleX = (float) Math.sin(this.tickCount++ / 200f);
		this.direction = new Vector3f(angleX, -1f, 0f).normalise();
		this.position.x = angleX * 20f;
	}
}
