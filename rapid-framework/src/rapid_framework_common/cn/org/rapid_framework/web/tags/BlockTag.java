package cn.org.rapid_framework.web.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.TagSupport;

public class BlockTag extends TagSupport{

	private String name;
	
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return EVAL_BODY_INCLUDE or EVAL_BODY_BUFFERED or SKIP_BODY
	 */
	@Override
	public int doStartTag() throws JspException {
		return getOverriedContent() == null ? EVAL_BODY_INCLUDE : SKIP_BODY;
	}

	/**
	 * @return EVAL_PAGE or SKIP_PAGE
	 */
	@Override
	public int doEndTag() throws JspException {
		String overriedContent = getOverriedContent();
		if(overriedContent == null) {
			return EVAL_PAGE;
		}
		
		try {
			pageContext.getOut().write(overriedContent);
		} catch (IOException e) {
			throw new JspException("tag output error",e);
		}
		return EVAL_PAGE;
	}
	
	private String getOverriedContent() {
		String varName = Utils.getOverrideVariableName(name);
		return (String)pageContext.getAttribute(varName);
	}
}
