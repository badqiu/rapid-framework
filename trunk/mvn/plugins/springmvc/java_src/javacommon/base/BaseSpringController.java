package javacommon.base;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

import javacommon.util.ConvertRegisterHelper;
import javacommon.util.PageRequestFactory;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import cn.org.rapid_framework.beanutils.BeanUtils;
import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.sun.jmx.snmp.Timestamp;

public class BaseSpringController extends MultiActionController{
	protected final static String CREATED_SUCCESS = "创建成功";
	protected final static String UPDATE_SUCCESS = "更新成功";
	protected final static String DELETE_SUCCESS = "删除成功";
	
	static {
		//注册converters
		ConvertRegisterHelper.registerConverters();
	}
	
	public static void copyProperties(Object target,Object source) {
		BeanUtils.copyProperties(target, source);
	}

	public static <T> T copyProperties(Class<T> destClass,Object orig) {
		return BeanUtils.copyProperties(destClass, orig);
	}
	
    /**
     * 初始化binder的回调函数.
     *
     * @see MultiActionController#createBinder(HttpServletRequest,Object)
     */
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
    	binder.registerCustomEditor(Short.class, new CustomNumberEditor(Short.class, true));
        binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, true));
        binder.registerCustomEditor(Long.class, new CustomNumberEditor(Long.class, true));
        binder.registerCustomEditor(Float.class, new CustomNumberEditor(Float.class, true));
        binder.registerCustomEditor(Double.class, new CustomNumberEditor(Double.class, true));
        binder.registerCustomEditor(BigDecimal.class, new CustomNumberEditor(BigDecimal.class, true));
        binder.registerCustomEditor(BigInteger.class, new CustomNumberEditor(BigInteger.class, true));
        
        binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
        
    }
	
	public static ModelMap toModelMap(Page page,PageRequest pageRequest) {
		return toModelMap("",page, pageRequest);
	}
	
	public static ModelMap toModelMap(String tableId,Page page,PageRequest pageRequest) {
		ModelMap model = new ModelMap();
		saveIntoModelMap(tableId,page,pageRequest,model);
		return model;
	}
	/**
	 * 用于一个页面有多个extremeTable是使用
	 * @param tableId 等于extremeTable的tableId属性
	 */
	public static void saveIntoModelMap(String tableId,Page page,PageRequest pageRequest,ModelMap model){
		Assert.notNull(tableId,"tableId must be not null");
		Assert.notNull(page,"page must be not null");
		
		model.addAttribute(tableId+"page", page);
		model.addAttribute(tableId+"totalRows", new Integer(page.getTotalCount()));
		model.addAttribute(tableId+"pageRequest", pageRequest);
		model.addAttribute(tableId+"query", pageRequest);
	}
	
	public static PageRequest bindPageRequest(HttpServletRequest request,PageRequest pageRequest,String defaultSortColumns){
		return PageRequestFactory.bindPageRequest(pageRequest,request, defaultSortColumns);
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
