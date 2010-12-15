package cn.org.rapid_framework.generator.util.test;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import cn.org.rapid_framework.generator.GeneratorFacade;
import cn.org.rapid_framework.generator.Generator.GeneratorModel;
import cn.org.rapid_framework.generator.provider.db.sql.model.Sql;
import cn.org.rapid_framework.generator.util.FileHelper;
/**
 * 生成器的测试工具类,用于将模板内容生成在某个临时文件夹,然后读取临时文件夹的内容返回
 * 返回之前会删除临时文件夹
 * 
 * @author badqiu
 *
 */
public class GeneratorTestHelper {
	private static AtomicLong count = new AtomicLong(System.currentTimeMillis());

	public static String generateBy(GeneratorFacade gf,
			GeneratorModel... models) throws Exception {
		File tempDir = getOutputTempDir();
		gf.getGenerator().setOutRootDir(tempDir.getPath());
		gf.generateBy(models);
		return readEntireDirectoryContentAndDelete(tempDir);
	}

	public static String generateByAllTable(GeneratorFacade gf)
			throws Exception {
		File tempDir = getOutputTempDir();
		gf.getGenerator().setOutRootDir(tempDir.getPath());
		gf.generateByAllTable();
		return readEntireDirectoryContentAndDelete(tempDir);
	}

	public static String generateByClass(GeneratorFacade gf, Class... clazzes)
			throws Exception {
		File tempDir = getOutputTempDir();
		gf.getGenerator().setOutRootDir(tempDir.getPath());
		gf.generateByClass(clazzes);
		return readEntireDirectoryContentAndDelete(tempDir);
	}

	public static String generateByMap(GeneratorFacade gf, Map... maps)
			throws Exception {
		File tempDir = getOutputTempDir();
		gf.getGenerator().setOutRootDir(tempDir.getPath());
		gf.generateByMap(maps);
		return readEntireDirectoryContentAndDelete(tempDir);
	}

	public static String generateBySql(GeneratorFacade gf, Sql... sqls)
			throws Exception {
		File tempDir = getOutputTempDir();
		gf.getGenerator().setOutRootDir(tempDir.getPath());
		gf.generateBySql(sqls);
		return readEntireDirectoryContentAndDelete(tempDir);
	}

	public static String generateByTable(GeneratorFacade gf,
			String... tableNames) throws Exception {
		File tempDir = getOutputTempDir();
		gf.getGenerator().setOutRootDir(tempDir.getPath());
		gf.generateByTable(tableNames);
		return readEntireDirectoryContentAndDelete(tempDir);
	}

	private static String readEntireDirectoryContentAndDelete(File tempDir) {
		String r = FileHelper.readEntireDirectoryContent(tempDir);
		try {
			FileHelper.deleteDirectory(tempDir);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return r;
	}

	private static File getOutputTempDir() {
		File tempDir = new File(FileHelper.getTempDir(), "GeneratorTestHelper/"
				+ count.incrementAndGet() + ".tmp");
		return tempDir;
	}

}
