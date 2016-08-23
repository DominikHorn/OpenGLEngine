package com.openglengine.util;

public abstract class ReferenceCountedDeletableContainer {
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
	 * cleans this entity. If it is not being referenced from anywhere anymore it will auto force delete
	 * 
	 * @return true if this object was deleted, false if there are still references to it
	 */
	public boolean cleanup() {
		if (--this.numReferences <= 0) {
			this.forceDelete();

			return true;
		}

		return false;
	}

	protected abstract void forceDelete();
}
