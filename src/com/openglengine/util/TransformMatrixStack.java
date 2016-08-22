package com.openglengine.util;

import java.util.*;

import com.openglengine.util.math.*;

public class TransformMatrixStack {
	private List<Matrix4f> matricesStack;
	private Matrix4f currentMatrix;

	public TransformMatrixStack() {
		this.currentMatrix = new Matrix4f();
		this.matricesStack = new ArrayList<>();
		this.matricesStack.add(this.currentMatrix);
	}

	/**
	 * returns the current matrix
	 * 
	 * @return
	 */
	public Matrix4f getCurrentMatrix() {
		return this.currentMatrix;
	}

	/**
	 * pops the last matrix into the current matrix
	 */
	public void pop() {
		this.currentMatrix = this.matricesStack.remove(this.matricesStack.size() - 1);
	}

	/**
	 * push last matrix onto stack (save for later, until next pop)
	 */
	public void push() {
		this.matricesStack.add(this.currentMatrix.getCopy());
	}

	/**
	 * load identity matrix into current matrix
	 */
	public void loadIdentity() {
		this.currentMatrix.loadIdentityMatrix();
	}

	/**
	 * translate by x, y, z
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public void translate(float x, float y, float z) {
		this.currentMatrix.multiply(MathUtil.createTranslationMatrix(x, y, z));
	}

	/**
	 * rotate
	 * 
	 * @param angle
	 *            in radians
	 * @param x
	 * @param y
	 * @param z
	 */
	public void rotate(float angle, float x, float y, float z) {
		this.currentMatrix.multiply(MathUtil.createRotationMatrix(angle, x, y, z));
	}

	/**
	 * Scale on each axis by factor
	 *
	 * @param x
	 * @param y
	 * @param z
	 */
	public void scale(float x, float y, float z) {
		this.currentMatrix.multiply(MathUtil.createScaleMatrix(x, y, z));
	}
}
