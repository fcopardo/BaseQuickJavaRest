package com.grizzly.restServices.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Created by fpardo on 5/30/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MeliHistoryNode {

    @JsonProperty("item_id")
    private String itemId;
    @JsonProperty("visited")
    private Date visitDate;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public Date getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(Date visitDate) {
        this.visitDate = visitDate;
    }
}
