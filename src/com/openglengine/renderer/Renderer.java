package com.openglengine.renderer;

import java.util.*;

import org.lwjgl.opengl.*;

import com.openglengine.core.*;
import com.openglengine.entitity.*;
import com.openglengine.renderer.model.*;
import com.openglengine.renderer.shader.*;
import com.openglengine.util.*;

public class Renderer {
	public void render(Map<TexturedModel, List<Entity>> entities) {
		entities.keySet().forEach(model -> {
			Shader shader = model.getShader();
			shader.startUsingShader();
			shader.uploadGlobalUniforms();

			// Prepare model
			prepareTexturedModel(model, shader);

			// Retrieve all entities
			List<Entity> batch = entities.get(model);

			// Prepare instance
			batch.forEach(entity -> {
				prepareInstance(entity, shader);

				// Draw prepared data
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getIndicesCount(), GL11.GL_UNSIGNED_INT, 0);
			});

			// Unbind all stuff
			unbindTexturedModel();
			shader.stopUsingShader();
		});
	}

	private void prepareTexturedModel(TexturedModel model, Shader shader) {
		// Bind vao for use
		GL30.glBindVertexArray(model.getVaoID());

		// Enable first vertex attrib array (Vertex data)
		GL20.glEnableVertexAttribArray(0);

		// Enable vertex attrib array 1 (Texture data)
		GL20.glEnableVertexAttribArray(1);

		// Enable vertex attrib array 2 (Normal data)
		GL20.glEnableVertexAttribArray(2);

		// Enable texture 0 TODO: potential overhead
		GL13.glActiveTexture(GL13.GL_TEXTURE0);

		// Bind our texture
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getTextureID());

		// Upload specular data
		shader.uploadModelUniforms(model);
	}

	private void prepareInstance(Entity entity, Shader shader) {
		TransformationMatrixStack tms = Engine.getModelMatrixStack();
		tms.push();
		tms.translate(entity.position);
		tms.rotateX(entity.rotation.x);
		tms.rotateY(entity.rotation.y);
		tms.rotateZ(entity.rotation.z);
		tms.scale(entity.scale.x, entity.scale.y, entity.scale.z);

		shader.uploadEntityUniforms(entity);
		tms.pop();
	}

	private void unbindTexturedModel() {
		// Disable first vertex attrib array (Vertex data)
		GL20.glDisableVertexAttribArray(0);

		// Disable vertex attrib array 1 (Texture data)
		GL20.glDisableVertexAttribArray(1);

		// Disable vertex attrib array 2 (Normal data)
		GL20.glDisableVertexAttribArray(2);

		// Unbind vao
		GL30.glBindVertexArray(0);
	}
}
