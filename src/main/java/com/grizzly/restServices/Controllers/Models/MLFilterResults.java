package com.grizzly.restServices.Controllers.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.grizzly.restServices.Models.MeliAvailableFilters;
import com.grizzly.restServices.Models.MeliFilter;

/**
 * Created by fpardo on 3/1/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MLFilterResults extends MeliAvailableFilters {

    @JsonProperty("queried_category")
    private String queriedCategory;
    @JsonProperty("total_filters")
    private int totalFilters;

    public MLFilterResults() {
    }

    public MLFilterResults(MeliAvailableFilters filters) {
        setAvailableFilters(filters.getAvailableFilters());
    }

    public String getQueriedCategory() {
        return queriedCategory;
    }

    public void setQueriedCategory(String queriedCategory) {
        this.queriedCategory = queriedCategory;
    }

    public int getTotalFilters() {
        return totalFilters;
    }

    public void setTotalFilters(int totalFilters) {
        this.totalFilters = totalFilters;
    }

    @Override
    public void setAvailableFilters(MeliFilter[] availableFilters) {
        super.setAvailableFilters(availableFilters);
        setTotalFilters(availableFilters.length);
    }
}
