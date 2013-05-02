package net.malta.adverbial;

import net.malta.model.Category;
import net.malta.model.TagDao;
import net.malta.web.app.result.CategoryShowTagsResult;

public class ShowCategoryTags {

    private TagDao tagDao;
    
    public CategoryShowTagsResult execute(Category category) {
        CategoryShowTagsResult result = new CategoryShowTagsResult();
        result.setId(category.getId());
        result.setTags(tagDao.getTagsForCategory(category));
        return result;
     }
    
    public void setTagDao(TagDao tagDao) {
        this.tagDao = tagDao;
    }

}
