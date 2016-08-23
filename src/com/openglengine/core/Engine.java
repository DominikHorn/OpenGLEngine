package com.openglengine.core;

import com.openglengine.entitity.*;
import com.openglengine.entitity.component.*;
import com.openglengine.eventsystem.*;
import com.openglengine.renderer.model.*;
import com.openglengine.renderer.texture.*;
import com.openglengine.util.*;
import com.openglengine.util.math.*;

/**
 * Container class for all the singleton instances. (More convenient to enforce one global instance per class this way)
 * 
 * @author Dominik
 *
 */
public class Engine {
	/** Version of this engine */
	public static final String ENGINE_VERSION = "0.0.1_a";

	/**
	 * Model matrix. Use this for moving/rotating and scaling objects. This matrix will be uploaded to the shader
	 * automatically. See Shader class for more info
	 */
	public static final TransformationMatrixStack MODEL_MATRIX_STACK = new TransformationMatrixStack();

	/**
	 * View matrix. The Camera class uses this for influencing the scene appropriately
	 */
	public static final TransformationMatrixStack VIEW_MATRIX_STACK = new TransformationMatrixStack();

	/**
	 * Projection matrix. This projects the vertex data correctly onto the 2d screen
	 */
	public static final ProjectionMatrixStack PROJECTION_MATRIX_STACK = new ProjectionMatrixStack();

	/** Logger that can be used for conveniently printing messages to the console */
	public static Logger LOGGER = new Logger();

	/** Event dispatch/receive system manager */
	public static EventManager EVENT_MANAGER;

	/** Texture system manager */
	public static TextureManager TEXTURE_MANAGER;

	/** Model system manager */
	public static ModelManager MODEL_MANAGER;

	/** Glfw system manager */
	public static GlfwManager GLFW_MANAGER;

	/** Input system manager */
	public static InputManager INPUT_MANAGER;

	/** Camera convenience class */
	public static Entity CAMERA;

	/**
	 * Initialize GameEngine with the following parameters
	 * 
	 * @param screenWidth
	 * @param screenHeight
	 * @param fullscreen
	 * @param windowTitle
	 */
	public static void loadEngineComponents(int screenWidth, int screenHeight, boolean fullscreen, String windowTitle) {
		EVENT_MANAGER = new EventManager();
		TEXTURE_MANAGER = new TextureManager();
		MODEL_MANAGER = new ModelManager();
		GLFW_MANAGER = new GlfwManager(screenWidth, screenHeight, fullscreen, windowTitle);
		INPUT_MANAGER = new InputManager();
		CAMERA = new Entity(new Vector3f(0, 0, 0), 0, 0, 0, 1).addComponent(new CameraInputComponent())
				.addComponent(new CameraComponent());
	}

	/**
	 * Clean/Delete all data
	 */
	public static void cleanup() {
		CAMERA.cleanup();
		EVENT_MANAGER.cleanup();
		TEXTURE_MANAGER.cleanup();
		MODEL_MANAGER.cleanup();
		GLFW_MANAGER.cleanup();
		INPUT_MANAGER.cleanup();
	}
}