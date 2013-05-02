package net.malta.web.app;

import java.util.ArrayList;
import java.util.Collection;

import net.malta.adverbial.ShowCategoryTags;
import net.malta.model.Category;
import net.malta.model.CategoryDao;
import net.malta.model.CategoryImpl;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.apache.struts2.rest.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@Results({ @Result(name = "success", type = "redirectAction", params = {
        "actionName", "category" }) })
public class CategoryController extends ActionSupport implements
        ModelDriven<Object> {

    private static Logger logger = LoggerFactory
            .getLogger(CategoryController.class);

    /**
     * Result objects - one of 3 used depending on which method is requested
     */
    public Category model = new CategoryImpl();
    private Collection<Category> list;
    public Object customResult; // set this to override Category result

    /**
     * Injected Objects
     */
    public CategoryDao categoryDao;
    public int id;
    private ShowCategoryTags showCategoryTags;

    // GET /category/1
    public HttpHeaders show() {
        logger.info("show()");
        return new DefaultHttpHeaders("show");
    }

    // GET /category
    public HttpHeaders index() {
        logger.info("index()");
        list = categoryDao.loadAll();
        return new DefaultHttpHeaders("index").disableCaching();
    }

    // GET /category/1/edit
    public String edit() {
        return "edit";
    }

    // GET /category/new
    public String editNew() {
        model = new CategoryImpl();
        return "editNew";
    }

    // GET /category/1/deleteConfirm
    public String deleteConfirm() {
        return "deleteConfirm";
    }

    // DELETE /category/1
    public String destroy() {
        categoryDao.remove(Integer.valueOf(id));
        addActionMessage("Category removed successfully");
        return "success";
    }

    // POST /category
    public HttpHeaders create() {
        categoryDao.create(model);
        addActionMessage("New category created successfully");
        return new DefaultHttpHeaders("success").setLocationId(model.getId());
    }

    // PUT /category/1
    public String update() {
        categoryDao.update(model);
        addActionMessage("Category updated successfully");
        return "success";
    }

    public void setId(Integer id) {
        logger.info("setId() [" + id + "]");
        this.id = id;
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

    private void setCustomResult(Object result) {
        customResult = result;
    }

    public void setCategoryDao(CategoryDao dao) {
        this.categoryDao = dao;
    }

    
    /*******************    Custom methods START   ********************/

    public HttpHeaders showTags() {
        Object result = showCategoryTags.execute(model);
        setCustomResult(result);
        return new DefaultHttpHeaders("show");
    }

    public HttpHeaders showWithProduct() {
        logger.info("showWithProduct()");

        // add some products
        ArrayList<String> products = new ArrayList<String>();
        products.add("1");
        products.add("2");
        model.setProducts(products);

        return new DefaultHttpHeaders("show");
    }

    /*******************    Custom methods END   ********************/

    
    /*******************    Additional injections START   ********************/
    public void setShowCategoryTags(ShowCategoryTags showCategoryTags) {
        this.showCategoryTags = showCategoryTags;
    }
    /*******************    Additional injections END   ********************/


}
