package javacommon.base;

import java.util.LinkedHashMap;

/**
 * RequestMap生成接口,由用户自行实现从数据库或其它地方查询URL-授权关系定义.
 * 
 * @author calvin
 */
public interface ResourceDetailService {

	/**
	 * 获取RequestMap.
	 * 
	 * Map中的key为URL, value为能访问该URL的以','分隔的授权列表.
	 */
	LinkedHashMap<String, String> getRequestMap() throws Exception;

}
