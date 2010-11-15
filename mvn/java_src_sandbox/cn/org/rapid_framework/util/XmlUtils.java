package cn.org.rapid_framework.util;  
  
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.VisitorSupport;

import cn.org.rapid_framework.generator.util.IOHelper;
  
public class XmlUtils {  
      
    public static Map<String, Object> dom2Map(final Document doc){  
        Map<String, Object> map = new HashMap<String, Object>();  
        if(doc == null)  
            return map;  
        final Map table = new HashMap();
        final List columns = new ArrayList();
        Element root = doc.getRootElement();  
        
        
        root.accept(new VisitorSupport() {
        	public void visit(Element node) {
        		if(node.getName().equals("column")) {
        			String sqlName = node.attributeValue("name");
        			String path = "//table/column/"+sqlName+"/*";
        			System.out.println(path+" name="+sqlName);
					
        		}
        		super.visit(node);
        	}
        	public void visit(Attribute node) {
        		table.put(node.getName(), node.getData());
        	}
        });
        
        List list = doc.selectNodes("xxx");
		System.out.println(list);
		
        System.out.println(table);
        System.out.println(columns);
        return table;
    }  
    
    public static void main(String[] str) throws DocumentException, IOException {
    	Document doc = DocumentHelper.createDocument();  
	    String file = "D:/webapp-generator-output/some_demo/generator_table_config/com.company.project/user_info_config.xml";
    	doc = DocumentHelper.parseText(IOHelper.readFile(new File(file)));
    	
    	System.out.println(dom2Map(doc));
    }
  
}  