import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import flex.messaging.io.ArrayList;



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
	

}
