package com.openglengine.util;

import com.openglengine.util.math.*;

public class TransformMatrixStack extends MatrixStack {
	/**
	 * translate by x, y, z
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public void translate(float x, float y, float z) {
		this.getCurrentMatrix().multiply(MathUtil.createTranslationMatrix(x, y, z));
	}

	/**
	 * translate by x, y, z
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public void translate(Vector3f trans) {
		this.translate(trans.x, trans.y, trans.z);
	}

	/**
	 * rotate around all axis
	 * 
	 * @param angle
	 *            in radians
	 * @param x
	 * @param y
	 * @param z
	 */
	public void rotate(float angle, float x, float y, float z) {
		this.getCurrentMatrix().multiply(MathUtil.createRotationMatrix(angle, x, y, z));
	}

	/**
	 * rotate around x axis
	 * 
	 * @param angle
	 */
	public void rotateX(float angle) {
		this.rotate(angle, 1, 0, 0);
	}

	/**
	 * rotate around y axis
	 * 
	 * @param angle
	 */
	public void rotateY(float angle) {
		this.rotate(angle, 0, 1, 0);
	}

	/**
	 * rotate around z axis
	 * 
	 * @param angle
	 */
	public void rotateZ(float angle) {
		this.rotate(angle, 0, 0, 1);
	}

	/**
	 * Scale on each axis by factor
	 *
	 * @param x
	 * @param y
	 * @param z
	 */
	public void scale(float x, float y, float z) {
		this.getCurrentMatrix().multiply(MathUtil.createScaleMatrix(x, y, z));
	}
}
