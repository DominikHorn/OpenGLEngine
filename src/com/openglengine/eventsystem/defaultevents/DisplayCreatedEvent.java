package com.openglengine.eventsystem.defaultevents;

import com.openglengine.core.*;
import com.openglengine.eventsystem.*;

/**
 * This event is sent when a new Display was successfully created
 * 
 * @author Dominik
 *
 */
public class DisplayCreatedEvent extends GlobalEvent {
	// TODO: refactor
	private Display sender;

	public DisplayCreatedEvent(Display sender) {
		this.sender = sender;
	}

	public Display getSender() {
		return this.sender;
	}
}
