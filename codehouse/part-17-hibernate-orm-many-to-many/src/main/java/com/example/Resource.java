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
    BankRepository bankRepository;

    @Inject
    CitizenRepository citizenRepository;

    @GET
    @Path("save_bank")
    @Transactional
    @Produces(MediaType.TEXT_PLAIN)
    public Response saveBank(){
        String[] bankNames = {"SBI","PNB","AXIS","HDFC","ICICI","KOTAK"};

        for (String bankName : bankNames){
            Bank bank = new Bank();
            bank.setBranch("IT Park, EPIP Sitapura");
            bank.setName(bankName);
            bank.setIfscCode("IFCS22"+bankName);

            bankRepository.persist(bank);
        }
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("save_citizen")
    @Transactional
    @Produces(MediaType.TEXT_PLAIN)
    public Response saveCitizen(){
        String[] bankNames = {"SBI","PNB","AXIS","HDFC","ICICI","KOTAK"};

        Bank SBIBank = bankRepository.find("name",bankNames[0]).firstResult();
        Bank PNBBank = bankRepository.find("name",bankNames[1]).firstResult();
        Bank AXISBank = bankRepository.find("name",bankNames[2]).firstResult();
        Bank HDFCBank = bankRepository.find("name",bankNames[3]).firstResult();
        Bank ICICIBank = bankRepository.find("name",bankNames[4]).firstResult();
        Bank KOTAKBank = bankRepository.find("name",bankNames[5]).firstResult();

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + PNBBank);

        Citizen citizenRahul = new Citizen();
        citizenRahul.setName("Rahul");
        citizenRahul.setGender("M");
        citizenRahul.setBank(List.of(SBIBank,AXISBank,ICICIBank,PNBBank));

        Citizen citizenAaka = new Citizen();
        citizenAaka.setName("Aakanksha");
        citizenAaka.setGender("F");
        citizenAaka.setBank(List.of(SBIBank,AXISBank,KOTAKBank));

        Citizen citizenMic = new Citizen();
        citizenMic.setName("Mic");
        citizenMic.setGender("F");
        citizenMic.setBank(List.of(AXISBank));

        citizenRepository.persist(citizenRahul);
        citizenRepository.persist(citizenAaka);
        citizenRepository.persist(citizenMic);


        return Response.status(Response.Status.OK).build();
    }
}
