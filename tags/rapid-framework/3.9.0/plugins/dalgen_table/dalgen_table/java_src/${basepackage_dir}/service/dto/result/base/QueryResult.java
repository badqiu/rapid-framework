package com.company.project.service.dto.result.base;

import java.util.ArrayList;
import java.util.List;


public class QueryResult<T extends java.io.Serializable> extends WSResult {
	private static final long serialVersionUID = 3667702877871528562L;
	
    private List<T> itemList = new ArrayList<T>(0);
	
	public QueryResult() {
	}
	
    public QueryResult(List<T> itemList) {
        this.itemList = itemList;
    }
    
    public QueryResult(String errorCode, String errorDetails) {
        super(errorCode, errorDetails);
    }

    public List<T> getItemList() {
        return itemList;
    }

    public void setItemList(List<T> itemList) {
        this.itemList = itemList;
    }

}
