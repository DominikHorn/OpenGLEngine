package com.openglengine.renderer.texture;

import org.lwjgl.opengl.*;

import com.openglengine.core.*;
import com.openglengine.util.*;

/**
 * Container class for storing a texID, includes reference counting
 * 
 * @author Dominik
 *
 */
public class Texture extends ReferenceCountedDeletableContainer {
	/**
	 * Texture id (opengl internal)
	 */
	private int textureID;

	/** Texture path. This is stored purely for debug purposes but should not matter too much */
	private String texturePath;

	/**
	 * Initialize Texture
	 * 
	 * @param textureID
	 */
	protected Texture(int textureID, String texturePath) {
		this.textureID = textureID;
		this.texturePath = texturePath;
	}

	/**
	 * Retrieve opengl internal textureID for this texture
	 * 
	 * @return
	 */
	public int getTextureID() {
		return this.textureID;
	}

	@Override
	public void forceDelete() {
		if (this.numReferences > 0)
			Engine.getLogger().warn(
					"Force deleting texture(id: " + this.textureID + ", path: " + this.texturePath + ") that has "
							+ numReferences + " references left!");

		// Actually delete this texture
		GL11.glDeleteTextures(this.textureID);
	}
}