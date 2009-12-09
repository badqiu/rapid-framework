package cn.org.rapid_framework.web.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.sun.org.apache.bcel.internal.generic.RETURN;

public class BlockTag extends BodyTagSupport{

	private String name;
	
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return EVAL_BODY_INCLUDE or EVAL_BODY_BUFFERED or SKIP_BODY
	 */
	@Override
	public int doStartTag() throws JspException {
		return EVAL_BODY_BUFFERED;
	}

	/**
	 * @return EVAL_BODY_AGAIN or SKIP_BODY
	 */
	@Override
	public int doAfterBody() throws JspException {
		BodyContent b = getBodyContent();
		System.out.println("block_content:"+b.getString());
		String varName = Utils.getOverrideVariableName(name);
		String overrideContent = getOverriedContent(varName);
		String outputContent = overrideContent == null ? b.getString() : overrideContent;
		//b.clearBody();
		try {
			b.getEnclosingWriter().write(outputContent);
			//pageContext.getOut().append(outputContent);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SKIP_BODY;
	}
	
	/**
	 * @return EVAL_PAGE or SKIP_PAGE
	 */
	@Override
	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}
	
	@Override
	public void setBodyContent(BodyContent b) {
		super.setBodyContent(b);
	}

	private String getOverriedContent(String varName) {
		return (String)pageContext.getAttribute(varName);
	}
}
