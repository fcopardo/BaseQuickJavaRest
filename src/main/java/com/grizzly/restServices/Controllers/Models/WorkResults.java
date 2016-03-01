package com.grizzly.restServices.Controllers.Models;

import com.grizzly.restServices.Models.MeliFilterLight;

/**
 * Created by fpardo on 3/1/16.
 */
public class WorkResults {

        public String category;
        public String categoryName;
        public int totalFilters;
        public MeliFilterLight[] availableFilters;
        public int completedTasks = 0;
        public boolean categoryExecuted = false;
        public boolean filterExecuted = false;

}
