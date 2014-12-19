package org.frohoff.inspectorgadget.model;

import com.tinkerpop.frames.Adjacency;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue("class") public interface ClassDef extends MemberDef {
	@Adjacency(label="extends")
	public Iterable<ClassDef> getExtends();
	@Adjacency(label="extends")
	public void addExtends(ClassDef d);
	
	@Adjacency(label="implements")
	public Iterable<ClassDef> getImplements();	
	@Adjacency(label="implements")
	public void addImplements(ClassDef d);
	
	@Adjacency(label="method")
	public Iterable<MethodDef> getMethods();	
	@Adjacency(label="method")
	public void addMethod(MethodDef d);	
	
	@Adjacency(label="field")
	public Iterable<FieldDef> getFields();
	@Adjacency(label="field")
	public void addField(FieldDef d);
}
