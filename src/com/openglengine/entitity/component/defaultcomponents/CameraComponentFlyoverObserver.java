package com.openglengine.entitity.component.defaultcomponents;

import com.openglengine.core.*;
import com.openglengine.entitity.*;
import com.openglengine.entitity.component.*;
import com.openglengine.eventsystem.defaultevents.*;
import com.openglengine.util.*;
import com.openglengine.util.math.*;

/**
 * This component implements functionality of a camera that can be used for flying around in an observer state
 * 
 * @author Dominik
 *
 */
public class CameraComponentFlyoverObserver extends EntityComponent {
	private float cameraTranslationSpeed;
	private float cameraRotationSpeed;

	public CameraComponentFlyoverObserver(float cameraTranslationSpeed, float cameraRotationSpeed) {
		this.cameraTranslationSpeed = cameraTranslationSpeed;
		this.cameraRotationSpeed = cameraRotationSpeed;
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
		this.processKeyboardInput(entity, input);
		this.processMouseInput(entity, input);
	}

	private void processKeyboardInput(Entity entity, InputManager input) {
		if (input.isKeyDown(InputManager.KEY_W)) {
			if (input.isKeyRepeat(InputManager.KEY_W))
				entity.position.z -= cameraTranslationSpeed;
			else
				entity.position.z -= cameraTranslationSpeed;
		}

		if (input.isKeyDown(InputManager.KEY_D)) {
			if (input.isKeyRepeat(InputManager.KEY_D))
				entity.position.x += cameraTranslationSpeed;
			else
				entity.position.x += cameraTranslationSpeed;
		}

		if (input.isKeyDown(InputManager.KEY_A)) {
			if (input.isKeyRepeat(InputManager.KEY_A))
				entity.position.x -= cameraTranslationSpeed;
			else
				entity.position.x -= cameraTranslationSpeed;
		}

		if (input.isKeyDown(InputManager.KEY_S)) {
			if (input.isKeyRepeat(InputManager.KEY_S))
				entity.position.z += cameraTranslationSpeed;
			else
				entity.position.z += cameraTranslationSpeed;
		}

		if (input.isKeyDown(InputManager.KEY_LEFT_SHIFT)) {
			if (input.isKeyRepeat(InputManager.KEY_LEFT_SHIFT))
				entity.position.y -= cameraTranslationSpeed;
			else
				entity.position.y -= cameraTranslationSpeed;
		}

		if (input.isKeyDown(InputManager.KEY_SPACE)) {
			if (input.isKeyRepeat(InputManager.KEY_SPACE))
				entity.position.y += cameraTranslationSpeed;
			else
				entity.position.y += cameraTranslationSpeed;
		}
	}

	private void processMouseInput(Entity entity, InputManager input) {
		Vector2f cursorDelta = input.getCursorDelta();
		System.out.println("Moving by: " + cursorDelta);
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
