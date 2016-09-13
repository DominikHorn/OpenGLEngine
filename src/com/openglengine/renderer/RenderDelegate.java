package com.openglengine.renderer;

import com.openglengine.renderer.shader.*;
import com.openglengine.renderer.texture.*;

/**
 * Will be consulted during rendering process for customization on how stuff is rendered
 * 
 * @author Dominik
 *
 */
public interface RenderDelegate<ShaderClass extends Shader> {
	/**
	 * Initializes rendering for this render object
	 */
	public void initRendercode(ShaderClass shader);

	/**
	 * Deinitializes rendering for this render object
	 */
	// public void deinitRendercode(ShaderClass shader);
	public void deinitRendercode();

	/**
	 * Overwriting and changing the return value will allow you to shift the texture
	 * 
	 * @return
	 */
	default float getTextureOffsetX(Texture texture) {
		return 0;
	}

	/**
	 * Overwriting and changing the return value will allow you to shift the texture
	 * 
	 * @return
	 */
	default float getTextureOffsetY(Texture texture) {
		return 0;
	}

	/**
	 * This method is a library method needed because generics (especially type erasure can be a huge hastle)
	 * 
	 * @param shader
	 */
	@SuppressWarnings("unchecked")
	default void _initSelfTypecast(Shader shader) {
		this.initRendercode((ShaderClass) shader);
	}

	// /**
	// * This method is a library method needed because generics (especially type erasure can be a huge hastle)
	// *
	// * @param shader
	// */
	// @SuppressWarnings("unchecked")
	// default void _deinitSelfTypecast(Shader shader) {
	// this.deinitRendercode((ShaderClass) shader);
	// }
}
