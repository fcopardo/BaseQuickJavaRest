package com.grizzly.restServices.Controllers.Models.Filters;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by fpardo on 4/4/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder(value = {"filter_id", "filter_name", "group", "type", "values", "active"})
//@XmlType(propOrder = {"id", "name", "group", "type", "values", "active"})
public class Filter {

    @XmlElement(name="filter_id")
    @JsonProperty("filter_id")
    private String id;
    @XmlElement(name="filter_name")
    @JsonProperty("filter_name")
    private String name;
    @XmlElement(name="group")
    @JsonProperty("group")
    private String group;
    @XmlElement(name="type")
    @JsonProperty("type")
    private String type;
    @XmlElement(name="values")
    @JsonProperty("values")
    private Values[] values;
    @XmlElement(name="ios_custom_key")
    @JsonProperty("ios_custom_key")
    private String[]iosCustomKeys;
    @XmlElement(name="android_custom_key")
    @JsonProperty("android_custom_key")
    private String[]androidCustomKeys;
    @XmlElement(name="exclusions")
    @JsonProperty("exclusions")
    private String[]exclusions;
    @XmlElement(name="active")
    @JsonProperty("active")
    private boolean active;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Values[] getValues() {
        return values;
    }

    public void setValues(Values[] values) {
        this.values = values;
    }

    public String[] getIosCustomKeys() {
        return iosCustomKeys;
    }

    public void setIosCustomKeys(String[] iosCustomKeys) {
        this.iosCustomKeys = iosCustomKeys;
    }

    public String[] getAndroidCustomKeys() {
        return androidCustomKeys;
    }

    public void setAndroidCustomKeys(String[] androidCustomKeys) {
        this.androidCustomKeys = androidCustomKeys;
    }

    public String[] getExclusions() {
        return exclusions;
    }

    public void setExclusions(String[] exclusions) {
        this.exclusions = exclusions;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
