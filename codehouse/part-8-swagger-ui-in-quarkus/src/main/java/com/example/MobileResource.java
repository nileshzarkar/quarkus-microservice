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

    List<Mobile> mobiles = new ArrayList<>();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMobilesList() {
        return Response.ok(mobiles).build();
    }   

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addNewMobile(Mobile mobile) {
        mobiles.add(mobile);
        return Response.ok(mobiles).build();
    }   

    
    @PUT
    @Path("/{oldmobilename}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateMobile(@PathParam("oldmobilename") String oldmobile, @QueryParam("newmobilename") String newMobile) {
        Mobile mobileToUpdate = null;
        for (Mobile mobile : mobiles) {
            if (mobile.getName().equals(oldmobile)) {
                mobileToUpdate = mobile;
                break;
            }
        }

        if (mobileToUpdate != null) {
            mobileToUpdate.setName(newMobile);
            System.out.println("Mobile updated successfully.");
        } else {
            System.out.println("Mobile not found in the list.");
        }
        return Response.ok(mobiles).build();
    }   

    @DELETE
    @Path("/{mobiletoremove}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteMobile(@PathParam("mobiletoremove")  String mobileToRemove) {
        mobiles.removeIf(mobile -> mobile.getName().equals(mobileToRemove));
        return Response.ok(mobiles).build();

    }   

    
}
