package com.openglengine.core;

import static org.lwjgl.glfw.GLFW.*;

import com.openglengine.eventsystem.defaultevents.*;
import com.openglengine.util.*;

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
			KEY_ESC = GLFW_KEY_ESCAPE,
			KEY_SPACE = GLFW_KEY_SPACE,
			KEY_LEFT_SHIFT = GLFW_KEY_LEFT_SHIFT,
			KEY_LEFT_CTRL = GLFW_KEY_LEFT_CONTROL;
	//@formatter:on

	/** internal structure holding all the key information */
	private int[] keys = new int[GLFW_KEY_LAST];

	/**
	 * Setup input manager
	 */
	public InputManager() {
		Engine.getGlobalEventManager().registerListenerForEvent(DisplayCreatedEvent.class,
				e -> setupListeners((DisplayCreatedEvent) e));
	}

	/**
	 * 
	 */
	private void setupListeners(DisplayCreatedEvent e) {
		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		glfwSetKeyCallback(e.getSender().getWindowID(),
				(window, key, scancode, action, mods) -> this.keyEvent(window, key, scancode, action, mods));
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
		// TODO: investigate mouse input etc
		if (key > 0 && key < keys.length)
			keys[key] = action;
	}

	/**
	 * Convenience method to ask whether a key is being pressed
	 * 
	 * @param glfwkeycode
	 * @return
	 */
	public boolean isKeyDown(int glfwkeycode) {
		return keys[glfwkeycode] != GLFW_RELEASE;
	}

	/**
	 * convenience method to ask whether or not a key is being repeated
	 * 
	 * @param glfwkeycode
	 * @return
	 */
	public boolean isKeyRepeat(int glfwkeycode) {
		return keys[glfwkeycode] == GLFW_REPEAT;
	}

	@Override
	public void cleanup() {
		// Nothing to do for now
	}
}