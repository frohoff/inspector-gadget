package org.frohoff.inspectorgadget.model;
import java.util.Iterator;

import com.tinkerpop.frames.Adjacency;
import com.tinkerpop.frames.Property;


public interface ClassDef {
	@Property("name")
	public String getName();
	@Property("name")
	public void setName(String name);
	
	@Adjacency(label="extends")
	public ClassDef getExtends();
	@Adjacency(label="extends")
	public void setExtends(ClassDef cd);
	
	@Adjacency(label="implements")
	public Iterator<ClassDef> getImplements();	
	@Adjacency(label="implements")
	public void addImplements(ClassDef cd);
	
	@Adjacency(label="method")
	public Iterator<MethodDef> getMethods();	
	@Adjacency(label="method")
	public void addMethod(MethodDef cd);	
}
