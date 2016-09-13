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

	/** Number of rows (used for texture atlases) */
	private int textureAtlasRows;

	/**
	 * Initialize Texture
	 * 
	 * @param textureID
	 */
	protected Texture(int textureID, String texturePath) {
		this(textureID, texturePath, 1);
	}

	protected Texture(int textureID, String texturePath, int textureAtlasRows) {
		this.textureID = textureID;
		this.texturePath = texturePath;
		this.textureAtlasRows = textureAtlasRows;
	}

	/**
	 * Retrieve opengl internal textureID for this texture
	 * 
	 * @return
	 */
	public int getTextureID() {
		return this.textureID;
	}

	/**
	 * Retrieve number of rows in this texture atlas (1 if it is no atlas)
	 * 
	 * @return
	 */
	public int getTextureAtlasRows() {
		return textureAtlasRows;
	}

	/**
	 * Set the number of rows in this texture (1 if it is no atlas)
	 * 
	 * @return
	 */
	public void setTextureAtlasRows(int textureAtlasRows) {
		this.textureAtlasRows = textureAtlasRows;
	}

	@Override
	public void forceDelete() {
		if (this.numReferences > 0)
			Engine.getLogger().warn("Force deleting texture(id: " + this.textureID + ", path: " + this.texturePath
					+ ") that has " + numReferences + " references left!");

		// Actually delete this texture
		GL11.glDeleteTextures(this.textureID);
	}
}