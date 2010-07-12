package cn.org.rapid_framework.util;
/**
 * 用于性能测试,得到性能测试的时间
 * @author badqiu
 *
 */
public class ProfileUtils {
	
	private ProfileUtils(){}
	
	public static long getCostTime(Runnable task) {
		long start = System.currentTimeMillis();
		task.run();
		long cost = System.currentTimeMillis() - start;
		return cost;
	}
	
	public static long printCostTime(String prefix,Runnable task) {
		long cost = getCostTime(task);
		System.out.println(prefix+",cost seconds:"+cost/1000.0+",cost mills:"+cost);
		return cost;
	}
}
