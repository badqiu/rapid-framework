package cn.org.rapid_framework.freemarker2; 

import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * @author badqiu
 */
public class OverrideDirective implements TemplateDirectiveModel {

	public final static String DIRECTIVE_NAME = "override";
	
	public void execute(Environment env,
            Map params, TemplateModel[] loopVars,
            TemplateDirectiveBody body) throws TemplateException, IOException {
		String name = DirectiveUtils.getRequiredParam(params, "name");
		String overrideVariableName = DirectiveUtils.getOverrideVariableName(name);
		
		if(isOverrieded(env, overrideVariableName)) {
			TemplateDirectiveBodyModel override = DirectiveUtils.getOverrideBody(env, name);
			override.bodys.offer(body);
		}else {
			env.setVariable(overrideVariableName, new TemplateDirectiveBodyModel(body));
		}
	}

	private boolean isOverrieded(Environment env, String overrideVariableName) throws TemplateModelException {
		return env.getVariable(overrideVariableName) != null;
	}
	
	public static class TemplateDirectiveBodyModel implements TemplateModel{
		public LinkedList<TemplateDirectiveBody> bodys = new LinkedList<TemplateDirectiveBody>();
		public TemplateDirectiveBodyModel(TemplateDirectiveBody body) {
			this.bodys.offer(body);
		}
	}
//	
//	public class TemplateDirectiveBodyWraper implements TemplateDirectiveBody{
//		TemplateDirectiveBody body;
//		@Override
//		public void render(Writer out) throws TemplateException, IOException {
//			Node current = OverrideContext.currentNode.get();
//			body.render(out);
//		}
//	}
	
}
