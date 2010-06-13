package cn.org.rapid_framework.struts;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.config.ModuleConfigFactory;

public class DynamicModuleConfigFactory extends ModuleConfigFactory implements Serializable{
	Logger log = Logger.getLogger(DynamicModuleConfigFactory.class);
	public ModuleConfig createModuleConfig(String prefix) {
		log.info("use "+DynamicModuleConfig.class+" class for ModuleConfig");
		return new DynamicModuleConfig(prefix);
	}
}

