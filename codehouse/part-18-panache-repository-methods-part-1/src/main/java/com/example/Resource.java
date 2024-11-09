package com.example;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/")
public class Resource {

    @Inject
    SimCardRepository simCardRepository;

    @GET
    @Path("save_simcard")
    @Transactional
    @Produces(MediaType.TEXT_PLAIN)
    public Response saveSimCard(){
        String[] provider = {"Jio","Airtel","VI","Aircel","BSNL"};

        for(long i=0L;i<20L;i++){
            SimCard simCard = new SimCard();
            simCard.setNumber(8876223210L +i);
            simCard.setProvider(provider[(int)i%provider.length]);
            simCard.setActive(i/3L==0);

            simCardRepository.persist(simCard);
            if(simCardRepository.isPersistent(simCard)){
                System.out.println(simCard + " saved Successfully");
            }else{
                System.out.println(simCard + " not saved. Please check");
            }
        }

        return Response.ok(new String("Sim Card Saved Successfully")).build();
    }

    @GET
    @Path("test_methods")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response testMethods(){
        List<SimCard> simCards = simCardRepository.listAll();
        return Response.ok(simCards).build();
    }


}
