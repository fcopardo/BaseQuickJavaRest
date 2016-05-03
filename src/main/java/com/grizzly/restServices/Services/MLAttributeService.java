package com.grizzly.restServices.Services;

import com.grizzly.rest.GenericRestCall;
import com.grizzly.rest.Model.RestResults;
import com.grizzly.restServices.Models.MeliCategory;
import com.grizzly.restServices.Models.MeliCategoryNode;
import org.springframework.http.HttpMethod;
import rx.Observable;
import rx.functions.Action1;


/**
 * Created by fpardo on 3/1/16.
 */
public class MLAttributeService {

    public static void getAllCategories(Action1<RestResults<MeliCategory>> action, String category){
        getChildrenCall(action, category).execute(true);
    }

    public static GenericRestCall<Void, MeliCategory, String> getChildrenCall(Action1<RestResults<MeliCategory>> action, String category){

        return new GenericRestCall<>(Void.class, MeliCategory.class, String.class)
                .setUrl("https://api.mercadolibre.com/categories/"+category+"/attributes")
                .isCacheEnabled(true)
                .setCacheTime(10800000L)
                .setMethodToCall(HttpMethod.GET)
                .addSuccessSubscriber(action)
                .setAutomaticCacheRefresh(true);
    }
}
