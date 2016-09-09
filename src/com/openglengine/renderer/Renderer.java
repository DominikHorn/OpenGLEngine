package com.openglengine.renderer;

import java.util.*;

import org.lwjgl.opengl.*;

import com.openglengine.renderer.model.*;
import com.openglengine.renderer.shader.*;

public class Renderer {
	public void render(Map<Model, List<RenderDelegate>> entities) {
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
			List<RenderDelegate> batch = entities.get(model);

			// Prepare instance
			batch.forEach(renderDelegate -> {
				// Allow entity to init render code
				renderDelegate.initRendercode();

				// Upload to shader
				shader.uploadRenderDelegateUniforms(renderDelegate);

				// Draw prepared data
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getIndicesCount(), GL11.GL_UNSIGNED_INT, 0);

				// Deinit entity rendercode
				renderDelegate.deinitRendercode();
			});

			// Unbind all stuff
			model.deinitRendercode();
			model.getMaterial().deinitRendercode();
			shader.stopUsingShader();
		});
	}
}
