package com.openglengine.util.math;

import java.nio.*;

/**
 * 4 * 4 matrix class
 * 
 * @author Dominik
 *
 */
public class Matrix4f {
	/** Dimension of the matrix. Dimension 4 -> 4 * 4 matrix */
	private static final int MATRIX_DIMENSION = 4;

	/** Value container */
	protected float[] matrixValues;

	/**
	 * Initialize to identity matrix
	 */
	public Matrix4f() {
		this.matrixValues = new float[MATRIX_DIMENSION * MATRIX_DIMENSION];

		// reset matrix to 0
		for (int i = 0; i < this.matrixValues.length; i++)
			this.matrixValues[i] = 0f;

		this.loadIdentityMatrix();
	}

	/**
	 * Internal constructor used by getCopy()
	 * 
	 * @param data
	 */
	private Matrix4f(float[] data) {
		this.matrixValues = data;
	}

	/**
	 * Loads the identity matrix (no transformation at all)
	 */
	public void loadIdentityMatrix() {
		for (int i = 0; i < MATRIX_DIMENSION * MATRIX_DIMENSION; i++) {
			if (i % (MATRIX_DIMENSION + 1) == 0)
				this.matrixValues[i] = 1f;
			else
				this.matrixValues[i] = 0f;
		}
	}

	/**
	 * Multiplies (this * other) and stores in this
	 * 
	 * @param matrix
	 * @return convenience return
	 */
	public Matrix4f multiply(Matrix4f matrix) {
		float[] result = new float[MATRIX_DIMENSION * MATRIX_DIMENSION];

		for (int column = 0; column < MATRIX_DIMENSION; column++) {
			for (int row = 0; row < MATRIX_DIMENSION; row++) {
				float sum = 0.0f;
				for (int e = 0; e < MATRIX_DIMENSION; e++) {
					sum += this.matrixValues[row + e * MATRIX_DIMENSION]
							* matrix.matrixValues[e + column * MATRIX_DIMENSION];
				}

				result[row + column * MATRIX_DIMENSION] = sum;
			}
		}

		this.matrixValues = result;

		return this;
	}

	/**
	 * Needed to pass matrix data via openGL to shader
	 * 
	 * @param buffer
	 */
	public void storeInFloatBuffer(FloatBuffer buffer) {
		buffer.position(0);
		for (int column = 0; column < MATRIX_DIMENSION; column++) {
			for (int row = 0; row < MATRIX_DIMENSION; row++) {
				buffer.put(this.matrixValues[row + column * MATRIX_DIMENSION]);
			}
		}

		buffer.flip();
	}

	/**
	 * Returns carbon copy of this matrix
	 * 
	 * @return
	 */
	public Matrix4f getCopy() {
		float[] copiedData = new float[MATRIX_DIMENSION * MATRIX_DIMENSION];
		for (int i = 0; i < MATRIX_DIMENSION * MATRIX_DIMENSION; i++) {
			copiedData[i] = this.matrixValues[i];
		}

		return new Matrix4f(copiedData);
	}

	@Override
	public String toString() {
		String returnValue = "\n--------------------------\n";
		for (int column = 0; column < MATRIX_DIMENSION; column++) {
			returnValue += String.format("| %2.2f  ", this.matrixValues[0 + column * MATRIX_DIMENSION]);
			returnValue += String.format("%2.2f  ", this.matrixValues[1 + column * MATRIX_DIMENSION]);
			returnValue += String.format("%2.2f  ", this.matrixValues[2 + column * MATRIX_DIMENSION]);
			returnValue += String.format("%2.2f |", this.matrixValues[3 + column * MATRIX_DIMENSION]);
			returnValue += "\n";
		}
		returnValue += "--------------------------\n";

		return returnValue;
	}
}
