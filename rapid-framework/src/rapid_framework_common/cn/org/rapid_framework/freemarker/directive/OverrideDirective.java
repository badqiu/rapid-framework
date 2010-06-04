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
		
		if(env.getVariable(overrideVariableName) == null) {
			env.setVariable(overrideVariableName, new TemplateDirectiveBodyModel(body));
		}
	}
	
	public static class TemplateDirectiveBodyModel implements TemplateModel{
		public TemplateDirectiveBody body;
		public TemplateDirectiveBodyModel(TemplateDirectiveBody body) {
			this.body = body;
		}
	}
}
