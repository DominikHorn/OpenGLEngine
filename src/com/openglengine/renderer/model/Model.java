package com.openglengine.renderer.model;

import org.lwjgl.opengl.*;

import com.openglengine.core.*;
import com.openglengine.renderer.*;
import com.openglengine.renderer.material.*;
import com.openglengine.renderer.shader.*;
import com.openglengine.util.*;

/**
 * Container class data needed for rendering
 * 
 * @author Dominik
 *
 */
public abstract class Model<ShaderClass extends Shader> extends ReferenceCountedDeletableContainer {
	/** VAOs are like a list that stores all data needed for rendering */
	private int vaoID;

	/** Amount of indices to render (usually amount of indices contained) */
	private int indicesCount;

	/** shader used when rendering this model */
	private ShaderClass shader;

	/** Models material */
	private Material<ShaderClass> material;

	/**
	 * Initialize Model
	 * 
	 * @param vaoID
	 * @param indicesCount
	 */
	protected Model(int indicesCount, ShaderClass shader, Material<ShaderClass> material) {
		this.indicesCount = indicesCount;
		this.shader = shader;
		this.material = material;
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
	 * Retrieve shader used when rendering this model
	 * 
	 * @return
	 */
	public ShaderClass getShader() {
		return this.shader;
	}

	/**
	 * Set shader with which this model will be rendered
	 * 
	 * @param shader
	 */
	public void setShader(ShaderClass shader) {
		this.shader = shader;
	}

	/**
	 * Retrieve material with which this model will be rendered
	 * 
	 * @return
	 */
	public Material<ShaderClass> getMaterial() {
		return material;
	}

	/**
	 * Set material with which this model will be rendered
	 * 
	 * @param material
	 */
	public void setMaterial(Material<ShaderClass> material) {
		this.material = material;
	}
	
	/**
	 * Call this method before rendering
	 * 
	 * @param shader
	 */
	public void initRendercode() {
		// Bind vao for use
		GL30.glBindVertexArray(this.getVaoID());

		// Enable first vertex attrib array (Vertex data)
		GL20.glEnableVertexAttribArray(0);

		// Enable vertex attrib array 2 (Normal data)
		GL20.glEnableVertexAttribArray(2);
	}

	/**
	 * Call this method after rendering
	 */
	public void deinitRendercode() {
		// Disable first vertex attrib array (Vertex data)
		GL20.glDisableVertexAttribArray(0);

		// Disable vertex attrib array 2 (Normal data)
		GL20.glDisableVertexAttribArray(2);

		// Unbind vao
		GL30.glBindVertexArray(0);
	}

	public void uploadRenderdelegateSpecificData(RenderDelegate<?> delegate) {
		// Do nothing by default
	}
}
