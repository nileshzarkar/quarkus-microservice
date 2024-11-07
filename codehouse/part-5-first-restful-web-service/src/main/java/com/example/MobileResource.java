package com.example;

import java.util.ArrayList;
import java.util.List;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/mobile")   //http://localhost:8080/mobile
public class MobileResource {

    List<String> mobiles = new ArrayList<>();

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public List<String> getMobilesList() {
        return mobiles;
    }   

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    public void addNewMobile(String mobile) {
        mobiles.add(mobile);
    }   

}
