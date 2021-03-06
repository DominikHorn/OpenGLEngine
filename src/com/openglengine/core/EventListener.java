package com.openglengine.core;

import com.openglengine.events.*;

/**
 * Interface that needs to be implemented in order for a class to receive Events from EventManager. Lambas are
 * recommended instead of a full implementation
 * 
 * @author Dominik
 *
 */
public interface EventListener {
	public void eventReceived(BaseEvent event);
}
