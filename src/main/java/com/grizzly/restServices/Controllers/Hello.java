package com.grizzly.restServices.Controllers;


import com.grizzly.rest.GenericRestCall;
import com.grizzly.rest.Model.afterTaskCompletion;
import com.grizzly.restServices.Services.ExampleService;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.HashMap;

/**
 * Created by FcoPardo on 1/3/16.
 */
@Path("/hello")
public class Hello extends BaseService {

    @GET
    @Path("test")
    @Produces(MediaType.APPLICATION_JSON)
    public Response test(){

        return Response.status(Response.Status.OK).entity("hello rest").build();
    }

    @Override
    protected HashMap<String, String> getMyMethods() {

        HashMap<String, String> aMap = new HashMap<>();
        aMap.put("Service 1:", "/hello");
        aMap.put("Service 2:", "/more");
        return aMap;
    }

    @GET
    @Path("/more")
    @Produces(MediaType.APPLICATION_JSON)
    public Response exampleGet(@Context final UriInfo headers) {
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("/async")
    @Produces(MediaType.APPLICATION_JSON)
    public void asyncGet(@Suspended final AsyncResponse asyncResponse) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Response result = veryExpensiveOperation();
                asyncResponse.resume(result);
            }

            private Response veryExpensiveOperation() {
                return Response.status(Response.Status.OK).entity("hello rest").build();
            }
        }).start();
    }

    @POST
    @Path("/post")
    @Produces(MediaType.APPLICATION_JSON)
    public Response examplePost(@Context final UriInfo headers) {
        return Response.status(Response.Status.OK).build();
    }

    @POST
    @Path("/asyncpost")
    @Produces(MediaType.APPLICATION_JSON)
    public void asyncPost(@Suspended final AsyncResponse asyncResponse) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Response result = veryExpensiveOperation();
                asyncResponse.resume(result);
            }

            private Response veryExpensiveOperation() {

                GenericRestCall<Void, Void, Void> restCall = ExampleService.postCall(null);
                if(restCall.get()){
                    return Response.status(Response.Status.OK).build();
                }
                else{
                    return Response.status(Response.Status.BAD_REQUEST).build();
                }
            }
        }).start();
    }



}
