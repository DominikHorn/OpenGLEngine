package com.openglengine.renderer;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.lwjgl.glfw.*;

import com.openglengine.eventsystem.*;
import com.openglengine.eventsystem.defaultevents.*;

/**
 * Manager for all the glfw code
 * 
 * @author Dominik
 *
 */
public class GlfwManager {
	// glfw window handle
	private long window;

	public GlfwManager(int screenWidth, int screenHeight, boolean fullscreen, String windowTitle) {
		if (fullscreen) {
			System.err.println("Fullscreen not supported for now");
			System.exit(-1);
		}

		this.init(screenWidth, screenHeight, fullscreen, windowTitle);
	}

	private void init(int screenWidth, int screenHeight, boolean fullscreen, String windowTitle) {
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if (!glfwInit()) throw new IllegalStateException("Unable to initialize GLFW");

		// Configure our window
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

		// Create the window
		window = glfwCreateWindow(screenWidth, screenHeight, windowTitle, NULL, NULL);
		if (window == NULL) throw new RuntimeException("Failed to create the GLFW window");

		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		glfwSetKeyCallback(window,
				(window, key, scancode, action, mods) -> EventManager
						.dispatch(new GlfwKeyInputEvent(window, key, scancode, action, mods)));

		// Get the resolution of the primary monitor
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

		// Center our window
		glfwSetWindowPos(window, (vidmode.width() - screenWidth) / 2, (vidmode.height() - screenHeight) / 2);

		// Make the OpenGL context current
		glfwMakeContextCurrent(window);

		// Enable vsync TODO: refactor
		glfwSwapInterval(1);

		// Make the window visible
		glfwShowWindow(window);
	}

	public boolean getWindowShouldClose() {
		return glfwWindowShouldClose(this.window);
	}

	public void swapBuffers() {
		glfwSwapBuffers(window);
	}

	public void pollEvents() {
		glfwPollEvents();
	}

	public void cleanup() {
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}
}
