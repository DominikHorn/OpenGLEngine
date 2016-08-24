package com.openglengine.renderer.model;

import java.io.*;
import java.util.*;

import com.openglengine.core.*;
import com.openglengine.util.*;
import com.openglengine.util.math.*;

/**
 * Class for loading model data from files. Currently only .obj (wavefront) is supported
 */
public class ModelManager extends Manager {
	private Map<String, Model> loadedModels;

	/**
	 * Initialize ModelManager
	 */
	public ModelManager() {
		this.loadedModels = new HashMap<>();
	}

	@Override
	public void cleanup() {
		this.loadedModels.keySet().forEach(key -> this.loadedModels.get(key).forceDelete());
	}

	/**
	 * cleans a model
	 * 
	 * @param texture
	 */
	public void cleanModel(Model model) {
		for (String key : this.loadedModels.keySet()) {
			Model mod = this.loadedModels.get(key);
			if (mod != null && mod.equals(model)) {
				this.loadedModels.put(key, null);

				break;
			}
		}
	}

	/**
	 * Load a model file. This method will cache previously loaded models and not reload each time
	 * 
	 * @param modelPath
	 * @return
	 */
	public TexturedModel getTexturedModel(String modelPath) {
		TexturedModel loadedModel = (TexturedModel) this.loadedModels.get(modelPath);

		if (loadedModel == null) {
			if (modelPath.endsWith(".obj"))
				loadedModel = this.loadObjModel(modelPath);
			else
				Engine.getLogger().err("Model file format not supported for file \"" + modelPath + "\"");

			this.loadedModels.put(modelPath, loadedModel);

		}

		loadedModel.use();
		return loadedModel;
	}

	/**
	 * Loads an obj
	 * 
	 * TODO: sanity checking and reimplement efficiently. REFACTOR TODO: Allow for dynamic models (only static supported
	 * atm)
	 * 
	 * @param fileBasePath
	 * @return
	 */
	private TexturedModel loadObjModel(String modelPath) {
		FileReader fr = null;
		try {
			fr = new FileReader(new File(modelPath));
		} catch (FileNotFoundException e) {
			Engine.getLogger().err("Model Obj file \"" + modelPath + "\" not found");
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
					processObjVertex(currentLine[i].split("/"), indices, textureCoords, normals, textureArray,
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
			Engine.getLogger().err("Could not load obj file \"" + modelPath + "\"");
		}

		return new TexturedModel(verticesArray, textureArray, normalsArray, indicesArray);
	}

	/***
	 * Rearranges data from obj file to fit the way opengl needs it
	 * 
	 * @param vertexData
	 * @param indices
	 * @param texCoords
	 * @param normals
	 * @param textureArray
	 * @param normalsArray
	 */
	private static void processObjVertex(String[] vertexData, List<Integer> indices, List<Vector2f> texCoords,
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
}
