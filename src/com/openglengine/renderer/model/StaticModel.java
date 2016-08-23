package com.openglengine.renderer.model;

import java.nio.*;
import java.util.*;

import org.lwjgl.*;
import org.lwjgl.opengl.*;

/**
 * Container class for static models that will never change
 * 
 * @author Dominik
 *
 */
public class StaticModel extends Model {
	/** Internal list used to keep track of all the vbos that were create for this model */
	private List<Integer> vbos;

	public StaticModel(float[] positions, int[] indices) {
		this(positions, null, indices);
	}

	public StaticModel(float[] positions, float[] texCoords, int[] indices) {
		// Initialize to 0
		super(0, indices.length);

		// Initialize
		this.vbos = new ArrayList<>();

		// Load actual values
		this.loadToVAO(positions, texCoords, indices);
	}

	@Override
	protected void forceDelete() {
		super.forceDelete();
		this.vbos.forEach(vbo -> GL15.glDeleteBuffers(vbo));
	}

	/**
	 * Load data to vao
	 * 
	 * @param vertices
	 * @param texCoords
	 * @param indices
	 */
	private void loadToVAO(float[] vertices, float[] texCoords, int[] indices) {
		// Create new VAO
		this.vaoID = GL30.glGenVertexArrays();

		// Bind that VAO for modification
		GL30.glBindVertexArray(this.vaoID);

		// Create indices VBO
		int indicesID = GL15.glGenBuffers();

		// Add to list for destruction upon force delete
		this.vbos.add(indicesID);

		// Bind Indices buffer
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesID);

		// Convert indices data to appropriate format
		IntBuffer buffer = convertToIntBuffer(indices);

		// Upload buffer data
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);

		// Store vertex data in attribute list 0
		this.storeDataInAttributeList(0, 3, vertices);

		if (texCoords != null)
			// Store tex data in attribute list 1
			this.storeDataInAttributeList(1, 2, texCoords);

		// Unbind VAO
		GL30.glBindVertexArray(0);
	}

	/**
	 * Convert int array to IntBuffer
	 * 
	 * @param data
	 * @return
	 */
	private IntBuffer convertToIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();

		return buffer;
	}

	/**
	 * Convert float array to FloatBuffer
	 * 
	 * @param data
	 * @return
	 */
	private FloatBuffer convertToFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();

		return buffer;
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
		FloatBuffer buffer = convertToFloatBuffer(data);

		// Upload to vbo
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);

		// Tell opengl how to interpret this attribute
		GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);

		// Unbind vbo
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
}
