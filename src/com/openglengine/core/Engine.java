package com.openglengine.core;

import java.io.*;

import com.openglengine.entitity.*;
import com.openglengine.entitity.component.*;
import com.openglengine.eventsystem.*;
import com.openglengine.renderer.*;
import com.openglengine.renderer.model.*;
import com.openglengine.renderer.shader.*;
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
	private static final TransformationMatrixStack MODEL_MATRIX_STACK = new TransformationMatrixStack();

	/**
	 * View matrix. The Camera class uses this for influencing the scene appropriately
	 */
	private static final TransformationMatrixStack VIEW_MATRIX_STACK = new TransformationMatrixStack();

	/**
	 * Projection matrix. This projects the vertex data correctly onto the 2d screen
	 */
	private static final ProjectionMatrixStack PROJECTION_MATRIX_STACK = new ProjectionMatrixStack();

	/** Event dispatch/receive system manager */
	private static final GlobalEventManager GLOBAL_EVENT_MANAGER = new GlobalEventManager();

	/** Texture system manager */
	private static final TextureManager TEXTURE_MANAGER = new TextureManager();

	/** Model system manager */
	private static final ModelManager MODEL_MANAGER = new ModelManager();

	/** Logger that can be used for conveniently printing messages to the console */
	private static final Logger LOGGER = new Logger();

	/** Camera convenience class */
	private static Entity CAMERA;

	/** Global batch renderering system */
	private static RenderManager RENDERER;

	/* TODO: refactor */
	/** Glfw system manager */
	private static GlfwManager GLFW_MANAGER;

	/** Input system manager */
	private static InputManager INPUT_MANAGER;

	/** This file will be used if no texture was found */
	private static Texture DEFAULT_TEXTURE;

	/** Default shader program if no shader was found */
	private static Shader DEFAULT_SHADER;

	// TODO: refactor (Allow for multiple light sources f.e.)
	/** Global light source */
	private static LightSource LIGHT_SOURCE;

	public static String getEngineVersion() {
		return ENGINE_VERSION;
	}

	public static TransformationMatrixStack getModelMatrixStack() {
		return MODEL_MATRIX_STACK;
	}

	public static TransformationMatrixStack getViewMatrixStack() {
		return VIEW_MATRIX_STACK;
	}

	public static ProjectionMatrixStack getProjectionMatrixStack() {
		return PROJECTION_MATRIX_STACK;
	}

	public static GlobalEventManager getGlobalEventManager() {
		return GLOBAL_EVENT_MANAGER;
	}

	public static TextureManager getTextureManager() {
		return TEXTURE_MANAGER;
	}

	public static ModelManager getModelManager() {
		return MODEL_MANAGER;
	}

	public static InputManager getInputManager() {
		return INPUT_MANAGER;
	}

	public static Logger getLogger() {
		return LOGGER;
	}

	public static GlfwManager getGlfwManager() {
		return GLFW_MANAGER;
	}

	public static void setGlfwManager(GlfwManager glfwManager) {
		GLFW_MANAGER = glfwManager;
	}

	public static Entity getCamera() {
		return CAMERA;
	}

	public static void setCamera(Entity camera) {
		CAMERA = camera;
	}

	public static RenderManager getRenderer() {
		return RENDERER;
	}

	public static void setRenderer(RenderManager renderer) {
		RENDERER = renderer;
	}

	public static Texture getDefaultTexture() {
		return DEFAULT_TEXTURE;
	}

	public static void setDefaultTexture(Texture defaultTexture) {
		DEFAULT_TEXTURE = defaultTexture;
	}

	public static LightSource getLightSource() {
		return LIGHT_SOURCE;
	}

	public static void setLightSource(LightSource lightSource) {
		LIGHT_SOURCE = lightSource;
	}

	/**
	 * Initialize all parts of the GameEngine that are opengl context independent with the following parameters
	 * 
	 * @param screenWidth
	 * @param screenHeight
	 * @param fullscreen
	 * @param windowTitle
	 * @throws IOException
	 */
	public static void loadEngineComponents(int screenWidth, int screenHeight, boolean fullscreen, String windowTitle)
			throws IOException {
		GLFW_MANAGER = new GlfwManager(screenWidth, screenHeight, fullscreen, windowTitle);
		INPUT_MANAGER = new InputManager();
		CAMERA = new Entity(new Vector3f(0, 0, 0), 0, 0, 0, 1).addComponent(new CameraInputComponent())
				.addComponent(new CameraComponent());
		LIGHT_SOURCE = new LightSource(new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));

		/** TODO: refactor */
		DEFAULT_TEXTURE = null;
	}

	/**
	 * Clean/Delete all data
	 */
	public static void cleanup() {
		if (DEFAULT_TEXTURE != null)
			DEFAULT_TEXTURE.cleanup();
		if (DEFAULT_SHADER != null)
			DEFAULT_SHADER.cleanup();

		if (CAMERA != null)
			CAMERA.cleanup();

		GLOBAL_EVENT_MANAGER.cleanup();
		TEXTURE_MANAGER.cleanup();
		MODEL_MANAGER.cleanup();
		GLFW_MANAGER.cleanup();
		INPUT_MANAGER.cleanup();
	}
}