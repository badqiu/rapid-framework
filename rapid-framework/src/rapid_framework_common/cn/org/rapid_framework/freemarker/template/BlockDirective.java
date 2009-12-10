package cn.org.rapid_framework.freemarker.template;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import freemarker.core.Environment;
import freemarker.ext.beans.StringModel;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * @author badqiu
 */
public class BlockDirective implements TemplateDirectiveModel{

	public void execute(Environment env,
            Map params, TemplateModel[] loopVars,
            TemplateDirectiveBody body) throws TemplateException, IOException {
		String name = Utils.getRequiredParam(params, "name", env);
		String overrideContent = getOverrideContent(env, name);
		if(overrideContent == null) {
			body.render(env.getOut());
		}else {
			env.getOut().append(overrideContent);
		}
	}

	private String getOverrideContent(Environment env, String name) throws TemplateModelException {
		StringModel stringModel = ((StringModel)env.getVariable(Utils.getOverrideVariableName(name)));
		if(stringModel == null)
			return null;
		else
			return stringModel.getAsString();
	}

}
