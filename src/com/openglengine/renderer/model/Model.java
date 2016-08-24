package com.openglengine.renderer.model;

import org.lwjgl.opengl.*;

import com.openglengine.core.*;
import com.openglengine.renderer.shader.*;
import com.openglengine.util.*;

/**
 * Container class data needed for rendering
 * 
 * @author Dominik
 *
 */
public abstract class Model extends ReferenceCountedDeletableContainer {
	/** VAOs are like a list that stores all data needed for rendering */
	private int vaoID;

	/** Amount of indices to render (usually amount of indices contained) */
	private int indicesCount;

	/** Shader used when rendering this model */
	private Shader shader;

	/**
	 * Initialize Model
	 * 
	 * @param vaoID
	 * @param indicesCount
	 */
	protected Model(int vaoID, int indicesCount) {
		this.vaoID = vaoID;
		this.indicesCount = indicesCount;

	}

	@Override
	protected void forceDelete() {
		if (this.numReferences > 0)
			Engine.getLogger().warn("Force deleting model that has " + numReferences + " references left!");

		GL30.glDeleteVertexArrays(this.vaoID);
	}

	/**
	 * Set the vao id. this is not to be used by any other class than Model's subclasses
	 * 
	 * @param id
	 */
	protected void setVaoID(int id) {
		this.vaoID = id;
	}

	/**
	 * retrieve unique opengl vao id
	 * 
	 * @return
	 */
	public int getVaoID() {
		return vaoID;
	}

	/**
	 * Retrieve indices count
	 * 
	 * @return
	 */
	public int getIndicesCount() {
		return indicesCount;
	}

	/**
	 * Set shader for this model
	 * 
	 * @param shader
	 */
	public void setShader(Shader shader) {
		this.shader = shader;
	}

	/**
	 * Retrieve the shader for this model
	 * 
	 * @return
	 */
	public Shader getShader() {
		return this.shader;
	}
}
