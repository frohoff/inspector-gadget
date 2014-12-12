package org.frohoff.inspectorgadget.model;
import java.util.Iterator;

import com.tinkerpop.frames.Adjacency;
import com.tinkerpop.frames.Property;

public interface MethodDef {
  @Property("name")
  public String getName();
  @Property("name")
  public void setName(String name);
  
  @Adjacency(label="parameterType")
  public Iterator<ClassDef> getParameterTypes();
  @Adjacency(label="parameterType")
  public void addParamterType(ClassDef cd);
  
  @Adjacency(label="returnType")
  public ClassDef getReturnType();
  @Adjacency(label="returnType")
  public void setReturnType(ClassDef cd);
}