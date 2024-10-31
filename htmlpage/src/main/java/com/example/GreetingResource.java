package com.example;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/page")
public class GreetingResource {

    // Inject the color property from application.properties
    @ConfigProperty(name = "page.color", defaultValue = "blue")
    String pageColor;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String hello() {
        // Returning an HTML string with inline CSS for blue-colored text
        return "<html>" +
                "<body>" +
                "<h1>Configure colour in application.properties for page.color = " + pageColor + "</h1>" +
                "<h1 style='color:" + pageColor + ";'>This is a configurable color HTML page!</h1>" +
                "</body>" +
                "</html>";
    }
}
