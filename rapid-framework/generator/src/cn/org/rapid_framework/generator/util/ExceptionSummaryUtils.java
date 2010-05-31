package cn.org.rapid_framework.generator.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.List;

public class ExceptionSummaryUtils {

	public static void printExceptionsSumary(File errorFile,String summaryInfo,List<Exception> exceptions) {
		if(exceptions != null && exceptions.size() > 0) {
			System.err.println("[Generate Error Summary]");
			ByteArrayOutputStream errorLog = new ByteArrayOutputStream();
			for(Exception e : exceptions) {
				System.err.println("[GENERATE ERROR]:"+e);
				e.printStackTrace(new PrintStream(errorLog));
			}
			IOHelper.saveFile(errorFile, errorLog.toString());
			System.err.println("***************************************************************");
			System.err.println("* "+summaryInfo);
			System.err.println("***************************************************************");
		}
	}
	
}
