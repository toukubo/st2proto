package net.storyteller2.web;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;

public class ModelInjectInterceptorTest {

	private ModelInjectInterceptor interceptor;
	private TestController controller;

	@Before
	public void setUp() {
		interceptor = new ModelInjectInterceptor();
		controller = new TestController();
	}
	
    @Test
    public void testInterceptor() throws Exception {
        assertNull(controller.model);
        ActionInvocation invocation = createActionInvocation("show", controller);
        interceptor.intercept(invocation);
        assertEquals("themodel", (String) controller.model);
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

    private ActionInvocation createActionInvocation(String method, Object action) {
        ActionProxy actionProxy = mock(ActionProxy.class);
        when(actionProxy.getMethod()).thenReturn(method);

        ActionInvocation actionInvocation = mock(ActionInvocation.class);
        when(actionInvocation.getProxy()).thenReturn(actionProxy);
        when(actionInvocation.getAction()).thenReturn(action);

        return actionInvocation;
    }
    
	class TestController {
		public int id = 5;
		public TestDao testDao = new TestDao();
		public Object model = null;
	}
	
	class TestDao { 
		public Object load(Integer id) {
			return new String("themodel");
		}
	}

}
