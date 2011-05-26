package cn.org.rapid_framework.generator.ext.tableconfig.model;

import junit.framework.TestCase;
import cn.org.rapid_framework.generator.GeneratorConstants;
import cn.org.rapid_framework.generator.GeneratorProperties;

public class TableConfigTest extends TestCase {
    
    public void test() {
        TableConfig tc = new TableConfig();
        tc.setSqlName("user_info");
        assertEquals(tc.getClassName(),"UserInfo");
        
        GeneratorProperties.setProperty(GeneratorConstants.TABLE_NAME_SINGULARIZE, "true");
        TableConfig table = new TableConfig();
        table.setSqlName("bashes");
        assertEquals("Bash",table.getClassName());
        
        table.setSqlName("cuStomeRs");
        assertEquals("CuStomeR",table.getClassName());
        
        GeneratorProperties.setProperty(GeneratorConstants.TABLE_REMOVE_PREFIXES, "t_,v_");
        table.setSqlName("t_user_infos");
        assertEquals("UserInfo",table.getClassName());
        
        GeneratorProperties.setProperty(GeneratorConstants.TABLE_NAME_SINGULARIZE, "false");
        table.setSqlName("bashes");
        assertEquals("Bashes",table.getClassName());
        
        table.setSqlName("cuStomeRs");
        assertEquals("CuStomeRs",table.getClassName());
        
        GeneratorProperties.setProperty(GeneratorConstants.TABLE_REMOVE_PREFIXES, "t_,v_");
        table.setSqlName("t_user_infos");
        assertEquals("UserInfos",table.getClassName());
    }
}
