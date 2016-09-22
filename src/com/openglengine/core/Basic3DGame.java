package com.openglengine.core;

import static org.lwjgl.glfw.GLFW.*;

import java.io.*;

import org.lwjgl.opengl.*;

import com.openglengine.eventsystem.defaultevents.*;

/**
 * This class implements the basic functionality as well as automatically initializing the engine. Subclass this as a
 * starting point for your game
 * 
 * @author Dominik
 *
 */
public abstract class Basic3DGame {
	/**
	 * Each basic3d game has one display. this is said display
	 */
	private Display gameDisplay;

	/**
	 * Whether or not we should quit the gameloop
	 */
	private boolean quit = false;

	/** These settings are used for projection matricy setup in resize() */
	private float fov, nearPlane, farPlane;

	/**
	 * Creates a new Basic3d game
	 * 
	 * @param fov
	 * @param near_plane
	 * @param far_plane
	 * @throws IOException
	 */
	public Basic3DGame(float fov, float near_plane, float far_plane) throws IOException {
		this.fov = fov;
		this.nearPlane = near_plane;
		this.farPlane = far_plane;

		Engine.getGlobalEventManager().registerListenerForEvent(FramebufferResizeEvent.class, e -> this
				.setViewSize(((FramebufferResizeEvent) e).getNewWidth(), ((FramebufferResizeEvent) e).getNewHeight()));
		Engine.getGlobalEventManager().registerListenerForEvent(UpdateEvent.class,
				e -> this.update(((UpdateEvent) e).getDeltatime()));

		this.gameDisplay = this.setupDisplay();
		this.gameDisplay.create();
	}

	/**
	 * Starts game execution
	 */
	public void startGame() {
		new Thread(() -> this.loop()).start();

		while (!this.quit) {
			glfwWaitEvents();
			Engine.getGlobalEventManager().queuForMainthread(new MainthreadUpdateEvent());
			Engine.getGlobalEventManager().fetchForMainthread();
		}
	}

	/**
	 * Initializes OpenGL
	 * 
	 * @param screenWidth
	 * @param screenHeight
	 * @param fov
	 * @param aspect
	 * @param nearPlane
	 * @param farPlane
	 */
	private void initGL() {
		int pixelWidth = this.gameDisplay.getWindowWidthInPixels();
		int pixeleight = this.gameDisplay.getWindowHeightInPixels();

		// Set the clear color
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		// Enable depth test
		GL11.glEnable(GL11.GL_DEPTH_TEST);

		// Disable rendering inside of models
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);

		this.setViewSize(pixelWidth, pixeleight);
	}

	/**
	 * Sets the view size
	 * 
	 * @param pixelWidth
	 * @param pixelHeight
	 */
	private void setViewSize(int pixelWidth, int pixelHeight) {
		// Setup viewport
		GL11.glViewport(0, 0, pixelWidth, pixelHeight);

		// Setup projection matrix
		Engine.getProjectionMatrixStack().setPerspectiveMatrix(this.fov, (float) pixelWidth / (float) pixelHeight,
				this.nearPlane, this.farPlane);
	}

	/**
	 * Internal gameloop, ups logic and glClear stuff aswell as buffer swap things
	 */
	private void loop() {
		try {
			this.gameDisplay.setupContextThread();
			this.initGL();
			this.setup();

			final double nanoToSecondFactor = 1000000000.0;
			long previous = System.nanoTime();

			// Simple fps and ups counter logic
			double secondCounter = 0.0;
			int fpsCounter = 0;

			// Actual gameloop
			while (!quit) {
				/* update */
				long now = System.nanoTime();
				long elapsed = now - previous;
				secondCounter += elapsed / nanoToSecondFactor;
				previous = now;

				if (secondCounter > 1.0) {
					secondCounter -= 1.0;
					this.gameDisplay.updateWindowTitle(fpsCounter + "fps");
					fpsCounter = 0;
				}

				// Fetch events for this thread
				Engine.getGlobalEventManager().fetchForRenderthread();

				// Send update event
				Engine.getGlobalEventManager().dispatch(new UpdateEvent(elapsed));

				// Render our scene
				Engine.getGlobalEventManager().dispatch(new RenderEvent(elapsed));
				fpsCounter++;

				// Swap out buffers
				synchronized (this.gameDisplay) {
					this.gameDisplay.swapBuffers();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.cleanup();
			this.gameDisplay.cleanup();
			Engine.cleanup();
		}
	}

	public void setViewFrustum(float fov, float nearPlane, float farPlane) {
		this.fov = fov;
		this.nearPlane = nearPlane;
		this.farPlane = farPlane;
	}

	/**
	 * Is a quit requested by the engine?
	 * 
	 * @return
	 */
	public boolean isQuitRequestedByEngine() {
		return this.gameDisplay.isCloseRequested();
	}

	/**
	 * terminates the main loop and quits the game
	 */
	public void quit() {
		quit = true;
	}

	/**
	 * Use this function to setup your game
	 */
	protected abstract void setup();

	/**
	 * This method will be called to retrieve a correctly configured display from your subclass.
	 * 
	 * @return
	 */
	protected abstract Display setupDisplay();

	/**
	 * Use this to update your game
	 * 
	 * @param deltatime
	 *            time interval between the last update() call and this one
	 */
	protected abstract void update(double deltatime);

	/**
	 * Use this to clean leftover resources
	 */
	protected abstract void cleanup();

}
