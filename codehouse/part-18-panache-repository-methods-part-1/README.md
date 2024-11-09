# part-18-panache-repository-methods-part-1

io.quarkus.hibernate.orm.panache.PanacheRepository is an interface provided by Quarkus to simplify repository operations when working with Hibernate ORM and JPA entities. 
It is part of the Panache API, which offers a more concise and readable syntax for data access operations compared to standard JPA.

When using PanacheRepository, you can easily perform common database operations (such as find, persist, delete, and update) without writing boilerplate code.
Key Features of PanacheRepository

    Simplified Data Access: Provides built-in methods for CRUD operations, eliminating the need to write boilerplate code for basic database operations.
    Easy Querying: Supports query methods that can be called directly on the repository without needing to define them explicitly.
    Type Safety: PanacheRepository is a generic interface, meaning it enforces type safety by working directly with your entity class.



Step-1
Create entity class 
@Entity
public class SimCard {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    Long number;
    String provider;
    boolean isActive;

    //setter and getter
}

Step-2
@ApplicationScoped
public class SimCardRepository implements PanacheRepository<SimCard> {

}

Step-3
@Path("/")
public class Resource {

    @Inject
    SimCardRepository simCardRepository;

    @GET
    @Path("save_simcard")
    @Transactional
    @Produces(MediaType.TEXT_PLAIN)
    public Response saveSimCard(){
        String[] provider = {"Jio","Airtel","VI","Aircel","BSNL"};

        for(long i=0L;i<20L;i++){
            SimCard simCard = new SimCard();
            simCard.setNumber(8876223210L +i);
            simCard.setProvider(provider[(int)i%provider.length]);
            simCard.setActive(i/3L==0);

            simCardRepository.persist(simCard);
            if(simCardRepository.isPersistent(simCard)){
                System.out.println(simCard + " saved Successfully");
            }else{
                System.out.println(simCard + " not saved. Please check");
            }
        }

        return Response.ok(new String("Sim Card Saved Successfully")).build();
    }

    @GET
    @Path("test_methods")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response testMethods(){
        List<SimCard> simCards = simCardRepository.listAll();
        return Response.ok(simCards).build();
    }


}