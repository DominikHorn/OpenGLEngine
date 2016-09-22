package com.openglengine.core;

import static org.lwjgl.glfw.GLFW.*;

import com.openglengine.eventsystem.*;
import com.openglengine.eventsystem.defaultevents.*;
import com.openglengine.util.*;
import com.openglengine.util.math.*;

/**
 * Input Manager class. This deals with glfw input
 * 
 * @author Dominik
 *
 */
public class InputManager implements ResourceManager {

	/** defined keys that are being tracked */
	//@formatter:off
	public static final int 
			KEY_W = GLFW_KEY_W,
			KEY_A = GLFW_KEY_A,
			KEY_S = GLFW_KEY_S,
			KEY_D = GLFW_KEY_D,
			KEY_Q = GLFW_KEY_Q,
			KEY_E = GLFW_KEY_E,
			KEY_PLUS = GLFW_KEY_RIGHT_BRACKET,	// TODO: this will break on other keyboard layouts
			KEY_MINUS = GLFW_KEY_SLASH,	// TODO: this will break on other keyboard layouts
			KEY_F1 = GLFW_KEY_F1,
			KEY_F2 = GLFW_KEY_F2,
			KEY_F3 = GLFW_KEY_F3,
			KEY_F4 = GLFW_KEY_F4,
			KEY_ESC = GLFW_KEY_ESCAPE,
			KEY_SPACE = GLFW_KEY_SPACE,
			KEY_LEFT_SHIFT = GLFW_KEY_LEFT_SHIFT,
			KEY_LEFT_CTRL = GLFW_KEY_LEFT_CONTROL;
	//@formatter:on
	//@formatter:off
	public static final int 
			BUTTON_LEFT = GLFW_MOUSE_BUTTON_1,
			BUTTON_RIGHT = GLFW_MOUSE_BUTTON_2,
			BUTTON_MIDDLE = GLFW_MOUSE_BUTTON_3;
	//@formatter:on

	/** internal structure holding all the key information */
	private int[] keys = new int[GLFW_KEY_LAST];
	private boolean[] pressedKeys = new boolean[GLFW_KEY_LAST];

	/** internal structure holding all mouse button information */
	private int[] mouseButtons = new int[GLFW_MOUSE_BUTTON_LAST];

	/** scroll information */
	private double scrollX;
	private double scrollY;
	private double lastScrollDeltaX;
	private double lastScrollDeltaY;

	/** mouse stuff */
	private Vector2f previousCursorPos;
	private boolean mouseGrabbed;

	/** Window id */
	private Display gameDisplay;

	/** Multithreading makes this necessary :( */
	private Vector2f accumulatedDelta = new Vector2f();
	private EventListener setCursorPosListener = new EventListener() {
		@Override
		public void eventReceived(BaseEvent event) {
			// Move cursor back to center of screen
			synchronized (gameDisplay) {
				Vector2f screenCenter = new Vector2f(gameDisplay.getWindowWidthInPixels() / 2,
						gameDisplay.getWindowHeightInPixels() / 2);
				accumulatedDelta.addVector(getCursorPosition().subtractVector(screenCenter));

				glfwSetCursorPos(gameDisplay.getWindowID(), screenCenter.x, screenCenter.y);
			}
		}
	};

	/**
	 * Setup input manager
	 */
	public InputManager() {
		this.previousCursorPos = new Vector2f();

		Engine.getGlobalEventManager().registerListenerForEvent(DisplayCreatedEvent.class,
				e -> setupListeners((DisplayCreatedEvent) e));
	}

	/**
	 * Sets the glfw listerner callback methods up
	 */
	private void setupListeners(DisplayCreatedEvent e) {
		this.gameDisplay = e.getSender();

		glfwSetKeyCallback(this.gameDisplay.getWindowID(),
				(window, key, scancode, action, mods) -> this.keyEvent(window, key, scancode, action, mods));
		glfwSetScrollCallback(this.gameDisplay.getWindowID(),
				(window, xoffset, yoffset) -> scrollEvent(window, xoffset, yoffset));
		glfwSetMouseButtonCallback(this.gameDisplay.getWindowID(),
				(window, button, action, mods) -> mouseButtonEvent(window, button, action, mods));
	}

	/**
	 * this method receives key events
	 * 
	 * @param window
	 * @param key
	 * @param scancode
	 * @param action
	 * @param mods
	 */
	private void keyEvent(long window, int key, int scancode, int action, int mods) {
		// TODO: don't ignore mods
		if (key > 0 && key < keys.length) {
			keys[key] = action;

			if (action == GLFW_PRESS)
				pressedKeys[key] = true;
		}
	}

	/**
	 * This method receives scroll event
	 * 
	 * @param window
	 * @param xoffset
	 * @param yoffset
	 */
	private void scrollEvent(long window, double xoffset, double yoffset) {
		this.lastScrollDeltaX = xoffset;
		this.lastScrollDeltaY = yoffset;
		this.scrollX += this.lastScrollDeltaX;
		this.scrollY += this.lastScrollDeltaY;
	}

	/**
	 * This method receives mouse button events
	 * 
	 * @param window
	 * @param button
	 * @param action
	 * @param mods
	 */
	private void mouseButtonEvent(long window, int button, int action, int mods) {
		// TODO: don't ignore mods
		if (button >= 0 && button < mouseButtons.length)
			mouseButtons[button] = action;
	}

	/**
	 * Hides mouse cursor. GetCursorDelta() will do the actual grabbing part (fixing cursor @ center of screen)
	 */
	public void setMouseGrabbed(boolean mouseGrabbed) {
		this.mouseGrabbed = mouseGrabbed;

		synchronized (gameDisplay) {
			if (this.mouseGrabbed == true) {
				glfwSetInputMode(this.gameDisplay.getWindowID(), GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
				Engine.getGlobalEventManager().registerListenerForEvent(MainthreadUpdateEvent.class,
						this.setCursorPosListener);
			} else {
				glfwSetInputMode(this.gameDisplay.getWindowID(), GLFW_CURSOR, GLFW_CURSOR_NORMAL);
				Engine.getGlobalEventManager().deleteListenerForEvent(MainthreadUpdateEvent.class,
						this.setCursorPosListener);
			}
		}
	}

	/**
	 * Convenience method to ask whether a key is being pressed
	 * 
	 * @param key
	 * @return
	 */
	public boolean isKeyDown(int key) {
		return keys[key] != GLFW_RELEASE;
	}

	public boolean wasKeyPressed(int key) {
		boolean result = pressedKeys[key];
		pressedKeys[key] = false;
		return result;
	}

	/**
	 * Convenience method to ask whether a mouse button is down
	 */
	public boolean isMouseButtonDown(int button) {
		return mouseButtons[button] != GLFW_RELEASE;
	}

	/**
	 * convenience method to ask whether or not a key is being repeated
	 * 
	 * @param key
	 * @return
	 */
	public boolean isKeyRepeat(int key) {
		return keys[key] == GLFW_REPEAT;
	}

	/**
	 * Returns the scroll x position
	 * 
	 * @return
	 */
	public double getScrollX() {
		return this.scrollX;
	}

	/**
	 * Returns the scroll y position
	 * 
	 * @return
	 */
	public double getScrollY() {
		return this.scrollY;
	}

	/**
	 * Returns the last scroll events x delta
	 * 
	 * @return
	 */
	public double getLastScrollDeltaX() {
		double returnValue = lastScrollDeltaX;
		lastScrollDeltaX = 0;
		return returnValue;
	}

	/**
	 * Returns the last scroll events y delta
	 * 
	 * @return
	 */
	public double getLastScrollDeltaY() {
		double returnValue = lastScrollDeltaY;
		lastScrollDeltaY = 0;
		return returnValue;
	}

	/**
	 * Retrieves current cursor position
	 * 
	 * @return
	 */
	public Vector2f getCursorPosition() {
		double[] xpos = new double[1];
		double[] ypos = new double[1];
		synchronized (gameDisplay) {
			glfwGetCursorPos(this.gameDisplay.getWindowID(), xpos, ypos);
		}

		return new Vector2f((float) xpos[0], (float) ypos[0]);
	}

	/**
	 * Retrieves the cursor delta since last invocation of this method
	 * 
	 * @return
	 */
	public Vector2f getCursorDelta() {
		Vector2f delta = new Vector2f();
		if (this.mouseGrabbed) {
			delta = accumulatedDelta;
			accumulatedDelta = new Vector2f();
		} else {
			Vector2f newCursorPos = getCursorPosition();
			delta = newCursorPos.getSubtractionResult(this.previousCursorPos);
			this.previousCursorPos = newCursorPos;
		}

		return delta;
	}

	@Override
	public void cleanup() {
		// Nothing to do for now
	}
}
