package com.openglengine.util;

/**
 * Manager baseclass. All Managers of this engine are derived from this class
 * 
 * @author Dominik
 *
 */
public interface ResourceManager {
	/**
	 * ResourceManagers will become invalid after this method has been called
	 */
	public void cleanup();
}
