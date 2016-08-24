package com.openglengine.eventsystem.defaultevents;

import com.openglengine.core.*;

/**
 * This event is sent every time the display resizes
 * 
 * @author Dominik
 *
 */
public class DisplayResizeEvent extends DisplayEvent {
	public DisplayResizeEvent(Display sender) {
		super(sender);
	}
}
