package org.frohoff.inspectorgadget.model;

import com.tinkerpop.frames.Adjacency;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue("method") public interface MethodDef extends MemberDef {
  @Adjacency(label="parameterType")
  public Iterable<ClassDef> getParameterTypes();
  @Adjacency(label="parameterType")
  public void addParamterType(ClassDef cd);
  
  @Adjacency(label="returnType")
  public ClassDef getReturnType();
  @Adjacency(label="returnType")
  public void setReturnType(ClassDef cd);
  
  @Adjacency(label="calls")
  public Iterable<MethodDef> getCalls();
  @Adjacency(label="calls")
  public void addCall(MethodDef method);
}