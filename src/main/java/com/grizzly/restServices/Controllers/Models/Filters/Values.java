package com.grizzly.restServices.Controllers.Models.Filters;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by fpardo on 4/4/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Values {

    @JsonProperty("value_id")
    private String valueId;
    @JsonProperty("value_name")
    private String valueName;

    public String getValueId() {
        return valueId;
    }

    public void setValueId(String valueId) {
        this.valueId = valueId;
    }

    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }
}
