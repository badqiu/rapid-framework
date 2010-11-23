package cn.org.rapid_framework.util;

import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;
/**
 * 用于打 tag标签
 * @author zhongxuan
 * @version $Id: Tags.java,v 0.1 2010-11-23 下午02:49:40 zhongxuan Exp $
 */
public class Tags implements java.io.Serializable{
    private static final long serialVersionUID = -576293555041436246L;
    
    public Set<String> tags = new LinkedHashSet<String>();
    
    public void add(String tag) {
        if(StringUtils.isNotBlank(tag)) {
            tags.add(tag);
        }
    }
    
    public boolean contains(String o) {
        return tags.contains(o);
    }

    public boolean isEmpty() {
        return tags.isEmpty();
    }

    public boolean remove(String o) {
        return tags.remove(o);
    }
    
    public String toString() {
        return StringUtils.join(tags, ",");
    }
    
    public static void main(String[] args) {
        Tags tags = new Tags();
        tags.add("blog");
        tags.add("blog");
        tags.add("user");
        
        Assert.isTrue(tags.contains("blog"));
        Assert.isTrue(!tags.contains("dddd"));
        System.out.println(tags);
    }
    
}
