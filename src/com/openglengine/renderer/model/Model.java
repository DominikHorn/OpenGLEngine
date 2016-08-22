package com.openglengine.renderer.model;

import com.openglengine.util.math.*;

/**
 * Model base class. A model does have an openGL representation that it can render
 * 
 * @author Dominik
 *
 */
public abstract class Model {
	public abstract void render(Vector3f position, float rotX, float rotY, float rotZ, float scale);
}
