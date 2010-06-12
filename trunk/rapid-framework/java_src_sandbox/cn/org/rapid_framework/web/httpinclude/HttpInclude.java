package cn.org.rapid_framework.web.httpinclude;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.util.WebUtils;
/**
 * 
 * 用于include其它页面以用于布局,可以用于在freemarker,velocity的servlet环境应用中直接include其它http请求
 * 
 * <br />
 * <b>Freemarker及Velocity示例使用:</b>
 * <pre>
 * ${httpInclude.include("http://www.google.com")};
 * ${httpInclude.include("/servlet/head?p1=v1&p2=v2")};
 * ${httpInclude.include("/head.jsp")};
 * ${httpInclude.include("/head.do?p1=v1&p2=v2")};
 * ${httpInclude.include("/head.htm")};
 * </pre>
 * 
 * @author badqiu
 *
 */
public class HttpInclude {
	protected static Log log = LogFactory.getLog(HttpInclude.class);
	
    private HttpServletRequest request;
    private HttpServletResponse response;
    public static String sessionIdKey = "jsessionid";
    
    public HttpInclude(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    public String include(String includePath) {
        try {
            if(isRemoteHttpRequest(includePath)) {
                return getHttpRemoteContent(includePath);
            }else {
            	ByteArrayOutputStream output = new ByteArrayOutputStream(8192);
                getLocalContent(output, includePath);
                return output.toString(response.getCharacterEncoding());
            }
        } catch (ServletException e) {
            throw new RuntimeException("include error,path:"+includePath+" cause:"+e,e);
        } catch (IOException e) {
            throw new RuntimeException("include error,path:"+includePath+" cause:"+e,e);
        }
    }

    private static boolean isRemoteHttpRequest(String includePath) {
        return  includePath != null && (
        			includePath.toLowerCase().startsWith("http://") ||
        			includePath.toLowerCase().startsWith("https://")
        		);
    }

    private void getLocalContent(final OutputStream outputStream,String includePath) throws ServletException, IOException {
        // TODO handle getLocalContent() encoding 
        final PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(outputStream,response.getCharacterEncoding()));
        request.getRequestDispatcher(includePath).include(request, new HttpServletResponseWrapper(response) {
            public PrintWriter getWriter() throws IOException {
                return printWriter;
            }
            public ServletOutputStream getOutputStream() throws IOException {
                return new ServletOutputStream() {
                    @Override
                    public void write(int b) throws IOException {
                        outputStream.write(b);
                    }
                    @Override
                    public void write(byte[] b) throws IOException {
                        outputStream.write(b);
                    }
                    @Override
                    public void write(byte[] b, int off, int len)throws IOException {
                        outputStream.write(b, off, len);
                    }
                };
            }
        });
        printWriter.flush();
    }
    //TODO handle cookies and http query parameters encoding, cookie并且需要处理不可见的 session id cookie问题
    
    private String getHttpRemoteContent(String url) throws MalformedURLException, IOException {
        URLConnection conn = new URL(url).openConnection();
        conn.setReadTimeout(6000);
        conn.setConnectTimeout(6000);
        String cookie = getCookieString();
		conn.setRequestProperty("Cookie", cookie);
		if(log.isDebugEnabled()) {
			log.info("set request cookie:"+cookie+" for url:"+url);
		}
		
        InputStream input = conn.getInputStream();
        ByteArrayOutputStream output = new ByteArrayOutputStream(8192);
        try {
        	IOUtils.copy(input,output);
        }finally {
        	if(input != null) input.close();
        }
        
        if(conn.getContentEncoding() == null) {
        	return output.toString(response.getCharacterEncoding());
        } else {
        	return output.toString(conn.getContentEncoding());
        }
    }

    private static final String SET_COOKIE_SEPARATOR="; ";
	private String getCookieString() {
		StringBuffer sb = new StringBuffer(64);
		for(Cookie c : request.getCookies()) {
			sb.append(c.getName()).append("=").append(c.getValue()).append(SET_COOKIE_SEPARATOR);
		}
		
		String sessionId = WebUtils.getSessionId(request);
		if(sessionId != null) {
			sb.append(sessionIdKey).append("=").append(sessionId).append(SET_COOKIE_SEPARATOR);
		}
		return sb.toString();
	}
    
    static class IOUtils { 
        private static void copy(InputStream in, OutputStream out) throws IOException {
            byte[] buff = new byte[8192];
            while(in.read(buff) >= 0) {
                out.write(buff);
            }
        }
        
        private static void copy(Reader in, Writer out) throws IOException {
            char[] buff = new char[8192];
            while(in.read(buff) >= 0) {
                out.write(buff);
            }
        }
    }
}
