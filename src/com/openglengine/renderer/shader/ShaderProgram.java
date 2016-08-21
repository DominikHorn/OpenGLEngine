package com.openglengine.renderer.shader;

import java.io.*;

import org.lwjgl.opengl.*;

import com.openglengine.core.*;

public abstract class ShaderProgram {
	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;

	public ShaderProgram(String vertexShaderPath, String fragmentShaderPath) {
		this.vertexShaderID = loadShader(vertexShaderPath, GL20.GL_VERTEX_SHADER);
		this.fragmentShaderID = loadShader(fragmentShaderPath, GL20.GL_FRAGMENT_SHADER);
		this.programID = GL20.glCreateProgram();
		GL20.glAttachShader(this.programID, this.vertexShaderID);
		GL20.glAttachShader(this.programID, this.fragmentShaderID);
		this.bindAttributes();
		GL20.glLinkProgram(this.programID);
		GL20.glValidateProgram(this.programID);
	}

	public void startUsingShader() {
		GL20.glUseProgram(this.programID);
	}

	public void stopUsingShader() {
		GL20.glUseProgram(0);
	}

	public void cleanup() {
		stopUsingShader();
		GL20.glDetachShader(this.programID, this.vertexShaderID);
		GL20.glDetachShader(this.programID, this.fragmentShaderID);
		GL20.glDeleteShader(this.vertexShaderID);
		GL20.glDeleteShader(this.fragmentShaderID);
		GL20.glDeleteProgram(this.programID);
	}

	protected void bindAttribute(int attribute, String variableName) {
		GL20.glBindAttribLocation(this.programID, attribute, variableName);
	}

	protected abstract void bindAttributes();

	private int loadShader(String file, int type) {
		StringBuilder shaderSource = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = reader.readLine()) != null)
				shaderSource.append(line).append("\n");

		} catch (FileNotFoundException e) {
			OpenGLGame.LOGGER.err("Shader file \"" + file + "\" was not found");
		} catch (IOException e) {
			e.printStackTrace(System.err);
			OpenGLGame.LOGGER.err("IOException while reading file \"" + file + "\":");
		}
		
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);
		if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE)
			OpenGLGame.LOGGER.err("Could not compile shader \"" + file + "\":\n" + GL20.glGetShaderInfoLog(shaderID));

		return shaderID;
	}
}
