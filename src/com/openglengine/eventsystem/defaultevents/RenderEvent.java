package com.openglengine.eventsystem.defaultevents;

/**
 * Event sent to notify the BatchRenderingSystem that it may render now
 * 
 * @author Dominik
 *
 */
public class RenderEvent extends UpdateEvent {
	public RenderEvent(double deltatime) {
		super(deltatime);
	}
}
