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
	private Map<TexturedModel, List<Entity>> texturedEntities;

	/**
	 * Create new RenderManager
	 */
	public RenderManager() {
		this.renderer = new Renderer();
		this.texturedEntities = new HashMap<>();

		Engine.getGlobalEventManager().registerListenerForEvent(RenderEvent.class, e -> render());
	}

	public void render() {
		/** rendering */
		renderer.render(this.texturedEntities);
		texturedEntities.clear();
	}

	public void processEntity(Entity entity) {
		TexturedModel texturedModel = (TexturedModel) entity.getValueProperty(DefaultEntityProperties.PROPERTY_MODEL);
		List<Entity> batch = texturedEntities.get(texturedModel);
		if (batch == null) {
			batch = new ArrayList<>();
			texturedEntities.put(texturedModel, batch);
		}

		batch.add(entity);
	}

	@Override
	public void cleanup() {
		this.texturedEntities.clear();
		this.texturedEntities = null;
	}
}
