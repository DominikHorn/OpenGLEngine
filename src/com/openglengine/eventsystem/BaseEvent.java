package com.openglengine.eventsystem;

/**
 * Base Event class from which all events sent through an EventManager must be derived
 * 
 * NOTE: each event will have a unique identifier (UID) which makes it possible to track events through the event system
 * or record event playback data
 * 
 * @author Dominik
 *
 */
public abstract class BaseEvent {

	/** each component event has a globally unique id, this is used to generate that */
	private static long globalID = 0;

	/** globally unique id */
	private long eventUID = -1;

	/**
	 * Initialize this event. NOTE: this must be called by subclasses. Otherwise the global UID feature will not work!
	 */
	public BaseEvent() {
		this.eventUID = globalID++;
	}

	/**
	 * Retrieve the globally unique ID of this event
	 * 
	 * @return
	 */
	public long getEventID() {
		return this.eventUID;
	}
}
