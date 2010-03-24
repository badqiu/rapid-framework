package cn.org.rapid_framework.generator.provider.db;


import java.lang.reflect.InvocationTargetException;
import java.util.Map;


import cn.org.rapid_framework.generator.IGeneratorModelProvider;
import cn.org.rapid_framework.generator.provider.db.model.Table;
import cn.org.rapid_framework.generator.util.BeanHelper;
/**
 * 
 * @author badqiu
 *
 */
public class DbTableGeneratorModelProvider implements IGeneratorModelProvider {
	Table table;
	
	public DbTableGeneratorModelProvider(Table table) {
		super();
		this.table = table;
	}

	public String getDisaplyText() {
		return table.toString();
	}

	public void mergeFilePathModel(Map model) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		model.putAll(BeanHelper.describe(table));
	}

	public void mergeTemplateModel(Map model) {
		model.put("table",table);
	}

}
