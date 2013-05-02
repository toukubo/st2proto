package net.malta.web.app.result;

import java.util.Collection;

import net.malta.model.Tag;

public class CategoryShowTagsResult {
    public int id;
    public Collection<Tag> tags;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Collection<Tag> getTags() {
        return tags;
    }

    public void setTags(Collection<Tag> tags) {
        this.tags = tags;
    }
}
