package com.grizzly.restServices.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by fpardo on 3/31/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MeliCategoryNode {

    @JsonProperty("id")
    protected String id;
    @JsonProperty("name")
    protected String name;
    @JsonProperty("total_items_in_this_category")
    protected int totalItems;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }
}
