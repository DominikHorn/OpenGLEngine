package com.openglengine.util.math;

/**
 * Math utility class containing some utility functionality
 * 
 * @author Dominik
 *
 */
public class MathUtils {
	/**
	 * Returns orthographic matrix, useful for 2D games
	 * 
	 * @param left
	 * @param right
	 * @param bottom
	 * @param top
	 * @param near
	 * @param far
	 * @return
	 */
	public static Matrix4f createOrthographicMatrix(float left, float right, float bottom, float top, float near,
			float far) {
		Matrix4f ortho = new Matrix4f();

		ortho.matrixValues[0 + 0 * 4] = +2.0f / (right - left);
		ortho.matrixValues[1 + 1 * 4] = +2.0f / (top - bottom);
		ortho.matrixValues[2 + 2 * 4] = -2.0f / (far - near);
		ortho.matrixValues[0 + 3 * 4] = (right + left) / (right - left);
		ortho.matrixValues[1 + 3 * 4] = (top + bottom) / (bottom - top);// (top - bottom); // (bottom - top)? TODO FIXME
		ortho.matrixValues[2 + 3 * 4] = (far + near) / (far - near);

		return ortho;
	}

	/**
	 * returns perspective matrix, useful for 3D games
	 * 
	 * @param fov
	 * @param aspectRatio
	 * @param znear
	 * @param zfar
	 * @return
	 */
	public static Matrix4f createPerspectiveMatrix(float fov, float aspectRatio, float znear, float zfar) {
		Matrix4f perspective = new Matrix4f();

		float q = 1.0f / (float) Math.tan(Math.toRadians(0.5f * fov));
		float a = q / aspectRatio;

		float b = (znear + zfar) / (znear - zfar);
		float c = (2.0f * znear * zfar) / (znear - zfar);

		perspective.matrixValues[0 + 0 * 4] = a;
		perspective.matrixValues[1 + 1 * 4] = q;
		perspective.matrixValues[2 + 2 * 4] = b;
		perspective.matrixValues[3 + 2 * 4] = -1.0f;
		perspective.matrixValues[2 + 3 * 4] = c;

		return perspective;
	}

	/**
	 * Creates a lookat matrix
	 * 
	 * @param eye
	 * @param center
	 * @param up
	 * @return
	 */
	public static Matrix4f createLookAtMatrix(Vector3f eye, Vector3f center, Vector3f up) {
		Matrix4f result = new Matrix4f();

		Vector3f f = center.getSubtractionResult(eye).normalise();
		Vector3f s = f.getCrossProductResult(up.getNormalizeResult()).normalise();
		Vector3f u = s.getCrossProductResult(f);

		result.matrixValues[0 + 0 * 4] = s.x;
		result.matrixValues[0 + 1 * 4] = s.y;
		result.matrixValues[0 + 2 * 4] = s.z;

		result.matrixValues[1 + 0 * 4] = u.x;
		result.matrixValues[1 + 1 * 4] = u.y;
		result.matrixValues[1 + 2 * 4] = u.z;

		result.matrixValues[2 + 0 * 4] = -f.x;
		result.matrixValues[2 + 1 * 4] = -f.y;
		result.matrixValues[2 + 2 * 4] = -f.z;

		return result.multiply(MathUtils.createTranslationMatrix(-eye.x, -eye.y, -eye.z));
	}

	/**
	 * returns new translation matrix
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public static Matrix4f createTranslationMatrix(float x, float y, float z) {
		Matrix4f result = new Matrix4f();

		result.matrixValues[0 + 3 * 4] = x;
		result.matrixValues[1 + 3 * 4] = y;
		result.matrixValues[2 + 3 * 4] = z;

		return result;
	}

	/**
	 * returns new scale matrix
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public static Matrix4f createScaleMatrix(float x, float y, float z) {
		Matrix4f result = new Matrix4f();

		result.matrixValues[0 + 0 * 4] = x;
		result.matrixValues[1 + 1 * 4] = y;
		result.matrixValues[2 + 2 * 4] = z;

		return result;
	}

	/**
	 * returns the rotation matrix
	 * 
	 * @param radiansAngle
	 *            amount of rotation
	 * @param x
	 *            rotation amount around x
	 * @param y
	 *            rotation amount around y
	 * @param z
	 *            rotation amount around z
	 * @return
	 */
	public static Matrix4f createRotationMatrix(float radiansAngle, float x, float y, float z) {
		Matrix4f matRotX = new Matrix4f();
		Matrix4f matRotY = new Matrix4f();
		Matrix4f matRotZ = new Matrix4f();

		float cosX = (float) Math.cos(radiansAngle * x);
		float sinX = (float) Math.sin(radiansAngle * x);

		float cosY = (float) Math.cos(radiansAngle * y);
		float sinY = (float) Math.sin(radiansAngle * y);

		float cosZ = (float) Math.cos(radiansAngle * z);
		float sinZ = (float) Math.sin(radiansAngle * z);

		matRotX.matrixValues[1 + 1 * 4] = +cosX;
		matRotX.matrixValues[1 + 2 * 4] = -sinX;
		matRotX.matrixValues[2 + 1 * 4] = +sinX;
		matRotX.matrixValues[2 + 2 * 4] = +cosX;

		matRotY.matrixValues[0 + 0 * 4] = +cosY;
		matRotY.matrixValues[0 + 2 * 4] = +sinY;
		matRotY.matrixValues[2 + 0 * 4] = -sinY;
		matRotY.matrixValues[2 + 2 * 4] = +cosY;

		matRotZ.matrixValues[0 + 0 * 4] = +cosZ;
		matRotZ.matrixValues[1 + 0 * 4] = +sinZ;
		matRotZ.matrixValues[0 + 1 * 4] = -sinZ;
		matRotZ.matrixValues[1 + 1 * 4] = +cosZ;

		return matRotX.multiply(matRotY.multiply(matRotZ));
	}

	/**
	 * Clamps a value to the given bounds.
	 * 
	 * @param value
	 * @param min
	 * @param max
	 * @return value, guaranteed to not be smaller than min or bigger than max
	 */
	public static float clamp(float value, float min, float max) {
		if (value < min)
			return min;

		if (value > max)
			return max;

		return value;
	}

	/**
	 * Calculate y value from triangle defined by p1, p2 and p3 at x-z coordinate pos
	 * 
	 * @param p1
	 * @param p2
	 * @param p3
	 * @param pos
	 * @return
	 */
	public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
		float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
		float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
		float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
		float l3 = 1.0f - l1 - l2;
		return l1 * p1.y + l2 * p2.y + l3 * p3.y;
	}

	/**
	 * This uses polynomial approximation to get results. The polynom used is a second degree polynom. This function's
	 * results will only be accurate around x=0.75 up to x=1.25, meaning this is only useful for renormalizing almost
	 * normalized vectors (floating point error fixing f.e.)
	 * 
	 * @param x
	 * @return
	 */
	protected static float fastSqrt(float x) {
		float a0 = 15.0f / 8.0f;
		float a1 = -5.0f / 4.0f;
		float a2 = 3.0f / 8.0f;

		return a0 + a1 * x + a2 * x * x;
	}
}
