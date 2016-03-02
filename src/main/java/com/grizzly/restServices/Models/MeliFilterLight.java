package com.grizzly.restServices.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by fpardo on 3/1/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MeliFilterLight extends MeliFilterValueLight{

    private MeliFilterValueLight[] values;
    @JsonProperty("type")
    protected String type;

    public MeliFilterValueLight[] getValues() {
        return values;
    }

    public void setValues(MeliFilterValueLight[] values) {
        this.values = values;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }
}
