package com.openglengine.util;

import java.nio.*;

import org.lwjgl.*;

/**
 * General Utils class for functions that might need to be performed
 * 
 * @author Dominik
 *
 */
public class Utils {
	/**
	 * Convert int array to IntBuffer
	 * 
	 * @param data
	 * @return
	 */
	public static IntBuffer convertToIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();

		return buffer;
	}

	/**
	 * Convert float array to FloatBuffer
	 * 
	 * @param data
	 * @return
	 */
	public static FloatBuffer convertToFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();

		return buffer;
	}

}
