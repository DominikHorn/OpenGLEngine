package com.openglengine.util;

import static org.lwjgl.glfw.GLFW.*;

import com.openglengine.core.*;

public class InputManager extends Manager {

	//@formatter:off
	public static final int 
			KEY_W = GLFW_KEY_W,
			KEY_A = GLFW_KEY_A,
			KEY_S = GLFW_KEY_S,
			KEY_D = GLFW_KEY_D,
			KEY_ESC = GLFW_KEY_ESCAPE;
	//@formatter:on

	private int[] keys = new int[GLFW_KEY_LAST];

	public InputManager() {
		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		glfwSetKeyCallback(Engine.GLFW_MANAGER.getWindowID(),
				(window, key, scancode, action, mods) -> this.keyEvent(window, key, scancode, action, mods));
	}

	private void keyEvent(long window, int key, int scancode, int action, int mods) {
		// TODO: investigate mouse input etc
		if (key > 0 && key < keys.length)
			keys[key] = action;
	}

	public boolean isKeyDown(int glfwkeycode) {
		return keys[glfwkeycode] != GLFW_RELEASE;
	}

	public boolean isKeyRepeat(int glfwkeycode) {
		return keys[glfwkeycode] == GLFW_REPEAT;
	}

	@Override
	public void cleanup() {
		// Nothing to do for now
	}
}
