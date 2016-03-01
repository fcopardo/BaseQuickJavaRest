package com.grizzly.restServices.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by fpardo on 3/1/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MeliAvailableFiltersLight {

    @JsonProperty("available_filters")
    protected MeliFilterLight[] availableFilters;

    public MeliFilterLight[] getAvailableFilters() {
        if(availableFilters == null) availableFilters = new MeliFilterLight[0];
        return availableFilters;
    }

    public void setAvailableFilters(MeliFilterLight[] availableFilters) {
        this.availableFilters = availableFilters;
    }
}
