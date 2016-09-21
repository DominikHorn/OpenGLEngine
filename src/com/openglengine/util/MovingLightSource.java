package com.openglengine.util;

import com.openglengine.core.*;
import com.openglengine.eventsystem.defaultevents.*;
import com.openglengine.util.math.*;

public abstract class MovingLightSource extends LightSource {
	public MovingLightSource(Vector3f position, Vector3f color, float brightness) {
		super(position, color, brightness);
		
		Engine.getGlobalEventManager().registerListenerForEvent(UpdateEvent.class,
				e -> update());
	}

	protected abstract void update();
}