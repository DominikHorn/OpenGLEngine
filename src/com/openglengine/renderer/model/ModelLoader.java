package com.openglengine.renderer.model;

import java.nio.*;
import java.util.*;

import org.lwjgl.*;
import org.lwjgl.opengl.*;

import com.openglengine.renderer.shader.*;

/**
 * TODO temporary class
 * 
 * @author Dominik
 *
 */

public class ModelLoader {

	/* Trac created buffers so that we can delete them on shutdown */
	private List<Integer> vaos;
	private List<Integer> vbos;

	public ModelLoader() {
		this.vaos = new ArrayList<>();
		this.vbos = new ArrayList<>();
	}

	public void cleanup() {
		this.vbos.forEach((vbos) -> GL30.glDeleteVertexArrays(vbos));
		this.vaos.forEach((vao) -> GL30.glDeleteVertexArrays(vao));
	}

	public RawModel loadToVAO(float[] positions, int[] indices, ShaderProgram shader) {
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeDataInAttributeList(0, 3, positions);
		unbindVAO();

		return new RawModel(vaoID, indices.length, shader);
	}

	public RawModel loadToVAO(float[] positions, float[] texCoords, int[] indices, ShaderProgram shader) {
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeDataInAttributeList(0, 3, positions);
		storeDataInAttributeList(1, 2, texCoords);
		unbindVAO();

		return new RawModel(vaoID, indices.length, shader);
	}

	private void bindIndicesBuffer(int[] indices) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);

		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = convertToIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}

	private int createVAO() {
		int vaoID = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoID);
		vaos.add(vaoID);

		return vaoID;
	}

	private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);

		FloatBuffer buffer = convertToFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

	private void unbindVAO() {
		GL30.glBindVertexArray(0);
	}

	private IntBuffer convertToIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();

		return buffer;
	}

	private FloatBuffer convertToFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();

		return buffer;
	}
}
