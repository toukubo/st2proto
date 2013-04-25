package net.storyteller2.web;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class ModelInjectInterceptorTest {

	private ModelInjectInterceptor interceptor;
	private TestController controller;

	@Before
	public void setUp() {
		interceptor = new ModelInjectInterceptor();
		controller = new TestController();
	}
	
	@Test
	public void testGetDao() throws Exception {
		assertEquals(TestDao.class, interceptor.getDao(controller).getClass());
	}
	
	@Test
	public void testGetId() throws Exception {
		assertEquals(5, interceptor.getId(controller));
	}

	@Test
	public void testBuildDaoName() {
		assertEquals("testDao", interceptor.buildDaoName("TestController"));
	}

	class TestController {
		public int id = 5;
		public TestDao testDao = new TestDao();
	}
	
	class TestDao { 
		
	}

}
