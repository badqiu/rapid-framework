import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;



public class OgnlTest {

	
	@Test
	public void isBlank() {
		Assert.assertTrue(Ognl.isBlank(null));
		Assert.assertTrue(Ognl.isBlank(""));
		
		Assert.assertTrue(Ognl.isBlank(" "));
		Assert.assertTrue(Ognl.isBlank(" \n \t  "));
		Assert.assertFalse(Ognl.isBlank(" a "));
		
		Assert.assertFalse(Ognl.isBlank(new Object()));
		Assert.assertFalse(Ognl.isBlank(new Integer(1)));
		Assert.assertFalse(Ognl.isBlank("a"));
	}
	@Test
	public void isNotBlank() {
		Assert.assertFalse(Ognl.isNotBlank(null));
		Assert.assertFalse(Ognl.isNotBlank(""));
		
		Assert.assertFalse(Ognl.isNotBlank(" "));
		Assert.assertTrue(Ognl.isNotBlank(new Object()));
		Assert.assertTrue(Ognl.isNotBlank("a"));
	}
	
	@Test
	public void isEmpty() {
		Assert.assertTrue(Ognl.isEmpty(null));
		Assert.assertTrue(Ognl.isEmpty(""));
		
		Assert.assertFalse(Ognl.isEmpty(" "));
		Assert.assertFalse(Ognl.isEmpty(new Object()));
		Assert.assertFalse(Ognl.isEmpty("a"));
	}
	
	@Test
	public void isNotEmpty() {
		Assert.assertFalse(Ognl.isNotEmpty(null));
		Assert.assertFalse(Ognl.isNotEmpty(""));
		
		Assert.assertTrue(Ognl.isNotEmpty(" "));
		Assert.assertTrue(Ognl.isNotEmpty(new Object()));
		Assert.assertTrue(Ognl.isNotEmpty("a"));
	}
	
	@Test
	public void isNumber() {
		Assert.assertFalse(Ognl.isNumber(""));
		Assert.assertFalse(Ognl.isNumber(null));
		Assert.assertFalse(Ognl.isNumber(" "));
		
		Assert.assertTrue(Ognl.isNumber("1.1"));
		Assert.assertTrue(Ognl.isNumber("1"));
		Assert.assertTrue(Ognl.isNumber("-1"));
		Assert.assertTrue(Ognl.isNumber(1));
		Assert.assertTrue(Ognl.isNumber(1.1));
		Assert.assertTrue(Ognl.isNumber(new Integer(1)));
	}
	
	@Test
	public void testPrintSystemProperties() throws FileNotFoundException {
		PrintStream out = new PrintStream(System.out);
		for(Map.Entry entry : System.getProperties().entrySet()) {
			out.println(entry.getKey()+":"+entry.getValue());
		}
	}
}
