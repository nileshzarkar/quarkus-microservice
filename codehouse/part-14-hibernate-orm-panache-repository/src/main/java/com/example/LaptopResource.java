package com.example;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/laptop")
public class LaptopResource {

    @Inject
    LaptopRepository laptopRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllLaptops() {
        List<Laptop> laptops = laptopRepository.listAll();
        return Response.ok(laptops).build();
    }

    @Transactional
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addLaptop(Laptop laptop) {
        laptopRepository.persist(laptop);
        if(laptopRepository.isPersistent(laptop)) {
            return Response.created(URI.create("/laptop/" + laptop.id)).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @Transactional
    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLaptop(@PathParam("id") long id) {
        Laptop laptop = laptopRepository.findById(id);
        return Response.ok(laptop).build();
    }

    @Transactional
    @Path("/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateLaptop(@PathParam("id") long id, Laptop laptop) {
        Optional<Laptop> optionalLaptop = laptopRepository.findByIdOptional(id);
        if(optionalLaptop.isPresent()){
            Laptop existingLaptop = optionalLaptop.get();

            if(Objects.nonNull(laptop.getName())) {
                existingLaptop.setName(laptop.getName());
            }
            if(Objects.nonNull(laptop.getBrand())) {
                existingLaptop.setBrand(laptop.getBrand());
            } 
            if(Objects.nonNull(laptop.getRam())) {
                existingLaptop.setRam(laptop.getRam());
            } 
            if(Objects.nonNull(laptop.getExternalStorage())) {
                existingLaptop.setExternalStorage(laptop.getExternalStorage());
            }  
            
            laptopRepository.persist(existingLaptop);
            if(laptopRepository.isPersistent(existingLaptop)){
                return Response.created(URI.create("/laptop/" + id)).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @Transactional
    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteLaptop(@PathParam("id") long id) {
        boolean isDeleted = laptopRepository.deleteById(id);
        if(isDeleted) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

}
