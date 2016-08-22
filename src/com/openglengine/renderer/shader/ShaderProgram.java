package com.openglengine.renderer.shader;

import java.io.*;
import java.nio.*;

import org.lwjgl.*;
import org.lwjgl.opengl.*;

import com.openglengine.core.*;
import com.openglengine.util.math.*;

/**
 * standard shader consisting of a vertex shader and fragment shader pair. All vertexShaders loaded with ShaderProgram
 * will have all three default matricies automatically uploaded
 * 
 * @author Dominik
 *
 */
public abstract class ShaderProgram {
	private static final String UNIFORM_NAME_TRANSFORMATION_MATRIX = "transformationMatrix";
	private static final String UNIFORM_NAME_PROJECTION_MATRIX = "projectionMatrix";
	private static final String UNIFORM_NAME_VIEW_MATRIX = "viewMatrix";

	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;

	/* standard shader uniform variables */
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;

	private static FloatBuffer matrixFloatBuffer = BufferUtils.createFloatBuffer(4 * 4);

	public ShaderProgram(String vertexShaderPath, String fragmentShaderPath) {
		this.vertexShaderID = loadShader(vertexShaderPath, GL20.GL_VERTEX_SHADER);
		this.fragmentShaderID = loadShader(fragmentShaderPath, GL20.GL_FRAGMENT_SHADER);
		this.programID = GL20.glCreateProgram();
		GL20.glAttachShader(this.programID, this.vertexShaderID);
		GL20.glAttachShader(this.programID, this.fragmentShaderID);
		this.bindAttributes();
		GL20.glLinkProgram(this.programID);
		GL20.glValidateProgram(this.programID);
		getAllUniformLocations();

		// Upload projection matrix only once
		this.startUsingShader();
		this.loadMatrix(this.location_projectionMatrix, Engine.PROJECTION_MATRIX_STACK.getCurrentMatrix());
		this.stopUsingShader();
	}

	public void uploadStandardUniforms() {
		this.loadMatrix(this.location_transformationMatrix, Engine.TRANSFORM_MATRIX_STACK.getCurrentMatrix());
		this.loadMatrix(this.location_viewMatrix, Engine.VIEW_MATRIX_STACK.getCurrentMatrix());
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

	protected void getAllUniformLocations() {
		this.location_projectionMatrix = this.getUniformLocation(UNIFORM_NAME_PROJECTION_MATRIX);
		this.location_transformationMatrix = this.getUniformLocation(UNIFORM_NAME_TRANSFORMATION_MATRIX);
		this.location_viewMatrix = this.getUniformLocation(UNIFORM_NAME_VIEW_MATRIX);
	}

	protected int getUniformLocation(String uniformName) {
		return GL20.glGetUniformLocation(this.programID, uniformName);
	}

	protected void bindAttribute(int attribute, String variableName) {
		GL20.glBindAttribLocation(this.programID, attribute, variableName);
	}

	protected abstract void bindAttributes();

	protected void loadFloat(int location, float value) {
		GL20.glUniform1f(location, value);
	}

	protected void loadVector(int location, Vector3f vector) {
		GL20.glUniform3f(location, vector.x, vector.y, vector.z);
	}

	protected void loadMatrix(int location, Matrix4f matrix) {
		matrix.storeInFloatBuffer(matrixFloatBuffer);
		GL20.glUniformMatrix4fv(location, false, matrixFloatBuffer); // TODO: breaking?!
	}

	// TODO: introduce shaderManager and shaderProgramManager
	private int loadShader(String file, int type) {
		StringBuilder shaderSource = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = reader.readLine()) != null)
				shaderSource.append(line).append("\n");

		} catch (FileNotFoundException e) {
			Engine.LOGGER.err("Shader file \"" + file + "\" was not found");
		} catch (IOException e) {
			e.printStackTrace(System.err);
			Engine.LOGGER.err("IOException while reading file \"" + file + "\":");
		}

		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);
		if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE)
			Engine.LOGGER
					.err("Could not compile shader \"" + file + "\":\n" + GL20.glGetShaderInfoLog(shaderID));

		return shaderID;
	}
}
