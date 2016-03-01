package com.grizzly.restServices.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by fpardo on 3/1/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MeliFilterLight extends MeliFilterValueLight{

    private MeliFilterValueLight[] values;

    public MeliFilterValueLight[] getValues() {
        return values;
    }

    public void setValues(MeliFilterValueLight[] values) {
        this.values = values;
    }
}
