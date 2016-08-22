package com.openglengine.renderer.model;

/**
 * TODO: REFACTOR
 * 
 * @author Dominik
 *
 */
public class RawModel {
	private int vaoID;
	private int indiciesCount;

	public RawModel(int vaoID, int vertexCount) {
		this.vaoID = vaoID;
		this.indiciesCount = vertexCount;
	}

	public int getVaoID() {
		return vaoID;
	}

	public int getIndiciesCount() {
		return indiciesCount;
	}
}
