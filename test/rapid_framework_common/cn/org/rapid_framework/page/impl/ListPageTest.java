package cn.org.rapid_framework.page.impl;

import java.util.ArrayList;
import java.util.List;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.impl.ListPage;

import junit.framework.TestCase;
/**
 * @author badqiu
 */
public class ListPageTest extends TestCase {

	List elements = new ArrayList();
	public void setUp() {
		for(int i = 1; i <= 20; i++) {
			elements.add(""+i);
		}	
	}
	public void testErrorPageNumber() {
		Page page = new ListPage(new ArrayList(),-1,2);
		page = new ListPage(new ArrayList(),1,2);
		
		page = new ListPage(elements,-1,2);
		List thisPage = (List)page.getResult();
		assertEquals(2,thisPage.size());
		assertEquals("1",thisPage.get(0));
		assertEquals("2",thisPage.get(1));
		assertEquals(10,page.getLastPageNumber());
		
		page = new ListPage(elements,Integer.MAX_VALUE,2);
		thisPage = (List)page.getResult();
		assertEquals(2,thisPage.size());
		assertEquals("19",thisPage.get(0));
		assertEquals("20",thisPage.get(1));
		assertEquals(10,page.getLastPageNumber());
	}
}
