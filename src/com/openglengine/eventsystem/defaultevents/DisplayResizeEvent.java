package com.openglengine.eventsystem.defaultevents;

/**
 * This event is sent every time the display resizes
 * 
 * @author Dominik
 *
 */
public class DisplayResizeEvent extends BaseEvent {
	/** New screen width */
	private int newScreenWidth;

	/** New screen height */
	private int newScreenHeight;

	public DisplayResizeEvent(int newScreenWidth, int newScreenHeight) {
		super();
		this.newScreenWidth = newScreenWidth;
		this.newScreenHeight = newScreenHeight;
	}

	public int getNewScreenWidth() {
		return newScreenWidth;
	}

	public int getNewScreenHeight() {
		return newScreenHeight;
	}
}
