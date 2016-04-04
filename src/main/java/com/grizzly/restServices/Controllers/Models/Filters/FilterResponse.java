package com.grizzly.restServices.Controllers.Models.Filters;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.grizzly.restServices.Models.MeliAvailableFilters;

/**
 * Created by fpardo on 4/4/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FilterResponse {

    @JsonProperty("available_filters")
    private AvailableFilters[] availableFilters;

    @JsonProperty("groups")
    private Groups[] groups;

    public AvailableFilters[] getAvailableFilters() {
        if(availableFilters == null) availableFilters = new AvailableFilters[0];
        return availableFilters;
    }

    public void setAvailableFilters(AvailableFilters[] availableFilters) {
        this.availableFilters = availableFilters;
    }

    public Groups[] getGroups() {
        if(groups == null) groups = new Groups[0];
        return groups;
    }

    public void setGroups(Groups[] groups) {
        this.groups = groups;
    }
}
