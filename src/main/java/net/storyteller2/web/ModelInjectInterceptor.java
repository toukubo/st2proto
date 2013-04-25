package net.storyteller2.web;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class ModelInjectInterceptor extends AbstractInterceptor {
	private static final long serialVersionUID = 8672868777153078486L;

	private static Logger logger = LoggerFactory
			.getLogger(ModelInjectInterceptor.class);

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		Object action = invocation.getAction();
		Class actionClass = action.getClass();
		logger.debug("action class = " + actionClass);

		Object dao = getDao(action);
		logger.debug("dao class = " + dao.getClass());

		int id = getId(action);
		logger.debug("category id = " + id);

		Method method = dao.getClass().getMethod("load", Integer.class);
		Object modelObject = method.invoke(dao, id);

		Field modelField = actionClass.getField("model");
		modelField.set(action, modelObject);

		return invocation.invoke();
	}

	protected Object getDao(Object action) throws Exception {
		String daoFieldName = buildDaoName(action.getClass().getSimpleName());
		Field daoField = action.getClass().getField(daoFieldName);
		return daoField.get(action);
	}

	protected int getId(Object action) throws Exception {
		Class klass = action.getClass();
		return klass.getField("id").getInt(action);
	}

	protected String buildDaoName(String controllerName) {
		String modelName = controllerName.replace("Controller", "");
		return modelName.substring(0, 1).toLowerCase()
				+ modelName.substring(1, modelName.length()) + "Dao";
	}

}
