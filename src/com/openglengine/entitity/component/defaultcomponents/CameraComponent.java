package com.openglengine.entitity.component.defaultcomponents;

import com.openglengine.core.*;
import com.openglengine.entitity.*;
import com.openglengine.entitity.component.*;
import com.openglengine.eventsystem.defaultevents.*;
import com.openglengine.util.*;

public class CameraComponent extends Component {
	@Override
	public void init(Entity entity) {
		// Do nothing
	}

	@Override
	public void update(Entity entity) {
		TransformationMatrixStack vm = Engine.getViewMatrixStack();
		vm.loadIdentity();
		vm.rotate(entity.rotation.x, 1, 0, 0);
		vm.rotate(entity.rotation.y, 0, 1, 0);
		vm.rotate(entity.rotation.z, 0, 0, 1);
		vm.translate(entity.position.getInvertResult());
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
