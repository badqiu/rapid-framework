package cn.org.rapid_framework.freemarker2; 

import java.io.IOException;
import java.util.Map;

import cn.org.rapid_framework.freemarker2.BlockDirective.OverrideContext;
import cn.org.rapid_framework.freemarker2.DirectiveUtils.Node;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * @author badqiu
 */
public class SuperDirective implements TemplateDirectiveModel{
	public final static String DIRECTIVE_NAME = "super";
	
	public void execute(Environment env,
            Map params, TemplateModel[] loopVars,
            TemplateDirectiveBody body) throws TemplateException, IOException {
		Node current = OverrideContext.currentNode.get();
		if(current == null) {
			throw new TemplateException("super direction must be child of override", env);
		}
		
		Node parent = current.parent;
		if(parent == null) {
			throw new TemplateException("not found super block", env);
		}
		try {
			OverrideContext.currentNode.set(parent);
			parent.body.render(env.getOut());
		}finally {
			OverrideContext.currentNode.set(current);
		}
	}
	
}
