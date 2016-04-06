package com.grizzly.restServices.Controllers.Models.Filters;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by fpardo on 4/4/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder(value = {"category_id", "category_name", "order", "filters"})
//@XmlType(propOrder = {"categoryId", "categoryName", "order", "filters"})
public class AvailableFilters {

    @XmlElement(name="category_id")
    private String categoryId;
    @XmlElement(name="category_name")
    private String categoryName;
    @XmlElement(name="order")
    private int order;
    @XmlElement(name="filters")
    private Filter[] filters;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Filter[] getFilters() {
        if(filters == null) filters = new Filter[0];
        return filters;
    }

    public void setFilters(Filter[] filters) {
        this.filters = filters;
    }
}
