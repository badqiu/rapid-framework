package cn.org.rapid_framework.io;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;



/**
 * 为Reader添回附加数据,在Reader读取数据结束后,再添加附加数据至Reader的末尾;
 * @author badqiu
 * 
 */
public class AppendDataReader extends Reader {
	Reader appendDataReader = null;
	Reader delegate;

	public AppendDataReader(Reader delegate, String appendData) {
		this(delegate,new StringReader(appendData));
	}

	public AppendDataReader(Reader delegate, Reader appendData) {
		if(delegate == null) throw new IllegalArgumentException("'delegate' must be not null");
		if(appendData == null) throw new IllegalArgumentException("'appendData' must be not null");
		
		this.delegate = delegate;
		this.appendDataReader = appendData;
	}

	@Override
	public void close() throws IOException {
		try {
			delegate.close();
		} finally {
			appendDataReader.close();
		}
	}

	private boolean isExceed = false;

	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		synchronized (lock) {
			if (isExceed) {
				try {
					return appendDataReader.read(cbuf, off, len);
				}catch(IOException e) {
					throw new IOException("read data from appendDataReader occer error,cause:"+e);
				}
			}
			int result = delegate.read(cbuf, off, len);
			if (result == -1) {
				isExceed = true;
				return read(cbuf, off, len);
			} else {
				return result;
			}
		}
	}

}
