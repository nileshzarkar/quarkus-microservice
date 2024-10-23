package com.example.exchange.client;

import com.example.exchange.model.ExchangeRate;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

@RegisterRestClient(baseUri = "http://currency-service:8080")  // Adjust this to the actual Currency Service URL
public interface CurrencyServiceClient {

    @GET
    @Path("/currency/exchange-rate/{from}/{to}")
    ExchangeRate getExchangeRate(@PathParam("from") String from, @PathParam("to") String to);
}
