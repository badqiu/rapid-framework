package cn.org.rapid_framework.generator.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
/**
 * 
 * @author badqiu
 * @email badqiu(a)gmail.com
 */
public class IOHelper {
	public static Writer NULL_WRITER = new NullWriter();
	
	public static void copy(Reader in,Writer out) throws IOException {
		char[] buf = new char[8192];
		while(in.read(buf) != -1) {
			out.write(buf);
		}
	}
	
    public static void copy(InputStream in,OutputStream out) throws IOException {
        byte[] buf = new byte[8192];
        while(in.read(buf) != -1) {
            out.write(buf);
        }
    }
	
    public static List readLines(Reader input) throws IOException {
        BufferedReader reader = new BufferedReader(input);
        List list = new ArrayList();
        String line = reader.readLine();
        while (line != null) {
            list.add(line);
            line = reader.readLine();
        }
        return list;
    }
    
	public static String readFile(File file) throws IOException {
		Reader in = new FileReader(file);
		StringWriter out = new StringWriter();
		copy(in,out);
		return out.toString();
	}
	
	public static String readFile(File file,String encoding) throws IOException {
		Reader in = new InputStreamReader(new FileInputStream(file),encoding);
		StringWriter out = new StringWriter();
		copy(in,out);
		return out.toString();
	}

    public static void saveFile(File file,String content)  {
        saveFile(file,content,false);
    }
	   
	public static void saveFile(File file,String content,boolean append)  {
		try {
		Writer writer = new FileWriter(file,append);
		writer.write(content);
		writer.close();
		}catch(IOException e){
			throw new RuntimeException(e);
		}
	}
	
	private static class NullWriter extends Writer {
		public void close() throws IOException {
		}
		public void flush() throws IOException {
		}
		public void write(char[] cbuf, int off, int len) throws IOException {
		}
	}

    public static void copyAndClose(InputStream in,OutputStream out) throws IOException {
        try {
            copy(in,out);
        }finally {
            close(in,out);
        }
    }

    private static void close(InputStream in, OutputStream out) {
        try { if(in != null) in.close();}catch(Exception e){};
        try { if(out != null) out.close();}catch(Exception e){};
    }
}
