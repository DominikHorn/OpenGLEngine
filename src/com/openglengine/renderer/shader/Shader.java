package com.openglengine.renderer.shader;

import java.io.*;
import java.nio.*;

import org.lwjgl.*;
import org.lwjgl.opengl.*;

import com.openglengine.core.*;
import com.openglengine.util.math.*;

// TODO: refactor. f.e. implement ShaderManager etc

/**
 * Standard shader consisting of a vertex shader and fragment shader pair. All vertexShaders loaded with ShaderProgram
 * will have all three default matricies automatically uploaded
 * 
 * 
 * @author Dominik
 *
 */
public abstract class Shader {
	/** uniform name of model matrix */
	private static final String UNIFORM_NAME_MODEL_MATRIX = "transformationMatrix";

	/** uniform name of view matrix */
	private static final String UNIFORM_NAME_VIEW_MATRIX = "viewMatrix";

	/** uniform name of projection matrix */
	private static final String UNIFORM_NAME_PROJECTION_MATRIX = "projectionMatrix";

	/** shader program id */
	private int programID;

	/** vertex shader id */
	private int vertexShaderID;

	/** fragment shader id */
	private int fragmentShaderID;

	/** uniform location of model matrix */
	private int location_modelMatrix;

	/** uniform location of projection matrix */
	private int location_projectionMatrix;

	/** uniform location of view matrix */
	private int location_viewMatrix;

	/** Float buffer used to upload 4x4 matrices to shader */
	private static FloatBuffer matrixFloatBuffer = BufferUtils.createFloatBuffer(4 * 4);

	/**
	 * Crafts a new shader program from a vertex and a fragment shader. This method will autoupload the currently set
	 * projection matrix to the shader
	 *
	 * @param vertexShaderPath
	 * @param fragmentShaderPath
	 */
	public Shader(String vertexShaderPath, String fragmentShaderPath) {
		// Create and load both shaders
		this.vertexShaderID = loadShader(vertexShaderPath, GL20.GL_VERTEX_SHADER);
		this.fragmentShaderID = loadShader(fragmentShaderPath, GL20.GL_FRAGMENT_SHADER);

		// Create shader program
		this.programID = GL20.glCreateProgram();

		// Attach both loaded shader programs
		GL20.glAttachShader(this.programID, this.vertexShaderID);
		GL20.glAttachShader(this.programID, this.fragmentShaderID);

		// Bind array slots in vao used for rendering to shader attributes
		this.bindAttributes();

		// Compile program
		GL20.glLinkProgram(this.programID);
		GL20.glValidateProgram(this.programID);

		// Retrieve uniform locations from shader
		this.getAllUniformLocations();

		// Upload projection matrix exactly once
		this.uploadProjectionMatrix();
	}

	/**
	 * Uploads the projection matrix
	 */
	public void uploadProjectionMatrix() {
		this.startUsingShader();
		this.loadMatrix4f(this.location_projectionMatrix, Engine.PROJECTION_MATRIX_STACK.getCurrentMatrix());
		this.stopUsingShader();
	}

	/**
	 * Upload the model matrix to the shader
	 */
	public void uploadModelMatrix() {
		this.loadMatrix4f(this.location_modelMatrix, Engine.MODEL_MATRIX_STACK.getCurrentMatrix());
	}

	/**
	 * Upload the view matrix to the shader
	 */
	public void uploadViewMatrix() {
		this.loadMatrix4f(this.location_viewMatrix, Engine.VIEW_MATRIX_STACK.getCurrentMatrix());
	}

	/**
	 * Enable this shader program. Must be invoked before rendering to use this shader
	 */
	public void startUsingShader() {
		GL20.glUseProgram(this.programID);
	}

	/**
	 * Disable this shader program
	 */
	public void stopUsingShader() {
		GL20.glUseProgram(0);
	}

	/**
	 * Delete/Clean this shader
	 */
	public void cleanup() {
		stopUsingShader();
		GL20.glDetachShader(this.programID, this.vertexShaderID);
		GL20.glDetachShader(this.programID, this.fragmentShaderID);
		GL20.glDeleteShader(this.vertexShaderID);
		GL20.glDeleteShader(this.fragmentShaderID);
		GL20.glDeleteProgram(this.programID);
	}

	/**
	 * retrieve all uniform locations in this method. Remember to invoke super.getAllUniformLocations() in custom
	 * implementation
	 */
	protected void getAllUniformLocations() {
		this.location_projectionMatrix = this.getUniformLocation(UNIFORM_NAME_PROJECTION_MATRIX);
		this.location_modelMatrix = this.getUniformLocation(UNIFORM_NAME_MODEL_MATRIX);
		this.location_viewMatrix = this.getUniformLocation(UNIFORM_NAME_VIEW_MATRIX);
	}

	/**
	 * Retrieve a uniform's location by name
	 * 
	 * @param uniformName
	 * @return
	 */
	protected int getUniformLocation(String uniformName) {
		return GL20.glGetUniformLocation(this.programID, uniformName);
	}

	/**
	 * Binds an attribute to a vao list index
	 * 
	 * @param attribute
	 *            vao list index
	 * @param variableName
	 *            attribute name in shader
	 */
	protected void bindAttribute(int attribute, String variableName) {
		GL20.glBindAttribLocation(this.programID, attribute, variableName);
	}

	/**
	 * Use this method to bind your attributes using bindAttribute(). This method will bind default attributes
	 * "position" and "textureCoords"
	 */
	protected void bindAttributes() {
		// Bind default attributes
		this.bindAttribute(0, "position");
		this.bindAttribute(1, "textureCoords");
		this.bindAttribute(2, "normal");
	}

	/**
	 * Upload a float to the shader
	 * 
	 * @param location
	 * @param value
	 */
	protected void loadFloat(int location, float value) {
		GL20.glUniform1f(location, value);
	}

	/**
	 * Upload a vector to the shader
	 * 
	 * @param location
	 * @param vector
	 */
	protected void loadVector3f(int location, Vector3f vector) {
		GL20.glUniform3f(location, vector.x, vector.y, vector.z);
	}

	/**
	 * Upload a matrix to the shader
	 * 
	 * @param location
	 * @param matrix
	 */
	protected void loadMatrix4f(int location, Matrix4f matrix) {
		matrix.storeInFloatBuffer(matrixFloatBuffer);
		GL20.glUniformMatrix4fv(location, false, matrixFloatBuffer);
	}

	/**
	 * Loads and compiles a shader
	 * 
	 * @param file
	 * @param type
	 * @return
	 */
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
			Engine.LOGGER.err("Could not compile shader \"" + file + "\":\n" + GL20.glGetShaderInfoLog(shaderID));

		return shaderID;
	}
}