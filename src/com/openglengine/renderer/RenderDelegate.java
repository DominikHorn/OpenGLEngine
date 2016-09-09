package com.openglengine.renderer;

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
	public void initRendercode();

	/**
	 * Deinitializes rendering for this render object
	 */
	public void deinitRendercode();

	// TODO: provide interface for shader to obtain information (Or have shader as parameter which will then be
	// typecasted)
}
