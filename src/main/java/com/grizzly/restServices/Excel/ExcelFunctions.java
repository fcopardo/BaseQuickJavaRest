package com.grizzly.restServices.Excel;

import com.grizzly.restServices.Controllers.Models.MeliCategoryFilterInfo;
import com.grizzly.restServices.Models.MeliAvailableFiltersLight;
import com.grizzly.restServices.Models.MeliFilterLight;
import com.grizzly.restServices.Models.MeliFilterValueLight;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
/**
 * Created by fpardo on 3/2/16.
 */
public class ExcelFunctions {

    public static boolean exportCategory(MeliCategoryFilterInfo filterInfo) {

        try {
            File dir = new File(System.getProperty("user.dir") + File.separator + "Excel");
            dir.mkdir();

            File myFile = new File(
                    System.getProperty("user.dir")+
                            File.separator+"Excel"+
                            File.separator+ filterInfo.categoryName+".xls");
            if(myFile.exists()) myFile.delete();
            myFile.createNewFile();
            DataOutputStream out = new DataOutputStream(new FileOutputStream(
                    myFile));
            WritableWorkbook w = Workbook.createWorkbook(out);
            int ahj = 0;
            WritableSheet s = w.createSheet("Filtros", 0);

            int row = 0;
            int column = 0;

            for(MeliFilterLight filtersLight : filterInfo.availableFilters){
                s.addCell(new Label(column, row, filtersLight.getName()));
                s.addCell(new Label(column+1, row, filtersLight.getType()));
                s.addCell(new Label(column+2, row, "Values"));
                column = 2;
                row++;
                for(MeliFilterValueLight value : filtersLight.getValues()){
                    s.addCell(new Label(column, row, value.getName()));
                    row++;
                }
                column = 0;
            }

            w.write();
            w.close();
            out.close();

            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (WriteException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
