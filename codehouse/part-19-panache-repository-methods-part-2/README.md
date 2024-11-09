# part-19-panache-repository-methods-part-2

Step-1
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

    @POST
    @Transactional
    @Path("/persist_SimCard")
    @Produces(MediaType.TEXT_PLAIN)
    public Response saveSimCard(@RequestBody SimCard simCard) {
        simCardRepository.persist(simCard);
        if (simCardRepository.isPersistent(simCard)) {
            return Response.ok(new String("Sim Card saved successfully")).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @GET
    @Path("/listAll_SimCard")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listAllSimCard() {
        List<SimCard> simCards = simCardRepository.listAll();
        return Response.ok(simCards).build();
    }

    @GET
    @Path("/findById_SimCard/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findByIdSimCard(@PathParam("id") Long simCardId) {
        SimCard simCard = simCardRepository.findById(simCardId);
        return Response.ok(simCard).build();
    }

    @GET
    @Path("/count_SimCard")
    @Produces(MediaType.TEXT_PLAIN)
    public Response countSimCard(@PathParam("id") Long simCardId) {
        long count = simCardRepository.count();
        return Response.ok(count).build();
    }

    @GET
    @Path("/provider_list_SimCard/{provider}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response providerListSimCard(@PathParam("provider") String simCardProvider) {
        List<SimCard> simCardList = simCardRepository.list("provider", simCardProvider);
        return Response.ok(simCardList).build();
    }

    @GET
    @Path("/active_list_SimCard")
    @Produces(MediaType.APPLICATION_JSON)
    public Response activeListSimCard() {
        List<SimCard> simCardList = simCardRepository.list("isActive", true);
        return Response.ok(simCardList).build();
    }
}

Step-5
test the endpoints using Swagger UI
http://localhost:8080/q/swagger-ui

POST /persist_SimCard
{
  "number": 9823150966,
  "provider": "jio",
  "active": true
}

GET /listAll_SimCard

GET /findById_SimCard/{id}

GET /count_SimCard

GET /provider_list_SimCard/{provider}

GET /active_list_SimCard


Multiple filters : 
find("isActive=?1 and provider=?2",true,jio)