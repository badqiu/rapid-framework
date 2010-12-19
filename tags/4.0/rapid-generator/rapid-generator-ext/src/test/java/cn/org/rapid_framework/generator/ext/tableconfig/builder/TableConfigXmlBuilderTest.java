package cn.org.rapid_framework.generator.ext.tableconfig.builder;

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
import cn.org.rapid_framework.generator.ext.tableconfig.model.TableConfig;
import cn.org.rapid_framework.generator.ext.tableconfig.model.TableConfigSet;
import cn.org.rapid_framework.generator.ext.tableconfig.model.TableConfig.OperationConfig;
import cn.org.rapid_framework.generator.util.FileHelper;
import cn.org.rapid_framework.generator.util.StringHelper;

public class TableConfigXmlBuilderTest extends TestCase {
	private File basedir;

	public void setUp() throws Exception {
		GeneratorProperties.setProperty("appName", "rapid");
		GeneratorProperties.setProperty("outRoot", "./target/temp/"+getClass().getSimpleName()+"/"+getName());
		System.setProperty(GeneratorConstants.GG_IS_OVERRIDE.code,"true");
		basedir = FileHelper.getFileByClassLoader("cn/org/rapid_framework/generator/ext/tableconfig");
	}
	
	public void test() throws IOException, Exception {
		List list = new ArrayList();
		list.add("mybatis_user_info.xml");
		list.add("user_info.xml");
		
		TableConfigSet tableConfigSet = new TableConfigXmlBuilder().parseFromXML(basedir,"com.company.test", list);
		GeneratorFacade gf = new GeneratorFacade();
		Map map = new HashMap();
		map.put("tableConfigSet", tableConfigSet);
		map.put("tableConfigs", tableConfigSet.getTableConfigs());
		map.put("basepackage", "com.company.project");
		map.put("sequencesList", "list,list");
		map.put("StringHelper", new StringHelper());
		
		gf.getGenerator().addTemplateRootDir(FileHelper.getFileByClassLoader("for_generate_by_table_config_set"));
		gf.generateByMap(map);
	}
	
	public void testload() throws IOException {
	    TableConfig config = new TableConfigXmlBuilder().parseFromXML(FileHelper.getFileByClassLoader("for_test_table_config_xml_builder/pdc_card.xml"));
	    OperationConfig oc = config.getOperation("getRealNameCardsByParentCardNo");
	    assertTrue(oc.getSql().indexOf("WHERE parent_card_no = ?  AND status = ? AND type = 'R'") >= 0);
	    assertFalse(oc.getSql(),oc.getSql().indexOf("<dynamic>") >= 0);
	    assertTrue(oc.getSqlmap().indexOf("<dynamic>") >= 0);
	    assertFalse(oc.getSqlmap().indexOf("WHERE parent_card_no = ?  AND status = ? AND type = 'R'") >= 0);
	    for(Object item : config.getOperations()) {
	    }
	}
}
