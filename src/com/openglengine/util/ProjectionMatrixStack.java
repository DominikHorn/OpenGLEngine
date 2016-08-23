package com.openglengine.util;

import com.openglengine.util.math.*;

/**
 * Matrix stack implementation specifically for the projection matrix stack
 * 
 * @author Dominik
 *
 */
public class ProjectionMatrixStack extends MatrixStack {
	/**
	 * Load a perspective matrix with these parameters
	 * 
	 * @param fov
	 * @param aspectRatio
	 * @param znear
	 * @param zfar
	 */
	public void setPerspectiveMatrix(float fov, float aspectRatio, float znear, float zfar) {
		this.setCurrentMatrix(MathUtil.createPerspectiveMatrix(fov, aspectRatio, znear, zfar));
	}

	/**
	 * Load an orthographic matrix with these parameters
	 * 
	 * @param left
	 * @param right
	 * @param bottom
	 * @param top
	 * @param near
	 * @param far
	 */
	public void setOrthographicMatrix(float left, float right, float bottom, float top, float near, float far) {
		this.setCurrentMatrix(MathUtil.createOrthographicMatrix(left, right, bottom, top, near, far));
	}
}
