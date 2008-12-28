package sample.model.web;

import javax.faces.model.SelectItem;

public class SelectItemWithId extends SelectItem {
    
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
