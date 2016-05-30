package com.grizzly.restServices.Conversions;

import com.grizzly.restServices.Controllers.Models.Filters.AvailableFilters;
import com.grizzly.restServices.Controllers.Models.Filters.Filter;
import com.grizzly.restServices.Controllers.Models.Filters.FilterResponse;
import com.grizzly.restServices.Controllers.Models.Filters.Values;
import com.grizzly.restServices.Controllers.Models.Responses.GenericResponser;
import com.grizzly.restServices.Models.MeliCategory;
import com.grizzly.restServices.Models.MeliFilter;
import com.grizzly.restServices.Models.MeliFilterValue;

import javax.ws.rs.core.MultivaluedMap;
import java.util.*;

/**
 * Created by fpardo on 4/5/16.
 */
public class Conversions {

    public static void createAvailableFilters(String id, String name, MeliFilter[] meliFilters, GenericResponser<FilterResponse> responseStatus){
        AvailableFilters availableFilter = new AvailableFilters();
        availableFilter.setCategoryId(id);
        availableFilter.setCategoryName(name);

        ArrayList<Filter> filterList = new ArrayList<>();
        for(MeliFilter filter : meliFilters){

            Filter aFilter = new Filter();
            aFilter.setId(filter.getId());
            aFilter.setActive(true);
            aFilter.setName(filter.getName());
            aFilter.setType(filter.getType());
            ArrayList<Values> list = new ArrayList<>();
            for(MeliFilterValue meliFilterValue: filter.getValues()){
                Values values = new Values();
                values.setValueId(meliFilterValue.getId());
                values.setValueName(meliFilterValue.getName());
                list.add(values);
            }
            aFilter.setValues(list.toArray(new Values[list.size()]));
            filterList.add(aFilter);
        }
        availableFilter.setFilters(filterList.toArray(new Filter[filterList.size()]));

        if(responseStatus.getResponse() == null){
            responseStatus.setResponse(new FilterResponse());
        }

        responseStatus.getResponse().getAvailableFiltersList().add(availableFilter);
    }

    public static void createAvailableFilters(String id, String name, MeliFilter[] meliFilters, GenericResponser<FilterResponse> responseStatus, List<String> whiteList){
        AvailableFilters availableFilter = new AvailableFilters();
        availableFilter.setCategoryId(id);
        availableFilter.setCategoryName(name);

        ArrayList<Filter> filterList = new ArrayList<>();


        if(whiteList != null){

            for(MeliFilter filter : meliFilters){

                if(whiteList.contains(filter.getId()) || whiteList.contains(filter.getName())){

                    Filter aFilter = new Filter();
                    aFilter.setId(filter.getId());
                    aFilter.setActive(true);
                    aFilter.setName(filter.getName());
                    aFilter.setType(filter.getType());
                    ArrayList<Values> list = new ArrayList<>();
                    for(MeliFilterValue meliFilterValue: filter.getValues()){
                        Values values = new Values();
                        values.setValueId(meliFilterValue.getId());
                        values.setValueName(meliFilterValue.getName());
                        list.add(values);
                    }
                    aFilter.setValues(list.toArray(new Values[list.size()]));
                    filterList.add(aFilter);

                }
            }

        }else{

            for(MeliFilter filter : meliFilters){

                Filter aFilter = new Filter();
                aFilter.setId(filter.getId());
                aFilter.setActive(true);
                aFilter.setName(filter.getName());
                aFilter.setType(filter.getType());
                ArrayList<Values> list = new ArrayList<>();
                for(MeliFilterValue meliFilterValue: filter.getValues()){
                    Values values = new Values();
                    values.setValueId(meliFilterValue.getId());
                    values.setValueName(meliFilterValue.getName());
                    list.add(values);
                }
                aFilter.setValues(list.toArray(new Values[list.size()]));
                filterList.add(aFilter);
            }
        }

        availableFilter.setFilters(filterList.toArray(new Filter[filterList.size()]));

        if(responseStatus.getResponse() == null){
            responseStatus.setResponse(new FilterResponse());
        }

        responseStatus.getResponse().getAvailableFiltersList().add(availableFilter);
    }

    public static void updateFilters(String id, String name, MeliFilter[] meliFilters, GenericResponser<Filter[]> responseStatus, List<String> whiteList){

        ArrayList<Filter> filterList = new ArrayList<>();


        if(whiteList != null){

            for(MeliFilter filter : meliFilters){

                if(whiteList.contains(filter.getId()) || whiteList.contains(filter.getName())){

                    Filter aFilter = new Filter();
                    aFilter.setId(filter.getId());
                    aFilter.setActive(true);
                    aFilter.setName(filter.getName());
                    aFilter.setType(filter.getType());
                    ArrayList<Values> list = new ArrayList<>();
                    for(MeliFilterValue meliFilterValue: filter.getValues()){
                        Values values = new Values();
                        values.setValueId(meliFilterValue.getId());
                        values.setValueName(meliFilterValue.getName());
                        list.add(values);
                    }
                    aFilter.setValues(list.toArray(new Values[list.size()]));
                    filterList.add(aFilter);

                }
            }

        }else{

            for(MeliFilter filter : meliFilters){

                Filter aFilter = new Filter();
                aFilter.setId(filter.getId());
                aFilter.setActive(true);
                aFilter.setName(filter.getName());
                aFilter.setType(filter.getType());
                ArrayList<Values> list = new ArrayList<>();
                for(MeliFilterValue meliFilterValue: filter.getValues()){
                    Values values = new Values();
                    values.setValueId(meliFilterValue.getId());
                    values.setValueName(meliFilterValue.getName());
                    list.add(values);
                }
                aFilter.setValues(list.toArray(new Values[list.size()]));
                filterList.add(aFilter);
            }
        }

        if(responseStatus.getResponse() == null){
            responseStatus.setResponse(filterList.toArray(new Filter[filterList.size()]));
        }
    }

    public static void createAvailableFilters(String id, String name, MeliFilter[] meliFilters, GenericResponser<FilterResponse> responseStatus, int addResponseTask){
        createAvailableFilters(id, name, meliFilters, responseStatus);
        responseStatus.addDoneOperations(1);
    }

    public static Map<String, Object> convertMultiToRegularMap(MultivaluedMap<String, String> m, String separator) {
        if(separator == null || separator.equalsIgnoreCase("")) separator = ",";
        Map<String, Object> map = new HashMap<>();
        if (m == null) {
            return map;
        }
        for (Map.Entry<String, List<String>> entry : m.entrySet()) {
            StringBuilder sb = new StringBuilder();
            for (String s : entry.getValue()) {
                if (sb.length() > 0) {
                    sb.append(separator);
                }
                sb.append(s);
            }
            map.put(entry.getKey(), sb.toString());
        }
        return map;
    }



}
