package com.openglengine.renderer.texture;

import org.lwjgl.opengl.*;

import com.openglengine.core.*;

/* private inner container class */
/* TODO: refactor */
/**
 * Container class for storing a texID and how many times this texture is being used
 * 
 * @author Dominik
 *
 */
public class Texture {
	private int textureID;
	private int numReferences;

	protected Texture(int textureID) {
		this.textureID = textureID;
	}

	public int getTextureID() {
		return this.textureID;
	}

	/**
	 * increase reference counter
	 */
	protected void use() {
		this.numReferences++;
	}

	/**
	 * decrease reference counter and auto delete texture
	 * 
	 * @return
	 */
	protected boolean cleanup() {
		this.numReferences--;
		if (numReferences <= 0) {
			// Delete texture
			this.forceDelete();

			return true;
		}

		return false;
	}

	public void forceDelete() {
		if (numReferences != 0)
			Engine.LOGGER.warn("Deleting texture that has " + numReferences + " references left!");

		GL11.glDeleteTextures(this.textureID);
	}
}