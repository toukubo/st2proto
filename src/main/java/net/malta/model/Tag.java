package net.malta.model;

public class Tag implements java.io.Serializable {

    private static final long serialVersionUID = 1993359213404716861L;
    
    private Integer tagId;

    public Tag() {
        
    }
    
    public Tag(int id) {
        this.tagId = id;
    }
    
    public Integer getTagId() {
        return this.tagId;
    }

    public void setTagId(Integer id) {
        this.tagId = id;
    }

}