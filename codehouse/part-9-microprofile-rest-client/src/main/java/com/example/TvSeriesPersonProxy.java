package com.example;

import java.util.List;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import io.vertx.core.json.JsonArray;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

// https://api.tvmaze.com/search/people?q=lauren
// baseurl : https://api.tvmaze.com
// path : search/people?q={person}

@Path("/search")
@RegisterRestClient(baseUri = "https://api.tvmaze.com")
// proxy will bring the data from this public endpoint
public interface TvSeriesPersonProxy {

    //https://api.tvmaze.com/shows/120
    @GET
    @Path("/people")
    // List<TvSeries> getTvSeriesByPerson(@QueryParam("q") String personName);
    // Cannot use TvSeries since the data returned does not match with the TvSeries class
    JsonArray getTvSeriesByPerson(@QueryParam("q") String personName);
}
