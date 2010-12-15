package cn.org.rapid_framework.util;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.beanutils.converters.BigIntegerConverter;
import org.apache.commons.beanutils.converters.BooleanConverter;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.DateTimeConverter;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.beanutils.converters.FloatConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.beanutils.converters.ShortConverter;
import org.apache.commons.beanutils.converters.SqlDateConverter;
import org.apache.commons.beanutils.converters.SqlTimeConverter;
import org.apache.commons.beanutils.converters.SqlTimestampConverter;
import org.apache.commons.lang.StringUtils;

import cn.org.rapid_framework.beanutils.converter.StringConverter;

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
	
	public void test_convert_utils() {
		ConvertUtilsBean convert = new ConvertUtilsBean();
		registerConverters(convert, new String[]{"yyyy-MM-dd","yyyy-MM-dd HH:mm:ss","yyyy-MM-dd HH:mm:ss.SSS"});
		System.out.println(convert.convert("2010-01-01", java.util.Date.class));
		System.out.println(convert.convert("2010-01-01 10:10:10", java.util.Date.class));
		System.out.println(convert.convert("2010-01-01 10:10:10.102", java.sql.Timestamp.class));
	}
	
	public static void registerConverters(ConvertUtilsBean convertUtils,String[] datePatterns) {
		convertUtils.register(new StringConverter(), String.class);
		//date 
		convertUtils.register(setPatterns(new DateConverter(null),datePatterns),java.util.Date.class);
		convertUtils.register(setPatterns(new SqlDateConverter(null),datePatterns),java.sql.Date.class);
		convertUtils.register(setPatterns(new SqlTimeConverter(null),datePatterns),Time.class);
		convertUtils.register(setPatterns(new SqlTimestampConverter(null),datePatterns),Timestamp.class);
		//number
		convertUtils.register(new BooleanConverter(null), Boolean.class);
		convertUtils.register(new ShortConverter(null), Short.class);
		convertUtils.register(new IntegerConverter(null), Integer.class);
		convertUtils.register(new LongConverter(null), Long.class);
		convertUtils.register(new FloatConverter(null), Float.class);
		convertUtils.register(new DoubleConverter(null), Double.class);
		convertUtils.register(new BigDecimalConverter(null), BigDecimal.class); 
		convertUtils.register(new BigIntegerConverter(null), BigInteger.class);	
	}
	
	public static <T extends DateTimeConverter> T setPatterns(T converter ,String... patterns) {
		converter.setPatterns(patterns);
		return converter;
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
	
    public void test() {
    	Reference<String> ref = new PhantomReference<String>("", null);
    }
}
