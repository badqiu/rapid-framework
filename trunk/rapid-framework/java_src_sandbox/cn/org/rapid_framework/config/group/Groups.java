package cn.org.rapid_framework.config.group;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class Groups implements java.io.Serializable{
	private Map<String,Properties> groups = new LinkedHashMap();
	
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
	
	//TODO 增加groups config的变量引用,如 group1.username=${group2.password}/diy


}
