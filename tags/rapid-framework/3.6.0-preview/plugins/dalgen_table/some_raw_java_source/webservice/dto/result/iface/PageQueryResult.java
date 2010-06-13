package com.company.project.facade.dto.result.iface;

public interface PageQueryResult extends WSResult{
	
	public int getPageSize();
	
	public int getPageNo();
	
	public int getTotalCount();
	
	public void setPageSize(int paeeSize);
	
	public void setPageNo(int pageNo);
	
	public void setTotalCount(int totalCont);
}
