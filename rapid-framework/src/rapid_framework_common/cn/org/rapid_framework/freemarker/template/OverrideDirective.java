package cn.org.rapid_framework.freemarker.template;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import freemarker.core.Environment;
import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.StringModel;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * @author badqiu
 */
public class OverrideDirective implements TemplateDirectiveModel {
	
	public void execute(Environment env,
            Map params, TemplateModel[] loopVars,
            TemplateDirectiveBody body) throws TemplateException, IOException {
		String name = Utils.getRequiredParam(params, "name", env);
		String overrideVariableName = Utils.getOverrideVariableName(name);
		if(env.getVariable(overrideVariableName) == null) {
			StringWriter out = new StringWriter(512);
			body.render(out);
			env.setVariable(overrideVariableName,new StringModel(out.toString(),new BeansWrapper()));
		}
	}

}
