package com.openglengine.renderer;

import java.util.*;

import org.lwjgl.opengl.*;

import com.openglengine.core.*;
import com.openglengine.entitity.*;
import com.openglengine.eventsystem.defaultevents.*;
import com.openglengine.renderer.model.*;
import com.openglengine.renderer.shader.*;
import com.openglengine.util.*;

/**
 * Class for managing batch rendering behavior. This is not pretty coding, but necessary to minimize the opengl overhead
 * call amount
 * 
 * @author Dominik
 *
 */
public class RenderManager implements ResourceManager {
	/** Batch rendering storage. This list maintains all entities that need to be rendered */
	private Map<Model<? extends Shader>, List<RenderDelegate>> texturedEntitiesBatch;

	/**
	 * Create new RenderManager
	 */
	public RenderManager() {
		this.texturedEntitiesBatch = new HashMap<>();

		Engine.getGlobalEventManager().registerListenerForEvent(RenderEvent.class, e -> render());
	}

	public void render() {
		/** rendering */
		this.render(this.texturedEntitiesBatch);
		texturedEntitiesBatch.clear();
	}

	public void processRenderObject(Model<? extends Shader> model, RenderDelegate renderDelegate) {
		List<RenderDelegate> batch = texturedEntitiesBatch.get(model);
		if (batch == null) {
			batch = new ArrayList<>();
			texturedEntitiesBatch.put(model, batch);
		}

		batch.add(renderDelegate);
	}

	public void processRenderableEntity(RenderableEntity e) {
		this.processRenderObject(e.model, e);
	}

	public void render(Map<Model<? extends Shader>, List<RenderDelegate>> renderDelegates) {
		renderDelegates.keySet().forEach(model -> {
			// Prepare model
			model.init();

			// Retrieve all entities
			List<RenderDelegate> batch = renderDelegates.get(model);

			// Prepare instance
			batch.forEach(renderDelegate -> {
				model.initRenderDelegate(renderDelegate);

				// Draw prepared data
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getIndicesCount(), GL11.GL_UNSIGNED_INT, 0);

				// Deinit entity rendercode
				model.deinitRenderDelegate(renderDelegate);
			});

			// Unbind all stuff
			model.deinit();
		});
	}

	@Override
	public void cleanup() {
		this.texturedEntitiesBatch.clear();
		this.texturedEntitiesBatch = null;
	}
}
