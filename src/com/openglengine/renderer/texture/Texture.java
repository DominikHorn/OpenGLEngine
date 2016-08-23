package com.openglengine.renderer.texture;

import org.lwjgl.opengl.*;

import com.openglengine.core.*;
import com.openglengine.util.*;

/**
 * Container class for storing a texID and how many times this texture is being used
 * 
 * @author Dominik
 *
 */
public class Texture extends ReferenceCountedDeletableContainer {
	private int textureID;

	protected Texture(int textureID) {
		this.textureID = textureID;
	}

	public int getTextureID() {
		return this.textureID;
	}

	public void forceDelete() {
		if (numReferences != 0)
			Engine.LOGGER.warn("Deleting texture that has " + numReferences + " references left!");

		GL11.glDeleteTextures(this.textureID);
	}
}