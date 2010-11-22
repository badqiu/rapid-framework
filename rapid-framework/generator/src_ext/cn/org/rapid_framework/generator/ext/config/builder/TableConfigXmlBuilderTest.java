package cn.org.rapid_framework.generator.ext.config.builder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;
import cn.org.rapid_framework.generator.GeneratorConstants;
import cn.org.rapid_framework.generator.GeneratorFacade;
import cn.org.rapid_framework.generator.GeneratorProperties;
import cn.org.rapid_framework.generator.ext.tableconfig.model.TableConfigSet;
import cn.org.rapid_framework.generator.util.FileHelper;

public class TableConfigXmlBuilderTest extends TestCase {
	private File basedir;

	public void setUp() throws Exception {
		GeneratorProperties.setProperty("appName", "rapid");
		GeneratorProperties.setProperty("outRoot", "./temp/"+getClass().getSimpleName()+"/"+getName());
		System.setProperty(GeneratorConstants.GG_IS_OVERRIDE,"true");
		basedir = FileHelper.getFileByClassLoader("cn/org/rapid_framework/generator/ext/ibatis");
	}
	
	public void test() throws IOException, Exception {
		List list = new ArrayList();
		list.add("mybatis_user_info.xml");
		list.add("user_info.xml");
		
		TableConfigSet tableConfigSet = new TableConfigXmlBuilder().parseFromXML(basedir, list);
		GeneratorFacade gf = new GeneratorFacade();
		Map map = new HashMap();
		map.put("tableConfigSet", tableConfigSet);
		map.put("tableConfigs", tableConfigSet.getTableConfigs());
		gf.generateByMap(map, FileHelper.getFileByClassLoader("for_generate_by_table_config_set").getAbsolutePath());
	}
	
}
