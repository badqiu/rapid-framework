package cn.org.rapid_framework.generator.util;

import java.io.ByteArrayInputStream;
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
        
        verifyColumnWithNodeAsKey(columns.get(0),"{columnAlias=a1, columnName=c1, sqlName=username}");
        verifyColumnWithNodeAsKey(columns.get(1),"{columnAlias=a2, columnName=c2, sqlName=password}");
        verifyColumnWithNodeAsKey(columns.get(2),"{columnAlias=a3, columnName=c3, sqlName=sex}");
        
        verifyColumn(columns.get(0),"{columnAlias=a1, columnName=c1}");
        verifyColumn(columns.get(1),"{columnAlias=a2, columnName=c2}");
        verifyColumn(columns.get(2),"{columnAlias=a3, columnName=c3}");
        
        assertEquals(columns.size(),5);
        
        System.out.println(table);
        System.out.println(columns);
    }

    public void test_parseXML_by_innerXML() throws SAXException, IOException {
        NodeData nd = parseXML("<?xml version='1.0' encoding='UTF-8'?> <root><!--comment--><name age='123'>bad&gt;&lt;</name><sex>F<!--123--></sex><alias><![CDATA[&gt;=123<>]]></alias><include refid='123'/></root>");
        assertEquals("bad>&lt;",nd.childs.get(0).innerXML);
        assertEquals("F",nd.childs.get(1).innerXML);
        assertEquals("<![CDATA[&gt;=123<>]]>",nd.childs.get(2).innerXML);
        assertEquals("<include refid=\"123\"/>",nd.childs.get(3).outerXML);
//        assertEquals("<root><name age='123'>bad</name><sex>F</sex><alias><![CDATA[&gt;=123<>]]></alias></root>",nd.innerXML);
//        assertEquals("<name age='123'>bad</name><sex>F</sex><alias><![CDATA[&gt;=123<>]]></alias>",nd.innerXML);
//      assertEquals("<!--comment--><name age='123'>bad</name><sex>F</sex>",nd.innerText);
//      assertEquals("<root><!--comment--><name age='123'>bad</name><sex>F</sex></root>",nd.outerText);
    }

    public void test_parseXML_by_innerXML_escape() throws SAXException, IOException {
        NodeData nd = parseXML("<?xml version='1.0' encoding='UTF-8'?> <root><!--comment--><name age='&apos;&quot;123&gt;&lt;&amp;'>bad&gt;&lt;&apos;&quot;123&gt;&lt;&amp;</name><sex>F<!--123--></sex><alias><![CDATA[&gt;=123<>]]></alias><include refid='123'/></root>");
        assertEquals("<name age=\"'&quot;123>&lt;&amp;\">bad>&lt;'\"123>&lt;&amp;</name>",nd.childs.get(0).outerXML);
        assertEquals("F",nd.childs.get(1).innerXML);
        assertEquals("<![CDATA[&gt;=123<>]]>",nd.childs.get(2).innerXML);
        assertEquals("<include refid=\"123\"/>",nd.childs.get(3).outerXML);
    }
    
    public void test_get_NodeData() throws SAXException, IOException {
    	NodeData nd = parseXML("<?xml version='1.0' encoding='UTF-8'?> <root><!--comment--><name age='123'>bad</name><sex>F</sex></root>");
    	assertEquals("badF",nd.nodeValue);
    	assertEquals("bad",nd.childs.get(0).nodeValue);
    	assertEquals("F",nd.childs.get(1).nodeValue);
    	
    	
    	assertEquals("<root><name age=\"123\">bad</name><sex>F</sex></root>",nd.outerXML);
    	assertEquals("<name age=\"123\">bad</name><sex>F</sex>",nd.innerXML);
//    	assertEquals("<!--comment--><name age='123'>bad</name><sex>F</sex>",nd.innerText);
//    	assertEquals("<root><!--comment--><name age='123'>bad</name><sex>F</sex></root>",nd.outerText);
    	
    }

    public void test_parseXML_by_cdata() throws SAXException, IOException {
		NodeData nd = parseXML("<?xml version='1.0' encoding='UTF-8'?> <root><!--comment--><name age='123'>bad</name><sex>F<!--123--></sex><alias><![CDATA[&gt;=123<>]]></alias></root>");
		assertEquals("bad",nd.childs.get(0).nodeValue);
		assertEquals("F",nd.childs.get(1).nodeValue);
		assertEquals("&gt;=123<>",nd.childs.get(2).nodeValue);
    	assertEquals("<root><name age=\"123\">bad</name><sex>F</sex><alias><![CDATA[&gt;=123<>]]></alias></root>",nd.outerXML);
    	assertEquals("<name age=\"123\">bad</name><sex>F</sex><alias><![CDATA[&gt;=123<>]]></alias>",nd.innerXML);
//    	assertEquals("<!--comment--><name age='123'>bad</name><sex>F</sex>",nd.innerText);
//    	assertEquals("<root><!--comment--><name age='123'>bad</name><sex>F</sex></root>",nd.outerText);
	}

    public void test_parseXML_by_nodevalue() throws SAXException, IOException {
		NodeData nd = parseXML("<?xml version='1.0' encoding='UTF-8'?> <root>AAA<!--comment-->BBB<name age='123'>bad</name><sex>F<!--123--></sex><alias><![CDATA[&gt;=123<>]]></alias>CCC</root>");
		assertEquals("AAABBBbadF&gt;=123<>CCC",nd.nodeValue);
		assertEquals("bad",nd.childs.get(0).nodeValue);
		assertEquals("F",nd.childs.get(1).nodeValue);
		assertEquals("&gt;=123<>",nd.childs.get(2).nodeValue);
		
		nd = parseXML("<?xml version='1.0' encoding='UTF-8'?> <root>AAA<!--comment--><name age='123'>bad</name><sex>F<!--123--></sex><alias><![CDATA[&gt;=123<>]]></alias></root>");
		assertEquals("AAAbadF&gt;=123<>",nd.nodeValue);
	}
    
	private NodeData parseXML(String str) throws SAXException, IOException {
		return new XMLHelper().parseXML(new ByteArrayInputStream(str.getBytes()));
	}
    
    public void test_getXMLEncoding() {
    	assertEquals("UTF-8",XMLHelper.getXMLEncoding("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
    	assertEquals("UTF-8",XMLHelper.getXMLEncoding("<?xml version=\"1.0\" encoding='UTF-8'?>"));
    }
    
    public void test_parseAttributes() {
    	Map map = XMLHelper.parse2Attributes("name='abc' sex='&amp;123' jj=\"123456\" ");
    	assertEquals("abc",map.get("name"));
    	assertEquals("&123",map.get("sex"));
    	assertEquals("123456",map.get("jj"));
    }

    public void test_removeXmlns() {
//    	assertEquals("abc  dd ",XMLHelper.removeXmlns("abc xmlns=\"http://maven.apache.org/POM/4.0.0\" dd xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""));
    	assertEquals("abc  dd xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"",XMLHelper.removeXmlns("abc xmlns=\"http://maven.apache.org/POM/4.0.0\" dd xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""));
    	assertEquals("abc ",XMLHelper.removeXmlns("abc xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\""));
//    	assertEquals("UTF-8",XMLHelper.removeXmlns("<?xml version=\"1.0\" encoding='UTF-8'?>"));
    }
    
    public void verifyColumnWithNodeAsKey(NodeData c, String expected) {
        Map column = c.nodeNameAsAttributes("sqlName");
        assertEquals(expected,column.toString());
    }
    
    public void verifyColumn(NodeData c, String expected) {
        assertEquals(expected,c.attributes.toString());
    }
    
//    public void test_parse_SqlConfig() throws Exception {
//    	File file = FileHelper.getFileByClassLoader("generator_config/sql/USER_INFO.xml");
//        NodeData nd = new XMLHelper().parseXML(new FileInputStream(file));
//        System.out.println("sql config:\n"+nd);
//    }
//    
//    public void test()  throws Exception{
//    	File file = FileHelper.getFileByClassLoader("fortest_gen_by_sqlmap/UserInfoSqlMap.xml");
//    	NodeData nd = new XMLHelper().parseXML(new FileInputStream(file));
//        System.out.println("sql config:\n"+nd);
//    }
}
