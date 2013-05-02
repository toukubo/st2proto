package net.malta.model;

import java.util.ArrayList;
import java.util.Collection;

public class TagDao {
    public Collection<Tag> getTagsForCategory(Category category) {
        // do hibernate querying here
        
        // for now just stub and return 2 tags
        Collection<Tag> tags = new ArrayList<Tag>();
        tags.add(new Tag(1111));
        tags.add(new Tag(2222));
        return tags;
    }
}
