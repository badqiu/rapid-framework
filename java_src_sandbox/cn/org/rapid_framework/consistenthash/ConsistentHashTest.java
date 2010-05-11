package cn.org.rapid_framework.consistenthash;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;


public class ConsistentHashTest {
	ConsistentHash hash;
	@Before
	public void setUp() {
		List nodes = new ArrayList();
		nodes.add("server1");
		nodes.add("server2");
		nodes.add("server3");
		nodes.add("server4");
		
		hash = new ConsistentHash(new MD5HashFunction(),1,nodes);
	}
	
	@Test public void testRemove() {
		for(int i = 0; i < 10; i++) {
			get(hash,""+i);
		}
		
		remove(hash, "server1");
		add(hash,"server10");
		
		for(int i = 0; i < 10; i++) {
			get(hash,""+i);
		}
		
	}

	private static void add(ConsistentHash hash, String node) {
		hash.add(node);
		System.out.println("added node:"+node+" circle:"+hash.circle);
	}
	
	private static void remove(ConsistentHash hash, String node) {
		hash.remove("server1");
		System.out.println("removed node:"+node+" circle:"+hash.circle);
	}

	private static void get(ConsistentHash hash, String key) {
		Object value = hash.get(key);
		System.out.println(key+"="+value+" circle:"+hash.circle);
	}
	
}
