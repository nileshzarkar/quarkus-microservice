package com.example;

import java.time.temporal.ChronoUnit;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/")
@Produces(MediaType.TEXT_PLAIN)
public class IndiaWeatherResource {

    public static final Logger LOGGER = Logger.getLogger(IndiaWeatherResource.class);

    @RestClient
    NasaWeatherRestClient weatherRestClient;

    @GET
    @Path("weather/{country}")
    @Fallback(fallbackMethod = "getWeatherByCountryFallback")
    @CircuitBreaker(
            requestVolumeThreshold = 4,
            failureRatio = 0.5,
            delay = 60,
            delayUnit = ChronoUnit.SECONDS)
    public Response getWeatherByCountry(@PathParam("country") String country) {
        LOGGER.info("Calling NasaWeatherResource::getWeatherByCountry");
        return weatherRestClient.weatherByCountry(country);
    }

    public Response getWeatherByCountryFallback(String country) {
        return Response.ok("NASA Server is down, Please wait or try after sometime").build();
    }

}