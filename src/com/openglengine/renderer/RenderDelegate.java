package com.openglengine.renderer;

import com.openglengine.renderer.shader.*;

/**
 * Will be consulted during rendering process for customization on how stuff is rendered
 * 
 * @author Dominik
 *
 */
public interface RenderDelegate {
	/**
	 * Initializes rendering for this render object
	 */
	public void initRendercode(Shader shader);

	/**
	 * Deinitializes rendering for this render object
	 */
	public void deinitRendercode();
}
