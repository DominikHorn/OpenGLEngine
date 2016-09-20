package com.openglengine.renderer.gui;

import com.openglengine.renderer.shader.*;

public class GuiShader extends Shader {
	private String vertexShader = "#version 140\r\nin vec2 position;out vec2 textureCoords;uniform mat4 transformationMatrix;void main(void){gl_Position = transformationMatrix * vec4(position, 0.0, 1.0);textureCoords = vec2((position.x+1.0)/2.0, 1 - (position.y+1.0)/2.0);}";
	private String fragmentShader = "#version 140\r\nin vec2 textureCoords;out vec4 out_Color;uniform sampler2D guiTexture;void main(void){	out_Color = texture(guiTexture,textureCoords);}";

	public GuiShader() {
		// TODO: make this src folder independent
		this.compileShaderFromSource(vertexShader, fragmentShader);
	}

	@Override
	protected void getAllUniformLocations() {

		super.getAllUniformLocations();
	}

	@Override
	public void uploadGlobalUniforms() {
		// Do nothing for now
	}
}
