package com.openglengine.util.light;

import com.openglengine.core.*;
import com.openglengine.eventsystem.defaultevents.*;
import com.openglengine.util.math.*;

public abstract class MovingLightSource extends LightSource {
	public MovingLightSource(Vector3f position, Vector3f color) {
		super(position, color);
		Engine.getGlobalEventManager().registerListenerForEvent(UpdateEvent.class, e -> update());
	}

	public MovingLightSource(Vector3f position, Vector3f color, Vector3f attenuation) {
		super(position, color, attenuation);
		Engine.getGlobalEventManager().registerListenerForEvent(UpdateEvent.class, e -> update());
	}

	protected abstract void update();
}