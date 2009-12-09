package cn.org.rapid_framework.web.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TagSupport;

public class OverrideTag extends BodyTagSupport{
		
	private String name;
	
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int doStartTag() throws JspException {
		return EVAL_BODY_BUFFERED;
	}

	@Override
	public int doAfterBody() throws JspException {
		BodyContent b = getBodyContent();
		System.out.println("override:"+b.getString());
		String varName = Utils.getOverrideVariableName(name);
		if(pageContext.getAttribute(varName) == null) {
			pageContext.setAttribute(varName, b.getString());
		}
		b.clearBody();
		return SKIP_BODY;
	}
	
	@Override
	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}
	
	@Override
	public void setBodyContent(BodyContent b) {
		super.setBodyContent(b);
	}
	
}
