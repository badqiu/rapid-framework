package cn.org.rapid_framework.consistenthash;

import java.util.Collection;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
/**
 * 一致性哈希
 * 
 * python实现: http://pypi.python.org/pypi/hash_ring/1.2
 * PHP: http://code.google.com/p/flexihash/
 * 
 * 一致性哈希算法: http://www.cnblogs.com/zsm/archive/2008/12/23/1360297.html
 * CONSISTENT HASHING算法: http://www.yeeach.com/2009/10/02/consistent-hashing%E7%AE%97%E6%B3%95/
 * 某分布式应用实践一致性哈希的一些问题: http://timyang.net/architecture/consistent-hashing-practice/
 * google keyword: DHT, Hash trees or Merkle trees
 * http://en.wikipedia.org/wiki/Hash_tree
 * 
 * TODO 还可以增加权重变量以区别hash
 * @author badqiu
 *
 * @param <T>
 */
public class ConsistentHash<T> {

	private final HashFunction hashFunction; //计算hash的接口,可以拥有不同实现,主要由于默认的Object.hashCode() hash不均匀
	private final int numberOfReplicas; //节点副本数,主要用于创建虚拟节点,以便解决在节点数少时,hash分布不均匀的问题
	final SortedMap<Integer, T> circle = new TreeMap<Integer, T>(); //hash 环
	
	public ConsistentHash(HashFunction hashFunction, int numberOfReplicas,Collection<T> nodes) {
		if(numberOfReplicas <= 0) throw new IllegalArgumentException("numberOfReplicas must be great than zero");
		if(hashFunction == null) throw new IllegalArgumentException("hashFunction must be not null");
		this.hashFunction = hashFunction;
		this.numberOfReplicas = numberOfReplicas;

		for (T node : nodes) {
			add(node);
		}
	}

	public void add(T node) {
		for (int i = 0; i < numberOfReplicas; i++) {
			circle.put(hashFunction.hash(node.toString() + i), node);
		}
	}

	public void remove(T node) {
		for (int i = 0; i < numberOfReplicas; i++) {
			circle.remove(hashFunction.hash(node.toString() + i));
		}
	}

	public T get(Object key) {
		if (circle.isEmpty()) {
			return null;
		}
		int hash = hashFunction.hash(key);
		if (!circle.containsKey(hash)) {
			SortedMap<Integer, T> tailMap = circle.tailMap(hash);
			hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
		}
		return circle.get(hash);
	}

}