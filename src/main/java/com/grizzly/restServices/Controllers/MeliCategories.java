package com.grizzly.restServices.Controllers;

import com.grizzly.rest.Model.RestResults;
import com.grizzly.restServices.Controllers.Models.Responses.GenericCollectionResponser;
import com.grizzly.restServices.Models.MeliCategory;
import com.grizzly.restServices.Models.MeliCategoryNode;
import com.grizzly.restServices.Services.MLCategoryService;
import rx.functions.Action1;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by FcoPardo on 1/3/16.
 */
@Path("/categories")
public class MeliCategories extends BaseService {

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
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    public void getAllChildrens(@Suspended final AsyncResponse asyncResponse, @Context UriInfo info) {

        String site = info.getQueryParameters().getFirst("site");
        if(site == null || site.trim() == "") site = "MLA";

        MLCategoryService.getAllCategoriesFrom(new Action1<RestResults<MeliCategoryNode[]>>() {
            @Override
            public void call(RestResults<MeliCategoryNode[]> restResults) {
                if(restResults.isSuccessful()) {

                    GenericCollectionResponser<MeliCategoryNode> responseStatus = new GenericCollectionResponser<MeliCategoryNode>(MeliCategoryNode.class);
                    responseStatus.setExpectedOperations(restResults.getSubscriberEntity().length);

                    for(MeliCategoryNode node :restResults.getSubscriberEntity()){
                        MLCategoryService.getCategories(new Action1<MeliCategory>() {
                            @Override
                            public void call(MeliCategory meliCategory) {
                                if(restResults.isSuccessful())responseStatus.addResponse(node);
                                responseStatus.addDoneOperations(1);
                            }
                        }, node.getId());
                    }

                    int i = 0;
                    while(!responseStatus.Done()){
                        i++;
                    }
                    System.out.println("Waiting Time:"+i);
                    if(!responseStatus.getResponse().isEmpty()){
                        asyncResponse.resume(Response.status(Response.Status.OK).entity(responseStatus.getResponse().toArray(new MeliCategoryNode[responseStatus.getResponse().size()])).build());
                    }else{
                        asyncResponse.resume(Response.status(Response.Status.OK).build());
                    }
                }else{
                    asyncResponse.resume(Response.status(Response.Status.SERVICE_UNAVAILABLE).build());
                }

            }
        }, site);
    }

}
