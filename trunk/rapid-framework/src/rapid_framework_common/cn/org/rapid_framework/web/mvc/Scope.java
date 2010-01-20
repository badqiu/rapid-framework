package cn.org.rapid_framework.web.mvc;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import cn.org.rapid_framework.web.util.CookieUtils;
/**
 * 
 * @author play-framework
 * @author badqiu
 *
 */
// TODO 完善scope Flash
public class Scope {

	public static String COOKIE_PREFIX = "RAPID";
    /**
     * Flash scope
     */
    public static class Flash {

        Map<String, String> data = new HashMap<String, String>();
        Map<String, String> out = new HashMap<String, String>();
        static Pattern flashParser = Pattern.compile("\u0000([^:]*):([^\u0000]*)\u0000");

        static Flash restore() {
        	return restore(null);
        }

        static Flash restore(HttpServletRequest request) {
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

        void save() {
            save(null);
        }

        void save(HttpServletResponse response) {
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

        // ThreadLocal access
        static ThreadLocal<Flash> current = new ThreadLocal<Flash>();

        public static Flash current() {
            return current.get();
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
    }

}
