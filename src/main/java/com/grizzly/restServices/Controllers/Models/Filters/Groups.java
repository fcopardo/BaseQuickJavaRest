package com.grizzly.restServices.Controllers.Models.Filters;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by fpardo on 4/4/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Groups {

    @JsonProperty("group_name")
    private String groupName;
    @JsonProperty("order")
    private int order;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
