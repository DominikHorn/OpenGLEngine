package com.openglengine.renderer.model;

import java.io.*;
import java.nio.*;
import java.util.*;

import org.lwjgl.*;
import org.lwjgl.opengl.*;

import com.openglengine.core.*;
import com.openglengine.renderer.shader.*;
import com.openglengine.util.math.*;

/**
 * TODO temporary class
 * 
 * @author Dominik
 *
 */

/* TODO: refactor to singleton model manager */
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

	/**
	 * Loads an obj
	 * 
	 * TODO: sanity checking and reimplement efficiently. REFACTOR
	 * 
	 * @param fileBasePath
	 * @return
	 */
	public RawModel loadObjModelToVAO(String filePath, ShaderProgram shader) {
		FileReader fr = null;
		try {
			fr = new FileReader(new File(filePath));
		} catch (FileNotFoundException e) {
			Engine.LOGGER.err("Model Obj file \"" + filePath + "\" not found");
		}

		float[] verticesArray = null;
		float[] normalsArray = null;
		float[] textureArray = null;
		int[] indicesArray = null;

		try (BufferedReader reader = new BufferedReader(fr)) {
			String line = null;
			List<Vector3f> vertices = new ArrayList<>();
			List<Vector2f> textureCoords = new ArrayList<>();
			List<Vector3f> normals = new ArrayList<>();
			List<Integer> indices = new ArrayList<>();


			boolean dataSector = true;
			while (dataSector) {
				line = reader.readLine();
				String[] currentLine = line.split(" ");
				if (line.startsWith("v ")) {
					Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]),
							Float.parseFloat(currentLine[3]));
					vertices.add(vertex);
				} else if (line.startsWith("vn ")) {
					Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]),
							Float.parseFloat(currentLine[3]));
					normals.add(normal);
				} else if (line.startsWith("vt ")) {
					Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]));
					textureCoords.add(texture);
				} else if (line.startsWith("f ")) {
					dataSector = false;
					continue;
				}
			}

			// Allocate buffers for next step
			textureArray = new float[vertices.size() * 2];
			normalsArray = new float[vertices.size() * 3];

			// Rearrange data so that f.e. the texCoord for vertex 1 will be @ textureArray[1] etc
			do {
				String[] currentLine = line.split(" ");

				for (int i = 1; i < 4; i++)
					processVertex(currentLine[i].split("/"), indices, textureCoords, normals, textureArray,
							normalsArray);
			} while ((line = reader.readLine()) != null);

			// Allocate actual data buffers for data that will be sent via opengl
			verticesArray = new float[vertices.size() * 3];
			indicesArray = new int[indices.size()];

			// Convert vertex data
			int vertexPointer = 0;
			for (Vector3f vertex : vertices) {
				verticesArray[vertexPointer++] = vertex.x;
				verticesArray[vertexPointer++] = vertex.y;
				verticesArray[vertexPointer++] = vertex.z;
			}
			
			// Convert indices data
			for (int i = 0; i < indices.size(); i++)
				indicesArray[i] = indices.get(i);
		} catch (Exception e) {
			e.printStackTrace();
			Engine.LOGGER.err("Could not load obj file \"" + filePath + "\"");
		}

		return this.loadToVAO(verticesArray, textureArray, indicesArray, shader);
	}

	private static void processVertex(String[] vertexData, List<Integer> indices, List<Vector2f> texCoords,
			List<Vector3f> normals, float[] textureArray, float[] normalsArray) {
		int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
		indices.add(currentVertexPointer);

		Vector2f currentTex = texCoords.get(Integer.parseInt(vertexData[1]) - 1);
		textureArray[currentVertexPointer * 2] = currentTex.x;
		textureArray[currentVertexPointer * 2 + 1] = 1 - currentTex.y;

		Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2]) - 1);
		normalsArray[currentVertexPointer * 3] = currentNorm.x;
		normalsArray[currentVertexPointer * 3 + 1] = currentNorm.y;
		normalsArray[currentVertexPointer * 3 + 2] = currentNorm.z;

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
