package com.openglengine.util;

/**
 * Container baseclass for all containers that want to use reference counting
 * 
 * @author Dominik
 *
 */
public abstract class ReferenceCountedDeletableContainer {
	/** reference counter */
	protected int numReferences;

	protected ReferenceCountedDeletableContainer() {
		this.numReferences = 0;
	}

	/**
	 * Notifies this object that it is used by another object
	 */
	public void use() {
		this.numReferences++;
	}

	/**
	 * Decreases reference counter. If this container is not being referenced from anywhere anymore it will auto self
	 * delete.
	 *
	 */
	public void cleanup() {
		if (--this.numReferences == 0) {
			this.forceDelete();
		}
	}

	/**
	 * This method will be called when the contained resource must be deleted, ignoring the reference counter
	 */
	protected abstract void forceDelete();
}
