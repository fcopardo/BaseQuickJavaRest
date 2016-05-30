package com.grizzly.restServices.Controllers;


import com.grizzly.rest.GenericRestCall;
import com.grizzly.rest.Model.RestResults;
import com.grizzly.rest.Model.afterTaskFailure;
import com.grizzly.restServices.Controllers.Models.Filters.AvailableFilters;
import com.grizzly.restServices.Controllers.Models.Filters.Filter;
import com.grizzly.restServices.Controllers.Models.Filters.FilterResponse;
import com.grizzly.restServices.Controllers.Models.Filters.Values;
import com.grizzly.restServices.Controllers.Models.MLFilterResults;
import com.grizzly.restServices.Controllers.Models.MLFilterResultsLight;
import com.grizzly.restServices.Controllers.Models.MeliCategoryFilterInfo;
import com.grizzly.restServices.Controllers.Models.Responses.GenericResponser;
import com.grizzly.restServices.Controllers.Models.Responses.Responser;
import com.grizzly.restServices.Controllers.Models.WorkResults;
import com.grizzly.restServices.Conversions.Conversions;
import com.grizzly.restServices.Excel.ExcelFunctions;
import com.grizzly.restServices.Models.MeliCategory;
import com.grizzly.restServices.Models.MeliCategoryNode;
import com.grizzly.restServices.Models.MeliFilter;
import com.grizzly.restServices.Models.MeliFilterLight;
import com.grizzly.restServices.Services.MLCategoryService;
import com.grizzly.restServices.Services.MLFilterService;
import org.springframework.http.HttpMethod;
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
@Path("/filters")
public class MeliFilters extends BaseService {

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
    @Path("/filters")
    @Produces(MediaType.APPLICATION_JSON)
    public void getFilters(@Suspended final AsyncResponse asyncResponse, @Context UriInfo info) {

        String site = info.getQueryParameters().getFirst("site");
        String category = info.getQueryParameters().getFirst("category");
        String light = info.getQueryParameters().getFirst("light");
        
        if(site == null || site.trim() == "") site = "MLM";
        if(category == null || category.trim() == "") category = "MLM1459";
        if(light == null || light.trim() == "") light = "0";

        if(light.equalsIgnoreCase("1")){
            MLFilterService.getFiltersLight(createSimpleMeliFilterActionLight(asyncResponse, category), site, category);
        }
        else{
            MLFilterService.getFilters(createSimpleMeliFilterAction(asyncResponse, category), site, category);
        }
    }


    @GET
    @Path("/works")
    @Produces(MediaType.APPLICATION_JSON)
    public void getFiltersNamed(@Suspended final AsyncResponse asyncResponse, @Context UriInfo info) {


        class IsWorking{
            public boolean now = true;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {

                final WorkResults workResults = new WorkResults();
                final IsWorking isWorking = new IsWorking();

                String site = info.getQueryParameters().getFirst("site");
                String category = info.getQueryParameters().getFirst("category");

                if(site == null || site.trim() == "") site = "MLM";
                if(category == null || category.trim() == "") category = "MLM1459";
                final String finalCategory = category;

                while(isWorking.now){

                    if(!workResults.categoryExecuted){
                        GenericRestCall<Void, MeliCategory, String> categoryCall = new GenericRestCall<>(Void.class, MeliCategory.class, String.class);
                        categoryCall.setUrl("https://api.mercadolibre.com/categories/"+ category)
                                .setMethodToCall(HttpMethod.GET)
                                .isCacheEnabled(true)
                                .addSuccessSubscriber(new Action1<RestResults<MeliCategory>>() {
                                    @Override
                                    public void call(RestResults<MeliCategory> meliCategoryRestResults) {
                                        workResults.completedTasks = workResults.completedTasks+1;
                                        workResults.category = finalCategory;
                                        if(meliCategoryRestResults.isSuccessful() && meliCategoryRestResults.getSubscriberEntity() !=null) {
                                            workResults.categoryName = meliCategoryRestResults.getSubscriberEntity().getName();
                                        }
                                        if(workResults.completedTasks>1) isWorking.now = false;
                                    }
                                });
                        categoryCall.setAutomaticCacheRefresh(true);
                        categoryCall.execute(true);
                        workResults.categoryExecuted = true;
                    }

                    if(!workResults.filterExecuted){
                        MLFilterService.getFiltersLight(new Action1<MeliFilterLight[]>() {
                            @Override
                            public void call(MeliFilterLight[] meliFilterLights) {
                                workResults.availableFilters = meliFilterLights;
                                if(meliFilterLights!=null) workResults.totalFilters = meliFilterLights.length;
                                workResults.completedTasks = workResults.completedTasks+1;
                                if(workResults.completedTasks>1) isWorking.now = false;
                            }
                        }, site, category );
                        workResults.filterExecuted = true;
                    }
                }

                MeliCategoryFilterInfo info = new MeliCategoryFilterInfo();
                info.availableFilters = workResults.availableFilters;
                info.category = workResults.category;
                info.categoryName = workResults.categoryName;
                info.totalFilters = workResults.totalFilters;

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ExcelFunctions.exportCategory(info);
                    }
                }).start();

                asyncResponse.resume(Response.status(Response.Status.OK).entity(info).build());
            }
        }).start();
    }

    private Action1<MeliFilter[]> createSimpleMeliFilterAction(AsyncResponse asyncResponse, String finalCategory){

        return new Action1<MeliFilter[]>() {
            @Override
            public void call(MeliFilter[] meliFilters) {
                MLFilterResults results = new MLFilterResults();
                results.setAvailableFilters(meliFilters);
                results.setQueriedCategory(finalCategory);
                asyncResponse.resume(Response.status(Response.Status.OK).entity(results).build());
            }
        };
    }

    private Action1<MeliFilterLight[]> createSimpleMeliFilterActionLight(AsyncResponse asyncResponse, String finalCategory){

        return new Action1<MeliFilterLight[]>() {
            @Override
            public void call(MeliFilterLight[] meliFilters) {
                MLFilterResultsLight results = new MLFilterResultsLight();
                results.setAvailableFilters(meliFilters);
                results.setQueriedCategory(finalCategory);
                asyncResponse.resume(Response.status(Response.Status.OK).entity(results).build());
            }
        };
    }

    /** New filter zone!
     *
     */
    @GET
    @Path("/category")
    @Produces(MediaType.APPLICATION_JSON)
    public void getChildren(@Suspended final AsyncResponse asyncResponse, @Context UriInfo info) {

        String category = info.getQueryParameters().getFirst("category");
        String site = category.substring(0,2);

        if(site == null || site.trim() == "") site = "MLM";
        if(category == null || category.trim() == "") category = "MLM1459";

        MLCategoryService.getCategories(new Action1<MeliCategory>() {
            @Override
            public void call(MeliCategory meliCategory) {
                asyncResponse.resume(Response.status(Response.Status.OK).entity(meliCategory).build());
            }
        }, category);
    }

    @GET
    @Path("/categories")
    @Produces(MediaType.APPLICATION_JSON)
    public void getChildrens(@Suspended final AsyncResponse asyncResponse, @Context UriInfo info) {

        String category = info.getQueryParameters().getFirst("category");
        if(category == null || category.trim() == "") category = "MLM1459";

        MLCategoryService.getCategories(new Action1<MeliCategory>() {
            @Override
            public void call(MeliCategory meliCategory) {

                Responser responseStatus = new Responser();
                responseStatus.setExpectedOperations(meliCategory.getChildrenCategories().length);

                for(MeliCategoryNode node : meliCategory.getChildrenCategories()){
                    System.out.println("Node : "+node.getId() + " - "+node.getName());

                    MLCategoryService.getCategories(new Action1<MeliCategory>() {
                        @Override
                        public void call(MeliCategory meliCategory) {
                            MLCategoryService.getAllCategories(new Action1<RestResults<MeliCategory>>() {
                                @Override
                                public void call(RestResults<MeliCategory> meliCategoryRestResults) {
                                    responseStatus.addDoneOperations(1);
                                    if(meliCategoryRestResults.isSuccessful()){
                                        if(meliCategoryRestResults.getSubscriberEntity().getChildrenCategories().length>0){
                                            responseStatus.addExpectedOperations(meliCategoryRestResults.getSubscriberEntity().getChildrenCategories().length);
                                            for(MeliCategoryNode node1:meliCategoryRestResults.getSubscriberEntity().getChildrenCategories()){
                                                System.out.println("Internal Node : "+node1.getId() + " - "+node1.getName());
                                                MLCategoryService.getAllCategories(new Action1<RestResults<MeliCategory>>() {
                                                    @Override
                                                    public void call(RestResults<MeliCategory> meliCategoryRestResults) {
                                                        responseStatus.addDoneOperations(1);
                                                    }
                                                }, node1.getId());
                                            }
                                        }
                                    }
                                }
                            }, meliCategory.getId());
                        }
                    }, node.getId());
                }
                int i = 0;
                while(!responseStatus.Done()){
                    i++;
                }
                System.out.println("Waiting Time:"+i);

                meliCategory.setOperations(responseStatus.DoneOperations);
                asyncResponse.resume(Response.status(Response.Status.OK).entity(meliCategory).build());
            }
        }, category);
    }


    @GET
    @Path("/allcategories")
    @Produces(MediaType.APPLICATION_JSON)
    public void getAllChildrens(@Suspended final AsyncResponse asyncResponse, @Context UriInfo info) {

        String category = info.getQueryParameters().getFirst("category");
        if(category == null || category.trim() == "") category = "MLM1459";

        MLCategoryService.getCategories(new Action1<MeliCategory>() {
            @Override
            public void call(MeliCategory meliCategory) {

                CountDownLatch latch;
                GenericResponser<FilterResponse> responseStatus = new GenericResponser<>(FilterResponse.class);
                if(meliCategory.getChildrenCategories().length>0) {
                    responseStatus.setExpectedOperations(meliCategory.getChildrenCategories().length+1);
                    latch = new CountDownLatch(meliCategory.getChildrenCategories().length+1);
                }else{
                    responseStatus.setExpectedOperations(1);
                    latch = new CountDownLatch(1);
                }


                MLFilterService.getFilters(new Action1<MeliFilter[]>() {
                    @Override
                    public void call(MeliFilter[] meliFilters) {
                        Conversions.createAvailableFilters(meliCategory.getId(), meliCategory.getName(), meliFilters, responseStatus);
                        latch.countDown();
                    }
                }, meliCategory.getId().substring(0,3),meliCategory.getId());

                for(MeliCategoryNode node : meliCategory.getChildrenCategories()){

                    MLCategoryService.getCategories(new Action1<MeliCategory>() {
                        @Override
                        public void call(MeliCategory meliCategory) {
                            MLCategoryService.getAllCategories(new Action1<RestResults<MeliCategory>>() {
                                @Override
                                public void call(RestResults<MeliCategory> meliCategoryRestResults) {

                                    if(meliCategoryRestResults.isSuccessful()){

                                        MLFilterService.getFiltersSafe(new Action1<MeliFilter[]>() {
                                            @Override
                                            public void call(MeliFilter[] meliFilters) {
                                                Conversions.createAvailableFilters(meliCategoryRestResults.getSubscriberEntity().getId(), meliCategoryRestResults.getSubscriberEntity().getName(), meliFilters, responseStatus);
                                                latch.countDown();
                                            }
                                        }, new afterTaskFailure() {
                                            @Override
                                            public void onTaskFailed(Object o, Exception e) {
                                                //responseStatus.addDoneOperations(1);
                                                latch.countDown();
                                            }
                                        }, meliCategoryRestResults.getSubscriberEntity().getId().substring(0, 3), meliCategoryRestResults.getSubscriberEntity().getId());

                                        /*if(meliCategoryRestResults.getSubscriberEntity().getChildrenCategories().length>0){
                                            responseStatus.addExpectedOperations(meliCategoryRestResults.getSubscriberEntity().getChildrenCategories().length);
                                            for(MeliCategoryNode node1:meliCategoryRestResults.getSubscriberEntity().getChildrenCategories()){
                                                System.out.println("Internal Node : "+node1.getId() + " - "+node1.getName());
                                                MLFilterService.getFilters(new Action1<MeliFilter[]>() {
                                                    @Override
                                                    public void call(MeliFilter[] meliFilters) {
                                                        Conversions.createAvailableFilters(node1.getId(), node1.getName(), meliFilters, responseStatus);
                                                        responseStatus.addDoneOperations(1);
                                                    }
                                                }, node1.getId().substring(0,3), node1.getId());
                                            }
                                        }*/
                                    }
                                }
                            }, meliCategory.getId());
                        }
                    }, node.getId());
                }
                try {
                    latch.await();
                    meliCategory.setOperations(responseStatus.DoneOperations);
                    meliCategory.setOperations(responseStatus.getResponse().getAvailableFilters().length);
                    responseStatus.getResponse().listsToArray();
                    asyncResponse.resume(Response.status(Response.Status.OK).entity(responseStatus.getResponse()).build());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    asyncResponse.resume(Response.status(Response.Status.NOT_FOUND).build());
                }
            }
        }, category);
    }

    @GET
    @Path("/allcategoriesp")
    @Produces(MediaType.APPLICATION_JSON)
    public void getAllChildrensWithParameters(@Suspended final AsyncResponse asyncResponse, @Context UriInfo info) {

        String category = info.getQueryParameters().getFirst("category");
        if(category == null || category.trim() == "") category = "MLM1459";
        Map<String, Object> params = Conversions.convertMultiToRegularMap(info.getQueryParameters(), "-");
        if(params.containsKey("whitelist")){
            params.remove("whitelist");
        }
        List<String> whiteList = info.getQueryParameters().get("whitelist");

        CountDownLatch listLatch = new CountDownLatch(1);
        LatchList lister = new LatchList(whiteList, listLatch);
        lister.execute();

        MLCategoryService.getCategories(new Action1<MeliCategory>() {
            @Override
            public void call(MeliCategory meliCategory) {

                CountDownLatch latch;
                GenericResponser<FilterResponse> responseStatus = new GenericResponser<>(FilterResponse.class);
                if(meliCategory.getChildrenCategories().length>0) {
                    responseStatus.setExpectedOperations(meliCategory.getChildrenCategories().length+1);
                    latch = new CountDownLatch(meliCategory.getChildrenCategories().length);
                }else{
                    responseStatus.setExpectedOperations(1);
                    latch = new CountDownLatch(1);
                }

                for(MeliCategoryNode node : meliCategory.getChildrenCategories()){

                    MLCategoryService.getCategories(new Action1<MeliCategory>() {
                        @Override
                        public void call(MeliCategory meliCategory) {
                            MLCategoryService.getAllCategories(new Action1<RestResults<MeliCategory>>() {
                                @Override
                                public void call(RestResults<MeliCategory> meliCategoryRestResults) {

                                    listLatch.countDown();

                                    if(meliCategoryRestResults.isSuccessful()){

                                        params.put("category", meliCategory.getId());

                                        MLFilterService.getFiltersSafe(new Action1<MeliFilter[]>() {
                                            @Override
                                            public void call(MeliFilter[] meliFilters) {
                                                Conversions.createAvailableFilters(meliCategoryRestResults.getSubscriberEntity().getId(), meliCategoryRestResults.getSubscriberEntity().getName(), meliFilters, responseStatus, whiteList);
                                                latch.countDown();
                                            }
                                        }, new afterTaskFailure() {
                                            @Override
                                            public void onTaskFailed(Object o, Exception e) {
                                                latch.countDown();
                                            }
                                        }, meliCategoryRestResults.getSubscriberEntity().getId().substring(0, 3), meliCategoryRestResults.getSubscriberEntity().getId(), params);
                                    }
                                }
                            }, meliCategory.getId());
                        }
                    }, node.getId());

                }
                try {
                    latch.await();
                    meliCategory.setOperations(responseStatus.getResponse().getAvailableFilters().length);
                    responseStatus.getResponse().listsToArray();
                    asyncResponse.resume(Response.status(Response.Status.OK).entity(responseStatus.getResponse()).build());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    asyncResponse.resume(Response.status(Response.Status.NOT_FOUND).build());
                }
            }
        }, category, params);
    }

    @GET
    @Path("/compleate")
    @Produces(MediaType.APPLICATION_JSON)
    public void getCompleated(@Suspended final AsyncResponse asyncResponse, @Context UriInfo info) {

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
                GenericResponser<FilterResponse> responseStatus = new GenericResponser<>(FilterResponse.class);
                responseStatus.setExpectedOperations(1);

                if(finalLister !=null) finalLister.countdown();
                latch = new CountDownLatch(1);

                MLFilterService.getFilters(new Action1<MeliFilter[]>() {
                    @Override
                    public void call(MeliFilter[] meliFilters) {
                        Conversions.createAvailableFilters(meliCategory.getId(), meliCategory.getName(), meliFilters, responseStatus, finalWhiteList);
                        latch.countDown();
                    }
                }, meliCategory.getId().substring(0,3),meliCategory.getId(), params);

                try {
                    latch.await();
                    meliCategory.setOperations(responseStatus.getResponse().getAvailableFilters().length);
                    responseStatus.getResponse().listsToArray();
                    asyncResponse.resume(Response.status(Response.Status.OK).entity(responseStatus.getResponse()).build());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    asyncResponse.resume(Response.status(Response.Status.NOT_FOUND).build());
                }
            }
        }, category, params);
    }

    @GET
    @Path("/update")
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
