package net.malta.web.app;

import java.util.ArrayList;

import net.malta.adverbial.ShowCategoryTags;
import net.malta.model.Category;
import net.malta.model.CategoryDao;
import net.malta.model.CategoryImpl;
import net.storyteller2.web.app.AbstractController;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.apache.struts2.rest.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Results({ @Result(name = "success", type = "redirectAction", params = {
        "actionName", "category" }) })
public class CategoryController extends AbstractController<Category> {

    private static final long serialVersionUID = 8152503813048460154L;

    private static Logger logger = LoggerFactory
            .getLogger(CategoryController.class);

    /**
     * Injected Objects
     */
    public CategoryDao categoryDao;
    private ShowCategoryTags showCategoryTags;


    @Override
    public Category newModel() {
        return new CategoryImpl();
    }

    // GET /category/1
    @Override
    public HttpHeaders show() {
        logger.info("show()");
        return new DefaultHttpHeaders("show");
    }

    // GET /category
    @SuppressWarnings("unchecked")
    @Override
    public HttpHeaders index() {
        logger.info("index()");
        list = categoryDao.loadAll();
        return new DefaultHttpHeaders("index").disableCaching();
    }

    // GET /category/new
    @Override
    public String editNew() {
        model = newModel();
        return "editNew";
    }

    // DELETE /category/1
    @Override
    public String destroy() {
        categoryDao.remove(Integer.valueOf(id));
        addActionMessage("Category removed successfully");
        return "success";
    }

    // POST /category
    @Override
    public HttpHeaders create() {
        categoryDao.create(model);
        addActionMessage("New category created successfully");
        return new DefaultHttpHeaders("success").setLocationId(model.getId());
    }

    // PUT /category/1
    @Override
    public String update() {
        categoryDao.update(model);
        addActionMessage("Category updated successfully");
        return "success";
    }

    /******************* Non default methods START ********************/

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

    /******************* Non default methods END ********************/

    /******************* Injections START ********************/
    public void setCategoryDao(CategoryDao dao) {
        this.categoryDao = dao;
    }

    public void setShowCategoryTags(ShowCategoryTags showCategoryTags) {
        this.showCategoryTags = showCategoryTags;
    }
    /******************* Injections END ********************/

}
