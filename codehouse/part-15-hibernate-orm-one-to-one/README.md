# part-15-hibernate-orm-one-to-one

Update application.properties
# Datasource configuration
quarkus.datasource.db-kind=postgresql
quarkus.datasource.username=myuser
quarkus.datasource.password=mypassword
quarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5432/adharcitizen
quarkus.hibernate-orm.log.sql=true
quarkus.hibernate-orm.database.generation=drop-and-create

Create entity classes
@Entity
public class Adhar {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    Long aadharNumber;
    String company;
}

@Entity
public class Citizen {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String name;
    String gender;
}

Both these classes represets 2 seperate database tables.

To create a one-to-one mapping between 2 tables we will have to create an extra column in one of the tables.
In one-to-one mapping you keep the foriegn key in only one table and not bot the tables.

Approach - 1 : So if I create an extra column in citizen table, then it will point to the id column of the adhar table which is the PK of the adhar table.
Approach - 2 : So if I create an extra column in adhar table, then it will point to the id column of the citizen table which is the PK of the citizen table.

With Approach 2

Step - 1
@Entity
public class Adhar {
    ...
    @OneToOne // 1 person can have 1 adhar number only
    // This means a new column would be created inside the adhar table
    Citizen citizen;
}

Step - 2
start the server and see the column (citizen_id) created in adhar table

Step - 3
Create Repository classes

@ApplicationScoped
public class CitizenRepository implements PanacheRepository<Citizen> {
}

@ApplicationScoped
public class AdharRepository implements PanacheRepository<Adhar> {
}

Step - 4
Create a Resource class and inject the repository classes

@Path("/")
public class Resource {
    @Inject
    AdharRepository adharRepository;
    @Inject
    CitizenRepository citizenRepository;
}

Step - 5

INFINITE RECURSION
You are asking citizen give me information of adhar.
So it went to adhar. Adhar said, Okay. Here is the information of adhar, but it also has the information of citizen.
So it went to citizen. Citizen said, Okay. Here is the information of citizen, but it also has the information of adhar.
And the loop continues infinitely...

So to fix this in the main class from which we are fetchng the data in our casee Citizen.
    @JsonManagedReference
    @OneToOne(mappedBy = "citizen", fetch = FetchType.EAGER)
    Adhar adhar;

With this you will go to Adhar , but now you dont want to go back to Citizen. 

    @JsonBackReference
    @OneToOne // 1 person can have 1 adhar number only
    // This means a new column would be created inside the adhar table
    Citizen citizen;

  Make sure to add the setter and getter for adhar and citizen reference in respective entity classes

Step - 6
Create the resource class

@Path("/")
public class Resource {

    @Inject
    AdharRepository adharRepository;

    @Inject
    CitizenRepository citizenRepository;

    @Transactional
    @Path("/save")
    @GET
    public Response saveCitizen(){
        Citizen citizen = new Citizen();
        citizen.setName("John");
        citizen.setGender("male");

        Adhar adhar = new Adhar();
        adhar.setAadharNumber(123456789L);
        adhar.setCompany("Tesla");
        adhar.setCitizen(citizen);
        
        citizenRepository.persist(citizen);
        adharRepository.persist(adhar);

        return Response.ok(adhar).build();
    }

    @Path("/get")
    @GET
    public Response getCitizen(){
        
        Citizen citizen = citizenRepository.findById(1l);
        return Response.ok(citizen).build();
    }

}


Endpoints : 

GET http://localhost:8080/save
GET http://localhost:8080/get


Imortant point to note : 
  citizenRepository.persist(citizen);
  adharRepository.persist(adhar);

First we have to save the citizen and then save the adhar.

What we want is once I create the citizen I attach the citizen with adhar. 
And when I save adhar the citizen should also get saved.

public Response saveCitizen(){


        Citizen citizen = new Citizen();
        citizen.setName("John");
        citizen.setGender("male");

        Adhar adhar = new Adhar();
        adhar.setAadharNumber(123456789L);
        adhar.setCompany("Tesla");
        adhar.setCitizen(citizen);

        citizen.setAdhar(adhar);
        
        citizenRepository.persist(citizen);
        //adharRepository.persist(adhar);

        return Response.ok(adhar).build();
}

GET http://localhost:8080/save

We get error : 
Caused by: org.hibernate.TransientPropertyValueException: object references an unsaved transient instance - save the transient instance before flushing : com.example.Citizen.adhar -> com.example.Adhar

This is coming because we are only saving citizen we are not saving the adhar along with it.

So we need to tell in come way when you save the citizen check if there is adhar attached to citizen, if YES please save the adhar as well.
(if adhar was not attached null, we would have not got this error)

How to tell this to the hibernate ?
   @JsonManagedReference
   @OneToOne(mappedBy = "citizen", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
   Adhar adhar;

cascade = CascadeType.ALL
This is telling when we save citizen and if there any other object attached to it save that as well.