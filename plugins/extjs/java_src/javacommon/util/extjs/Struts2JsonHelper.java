package javacommon.util.extjs;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;

import org.apache.struts2.ServletActionContext;

public class Struts2JsonHelper {
	
	public static HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}
	
	public static HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}
	
	public static void outJsonString(String str) {
		getResponse().setContentType("text/javascript;charset=UTF-8");
		outString(str);
	}

 
	  /**   
     * JSON 时间解析器具   
     *    
     * @param datePattern   
     * @return   
     */   
    public static JsonConfig configJson(String datePattern) {    
        JsonConfig jsonConfig = new JsonConfig();    
        jsonConfig.setExcludes(new String[] { "" });    
        jsonConfig.setIgnoreDefaultExcludes(false);    
        jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);    
        jsonConfig.registerJsonValueProcessor(Date.class,    
                new JsonDateValueProcessor(datePattern));    
   
        return jsonConfig;    
    }    

	public static void outJson(Object obj) {
        JsonConfig jsonConfig = configJson("yyyy-MM-dd HH:mm:ss");    
		outJsonString(JSONObject.fromObject(obj,jsonConfig).toString());
	}

	public static void outJsonArray(Object array) {
		outJsonString(JSONArray.fromObject(array).toString());
	}
	
	public static void outString(String str) {
		try {
			PrintWriter out = getResponse().getWriter();
			out.write(str);
		} catch (IOException e) {
		}
	}

	public static void outXMLString(String xmlStr) {
		getResponse().setContentType("application/xml;charset=UTF-8");
		outString(xmlStr);
	}
	
}
