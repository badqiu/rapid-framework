package cn.org.rapid_framework.page;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import junit.framework.TestCase;

public class SortInfoTest extends TestCase {
	
	public void testParseSortColumns() {
		List<SortInfo> sortInfos = SortInfo.parseSortColumns("username desc");
		assertEquals(1,sortInfos.size());
		SortInfo sortInfo = sortInfos.get(0);
		assertEquals("username",sortInfo.getColumnName());
		assertEquals("desc",sortInfo.getSortOrder());
	}
	
	public void testParseSortColumnsWithNull() {
		List<SortInfo> sortInfos = SortInfo.parseSortColumns(null);
		assertEquals(0,sortInfos.size());
	}
	
	public void testParseSortColumnsWithMultiSort() {
		List<SortInfo> sortInfos = SortInfo.parseSortColumns(" username  desc,age,sex asc ");
		assertEquals(3,sortInfos.size());
		
		SortInfo sort1 = sortInfos.get(0);
		assertEquals("username",sort1.getColumnName());
		assertEquals("desc",sort1.getSortOrder());
		
		SortInfo sort2 = sortInfos.get(1);
		assertEquals("age",sort2.getColumnName());
		assertEquals(null,sort2.getSortOrder());
		
		SortInfo sort3 = sortInfos.get(2);
		assertEquals("sex",sort3.getColumnName());
		assertEquals("asc",sort3.getSortOrder());
	}
	
	public void testJoinSortInfos() {
		List<SortInfo> sortInfos = SortInfo.parseSortColumns(" username  desc,age,sex asc ");
		assertEquals("username desc,age,sex asc",StringUtils.join(sortInfos.iterator(), ","));
	}
}
