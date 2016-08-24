package com.openglengine.core;

import java.io.*;

import org.lwjgl.opengl.*;

import com.openglengine.eventsystem.defaultevents.*;
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

	public Basic3DGame(float fov, float near_plane, float far_plane) throws IOException {
		Engine.loadEngineComponents();
		Engine.getGlobalEventManager().registerListenerForEvent(DisplayCreatedEvent.class,
				e -> initGL(fov, near_plane, far_plane));
		Engine.getGlobalEventManager().registerListenerForEvent(DisplayCreatedEvent.class, e -> this.loop());
		Engine.getGlobalEventManager().registerListenerForEvent(UpdateEvent.class, e -> this.update());
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
	private void initGL(float fov, float near_plane, float far_plane) {
		int screenWidth = this.getGameDisplay().getScreenWidth();
		int screenHeight = this.getGameDisplay().getScreenHeight();

		// Set the clear color
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		// Setup projection matrix
		Engine.getProjectionMatrixStack().setPerspectiveMatrix(fov, (float) screenWidth / (float) screenHeight,
				near_plane, far_plane);

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
			int fpsCounter = 0;

			// TODO: tmp esc quit (until proper menus are implemented etc)
			while (!this.getGameDisplay().isCloseRequested()
					&& !Engine.getInputManager().isKeyDown(InputManager.KEY_ESC)) {
				/* update */
				long now = System.nanoTime();
				long elapsed = now - previous;
				secondCounter += elapsed / nanoToSecondFactor;
				previous = now;
				steps += elapsed / nanoToSecondFactor;

				if (secondCounter > 1.0) {
					secondCounter -= 1.0;
					this.getGameDisplay()
							.updateWindowTitle(fpsCounter + "fps | " + upsCounter + "ups");
					upsCounter = 0;
					fpsCounter = 0;
				}

				/* update */

				while (steps >= this.secsPerUpdate) {
					Engine.getCamera().update();

					// send update event
					Engine.getGlobalEventManager().dispatch(new UpdateEvent(secsPerUpdate));
					steps -= secsPerUpdate;
					upsCounter++;
				}

				/* render */
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

				// Render our scene
				Engine.getGlobalEventManager().dispatch(new RenderEvent(elapsed));
				fpsCounter++;

				// Swap buffers (this will, due to vsync, limit to the monitor refresh rate)
				this.getGameDisplay().swapBuffers();
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

	/**
	 * Return the games display
	 * 
	 * @return
	 */
	protected abstract Display getGameDisplay();

}
