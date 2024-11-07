package com.example.exchange.resource;

import com.example.exchange.client.CurrencyServiceClient;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Path("/exchange")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ExchangeResource {

    @Inject
    @RestClient
    CurrencyServiceClient currencyServiceClient;

    @GET
    @Path("/{from}/{to}/{amount}")
    public String convertCurrency(@PathParam("from") String from,
                                  @PathParam("to") String to,
                                  @PathParam("amount") double amount) {

        // Fetch the exchange rate from the Currency Service
        double rate = currencyServiceClient.getExchangeRate(from, to).rate;

        // Perform the currency conversion
        double convertedAmount = rate * amount;

        return String.format("%.2f %s is %.2f %s", amount, from, convertedAmount, to);
    }
}
