package cn.org.rapid_framework.freemarker2; 

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import cn.org.rapid_framework.freemarker2.OverrideDirective.TemplateDirectiveBodyModel;
import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModelException;

/**
 * @author badqiu
 */
public class DirectiveUtils {
	
	public static String BLOCK = "__ftl_override__";
	
	public static String getOverrideVariableName(String name) {
		return BLOCK + name;
	}
	
	public static void exposeRapidMacros(Configuration conf) {
		conf.setSharedVariable(BlockDirective.DIRECTIVE_NAME, new BlockDirective());
		conf.setSharedVariable(ExtendsDirective.DIRECTIVE_NAME, new ExtendsDirective());
		conf.setSharedVariable(OverrideDirective.DIRECTIVE_NAME, new OverrideDirective());
	}
	
	static String getRequiredParam(Map params,String key) throws TemplateException {
		Object value = params.get(key);
		if(value == null || StringUtils.isEmpty(value.toString())) {
			throw new TemplateModelException("not found required parameter:"+key+" for directive");
		}
		return value.toString();
	}
	
	static String getParam(Map params,String key,String defaultValue) throws TemplateException {
		Object value = params.get(key);
		return value == null ? defaultValue : value.toString();
	}
	
	static TemplateDirectiveBodyModel getOverrideBody(Environment env, String name) throws TemplateModelException {
		TemplateDirectiveBodyModel value = (TemplateDirectiveBodyModel)env.getVariable(DirectiveUtils.getOverrideVariableName(name));
		return value;
	}
	
	static class Node {
		Node child;
		Node parent;
		TemplateDirectiveBody body;
		public Node(Node parent,Node child, TemplateDirectiveBody body) {
			this.parent = parent;
			this.child = child;
			this.body = body;
		}
		public String toString() {
			return " body:"+body+",parent:"+parent+"\n";
		}
	}
	
	static Node toTreeNode(TemplateDirectiveBodyModel overrideBody) {
		Node result = null;
		
		Node child = null;
		Node current = null;
		while(!overrideBody.bodys.isEmpty()) {
			TemplateDirectiveBody item = overrideBody.bodys.poll();
			current = new Node(null,null,item);
			if(result == null) result = current;
			if(child != null) {
				current.child = child;
				child.parent = current;
			}
			child = current;
		}
		return result;
	}
}
