package com.openglengine.core;

import com.openglengine.eventsystem.*;
import com.openglengine.renderer.*;
import com.openglengine.renderer.model.*;
import com.openglengine.renderer.texture.*;
import com.openglengine.util.*;

/**
 * Container class for all the singleton instances. (More convenient to enforce one global instance per class this way)
 * 
 * @author Dominik
 *
 */
public class Engine {
	/** Version of this engine */
	public static final String ENGINE_VERSION = "0.2.0a";

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

	/** Logger that can be used for conveniently printing messages to the console */
	private static final Logger LOGGER = new Logger();

	/** Texture system manager */
	private static TextureManager TEXTURE_MANAGER = new TextureManager();

	/** Model system manager */
	private static ModelDataManager MODEL_MANAGER = new ModelDataManager();

	/** Global batch renderering system */
	private static RenderManager RENDER_MANAGER = new RenderManager();

	/** Input system manager */
	private static InputManager INPUT_MANAGER = new InputManager();

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

	public static ModelDataManager getModelDataManager() {
		return MODEL_MANAGER;
	}

	public static InputManager getInputManager() {
		return INPUT_MANAGER;
	}

	public static Logger getLogger() {
		return LOGGER;
	}

	public static RenderManager getRenderManager() {
		return RENDER_MANAGER;
	}

	public static void setRenderManager(RenderManager renderer) {
		RENDER_MANAGER = renderer;
	}

	/**
	 * Clean/Delete all data
	 */
	public static void cleanup() {
		GLOBAL_EVENT_MANAGER.cleanup();
		TEXTURE_MANAGER.cleanup();
		MODEL_MANAGER.cleanup();
		INPUT_MANAGER.cleanup();
		RENDER_MANAGER.cleanup();
	}
}