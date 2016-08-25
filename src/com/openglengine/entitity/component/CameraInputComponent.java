package com.openglengine.entitity.component;

import com.openglengine.core.*;
import com.openglengine.entitity.*;
import com.openglengine.eventsystem.defaultevents.*;

public class CameraInputComponent extends Component {

	private static final float CAMERA_SPEED = 0.2f;

	@Override
	public void update(Entity entity) {
		InputManager input = Engine.getInputManager();

		if (input.isKeyDown(InputManager.KEY_W)) {
			entity.position.z -= CAMERA_SPEED;
		}

		if (input.isKeyDown(InputManager.KEY_D)) {
			entity.position.x += CAMERA_SPEED;
		}

		if (input.isKeyDown(InputManager.KEY_A)) {
			entity.position.x -= CAMERA_SPEED;
		}

		if (input.isKeyDown(InputManager.KEY_S)) {
			entity.position.z += CAMERA_SPEED;
		}

		if (input.isKeyDown(InputManager.KEY_LEFT_SHIFT)) {
			entity.position.y -= CAMERA_SPEED;
		}

		if (input.isKeyDown(InputManager.KEY_SPACE)) {
			entity.position.y += CAMERA_SPEED;
		}
	}

	@Override
	public void receiveEvent(BaseEvent event) {
		// Do nothing
	}

	@Override
	public void cleanup() {
		// No resources -> no cleanup needed
	}

}
