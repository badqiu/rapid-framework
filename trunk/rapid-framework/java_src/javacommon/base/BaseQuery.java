package javacommon.base;

import cn.org.rapid_framework.page.PageRequest;

public class BaseQuery extends PageRequest implements java.io.Serializable {
	private static final long serialVersionUID = -360860474471966681L;

	public BaseQuery() {
		setPageSize(10);
	}
	  
}
