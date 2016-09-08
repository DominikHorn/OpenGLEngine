package com.openglengine.entitity.component.defaultcomponents;

import com.openglengine.core.*;
import com.openglengine.entitity.*;
import com.openglengine.entitity.component.*;
import com.openglengine.eventsystem.defaultevents.*;
import com.openglengine.util.*;
import com.openglengine.util.math.*;

/**
 * This component implements functionality of a camera that can be used for flying around in an observer state. To be
 * used with a seperate entity
 * 
 * @author Dominik
 *
 */
public class CameraComponentFlyoverObserver extends EntityComponent {
	private static final double ANGLE_OFFSET = 90.0 * Math.PI / 180.0;

	private float cameraTranslationSpeed;
	private float cameraRotationSpeed;

	private Vector3f translationSpeed;

	public CameraComponentFlyoverObserver(float cameraTranslationSpeed, float cameraRotationSpeed) {
		this.cameraTranslationSpeed = cameraTranslationSpeed;
		this.cameraRotationSpeed = cameraRotationSpeed;
		this.translationSpeed = new Vector3f();
	}

	@Override
	public void init(Entity entity) {
		// Initialize view matrix properly
		this.updateViewMatrix(entity);
	}

	@Override
	public void update(Entity entity) {
		// Process Input
		this.processInput(entity);

		// Update the view matrix
		this.updateViewMatrix(entity);
	}

	private void updateViewMatrix(Entity entity) {
		TransformationMatrixStack vm = Engine.getViewMatrixStack();
		vm.loadIdentity();
		vm.rotate(entity.rotation.x, 1, 0, 0);
		vm.rotate(entity.rotation.y, 0, 1, 0);
		vm.rotate(entity.rotation.z, 0, 0, 1);
		vm.translate(entity.position.getInvertResult());
	}

	private void processInput(Entity entity) {
		InputManager input = Engine.getInputManager();
		this.processMouseInput(entity, input);
		this.processKeyboardInput(entity, input);
	}

	private void processKeyboardInput(Entity entity, InputManager input) {
		this.translationSpeed.x = 0;
		this.translationSpeed.y = 0;
		this.translationSpeed.z = 0;

		if (input.isKeyDown(InputManager.KEY_W)) {
			this.translationSpeed.x += (float) Math.sin(entity.rotation.y);
			this.translationSpeed.z -= (float) Math.cos(entity.rotation.y);
		}

		if (input.isKeyDown(InputManager.KEY_S)) {
			this.translationSpeed.x -= (float) (this.cameraTranslationSpeed * Math.sin(entity.rotation.y));
			this.translationSpeed.z += (float) (this.cameraTranslationSpeed * Math.cos(entity.rotation.y));
		}

		if (input.isKeyDown(InputManager.KEY_D)) {
			this.translationSpeed.x += (float) (this.cameraTranslationSpeed
					* Math.sin(entity.rotation.y + ANGLE_OFFSET));
			this.translationSpeed.z -= (float) (this.cameraTranslationSpeed
					* Math.cos(entity.rotation.y + ANGLE_OFFSET));
		}

		if (input.isKeyDown(InputManager.KEY_A)) {
			this.translationSpeed.x += (float) (this.cameraTranslationSpeed
					* Math.sin(entity.rotation.y + 3 * ANGLE_OFFSET));
			this.translationSpeed.z -= (float) (this.cameraTranslationSpeed
					* Math.cos(entity.rotation.y + 3 * ANGLE_OFFSET));
		}

		if (input.isKeyDown(InputManager.KEY_LEFT_SHIFT))
			this.translationSpeed.y -= cameraTranslationSpeed;

		if (input.isKeyDown(InputManager.KEY_SPACE))
			this.translationSpeed.y += cameraTranslationSpeed;

		// Add translation
		entity.position.addVector(this.translationSpeed);
	}

	private void processMouseInput(Entity entity, InputManager input) {
		Vector2f cursorDelta = input.getCursorDelta();
		entity.rotation.x += Math.toRadians(cursorDelta.y) * this.cameraRotationSpeed;
		entity.rotation.y += Math.toRadians(cursorDelta.x) * this.cameraRotationSpeed;
	}

	@Override
	public void receiveEvent(BaseEvent event) {
		// Do nothing
	}

	@Override
	public void cleanup(Entity entity) {
		// No resources -> no cleanup needed
	}
}
