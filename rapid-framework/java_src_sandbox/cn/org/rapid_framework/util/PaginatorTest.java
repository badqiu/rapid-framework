package cn.org.rapid_framework.util;

import java.util.ArrayList;

import junit.framework.TestCase;

public class PaginatorTest extends TestCase {
	
	Paginator p = new Paginator();
	
	public void setUp() {
		p.setPage(10);
		p.setPageSize(10);
		p.setTotalItems(101);		
	}
	
	public void test_getTotalPages() {
		assertEquals(p.getTotalPages(), 11);
	}
	
	public void test_is() {
		assertEquals(p.isFirstPage(),false);
		assertEquals(p.isLastPage(),false);
		assertEquals(p.isHasNextPage(),true);
		assertEquals(p.isHasPrePage(),true);
		
		p.setPage(1);
		assertEquals(p.isFirstPage(),true);
		assertEquals(p.isLastPage(),false);
		assertEquals(p.isHasPrePage(),false);
		assertEquals(p.isHasNextPage(),true);
		
		p.setPage(11);
		assertEquals(p.isFirstPage(),false);
		assertEquals(p.isLastPage(),true);
		assertEquals(p.isHasPrePage(),true);
		assertEquals(p.isHasNextPage(),false);
		
		p.setPage(5);
		assertEquals(p.isFirstPage(),false);
		assertEquals(p.isLastPage(),false);
		assertEquals(p.isHasPrePage(),true);
		assertEquals(p.isHasNextPage(),true);
	}
	
	public void test_get() {
		assertEquals(p.getStartRow(),91);
		assertEquals(p.getEndRow(),100);
		assertEquals(p.getOffset(),90);
		assertEquals(p.getPage(),10);
		assertEquals(p.getPageSize(),10);
		assertEquals(p.getPrePage(),9);
		assertEquals(p.getNextPage(),11);
		assertEquals(p.getTotalPages(),11);
		assertEquals(p.getTotalItems(),101);

	}
	
	public void test_get_with_pageSize() {
		p.setPageSize(5);
		assertEquals(p.getStartRow(),46);
		assertEquals(p.getEndRow(),50);
		assertEquals(p.getOffset(),45);
		assertEquals(p.getPage(),10);
		assertEquals(p.getPageSize(),5);
		assertEquals(p.getPrePage(),9);
		assertEquals(p.getNextPage(),11);
		assertEquals(p.getTotalPages(),21);
		assertEquals(p.getTotalItems(),101);

		p.setPageSize(200);
		assertEquals(p.getStartRow(),1801);
		assertEquals(p.getEndRow(),101);
		assertEquals(p.getOffset(),1800);
		assertEquals(p.getPage(),10);
		assertEquals(p.getPageSize(),200);
		assertEquals(p.getPrePage(),9);
		assertEquals(p.getNextPage(),10);
		assertEquals(p.getTotalPages(),1);
		assertEquals(p.getTotalItems(),101);
		
		for(int i = 1; i <= p.getTotalPages(); i++) {
			PageList<String> pageList = findPage(i,p.getPageSize(),"query");
		}
	}

	private PageList findPage(int page, int pageSize, String string) {
		return new PageList(new ArrayList(),page,pageSize,101);
	}
	
}
