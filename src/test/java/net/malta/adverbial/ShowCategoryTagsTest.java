package net.malta.adverbial;

import static org.junit.Assert.*;
import net.malta.model.Category;
import net.malta.model.CategoryImpl;
import net.malta.model.TagDao;
import net.malta.web.app.result.CategoryShowTagsResult;

import org.junit.Before;
import org.junit.Test;

public class ShowCategoryTagsTest {

    private ShowCategoryTags showCategoryTags;

    @Before
    public void setUp() {
        showCategoryTags = new ShowCategoryTags();
        showCategoryTags.setTagDao(new TagDao());
    }

    @Test
    public void test() {
        Category category = new CategoryImpl();
        category.setId(1);

        assertNull(category.getTags());
        CategoryShowTagsResult result = showCategoryTags.execute(category);
        assertEquals(2, result.getTags().size());
        assertEquals(1, result.getId());
    }

}
