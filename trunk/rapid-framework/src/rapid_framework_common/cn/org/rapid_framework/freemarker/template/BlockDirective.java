package cn.org.rapid_framework.freemarker.template;

import java.io.IOException;
import java.util.Map;

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
	
	public String getName() {
		return "block";
	}
	
	public void execute(Environment env,
            Map params, TemplateModel[] loopVars,
            TemplateDirectiveBody body) throws TemplateException, IOException {
		String name = DirectiveUtils.getRequiredParam(params, "name", env);
		String overrideContent = getOverrideContent(env, name);
		if(overrideContent == null) {
			if(body != null) 
				body.render(env.getOut());
		}else {
			env.getOut().append(overrideContent);
		}
	}

	private String getOverrideContent(Environment env, String name) throws TemplateModelException {
		TemplateScalarModel value = ((TemplateScalarModel)env.getVariable(DirectiveUtils.getOverrideVariableName(name)));
		if(value == null)
			return null;
		else
			return value.getAsString();
	}

}
