package cn.org.rapid_framework.io;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import cn.org.rapid_framework.io.AsyncExceptinHandler.DefaultAsyncExceptinHandler;
/**
 * 
 * 异步的输出流
 * 使用示例:
 * <pre>
 * BufferedOutputStream output = new BufferedOutputStream(new AsyncOutputStream(new FileOutputStream("c:/debug.log")));
 * output.write("foo".getBytes());
 * </pre>
 * @author badqiu
 */
public class AsyncOutputStream extends OutputStream{
	
	private final static byte[] CLOSED_SIGNEL = new byte[0];
	private DataProcessorThread dataProcessor;
	
	private boolean isClosed = false;
	private boolean isStartd = false;
	OutputStream output;
	BlockingQueue queue;
	
	public AsyncOutputStream(OutputStream output) {
		this(output,new ArrayBlockingQueue(50000));
	}
	
	public AsyncOutputStream(OutputStream output, BlockingQueue queue) {
		super();
		if(output == null) throw new NullPointerException();
		if(queue == null) throw new NullPointerException();
		
		this.output = output;
		this.queue = queue;
		this.dataProcessor = new DataProcessorThread();
	}
	
	public void start() {
		this.dataProcessor.start();
		isStartd = true;
	}
	
	private AsyncExceptinHandler asyncExceptinHandler = new DefaultAsyncExceptinHandler();
	
	private static long threadSeqNumber;
	private static synchronized long nextThreadID() {
		return ++threadSeqNumber;
    }
	
	private class DataProcessorThread extends Thread {
	    
		private boolean enabled = true;
		private boolean hasRuned = false;
		DataProcessorThread() {
			super("AsyncOutputStream.DataProcessorThread-"+nextThreadID());
			setDaemon(true);
		}

		public void run() {
			hasRuned = true;
			while (this.enabled || !queue.isEmpty()) {
				
				Object buf;
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
					if(buf instanceof Integer) {
						output.write((Integer)buf);
					}else {
						output.write((byte[])buf);
					}
				} catch (IOException e) {
					 asyncExceptinHandler.handle(e);
				}
			}
		}
	}
	
	@Override
	public void write(int b) throws IOException {
		if(!isStartd) throw new IOException("must start() before wirte()");
		if(isClosed) throw new IOException("output is closed");
		synchronized (this) {
			try {
				queue.put(b);
			} catch (InterruptedException e) {
				throw new IOException("AsyncOutputStream occer InterruptedException error");
			}
		}
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		if(!isStartd) throw new IOException("must start() before wirte()");
		if (b == null)  throw new NullPointerException();
		synchronized (this) {
			if(isClosed) throw new IOException("output is closed");
			try {
				queue.put(BufferCopyUtils.copyBuffer(b,off,len));
			} catch (InterruptedException e) {
				throw new IOException("AsyncOutputStream occer InterruptedException error");
			}
		}
	}

	@Override
	public void flush() throws IOException {
	}

	public void forceFlush() throws IOException {
		synchronized (this) {
			// wait until dataQueue is empty, before calling flush()
			while (queue.size() > 0) {
				try {
					wait(100);
				} catch (InterruptedException e) {
				}
			}
			output.flush();
		}
	}
	
	@Override
	public void close() throws IOException {
		synchronized (this) {
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
				output.close();
			}
		}
	}
	
	public void setAsyncExceptinHandler(AsyncExceptinHandler asyncExceptinHandler) {
		if(asyncExceptinHandler == null) throw new NullPointerException();
		this.asyncExceptinHandler = asyncExceptinHandler;
	}


}
