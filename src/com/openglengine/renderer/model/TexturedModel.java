package com.openglengine.renderer.model;

import org.lwjgl.opengl.*;

import com.openglengine.core.*;
import com.openglengine.renderer.shader.*;
import com.openglengine.renderer.texture.*;
import com.openglengine.util.*;
import com.openglengine.util.math.*;

/**
 * Textured model, as the name would imply
 * 
 * @author Dominik
 *
 */
public class TexturedModel extends RawModel {
	private Texture texture;

	public TexturedModel(int vaoID, int indicesCount, ShaderProgram program, Texture texture) {
		super(vaoID, indicesCount, program);
		this.texture = texture;
	}

	public TexturedModel(RawModel rawModel, Texture texture) {
		super(rawModel.vaoID, rawModel.indicesCount, rawModel.shader);
		this.texture = texture;
	}

	@Override
	public void render(Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		// Bind our vao
		GL30.glBindVertexArray(this.vaoID);

		// Enable vertex data in vao (vbo @ vao index 0)
		GL20.glEnableVertexAttribArray(0);

		// Enable texture data in vao (vbo @ vao index 0)
		GL20.glEnableVertexAttribArray(1);

		// Load transformation matrix
		TransformMatrixStack transformMatrix = Engine.TRANSFORM_MATRIX_STACK;

		// push copy on stack
		transformMatrix.push();

		// Apply transformations
		transformMatrix.translate(position);
		transformMatrix.rotateX(rotX);
		transformMatrix.rotateY(rotY);
		transformMatrix.rotateZ(rotZ);
		transformMatrix.scale(scale, scale, scale);

		// Enable shader program
		this.shader.startUsingShader();

		// Upload standard uniforms (view, projection and model matrix)
		this.shader.uploadStandardUniforms();

		// Enable texture 0
		GL13.glActiveTexture(GL13.GL_TEXTURE0);

		// Bind our texture to that
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.texture.getTextureID());

		// Draw as glTriangles TODO: draw triangle strips
		GL11.glDrawElements(GL11.GL_TRIANGLES, this.indicesCount, GL11.GL_UNSIGNED_INT, 0);

		// Disable vertex data
		GL20.glDisableVertexAttribArray(0);

		// Disable texture data
		GL20.glDisableVertexAttribArray(1);

		// Unbind vao
		GL30.glBindVertexArray(0);

		// restore copy from stack
		transformMatrix.pop();

		// stop using this shader
		this.shader.stopUsingShader();
	}
}
