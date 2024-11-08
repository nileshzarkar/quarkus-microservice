# part-13-hibernate-orm-panache-entity

Create postgresql docker container locally

Step-1
docker pull postgres

Step-2
docker run --name my_postgres -e POSTGRES_USER=myuser -e POSTGRES_PASSWORD=mypassword -e POSTGRES_DB=laptops -p 5432:5432 -d postgres
Here’s a breakdown of the options:

    --name my_postgres: Names the container my_postgres (you can choose any name you prefer).
    -e POSTGRES_USER=myuser: Sets the PostgreSQL user to myuser.
    -e POSTGRES_PASSWORD=mypassword: Sets the PostgreSQL password to mypassword.
    -e POSTGRES_DB=mydatabase: Creates a database named mydatabase.
    -p 5432:5432: Maps port 5432 on your host to port 5432 on the container, allowing external connections.
    -d: Runs the container in detached mode, so it runs in the background.

Step-3
docker exec -it my_postgres psql -U myuser -d laptops

Connect with DBeaver
localhost 5432
laptops
myuser
mypassword

===================

@Entity
public class Laptop {

    String name;
    String brand;
    int ram;
    int externalStorage;
   //setter and getter
}

This is simple POJO class to enable this class and empower it to perform CRUD operations we extend it io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Laptop extends PanacheEntity {
    ...
}

Create the resource class for all CRUD operations using Panache methods

@Path("/laptop")
public class LaptopResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllLaptops() {
        List<Laptop> laptops = Laptop.listAll();
        return Response.ok(laptops).build();
    }

    @Transactional
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addLaptop(Laptop laptop) {
        Laptop.persist(laptop);
        if(laptop.isPersistent()) {
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
        Laptop laptop = Laptop.findById(id);
        return Response.ok(laptop).build();
    }

    @Transactional
    @Path("/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateLaptop(@PathParam("id") long id, Laptop laptop) {
        Optional<Laptop> optionalLaptop = Laptop.findByIdOptional(id);
        if(optionalLaptop.isPresent()){
            Laptop existingLaptop = optionalLaptop.get();

            if(Objects.nonNull(laptop.getName())) {
                existingLaptop.setName(laptop.getName());
            }
            if(Objects.nonNull(laptop.getBrand())) {
                existingLaptop.setBrand(laptop.getBrand());
            } 
            if(Objects.nonNull(laptop.getRam()))) {
                existingLaptop.setRam(laptop.getRam());
            } 
            if(Objects.nonNull(laptop.getExternalStorage())) {
                existingLaptop.setExternalStorage(laptop.getExternalStorage());
            }  
            
            existingLaptop.persist();
            if(existingLaptop.isPersistent()){
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
        boolean isDeleted = Laptop.deleteById(id);
        if(isDeleted) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

}


Add database properties
# Datasource configuration
quarkus.datasource.db-kind=postgresql
quarkus.datasource.username=myuser
quarkus.datasource.password=mypassword
quarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5432/laptops
quarkus.hibernate-orm.log.sql=true
quarkus.hibernate-orm.database.generation=drop-and-create

Add swagger-ui dependency to test the endpoints

        <dependency>
            <groupId>io.quarkus</groupId>
            <artifactId>quarkus-smallrye-openapi</artifactId>
        </dependency>

http://localhost:8080/q/swagger-ui




