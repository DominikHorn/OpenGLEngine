package com.openglengine.util.math;

/**
 * 4D vector class
 * 
 * @author Dominik
 *
 */
public class Vector4f {
	/** 4D position */
	public float x, y, z, w;

	/**
	 * Initialize with 0,0,0,0 position
	 */
	public Vector4f() {
		this.x = this.y = this.z = this.w = 0;
	}

	/**
	 * Initialize with new positions
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param w
	 */
	public Vector4f(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	/**
	 * Expensive length calculation
	 * 
	 * @return
	 */
	public double getLength() {
		return Math.sqrt(this.getSquaredLength());
	}

	/**
	 * Expensive normalize function
	 * 
	 * @return convenience return
	 */
	public Vector4f normalize() {
		double length = getLength();
		this.x /= length;
		this.y /= length;
		this.z /= length;
		this.w /= length;

		return this;
	}

	/**
	 * Fast normalize, though this is only accurate when the vector is almost normalized already. This uses a polynomial
	 * approximation of the sqrt() function. This function modifies this vector
	 * 
	 * @return
	 */
	public Vector4f reNormalize() {
		float length = MathUtils.fastSqrt(this.getSquaredLength());
		this.x /= length;
		this.y /= length;
		this.z /= length;
		this.w /= length;

		return this;
	}

	/**
	 * Fast normalize, though this is only accurate when the vector is almost normalized already. This uses a polynomial
	 * approximation of the sqrt() function. This function returns a completely new vector containing the fast
	 * normalization result
	 * 
	 * @return
	 */
	public Vector4f getReNormalizeResult() {
		float length = MathUtils.fastSqrt(this.getSquaredLength());

		return new Vector4f(this.x / length, this.y / length, this.z / length, this.w / length);
	}

	/**
	 * Returns a new vector containing the normalisation result
	 * 
	 * @return
	 */
	public Vector4f getNormalizeResult() {
		float length = (float) getLength();
		return new Vector4f(this.x / length, this.y / length, this.z / length, this.w / length);
	}

	/**
	 * Calculates squared length
	 * 
	 * @return
	 */
	public float getSquaredLength() {
		return this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w;
	}

	/**
	 * Adds another vector to this vector
	 * 
	 * @param vector
	 * @return convenience return
	 */
	public Vector4f addVector(Vector4f vector) {
		this.x += vector.x;
		this.y += vector.y;
		this.z += vector.z;
		this.w += vector.w;

		return this;
	}

	/**
	 * returns a new vector containing the addition result of this - vector
	 * 
	 * @param vector
	 * @return
	 */
	public Vector4f getAdditionResult(Vector4f vector) {
		return new Vector4f(this.x + vector.x, this.y + vector.y, this.z + vector.z, this.w + vector.w);
	}

	/**
	 * Subtracts another vector to this vector
	 * 
	 * @param vector
	 * @return convenience return
	 */
	public Vector4f subtractVector(Vector4f vector) {
		this.x -= vector.x;
		this.y -= vector.y;
		this.z -= vector.z;
		this.w -= vector.w;

		return this;
	}

	/**
	 * returns a new vector containing the subtraction result of this - vector
	 * 
	 * @param vector
	 * @return
	 */
	public Vector4f getSubtractionResult(Vector4f vector) {
		return new Vector4f(this.x - vector.x, this.y - vector.y, this.z - vector.z, this.w - vector.w);
	}

	/**
	 * Scales this vector using scalar
	 * 
	 * @param scalar
	 * @return convenience return
	 */
	public Vector4f scaleVector(float scalar) {
		this.x *= scalar;
		this.y *= scalar;
		this.z *= scalar;
		this.w *= scalar;

		return this;
	}

	/**
	 * Inverts this vector
	 * 
	 * @return convenience return
	 */
	public Vector4f invert() {
		return this.scaleVector(-1);
	}

	/**
	 * Returns new vector equal to this vector in direction, but scaled by scalar
	 * 
	 * @param scalar
	 * @return
	 */
	public Vector4f getScaleResult(float scalar) {
		return new Vector4f(this.x * scalar, this.y * scalar, this.z * scalar, this.w * scalar);
	}

	/**
	 * Returns the dot product of this vector and the other vector as in a * b = |a| * |b| * cos(€); this returns the a
	 * * b part
	 * 
	 * @param otherVector
	 * @return
	 */
	public float getDotProductResult(Vector4f otherVector) {
		return this.x * otherVector.x + this.y * otherVector.y + this.z * otherVector.z + this.w * otherVector.w;
	}

	@Override
	public String toString() {
		return "Vector4f: (" + this.x + ", " + this.y + ", " + this.z + this.w + ")";
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Vector4f))
			return false;

		Vector4f other = (Vector4f) obj;
		if (other.x != this.x || other.y != this.y || other.z != this.z || other.w != this.w)
			return false;

		return true;
	}
}
