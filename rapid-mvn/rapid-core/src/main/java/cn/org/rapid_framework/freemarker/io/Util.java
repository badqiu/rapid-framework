package cn.org.rapid_framework.freemarker.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import freemarker.template.Configuration;

class Util {

	public static StringReader cache(Reader input) throws IOException {
		StringWriter cachedOutput = new StringWriter();
		IOUtils.copy(input, cachedOutput);
		return new StringReader(cachedOutput.toString());
	}

	public static ByteArrayInputStream cache(InputStream input) throws IOException {
		ByteArrayOutputStream cachedOutput = input.available() >= 0 ? new ByteArrayOutputStream(input.available()) : new ByteArrayOutputStream();
		IOUtils.copy(input, cachedOutput);
		ByteArrayInputStream result = new ByteArrayInputStream(cachedOutput.toByteArray());
		return result;
	}
	
	public static Map relaceAllKeyChar(Map props,char oldChar,char newChar) {
		Map map = new LinkedHashMap();
		for(Object keyObj : props.keySet()) {
			String key = keyObj.toString();
			map.put(key.replace(oldChar, newChar), props.get(key));
		}
		return map;
	}
	
	public static Configuration newDefaultConfirutaion() {
		Configuration conf = new Configuration();
		conf.setNumberFormat("###############");
		conf.setBooleanFormat("true,false");
		
		return conf;
	}
	
}
