package cn.org.rapid_framework.io;

/**
 * @author badqiu
 */
class BufferCopyUtils {

	static byte[] copyBuffer(byte[] buf, int offset, int length) {
		byte[] offerBuf = new byte[length];
		System.arraycopy(buf, offset, offerBuf, 0, length);
		return offerBuf;
	}
	
	static char[] copyBuffer(char[] buf, int offset, int length) {
		char[] offerBuf = new char[length];
		System.arraycopy(buf, offset, offerBuf, 0, length);
		return offerBuf;
	}
	
}
