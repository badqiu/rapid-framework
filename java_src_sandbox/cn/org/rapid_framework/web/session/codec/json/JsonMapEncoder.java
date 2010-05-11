package cn.org.rapid_framework.web.session.codec.json;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;

import cn.org.rapid_framework.web.session.codec.MapEncoder;

public class JsonMapEncoder implements MapEncoder{

	private ObjectMapper objectMapper = new ObjectMapper();

	private JsonEncoding encoding = JsonEncoding.UTF8;

	public String encode(Map map) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		JsonGenerator generator = objectMapper.getJsonFactory().createJsonGenerator(out, encoding);
		objectMapper.writeValue(generator, map);
		return out.toString(encoding.getJavaName());
	}
	
}
