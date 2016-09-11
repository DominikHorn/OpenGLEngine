package com.openglengine.util;

import com.openglengine.core.*;
import com.openglengine.eventsystem.defaultevents.*;
import com.openglengine.util.math.*;

public class MovingLightSource extends LightSource {
	private long tickCount;

	public MovingLightSource(UpdateCallback callback, Vector3f position, Vector3f color, float brightness) {
		super(position, color, brightness);
		this.tickCount = 0;
		
		Engine.getGlobalEventManager().registerListenerForEvent(UpdateEvent.class,
				e -> callback.update(this, this.tickCount++));
	}

	public interface UpdateCallback {
		public void update(MovingLightSource source, long tickCount);
	}
}