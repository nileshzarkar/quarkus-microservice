# part-20-panache-repository-methods-part-3

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

    // =============================================================================

    @GET
    @Path("/findByIdOptional_SimCard/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findByIdOptionalSimCard(@PathParam("id") Long id) {
        Optional<SimCard> OptionalSimCard = simCardRepository.findByIdOptional(id);
        if (OptionalSimCard.isPresent()) {
            SimCard simCard = OptionalSimCard.get();
            return Response.ok(simCard).build();
        } else {
            return Response.noContent().build();
        }
    }

    @GET
    @Path("/conditional_count_SimCard/{provider}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response conditionalCountSimCard(@PathParam("provider") String simProvider) {
        //select count(*) from SimCard where provider =  @PathParam("provider")
        long count = simCardRepository.count("provider", simProvider);
        return Response.ok(count).build();
    }

    @GET
    @Path("/conditional_delete_SimCard/{provider}")
    @Transactional
    @Produces(MediaType.TEXT_PLAIN)
    public Response conditionalDeleteSimCard(@PathParam("provider") String simProvider) {
        // delete from SimCard where provider =  @PathParam("provider")
        long delete = simCardRepository.delete("provider", simProvider);
        return Response.ok(delete).build();
    }

    @GET
    @Path("/conditionalDeleteById_SimCard/{id}")
    @Transactional
    @Produces(MediaType.TEXT_PLAIN)
    public Response conditionalDeleteByIdSimCard(@PathParam("id") Long id) {
        // delete from SimCard where id =  @PathParam("id")
        boolean isDeleted = simCardRepository.deleteById(id);
        if (isDeleted)
            return Response.ok("Sim card deleted successfully").build();
        else
            return Response.ok("Something went wrong").build();
    }

    @GET
    @Path("/update/{id}/{provider}")
    @Produces(MediaType.TEXT_PLAIN)
    @Transactional
    public Response conditionalDeleteSimCard(@PathParam("id") Long id, @PathParam("provider") String provider) {
        // update SimCard set provider = @PathParam("provider") where id = @PathParam("id")
        int update = simCardRepository.update("provider =?1 where id =?2", provider, id);
        if (update == 1)
            return Response.ok("Sim card provider updated successfully").build();
        else
            return Response.ok("Something went wrong").build();
    }

    @GET
    @Path("/sortBy")
    @Produces(MediaType.APPLICATION_JSON)
    public Response sortBySimCard() {
        //select * from SimCard where isActive=false order by provider desc
        List<SimCard> simCardList = simCardRepository
                .list("isActive",
                        Sort.descending("provider"),
                        false);
            return Response.ok(simCardList).build();
    }

}

Step-4
test the endpoints using Swagger UI
http://localhost:8080/q/swagger-ui

POST /persist_SimCard
{
  "number": 9881719848,
  "provider": "idea",
  "active": true
}

{
  "number": 9823150966,
  "provider": "jio",
  "active": true
}

{
  "number": 7564738475,
  "provider": "bsnl",
  "active": false
}

GET /findByIdOptional_SimCard/{id}

GET /conditional_count_SimCard/{provider}

GET /conditionalDeleteById_SimCard/{id}

GET /conditional_delete_SimCard/{provider}

GET /update/{id}/{provider}



Multiple filters : 
find("isActive=?1 and provider=?2",true,jio)