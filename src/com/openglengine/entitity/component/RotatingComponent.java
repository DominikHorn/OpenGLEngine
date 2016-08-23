package com.openglengine.entitity.component;

import com.openglengine.entitity.*;
import com.openglengine.entitity.component.event.*;

public class RotatingComponent extends Component {

	@Override
	public void update(Entity entity) {
		entity.rotY += 0.4f;
	}

	@Override
	public void receiveEvent(ComponentEvent event) {
		// Do nothing
	}

}
