package com.openglengine.entitity.component.defaultcomponents;

import com.openglengine.core.*;
import com.openglengine.entitity.*;
import com.openglengine.entitity.component.*;
import com.openglengine.util.*;
import com.openglengine.util.math.*;

/**
 * This component implements functionality of a camera that can be used for flying around in an observer state. To be
 * used with a seperate entity
 * 
 * @author Dominik
 *
 */
public class CameraComponentFirstPerson implements RenderableEntityComponent {
	private static float yawOffset = (float) Math.toRadians(180);

	private Vector3f position;
	private Vector3f rotation;

	private float cameraRotationSpeed;
	private float cameraPitchMin;
	private float cameraPitchMax;

	public CameraComponentFirstPerson(float cameraRotationSpeed, float cameraPitchMin, float cameraPitchMax) {
		this.cameraRotationSpeed = cameraRotationSpeed;
		this.position = new Vector3f();
		this.rotation = new Vector3f();
		this.cameraPitchMax = cameraPitchMax;
		this.cameraPitchMin = cameraPitchMin;
	}

	@Override
	public void init(RenderableEntity<?> entity) {
		// Initialize view matrix properly
		this.updateViewMatrix(entity);
	}

	@Override
	public void update(RenderableEntity<?> entity) {
		// Process Input
		this.processInput(entity);

		// Update entity rotation y
		entity.rotation.y = -(this.rotation.y - yawOffset);

		// Take entity translation into account
		Vector3f lookat = MathUtils.getLookatVector(entity.rotation);
		this.position.x = entity.position.x + lookat.x * 2f;
		this.position.y = entity.position.y + 10f;
		this.position.z = entity.position.z + lookat.z * 2f;

		// Update the view matrix
		this.updateViewMatrix(entity);
	}

	private void updateViewMatrix(RenderableEntity<?> entity) {
		TransformationMatrixStack vm = Engine.getViewMatrixStack();
		vm.loadIdentity();
		vm.rotate(this.rotation.x, 1, 0, 0);
		vm.rotate(this.rotation.y, 0, 1, 0); // Take entity y rotation into account
		vm.rotate(this.rotation.z, 0, 0, 1);
		vm.translate(this.position.getInvertResult());
	}

	private void processInput(RenderableEntity<?> entity) {
		InputManager input = Engine.getInputManager();
		this.processMouseInput(entity, input);
	}

	private void processMouseInput(RenderableEntity<?> entity, InputManager input) {
		Vector2f cursorDelta = input.getCursorDelta();
		this.rotation.x += Math.toRadians(cursorDelta.y) * this.cameraRotationSpeed;
		this.rotation.y += Math.toRadians(cursorDelta.x) * this.cameraRotationSpeed;

		// Clamp x rotation to some boundaries
		this.rotation.x = MathUtils.clamp(this.rotation.x, this.cameraPitchMin, this.cameraPitchMax);
	}

	@Override
	public void cleanup() {
		// No resources -> no cleanup needed
	}
}
