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
    public void testInterceptorDefaultInjectedMethods() throws Exception {
        String[] methods = { "show", "edit" };
        for (int i = 0; i < methods.length; i++) {
            String method = methods[i];
            controller.model = null;
            ActionInvocation invocation = createActionInvocation(method,
                    controller);
            interceptor.intercept(invocation);
            assertEquals("method " + method, "themodel",
                    (String) controller.model);
        }
    }

    @Test
    public void testInterceptorExcludedMethods() throws Exception {
        String[] otherMethods = { "deleteConfirm", "index", "editNew",
                "create", "destroy", "update" };
        for (int i = 0; i < otherMethods.length; i++) {
            String method = otherMethods[i];
            assertNull(controller.model);
            ActionInvocation invocation = createActionInvocation(method,
                    controller);
            interceptor.intercept(invocation);
            assertNull("not null for method " + method, controller.model);
        }
    }

    @Test
    public void testInterceptorNonDefaultMethod() throws Exception {
        final String method = "showWithTag";
        controller.model = null;
        ActionInvocation invocation = createActionInvocation(method,
                controller);
        interceptor.intercept(invocation);
        assertEquals("method " + method, "themodel",
                (String) controller.model);
    }

    @Test
    public void testGetDao() throws Exception {
        assertEquals(TestDao.class, interceptor.getDao(controller).getClass());
        assertNull(interceptor.getDao(new TestNoDaoController()));
    }

    @Test
    public void testGetId() throws Exception {
        assertEquals(5, interceptor.getId(controller));
    }

    @Test
    public void testBuildDaoName() {
        assertEquals("testDao", interceptor.buildDaoName("TestController"));
        assertEquals("oneTwoDao", interceptor.buildDaoName("OneTwoController"));
        assertNull(interceptor.buildDaoName("MyAction"));
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

    class TestNonStandardController {
        public int id = 5;
        public TestDao testDao = new TestDao();
        public Object model = null;
    }

    class TestNoDaoController {
        public int id = 5;
        public Object model = null;
    }

    class TestDao {
        public Object load(Integer id) {
            return new String("themodel");
        }
    }

}
