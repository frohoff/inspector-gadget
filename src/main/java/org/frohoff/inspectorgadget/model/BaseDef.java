package org.frohoff.inspectorgadget.model;

import com.tinkerpop.frames.Property;
import com.tinkerpop.frames.modules.typedgraph.TypeField;

@TypeField("type") public interface BaseDef {
	@Property("name")
	public String getName();
	@Property("name")
	public void setName(String name);
}
