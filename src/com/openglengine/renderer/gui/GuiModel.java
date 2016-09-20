package com.openglengine.renderer.gui;

import java.nio.*;
import java.util.*;

import org.lwjgl.opengl.*;

import com.openglengine.renderer.model.*;
import com.openglengine.util.*;

public class GuiModel extends Model<GuiShader> {
	private List<Integer> vbos;

	public GuiModel(GuiShader shader) {
		super(4, shader, null);
		this.vbos = new ArrayList<>();

		//@formatter:off
		float[] vertices = new float[] {
				-1,  1,
				-1, -1,
				 1,  1,
				 1, -1,
		};
		//@formatter:on

		// Load quad data
		this.loadToVAO(vertices);
	}

	/**
	 * Upload data to opengl as VAO
	 * 
	 * @param vertices
	 * @param indices
	 */
	private void loadToVAO(float[] vertices) {
		// Create new VAO
		this.setVaoID(GL30.glGenVertexArrays());

		// Bind that VAO for modification
		GL30.glBindVertexArray(this.getVaoID());

		// Store vertex data in attribute list slot 0
		this.storeDataInAttributeList(0, 2, vertices);

		// Unbind VAO
		GL30.glBindVertexArray(0);
	}

	/**
	 * Upload float data to a certain attributeList slot
	 * 
	 * @param attributeNumber
	 * @param coordinateSize
	 * @param data
	 */
	private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data) {
		// Gen vbo for data
		int vboID = GL15.glGenBuffers();

		// Add to autodestruct list
		this.vbos.add(vboID);

		// Bind for use
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);

		// Convert to correct data format
		FloatBuffer buffer = Utils.convertToFloatBuffer(data);

		// Upload to vbo
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);

		// Tell opengl how to interpret this attribute
		GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);

		// Unbind vbo
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

	@Override
	protected void forceDelete() {
		super.forceDelete();

		this.vbos.forEach(i -> GL15.glDeleteBuffers(i));
	}
}
