package com.openglengine.eventsystem;

import java.util.*;

/**
 * Global event manager that allows for thread save message queueing and dispatching
 * 
 * @author Dominik
 *
 */
public class GlobalEventManager extends EventManager<GlobalEvent> {
	private List<GlobalEvent> renderThreadQueue;
	private List<GlobalEvent> mainThreadQueue;

	public GlobalEventManager() {
		super();
		this.renderThreadQueue = new ArrayList<>();
		this.mainThreadQueue = new ArrayList<>();
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
	 * Queues an event for the main thread.
	 * 
	 * @param event
	 */
	public void queuForMainthread(GlobalEvent event) {
		synchronized (this.mainThreadQueue) {
			this.mainThreadQueue.add(event);
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

	/**
	 * Dispatches all queud events immediately on calling thread. Make sure this is only called by main thread
	 */
	public void fetchForMainthread() {
		synchronized (this.mainThreadQueue) {
			this.mainThreadQueue.forEach(event -> this.dispatch(event));
			this.mainThreadQueue.clear();
		}
	}
}
