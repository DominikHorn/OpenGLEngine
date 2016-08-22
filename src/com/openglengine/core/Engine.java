package com.openglengine.core;

import com.openglengine.eventsystem.*;
import com.openglengine.renderer.texture.*;
import com.openglengine.util.*;

/**
 * Container class for all the singleton instances. (More convenienct to enforce one global instance per class this way)
 * 
 * @author Dominik
 *
 */
public class Engine {
	public static final String ENGINE_VERSION = "0.0.1_a";
	public static final int DEFAULT_SCREEN_WIDTH = 1920;
	public static final int DEFAULT_SCREEN_HEIGHT = 1080;

	public static final Logger LOGGER = new DebugLogger();

	public static final TransformMatrixStack TRANSFORM_MATRIX_STACK = new TransformMatrixStack();
	public static final TransformMatrixStack VIEW_MATRIX_STACK = new TransformMatrixStack();
	public static final ProjectionMatrixStack PROJECTION_MATRIX_STACK = new ProjectionMatrixStack();

	public static EventManager EVENT_MANAGER;
	public static TextureManager TEXTURE_MANAGER;
	public static GlfwManager GLFW_MANAGER;
	public static InputManager INPUT_MANAGER;

	// TODO: make settings with glfwmanager better
	public static void loadDefaultEngineComponents() {
		EVENT_MANAGER = new EventManager();
		TEXTURE_MANAGER = new TextureManager();
		GLFW_MANAGER = new GlfwManager(DEFAULT_SCREEN_WIDTH, DEFAULT_SCREEN_HEIGHT, false, ENGINE_VERSION);
		INPUT_MANAGER = new InputManager();
	}

	public static void cleanup() {
		EVENT_MANAGER.cleanup();
		TEXTURE_MANAGER.cleanup();
		GLFW_MANAGER.cleanup();
		INPUT_MANAGER.cleanup();
	}

}
