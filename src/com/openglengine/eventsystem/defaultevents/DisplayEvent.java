package com.openglengine.eventsystem.defaultevents;

import com.openglengine.core.*;

/**
 * 
 * All display events inherit from this event
 * 
 * @author Dominik
 *
 */
public abstract class DisplayEvent extends BaseEvent {
	private Display sender;

	// TODO: refactor to only convey necessary information, not the whole display
	public DisplayEvent(Display sender) {
		this.sender = sender;
	}

	public Display getSender() {
		return sender;
	}
}
