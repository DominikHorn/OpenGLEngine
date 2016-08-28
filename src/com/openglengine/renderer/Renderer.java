package com.openglengine.renderer;

import java.util.*;

import org.lwjgl.opengl.*;

import com.openglengine.entitity.*;
import com.openglengine.renderer.model.*;
import com.openglengine.renderer.shader.*;

public class Renderer {
	public void render(Map<Model, List<Entity>> entities) {
		entities.keySet().forEach(model -> {
			Shader shader = model.getShader();
			shader.startUsingShader();
			shader.uploadGlobalUniforms();

			// Prepare model
			model.initRendercode();

			// Prepare model material
			model.getMaterial().initRendercode();

			// // Upload model data
			shader.uploadModelUniforms(model);

			// Retrieve all entities
			List<Entity> batch = entities.get(model);

			// Prepare instance
			batch.forEach(entity -> {
				// Allow entity to init render code
				entity.initRendercode();

				// // Upload to shader
				shader.uploadEntityUniforms(entity);

				// Draw prepared data
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getIndicesCount(), GL11.GL_UNSIGNED_INT, 0);

				// Deinit entity rendercode
				entity.deinitRenderCode();
			});

			// Unbind all stuff
			model.deinitRendercode();
			model.getMaterial().deinitRendercode();
			shader.stopUsingShader();
		});
	}
}
