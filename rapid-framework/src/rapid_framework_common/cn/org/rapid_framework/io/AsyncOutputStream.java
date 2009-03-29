package cn.org.rapid_framework.io;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import cn.org.rapid_framework.io.AsyncExceptinHandler.DefaultAsyncExceptinHandler;

public class AsyncOutputStream extends OutputStream{
	
	private final static byte[] CLOSED_SIGNEL = new byte[0];
	private DataProcessorThread dataProcessor;
	
	private boolean isClosed = false;
	OutputStream output;
	BlockingQueue queue;
	
	public AsyncOutputStream(OutputStream output) {
		this(output,new ArrayBlockingQueue(50000));
	}
	
	public AsyncOutputStream(OutputStream output, BlockingQueue queue) {
		super();
		this.output = output;
		this.queue = queue;
		this.dataProcessor = new DataProcessorThread();
		this.dataProcessor.start();
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
		if(isClosed) throw new IOException("output is closed");
		synchronized (this) {
			try {
				queue.put(b);
			} catch (InterruptedException e) {
				throw new IOException(e);
			}
		}
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		if(isClosed) throw new IOException("output is closed");
		if (b == null)  throw new NullPointerException();
		synchronized (this) {
			try {
				queue.put(ArrayUtils.copyBuffer(b,off,len));
			} catch (InterruptedException e) {
				throw new IOException(e);
			}
		}
	}
	
	@Override
	public void flush() throws IOException {
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
