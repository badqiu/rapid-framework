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
        
        File file = FileHelper.getFileByClassLoader("XMLHelper_test.xml");
        NodeData nd = new XMLHelper().parseXML(new FileInputStream(file));
        
        Map table = nd.attributes;
        List<NodeData> columns = nd.childs;
        assertEquals("table_name",nd.nodeName);
        assertEquals("cn1",table.get("className"));
        assertEquals("ta1",table.get("tableAlias"));
        
        verifyColumn(columns.get(0),"{sqlName=username, columnName=c1, columnAlias=a1}");
        verifyColumn(columns.get(1),"{sqlName=password, columnName=c2, columnAlias=a2}");
        verifyColumn(columns.get(2),"{sqlName=sex, columnName=c3, columnAlias=a3}");
        
        assertEquals(columns.size(),3);
        
        System.out.println(table);
        System.out.println(columns);
    }
    
    public void test_getXMLEncoding() {
    	assertEquals("UTF-8",XMLHelper.getXMLEncoding("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
    	assertEquals("UTF-8",XMLHelper.getXMLEncoding("<?xml version=\"1.0\" encoding='UTF-8'?>"));
    }

    public void test_removeXmlns() {
//    	assertEquals("abc  dd ",XMLHelper.removeXmlns("abc xmlns=\"http://maven.apache.org/POM/4.0.0\" dd xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""));
    	assertEquals("abc  dd xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"",XMLHelper.removeXmlns("abc xmlns=\"http://maven.apache.org/POM/4.0.0\" dd xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""));
    	assertEquals("abc ",XMLHelper.removeXmlns("abc xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\""));
//    	assertEquals("UTF-8",XMLHelper.removeXmlns("<?xml version=\"1.0\" encoding='UTF-8'?>"));
    }
    
    public void verifyColumn(NodeData c, String expected) {
        Map column = c.getElementMap("sqlName");
        assertEquals(expected,column.toString());
    }
    
    public void test_parse_SqlConfig() throws Exception {
    	File file = FileHelper.getFileByClassLoader("generator_config/sql/USER_INFO.xml");
        NodeData nd = new XMLHelper().parseXML(new FileInputStream(file));
        System.out.println("sql config:\n"+nd);
    }
    
    public void test()  throws Exception{
    	File file = FileHelper.getFileByClassLoader("fortest_gen_by_sqlmap/UserInfoSqlMap.xml");
    	NodeData nd = new XMLHelper().parseXML(new FileInputStream(file));
        System.out.println("sql config:\n"+nd);
    }
}
