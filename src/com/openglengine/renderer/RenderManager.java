package com.openglengine.renderer;

import java.util.*;

import com.openglengine.core.*;
import com.openglengine.entitity.*;
import com.openglengine.eventsystem.defaultevents.*;
import com.openglengine.renderer.model.*;
import com.openglengine.util.*;

/**
 * Class for managing batch rendering behavior. This is not pretty coding, but necessary to minimize the opengl overhead
 * call amount
 * 
 * @author Dominik
 *
 */
public class RenderManager implements ResourceManager {
	/** actual renderer containing rendering code */
	private Renderer renderer;

	/** Batch rendering storage. This list maintains all entities that need to be rendered */
	private Map<Model, List<RenderDelegate>> texturedEntitiesBatch;

	/**
	 * Create new RenderManager
	 */
	public RenderManager() {
		this.renderer = new Renderer();
		this.texturedEntitiesBatch = new HashMap<>();

		Engine.getGlobalEventManager().registerListenerForEvent(RenderEvent.class, e -> render());
	}

	public void render() {
		/** rendering */
		renderer.render(this.texturedEntitiesBatch);
		texturedEntitiesBatch.clear();
	}

	public void processRenderObject(Model model, RenderDelegate renderDelegate) {
		List<RenderDelegate> batch = texturedEntitiesBatch.get(model);
		if (batch == null) {
			batch = new ArrayList<>();
			texturedEntitiesBatch.put(model, batch);
		}

		batch.add(renderDelegate);
	}

	public void processEntity(Entity e) {
		this.processRenderObject(e.model, e);
	}

	@Override
	public void cleanup() {
		this.texturedEntitiesBatch.clear();
		this.texturedEntitiesBatch = null;
	}
}
