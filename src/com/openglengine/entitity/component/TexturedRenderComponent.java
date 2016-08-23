package com.openglengine.entitity.component;

import java.io.*;

import org.lwjgl.opengl.*;

import com.openglengine.core.*;
import com.openglengine.entitity.*;
import com.openglengine.renderer.shader.*;
import com.openglengine.renderer.texture.*;
import com.openglengine.util.*;

public class TexturedRenderComponent extends RenderComponent {
	private Texture texture;

	public TexturedRenderComponent(String texturePath, String modelPath, Shader shader) throws IOException {
		super(modelPath, shader);
		this.texture = Engine.TEXTURE_MANAGER.loadTexture(texturePath);
	}

	@Override
	public void update(Entity entity) {
		// Bind vao for use
		GL30.glBindVertexArray(this.model.getVaoID());

		// Enable first vertex attrib array (Vertex data)
		GL20.glEnableVertexAttribArray(0);

		// Enable vertex attrib array 1 (Texture data)
		GL20.glEnableVertexAttribArray(1);

		// Enable texture 0 TODO: potential overhead
		GL13.glActiveTexture(GL13.GL_TEXTURE0);

		// Bind our texture
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.texture.getTextureID());

		// Load shader
		this.shader.startUsingShader();

		// Setup transform matrix
		ModelMatrixStack tms = Engine.MODEL_MATRIX_STACK;
		tms.push();
		tms.translate(entity.position);
		tms.rotateX(entity.rotX);
		tms.rotateY(entity.rotY);
		tms.rotateZ(entity.rotZ);
		tms.scale(entity.scale, entity.scale, entity.scale);

		// Upload relevant matricies to shader TODO: refactor
		this.shader.uploadStandardUniforms();

		// Draw using our bound data
		GL11.glDrawElements(GL11.GL_TRIANGLES, this.model.getIndicesCount(), GL11.GL_UNSIGNED_INT, 0);

		// Unbind texture
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

		// Disable first vertex attrib array (Vertex data)
		GL20.glDisableVertexAttribArray(0);

		// Disable vertex attrib array 1 (Texture data)
		GL20.glDisableVertexAttribArray(1);

		// Unbind vao
		GL30.glBindVertexArray(0);


		// Undo changes to ModelMatrix
		tms.pop();

		// Unload shader TODO: save overhead by managing shaders centrally
		this.shader.stopUsingShader();

	}
}
