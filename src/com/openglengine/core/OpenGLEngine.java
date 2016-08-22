package com.openglengine.core;

import com.openglengine.renderer.texture.*;
import com.openglengine.util.*;

public class OpenGLEngine {
	public static final String ENGINE_VERSION = "0.0.1_a";
	public static final Logger LOGGER = new DebugLogger();
	public static final TransformMatrixStack TRANSFORM_STACK = new TransformMatrixStack();
	public static final TextureManager TEXTURE_MANAGER = new TextureManager();

	// TODO: add shader manager
}
