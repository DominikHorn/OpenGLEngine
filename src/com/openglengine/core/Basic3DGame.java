package com.openglengine.core;

import org.lwjgl.opengl.*;

import com.openglengine.eventsystem.defaultevents.*;
import com.openglengine.util.*;

public abstract class Basic3DGame {
	private double secsPerUpdate = 1.0 / 60.0;
	private String windowTitle;

	public Basic3DGame(int screenWidth, int screenHeight, boolean fullscreen, String windowTitle, float fov,
			float aspect, float near_plane, float far_plane) {
		this.windowTitle = windowTitle;
		Engine.loadEngineComponents(screenWidth, screenHeight, fullscreen, this.windowTitle);
		Engine.EVENT_MANAGER.registerListenerForEvent(UpdateEvent.class, e -> update((UpdateEvent) e));
		Engine.EVENT_MANAGER.registerListenerForEvent(RenderEvent.class, e -> render((RenderEvent) e));
		this.setup(screenWidth, screenHeight, fov, aspect, near_plane, far_plane);
	}

	private void setup(int screenWidth, int screenHeight, float fov, float aspect, float near_plane, float far_plane) {
		this.initGL(screenWidth, screenHeight, fov, aspect, near_plane, far_plane);
		this.setup();
		this.loop();
		Engine.cleanup();
		cleanup();
	}

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
	}

	protected void setUPS(double ups) {
		this.secsPerUpdate = 1.0 / ups;
	}

	private void loop() {
		final double nanoToSecondFactor = 1000000000.0;
		long previous = System.nanoTime();
		double steps = 0.0;

		// Simple fps and ups counter logic
		double secondCounter = 0.0;
		int fpsCounter = 0;
		int upsCounter = 0;

		// TODO: tmp esc quit (until proper menus are implemented etc)
		while (!Engine.GLFW_MANAGER.getWindowShouldClose() && !Engine.INPUT_MANAGER.isKeyDown(InputManager.KEY_ESC)) {
			/* update */
			long now = System.nanoTime();
			long elapsed = now - previous;
			secondCounter += elapsed / nanoToSecondFactor;
			previous = now;
			steps += elapsed / nanoToSecondFactor;

			if (secondCounter > 1.0) {
				secondCounter -= 1.0;
				Engine.GLFW_MANAGER
						.updateWindowTitle(this.windowTitle + ": " + upsCounter + "ups | " + fpsCounter + "fps");
				upsCounter = 0;
				fpsCounter = 0;
			}

			while (steps >= this.secsPerUpdate) {
				Engine.EVENT_MANAGER.dispatch(new UpdateEvent());
				steps -= secsPerUpdate;
				upsCounter++;
			}

			/* render */
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			Engine.EVENT_MANAGER.dispatch(new RenderEvent());

			Engine.GLFW_MANAGER.swapBuffers();
			fpsCounter++;
		}
	}

	/**
	 * Use this function to setup your game
	 */
	protected abstract void setup();

	protected abstract void update(UpdateEvent e);

	protected abstract void render(RenderEvent e);

	protected abstract void cleanup();

}
