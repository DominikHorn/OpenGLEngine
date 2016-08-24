package com.openglengine.util;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import com.openglengine.core.*;
import com.openglengine.eventsystem.defaultevents.*;

/**
 * Manager for all the glfw code
 * 
 * @author Dominik
 *
 */
public class GlfwManager extends Manager {
	// glfw window handle
	private long windowID;

	public GlfwManager(int screenWidth, int screenHeight, boolean fullscreen, String windowTitle) {
		if (fullscreen == true)
			Engine.LOGGER.err("Fullscreen not supported atm");

		this.init(screenWidth, screenHeight, fullscreen, windowTitle);

		Engine.EVENT_MANAGER.registerListenerForEvent(UpdateEvent.class, e -> glfwPollEvents());
	}

	/**
	 * Initialize our window with these Parameters
	 * 
	 * @param screenWidth
	 * @param screenHeight
	 * @param fullscreen
	 * @param windowTitle
	 */
	private void init(int screenWidth, int screenHeight, boolean fullscreen, String windowTitle) {
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");

		// Configure our window
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

		// Create the window
		if (fullscreen)
			windowID = glfwCreateWindow(screenWidth, screenHeight, windowTitle, glfwGetPrimaryMonitor(), NULL);
		else
			windowID = glfwCreateWindow(screenWidth, screenHeight, windowTitle, NULL, NULL);

		if (windowID == NULL)
			throw new RuntimeException("Failed to create the GLFW window");

		// Get the resolution of the primary monitor
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

		// Center our window
		glfwSetWindowPos(windowID, (vidmode.width() - screenWidth) / 2, (vidmode.height() - screenHeight) / 2);

		// Make the OpenGL context current
		glfwMakeContextCurrent(windowID);

		// Enable vsync TODO: refactor
		glfwSwapInterval(1);

		// Make the window visible
		glfwShowWindow(windowID);

		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();
	}

	/**
	 * Retrieve whether or not a window close is requested
	 * 
	 * @return
	 */
	public boolean getWindowShouldClose() {
		return glfwWindowShouldClose(this.windowID);
	}

	/**
	 * Update the window title to say something new
	 * 
	 * @param newTitle
	 */
	public void updateWindowTitle(String newTitle) {
		glfwSetWindowTitle(this.windowID, newTitle);
	}

	/**
	 * swap the buffers
	 */
	public void swapBuffers() {
		glfwSwapBuffers(windowID);
	}

	/**
	 * retrieve window id
	 * 
	 * @return
	 */
	public long getWindowID() {
		return this.windowID;
	}

	/**
	 * Clean up
	 */
	@Override
	public void cleanup() {
		try {
			glfwFreeCallbacks(windowID);
			glfwDestroyWindow(windowID);

			// Terminate GLFW and free the error callback
			glfwTerminate();
			glfwSetErrorCallback(null).free();
		} catch (NullPointerException e) {
			// Ignore as this must mean that everything was successful
		}
	}
}
