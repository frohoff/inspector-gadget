package org.frohoff.inspectorgadget

import java.io.File
import java.util.jar.JarFile

import scala.collection.JavaConversions._
import scala.util.Try

import org.frohoff.inspectorgadget.model._

import com.tinkerpop.blueprints.impls.tg.TinkerGraph
import com.tinkerpop.frames.FramedGraphFactory
import com.tinkerpop.frames.modules.gremlingroovy.GremlinGroovyModule

import javassist._


object IndexApp extends App {  
  def indexClass(c:CtClass): ClassDef = {
    var cd = graph.getVertex(c.getName, classOf[ClassDef])
    if (cd != null)
      cd
    else {
      cd = graph.addVertex(c.getName, classOf[ClassDef])
      c.getInterfaces.map(indexClass).foreach(cd.addImplements(_))
      Option(c.getSuperclass).map(indexClass).foreach(cd.setExtends(_))
      c.getMethods.map(indexMethod).foreach(cd.addMethod(_))
      cd
    }    
  }
  
  def indexMethod(m:CtMethod) = {
    var md = graph.getVertex(m.getLongName, classOf[MethodDef])
    if (md != null)
      md
    else {
      md = graph.addVertex(m.getLongName, classOf[MethodDef])
      Option(m.getReturnType).map(indexClass).foreach(md.setReturnType(_))
      m.getParameterTypes.map(indexClass).foreach(md.addParamterType(_)) 
      md
    }
  }
    
  println( "Start" )
  
  val f = new FramedGraphFactory(new GremlinGroovyModule()); 

  val graph = f.create(new TinkerGraph());


  val cp = ClassPool.getDefault
  
  val libDir = new File(System.getProperty("java.home") + "/lib")
  val libJars = libDir.listFiles().filter{_.getName.endsWith(".jar")}.map{new JarFile(_)}
  val libClasses = libJars.flatMap{_.entries().map{_.getName}.filter{ _.endsWith(".class")}.map{_.replaceAll("\\.class$", "").replaceAll("/",".")}}
  libClasses.flatMap(c => Try(cp.getCtClass(c)).toOption).map(indexClass)

  println("Done parsing")
  
  println( "End" )  
  
}
