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

	/** shine dampener factor of this texture */
	private float shineDamper = 10.0f;

	/** reflecivity factor of this texture */
	private float reflectivity = 1.0f;

	/**
	 * Initialize Texture
	 * 
	 * @param textureID
	 */
	protected Texture(int textureID) {
		this.textureID = textureID;
	}

	/**
	 * Set a new shine damper for this texture
	 * 
	 * @param newDamper
	 */
	public void setShineDamper(float newDamper) {
		this.shineDamper = newDamper;
	}

	/**
	 * retrieve shine damper
	 * 
	 * @return
	 */
	public float getShineDamper() {
		return this.shineDamper;
	}

	/**
	 * Set a new reflectivity for this texture
	 * 
	 * @param newReflectivity
	 */
	public void setReflectivity(float newReflectivity) {
		this.reflectivity = newReflectivity;
	}

	/**
	 * retrieve reflectivity
	 * 
	 * @return
	 */
	public float getReflectivity() {
		return this.reflectivity;
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
			Engine.getLogger().warn("Force deleting texture that has " + numReferences + " references left!");

		GL11.glDeleteTextures(this.textureID);
	}
}