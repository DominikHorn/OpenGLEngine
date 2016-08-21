package com.openglengine.eventsystem;

import java.util.*;

import com.openglengine.eventsystem.*;
import com.openglengine.eventsystem.events.*;

/**
 * Manager for dispatching and receiving events
 * 
 * @author Dominik
 *
 */
public class EventManager {

	private static EventManager eventManager;

	private Map<Class<? extends BaseEvent>, List<EventListener>> listeners;

	private EventManager() {
		this.listeners = new HashMap<>();
	}

	public static EventManager getInstance() {
		if (eventManager == null)
			eventManager = new EventManager();

		return eventManager;
	}

	public static void dispatch(BaseEvent event) {
		getInstance()._dispatch(event);
	}

	public static boolean registerListenerForEvent(EventListener listener, Class<? extends BaseEvent> eventClass) {
		return getInstance()._registerListenerForEvent(listener, eventClass);
	}

	public static boolean deleteListenerForEvent(EventListener listener, Class<? extends BaseEvent> eventClass) {
		return getInstance()._deleteListenerForEvent(listener, eventClass);
	}

	/** actual class implementations */

	private void _dispatch(BaseEvent event) {
		// Dispatch this event to all listeners
		listeners.get(event.getClass()).forEach(listener -> listener.eventReceived(event));
	}

	/**
	 * Register a new Listener for a certain event
	 * 
	 * @param listener
	 * @param eventClass
	 * @return true if the listener was added, false if it previously had been registered already and thus was not added
	 *         a second time successfully
	 */
	private boolean _registerListenerForEvent(EventListener listener, Class<? extends BaseEvent> eventClass) {
		// Extract previous values
		List<EventListener> eventListenerList = this.listeners.get(eventClass);
		boolean alreadyRegistered = false;

		// Make sure we actually have an array list, and if we do make sure we don't duplicate listeners
		if (eventListenerList == null)
			eventListenerList = new ArrayList<>();
		else
			alreadyRegistered = eventListenerList.contains(listener);

		// Add listener
		if (!alreadyRegistered)
			eventListenerList.add(listener);

		// Readd to map
		this.listeners.put(eventClass, eventListenerList);

		return !alreadyRegistered;
	}

	/**
	 * Deletes listener for event
	 * 
	 * @param listener
	 * @param eventClass
	 * @return whether or not deletion was successful. Note: this can be unsuccessful if the listener was not actually
	 *         registered previously
	 */
	private boolean _deleteListenerForEvent(EventListener listener, Class<? extends BaseEvent> eventClass) {
		List<EventListener> eventListenerList = this.listeners.get(eventClass);

		if (eventListenerList != null && eventListenerList.contains(listener)) {
			eventListenerList.remove(listener);
			return true;
		}

		return false;
	}
}
