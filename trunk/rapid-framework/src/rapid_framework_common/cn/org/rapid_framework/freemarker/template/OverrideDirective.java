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

public class OverrideDirective implements TemplateDirectiveModel {
	
	public void execute(Environment env,
            Map params, TemplateModel[] loopVars,
            TemplateDirectiveBody body) throws TemplateException, IOException {
		String name = Utils.getRequiredParam(params, "name", env);
		if(env.getVariable(Utils.getBlockVariableName(name)) == null) {
			StringWriter out = new StringWriter(1024);
			body.render(out);
			env.setVariable(Utils.getBlockVariableName(name),new StringModel(out.toString(),new BeansWrapper()));
		}
	}

}
