package com.openglengine.renderer.texture;

import java.io.*;
import java.nio.*;
import java.util.*;

import org.lwjgl.*;
import org.lwjgl.opengl.*;
import org.lwjgl.stb.*;

import com.openglengine.core.*;

/**
 * Takes care of loading textures and makes sure, that each texture is loaded exactly once
 * 
 * @author Dominik
 *
 */
// TODO: refactor
public class TextureManager {
	private static final String TEX_FOLDER_PATH = "res/tex/";

	/* Singleton pattern. */
	private static TextureManager manager;

	private TextureManager() {
		this.loadedTextures = new HashMap<>();
	}

	private static TextureManager getInstance() {
		if (manager == null)
			manager = new TextureManager();

		return manager;
	}

	/* Attributes */

	private Map<String, Texture> loadedTextures;

	/* Methods */
	public static Texture loadTexture(String fileName) {
		try {
			return getInstance()._loadTexture(fileName, "png");
		} catch (IOException e) {
			e.printStackTrace();
			OpenGLEngine.LOGGER.err("IO Error loading texture \"" + fileName + "\"");
		}
		return null;
	}

	public static void cleanTexture(String fileName) {
		getInstance()._cleanTexture(fileName);
	}

	// TODO. refactor http://wiki.lwjgl.org/wiki/The_Quad_textured
	private Texture _loadTexture(String fileName, String fileExtension) throws IOException {
		String filePath = TEX_FOLDER_PATH + fileName + "." + fileExtension;
		Texture loadedTexture = this.loadedTextures.get(fileName);

		if (loadedTexture == null) {
			// Load byte data from texture file
			IntBuffer w = BufferUtils.createIntBuffer(1);
			IntBuffer h = BufferUtils.createIntBuffer(1);
			IntBuffer comp = BufferUtils.createIntBuffer(1);

			ByteBuffer buffer = STBImage.stbi_load(filePath, w, h, comp, 4);
			int tWidth = w.get();
			int tHeight = h.get();

			// Create OpenGL Texture
			int texID = GL11.glGenTextures();
			GL13.glActiveTexture(GL13.GL_TEXTURE0); // TODO: refactor, support multiple texture layers
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texID);

			GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, tWidth, tHeight, 0, GL11.GL_RGBA,
					GL11.GL_UNSIGNED_BYTE, buffer);
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);

			loadedTexture = new Texture(texID);

			// Unbind
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		}

		// Increase references
		loadedTexture.use();

		// Return
		return loadedTexture;
	}

	private void _cleanTexture(String fileName) {
		if (this.loadedTextures.containsKey(fileName)) {
			Texture loadedTexture = this.loadedTextures.get(fileName);

			// remove reference
			if (loadedTexture.cleanup())
				this.loadedTextures.put(fileName, null);

		} else
			OpenGLEngine.LOGGER.warn("Tex file \"" + fileName + "\" is not loaded into memory");
	}
}
