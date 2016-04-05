package com.grizzly.restServices.Controllers.Models.Filters;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fpardo on 4/4/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder(alphabetic=true)
public class FilterResponse {

    @JsonProperty("available_filters")
    private AvailableFilters[] availableFilters;
    @JsonIgnore
    @XmlTransient
    private List<AvailableFilters> availableFiltersList;

    @JsonProperty("groups")
    private Groups[] groups;
    @JsonIgnore
    @XmlTransient
    private List<Groups> groupsList;

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

    @JsonIgnore
    public List<AvailableFilters> getAvailableFiltersList() {
        if(availableFiltersList == null) availableFiltersList = new ArrayList<>();
        return availableFiltersList;
    }

    @JsonIgnore
    public void setAvailableFiltersList(List<AvailableFilters> availableFiltersList) {
        this.availableFiltersList = availableFiltersList;
    }

    @JsonIgnore
    public List<Groups> getGroupsList() {
        return groupsList;
    }

    @JsonIgnore
    public void setGroupsList(List<Groups> groupsList) {
        if(groupsList == null) groupsList = new ArrayList<>();
        this.groupsList = groupsList;
    }

    @JsonIgnore
    public void listsToArray(){
        if(groupsList!=null)groups = groupsList.toArray(new Groups[0]);
        if(availableFiltersList!=null) availableFilters = availableFiltersList.toArray(new AvailableFilters[0]);
    }
}
