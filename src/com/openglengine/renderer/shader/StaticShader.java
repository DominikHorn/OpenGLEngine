package com.openglengine.renderer.shader;

import com.openglengine.renderer.*;

public class StaticShader extends Shader {
	private int location_lightPosition;
	private int location_lightColor;
	private int location_shineDamper;
	private int location_reflectivity;

	public StaticShader(String vertexShaderPath, String fragmentShaderPath) {
		super(vertexShaderPath, fragmentShaderPath);
	}

	@Override
	protected void getAllUniformLocations() {
		super.getAllUniformLocations();

		location_lightPosition = super.getUniformLocation("lightPosition");
		location_lightColor = super.getUniformLocation("lightColor");
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
	}

	/**
	 * Upload all uniforms that are not going to change throughout the rendering
	 */
	public void uploadDiffuseData(LightSource source) {
		// Upload diffuse light data
		super.loadVector3f(location_lightPosition, source.getPosition());
		super.loadVector3f(location_lightColor, source.getColor());
	}

	/**
	 * upload specular light calculation data
	 * 
	 * @param shineDamper
	 * @param reflectivity
	 */
	public void uploadSpecularData(float shineDamper, float reflectivity) {
		super.loadFloat(location_shineDamper, shineDamper);
		super.loadFloat(location_reflectivity, reflectivity);
	}
}
