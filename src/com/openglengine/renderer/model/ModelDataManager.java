package com.openglengine.renderer.model;

import java.util.*;

import com.openglengine.core.*;
import com.openglengine.util.*;

/**
 * Class for loading model data from files. Currently only .obj (wavefront) is supported
 */
public class ModelDataManager implements ResourceManager {
	private Map<String, ModelData> loadedModelDatas;

	/**
	 * Initialize ModelManager
	 */
	public ModelDataManager() {
		this.loadedModelDatas = new HashMap<>();
	}

	@Override
	public void cleanup() {
		this.loadedModelDatas.clear();
	}

	/**
	 * cleans a model
	 * 
	 * @param texture
	 */
	public void cleanModelData(ModelData modelData) {
		for (String key : this.loadedModelDatas.keySet()) {
			ModelData md = this.loadedModelDatas.get(key);
			if (md != null && md.equals(modelData)) {
				md.cleanup();
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
	public ModelData loadModelData(String modelPath) {
		ModelData loadedModelData = this.loadedModelDatas.get(modelPath);

		if (loadedModelData == null) {
			if (modelPath.endsWith(".obj"))
				loadedModelData = OBJFileLoader.loadOBJ(modelPath);
			else
				Engine.getLogger().err("Model file format not supported for file \"" + modelPath + "\"");

			this.loadedModelDatas.put(modelPath, loadedModelData);
		}

		loadedModelData.use();
		return loadedModelData;
	}
}
