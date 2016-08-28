package com.openglengine.core;

import static org.lwjgl.glfw.GLFW.*;

import java.io.*;

import org.lwjgl.opengl.*;

import com.openglengine.eventsystem.defaultevents.*;

/**
 * This class implements the basic functionality aswell as automatically initializing this engine. Subclass this as a
 * starting point for your game entry point
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
	 * amount of seconds that pass between updates
	 */
	private double secsPerUpdate = 1.0 / 60.0;

	/**
	 * amount of seconds that have to pass in one gameloop tick before updates are skipped
	 */
	private double updateSkipThreshold = 20 * secsPerUpdate;

	/**
	 * Whether or not we should quit the gameloop
	 */
	private boolean quit = false;

	/** These settings are used for projection matricy setup in resize() */
	private float fov, nearPlane, farPlane;

	public Basic3DGame(float fov, float near_plane, float far_plane) throws IOException {
		this.fov = fov;
		this.nearPlane = near_plane;
		this.farPlane = far_plane;

		Engine.getGlobalEventManager().registerListenerForEvent(DisplayCreatedEvent.class, e -> this.run());
		Engine.getGlobalEventManager().registerListenerForEvent(FramebufferResizeEvent.class, e -> this
				.setViewSize(((FramebufferResizeEvent) e).getNewWidth(), ((FramebufferResizeEvent) e).getNewHeight()));
		Engine.getGlobalEventManager().registerListenerForEvent(UpdateEvent.class, e -> this.update());

		this.gameDisplay = this.setupDisplay();
		this.gameDisplay.create();
	}

	/**
	 * GL init code
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
		GL11.glClearColor(0f, 0f, 0f, 1.0f);

		// Enable depth test
		GL11.glEnable(GL11.GL_DEPTH_TEST);

		// Disable rendering inside of models
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);

		this.setViewSize(pixelWidth, pixeleight);
	}

	private void setViewSize(int pixelWidth, int pixelHeight) {
		// Setup viewport
		GL11.glViewport(0, 0, pixelWidth, pixelHeight);

		// Setup projection matrix
		Engine.getProjectionMatrixStack().setPerspectiveMatrix(this.fov, (float) pixelWidth / (float) pixelHeight,
				this.nearPlane, this.farPlane);
	}

	private void run() {
		// Start rendering thread
		new Thread(() -> this.loop()).start();

		// Seperate loop wating for glfw events on this thread
		while (!quit) {
			glfwWaitEvents();
		}

		// If we quit, wait for last render loop pass and quit
		this.quit();
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
			double steps = 0.0;

			// Simple fps and ups counter logic
			double secondCounter = 0.0;
			int upsCounter = 0;
			int fpsCounter = 0;

			// Actual gameloop
			while (!quit) {
				/* update */
				long now = System.nanoTime();
				long elapsed = now - previous;
				secondCounter += elapsed / nanoToSecondFactor;
				previous = now;
				steps += elapsed / nanoToSecondFactor;

				if (secondCounter > 1.0) {
					secondCounter -= 1.0;
					this.gameDisplay.updateWindowTitle(fpsCounter + "fps | " + upsCounter + "ups");
					upsCounter = 0;
					fpsCounter = 0;
				}

				// Skip steps if we have to many
				if (steps >= this.updateSkipThreshold) {
					Engine.getLogger().info("Skipping all updates because we went over the threshold of "
							+ this.updateSkipThreshold + " seconds");
					steps = 0;
					continue;
				}

				// Fetch events
				Engine.getGlobalEventManager().fetchForRenderthread();

				// Update
				while (steps >= this.secsPerUpdate) {
					// send update event
					Engine.getGlobalEventManager().dispatch(new UpdateEvent(secsPerUpdate));
					steps -= secsPerUpdate;
					upsCounter++;
				}

				// Render our scene
				Engine.getGlobalEventManager().dispatch(new RenderEvent(elapsed));
				fpsCounter++;

				// Swap buffers (this will, due to vsync, limit to the monitor refresh rate)
				synchronized (this.gameDisplay) {
					// Critical line to keep buffers from flickering ..
					GL11.glFlush();

					// Swap out buffers
					this.gameDisplay.swapBuffers();

					// Clear new backbuffer
					GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
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
	 * Use this method to dynamically set how many ups and therefore fps you want to receive (Rendering more fps than
	 * ups does not make sense as the game will stutter regardless)
	 * 
	 * @param ups
	 */
	public void setUPS(double ups) {
		this.secsPerUpdate = 1.0 / ups;
	}

	/**
	 * Set the update skip threshold.
	 * 
	 * Explanation: If the game loop is paused for some reason (f.e. user drags window), and starts up again the
	 * gameloop will try to send every update that it missed. This will cause a lag spike and the game zip-forwards many
	 * seconds. If this threshold is surpassed updates will simply be skipped.
	 * 
	 * @param seconds
	 */
	public void setUpdateSkipThreshold(double seconds) {
		this.updateSkipThreshold = seconds;
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
	 */
	protected abstract void update();

	/**
	 * Use this to clean leftover resources
	 */
	protected abstract void cleanup();

}
