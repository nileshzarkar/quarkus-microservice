package com.example;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@RegisterRestClient(baseUri = "http://localhost:8081")
@Path("/api")
public interface NasaWeatherRestClient {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("weather_by_country/{country}")
    Response weatherByCountry(@PathParam("country") String country);
}