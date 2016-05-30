package com.grizzly.restServices.Services;

import com.grizzly.rest.GenericRestCall;
import com.grizzly.rest.Model.RestResults;
import com.grizzly.restServices.Models.MeliCategory;
import org.springframework.http.HttpMethod;
import rx.Observable;
import rx.functions.Action1;

import java.util.Map;


/**
 * Created by fpardo on 3/1/16.
 */
public class MLCategoryService {

    public static void getCategories(Action1<MeliCategory> action, String category){
        getCategories(action, category, null);
    }

    public static void getCategories(Action1<MeliCategory> action, String category, Map<String, Object> queryParams){
        Action1<RestResults<MeliCategory>> action1 = new Action1<RestResults<MeliCategory>>() {
            @Override
            public void call(RestResults<MeliCategory> meliCategoryRestResults) {
                if(meliCategoryRestResults.isSuccessful()) Observable.just(meliCategoryRestResults.getSubscriberEntity()).subscribe(action);
            }
        };
        getChildrenCall(action1, category, queryParams).execute(true);
    }

    public static void getAllCategories(Action1<RestResults<MeliCategory>> action, String category){
        getAllCategories(action, category, null);
    }

    public static void getAllCategories(Action1<RestResults<MeliCategory>> action, String category, Map<String, Object> queryParam){
        getChildrenCall(action, category, queryParam).execute(true);
    }

    public static GenericRestCall<Void, MeliCategory, String> getChildrenCall(Action1<RestResults<MeliCategory>> action, String category, Map<String, Object> queryParams){

        return new GenericRestCall<>(Void.class, MeliCategory.class, String.class)
                .setUrl("https://api.mercadolibre.com/categories/"+category)
                .addUrlParams(queryParams)
                .isCacheEnabled(true)
                .setCacheTime(10800000L)
                .setMethodToCall(HttpMethod.GET)
                .addSuccessSubscriber(action)
                .setAutomaticCacheRefresh(true);
    }
}
