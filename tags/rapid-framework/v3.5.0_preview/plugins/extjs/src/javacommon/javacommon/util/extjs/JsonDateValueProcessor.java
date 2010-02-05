package javacommon.util.extjs;    
   
import java.text.SimpleDateFormat;    
import java.util.Date;    
   
import net.sf.json.JsonConfig;    
import net.sf.json.processors.JsonValueProcessor;    
   
/**   
 *    
 * @author yongtree   
 * @date 2008-11-22 上午10:54:19   
 * @version 1.0   
 */   
public class JsonDateValueProcessor implements JsonValueProcessor {       
          
    private String format = "yyyy-MM-dd HH:mm:ss";       
      
    public JsonDateValueProcessor() {       
      
    }       
      
    public JsonDateValueProcessor(String format) {       
        this.format = format;       
    }       
      
    public Object processArrayValue(Object value, JsonConfig jsonConfig) {
		String[] obj = {};
		if (value == null)
			return obj;
		if (value instanceof java.util.Date[]) {
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date[] dates = (java.util.Date[]) value;
			obj = new String[dates.length];
			for (int i = 0; i < dates.length; i++) {
				obj[i] = sf.format(dates[i]);
			}
		} else if (value instanceof java.sql.Date[]) {
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			java.sql.Date[] dates = (java.sql.Date[]) value;
			obj = new String[dates.length];
			for (int i = 0; i < dates.length; i++) {
				obj[i] = sf.format(dates[i]);
			}
		}
		return obj;
	}

	public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {
		if (value == null)
			return "";
		//注意：在判断几个父子级类型时要先判断子类型再判断父类型
		if (value instanceof java.sql.Date) {
			String str = new SimpleDateFormat("yyyy-MM-dd").format((java.sql.Date) value);
			return str;
		} else if (value instanceof java.sql.Timestamp || value instanceof java.util.Date) {
			String str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((java.util.Date) value);
			return str;
		}
		return value.toString();
	}   
      
    public String getFormat() {       
        return format;       
    }       
      
    public void setFormat(String format) {       
        this.format = format;       
    }       
      
}    


