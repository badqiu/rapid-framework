package cn.org.rapid_framework.web.scope;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.org.rapid_framework.util.Crypto;
import cn.org.rapid_framework.web.util.CookieUtils;
/**
 *
 * @author play-framework
 * @author badqiu
 *
 */
// TODO 完善scope Flash
public class Scope {
	static Log log = LogFactory.getLog(Scope.class);
	public static String COOKIE_PREFIX = "RAPID";
    /**
     * Flash scope
     */
    public static class Flash {

        Map<String, String> data = new HashMap<String, String>();
        Map<String, String> out = new HashMap<String, String>();
        static Pattern flashParser = Pattern.compile("\u0000([^:]*):([^\u0000]*)\u0000");

        public static Flash restore(HttpServletRequest request) {
            try {
                Flash flash = new Flash();
                Map<String,Cookie> cookies = CookieUtils.toMap(request.getCookies());
                Cookie cookie = cookies.get(COOKIE_PREFIX + "_FLASH");
                if (cookie != null) {
                    String flashData = URLDecoder.decode(cookie.getValue(), "utf-8");
                    Matcher matcher = flashParser.matcher(flashData);
                    while (matcher.find()) {
                        flash.data.put(matcher.group(1), matcher.group(2));
                    }
                }
                return flash;
            } catch (Exception e) {
                throw new IllegalStateException("Flash corrupted", e);
            }
        }

        public void save(HttpServletResponse response) {
            try {
                StringBuilder flash = new StringBuilder();
                for (String key : out.keySet()) {
                    flash.append("\u0000");
                    flash.append(key);
                    flash.append(":");
                    flash.append(out.get(key));
                    flash.append("\u0000");
                }
                String flashData = URLEncoder.encode(flash.toString(), "utf-8");
                response.addCookie(new Cookie(COOKIE_PREFIX + "_FLASH",flashData));
            } catch (Exception e) {
                throw new IllegalStateException("Flash serializationProblem", e);
            }
        }

//        public static String encode(Map<String,String> map) throws UnsupportedEncodingException {
//            StringBuilder flash = new StringBuilder();
//            for (String key : map.keySet()) {
//                flash.append("\u0000");
//                flash.append(key);
//                flash.append(":");
//                flash.append(map.get(key));
//                flash.append("\u0000");
//            }
//            String result = URLEncoder.encode(flash.toString(), "utf-8");
//            return result;
//        }
//
//        public static Map decode(String cookieString) throws UnsupportedEncodingException {
//        	Map map = new HashMap();
//            String flashData = URLDecoder.decode(cookieString, "utf-8");
//            Matcher matcher = flashParser.matcher(flashData);
//            while (matcher.find()) {
//                map.put(matcher.group(1), matcher.group(2));
//            }
//            return map;
//        }

        // ThreadLocal access
        static ThreadLocal<Flash> current = new ThreadLocal<Flash>();

        public static Flash current() {
            return current.get();
        }

        public static void setCurrent(Flash f) {
            current.set(f);
        }

        public void put(String key, String value) {
            if (key.contains(":")) {
                throw new IllegalArgumentException("Character ':' is invalid in a flash key.");
            }
            data.put(key, value);
            out.put(key, value);
        }

        public void put(String key, Object value) {
            if (value == null) {
                put(key, (String) null);
            }
            put(key, value + "");
        }

        public void now(String key, String value) {
            if (key.contains(":")) {
                throw new IllegalArgumentException("Character ':' is invalid in a flash key.");
            }
            data.put(key, value);
        }

        public void error(String value, Object... args) {
            put("error", String.format(value, args));
        }

        public void success(String value, Object... args) {
            put("success", String.format(value, args));
        }

        public void discard(String key) {
            out.remove(key);
        }

        public void discard() {
            out.clear();
        }

        public void keep(String key) {
            if (data.containsKey(key)) {
                out.put(key, data.get(key));
            }
        }

        public void keep() {
            out.putAll(data);
        }

        public String get(String key) {
            return data.get(key);
        }

        public boolean remove(String key) {
            return data.remove(key) != null;
        }

        public void clear() {
            data.clear();
        }

        public boolean contains(String key) {
            return data.containsKey(key);
        }

        public String toString() {
            return data.toString();
        }

		public Map<String, String> getData() {
			return data;
		}

    }

    /**
     * Session scope
     */
    public static class Session {

    	public static String SESSION_ID_KEY = "___ID";
        static Pattern sessionParser = Pattern.compile("\u0000([^:]*):([^\u0000]*)\u0000");

        public static Map restore(HttpServletRequest request,String secretKey) {
            try {
                Map session = new HashMap();
                Cookie cookie = CookieUtils.toMap(request.getCookies()).get(COOKIE_PREFIX + "_SESSION");
                if (cookie != null) {
                    String value = cookie.getValue();
                    String sign = value.substring(0, value.indexOf("-"));
                    String data = value.substring(value.indexOf("-") + 1);
                    if (sign.equals(Crypto.sign(data, secretKey.getBytes()))) {
                        String sessionData = URLDecoder.decode(data, "utf-8");
                        Matcher matcher = sessionParser.matcher(sessionData);
                        while (matcher.find()) {
                            session.put(matcher.group(1), matcher.group(2));
                        }
                    } else {
                        log.warn("Corrupted HTTP session from "+request.getRemoteAddr());
                    }
                }
//				if(!session.containsKey(SESSION_ID_KEY)) {
//                    session.put(SESSION_ID_KEY, UUID.randomUUID().toString());
//                }
                return session;
            } catch (Exception e) {
                throw new IllegalStateException("Corrupted HTTP session from " + request.getRemoteAddr(), e);
            }
        }

        Map<String, String> data = new HashMap<String, String>();        // ThreadLocal access
        public static ThreadLocal<Session> current = new ThreadLocal<Session>();

        public Session(Map<String, String> data) {
			super();
			this.data = data;
		}

		public static Session current() {
            return current.get();
        }

		public static void setCurrent(Session session) {
			current.set(session);
		}

        public String getId() {
            return data.get(SESSION_ID_KEY);
        }

        public void save(HttpServletResponse response) {
//            save(response,this.data);
        	throw new UnsupportedOperationException();
        	//TODO fixed me
        }
        
        public static void save(HttpServletResponse response,Map<String,?> sessionMap,String secretKey,Integer maxAge) {
            try {
                String sessionData = encodeSession(sessionMap);
                String sign = Crypto.sign(sessionData, secretKey.getBytes());

                Cookie sessionCookie = new Cookie(COOKIE_PREFIX + "_SESSION", sign + "-" + sessionData);
				if(maxAge != null) {
					sessionCookie.setMaxAge(maxAge);
                }
				sessionCookie.setPath("/");
				sessionCookie.setDomain("");
				response.addCookie(sessionCookie);
            } catch (Exception e) {
                throw new IllegalStateException("Session serializationProblem", e);
            }
        }

		private static String encodeSession(Map<String, ?> sessionMap) throws UnsupportedEncodingException {
			StringBuilder session = new StringBuilder();
			for (String key : sessionMap.keySet()) {
			    session.append("\u0000");
			    session.append(key);
			    session.append(":");
			    session.append(sessionMap.get(key));
			    session.append("\u0000");
			}
			String sessionData = URLEncoder.encode(session.toString(), "utf-8");
			return sessionData;
		}

		public void put(String key, String value) {
            if (key.contains(":")) {
                throw new IllegalArgumentException("Character ':' is invalid in a session key.");
            }
            data.put(key, value);
        }

        public void put(String key, Object value) {
            if (value == null) {
                put(key, (String) null);
            }
            put(key, value + "");
        }

        public String get(String key) {
            return data.get(key);
        }

        public boolean remove(String key) {
            return data.remove(key) != null;
        }

        public void remove(String... keys) {
            for (String key : keys) {
                remove(key);
            }
        }

        public void clear() {
            data.clear();
        }

        public boolean contains(String key) {
            return data.containsKey(key);
        }

        @Override
        public String toString() {
            return data.toString();
        }

    }

    /**
     * Render args (used in template rendering)
     */
    public static class RenderArgs {

        public Map<String, Object> data = new HashMap<String, Object>();        // ThreadLocal access
        static ThreadLocal<RenderArgs> current = new ThreadLocal<RenderArgs>();

        public static RenderArgs current() {
            return current.get();
        }

        public static void setCurrent(RenderArgs r) {
            current.set(r);
        }

        public void put(String key, Object arg) {
            this.data.put(key, arg);
        }

        public Object get(String key) {
            return data.get(key);
        }

        public <T> T get(String key, Class<T> clazz) {
            return (T) this.get(key);
        }
    }
}
