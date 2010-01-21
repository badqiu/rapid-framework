package cn.org.rapid_framework.web.cache;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.transcoders.SerializingTranscoder;

/**
 * Memcached implementation (using http://code.google.com/p/spymemcached/)
 */
public class MemcachedImpl implements ICache {
	Log logger = LogFactory.getLog(ICache.class);
    private static MemcachedImpl uniqueInstance;
    
    private Properties configuration;

    public static MemcachedImpl getInstance() throws IOException {
        if (uniqueInstance == null) {
            uniqueInstance = new MemcachedImpl();
        }
        return uniqueInstance;
    }
    MemcachedClient client;
    SerializingTranscoder tc;

    private MemcachedImpl() throws IOException {
//        tc = new SerializingTranscoder() {
//
//            @Override
//            protected Object deserialize(byte[] data) {
//                try {
//                    return new ObjectInputStream(new ByteArrayInputStream(data)) {
//
//                        @Override
//                        protected Class<?> resolveClass(ObjectStreamClass desc)
//                                throws IOException, ClassNotFoundException {
//                            return Play.classloader.loadClass(desc.getName());
//                        }
//                    }.readObject();
//                } catch (Exception e) {
//                    logger.error("Could not deserialize",e);
//                }
//                return null;
//            }
//
//            @Override
//            protected byte[] serialize(Object object) {
//                try {
//                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
//                    new ObjectOutputStream(bos).writeObject(object);
//                    return bos.toByteArray();
//                } catch (IOException e) {
//                    logger.error("Could not serialize",e);
//                }
//                return null;
//            }
//        };
    	tc = new SerializingTranscoder();

        System.setProperty("net.spy.log.LoggerImpl", "net.spy.memcached.compat.log.Log4JLogger");
        if (configuration.containsKey("memcached.host")) {
            client = new MemcachedClient(AddrUtil.getAddresses(configuration.getProperty("memcached.host")));
        } else if (configuration.containsKey("memcached.1.host")) {
            int nb = 1;
            String addresses = "";
            while (configuration.containsKey("memcached." + nb + ".host")) {
                addresses += configuration.get("memcached." + nb + ".host") + " ";
                nb++;
            }
            client = new MemcachedClient(AddrUtil.getAddresses(addresses));
        } else {
            throw new IllegalStateException(("Bad configuration for memcached"));
        }
    }

    public void add(String key, Object value, int expiration) {
        client.add(key, expiration, value, tc);
    }

    public Object get(String key) {
        Future<Object> future = client.asyncGet(key, tc);
        try {
            return future.get(1, TimeUnit.SECONDS);
        } catch (Exception e) {
            future.cancel(false);
        }
        return null;
    }

    public void clear() {
        client.flush();
    }

    public void delete(String key) {
        client.delete(key);
    }

    public Map<String, Object> get(String[] keys) {
        Future<Map<String, Object>> future = client.asyncGetBulk(tc, keys);
        try {
            return future.get(1, TimeUnit.SECONDS);
        } catch (Exception e) {
            future.cancel(false);
        }
        return new HashMap<String, Object>();
    }

    public long incr(String key, int by) {
        return client.incr(key, by);
    }

    public long decr(String key, int by) {
        return client.decr(key, by);
    }

    public void replace(String key, Object value, int expiration) {
        client.replace(key, expiration, value, tc);
    }

    public boolean safeAdd(String key, Object value, int expiration) {
        Future<Boolean> future = client.add(key, expiration, value, tc);
        try {
            return future.get(1, TimeUnit.SECONDS);
        } catch (Exception e) {
            future.cancel(false);
        }
        return false;
    }

    public boolean safeDelete(String key) {
        Future<Boolean> future = client.delete(key);
        try {
            return future.get(1, TimeUnit.SECONDS);
        } catch (Exception e) {
            future.cancel(false);
        }
        return false;
    }

    public boolean safeReplace(String key, Object value, int expiration) {
        Future<Boolean> future = client.replace(key, expiration, value, tc);
        try {
            return future.get(1, TimeUnit.SECONDS);
        } catch (Exception e) {
            future.cancel(false);
        }
        return false;
    }

    public boolean safeSet(String key, Object value, int expiration) {
        Future<Boolean> future = client.set(key, expiration, value, tc);
        try {
            return future.get(1, TimeUnit.SECONDS);
        } catch (Exception e) {
            future.cancel(false);
        }
        return false;
    }

    public void set(String key, Object value, int expiration) {
        client.set(key, expiration, value, tc);
    }

    public void stop() {
        client.shutdown();
    }
}
