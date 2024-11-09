package com.example;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/")
public class Resource {

    
    @Inject
    CitizenRepository citizenRepository;

    @GET
    @Path("/save")
    @Transactional
    public Response saveData(){

        Citizen citizen = new Citizen();
        citizen.setName("Ramesh");
        citizen.setGender("M");

        SimCard simCard1 = new SimCard();
        simCard1.setActive(true);
        simCard1.setNumber(9987L);
        simCard1.setProvider("Jio");
        simCard1.setCitizen(citizen);

        SimCard simCard2 = new SimCard();
        simCard2.setActive(true);
        simCard2.setNumber(8778L);
        simCard2.setProvider("Airtel");
        simCard2.setCitizen(citizen);

        SimCard simCard3 = new SimCard();
        simCard3.setActive(true);
        simCard3.setNumber(6786L);
        simCard3.setProvider("Jio");
        simCard3.setCitizen(citizen);

        citizen.setSimCards(List.of(simCard1,simCard2,simCard3));

        citizenRepository.persist(citizen);

        if(citizenRepository.isPersistent(citizen)){
            return  Response.ok(new String("Citizen with Sim saved Successfully")).build();
        }
        return  Response.ok(new String("Something went wrong")).build();
    }

    @GET
    @Path("/get")
    public Response getData(){

        Citizen citizen = citizenRepository.findById(1l);

        return  Response.ok(citizen).build();
    }

}
