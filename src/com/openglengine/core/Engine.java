package com.openglengine.core;

import com.openglengine.eventsystem.*;
import com.openglengine.renderer.model.*;
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

	public static final Logger LOGGER = new DebugLogger();

	public static final ModelMatrixStack MODEL_MATRIX_STACK = new ModelMatrixStack();
	public static final ModelMatrixStack VIEW_MATRIX_STACK = new ModelMatrixStack();
	public static final ProjectionMatrixStack PROJECTION_MATRIX_STACK = new ProjectionMatrixStack();

	public static EventManager EVENT_MANAGER;
	public static TextureManager TEXTURE_MANAGER;
	public static ModelManager MODEL_MANAGER;
	public static GlfwManager GLFW_MANAGER;
	public static InputManager INPUT_MANAGER;
	public static Camera CAMERA;

	public static void loadEngineComponents(int screenWidth, int screenHeight, boolean fullscreen, String windowTitle) {
		EVENT_MANAGER = new EventManager();
		TEXTURE_MANAGER = new TextureManager();
		MODEL_MANAGER = new ModelManager();
		GLFW_MANAGER = new GlfwManager(screenWidth, screenHeight, fullscreen, windowTitle);
		INPUT_MANAGER = new InputManager();
		CAMERA = new Camera();
	}

	public static void cleanup() {
		EVENT_MANAGER.cleanup();
		TEXTURE_MANAGER.cleanup();
		MODEL_MANAGER.cleanup();
		GLFW_MANAGER.cleanup();
		INPUT_MANAGER.cleanup();
	}

}