package com.openglengine.renderer.model;

import org.lwjgl.opengl.*;

import com.openglengine.util.*;

/**
 * Container class for model data used by ModelManager.
 * 
 * @author Dominik
 *
 */
public abstract class Model extends ReferenceCountedDeletableContainer {
	protected int vaoID;
	protected int indicesCount;

	public Model(int vaoID, int indicesCount) {
		this.vaoID = vaoID;
		this.indicesCount = indicesCount;
	}

	@Override
	protected void forceDelete() {
		GL30.glDeleteVertexArrays(this.vaoID);
	}

	public int getVaoID() {
		return vaoID;
	}

	public int getIndicesCount() {
		return indicesCount;
	}
}
