package com.company.project.service.dto.result;

import java.util.ArrayList;
import java.util.List;


public class QueryResult<T extends java.io.Serializable> extends WSResult {
	private static final long serialVersionUID = 3667702877871528562L;
	
    private List<T> itemList = new ArrayList(0);
	
	public QueryResult() {
	}
	
    public QueryResult(List<T> itemList) {
        this.itemList = itemList;
    }
    
    public QueryResult(List<T> itemList,boolean isSuccess, String errorCode, String errorDetails) {
        super(isSuccess, errorCode, errorDetails);
        this.itemList = itemList;
    }

    public List<T> getItemList() {
        return itemList;
    }

    public void setItemList(List<T> itemList) {
        this.itemList = itemList;
    }

}
