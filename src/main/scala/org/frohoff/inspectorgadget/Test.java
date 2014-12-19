package org.frohoff.inspectorgadget;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;

public class Test {
	public static void main(String[] args) throws Exception {
		TemplatesImpl ti = new TemplatesImpl();
		
		Class aih = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");
		
		//System.out.println(Arrays.asList(aih.getConstructors()));
		Constructor ctor = aih.getDeclaredConstructor(Class.class,Map.class);
		ctor.setAccessible(true);
//		InvocationHandler ih = (InvocationHandler) ctor.newInstance(
//				com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl.class, 
//				new HashMap(){{}}); 
//		Comparable c = (Comparable) Proxy.newProxyInstance(Test.class.getClassLoader(), new Class[] {Comparable.class}, ih);
//		c.equals(ti);
		equalsImpl(ti);
	}
	
	private static Class type = TemplatesImpl.class;
	private static Map<String,Object> memberValues = new HashMap<String, Object>(){{}};
   private static Boolean equalsImpl(Object o) {

        if (!type.isInstance(o))
            return false;
        for (Method memberMethod : type.getDeclaredMethods()) {
        	memberMethod.setAccessible(true);
            String member = memberMethod.getName();
            Object ourValue = memberValues.get(member);
            Object hisValue = null;
                try {
                    hisValue = memberMethod.invoke(o);
                } catch (InvocationTargetException e) {
                    return false;
                } catch (IllegalAccessException e) {
                    throw new AssertionError(e);
                }
            if (ourValue.equals(hisValue))
                return false;
        }
        return true;
    }
}
