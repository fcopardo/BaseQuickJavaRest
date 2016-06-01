package com.grizzly.restServices.Controllers;


import com.grizzly.rest.Model.RestResults;
import com.grizzly.restServices.Models.MLItem;
import com.grizzly.restServices.Models.MeliCategory;
import com.grizzly.restServices.Models.MeliCategoryNode;
import com.grizzly.restServices.Models.MeliHistoryNode;
import com.grizzly.restServices.Services.MLCategoryService;
import com.grizzly.restServices.Services.MLItemService;
import com.grizzly.restServices.Services.MLSearchHistoryService;
import rx.functions.Action1;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by FcoPardo on 1/3/16.
 */
@Path("/history")
public class MeliHistory extends BaseService {

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
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    public void getUpdated(@Suspended final AsyncResponse asyncResponse, @Context UriInfo info) {

        String category = info.getQueryParameters().getFirst("category");
        String userId = info.getQueryParameters().getFirst("user_id");
        if(category == null || category.trim() == "") category = "MLM1459";
        String site = category.substring(0,3);

        CountDownLatch latch = new CountDownLatch(1);

        MLCategoryService.getCategories(new Action1<MeliCategory>() {
            @Override
            public void call(MeliCategory meliCategory) {

                List<MeliHistoryNode> responseNode = new ArrayList<MeliHistoryNode>();

                List<String> categories = new ArrayList<>();
                categories.add(meliCategory.getId());
                for(MeliCategoryNode categoryNode : meliCategory.getChildrenCategories()){
                    categories.add(categoryNode.getId());
                    System.out.println("My category id is "+categoryNode.getId());
                }

                MLSearchHistoryService.getUserHistory(new Action1<MeliHistoryNode[]>() {
                    @Override
                    public void call(MeliHistoryNode[] meliHistoryNodes) {

                        CountDownLatch groupLatch = new CountDownLatch(meliHistoryNodes.length);

                        for(MeliHistoryNode node : meliHistoryNodes){

                            MLItemService.callItem(new Action1<RestResults<MLItem>>() {
                                @Override
                                public void call(RestResults<MLItem> mlItemRestResults) {
                                    if(mlItemRestResults.isSuccessful()){
                                        System.out.println("SUCCESS");
                                        System.out.println("Item category:"+mlItemRestResults.getSubscriberEntity().getCategoryId());

                                        //if(categories.contains(mlItemRestResults.getSubscriberEntity().getCategoryId())){
                                        for(String s: categories){

                                            System.out.println(mlItemRestResults.getSubscriberEntity().getCategoryId() +" == "+s);

                                            if(s.equalsIgnoreCase(mlItemRestResults.getSubscriberEntity().getCategoryId())){
                                                responseNode.add(node);
                                                groupLatch.countDown();
                                            }
                                        }
                                        /*if(categories.contains(mlItemRestResults.getSubscriberEntity().getCategoryId())){

                                        }*/
                                    }
                                }
                            }, node.getItemId(), true);
                        }

                        try {
                            groupLatch.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }, site, userId, true);

                latch.countDown();
                asyncResponse.resume(Response.status(Response.Status.OK).entity(responseNode.toArray(new MeliHistoryNode[responseNode.size()])).build());
            }
        }, category, null);
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
