package org.frohoff.inspectorgadget.model;

import com.tinkerpop.frames.Adjacency;
import com.tinkerpop.frames.Property;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue("field") public interface FieldDef extends MemberDef {
	@Adjacency(label="fieldType")
	public ClassDef getType();
	@Adjacency(label="fieldType")
	public void setType(ClassDef d);
	
	@Property("transient")
	public boolean isTransient();
	@Property("transient")
	public void setTransient(boolean a);
}
