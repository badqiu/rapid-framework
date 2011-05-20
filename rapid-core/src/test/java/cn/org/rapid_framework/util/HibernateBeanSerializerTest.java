package cn.org.rapid_framework.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import junit.framework.TestCase;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.IOUtils;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.ResourceUtils;

import cn.org.rapid_framework.beanutils.BeanUtils;
import cn.org.rapid_framework.mock.MockOpenSessionInViewFilter;
import cn.org.rapid_framework.util.fortest.Resource;
import cn.org.rapid_framework.util.fortest.ResourceDao;
import cn.org.rapid_framework.util.fortest.Role;
import cn.org.rapid_framework.util.fortest.RoleDao;
/**
 * @author badqiu
 */
public class HibernateBeanSerializerTest extends TestCase {
	ApplicationContext context = null;
	ResourceDao resourceDao = null;
	RoleDao roleDao = null;
	MockOpenSessionInViewFilter filter = new MockOpenSessionInViewFilter();
	JdbcTemplate jdbcTemplate;
	private Role role;
	public void setUp() throws DataAccessException, FileNotFoundException, IOException {
		context = new ClassPathXmlApplicationContext("/fortest_spring/applicationContext-*.xml");
		jdbcTemplate = (JdbcTemplate)context.getBean("jdbcTemplate");
		roleDao = (RoleDao)context.getBean("roleDao");
		resourceDao = (ResourceDao)context.getBean("resourceDao");
		filter.setSessionFactory((SessionFactory)context.getBean("sessionFactory"));
		filter.beginFilter();
		
		//File sql = ResourceUtils.getFile("classpath:/fortest_spring/tables.sql");
		//jdbcTemplate.execute(IOUtils.toString(new FileReader(sql)));
	}
	public void testSerializerWithNotInitialize() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		
		//role为原始对象
		role = (Role)roleDao.getById(new Long(1));
		//生成的动态代理proxyRole,访问延迟加载的属性将return null
		Role proxyRole = (Role)new HibernateBeanSerializer<Role>(role).getProxy();
		assertNotNull(role.getResource());  
		assertNull(proxyRole.getResource()); //延迟加载,为null
		
		Hibernate.initialize(role.getResource()); //抓取进来
		assertNotNull(proxyRole.getResource()); //不为null
		
		System.out.println("ProxyRole:"+BeanUtils.describe(proxyRole));
		System.out.println("ProxyResource:"+BeanUtils.describe(proxyRole.getResource()));
		
		System.out.println("class:"+proxyRole.getClass());
		Object[] callbacks = (Object[])PropertyUtils.getSimpleProperty(proxyRole,"callbacks");
		assertNotNull(callbacks);
		for(Object callback : callbacks) {
			System.out.println("callback:"+callback);
		}
		
		System.out.println("Role:"+BeanUtils.describe(role));
		System.out.println("Resource:"+BeanUtils.describe(role.getResource()));
	}
	
	public void testSerializerWithInitialize(){
		role = (Role)roleDao.getById(new Long(1));
		
		Hibernate.initialize(role.getResource());
		Role proxyRole = new HibernateBeanSerializer<Role>(role).getProxy();
		assertNotNull(role.getResource());
		assertNotNull(proxyRole.getResource());
		
		assertNull(proxyRole.getResource().getRoles());		
		Hibernate.initialize(role.getResource().getRoles());		
		assertNotNull(proxyRole.getResource().getRoles());
		
		Role navRole = proxyRole.getResource().getRoles().iterator().next();
		assertNotNull(navRole.getResource());		
	}

	public void testSerializerWithInitialize2(){
		Resource resource = resourceDao.getById(new Long(1));
		Resource proxyResource = (Resource)new HibernateBeanSerializer(resource).getProxy();
		
		assertNotNull(resource.getRoles());
		assertNull(proxyResource.getRoles());
		
		Hibernate.initialize(resource.getRoles());
		assertNotNull(proxyResource.getRoles());
	}
	
	
	public void tearDown() {
		filter.endFilter();
	}
	
	public void test() {}
}
