package com.example;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Citizen {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)             
    Long id;
    String name;
    String gender;

    @OneToMany(mappedBy = "citizen", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    List<SimCard> simCards;


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public List<SimCard> getSimCards() {
        return simCards;
    }
    public void setSimCards(List<SimCard> simCards) {
        this.simCards = simCards;
    }

    

    
}
