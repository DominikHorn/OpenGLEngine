package com.openglengine.eventsystem.defaultevents;

import com.openglengine.core.*;

/**
 * This event is sent when a new Display was successfully created
 * 
 * @author Dominik
 *
 */
public class DisplayCreatedEvent extends DisplayEvent {

	public DisplayCreatedEvent(Display sender) {
		super(sender);
	}

}
