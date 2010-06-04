package cn.org.rapid_framework.freemarker.directive; 

import java.io.IOException;
import java.util.Map;

import cn.org.rapid_framework.freemarker.directive.OverrideDirective.TemplateDirectiveBodyModel;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;

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
		TemplateDirectiveBody outputBody = overrideBody == null ? body : overrideBody.body;
		if(outputBody != null) {
			outputBody.render(env.getOut());
		}
	}

	private TemplateDirectiveBodyModel getOverrideBody(Environment env, String name) throws TemplateModelException {
		TemplateDirectiveBodyModel value = (TemplateDirectiveBodyModel)env.getVariable(DirectiveUtils.getOverrideVariableName(name));
		return value;
	}

}
