package com.grizzly.restServices.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.log4j.lf5.viewer.categoryexplorer.CategoryNode;

/**
 * Created by fpardo on 3/1/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MeliCategory extends MeliCategoryNode{

    @JsonProperty("path_from_root")
    protected MeliCategoryNode[] pathFromRoot;
    @JsonProperty("children_categories")
    protected MeliCategoryNode[] childrenCategories;

    @JsonProperty("extra_operations")
    protected int operations;

    public MeliCategoryNode[] getPathFromRoot() {
        return pathFromRoot;
    }

    public void setPathFromRoot(MeliCategoryNode[] pathFromRoot) {
        this.pathFromRoot = pathFromRoot;
    }

    public MeliCategoryNode[] getChildrenCategories() {
        return childrenCategories;
    }

    public int getOperations() {
        return operations;
    }

    public void setOperations(int operations) {
        this.operations = operations;
    }

    public void setChildrenCategories(MeliCategoryNode[] childrenCategories) {
        this.childrenCategories = childrenCategories;
    }
}
