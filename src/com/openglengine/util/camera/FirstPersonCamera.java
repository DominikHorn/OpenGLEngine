package com.openglengine.util.camera;

import com.openglengine.core.*;
import com.openglengine.entitity.*;
import com.openglengine.util.math.*;

/**
 * This camera will follow an entity in first person mode
 * 
 * @author Dominik
 *
 */
public class FirstPersonCamera extends Camera {
	private static float yawOffset = (float) Math.toRadians(180);

	private float cameraRotationSpeed;
	private float cameraPitchMin;
	private float cameraPitchMax;

	public FirstPersonCamera(float cameraRotationSpeed, float cameraPitchMin, float cameraPitchMax) {
		this.cameraRotationSpeed = cameraRotationSpeed;
		this.cameraPitchMin = cameraPitchMin;
		this.cameraPitchMax = cameraPitchMax;
	}

	public void update(RenderableEntity<?> entity) {
		// Process Input
		this.processInput(entity);

		// Update entity rotation y
		entity.rotation.y = -(this.yaw - yawOffset);

		// Take entity translation into account
		Vector3f lookat = MathUtils.createLookatVector(entity.rotation);
		this.position.x = entity.position.x + lookat.x * 3f;
		this.position.y = entity.position.y + 10f;
		this.position.z = entity.position.z + lookat.z * 3f;

		// Update view matrix
		this.update();
	}

	private void processInput(RenderableEntity<?> entity) {
		InputManager input = Engine.getInputManager();
		Vector2f cursorDelta = input.getCursorDelta();
		this.pitch += Math.toRadians(cursorDelta.y) * this.cameraRotationSpeed;
		this.yaw += Math.toRadians(cursorDelta.x) * this.cameraRotationSpeed;

		// Clamp x rotation to some boundaries
		this.pitch = MathUtils.clamp(this.pitch, this.cameraPitchMin, this.cameraPitchMax);
	}
}
