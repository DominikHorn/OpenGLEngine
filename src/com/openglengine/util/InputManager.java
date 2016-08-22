package com.openglengine.util;

import static org.lwjgl.glfw.GLFW.*;

import com.openglengine.eventsystem.*;
import com.openglengine.eventsystem.defaultevents.*;

public class InputManager {

	//@formatter:off
	public static final int 
			KEY_W = GLFW_KEY_W,
			KEY_A = GLFW_KEY_A,
			KEY_S = GLFW_KEY_S,
			KEY_D = GLFW_KEY_D;
	//@formatter:on
	
	private int[] keys = new int[GLFW_KEY_LAST];

	public InputManager() {
		EventManager.registerListenerForEvent(GlfwKeyInputEvent.class, e -> keyEvent((GlfwKeyInputEvent) e));
	}

	private void keyEvent(GlfwKeyInputEvent e) {
		keys[e.getKey()] = e.getAction();
	}

	public boolean isKeyDown(int glfwkeycode) {
		return keys[glfwkeycode] != GLFW_RELEASE;
	}

	public boolean isKeyRepeat(int glfwkeycode) {
		return keys[glfwkeycode] == GLFW_REPEAT;
	}
}
