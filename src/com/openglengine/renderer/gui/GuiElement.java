package com.openglengine.renderer.gui;

import com.openglengine.renderer.texture.*;
import com.openglengine.util.*;
import com.openglengine.util.math.*;

public class GuiElement extends ReferenceCountedDeletableContainer {
	private Vector2f position;
	private Vector2f scale;
	private Texture texture;

	public GuiElement(Texture texture, Vector2f position, Vector2f scale) {
		this.texture = texture;
		this.position = position;
		this.scale = scale;
	}

	public Texture getTexture() {
		return this.texture;
	}

	public Vector2f getPosition() {
		return this.position;
	}

	public Vector2f getScale() {
		return this.scale;
	}

	@Override
	protected void forceDelete() {
		this.texture.cleanup();
	}
}
