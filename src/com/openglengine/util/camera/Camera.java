package com.openglengine.util.camera;

import com.openglengine.core.*;
import com.openglengine.util.*;
import com.openglengine.util.math.*;

/**
 * Abstract Camera class. Subclass this to create your own camera
 * 
 * @author Dominik
 *
 */
public abstract class Camera {
	protected Vector3f position;
	protected float pitch, yaw, roll;

	public Camera() {
		this(new Vector3f(), 0, 0, 0);
	}

	public Camera(Vector3f position, float pitch, float yaw, float roll) {
		this.position = position;
		this.pitch = pitch;
		this.yaw = yaw;
		this.roll = roll;
	}

	protected void update() {
		TransformationMatrixStack vm = Engine.getViewMatrixStack();
		vm.loadIdentity();
		vm.rotate(this.pitch, 1, 0, 0);
		vm.rotate(this.yaw, 0, 1, 0); // Take entity y rotation into account
		vm.rotate(this.roll, 0, 0, 1);
		vm.translate(this.position.getInvertResult());
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public float getRoll() {
		return roll;
	}

	public void setRoll(float roll) {
		this.roll = roll;
	}

	public Vector3f getRotation() {
		return new Vector3f(-this.pitch, (float) -(this.yaw + Math.toRadians(180)), -this.roll);
	}

}
