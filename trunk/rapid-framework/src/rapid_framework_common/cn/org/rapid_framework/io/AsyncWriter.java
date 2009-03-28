package cn.org.rapid_framework.io;

import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *　异步的writer
 * @author badqiu
 *
 */
public class AsyncWriter extends Writer {

	private static Log log = LogFactory.getLog(AsyncWriter.class);
	
	private static final int DEFAULT_QUEUE_CAPACITY = 50000;
	private final static char[] CLOSED_SIGNEL = new char[0];
	
	private Writer out;
	private DataProcessorThread dataProcessor;
	private boolean isClosed = false;
	private BlockingQueue<char[]> queue ;
	private AsyncExceptinHandler asyncExceptinHandler = new DefaultAsyncExceptinHandler();
		
	public static interface AsyncExceptinHandler {
		public void handle(Throwable e);
	}
	
	class DefaultAsyncExceptinHandler implements AsyncExceptinHandler {
		public void handle(Throwable e) {
			log.error("Exception during write(): " + e,e);
		}
	};
	
	private class DataProcessorThread extends Thread {

		private boolean enabled = true;
		private boolean hasRuned = false;
		DataProcessorThread() {
			setDaemon(true);
		}

		public void run() {
			hasRuned = true;
			while (this.enabled || !queue.isEmpty()) {
				
				char[] buf;
				try {
					buf = queue.take();
				} catch (InterruptedException e) {
//					e.printStackTrace();
					continue;
				}
				
				if(buf == CLOSED_SIGNEL) {
					return;
				}
				
				try {
					out.write(buf);
				} catch (IOException e) {
					 asyncExceptinHandler.handle(e);
				}
			}
		}
	}

	public AsyncWriter(Writer out) {
		this(out,DEFAULT_QUEUE_CAPACITY);
	}
	
	public AsyncWriter(Writer out,int queueCapacity) {
		this.queue = new ArrayBlockingQueue(queueCapacity);
		this.dataProcessor = new DataProcessorThread();
		this.dataProcessor.start();
		this.out = out;
	}
	
	public AsyncWriter(Writer out,AsyncExceptinHandler handler) {
		this(out);
		setAsyncExceptinHandler(handler);
	}

	public void write(char[] buf, int offset, int length) throws IOException {
		if(isClosed) throw new IOException("already closed");
		synchronized (lock) {
			try {
				queue.put(copyBuffer(buf, offset, length));
			} catch (InterruptedException e) {
				throw new IOException(e);
			}
		}
	}

	private static char[] copyBuffer(char[] buf, int offset, int length) {
		char[] offerBuf = new char[length];
		System.arraycopy(buf, offset, offerBuf, 0, length);
		return offerBuf;
	}

	public void close() throws IOException {
		synchronized (lock) {
			try {
				isClosed = true;
				dataProcessor.enabled = false;
				if(queue.isEmpty()) {
					queue.offer(CLOSED_SIGNEL);
				}
				
				try {
					dataProcessor.join();
				} catch (InterruptedException e) {
					//ignore
				}
				
				if(!dataProcessor.hasRuned) {
					dataProcessor.run();
				}
			}finally {
				out.close();
			}
		}
	}
	
	public void flush() throws IOException {
	}

	protected void finalize() throws Throwable {
		super.finalize();
		if(!isClosed) {
			log.warn("AsyncWriter not close:"+this);
			close();
		}
	}

	public void setAsyncExceptinHandler(AsyncExceptinHandler asyncExceptinHandler) {
		if(asyncExceptinHandler == null) throw new NullPointerException();
		this.asyncExceptinHandler = asyncExceptinHandler;
	}
}
