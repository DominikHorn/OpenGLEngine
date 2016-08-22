package com.openglengine.util;

import com.openglengine.core.*;
import com.openglengine.eventsystem.defaultevents.*;
import com.openglengine.util.math.*;

public class Camera {
	private Vector3f position = new Vector3f(0, 0, 0);
	private float pitch;
	private float yaw;
	private float roll;

	public Camera() {
		Engine.EVENT_MANAGER.registerListenerForEvent(UpdateEvent.class, e -> update((UpdateEvent) e));
	}

	public void update(UpdateEvent e) {
		InputManager input = Engine.INPUT_MANAGER;

		if (input.isKeyDown(InputManager.KEY_W)) {
			position.z -= 0.02f;
		}

		if (input.isKeyDown(InputManager.KEY_D)) {
			position.x += 0.02f;
		}

		if (input.isKeyDown(InputManager.KEY_A)) {
			position.x -= 0.02f;
		}

		if (input.isKeyDown(InputManager.KEY_S)) {
			position.z += 0.02f;
		}

		applyCameraTransforms();
	}

	public void applyCameraTransforms() {
		TransformMatrixStack vm = Engine.VIEW_MATRIX_STACK;
		vm.loadIdentity();
		vm.rotate(this.pitch, 1, 0, 0);
		vm.rotate(this.yaw, 0, 1, 0);
		vm.rotate(this.roll, 0, 0, 1);
		vm.translate(position.getInvertResult());
	}
}
