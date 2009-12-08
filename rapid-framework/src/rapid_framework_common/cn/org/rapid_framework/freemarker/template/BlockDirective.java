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

public class BlockDirective implements TemplateDirectiveModel{

	public void execute(Environment env,
            Map params, TemplateModel[] loopVars,
            TemplateDirectiveBody body) throws TemplateException, IOException {
		String name = getRequiredParam(params,"name",env);
		String overrideContent = getOverrideContent(env, name);
		if(overrideContent == null) {
			body.render(env.getOut());
		}else {
			env.getOut().append(overrideContent);
		}
	}

	public static String getRequiredParam(Map params,String key,Environment env) throws TemplateException {
		Object value = params.get(key);
		if(value == null || StringUtils.isEmpty(value.toString())) {
			throw new TemplateException("not found required param:"+key,env);
		}
		return value.toString();
	}

	private String getOverrideContent(Environment env, String name) throws TemplateModelException {
		StringModel stringModel = ((StringModel)env.getVariable(OverrideDirective.getBlockName(name)));
		if(stringModel == null)
			return null;
		else
			return stringModel.getAsString();
	}

}
