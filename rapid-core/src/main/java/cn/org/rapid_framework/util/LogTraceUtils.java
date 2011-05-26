package cn.org.rapid_framework.util;

import java.util.UUID;

import org.apache.log4j.MDC;

/**
 * 系统运行时打印方便调试与追踪信息的工具类.
 * 
 * 使用MDC存储traceID, 一次trace中所有日志都自动带有该ID,
 * 可以方便的用grep命令在日志文件中提取该trace的所有日志.
 * 
 * 需要在log4j.properties中将ConversionPattern添加%X{traceId},如:
 * log4j.appender.stdout.layout.ConversionPattern=%d [%c] %X{traceId}-%m%n
 * 
 * @author calvin
 * @authro badqiu
 */
public class LogTraceUtils {

	public static final String TRACE_ID_KEY = "traceId";

	/**
	 * 开始Trace, 如果已经存在该traceId则返回,不存在则生成UUID并放入MDC.
	 * @return traceId
	 */
	public static String beginTrace() {
		String traceId = (String)MDC.get(TRACE_ID_KEY);
		if(traceId == null) {
			traceId = UUID.randomUUID().toString().replace("-","");
			MDC.put(TRACE_ID_KEY, traceId);
		}
		return traceId;
	}

	/**
	 * 结束一次Trace.
	 * 清除traceId.
	 */
	public static void endTrace() {
		MDC.remove(TRACE_ID_KEY);
	}
}