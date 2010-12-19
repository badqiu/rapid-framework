
package cn.org.rapid_framework.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import cn.org.rapid_framework.web.util.FilterConfigUtils;
/**
 * 性能filter。
 * 
 * <p>
 * web.xml配置文件格式如下：
 * <pre><![CDATA[
 <filter>
 <filter-name>PerformanceFilter</filter-name>
 <filter-class>cn.org.rapid_framework.web.filter.PerformanceFilter</filter-class>
 <init-param>
 <param-name>threshold</param-name>
 <param-value>3000</param-value>
 </init-param>
 </filter>
 ]]></pre>
 * </p>
 * 
 * <p>
 * 其中<code>threshold</code>参数表明超时阈值，如果处理的总时间超过该值，则filter会以warning的方式记录该次操作。
 * </p>
 *
 * @author badqiu
 *
 */
public class PerformanceFilter  extends OncePerRequestFilter implements Filter {
    int threshold = 3000;
    boolean includeQueryString = false;
    private static final Log log = LogFactory.getLog(PerformanceFilter.class);

    public void destroy() {
    }

	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		
        String requestString = dumpRequest(request);

        Throwable failed = null;
        long start = System.currentTimeMillis();
        try {
            chain.doFilter(request, response);
        } catch (Throwable e) {
            failed = e;
            rethrowThrowable(failed);
        } finally {
            long duration = System.currentTimeMillis() - start;
            if (failed != null) {
                log.error("["+requestString+",F,"+duration+"ms");
            } else if (duration > threshold) {
                log.warn("["+requestString+",Y,"+duration+"ms");
            } else if (log.isInfoEnabled()) {
                log.info("["+requestString+",Y,"+duration+"ms");
            }
        }

    }

    private static void rethrowThrowable(Throwable failed) throws Error, IOException, ServletException {
        if (failed != null) {
            if (failed instanceof Error) {
                throw (Error) failed;
            } else if (failed instanceof RuntimeException) {
                throw (RuntimeException) failed;
            } else if (failed instanceof IOException) {
                throw (IOException) failed;
            } else if (failed instanceof ServletException) {
                throw (ServletException) failed;
            }
        }
    }

    @Override
    public void initFilterBean() throws ServletException {
        this.threshold = FilterConfigUtils.getIntParameter(getFilterConfig(), "threshold", threshold);
        this.includeQueryString = FilterConfigUtils.getBooleanParameter(getFilterConfig(), "includeQueryString", includeQueryString);
        log.info("PerformanceFilter started with threshold:"+threshold+"ms includeQueryString:"+includeQueryString);
    }
    
    /**
     * 取得request的内容(HTTP方法, URI)
     *
     * @param request HTTP请求
     *
     * @return 字符串
     */
    protected String dumpRequest(ServletRequest request) {
        HttpServletRequest req = (HttpServletRequest)request;
        StringBuffer buffer = new StringBuffer(req.getMethod());

        buffer.append("__").append(req.getRequestURI());

        if(includeQueryString) {
            String queryString = req.getQueryString();
            if (StringUtils.isNotBlank(queryString)) {
                buffer.append("?").append(queryString);
            }
        }
        
        return buffer.toString();
    }



}
