package cn.org.rapid_framework.config.group;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import org.junit.Test;
import org.springframework.util.ResourceUtils;


public class GroupsTest {
	@Test
	public void test() throws InvalidPropertiesFormatException, IOException {
		Properties props = new Properties();
		props.setProperty("1", "1");
		props.setProperty("1222", "222");
		props.storeToXML(System.out, "comment");
		System.out.println("\n\n");
		
		Groups g = verifyXml(new FileInputStream(ResourceUtils.getFile("classpath:cn/org/rapid_framework/config/group/groups_test.xml")));		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		g.storeAsXml(out);
		g.storeAsXml(System.out);
		
		verifyXml(new ByteArrayInputStream(out.toByteArray()));
		
		
	}

	private Groups verifyXml(InputStream input) throws InvalidPropertiesFormatException,
			IOException, FileNotFoundException {
		Groups g = new Groups();
		g.loadFromXml(input);
		
		assertEquals(2, g.size());
		assertNotNull(g.getGroup("g1"));
		assertNotNull(g.getGroup("g2"));
		
		Properties p = g.getGroup("g1");
		assertEquals("v1_1",p.getProperty("g1_1"));
		assertEquals("v1_2",p.getProperty("g1_2"));
		return g;
	}
}
