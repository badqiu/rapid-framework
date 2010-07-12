package cn.org.rapid_framework.util.config.group;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class Groups implements java.io.Serializable{
	private static final long serialVersionUID = -1335431769066383676L;
	private Map<String,Properties> groups = new LinkedHashMap<String,Properties>();
	
	public Set<String> getGroupNames() {
		return groups.keySet();
	}
	
	public Properties getGroup(String groupName) {
		return groups.get(groupName);
	}
	
	public void addGroup(String groupName,Properties properties) {
		groups.put(groupName,properties);
	}
	
	public Properties removeGroup(String groupName) {
		return groups.remove(groupName);
	}
	
	public void removeAllGroup(String groupName) {
		groups.clear();
	}

	public boolean containsGroup(String groupName) {
		return groups.containsKey(groupName);
	}

	public boolean isEmpty() {
		return groups.isEmpty();
	}

	public int size() {
		return groups.size();
	}

	public Map<String, Properties> getGroups() {
		return groups;
	}

	public void setGroups(Map<String, Properties> groups) {
		if(groups == null) throw new IllegalArgumentException("'groups' must be not null");
		this.groups = groups;
	}
	
	public void mergeGroups(Groups g) {
		this.groups.putAll(g.getGroups());
	}
	
//	public void loadFromWindowsInI(InputStream in) {
//		
//	}
//	
//	public void saveAsWindowsInI(InputStream in) {
//		
//	}
	
	public void loadFromXML(InputStream in) throws IOException {
		XMLUtils.load(this, in);
	}
	
	public void storeToXML(OutputStream out,String comment,String encoding) throws IOException {
        XMLUtils.save(this, out, comment, encoding);
    }
	
	public void storeToXML(OutputStream out,String comment) throws IOException {
		XMLUtils.save(this, out, comment, "UTF-8");
	}
	
	public void storeToXML(OutputStream out) throws IOException {
	    storeToXML(out, null);
	}

   public Properties toProperties() {
        return toProperties(".");
    }
	   
	public Properties toProperties(String seperator) {
	    Properties properties = new Properties();
	    Set<Map.Entry<String, Properties>> entrySet = getGroups().entrySet();
	    for(Map.Entry<String, Properties> entry : entrySet) {
	        String group = entry.getKey();
	        Properties p = entry.getValue();
	        for(Object key : p.keySet()) {
	            properties.put(group+seperator+key, p.get(key));
	        }
	    }
	    return properties;
	}
	
	public void storeAsWindowsInI(Writer writer) {
	    PrintWriter pw = new PrintWriter(writer);
	    Set<Map.Entry<String, Properties>> entrySet = getGroups().entrySet();
        for(Map.Entry<String, Properties> entry : entrySet) {
            String group = entry.getKey();
            Properties p = entry.getValue();
            pw.println("["+group+"]");
            for(Object key : p.keySet()) {
                pw.println(key+"="+p.getProperty((String)key));
            }
            pw.println("");
        }
        pw.flush();
    }
	
//	public String toString() {
//	}
	//TODO 增加groups config的变量引用,如 group1.username=${group2.password}/diy


}
