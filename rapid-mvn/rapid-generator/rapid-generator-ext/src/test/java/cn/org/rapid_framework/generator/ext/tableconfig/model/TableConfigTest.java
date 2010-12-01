package cn.org.rapid_framework.generator.ext.tableconfig.model;

import junit.framework.TestCase;
import cn.org.rapid_framework.generator.GeneratorConstants;
import cn.org.rapid_framework.generator.GeneratorProperties;

public class TableConfigTest extends TestCase {
    
    public void test() {
        TableConfig tc = new TableConfig();
        tc.setSqlname("user_info");
        assertEquals(tc.getClassName(),"UserInfo");
        
        GeneratorProperties.setProperty(GeneratorConstants.TABLE_NAME_SINGULARIZE, "true");
        TableConfig table = new TableConfig();
        table.setSqlname("bashes");
        assertEquals("Bashe",table.getClassName());
        
        table.setSqlname("cuStomeRs");
        assertEquals("CuStomeR",table.getClassName());
        
        GeneratorProperties.setProperty(GeneratorConstants.TABLE_REMOVE_PREFIXES, "t_,v_");
        table.setSqlname("t_user_infos");
        assertEquals("UserInfo",table.getClassName());
        
        GeneratorProperties.setProperty(GeneratorConstants.TABLE_NAME_SINGULARIZE, "false");
        table.setSqlname("bashes");
        assertEquals("Bashes",table.getClassName());
        
        table.setSqlname("cuStomeRs");
        assertEquals("CuStomeRs",table.getClassName());
        
        GeneratorProperties.setProperty(GeneratorConstants.TABLE_REMOVE_PREFIXES, "t_,v_");
        table.setSqlname("t_user_infos");
        assertEquals("UserInfos",table.getClassName());
    }
}
