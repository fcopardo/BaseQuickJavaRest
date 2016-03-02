package com.grizzly.restServices.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by fpardo on 3/1/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MeliFilter extends MeliFilterValue{

    private MeliFilterValue[] values;
    private String type;

    public MeliFilterValue[] getValues() {
        return values;
    }

    public void setValues(MeliFilterValue[] values) {
        this.values = values;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
