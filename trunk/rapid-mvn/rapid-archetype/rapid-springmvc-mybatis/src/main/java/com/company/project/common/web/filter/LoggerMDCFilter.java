package com.company.project.common.web.filter;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import com.sun.xml.internal.ws.util.StringUtils;
/**
 * 存放在MDC中的数据，log4j可以直接引用并作为日志信息打印出来.
 * 
 * <pre>
 * 示例使用:
 * log4j.appender.stdout.layout.conversionPattern=%d [%X{loginUserId}/%X{req.remoteAddr}/%X{traceId} - %X{req.requestURI}?%X{req.queryString}] %-5p %c{2} - %m%n
 * </pre>
 * @author badqiu
 */
public class LoggerMDCFilter extends OncePerRequestFilter implements Filter{
    
    protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response, FilterChain chain)throws ServletException,IOException {
        try {
            //示例为一个固定的登陆用户,请直接修改代码
            MDC.put("loginUserId", "demo-loginUsername");
            
            MDC.put("req.requestURI", StringUtils.defaultString(request.getRequestURI()));
            MDC.put("req.queryString", StringUtils.defaultString(request.getQueryString()));
            MDC.put("req.requestURIWithQueryString", request.getRequestURI() + (request.getQueryString() == null ? "" : "?"+request.getQueryString()));
            MDC.put("req.remoteAddr", StringUtils.defaultString(request.getRemoteAddr()));
            
            //为每一个请求创建一个ID，方便查找日志时可以根据ID查找出一个http请求所有相关日志
            MDC.put("traceId", StringUtils.remove(UUID.randomUUID().toString(),"-")); 
            chain.doFilter(request, response);
        }finally {
            clearMDC();
        }
    }

    private static void clearMDC() {
        Map map = MDC.getContext();
        if(map != null) {
            map.clear();
        }
    }
}
