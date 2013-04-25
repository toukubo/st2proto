package net.malta.web.app;

import java.util.Collection;

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

	// TODO: wire up for injection
	private Category model = new CategoryImpl();

	private CategoryDao categoryDao;

	private String id;
	private Collection<Category> list;

	// GET /category/1
	public HttpHeaders show() {
		return new DefaultHttpHeaders("show");
	}

	// GET /category
	public HttpHeaders index() {
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
		addActionMessage("Order removed successfully");
		return "success";
	}

	// POST /category
	public HttpHeaders create() {
		categoryDao.create(model);
		addActionMessage("New order created successfully");
		return new DefaultHttpHeaders("success").setLocationId(model.getId());
	}

	// PUT /category/1
	public String update() {
		categoryDao.update(model);
		addActionMessage("Order updated successfully");
		return "success";
	}

//	public void validate() {
//		// validation ??
//	}

	public void setId(String id) {
		if (id != null) {
			logger.info("id = [" + id + "]");
			this.model = categoryDao.load(Integer.valueOf(id));
		}
		this.id = id;
	}

	public Object getModel() {
		return (list != null ? list : model);
	}

	public void setCategoryDao(CategoryDao dao) {
		this.categoryDao = dao;
	}

}
