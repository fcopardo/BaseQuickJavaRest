package com.grizzly.restServices.Models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by FcoPardo on 1/28/16.
 */
public class HtmlString {

    @JsonProperty("html")
    private String html;

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }
}
