package cn.org.rapid_framework.web.tags;

import javax.servlet.jsp.tagext.TagSupport;

public class ExtendsTag extends TagSupport{
	
	private String name;
	
	public void setName(String name) {
		this.name = name;
	}
	
}
