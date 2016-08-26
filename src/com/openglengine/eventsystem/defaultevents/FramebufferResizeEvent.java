package com.openglengine.eventsystem.defaultevents;

/**
 * This event is sent when display's framebuffer was resized
 * 
 * @author Dominik
 *
 */
public class FramebufferResizeEvent extends BaseEvent {
	/** new framebuffer width */
	private int newWidth;

	/** new framebuffer height */
	private int newHeight;

	/**
	 * Create a new FramebufferResize event
	 * 
	 * @param newWidth
	 * @param newHeight
	 */
	public FramebufferResizeEvent(int newWidth, int newHeight) {
		super();
		this.newWidth = newWidth;
		this.newHeight = newHeight;
	}

	/**
	 * Retrieve the new framebuffer width
	 * 
	 * @return
	 */
	public int getNewWidth() {
		return newWidth;
	}

	/**
	 * Retrieve the new framebuffer height
	 * 
	 * @return
	 */
	public int getNewHeight() {
		return newHeight;
	}

}
