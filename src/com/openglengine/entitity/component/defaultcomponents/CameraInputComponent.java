package com.openglengine.entitity.component.defaultcomponents;

import com.openglengine.core.*;
import com.openglengine.entitity.*;
import com.openglengine.entitity.component.*;
import com.openglengine.eventsystem.defaultevents.*;

public class CameraInputComponent extends Component {

	private float cameraSpeed;

	public CameraInputComponent(int cameraSpeed) {
		this.cameraSpeed = cameraSpeed;
	}

	@Override
	public void init(Entity entity) {
		// Do nothing
	}

	@Override
	public void update(Entity entity) {
		InputManager input = Engine.getInputManager();

		if (input.isKeyDown(InputManager.KEY_W)) {
			entity.position.z -= cameraSpeed;
		}

		if (input.isKeyDown(InputManager.KEY_D)) {
			entity.position.x += cameraSpeed;
		}

		if (input.isKeyDown(InputManager.KEY_A)) {
			entity.position.x -= cameraSpeed;
		}

		if (input.isKeyDown(InputManager.KEY_S)) {
			entity.position.z += cameraSpeed;
		}

		if (input.isKeyDown(InputManager.KEY_LEFT_SHIFT)) {
			entity.position.y -= cameraSpeed;
		}

		if (input.isKeyDown(InputManager.KEY_SPACE)) {
			entity.position.y += cameraSpeed;
		}
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
