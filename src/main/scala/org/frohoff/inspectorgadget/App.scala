package org.frohoff.inspectorgadget

import com.tinkerpop.blueprints.impls.orient.OrientGraph
import com.tinkerpop.blueprints.impls.tg.TinkerGraphFactory
import com.tinkerpop.frames.FramedGraphFactory
import com.tinkerpop.frames.modules.gremlingroovy.GremlinGroovyModule
import org.frohoff.inspectorgadget.model._
import scala.collection.JavaConversions._
import com.tinkerpop.blueprints.Vertex
import com.tinkerpop.blueprints.impls.tg.TinkerGraph
import javassist.ClassPool
import javassist.CtClass
import java.util.jar.JarFile
import java.net.URLClassLoader
import java.io.File
import scala.util.Try
import javassist.CtMethod


object App extends App {
  def doClass(c:CtClass):ClassDef = {
    var cd = fg.getVertex(c.getName, classOf[ClassDef])
    if (cd != null)
      cd
    else {
      cd = fg.addVertex(c.getName, classOf[ClassDef])
      c.getInterfaces.map(doClass).foreach(cd.addImplements(_))
      Option(c.getSuperclass).map(doClass).foreach(cd.setExtends(_))
      c.getMethods.map(doMethod).foreach(cd.addMethod(_))
      cd
    }    
  }
  
  def doMethod(m:CtMethod) = {
    var md = fg.getVertex(m.getLongName, classOf[MethodDef])
    if (md != null)
      md
    else {
      md = fg.addVertex(m.getLongName, classOf[MethodDef])
      Option(m.getReturnType).map(doClass).foreach(md.setReturnType(_))
      m.getParameterTypes.map(doClass).foreach(md.addParamterType(_))
      md
    }
  }
    
  
  println( "Start" )
//  val g = new OrientGraph("memory:test")
//  try {
//    val v1 = g.addVertex("v1","v1")
//    v1.setProperty("name", "one");
//    val v2 = g.addVertex("v2","v2")
//    v2.setProperty("name", "two")
//    g.addEdge("related", v1, v2, "related")
//    g.commit()
//
//  } finally {
//    g.shutdown()
//  }
  
  val g = new TinkerGraph(); //This graph is pre-populated.
  val f = new FramedGraphFactory(new GremlinGroovyModule()); //(1) Factories should be reused for performance and memory conservation.

  val fg = f.create(g); //Frame the graph.

//  
  val cp = ClassPool.getDefault
  
//  doClass(cp.getCtClass("java.util.ArrayList"))
//  doClass(cp.getCtClass("java.io.Serializable"))
//  doClass(cp.getCtClass("java.lang.reflect.InvocationHandler"))
  
  //println(getClass.getClassLoader.getParent.asInstanceOf[URLClassLoader].getURLs.toList)
  
  val lib = new File(System.getProperty("java.home") + "/lib")
  val libJars = lib.listFiles().filter {_.getName.endsWith(".jar")}.map { new JarFile(_) }
  val classes = libJars.flatMap {_.entries().map { _.getName}.filter { _.endsWith(".class") }.map{_.replaceAll("\\.class$", "").replaceAll("/",".")}}.toList
  //println(classes)
  classes.flatMap(c => Try(cp.getCtClass(c)).toOption).map(doClass)
  //classes.foreach(c => doClass(cp.getCtClass(c)))
  
  //val jf = new JarFile(new File())
//
//  val cd1 = fg.addVertex(null, classOf[ClassDef])
//  cd1.setName("Foo")
//  
//  val cd2 = fg.addVertex(null, classOf[ClassDef])
//  cd2.setName("Bar")
//  
//  cd1.setExtends(cd2)
//  
//  fg.query().has("")
  
//  g.getVertices.foreach { x:Vertex => println(x.getProperty("name")); println(x) }
//  g.getEdges.foreach { println(_) }

  
  
  println( "End" )  
  
}
