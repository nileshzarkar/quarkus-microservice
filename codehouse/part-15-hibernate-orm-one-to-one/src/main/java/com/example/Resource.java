package com.example;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/")
public class Resource {

    @Inject
    AdharRepository adharRepository;

    @Inject
    CitizenRepository citizenRepository;

    @Transactional
    @Path("/save")
    @GET
    public Response saveCitizen(){


        Citizen citizen = new Citizen();
        citizen.setName("John");
        citizen.setGender("male");

        Adhar adhar = new Adhar();
        adhar.setAadharNumber(123456789L);
        adhar.setCompany("Tesla");
        adhar.setCitizen(citizen);

        citizen.setAdhar(adhar);
        
        citizenRepository.persist(citizen);
        //adharRepository.persist(adhar);

        return Response.ok(adhar).build();
    }

    @Path("/get")
    @GET
    public Response getCitizen(){
        
        Citizen citizen = citizenRepository.findById(1l);
        return Response.ok(citizen).build();
    }

  

}
