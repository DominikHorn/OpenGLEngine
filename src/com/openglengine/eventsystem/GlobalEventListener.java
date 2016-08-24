package com.openglengine.eventsystem;

import com.openglengine.eventsystem.defaultevents.*;

/**
 * Interface that needs to be implemented in order for a class to receive Events from EventManager. Lambas are
 * recommended instead of a full implementation
 * 
 * @author Dominik
 *
 */
public interface GlobalEventListener {

	/**
	 * Implementing classes will receive any event they registered for as a listener in this method. Either implement a
	 * unique event handler for every event using lamdas or do some typecasting voodo
	 * 
	 * @param event
	 */
	public void eventReceived(BaseEvent event);
}
