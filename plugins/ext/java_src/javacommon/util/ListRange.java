package javacommon.util;

import java.util.ArrayList;
import java.util.List;



public class ListRange<T> {
	private boolean success;
	private String message;
	private long totalSize;
	private List<T> list;

	public ListRange() {
		this.totalSize = 0;
		this.list = new ArrayList<T>();
	}

	public long getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(long totalSize) {
		this.totalSize = totalSize;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
