package com.example;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

// https://api.tvmaze.com/shows/120
// baseurl : https://api.tvmaze.com
// path : /shows/{id}

@Path("/")
@RegisterRestClient(baseUri = "https://api.tvmaze.com")
// proxy will bring the data from this public endpoint
public interface TvSeriesIdProxy {

    //https://api.tvmaze.com/shows/120
    @GET
    @Path("showss/{id}")
    TvSeries getTvSeriesById(@PathParam("id") int id);
}
