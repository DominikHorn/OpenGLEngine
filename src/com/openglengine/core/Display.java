package com.openglengine.core;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import com.openglengine.eventsystem.defaultevents.*;
import com.openglengine.util.*;

/**
 * Manager for all the glfw code
 * 
 * @author Dominik
 *
 */
public class Display extends Manager {
	/** glfw window handle */
	private long windowID;

	/** screen width of glfw view space */
	private int screenWidth;

	/** screen height of glfw view space */
	private int screenHeight;

	/** whether or not view space should take up full screen */
	private boolean fullscreen;

	/** window title */
	private String windowBaseTitle;

	public Display(int screenWidth, int screenHeight, boolean fullscreen, String windowBaseTitle) {
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.fullscreen = fullscreen;
		this.windowBaseTitle = windowBaseTitle;
		this.windowID = -1;

		Engine.getGlobalEventManager().registerListenerForEvent(UpdateEvent.class, e -> glfwPollEvents());
	}

	/**
	 * Initialize our window with Parameters set
	 */
	public void create() {
		if (fullscreen == true)
			Engine.getLogger().err("Fullscreen not supported atm");

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
			this.windowID = glfwCreateWindow(this.screenWidth, this.screenHeight, this.windowBaseTitle,
					glfwGetPrimaryMonitor(), NULL);
		else
			this.windowID = glfwCreateWindow(this.screenWidth, this.screenHeight, this.windowBaseTitle, NULL, NULL);

		if (this.windowID == NULL)
			throw new RuntimeException("Failed to create the GLFW window");

		// Get the resolution of the primary monitor
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

		// Center our window
		glfwSetWindowPos(this.windowID, (vidmode.width() - this.screenWidth) / 2,
				(vidmode.height() - this.screenHeight) / 2);

		// Make the OpenGL context current
		glfwMakeContextCurrent(this.windowID);

		// Enable vsync TODO: refactor
		glfwSwapInterval(1);

		// Make the window visible
		glfwShowWindow(this.windowID);

		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();

		// Notify listeners that the display was created
		Engine.getGlobalEventManager().dispatch(new DisplayCreatedEvent(this));
	}

	/**
	 * Retrieve whether or not a window close is requested
	 * 
	 * @return
	 */
	public boolean isCloseRequested() {
		return glfwWindowShouldClose(this.windowID);
	}

	/**
	 * Update the window title to say something new
	 * 
	 * @param newTitle
	 */
	public void updateWindowTitle(String newTitle) {
		glfwSetWindowTitle(this.windowID, this.windowBaseTitle + ": " + newTitle);
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

	public int getScreenWidth() {
		return screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
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
