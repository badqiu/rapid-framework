package cn.org.rapid_framework.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.Assert;

/**
 * 搜索class的工具类
 * @author badqiu
 *
 */
public class ScanClassUtils {
	/**
	 * 根据多个包名搜索class
	 * 例如: ScanClassUtils.scanPakcages("javacommon.**.*");
	 * @param basePackages 各个包名使用逗号分隔,各个包名可以有通配符
	 * @return List包含className
	 */
	@SuppressWarnings("all")
	public static List<String> scanPackages(String basePackages) throws IllegalArgumentException{
		Assert.notNull(basePackages,"'basePakcages' must be not null");
		ResourcePatternResolver rl = new PathMatchingResourcePatternResolver();
		MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(rl); 
		List result = new ArrayList();
		String[] arrayPackages = basePackages.split(",");
		try {
			for(int j = 0; j < arrayPackages.length; j++) {
				String packageToScan = arrayPackages[j];
				String packagePart = packageToScan.replace('.', '/');
				String classPattern = "classpath*:/" + packagePart + "/**/*.class";
				Resource[] resources = rl.getResources(classPattern);
				for (int i = 0; i < resources.length; i++) {
					Resource resource = resources[i];
					MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);   
					String className = metadataReader.getClassMetadata().getClassName();
					result.add(className);
				}
			}
		}catch(Exception e) {
			new IllegalArgumentException("scan pakcage class error,pakcages:"+basePackages);
		}

		return result;
	}
	
}
