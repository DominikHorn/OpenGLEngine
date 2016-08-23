package com.openglengine.eventsystem.defaultevents;

/**
 * Global update event, sent 60 times a second
 * 
 * @author Dominik
 *
 */
public class UpdateEvent extends BaseEvent {
	private double deltatime;

	/**
	 * Initialize UpdateEvent
	 * 
	 * @param deltatime
	 */
	public UpdateEvent(double deltatime) {
		this.deltatime = deltatime;
	}

	/**
	 * Deltatime passed since last update
	 * 
	 * @return
	 */
	public double getDeltatime() {
		return this.deltatime;
	}
}
