package com.openglengine.util.math;

public class MathUtil {
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
	public static Matrix4f getOrthographicMatrix(float left, float right, float bottom, float top, float near,
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

	public static Matrix4f createLookAtMatrix(Vector3f eye, Vector3f center, Vector3f up) {
		Matrix4f result = new Matrix4f();

		Vector3f f = center.getSubtractionResult(eye).normalize();
		Vector3f s = f.getCrossProductResult(up.getNormalizeResult()).normalize();
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

		return result.multiply(MathUtil.createTranslationMatrix(-eye.x, -eye.y, -eye.z));
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
	 * returns new Translation matrix
	 * 
	 * @param translation
	 *            a 3D vector describing the translation
	 * @return
	 */
	public static Matrix4f createTranslationMatrix(Vector3f translation) {
		return createTranslationMatrix(translation.x, translation.y, translation.z);
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
	 * returns new scale matrix
	 * 
	 * @param scale
	 *            3D vector describing the amount of scaling on each axis
	 * @return
	 */
	public static Matrix4f createScaleMatrix(Vector3f scale) {
		return createScaleMatrix(scale.x, scale.y, scale.z);
	}

	/**
	 * returns the rotation matrix
	 * 
	 * @param angle
	 *            amount of rotation
	 * @param x
	 *            rotation amount around x
	 * @param y
	 *            rotation amount around y
	 * @param z
	 *            rotation amount around z
	 * @return
	 */
	public static Matrix4f createRotationMatrix(float angle, float x, float y, float z) {
		float radiens = (float) Math.toRadians(angle);
		Matrix4f matRotX = new Matrix4f();
		Matrix4f matRotY = new Matrix4f();
		Matrix4f matRotZ = new Matrix4f();

		float cosX = (float) Math.cos(radiens * x);
		float sinX = (float) Math.sin(radiens * x);

		float cosY = (float) Math.cos(radiens * y);
		float sinY = (float) Math.sin(radiens * y);

		float cosZ = (float) Math.cos(radiens * z);
		float sinZ = (float) Math.sin(radiens * z);

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
	 * returns the rotation matrix
	 * 
	 * @param angle
	 *            amount of rotation
	 * @param axis
	 *            3D vector describing the axis rotation
	 * @return
	 */
	public static Matrix4f createRotationMatrix(float angle, Vector3f axis) {
		return createRotationMatrix(angle, axis.x, axis.y, axis.z);
	}
}
