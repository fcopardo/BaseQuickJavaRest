package com.grizzly.restServices.Controllers.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.grizzly.restServices.Models.MeliAvailableFilters;
import com.grizzly.restServices.Models.MeliAvailableFiltersLight;
import com.grizzly.restServices.Models.MeliFilter;
import com.grizzly.restServices.Models.MeliFilterLight;

/**
 * Created by fpardo on 3/1/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MLFilterResultsLight extends MeliAvailableFiltersLight {

    @JsonProperty("queried_category")
    private String queriedCategory;
    @JsonProperty("total_filters")
    private int totalFilters;

    public MLFilterResultsLight() {
    }

    public MLFilterResultsLight(MeliAvailableFiltersLight filters) {
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
    public void setAvailableFilters(MeliFilterLight[] availableFilters) {
        super.setAvailableFilters(availableFilters);
        setTotalFilters(availableFilters.length);
    }
}
