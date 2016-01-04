package com.grizzly.rest.Services;


import org.eclipse.jetty.server.Request;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
        //return "hello rest";
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

}
