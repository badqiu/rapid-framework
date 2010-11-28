package cn.org.rapid_framework.jar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.util.WeakHashMap;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
/**
 * Manifest读取的工具类
 * 
 * @author badqiu
 */
public class ManifestUtils {
	private static WeakHashMap<URL, Manifest> cache = new WeakHashMap<URL, Manifest>();
	
	/**
	 * 根据Class得到相对应的 META-INF/MANIFEST.MF信息 , 该方法会使用WeakHashMap作为Cache
	 * @param clazz
	 * @return Manifest
	 */
	public static Manifest getManifest(Class<?> clazz) {
		CodeSource codeSource = clazz.getProtectionDomain().getCodeSource();
		
		// check null for java.lang.* class
		if(codeSource == null) { 
			return new Manifest();
		}
		
		URL location = codeSource.getLocation();
		Manifest result = cache.get(location);
		if(result == null) {
			result = getManifest(location);
			cache.put(location, result);
		}
		return result;
	}
	
	//FIXME 需要测试打为war包后,通过jar可不可以得到MANIFEST
	private static Manifest getManifest(URL location) {
		File file = new File(location.getFile());
		if(file.isDirectory()) {
			File manifestFile = new File(file,"META-INF/MANIFEST.MF");
			if(manifestFile.exists()) {
				try {
					return new Manifest(new FileInputStream(manifestFile));
				} catch (FileNotFoundException e) {
					//ignore
					return new Manifest();
				} catch (IOException e) {
					throw new RuntimeException("error on read META-INF/MANIFEST.MF  with location:"+location);
				}
			}
			return new Manifest();
		}else if(file.isFile()) {
			try {
				return new JarFile(file).getManifest();
			} catch (IOException e) {
				throw new RuntimeException("error on read META-INF/MANIFEST.MF with location:"+location);
			}
		}
		throw new RuntimeException("META-INF/MANIFEST.MF FileNotFound with location:"+location);
	}
}