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
	 * amount of seconds that pass between updates
	 */
	private double secsPerUpdate = 1.0 / 60.0;

	/**
	 * amount of seconds that have to pass in one gameloop tick before updates are skipped
	 */
	private double updateSkipThreshold = 20 * secsPerUpdate;

	/**
	 * Each basic3d game has one display. this is said display
	 */
	private Display gameDisplay;

	/**
	 * Whether or not we should quit the gameloop
	 */
	private boolean quit = false;

	// TODO: refactor
	/** These settings are used for projection matricy setup in resize() */
	protected float fov, near_plane, far_plane;

	public Basic3DGame(float fov, float near_plane, float far_plane) throws IOException {
		this.fov = fov;
		this.near_plane = near_plane;
		this.far_plane = far_plane;

		Engine.loadEngineComponents();
		Engine.getGlobalEventManager().registerListenerForEvent(DisplayCreatedEvent.class, e -> this.run());
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
	 * @param near_plane
	 * @param far_plane
	 */
	private void initGL() {
		int screenWidth = this.gameDisplay.getScreenWidth();
		int screenHeight = this.gameDisplay.getScreenHeight();

		// Set the clear color
		GL11.glClearColor(0f, 0f, 0.1f, 0.1f);

		// Enable transparency
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		// Enable depth test
		GL11.glEnable(GL11.GL_DEPTH_TEST);

		// Disable rendering inside of models
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);

		this.setViewSize(screenWidth, screenHeight);
	}

	private void setViewSize(int screenWidth, int screenHeight) {
		// Setup viewport
		GL11.glViewport(0, 0, screenWidth, screenHeight);

		// Setup projection matrix
		Engine.getProjectionMatrixStack().setPerspectiveMatrix(this.fov, (float) screenWidth / (float) screenHeight,
				this.near_plane, this.far_plane);
	}

	private void run() {
		winProcLoop();

		synchronized (this.gameDisplay) {
			quit = true;
			this.gameDisplay.cleanup();
		}
	}

	// TODO: tmp
	private void winProcLoop() {
		/*
		 * Start new thread to have the OpenGL context current in and which does the rendering.
		 */
		new Thread(() -> this.loop()).start();

		while (!quit) {
			glfwWaitEvents();
		}
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

				/* update */
				if (steps >= this.updateSkipThreshold) {
					Engine.getLogger().info("Skipping all updates because we went over the threshold of "
							+ this.updateSkipThreshold + " seconds");
					steps = 0;
					continue;
				}

				while (steps >= this.secsPerUpdate) {
					// send update event
					Engine.getGlobalEventManager().dispatch(new UpdateEvent(secsPerUpdate));
					steps -= secsPerUpdate;
					upsCounter++;
				}

				/* render */
				this.setViewSize(this.gameDisplay.getScreenWidth(), this.gameDisplay.getScreenHeight());
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

				// Render our scene
				Engine.getGlobalEventManager().dispatch(new RenderEvent(elapsed));
				fpsCounter++;

				// Swap buffers (this will, due to vsync, limit to the monitor refresh rate)
				synchronized (this.gameDisplay) {
					this.gameDisplay.swapBuffers();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.cleanup();
			Engine.cleanup();
		}
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
	 * terminates the gameloop after it finishes the current cycle
	 */
	public void quit() {
		this.quit = true;
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
