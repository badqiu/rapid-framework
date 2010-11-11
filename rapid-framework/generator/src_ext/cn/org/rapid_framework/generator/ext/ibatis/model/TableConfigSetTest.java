package cn.org.rapid_framework.generator.ext.ibatis.model;

import junit.framework.TestCase;

public class TableConfigSetTest extends TestCase {
    
    public void test() {
        TableConfigSet set = new TableConfigSet();
        assertTrue(set.getSequences().isEmpty());
        
        TableConfig config = new TableConfig();
        set.addTableConfig(config);
        assertTrue(set.getSequences().isEmpty());
        
        config.setSequence("123");
        assertFalse(set.getSequences().isEmpty());
    }
}
