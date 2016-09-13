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
	public static final String UNIFORM_NAME_MODEL_MATRIX = "transformationMatrix";

	/** uniform name of view matrix */
	public static final String UNIFORM_NAME_VIEW_MATRIX = "viewMatrix";

	/** uniform name of projection matrix */
	public static final String UNIFORM_NAME_PROJECTION_MATRIX = "projectionMatrix";

	/** uniform name of projection matrix */
	public static final String UNIFORM_NAME_TEX_ATLAS_ROW_COUNT = "texAtlasRowCount";

	/** uniform name of projection matrix */
	public static final String UNIFORM_NAME_TEX_ATLAS_OFFSET = "texOffset";

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

	/** uniform location of tex atlas row count data */
	private int location_texAtlasRowCount;

	/** uniform location of tex atlas index */
	private int location_texOffset;

	/** Float buffer used to upload 4x4 matrices to shader */
	private static FloatBuffer matrixFloatBuffer = BufferUtils.createFloatBuffer(4 * 4);

	/**
	 * Crafts a new shader program from a vertex and a fragment shader.
	 *
	 * @param vertexShaderPath
	 * @param fragmentShaderPath
	 */
	protected Shader() {
		this.vertexShaderID = this.fragmentShaderID = -1;
	}

	public abstract void uploadGlobalUniforms();

	/**
	 * Upload texture atlas data
	 * 
	 * @param texAtlasRowCount
	 * @param texAtlasIndex
	 */
	public void uploadTextureAtlasData(int texAtlasRowCount, Vector2f texOffset) {
		this.loadInt(this.location_texAtlasRowCount, texAtlasRowCount);
		this.loadVector2f(this.location_texOffset, texOffset);
	}

	/**
	 * Uploads projection and view matrix
	 */
	public void uploadProjectionAndViewMatrix() {
		this.loadMatrix4f(this.location_projectionMatrix, Engine.getProjectionMatrixStack().getCurrentMatrix());
		this.loadMatrix4f(this.location_viewMatrix, Engine.getViewMatrixStack().getCurrentMatrix());
	}

	/**
	 * Uploads the model view matrix
	 */
	public void uploadModelMatrixUniform() {
		this.loadMatrix4f(this.location_modelMatrix, Engine.getModelMatrixStack().getCurrentMatrix());
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

	public void forceDelete() {
		stopUsingShader();
		GL20.glDetachShader(this.programID, this.vertexShaderID);
		GL20.glDetachShader(this.programID, this.fragmentShaderID);
		GL20.glDeleteShader(this.vertexShaderID);
		GL20.glDeleteShader(this.fragmentShaderID);
		GL20.glDeleteProgram(this.programID);
	}

	/**
	 * Compiles this shader from source
	 * 
	 * @param vertexShaderSource
	 * @param fragmentShaderSource
	 */
	public void compileShaderFromSource(String vertexShaderSource, String fragmentShaderSource) {
		if (this.vertexShaderID != -1 || this.fragmentShaderID != -1)
			this.forceDelete();

		this.vertexShaderID = loadShaderFromSource(vertexShaderSource, GL20.GL_VERTEX_SHADER);
		this.fragmentShaderID = loadShaderFromSource(fragmentShaderSource, GL20.GL_FRAGMENT_SHADER);

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
	}

	public void compileShaderFromFiles(String vertexShaderPath, String fragmentShaderPath) {
		this.compileShaderFromSource(getShaderSource(vertexShaderPath), getShaderSource(fragmentShaderPath));
	}

	/**
	 * retrieve all uniform locations in this method. Remember to invoke super.getAllUniformLocations() in custom
	 * implementation
	 */
	protected void getAllUniformLocations() {
		this.location_projectionMatrix = this.getUniformLocation(UNIFORM_NAME_PROJECTION_MATRIX);
		this.location_modelMatrix = this.getUniformLocation(UNIFORM_NAME_MODEL_MATRIX);
		this.location_viewMatrix = this.getUniformLocation(UNIFORM_NAME_VIEW_MATRIX);
		this.location_texAtlasRowCount = this.getUniformLocation(UNIFORM_NAME_TEX_ATLAS_ROW_COUNT);
		this.location_texOffset = this.getUniformLocation(UNIFORM_NAME_TEX_ATLAS_OFFSET);
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
	 * Upload a matrix to the shader
	 * 
	 * @param location
	 * @param matrix
	 */
	public void loadMatrix4f(int location, Matrix4f matrix) {
		matrix.storeInFloatBuffer(matrixFloatBuffer);
		GL20.glUniformMatrix4fv(location, false, matrixFloatBuffer);
	}

	/**
	 * Upload a 3d vector to the shader
	 * 
	 * @param location
	 * @param vector
	 */
	public void loadVector3f(int location, Vector3f vector) {
		GL20.glUniform3f(location, vector.x, vector.y, vector.z);
	}

	/**
	 * Upload a 2d vector to the shader
	 * 
	 * @param location
	 * @param vector
	 */
	public void loadVector2f(int location, Vector2f vector) {
		GL20.glUniform2f(location, vector.x, vector.y);
	}

	/**
	 * Upload a float to the shader
	 * 
	 * @param location
	 * @param value
	 */
	public void loadFloat(int location, float value) {
		GL20.glUniform1f(location, value);
	}

	/**
	 * Upload a float to the shader
	 * 
	 * @param location
	 * @param value
	 */
	public void loadInt(int location, int value) {
		GL20.glUniform1i(location, value);
	}

	private int loadShaderFromSource(String shaderSource, int type) {
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);
		if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE)
			Engine.getLogger().err("Could not compile shader(type = " + type + ")\"" + shaderSource + "\":\n"
					+ GL20.glGetShaderInfoLog(shaderID));

		return shaderID;
	}

	/**
	 * Loads shader source from a file
	 * 
	 * @param file
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

}
