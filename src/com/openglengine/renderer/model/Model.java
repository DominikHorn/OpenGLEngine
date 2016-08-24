package com.openglengine.renderer.model;

import org.lwjgl.opengl.*;

import com.openglengine.core.*;
import com.openglengine.util.*;

/**
 * Container class data needed for rendering
 * 
 * @author Dominik
 *
 */
public abstract class Model extends ReferenceCountedDeletableContainer {
	/** VAOs are like a list that stores all data needed for rendering */
	protected int vaoID;

	/** Amount of indices to render (usually amount of indices contained) */
	protected int indicesCount;

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
			Engine.LOGGER.warn("Force deleting model that has " + numReferences + " references left!");

		GL30.glDeleteVertexArrays(this.vaoID);
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
}
