package com.openglengine.events;

/**
 * Base Event class from which all events sent through EventManager must be derived
 * 
 * NOTE: each event will have a unique identifier (UID) which makes it possible to track events through the event system
 * or record event playback data
 * 
 * @author Dominik
 *
 */
public abstract class BaseEvent {
	private static long globalEventID = -1;
	private long eventUID;

	public long getEventUID() {
		if (this.eventUID == -1)
			this.eventUID = globalEventID++;
		return this.eventUID;
	}
}
