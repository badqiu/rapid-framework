package cn.org.rapid_framework.generator;

import java.util.Map;
/**
 * 
 * @author badqiu
 *
 */
public interface IGeneratorModelProvider {

	public String getDisaplyText();
	
	public void mergeTemplateModel(Map model) throws Exception;
	
	public void mergeFilePathModel(Map model) throws Exception;
	
}
