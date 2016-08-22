package com.openglengine.entities;

import com.openglengine.eventsystem.*;
import com.openglengine.eventsystem.defaultevents.*;
import com.openglengine.util.math.*;

public class Camera {
	private Vector3f position = new Vector3f(0, 0, 0);
	private float pitch;
	private float yaw;
	private float roll;

	public Camera() {
		EventManager.registerListenerForEvent(UpdateEvent.class, e -> update((UpdateEvent) e));
	}

	public void update(UpdateEvent e) {

	}

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}

}
