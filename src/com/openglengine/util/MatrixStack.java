package com.openglengine.util;

import java.util.*;

import com.openglengine.util.math.*;

/**
 * Matrix stack implementation similar to legacy opengl matrix handeling code
 * 
 * @author Dominik
 *
 */
public class MatrixStack {
	protected List<Matrix4f> matricesStack;

	public MatrixStack() {
		this.matricesStack = new ArrayList<>();
		this.matricesStack.add(new Matrix4f());
	}

	/**
	 * returns the current matrix
	 * 
	 * @return
	 */
	public Matrix4f getCurrentMatrix() {
		if (this.matricesStack.isEmpty())
			return null;

		return this.matricesStack.get(this.matricesStack.size() - 1);
	}

	/**
	 * sets current matrix, overriding
	 * 
	 * @param matrix
	 */
	protected void setCurrentMatrix(Matrix4f matrix) {
		this.pop();
		this.matricesStack.add(matrix);
	}

	/**
	 * pops the last matrix into the current matrix
	 */
	public void pop() {
		if (!this.matricesStack.isEmpty())
			this.matricesStack.remove(this.matricesStack.size() - 1);
	}

	/**
	 * push last matrix onto stack (save for later, until next pop)
	 */
	public void push() {
		this.matricesStack.add(this.getCurrentMatrix().getCopy());
	}

	/**
	 * load identity matrix into current matrix
	 */
	public void loadIdentity() {
		this.getCurrentMatrix().loadIdentityMatrix();
	}
}
