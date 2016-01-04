package com.grizzly.rest.Services;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;

/**
 * Created by FcoPardo on 1/3/16.
 */
public abstract class BaseService {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response home(){
        if(getMyMethods()==null || getMyMethods().isEmpty()){
            return Response.status(Response.Status.OK).entity(myMethods()).build();
            //return myMethods();
        }
        else{
            return Response.status(Response.Status.OK).entity(getMyMethods()).build();
            //return getMyMethods();
        }
    }

    protected HashMap<String, String> myMethods(){

        HashMap<String, String> aMap = new HashMap<>();
        aMap.put("Service:", "Describe your service");

        return aMap;
    }

    protected abstract HashMap<String, String> getMyMethods();

}
