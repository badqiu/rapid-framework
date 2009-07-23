package cn.org.rapid_framework.util;

import java.util.Map;

public class FilePartition extends Partition{
	String baseDir;
	
	public FilePartition(String baseDir,char seperator, String... keys) {
		super(seperator, keys);
		this.baseDir = baseDir;
	}

	public FilePartition(String baseDir,String... keys) {
		super(keys);
		this.baseDir = baseDir;
	}

	@Override
	public String getPartitionString(Map row) {
		return baseDir+getSeperator()+super.getPartitionString(row);
	}

	@Override
	public Map parseRartition(String partitionString) {
		String child = partitionString.substring(baseDir.length());
		return super.parseRartition(child);
	}
	
	
	
}
