package com.openglengine.renderer.model;

import com.openglengine.renderer.texture.*;

public class TexturedModel {
	private RawModel rawModel;
	private Texture texture;

	public TexturedModel(RawModel model, Texture texture) {
		this.rawModel = model;
		this.texture = texture;
	}

	public RawModel getRawModel() {
		return rawModel;
	}

	public Texture getTexture() {
		return texture;
	}
}
