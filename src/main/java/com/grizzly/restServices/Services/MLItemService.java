package com.grizzly.restServices.Services;

import com.grizzly.rest.GenericRestCall;
import com.grizzly.rest.Model.RestResults;
import com.grizzly.restServices.Models.MLItem;
import org.springframework.http.HttpMethod;
import rx.Observable;
import rx.functions.Action1;


/**
 * Created by fpardo on 3/1/16.
 */
public class MLItemService {

    public static void getItem(Action1<MLItem> action, String itemId, boolean isAsync){
        Action1<RestResults<MLItem>> action1 = new Action1<RestResults<MLItem>>() {
            @Override
            public void call(RestResults<MLItem> restResults) {
                if(restResults.isSuccessful()){
                    Observable.just(restResults.getSubscriberEntity()).subscribe(action);
                }
            }
        };
        callItem(action1, itemId, isAsync);
    }

    public static void callItem(Action1<RestResults<MLItem>> action, String itemId, boolean isAsync){
        getItemCall(action, itemId).execute(isAsync);
    }

    public static GenericRestCall<Void, MLItem, String> getItemCall(Action1<RestResults<MLItem>> action, String itemId){

        return new GenericRestCall<>(Void.class, MLItem.class, String.class)
                .setUrl("https://api.mercadolibre.com/items/"+itemId)
                .isCacheEnabled(true)
                .setCacheTime(10800000L)
                .setMethodToCall(HttpMethod.GET)
                .addSuccessSubscriber(action);
    }
}
