package com.openglengine.core;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.*;

import com.openglengine.tutorial2.*;

/**
 * Game entry point
 * 
 * @author Dominik
 *
 */
public class OpenGLGame {
	public static final String APP_VERSION = "0.0.1_a";
	public static final String DISPLAY_TITLE = "OpenGLEngine " + APP_VERSION;
	public static final int DEFAULT_DISPLAY_WIDTH = 1920;
	public static final int DEFAULT_DISPLAY_HEIGHT = 1080;

	private final GlfwManager glfwManager;

	private OpenGLGame() {
		this(DEFAULT_DISPLAY_WIDTH, DEFAULT_DISPLAY_HEIGHT, false);
	}

	private OpenGLGame(int screenWidth, int screenHeight, boolean fullscreen) {
		// Initialize glfw
		this.glfwManager = new GlfwManager(screenWidth, screenHeight, fullscreen);
	}

	/* TODO: refactor start() and terminate() */
	public void start() {
		this.initGL();
		this.loop();
	}

	public void cleanup() {
		this.glfwManager.cleanup();
	}

	private void initGL() {
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();

		// Set the clear color
		glClearColor(1.0f, 0.0f, 0.0f, 0.0f);

		GL11.glViewport(0, 0, DEFAULT_DISPLAY_WIDTH, DEFAULT_DISPLAY_HEIGHT);
	}

	private void loop() {
		// TODO: tmp
		Loader loader = new Loader();
		Renderer renderer = new Renderer();

		//@formatter:off
		float[] vertices = {
			-0.5f, +0.5f, +0.0f,
			-0.5f, -0.5f, +0.0f,
			+0.5f, -0.5f, +0.0f,
			+0.5f, +0.5f, +0.0f
		};
		
		int [] indices = {
			0,1,3,
			3,1,2,
		};		
		//@formatter:on

		RawModel model = loader.loadToVAO(vertices, indices);

		while (!this.glfwManager.getWindowShouldClose()) {
			renderer.prepare();

			renderer.render(model);

			this.glfwManager.swapBuffers();
			this.glfwManager.pollEvents();
		}

		// TODO: tmp
		loader.cleanup();
	}

	public static void main(String argv[]) {
		OpenGLGame game = new OpenGLGame();

		try {
			game.start();
		} finally {
			game.cleanup();
		}
	}
}
