package com.grizzly.restServices.Services;

import com.grizzly.rest.GenericRestCall;
import com.grizzly.rest.Model.afterTaskCompletion;
import org.springframework.http.HttpMethod;

/**
 * Created by FcoPardo on 1/12/16.
 */
public class ExampleService {


    public static GenericRestCall<Void, Void, Void> postCall(afterTaskCompletion taskCompletion){
        GenericRestCall<Void, Void, Void> restCall = new GenericRestCall<>(Void.class, Void.class, Void.class);
        restCall.setMethodToCall(HttpMethod.POST);
        restCall.setUrl("http://localhost:8082/hello/post");
        restCall.setTaskCompletion(taskCompletion);
        return restCall;
    }

}
