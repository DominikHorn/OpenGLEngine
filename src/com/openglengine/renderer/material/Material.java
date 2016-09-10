package com.openglengine.renderer.material;

import com.openglengine.renderer.shader.*;

/**
 * Container class for storing all material properties of a Model
 * 
 * @author Dominik
 *
 */
public abstract class Material<ShaderClass extends Shader> {
	public abstract void initRendercode(ShaderClass shader);

	public abstract void deinitRendercode();
}
