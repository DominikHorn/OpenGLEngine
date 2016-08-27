package com.openglengine.entitity.component.defaultcomponents;

import com.openglengine.core.*;
import com.openglengine.entitity.*;
import com.openglengine.entitity.component.*;
import com.openglengine.eventsystem.defaultevents.*;
import com.openglengine.util.math.*;

public class CameraInputComponent extends Component {

	private float cameraSpeed;

	public CameraInputComponent(int cameraSpeed) {
		this.cameraSpeed = cameraSpeed;
	}

	@Override
	public void update(Entity entity) {
		InputManager input = Engine.getInputManager();
		Vector3f position = (Vector3f) entity.getValueProperty(DefaultEntityProperties.PROPERTY_POSITION);

		if (input.isKeyDown(InputManager.KEY_W)) {
			position.z -= cameraSpeed;
		}

		if (input.isKeyDown(InputManager.KEY_D)) {
			position.x += cameraSpeed;
		}

		if (input.isKeyDown(InputManager.KEY_A)) {
			position.x -= cameraSpeed;
		}

		if (input.isKeyDown(InputManager.KEY_S)) {
			position.z += cameraSpeed;
		}

		if (input.isKeyDown(InputManager.KEY_LEFT_SHIFT)) {
			position.y -= cameraSpeed;
		}

		if (input.isKeyDown(InputManager.KEY_SPACE)) {
			position.y += cameraSpeed;
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
