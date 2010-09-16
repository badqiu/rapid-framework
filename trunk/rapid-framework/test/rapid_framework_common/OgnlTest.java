import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.springframework.dao.DataAccessException;




public class OgnlTest extends TestCase {

	HashMap NOT_EMPTY_MAP = new HashMap();
	public void setUp() {
		NOT_EMPTY_MAP.put("1", 1);
	}
	
	public void test_isBlank() {
		assertTrue(Ognl.isBlank(null));
		assertTrue(Ognl.isBlank(""));
		
		assertTrue(Ognl.isBlank(" "));
		assertTrue(Ognl.isBlank(" \n \t  "));
		assertFalse(Ognl.isBlank(" a "));
		
		assertFalse(Ognl.isBlank(new Object()));
		assertFalse(Ognl.isBlank(new Integer(1)));
		assertFalse(Ognl.isBlank("a"));
	}
	
	public void test_isNotBlank() {
		assertFalse(Ognl.isNotBlank(null));
		assertFalse(Ognl.isNotBlank(""));
		
		assertFalse(Ognl.isNotBlank(" "));
		assertTrue(Ognl.isNotBlank(new Object()));
		assertTrue(Ognl.isNotBlank("a"));
	}
	
	
	public void test_isEmpty() {
		assertTrue(Ognl.isEmpty(null));
		assertTrue(Ognl.isEmpty(""));
		
		assertFalse(Ognl.isEmpty(" "));
		assertFalse(Ognl.isEmpty("a"));
	}

	public void test_isEmptyWithObjectException() {
		assertFalse(Ognl.isEmpty(new Object()));
		assertFalse(Ognl.isEmpty(new Integer(1)));
	}

	public void test_isNotEmptyWithObjectException() {
		assertTrue(Ognl.isNotEmpty(new Object()));
		assertTrue(Ognl.isNotEmpty(new Integer(1)));
	}
	
	public void testIsEmpty() {
		assertTrue(Ognl.isEmpty(null));
		assertTrue(Ognl.isEmpty(""));
		assertTrue(Ognl.isEmpty(new ArrayList()));
		assertTrue(Ognl.isEmpty(new HashMap()));
		assertTrue(Ognl.isEmpty(new Object[]{}));
		
		assertTrue(!Ognl.isEmpty(" "));
		assertTrue(!Ognl.isEmpty(Arrays.asList(1,2,3)));
		assertTrue(!Ognl.isEmpty(NOT_EMPTY_MAP));
		assertTrue(!Ognl.isEmpty(new Object[]{1,2,3}));
	}
	
	public void testIsEmptyWithIeelgalArgument() {
		assertFalse(Ognl.isEmpty(new Object()));
	}
	
	
	public void test_isNotEmpty() {
		assertFalse(Ognl.isNotEmpty(null));
		assertFalse(Ognl.isNotEmpty(""));
		
		assertTrue(Ognl.isNotEmpty(" "));
		assertTrue(Ognl.isNotEmpty("a"));
	}
	
	
	public void test_isNumber() {
		assertFalse(Ognl.isNumber(""));
		assertFalse(Ognl.isNumber(null));
		assertFalse(Ognl.isNumber(" "));
		
		assertTrue(Ognl.isNumber("1.1"));
		assertTrue(Ognl.isNumber("1"));
		assertTrue(Ognl.isNumber("-1"));
		assertTrue(Ognl.isNumber("234E12"));
		assertTrue(Ognl.isNumber(Double.MAX_VALUE));
		assertTrue(Ognl.isNumber(Double.MIN_VALUE));
		assertTrue(Ognl.isNumber(Double.POSITIVE_INFINITY));
		assertTrue(Ognl.isNumber(Double.NEGATIVE_INFINITY));
		assertTrue(Ognl.isNumber(1));
		assertTrue(Ognl.isNumber((double)1));
		assertTrue(Ognl.isNumber(1.1));
		assertTrue(Ognl.isNumber(new Integer(1)));
		System.out.println(Double.POSITIVE_INFINITY);
	}
	
	
	public void testPrintSystemProperties() throws FileNotFoundException {
		PrintStream out = new PrintStream(System.out);
		for(Map.Entry entry : System.getProperties().entrySet()) {
			out.println(entry.getKey()+":"+entry.getValue());
		}
	}
	
	public void test_checkOrderby() {
		Ognl.checkOrderBy(null, "");
		Ognl.checkOrderBy(null, null);
		Ognl.checkOrderBy("", null);
		
		Ognl.checkOrderBy(" username ", "username");
		Ognl.checkOrderBy("username", "username");
		Ognl.checkOrderBy("username asc", "username");
		Ognl.checkOrderBy("username  desc", "username");
		Ognl.checkOrderBy("username asc,password desc", "username,password");
		Ognl.checkOrderBy("username asc,password desc", "username,password");
		
		
		try {
			Ognl.checkOrderBy(" username ", "password");
			fail();
		}catch(DataAccessException expected) {
		}
		
		try {
			Ognl.checkOrderBy("username asc", "password");
			fail();
		}catch(DataAccessException expected) {
		}
		
		try {
			Ognl.checkOrderBy("username asc,password desc,blog", "password");
			fail();
		}catch(DataAccessException expected) {
		}
		
		try {
			Ognl.checkOrderBy("username asc,password   desc", "password");
			fail();
		}catch(DataAccessException expected) {
		}
		
		try {
			Ognl.checkOrderBy("username' asc,password   desc", "password");
			fail();
		}catch(IllegalArgumentException expected) {
		}
		
		try {
			Ognl.checkOrderBy("username\\ asc,password   desc", "password");
			fail();
		}catch(IllegalArgumentException expected) {
		}
	}
	
	public void checkOrderby() {
		assertFalse(Ognl.checkOrderBy(null, ""));
		assertFalse(Ognl.checkOrderBy(null, null));
		assertFalse(Ognl.checkOrderBy("", null));
		assertFalse(Ognl.checkOrderBy(" ", null));
		
		assertFalse(Ognl.checkOrderBy(" username ", "password"));
		assertFalse(Ognl.checkOrderBy("username asc", "password"));
		assertFalse(Ognl.checkOrderBy("username asc,password desc,blog", "password"));
		assertFalse(Ognl.checkOrderBy("username asc,password   desc", "password"));
		
		assertTrue(Ognl.checkOrderBy(" username ", "username"));
		assertTrue(Ognl.checkOrderBy("username", "username"));
		assertTrue(Ognl.checkOrderBy("username asc", "username"));
		assertTrue(Ognl.checkOrderBy("username  desc", "username"));
		assertTrue(Ognl.checkOrderBy("username asc,password desc", "username,password"));
		assertTrue(Ognl.checkOrderBy("username asc,password desc", "username,password"));
	}

}
