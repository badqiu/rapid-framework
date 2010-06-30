package cn.org.rapid_framework.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.beans.BeanUtils;

public class MiscTest extends TestCase {
	
	public void test_removeString() {
		assertEquals("abc123456",StringUtils.remove("abc-123-456", "-"));
//		assertEquals("abc123456",StringUtils.remove(UUID.randomUUID().toString(), "-"));
		
		assertEquals("abc.file","classpath:abc.file".substring("classpath:".length()));
		
		Map map = new HashMap();
		map.put("array", new String[]{"123","abc"});
		map.put("k1", "v1");
		map.put("k2", "v2");
		map.put("k3", "v4");
		System.out.println(map);
		System.out.println(toString(map));
	}

    public static String toString(Map<?,?> map) {
        StringBuffer sb = new StringBuffer();
        int count = 0;
        for(Map.Entry<?,?> entry : map.entrySet()) {
            Object value = entry.getValue();
            if(value != null && value.getClass().isArray()) {
                sb.append(entry.getKey()+"="+Arrays.toString((Object[])value));
            }else {
                sb.append(entry.getKey()+"="+value);
            }
            if(count++ != map.size() - 1) {
                sb.append(',');
            }
        }
        return sb.toString();
    }
	
}
