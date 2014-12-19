package org.frohoff.inspectorgadget.model;

import com.tinkerpop.frames.Adjacency;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue("class") public interface ClassDef extends BaseDef {
	@Adjacency(label="extends")
	public Iterable<ClassDef> getExtends();
	@Adjacency(label="extends")
	public void addExtends(ClassDef cd);
	
	@Adjacency(label="implements")
	public Iterable<ClassDef> getImplements();	
	@Adjacency(label="implements")
	public void addImplements(ClassDef cd);
	
	@Adjacency(label="method")
	public Iterable<MethodDef> getMethods();	
	@Adjacency(label="method")
	public void addMethod(MethodDef cd);	
}
