package com.example;

import java.util.ArrayList;
import java.util.List;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/mobile")   //http://localhost:8080/mobile
public class MobileResource {

    List<String> mobiles = new ArrayList<>();

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response getMobilesList() {
        return Response.ok(mobiles).build();
    }   

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response addNewMobile(String mobile) {
        mobiles.add(mobile);
        return Response.ok(mobiles).build();
    }   

    @PUT
    @Path("/{oldmobilename}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateMobile(@PathParam("oldmobilename") String oldmobile, @QueryParam("newmobilename") String newMobile) {
        List<String> temp = new ArrayList<>();
        temp.addAll(mobiles);
        System.out.println("Old Mobile: " + oldmobile);
        System.out.println("Mobiles list: " + temp);
        // Check if the old mobile is in the list
        if (temp.contains(oldmobile)) {
            temp.remove(oldmobile);
            System.out.println("Mobile removed successfully.");
        } else {
            System.out.println("Mobile not found in the list.");
        }
        temp.add(newMobile);
        mobiles = temp;
        return Response.ok(mobiles).build();
    }   

    @DELETE
    @Path("/{mobiletoremove}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteMobile(@PathParam("mobiletoremove")  String mobileToRemove) {
        mobiles.remove(mobileToRemove);
        return Response.ok(mobiles).build();

    }   


}
