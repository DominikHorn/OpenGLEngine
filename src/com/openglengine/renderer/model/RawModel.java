package com.openglengine.renderer.model;

import org.lwjgl.opengl.*;

import com.openglengine.core.*;
import com.openglengine.renderer.shader.*;
import com.openglengine.util.*;
import com.openglengine.util.math.*;

/**
 * Raw model that can just render vertex data
 * 
 * @author Dominik
 *
 */
public class RawModel extends Model {
	protected int vaoID;
	protected int indicesCount;
	protected ShaderProgram shader;

	public RawModel(int vaoID, int vertexCount, ShaderProgram shader) {
		this.vaoID = vaoID;
		this.indicesCount = vertexCount;
		this.shader = shader;
	}

	@Override
	public void render(Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		GL30.glBindVertexArray(this.vaoID);
		GL20.glEnableVertexAttribArray(0);
		this.shader.startUsingShader();

		// Setup transform matrix
		TransformMatrixStack tms = Engine.TRANSFORM_MATRIX_STACK;
		tms.push();
		tms.translate(position);
		tms.rotateX(rotX);
		tms.rotateY(rotY);
		tms.rotateZ(rotZ);
		tms.scale(scale, scale, scale);

		this.shader.uploadStandardUniforms();
		GL11.glDrawElements(GL11.GL_TRIANGLES, this.indicesCount, GL11.GL_UNSIGNED_INT, 0);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		tms.pop();
		this.shader.stopUsingShader();
	}
}
