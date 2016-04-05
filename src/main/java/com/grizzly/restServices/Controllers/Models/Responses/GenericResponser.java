package com.grizzly.restServices.Controllers.Models.Responses;

/**
 * Created by fpardo on 4/5/16.
 */
public class GenericResponser<T> extends Responser {
    private Class<T> responseClass;
    private T response;

    public GenericResponser(Class<T> responseClass){
        this.responseClass = responseClass;
    }

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }
}
