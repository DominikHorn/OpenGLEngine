package com.openglengine.core;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.*;

public abstract class Basic3DGame {
	public Basic3DGame(float fov, float aspect, float near_plane, float far_plane) {
		Engine.loadDefaultEngineComponents();
		this.setup(fov, aspect, near_plane, far_plane);
	}

	private void setup(float fov, float aspect, float near_plane, float far_plane) {
		this.initGL(fov, aspect, near_plane, far_plane);
		this.loop();
		Engine.cleanup();
	}

	protected void initGL(float fov, float aspect, float near_plane, float far_plane) {
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();

		// Set the clear color
		glClearColor(1.0f, 0.0f, 0.0f, 0.0f);

		// Setup projection matrix
		Engine.PROJECTION_MATRIX_STACK.setPerspectiveMatrix(fov, aspect, near_plane, far_plane);

		// Setup viewport
		glViewport(0, 0, Engine.DEFAULT_SCREEN_WIDTH, Engine.DEFAULT_SCREEN_HEIGHT);

		// Enable transparency
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}

	protected abstract void loop();
}
