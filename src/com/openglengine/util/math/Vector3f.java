package com.openglengine.util.math;

/**
 * 3d vector class
 * 
 * @author Dominik
 *
 */
public class Vector3f {
	/** 3d position */
	public float x, y, z;

	/**
	 * Initialize with 0,0,0 position
	 */
	public Vector3f() {
		this.x = this.y = this.z = 0;
	}

	/**
	 * initialize with new coordinates
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Expensive normalize function
	 * 
	 * @return convenience return
	 */
	public Vector3f normalise() {
		double length = getLength();
		this.x /= length;
		this.y /= length;
		this.z /= length;

		return this;
	}

	/**
	 * Fast normalize, though this is only accurate when the vector is almost normalized already. This uses a polynomial
	 * approximation of the sqrt() function. This function modifies this vector
	 * 
	 * @return
	 */
	public Vector3f reNormalize() {
		float length = MathUtils.fastSqrt(this.getSquaredLength());
		this.x /= length;
		this.y /= length;
		this.z /= length;

		return this;
	}

	/**
	 * Fast normalize, though this is only accurate when the vector is almost normalized already. This uses a polynomial
	 * approximation of the sqrt() function. This function returns a completely new vector containing the fast
	 * normalization result
	 * 
	 * @return
	 */
	public Vector3f getReNormalizeResult() {
		float length = MathUtils.fastSqrt(this.getSquaredLength());

		return new Vector3f(this.x / length, this.y / length, this.z / length);
	}

	/**
	 * Returns a new vector containing the normalisation result
	 * 
	 * @return
	 */
	public Vector3f getNormalizeResult() {
		float length = (float) getLength();
		return new Vector3f(this.x / length, this.y / length, this.z / length);
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
	 * Calculates squared length
	 * 
	 * @return
	 */
	public float getSquaredLength() {
		return this.x * this.x + this.y * this.y + this.z * this.z;
	}

	/**
	 * Adds another vector to this vector
	 * 
	 * @param vector
	 * @return convenience return
	 */
	public Vector3f addVector(Vector3f vector) {
		this.x += vector.x;
		this.y += vector.y;
		this.z += vector.z;

		return this;
	}

	/**
	 * returns a new vector containing the addition result of this - vector
	 * 
	 * @param vector
	 * @return
	 */
	public Vector3f getAdditionResult(Vector3f vector) {
		return new Vector3f(this.x + vector.x, this.y + vector.y, this.z + vector.z);
	}

	/**
	 * Subtracts another vector to this vector
	 * 
	 * @param vector
	 * @return convenience return
	 */
	public Vector3f subtractVector(Vector3f vector) {
		this.x -= vector.x;
		this.y -= vector.y;
		this.z -= vector.z;

		return this;
	}

	/**
	 * returns a new vector containing the subtraction result of this - vector
	 * 
	 * @param vector
	 * @return
	 */
	public Vector3f getSubtractionResult(Vector3f vector) {
		return new Vector3f(this.x - vector.x, this.y - vector.y, this.z - vector.z);
	}

	/**
	 * Scales this vector using scalar
	 * 
	 * @param scalar
	 * @return convenience return
	 */
	public Vector3f scaleVector(float scalar) {
		this.x *= scalar;
		this.y *= scalar;
		this.z *= scalar;

		return this;
	}

	/**
	 * Returns new vector equal to this vector in direction, but scaled by scalar
	 * 
	 * @param scalar
	 * @return
	 */
	public Vector3f getScaleResult(float scalar) {
		return new Vector3f(this.x * scalar, this.y * scalar, this.z * scalar);
	}

	/**
	 * Inverts this vector
	 * 
	 * TODO: optimize (instead of multiply, use "-this.x")
	 * 
	 * @return convenience return
	 */
	public Vector3f invert() {
		return this.scaleVector(-1);
	}

	/**
	 * returns the result of inverting this vector in a new vector
	 * 
	 * TODO: optimize
	 * 
	 * @return
	 */
	public Vector3f getInvertResult() {
		return this.getScaleResult(-1);
	}

	/**
	 * Returns the cross product of this vector and the other vector. this only uses the vectors real components (3d
	 * cross product)
	 * 
	 * @param otherVector
	 * @return
	 */
	public Vector3f getCrossProductResult(Vector3f otherVector) {
		return new Vector3f(this.y * otherVector.z - this.z * otherVector.y,
				this.z * otherVector.x - this.x * otherVector.z, this.x * otherVector.y - this.y * otherVector.x);
	}

	/**
	 * Returns the dot product of this vector and the other vector as in a * b = |a| * |b| * cos(€); this returns the a
	 * * b part
	 * 
	 * @param otherVector
	 * @return
	 */
	public float getDotProductResult(Vector3f otherVector) {
		return (this.x * otherVector.x + this.y * otherVector.y + this.z * otherVector.z);
	}

	@Override
	public String toString() {
		return "Vector3f: (" + this.x + ", " + this.y + ", " + this.z + ");";
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Vector3f))
			return false;

		Vector3f other = (Vector3f) obj;
		if (other.x != this.x || other.y != this.y || other.z != this.z)
			return false;

		return true;
	}
}
