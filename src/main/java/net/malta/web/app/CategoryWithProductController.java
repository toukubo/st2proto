package net.malta.web.app;

import java.util.ArrayList;

import net.malta.model.Category;
import net.malta.model.CategoryDao;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.apache.struts2.rest.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class CategoryWithProductController extends ActionSupport implements
        ModelDriven<Object> {
    private static Logger logger = LoggerFactory
            .getLogger(CategoryWithProductController.class);

    public Category model;

    public CategoryDao categoryDao;

    public int id;

    // GET /category/showWithProduct
    @Action(value = "/category/showWithProduct", results = { 
            @Result(name = "show", location = "/WEB-INF/content/category-show.jsp") })
    public HttpHeaders show() {
        logger.info("showWithProduct()");

        // add some products
        ArrayList<String> products = new ArrayList<String>();
        products.add("1");
        products.add("2");
        model.setProducts(products);

        return new DefaultHttpHeaders("show");
    }

    public void setId(Integer id) {
        logger.info("setId() [" + id + "]");
        if (id != null) {
            this.model = categoryDao.load(id);
        }
        this.id = id;
    }

    public Object getModel() {
        logger.info("getModel()");
        return model;
    }

    public void setCategoryDao(CategoryDao dao) {
        this.categoryDao = dao;
    }

}
