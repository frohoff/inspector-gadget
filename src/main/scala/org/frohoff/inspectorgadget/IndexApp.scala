package org.frohoff.inspectorgadget


import scala.collection.JavaConversions._
import java.io.File
import java.util.jar.JarFile
import scala.util.Try
import org.frohoff.inspectorgadget.model._
import com.tinkerpop.blueprints.impls.tg.TinkerGraph
import com.tinkerpop.frames.FramedGraphFactory
import com.tinkerpop.frames.modules.gremlingroovy.GremlinGroovyModule
import javassist._
import com.tinkerpop.gremlin.groovy.console.Console
import scala.collection.mutable.MutableList
import javassist.expr.ExprEditor
import javassist.expr.MethodCall
import com.tinkerpop.frames.modules.typedgraph.TypedGraphModuleBuilder


object IndexApp extends App {
  println( "Start" )
  
  val f = new FramedGraphFactory(
      new GremlinGroovyModule(), 
      new TypedGraphModuleBuilder()
        .withClass(classOf[ClassDef])
        .withClass(classOf[MethodDef])
        .withClass(classOf[FieldDef])
        .build()); 

  val graph = f.create(new TinkerGraph());

  val cp = ClassPool.getDefault  
  
  def indexClass(c:CtClass): ClassDef = {
    var cd = graph.getVertex(c.getName, classOf[ClassDef])
    if (cd != null)
      cd
    else {
      cd = graph.addVertex(c.getName, classOf[ClassDef])
      cd.setName(c.getSimpleName)
      c.getInterfaces.map(indexClass).flatMap(i=>i::i.getImplements.toList).foreach(if (c.isInterface()) cd.addExtends(_) else cd.addImplements(_))
      Option(c.getSuperclass).map(indexClass).toList.flatMap(s=>s::s.getExtends.toList).foreach(cd.addExtends(_))
      c.getDeclaredMethods.map(indexMethod).foreach(cd.addMethod(_)) // add methods of class
      cd.getExtends.flatMap(_.getMethods).foreach(cd.addMethod(_)) // add methods of superclass(es)
      c.getDeclaredFields.map(indexField).foreach(cd.addField(_))
      cd.getExtends.flatMap(_.getFields).foreach(cd.addField(_))
      cd.setAccess(indexAccess(c.getModifiers))
      if (c.isArray()) {
        indexClass(c.getComponentType)
      }
      cd
    }    
  }
  
  def indexMethod(m:CtMethod) = {
    var md = graph.getVertex(m.getLongName, classOf[MethodDef])
    if (md != null)
      md
    else {
      md = graph.addVertex(m.getLongName, classOf[MethodDef])
      md.setName(m.getName)
      Option(m.getReturnType).map(indexClass).foreach(md.setReturnType(_))
      m.getParameterTypes.map(indexClass).foreach(md.addParamterType(_))
        md.setStatic((m.getModifiers & Modifier.STATIC) > 0)
      md.setAccess(indexAccess(m.getModifiers))
      if ((m.getModifiers & Modifier.NATIVE) == 0 && !m.getDeclaringClass.isInterface()) {
        indexInvocations(m)
      }
      md
    } 
  }
  
  def indexField(f:CtField) = {
    val n = f.getDeclaringClass.getName + "." + f.getName
    var fd = graph.getVertex(n, classOf[FieldDef])
    if (fd != null)
      fd
    else {
      fd = graph.addVertex(n, classOf[FieldDef])
      fd.setName(f.getName)
      fd.setType(indexClass(f.getType))
      fd.setStatic((f.getModifiers & Modifier.STATIC) > 0)
      fd.setAccess(indexAccess(f.getModifiers))
    }
    fd
  }

  def indexInvocations(m:CtMethod) {
    try {
      m.instrument(new ExprEditor() {
        override def edit(mc:MethodCall) {
          indexMethod(m).addCall(indexMethod(mc.getMethod))
        }
      });
    } catch {
      case e => println(s"${e}; couldn't inspect ${m.getLongName}")
    }
  }
  
  def indexAccess(m:Int) = {
    if ((m & Modifier.PRIVATE) > 0)
      Access.PRIVATE
    else if ((m & Modifier.PROTECTED) > 0)
      Access.PROTECTED
    else if ((m & Modifier.PUBLIC) > 0)
      Access.PUBLIC
    else
      Access.PACKAGE
  }
  
  val libDir = new File(System.getProperty("java.home") + "/lib")
  val libJars = libDir.listFiles().filter{_.getName.endsWith(".jar")}.map{new JarFile(_)}
  val libClasses = libJars.flatMap{_.entries().map{_.getName}.filter{ _.endsWith(".class")}.map{_.replaceAll("\\.class$", "").replaceAll("/",".")}}
  libClasses.flatMap(c => Try(cp.getCtClass(c)).toOption).map(indexClass)

  println("Done parsing")
  
  Console.getGroovysh.getInterp.getContext.setProperty("g", graph)
  val c = new Console(null);
  
  println( "End" )   
}
