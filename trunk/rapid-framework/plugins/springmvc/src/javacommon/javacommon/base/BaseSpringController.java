package javacommon.base;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javacommon.util.ConvertRegisterHelper;
import javacommon.util.PageRequestFactory;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.beanutils.converters.BigIntegerConverter;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.beanutils.converters.FloatConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.beanutils.converters.ShortConverter;
import org.apache.commons.beanutils.converters.SqlDateConverter;
import org.apache.commons.beanutils.converters.SqlTimeConverter;
import org.apache.commons.beanutils.converters.SqlTimestampConverter;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import cn.org.rapid_framework.beanutils.BeanUtils;
import cn.org.rapid_framework.beanutils.converter.StringConverter;
import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

public class BaseSpringController extends MultiActionController{
	
	static {
		//注册converters
		ConvertRegisterHelper.registerConverters();
	}
	
	public void copyProperties(Object target,Object source) {
		BeanUtils.copyProperties(target, source);
	}

	public <T> T copyProperties(Class<T> destClass,Object orig) {
		return BeanUtils.copyProperties(destClass, orig);
	}
	
    /**
     * 初始化binder的回调函数.
     * 默认以DateUtil中的日期格式设置DateEditor及允许Integer,Double的字符串为空.
     *
     * @see MultiActionController#createBinder(HttpServletRequest,Object)
     */
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
        binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, true));
        binder.registerCustomEditor(Long.class, new CustomNumberEditor(Long.class, true));
        binder.registerCustomEditor(Float.class, new CustomNumberEditor(Float.class, true));
        binder.registerCustomEditor(Double.class, new CustomNumberEditor(Double.class, true));
        binder.registerCustomEditor(BigDecimal.class, new CustomNumberEditor(BigDecimal.class, true));
        binder.registerCustomEditor(BigInteger.class, new CustomNumberEditor(BigInteger.class, true));
    }
	
	public void savePage(Page page,HttpServletRequest request){
		savePage("",page,request);
	}
	/**
	 * 用于一个页面有多个extremeTable是使用
	 * @param tableId 等于extremeTable的tableId属性
	 */
	public void savePage(String tableId,Page page,HttpServletRequest request){
		Assert.notNull(tableId);
		request.setAttribute(tableId+"page", page);
		request.setAttribute(tableId+"totalRows", new Integer(page.getTotalCount()));
	}
	
	public PageRequest newPageRequest(HttpServletRequest request,String defaultSortColumns){
		return PageRequestFactory.newPageRequest(request, defaultSortColumns);
    }
	
	/**
	 * 保存消息在request中,messages.jsp会取出来显示此消息
	 */
    protected static void saveMessage(HttpServletRequest request, String message) {
        if (StringUtils.isNotBlank(message)) {
        	List list = getOrCreateRequestAttribute(request, "springMessages",ArrayList.class);
            list.add(message);
        }
    }
    /**
	 * 保存错误消息在request中,messages.jsp会取出来显示此消息
	 */
    protected static void saveError(HttpServletRequest request, String errorMsg) {
        if (StringUtils.isNotBlank(errorMsg)) {
        	List list = getOrCreateRequestAttribute(request, "springErrors",ArrayList.class);
            list.add(errorMsg);
        }
    }
    
	public static <T> T getOrCreateRequestAttribute(HttpServletRequest request, String key,Class<T> clazz) {
		Object value = request.getAttribute(key);
		if(value == null) {
			try {
				value = clazz.newInstance();
			} catch (Exception e) {
				ReflectionUtils.handleReflectionException(e);
			}
			request.setAttribute(key, value);
		}
		return (T)value;
	}
	
}
