# part-17-hibernate-orm-many-to-many

Use case of many-to-many relationship

A citizen can have account in many banks
A bank can open accounts of many citizens

id  name   gender 
 1  Rahul   m
 2  Mohit   m
 3  Amar    m

 id name   branch
  1  SBI    pune 
  2  ICICI  pune
  3  HDFC   pune

Rahul has account in SBI, ICICI
Mohit has account in SBI, ICICI and HDFC
Amar has account in HDFC, ICICI

ICICI bank has account of Rahul, Mohit and Amar
SBI has account of Rahul and Mohit
HDFC has account of Mohit and Amar

So this is many-to-many relationship


RECAP:
In one-to-one  we had 2 tables and we created an extra column in one of the tables which represented/stored the PK of the other table. That extra column was called as foriegn key.
In one-to-many we had 2 tables and we created an extra column on the many side table and that extra column represented/stored the PK of the other table.


In many-to-many we have 2 tables citizen and bank, we have to create a brand new table "citizen_bank",
In this new table we will have 2 columns 
1st column will be the PK of citizen table
2nd column will be the PK of the bank table
If we take above example:
    citizen_bank
PK_CITIZEN  PK_BANK
    1         1
    1         2
    2         1
    2         2
    2         3 
    3         2
    3         1

Step-1
Create the entity class

@Entity
public class Citizen {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String name;
    String gender;
    //setter and getter
}    

@Entity
public class Bank {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String name;
    String branch;
    //setter and getter
}

Step-2
Create Panache repository classes for bank and citizen

@ApplicationScoped
public class BankRepository implements PanacheRepository<Bank> {

}

@ApplicationScoped
public class CitizenRepository implements PanacheRepository<Citizen> {

}

Step-3

    @ManyToMany
    List<Citizen> citizens = new ArrayList<>();

    @ManyToMany
    List<Bank> banks = new ArrayList<>();

If you run the project this stage
you will get 4 tables
bank  - with fields as columns
citizen - with fields as columns
bank_citizen - bank_id and citizen_id
citizen_bank - citizen_id and bank_id

Why this happened because 
We have mentioned in out entity class of Clitizen 
    @ManyToMany
    List<Bank> bank = new ArrayList<>();
We have mentioned in out entity class of Bank 
    @ManyToMany
    List<Citizen> citizen = new ArrayList<>();

But here is a problem 
Bank does not know that Citizen is already creating a table bank_citizen
Citizen does not know that Bank is already creating a table citizen_bank

So we have to tell hibernate only one of you create the table no need to two tables
So lets tell Bank you do not create the table let Citizen create it 
    @ManyToMany(mappedBy = "bank")
    List<Citizen> citizen = new ArrayList<>();

Drop the tables manually and  start the server again

you will get 3 tables
bank  - with fields as columns
citizen - with fields as columns
citizen_bank - citizen_id and bank_id

We have the table name as citizen_bank because Citizen is creating the table.

Step-4
Make sure we have th setter and getter in all entity classes

Step-5

This endpoint will loop through all banks and save it in the database.

@Path("/")
public class Resource {

@Inject
    BankRepository bankRepository;

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
}

Step-6



@Path("/")
public class Resource {

    @Inject
    CitizenRepository citizenRepository;

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






