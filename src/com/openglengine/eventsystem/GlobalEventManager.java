package com.openglengine.eventsystem;

import java.util.*;

/**
 * Global event manager that allows for thread save message queueing and dispatching
 * 
 * @author Dominik
 *
 */
// TODO: search for better way to handle this
public class GlobalEventManager extends EventManager<GlobalEvent> {
	private List<GlobalEvent> renderThreadQueue;

	public GlobalEventManager() {
		super();
		this.renderThreadQueue = new ArrayList<>();
	}

	/**
	 * Queues an event for the renderthread. the renderthread will call fetchForRenderthread() one each frame and
	 * retrieve events that way. This is necessary to deal with events that must be executed on the render thread
	 * 
	 * @param event
	 */
	public void queueForRenderthread(GlobalEvent event) {
		synchronized (this.renderThreadQueue) {
			this.renderThreadQueue.add(event);
		}
	}

	/**
	 * Dispatches all queued events immediately on the calling thread. Make sure this is only called by the renderthread
	 */
	public void fetchForRenderthread() {
		synchronized (this.renderThreadQueue) {
			this.renderThreadQueue.forEach(event -> this.dispatch(event));
			this.renderThreadQueue.clear();
		}
	}
}
