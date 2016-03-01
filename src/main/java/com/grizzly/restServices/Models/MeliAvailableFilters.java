package com.grizzly.restServices.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by fpardo on 3/1/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MeliAvailableFilters {

    @JsonProperty("available_filters")
    protected MeliFilter[] availableFilters;

    public MeliFilter[] getAvailableFilters() {
        if(availableFilters == null) availableFilters = new MeliFilter[0];
        return availableFilters;
    }

    public void setAvailableFilters(MeliFilter[] availableFilters) {
        this.availableFilters = availableFilters;
    }
}
