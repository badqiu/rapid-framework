package cn.org.rapid_framework.web.session.codec.json;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.map.ObjectMapper;

import cn.org.rapid_framework.web.session.codec.MapDecoder;

public class JsonMapDecoder implements MapDecoder{
	private ObjectMapper objectMapper = new ObjectMapper();

	private JsonEncoding encoding = JsonEncoding.UTF8;
	
	public String decode(String str) {
		return null;
	}

}
