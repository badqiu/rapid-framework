package cn.org.rapid_framework.consistenthash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 * 通过md5提供更加均匀分布的hash
 * @author badqiu
 *
 */
public class MD5HashFunction implements HashFunction{
	
	public int hash(Object obj) {
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("NoSuchAlgorithmException:MD5",e);
		}
		md5.update(obj.toString().getBytes());
		return Math.abs(new String(md5.digest()).hashCode());
	}

}
