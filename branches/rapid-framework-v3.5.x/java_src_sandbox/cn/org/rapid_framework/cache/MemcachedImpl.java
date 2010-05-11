package cn.org.rapid_framework.cache;

import java.beans.BeanInfo;
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
import org.springframework.beans.factory.InitializingBean;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.transcoders.SerializingTranscoder;

/**
 * Memcached implementation (using http://code.google.com/p/spymemcached/)
 */
public class MemcachedImpl implements ICache,InitializingBean {
	Log logger = LogFactory.getLog(ICache.class);
    private MemcachedClient client;
    private SerializingTranscoder serializingTranscoder = new SerializingTranscoder();
    private String hosts = null;
//    private static MemcachedImpl uniqueInstance;
//    public static MemcachedImpl getInstance() throws IOException {
//        if (uniqueInstance == null) {
//            uniqueInstance = new MemcachedImpl();
//        }
//        return uniqueInstance;
//    }

	public SerializingTranscoder getSerializingTranscoder() {
		return serializingTranscoder;
	}

	public void setSerializingTranscoder(SerializingTranscoder serializingTranscoder) {
		this.serializingTranscoder = serializingTranscoder;
	}

	public String getHosts() {
		return hosts;
	}

	public void setHosts(String hosts) {
		this.hosts = hosts;
	}
	
	public MemcachedClient getMemcachedClient() {
		return client;
	}

	public void setMemcachedClient(MemcachedClient client) {
		this.client = client;
	}

	public void afterPropertiesSet() throws Exception {
		//        serializingTranscoder = new SerializingTranscoder() {
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

        System.setProperty("net.spy.log.LoggerImpl", "net.spy.memcached.compat.log.Log4JLogger");
        if(client == null)
        	client = new MemcachedClient(AddrUtil.getAddresses(hosts));
	}

    public void add(String key, Object value, int expiration) {
        client.add(key, expiration, value, serializingTranscoder);
    }

    public Object get(String key) {
        Future<Object> future = client.asyncGet(key, serializingTranscoder);
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
        Future<Map<String, Object>> future = client.asyncGetBulk(serializingTranscoder, keys);
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
        client.replace(key, expiration, value, serializingTranscoder);
    }

    public boolean safeAdd(String key, Object value, int expiration) {
        Future<Boolean> future = client.add(key, expiration, value, serializingTranscoder);
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
        Future<Boolean> future = client.replace(key, expiration, value, serializingTranscoder);
        try {
            return future.get(1, TimeUnit.SECONDS);
        } catch (Exception e) {
            future.cancel(false);
        }
        return false;
    }

    public boolean safeSet(String key, Object value, int expiration) {
        Future<Boolean> future = client.set(key, expiration, value, serializingTranscoder);
        try {
            return future.get(1, TimeUnit.SECONDS);
        } catch (Exception e) {
            future.cancel(false);
        }
        return false;
    }

    public void set(String key, Object value, int expiration) {
        client.set(key, expiration, value, serializingTranscoder);
    }

    public void stop() {
        client.shutdown();
    }

}
