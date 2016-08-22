package com.openglengine.renderer;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.*;

import com.openglengine.core.*;
import com.openglengine.entities.*;
import com.openglengine.renderer.model.*;
import com.openglengine.renderer.shader.*;
import com.openglengine.util.*;

/**
 * TODO REFACTOR
 * 
 * @author Dominik
 *
 */
public class Renderer {

	public void prepare() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	public void render(BaseEntity entity, ShaderProgram shader) {
		TexturedModel texturedModel = entity.getModel();
		RawModel model = texturedModel.getRawModel();
		GL30.glBindVertexArray(model.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);

		TransformMatrixStack transformMatrix = OpenGLEngine.TRANSFORM_MATRIX_STACK;
		transformMatrix.push();
		transformMatrix.loadIdentity();
		transformMatrix.translate(entity.getPosition());
		transformMatrix.rotateX(entity.getRotX());
		transformMatrix.rotateY(entity.getRotY());
		transformMatrix.rotateZ(entity.getRotZ());
		transformMatrix.scale(entity.getScale(), entity.getScale(), entity.getScale());

		shader.startUsingShader();
		shader.loadTransformationMatrix(transformMatrix.getCurrentMatrix());

		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturedModel.getTexture().getTextureID());
		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getIndiciesCount(), GL11.GL_UNSIGNED_INT, 0);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
		transformMatrix.pop();
		shader.stopUsingShader();
	}
}
