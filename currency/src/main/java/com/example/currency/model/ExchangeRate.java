package com.example.currency.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class ExchangeRate extends PanacheEntity {
    public String fromCurrency;
    public String toCurrency;
    public double rate;
   
}
