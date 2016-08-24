package com.openglengine.renderer.shader;

import java.io.*;
import java.util.*;

import com.openglengine.core.*;
import com.openglengine.util.*;

public class ShaderManager<T extends Shader> extends Manager {
	/** All loaded vertex shaders */
	private Map<String, T> shaders;

	public ShaderManager() {
		this.shaders = new HashMap<>();
	}

	public T loadShaderFromFiles(Class<T> shaderClass, String vertexShaderPath, String fragmentShaderPath) {
		T shader = this.shaders.get(vertexShaderPath + "|" + fragmentShaderPath);
		if (shader == null) {
			// Load shader
			try {
				shader = shaderClass.newInstance();
			} catch (Exception e) {
				// Ignore expections
			}

			shader.compileShaderFromSource(getShaderSource(vertexShaderPath), getShaderSource(fragmentShaderPath));
			this.shaders.put(vertexShaderPath + "|" + fragmentShaderPath, shader);
		}

		shader.use();
		return shader;
	}

	/**
	 * Loads shader source from a file
	 * 
	 * @param file
	 * @param type
	 * @return
	 */
	private String getShaderSource(String file) {
		StringBuilder shaderSource = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = reader.readLine()) != null)
				shaderSource.append(line).append("\n");

		} catch (FileNotFoundException e) {
			Engine.getLogger().err("Shader file \"" + file + "\" was not found");
		} catch (IOException e) {
			e.printStackTrace(System.err);
			Engine.getLogger().err("IOException while reading file \"" + file + "\":");
		}

		return shaderSource.toString();
	}

	@Override
	public void cleanup() {
		this.shaders.keySet().forEach(key -> {
			this.shaders.get(key).forceDelete();
		});
	}
}
