package com.grizzly.restServices.Services;

import com.grizzly.rest.GenericRestCall;
import com.grizzly.rest.Model.afterTaskCompletion;
import com.grizzly.restServices.Models.MeliAvailableFilters;
import com.grizzly.restServices.Models.MeliAvailableFiltersLight;
import com.grizzly.restServices.Models.MeliFilter;
import com.grizzly.restServices.Models.MeliFilterLight;
import org.springframework.http.HttpMethod;
import rx.Observable;
import rx.functions.Action1;


/**
 * Created by fpardo on 3/1/16.
 */
public class MLFilterService {

    public static void getFilters(Action1<MeliFilter[]> action, String site, String category){
        getFiltersCall(action, site, category).execute(true);
    }

    public static GenericRestCall<Void, MeliAvailableFilters, String> getFiltersCall(Action1<MeliFilter[]> action, String site, String category){

        GenericRestCall<Void, MeliAvailableFilters, String> restCall = new GenericRestCall<>(Void.class, MeliAvailableFilters.class, String.class)
        .setUrl("https://api.mercadolibre.com/sites/"+site+"/search?category="+category)
        .isCacheEnabled(true)
        .setMethodToCall(HttpMethod.GET)
                .setTaskCompletion(new afterTaskCompletion<MeliAvailableFilters>() {
                    @Override
                    public void onTaskCompleted(MeliAvailableFilters o) {
                        Observable.just(o.getAvailableFilters()).subscribe(action);
                    }
                });
        restCall.setAutomaticCacheRefresh(true);
        return restCall;
    }

    public static void getFiltersLight(Action1<MeliFilterLight[]> action, String site, String category){
        getFiltersCallLight(action, site, category).execute(true);
    }

    public static GenericRestCall<Void, MeliAvailableFiltersLight, String> getFiltersCallLight(Action1<MeliFilterLight[]> action, String site, String category){

        GenericRestCall<Void, MeliAvailableFiltersLight, String> restCall = new GenericRestCall<>(Void.class, MeliAvailableFiltersLight.class, String.class)
                .setUrl("https://api.mercadolibre.com/sites/"+site+"/search?category="+category)
                .isCacheEnabled(true)
                .setMethodToCall(HttpMethod.GET)
                .setTaskCompletion(new afterTaskCompletion<MeliAvailableFiltersLight>() {
                    @Override
                    public void onTaskCompleted(MeliAvailableFiltersLight o) {
                        Observable.just(o.getAvailableFilters()).subscribe(action);
                    }
                });
        restCall.setAutomaticCacheRefresh(true);
        return restCall;
    }

}
