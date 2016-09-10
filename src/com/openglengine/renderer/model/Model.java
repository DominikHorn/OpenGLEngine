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

	/** Shader used when rendering this model */
	protected ShaderClass shader; // TODO: refactor

	/** Material used when rendering this model */
	private Material<ShaderClass> material;

	/**
	 * Initialize Model
	 * 
	 * @param vaoID
	 * @param indicesCount
	 */
	protected Model(int vaoID, int indicesCount, ShaderClass shader, Material<ShaderClass> material) {
		this.vaoID = vaoID;
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
	 * Set shader for this model
	 * 
	 * @param shader
	 */
	public void setShader(ShaderClass shader) {
		this.shader = shader;
	}

	/**
	 * Set material
	 */
	public void setMaterial(Material<ShaderClass> material) {
		this.material = material;
	}

	/**
	 * Inits this model for rendering
	 */
	public void init() {
		// Init shader
		shader.startUsingShader();
		shader.uploadProjectionAndViewMatrix();
		shader.uploadGlobalUniforms();

		this.initRendercode();

		this.material.initRendercode(this.shader);
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
	 * Inits a render delegate for rendering
	 * 
	 * @param delegate
	 */
	public void initRenderDelegate(RenderDelegate delegate) {
		// Allow entity to init render code
		delegate.initRendercode(this.shader);

		// Do this as an engine service
		this.shader.uploadModelViewMatrixUniform();
	}

	/**
	 * Deinits a renderdelegate from rendering
	 * 
	 * @param delegate
	 */
	public void deinitRenderDelegate(RenderDelegate delegate) {
		delegate.deinitRendercode();
	}

	/**
	 * Deinits this model for rendering
	 */
	public void deinit() {
		this.deinitRendercode();
		this.material.deinitRendercode();
		this.shader.stopUsingShader();
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
}
