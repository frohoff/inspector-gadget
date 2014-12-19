package org.frohoff.inspectorgadget.model;

public enum Access {
	PRIVATE, PROTECTED, PACKAGE, PUBLIC;
	
	@Override
	public String toString() {
		return name().toLowerCase();
	};
}
