package com.grizzly.restServices;

import com.grizzly.rest.EasyRest;
import com.grizzly.restServices.Controllers.Hello;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.servlet.ServletContainer;

/**
 * Created by FcoPardo on 1/3/16.
 */
public class App {


    public static void main(String[] args) throws Exception {

        EasyRest.setDebugMode(false);
        EasyRest.setQuickCachingAmount(1000);
        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.setContextPath("/");

        Server jettyServer = new Server(8082);
        jettyServer.setHandler(contextHandler);

        ServletHolder jerseyServlet = contextHandler.addServlet(ServletContainer.class, "/*");
        jerseyServlet.setInitParameter(ServerProperties.PROVIDER_PACKAGES, Hello.class.getPackage().getName());

        try{

            jettyServer.start();
            jettyServer.join();

        }
        catch(Exception e){
            jettyServer.destroy();
        }


    }


}
