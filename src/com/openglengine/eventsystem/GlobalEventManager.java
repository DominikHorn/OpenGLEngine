package com.openglengine.eventsystem;

import java.util.*;

import com.openglengine.eventsystem.defaultevents.*;
import com.openglengine.util.*;

/**
 * Manager for dispatching and receiving events
 * 
 * @author Dominik
 *
 */
public class GlobalEventManager implements ResourceManager {
	/** listeners data this class operates on */
	private Map<Class<? extends BaseEvent>, List<GlobalEventListener>> listeners;

	/** queue for the render thread */
	private List<BaseEvent> renderthreadQueue;

	public GlobalEventManager() {
		this.listeners = new HashMap<>();
		this.renderthreadQueue = new ArrayList<>();
	}

	/** actual class implementations */

	/**
	 * Immediatelly dispatch an event on this thread and wait until all listeners have received that event
	 * 
	 * @param event
	 */
	public void dispatch(BaseEvent event) {
		// Dispatch this event to all listeners
		listeners.get(event.getClass()).forEach(listener -> listener.eventReceived(event));
	}

	/**
	 * Queues an event for the renderthread. the renderthread will call fetchForRenderthread() one each frame and
	 * retrieve events that way. This is necessary to deal with events that must be executed on the render thread
	 * 
	 * @param event
	 */
	// TODO: search for better way
	public void queueForRenderthread(BaseEvent event) {
		synchronized (this.renderthreadQueue) {
			this.renderthreadQueue.add(event);
		}
	}

	/**
	 * Dispatches all queued events immediatelly on the calling thread. Make sure this is only called by the
	 * renderthread
	 */
	public void fetchForRenderthread() {
		synchronized (this.renderthreadQueue) {
			this.renderthreadQueue.forEach(event -> this.dispatch(event));
			this.renderthreadQueue.clear();
		}
	}

	/**
	 * Register a new Listener for a certain event
	 * 
	 * @param listener
	 * @param eventClass
	 * @return true if the listener was added, false if it previously had been registered already and thus was not added
	 *         a second time successfully
	 */
	public boolean registerListenerForEvent(Class<? extends BaseEvent> eventClass, GlobalEventListener listener) {
		synchronized (this.listeners) {
			// Extract previous values
			List<GlobalEventListener> eventListenerList = this.listeners.get(eventClass);
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
	}

	/**
	 * Deletes listener for event
	 * 
	 * @param listener
	 * @param eventClass
	 * @return whether or not deletion was successful. Note: this can be unsuccessful if the listener was not actually
	 *         registered previously
	 */
	public synchronized boolean deleteListenerForEvent(GlobalEventListener listener,
			Class<? extends BaseEvent> eventClass) {
		synchronized (this.listeners) {
			List<GlobalEventListener> eventListenerList = this.listeners.get(eventClass);

			if (eventListenerList != null && eventListenerList.contains(listener)) {
				eventListenerList.remove(listener);
				return true;
			}

			return false;
		}
	}

	@Override
	public void cleanup() {
		this.listeners = null;
	}
}
