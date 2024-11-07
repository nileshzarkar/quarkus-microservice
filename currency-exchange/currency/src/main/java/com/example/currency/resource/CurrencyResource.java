package com.example.currency.resource;

import com.example.currency.model.ExchangeRate;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/currency")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CurrencyResource {

    @GET
    @Path("/exchange-rate/{from}/{to}")
    public ExchangeRate getExchangeRate(@PathParam("from") String from, @PathParam("to") String to) {
        ExchangeRate rate = ExchangeRate.find("fromCurrency = ?1 and toCurrency = ?2", from, to).firstResult();
        if (rate != null) {
            return rate;
        }
        throw new WebApplicationException("Exchange rate not found", 404);
    }

    @POST
    @Path("/exchange-rate")
    @Transactional
    public ExchangeRate addExchangeRate(ExchangeRate exchangeRate) {
        exchangeRate.persist();
        return exchangeRate;
    }

    @GET
    @Path("/exchange-rates")
    public List<ExchangeRate> getAllExchangeRates() {
        return ExchangeRate.listAll();
    }
}
