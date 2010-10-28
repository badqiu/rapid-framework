package cn.org.rapid_framework.generator.util;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;

import junit.framework.TestCase;

public class BeanHelperTest extends TestCase {

    public void testCopyProperties() {
        BeanHelper.copyProperties(new Bean1(), new Bean2());
        BeanHelper.copyProperties(new Bean2(), new Bean1());
        BeanHelper.copyProperties(new Bean2(), new HashMap());
        
        try {
        BeanHelper.copyProperties(new HashMap(), new Bean1());
        fail();
        }catch(UnsupportedOperationException e) {
        }
    }

    public static class Bean1 {
        String  key1 = "99";
        Boolean key2 = false;
        Integer key3 = 99;
        Long    key4 = 99L;
        Short   key5 = (short) 99;
        String  key6 = "99";
        
        String raw1 = "false";
        String raw2 = "99";
        String raw3 = "99";
        String raw4 = "99";
        Date d1 = new Date();
        Timestamp d2 = new Timestamp(System.currentTimeMillis());
        Time d3 = new Time(System.currentTimeMillis());
        java.sql.Date d4 = new java.sql.Date(System.currentTimeMillis());
        Date d5 = null;
        Date d6 = null;

        public String getKey1() {
            return key1;
        }
        public void setKey1(String key1) {
            this.key1 = key1;
        }
        public Boolean getKey2() {
            return key2;
        }
        public void setKey2(Boolean key2) {
            this.key2 = key2;
        }
        public Integer getKey3() {
            return key3;
        }
        public void setKey3(Integer key3) {
            this.key3 = key3;
        }
        public Long getKey4() {
            return key4;
        }
        public void setKey4(Long key4) {
            this.key4 = key4;
        }
        public Short getKey5() {
            return key5;
        }
        public void setKey5(Short key5) {
            this.key5 = key5;
        }
        public String getKey6() {
            return key6;
        }
        public void setKey6(String key6) {
            this.key6 = key6;
        }
        public String getRaw1() {
            return raw1;
        }
        public void setRaw1(String raw1) {
            this.raw1 = raw1;
        }
        public String getRaw2() {
            return raw2;
        }
        public void setRaw2(String raw2) {
            this.raw2 = raw2;
        }
        public String getRaw3() {
            return raw3;
        }
        public void setRaw3(String raw3) {
            this.raw3 = raw3;
        }
        public String getRaw4() {
            return raw4;
        }
        public void setRaw4(String raw4) {
            this.raw4 = raw4;
        }
        public Date getD1() {
            return d1;
        }
        public void setD1(Date d1) {
            this.d1 = d1;
        }
        public Timestamp getD2() {
            return d2;
        }
        public void setD2(Timestamp d2) {
            this.d2 = d2;
        }
        public Time getD3() {
            return d3;
        }
        public void setD3(Time d3) {
            this.d3 = d3;
        }
        public java.sql.Date getD4() {
            return d4;
        }
        public void setD4(java.sql.Date d4) {
            this.d4 = d4;
        }
        public Date getD5() {
            return d5;
        }
        public void setD5(Date d5) {
            this.d5 = d5;
        }
        public Date getD6() {
            return d6;
        }
        public void setD6(Date d6) {
            this.d6 = d6;
        }

    }

    public static class Bean2 {
        String key1 = "1";
        String key2 = "true";
        String key3 = "1";
        String key4 = "1";
        String key5 = null;
        String key6 = "1";
        
        boolean raw1 = true;
        int raw2 = 1;
        long raw3 = 2;
        short raw4 = 3;
        String d1 = "2010-01-01";
        String d2 = "10:10:10";
        String d3 = "20100101";
        String d4 = "2010-10-01 10:10:10";
        String d5 = "2010-01-01 10:10:10.983";
        String d6 = "Tue Jun 01 17:25:05 CST 2010";
        
        public String getKey1() {
            return key1;
        }
        public void setKey1(String key1) {
            this.key1 = key1;
        }
        public String getKey2() {
            return key2;
        }
        public void setKey2(String key2) {
            this.key2 = key2;
        }
        public String getKey3() {
            return key3;
        }
        public void setKey3(String key3) {
            this.key3 = key3;
        }
        public String getKey4() {
            return key4;
        }
        public void setKey4(String key4) {
            this.key4 = key4;
        }
        public String getKey5() {
            return key5;
        }
        public void setKey5(String key5) {
            this.key5 = key5;
        }
        public String getKey6() {
            return key6;
        }
        public void setKey6(String key6) {
            this.key6 = key6;
        }
        public boolean isRaw1() {
            return raw1;
        }
        public void setRaw1(boolean raw1) {
            this.raw1 = raw1;
        }
        public int getRaw2() {
            return raw2;
        }
        public void setRaw2(int raw2) {
            this.raw2 = raw2;
        }
        public long getRaw3() {
            return raw3;
        }
        public void setRaw3(long raw3) {
            this.raw3 = raw3;
        }
        public short getRaw4() {
            return raw4;
        }
        public void setRaw4(short raw4) {
            this.raw4 = raw4;
        }
        public String getD1() {
            return d1;
        }
        public void setD1(String d1) {
            this.d1 = d1;
        }
        public String getD2() {
            return d2;
        }
        public void setD2(String d2) {
            this.d2 = d2;
        }
        public String getD3() {
            return d3;
        }
        public void setD3(String d3) {
            this.d3 = d3;
        }
        public String getD4() {
            return d4;
        }
        public void setD4(String d4) {
            this.d4 = d4;
        }
        public String getD5() {
            return d5;
        }
        public void setD5(String d5) {
            this.d5 = d5;
        }
        public String getD6() {
            return d6;
        }
        public void setD6(String d6) {
            this.d6 = d6;
        }
        
    }

}
