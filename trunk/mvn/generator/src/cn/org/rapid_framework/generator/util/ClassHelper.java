package cn.org.rapid_framework.generator.util;

public class ClassHelper {

	public static Object newInstance(Class<?> c) {
		try {
			return c.newInstance();
		} catch (Exception e) {
			throw new IllegalArgumentException(
					"cannot new instance with class:" + c.getName(), e);
		}
	}

	public static Object newInstance(String className) {
		try {
			return newInstance(Class.forName(className));
		} catch (Exception e) {
			throw new IllegalArgumentException(
					"cannot new instance with className:" + className, e);
		}
	}
}
