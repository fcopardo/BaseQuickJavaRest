package com.grizzly.restServices.Controllers.Models.Responses;

import java.util.ArrayList;

/**
 * Created by fpardo on 4/5/16.
 */
public class GenericCollectionResponser<T> extends Responser {
    private Class<T> responseClass;
    private ArrayList<T> response;

    public GenericCollectionResponser(Class<T> responseClass){
        this.responseClass = responseClass;
    }

    public ArrayList<T> getResponse() {
        if(response == null) response = new ArrayList<>();
        return response;
    }

    public void setResponse(ArrayList<T> response) {
        this.response = response;
    }

    public void addResponse(T responseNode){
        if(response == null) response = new ArrayList<>();
        response.add(responseNode);
    }
}
