package com.grizzly.restServices.Controllers;

import com.grizzly.restServices.Models.HtmlString;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.pdf.PdfWriter;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.*;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by FcoPardo on 1/28/16.
 */
@Path("pdf")
public class Pdf extends BaseService{
    @Override
    protected HashMap<String, String> getMyMethods() {
        return null;
    }


    @POST
    @Path("fromhtml")
    public void asyncPost(@Suspended final AsyncResponse asyncResponse, @Context final UriInfo context, HtmlString string) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                Response result = getPdfResponse(string);
                asyncResponse.resume(result);
            }

            private Response getPdfResponse(HtmlString param) {

                if(param!=null && !param.getHtml().isEmpty()){

                    try {

                        File dir = new File(System.getProperty("user.dir") + File.separator + "pdf");
                        dir.mkdir();

                        File myFile = new File(
                                System.getProperty("user.dir")+
                                        File.separator+"pdf"+
                                        File.separator+Calendar.getInstance().getTime().toString().replace(" ", "").replace(":", "")+".pdf");
                        OutputStream file = new FileOutputStream(myFile);
                        Document doc = new Document(PageSize.A4);
                        PdfWriter.getInstance(doc, file);
                        doc.open();
                        HTMLWorker hw = new HTMLWorker(doc);
                        hw.parse(new StringReader(param.getHtml()));
                        doc.close();

                        return Response.accepted().entity(myFile).build();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("NO FILE!").build();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("NO PDF!").build();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("NO STREAM!").build();
                    }

                }
                return Response.status(Response.Status.BAD_REQUEST).entity("No HTML to convert").build();
            }
        }).start();
    }
}
