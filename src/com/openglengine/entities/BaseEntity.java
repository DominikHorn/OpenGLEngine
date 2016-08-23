package com.openglengine.entities;

import com.openglengine.core.*;
import com.openglengine.eventsystem.defaultevents.*;

/**
 * Entity baseclass
 * 
 * @author Dominik
 *
 */
public abstract class BaseEntity {
	public BaseEntity() {
		Engine.EVENT_MANAGER.registerListenerForEvent(UpdateEvent.class, e -> update((UpdateEvent) e));
	}

	protected abstract void update(UpdateEvent e);
}
