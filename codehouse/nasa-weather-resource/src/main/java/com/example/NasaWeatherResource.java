package com.example;

import java.util.Random;

import org.jboss.logging.Logger;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/api")
public class NasaWeatherResource {

    public static final Logger LOGGER = Logger.getLogger(NasaWeatherResource.class);

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/weather_by_country/{country}")
    public Response weatherByCountry(@PathParam("country") String country) {
        LOGGER.info("Calling NasaWeatherResource::weatherByCountry for country : " + country);
        return Response.ok("Weather of " + country + " : " + new Random().nextInt(55)).build();
    }
}
