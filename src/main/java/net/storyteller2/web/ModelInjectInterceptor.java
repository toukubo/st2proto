package net.storyteller2.web;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class ModelInjectInterceptor extends AbstractInterceptor {
    private static final long serialVersionUID = 8672868777153078486L;

    // Black list rather then white list to allow custom methods to be injected. 
    // eg. showWithTag. 
    private static final Collection<String> EXCLUDED_METHODS = new ArrayList<String>();
    static {
        EXCLUDED_METHODS.add("create");
        EXCLUDED_METHODS.add("update");
        EXCLUDED_METHODS.add("destroy");
        EXCLUDED_METHODS.add("editNew");
        EXCLUDED_METHODS.add("deleteConfirm");
        EXCLUDED_METHODS.add("index");
    }
    
    private static Logger logger = LoggerFactory
            .getLogger(ModelInjectInterceptor.class);

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        String proxyMethod = invocation.getProxy().getMethod();
        Object action = invocation.getAction();
        Class actionClass = action.getClass();
        logger.debug("action class = " + actionClass);

        boolean isRestController = actionClass.getSimpleName().endsWith(
                "Controller");
        boolean isExcludedMethod = EXCLUDED_METHODS.contains(proxyMethod);
        if (!isExcludedMethod && isRestController) {
            Object dao = getDao(action);
            if (dao != null) {
                logger.debug("dao class = " + dao.getClass());

                int id = getId(action);
                logger.debug("category id = " + id);

                Method method = dao.getClass().getMethod("load", Integer.class);
                Object modelObject = method.invoke(dao, id);

                if (modelObject != null) {
                    Field modelField = actionClass.getField("model");
                    modelField.set(action, modelObject);
                }
            }
        }
        return invocation.invoke();
    }

    /**
     * For given REST controller get the <model>Dao field
     * 
     * @param controller
     *            Instance of a REST Controller
     * @return dao field for the controller or null if none found
     * @throws Exception
     */
    protected Object getDao(Object controller) throws Exception {
        String className = controller.getClass().getSimpleName();
        String daoFieldName = buildDaoName(className);
        if (daoFieldName == null) {
            return null;
        }

        Field daoField;
        try {
            daoField = controller.getClass().getField(daoFieldName);
        } catch (NoSuchFieldException nsfe) {
            logger.debug("no dao field " + daoFieldName
                    + " in controller for class " + className);
            return null;
        }

        return daoField.get(controller);
    }

    protected int getId(Object action) throws Exception {
        Class klass = action.getClass();
        return klass.getField("id").getInt(action);
    }

    /**
     * Make dao field name given a REST controller name.
     * 
     * Eg. "CategoryController" --> "cateogryDao"
     * 
     * @param controllerName
     *            Name of a REST controller
     * @return dao field name or null if does not end in "Controller"
     */
    protected String buildDaoName(String controllerName) {
        if (!controllerName.endsWith("Controller"))
            return null;

        String modelName = controllerName.replace("Controller", "");
        return modelName.substring(0, 1).toLowerCase()
                + modelName.substring(1, modelName.length()) + "Dao";
    }

}
