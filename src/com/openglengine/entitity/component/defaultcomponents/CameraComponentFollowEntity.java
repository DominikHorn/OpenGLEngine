package com.openglengine.entitity.component.defaultcomponents;

import com.openglengine.core.*;
import com.openglengine.entitity.*;
import com.openglengine.entitity.component.*;
import com.openglengine.util.*;
import com.openglengine.util.math.*;

/**
 * Component that will follow a specific entity. To be used with the entity that is meant to be tracked
 * 
 * @author Dominik
 *
 */
public class CameraComponentFollowEntity extends EntityComponent {
	private float distanceFromPlayer = 20;
	private float angleAroundTrackedEntity = 0;
	private float scroll = 0;
	private float pitch = 0.4f;

	private Vector3f cameraPosition;
	private Vector3f cameraRotation;

	private float maxDistance = -20;
	private float minDistance = 45;
	private float minPitch = 0.1f;
	private float maxPitch = 1.5f;

	@Override
	public void init(Entity entity) {
		this.cameraPosition = new Vector3f();
		this.cameraRotation = new Vector3f(0.35f, 3.14f, 0f);

		this.updateViewMatrix(entity);

	}

	@Override
	public void update(Entity entity) {
		this.processInput();

		this.updateViewMatrix(entity);
	}

	private void processInput() {
		InputManager input = Engine.getInputManager();

		// Get cursor delta once as this will reset
		Vector2f cursorDelta = input.getCursorDelta();

		// Calculate the zoom (distance from player) TODO: limit zooming to some bounds
		scroll += input.getLastScrollDeltaY() * 2.5;
		scroll = MathUtils.clamp(scroll, this.maxDistance, this.minDistance);
		this.distanceFromPlayer = 50 - scroll;

		// Calculate pitch and angle around tracked entity offset
		if (input.isMouseButtonDown(InputManager.BUTTON_LEFT)) {
			this.angleAroundTrackedEntity -= Math.toRadians(cursorDelta.x * 4f);

			pitch += (float) Math.toRadians(cursorDelta.y * 0.2f);
			pitch = MathUtils.clamp(pitch, minPitch, maxPitch);
		}
		this.cameraRotation.x = pitch;
	}

	private void updateViewMatrix(Entity entity) {
		// Calculate stuff
		float horizontalDistance = (float) (this.distanceFromPlayer * Math.cos(this.cameraRotation.x));
		float verticalDistance = (float) (this.distanceFromPlayer * Math.sin(this.cameraRotation.x));
		this.calculateCameraPosition(entity, horizontalDistance, verticalDistance);

		// Apply to view matrix
		TransformationMatrixStack vm = Engine.getViewMatrixStack();
		vm.loadIdentity();
		vm.rotate(this.cameraRotation.x, 1, 0, 0);
		vm.rotate(this.cameraRotation.y, 0, 1, 0);
		vm.rotate(this.cameraRotation.z, 0, 0, 1);
		vm.translate(this.cameraPosition.getInvertResult());
	}

	private void calculateCameraPosition(Entity entity, float horizontalDistance, float verticalDistance) {
		float theta = entity.rotation.y + (float) Math.toRadians(this.angleAroundTrackedEntity);
		float offsetX = (float) (horizontalDistance * Math.sin(theta));
		float offsetZ = (float) (horizontalDistance * Math.cos(theta));
		this.cameraRotation.y = (float) Math.PI - theta;

		this.cameraPosition.x = entity.position.x - offsetX;
		this.cameraPosition.z = entity.position.z - offsetZ;
		this.cameraPosition.y = entity.position.y + verticalDistance;
	}

	@Override
	public void cleanup(Entity entity) {
		// Do nothing
	}

}
