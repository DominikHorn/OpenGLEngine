package com.openglengine.renderer.material;

import com.openglengine.renderer.*;
import com.openglengine.renderer.shader.*;

/**
 * Container class for material stuff (this can be set per entity)
 * 
 * @author Dominik
 *
 * @param <ShaderClass>
 */
public abstract class Material<ShaderClass extends Shader> implements RenderDelegate<ShaderClass> {}