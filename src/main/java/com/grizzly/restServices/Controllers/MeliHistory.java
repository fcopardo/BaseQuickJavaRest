package com.grizzly.restServices.Controllers;


import com.grizzly.restServices.Controllers.Models.Filters.Filter;
import com.grizzly.restServices.Controllers.Models.Responses.GenericResponser;
import com.grizzly.restServices.Conversions.Conversions;
import com.grizzly.restServices.Models.MeliCategory;
import com.grizzly.restServices.Models.MeliFilter;
import com.grizzly.restServices.Services.MLCategoryService;
import com.grizzly.restServices.Services.MLFilterService;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        if(category == null || category.trim() == "") category = "MLM1459";
        Map<String, Object> params = Conversions.convertMultiToRegularMap(info.getQueryParameters(), "-");
        List<String> whiteList = null;
        LatchList lister = null;

        if(params.containsKey("whitelist")){
            params.remove("whitelist");
            whiteList = info.getQueryParameters().get("whitelist");
            CountDownLatch listLatch = new CountDownLatch(1);
            lister = new LatchList(whiteList, listLatch);
            lister.execute();
        }

        final LatchList finalLister = lister;
        final List<String> finalWhiteList = whiteList;

        MLCategoryService.getCategories(new Action1<MeliCategory>() {
            @Override
            public void call(MeliCategory meliCategory) {

                CountDownLatch latch;
                GenericResponser<Filter[]> responseStatus = new GenericResponser<>(Filter[].class);
                responseStatus.setExpectedOperations(1);
                if(finalLister !=null) finalLister.countdown();

                latch = new CountDownLatch(1);

                MLFilterService.getFilters(new Action1<MeliFilter[]>() {
                    @Override
                    public void call(MeliFilter[] meliFilters) {
                        Conversions.updateFilters(meliCategory.getId(), meliCategory.getName(), meliFilters, responseStatus, finalWhiteList);
                        latch.countDown();
                    }
                }, meliCategory.getId().substring(0,3),meliCategory.getId(), params);

                try {
                    latch.await();
                    asyncResponse.resume(Response.status(Response.Status.OK).entity(responseStatus.getResponse()).build());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    asyncResponse.resume(Response.status(Response.Status.NOT_FOUND).build());
                }
            }
        }, category, params);
    }

    class LatchList implements Runnable {

        public List<String> whiteList;
        public CountDownLatch listLatch;

        public LatchList(List<String> aWhiteList, CountDownLatch aLatch){
            whiteList = aWhiteList;
            listLatch = aLatch;
        }

        @Override
        public void run() {
            if(whiteList.size() == 1){
                String joinedString = whiteList.get(0);
                whiteList.removeAll(whiteList);
                String[] newList = joinedString.split(",");
                whiteList.addAll(Arrays.asList(newList));
            }
            listLatch.countDown();
        }

        public void countdown(){
            try {
                listLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void execute(){
            Thread t = new Thread(this);
            t.start();
        }
    }

}
