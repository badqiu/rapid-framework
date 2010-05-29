package cn.org.rapid_framework.generator.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.xml.sax.SAXException;

import cn.org.rapid_framework.generator.util.XMLHelper.NodeData;


public class XMLHelperTest extends TestCase {
    
    public void test_blog() throws FileNotFoundException, SAXException, IOException {
        
        File file = FileHelper.getRsourcesByClassLoader("classpath:XMLHelper_test.xml");
        NodeData nd = new XMLHelper().parseXML(new FileInputStream(file));
        
        Map table = nd.attributes;
        List<NodeData> columns = nd.childs;
        assertEquals("table_name",nd.nodeName);
        assertEquals("cn1",table.get("className"));
        assertEquals("ta1",table.get("tableAlias"));
        
        verifyColumn(columns.get(0),"{columnAlias=a1, sqlName=username, columnName=c1}");
        verifyColumn(columns.get(1),"{columnAlias=a2, sqlName=password, columnName=c2}");
        verifyColumn(columns.get(2),"{columnAlias=a3, sqlName=sex, columnName=c3}");
        
        assertEquals(columns.size(),3);
        
        System.out.println(table);
        System.out.println(columns);
    }
    
    public void verifyColumn(NodeData c, String expected) {
        Map column = c.getElementMap("sqlName");
        assertEquals(expected,column.toString());
    }
}
