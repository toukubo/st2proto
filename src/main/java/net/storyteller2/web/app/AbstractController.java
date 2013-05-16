package net.storyteller2.web.app;

import java.util.Collection;

import org.apache.struts2.rest.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public abstract class AbstractController<T> extends ActionSupport implements
        ModelDriven<Object> {
    private static final long serialVersionUID = 6853207991924183187L;

    private static Logger logger = LoggerFactory
            .getLogger(AbstractController.class);

    public int id;

    // model must be public for ModelDriven to work correctly
    public T model = newModel();

    protected Collection<T> list;
    protected Object customResult; // set this to override Category result

    /**
     * Subclass must create an instance of the model type here.
     * 
     * @return T instantiated instance
     */
    public abstract T newModel();

    // GET /category/1
    public abstract HttpHeaders show();

    // GET /category
    public abstract HttpHeaders index();

    // GET /category/new
    public abstract String editNew();

    // DELETE /category/1
    public abstract String destroy();

    // POST /category
    public abstract HttpHeaders create();

    // PUT /category/1
    public abstract String update();

    // GET /category/1/edit
    public String edit() {
        return "edit";
    }

    // GET /category/1/deleteConfirm
    public String deleteConfirm() {
        return "deleteConfirm";
    }

    public void setId(Integer id) {
        logger.info("setId() [" + id + "]");
        this.id = id;
    }

    protected void setCustomResult(Object result) {
        this.customResult = result;
    }

    public Object getModel() {
        logger.info("getModel()");
        Object resultObj;
        if (customResult != null) {
            resultObj = customResult;
        } else if (list != null) {
            resultObj = list;
        } else {
            resultObj = model;
        }
        return resultObj;
    }

}
