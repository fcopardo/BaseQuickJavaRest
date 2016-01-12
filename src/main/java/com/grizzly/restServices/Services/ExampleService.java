package com.grizzly.restServices.Services;

import com.grizzly.rest.GenericRestCall;
import com.grizzly.rest.Model.afterTaskCompletion;
import org.springframework.http.HttpMethod;

/**
 * Created by FcoPardo on 1/12/16.
 */
public class ExampleService {


    public static GenericRestCall<Void, Void, Void> postCall(afterTaskCompletion taskCompletion){
        return new GenericRestCall<>(Void.class, Void.class, Void.class)
        .setMethodToCall(HttpMethod.POST)
        .setUrl("http://localhost:8082/hello/post")
        .setTaskCompletion(taskCompletion);
    }

}
