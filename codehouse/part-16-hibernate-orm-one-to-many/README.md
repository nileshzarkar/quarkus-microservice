# part-16-hibernate-orm-one-to-many

So in one-to-one relationship we would make a extra column in one of the tables. This extra column would represent the primary key of that other table.

In one to many relationship we have to always make a new field - columns on the many side table.

A citizen can have many sim cards
So for example we have table Citizen [id, name, gender] and SimCard [id, number, provider]
The extra field or column will always be created on the Simcard table [id, number, provider, citizen_id]

Step-1
@Entity
public class Citizen {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)             
    Long id;
    String name;
    String gender;
   // setter and getter
}

@Entity
public class SimCard {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    Long number;
    String provider;
    boolean isActive;
   // setter and getter
}

Step-2
update application.properties
# Datasource configuration
quarkus.datasource.db-kind=postgresql
quarkus.datasource.username=myuser
quarkus.datasource.password=mypassword
quarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5432/adharcitizen
quarkus.hibernate-orm.log.sql=true
quarkus.hibernate-orm.database.generation=drop-and-create

Step-3
Create Citizen and Simcard repository
@ApplicationScoped
public class SimCardRepository implements PanacheRepository<SimCard> {

}

@ApplicationScoped
public class CitizenRepository implements PanacheRepository<Citizen> {

}

Step-4
Configuring the one-to-many relationship between Citizen and SimCard

@Entity
public class Citizen {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)             
    Long id;
    String name;
    String gender;

    @OneToMany
    SimCard simCard;
 ...
}

If we run the application we will have 2 tables Citizen and SimCard.
An in Simcard table we will have extra columns named "citizen_id" 

Caused by: org.hibernate.AnnotationException: Property 'com.example.Citizen.simCard' is not a collection and may not be a '@OneToMany', '@ManyToMany', or '@ElementCollection'
    @OneToMany
    List<SimCard> simCards;
  Also add setter and getter

  An in Simcard table we will have extra columns named "citizen_id"  - THIS DID NOT HAPPEN

What happened is this : 
Created table 1 : citizen 
Created table 2 : citizen_simcard
Created table 3 : simcard

What is inside this table citizen_simcard 
one column    = citizen_id 
second column = simcard_id
citizen_id  simcard_id
    1            1
    1            2
Here we have the one-to-many relationship records...

We do not want this structure

We want only 2 tables citizen and simcard (with a new column named 'citizen_id' in simcard table)

    @OneToMany(mappedBy = "citizen", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    List<SimCard> simCards;

    @ManyToOne
    Citizen citizen;

If we run the application again we will have 2 tables citizen and simcard (with a new column named 'citizen_id' in simcard table)


BIDIRECTIONAL RELATIONSHIPS
The issue you're facing with Jackson serialization is likely because of a bidirectional relationship between Citizen and SimCard, causing a circular reference when Jackson tries to serialize the data. This is a common issue when using @OneToMany and @ManyToOne relationships. Jackson tries to serialize both entities, leading to an infinite loop.

Explanation

    @JsonManagedReference: Used on the parent side of the relationship (Citizen), it indicates that Jackson should manage the serialization of this relationship.
    @JsonBackReference: Used on the child side of the relationship (SimCard), it prevents Jackson from serializing this property, thus avoiding a circular reference.

To solve this, you can use the @JsonManagedReference and @JsonBackReference annotations to handle the serialization properly by breaking the circular dependency.

Here’s how to modify your Citizen and SimCard entities:

    Add @JsonManagedReference in Citizen on the simCards field.
    Add @JsonBackReference in SimCard on the citizen field.

    @ManyToOne
    @JsonBackReference
    Citizen citizen;

    @OneToMany(mappedBy = "citizen", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    List<SimCard> simCards;

Alternative Solution with @JsonIgnoreProperties

Alternatively, you can also use @JsonIgnoreProperties on the citizen field in SimCard to ignore the back reference during serialization:
@ManyToOne
@JsonIgnoreProperties("simCards")
Citizen citizen;

This approach ignores the simCards field when serializing citizen in SimCard, breaking the cycle and avoiding serialization issues.

GET http://localhost:8080/save
GET http://localhost:8080/get

