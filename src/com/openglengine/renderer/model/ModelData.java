package com.openglengine.renderer.model;

import com.openglengine.util.*;

/**
 * Model Data container class
 * 
 * @author Dominik
 *
 */
public class ModelData extends ReferenceCountedDeletableContainer {
	private float[] vertices;
	private float[] textureCoords;
	private float[] normals;
	private int[] indices;
	private double furthesPoint;

	public ModelData(float[] vertices, float[] textureCoords, float[] normals, int[] indices, double furthesPoint) {
		super();
		this.vertices = vertices;
		this.textureCoords = textureCoords;
		this.normals = normals;
		this.indices = indices;
		this.furthesPoint = furthesPoint;
	}

	public float[] getVertices() {
		return vertices;
	}

	public float[] getTextureCoords() {
		return textureCoords;
	}

	public float[] getNormals() {
		return normals;
	}

	public int[] getIndices() {
		return indices;
	}

	public double getFurthesPoint() {
		return furthesPoint;
	}

	@Override
	protected void forceDelete() {
		this.vertices = null;
		this.textureCoords = null;
		this.normals = null;
		this.indices = null;
	}
}
