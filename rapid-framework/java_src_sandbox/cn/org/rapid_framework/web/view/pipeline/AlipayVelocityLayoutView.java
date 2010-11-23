/*
 * Alipay.com Inc.
 * Copyright (c) 2004-2009 All Rights Reserved.
 */
package cn.org.rapid_framework.web.view.pipeline;

import java.io.StringWriter;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import org.apache.velocity.runtime.RuntimeConstants;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.velocity.VelocityLayoutView;
import org.springframework.web.servlet.view.velocity.VelocityToolboxView;

/**
 * 该类重写了<code>spring-mvc</code>中自带的{@link VelocityLayoutView}类， 我们添加了
 * <code>sofa-mvc</code>特有的car属性。
 * 
 * <p>
 * 我们重置了{@link VelocityLayoutView}类原有的一些默认配置，以能够适应原有webx中vm模板的默认配置。
 * <ul>
 * <li>layoutUrl : <code>"default.vm"</code>;
 * <li>screenContentKey : </code>screen_placeholder</code>;
 * 
 * <p>
 * 添加是否需要layout渲染变量，默认为true。这里给那些不需要layout渲染的view提供支持，例如：Tile，Taglib
 * 
 * <p>
 * 注意这里创建的视图名称，都是可以定位到视图模板的，名称不许要再做装换，所有的视图名称转换请放在{@link ViewResolver}类完成。
 * 
 * @author xi.hux@alipay.com 为了使包更清晰,更内聚性，duwei 于 20091125修改 修改内容：
 *         1、删除AlipayToolboxManager类,因为该类已经不使用。
 *         2、通过byName形式获取TemplateTool、TileTool、AlipayToolBox
 * 
 * @version $Id: AlipayVelocityLayoutView.java,v 0.1 2009-1-11 下午06:41:54 xi.hux
 *          Exp $
 */

public class AlipayVelocityLayoutView extends VelocityToolboxView  {

	/**
	 * The default {@link #setLayoutUrl(String) layout url}.
	 */
	public static final String DEFAULT_LAYOUT_URL = "default.vm";

	/**
	 * The default {@link #setLayoutKey(String) layout key}.
	 */
	public static final String DEFAULT_LAYOUT_KEY = "layout";

	/**
	 * The default {@link #setScreenContentKey(String) screen content key}.
	 */
	public static final String DEFAULT_SCREEN_CONTENT_KEY = "screen_placeholder";

	private String layoutUrl = DEFAULT_LAYOUT_URL;

	private String layoutKey = DEFAULT_LAYOUT_KEY;

	/** 当前web request返回的view的vm url */
	public static final String URL_UNDER_RENDERING_KEY = "rendering_url";

	public static final String SCREEN_CONTEXT_KEY = "screen_context_key";

	private String screenContentKey = DEFAULT_SCREEN_CONTENT_KEY;




	@Override
	protected void exposeToolAttributes(Context velocityContext, HttpServletRequest request)
	                                                                                        throws Exception {
	    
	    super.exposeToolAttributes(velocityContext, request);
	    
	    velocityContext.put("now", new Date());
	}

	/**
	 * Set the layout template to use. Default is {@link #DEFAULT_LAYOUT_URL
	 * "layout.vm"}.
	 * 
	 * @param layoutUrl
	 *            the template location (relative to the template root
	 *            directory)
	 */
	public void setLayoutUrl(String layoutUrl) {
		this.layoutUrl = layoutUrl;
	}

	/**
	 * Set the context key used to specify an alternate layout to be used
	 * instead of the default layout. Screen content templates can override the
	 * layout template that they wish to be wrapped with by setting this value
	 * in the template, for example:<br>
	 * <code>#set( $layout = "MyLayout.vm" )</code>
	 * <p>
	 * Default key is {@link #DEFAULT_LAYOUT_KEY "layout"}, as illustrated
	 * above.
	 * 
	 * @param layoutKey
	 *            the name of the key you wish to use in your screen content
	 *            templates to override the layout template
	 */
	public void setLayoutKey(String layoutKey) {
		this.layoutKey = layoutKey;
	}

	/**
	 * Set the name of the context key that will hold the content of the screen
	 * within the layout template. This key must be present in the layout
	 * template for the current screen to be rendered.
	 * <p>
	 * Default is {@link #DEFAULT_SCREEN_CONTENT_KEY "screen_content"}: accessed
	 * in VTL as <code>$screen_content</code>.
	 * 
	 * @param screenContentKey
	 *            the name of the screen content key to use
	 */
	public void setScreenContentKey(String screenContentKey) {
		this.screenContentKey = screenContentKey;
	}

	@Override
	protected void exposeModelAsRequestAttributes(Map<String, Object> model,
	                                              HttpServletRequest request) throws Exception {
	    super.exposeModelAsRequestAttributes(model, request);
	}



	/**
	 * Overrides the normal rendering process in order to pre-process the
	 * Context, merging it with the screen template into a single value
	 * (identified by the value of screenContentKey). The layout template is
	 * then merged with the modified Context in the super class.
	 */
	@Override
	protected void doRender(Context context, HttpServletResponse response)
			throws Exception {

		renderScreenContent(context);

		// Velocity context now includes any mappings that were defined
		// (via #set) in screen content template.
		// The screen template can overrule the layout by doing
		// #set( $layout = "MyLayout.vm" )
		String layoutUrlToUse = (String) context.get(this.layoutKey);
		if (layoutUrlToUse != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Screen content template has requested layout ["
						+ layoutUrlToUse + "]");
			}
		} else {
			// No explicit layout URL given -> use default layout of this view.
			layoutUrlToUse = this.layoutUrl;
		}

		mergeTemplate(getTemplate(layoutUrlToUse), context, response);

	}

	/**
	 * The resulting context contains any mappings from render, plus screen
	 * content.
	 */
	private void renderScreenContent(Context velocityContext) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("Rendering screen content template [" + getUrl()+ "]");
		}

		StringWriter sw = new StringWriter();
		Template screenContentTemplate = getTemplate(getUrl());
		screenContentTemplate.merge(velocityContext, sw);

		// Put rendered content into Velocity context.
		velocityContext.put(this.screenContentKey, sw.toString());
	}

	@Override
	protected void applyContentType(HttpServletResponse response) {
		super.applyContentType(response);
		// 设置输出编码
		response.setCharacterEncoding((String) getVelocityEngine().getProperty(
				RuntimeConstants.OUTPUT_ENCODING));
	}

	public Object getVelocityConfigurer() {
		return getApplicationContext().getBean("velocityConfigurer");
	}
}
