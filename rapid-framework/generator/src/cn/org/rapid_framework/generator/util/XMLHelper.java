package cn.org.rapid_framework.generator.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * 将xml
 * @author badqiu
 */
public class XMLHelper {

    static Document getLoadingDoc(InputStream in) throws SAXException,IOException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setIgnoringElementContentWhitespace(true);
        dbf.setValidating(false);
        dbf.setCoalescing(true);
        dbf.setIgnoringComments(true);
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(in);
            return db.parse(is);
        } catch (ParserConfigurationException x) {
            throw new Error(x);
        }
    }
    
    private NodeData treeWalk(Element elm) {
        NodeData nodeData = new NodeData();
        Map map = attrbiuteToMap(elm.getAttributes());
        List childs = new ArrayList();
        nodeData.attributes = map;
        nodeData.nodeName = elm.getNodeName();
        nodeData.childs = childs;
        NodeList list = elm.getChildNodes();
        for(int i = 0; i < list.getLength() ; i++) {
            Node node = list.item(i);
            if(node.getNodeType() == Node.ELEMENT_NODE) {
                childs.add(treeWalk((Element)node));
            }else { // do some thing
            }
        }
        return nodeData;
    }
    
    private static Map attrbiuteToMap(NamedNodeMap attributes) {
        if(attributes == null) return new LinkedHashMap();
        Map result = new LinkedHashMap();
        for(int i = 0; i < attributes.getLength(); i++) {
            result.put(attributes.item(i).getNodeName(), attributes.item(i).getNodeValue());
        }
        return result;
    }
    
    public NodeData parseXML(InputStream in) throws SAXException, IOException {
        Document doc = getLoadingDoc(in);
        return new XMLHelper().treeWalk(doc.getDocumentElement());
    }
    
    public static class NodeData {
        public String nodeName;
        public Map attributes = new HashMap();
        public List<NodeData> childs = new ArrayList();
        
        public String toString() {
            return "nodeName="+nodeName+",attributes="+attributes+" child:\n"+childs;
        }
        public Map getElementMap(String nodeNameKey) {
            Map map = new HashMap();
            map.putAll(attributes);
            map.put(nodeNameKey, nodeName);
            return map;
        }
    }
    
    public static void main(String[] args) throws FileNotFoundException, SAXException, IOException {
        String file = "D:/dev/workspaces/alipay/ali-generator/generator/src/table_test.xml";
        NodeData nd = new XMLHelper().parseXML(new FileInputStream(new File(file)));
        
        Map table = nd.attributes;
        List columns = nd.childs;
        System.out.println(table);
        System.out.println(columns);
//        System.out.println(nd);
    }

}
