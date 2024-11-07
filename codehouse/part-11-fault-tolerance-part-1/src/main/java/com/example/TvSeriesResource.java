package com.example;

import java.util.List;

import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.vertx.core.json.JsonArray;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

@Path("/tvseries")
public class TvSeriesResource {
   // localhost:8080/tvseries/120
   
   @RestClient
   TvSeriesIdProxy proxy;

   @RestClient
   TvSeriesPersonProxy personProxy;

   @GET
   @Fallback(fallbackMethod = "getTvSeriesByIdFallBacl")
   @Path("/{id}")
   public Response getTvSeriesById(@PathParam("id") int id) {
     return Response.ok(proxy.getTvSeriesById(id)).build();
   }

   @GET
   @Path("/person/{personname}")
   // public List<TvSeries> getTvSeriesByPerson(@PathParam("personname") String personName) {
   // Cannot use TvSeries since the data returned does not match with the TvSeries class
   public JsonArray getTvSeriesByPerson(@PathParam("personname") String personName) {
     return personProxy.getTvSeriesByPerson(personName);
   }

   public Response getTvSeriesByIdFallBacl(int id) {
    return Response.ok("Site is under maintainence").build();
   }

}
