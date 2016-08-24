package com.openglengine.core;

import java.io.*;

import org.lwjgl.opengl.*;

import com.openglengine.eventsystem.defaultevents.*;
import com.openglengine.renderer.*;
import com.openglengine.util.*;

/**
 * This class implements the basic functionality aswell as automatically initializing this engine. Subclass this as a
 * starting point for your game entry point
 * 
 * @author Dominik
 *
 */
public abstract class Basic3DGame {
	private double secsPerUpdate = 1.0 / 60.0;
	private String windowTitle;

	/**
	 * Initialize this game
	 * 
	 * @param screenWidth
	 * @param screenHeight
	 * @param fullscreen
	 * @param windowTitle
	 * @param fov
	 * @param aspect
	 * @param near_plane
	 * @param far_plane
	 * @throws IOException
	 */
	public Basic3DGame(int screenWidth, int screenHeight, boolean fullscreen, String windowTitle, float fov,
			float aspect, float near_plane, float far_plane) throws IOException {
		this.windowTitle = windowTitle;

		Engine.loadEngineComponents(screenWidth, screenHeight, fullscreen, this.windowTitle);
		this.initGL(screenWidth, screenHeight, fov, aspect, near_plane, far_plane);

		// TODO: tmp
		Engine.RENDERER = new RenderManager();
		Engine.NO_TEX_TEXTURE = Engine.TEXTURE_MANAGER.referenceTexture(Engine.TEX_FOLDER + "notex.png");

		Engine.EVENT_MANAGER.registerListenerForEvent(UpdateEvent.class, e -> this.update());

		this.loop();
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
	private void initGL(int screenWidth, int screenHeight, float fov, float aspect, float near_plane, float far_plane) {
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();

		// Set the clear color
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		// Setup projection matrix
		Engine.PROJECTION_MATRIX_STACK.setPerspectiveMatrix(fov, aspect, near_plane, far_plane);

		// Setup viewport
		GL11.glViewport(0, 0, screenWidth, screenHeight);

		// Enable transparency
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		// Enable depth test
		GL11.glEnable(GL11.GL_DEPTH_TEST);

		// Disable rendering inside of models
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}

	/**
	 * Use this method to dynamically set how many ups and therefore fps you want to receive (Rendering more fps than
	 * ups does not make sense as the game will stutter regardless)
	 * 
	 * @param ups
	 */
	protected void setUPS(double ups) {
		this.secsPerUpdate = 1.0 / ups;
	}

	/**
	 * Internal gameloop, ups logic and glClear stuff aswell as buffer swap things
	 */
	private void loop() {
		try {
			this.setup();

			final double nanoToSecondFactor = 1000000000.0;
			long previous = System.nanoTime();
			double steps = 0.0;

			// Simple fps and ups counter logic
			double secondCounter = 0.0;
			int upsCounter = 0;

			// TODO: tmp esc quit (until proper menus are implemented etc)
			while (!Engine.GLFW_MANAGER.getWindowShouldClose()
					&& !Engine.INPUT_MANAGER.isKeyDown(InputManager.KEY_ESC)) {
				/* update */
				long now = System.nanoTime();
				long elapsed = now - previous;
				secondCounter += elapsed / nanoToSecondFactor;
				previous = now;
				steps += elapsed / nanoToSecondFactor;

				if (secondCounter > 1.0) {
					secondCounter -= 1.0;
					Engine.GLFW_MANAGER.updateWindowTitle(this.windowTitle + ": " + upsCounter + "ups");
					upsCounter = 0;
				}

				/* update */

				while (steps >= this.secsPerUpdate) {
					// Update camera
					Engine.CAMERA.update();

					// send update event
					Engine.EVENT_MANAGER.dispatch(new UpdateEvent(secsPerUpdate));
					steps -= secsPerUpdate;
					upsCounter++;
				}

				/* render */
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

				// Render our scene
				Engine.EVENT_MANAGER.dispatch(new RenderEvent(elapsed));

				// Swap buffers (this will, due to vsync, limit to the monitor refresh rate)
				Engine.GLFW_MANAGER.swapBuffers();
			}
		} finally {
			this.cleanup();
			Engine.cleanup();
		}
	}

	/**
	 * Use this function to setup your game
	 */
	protected abstract void setup();

	/**
	 * Use this to update your game
	 */
	protected abstract void update();

	/**
	 * Use this to clean leftover resources
	 */
	protected abstract void cleanup();

}
