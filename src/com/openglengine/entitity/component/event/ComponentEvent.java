package com.openglengine.entitity.component.event;

public class ComponentEvent {
	private static int globalID = 0;
	private int eventID;

	public ComponentEvent() {
		this.eventID = globalID++;
	}

	public int getEventID() {
		return this.eventID;
	}
}
