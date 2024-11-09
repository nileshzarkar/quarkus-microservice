package com.example;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class Adhar {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    Long aadharNumber;
    String company;

    @JsonBackReference
    @OneToOne // 1 person can have 1 adhar number only
    // This means a new column would be created inside the adhar table
    Citizen citizen;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAadharNumber() {
        return aadharNumber;
    }

    public void setAadharNumber(Long aadharNumber) {
        this.aadharNumber = aadharNumber;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Citizen getCitizen() {
        return citizen;
    }

    public void setCitizen(Citizen citizen) {
        this.citizen = citizen;
    }

    
}
