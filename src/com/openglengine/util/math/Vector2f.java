package com.openglengine.util.math;

/**
 * 2D Vector container class
 * 
 * @author Dominik
 *
 */
public class Vector2f {
	/** 2d position */
	public float x, y;

	/**
	 * Initialize with 0,0 position
	 */
	public Vector2f() {
		this.x = this.y = 0;
	}

	/**
	 * Initialize with new 2d position
	 * 
	 * @param x
	 * @param y
	 */
	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Expensive normalize function
	 * 
	 * @return convenience return
	 */
	public Vector2f normalize() {
		double length = getLength();
		this.x /= length;
		this.y /= length;

		return this;
	}

	/**
	 * Fast normalize, though this is only accurate when the vector is almost normalized already. This uses a polynomial
	 * approximation of the sqrt() function. This function modifies this vector
	 * 
	 * @return
	 */
	public Vector2f reNormalize() {
		float length = MathUtils.fastSqrt(this.getSquaredLength());
		this.x /= length;
		this.y /= length;

		return this;
	}

	/**
	 * Fast normalize, though this is only accurate when the vector is almost normalized already. This uses a polynomial
	 * approximation of the sqrt() function. This function returns a completely new vector containing the fast
	 * normalization result
	 * 
	 * @return
	 */
	public Vector2f getReNormalizedResult() {
		float length = MathUtils.fastSqrt(this.getSquaredLength());

		return new Vector2f(this.x / length, this.y / length);
	}

	/**
	 * Returns a new vector containing the normalisation result
	 * 
	 * @return
	 */
	public Vector2f getNormalizedResult() {
		float length = (float) getLength();

		return new Vector2f(this.x / length, this.y / length);
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
		return this.x * this.x + this.y * this.y;
	}

	/**
	 * Adds another vector to this vector
	 * 
	 * @param vector
	 * @return convenience return
	 */
	public Vector2f addVector(Vector2f vector) {
		this.x += vector.x;
		this.y += vector.y;

		return this;
	}

	/**
	 * returns a new vector containing the addition result of this - vector
	 * 
	 * @param vector
	 * @return
	 */
	public Vector2f getAdditionResult(Vector2f vector) {
		return new Vector2f(this.x + vector.x, this.y + vector.y);
	}

	/**
	 * Subtracts another vector to this vector
	 * 
	 * @param vector
	 * @return convenience return
	 */
	public Vector2f subtractVector(Vector2f vector) {
		this.x -= vector.x;
		this.y -= vector.y;

		return this;
	}

	/**
	 * returns a new vector containing the subtraction result of this - vector
	 * 
	 * @param vector
	 * @return
	 */
	public Vector2f getSubtractionResult(Vector2f vector) {
		return new Vector2f(this.x - vector.x, this.y - vector.y);
	}

	/**
	 * Scales this vector using scalar
	 * 
	 * @param scalar
	 * @return convenience return
	 */
	public Vector2f scaleVector(float scalar) {
		this.x *= scalar;
		this.y *= scalar;

		return this;
	}

	/**
	 * Returns new vector equal to this vector in direction, but scaled by scalar
	 * 
	 * @param scalar
	 * @return
	 */
	public Vector2f getScaleResult(float scalar) {
		return new Vector2f(this.x * scalar, this.y * scalar);
	}

	/**
	 * Inverts this vector
	 * 
	 * TODO: optimize (instead of multiply, use "-this.x")
	 * 
	 * @return convenience return
	 */
	public Vector2f invert() {
		return this.scaleVector(-1);
	}

	/**
	 * returns the result of inverting this vector in a new vector
	 * 
	 * TODO: optimize
	 * 
	 * @return
	 */
	public Vector2f getInvertResult() {
		return this.getScaleResult(-1);
	}

	/**
	 * Returns the dot product of this vector and the other vector as in a * b = |a| * |b| * cos(€); this returns the a
	 * * b part
	 * 
	 * @param otherVector
	 * @return
	 */
	public float getDotProductResult(Vector3f otherVector) {
		return (this.x * otherVector.x + this.y * otherVector.y);
	}

	@Override
	public String toString() {
		return "Vector2f: (" + this.x + ", " + this.y + ")";
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Vector2f))
			return false;

		Vector2f other = (Vector2f) obj;
		if (other.x != this.x || other.y != this.y)
			return false;

		return true;
	}
}
