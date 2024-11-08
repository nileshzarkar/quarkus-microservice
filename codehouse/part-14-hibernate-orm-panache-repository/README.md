# part-14-hibernate-orm-panache-repository

In previous session we extended entity class by io.quarkus.hibernate.orm.panache.PanacheEntity;

In this session we will make a class LaptopRepository which implements PanacheRepository.

Remove the PanacheEntity from Laptop class.
@Entity
public class Laptop {
}
The moment we do this we will get the error about primary key which we were inheriting from PanacheEntity
We fix this by 

@Entity
public class Laptop extends PanacheEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id; 
    String name;
    ...
}

Create the Repositoryclass
@ApplicationScoped
public class LaptopRepository implements PanacheRepository {

}


UPdate the resource class as below using the LaptopRepository object

@Path("/laptop")
public class LaptopResource {

    @Inject
    LaptopRepository laptopRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllLaptops() {
        List<Laptop> laptops = laptopRepository.listAll();
        return Response.ok(laptops).build();
    }

    @Transactional
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addLaptop(Laptop laptop) {
        laptopRepository.persist(laptop);
        if(laptopRepository.isPersistent(laptop)) {
            return Response.created(URI.create("/laptop/" + laptop.id)).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @Transactional
    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLaptop(@PathParam("id") long id) {
        Laptop laptop = laptopRepository.findById(id);
        return Response.ok(laptop).build();
    }

    @Transactional
    @Path("/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateLaptop(@PathParam("id") long id, Laptop laptop) {
        Optional<Laptop> optionalLaptop = laptopRepository.findByIdOptional(id);
        if(optionalLaptop.isPresent()){
            Laptop existingLaptop = optionalLaptop.get();

            if(Objects.nonNull(laptop.getName())) {
                existingLaptop.setName(laptop.getName());
            }
            if(Objects.nonNull(laptop.getBrand())) {
                existingLaptop.setBrand(laptop.getBrand());
            } 
            if(Objects.nonNull(laptop.getRam())) {
                existingLaptop.setRam(laptop.getRam());
            } 
            if(Objects.nonNull(laptop.getExternalStorage())) {
                existingLaptop.setExternalStorage(laptop.getExternalStorage());
            }  
            
            laptopRepository.persist(existingLaptop);
            if(laptopRepository.isPersistent(existingLaptop)){
                return Response.created(URI.create("/laptop/" + id)).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @Transactional
    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteLaptop(@PathParam("id") long id) {
        boolean isDeleted = laptopRepository.deleteById(id);
        if(isDeleted) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

}

Add swagger-ui dependency to test the endpoints

        <dependency>
            <groupId>io.quarkus</groupId>
            <artifactId>quarkus-smallrye-openapi</artifactId>
        </dependency>

http://localhost:8080/q/swagger-ui
