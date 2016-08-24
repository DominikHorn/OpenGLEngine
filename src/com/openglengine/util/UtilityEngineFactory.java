package com.openglengine.util;

import com.openglengine.entitity.*;
import com.openglengine.entitity.component.*;
import com.openglengine.util.math.*;

public class UtilityEngineFactory {
	public static Entity getCamera(Vector3f position, float rotX, float rotY, float rotZ) {
		return new Entity(position, rotX, rotY, rotZ).addComponent(new CameraInputComponent())
				.addComponent(new CameraComponent());
	}
}
