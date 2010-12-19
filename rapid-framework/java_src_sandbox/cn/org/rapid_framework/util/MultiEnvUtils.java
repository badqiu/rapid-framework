package cn.org.rapid_framework.util;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

public class MultiEnvUtils {
	public static char DEFAULT_SEPERATOR = '-';
	
	public static String getFilenameByEnv(String filename,String env) {
		return getFilenameByEnv(filename, env,DEFAULT_SEPERATOR);
	}

	public static String getFilenameByEnv(String filename,String env,char seperator) {
		String extension = FilenameUtils.getExtension(filename);
		return FilenameUtils.getBaseName(filename)+seperator+env+ (StringUtils.isBlank(extension) ? "" : "."+extension);
	}
	
	public static void main(String[] args) {
		Assert.isTrue("app-dev.properties".equals(getFilenameByEnv("app.properties", "dev")));
		Assert.isTrue("app-prod.properties".equals(getFilenameByEnv("app.properties", "prod")));
		Assert.isTrue("app-dev".equals(getFilenameByEnv("app", "dev")),getFilenameByEnv("app", "dev"));
		Assert.isTrue("app_dev".equals(getFilenameByEnv("app", "dev",'_')),getFilenameByEnv("app", "dev"));
		System.out.println("executed");
	}
}
