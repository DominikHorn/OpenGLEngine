package com.openglengine.entitity.component.defaultcomponents;

import com.openglengine.core.*;
import com.openglengine.entitity.*;
import com.openglengine.entitity.component.*;
import com.openglengine.eventsystem.defaultevents.*;
import com.openglengine.util.*;
import com.openglengine.util.math.*;

public class CameraComponent extends Component {

	@Override
	public void update(Entity entity) {
		Vector3f position = (Vector3f) entity.getValueProperty(DefaultEntityProperties.PROPERTY_POSITION);
		Vector3f rotation = (Vector3f) entity.getValueProperty(DefaultEntityProperties.PROPERTY_ROTATION);

		TransformationMatrixStack vm = Engine.getViewMatrixStack();
		vm.loadIdentity();
		vm.rotate(rotation.x, 1, 0, 0);
		vm.rotate(rotation.y, 0, 1, 0);
		vm.rotate(rotation.z, 0, 0, 1);
		vm.translate(position.getInvertResult());
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
