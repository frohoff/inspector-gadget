package org.frohoff.inspectorgadget.model;

import com.tinkerpop.frames.Property;

public interface MemberDef extends BaseDef {
	@Property("static")
	public boolean isStatic();
	@Property("static")
	public void setStatic(boolean s);
	
	@Property("access")
	public Access getAccess();
	@Property("access")
	public void setAccess(Access a);	
}
