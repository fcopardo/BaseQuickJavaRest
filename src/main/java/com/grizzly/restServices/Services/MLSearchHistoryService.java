package com.grizzly.restServices.Services;

import com.grizzly.rest.GenericRestCall;
import com.grizzly.rest.Model.RestResults;
import com.grizzly.restServices.Models.MeliHistoryNode;
import org.springframework.http.HttpMethod;
import rx.Observable;
import rx.functions.Action1;


/**
 * Created by fpardo on 3/1/16.
 */
public class MLSearchHistoryService {

    public static void getUserHistory(Action1<MeliHistoryNode[]> action, String site, String userId, boolean isAsync){
        Action1<RestResults<MeliHistoryNode[]>> action1 = new Action1<RestResults<MeliHistoryNode[]>>() {
            @Override
            public void call(RestResults<MeliHistoryNode[]> restResults) {
                if(restResults.isSuccessful()){
                    Observable.just(restResults.getSubscriberEntity()).subscribe(action);
                }
            }
        };
        callHistory(action1, site, userId, isAsync);
    }

    public static void callHistory(Action1<RestResults<MeliHistoryNode[]>> action, String site, String userId, boolean isAsync){
        getHistoryCall(action, site, userId).execute(isAsync);
    }

    public static GenericRestCall<Void, MeliHistoryNode[], String> getHistoryCall(Action1<RestResults<MeliHistoryNode[]>> action, String site, String userId){

        return new GenericRestCall<>(Void.class, MeliHistoryNode[].class, String.class)
                .isCacheEnabled(false)
                .setMethodToCall(HttpMethod.GET)
                .addSuccessSubscriber(action);
    }
}
