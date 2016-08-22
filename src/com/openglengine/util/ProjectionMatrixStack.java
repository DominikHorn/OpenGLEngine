package com.openglengine.util;

import com.openglengine.util.math.*;

public class ProjectionMatrixStack extends MatrixStack {
	public void setPerspectiveMatrix(float fov, float aspectRatio, float znear, float zfar) {
		this.setCurrentMatrix(MathUtil.createPerspectiveMatrix(fov, aspectRatio, znear, zfar));
	}

	public void setOrthographicMatrix(float left, float right, float bottom, float top, float near, float far) {
		this.setCurrentMatrix(MathUtil.createOrthographicMatrix(left, right, bottom, top, near, far));
	}
}
