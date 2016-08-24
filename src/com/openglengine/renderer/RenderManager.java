package com.openglengine.renderer;

import java.util.*;

import com.openglengine.core.*;
import com.openglengine.entitity.*;
import com.openglengine.eventsystem.defaultevents.*;
import com.openglengine.renderer.model.*;
import com.openglengine.renderer.shader.*;
import com.openglengine.util.*;

public class RenderManager extends Manager {
	private static final String VERTEX_SHADER = "vertex.glsl";
	private static final String FRAGMENT_SHADER = "fragment.glsl";

	private StaticShader shader;
	private Renderer renderer;

	private Map<TexturedModel, List<VisibleEntity>> texturedEntities;

	public RenderManager() {
		this.shader = new StaticShader(Engine.SHADER_FOLDER + VERTEX_SHADER, Engine.SHADER_FOLDER + FRAGMENT_SHADER);
		this.renderer = new Renderer();
		this.texturedEntities = new HashMap<>();

		Engine.EVENT_MANAGER.registerListenerForEvent(RenderEvent.class, e -> render());
	}

	public void render() {
		this.shader.startUsingShader();
		this.shader.uploadDiffuseData(Engine.LIGHT_SOURCE);
		this.shader.uploadViewMatrix();
		renderer.render(this.texturedEntities, this.shader);
		this.shader.stopUsingShader();
		texturedEntities.clear();
	}

	public void processEntity(VisibleEntity entity) {
		TexturedModel texturedModel = entity.getModel();
		List<VisibleEntity> batch = texturedEntities.get(texturedModel);
		if (batch == null) {
			batch = new ArrayList<>();
			texturedEntities.put(texturedModel, batch);
		}

		batch.add(entity);
	}

	@Override
	public void cleanup() {
		// TODO implement
	}
}
