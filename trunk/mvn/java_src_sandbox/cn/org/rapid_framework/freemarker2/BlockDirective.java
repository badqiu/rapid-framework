package cn.org.rapid_framework.freemarker2; 

import java.io.IOException;
import java.util.Map;

import cn.org.rapid_framework.freemarker2.OverrideDirective.TemplateDirectiveBodyModel;
import cn.org.rapid_framework.freemarker2.DirectiveUtils.Node;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * @author badqiu
 */
public class BlockDirective implements TemplateDirectiveModel{
	public final static String DIRECTIVE_NAME = "block";
	
	public void execute(Environment env,
            Map params, TemplateModel[] loopVars,
            TemplateDirectiveBody body) throws TemplateException, IOException {
		String name = DirectiveUtils.getRequiredParam(params, "name");
		TemplateDirectiveBodyModel overrideBody = getOverrideBody(env, name);
		if(overrideBody == null && body != null) {
			body.render(env.getOut());
			return;
		}
		
		TemplateDirectiveBody outputBody = overrideBody.bodys.peek();
		overrideBody.bodys.offer(body);
		Node node = DirectiveUtils.toTreeNode(overrideBody);
		
		if(outputBody != node.body) throw new IllegalStateException();
		if(node.parent == null) throw new IllegalStateException();
		
		try {
			OverrideContext.currentNode.set(node);
			node.body.render(env.getOut());
		}finally {
			OverrideContext.currentNode.set(null);
		}
	}

	private TemplateDirectiveBodyModel getOverrideBody(Environment env, String name) throws TemplateModelException {
		TemplateDirectiveBodyModel value = (TemplateDirectiveBodyModel)env.getVariable(DirectiveUtils.getOverrideVariableName(name));
		return value;
	}
	
	public static class OverrideContext {
		public static ThreadLocal<Node> currentNode = new ThreadLocal<Node>();
	}
	
}
