package cn.org.rapid_framework.freemarker.directive; 

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import freemarker.core.Environment;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;

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
		
		if(isOverried(env, overrideVariableName)) {
			return;
		}
		
		env.setVariable(overrideVariableName, new TemplateDirectiveBodyModel(body));
	}

	private boolean isOverried(Environment env, String overrideVariableName) throws TemplateModelException {
		return env.getVariable(overrideVariableName) != null;
	}
	
	public static class TemplateDirectiveBodyModel implements TemplateModel{
		public TemplateDirectiveBody body;
		public TemplateDirectiveBodyModel(TemplateDirectiveBody body) {
			this.body = body;
		}
	}
}
