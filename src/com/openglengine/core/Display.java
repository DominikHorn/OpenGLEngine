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
public class Display implements ResourceManager {
	/** error callback */
	private GLFWErrorCallback errorCallback;

	/** glfw window handle */
	private long windowID;

	/** screen width of glfw view space */
	private int windowWidthInPixels;

	/** screen height of glfw view space */
	private int windowHeightInPixels;

	/** whether or not view space should take up full screen */
	private boolean fullscreen;

	/** window title */
	private String windowBaseTitle;

	public Display(int screenWidth, int screenHeight, boolean fullscreen, String windowBaseTitle) {
		this.windowWidthInPixels = screenWidth;
		this.windowHeightInPixels = screenHeight;
		this.fullscreen = fullscreen;
		this.windowBaseTitle = windowBaseTitle;
		this.windowID = -1;
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
	 * Initialize our window with Parameters set
	 */
	protected void create() {
		if (fullscreen == true) {
			Engine.getLogger().err("Fullscreen not supported atm");
			return;
		}

		if (this.windowID != -1) {
			Engine.getLogger().err("Display already created!");
			return;
		}

		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");

		// Get the video mode of the primary monitor
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

		// Configure our window
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation

		// Create the window
		if (fullscreen)
			this.windowID = glfwCreateWindow(this.windowWidthInPixels, this.windowHeightInPixels, this.windowBaseTitle,
					glfwGetPrimaryMonitor(), NULL);
		else
			this.windowID = glfwCreateWindow(this.windowWidthInPixels, this.windowHeightInPixels, this.windowBaseTitle,
					NULL, NULL);

		if (this.windowID == NULL)
			throw new RuntimeException("Failed to create the GLFW window");

		// Set window size limits
		glfwSetWindowSizeLimits(this.windowID, 960, 540, GLFW_DONT_CARE, GLFW_DONT_CARE);

		// Center our window
		glfwSetWindowPos(this.windowID, (vidmode.width() - this.windowWidthInPixels) / 2,
				(vidmode.height() - this.windowHeightInPixels) / 2);

		// Make the window visible
		glfwShowWindow(this.windowID);

		// Framebuffer size callback
		glfwSetFramebufferSizeCallback(this.windowID, (window, width, height) -> resize(window, width, height));

		// Notify listeners that the display was created
		Engine.getGlobalEventManager().dispatch(new DisplayCreatedEvent(this));
	}

	protected void setupContextThread() {
		glfwMakeContextCurrent(this.windowID);

		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();

		// Enable vsync TODO: refactor
		glfwSwapInterval(1);
	}

	/**
	 * swaps buffers and polls events
	 */
	protected void swapBuffers() {
		// Swap the buffer
		glfwSwapBuffers(windowID);

		// Clear the new backbuffer
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}

	/**
	 * retrieve window id
	 * 
	 * @return
	 */
	protected long getWindowID() {
		return this.windowID;
	}

	/**
	 * get width of the screen
	 * 
	 * @return
	 */
	public int getWindowWidthInPixels() {
		return this.windowWidthInPixels;
	}

	/**
	 * get height of the screen
	 * 
	 * @return
	 */
	public int getWindowHeightInPixels() {
		return this.windowHeightInPixels;
	}

	@Override
	public void cleanup() {
		try {
			glfwFreeCallbacks(windowID);
			glfwDestroyWindow(windowID);

			// Terminate GLFW and free the error callback
			glfwTerminate();
			errorCallback.free();
		} catch (NullPointerException e) {
			// Ignore as this must mean that everything was successful
		}
	}

	/**
	 * Resize callback handler
	 * 
	 * @param window
	 * @param width
	 * @param height
	 */
	private void resize(long window, int width, int height) {
		if (window == this.windowID) {
			this.windowWidthInPixels = width;
			this.windowHeightInPixels = height;
			Engine.getGlobalEventManager()
					.queueForRenderthread(
							new FramebufferResizeEvent(this.windowWidthInPixels, this.windowHeightInPixels));
		}
	}
}
