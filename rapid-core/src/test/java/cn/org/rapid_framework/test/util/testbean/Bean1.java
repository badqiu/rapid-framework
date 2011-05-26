package cn.org.rapid_framework.test.util.testbean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import cn.org.rapid_framework.jdbc.sqlgenerator.metadata.CommontBean;

public  class Bean1 {
    private char char1;
    private String s1;
    private int int1;
    private Integer integer1;
    private Long long1;
    private java.sql.Date sqldate1;
    private java.util.Date utildate1;
    private java.sql.Timestamp timestamp1;
    private java.sql.Time time1;
    private SexEnumBean sex;
    private long[] longArray;
    private List<Long> listLong;
    private Map map;
    private ArrayList arrayList;
    private HashMap hashMap;
    private CommontBean testBean;
    private Set set;
    private HashSet hashSet;
    private Collection collection;
    private Stack stack;
    private Calendar calendar;
    public char getChar1() {
        return char1;
    }
    public void setChar1(char char1) {
        this.char1 = char1;
    }
    public String getS1() {
        return s1;
    }
    public void setS1(String s1) {
        this.s1 = s1;
    }
    public int getInt1() {
        return int1;
    }
    public void setInt1(int int1) {
        this.int1 = int1;
    }
    public Integer getInteger1() {
        return integer1;
    }
    public void setInteger1(Integer integer1) {
        this.integer1 = integer1;
    }
    public Long getLong1() {
        return long1;
    }
    public void setLong1(Long long1) {
        this.long1 = long1;
    }
    public java.sql.Date getSqldate1() {
        return sqldate1;
    }
    public void setSqldate1(java.sql.Date sqldate1) {
        this.sqldate1 = sqldate1;
    }
    public java.util.Date getUtildate1() {
        return utildate1;
    }
    public void setUtildate1(java.util.Date utildate1) {
        this.utildate1 = utildate1;
    }
    public java.sql.Timestamp getTimestamp1() {
        return timestamp1;
    }
    public void setTimestamp1(java.sql.Timestamp timestamp1) {
        this.timestamp1 = timestamp1;
    }
    public java.sql.Time getTime1() {
        return time1;
    }
    public void setTime1(java.sql.Time time1) {
        this.time1 = time1;
    }
    public SexEnumBean getSex() {
        return sex;
    }
    public void setSex(SexEnumBean sex) {
        this.sex = sex;
    }
    public long[] getLongArray() {
        return longArray;
    }
    public void setLongArray(long[] longArray) {
        this.longArray = longArray;
    }
    public List<Long> getListLong() {
        return listLong;
    }
    public void setListLong(List<Long> listLong) {
        this.listLong = listLong;
    }
    public Map getMap() {
        return map;
    }
    public void setMap(Map map) {
        this.map = map;
    }
	public ArrayList getArrayList() {
		return arrayList;
	}
	public void setArrayList(ArrayList arrayList) {
		this.arrayList = arrayList;
	}
	public HashMap getHashMap() {
		return hashMap;
	}
	public void setHashMap(HashMap hashMap) {
		this.hashMap = hashMap;
	}
	public CommontBean getTestBean() {
		return testBean;
	}
	public void setTestBean(CommontBean testBean) {
		this.testBean = testBean;
	}
	public Set getSet() {
		return set;
	}
	public void setSet(Set set) {
		this.set = set;
	}
	public HashSet getHashSet() {
		return hashSet;
	}
	public void setHashSet(HashSet hashSet) {
		this.hashSet = hashSet;
	}
	public Collection getCollection() {
		return collection;
	}
	public void setCollection(Collection collection) {
		this.collection = collection;
	}
	public Stack getStack() {
		return stack;
	}
	public void setStack(Stack stack) {
		this.stack = stack;
	}
	public Calendar getCalendar() {
		return calendar;
	}
	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}
}